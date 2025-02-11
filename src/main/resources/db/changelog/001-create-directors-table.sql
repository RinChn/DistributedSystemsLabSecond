--liquibase formatted sql
--changeset rina:1

CREATE TABLE directors
(
    id UUID NOT NULL PRIMARY KEY default gen_random_uuid(),
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL
)