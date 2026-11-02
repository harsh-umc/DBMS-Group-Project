--Niko's part

--Queries can have WHERE clauses added to them to narrow down searchs or to select specific things

--Looking up Farmer's Markets
SELECT MarketName, Address 
  FROM Farmers_Market;

--Looking up Vendors
SELECT VendorName, VendorWebsite 
  FROM Vendor;

--Looking up what Vendors are at what Farmer's Markets
SELECT MarketName, VendorName, StallNumber
  FROM Vendor_Market 
  RIGHT JOIN Farmers_Market 
  ON Vendor_Market.MarketID = Farmers_Market.MarketID 
  RIGHT JOIN Vendor 
  ON Vendor_Market.VendorID = Vendor.VendorID
  GROUP BY MarketName;

--Looking up products
SELECT ProductName 
  FROM Product;

--Looking up what Vendors sell
SELECT VendorName, ProductName
  FROM Vendor_Inventory
  RIGHT JOIN Products
  ON Vendor_Inventory.ProductID = Products.ProductID
  Right Join Vendor
  ON Vendor_Inventory.VendorID = Vendor.VendorID
  GROUP BY VendorName;

-- Looking at what you have ordered
SELECT ProductName, Quantity, VendorName, SUM(
  FROM Order_Contents
  RIGHT JOIN Products
  ON Order_Contents.ProductID = Products.ProductID
  Right JOIN Vendor
  ON Order_Contents.VendorID = Vendor.VendorID
  RIGHT JOIN Order_Details
  ON Order_Contents.OrderID = Order_Details.OrderID
  WHERE OrderID = --entered OrderID
  GROUP BY VendorName;

--Looking up Customer Infromation (for the Vendors)
SELECT *
  FROM Customer;

