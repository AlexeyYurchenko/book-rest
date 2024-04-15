package com.example.bookrest.web.controller;

import com.example.bookrest.entity.Book;
import com.example.bookrest.mapper.BookMapper;
import com.example.bookrest.service.BookService;
import com.example.bookrest.web.model.BookResponse;
import com.example.bookrest.web.model.UpsertBookRequest;
import com.example.bookrest.web.model.UpsertNameAndAuthorRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;

    @GetMapping
    public ResponseEntity<List<BookResponse>> findAll() {
        return ResponseEntity.ok(bookService.findAll().stream().map(bookMapper::bookToResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(bookMapper.bookToResponse(bookService.findByBookId(id)));
    }

    @GetMapping("/category")
    public ResponseEntity<List<BookResponse>> findAllBooksByCategory(@RequestParam("name") String categoryName) {
        return ResponseEntity.ok(bookService.findAllByCategory(categoryName).stream().map(bookMapper::bookToResponse).toList());
    }

    @GetMapping("/filter")
    public ResponseEntity<BookResponse> findBookByAuthorAndTitle(@RequestBody @Valid UpsertNameAndAuthorRequest request) {
        return ResponseEntity.ok(bookMapper.bookToResponse
                (bookService.findByAuthorAndName(request.getAuthor(), request.getName())));
    }

    @PostMapping
    public ResponseEntity<BookResponse> create(@RequestBody @Valid UpsertBookRequest request) {
        Book newBook = bookService.create(bookMapper.requestToBook(request), request.getCategoryName());
        return ResponseEntity.status(HttpStatus.CREATED).body(bookMapper.bookToResponse(newBook));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> update(@PathVariable("id") Long bookId,
                                               @RequestBody @Valid UpsertBookRequest request) {
        Book updateBook = bookService.update(bookMapper.requestToBook(request),bookId);
        return ResponseEntity.ok(bookMapper.bookToResponse(updateBook));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}