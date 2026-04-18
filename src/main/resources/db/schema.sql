-- ── Quantity Measurement – Production Schema ──────────────────────────────────

CREATE TABLE IF NOT EXISTS quantity_measurement_entity (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    operation_type   VARCHAR(20)  NOT NULL,
    operand1         VARCHAR(255) NOT NULL,
    operand2         VARCHAR(255),
    result           VARCHAR(255),
    has_error        BOOLEAN      NOT NULL DEFAULT FALSE,
    error_message    VARCHAR(500),
    created_at       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_operation_type ON quantity_measurement_entity(operation_type);
CREATE INDEX IF NOT EXISTS idx_created_at      ON quantity_measurement_entity(created_at);

CREATE TABLE IF NOT EXISTS quantity_measurement_history (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    entity_id        BIGINT       NOT NULL,
    changed_at       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    change_summary   VARCHAR(500),
    FOREIGN KEY (entity_id) REFERENCES quantity_measurement_entity(id)
);
