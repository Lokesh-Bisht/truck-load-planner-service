package dev.lokeshbisht.truck_load_planner_service.exception;

import dev.lokeshbisht.truck_load_planner_service.dto.response.ApiErrorResponseDto;
import dev.lokeshbisht.truck_load_planner_service.dto.response.FieldValidationErrorResponseDto;
import dev.lokeshbisht.truck_load_planner_service.enums.ErrorCodes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TruckLoadPlannerServiceException.class)
    public ResponseEntity<ApiErrorResponseDto> handleTruckLoadPlannerServiceException(TruckLoadPlannerServiceException e) {
        ApiErrorResponseDto apiErrorResponseDto = new ApiErrorResponseDto(e.getErrorCode(), e.getMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(apiErrorResponseDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<FieldValidationErrorResponseDto> errors = e.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(err -> new FieldValidationErrorResponseDto(
                err.getField(),
                err.getDefaultMessage()
            ))
            .collect(Collectors.toList());

        log.error("Validation failure.");
        ApiErrorResponseDto apiErrorResponseDto = new ApiErrorResponseDto(ErrorCodes.BAD_REQUEST, "Validation failed!", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiErrorResponseDto);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponseDto> handleUnhandledExceptions(Exception e) {
        log.error("Unhandled exception: {}", e.getMessage(), e);
        ApiErrorResponseDto apiErrorResponseDto = new ApiErrorResponseDto(ErrorCodes.INTERNAL_SERVER_ERROR, "An error occurred while processing the request. Please, try again!");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiErrorResponseDto);
    }
}
