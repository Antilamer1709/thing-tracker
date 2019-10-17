package com.antilamer.thingTracker.repository;

import com.antilamer.thingTracker.model.UserEntity;
import com.antilamer.thingTracker.model.UserMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserMessageRepo extends JpaRepository<UserMessageEntity, Integer> {

    List<UserMessageEntity> findAllByUser(UserEntity target);

}
