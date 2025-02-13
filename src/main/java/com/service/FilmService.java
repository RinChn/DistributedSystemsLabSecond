package com.service;

import com.dto.FilmDto;
import com.controller.request.FilmDeleteRequest;
import com.dto.FilmSearchFilter;

import java.util.List;
import java.util.UUID;

public interface FilmService {
    List<FilmDto> getAllFilms(Integer pageNumber, Integer pageSize);
    FilmDto addFilm(FilmDto filmDto);
    UUID deleteFilm(FilmDeleteRequest request);
    List<FilmDto> searchFilms(FilmSearchFilter filmSearchFilter);
}
