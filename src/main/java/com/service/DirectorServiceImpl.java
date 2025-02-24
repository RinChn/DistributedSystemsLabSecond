package com.service;

import com.dto.DirectorDto;
import com.entity.Director;
import com.exception.ApplicationException;
import com.exception.ErrorType;
import com.repository.DirectorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DirectorServiceImpl implements DirectorService {
    private final DirectorRepository directorRepository;
    private final ConversionService conversionService;

    @Override
    @Transactional(readOnly = true)
    public List<DirectorDto> getAllDirectors() {
        return directorRepository.findAll().stream()
                .map(director -> conversionService.convert(director, DirectorDto.class))
                .toList();
    }

    @Override
    @Transactional
    public DirectorDto addDirector(DirectorDto directorDto) {
        Director director = conversionService.convert(directorDto, Director.class);
        directorRepository.findByName(director.getFirstName(), director.getLastName())
                .ifPresent(result -> {
                    throw new ApplicationException(ErrorType.DUPLICATE_DIRECTOR);
                });
        directorRepository.save(director);
        return directorDto;
    }

    @Override
    @Transactional
    public UUID deleteDirector(DirectorDto directorDto) {
        Director director = conversionService.convert(directorDto, Director.class);
        director = directorRepository.findByName(director.getFirstName(), director.getLastName())
                .orElseThrow(() -> new ApplicationException(ErrorType.NON_EXISTENT_DIRECTOR));
        directorRepository.delete(director);
        return director.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DirectorDto> searchDirectors(DirectorDto directorDto) {
        List<String> directorName = List.of(directorDto.getName().split(" "));
        return directorRepository.findByNameLike(directorName.getFirst(), directorName.getLast()).stream()
                .map(director -> conversionService.convert(director, DirectorDto.class))
                .toList();
    }

    @Transactional(readOnly = true)
    public Director findByName(String fullName) {
        List<String> directorName = List.of(fullName.split(" "));
        return directorRepository.findByName(directorName.getFirst(), directorName.getLast())
                .orElseThrow(() -> new ApplicationException(ErrorType.NON_EXISTENT_DIRECTOR));
    }
}
