--liquibase formatted sql
--changeset rina:5

INSERT INTO directors (first_name, last_name)
VALUES
    ('Andrei', 'Tarkovsky'),
    ('Nikita', 'Mikhalkov'),
    ('Sergei', 'Eisenstein'),
    ('Aleksei', 'German'),
    ('Kirill', 'Serebrennikov'),
    ('Christopher', 'Nolan'),
    ('Quentin', 'Tarantino'),
    ('Steven', 'Spielberg'),
    ('Greta', 'Gerwig'),
    ('Martin', 'Scorsese');