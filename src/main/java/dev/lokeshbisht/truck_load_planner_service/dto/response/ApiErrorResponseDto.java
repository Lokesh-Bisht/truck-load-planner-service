package dev.lokeshbisht.truck_load_planner_service.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.lokeshbisht.truck_load_planner_service.enums.ErrorCodes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorResponseDto {

    @JsonProperty("error_code")
    private ErrorCodes errorCode;

    private String message;

    private List<FieldValidationErrorResponseDto> errors;

    public ApiErrorResponseDto(ErrorCodes errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}
