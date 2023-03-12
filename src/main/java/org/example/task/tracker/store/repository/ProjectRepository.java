package org.example.task.tracker.store.repository;

import org.example.task.tracker.store.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

    Optional<ProjectEntity> findByName(String name);

    Optional<ProjectEntity> findById(Long id);

    List<ProjectEntity> findAll();

    void deleteById(Long id);

    @Query(
            "SELECT project FROM ProjectEntity project " +
                    "WHERE project.name " +
                    "LIKE LOWER(CONCAT('%',:prefix,'%')) " +
                    "ORDER BY project.name"
    )
    Stream<ProjectEntity> streamAllByNameContainsIgnoreCase(@Param("prefix") String prefix);
}
