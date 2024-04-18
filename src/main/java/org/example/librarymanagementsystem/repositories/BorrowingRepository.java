package org.example.librarymanagementsystem.repositories;

import org.example.librarymanagementsystem.entities.Borrowing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BorrowingRepository extends JpaRepository<Borrowing, Long> {

    @Query("SELECT b FROM Borrowing b WHERE b.book.id = ?1 AND b.patron.id = ?2")
    Optional<Borrowing> findByBookIdAndPatronId(Long bookId, Long patronId);
}
