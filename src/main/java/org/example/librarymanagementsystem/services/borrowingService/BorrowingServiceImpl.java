package org.example.librarymanagementsystem.services.borrowingService;

import jakarta.transaction.Transactional;
import org.example.librarymanagementsystem.exceptions.Patron.PatronNotFoundException;
import org.example.librarymanagementsystem.exceptions.bookExceptions.BookNotFoundException;
import org.example.librarymanagementsystem.exceptions.bookExceptions.NotAvailableBookException;
import org.example.librarymanagementsystem.entities.Book;
import org.example.librarymanagementsystem.entities.Borrowing;
import org.example.librarymanagementsystem.entities.Patron;
import org.example.librarymanagementsystem.exceptions.borrowingsExceptions.AlreadyReturnedBookException;
import org.example.librarymanagementsystem.repositories.BorrowingRepository;
import org.example.librarymanagementsystem.services.bookService.BookService;
import org.example.librarymanagementsystem.services.patronService.PatronService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class BorrowingServiceImpl implements BorrowingService {

    private final BorrowingRepository borrowingRepository;
    private final BookService bookService;
    private final PatronService patronService;

    @Autowired
    public BorrowingServiceImpl(BorrowingRepository borrowingRepository, BookService bookService, PatronService patronService) {
        this.borrowingRepository = borrowingRepository;
        this.bookService = bookService;
        this.patronService = patronService;
    }

    @Transactional
    @Override
    public Borrowing borrowBook(Long bookId, Long patronId, LocalDate returnDate) {
        try {
            if (bookId == null || bookId <= 0) {
                throw new IllegalArgumentException("Book ID cannot be null or less than or equal to 0");
            }

            if (patronId == null || patronId <= 0) {
                throw new IllegalArgumentException("Patron ID cannot be null or less than or equal to 0");
            }

            Optional<Book> book = bookService.getBookById(bookId);

            if (book.isEmpty()) {
                throw new BookNotFoundException("Book with ID " + bookId + " not found");
            }

            if (book.get().isAvailable()) {
                throw new NotAvailableBookException("Book with ID " + bookId + " is not available");
            }

            Optional<Patron> patron = patronService.getPatronById(patronId);

            if (patron.isEmpty()) {
                throw new PatronNotFoundException("Patron with ID " + patronId + " not found");
            }

            if (returnDate == null) {
                throw new IllegalArgumentException("Return date cannot be null");
            }

            if (returnDate.isBefore(java.time.LocalDate.now())) {
                throw new IllegalArgumentException("Return date cannot be before today");
            }

            if (returnDate.isAfter(java.time.LocalDate.now().plusDays(14))) {
                throw new IllegalArgumentException("Return date cannot be more than 14 days from today");
            }

            Borrowing borrowing = new Borrowing();
            borrowing.setBook(book.get());
            borrowing.setPatron(patron.get());
            borrowing.setBorrowingDate(java.time.LocalDate.now());
            borrowing.setReturnDate(returnDate);
            borrowing.setReturned(false);

            return borrowingRepository.save(borrowing);

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (BookNotFoundException e) {
            throw new BookNotFoundException(e.getMessage());
        } catch (NotAvailableBookException e) {
            throw new NotAvailableBookException(e.getMessage());
        } catch (PatronNotFoundException e) {
            throw new PatronNotFoundException(e.getMessage());
        } catch (DataAccessException e) {
            throw new RuntimeException("An error occurred while borrowing the book", e);
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred", e);
        }
    }

    @Transactional
    @Override
    public boolean returnBook(Long bookId, Long patronId) {
        try {
            if (bookId == null || bookId <= 0) {
                throw new IllegalArgumentException("Book ID cannot be null or less than or equal to 0");
            }

            if (patronId == null || patronId <= 0) {
                throw new IllegalArgumentException("Patron ID cannot be null or less than or equal to 0");
            }

            Optional<Borrowing> borrowing = borrowingRepository.findByBookIdAndPatronId(bookId, patronId);

            if (borrowing.isEmpty()) {
                throw new RuntimeException("No borrowing found for book with ID " + bookId + " and patron with ID " + patronId);
            }

            if (borrowing.get().isReturned()) {
                throw new AlreadyReturnedBookException("Book with ID " + bookId + " has already been returned by patron with ID " + patronId);
            }

            borrowing.get().setReturned(true);
            borrowingRepository.save(borrowing.get());

            return true;

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred", e);
        }
    }
}
