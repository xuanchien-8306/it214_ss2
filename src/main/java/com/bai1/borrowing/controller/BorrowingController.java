package com.bai1.borrowing.controller;

import com.bai1.borrowing.dto.BorrowRequest;
import com.bai1.borrowing.service.BorrowingService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/borrowings")
public class BorrowingController {

    private final BorrowingService service;

    public BorrowingController(BorrowingService service) {
        this.service = service;
    }

    @PostMapping
    public String borrow(@RequestBody BorrowRequest request) {
        service.borrowBook(request.memberId(), request.bookId());
        return "Borrow success";
    }
}
