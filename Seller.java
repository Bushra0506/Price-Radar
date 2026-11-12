
import java.util.ArrayList;

public class Seller {

    private String sellerName;
    private double price;
    private double deliveryCharge;
    private int deliveryDays;

    private ArrayList<Double> priceHistory;

    public Seller(String sellerName, double price,
                  double deliveryCharge, int deliveryDays) {

        this.sellerName = sellerName;
        this.price = price;
        this.deliveryCharge = deliveryCharge;
        this.deliveryDays = deliveryDays;

        priceHistory = new ArrayList<>();
        priceHistory.add(price);
    }

    public String getSellerName() {
        return sellerName;
    }

    public double getPrice() {
        return price;
    }

    public double getDeliveryCharge() {
        return deliveryCharge;
    }

    public int getDeliveryDays() {
        return deliveryDays;
    }

    public double getTotalPrice() {
        return price + deliveryCharge;
    }

    public void updatePrice(double newPrice) {
        price = newPrice;
        priceHistory.add(newPrice);
    }

    public double getLowestPriceEver() {
        double lowest = priceHistory.get(0);

        for (double p : priceHistory) {
            if (p < lowest) {
                lowest = p;
            }
        }

        return lowest;
    }

    public ArrayList<Double> getPriceHistory() {
        return priceHistory;
    }
}
