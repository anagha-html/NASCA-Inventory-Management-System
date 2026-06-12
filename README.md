# NASCA Inventory Management System

A desktop **Inventory Management System** built with **Java Swing** and **MySQL**, designed to help small businesses manage stock, suppliers, customers, transactions, and reports through a clean, modern UI.

---

## ✨ Features

- **Secure Login** — username/password authentication against a MySQL `login` table
- **Dashboard** — at-a-glance stats: total products, low stock items, suppliers, and monthly transactions
- **Inventory Management** — add, update, delete, and search inventory items
- **Barcode Scanning** — scan/enter a product barcode to auto-fill item details via the [Open Food Facts API](https://world.openfoodfacts.org/)
- **Supplier Management** — maintain a list of suppliers and link them to inventory items
- **Transaction Management** — record Stock In, Stock Out, Sales, and Purchases, with automatic stock-level updates
- **Reports** — generate Stock Reports, Sales Reports, Purchase Reports, and Low Stock Reports
- **Low Stock Alerts** — automatic alerts on the dashboard when items fall below their minimum stock level
- **Settings** — manage account/profile settings

---

## 🛠️ Tech Stack

- **Language:** Java (JDK 21)
- **GUI:** Java Swing
- **Database:** MySQL
- **JDBC Driver:** MySQL Connector/J
- **Architecture:** Layered (UI → Service → DAO → Database)

---

## 📁 Project Structure

```
InventoryManagementSystem/
├── src/
│   ├── dataaccess/        # DAO classes (DB queries)
│   │   ├── AlertDAO.java
│   │   ├── CustomerDAO.java
│   │   ├── InventoryDAO.java
│   │   ├── SupplierDAO.java
│   │   ├── TransactionDAO.java
│   │   └── UserDAO.java
│   │
│   ├── database/
│   │   └── DBConnection.java   # MySQL connection setup
│   │
│   ├── exception/
│   │   └── InventoryException.java
│   │
│   ├── main/
│   │   └── Main.java           # Application entry point
│   │
│   ├── model/              # Data models / POJOs
│   │   ├── Alert.java
│   │   ├── Customer.java
│   │   ├── Inventory.java
│   │   ├── Supplier.java
│   │   ├── TransactionReport.java
│   │   └── User.java
│   │
│   ├── service/            # Business logic layer
│   │   ├── CustomerService.java
│   │   ├── InventoryService.java
│   │   ├── LoginService.java
│   │   ├── ReportService.java
│   │   ├── SupplierService.java
│   │   └── UserService.java
│   │
│   └── ui/                 # Swing UI screens
│       ├── InventoryDashboard.java
│       ├── LoginPage.java
│       ├── SettingsPanel.java
│       ├── SuppliersPanel.java
│       └── TransactionAndReportsPanel.java
│
├── images/                  # UI assets (login screen, etc.)
└── README.md
```

---

## ⚙️ Setup & Installation

### Prerequisites
- **JDK 21** or later
- **MySQL Server** (5.7+ or 8.0+)
- **Eclipse IDE** (or any Java IDE)
- **MySQL Connector/J** JAR

### 1. Clone the repository
```bash
git clone https://github.com/yourusername/InventoryManagementSystem.git
```

### 2. Set up the database
Create the database and required tables (`InventoryItem`, `supplierdetails`, `Transactions`, `login`, etc.) in MySQL. Example:

```sql
CREATE DATABASE inventorys_schema;
USE inventorys_schema;

CREATE TABLE login (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE InventoryItem (
    ItemID INT AUTO_INCREMENT PRIMARY KEY,
    ItemName VARCHAR(100) NOT NULL,
    Category VARCHAR(100),
    QuantityAvailable INT DEFAULT 0,
    MinimumStockLevel INT DEFAULT 0,
    Price DECIMAL(10,2) DEFAULT 0,
    SupplierID INT,
    FOREIGN KEY (SupplierID) REFERENCES supplierdetails(SupplierID)
);

CREATE TABLE Transactions (
    TransactionID   INT AUTO_INCREMENT PRIMARY KEY,
    ItemID          INT NOT NULL,
    TransactionType VARCHAR(20) NOT NULL,
    Quantity        INT NOT NULL,
    Price           DECIMAL(10,2) NOT NULL,
    TransactionDate DATETIME NOT NULL,
    Remarks         VARCHAR(255),
    FOREIGN KEY (ItemID) REFERENCES InventoryItem(ItemID)
);
```

### 3. Configure the database connection
Update the credentials in `src/database/DBConnection.java`:

```java
private static final String URL = "jdbc:mysql://localhost:3306/inventorys_schema?useSSL=false&serverTimezone=UTC";
private static final String USERNAME = "root";
private static final String PASSWORD = "your_password";
```

> ⚠️ **Note:** This project uses MySQL Connector/J **5.1.x**, which requires the driver class `com.mysql.jdbc.Driver` and the `mysql_native_password` authentication plugin. If using MySQL 8+, run:
> ```sql
> ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'your_password';
> ```

### 4. Add the MySQL Connector JAR
In Eclipse:
1. Right-click the project → **Build Path** → **Configure Build Path**
2. Go to **Libraries** → **Add External JARs**
3. Select your `mysql-connector-java-5.1.x.jar`
4. Click **Apply and Close**

### 5. Run the application
Run `src/main/Main.java` (or `LoginPage.java`) as a Java Application.

---

## 🖼️ Assets

Place the following images in the `images/` folder at the project root for the login screen background:
- `warehouse.jpg`
- `logo.png` *(optional — currently commented out until available)*

---

## 📦 External APIs

- **[Open Food Facts API](https://world.openfoodfacts.org/data)** — used for barcode-based product lookup when adding new inventory items. No API key required.

---

## 👥 Author

Developed as part of an Inventory Management System project (NASCA).

---

## 📄 License

This project is for educational/demonstration purposes.
