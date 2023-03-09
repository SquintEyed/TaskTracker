package org.example.task.tracker.store.service.serviceImpl;

import org.example.task.tracker.store.repository.TaskStateRepository;
import org.example.task.tracker.store.service.TaskStateService;
import org.springframework.stereotype.Service;

@Service
public class TaskStateServiceImpl implements TaskStateService {

    private final TaskStateRepository taskStateRepository;

    public TaskStateServiceImpl(TaskStateRepository taskStateRepository){
        this.taskStateRepository = taskStateRepository;
    }
}
