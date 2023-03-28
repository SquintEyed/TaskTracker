package org.example.task.tracker.api.controller.helpUtil;

import org.example.task.tracker.api.exception.BadRequestException;
import org.example.task.tracker.api.exception.NotFoundException;
import org.example.task.tracker.store.entity.ProjectEntity;
import org.example.task.tracker.store.entity.TaskEntity;
import org.example.task.tracker.store.entity.TaskStateEntity;
import org.example.task.tracker.store.service.ProjectService;
import org.example.task.tracker.store.service.TaskStateService;
import org.springframework.stereotype.Component;


@Component
public class ControllerHelper {

    private final ProjectService projectService;
    private final TaskStateService taskStateService;

    public ControllerHelper(ProjectService projectService, TaskStateService taskStateService) {
        this.projectService = projectService;
        this.taskStateService = taskStateService;
    }


    public void throwExceptionIfProjectAlreadyExist(String projectName) {

       projectService.findByName(projectName)
              .ifPresent(project -> {
                  throw new BadRequestException("Project with name:" + projectName + ", already exist");
              });
    }

    public ProjectEntity getProjectEntityOrThrowException(Long projectId) {

       return projectService
               .findById(projectId)
               .orElseThrow(() -> new NotFoundException("Project with id = " + projectId + ", not found"));
    }

    public TaskStateEntity getTaskStateEntityByIdOrThrowException(Long taskStateId) {

        return taskStateService
                .findById(taskStateId)
                .orElseThrow(() -> new NotFoundException(String.format("TaskStateEntity with id = \"%s\" not exist",taskStateId)));
    }

    public TaskEntity getTaskEntityOrThrowException(TaskStateEntity taskStateEntity, Long taskId) {

        return taskStateEntity
                .getTasks()
                .stream()
                .filter(task -> task.getId() == taskId)
                .findAny()
                .orElseThrow(() -> new BadRequestException(String.format("Task with id = %d does not exist in this task states", taskId)));
    }

}
