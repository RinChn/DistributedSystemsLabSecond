package com.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "tickets")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    UUID id;
    @Column(name = "row_number", nullable = false)
    Integer rowNumber;
    @Column(name = "place_number", nullable = false)
    Integer placeNumber;
    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    Session session;
    @Column(name = "bought", nullable = false, columnDefinition = "Boolean default false")
    @Builder.Default
    Boolean bought = false;
}
