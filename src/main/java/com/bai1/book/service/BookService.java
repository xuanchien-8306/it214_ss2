package com.bai1.book.service;

import com.bai1.book.entity.Book;
import com.bai1.book.repository.BookRepository;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public Book findById(Integer id) {
        return repository.findById(id).orElseThrow();
    }
}
