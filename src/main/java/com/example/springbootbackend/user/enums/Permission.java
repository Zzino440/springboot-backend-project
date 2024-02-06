package com.example.springbootbackend.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permission {

    USER_READ("user:read"),
    USER_UPDATE("user:update"),
    USER_POST("user:post"),
    USER_DELETE("user:delete"),

    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_POST("admin:post"),
    ADMIN_DELETE("admin:delete");


    private final String permission;
}
