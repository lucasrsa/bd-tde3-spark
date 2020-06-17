package tde3;

import java.io.Serializable;
import java.util.Objects;

public class YearCommodity implements Serializable {

    private String year;
    private String commodity;

    public YearCommodity() {
    }

    public YearCommodity(String year, String commodity) {
        this.year = year;
        this.commodity = commodity;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getCommodity() {
        return commodity;
    }

    public void setCommodity(String commodity) {
        this.commodity = commodity;
    }

    @Override
    public String toString() {
        return year + ',' + commodity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        YearCommodity that = (YearCommodity) o;
        return year.equals(that.getYear()) &&
                commodity.equals(that.getCommodity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, commodity);
    }
}
