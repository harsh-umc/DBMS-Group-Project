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

--Jd's part


--Looking up a specific Farmer's Market
SELECT MarketName, Address
  FROM Farmers_Market
  WHERE MarketID = 1;

--Looking up Vendors at a specific Farmer's Market
SELECT VendorName, StallNumber
  FROM Vendor_Market
  JOIN Vendor
  ON Vendor_Market.VendorID = Vendor.VendorID
  WHERE MarketID = 1;

--Counting how many Vendors are at each Farmer's Market
SELECT MarketName, COUNT(VendorID) AS TotalVendors
  FROM Farmers_Market
  LEFT JOIN Vendor_Market
  ON Farmers_Market.MarketID = Vendor_Market.MarketID
  GROUP BY Farmers_Market.MarketID, MarketName;

--Looking up Farmer's Markets in alphabetical order
SELECT MarketName, Address
  FROM Farmers_Market
  ORDER BY MarketName ASC;

--Looking up Vendors by name
SELECT VendorName, VendorWebsite
  FROM Vendor
  WHERE VendorName LIKE '%Farm%';

-- Harsh's Part: Additional application SELECT queries
-- These queries use the existing table and column names from schema.sql.

-- 1. Search products by name or category (example: jam).
-- Replace 'jam' with the user's search text.
SELECT ProductID, ProductName, ProductCategory
FROM Products
WHERE ProductName LIKE '%jam%' OR ProductCategory LIKE '%jam%'
ORDER BY ProductName;

-- 2. Display available products, vendors, stock quantities, and prices.
SELECT p.ProductName, p.ProductCategory, v.VendorName,
       vi.QuantityInStock, vi.ProductCost
FROM Vendor_Inventory AS vi
JOIN Products AS p ON p.ProductID = vi.ProductID
JOIN Vendor AS v ON v.VendorID = vi.VendorID
WHERE vi.QuantityInStock > 0
ORDER BY p.ProductName, vi.ProductCost;

-- 3. Compare prices for a selected product (example: Honey).
SELECT p.ProductName, v.VendorName, vi.ProductCost, vi.QuantityInStock
FROM Vendor_Inventory AS vi
JOIN Products AS p ON p.ProductID = vi.ProductID
JOIN Vendor AS v ON v.VendorID = vi.VendorID
WHERE p.ProductName = 'Honey' AND vi.QuantityInStock > 0
ORDER BY vi.ProductCost ASC;

-- 4. Find vendors and stall numbers at a selected market (example: market 1).
SELECT fm.MarketName, v.VendorName, v.VendorWebsite, vm.StallNumber
FROM Vendor_Market AS vm
JOIN Farmers_Market AS fm ON fm.MarketID = vm.MarketID
JOIN Vendor AS v ON v.VendorID = vm.VendorID
WHERE fm.MarketID = 1
ORDER BY vm.StallNumber;

-- 5. Show the total inventory units and number of product types per vendor.
SELECT v.VendorID, v.VendorName,
       COUNT(vi.ProductID) AS ProductTypes,
       COALESCE(SUM(vi.QuantityInStock), 0) AS TotalUnits
FROM Vendor AS v
LEFT JOIN Vendor_Inventory AS vi ON vi.VendorID = v.VendorID
GROUP BY v.VendorID, v.VendorName
ORDER BY TotalUnits DESC;

-- 6. Identify vendors with low-stock items (fewer than 10 units).
SELECT v.VendorName, p.ProductName, vi.QuantityInStock
FROM Vendor_Inventory AS vi
JOIN Vendor AS v ON v.VendorID = vi.VendorID
JOIN Products AS p ON p.ProductID = vi.ProductID
WHERE vi.QuantityInStock < 10
ORDER BY vi.QuantityInStock, v.VendorName;

-- 7. Summarize orders by customer, including item quantities.
SELECT c.CustomerID, c.CustomerName, od.OrderID, od.OrderCost,
       COALESCE(SUM(oc.QuantityOrdered), 0) AS TotalItems
FROM Customer AS c
JOIN Customer_Order AS co ON co.CustomerID = c.CustomerID
JOIN Order_Details AS od ON od.OrderID = co.OrderID
LEFT JOIN Order_Contents AS oc ON oc.OrderID = od.OrderID
GROUP BY c.CustomerID, c.CustomerName, od.OrderID, od.OrderCost
ORDER BY od.OrderID;
