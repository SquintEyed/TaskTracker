package org.example.task.tracker.store.service.serviceImpl;

import org.example.task.tracker.store.entity.ProjectEntity;
import org.example.task.tracker.store.repository.ProjectRepository;
import org.example.task.tracker.store.service.ProjectService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }


    @Override
    public Stream<ProjectEntity> streamAllByNameContainsIgnoreCase(String prefix) {
        return projectRepository.streamAllByNameContainsIgnoreCase(prefix);
    }

    @Override
    public Optional<ProjectEntity> findByName(String projectName) {
        return projectRepository.findByName(projectName);
    }

    @Override
    public ProjectEntity saveAndFlush(ProjectEntity entity) {
        return projectRepository.saveAndFlush(entity);
    }

    @Override
    public Optional<ProjectEntity> findById(Long id) {
        return projectRepository.findById(id);
    }

    @Override
    public List<ProjectEntity> findAll() {
        return projectRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        projectRepository.deleteById(id);
    }
}
