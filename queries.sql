-- Existing team query examples, corrected for MySQL.
USE Market_Manager;

-- Niko's part
-- Looking up farmers markets
SELECT MarketName, Address FROM Farmers_Market;

-- Looking up vendors
SELECT VendorName, VendorWebsite FROM Vendor;

-- Vendors at each farmers market
SELECT fm.MarketName, v.VendorName, vm.StallNumber
FROM Vendor_Market vm
JOIN Farmers_Market fm ON vm.MarketID = fm.MarketID
JOIN Vendor v ON vm.VendorID = v.VendorID
ORDER BY fm.MarketName, vm.StallNumber;

-- Looking up products
SELECT ProductName FROM Products;

-- What vendors sell
SELECT v.VendorName, p.ProductName
FROM Vendor_Inventory vi
JOIN Products p ON vi.ProductID = p.ProductID
JOIN Vendor v ON vi.VendorID = v.VendorID
ORDER BY v.VendorName, p.ProductName;

-- Contents of a specific order (change the order ID for your test)
SELECT oc.OrderID, p.ProductName, oc.QuantityOrdered, v.VendorName,
       (oc.QuantityOrdered * vi.ProductCost) AS LineTotal
FROM Order_Contents oc
JOIN Products p ON oc.ProductID = p.ProductID
JOIN Vendor v ON oc.VendorID = v.VendorID
JOIN Vendor_Inventory vi ON vi.ProductID = oc.ProductID AND vi.VendorID = oc.VendorID
WHERE oc.OrderID = 1;

-- Customer information
SELECT * FROM Customer;

-- JD's part
-- A specific farmers market
SELECT MarketName, Address FROM Farmers_Market WHERE MarketID = 1;

-- Vendors at a specific farmers market
SELECT v.VendorName, vm.StallNumber
FROM Vendor_Market vm
JOIN Vendor v ON vm.VendorID = v.VendorID
WHERE vm.MarketID = 1;

-- Count vendors at each market
SELECT fm.MarketName, COUNT(vm.VendorID) AS TotalVendors
FROM Farmers_Market fm
LEFT JOIN Vendor_Market vm ON fm.MarketID = vm.MarketID
GROUP BY fm.MarketID, fm.MarketName;

-- Markets in alphabetical order
SELECT MarketName, Address FROM Farmers_Market ORDER BY MarketName ASC;

-- Find vendors by name
SELECT VendorName, VendorWebsite FROM Vendor WHERE VendorName LIKE '%Farm%';
