package org.example.task.tracker.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskStateDto {

    private Long id;

    private String name;

    private Integer ordinal;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    private List<TaskDto> tasks = new ArrayList<>();
}
