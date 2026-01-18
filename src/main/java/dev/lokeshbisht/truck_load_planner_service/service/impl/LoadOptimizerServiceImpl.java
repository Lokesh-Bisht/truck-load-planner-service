package dev.lokeshbisht.truck_load_planner_service.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import dev.lokeshbisht.truck_load_planner_service.dto.Order;
import dev.lokeshbisht.truck_load_planner_service.dto.Truck;
import dev.lokeshbisht.truck_load_planner_service.dto.response.OptimizeLoadResponse;
import dev.lokeshbisht.truck_load_planner_service.records.OptimizationCacheKey;
import dev.lokeshbisht.truck_load_planner_service.service.LoadOptimizerService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoadOptimizerServiceImpl implements LoadOptimizerService {

    private final Cache<String, OptimizeLoadResponse> cache;

    private static final Logger logger = LoggerFactory.getLogger(LoadOptimizerServiceImpl.class);

    @Override
    public OptimizeLoadResponse optimize(Truck truck, List<Order> orders) {
        String truckId = truck.getId();
        logger.info("Start LoadOptimizerServiceImpl - optimize(). Optimize load for truck: {}", truckId);

        List<String> orderIds = orders.stream()
            .map(Order::getId)
            .toList();

        String key = "optimize-api:" + truckId + ":" + new OptimizationCacheKey(truckId, orderIds);

        if (cache.getIfPresent(key) != null) {
            logger.info("Fetching response from cache for key: {}", key);
            return cache.getIfPresent(key);
        }

        if (orders.isEmpty()) {
            OptimizeLoadResponse response = new OptimizeLoadResponse();
            response.setTruckId(truckId);
            response.setSelectedOrderIds(Collections.emptyList());
            return response;
        }

        List<Order> validOrders = findValidOrders(orders);

        long bestPayout = 0;
        int bestMask = 0;

        for (int mask = 1; mask < (1 << validOrders.size()); ++mask) {
            int totalWeight = 0;
            int totalVolume = 0;
            long totalPayout = 0;

            for (int i = 0; i < validOrders.size(); ++i) {
                if ((mask & (1 << i)) != 0) {
                    Order order = validOrders.get(i);
                    totalWeight += order.getWeightLbs();
                    totalVolume += order.getVolumeCuft();
                    totalPayout += order.getPayoutCents();

                    if (totalWeight > truck.getMaxWeightLbs() || totalVolume > truck.getMaxVolumeCuft()) {
                        break;
                    }
                }

                if (totalWeight <= truck.getMaxWeightLbs()
                    && totalVolume <= truck.getMaxVolumeCuft()
                    && totalPayout > bestPayout) {
                    bestPayout = totalPayout;
                    bestMask = mask;
                }
            }
        }

        OptimizeLoadResponse response = buildResponse(truck, validOrders, bestMask);
        logger.info("End LoadOptimizerServiceImpl - optimize()");
        cache.put(key, response);

        return response;
    }

    private List<Order> findValidOrders(List<Order> orders) {
        List<Order> validOrders = new ArrayList<>();
        Order firstOrder = orders.get(0);

        for (Order order : orders) {
            if (order.getOrigin().equals(firstOrder.getOrigin()) &&
                order.getDestination().equals(firstOrder.getDestination()) &&
                !order.getPickupDate().isAfter(order.getDeliveryDate()) &&
                !order.isHazmat()
            ) {
                validOrders.add(order);
            }
        }

        return validOrders;
    }

    private OptimizeLoadResponse buildResponse(Truck truck, List<Order> validOrders, int mask) {
        List<String> selectedOrderIds = new ArrayList<>();
        int totalWeight = 0;
        int totalVolume = 0;
        long totalPayout = 0;

        for (int i = 0; i < validOrders.size(); i++) {
            if ((mask & (1 << i)) != 0) {
                Order order = validOrders.get(i);
                selectedOrderIds.add(order.getId());
                totalWeight += order.getWeightLbs();
                totalVolume += order.getVolumeCuft();
                totalPayout += order.getPayoutCents();
            }
        }

        double utilizationWeightPercent = round((totalWeight * 100.0) / truck.getMaxWeightLbs());
        double utilizationVolumePercent = round((totalVolume * 100.0) / truck.getMaxVolumeCuft());

        return OptimizeLoadResponse.builder()
            .truckId(truck.getId())
            .selectedOrderIds(selectedOrderIds)
            .totalPayoutCents(totalPayout)
            .totalWeightLbs(totalWeight)
            .totalVolumeCuft(totalVolume)
            .utilizationWeightPercent(utilizationWeightPercent)
            .utilizationVolumePercent(utilizationVolumePercent)
            .build();
    }

    private double round(double num) {
        return Math.round(num * 100.0) / 100.0;
    }
}
