package org.example.librarymanagementsystem.services.bookService;

import jakarta.transaction.Transactional;
import org.example.librarymanagementsystem.exceptions.bookExceptions.BookNotFoundException;
import org.example.librarymanagementsystem.entities.Book;
import org.example.librarymanagementsystem.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Cacheable(value = "books") // Caching the result of this method
    @Override
    public List<Book> getAllBooks() {
        try {
            List<Book> books = bookRepository.findAll();

            if (books.isEmpty()) {
                throw new BookNotFoundException("No books found");
            }

            return books;
        } catch (BookNotFoundException e) {
            throw new BookNotFoundException(e.getMessage());
        } catch (DataAccessException e) {
            throw new RuntimeException("An error occurred while fetching the books", e);
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred", e);
        }
    }

    @Cacheable(value = "books", key = "#id") // Caching the result of this method with the key as the book ID
    @Override
    public Optional<Book> getBookById(Long id) {
        try {
            if (id == null || id <= 0) {
                throw new IllegalArgumentException("Book ID cannot be null or less than or equal to 0");
            }

            Optional<Book> book = bookRepository.findById(id);

            if (book.isPresent()) {
                return book;
            } else {
                throw new BookNotFoundException("Book with ID " + id + " not found");
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (BookNotFoundException e) {
            throw new BookNotFoundException(e.getMessage());
        } catch (DataAccessException e) {
            throw new RuntimeException("An error occurred while fetching the book", e);
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred", e);
        }
    }

    @Transactional
    @Override
    public Book addBook(Book book) {
        try {

            // Validate the book
            validateBook(book);

            if (book.getId() != null) {
                book.setId(null);
            }

            book.setAvailable(true);

            // Save the book
            return bookRepository.save(book);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (DataAccessException e) {
            throw new RuntimeException("An error occurred while adding the book", e);
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred", e);
        }
    }

    @Transactional
    @Override
    public Book updateBook(Long id, Book book) {
        try {

            // Validate the book ID
            if (id == null || id <= 0) {
                throw new IllegalArgumentException("Book ID cannot be null or less than or equal to 0");
            }

            // Validate the book
            validateBook(book);

            // Fetch the book
            Optional<Book> existingBook = bookRepository.findById(id);

            if (existingBook.isPresent()) {
                // Update the book
                existingBook.get().setTitle(book.getTitle());
                existingBook.get().setAuthor(book.getAuthor());
                existingBook.get().setPublicationYear(book.getPublicationYear());
                existingBook.get().setIsbn(book.getIsbn());
                existingBook.get().setGenre(book.getGenre());
                existingBook.get().setPublisher(book.getPublisher());
                existingBook.get().setPages(book.getPages());
                existingBook.get().setAvailable(book.isAvailable());

                // Save the book
                return bookRepository.save(existingBook.get());
            } else {
                throw new BookNotFoundException("Book with ID " + id + " not found");
            }

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (BookNotFoundException e) {
            throw new BookNotFoundException(e.getMessage());
        } catch (DataAccessException e) {
            throw new RuntimeException("An error occurred while updating the book", e);
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred", e);
        }
    }

    @Transactional
    @Override
    public boolean deleteBook(Long id) {
        try {

            if (id == null || id <= 0) {
                throw new IllegalArgumentException("Book ID cannot be null or less than or equal to 0");
            }

            Optional<Book> book = bookRepository.findById(id);

            if (book.isEmpty()) {
                throw new BookNotFoundException("Book with ID " + id + " not found");
            }

            bookRepository.deleteById(id);
            return true;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (BookNotFoundException e) {
            throw new BookNotFoundException(e.getMessage());
        } catch (DataAccessException e) {
            throw new RuntimeException("An error occurred while deleting the book", e);
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred", e);
        }
    }

    private void validateBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }

        if (book.getTitle() == null || book.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Book title cannot be null or empty");
        }

        if (book.getPublicationYear() <= 0) {
            throw new IllegalArgumentException("Book publication year cannot be less than or equal to 0");
        }

        if (book.getPages() <= 0) {
            throw new IllegalArgumentException("Book pages cannot be less than or equal to 0");
        }
    }
}
