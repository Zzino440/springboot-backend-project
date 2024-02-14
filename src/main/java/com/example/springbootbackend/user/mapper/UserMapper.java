package com.example.springbootbackend.user.mapper;

import com.example.springbootbackend.user.DTO.UserDTO;
import com.example.springbootbackend.user.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.stream.Collectors;

public class UserMapper {

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

    // Se hai bisogno di convertire anche da UserDTO a User, puoi aggiungere un metodo qui
}
