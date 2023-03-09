package org.example.task.tracker.store.service.serviceImpl;

import org.example.task.tracker.store.repository.TaskRepository;
import org.example.task.tracker.store.service.TaskService;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }
}
