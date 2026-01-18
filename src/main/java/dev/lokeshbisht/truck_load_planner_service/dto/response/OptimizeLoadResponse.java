package dev.lokeshbisht.truck_load_planner_service.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OptimizeLoadResponse {

    @JsonProperty("truck_id")
    private String truckId;

    @JsonProperty("selected_order_ids")
    private List<String> selectedOrderIds;

    @JsonProperty("total_payout_cents")
    private long totalPayoutCents;

    @JsonProperty("total_weight_lbs")
    private int totalWeightLbs;

    @JsonProperty("total_volume_cuft")
    private int totalVolumeCuft;

    @JsonProperty("utilization_weight_percent")
    private double utilizationWeightPercent;

    @JsonProperty("utilization_volume_percent")
    private double utilizationVolumePercent;
}
