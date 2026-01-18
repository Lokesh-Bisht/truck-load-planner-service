package dev.lokeshbisht.truck_load_planner_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FieldValidationErrorResponseDto {

    private String field;

    private String message;
}
