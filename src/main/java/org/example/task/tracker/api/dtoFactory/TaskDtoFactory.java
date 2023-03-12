package org.example.task.tracker.api.dtoFactory;

import org.example.task.tracker.api.dto.TaskDto;
import org.example.task.tracker.store.entity.TaskEntity;
import org.springframework.stereotype.Component;

@Component
public class TaskDtoFactory {

    public TaskDto makeTaskDto (TaskEntity entity){

        return TaskDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .description(entity.getDescription())
                .build();
    }
}
