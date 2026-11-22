package dbms_queries;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** UI-facing database operations. Separate from Nikolai's Queries.java. */
public final class MarketService {
    public record Row(Object... cells) {}
    public List<Row> markets(String search) throws SQLException {
        String sql = "SELECT MarketID, MarketName, Address FROM Farmers_Market WHERE MarketName LIKE ? OR Address LIKE ? ORDER BY MarketName";
        try (Connection c = DatabaseConnection.open(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, "%" + search + "%"); ps.setString(2, "%" + search + "%");
            return rows(ps);
        }
    }
    public List<Row> vendors(int marketId) throws SQLException {
        String sql = "SELECT v.VendorID, v.VendorName, v.VendorWebsite, vm.StallNumber FROM Vendor_Market vm JOIN Vendor v ON v.VendorID=vm.VendorID WHERE vm.MarketID=? ORDER BY vm.StallNumber";
        try (Connection c = DatabaseConnection.open(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, marketId); return rows(ps);
        }
    }
    public List<Row> products(String search) throws SQLException {
        String sql = "SELECT p.ProductID, p.ProductName, p.ProductCategory, v.VendorID, v.VendorName, vi.ProductCost, vi.QuantityInStock FROM Vendor_Inventory vi JOIN Products p ON p.ProductID=vi.ProductID JOIN Vendor v ON v.VendorID=vi.VendorID WHERE p.ProductName LIKE ? OR p.ProductCategory LIKE ? ORDER BY p.ProductName, vi.ProductCost";
        try (Connection c = DatabaseConnection.open(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, "%" + search + "%"); ps.setString(2, "%" + search + "%");
            return rows(ps);
        }
    }
    private List<Row> rows(PreparedStatement ps) throws SQLException {
        try (ResultSet rs = ps.executeQuery()) {
            List<Row> result = new ArrayList<>();
            int count = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                Object[] cells = new Object[count];
                for (int i = 0; i < count; i++) cells[i] = rs.getObject(i + 1);
                result.add(new Row(cells));
            }
            return result;
        }
    }
    /** One order per product/vendor; atomic stock decrement and order inserts. */
    public int placeOrder(int productId, int vendorId, String customerName, String email,
                          String address, int quantity) throws SQLException {
        if (quantity < 1 || customerName.isBlank() || email.isBlank() || address.isBlank())
            throw new IllegalArgumentException("Complete all fields and enter a positive quantity.");
        try (Connection c = DatabaseConnection.open()) {
            c.setAutoCommit(false);
            try {
                BigDecimal price;
                int stock;
                try (PreparedStatement ps = c.prepareStatement(
                    "SELECT ProductCost, QuantityInStock FROM Vendor_Inventory WHERE ProductID=? AND VendorID=? FOR UPDATE")) {
                    ps.setInt(1, productId); ps.setInt(2, vendorId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) throw new SQLException("This vendor does not sell the selected product.");
                        price = rs.getBigDecimal(1); stock = rs.getInt(2);
                    }
                }
                if (stock < quantity) throw new SQLException("Only " + stock + " items remain in stock.");
                int customerId;
                try (PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO Customer(CustomerName,CustomerEmail) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, customerName); ps.setString(2, email); ps.executeUpdate();
                    customerId = generatedId(ps);
                }
                int orderId;
                try (PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO Order_Details(OrderCost,DeliveryAddress) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS)) {
                    ps.setBigDecimal(1, price.multiply(BigDecimal.valueOf(quantity)));
                    ps.setString(2, address); ps.executeUpdate(); orderId = generatedId(ps);
                }
                try (PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO Customer_Order(CustomerID,OrderID) VALUES(?,?)")) {
                    ps.setInt(1, customerId); ps.setInt(2, orderId); ps.executeUpdate();
                }
                try (PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO Order_Contents(OrderID,ProductID,VendorID,QuantityOrdered) VALUES(?,?,?,?)")) {
                    ps.setInt(1, orderId); ps.setInt(2, productId);
                    ps.setInt(3, vendorId); ps.setInt(4, quantity); ps.executeUpdate();
                }
                try (PreparedStatement ps = c.prepareStatement(
                    "UPDATE Vendor_Inventory SET QuantityInStock=QuantityInStock-? WHERE ProductID=? AND VendorID=? AND QuantityInStock>=?")) {
                    ps.setInt(1, quantity); ps.setInt(2, productId); ps.setInt(3, vendorId); ps.setInt(4, quantity);
                    if (ps.executeUpdate() != 1) throw new SQLException("Could not update inventory.");
                }
                c.commit(); return orderId;
            } catch (SQLException | RuntimeException ex) {
                c.rollback(); throw ex;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }
    private int generatedId(PreparedStatement ps) throws SQLException {
        try (ResultSet rs = ps.getGeneratedKeys()) {
            if (!rs.next()) throw new SQLException("Database did not return an ID.");
            return rs.getInt(1);
        }
    }
}
