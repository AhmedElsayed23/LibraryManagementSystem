package org.example.librarymanagementsystem.services.borrowingService;

import org.example.librarymanagementsystem.entities.Borrowing;

import java.time.LocalDate;

public interface BorrowingService {

    Borrowing borrowBook(Long bookId, Long patronId, LocalDate returnDate);

    boolean returnBook(Long bookId, Long patronId);
}
