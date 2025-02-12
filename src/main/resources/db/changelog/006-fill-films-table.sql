--liquibase formatted sql
--changeset rina:6

INSERT INTO films (title, director_id, year_release, length, genre)
VALUES
    ('Stalker', (SELECT id FROM directors WHERE first_name = 'Andrei' AND last_name = 'Tarkovsky'), 1979, 163, 'Sci-Fi'),
    ('Solaris', (SELECT id FROM directors WHERE first_name = 'Andrei' AND last_name = 'Tarkovsky'), 1972, 167, 'Sci-Fi'),
    ('Burnt by the Sun', (SELECT id FROM directors WHERE first_name = 'Nikita' AND last_name = 'Mikhalkov'), 1994, 135, 'Drama'),
    ('12', (SELECT id FROM directors WHERE first_name = 'Nikita' AND last_name = 'Mikhalkov'), 2007, 153, 'Drama'),
    ('Battleship Potemkin', (SELECT id FROM directors WHERE first_name = 'Sergei' AND last_name = 'Eisenstein'), 1925, 75, 'History'),
    ('Alexander Nevsky', (SELECT id FROM directors WHERE first_name = 'Sergei' AND last_name = 'Eisenstein'), 1938, 112, 'History'),
    ('Hard to Be a God', (SELECT id FROM directors WHERE first_name = 'Aleksei' AND last_name = 'German'), 2013, 177, 'Drama'),
    ('My Friend Ivan Lapshin', (SELECT id FROM directors WHERE first_name = 'Aleksei' AND last_name = 'German'), 1984, 101, 'Drama'),
    ('Leto', (SELECT id FROM directors WHERE first_name = 'Kirill' AND last_name = 'Serebrennikov'), 2018, 126, 'Biography'),
    ('The Student', (SELECT id FROM directors WHERE first_name = 'Kirill' AND last_name = 'Serebrennikov'), 2016, 118, 'Drama'),
    ('Inception', (SELECT id FROM directors WHERE first_name = 'Christopher' AND last_name = 'Nolan'), 2010, 148, 'Sci-Fi'),
    ('The Dark Knight', (SELECT id FROM directors WHERE first_name = 'Christopher' AND last_name = 'Nolan'), 2008, 152, 'Action'),
    ('Pulp Fiction', (SELECT id FROM directors WHERE first_name = 'Quentin' AND last_name = 'Tarantino'), 1994, 154, 'Crime'),
    ('Django Unchained', (SELECT id FROM directors WHERE first_name = 'Quentin' AND last_name = 'Tarantino'), 2012, 165, 'Western'),
    ('Schindler''s List', (SELECT id FROM directors WHERE first_name = 'Steven' AND last_name = 'Spielberg'), 1993, 195, 'Biography'),
    ('Jurassic Park', (SELECT id FROM directors WHERE first_name = 'Steven' AND last_name = 'Spielberg'), 1993, 127, 'Adventure'),
    ('Little Women', (SELECT id FROM directors WHERE first_name = 'Greta' AND last_name = 'Gerwig'), 2019, 135, 'Drama'),
    ('Lady Bird', (SELECT id FROM directors WHERE first_name = 'Greta' AND last_name = 'Gerwig'), 2017, 94, 'Comedy'),
    ('The Irishman', (SELECT id FROM directors WHERE first_name = 'Martin' AND last_name = 'Scorsese'), 2019, 209, 'Crime'),
    ('The Departed', (SELECT id FROM directors WHERE first_name = 'Martin' AND last_name = 'Scorsese'), 2006, 151, 'Thriller');