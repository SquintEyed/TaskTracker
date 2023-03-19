package org.example.task.tracker.api.controller;

import org.example.task.tracker.api.controller.helpUtil.ControllerHelper;
import org.example.task.tracker.api.dto.TaskStateDto;
import org.example.task.tracker.api.dtoFactory.TaskStateDtoFactory;
import org.example.task.tracker.api.exception.BadRequestException;
import org.example.task.tracker.store.entity.ProjectEntity;
import org.example.task.tracker.store.entity.TaskStateEntity;
import org.example.task.tracker.store.service.TaskStateService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/projects/")
public class TaskStateController {

    private final TaskStateService taskStateService;
    private final ControllerHelper controllerHelper;
    private final TaskStateDtoFactory taskStateDtoFactory;

    private final String GET_TASK_STATES = "{project_id}/task-states";
    private final String CREATE_TASK_STATE = "{project_id}/task-states";
    private final String UPDATE_TASK_STATE = "task-states/{task_state_id}";
    private final String CHANGE_TASK_STATE_POSITION = "task-states/changed/{task_state_id}";

    public TaskStateController(TaskStateService taskStateService,
                               ControllerHelper controllerHelper,
                               TaskStateDtoFactory taskStateDtoFactory) {

        this.taskStateService = taskStateService;
        this.controllerHelper = controllerHelper;
        this.taskStateDtoFactory = taskStateDtoFactory;
    }

    @GetMapping(GET_TASK_STATES)
    public List<TaskStateDto> getTaskStates(@PathVariable(name = "project_id") Long projectId) {

        ProjectEntity project = controllerHelper.getProjectEntityOrThrowException(projectId);

        return project
                .getTaskStates()
                .stream()
                .map(taskStateDtoFactory::makeTaskStateDto)
                .collect(Collectors.toList());
    }

    @PostMapping(CREATE_TASK_STATE)
    public TaskStateDto createTaskState(@PathVariable(name = "project_id") Long projectId,
                                        @RequestParam(name = "task_state_name") String taskStateName) {

        if(taskStateName.trim().isEmpty()) {
            throw new BadRequestException("Task State Name can't be empty");
        }

        ProjectEntity project = controllerHelper.getProjectEntityOrThrowException(projectId);

        Optional<TaskStateEntity> optionalTaskState = Optional.empty();

        for(TaskStateEntity taskStateEntity : project.getTaskStates()) {

            if(taskStateEntity.getName().equalsIgnoreCase(taskStateName)) {
                throw new BadRequestException(
                        String.format("Task state with name %S already exist in project", taskStateName)
                );
            }

            if(!taskStateEntity.getRightTaskState().isPresent()) {
                optionalTaskState = Optional.of(taskStateEntity);
            }
        }

        TaskStateEntity taskState = TaskStateEntity
                .builder()
                .projectEntity(project)
                .name(taskStateName).build();

        taskStateService.saveAndFlush(taskState);

        optionalTaskState
                .ifPresent(findTaskState -> {
                    taskState.setLeftTaskState(findTaskState);
                    findTaskState.setRightTaskState(taskState);
                    taskStateService.saveAndFlush(findTaskState);
                    taskStateService.saveAndFlush(taskState);
                });

        return taskStateDtoFactory.makeTaskStateDto(taskState);
    }

    @PatchMapping(UPDATE_TASK_STATE)
    public TaskStateDto updateTaskState(@PathVariable(name = "task_state_id") Long taskStateId,
                                        @RequestParam(name = "task_state_name") String taskStateName) {

        if(taskStateName.trim().isEmpty()) {
            throw new BadRequestException("TaskStateName doesn't be empty");
        }

        TaskStateEntity taskStateEntity = controllerHelper.getTaskStateEntityByIdOrThrowException(taskStateId);

        taskStateService
                .findByNameAndProjectEntityId(taskStateName, taskStateEntity.getProjectEntity().getId())
                .ifPresent(optionalTaskState -> {
                            throw new BadRequestException(String.format("TaskState with name %s already exist in project", taskStateName));
                        });
        taskStateEntity.setName(taskStateName);
        taskStateService.saveAndFlush(taskStateEntity);

        return taskStateDtoFactory.makeTaskStateDto(taskStateEntity);
    }

    @PatchMapping(CHANGE_TASK_STATE_POSITION)
    public TaskStateDto changeTaskStatePosition(@PathVariable(name = "task_state_id") Long taskStateId,
                                                @RequestParam(name = "left_task_state_id", required = false) Long leftTaskStateId) {

        TaskStateEntity changedTaskState = controllerHelper.getTaskStateEntityByIdOrThrowException(taskStateId);

        Optional<Long> optionalLeftTaskStateId = Optional.of(leftTaskStateId);

        ProjectEntity project = changedTaskState.getProjectEntity();

        Long oldLeftTaskStateId = changedTaskState
                .getLeftTaskState()
                .map(TaskStateEntity::getId)
                .orElse(null);

        if (oldLeftTaskStateId == leftTaskStateId) {
            return taskStateDtoFactory.makeTaskStateDto(changedTaskState);
        }

        Optional<TaskStateEntity> optionalNewLeftTaskState = optionalLeftTaskStateId
                .map(existingLeftTaskStateId -> {

                    if (existingLeftTaskStateId == taskStateId) {
                        throw new BadRequestException("Task state id and left task state id can't be equals");
                    }

                    TaskStateEntity leftTaskStateEntity = controllerHelper
                            .getTaskStateEntityByIdOrThrowException(existingLeftTaskStateId);

                    if (project.getId() != leftTaskStateEntity.getProjectEntity().getId()) {
                        throw new BadRequestException("Changed task state and left task state not task states with in some project");
                    }

                    return leftTaskStateEntity;
                });

        Optional<TaskStateEntity> optionalNewRightTaskState;

        if (!optionalNewLeftTaskState.isPresent()) {

            optionalNewRightTaskState = project
                    .getTaskStates()
                    .stream()
                    .filter(taskState -> !taskState.getLeftTaskState().isPresent())
                    .findAny();
        }

        else {

            optionalNewRightTaskState = optionalNewLeftTaskState
                    .get()
                    .getRightTaskState();
        }

        Optional<TaskStateEntity> optionalOldLeftTaskState = changedTaskState.getLeftTaskState();
        Optional<TaskStateEntity> optionalOldRightTaskState = changedTaskState.getRightTaskState();

        optionalOldLeftTaskState
                .ifPresent(it -> {

                    it.setRightTaskState(optionalOldRightTaskState.orElse(null));

                    taskStateService.saveAndFlush(it);
                });

        optionalOldRightTaskState
                .ifPresent(it-> {

                    it.setLeftTaskState(optionalOldLeftTaskState.orElse(null));

                    taskStateService.saveAndFlush(it);
                });

        if (optionalNewLeftTaskState.isPresent()) {

            TaskStateEntity newLeftTaskState = optionalNewLeftTaskState.get();

            newLeftTaskState.setRightTaskState(changedTaskState);

            changedTaskState.setLeftTaskState(newLeftTaskState);

            taskStateService.saveAndFlush(newLeftTaskState);
        }

        else {
            changedTaskState.setLeftTaskState(null);
        }

        if (optionalNewRightTaskState.isPresent()) {

            TaskStateEntity newRightTaskState = optionalNewRightTaskState.get();

            newRightTaskState.setLeftTaskState(changedTaskState);

            changedTaskState.setRightTaskState(newRightTaskState);

            taskStateService.saveAndFlush(newRightTaskState);
        }

        else {
            changedTaskState.setRightTaskState(null);
        }

        taskStateService.saveAndFlush(changedTaskState);

        return taskStateDtoFactory.makeTaskStateDto(changedTaskState);
    }
}

