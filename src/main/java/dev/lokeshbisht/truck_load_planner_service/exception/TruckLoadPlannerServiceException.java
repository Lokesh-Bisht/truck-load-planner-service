package dev.lokeshbisht.truck_load_planner_service.exception;

import dev.lokeshbisht.truck_load_planner_service.enums.ErrorCodes;
import org.springframework.http.HttpStatus;

public class TruckLoadPlannerServiceException extends RuntimeException {

    private final ErrorCodes errorCode;

    private final HttpStatus httpStatus;

    public TruckLoadPlannerServiceException(ErrorCodes errorCode, HttpStatus httpStatus, String message) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public ErrorCodes getErrorCode() { return this.errorCode; }

    public int getHttpStatus() { return this.httpStatus.value(); }
}
