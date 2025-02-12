package com.converter;

import com.dto.FilmDto;
import com.dto.SessionResponse;
import com.entity.Session;
import com.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.sql.Time;

@Component
@RequiredArgsConstructor
public class SessionToResponseConverter implements Converter<Session, SessionResponse> {
    private final ConversionService conversionService;
    private final TicketRepository ticketRepository;

    @Override
    public SessionResponse convert(Session source) {
        return SessionResponse.builder()
                .time(new Time(source.getTimeAndDate().getTime()))
                .film(conversionService.convert(source.getFilm(), FilmDto.class))
                .date(source.getTimeAndDate())
                .cinemaHallNumber(source.getCinemaHallNumber())
                .numberOfTicketsAvailable(ticketRepository.getTicketsBySessionAndBought(source, false).size())
                .numberOfTicketsSold(ticketRepository.getTicketsBySessionAndBought(source, true).size())
                .build();
    }
}
