package com.rideshare.commons.kafka;

public final class Topics {
    public static final String ORDER_REQUESTED = "order.requested";
    public static final String ORDER_CANCELLED = "order.cancelled";

    public static final String ORDER_ACCEPTED   = "order.accepted";
    public static final String ORDER_COMPLETED  = "order.completed";

    private Topics() {}
}
