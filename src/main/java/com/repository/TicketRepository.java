package com.repository;

import com.entity.Session;
import com.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.relational.core.sql.In;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    @Query("SELECT t FROM Ticket t WHERE t.session = :session AND t.bought = :bought")
    List<Ticket> getTicketsBySessionAndBought(@Param("session")Session session,
                                              @Param("bought") boolean bought);

    @Query("SELECT t FROM Ticket t WHERE t.session = :session")
    List<Ticket> getAllTicketsBySession(@Param("session") Session session);

    @Query("SELECT t FROM Ticket t JOIN FETCH t.session WHERE t.session = :session " +
            "AND t.placeNumber = :place AND t.rowNumber = :row")
    Optional<Ticket> getTicketBySessionAndNumber(@Param("session") Session session,
                                                 @Param("row") Integer row,
                                                 @Param("place") Integer place);
}
