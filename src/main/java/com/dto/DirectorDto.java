package com.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DirectorDto {
    @NotBlank(message = "The name cannot be empty")
    private String name;
}
