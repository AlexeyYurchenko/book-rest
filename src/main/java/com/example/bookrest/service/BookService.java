package com.example.bookrest.service;

import com.example.bookrest.repository.BookRepository;
import com.example.bookrest.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;



}
