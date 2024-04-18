package org.example.librarymanagementsystem.controllers;

import org.example.librarymanagementsystem.exceptions.Patron.PatronNotFoundException;
import org.example.librarymanagementsystem.entities.Patron;
import org.example.librarymanagementsystem.services.patronService.PatronService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api")
public class PatronController {

    private final PatronService patronService;

    @Autowired
    public PatronController(PatronService patronService) {
        this.patronService = patronService;
    }

    @GetMapping("/patrons")
    public ResponseEntity<List<Patron>> getAllPatrons() {
        try {
            List<Patron> patrons = patronService.getAllPatrons();
            return ResponseEntity.ok(patrons);
        } catch (PatronNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/patrons/{id}")
    public ResponseEntity<Patron> getPatronById(@PathVariable Long id) {
        try {

            Optional<Patron> patron = patronService.getPatronById(id);

            return patron.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (PatronNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/patrons")
    public ResponseEntity<Patron> addPatron(@RequestBody Patron patron) {
        try {
            Patron newPatron = patronService.addPatron(patron);

            if (newPatron == null) {
                return ResponseEntity.badRequest().build();
            }

            return ResponseEntity.ok(newPatron);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/patrons/{id}")
    public ResponseEntity<Patron> updatePatron(@PathVariable Long id, @RequestBody Patron patron) {
        try {
            Patron updatedPatron = patronService.updatePatron(id, patron);

            if (updatedPatron == null) {
                return ResponseEntity.badRequest().build();
            }

            return ResponseEntity.ok(updatedPatron);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (PatronNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/patrons/{id}")
    public ResponseEntity<Boolean> deletePatron(@PathVariable Long id) {
        try {
            boolean deleted = patronService.deletePatron(id);

            if (!deleted) {
                return ResponseEntity.badRequest().build();
            }

            return ResponseEntity.ok(true);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
