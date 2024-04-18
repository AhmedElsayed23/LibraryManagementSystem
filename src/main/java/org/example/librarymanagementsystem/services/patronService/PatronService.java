package org.example.librarymanagementsystem.services.patronService;

import org.example.librarymanagementsystem.entities.Patron;

import java.util.Optional;
import java.util.List;

public interface PatronService {

    List<Patron> getAllPatrons();
    Optional<Patron> getPatronById(Long id);
    Patron addPatron(Patron patron);
    Patron updatePatron(Long id, Patron patron);
    boolean deletePatron(Long id);

}
