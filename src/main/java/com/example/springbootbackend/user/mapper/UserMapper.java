package com.example.springbootbackend.user.mapper;

import com.example.springbootbackend.user.DTO.UserCreateUpdateDTO;
import com.example.springbootbackend.user.DTO.UserDTO;
import com.example.springbootbackend.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public static UserDTO toUserDTO(User user) {
        Collection<String> authorities = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet()); // Utilizzo toSet per rimuovere duplicati

        return UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .authorities(authorities)
                .build();
    }

    public static UserCreateUpdateDTO toUserCreateUpdateDTO(User user) {
        return UserCreateUpdateDTO.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .build();
    }

    public User toUser(UserCreateUpdateDTO userDTO) {
        return User.builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .role(userDTO.getRole())
                .build();
    }

    // Se hai bisogno di convertire anche da UserDTO a User, puoi aggiungere un metodo qui
}
