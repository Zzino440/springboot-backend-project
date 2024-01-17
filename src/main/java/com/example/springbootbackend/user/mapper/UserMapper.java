package com.example.springbootbackend.user.mapper;

import com.example.springbootbackend.user.DTO.UserDTO;
import com.example.springbootbackend.user.model.User;

public class UserMapper {

    public static UserDTO toUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }

    // Se hai bisogno di convertire anche da UserDTO a User, puoi aggiungere un metodo qui
}
