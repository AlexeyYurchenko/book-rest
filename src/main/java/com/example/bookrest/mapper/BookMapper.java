package com.example.bookrest.mapper;

import com.example.bookrest.entity.Book;
import com.example.bookrest.entity.Category;
import com.example.bookrest.exception.EntityNotFoundException;
import com.example.bookrest.repository.CategoryRepository;
import com.example.bookrest.web.model.BookResponse;
import com.example.bookrest.web.model.UpsertBookRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.Optional;

@Component
public class BookMapper {

    @Autowired
    private CategoryRepository categoryRepository;

    public Book requestToBook(UpsertBookRequest request) throws EntityNotFoundException {
        Book book = new Book();
        book.setName(request.getName());
        book.setAuthor(request.getAuthor());
        Optional<Category> categoryOptional = categoryRepository.findByCategoryName(request.getCategoryName());
        if (categoryOptional.isPresent()) {
            book.setCategory(categoryOptional.get());
            categoryOptional.get().addBook(book);
        } else {
            Category category = new Category();
            category.setCategoryName(request.getCategoryName());
            category.addBook(book);
            categoryRepository.save(category);

        }
        return book;
    }


    public BookResponse bookToResponse(Book book) {
        BookResponse bookResponse = new BookResponse();
        bookResponse.setId(book.getId());
        bookResponse.setName(book.getName());
        bookResponse.setAuthor(book.getAuthor());
        bookResponse.setCategoryName(book.getCategory().getCategoryName());
        return bookResponse;
    }


}
