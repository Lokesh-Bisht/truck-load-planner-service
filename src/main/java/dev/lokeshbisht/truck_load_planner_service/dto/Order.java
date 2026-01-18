package dev.lokeshbisht.truck_load_planner_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Order {

    @NotBlank(message = "Order id is required. It can't be empty.")
    private String id;

    @JsonProperty("payout_cents")
    @Positive(message = "payout_cents should be greater than 0.")
    private long payoutCents;

    @JsonProperty("weight_lbs")
    @Positive(message = "weight_lbs should be greater than 0.")
    private int weightLbs;

    @JsonProperty("volume_cuft")
    @Positive(message = "volume_cuft should be greater than 0.")
    private int volumeCuft;

    @NotBlank(message = "origin is required.")
    private String origin;

    @NotBlank(message = "destination is required.")
    private String destination;

    @JsonProperty("pickup_date")
    @NotNull(message = "pickup_date is required.")
    private LocalDate pickupDate;

    @JsonProperty("delivery_date")
    @NotNull(message = "delivery_date is required.")
    private LocalDate deliveryDate;

    @JsonProperty("is_hazmat")
    private boolean isHazmat;
}
