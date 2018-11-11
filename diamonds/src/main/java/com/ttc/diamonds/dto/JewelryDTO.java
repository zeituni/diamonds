package com.ttc.diamonds.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JewelryDTO {
    private long id;
    private String barcode;
    private String additionalInfo;
    @JsonIgnore
    private Date creationDate;
    private Long manufacturer;
    private String videoLink;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Long getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Long manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JewelryDTO that = (JewelryDTO) o;
        return id == that.id &&
                Objects.equals(barcode, that.barcode) &&
                Objects.equals(videoLink, that.videoLink) &&
                Objects.equals(additionalInfo, that.additionalInfo) &&
                Objects.equals(creationDate, that.creationDate) &&
                Objects.equals(manufacturer, that.manufacturer);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, barcode, videoLink, additionalInfo, creationDate, manufacturer);
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public String getVideoLink() {
        return videoLink;
    }
}
