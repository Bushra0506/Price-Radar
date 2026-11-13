# PriceRadar: Product Price Comparison System

## Team

1. Bushra Ishaq (USN: 1AT24IS035)
2. A S Brunda (USN: 1AT24IS001)

**Track:** Java

## How to Run

### Prerequisites

- Java JDK
- MySQL Server
- MySQL Connector/J
- VS Code or any Java IDE

### Database Setup

1. Open MySQL Workbench and connect to your local MySQL server.
2. Open the `database.sql` file included in this repository.
3. Execute the script using the lightning-bolt button.
4. The script creates the `priceradar_db` database and its four required tables: `products`, `sellers`, `price_history` and `target_alerts`.

> The script creates tables only if they do not already exist. It does not insert the sample product catalogue or delete existing records.

### Application Setup

1. Clone or download this repository.
2. Configure your local MySQL username and password in `DBConnection.java`. Keep your actual database password private and never commit it to GitHub.
3. Make sure `mysql-connector-j-26.10.0.jar` is in the project root folder (the same folder as the Java files).
4. Open the repository folder in the terminal.
5. Compile the Java files (Windows PowerShell):

```powershell
javac -cp ".;mysql-connector-j-26.10.0.jar" *.java
```

6. Run the application:

```powershell
java -cp ".;mysql-connector-j-26.10.0.jar" Main
```

> The commands above match the current GitHub repository layout, where the Java files and Connector/J JAR are in the root folder.

## Features That Work

1. **Add Products:** Add products with exactly three sellers.
2. **Price Comparison:** Compare product prices, delivery charges and total prices to identify the cheapest seller.
3. **Price History:** Store and view historical prices with timestamps and identify the lowest recorded price.
4. **Target Price Alerts:** Set a target price and receive an alert when the current total price reaches or falls below it.
5. **Search Products:** Search for products using their names, including partial matches.
6. **Sort Products:** Sort products by name, lowest total price and fastest delivery.
7. **Update Seller Prices:** Update seller prices and store the changes in the price history.
8. **Database Persistence:** Store product, seller, price history and target price information in MySQL.

## Sample Run

```text
===== PRICE RADAR =====

1. Add Product
2. View All Products
3. Compare Seller Prices
4. View Price History
5. Set Target Price Alert
6. Search Products
7. Sort Products
8. Update Seller Price
9. Exit

Enter your choice: 3
Enter product name: Final Check Product

===== PRICE COMPARISON =====

Seller       Price       Delivery       Total
Amazon       900.00      50.00          950.00
Flipkart     950.00      100.00         1050.00
Croma        1100.00     0.00           1100.00

Cheapest Seller: Amazon
Best Total Price: Rs. 950.0
```
