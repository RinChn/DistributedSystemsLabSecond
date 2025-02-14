package com.service;

import com.controller.request.SessionCreateRequest;
import com.controller.request.SessionSearchRequest;
import com.controller.request.TicketRequest;
import com.controller.response.SessionResponse;
import com.controller.response.TicketResponse;
import com.dto.*;
import com.entity.Director;
import com.entity.Film;
import com.entity.Session;
import com.entity.Ticket;
import com.exception.ApplicationException;
import com.exception.ErrorType;
import com.repository.FilmRepository;
import com.repository.SessionRepository;
import com.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionServiceImpl implements SessionService {
    private final SessionRepository sessionRepository;
    private final ConversionService conversionService;
    private final DirectorServiceImpl directorService;
    private final FilmRepository filmRepository;
    private final TicketRepository ticketRepository;

    @Override
    @Transactional(readOnly = true)
    public List<SessionResponse> getAllSessions() {
        return sessionRepository.findAll().stream()
                .map(session -> SessionResponse.builder()
                        .time(new Time(session.getTimeAndDate().getTime()))
                        .film(conversionService.convert(session.getFilm(), FilmDto.class))
                        .date(session.getTimeAndDate())
                        .cinemaHallNumber(session.getCinemaHallNumber())
                        .numberOfTicketsAvailable(ticketRepository.getTicketsBySessionAndBought(session, false).size())
                        .numberOfTicketsSold(ticketRepository.getTicketsBySessionAndBought(session, true).size())
                        .build())
                .toList();
    }

    @Override
    @Transactional
    public SessionResponse addSession(SessionCreateRequest sessionDto) {
        Director director = directorService.findByName(sessionDto.getDirectorName());
        Film film = filmRepository
                .findByTitleAndDirector(sessionDto.getFilmTitle(), director)
                .orElseThrow(() -> new ApplicationException(ErrorType.NON_EXISTENT_MOVIE));
        Timestamp timestampOfSession = getTimestamp(sessionDto.getTime(), sessionDto.getDate());
        log.info("{}", timestampOfSession);
        sessionRepository.findByDatetimeAndHallNumber(timestampOfSession, sessionDto.getCinemaHallNumber())
                .ifPresent(_ -> {
                        throw new ApplicationException(ErrorType.HALL_IS_OCCUPIED);
                        });
        Session session = Session.builder()
                .film(film)
                .cinemaHallNumber(sessionDto.getCinemaHallNumber())
                .timeAndDate(timestampOfSession)
                .build();
        sessionRepository.save(session);

        List<Ticket> tickets = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            for (int j = 1; j < 6; j++) {
                tickets.add(Ticket.builder()
                        .session(session)
                        .rowNumber(i)
                        .placeNumber(j)
                        .build());
            }
        }
        ticketRepository.saveAll(tickets);

        return SessionResponse.builder()
                .time(sessionDto.getTime())
                .film(conversionService.convert(film, FilmDto.class))
                .date(sessionDto.getDate())
                .cinemaHallNumber(session.getCinemaHallNumber())
                .numberOfTicketsAvailable(ticketRepository.getTicketsBySessionAndBought(session, false).size())
                .numberOfTicketsSold(ticketRepository.getTicketsBySessionAndBought(session, true).size())
                .build();
    }

    private Timestamp getTimestamp(Time time, Date date) {
        return Timestamp.valueOf(LocalDateTime
                .of(date.toLocalDate(), time.toLocalTime()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketResponse> getAllTickets(SessionSearchRequest sessionDto) {
        Timestamp timestampOfSession = Timestamp.valueOf(LocalDateTime
                .of(sessionDto.getDate().toLocalDate(), sessionDto.getTime().toLocalTime()));
        Session session = sessionRepository.findByDatetimeAndHallNumber(timestampOfSession, sessionDto.getCinemaHallNumber())
                .orElseThrow(() -> new ApplicationException(ErrorType.NON_EXISTENT_SESSION));
        return ticketRepository.getAllTicketsBySession(session).stream()
                .map(ticket -> conversionService.convert(ticket, TicketResponse.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketResponse> getAvailableTickets(SessionSearchRequest sessionDto) {
        Timestamp timestampOfSession = getTimestamp(sessionDto.getTime(), sessionDto.getDate());
        Session session = sessionRepository.findByDatetimeAndHallNumber(timestampOfSession, sessionDto.getCinemaHallNumber())
                .orElseThrow(() -> new ApplicationException(ErrorType.NON_EXISTENT_SESSION));
        return ticketRepository.getTicketsBySessionAndBought(session, false).stream()
                .map(ticket -> conversionService.convert(ticket, TicketResponse.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketResponse> getNotAvailableTickets(SessionSearchRequest sessionDto) {
        Timestamp timestampOfSession = getTimestamp(sessionDto.getTime(), sessionDto.getDate());
        Session session = sessionRepository.findByDatetimeAndHallNumber(timestampOfSession, sessionDto.getCinemaHallNumber())
                .orElseThrow(() -> new ApplicationException(ErrorType.NON_EXISTENT_SESSION));
        return ticketRepository.getTicketsBySessionAndBought(session, true).stream()
                .map(ticket -> conversionService.convert(ticket, TicketResponse.class))
                .toList();
    }

    @Override
    @Transactional
    public SessionResponse bookTicket(TicketRequest ticketDto) {
        Ticket ticket = getTicket(ticketDto);
        if (ticket.getBought())
            throw new ApplicationException(ErrorType.REPEAT_BOOKING);
        ticket.setBought(true);
        ticketRepository.save(ticket);
        Session session = ticket.getSession();
        return SessionResponse.builder()
                .time(new Time(session.getTimeAndDate().getTime()))
                .film(conversionService.convert(session.getFilm(), FilmDto.class))
                .date(session.getTimeAndDate())
                .cinemaHallNumber(session.getCinemaHallNumber())
                .numberOfTicketsAvailable(ticketRepository.getTicketsBySessionAndBought(session, false).size())
                .numberOfTicketsSold(ticketRepository.getTicketsBySessionAndBought(session, true).size())
                .build();
    }

    private Ticket getTicket(TicketRequest ticketDto) {
        Timestamp timestampOfSession = getTimestamp(ticketDto.getTime(), ticketDto.getDate());
        Session session = sessionRepository.findByDatetimeAndHallNumber(timestampOfSession, ticketDto.getCinemaHallNumber())
                .orElseThrow(() -> new ApplicationException(ErrorType.NON_EXISTENT_SESSION));
        return ticketRepository
                .getTicketBySessionAndNumber(session, ticketDto.getRowNumber(), ticketDto.getSeatNumber())
                .orElseThrow(() -> new ApplicationException(ErrorType.NON_EXISTENT_PLACE));
    }

    @Override
    @Transactional
    public SessionResponse unbookTicket(TicketRequest ticketDto) {
        Ticket ticket = getTicket(ticketDto);
        if (!ticket.getBought())
            throw new ApplicationException(ErrorType.FREE_SEAT);
        ticket.setBought(false);
        ticketRepository.save(ticket);
        Session session = ticket.getSession();
        return SessionResponse.builder()
                .time(new Time(session.getTimeAndDate().getTime()))
                .film(conversionService.convert(session.getFilm(), FilmDto.class))
                .date(session.getTimeAndDate())
                .cinemaHallNumber(session.getCinemaHallNumber())
                .numberOfTicketsAvailable(ticketRepository.getTicketsBySessionAndBought(session, false).size())
                .numberOfTicketsSold(ticketRepository.getTicketsBySessionAndBought(session, true).size())
                .build();
    }

    @Override
    @Transactional
    public UUID deleteSession(SessionSearchRequest sessionDto) {
        Timestamp timestampOfSession = Timestamp.valueOf(LocalDateTime
                .of(sessionDto.getDate().toLocalDate(), sessionDto.getTime().toLocalTime()));
        Session session = sessionRepository.findByDatetimeAndHallNumber(timestampOfSession, sessionDto.getCinemaHallNumber())
                .orElseThrow(() -> new ApplicationException(ErrorType.NON_EXISTENT_SESSION));
        ticketRepository.deleteAll(ticketRepository.getAllTicketsBySession(session));
        sessionRepository.delete(session);
        return session.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SessionResponse> searchSessions(SessionSearchFilter filter) {
        Time minTime;
        if (filter.getMinTime() != null)
            minTime = filter.getMinTime();
        else
            minTime = Time.valueOf("00:00:00");
        Time maxTime;
        if (filter.getMaxTime() != null)
            maxTime = filter.getMaxTime();
        else
            maxTime = Time.valueOf("23:59:59");
        Date minDate;
        if (filter.getMinDate() != null)
            minDate = filter.getMinDate();
        else
            minDate = Date.valueOf("1895-12-28");
        Date maxDate;
        if (filter.getMaxDate() != null)
            maxDate = filter.getMaxDate();
        else
            maxDate = Date.valueOf("3000-12-31");
        Timestamp minTimestamp = getTimestamp(minTime, minDate);
        log.info("Min time and date {}", minTimestamp);
        Timestamp maxTimestamp = getTimestamp(maxTime, maxDate);
        log.info("Max time and date {}", maxTimestamp);
        return sessionRepository.findSessionsByFilter(minTimestamp, maxTimestamp,
                filter.getCinemaHallNumber(), filter.getMovieName()).stream()
                .map(session -> SessionResponse.builder()
                        .time(new Time(session.getTimeAndDate().getTime()))
                        .film(conversionService.convert(session.getFilm(), FilmDto.class))
                        .date(session.getTimeAndDate())
                        .cinemaHallNumber(session.getCinemaHallNumber())
                        .numberOfTicketsAvailable(ticketRepository.getTicketsBySessionAndBought(session, false).size())
                        .numberOfTicketsSold(ticketRepository.getTicketsBySessionAndBought(session, true).size())
                        .build())
                .toList();
    }
}
