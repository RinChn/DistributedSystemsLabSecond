package com.dto;

import com.util.Genre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilmDto {
    @NotBlank(message = "The title cannot be empty")
    private String title;
    @NotBlank(message = "The director's name cannot be empty")
    private String director_name;
    @Positive(message = "The release year cannot be a zero or a negative number.")
    private Integer yearReleased;
    @Positive(message = "The duration cannot be zero or a negative number.")
    private Integer length;
    private Genre genre;

}
