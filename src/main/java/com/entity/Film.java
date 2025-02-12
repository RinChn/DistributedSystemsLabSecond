package com.entity;

import com.util.Genre;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;

import java.util.UUID;

@Entity
@Table(name = "films")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    UUID id;
    @Column(name = "title", nullable = false)
    String title;
    @ManyToOne
    @JoinColumn(name = "director_id", nullable = false)
    Director director;
    @Column(name = "year_release", nullable = false)
    Integer yearReleased;
    @Column(name = "length", nullable = false)
            @Check(constraints = "length >= 0")
    Integer length;
    @Column(name = "genre", nullable = false)
            @Enumerated(EnumType.STRING)
    Genre genre;
}
