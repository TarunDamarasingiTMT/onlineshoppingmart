package daoimpl;

import dao.PromoCodesDao;
import model.PromoCodes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class PromoCodeDaoImpl implements PromoCodesDao {

    private final Connection connection;

    public PromoCodeDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public PromoCodes getPromoCodeByCode(String promoCode) {
        String query = "SELECT promo_code, discount_percentage, valid_from, valid_to FROM PromoCodes WHERE promo_code = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, promoCode);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String code = rs.getString("promo_code");
                    float discount = rs.getFloat("discount_percentage");
                    Date validFrom = rs.getDate("valid_from");
                    Date validTo = rs.getDate("valid_to");
                    return new PromoCodes(code, discount, validFrom, validTo);
                } else {
                    System.err.println("Promo code not found or invalid.");
                    return null;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching promo code: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
