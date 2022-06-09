package com.twenty4.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    private Long role;
}
