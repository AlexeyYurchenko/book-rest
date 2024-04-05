package com.example.bookrest.web.controller;

import com.example.bookrest.entity.Book;
import com.example.bookrest.mapper.BookMapper;
import com.example.bookrest.service.BookService;
import com.example.bookrest.web.model.BookListResponse;
import com.example.bookrest.web.model.BookResponse;
import com.example.bookrest.web.model.UpsertBookRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;

    @GetMapping
    public ResponseEntity<BookListResponse> findAll() {
        return ResponseEntity.ok(bookMapper.bookListToResponseList(bookService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(bookMapper.bookToResponse(bookService.findByBookId(id)));
    }

    @GetMapping("/category")
    public ResponseEntity<BookListResponse> findBooksByCategory(@RequestParam("name") String categoryName) {
        return ResponseEntity.ok(bookMapper.bookListToResponseList(bookService.findByCategoryName(categoryName)));
    }

    @GetMapping("/book-name")
    public ResponseEntity<BookResponse> findBookByName(@RequestParam("name") String name) {
        return ResponseEntity.ok(bookMapper.bookToResponse(bookService.findByBookName(name)));
    }

    @GetMapping("/book-author")
    public ResponseEntity<BookResponse> findBookByAuthor(@RequestParam("author") String author) {
        return ResponseEntity.ok((bookMapper.bookToResponse(bookService.findByBookAuthor(author))));
    }

    @PostMapping
    public ResponseEntity<BookResponse> create(@RequestBody UpsertBookRequest request) {
        Book newBook = bookService.save(bookMapper.requestToBook(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(bookMapper.bookToResponse(newBook));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> update(@PathVariable Long id, @RequestBody UpsertBookRequest request) {
        Book updateBook = bookService.update(bookMapper.requestToBook(id,request));
        return ResponseEntity.ok(bookMapper.bookToResponse(updateBook));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        bookService.deleteByBookId(id);
        return ResponseEntity.noContent().build();
    }
}