package com.ttc.diamonds.dto;

import java.util.Objects;

public class StoreStatistics {

    private StoreDTO store;
    private String day;
    private int total;
    private JewelryDTO jewelryDTO;

    public JewelryDTO getJewelryDTO() {
        return jewelryDTO;
    }

    public void setJewelryDTO(JewelryDTO jewelryDTO) {
        this.jewelryDTO = jewelryDTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StoreStatistics)) return false;
        StoreStatistics that = (StoreStatistics) o;
        return getTotal() == that.getTotal() &&
                Objects.equals(getStore(), that.getStore()) &&
                Objects.equals(getDay(), that.getDay());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getStore(), getDay(), getTotal());
    }

    public StoreDTO getStore() {

        return store;
    }

    public void setStore(StoreDTO store) {
        this.store = store;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
