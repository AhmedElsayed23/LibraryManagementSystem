package org.example.librarymanagementsystem.controllers;

import org.example.librarymanagementsystem.entities.Borrowing;
import org.example.librarymanagementsystem.exceptions.Patron.PatronNotFoundException;
import org.example.librarymanagementsystem.exceptions.bookExceptions.BookNotFoundException;
import org.example.librarymanagementsystem.exceptions.bookExceptions.NotAvailableBookException;
import org.example.librarymanagementsystem.services.borrowingService.BorrowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;

@Controller
@RequestMapping("/api")
public class BorrowingsController {

    private final BorrowingService borrowingsService;

    @Autowired
    public BorrowingsController(BorrowingService borrowingService) {
        this.borrowingsService = borrowingService;
    }

    @PostMapping("/borrow/{bookId}/patron/{patronId}")
    public ResponseEntity<Borrowing> borrowBook(@PathVariable Long bookId, @PathVariable Long patronId) {
        try {
            LocalDate returnDate = LocalDate.now().plusDays(14);
            Borrowing borrowing = borrowingsService.borrowBook(bookId, patronId, returnDate);
            return ResponseEntity.ok(borrowing);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (BookNotFoundException | PatronNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (NotAvailableBookException e) {
            return ResponseEntity.status(409).build(); // Conflict
        } catch (Exception e) {
            return ResponseEntity.status(500).build(); // Internal Server Error
        }
    }

    @PutMapping("/return/{bookId}/patron/{patronId}")
    public ResponseEntity<Boolean> returnBook(@PathVariable Long bookId, @PathVariable Long patronId) {
        try {
            Boolean returned = borrowingsService.returnBook(bookId, patronId);
            return ResponseEntity.ok(returned);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (BookNotFoundException | PatronNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build(); // Internal Server Error
        }
    }
}
