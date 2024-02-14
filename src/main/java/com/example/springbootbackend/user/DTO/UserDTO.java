package com.example.springbootbackend.user.DTO;

import com.example.springbootbackend.user.enums.Role;
import lombok.*;

import java.util.Collection;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    //attributo disponibile solo nel DTO per avere a disposizione le authorities lato FE
    private Collection<String> authorities;
}
