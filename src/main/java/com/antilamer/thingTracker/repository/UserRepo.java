package com.antilamer.thingTracker.repository;

import com.antilamer.thingTracker.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepo extends JpaRepository<UserEntity, Integer> {

    UserEntity findByEmailIgnoreCase(String email);

    List<UserEntity> findTop5ByFullNameContainingIgnoreCase(String fullName);

}
