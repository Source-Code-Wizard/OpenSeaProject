package com.example.webapplication.User;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class UserRequestDto {
    private String username;
    private String email;

    public UserRequestDto(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
