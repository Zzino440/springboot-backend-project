package com.example.springbootbackend.user.DTO;

import com.example.springbootbackend.user.enums.Role;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserCreateUpdateDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Role role;
}
