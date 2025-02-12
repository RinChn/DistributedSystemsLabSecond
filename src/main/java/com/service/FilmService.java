package com.service;

import com.dto.FilmDto;
import com.dto.FilmDeleteRequest;

import java.util.List;
import java.util.UUID;

public interface FilmService {
    List<FilmDto> getAllFilms(Integer pageNumber, Integer pageSize);
    FilmDto addFilm(FilmDto filmDto);
    UUID deleteFilm(FilmDeleteRequest request);
}
