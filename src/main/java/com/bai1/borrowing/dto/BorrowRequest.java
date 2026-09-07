package com.bai1.borrowing.dto;

public record BorrowRequest(
        Integer memberId,
        Integer bookId
) {}
