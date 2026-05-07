package com.rideshare.order.web.examples;

public class RequestExample {

    public static final String CREATE_ORDER_REQUEST = """
            {
                "pickupLocation": "Times Square, New York, NY",
                "dropOffLocation": "JFK International Airport, Queens, NY",
                "pickupLat": 40.758896,
                "pickupLng": -73.985130,
                "dropOffLat": 40.641766,
                "dropOffLng": -73.780968
            }
            """;

    public static final String CANCEL_ORDER_REQUEST = """
            {
                "reason": "Change of plans"
            }
            """;
}
