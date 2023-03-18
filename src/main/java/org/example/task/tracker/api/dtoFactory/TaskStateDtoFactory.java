package org.example.task.tracker.api.dtoFactory;

import lombok.RequiredArgsConstructor;
import org.example.task.tracker.api.dto.TaskStateDto;
import org.example.task.tracker.store.entity.TaskStateEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TaskStateDtoFactory {

    private final TaskDtoFactory taskDtoFactory;

    public TaskStateDto makeTaskStateDto (TaskStateEntity entity){

        return TaskStateDto.builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .rightTaskStateDtoId(entity.getRightTaskState().map(TaskStateEntity::getId).orElse(null))
                    .leftTaskStateDtoId(entity.getLeftTaskState().map(TaskStateEntity::getId).orElse(null))
                    .createdAt(entity.getCreatedAt())
                    .tasks(
                            entity
                                .getTasks()
                                .stream()
                                .map(taskDtoFactory::makeTaskDto)
                                .collect(Collectors.toList())
                    )
                    .build();
    }
}
