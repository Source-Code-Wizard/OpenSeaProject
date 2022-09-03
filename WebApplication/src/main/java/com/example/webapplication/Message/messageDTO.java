package com.example.webapplication.Message;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class messageDTO {
    private String senderUsername;
    private String receiverUsername;
    private String message;

    public messageDTO(String senderUsername, String receiverUsername, String message) {
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
        this.message = message;
    }
}
