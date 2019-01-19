package com.ttc.diamonds.repository;

import com.ttc.diamonds.model.Customer;
import com.ttc.diamonds.model.Manufacturer;
import com.ttc.diamonds.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByManufacturer(Manufacturer manufacturer);

    List<Customer> findByStore(Store store);

    List<Customer> findByBarcode(String barcode);
}
