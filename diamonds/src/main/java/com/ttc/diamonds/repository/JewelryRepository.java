package com.ttc.diamonds.repository;

import com.ttc.diamonds.model.Jewelry;
import com.ttc.diamonds.model.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JewelryRepository extends JpaRepository<Jewelry, Long> {

    List<Jewelry> findByManufacturer(Manufacturer manufacturer);
    Jewelry findByBarcode(String barcode);
}
