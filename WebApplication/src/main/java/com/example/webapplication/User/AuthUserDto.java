package com.example.webapplication.User;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class AuthUserDto {
    private String username;

    public AuthUserDto(String username) {
        this.username = username;
    }
}
