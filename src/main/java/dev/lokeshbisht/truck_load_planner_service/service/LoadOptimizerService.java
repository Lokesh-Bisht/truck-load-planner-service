package dev.lokeshbisht.truck_load_planner_service.service;

import dev.lokeshbisht.truck_load_planner_service.dto.Order;
import dev.lokeshbisht.truck_load_planner_service.dto.Truck;
import dev.lokeshbisht.truck_load_planner_service.dto.response.OptimizeLoadResponse;

import java.util.List;

public interface LoadOptimizerService {

    OptimizeLoadResponse optimize(Truck truck, List<Order> orders);
}
