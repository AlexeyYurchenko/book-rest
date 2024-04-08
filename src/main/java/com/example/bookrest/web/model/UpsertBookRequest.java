package com.example.bookrest.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpsertBookRequest {

    private String name;
    private String author;
    private String categoryName;
}