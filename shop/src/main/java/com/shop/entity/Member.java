package com.shop.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Member {
    @Id
    private String id;
    private String pwd;
    private String name;
    private String address;
}
