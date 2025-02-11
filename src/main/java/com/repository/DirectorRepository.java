package com.repository;

import com.entity.Director;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.Optional;

@Repository
public interface DirectorRepository extends JpaRepository<Director, UUID> {
    @Query("SELECT d FROM Director d WHERE d.firstName = :first AND d.lastName = :last")
    Optional<Director> findByName(@Param("first") String firstName, @Param("second") String lastName);
}
