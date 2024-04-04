package com.example.bookrest.repository;

import com.example.bookrest.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {

    List<Book> findByCategoryId(Long CategoryId);

    Optional<Book> findByName (String name);

    Optional<Book> findByAuthor(String author);
}
