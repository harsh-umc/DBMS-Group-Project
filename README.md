# DBMS-Group-Project

## Team Members
- Harshwardhan Jejaria
- Nikolai Jalowiec
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

This is a **JavaFX desktop app**, not a website. Source code is under `DBMS Program/dbms_queries`. Each teammate runs the JavaFX app and a **local MySQL server** on their own computer; GitHub shares code and SQL scripts, **not** the contents of anyone's local MySQL database.

### Requirements

- Java JDK 17
- Apache Maven
- MySQL Server and MySQL Workbench

### First-time database setup

1. Clone or pull the repository.
2. Open **`schema.sql`** from the repository root in MySQL Workbench, and execute the whole file. It creates the `Market_Manager` database and nine tables.
3. Open **`data.sql`** from the repository root and execute the whole file. It seeds 10 markets, 20 vendors, 26 products, 20 vendor-market relationships, 26 inventory records, and 2 customers.
4. The seed script uses `INSERT IGNORE` so existing primary keys are skipped on rerun. It **does not overwrite or repair existing data**. Check Workbench warnings if records are missing. Do not drop or delete a teammate's database just to rerun setup.

### Local credentials and launch (Windows PowerShell)

Use a MySQL account with access to `Market_Manager`. The credentials below are local to **your computer**. Never commit passwords or secrets to GitHub.

From the **repository root**:

```powershell
$env:DB_HOST="127.0.0.1"
$env:DB_PORT="3306"
$env:DB_NAME="Market_Manager"
$env:DB_USER="YOUR_LOCAL_MYSQL_USER"
$env:DB_PASSWORD = Read-Host "MySQL password"
cd "DBMS Program/dbms_queries"
mvn clean javafx:run
```

If your terminal is **already in** `DBMS Program/dbms_queries`, skip the `cd` line. Set the variables and run Maven in the **same terminal session**.

### Test checklist

- **Markets:** 10 rows should load.
- **Vendors:** Select Downtown Farmers Market, then **View Vendors**. Green Acres and Red Barn Farm should appear.
- **Products:** 26 inventory listings should load.
- **Search:** Search for `Apple` in Products; one matching listing should appear.
- **Pre-Orders:** Select Apple, click **Pre-Order Selected Product**, enter an existing customer ID (1 or 2), a delivery address, and quantity 1. A successful order is stored in MySQL and reduces stock. **This changes your local database.**

Verify initial seed counts in MySQL Workbench:

```sql
USE Market_Manager;
SELECT
  (SELECT COUNT(*) FROM Farmers_Market) AS Markets,
  (SELECT COUNT(*) FROM Vendor) AS Vendors,
  (SELECT COUNT(*) FROM Products) AS Products,
  (SELECT COUNT(*) FROM Vendor_Inventory) AS Inventory,
  (SELECT COUNT(*) FROM Vendor_Market) AS VendorMarkets,
  (SELECT COUNT(*) FROM Customer) AS Customers;
```

Expected initial counts: **10, 20, 26, 26, 20, 2**.

Run `queries.sql` in Workbench for additional SQL examples. Its order-contents example assumes order ID 1 and will return no rows before that order exists.

### Notes for the team

- The JavaFX UI calls methods in `Queries.java`; `DatabaseConnection.java` reads local environment variables.
- The separate HTML/CSS/JavaScript prototype is **not needed** to run the JavaFX application.
- Inventory and vendor-market assignments are demo fixtures pending team review. Product ID 9 is labeled Carrot as a placeholder.
- Local end-to-end testing passed for markets, vendors, products, search, pre-order creation, and inventory update. **Independent teammate installation is still unverified.**
