create table egg_production_month(
    id BIGSERIAL PRIMARY KEY,
    month SMALLINT NOT NULL,
    year SMALLINT NOT NULL,
    eggs_count SMALLINT NOT NULL,
    chicken_id BIGINT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_egg_production_month_chicken FOREIGN KEY (chicken_id) REFERENCES chicken(id) ON DELETE CASCADE,
    CONSTRAINT uq_egg_production_month UNIQUE (chicken_id, month, year),
    CONSTRAINT ch_egg_production_month CHECK (month BETWEEN 1 AND 12),
    CONSTRAINT ch_egg_production_year CHECK (year > 2000)
);