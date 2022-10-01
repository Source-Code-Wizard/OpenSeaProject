package com.example.webapplication.Message;

import com.example.webapplication.User.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Messages")
public class Message {
    @Id
    @SequenceGenerator(
            name= "user_sequence", sequenceName = "user_sequence",allocationSize = 1
    )

    @GeneratedValue( strategy = GenerationType.SEQUENCE,generator = "user_sequence")
    private Long messageId;
    private String message;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime dateTime;

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    @ManyToOne
    //@JsonIgnore
    @JoinColumn(name = "sender_id", referencedColumnName = "user_id"/*nullable = false*/)
    @JsonBackReference(value = "userSender")
    private User sender;

    @ManyToOne
    //@JsonIgnore
    @JoinColumn(name = "receiver_id",referencedColumnName = "user_id"/*nullable = false*/)
    @JsonBackReference(value = "userReceiver")
    private User receiver;

    public Message(){

    }

    public Message(String message, User sender, User receiver){
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.dateTime = LocalDateTime.now();
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                '}';
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
