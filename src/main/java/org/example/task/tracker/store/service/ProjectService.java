package org.example.task.tracker.store.service;

import org.example.task.tracker.store.entity.ProjectEntity;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface ProjectService {

     Stream<ProjectEntity> streamAllByNameContainsIgnoreCase(@Param("prefix") String prefix);

     Optional<ProjectEntity> findByName(String projectName);

     ProjectEntity saveAndFlush(ProjectEntity entity);

     Optional<ProjectEntity> findById(Long id);

     List<ProjectEntity> findAll();

     void deleteById(Long id);
}
