package dbms_queries;
import java.util.*;

public class Queries{

    //Show all markets alphabetically
    public static Map<String, String> showAllMarkets(){
        return create.select(MarketName, Address)
            .from(Farmers_Market)
            .orderby(MarketName.asc())
            .fetch();
    }

    //searches for vendors by their name
    public static Map<String, String> findVendorsByName(String name){
        return create.select(VendorName, VendorWebsite)
            .from(Vendor)
            .where(VendorName.like("%"+name+"%"))
            .fetch();
    }

    //searches for markets by their ID number
    public static Map<String, String> findMarketByID(int id){
        return create.select(MarketName, Address)
            .from(Farmers_Market)
            .where(MarketID.eq(id))
            .fetch();
    }

    //searches products by name and category
    public static Map<String, String> searchProducts(String name){
        return create.select(ProductID, ProductName, ProductCategory)
            .from(Products)
            .where(ProductName.like("%"+name+"%").or(ProductCategory.like("%"+name+"%"))
            .orderby(ProductName.asc())
            .fetch();
    }

    //shows what vendors are at a given market
    public static Map<String, String> showVendorsAtMarket(int id){
        return create.select(VendorName, StallNumber)
        .from(Vendor_Market)
        .join(Vendor)
            .on(Vendor.VendorID.eq(Vendor_Market.VendorID))
        .where(MarketID.eq(id))
        .fetch();
    }

    //counts the number of vendors at each market
    public static Map<String, String> countVendors(){
        return create.select(MarketName, count(VendorID).as("TotalVendors"))
            .from(Farmers_Market)
            .join(Vendor_Market)
                .on(Farmers_Market.MarketID.eq(Vendor_Market.MarketID))
            .groupby(Farmers_Market.MarketID, MarketName)
            .fetch();
    }

    //shows all available products
    public static Map<String, String> showAllAvailableProducts(){
        return create.select(p.ProductName, p.ProductCategory, v.VendorName, vi.QuantityInStock, vi.ProductCost)
            .from(Vendor_Inventory)
                .as("vi")
            .join(Products)
                .as("p")
                .on(p.ProductID.eq(vi.ProductID))
            .join(Vendor)
                .as("v")
                .on(v.VendorID.eq(vi.VendorID))
            .where(vi.QuantityInStock.gt(0))
            .orderby(p.ProductName, vi.ProductCost)
            .fetch();
    }

    //compares the price of a given product from different vendors
    public static Map<String, String> comparePrice(String product){
        return create.select(p.ProductName, v.VendorName, vi.ProductCost, vi.QuantityInStock)
        .from(Vendor_Inventory)
            .as("vi")
        .join(Products)
            .as("p")
            .on(p.ProductID.eq(vi.ProductID))
        .join(Vendor)
            .as("v")
            .on(v.VendorID.eq(vi.VendorID))
        .where(p.ProductName.eq(product).and(vi.QuantityInStock.gt(0)))
        .orderby(vi.ProductCost.asc)
        .fetch();
    }

    //places a preorder
    public static void order(int productID, int vendorID, int customerID, String address){
        create.transaction((Configuration trx) -> {
            //get the product cost
            Vendor_InventoryRecord productCost = trx.dsl()
                .select(ProductCost)
                .from(Vendor_Inventory)
                .where(ProductID.eq(productID).and(VendorID.eg(vendorID)))
                .fetch();

            //create a new entry in the Order_Details table
            Order_DetailsRecord orderDetails = trx.dsl()
                .insertInto(Order_Details, Order_Details.OrderCost, Order_Details.DeliveryAddress)
                .values(productCost, address)
                .fetch();

            //create a new entry in the Order_Contents table
            trx.dsl()
                .insertInto(Order_Contents, Order_Contents.OrderID, Order_Contents.ProductID, Order_Contents.VendorID, Order_Contents.QuantityOrdered)
                .values(OrderDetails.getID(), productID, vendorID, 1)
                .execute();

            //create a new entry in the Customer_Order table
            trx.dsl()
                .insertInto(Customer_Order, Customer_Order.CustomerID, Customer_Order.OrderID)
                .values(customerID, OrderDetails.getID())
                .execute();
        });
    }
}