package org.example.task.tracker.store.repository;

import org.example.task.tracker.store.entity.TaskStateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TaskStateRepository extends JpaRepository<TaskStateEntity, Long> {

    @Query(
            "SELECT t FROM TaskStateEntity t where t.rightTaskState.id = 0 AND t.projectEntity.id = :project_id"
    )

    Optional<TaskStateEntity> findTaskStateEntityByRightTaskStateIdIsNullAndProjectEntityId(@Param("project_id") Long projectId);

    TaskStateEntity saveAndFlush(TaskStateEntity entity);

    Optional<TaskStateEntity> findById(Long id);

    @Query(
            "SELECT taskState from TaskStateEntity taskState WHERE taskState.name = :task_state_name" +
                    " AND taskState.projectEntity.id = :project_id "
    )
    Optional<TaskStateEntity> findByNameAndProjectEntityId(@Param("task_state_name") String taskStateName,
                                                           @Param("project_id")Long projectId);

    void deleteById(Long taskStateId);
}
