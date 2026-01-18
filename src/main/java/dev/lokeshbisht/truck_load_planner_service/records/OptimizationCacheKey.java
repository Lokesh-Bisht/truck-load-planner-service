package dev.lokeshbisht.truck_load_planner_service.records;

import java.util.List;

public record OptimizationCacheKey(
    String truckId,
    List<String> orderIds
) {
    // [OrderA, OrderB, OrderC] = [OrderC, OrderB, OrderA]
    public OptimizationCacheKey {
        orderIds = orderIds.stream()
            .sorted()
            .toList();
    }
}
