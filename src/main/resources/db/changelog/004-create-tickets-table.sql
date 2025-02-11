--liquibase formatted sql
--changeset rina:4

CREATE TABLE tickets
(
    id UUID NOT NULL PRIMARY KEY default gen_random_uuid(),
    row_number INTEGER NOT NULL,
    place_number INTEGER NOT NULL,
    session_id UUID NOT NULL REFERENCES sessions(id),
    bought BOOLEAN NOT NULL default false
)