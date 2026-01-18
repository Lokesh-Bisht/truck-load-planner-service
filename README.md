# Truck-load-planner-service
A stateless Spring Boot service that computes the optimal combination of shipment orders for a truck, maximizing carrier payout while respecting weight, volume, route, hazmat, and time-window constraints.

<br/>

## Steps to build and run the service

1. Build the Docker image
```bash
docker compose build
```

2. Start the service

```bash
docker compose up
```

3. Verify service is running

```bash
docker ps
```

4. Test the health check API

```curl
curl --location 'http://localhost:8080/actuator/health'
```

**Health Check API Response**
```json
{
    "groups": [
        "liveness",
        "readiness"
    ],
    "status": "UP"
}
```

<br/>

# Test the Load Optimizer API

## Scenario 1: Success

You can just run the following curl command.

**API Request**
```curl
curl --location 'http://localhost:8080/api/v1/load-optimizer/optimize' \
--header 'Content-Type: application/json' \
--data '{
    "truck": {
        "id": "truck-123",
        "max_weight_lbs": 44000,
        "max_volume_cuft": 3000
    },
    "orders": [
        {
            "id": "ord-001",
            "payout_cents": 250000,
            "weight_lbs": 18000,
            "volume_cuft": 1200,
            "origin": "Los Angeles, CA",
            "destination": "Dallas, TX",
            "pickup_date": "2025-12-05",
            "delivery_date": "2025-12-09",
            "is_hazmat": false
        },
        {
            "id": "ord-002",
            "payout_cents": 180000,
            "weight_lbs": 12000,
            "volume_cuft": 900,
            "origin": "Los Angeles, CA",
            "destination": "Dallas, TX",
            "pickup_date": "2025-12-04",
            "delivery_date": "2025-12-10",
            "is_hazmat": false
        },
        {
            "id": "ord-003",
            "payout_cents": 320000,
            "weight_lbs": 30000,
            "volume_cuft": 1800,
            "origin": "Los Angeles, CA",
            "destination": "Dallas, TX",
            "pickup_date": "2025-12-06",
            "delivery_date": "2025-12-08",
            "is_hazmat": false
        },
        {
            "id": "ord-004",
            "payout_cents": 300000,
            "weight_lbs": 2000,
            "volume_cuft": 100,
            "origin": "Los Angeles, CA",
            "destination": "Dallas, TX",
            "pickup_date": "2025-12-04",
            "delivery_date": "2025-12-10",
            "is_hazmat": false
        },
        {
            "id": "ord-005",
            "payout_cents": 280000,
            "weight_lbs": 1200,
            "volume_cuft": 100,
            "origin": "Los Angeles, CA",
            "destination": "Dallas, TX",
            "pickup_date": "2025-12-04",
            "delivery_date": "2025-12-10",
            "is_hazmat": false
        },
        {
            "id": "ord-006",
            "payout_cents": 280000,
            "weight_lbs": 1200,
            "volume_cuft": 100,
            "origin": "Los Angeles, CA",
            "destination": "allas, TX",
            "pickup_date": "2025-12-04",
            "delivery_date": "2025-12-10",
            "is_hazmat": false
        },
        {
            "id": "ord-007",
            "payout_cents": 100000,
            "weight_lbs": 1200,
            "volume_cuft": 200,
            "origin": "Los Angeles, CA",
            "destination": "Dallas, TX",
            "pickup_date": "2025-12-04",
            "delivery_date": "2025-12-10",
            "is_hazmat": false
        },
        {
            "id": "ord-008",
            "payout_cents": 20000,
            "weight_lbs": 5000,
            "volume_cuft": 400,
            "origin": "Los Angeles, CA",
            "destination": "Dallas, TX",
            "pickup_date": "2025-12-04",
            "delivery_date": "2025-12-10",
            "is_hazmat": false
        }
    ]
}'
```

<br/>

**API Response**
```json
{
    "truck_id": "truck-123",
    "selected_order_ids": [
        "ord-001",
        "ord-002",
        "ord-004",
        "ord-005",
        "ord-007",
        "ord-008"
    ],
    "total_payout_cents": 1130000,
    "total_weight_lbs": 39400,
    "total_volume_cuft": 2900,
    "utilization_weight_percent": 89.55,
    "utilization_volume_percent": 96.67
}
```

<br/>
<br/>

## Scenario 2: Bad Request

Invalid request body. Missing fields or invalid field values.

**API Request**
```curl
curl --location 'http://localhost:8080/api/v1/load-optimizer/optimize' \
--header 'Content-Type: application/json' \
--data '{
    "truck": {
        "id": "truck-123",
        "max_weight_lbs": -1,
        "max_volume_cuft": -1
    },
    "orders": [
        {
            "payout_cents": -1,
            "weight_lbs": 18000,
            "volume_cuft": 1200,
            "is_hazmat": false
        },
        {
            "id": "ord-002"
        }
    ]
}'
```

<br/>

**API Response**
```json
{
    "error_code": "BAD_REQUEST",
    "errors": [
        {
            "field": "orders[0].origin",
            "message": "origin is required."
        },
        {
            "field": "truck.maxVolumeCuft",
            "message": "max_volume_cuft should be greater than 0."
        },
        {
            "field": "orders[0].pickupDate",
            "message": "pickup_date is required."
        },
        {
            "field": "orders[0].destination",
            "message": "destination is required."
        },
        {
            "field": "orders[0].payoutCents",
            "message": "payout_cents should be greater than 0."
        },
        {
            "field": "orders[1].weightLbs",
            "message": "weight_lbs should be greater than 0."
        },
        {
            "field": "orders[0].deliveryDate",
            "message": "delivery_date is required."
        },
        {
            "field": "orders[1].origin",
            "message": "origin is required."
        },
        {
            "field": "orders[1].pickupDate",
            "message": "pickup_date is required."
        },
        {
            "field": "orders[1].volumeCuft",
            "message": "volume_cuft should be greater than 0."
        },
        {
            "field": "truck.maxWeightLbs",
            "message": "max_weight_lbs should be greater than 0."
        },
        {
            "field": "orders[1].deliveryDate",
            "message": "delivery_date is required."
        },
        {
            "field": "orders[1].destination",
            "message": "destination is required."
        },
        {
            "field": "orders[0].id",
            "message": "Order id is required. It can't be empty."
        },
        {
            "field": "orders[1].payoutCents",
            "message": "payout_cents should be greater than 0."
        }
    ],
    "message": "Validation failed!"
}
```

<br/>
<br/>


## Scenario 3: Payload too large

The API request payload contains more than 22 orders.

**API Request**
```curl
curl --location 'http://localhost:8080/api/v1/load-optimizer/optimize' \
--header 'Content-Type: application/json' \
--data '{
    "truck": {
        "id": "truck-123",
        "max_weight_lbs": 44000,
        "max_volume_cuft": 3000
    },
    "orders": [
        {
            "id": "ord-001",
            "payout_cents": 250000,
            "weight_lbs": 18000,
            "volume_cuft": 1200,
            "origin": "Los Angeles, CA",
            "destination": "Dallas, TX",
            "pickup_date": "2025-12-05",
            "delivery_date": "2025-12-09",
            "is_hazmat": false
        },
        {
            "id": "ord-002",
            "payout_cents": 180000,
            "weight_lbs": 12000,
            "volume_cuft": 900,
            "origin": "Los Angeles, CA",
            "destination": "Dallas, TX",
            "pickup_date": "2025-12-04",
            "delivery_date": "2025-12-10",
            "is_hazmat": false
        },
        {
            "id": "ord-003",
            "payout_cents": 320000,
            "weight_lbs": 30000,
            "volume_cuft": 1800,
            "origin": "Los Angeles, CA",
            "destination": "Dallas, TX",
            "pickup_date": "2025-12-06",
            "delivery_date": "2025-12-08",
            "is_hazmat": false
        },
        {
            "id": "ord-004",
            "payout_cents": 300000,
            "weight_lbs": 2000,
            "volume_cuft": 100,
            "origin": "Los Angeles, CA",
            "destination": "Dallas, TX",
            "pickup_date": "2025-12-04",
            "delivery_date": "2025-12-10",
            "is_hazmat": false
        },
        {
            "id": "ord-005",
            "payout_cents": 280000,
            "weight_lbs": 1200,
            "volume_cuft": 100,
            "origin": "Los Angeles, CA",
            "destination": "Dallas, TX",
            "pickup_date": "2025-12-04",
            "delivery_date": "2025-12-10",
            "is_hazmat": false
        },
        {
            "id": "ord-006",
            "payout_cents": 280000,
            "weight_lbs": 1200,
            "volume_cuft": 100,
            "origin": "Los Angeles, CA",
            "destination": "allas, TX",
            "pickup_date": "2025-12-04",
            "delivery_date": "2025-12-10",
            "is_hazmat": false
        },
        {
            "id": "ord-007",
            "payout_cents": 100000,
            "weight_lbs": 1200,
            "volume_cuft": 200,
            "origin": "Los Angeles, CA",
            "destination": "Dallas, TX",
            "pickup_date": "2025-12-04",
            "delivery_date": "2025-12-10",
            "is_hazmat": false
        },
        {
            "id": "ord-008",
            "payout_cents": 20000,
            "weight_lbs": 5000,
            "volume_cuft": 400,
            "origin": "Los Angeles, CA",
            "destination": "Dallas, TX",
            "pickup_date": "2025-12-04",
            "delivery_date": "2025-12-10",
            "is_hazmat": false
        },
        {
            "id": "ord-009",
            "payout_cents": 20000,
            "weight_lbs": 5000,
            "volume_cuft": 400,
            "origin": "Los Angeles, CA",
            "destination": "Dallas, TX",
            "pickup_date": "2025-12-04",
            "delivery_date": "2025-12-10",
            "is_hazmat": false
        },
        {
            "id": "ord-010",
            "payout_cents": 20000,
            "weight_lbs": 5000,
            "volume_cuft": 400,
            "origin": "Los Angeles, CA",
            "destination": "Dallas, TX",
            "pickup_date": "2025-12-04",
            "delivery_date": "2025-12-10",
            "is_hazmat": false
        },
        {
            "id": "ord-011",
            "payout_cents": 20000,
            "weight_lbs": 5000,
            "volume_cuft": 400,
            "origin": "Los Angeles, CA",
            "destination": "Dallas, TX",
            "pickup_date": "2025-12-04",
            "delivery_date": "2025-12-10",
            "is_hazmat": false
        },
        {
            "id": "ord-012",
            "payout_cents": 20000,
            "weight_lbs": 5000,
            "volume_cuft": 400,
            "origin": "Los Angeles, CA",
            "destination": "Dallas, TX",
            "pickup_date": "2025-12-04",
            "delivery_date": "2025-12-10",
            "is_hazmat": false
        },
        {
            "id": "ord-013",
            "payout_cents": 20000,
            "weight_lbs": 5000,
            "volume_cuft": 400,
            "origin": "Los Angeles, CA",
            "destination": "Dallas, TX",
            "pickup_date": "2025-12-04",
            "delivery_date": "2025-12-10",
            "is_hazmat": false
        },
        {
            "id": "ord-014",
            "payout_cents": 20000,
            "weight_lbs": 5000,
            "volume_cuft": 400,
            "origin": "Los Angeles, CA",
            "destination": "Dallas, TX",
            "pickup_date": "2025-12-04",
            "delivery_date": "2025-12-10",
            "is_hazmat": false
        },
        {
            "id": "ord-015",
            "payout_cents": 20000,
            "weight_lbs": 5000,
            "volume_cuft": 400,
            "origin": "Los Angeles, CA",
            "destination": "Dallas, TX",
            "pickup_date": "2025-12-04",
            "delivery_date": "2025-12-10",
            "is_hazmat": false
        },
        {
            "id": "ord-016",
            "payout_cents": 20000,
            "weight_lbs": 5000,
            "volume_cuft": 400,
            "origin": "Los Angeles, CA",
            "destination": "Dallas, TX",
            "pickup_date": "2025-12-04",
            "delivery_date": "2025-12-10",
            "is_hazmat": false
        },
        {
            "id": "ord-017",
            "payout_cents": 20000,
            "weight_lbs": 5000,
            "volume_cuft": 400,
            "origin": "Los Angeles, CA",
            "destination": "Dallas, TX",
            "pickup_date": "2025-12-04",
            "delivery_date": "2025-12-10",
            "is_hazmat": false
        },
        {
            "id": "ord-018",
            "payout_cents": 20000,
            "weight_lbs": 5000,
            "volume_cuft": 400,
            "origin": "Los Angeles, CA",
            "destination": "Dallas, TX",
            "pickup_date": "2025-12-04",
            "delivery_date": "2025-12-10",
            "is_hazmat": false
        },
        {
            "id": "ord-019",
            "payout_cents": 20000,
            "weight_lbs": 5000,
            "volume_cuft": 400,
            "origin": "Los Angeles, CA",
            "destination": "Dallas, TX",
            "pickup_date": "2025-12-04",
            "delivery_date": "2025-12-10",
            "is_hazmat": false
        },
        {
            "id": "ord-020",
            "payout_cents": 20000,
            "weight_lbs": 5000,
            "volume_cuft": 400,
            "origin": "Los Angeles, CA",
            "destination": "Dallas, TX",
            "pickup_date": "2025-12-04",
            "delivery_date": "2025-12-10",
            "is_hazmat": false
        },
        {
            "id": "ord-021",
            "payout_cents": 20000,
            "weight_lbs": 5000,
            "volume_cuft": 400,
            "origin": "Los Angeles, CA",
            "destination": "Dallas, TX",
            "pickup_date": "2025-12-04",
            "delivery_date": "2025-12-10",
            "is_hazmat": false
        },
        {
            "id": "ord-022",
            "payout_cents": 20000,
            "weight_lbs": 5000,
            "volume_cuft": 400,
            "origin": "Los Angeles, CA",
            "destination": "Dallas, TX",
            "pickup_date": "2025-12-04",
            "delivery_date": "2025-12-10",
            "is_hazmat": false
        },
        {
            "id": "ord-023",
            "payout_cents": 20000,
            "weight_lbs": 5000,
            "volume_cuft": 400,
            "origin": "Los Angeles, CA",
            "destination": "Dallas, TX",
            "pickup_date": "2025-12-04",
            "delivery_date": "2025-12-10",
            "is_hazmat": false
        }
    ]
}'
```

<br/>

**API Response**
```json
{
    "error_code": "PAYLOAD_TOO_LARGE",
    "message": "Max 22 orders allowed"
}
```
