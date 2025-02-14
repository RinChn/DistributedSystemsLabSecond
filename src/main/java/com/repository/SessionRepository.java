package com.repository;

import com.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SessionRepository extends JpaRepository<Session, UUID> {
    @Query("SELECT s FROM Session s WHERE s.cinemaHallNumber = :hallNumber AND s.timeAndDate = :datetime")
    Optional<Session> findByDatetimeAndHallNumber(@Param("datetime") Timestamp datetime,
                                                  @Param("hallNumber") Integer hallNumber);

    @Query("SELECT s FROM Session s WHERE s.timeAndDate >= :minTimestamp AND s.timeAndDate <= :maxTimestamp " +
            "AND (:hallNumber IS NULL OR s.cinemaHallNumber = :hallNumber) " +
            "AND (:movieName IS NULL OR s.film.title LIKE %:movieName%)")
    List<Session> findSessionsByFilter(@Param("minTimestamp") Timestamp minTimeStamp,
                                       @Param("maxTimestamp") Timestamp maxTimeStamp,
                                       @Param("hallNumber") Integer hallNumber,
                                       @Param("movieName") String movieName);
}
