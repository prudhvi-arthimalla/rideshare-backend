CREATE TABLE users
(
    id            BIGSERIAL PRIMARY KEY,
    email         VARCHAR(255)             NOT NULL UNIQUE,
    password_hash VARCHAR(255)             NOT NULL,
    first_name    VARCHAR(100)             NOT NULL,
    last_name     VARCHAR(100)             NOT NULL,
    phone_number  VARCHAR(20)              NOT NULL UNIQUE,
    role          VARCHAR(20)              NOT NULL,
    created_at    TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at    TIMESTAMP WITH TIME ZONE NOT NULL
);