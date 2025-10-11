CREATE TABLE staff(
    id BIGSERIAL PRIMARY KEY,
    position VARCHAR(20) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),

    CONSTRAINT uq_staff_position UNIQUE (position)
)