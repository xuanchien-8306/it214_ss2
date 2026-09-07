package com.bai1.book.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Book {

    @Id
    @GeneratedValue
    private Integer id;

    private String title;
}
