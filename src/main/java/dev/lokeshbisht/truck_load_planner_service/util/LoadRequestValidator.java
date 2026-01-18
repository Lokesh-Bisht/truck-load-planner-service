package dev.lokeshbisht.truck_load_planner_service.util;

import dev.lokeshbisht.truck_load_planner_service.dto.LoadRequest;
import dev.lokeshbisht.truck_load_planner_service.enums.ErrorCodes;
import dev.lokeshbisht.truck_load_planner_service.exception.TruckLoadPlannerServiceException;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class LoadRequestValidator {

    public void validate(LoadRequest loadRequest) {
        if (ObjectUtils.isEmpty(loadRequest) || ObjectUtils.isEmpty(loadRequest.getTruck())) {
            throw new TruckLoadPlannerServiceException(ErrorCodes.BAD_REQUEST, HttpStatus.BAD_REQUEST, "Truck is required!");
        }

        if (ObjectUtils.isEmpty(loadRequest.getOrders())) {
            throw new TruckLoadPlannerServiceException(ErrorCodes.BAD_REQUEST, HttpStatus.BAD_REQUEST, "Orders are required!");
        }

        if (loadRequest.getOrders().size() > 22) {
            throw new TruckLoadPlannerServiceException(ErrorCodes.PAYLOAD_TOO_LARGE, HttpStatus.CONTENT_TOO_LARGE, "Max 22 orders allowed");
        }
    }
}
