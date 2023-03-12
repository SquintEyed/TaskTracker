package org.example.task.tracker.api.dtoFactory;

import org.example.task.tracker.api.dto.ProjectDto;
import org.example.task.tracker.store.entity.ProjectEntity;
import org.springframework.stereotype.Component;

@Component
public class ProjectDtoFactory {

    public ProjectDto makeProjectDto (ProjectEntity entity) {

      return  ProjectDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .updateAt(entity.getUpdatedAt())
                .build();
    }
}
