package com.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DirectorDto {
    @NotBlank(message = "The name cannot be empty")
    private String name;
}
