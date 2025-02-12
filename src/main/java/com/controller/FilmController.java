package com.controller;

import com.dto.FilmDto;
import com.dto.FilmDeleteRequest;
import com.service.FilmServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {
    private final FilmServiceImpl filmService;

    @GetMapping
    public List<FilmDto> getAllFilms(@RequestParam(value = "page", defaultValue = "0") Integer pageNumber,
                                     @RequestParam(value = "limit", defaultValue = "5") Integer pageSize) {
        return filmService.getAllFilms(pageNumber, pageSize);
    }

    @PostMapping
    public FilmDto addFilm(@Valid @RequestBody FilmDto filmDto) {
        return filmService.addFilm(filmDto);
    }

    @DeleteMapping
    public UUID deleteFilm(@Valid @RequestBody FilmDeleteRequest request) {
        return filmService.deleteFilm(request);
    }
}
