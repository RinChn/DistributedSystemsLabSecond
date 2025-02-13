package com.repository;


import com.entity.Director;
import com.entity.Film;
import com.util.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FilmRepository extends JpaRepository<Film, UUID> {
    @Query("SELECT f FROM Film f WHERE f.title = :title AND f.director = :director")
    Optional<Film> findByTitleAndDirector(@Param("title") String title, @Param("director") Director director);

    @Query("SELECT f FROM Film f WHERE (:title IS NULL OR f.title LIKE %:title%)" +
            "AND (:director IS NULL OR f.director = :director)" +
            "AND (:maxYearReleased IS NULL OR f.yearReleased <= :maxYearReleased)" +
            "AND (:minYearReleased IS NULL OR f.yearReleased >= :minYearReleased)" +
            "AND (:maxLength IS NULL OR f.length <= :maxLength)" +
            "AND (:minLength IS NULL OR f.length >= :minLength)" +
            "AND (:genre IS NULL OR f.genre = :genre)")
    List<Film> searchFilm(@Param("title") String title, @Param("director") Director director,
                          @Param("maxYearReleased")Integer maxYearReleased, @Param("minYearReleased") Integer minYearReleased,
                          @Param("maxLength") Integer maxLength, @Param("minLength") Integer minLength,
                          @Param("genre") Genre genre);
}
