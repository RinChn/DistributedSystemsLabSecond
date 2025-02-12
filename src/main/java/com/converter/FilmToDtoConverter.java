package com.converter;

import com.dto.FilmDto;
import com.entity.Director;
import com.entity.Film;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FilmToDtoConverter implements Converter<Film, FilmDto> {
    @Override
    public FilmDto convert(Film source) {
        Director director = source.getDirector();
        return FilmDto.builder()
                .genre(source.getGenre())
                .title(source.getTitle())
                .length(source.getLength())
                .yearReleased(source.getYearReleased())
                .directorName(director.getFirstName() + " " + director.getLastName())
                .build();
    }
}
