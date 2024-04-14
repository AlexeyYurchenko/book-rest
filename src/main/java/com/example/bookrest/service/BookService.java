package com.example.bookrest.service;

import com.example.bookrest.configuration.properties.BookCacheProperties;
import com.example.bookrest.entity.Book;
import com.example.bookrest.entity.Category;
import com.example.bookrest.exception.EntityNotFoundException;
import com.example.bookrest.repository.BookRepository;
import com.example.bookrest.utils.BeanUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@EnableCaching
@CacheConfig(cacheManager = "redisCacheManager")
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;


    @Cacheable(BookCacheProperties.CacheNames.FIND_ALL_BOOKS)
    public List<Book> findAll() {
        log.debug("BookService -> findAll");
        return bookRepository.findAll();
    }
    @Cacheable(cacheNames = BookCacheProperties.CacheNames.FIND_BY_BOOK_ID, key = "#id")
    public Book findByBookId(Long id) {
        log.debug("BookService -> findByBookId id = {}", id);
        return bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(MessageFormat
                .format("Book with ID {0} not found", id)));
    }

    @Cacheable(cacheNames = BookCacheProperties.CacheNames.DATABASE_ENTITIES, key = "#categoryName")
    public List<Book> findByCategoryName(String categoryName) {
        log.debug("BookService -> findByCategoryName name = {}", categoryName);
        Optional<Category> category = categoryRepository.findByCategoryName(categoryName);
        if (category.isPresent()) {
            return bookRepository.findByCategoryId(category.get().getId());
        } else {
            throw new EntityNotFoundException(MessageFormat.format("Category with name {0} not found", categoryName));
        }
    }
    @Cacheable(cacheNames = BookCacheProperties.CacheNames.DATABASE_ENTITIES, key = "#name")
    public Book findByBookName(String name) {
        log.debug("BookService -> findByBookName name = {}", name);
        return bookRepository.findByName(name).orElseThrow(() ->
                new EntityNotFoundException(MessageFormat.format("Book with name {0} not found", name)));
    }

    @Cacheable(cacheNames = BookCacheProperties.CacheNames.DATABASE_ENTITIES, key = "#author")
    public List<Book> findByBookAuthor(String author) {
        log.debug("BookService -> findByBookAuthor author = {}", author);
        List<Book> books = bookRepository.findByAuthor(author);
        if (books.isEmpty()) {
            throw new EntityNotFoundException(MessageFormat.format("Book with author {0} not found", author));
        } else {
            return books;
        }
    }

    @Caching(
            evict = {
                    @CacheEvict(cacheNames = BookCacheProperties.CacheNames.DATABASE_ENTITIES, beforeInvocation = true, key = "#book.name + #book.author"),
                    @CacheEvict(cacheNames = BookCacheProperties.CacheNames.DATABASE_ENTITIES, beforeInvocation = true, key = "#book.category.categoryName")
            }
    )
    public Book save(Book book) {
        log.debug("BookService -> save");
        return bookRepository.save(book);
    }

    @Caching(
            evict = {
                    @CacheEvict(cacheNames = BookCacheProperties.CacheNames.DATABASE_ENTITIES, beforeInvocation = true, key = "#book.name + #book.author"),
                    @CacheEvict(cacheNames = BookCacheProperties.CacheNames.DATABASE_ENTITIES, beforeInvocation = true, key = "#book.category.categoryName")
            }
    )
    public Book update(Book book) {
        log.debug("BookService -> update");
        Book existedBook = findByBookId(book.getId());
        Category category = findByCategoryId(book.getId());
        BeanUtils.copyNonNullProperties(book, existedBook);
        existedBook.setCategory(category);
        return bookRepository.save(existedBook);
    }

    public void deleteByBookId(Long id) {
        log.debug("BookService -> deleteById");
        bookRepository.deleteById(id);
    }
}