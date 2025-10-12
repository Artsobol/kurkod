CREATE TABLE employment_contract(
    id BIGSERIAL PRIMARY KEY,
    contract_number VARCHAR(20) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    salary INTEGER NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    worker_id BIGINT NOT NULL,
    staff_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),

    CONSTRAINT uq_contract_number UNIQUE (contract_number),
    CONSTRAINT ck_contract_dates CHECK (end_date >= start_date),
    CONSTRAINT fk_employment_contract_worker FOREIGN KEY (worker_id) REFERENCES worker(id) ON DELETE CASCADE,
    CONSTRAINT fk_employment_contract_staff FOREIGN KEY (staff_id) REFERENCES staff(id) ON DELETE CASCADE
);