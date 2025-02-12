package com.service;

import com.dto.*;

import java.util.List;
import java.util.UUID;

public interface SessionService {
    List<SessionResponse> getAllSessions();
    SessionResponse addSession(SessionCreateRequest session);
    List<TicketResponse> getAllTickets(SessionSearchRequest session);
    List<TicketResponse> getAvailableTickets(SessionSearchRequest session);
    List<TicketResponse> getNotAvailableTickets(SessionSearchRequest session);
    SessionResponse bookTicket(TicketRequest ticket);
    SessionResponse unbookTicket(TicketRequest ticket);
    UUID deleteSession(SessionSearchRequest session);
}
