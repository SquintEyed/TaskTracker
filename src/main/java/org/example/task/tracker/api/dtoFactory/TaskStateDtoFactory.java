package org.example.task.tracker.api.dtoFactory;

import org.example.task.tracker.api.dto.TaskStateDto;
import org.example.task.tracker.store.entity.TaskStateEntity;
import org.springframework.stereotype.Component;

@Component
public class TaskStateDtoFactory {

    public TaskStateDto makeTaskStateDto (TaskStateEntity entity){

        return TaskStateDto.builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .ordinal(entity.getOrdinal())
                    .createdAt(entity.getCreatedAt())
                    .build();
    }
}
