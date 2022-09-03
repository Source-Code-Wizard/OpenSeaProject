package com.example.webapplication.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/message")
    public ResponseEntity<?> sendMessage(@RequestBody messageDTO message){
        return new ResponseEntity<>(messageService.sendMessage(message.getMessage(), message.getSenderUsername(), message.getReceiverUsername()), HttpStatus.OK);
    }

    @GetMapping("/getMessage/{message_id}")
    public ResponseEntity<?> getMessage(@PathVariable("message_id") Long id){
        System.out.println(id);
        return new ResponseEntity<>(messageService.getMessage((id)), HttpStatus.OK);
    }
}
