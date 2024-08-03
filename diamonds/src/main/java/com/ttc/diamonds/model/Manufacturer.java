package com.ttc.diamonds.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="manufacturer")
public class Manufacturer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @Column(name = "referenced_manufacturer")
    private Long referencedManufacturer;

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

    public Long getReferencedManufacturer() {
        return referencedManufacturer;
    }

    public void setReferencedManufacturer(long referencedManufacturer) {
        this.referencedManufacturer = referencedManufacturer;
    }
}
