package com.repository;

import com.entity.Director;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface DirectorRepository extends JpaRepository<Director, UUID> {
    @Query("SELECT d FROM Director d WHERE d.firstName = :first AND d.lastName = :last")
    Optional<Director> findByName(@Param("first") String firstName, @Param("last") String lastName);
    @Query("SELECT d FROM Director d WHERE " +
            "(LOWER(d.firstName) LIKE LOWER(CONCAT(:first, '%')) " +
            "AND LOWER(d.lastName) LIKE LOWER(CONCAT(:last, '%'))) " +
            "OR (LOWER(d.firstName) LIKE LOWER(CONCAT(:last, '%')) " +
            "AND LOWER(d.lastName) LIKE LOWER(CONCAT(:first, '%')))")
    Optional<Director> findByTwoNamesLike(@Param("first") String firstName, @Param("last") String lastName);

    @Query("SELECT d FROM Director d WHERE " +
            "LOWER(d.firstName) LIKE LOWER(CONCAT(:name, '%')) " +
            "OR LOWER(d.lastName) LIKE LOWER(CONCAT(:name, '%'))")
    List<Director> searchByFirstOrLastName(@Param("name") String name);
}
