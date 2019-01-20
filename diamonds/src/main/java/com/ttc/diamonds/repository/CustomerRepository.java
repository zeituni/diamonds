package com.ttc.diamonds.repository;

import com.ttc.diamonds.model.Customer;
import com.ttc.diamonds.model.Manufacturer;
import com.ttc.diamonds.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByManufacturer(Manufacturer manufacturer);

    List<Customer> findByStore(Store store);

    @Query(value = "select c.* from customer c inner join jewelry j on c.jewelry = j.id where j.barcode = :barcode", nativeQuery = true)
    List<Customer> findByBarcode(@Param("barcode") String barcode);
}
