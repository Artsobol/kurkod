CREATE TABLE passport(
    id BIGSERIAL PRIMARY KEY,
    series VARCHAR(4) NOT NULL,
    number VARCHAR(6) NOT NULL,
    worker_id BIGINT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),

    CONSTRAINT uq_passport_series_number UNIQUE (series, number),
    CONSTRAINT ck_passport_series_digits CHECK (series ~ '^[0-9]{4}$'),
    CONSTRAINT ck_passport_number_digits CHECK (number ~ '^[0-9]{6}$'),
    CONSTRAINT fk_passport_worker FOREIGN KEY (worker_id) REFERENCES worker(id) ON DELETE CASCADE
)