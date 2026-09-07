package com.bai1.borrowing.repository;

import com.bai1.borrowing.entity.Borrowing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowingRepository extends JpaRepository<Borrowing, Integer> {

    @Query("""
        select count(b)
        from Borrowing b
        where b.memberId = :memberId
        and b.returned = false
    """)
    int countBorrowingBooks(Integer memberId);
}
