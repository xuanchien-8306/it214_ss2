package com.bai1.member.service;

import com.bai1.member.entity.Member;
import com.bai1.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository repository;

    public MemberService(MemberRepository repository) {
        this.repository = repository;
    }

    public Member findById(Integer id) {
        return repository.findById(id).orElseThrow();
    }
}
