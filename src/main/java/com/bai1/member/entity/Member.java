package com.bai1.member.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Member {

    @Id
    @GeneratedValue
    private Integer id;

    private String name;
}
