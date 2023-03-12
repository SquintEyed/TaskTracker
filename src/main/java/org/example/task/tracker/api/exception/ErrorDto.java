package org.example.task.tracker.api.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class ErrorDto {

    @JsonProperty("error")
    private HttpStatus httpStatus;

    @JsonProperty("error_description")
    private String errorDescription;


}
