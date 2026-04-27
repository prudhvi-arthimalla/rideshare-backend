CREATE TABLE outbox_events
(
    id          UUID                     PRIMARY KEY DEFAULT gen_random_uuid(),
    topic       VARCHAR(255)             NOT NULL,
    message_key VARCHAR(255)             NOT NULL,
    payload     TEXT                     NOT NULL,
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    published_at TIMESTAMP WITH TIME ZONE
);

CREATE INDEX idx_outbox_unpublished ON outbox_events (created_at)
    WHERE published_at IS NULL;
