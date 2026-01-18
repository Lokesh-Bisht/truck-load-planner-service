package dev.lokeshbisht.truck_load_planner_service.controller;

import dev.lokeshbisht.truck_load_planner_service.dto.LoadRequest;
import dev.lokeshbisht.truck_load_planner_service.dto.response.OptimizeLoadResponse;
import dev.lokeshbisht.truck_load_planner_service.service.LoadOptimizerService;
import dev.lokeshbisht.truck_load_planner_service.util.LoadRequestValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/load-optimizer")
public class LoadOptimizerController {

    private final LoadRequestValidator loadRequestValidator;

    private final LoadOptimizerService loadOptimizerService;

    private static final Logger logger = LoggerFactory.getLogger(LoadOptimizerController.class);

    @PostMapping(value = "/optimize", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OptimizeLoadResponse> optimize(@Valid @RequestBody LoadRequest loadRequest) {
        logger.info("Start POST optimize request for loadRequest: {}", loadRequest);
        loadRequestValidator.validate(loadRequest);
        logger.info("Load request is successfully validated");

        return ResponseEntity.status(HttpStatus.OK)
            .body(loadOptimizerService.optimize(loadRequest.getTruck(), loadRequest.getOrders()));
    }
}
