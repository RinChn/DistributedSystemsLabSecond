package com.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "sessions")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    UUID id;
    @Column(name = "date_and_time", nullable = false, columnDefinition = "timestamp default CURRENT_TIMESTAMP")
            @Builder.Default
    Timestamp timeAndDate = new Timestamp(System.currentTimeMillis());
    @Column(name = "cinema_hall_number", nullable = false, columnDefinition = "Integer default 1")
            @Builder.Default
    Integer cinemaHallNumber = 1;
    @ManyToOne
    @JoinColumn(name = "film_id", nullable = false)
    Film film;
}
