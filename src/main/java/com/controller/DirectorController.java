package com.controller;

import com.dto.DirectorDto;
import com.service.DirectorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/directors")
@Slf4j
@RequiredArgsConstructor
public class DirectorController {
    private final DirectorService directorService;

    @GetMapping
    public List<DirectorDto> getAllDirectors() {
        return directorService.getAllDirectors();
    }

    @PostMapping
    public DirectorDto createDirector(@Valid @RequestBody DirectorDto directorDto) {
        return directorService.addDirector(directorDto);
    }

    @DeleteMapping
    public UUID deleteDirector(@Valid @RequestBody DirectorDto directorDto) {
        return directorService.deleteDirector(directorDto);
    }

}
