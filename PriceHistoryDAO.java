
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PriceHistoryDAO {

    // Save price history
    public void addPriceHistory(int sellerId, double price) {

        String sql = "INSERT INTO price_history (seller_id, price) VALUES (?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, sellerId);
            ps.setDouble(2, price);

            ps.executeUpdate();

            System.out.println("Price history saved successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Retrieve price history from MySQL
    public ArrayList<String> getPriceHistory(int sellerId) {

        ArrayList<String> history = new ArrayList<>();

        String sql = "SELECT price, updated_at FROM price_history "
                   + "WHERE seller_id = ? ORDER BY updated_at ASC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, sellerId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                double price = rs.getDouble("price");
                String date = rs.getString("updated_at");

                history.add("Rs. " + price + " | " + date);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return history;
    }

    // Get the lowest price ever recorded for a seller
    public double getLowestPriceEver(int sellerId) {

        String sql = "SELECT MIN(price) AS lowest_price "
                   + "FROM price_history WHERE seller_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, sellerId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("lowest_price");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

}
