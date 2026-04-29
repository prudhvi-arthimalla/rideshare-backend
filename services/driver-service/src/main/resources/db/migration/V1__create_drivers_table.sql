CREATE TABLE drivers
(
    id         BIGSERIAL                PRIMARY KEY,
    user_id    BIGINT                   NOT NULL UNIQUE,
    status     VARCHAR(20)              NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_drivers_status ON drivers (status);
