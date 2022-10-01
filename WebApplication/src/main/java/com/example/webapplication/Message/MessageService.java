package com.example.webapplication.Message;

import com.example.webapplication.Auction.AuctionRepository;
import com.example.webapplication.User.User;
import com.example.webapplication.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public ResponseEntity<?> getMessageId(String senderUsername, String receiverUsername, String message, LocalDateTime dateTime){
        List<Message> messageList = messageRepository.findAll();
        for(int i = 0; i < messageList.size(); i++){
            Message message1 = messageList.get(i);

            System.out.println(message1.getSender().getUsername());
            System.out.println(senderUsername);
            System.out.println(message1.getReceiver().getUsername());
            System.out.println(receiverUsername);
            System.out.println(message1.getMessage());
            System.out.println(message);
            System.out.println(message1.getDateTime());
            System.out.println(dateTime);



            if(message1.getSender().getUsername().equals(senderUsername) && message1.getReceiver().getUsername().equals(receiverUsername) && message1.getMessage().equals(message) &&
                    message1.getDateTime().getYear()==dateTime.getYear() && message1.getDateTime().getMonthValue() == dateTime.getMonthValue() &&
                    message1.getDateTime().getDayOfMonth() == dateTime.getDayOfMonth() && message1.getDateTime().getHour() == dateTime.getHour() &&
                    message1.getDateTime().getMinute() == dateTime.getMinute()){
                return new ResponseEntity<>(message1.getMessageId(), HttpStatus.OK);
            }
        }
        System.out.println("hi gioxan");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<?> deleteMessage(Long id){
        messageRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> deleteInboxMessage(Long userId, Long messageId){
        Set<Message> inbox = userRepository.findById(userId).get().getInbox();
        Optional<Message> toDelete = messageRepository.findById(messageId);
        if(toDelete.isPresent()){
            Message message = toDelete.get();
            inbox.remove(message);
            userRepository.findById(userId).get().setInbox(inbox);
            userRepository.save(userRepository.findById(userId).get());
            return new ResponseEntity<>(userRepository.findById(userId).get().getInbox(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> deleteOutboxMessage(Long userId, Long messageId){
        Set<Message> outbox = userRepository.findById(userId).get().getOutbox();
        Optional<Message> toDelete = messageRepository.findById(messageId);
        if(toDelete.isPresent()){
            Message message = toDelete.get();
            System.out.println(outbox.size());
            outbox.remove(message);
            userRepository.findById(userId).get().setOutbox(outbox);
            System.out.println(userRepository.findById(userId).get().getOutbox().size());
            userRepository.save(userRepository.findById(userId).get());
            return new ResponseEntity<>(userRepository.findById(userId).get().getOutbox(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
