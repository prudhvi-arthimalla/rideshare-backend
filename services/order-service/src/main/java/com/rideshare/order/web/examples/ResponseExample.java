package com.rideshare.order.web.examples;

public class ResponseExample {

    public static final String ORDER_RESPONSE = """
            {
                "id": 1,
                "riderId": 2,
                "driverId": 5,
                "status": "ACCEPTED",
                "pickupLocation": "Times Square, New York, NY",
                "dropOffLocation": "JFK International Airport, Queens, NY",
                "pickupLat": 40.758896,
                "pickupLng": -73.985130,
                "dropOffLat": 40.641766,
                "dropOffLng": -73.780968,
                "cancellationReason": null,
                "createdAt": "2026-05-07T06:30:00Z",
                "updatedAt": "2026-05-07T06:31:00Z"
            }
            """;

    public static final String ORDER_CANCELLED_RESPONSE = """
            {
                "id": 1,
                "riderId": 2,
                "driverId": null,
                "status": "CANCELLED",
                "pickupLocation": "Times Square, New York, NY",
                "dropOffLocation": "JFK International Airport, Queens, NY",
                "pickupLat": 40.758896,
                "pickupLng": -73.985130,
                "dropOffLat": 40.641766,
                "dropOffLng": -73.780968,
                "cancellationReason": "Change of plans",
                "createdAt": "2026-05-07T06:30:00Z",
                "updatedAt": "2026-05-07T06:35:00Z"
            }
            """;
}
