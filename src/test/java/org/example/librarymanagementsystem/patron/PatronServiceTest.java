package org.example.librarymanagementsystem.patron;

import org.example.librarymanagementsystem.entities.Patron;
import org.example.librarymanagementsystem.repositories.PatronRepository;
import org.example.librarymanagementsystem.services.patronService.PatronServiceImpl;
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

public class PatronServiceTest {

    @Mock // Create a mock object of PatronRepository class to test PatronServiceImpl class
    private PatronRepository patronRepository;

    @InjectMocks // Create an instance of PatronServiceImpl class and inject the mock object patronRepository into it
    private PatronServiceImpl patronServiceImpl;

    @BeforeEach // This method will be called before each test
    public void init() {
        MockitoAnnotations.openMocks(this); // Initialize the annotated fields
    }

    @Test
    public void testGetAllPatrons() {
        // Arrange
        Patron patron1 = new Patron();
        patron1.setId(1L);
        Patron patron2 = new Patron();
        patron2.setId(2L);
        when(patronRepository.findAll()).thenReturn(List.of(patron1, patron2)); // Mock the findAll() method of patronRepository to return a list of 2 patrons

        // Act
        List<Patron> patrons = patronServiceImpl.getAllPatrons();

        // Assert
        assertEquals(2, patrons.size()); // Check if the size of the list is 2
        verify(patronRepository, times(1)).findAll(); // Verify that the findAll() method of patronRepository is called only once
    }

    @Test
    public void testGetPatronById() {
        // Arrange
        Patron patron = new Patron();
        patron.setId(1L);
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));

        // Act
        Optional<Patron> returnedPatron = patronServiceImpl.getPatronById(1L);

        // Assert
        assertTrue(returnedPatron.isPresent()); // Check if the returnedPatron is not empty
        assertEquals(patron.getId(), returnedPatron.get().getId());
        verify(patronRepository, times(1)).findById(1L);
    }

    @Test
    public void testAddPatron() {
        // Arrange
        Patron patron = new Patron();
        patron.setId(1L);
        patron.setName("John Doe");
        patron.setEmail("John@gmail");
        patron.setPhone("5465545");
        patron.setAddress("s12 asdsd sd");
        when(patronRepository.save(patron)).thenReturn(patron);

        // Act
        Patron returnedPatron = patronServiceImpl.addPatron(patron);

        // Assert
        assertEquals(patron.getId(), returnedPatron.getId());
        verify(patronRepository, times(1)).save(patron);
    }

    @Test
    public void testUpdatePatron() {
        // Arrange
        Patron patron = new Patron();
        patron.setId(1L);
        patron.setName("John Doe");
        patron.setEmail("john@gmail.com");
        patron.setPhone("1234567890");
        patron.setAddress("123 Main St, New York, NY");
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron)); // Mock the existsById() method of patronRepository to return true
        when(patronRepository.save(patron)).thenReturn(patron);

        // Act
        Patron returnedPatron = patronServiceImpl.updatePatron(1L, patron);

        // Assert
        assertEquals(patron.getId(), returnedPatron.getId());
        verify(patronRepository, times(1)).findById(1L);
        verify(patronRepository, times(1)).save(patron);
    }

    @Test
    public void testDeletePatron() {
        // Arrange
        when(patronRepository.findById(1L)).thenReturn(Optional.of(new Patron()));

        // Act
        boolean result = patronServiceImpl.deletePatron(1L);

        // Assert
        assertTrue(result); // Check if the result is true
        verify(patronRepository, times(1)).findById(1L);
        verify(patronRepository, times(1)).deleteById(1L);
    }
}
