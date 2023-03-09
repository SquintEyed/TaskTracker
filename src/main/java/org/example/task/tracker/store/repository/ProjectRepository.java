package org.example.task.tracker.store.repository;

import org.example.task.tracker.store.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

}
