create table dismissal(
    id BIGSERIAL PRIMARY KEY,
    dismissal_date DATE NOT NULL,
    reason TEXT NOT NULL,
    worker_id BIGINT NOT NULL,
    who_dismiss_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_dismissal_worker FOREIGN KEY (worker_id) REFERENCES worker(id) ON DELETE CASCADE,
    CONSTRAINT fk_dismissal_who_dismiss FOREIGN KEY (who_dismiss_id) REFERENCES worker(id) ON DELETE CASCADE
);