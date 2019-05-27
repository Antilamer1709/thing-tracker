package com.antilamer.thingTracker.repository;

import com.antilamer.thingTracker.model.UserEntity;
import com.antilamer.thingTracker.model.UserInviteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserInviteRepo extends JpaRepository<UserInviteEntity, Integer> {

    List<UserInviteEntity> findAllByTarget(UserEntity target);

    Optional<UserInviteEntity> findAllByInviterAndTarget(UserEntity inviter, UserEntity target);

}
