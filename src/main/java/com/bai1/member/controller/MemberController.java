package com.bai1.member.controller;

import com.bai1.member.entity.Member;
import com.bai1.member.service.MemberService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService service;

    public MemberController(MemberService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public Member getMember(@PathVariable Integer id) {
        return service.findById(id);
    }
}
