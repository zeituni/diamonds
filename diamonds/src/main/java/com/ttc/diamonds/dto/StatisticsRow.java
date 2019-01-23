package com.ttc.diamonds.dto;

import java.util.Objects;

public class StatisticsRow {

    private UserDTO user;
    private JewelryDTO jewelryDTO;
    private String day;
    private String hour;
    private int total;

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public JewelryDTO getJewelryDTO() {
        return jewelryDTO;
    }

    public void setJewelryDTO(JewelryDTO jewelryDTO) {
        this.jewelryDTO = jewelryDTO;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StatisticsRow)) return false;
        StatisticsRow that = (StatisticsRow) o;
        return getTotal() == that.getTotal() &&
                Objects.equals(getUser(), that.getUser()) &&
                Objects.equals(getJewelryDTO(), that.getJewelryDTO()) &&
                Objects.equals(getDay(), that.getDay()) &&
                Objects.equals(getHour(), that.getHour());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUser(), getJewelryDTO(), getDay(), getHour(), getTotal());
    }
}
