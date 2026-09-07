package com.bai1.borrowing.service;

import com.bai1.borrowing.entity.Borrowing;
import com.bai1.borrowing.repository.BorrowingRepository;
import org.springframework.stereotype.Service;

@Service
public class BorrowingService {

    private final BorrowingRepository borrowingRepository;

    public BorrowingService(BorrowingRepository borrowingRepository) {
        this.borrowingRepository = borrowingRepository;
    }

    public void borrowBook(Integer memberId, Integer bookId) {

        int borrowed = borrowingRepository.countBorrowingBooks(memberId);

        if (borrowed >= 5) {
            throw new RuntimeException("Member reached maximum 5 books");
        }

        Borrowing borrowing = new Borrowing(memberId, bookId);
        borrowingRepository.save(borrowing);
    }
}
