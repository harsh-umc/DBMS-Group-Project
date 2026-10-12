CREATE DATABASE Market_Manager;

CREATE TABLE Farmers_Market(
  MarketID INT PRIMARY KEY AUTO_INCREMENT,
  MarketName VARCHAR(30) NOT NULL,
  Address varchar(30) NOT NULL
  );

CREATE TABLE Vendor(
  VendorID INT PRIMARY KEY AUTO_INCREMENT,
  VendorName VARCHAR(15) NOT NULL,
  VendorWebsite VARCHAR(30)
  );

CREATE TABLE Vendor_Market(
  MarketID INT PRIMARY KEY,
  VendorID INT PRIMARY KEY,
  StallNumber INT NOT NULL,
  FOREIGN KEY (MarketID) REFERENCES Farmers_Market(MarketID),
  FOREIGN KEY (VendorID) REFERENCES Vendor(VendorID)
  );
