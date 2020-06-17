package tde3;

import java.io.Serializable;

public class AvgWeight implements Serializable {

    private Float weight;
    private Integer qtd;

    public AvgWeight() {
    }

    public AvgWeight(Float weight) {
        this.weight = weight;
        this.qtd = 1;
    }

    public AvgWeight(Float weight, Integer qtd) {
        this.weight = weight;
        this.qtd = qtd;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Integer getQtd() {
        return qtd;
    }

    public void setQtd(Integer qtd) {
        this.qtd = qtd;
    }

    @Override
    public String toString() {
        return String.valueOf(weight / qtd);
    }
}
