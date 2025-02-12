package com.repository;


import com.entity.Director;
import com.entity.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FilmRepository extends JpaRepository<Film, UUID> {
    @Query("SELECT f FROM Film f WHERE f.title = :title AND f.director = :director")
    Optional<Film> findByTitleAndDirector(@Param("title") String title, @Param("director") Director director);
}
