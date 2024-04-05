package com.example.bookrest.mapper;

import com.example.bookrest.entity.Book;
import com.example.bookrest.web.model.BookListResponse;
import com.example.bookrest.web.model.BookResponse;
import com.example.bookrest.web.model.UpsertBookRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookMapper {

    Book requestToBook(UpsertBookRequest request);
    @Mapping(source = "bookId", target = "id")
    Book requestToBook(Long bookId,UpsertBookRequest request);
    @Mapping(source = "category.name",target = "name")
    BookResponse bookToResponse(Book book);
    BookListResponse bookListToResponseList(Book book);








}
