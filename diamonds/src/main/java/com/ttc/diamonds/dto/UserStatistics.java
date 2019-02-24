package com.ttc.diamonds.dto;

import java.util.Objects;

public class UserStatistics extends StatisticsRow {

    private UserDTO user;
    private String hour;

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }


    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserStatistics)) return false;
        UserStatistics that = (UserStatistics) o;
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
