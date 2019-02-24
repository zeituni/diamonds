package com.ttc.diamonds.dto;

import java.util.Objects;

public class StoreStatistics extends StatisticsRow {

    private StoreDTO store;


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

}
