package com.domain;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String name;
    private String email;
    private String password;
    private String zipcode;
    private String address;
    private String telephone;
}
