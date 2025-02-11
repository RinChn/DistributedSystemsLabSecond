--liquibase formatted sql
--changeset rina:2

CREATE TABLE films
(
    id UUID NOT NULL PRIMARY KEY default gen_random_uuid(),
    title VARCHAR(255) NOT NULL,
    director_id UUID NOT NULL REFERENCES directors(id),
    year_release INTEGER NOT NULL,
    length INTEGER NOT NULL CHECK (length >= 0),
    genre VARCHAR(255) NOT NULL
)