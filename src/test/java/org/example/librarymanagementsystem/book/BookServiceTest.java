package org.example.librarymanagementsystem.book;

import org.example.librarymanagementsystem.entities.Book;
import org.example.librarymanagementsystem.repositories.BookRepository;
import org.example.librarymanagementsystem.services.bookService.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookServiceImpl;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this); // Initialize the annotated fields
    }

    @Test
    public void testGetAllBooks() {
        // Arrange
        Book book1 = new Book();
        book1.setId(1L);
        Book book2 = new Book();
        book2.setId(2L);
        when(bookRepository.findAll()).thenReturn(List.of(book1, book2));

        // Act
        List<Book> books = bookServiceImpl.getAllBooks();

        // Assert
        assertEquals(2, books.size());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    public void testGetBookById() {
        // Arrange
        Book book = new Book();
        book.setId(1L);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        // Act
        Optional<Book> returnedBook = bookServiceImpl.getBookById(1L);

        // Assert
        assertTrue(returnedBook.isPresent());
        assertEquals(book.getId(), returnedBook.get().getId());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    public void testAddBook() {
        // Arrange
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Book Title");
        book.setAuthor("Book Author");
        book.setIsbn("1234567890");
        book.setPublicationYear(2021);
        book.setPages(100);

        when(bookRepository.save(book)).thenReturn(book);

        // Act
        Book returnedBook = bookServiceImpl.addBook(book);

        // Assert
        assertEquals(book.getId(), returnedBook.getId());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    public void testUpdateBook() {
        // Arrange
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Book Title");
        book.setAuthor("Book Author");
        book.setIsbn("1234567890");
        book.setPublicationYear(2021);
        book.setPages(100);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);

        // Act
        Book returnedBook = bookServiceImpl.updateBook(1L, book);

        // Assert
        assertEquals(book.getId(), returnedBook.getId());
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    public void testDeleteBook() {
        // Arrange
        when(bookRepository.findById(1L)).thenReturn(Optional.of(new Book()));

        // Act
        bookServiceImpl.deleteBook(1L);

        // Assert
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).deleteById(1L);
    }



}
