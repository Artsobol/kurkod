create table chicken_movement(
    id SERIAL PRIMARY KEY,
    moved_at TIMESTAMP NOT NULL,
    chicken_id BIGINT NOT NULL,
    from_cage_id BIGINT NOT NULL,
    to_cage_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_chicken_movement_chicken FOREIGN KEY (chicken_id) REFERENCES chicken(id) ON DELETE CASCADE,
    CONSTRAINT fk_chicken_movement_from_cage FOREIGN KEY (from_cage_id) REFERENCES cage(id) ON DELETE SET NULL,
    CONSTRAINT fk_chicken_movement_to_cage FOREIGN KEY (to_cage_id) REFERENCES cage(id) ON DELETE CASCADE,
    CONSTRAINT ch_chicken_movement_from_to CHECK (from_cage_id IS NULL OR from_cage_id <> to_cage_id)
);