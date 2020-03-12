package com.antilamer.thingTracker.repository;

import com.antilamer.thingTracker.domain.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepo extends JpaRepository<GroupEntity, Integer> {

    List<GroupEntity> findAllByName(String name);
}
