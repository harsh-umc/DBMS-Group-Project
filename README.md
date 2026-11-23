# DBMS-Group-Project

## Team Members
- Harshwardhan Jejaria
- Nickolai Jalowiec
- Jacob Roberts

We are using Trello Board and the link is https://trello.com/invite/b/6ab9964d0fa7f6deec978def/ATTId9dc032568977e3b113f9e2ac5d3c8622B3742BB/dbms-group-project

# DBMS-Group-Project

## Team Members
- Harshwardhan Jejaria
- Nickolai Jalowiec
- Jacob Roberts

We are using Trello Board and the link is https://trello.com/invite/b/6ab9964d0fa7f6deec978def/ATTId9dc032568977e3b113f9e2ac5d3c8622B3742BB/dbms-group-p

## Project Proposal

Our project is a Farmers Market Database Application that will make it easier for customers to find farmers markets, vendors, and products. Users will be able to see what products are available before visiting a market and place pre orders for products they want to purchase.

## Features

- Browse a list of farmers markets
- View details about a specific farmers market
- View vendors that are located at each market
- Search for products across all farmers markets
- View product prices and availability
- Place pre-orders for products
- Keep track of customer orders
- Update product inventory when orders are placed

## Database

The database will store information about:

- Farmers Markets
- Vendors
- Products
- Customers
- Orders
- Order Items

A farmers market can have multiple vendors, and a vendor can participate in multiple farmers markets. Vendors can sell multiple products, and customers can place orders containing one or more products.

## Pre-Orders

When a customer places a pre-order, the application will use a database transaction. The transaction will create the order, add the selected products, and update the available product inventory. If part of the order fails, the transaction will be rolled back so incorrect information is not saved.

## Final Application

The final application will have a graphical user interface connected to the database. Users will be able to browse markets, view vendors, search for products, and place pre-orders through the application.


## JavaFX desktop application setup

Requirements: Java 17, Maven, MySQL Server. The application is in `DBMS Program/dbms_queries`.

1. In MySQL Workbench, open `schema.sql` from the repository root and execute the entire file against your local MySQL server. Then execute `data.sql` **once** to load the shared sample records. The seed file contains explicit IDs and is not designed for repeated imports.
2. Create a MySQL account for your local installation with access to the `Market_Manager` database, or use an existing account. **Never commit credentials.**
3. In PowerShell, set your own local connection environment variables:

```powershell
$env:DB_HOST="127.0.0.1"
$env:DB_PORT="3306"
$env:DB_NAME="Market_Manager"
$env:DB_USER="YOUR_LOCAL_MYSQL_USER"
$env:DB_PASSWORD = Read-Host "MySQL password"
cd "DBMS Program/dbms_queries"
mvn clean javafx:run
```

4. Browse Markets; select a market and click **View Vendors**; use Products to search inventory and choose a product for a pre-order. The order form accepts a customer ID from the sample `Customer` table (1 or 2), an address, and a quantity. Orders use a transaction and decrement stock on success.
5. Run `queries.sql` in Workbench to inspect the team's SQL query examples. An order-contents example assumes order ID 1, so it returns no rows until that order exists.

**Already created a local database?** Back up any data you want to keep first. The scripts are for a fresh installation; do not run `data.sql` on top of the earlier three temporary test markets or partially imported team data, because explicit IDs will collide. For a clean *development-only* reset, run `DROP DATABASE Market_Manager;` in Workbench only after confirming it contains no important data, then execute `schema.sql` followed by `data.sql`.

The JavaFX app uses the existing `Queries.java` methods; it does not require the separate web prototype files. Inventory and vendor-market assignments in `data.sql` are demo fixtures pending team review. Product ID 9 was unnamed in the original data and is temporarily labeled Carrot. The SQL examples, application and sample transaction still require a complete end-to-end team test.
