package com.ttc.diamonds.repository;

import com.ttc.diamonds.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {

    List<Store> findByState(String state);
}
