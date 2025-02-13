package com.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.util.Genre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FilmSearchFilter {
    private String title;
    private String directorName;
    private Integer maxYearReleased;
    private Integer minYearReleased;
    private Integer maxLength;
    private Integer minLength;
    private Genre genre;
}
