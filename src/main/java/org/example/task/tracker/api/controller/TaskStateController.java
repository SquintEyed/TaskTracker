package org.example.task.tracker.api.controller;

import org.example.task.tracker.api.controller.helpUtil.ControllerHelper;
import org.example.task.tracker.api.dto.TaskStateDto;
import org.example.task.tracker.api.dtoFactory.TaskStateDtoFactory;
import org.example.task.tracker.store.entity.ProjectEntity;
import org.example.task.tracker.store.service.TaskStateService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/projects/")
public class TaskStateController {

    private final TaskStateService taskStateService;
    private final ControllerHelper controllerHelper;
    private final TaskStateDtoFactory taskStateDtoFactory;

    private final String GET_ALL_TASK_STATES = "{project_id}/tasks-states";

    public TaskStateController(TaskStateService taskStateService,
                               ControllerHelper controllerHelper,
                               TaskStateDtoFactory taskStateDtoFactory) {
        this.taskStateService = taskStateService;
        this.controllerHelper = controllerHelper;
        this.taskStateDtoFactory = taskStateDtoFactory;
    }

    @GetMapping(GET_ALL_TASK_STATES)
    public List<TaskStateDto> getAllTaskStates(@PathVariable(name = "project_id") Long projectId) {

        ProjectEntity project = controllerHelper.getProjectEntityOrThrowException(projectId);

        return project
                .getTaskStates()
                .stream()
                .map(taskStateDtoFactory::makeTaskStateDto)
                .collect(Collectors.toList());
    }
}
