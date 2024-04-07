package com.example.bookrest.service;

import com.example.bookrest.entity.Book;
import com.example.bookrest.entity.Category;
import com.example.bookrest.exception.EntityNotFoundException;
import com.example.bookrest.repository.BookRepository;
import com.example.bookrest.repository.CategoryRepository;
import com.example.bookrest.utils.BeanUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    public List<Book> findAll() {
        log.debug("BookService -> findAll");
        return bookRepository.findAll();
    }

    public Book findByBookId(Long id) {
        log.debug("BookService -> findByBookId id = {}", id);
        return bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(MessageFormat
                .format("Book with ID {0} not found", id)));
    }

    public Category findByCategoryId(Long id) {
        log.debug("BookService -> findByCategoryId id = {}", id);
        return categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(MessageFormat
                .format("Category with ID {0} not found", id)));
    }

    public List<Book> findByCategoryName(String name) {
        log.debug("BookService -> findByCategoryName name = {}", name);
        Optional<Category> category = categoryRepository.findByCategoryName(name);
        if (category.isPresent()) {
            return bookRepository.findByCategoryId(category.get().getId());
        } else {
            throw new EntityNotFoundException(MessageFormat.format("Category with name {0} not found", name));
        }
    }

    public Book findByBookName(String name) {
        log.debug("BookService -> findByBookName name = {}", name);
        return bookRepository.findByName(name).orElseThrow(() ->
                new EntityNotFoundException(MessageFormat.format("Book with name {0} not found", name)));
    }

    public Book findByBookAuthor(String author) {
        log.debug("BookService -> findByBookAuthor author = {}", author);
        return bookRepository.findByAuthor(author).orElseThrow(() ->
                new EntityNotFoundException(MessageFormat.format("Book with author {0} not found", author)));
    }

    public Book save(Book book) {
        log.debug("BookService -> save");
        return bookRepository.save(book);
    }

    public Book update(Book book) {
        log.debug("BookService -> update");
        Category category = findByCategoryId(book.getId());
        Book existedBook = findByBookId(book.getId());
        BeanUtils.copyNonNullProperties(book, existedBook);
        existedBook.setCategory(category);
        return bookRepository.save(existedBook);
    }

    public void deleteByBookId(Long id) {
        log.debug("BookService -> deleteById");
        bookRepository.deleteById(id);
    }
}