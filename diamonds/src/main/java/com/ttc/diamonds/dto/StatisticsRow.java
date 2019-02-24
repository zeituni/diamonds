package com.ttc.diamonds.dto;

public class StatisticsRow {

    protected JewelryDTO jewelryDTO;
    protected String day;
    protected int total;

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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
