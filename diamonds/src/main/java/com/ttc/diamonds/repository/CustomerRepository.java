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

    @Query(value = "select c.* from customer c inner join user u on c.user = u.id where u.store = :store", nativeQuery = true)
    List<Customer> findByStore(@Param("store") Long store);

    @Query(value = "select c.* from customer c inner join jewelry j on c.jewelry = j.id where j.barcode = :barcode", nativeQuery = true)
    List<Customer> findByBarcode(@Param("barcode") String barcode);

    @Query(value = "select c.* from customer c where c.manufacturer = :manufacturer order by creation_date desc", nativeQuery = true)
    List<Customer> getByManufacturerDateDesc(@Param("manufacturer") Long manufacturer );
}
