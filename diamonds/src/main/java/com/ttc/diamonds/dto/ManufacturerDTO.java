package com.ttc.diamonds.dto;

import java.util.Objects;

public class ManufacturerDTO {
    private long id;
    private String name;

    public long getReferencedManufacturer() {
        return referencedManufacturer;
    }

    public void setReferencedManufacturer(long referencedManufacturer) {
        this.referencedManufacturer = referencedManufacturer;
    }

    private long referencedManufacturer;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ManufacturerDTO that = (ManufacturerDTO) o;
        return id == that.id &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, referencedManufacturer);
    }
}
