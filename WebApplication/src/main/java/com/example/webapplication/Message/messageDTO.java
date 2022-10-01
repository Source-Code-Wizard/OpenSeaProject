package com.example.webapplication.Message;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class messageDTO {
    private String senderUsername;
    private String receiverUsername;
    private String message;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime dateTime;

    public messageDTO(String senderUsername, String receiverUsername, String message, LocalDateTime dateTime) {
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
        this.message = message;
        this.dateTime = dateTime;
    }
}
