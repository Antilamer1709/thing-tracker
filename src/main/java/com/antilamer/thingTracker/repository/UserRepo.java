package com.antilamer.thingTracker.repository;

import com.antilamer.thingTracker.model.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepo extends JpaRepository<UserEntity, Integer> {

    UserEntity findByEmailIgnoreCase(String email);

    @Query(value = "SELECT u FROM UserEntity u " +
            "WHERE lower(u.fullName) LIKE lower(concat('%', :name,'%')) OR " +
            "lower(u.email) LIKE lower(concat('%', :name,'%'))")
    List<UserEntity> findTop5ByFullNameOrUsername(Pageable pageable, @Param("name") String name);

}
