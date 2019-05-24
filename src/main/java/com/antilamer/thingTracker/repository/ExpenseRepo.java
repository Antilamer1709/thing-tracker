package com.antilamer.thingTracker.repository;

import com.antilamer.thingTracker.dto.ExpenseSearchDTO;
import com.antilamer.thingTracker.model.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface ExpenseRepo extends JpaRepository<ExpenseEntity, Integer>, AbstractCustomRepo<ExpenseEntity, ExpenseSearchDTO> {

    @Query(value = "SELECT e FROM ExpenseEntity e " +
            "JOIN e.user u " +
            "WHERE u.id in :userIds AND e.date BETWEEN :dateFrom AND :dateTo")
    @Deprecated
    List<ExpenseEntity> searchChart(@Param("userIds") Set<Integer> userIds, @Param("dateFrom") LocalDateTime dateFrom, @Param("dateTo") LocalDateTime dateTo);

}
