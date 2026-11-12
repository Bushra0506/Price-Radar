
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        PriceRadar radar = new PriceRadar(sc);

        int choice = 0;

        while (choice != 9) {

            System.out.println("\n===== PRICE RADAR =====");
            System.out.println("1. Add Product");
            System.out.println("2. View All Products");
            System.out.println("3. Compare Seller Prices");
            System.out.println("4. View Price History");
            System.out.println("5. Set Target Price Alert");
            System.out.println("6. Search Products");
            System.out.println("7. Sort Products");
            System.out.println("8. Update Seller Price");
            System.out.println("9. Exit");

            System.out.print("Enter your choice: ");

            // Check whether input is available
            if (!sc.hasNextLine()) {
                System.out.println("\nInput ended. Exiting PriceRadar.");
                break;
            }

            try {

                choice = Integer.parseInt(sc.nextLine().trim());

                switch (choice) {

                    case 1:
                        radar.addProduct();
                        break;

                    case 2:
                        radar.viewAllProducts();
                        break;

                    case 3:
                        radar.comparePrices();
                        break;

                    case 4:
                        radar.viewPriceHistory();
                        break;

                    case 5:
                        radar.setTargetPriceAlert();
                        break;

                    case 6:
                        radar.searchProducts();
                        break;

                    case 7:
                        radar.sortProducts();
                        break;

                    case 8:
                        radar.updateSellerPrice();
                        break;

                    case 9:
                        System.out.println(
                                "Thank you for using PriceRadar!");
                        break;

                    default:
                        System.out.println(
                                "Invalid choice! Enter a number between 1 and 9.");
                }

            } catch (NumberFormatException e) {

                System.out.println(
                        "Invalid input! Enter numbers only.");
            }
        }

        sc.close();
    }
}
