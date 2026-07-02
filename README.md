# NASCA Inventory Management System

NASCA is a desktop-based Inventory Management System built with **Java Swing** and **MySQL**. It helps businesses efficiently manage inventory records, suppliers, stock transactions, and reports through a modern, user-friendly interface.

### Important Note

A runnable JAR version of NASCA has been provided for convenience. However, due to image resource loading limitations during JAR packaging, some UI images may not display correctly when running the application through the JAR file.

For the best experience and full functionality, it is recommended to download the source code and run the project through Eclipse using the setup instructions provided above. Running the project from Eclipse ensures that all images, assets, database connections, and application features work as intended.


---

## Features

- **Secure Login** — username and password authentication against a MySQL database
- **Dashboard** — at-a-glance stats including total products, low stock items, suppliers, and monthly transactions
- **Inventory Management** — add, update, delete, and search inventory items
- **Barcode-Assisted Entry** — scan or type a barcode to auto-fill product details via the Open Food Facts API
- **Supplier Management** — maintain supplier records and link them to inventory items
- **Transaction Tracking** — record Stock In, Stock Out, Sales, and Purchases with automatic stock-level updates
- **Low Stock Alerts** — automatic alerts when items fall below their minimum stock level
- **Reports** — generate Stock Reports, Sales Reports, Purchase Reports, and Low Stock Reports
- **Settings** — light and dark mode toggle, account and profile customization

---

## Technology Stack

| Technology | Purpose |
|---|---|
| Java (JDK 21) | Core programming language |
| Java Swing | GUI development |
| MySQL | Database management |
| JDBC / MySQL Connector/J | Database connectivity |
| org.json | JSON data processing |
| Open Food Facts API | Barcode-based product lookup |
| Figma | UI/UX design and prototyping |
| GitHub | Version control and collaboration |

---

## Architecture

The project follows a layered architecture that separates concerns and improves maintainability:

```
UI Layer (Java Swing screens)
        ↓
Service Layer (business logic)
        ↓
DAO Layer (database queries)
        ↓
MySQL Database
```

---

## Project Structure

```
InventoryManagementSystem/
│
├── src/
│   ├── dataaccess/          # DAO classes (database queries)
│   │   ├── AlertDAO.java
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
│   ├── model/                  # Data models / POJOs
│   │   ├── Alert.java
│   │   ├── Inventory.java
│   │   ├── Supplier.java
│   │   ├── TransactionReport.java
│   │   └── User.java
│   │
│   ├── service/                # Business logic layer
│   │   ├── InventoryService.java
│   │   ├── LoginService.java
│   │   ├── ReportService.java
│   │   ├── SupplierService.java
│   │   └── UserService.java
│   │
│   └── ui/                     # Swing UI screens
│       ├── InventoryDashboard.java
│       ├── LoginPage.java
│       ├── SettingsPanel.java
│       ├── SuppliersPanel.java
│       └── TransactionAndReportsPanel.java
│
├── images/                     # UI assets (login background, logo, etc.)
├── database/
│   └── inventorys_schema.sql   # Database schema and setup script
└── README.md
```

---

## Setup & Installation

### Prerequisites

- Java JDK 21 or later
- MySQL Server (5.7+ or 8.0+)
- Eclipse IDE (recommended) or any Java IDE
- MySQL Connector/J JAR
- org.json JAR

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/NASCA.git
```

### 2. Set Up the Database

Create the database and import the schema:

```sql
CREATE DATABASE inventorys_schema;
USE inventorys_schema;
```

Then import the provided SQL file:

```bash
mysql -u root -p inventorys_schema < database/inventorys_schema.sql
```

Or open `database/inventorys_schema.sql` in MySQL Workbench and run it. This will create all required tables (`InventoryItem`, `supplierdetails`, `Transactions`, `login`, etc.) with sample data.

### 3. Configure the Database Connection

Open `src/database/DBConnection.java` and update the credentials:

```java
private static final String URL =
    "jdbc:mysql://localhost:3306/inventorys_schema?useSSL=false&serverTimezone=UTC";
private static final String USERNAME = "root";
private static final String PASSWORD = "your_password";
```

> **Note on port:** MySQL typically runs on `3306`, but some setups use `3307`. Check your MySQL configuration if the connection fails.

> **Note for MySQL 8+ users:** If you get an authentication error, run this in MySQL:
> ```sql
> ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'your_password';
> ```

### 4. Add Required Libraries to Eclipse

1. Right-click the project → **Build Path** → **Configure Build Path**
2. Go to **Libraries** → **Add External JARs**
3. Add:
   - `mysql-connector-java-x.x.x.jar`
   - `org.json-x.x.x.jar`
4. Click **Apply and Close**

### 5. Run the Application

Run `src/ui/LoginPage.java` as a Java Application. The login screen will appear.

Enter your credentials (from the `login` table in the database) to access the system.

---

## Barcode Product Lookup

NASCA supports barcode-assisted product entry using the **Open Food Facts API** — no API key required.

**To use barcode scanning:**

1. Download the **Barcode to PC Scanner** app on both your phone and PC.
2. Follow the app's instructions to connect the two devices.
3. In the **Add Item** screen, click into the barcode field and scan any product barcode with your phone — it will be entered automatically.
4. If the product exists in the Open Food Facts database, the item name, category, and other fields will be auto-filled.
5. Review the details, make any edits, and save the item to your inventory.

**Can't find a product?** Visit [world.openfoodfacts.org](https://world.openfoodfacts.org), search by barcode, and add or edit the product details yourself — it's free and open to contributions.

---

## Authors

Developed as part of the **ICT Academy of Kerala** program by:

- Anagha V Nair
- Ashna Anna Biju
- Chaithanya M
- Nrithika Dileepkumar
- Shreelekshmi Pillai

---

## License

This project is intended for educational and demonstration purposes only.
