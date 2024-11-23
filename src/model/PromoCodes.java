package model;

import java.util.Date;

public class PromoCodes {
    private String promocode;
    private float discountPercentage;
    private Date validFrom;
    private Date validTo;

    public PromoCodes(String promocode, float discountPercentage, Date validFrom, Date validTo) {
        this.promocode = promocode;
        this.discountPercentage = discountPercentage;
        this.validFrom = validFrom;
        this.validTo = validTo;
    }

    public String getPromocode() {
        return promocode;
    }

    public void setPromocode(String promocode) {
        this.promocode = promocode;
    }

    public float getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(float discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    public Date getValidTo() {
        return validTo;
    }

    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }

    @Override
    public String toString() {
        return "PromoCodes{" +
                "promocode='" + promocode + '\'' +
                ", discountPercentage=" + discountPercentage +
                ", validFrom=" + validFrom +
                ", validTo=" + validTo +
                '}';
    }
}
