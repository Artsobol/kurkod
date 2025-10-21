CREATE TABLE workshop(
    id BIGSERIAL PRIMARY KEY,
    workshop_number INTEGER NOT NULL,
    CONSTRAINT uq_workshop_number UNIQUE (workshop_number),
    CONSTRAINT ch_workshop_number CHECK (workshop_number > 0)
);

CREATE TABLE rows(
    id BIGSERIAL PRIMARY KEY,
    row_number INTEGER NOT NULL,
    workshop_id BIGINT NOT NULL,
    CONSTRAINT uq_rows_workshop_row UNIQUE (workshop_id, row_number),
    CONSTRAINT ch_rows_row_number CHECK (row_number > 0),
    CONSTRAINT fk_rows_workshop FOREIGN KEY (workshop_id) REFERENCES workshop(id) ON DELETE CASCADE
);

CREATE TABLE cage(
    id BIGSERIAL PRIMARY KEY,
    cage_number INTEGER NOT NULL,
    row_id BIGINT NOT NULL,
    CONSTRAINT fk_cage_row FOREIGN KEY (row_id) REFERENCES rows(id) ON DELETE CASCADE,
    CONSTRAINT uq_cage_row_cage UNIQUE (row_id, cage_number),
    CONSTRAINT ch_cage_cage_number CHECK (cage_number > 0)
);