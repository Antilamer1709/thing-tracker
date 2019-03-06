package com.antilamer.thingTracker.repository;

import com.antilamer.thingTracker.model.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ExpenseRepo extends JpaRepository<ExpenseEntity, Integer> {

    @Query(value = "SELECT e FROM ExpenseEntity e " +
            "JOIN e.user u " +
            "WHERE u.id = :userId AND e.date BETWEEN :dateFrom AND :dateTo")
    List<ExpenseEntity> searchChart(@Param("userId") Integer userId, @Param("dateFrom") LocalDateTime dateFrom, @Param("dateTo") LocalDateTime dateTo);

}
