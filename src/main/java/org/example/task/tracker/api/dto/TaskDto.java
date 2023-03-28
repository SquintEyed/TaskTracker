package org.example.task.tracker.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.example.task.tracker.store.entity.TaskEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {

    private Long id;

    private String name;

    private String description;

    @JsonProperty("taskStateId")
    private Long taskStateId;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;
}
