package com.example.bookrest.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@Entity(name = "categories")
public class Category implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NonNull
    private String categoryName;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Book> books = new ArrayList<>();

    public void addBook(Book book) {
        book.setCategory(this);
        books.add(book);
    }

}
//@Entity
//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@Setter
//@ToString
//public class Category {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String name;
//
//    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @ToString.Exclude
//    private List<Book> books = new ArrayList<>();
//
//    public Category(String categoryName) {
//    }
//    public void addBook(Book book) {
//        book.setCategory(this);
//        books.add(book);
//    }
//}
