CREATE TABLE orders
(
    id                  BIGSERIAL PRIMARY KEY,
    rider_id            BIGINT                   NOT NULL,
    driver_id           BIGINT,
    status              VARCHAR(20)              NOT NULL,
    pickup_location     VARCHAR(255)             NOT NULL,
    dropOff_location    VARCHAR(255)             NOT NULL,
    pickup_lat          NUMERIC(9, 6),
    pickup_lng          NUMERIC(9, 6),
    dropOff_lat         NUMERIC(9, 6),
    dropOff_lng         NUMERIC(9, 6),
    cancellation_reason VARCHAR(500),
    created_at          TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at          TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_orders_rider_id ON orders (rider_id);
CREATE INDEX idx_orders_driver_id ON orders (driver_id);
CREATE INDEX idx_orders_status ON orders (status);