package savedcarts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class SavedCartsDAO implements SavedCartsInterface{
    private final Connection connection ;

    public SavedCartsDAO(Connection connection) {
        this.connection = connection;
    }


    public void saveCartItems(int userId) {
        String query = "SELECT itemId, quantity FROM Cart WHERE userId = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int itemId = rs.getInt("itemId");
                int quantity = rs.getInt("quantity");


                String insertQuery = "INSERT INTO SavedCarts (userId, itemId, quantity, updatedTime) VALUES (?, ?, ?, ?)";
                try (PreparedStatement insertPs = connection.prepareStatement(insertQuery)) {
                    insertPs.setInt(1, userId);
                    insertPs.setInt(2, itemId);
                    insertPs.setInt(3, quantity);
                    insertPs.setObject(4, LocalDate.now());
                    insertPs.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
