package com.ttc.diamonds.dto;

import java.util.Objects;

public class StoreDTO {
    private Long id;
    private String name;
    private String city;
    private String state;
    private Double latitude;
    private Double longitude;
    private int externalId;
    private String storeContact;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StoreDTO)) return false;
        StoreDTO storeDTO = (StoreDTO) o;
        return Objects.equals(getId(), storeDTO.getId()) &&
                Objects.equals(getName(), storeDTO.getName()) &&
                Objects.equals(getCity(), storeDTO.getCity()) &&
                Objects.equals(getState(), storeDTO.getState());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getCity(), getState());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getStoreContact() {
        return storeContact;
    }

    public void setStoreContact(String storeContact) {
        this.storeContact = storeContact;
    }

    public int getExternalId() {
        return externalId;
    }

    public void setExternalId(int externalId) {
        this.externalId = externalId;
    }
}
