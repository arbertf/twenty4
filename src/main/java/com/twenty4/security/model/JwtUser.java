package com.twenty4.security.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtUser {

    private Long id;

    private String email;

    private String role;
}