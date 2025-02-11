package com.service;

import com.dto.DirectorDto;

import java.util.List;
import java.util.UUID;

public interface DirectorService {
    List<DirectorDto> getAllDirectors();
    DirectorDto addDirector(DirectorDto directorDto);
    UUID deleteDirector(DirectorDto directorDto);
}
