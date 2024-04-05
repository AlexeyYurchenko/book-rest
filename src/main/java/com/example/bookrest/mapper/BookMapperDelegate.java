package com.example.bookrest.mapper;

import com.example.bookrest.entity.Book;
import com.example.bookrest.entity.Category;
import com.example.bookrest.repository.CategoryRepository;
import com.example.bookrest.web.model.BookResponse;
import com.example.bookrest.web.model.UpsertBookRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public abstract class BookMapperDelegate implements BookMapper {

    @Autowired
    private BookMapper bookMapper;
//    @Autowired
//    private BookService bookService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Book requestToBook(UpsertBookRequest request) {
        Book book = new Book();
        book.setName(request.getName());
        book.setAuthor(request.getAuthor());
        Optional<Category> category = categoryRepository.findByName(request.getCategoryName());
        if (category.isPresent()) {
            book.setCategory(category.get());
        } else {
            Category newCategory = new Category();
            newCategory.setName(request.getName());
            categoryRepository.save(newCategory);
        }
        return book;
    }

    @Override
    public Book requestToBook(Long bookId, UpsertBookRequest request) {
        Book book = requestToBook(request);
        book.setId(bookId);
        return book;
    }

    @Override
    public BookResponse bookToResponse(Book book) {
        BookResponse response = bookMapper.bookToResponse(book);
        response.setName(book.getName());
        response.setAuthor(book.getAuthor());
        response.setCategoryName(book.getCategory().getName());
        return response;
    }

//    @Override
//    public BookListResponse bookListToResponseList(Book book) {
//        return null;
//    }
}
