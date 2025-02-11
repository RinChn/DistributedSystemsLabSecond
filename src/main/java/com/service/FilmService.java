package com.service;

import com.dto.FilmDto;

import java.util.List;
import java.util.UUID;

public interface FilmService {
    List<FilmDto> getAllFilms();
    FilmDto addFilm(FilmDto film);
    UUID deleteFilm(FilmDto film);
}
