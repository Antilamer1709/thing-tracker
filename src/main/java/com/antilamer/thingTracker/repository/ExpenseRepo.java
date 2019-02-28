package com.antilamer.thingTracker.repository;

import com.antilamer.thingTracker.model.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepo extends JpaRepository<ExpenseEntity, Integer> {


}
