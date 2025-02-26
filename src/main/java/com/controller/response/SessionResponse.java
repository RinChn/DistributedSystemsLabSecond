package com.controller.response;

import com.dto.FilmDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Builder
public class SessionResponse {
    LocalDate date;
    Time time;
    FilmDto film;
    Integer cinemaHallNumber;
    Integer numberOfTicketsSold;
    Integer numberOfTicketsAvailable;
}
