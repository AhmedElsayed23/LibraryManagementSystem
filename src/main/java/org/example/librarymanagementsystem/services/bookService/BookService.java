package org.example.librarymanagementsystem.services.bookService;

import org.example.librarymanagementsystem.entities.Book;

import java.util.Optional;
import java.util.List;

public interface BookService {

    List<Book> getAllBooks();
    Optional<Book> getBookById(Long id);
    Book addBook(Book id);
    Book updateBook(Long id, Book book);
    boolean deleteBook(Long id);
}
