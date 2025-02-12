package com.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.sql.Date;
import java.sql.Time;

@Getter
public class TicketRequest {
    @NotNull(message = "The time cannot be empty")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    Time time;
    @NotNull(message = "The date cannot be empty")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    Date date;
    @NotNull(message = "The room number cannot be empty")
    Integer cinemaHallNumber;
    @NotNull(message = "The row number cannot be empty")
    Integer rowNumber;
    @NotNull(message = "The seat number cannot be empty")
    Integer seatNumber;
}
