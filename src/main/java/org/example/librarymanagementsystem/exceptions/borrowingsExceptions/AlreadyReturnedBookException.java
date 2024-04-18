package org.example.librarymanagementsystem.exceptions.borrowingsExceptions;

public class AlreadyReturnedBookException extends RuntimeException {
    public AlreadyReturnedBookException(String message) {
        super(message);
    }
}
