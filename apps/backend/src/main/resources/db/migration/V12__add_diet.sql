CREATE TABLE diet(
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(30) NOT NULL,
    code VARCHAR(10) NOT NULL,
    description TEXT,
    season VARCHAR(6) NOT NULL,
    CONSTRAINT uq_diet_code UNIQUE (code),
    CONSTRAINT uq_diet_title UNIQUE (title),
    CONSTRAINT ch_diet_season CHECK (season IN ('SPRING', 'SUMMER', 'AUTUMN', 'WINTER'))
);

CREATE TABLE breed_diet(
    breed_id BIGINT NOT NULL,
    diet_id BIGINT NOT NULL,
    CONSTRAINT pk_breed_diet PRIMARY KEY (breed_id, diet_id),
    CONSTRAINT fk_breed_diet_breed FOREIGN KEY (breed_id) REFERENCES breed(id) ON DELETE CASCADE,
    CONSTRAINT fk_breed_diet_diet FOREIGN KEY (diet_id) REFERENCES diet(id) ON DELETE CASCADE
)