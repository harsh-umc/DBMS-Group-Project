-- Shared demo seed data for Market_Manager. Run schema.sql first.
-- INSERT IGNORE skips existing primary keys when this script is rerun.
-- It does not overwrite existing records; review data conflicts separately.
USE Market_Manager;

-- Niko's product list (fixed SQL quoting and missing product #9: Carrot placeholder).
INSERT IGNORE INTO Products (ProductID, ProductName, ProductCategory) VALUES
(0, 'Apple', 'Produce'),
(1, 'Orange', 'Produce'),
(2, 'Pear', 'Produce'),
(3, 'Watermelon', 'Produce'),
(4, 'Pumpkin', 'Produce'),
(5, 'Strawberry', 'Produce'),
(6, 'Corn', 'Produce'),
(7, 'Tomato', 'Produce'),
(8, 'Potato', 'Produce'),
(9, 'Carrot', 'Produce'),
(10, 'Celery', 'Produce'),
(11, 'Egg', 'Animal Product'),
(12, 'Cow Milk', 'Animal Product'),
(13, 'Goat Milk', 'Animal Product'),
(14, 'Honey', 'Animal Product'),
(15, 'Cheese', 'Animal Product'),
(16, 'Beef', 'Meat'),
(17, 'Chicken', 'Meat'),
(18, 'Pork', 'Meat'),
(19, 'Turkey', 'Meat'),
(20, 'Sausage', 'Meat'),
(21, 'Wool', 'Textiles'),
(22, 'Hemp', 'Textiles'),
(23, 'Cotton', 'Textiles'),
(24, 'Linen', 'Textiles'),
(25, 'Silk', 'Textiles');

-- JD's part

-- 10 Farmers Markets

INSERT IGNORE INTO Farmers_Market (MarketID, MarketName, Address)
VALUES (001, 'Downtown Farmers Market', '123 Main St');

INSERT IGNORE INTO Farmers_Market (MarketID, MarketName, Address)
VALUES (002, 'Riverside Farmers Market', '456 River Rd');

INSERT IGNORE INTO Farmers_Market (MarketID, MarketName, Address)
VALUES (003, 'Green Valley Market', '789 Valley Dr');

INSERT IGNORE INTO Farmers_Market (MarketID, MarketName, Address)
VALUES (004, 'Sunrise Farmers Market', '101 Sunrise Ave');

INSERT IGNORE INTO Farmers_Market (MarketID, MarketName, Address)
VALUES (005, 'Oakwood Farmers Market', '202 Oak St');

INSERT IGNORE INTO Farmers_Market (MarketID, MarketName, Address)
VALUES (006, 'Lakeside Farmers Market', '303 Lake Rd');

INSERT IGNORE INTO Farmers_Market (MarketID, MarketName, Address)
VALUES (007, 'Harvest Hill Market', '404 Hill St');

INSERT IGNORE INTO Farmers_Market (MarketID, MarketName, Address)
VALUES (008, 'Prairie Farmers Market', '505 Prairie Ave');

INSERT IGNORE INTO Farmers_Market (MarketID, MarketName, Address)
VALUES (009, 'Maple Grove Market', '606 Maple Dr');

INSERT IGNORE INTO Farmers_Market (MarketID, MarketName, Address)
VALUES (010, 'Fresh Fields Market', '707 Field Rd');


-- 20 Vendors

INSERT IGNORE INTO Vendor (VendorID, VendorName, VendorWebsite)
VALUES (001, 'Green Acres', 'greenacres.com');

INSERT IGNORE INTO Vendor (VendorID, VendorName, VendorWebsite)
VALUES (002, 'Sunny Farms', 'sunnyfarms.com');

INSERT IGNORE INTO Vendor (VendorID, VendorName, VendorWebsite)
VALUES (003, 'Oak Hill Farm', 'oakhillfarm.com');

INSERT IGNORE INTO Vendor (VendorID, VendorName, VendorWebsite)
VALUES (004, 'Fresh Harvest', 'freshharvest.com');

INSERT IGNORE INTO Vendor (VendorID, VendorName, VendorWebsite)
VALUES (005, 'Riverbend Farm', 'riverbendfarm.com');

INSERT IGNORE INTO Vendor (VendorID, VendorName, VendorWebsite)
VALUES (006, 'Blue Sky Farm', 'blueskyfarm.com');

INSERT IGNORE INTO Vendor (VendorID, VendorName, VendorWebsite)
VALUES (007, 'Golden Fields', 'goldenfields.com');

INSERT IGNORE INTO Vendor (VendorID, VendorName, VendorWebsite)
VALUES (008, 'Maple Creek', 'maplecreek.com');

INSERT IGNORE INTO Vendor (VendorID, VendorName, VendorWebsite)
VALUES (009, 'Happy Hen Farm', 'happyhenfarm.com');

INSERT IGNORE INTO Vendor (VendorID, VendorName, VendorWebsite)
VALUES (010, 'Prairie Roots', 'prairieroots.com');

INSERT IGNORE INTO Vendor (VendorID, VendorName, VendorWebsite)
VALUES (011, 'Red Barn Farm', 'redbarnfarm.com');

INSERT IGNORE INTO Vendor (VendorID, VendorName, VendorWebsite)
VALUES (012, 'Hilltop Produce', 'hilltopproduce.com');

INSERT IGNORE INTO Vendor (VendorID, VendorName, VendorWebsite)
VALUES (013, 'Valley Fresh', 'valleyfresh.com');

INSERT IGNORE INTO Vendor (VendorID, VendorName, VendorWebsite)
VALUES (014, 'Morning Dew', 'morningdew.com');

INSERT IGNORE INTO Vendor (VendorID, VendorName, VendorWebsite)
VALUES (015, 'Cedar Grove', 'cedargrove.com');

INSERT IGNORE INTO Vendor (VendorID, VendorName, VendorWebsite)
VALUES (016, 'Sweet Berry', 'sweetberry.com');

INSERT IGNORE INTO Vendor (VendorID, VendorName, VendorWebsite)
VALUES (017, 'Meadow Farms', 'meadowfarms.com');

INSERT IGNORE INTO Vendor (VendorID, VendorName, VendorWebsite)
VALUES (018, 'Farmhouse Eggs', 'farmhouseeggs.com');

INSERT IGNORE INTO Vendor (VendorID, VendorName, VendorWebsite)
VALUES (019, 'Country Greens', 'countrygreens.com');

INSERT IGNORE INTO Vendor (VendorID, VendorName, VendorWebsite)
VALUES (020, 'Wildflower Farm', 'wildflowerfarm.com');


-- Demo relationships/inventory needed for the existing application queries.
-- Assign each of the team's 20 vendors to one of its 10 markets.
INSERT IGNORE INTO Vendor_Market (MarketID, VendorID, StallNumber) VALUES
(1, 1, 1),
(2, 2, 1),
(3, 3, 1),
(4, 4, 1),
(5, 5, 1),
(6, 6, 1),
(7, 7, 1),
(8, 8, 1),
(9, 9, 1),
(10, 10, 1),
(1, 11, 2),
(2, 12, 2),
(3, 13, 2),
(4, 14, 2),
(5, 15, 2),
(6, 16, 2),
(7, 17, 2),
(8, 18, 2),
(9, 19, 2),
(10, 20, 2);

-- Give each existing product a vendor listing (with stock and price).
INSERT IGNORE INTO Vendor_Inventory (ProductID, VendorID, QuantityInStock, ProductCost) VALUES
(0, 1, 20, 2.00),
(1, 2, 21, 2.75),
(2, 3, 22, 3.50),
(3, 4, 23, 4.25),
(4, 5, 24, 5.00),
(5, 6, 25, 5.75),
(6, 7, 26, 6.50),
(7, 8, 27, 7.25),
(8, 9, 28, 2.00),
(9, 10, 29, 2.75),
(10, 11, 30, 3.50),
(11, 12, 31, 4.25),
(12, 13, 20, 5.00),
(13, 14, 21, 5.75),
(14, 15, 22, 6.50),
(15, 16, 23, 7.25),
(16, 17, 24, 2.00),
(17, 18, 25, 2.75),
(18, 19, 26, 3.50),
(19, 20, 27, 4.25),
(20, 1, 28, 5.00),
(21, 2, 29, 5.75),
(22, 3, 30, 6.50),
(23, 4, 31, 7.25),
(24, 5, 20, 2.00),
(25, 6, 21, 2.75);

-- Sample customers for the existing pre-order workflow.
INSERT IGNORE INTO Customer (CustomerID, CustomerName, CustomerEmail) VALUES
(1, 'Sample Customer', 'sample1@example.com'),
(2, 'Demo Customer', 'sample2@example.com');
