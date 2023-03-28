package org.example.task.tracker.store.service.serviceImpl;

import org.example.task.tracker.store.entity.TaskEntity;
import org.example.task.tracker.store.repository.TaskRepository;
import org.example.task.tracker.store.service.TaskService;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }


    @Override
    public TaskEntity saveAndFlush(TaskEntity task) {

      return  taskRepository.saveAndFlush(task);
    }

    @Override
    public void delete(TaskEntity task) {
        taskRepository.delete(task);
    }

    @Override
    public void deleteById(Long taskId) {
        taskRepository.deleteById(taskId);
    }
}
