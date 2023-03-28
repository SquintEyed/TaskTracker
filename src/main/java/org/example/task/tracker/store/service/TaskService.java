package org.example.task.tracker.store.service;

import org.example.task.tracker.store.entity.TaskEntity;

public interface TaskService {

    TaskEntity saveAndFlush(TaskEntity task);

    void delete(TaskEntity task);

    void deleteById(Long taskId);
}
