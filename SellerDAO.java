
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SellerDAO {

    // Add seller to MySQL
    
// Add seller and initial price history to MySQL

// Add seller and save initial price history

// Add seller and save their initial price in price_history
public boolean addSeller(int productId, Seller seller) {

    String sellerSQL = "INSERT INTO sellers " +
            "(product_id, seller_name, price, delivery_charge, delivery_days) " +
            "VALUES (?, ?, ?, ?, ?)";

    String historySQL =
            "INSERT INTO price_history (seller_id, price) VALUES (?, ?)";

    try (Connection con = DBConnection.getConnection()) {

        con.setAutoCommit(false);

        try {
            int sellerId;

            // Insert seller
            try (PreparedStatement ps = con.prepareStatement(
                    sellerSQL, java.sql.Statement.RETURN_GENERATED_KEYS)) {

                ps.setInt(1, productId);
                ps.setString(2, seller.getSellerName());
                ps.setDouble(3, seller.getPrice());
                ps.setDouble(4, seller.getDeliveryCharge());
                ps.setInt(5, seller.getDeliveryDays());

                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        sellerId = rs.getInt(1);
                    } else {
                        throw new SQLException("Seller ID was not generated!");
                    }
                }
            }

            // Save initial price history
            try (PreparedStatement ps = con.prepareStatement(historySQL)) {

                ps.setInt(1, sellerId);
                ps.setDouble(2, seller.getPrice());

                ps.executeUpdate();
            }

            con.commit();

            System.out.println("Seller saved successfully!");
            System.out.println("Initial price history saved!");
            return true;

        } catch (SQLException e) {
            con.rollback();
            throw e;
        }

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

public boolean addSeller(int productId, Seller seller, Connection con)
        throws SQLException {

    String sellerSQL = "INSERT INTO sellers " +
            "(product_id, seller_name, price, delivery_charge, delivery_days) " +
            "VALUES (?, ?, ?, ?, ?)";

    String historySQL =
            "INSERT INTO price_history (seller_id, price) VALUES (?, ?)";

    int sellerId;

    // Insert seller
    try (PreparedStatement ps = con.prepareStatement(
            sellerSQL, java.sql.Statement.RETURN_GENERATED_KEYS)) {

        ps.setInt(1, productId);
        ps.setString(2, seller.getSellerName());
        ps.setDouble(3, seller.getPrice());
        ps.setDouble(4, seller.getDeliveryCharge());
        ps.setInt(5, seller.getDeliveryDays());

        ps.executeUpdate();

        try (ResultSet rs = ps.getGeneratedKeys()) {
            if (rs.next()) {
                sellerId = rs.getInt(1);
            } else {
                throw new SQLException("Seller ID was not generated!");
            }
        }
    }

    // Save initial price history
    try (PreparedStatement ps = con.prepareStatement(historySQL)) {

        ps.setInt(1, sellerId);
        ps.setDouble(2, seller.getPrice());

        ps.executeUpdate();
    }

    return true;
}




    // Update seller price and save price history
    public boolean updateSellerPrice(int sellerId, double newPrice) {

        String updateSQL =
                "UPDATE sellers SET price = ? WHERE seller_id = ?";

        String historySQL =
                "INSERT INTO price_history (seller_id, price) VALUES (?, ?)";

        try (Connection con = DBConnection.getConnection()) {

            con.setAutoCommit(false);

            try {
                // Update seller's current price
                try (PreparedStatement ps = con.prepareStatement(updateSQL)) {

                    ps.setDouble(1, newPrice);
                    ps.setInt(2, sellerId);

                    int rows = ps.executeUpdate();

                    if (rows == 0) {
                        con.rollback();
                        System.out.println("Seller not found!");
                        return false;
                    }
                }

                // Save new price in history
                try (PreparedStatement ps = con.prepareStatement(historySQL)) {

                    ps.setInt(1, sellerId);
                    ps.setDouble(2, newPrice);

                    ps.executeUpdate();
                }

                con.commit();

                System.out.println("Seller price updated successfully!");
                System.out.println("Price history saved successfully!");

                return true;

            } catch (SQLException e) {
                con.rollback();
                throw e;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get seller ID using product ID and seller name
    public int getSellerId(int productId, String sellerName) {

        String sql = "SELECT seller_id FROM sellers " +
                     "WHERE product_id = ? AND seller_name = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productId);
            ps.setString(2, sellerName);

            try (java.sql.ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt("seller_id");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }
// Get all sellers belonging to a product
public ArrayList<Seller> getSellersByProductId(int productId) {

    ArrayList<Seller> sellers = new ArrayList<>();

    String sql = "SELECT * FROM sellers WHERE product_id = ?";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, productId);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            Seller seller = new Seller(
                rs.getString("seller_name"),
                rs.getDouble("price"),
                rs.getDouble("delivery_charge"),
                rs.getInt("delivery_days")
            );

            sellers.add(seller);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return sellers;
}

}
