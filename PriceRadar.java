
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.SQLException;

public class PriceRadar {

    private ArrayList<Product> products;
    private Scanner sc;
    private ProductDAO productDAO;
    private SellerDAO sellerDAO;
    private PriceHistoryDAO priceHistoryDAO;

    public PriceRadar(Scanner sc) {

    products = new ArrayList<>();
    this.sc = sc;

    productDAO = new ProductDAO();
sellerDAO = new SellerDAO();
priceHistoryDAO = new PriceHistoryDAO();

loadProductsFromDatabase();
}
    // Find product by name
    public Product findProduct(String name) {

        for (Product p : products) {
            if (p.getProductName().equalsIgnoreCase(name)) {
                return p;
            }
        }

        return null;
    }

    // Add product
    
public void addProduct() {

    System.out.println("\n===== ADD PRODUCT =====");

    System.out.print("Enter product name: ");
    String name = sc.nextLine().trim();

    if (name.isEmpty()) {
        System.out.println("Product name cannot be empty!");
        return;
    }

    if (findProduct(name) != null) {
        System.out.println("Product already exists!");
        return;
    }

    // Check whether product already exists in MySQL
    if (productDAO.getProductId(name) != -1) {
        System.out.println("Product already exists in database!");
        return;
    }

    Product product = new Product(name);

    for (int i = 1; i <= 3; i++) {

        System.out.println("\nSeller " + i);

        System.out.print("Enter seller name: ");
        String sellerName = sc.nextLine().trim();

        if (sellerName.isEmpty()) {
            System.out.println("Seller name cannot be empty!");
            i--;
            continue;
        }

        if (isDuplicateSeller(product, sellerName)) {
            System.out.println("Seller already entered!");
            i--;
            continue;
        }

        double price = readPositiveDouble("Enter product price: ", false);
        double delivery = readPositiveDouble("Enter delivery charge: ", true);
        int days = readNonNegativeInt("Enter delivery days: ");

        Seller seller = new Seller(
                sellerName, price, delivery, days
        );

        product.addSeller(seller);
    }

    // Save product and all sellers in one transaction
    try (Connection con = DBConnection.getConnection()) {

        con.setAutoCommit(false);

        try {

            int productId = productDAO.addProduct(name, con);
            if (productId <= 0) {
    throw new SQLException("Product ID could not be generated!");
}

            for (Seller s : product.getSellers()) {
    sellerDAO.addSeller(productId, s, con);
}
            // Commit only when everything is saved
            con.commit();

            products.add(product);

            System.out.println("\nProduct and all 3 sellers saved successfully!");

        } catch (SQLException e) {

            con.rollback();

            System.out.println("\nSaving failed! All database changes were rolled back.");
            e.printStackTrace();
        }

    } catch (SQLException e) {

        System.out.println("Database connection error!");
        e.printStackTrace();
    }
}

    // Check duplicate seller
    private boolean isDuplicateSeller(Product product, String name) {

        for (Seller s : product.getSellers()) {
            if (s.getSellerName().equalsIgnoreCase(name)) {
                return true;
            }
        }

        return false;
    }

    // Read valid price or delivery charge
    private double readPositiveDouble(String message, boolean allowZero) {

        while (true) {

            System.out.print(message);

            try {
                double value = Double.parseDouble(sc.nextLine().trim());

                if (Double.isNaN(value) || Double.isInfinite(value)) {
                    System.out.println("Enter a finite number!");
                    continue;
                }

                if (allowZero ? value < 0 : value <= 0) {
                    System.out.println("Invalid value! Try again.");
                    continue;
                }

                return value;

            } catch (NumberFormatException e) {
                System.out.println("Enter a valid numeric value!");
            }
        }
    }

    // Read valid delivery days
    private int readNonNegativeInt(String message) {

        while (true) {

            System.out.print(message);

            try {
                int days = Integer.parseInt(sc.nextLine().trim());

                if (days < 0) {
                    System.out.println("Days cannot be negative!");
                    continue;
                }

                return days;

            } catch (NumberFormatException e) {
                System.out.println("Enter whole numbers only!");
            }
        }
    }

    // View all products
    public void viewAllProducts() {

        if (products.isEmpty()) {
            System.out.println("No products available!");
            return;
        }

        for (Product p : products) {
            p.displayProduct();
        }
    }

public void searchProducts() {

    System.out.print("Enter product name to search: ");
    String name = sc.nextLine().trim();

    boolean found = false;

    for (Product product : products) {

        if (product.getProductName().toLowerCase()
                .contains(name.toLowerCase())) {

            if (!found) {
                System.out.println("\nMatching products found:");
                found = true;
            }

            product.displayProduct();
        }
    }

    if (!found) {
        System.out.println("Product not found!");
    }
}

public void sortProducts() {

    if (products.isEmpty()) {
        System.out.println("No products available!");
        return;
    }

    System.out.println("\n===== SORT PRODUCTS =====");
    System.out.println("1. Sort by Product Name");
    System.out.println("2. Sort by Lowest Total Price");
    System.out.println("3. Sort by Fastest Delivery Days");

    System.out.print("Enter your choice: ");
    String choice = sc.nextLine().trim();

    switch (choice) {

        case "1":
            products.sort((p1, p2) ->
                    p1.getProductName().compareToIgnoreCase(
                            p2.getProductName()));

            System.out.println("\n===== SORTED BY NAME =====");
            break;

        case "2":
    products.sort((p1, p2) -> {
        double price1 = p1.getLowestTotalPrice();
        double price2 = p2.getLowestTotalPrice();

        if (price1 == -1) return 1;
        if (price2 == -1) return -1;

        return Double.compare(price1, price2);
    });

    System.out.println("\n===== SORTED BY PRICE =====");
    break;

case "3":
    products.sort((p1, p2) -> {
        int days1 = p1.getFastestDeliveryDays();
        int days2 = p2.getFastestDeliveryDays();

        if (days1 == -1) return 1;
        if (days2 == -1) return -1;

        return Integer.compare(days1, days2);
    });

    System.out.println("\n===== SORTED BY DELIVERY DAYS =====");
    break;

        default:
            System.out.println("Invalid choice! Try again.");
            return;
    }

    for (Product p : products) {

        System.out.println(
                p.getProductName()
                + " | Lowest Price: Rs. "
                + p.getLowestTotalPrice()
                + " | Fastest Delivery: "
                + p.getFastestDeliveryDays() + " days");
    }
}


    public void comparePrices() {

    System.out.print("Enter product name: ");
    String name = sc.nextLine().trim();

    Product product = findProduct(name);

    if (product == null) {
        System.out.println("Product not found!");
        return;
    }

    if (product.getSellers().isEmpty()) {
        System.out.println("No sellers available for this product!");
        return;
    }

    System.out.println("\n===== PRICE COMPARISON =====");

    System.out.printf("%-15s %-12s %-12s %-12s%n",
            "Seller", "Price", "Delivery", "Total");

    for (Seller s : product.getSellers()) {

        System.out.printf("%-15s %-12.2f %-12.2f %-12.2f%n",
                s.getSellerName(),
                s.getPrice(),
                s.getDeliveryCharge(),
                s.getTotalPrice());
    }

    Seller cheapest = product.getCheapestSeller();

    System.out.println("\nCheapest Seller: "
            + cheapest.getSellerName());

    System.out.println("Best Total Price: Rs. "
            + cheapest.getTotalPrice());
}

public void viewPriceHistory() {

    System.out.print("Enter product name: ");
    String name = sc.nextLine().trim();

    Product product = findProduct(name);

    if (product == null) {
        System.out.println("Product not found!");
        return;
    }

    int productId = productDAO.getProductId(
            product.getProductName());

    System.out.println("\n===== PRICE HISTORY =====");
    System.out.println("Product: " + product.getProductName());

    // Check whether sellers are available
    if (product.getSellers().isEmpty()) {
        System.out.println("No sellers available for this product!");
        return;
    }

    for (Seller seller : product.getSellers()) {

        int sellerId = sellerDAO.getSellerId(
                productId, seller.getSellerName());

        System.out.println("\nSeller: " + seller.getSellerName());

        if (sellerId == -1) {
            System.out.println("Seller not found in database.");
            continue;
        }

        ArrayList<String> history =
                priceHistoryDAO.getPriceHistory(sellerId);

        if (history.isEmpty()) {
            System.out.println("No price history available.");
            continue;
        }

        System.out.println("Recorded Prices:");

        for (String record : history) {
            System.out.println(record);
        }

        double lowest =
                priceHistoryDAO.getLowestPriceEver(sellerId);

        System.out.println("Lowest Price Ever: Rs. " + lowest);
    }
}


// Set Target Price Alert
public void setTargetPriceAlert() {

    System.out.print("Enter product name: ");
    String name = sc.nextLine().trim();

    Product product = findProduct(name);

    if (product == null) {
        System.out.println("Product not found!");
        return;
    }

    double existingTarget = product.getTargetPrice();

    if (existingTarget > 0) {

        System.out.println("Previously saved target price: Rs. "
                + existingTarget);

        System.out.print("Do you want to update it? (yes/no): ");
        String choice = sc.nextLine().trim();

        if (!choice.equalsIgnoreCase("yes")) {
    System.out.println("Keeping your existing target price.");

    double currentTarget = existingTarget;
    Seller cheapest = product.getCheapestSeller();

    if (cheapest != null &&
            cheapest.getTotalPrice() <= currentTarget) {

        System.out.println("\n*** PRICE ALERT! ***");
        System.out.println("Your target price has been reached!");
        System.out.println("Cheapest Seller: "
                + cheapest.getSellerName());
        System.out.println("Current Total: Rs. "
                + cheapest.getTotalPrice());

    } else if (cheapest != null) {

        System.out.println("\nTarget price not reached yet.");
        System.out.println("Current Lowest Price: Rs. "
                + cheapest.getTotalPrice());
    }

    return;
}
    }

    double target = readPositiveDouble(
            "Enter your target price: ", false);

    int productId = productDAO.getProductId(
            product.getProductName());

    boolean saved = productDAO.saveTargetPrice(
            productId, target);

    if (!saved) {
        System.out.println("Could not save target alert!");
        return;
    }

    product.setTargetPrice(target);

    System.out.println("\nTarget price saved successfully!");
    System.out.println("Target Price: Rs. " + target);

    Seller cheapest = product.getCheapestSeller();

    if (cheapest != null && cheapest.getTotalPrice() <= target) {

        System.out.println("\n*** PRICE ALERT! ***");
        System.out.println("Your target price has been reached!");
        System.out.println("Cheapest Seller: "
                + cheapest.getSellerName());
        System.out.println("Current Total: Rs. "
                + cheapest.getTotalPrice());

    } else if (cheapest != null) {

        System.out.println("\nTarget price not reached yet.");
        System.out.println("Current Lowest Price: Rs. "
                + cheapest.getTotalPrice());
    }
}



public void updateSellerPrice() {

    System.out.print("Enter product name: ");
    String name = sc.nextLine().trim();

    Product product = findProduct(name);

    if (product == null) {
        System.out.println("Product not found!");
        return;
    }

    System.out.print("Enter seller name: ");
    String sellerName = sc.nextLine().trim();

    Seller selectedSeller = null;

    for (Seller s : product.getSellers()) {

        if (s.getSellerName().equalsIgnoreCase(sellerName)) {
            selectedSeller = s;
            break;
        }
    }

    if (selectedSeller == null) {
        System.out.println("Seller not found!");
        return;
    }

    System.out.println("Current price: Rs. "
            + selectedSeller.getPrice());

    double newPrice = readPositiveDouble(
            "Enter new product price: ", false);

    // Get product ID and seller ID from MySQL
    int productId = productDAO.getProductId(
            product.getProductName());

    int sellerId = sellerDAO.getSellerId(
            productId, selectedSeller.getSellerName());

    if (sellerId == -1) {
        System.out.println("Seller not found in database!");
        return;
    }

    // Update price and save price history in MySQL
    boolean updated = sellerDAO.updateSellerPrice(
            sellerId, newPrice);

    if (!updated) {
        System.out.println("Could not update price in database!");
        return;
    }

    // Update the Java object only after database update succeeds
    selectedSeller.updatePrice(newPrice);

    System.out.println("\nPrice updated successfully!");

    System.out.println("New total price: Rs. "
            + selectedSeller.getTotalPrice());

    double lowestPrice = priceHistoryDAO.getLowestPriceEver(sellerId);

System.out.println("Lowest price ever: Rs. " + lowestPrice);

    // Check target price if one has been set
    if (product.getTargetPrice() >= 0 &&
            selectedSeller.getTotalPrice()
                    <= product.getTargetPrice()) {

        System.out.println("*** PRICE ALERT! ***");
        System.out.println("Your target price has been reached!");
    }
}


    // Load products and their sellers from MySQL
    private void loadProductsFromDatabase() {

        products = productDAO.getAllProducts();

        for (Product product : products) {

            int productId = productDAO.getProductId(
                    product.getProductName());
                    double savedTarget = productDAO.getTargetPrice(productId);

if (savedTarget >= 0) {
    product.setTargetPrice(savedTarget);
}

            ArrayList<Seller> sellers =
                    sellerDAO.getSellersByProductId(productId);

            for (Seller seller : sellers) {
                product.addSeller(seller);
            }
        }

        System.out.println("Products loaded from database: "
                + products.size());
    }


}
