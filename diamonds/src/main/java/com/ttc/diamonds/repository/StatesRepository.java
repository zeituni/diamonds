package com.ttc.diamonds.repository;

import com.ttc.diamonds.model.State;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatesRepository extends JpaRepository<State, Integer> {
}
