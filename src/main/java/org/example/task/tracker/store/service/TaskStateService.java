package org.example.task.tracker.store.service;

import org.example.task.tracker.store.entity.TaskStateEntity;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TaskStateService {

    Optional<TaskStateEntity> findTaskStateEntityByRightTaskStateIdIsNullAndProjectEntityId(@Param("projectId") Long projectId);

    TaskStateEntity saveAndFlush(TaskStateEntity entity);

    Optional<TaskStateEntity> findById(Long taskStateId);

    Optional<TaskStateEntity> findByNameAndProjectEntityId(String taskStateName, Long projectId);

    void deleteById(Long taskStateId);
}
