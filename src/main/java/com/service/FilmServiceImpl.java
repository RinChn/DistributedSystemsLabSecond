package com.service;

import com.dto.FilmDto;
import com.dto.FilmDeleteRequest;
import com.entity.Director;
import com.entity.Film;
import com.exception.ApplicationException;
import com.exception.ErrorType;
import com.repository.FilmRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmServiceImpl implements FilmService {
    private final FilmRepository filmRepository;
    private final DirectorServiceImpl directorService;
    private final ConversionService conversionService;

    @Override
    @Transactional(readOnly = true)
    public List<FilmDto> getAllFilms(Integer pageNumber, Integer pageSize) {
        return filmRepository.findAll(PageRequest.of(pageNumber, pageSize)).stream()
                .map(film -> conversionService.convert(film, FilmDto.class))
                .toList();
    }

    @Override
    @Transactional
    public FilmDto addFilm(FilmDto filmDto) {
        Film film = conversionService.convert(filmDto, Film.class);
        filmRepository.findByTitleAndDirector(film.getTitle(), film.getDirector())
                .ifPresent(search -> {
                    throw new ApplicationException(ErrorType.DUPLICATE_MOVIE);
                });
        filmRepository.save(film);
        return filmDto;
    }

    @Override
    @Transactional
    public UUID deleteFilm(FilmDeleteRequest request) {
        Director director = directorService.findByName(request.getDirectorName());
        Film film = filmRepository
                .findByTitleAndDirector(request.getTitle(), director)
                .orElseThrow(() -> new ApplicationException(ErrorType.NON_EXISTENT_DIRECTOR));
        filmRepository.delete(film);
        return film.getId();
    }
}
