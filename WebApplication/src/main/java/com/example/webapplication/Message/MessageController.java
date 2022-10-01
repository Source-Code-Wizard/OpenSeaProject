package com.example.webapplication.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


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

    @GetMapping("/getMessageId")
    public ResponseEntity<?> getMessageId(@RequestParam(required = false)String senderUsername,
                                          @RequestParam(required = false)String receiverUsername,
                                          @RequestParam(required = false)String message,
                                          @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime){
        return messageService.getMessageId(senderUsername, receiverUsername, message, dateTime);
    }

    @DeleteMapping("/deleteMessage/{message_id}")
    public ResponseEntity<?> deleteMessage(@PathVariable("message_id") Long id){
        return new ResponseEntity<>(messageService.deleteMessage(id), HttpStatus.OK);
    }

    @PostMapping("/deleteInboxMessage/{user_id}/{message_id}")
    public ResponseEntity<?> deleteInboxMessage(@PathVariable("user_id") String userId, @PathVariable("message_id") String messageId){
        return messageService.deleteInboxMessage(Long.parseLong(userId), Long.parseLong(messageId));
    }

    @PostMapping("/deleteOutboxMessage/{user_id}/{message_id}")
    public ResponseEntity<?> deleteOutboxMessage(@PathVariable("user_id") String userId, @PathVariable("message_id") String messageId){
        return messageService.deleteOutboxMessage(Long.parseLong(userId), Long.parseLong(messageId));
    }
}
