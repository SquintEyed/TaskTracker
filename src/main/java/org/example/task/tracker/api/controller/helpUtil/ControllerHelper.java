package org.example.task.tracker.api.controller.helpUtil;

import org.example.task.tracker.api.exception.BadRequestException;
import org.example.task.tracker.api.exception.NotFoundException;
import org.example.task.tracker.store.entity.ProjectEntity;
import org.example.task.tracker.store.service.ProjectService;
import org.springframework.stereotype.Component;


@Component
public class ControllerHelper {

    private final ProjectService projectService;

    public ControllerHelper(ProjectService projectService) {
        this.projectService = projectService;
    }

    public ProjectEntity getProjectEntityOrThrowException(Long projectId) {

       return projectService
               .findById(projectId)
               .orElseThrow(() -> new NotFoundException("Project with id = " + projectId + ", not found"));
    }

}
