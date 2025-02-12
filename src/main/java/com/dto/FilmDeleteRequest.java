package com.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FilmDeleteRequest {
    @NotBlank(message = "Enter the movie name")
    String title;
    @NotBlank(message = "Enter the film's director")
    String directorName;
}
