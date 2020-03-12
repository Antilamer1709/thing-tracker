package com.antilamer.thingTracker.repository;

import com.antilamer.thingTracker.domain.ExpenseTypeDictEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExpenseTypeDictRepo extends JpaRepository<ExpenseTypeDictEntity, Integer> {

    Optional<ExpenseTypeDictEntity> findByNameIgnoreCase(String name);

    List<ExpenseTypeDictEntity> findTop5ByNameContainingIgnoreCase(String name);

}
