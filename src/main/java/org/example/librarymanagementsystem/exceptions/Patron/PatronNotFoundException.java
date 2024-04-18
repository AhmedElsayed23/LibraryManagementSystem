package org.example.librarymanagementsystem.exceptions.Patron;

public class PatronNotFoundException extends RuntimeException {
    public PatronNotFoundException(String message) {
        super(message);
    }
}
