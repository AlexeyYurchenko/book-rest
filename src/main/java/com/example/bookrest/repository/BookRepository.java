package com.example.bookrest.repository;

import com.example.bookrest.entity.Book;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book,Long>, JpaSpecificationExecutor<Book> {

    List<Book> findByCategoryId(Long CategoryId);

    Optional<Book> findByName (String name);

    List<Book> findByAuthor(String author);

    @EntityGraph(attributePaths = {"category"})
    List<Book> findAll();
}