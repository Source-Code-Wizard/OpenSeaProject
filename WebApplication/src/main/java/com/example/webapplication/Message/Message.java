package com.example.webapplication.Message;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

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

    private String sender;

    private String receiver;

    public Message(){

    }

    public Message(String message, String sender, String receiver){
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                '}';
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

}
