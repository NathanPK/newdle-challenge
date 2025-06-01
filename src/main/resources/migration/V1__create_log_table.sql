CREATE TABLE logs (
    model_id VARCHAR(255) NOT NULL,
    conversation_id VARCHAR(255) NOT NULL,
    log_index INTEGER NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    PRIMARY KEY (model_id, conversation_id, log_index)
);

CREATE INDEX idx_logs_model_id ON logs(model_id);

CREATE TABLE metrics (
    model_id VARCHAR(255),
    latency NUMERIC,
    time_stamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (model_id, time_stamp)
);