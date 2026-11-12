
import java.util.ArrayList;

public class Product {

    private String productName;
    private ArrayList<Seller> sellers;
    private double targetPrice = -1;
    

    // Constructor
    public Product(String productName) {
        this.productName = productName;
        sellers = new ArrayList<>();
        targetPrice = -1;
    }

    // Add seller
    public boolean addSeller(Seller seller) {

        for (Seller s : sellers) {
            if (s.getSellerName().equalsIgnoreCase(
                    seller.getSellerName())) {
                return false;
            }
        }

        sellers.add(seller);
        return true;
    }

    // Get product name
    public String getProductName() {
        return productName;
    }

    // Get sellers
    public ArrayList<Seller> getSellers() {
        return sellers;
    }

    // Set target price
    public void setTargetPrice(double targetPrice) {
        this.targetPrice = targetPrice;
    }

    // Get target price
    public double getTargetPrice() {
        return targetPrice;
    }

    // Find cheapest seller
    public Seller getCheapestSeller() {

        if (sellers.isEmpty()) {
            return null;
        }

        Seller cheapest = sellers.get(0);

        for (Seller s : sellers) {
            if (s.getTotalPrice() <
                    cheapest.getTotalPrice()) {
                cheapest = s;
            }
        }

        return cheapest;
    }

    // Get lowest total price
    public double getLowestTotalPrice() {

        Seller cheapest = getCheapestSeller();

        if (cheapest == null) {
            return -1;
        }

        return cheapest.getTotalPrice();
    }

    // Get fastest delivery
    public int getFastestDeliveryDays() {

        if (sellers.isEmpty()) {
            return -1;
        }

        int fastest = sellers.get(0).getDeliveryDays();

        for (Seller s : sellers) {
            if (s.getDeliveryDays() < fastest) {
                fastest = s.getDeliveryDays();
            }
        }

        return fastest;
    }

    // Display product
    public void displayProduct() {

        System.out.println("\nProduct: " + productName);

        for (Seller s : sellers) {
            System.out.println(
                s.getSellerName() +
                " | Price: " + s.getPrice() +
                " | Delivery: " + s.getDeliveryCharge() +
                " | Days: " + s.getDeliveryDays() +
                " | Total: " + s.getTotalPrice()
            );
        }
    }
    
}
