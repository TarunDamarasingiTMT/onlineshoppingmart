package dao;

import model.PromoCodes;

public interface PromoCodesDao {

    PromoCodes getPromoCodeByCode(String promoCode);

}
