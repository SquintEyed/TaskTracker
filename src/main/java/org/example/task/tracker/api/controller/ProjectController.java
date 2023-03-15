package org.example.task.tracker.api.controller;

import lombok.extern.log4j.Log4j2;
import org.example.task.tracker.api.controller.helpUtil.ControllerHelper;
import org.example.task.tracker.api.dto.AskDto;
import org.example.task.tracker.api.dto.ProjectDto;
import org.example.task.tracker.api.dtoFactory.ProjectDtoFactory;
import org.example.task.tracker.api.exception.BadRequestException;
import org.example.task.tracker.api.exception.NotFoundException;
import org.example.task.tracker.store.entity.ProjectEntity;
import org.example.task.tracker.store.service.ProjectService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Log4j2
@RestController
@RequestMapping("api/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final ControllerHelper controllerHelper;
    private final ProjectDtoFactory projectDtoFactory;

    private final String EDIT_PROJECT = "/{project_id}";
    private final String DELETE_PROJECT = "/{project_id}";


    public ProjectController(ProjectService projectService,
                             ControllerHelper controllerHelper,
                             ProjectDtoFactory projectDtoFactory) {

        this.projectService = projectService;
        this.controllerHelper = controllerHelper;
        this.projectDtoFactory = projectDtoFactory;
    }

    @GetMapping
    @Transactional
    public List<ProjectDto> fetchProjects(
            @RequestParam(value = "prefix_name", required = false) Optional<String> prefixName) {

        if(prefixName.filter(prefix -> !prefix.trim().isEmpty()).isPresent()) {
            return  projectService.streamAllByNameContainsIgnoreCase(prefixName.get())
                    .map(p -> projectDtoFactory.makeProjectDto(p))
                    .collect(Collectors.toList());
        }

        return projectService.findAll().stream()
                .map(project -> projectDtoFactory.makeProjectDto(project))
                .collect(Collectors.toList());
    }

    @PostMapping
    public ProjectDto createNewProject(@RequestParam(value = "name", required = true) String projectName) {

        projectService.findByName(projectName)
              .ifPresent(project -> {
                  throw new BadRequestException("Project with name:" + projectName + ", already exist");
              });

        ProjectEntity entity = ProjectEntity
                .builder()
                .name(projectName)
                .build();

        projectService.saveAndFlush(entity);

        log.info("New Project: " +  entity.getName() + ", successfully created.");

        return projectDtoFactory.makeProjectDto(entity);
    }

    @PatchMapping(EDIT_PROJECT)
    public ProjectDto editProject(@PathVariable(name = "project_id", required = true) Long projectId,
                                  @RequestParam(value = "name", required = true) String projectName) {

        ProjectEntity project = controllerHelper.getProjectEntityOrThrowException(projectId);

        projectService.findByName(projectName)
                .filter(anotherProject -> anotherProject.getId() != project.getId())
                .ifPresent(anotherProject -> {
                    throw new BadRequestException("Project with name:" + projectName + ", already exist");
                });

        log.info("Project " + project.getName() + " successfully change name to -> " + projectName);

        project.setName(projectName);
        project.setUpdatedAt(LocalDateTime.now());

        projectService.saveAndFlush(project);

        return projectDtoFactory.makeProjectDto(project);
    }

    @DeleteMapping(DELETE_PROJECT)
    public AskDto deleteProject(@PathVariable(value = "project_id", required = true) Long projectId) {

        controllerHelper.getProjectEntityOrThrowException(projectId);

        projectService.deleteById(projectId);

        log.info("Project with id = " + projectId + ", successfully deleted");

        return AskDto
                .builder()
                .askMessage("Project with id = " + projectId + ", successfully deleted")
                .build();
    }

    @PutMapping
    public ProjectDto createOrUpdateProject(@RequestParam(value = "project_id", required = false) Optional<Long> optionalProjectId,
                                            @RequestParam(value = "project_name", required = false) Optional<String> optionalProjectName) {

        optionalProjectName = optionalProjectName.filter(projectName -> !projectName.trim().isEmpty());

        boolean isCreate = !optionalProjectId.isPresent();

        if (isCreate && !optionalProjectName.isPresent()) {
            throw new BadRequestException("Project name can't be empty.");
        }

        final ProjectEntity project = optionalProjectId
                .map(controllerHelper::getProjectEntityOrThrowException)
                .orElseGet(() -> ProjectEntity.builder().build());

        optionalProjectName
                .ifPresent(projectName -> {

                    projectService
                            .findByName(projectName)
                            .filter(anotherProject -> !Objects.equals(anotherProject.getId(), project.getId()))
                            .ifPresent(anotherProject -> {
                                throw new BadRequestException(
                                        String.format("Project  %s already exists.", projectName)
                                );
                            });

                    project.setName(projectName);
                });

        final ProjectEntity savedProject = projectService.saveAndFlush(project);

        return projectDtoFactory.makeProjectDto(savedProject);
    }
}
