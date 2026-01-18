package dev.lokeshbisht.truck_load_planner_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Truck {

    @NotBlank(message = "Truck id is required.")
    private String id;

    @JsonProperty("max_weight_lbs")
    @Positive(message = "max_weight_lbs should be greater than 0.")
    private int maxWeightLbs;

    @JsonProperty("max_volume_cuft")
    @Positive(message = "max_volume_cuft should be greater than 0.")
    private int maxVolumeCuft;
}
