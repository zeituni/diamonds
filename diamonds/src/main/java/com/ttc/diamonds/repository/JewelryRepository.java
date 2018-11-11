package com.ttc.diamonds.repository;

import com.ttc.diamonds.model.Jewelry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JewelryRepository extends JpaRepository<Jewelry, Long> {

    List<Jewelry> findByManufacturer(Long manufacturerId);
    Jewelry findByBarcode(String barcode);
}
