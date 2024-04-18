package org.example.librarymanagementsystem.borrow;

import org.example.librarymanagementsystem.entities.Book;
import org.example.librarymanagementsystem.entities.Borrowing;
import org.example.librarymanagementsystem.entities.Patron;
import org.example.librarymanagementsystem.repositories.BorrowingRepository;
import org.example.librarymanagementsystem.services.bookService.BookService;
import org.example.librarymanagementsystem.services.borrowingService.BorrowingServiceImpl;
import org.example.librarymanagementsystem.services.patronService.PatronService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BorrowingServiceTest {

    @Mock
    private BorrowingRepository borrowingRepository;

    @Mock
    private BookService bookService;

    @Mock
    private PatronService patronService;

    @InjectMocks
    private BorrowingServiceImpl borrowingServiceImpl;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this); // Initialize the annotated fields
    }

    @Test
    public void testBorrowBook() {
        // Arrange
        Long bookId = 1L;
        Long patronId = 1L;
        LocalDate returnDate = LocalDate.now().plusDays(10);

        Book book = new Book();
        book.setId(bookId);

        Patron patron = new Patron();
        patron.setId(patronId);

        Borrowing borrowing = new Borrowing();
        borrowing.setBook(book);
        borrowing.setPatron(patron);
        borrowing.setBorrowingDate(LocalDate.now());
        borrowing.setReturnDate(returnDate);
        borrowing.setReturned(false);

        when(bookService.getBookById(bookId)).thenReturn(Optional.of(book));
        when(patronService.getPatronById(patronId)).thenReturn(Optional.of(patron));
        when(borrowingRepository.save(any(Borrowing.class))).thenReturn(borrowing);

        // Act
        Borrowing returnedBorrowing = borrowingServiceImpl.borrowBook(bookId, patronId, returnDate);

        // Assert
        assertEquals(borrowing.getId(), returnedBorrowing.getId());
        verify(bookService, times(1)).getBookById(bookId);
        verify(patronService, times(1)).getPatronById(patronId);
        verify(borrowingRepository, times(1)).save(any(Borrowing.class));
    }

    @Test
    public void testReturnBook() {
        // Arrange
        Long bookId = 1L;
        Long patronId = 1L;

        Borrowing borrowing = new Borrowing();
        borrowing.setBook(new Book());
        borrowing.setPatron(new Patron());
        borrowing.setReturned(false);

        when(borrowingRepository.findByBookIdAndPatronId(bookId, patronId)).thenReturn(Optional.of(borrowing));

        // Act
        boolean isReturned = borrowingServiceImpl.returnBook(bookId, patronId);

        // Assert
        assertTrue(isReturned);
        verify(borrowingRepository, times(1)).findByBookIdAndPatronId(bookId, patronId);
        verify(borrowingRepository, times(1)).save(any(Borrowing.class));
    }

}
