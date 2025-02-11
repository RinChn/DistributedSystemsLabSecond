package com.service;

import com.dto.DirectorDto;
import com.entity.Director;
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
                .ifPresent(result -> new RuntimeException("Such a director is already in the database."));
        directorRepository.save(director);
        return directorDto;
    }

    @Override
    @Transactional
    public UUID deleteDirector(DirectorDto directorDto) {
        Director director = conversionService.convert(directorDto, Director.class);
        director = directorRepository.findByName(director.getFirstName(), director.getLastName())
                .orElseThrow(() -> new RuntimeException("Director not found"));
        directorRepository.delete(director);
        return director.getId();
    }
}
