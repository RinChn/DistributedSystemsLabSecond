package com.service;

import com.dto.DirectorDto;
import com.entity.Director;
import com.exception.ApplicationException;
import com.exception.ErrorType;
import com.repository.DirectorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
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
        log.info("Add director {}", directorDto.getName());
        Director director = conversionService.convert(directorDto, Director.class);
        directorRepository.findByName(director.getFirstName(), director.getLastName())
                .ifPresent(_ -> {
                    throw new ApplicationException(ErrorType.DUPLICATE_DIRECTOR);
                });
        directorRepository.save(director);
        log.info("Director added successfully");
        return directorDto;
    }

    @Override
    @Transactional
    public UUID deleteDirector(DirectorDto directorDto) {
        log.info("Deleting director {}", directorDto.getName());
        Director director = conversionService.convert(directorDto, Director.class);
        director = directorRepository.findByName(director.getFirstName(), director.getLastName())
                .orElseThrow(() -> new ApplicationException(ErrorType.NON_EXISTENT_DIRECTOR));
        directorRepository.delete(director);
        log.info("Director deleted successfully");
        return director.getId();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "searchDirectorCache", key = "#directorDto")
    public List<DirectorDto> searchDirectors(DirectorDto directorDto) {
        log.info("Searching director {}", directorDto.getName());
        String[] parts = directorDto.getName().trim().split("\\s+");
        if (parts.length > 1) {
            return directorRepository.findByTwoNamesLike(parts[0], parts[1]).stream()
                    .map(director -> conversionService.convert(director, DirectorDto.class))
                    .toList();
        } else {
            return directorRepository.searchByFirstOrLastName(directorDto.getName()).stream()
                    .map(director -> conversionService.convert(director, DirectorDto.class))
                    .toList();
        }
    }

    @Transactional(readOnly = true)
    public Director findByName(String fullName) {
        List<String> directorName = List.of(fullName.split(" "));
        return directorRepository.findByName(directorName.getFirst(), directorName.getLast())
                .orElseThrow(() -> new ApplicationException(ErrorType.NON_EXISTENT_DIRECTOR));
    }
}
