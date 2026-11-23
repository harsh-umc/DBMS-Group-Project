package dbms_queries;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Existing team query operations, implemented with JDBC so they run against
 * the project's current SQL schema without generated jOOQ classes.
 */
public class Queries {
    public record Row(Object... cells) {}

    private static List<Row> read(PreparedStatement ps) throws SQLException {
        List<Row> result = new ArrayList<>();
        try (ResultSet rs = ps.executeQuery()) {
            int count = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                Object[] cells = new Object[count];
                for (int i = 0; i < count; i++) cells[i] = rs.getObject(i + 1);
                result.add(new Row(cells));
            }
        }
        return result;
    }

    // Show all markets alphabetically, optionally filtering by name or address.
    public static List<Row> showAllMarkets() throws SQLException {
        return showAllMarkets("");
    }
    public static List<Row> showAllMarkets(String search) throws SQLException {
        try (Connection c = DatabaseConnection.open();
             PreparedStatement ps = c.prepareStatement(
                 "SELECT MarketID, MarketName, Address FROM Farmers_Market " +
                 "WHERE MarketName LIKE ? OR Address LIKE ? ORDER BY MarketName")) {
            ps.setString(1, "%" + search + "%");
            ps.setString(2, "%" + search + "%");
            return read(ps);
        }
    }

    // Search vendors by name.
    public static List<Row> findVendorsByName(String name) throws SQLException {
        try (Connection c = DatabaseConnection.open();
             PreparedStatement ps = c.prepareStatement(
                 "SELECT VendorID, VendorName, VendorWebsite FROM Vendor " +
                 "WHERE VendorName LIKE ? ORDER BY VendorName")) {
            ps.setString(1, "%" + name + "%");
            return read(ps);
        }
    }

    // Find one market by ID.
    public static List<Row> findMarketByID(int id) throws SQLException {
        try (Connection c = DatabaseConnection.open();
             PreparedStatement ps = c.prepareStatement(
                 "SELECT MarketID, MarketName, Address FROM Farmers_Market WHERE MarketID=?")) {
            ps.setInt(1, id);
            return read(ps);
        }
    }

    // Search products by name or category.
    public static List<Row> searchProducts(String name) throws SQLException {
        try (Connection c = DatabaseConnection.open();
             PreparedStatement ps = c.prepareStatement(
                 "SELECT ProductID, ProductName, ProductCategory FROM Products " +
                 "WHERE ProductName LIKE ? OR ProductCategory LIKE ? ORDER BY ProductName")) {
            ps.setString(1, "%" + name + "%");
            ps.setString(2, "%" + name + "%");
            return read(ps);
        }
    }

    // Vendors at a selected market, including stall number.
    public static List<Row> showVendorsAtMarket(int id) throws SQLException {
        try (Connection c = DatabaseConnection.open();
             PreparedStatement ps = c.prepareStatement(
                 "SELECT v.VendorID, v.VendorName, v.VendorWebsite, vm.StallNumber " +
                 "FROM Vendor_Market vm JOIN Vendor v ON v.VendorID=vm.VendorID " +
                 "WHERE vm.MarketID=? ORDER BY vm.StallNumber")) {
            ps.setInt(1, id);
            return read(ps);
        }
    }

    // Count vendors at each market.
    public static List<Row> countVendors() throws SQLException {
        try (Connection c = DatabaseConnection.open();
             PreparedStatement ps = c.prepareStatement(
                 "SELECT fm.MarketID, fm.MarketName, COUNT(vm.VendorID) AS TotalVendors " +
                 "FROM Farmers_Market fm LEFT JOIN Vendor_Market vm ON fm.MarketID=vm.MarketID " +
                 "GROUP BY fm.MarketID, fm.MarketName ORDER BY fm.MarketName")) {
            return read(ps);
        }
    }

    // Available products and their vendor prices and inventory.
    public static List<Row> showAllAvailableProducts() throws SQLException {
        return showAllAvailableProducts("");
    }
    public static List<Row> showAllAvailableProducts(String search) throws SQLException {
        try (Connection c = DatabaseConnection.open();
             PreparedStatement ps = c.prepareStatement(
                 "SELECT p.ProductID, p.ProductName, p.ProductCategory, " +
                 "v.VendorID, v.VendorName, vi.ProductCost, vi.QuantityInStock " +
                 "FROM Vendor_Inventory vi JOIN Products p ON p.ProductID=vi.ProductID " +
                 "JOIN Vendor v ON v.VendorID=vi.VendorID " +
                 "WHERE (p.ProductName LIKE ? OR p.ProductCategory LIKE ?) " +
                 "AND vi.QuantityInStock>0 ORDER BY p.ProductName, vi.ProductCost")) {
            ps.setString(1, "%" + search + "%");
            ps.setString(2, "%" + search + "%");
            return read(ps);
        }
    }

    // Compare vendors' prices for a product.
    public static List<Row> comparePrice(String product) throws SQLException {
        try (Connection c = DatabaseConnection.open();
             PreparedStatement ps = c.prepareStatement(
                 "SELECT p.ProductName, v.VendorName, vi.ProductCost, vi.QuantityInStock " +
                 "FROM Vendor_Inventory vi JOIN Products p ON p.ProductID=vi.ProductID " +
                 "JOIN Vendor v ON v.VendorID=vi.VendorID " +
                 "WHERE p.ProductName=? AND vi.QuantityInStock>0 ORDER BY vi.ProductCost")) {
            ps.setString(1, product);
            return read(ps);
        }
    }

    // Original order method: one item per preorder, with a database transaction.
    public static int order(int productID, int vendorID, int customerID, String address)
            throws SQLException {
        return order(productID, vendorID, customerID, address, 1);
    }

    public static int order(int productID, int vendorID, int customerID, String address,
                            int quantity) throws SQLException {
        if (quantity < 1 || address == null || address.isBlank())
            throw new IllegalArgumentException("Enter a positive quantity and delivery address.");
        try (Connection c = DatabaseConnection.open()) {
            c.setAutoCommit(false);
            try {
                BigDecimal price;
                int stock;
                try (PreparedStatement ps = c.prepareStatement(
                    "SELECT ProductCost, QuantityInStock FROM Vendor_Inventory " +
                    "WHERE ProductID=? AND VendorID=? FOR UPDATE")) {
                    ps.setInt(1, productID);
                    ps.setInt(2, vendorID);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) throw new SQLException("Product/vendor combination not found.");
                        price = rs.getBigDecimal(1);
                        stock = rs.getInt(2);
                    }
                }
                if (stock < quantity) throw new SQLException("Only " + stock + " items available.");
                int orderID;
                try (PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO Order_Details(OrderCost,DeliveryAddress) VALUES(?,?)",
                    Statement.RETURN_GENERATED_KEYS)) {
                    ps.setBigDecimal(1, price.multiply(BigDecimal.valueOf(quantity)));
                    ps.setString(2, address);
                    ps.executeUpdate();
                    orderID = generatedId(ps);
                }
                try (PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO Order_Contents(OrderID,ProductID,VendorID,QuantityOrdered) " +
                    "VALUES(?,?,?,?)")) {
                    ps.setInt(1, orderID);
                    ps.setInt(2, productID);
                    ps.setInt(3, vendorID);
                    ps.setInt(4, quantity);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO Customer_Order(CustomerID,OrderID) VALUES(?,?)")) {
                    ps.setInt(1, customerID);
                    ps.setInt(2, orderID);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = c.prepareStatement(
                    "UPDATE Vendor_Inventory SET QuantityInStock=QuantityInStock-? " +
                    "WHERE ProductID=? AND VendorID=? AND QuantityInStock>=?")) {
                    ps.setInt(1, quantity);
                    ps.setInt(2, productID);
                    ps.setInt(3, vendorID);
                    ps.setInt(4, quantity);
                    if (ps.executeUpdate() != 1) throw new SQLException("Could not update inventory.");
                }
                c.commit();
                return orderID;
            } catch (SQLException | RuntimeException e) {
                c.rollback();
                throw e;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    public static int createCustomer(String name, String email) throws SQLException {
        if (name == null || name.isBlank() || email == null || email.isBlank())
            throw new IllegalArgumentException("Enter customer name and email.");
        try (Connection c = DatabaseConnection.open();
             PreparedStatement ps = c.prepareStatement(
                 "INSERT INTO Customer(CustomerName,CustomerEmail) VALUES(?,?)",
                 Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.executeUpdate();
            return generatedId(ps);
        }
    }

    private static int generatedId(PreparedStatement ps) throws SQLException {
        try (ResultSet rs = ps.getGeneratedKeys()) {
            if (!rs.next()) throw new SQLException("No generated ID returned.");
            return rs.getInt(1);
        }
    }
}
