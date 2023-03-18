package org.example.task.tracker.api.controller;

import org.example.task.tracker.api.controller.helpUtil.ControllerHelper;
import org.example.task.tracker.api.dto.TaskStateDto;
import org.example.task.tracker.api.dtoFactory.TaskStateDtoFactory;
import org.example.task.tracker.api.exception.BadRequestException;
import org.example.task.tracker.store.entity.ProjectEntity;
import org.example.task.tracker.store.entity.TaskStateEntity;
import org.example.task.tracker.store.service.ProjectService;
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
    private final String UPDATE_TASK_STATE = "{project_id}/task-states/{task-states_id}";

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
}
