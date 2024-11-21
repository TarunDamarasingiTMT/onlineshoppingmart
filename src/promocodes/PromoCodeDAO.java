package promocodes;

import java.sql.*;
import java.util.Date;

public class PromoCodeDAO implements PromoCodesInterface {

    private Connection connection;

    public PromoCodeDAO(Connection connection) {
        this.connection = connection;
    }


    public PromoCodes getPromoCodeByCode(String promoCode) {
        try {
            String query = "SELECT promoCode, discountPercentage, validFrom, validTo FROM PromoCodes WHERE promoCode = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, promoCode);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String code = rs.getString("promoCode");
                float discount = rs.getFloat("discountPercentage");
                Date validFrom = rs.getDate("validFrom");
                Date validTo = rs.getDate("validTo");

                return new PromoCodes(code, discount, validFrom, validTo);
            } else {
                System.out.println("Promo code not found or invalid.");
                return null;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


}

