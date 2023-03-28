package org.example.task.tracker.store.repository;

import org.example.task.tracker.store.entity.TaskEntity;
import org.example.task.tracker.store.entity.TaskStateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    TaskEntity saveAndFlush(TaskEntity task);

    void deleteById(Long taskId);

    void delete(TaskEntity task);
}
