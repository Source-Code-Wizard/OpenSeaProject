package com.example.webapplication.WebConfiguration.RefreshToken;

import lombok.*;

import javax.validation.constraints.NotBlank;


@Getter
@EqualsAndHashCode
@Setter
@ToString
@NoArgsConstructor
public class TokenRefreshRequest {
    @NotBlank
    private String refreshToken;
    public String getRefreshToken() {
        return refreshToken;
    }
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public TokenRefreshRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
