package dev.lokeshbisht.truck_load_planner_service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoadRequest {

    @NotNull
    @Valid
    private Truck truck;

    @NotNull
    @Valid
    @JsonProperty("orders")
    private List<Order> orders;
}
