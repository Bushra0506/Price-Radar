
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductDAO {

    // Add product to MySQL
    public boolean addProduct(String productName) {

        String sql = "INSERT INTO products (product_name) VALUES (?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, productName);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Product saved to database!");
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Could not save product!");
            e.printStackTrace();
        }

        return false;
    }

public int addProduct(String productName, Connection con)
        throws SQLException {

    String sql = "INSERT INTO products (product_name) VALUES (?)";

    try (PreparedStatement ps = con.prepareStatement(
            sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

        ps.setString(1, productName);
        ps.executeUpdate();

        try (ResultSet rs = ps.getGeneratedKeys()) {
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                throw new SQLException("Product ID was not generated!");
            }
        }
    }
}


    // Get product ID using product name
    public int getProductId(String productName) {

        String sql = "SELECT product_id FROM products WHERE product_name = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, productName);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("product_id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    // Get all products from MySQL
    public ArrayList<Product> getAllProducts() {

        ArrayList<Product> products = new ArrayList<>();

        String sql = "SELECT * FROM products";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                String name = rs.getString("product_name");

                Product product = new Product(name);

                products.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    // Save or update target price for a product
    public boolean saveTargetPrice(int productId, double targetPrice) {

        String checkSql =
                "SELECT alert_id FROM target_alerts WHERE product_id = ?";

        String updateSql =
                "UPDATE target_alerts SET target_price = ? WHERE product_id = ?";

        String insertSql =
                "INSERT INTO target_alerts (product_id, target_price) VALUES (?, ?)";

        try (Connection con = DBConnection.getConnection()) {

            // Check whether an alert already exists
            boolean exists = false;

            try (PreparedStatement check =
                    con.prepareStatement(checkSql)) {

                check.setInt(1, productId);

                ResultSet rs = check.executeQuery();
                exists = rs.next();
            }

            if (exists) {

                try (PreparedStatement update =
                        con.prepareStatement(updateSql)) {

                    update.setDouble(1, targetPrice);
                    update.setInt(2, productId);

                    update.executeUpdate();
                }

            } else {

                try (PreparedStatement insert =
                        con.prepareStatement(insertSql)) {

                    insert.setInt(1, productId);
                    insert.setDouble(2, targetPrice);

                    insert.executeUpdate();
                }
            }

            System.out.println("Target price saved to database!");
            return true;

        } catch (SQLException e) {
            System.out.println("Could not save target price!");
            e.printStackTrace();
            return false;
        }
    }

    // Retrieve saved target price
    public double getTargetPrice(int productId) {

        String sql =
                "SELECT target_price FROM target_alerts WHERE product_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble("target_price");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

}
