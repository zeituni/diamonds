package com.ttc.diamonds.repository;

import com.ttc.diamonds.model.Customer;
import com.ttc.diamonds.model.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByManufacturer(Manufacturer manufacturer);
}
