package com.controller;

import com.dto.DirectorDto;
import com.service.DirectorServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/directors")
@CrossOrigin(origins = "http://localhost:5500")
@Slf4j
@RequiredArgsConstructor
public class DirectorController {
    private final DirectorServiceImpl directorService;

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

    @GetMapping("/search")
    public List<DirectorDto> searchDirector(@RequestBody DirectorDto requestDto) {
        return directorService.searchDirectors(requestDto);
    }

}
