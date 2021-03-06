package com.antilamer.thingTracker.repository;

import com.antilamer.thingTracker.domain.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<RoleEntity, Integer> {

    RoleEntity findByCode(String code);

}
