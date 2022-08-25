package com.example.webapplication.Message;

import com.example.webapplication.Auction.AuctionRepository;
import com.example.webapplication.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private final UserRepository userRepository;
    private final AuctionRepository auctionRepository;

    public MessageService(UserRepository userRepository, AuctionRepository auctionRepository) {
        this.userRepository = userRepository;
        this.auctionRepository = auctionRepository;
    }

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public Message sendMessage(Message message) {
        boolean exists = userRepository.existsByUsername(message.getReceiver());
        if(exists){
            simpMessagingTemplate.convertAndSendToUser(message.getReceiver(),"/chat/message", message);
            return message;
        }
        return null;
    }
}
