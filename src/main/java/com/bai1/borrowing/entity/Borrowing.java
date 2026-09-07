package com.bai1.borrowing.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Borrowing {

    @Id
    @GeneratedValue
    private Integer id;

    private Integer memberId;
    private Integer bookId;
    private boolean returned = false;

    public Borrowing() {}

    public Borrowing(Integer memberId, Integer bookId) {
        this.memberId = memberId;
        this.bookId = bookId;
    }
}
