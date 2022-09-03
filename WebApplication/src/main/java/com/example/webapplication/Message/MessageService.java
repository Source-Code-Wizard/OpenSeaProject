package com.example.webapplication.Message;

import com.example.webapplication.Auction.AuctionRepository;
import com.example.webapplication.User.User;
import com.example.webapplication.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class MessageService {

    private final UserRepository userRepository;
    private final AuctionRepository auctionRepository;

    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(UserRepository userRepository, AuctionRepository auctionRepository, MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.auctionRepository = auctionRepository;
        this.messageRepository = messageRepository;
    }

    public ResponseEntity<?> sendMessage(String message, String sender, String receiver) {
        System.out.println(sender);
        Optional<User> optionalUserSender = userRepository.findByUsername(sender);
        if(optionalUserSender.isPresent()){
            User userSender = optionalUserSender.get();
            Optional<User> optionalUserReceiver = userRepository.findByUsername(receiver);
            if(optionalUserReceiver.isPresent()){
                User userReceiver = optionalUserReceiver.get();
                Message message1 = new Message(message, userSender, userReceiver);
                message1.setSender(userSender);
                message1.setReceiver(userReceiver);
//                simpMessagingTemplate.convertAndSendToUser(userReceiver.getName(),"/private", message);
                Set<Message> senderOutbox = userSender.getOutbox();
                senderOutbox.add(message1);
                userSender.setOutbox(senderOutbox);

                Set<Message> receiverInbox = userReceiver.getInbox();
                receiverInbox.add(message1);
                userReceiver.setInbox(receiverInbox);

                messageRepository.save(message1);

                return new ResponseEntity<>("Message was sent.", HttpStatus.OK);
            }
            return new ResponseEntity<>("Receiver not found.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Sender not found.", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> getMessage(Long id){
        Optional<Message> message = messageRepository.findById(id);
        if(message.isPresent()){
            Message presentMessage = message.get();
            User sender = presentMessage.getSender();
            System.out.println(sender.getUsername());
            return new ResponseEntity<>("getter ok", HttpStatus.OK);
        }
        return new ResponseEntity<>("getter not ok", HttpStatus.BAD_REQUEST);
    }
}
