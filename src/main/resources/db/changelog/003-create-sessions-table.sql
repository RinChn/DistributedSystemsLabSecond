--liquibase formatted sql
--changeset rina:3

CREATE TABLE sessions
(
    id UUID NOT NULL PRIMARY KEY default gen_random_uuid(),
    date_and_time timestamp NOT NULL default current_timestamp,
    cinema_hall_number INTEGER NOT NULL default 1,
    film_id UUID REFERENCES films(id)
)