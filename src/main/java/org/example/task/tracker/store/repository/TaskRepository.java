package org.example.task.tracker.store.repository;

import org.example.task.tracker.store.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
}
