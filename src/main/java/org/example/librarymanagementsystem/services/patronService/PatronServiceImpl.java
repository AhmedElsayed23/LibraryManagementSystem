package org.example.librarymanagementsystem.services.patronService;

import jakarta.transaction.Transactional;
import org.example.librarymanagementsystem.exceptions.Patron.PatronNotFoundException;
import org.example.librarymanagementsystem.entities.Patron;
import org.example.librarymanagementsystem.repositories.PatronRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatronServiceImpl implements PatronService{

    private final PatronRepository patronRepository;

    @Autowired
    public PatronServiceImpl(PatronRepository patronRepository) {
        this.patronRepository = patronRepository;
    }

    @Cacheable(value = "patrons") // Caching the result of this method
    @Override
    public List<Patron> getAllPatrons() {
        try {
            List<Patron> patrons = patronRepository.findAll();

            if (patrons.isEmpty()) {
                throw new PatronNotFoundException("No patrons found");
            }

            return patrons;
        } catch (PatronNotFoundException e) {
            throw new PatronNotFoundException(e.getMessage());
        } catch (DataAccessException e) {
            throw new RuntimeException("An error occurred while fetching the patrons", e);
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred", e);
        }
    }

    @Cacheable(value = "patrons", key = "#id") //
    @Override
    public Optional<Patron> getPatronById(Long id) {
        try {
            if (id == null || id <= 0) {
                throw new IllegalArgumentException("Patron ID cannot be null or less than or equal to 0");
            }

            Optional<Patron> patron = patronRepository.findById(id);

            if (patron.isPresent()) {
                return patron;
            } else {
                throw new PatronNotFoundException("Patron with ID " + id + " not found");
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (PatronNotFoundException e) {
            throw new PatronNotFoundException(e.getMessage());
        } catch (DataAccessException e) {
            throw new RuntimeException("An error occurred while fetching the patron", e);
        }
    }

    @Transactional
    @Override
    public Patron addPatron(Patron patron) {
        try {

            validatePatron(patron);

            if (patron.getId() != null) {
                patron.setId(null);
            }

            if (patronRepository.existsByEmail(patron.getEmail())) {
                throw new IllegalArgumentException("Patron with email " + patron.getEmail() + " already exists");
            }

            return patronRepository.save(patron);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (DataAccessException e) {
            throw new RuntimeException("An error occurred while adding the patron", e);
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred", e);
        }
    }

    @Transactional
    @Override
    public Patron updatePatron(Long id, Patron patron) {
        try {
            if (id == null || id <= 0) {
                throw new IllegalArgumentException("Patron ID cannot be null or less than or equal to 0");
            }

            validatePatron(patron);

            Optional<Patron> existingPatron = patronRepository.findById(id);

            if (existingPatron.isPresent()) {
                Patron patronToUpdate = existingPatron.get();

                patronToUpdate.setName(patron.getName());
                patronToUpdate.setEmail(patron.getEmail());
                patronToUpdate.setPhone(patron.getPhone());
                patronToUpdate.setAddress(patron.getAddress());

                return patronRepository.save(patronToUpdate);
            } else {
                throw new PatronNotFoundException("Patron with ID " + id + " not found");
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (PatronNotFoundException e) {
            throw new PatronNotFoundException(e.getMessage());
        } catch (DataAccessException e) {
            throw new RuntimeException("An error occurred while updating the patron", e);
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred", e);
        }
    }

    @Transactional // This annotation is used to indicate that the following method should be executed within a transaction
    @Override
    public boolean deletePatron(Long id) {
        try {
            if (id == null || id <= 0) {
                throw new IllegalArgumentException("Patron ID cannot be null or less than or equal to 0");
            }

            Optional<Patron> patron = patronRepository.findById(id);

            if (patron.isEmpty()) {
                throw new PatronNotFoundException("Patron with ID " + id + " not found");
            }

            patronRepository.deleteById(id);
            return true;
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(e.getMessage());
            } catch (PatronNotFoundException e) {
                throw new PatronNotFoundException(e.getMessage());
            } catch (DataAccessException e) {
                throw new RuntimeException("An error occurred while deleting the patron", e);
            } catch (Exception e) {
                throw new RuntimeException("An unexpected error occurred", e);
        }

    }

    private void validatePatron(Patron patron) {
        if (patron == null) {
            throw new IllegalArgumentException("Patron cannot be null");
        }

        if (patron.getName() == null || patron.getName().isEmpty()) {
            throw new IllegalArgumentException("Patron name cannot be null or empty");
        }

        if (patron.getEmail() == null || patron.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Patron email cannot be null or empty");
        }

        if (patron.getPhone() == null || patron.getPhone().isEmpty()) {
            throw new IllegalArgumentException("Patron phone cannot be null or empty");
        }

        if (patron.getAddress() == null || patron.getAddress().isEmpty()) {
            throw new IllegalArgumentException("Patron address cannot be null or empty");
        }
    }

}
