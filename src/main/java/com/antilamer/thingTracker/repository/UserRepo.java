package com.antilamer.thingTracker.repository;

import com.antilamer.thingTracker.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<UserEntity, Integer> {

    UserEntity findByEmailIgnoreCase(String email);

}
