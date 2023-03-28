package org.example.task.tracker.api.controller;

import org.example.task.tracker.api.controller.helpUtil.ControllerHelper;
import org.example.task.tracker.api.dto.AskDto;
import org.example.task.tracker.api.dto.TaskDto;
import org.example.task.tracker.api.dtoFactory.TaskDtoFactory;
import org.example.task.tracker.api.exception.BadRequestException;
import org.example.task.tracker.store.entity.TaskEntity;
import org.example.task.tracker.store.entity.TaskStateEntity;
import org.example.task.tracker.store.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
public class TaskController {

    private final TaskService taskService;
    private final TaskDtoFactory taskDtoFactory;
    private final ControllerHelper controllerHelper;


    private final String CREATE_TASK = "/api/projects/task-states/{task_state_id}/tasks";
    private final String FIND_ALL_TASKS = "/api/projects/task-states/{task_state_id}/tasks";
    private final String UPDATE_TASK = "/api/projects/task-states/{task_state_id}/tasks/{task_id}";
    private final String DELETE_TASK = "/api/projects/task-states/{task_state_id}/tasks/{task_id}";
    private final String DISPLACE_TASK_BETWEEN_TASK_STATES = "/api/projects/task-states/{task_state_id}/tasks/{task_id}";

    public TaskController(TaskService taskService,
                          TaskDtoFactory taskDtoFactory,
                          ControllerHelper controllerHelper) {

        this.taskService = taskService;
        this.taskDtoFactory = taskDtoFactory;
        this.controllerHelper = controllerHelper;
    }

    @GetMapping(FIND_ALL_TASKS)
    public List<TaskDto> getAllTasks(@PathVariable(name = "task_state_id") Long taskStateId) {

        TaskStateEntity taskState = controllerHelper.getTaskStateEntityByIdOrThrowException(taskStateId);

        return taskState
                .getTasks()
                .stream()
                .map(task -> taskDtoFactory.makeTaskDto(task))
                .collect(Collectors.toList());
    }

    @PostMapping(CREATE_TASK)
    public TaskDto createNewTask(@PathVariable(name = "task_state_id") Long taskStateId,
                                 @RequestParam(name = "task_name") String taskName,
                                 @RequestParam(name = "task_description") String taskDescription) {

        TaskStateEntity taskState = controllerHelper.getTaskStateEntityByIdOrThrowException(taskStateId);

        if (taskName.trim().isEmpty()) {

            throw new BadRequestException("Creating task name cannot be empty");
        }

        taskState
                .getTasks()
                .stream()
                .filter(task -> task.getName().equalsIgnoreCase(taskName))
                .findAny()
                .ifPresent(task -> {
                    throw new BadRequestException(String.format("Task with name %s already exist in project",taskName));
                });

        TaskEntity task = taskService
                .saveAndFlush(TaskEntity
                        .builder()
                        .taskStateEntity(taskState)
                        .name(taskName)
                        .description(taskDescription)
                        .build());

        return taskDtoFactory.makeTaskDto(task);
    }

    @PatchMapping(UPDATE_TASK)
    public TaskDto updateTask(@PathVariable(name = "task_state_id") Long taskStateId,
                              @PathVariable(name = "task_id") Long taskId,
                              @RequestParam(name = "new_task_name", required = false) String newTaskName,
                              @RequestParam(name = "new_description", required = false) String newDescription) {

        TaskStateEntity taskStateEntity = controllerHelper.getTaskStateEntityByIdOrThrowException(taskStateId);

        TaskEntity taskEntity = controllerHelper.getTaskEntityOrThrowException(taskStateEntity, taskId);

        if (!Objects.isNull(newTaskName)) {

            taskStateEntity
                    .getTasks()
                    .stream()
                    .filter(task -> task.getName().equalsIgnoreCase(newTaskName))
                    .findAny()
                    .ifPresent(task -> {
                        throw new BadRequestException(String.format("Task with name %s already exist in project, change another new name", newTaskName));
                    });

            if (!newTaskName.trim().isEmpty()) {

                taskEntity.setName(newTaskName);
            }
        }

        if (!Objects.isNull(newDescription)) {

                taskEntity.setDescription(newDescription);
        }

        taskService.saveAndFlush(taskEntity);

        return taskDtoFactory.makeTaskDto(taskEntity);
    }

    @PutMapping(DISPLACE_TASK_BETWEEN_TASK_STATES)
    public TaskDto displaceTask(@PathVariable(name = "task_state_id") Long oldTaskStateId,
                                @PathVariable(name = "task_id") Long taskId,
                                @RequestParam(name = "new_task_state_id") Long newTaskStateId) {

        TaskStateEntity oldTaskState = controllerHelper.getTaskStateEntityByIdOrThrowException(oldTaskStateId);
        TaskStateEntity newTaskState = controllerHelper.getTaskStateEntityByIdOrThrowException(newTaskStateId);

        if (oldTaskState.getProjectEntity().getId() != newTaskState.getProjectEntity().getId()) {

            throw new BadRequestException("Task can be moved within the same project");
        }

        TaskEntity taskEntity = controllerHelper.getTaskEntityOrThrowException(oldTaskState, taskId);

        taskEntity.setTaskStateEntity(newTaskState);

        taskService.saveAndFlush(taskEntity);

        return taskDtoFactory.makeTaskDto(taskEntity);
    }

    @DeleteMapping(DELETE_TASK)
    public AskDto deleteTask(@PathVariable(name = "task_state_id") Long taskStatesId,
                             @PathVariable(name = "task_id") Long taskId) {

        TaskStateEntity taskStateEntity = controllerHelper.getTaskStateEntityByIdOrThrowException(taskStatesId);

        TaskEntity taskEntity = controllerHelper.getTaskEntityOrThrowException(taskStateEntity, taskId);

        taskService.delete(taskEntity);

        return AskDto
                .builder()
                .askMessage("Task with id = " + taskId + " successfully deleted")
                .build();
    }
}
