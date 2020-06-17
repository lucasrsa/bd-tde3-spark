package tde3;

import java.io.Serializable;

public class PriceWeight implements Serializable {

    private Float price;
    private Float weight;

    public PriceWeight() {
    }

    public PriceWeight(Float price, Float weight) {
        this.price = price;
        this.weight = weight;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return String.valueOf(price/weight);
    }
}
