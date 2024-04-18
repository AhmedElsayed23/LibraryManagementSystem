package org.example.librarymanagementsystem.exceptions.bookExceptions;

public class NotAvailableBookException extends RuntimeException {
    public NotAvailableBookException(String message) {
        super(message);
    }
}
