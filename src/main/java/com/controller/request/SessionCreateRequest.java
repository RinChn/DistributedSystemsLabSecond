package com.controller.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Time;
import java.sql.Date;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SessionCreateRequest {
    @NotNull(message = "The time cannot be empty")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    Time time;
    @NotNull(message = "The date cannot be empty")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    Date date;
    @NotNull(message = "The room number cannot be empty")
    Integer cinemaHallNumber;
    @NotBlank(message = "The title cannot be empty")
    String filmTitle;
    @NotBlank(message = "The director's name cannot be empty")
    String directorName;
}
