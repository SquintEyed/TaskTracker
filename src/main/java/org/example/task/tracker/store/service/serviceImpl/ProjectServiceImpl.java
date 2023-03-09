package org.example.task.tracker.store.service.serviceImpl;

import org.example.task.tracker.store.repository.ProjectRepository;
import org.example.task.tracker.store.service.ProjectService;
import org.springframework.stereotype.Service;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository){
        this.projectRepository = projectRepository;
    }
}
