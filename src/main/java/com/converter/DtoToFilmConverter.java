package com.converter;

import com.dto.FilmDto;
import com.entity.Film;
import com.repository.DirectorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DtoToFilmConverter implements Converter<FilmDto, Film> {
    private final DirectorRepository directorRepository;
    @Override
    public Film convert(FilmDto source) {
        List<String> directorName = List.of(source.getDirectorName().split(" "));
        return Film.builder()
                .title(source.getTitle())
                .genre(source.getGenre())
                .length(source.getLength())
                .yearReleased(source.getYearReleased())
                .director(directorRepository.findByName(directorName.getFirst(), directorName.getLast())
                        .orElseThrow(() -> new RuntimeException("Director not found")))
                .build();
    }
}
