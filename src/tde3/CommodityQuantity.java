package tde3;

import java.io.Serializable;

public class CommodityQuantity implements Serializable {

    private String commodity;
    private Float quantity;

    public CommodityQuantity() {
    }

    public CommodityQuantity(String commodity, Float quantity) {
        this.commodity = commodity;
        this.quantity = quantity;
    }

    public String getCommodity() {
        return commodity;
    }

    public void setCommodity(String commodity) {
        this.commodity = commodity;
    }

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return commodity + "," + quantity;
    }

}
