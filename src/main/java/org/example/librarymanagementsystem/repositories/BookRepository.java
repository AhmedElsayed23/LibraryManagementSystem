package org.example.librarymanagementsystem.repositories;

import org.example.librarymanagementsystem.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
