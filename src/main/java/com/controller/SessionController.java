package com.controller;

import com.controller.request.SessionCreateRequest;
import com.controller.request.SessionSearchRequest;
import com.controller.request.TicketRequest;
import com.controller.response.SessionResponse;
import com.controller.response.TicketResponse;
import com.dto.SessionSearchFilter;
import com.service.SessionServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/sessions")
@Slf4j
@RequiredArgsConstructor
public class SessionController {
    private final SessionServiceImpl sessionService;

    @GetMapping
    public List<SessionResponse> getAllSessions() {
        return sessionService.getAllSessions();
    }

    @PostMapping
    public SessionResponse addSession(@Valid @RequestBody SessionCreateRequest session) {
        return sessionService.addSession(session);
    }

    @GetMapping("/tickets")
    public List<TicketResponse> getAllTickets(@Valid @RequestBody SessionSearchRequest session) {
        return sessionService.getAllTickets(session);
    }

    @GetMapping("/tickets/available")
    public List<TicketResponse> getAvailableTickets(@Valid @RequestBody SessionSearchRequest session) {
        return sessionService.getAvailableTickets(session);
    }

    @GetMapping("/tickets/notavailable")
    public List<TicketResponse> getNotAvailableTickets(@Valid @RequestBody SessionSearchRequest session) {
        return sessionService.getNotAvailableTickets(session);
    }

    @PutMapping("/tickets/book")
    public SessionResponse bookTicket(@Valid @RequestBody TicketRequest ticket) {
        return sessionService.bookTicket(ticket);
    }

    @PutMapping("/tickets/unbook")
    public SessionResponse unbookTicket(@Valid @RequestBody TicketRequest ticket) {
        return sessionService.unbookTicket(ticket);
    }

    @DeleteMapping
    public UUID deleteSession(@RequestBody @Valid SessionSearchRequest sessionSearchRequest) {
        return sessionService.deleteSession(sessionSearchRequest);
    }

    @GetMapping("/search")
    public List<SessionResponse> searchSessions(@Valid @RequestBody SessionSearchFilter request) {
        return sessionService.searchSessions(request);
    }

}
