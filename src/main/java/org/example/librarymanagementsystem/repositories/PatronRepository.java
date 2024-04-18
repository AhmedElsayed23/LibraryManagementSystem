package org.example.librarymanagementsystem.repositories;

import org.example.librarymanagementsystem.entities.Patron;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PatronRepository extends JpaRepository<Patron, Long> {

    @Query("SELECT P FROM Patron P WHERE P.email = ?1") // JPQL query to find a patron by email address in the database
    boolean existsByEmail(String email);
}
