package com.example.bookrest.mapper;

import com.example.bookrest.entity.Book;
import com.example.bookrest.web.model.BookResponse;
import com.example.bookrest.web.model.UpsertBookRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookMapper {

    @Mapping(source = "category.categoryName", target = "categoryName")
    BookResponse bookToResponse(Book book);

    @Mapping(source = "categoryName", target = "category.categoryName")
    Book requestToBook(UpsertBookRequest request);

}