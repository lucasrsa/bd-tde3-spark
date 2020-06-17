package tde3;

import java.io.Serializable;
import java.util.Objects;

public class YearFlux implements Serializable {

    private String ano;
    private String fluxo;

    public YearFlux() {
    }

    public YearFlux(String ano, String fluxo) {
        this.ano = ano;
        this.fluxo = fluxo;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getFluxo() {
        return fluxo;
    }

    public void setFluxo(String fluxo) {
        this.fluxo = fluxo;
    }

    @Override
    public String toString() {
        return ano + ',' + fluxo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        YearFlux yearFlux = (YearFlux) o;
        return ano.equals(yearFlux.getAno()) &&
                fluxo.equals(yearFlux.getFluxo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAno(), getFluxo());
    }
}
