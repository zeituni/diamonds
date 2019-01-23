package com.ttc.diamonds.repository;

import com.ttc.diamonds.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String userName);

    @Query(value = "select u.* from user u inner join store s on u.store = s.id where u.manufacturer = :manufacturerId and s.name = :storeName", nativeQuery =  true)
    List<User> getUsersByStore(@Param("manufacturerId") Long manufacturerId, @Param(value = "storeName") String storeName);

    @Query(value = "select * from user where manufacturer = :manufacturerId", nativeQuery = true)
    List<User> getAllManufacturerUsers(@Param(value = "manufacturerId") Long manufacturerId);

    @Query(value = "select u.* from user u inner join store s on u.store = s.id where u.manufacturer = :manufacturerId and s.state = :state and s.city = :city", nativeQuery =  true)
    List<User> getUsersByCity(@Param("manufacturerId") Long manufacturerId, @Param(value = "state") String state, @Param(value = "city") String city);

}
