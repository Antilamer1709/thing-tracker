package com.antilamer.thingTracker.repository;

import com.antilamer.thingTracker.model.UserInviteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInviteRepo extends JpaRepository<UserInviteEntity, Integer> {

}
