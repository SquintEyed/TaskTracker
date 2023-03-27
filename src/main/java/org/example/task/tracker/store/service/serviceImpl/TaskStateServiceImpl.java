package org.example.task.tracker.store.service.serviceImpl;

import org.example.task.tracker.store.entity.TaskStateEntity;
import org.example.task.tracker.store.repository.TaskStateRepository;
import org.example.task.tracker.store.service.TaskStateService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaskStateServiceImpl implements TaskStateService {

    private final TaskStateRepository taskStateRepository;

    public TaskStateServiceImpl(TaskStateRepository taskStateRepository){
        this.taskStateRepository = taskStateRepository;
    }

    @Override
    public TaskStateEntity saveAndFlush(TaskStateEntity entity) {
        return taskStateRepository.saveAndFlush(entity);
    }

    @Override
    public Optional<TaskStateEntity> findTaskStateEntityByRightTaskStateIdIsNullAndProjectEntityId(Long projectId) {
        return taskStateRepository.findTaskStateEntityByRightTaskStateIdIsNullAndProjectEntityId(projectId);
    }

    @Override
    public Optional<TaskStateEntity> findById(Long taskStateId) {
        return taskStateRepository.findById(taskStateId);
    }

    @Override
    public void deleteById(Long taskStateId) {
        taskStateRepository.deleteById(taskStateId);
    }

    @Override
    public Optional<TaskStateEntity> findByNameAndProjectEntityId(String taskStateName, Long projectId) {
        return taskStateRepository.findByNameAndProjectEntityId(taskStateName, projectId);
    }
}
