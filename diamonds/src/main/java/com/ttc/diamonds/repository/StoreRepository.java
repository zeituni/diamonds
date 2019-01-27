package com.ttc.diamonds.repository;

import com.ttc.diamonds.model.Manufacturer;
import com.ttc.diamonds.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {

    List<Store> findByStateAndManufacturer(String state, Manufacturer manufacturer);

    List<Store> findByManufacturer(Manufacturer manufacturer);

    Store findByManufacturerAndName(Manufacturer manufacturer, String name);
}
