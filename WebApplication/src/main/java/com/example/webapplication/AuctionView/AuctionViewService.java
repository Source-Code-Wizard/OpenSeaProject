package com.example.webapplication.AuctionView;

import com.example.webapplication.Auction.Auction;
import com.example.webapplication.Auction.AuctionRepository;
import com.example.webapplication.User.User;
import com.example.webapplication.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuctionViewService {

    private final AuctionViewRepository auctionViewRepository;
    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;

    @Autowired
    public AuctionViewService(AuctionViewRepository auctionViewRepository, AuctionRepository auctionRepository, UserRepository userRepository) {
        this.auctionViewRepository = auctionViewRepository;
        this.auctionRepository = auctionRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> registerAuctionView(AuctionViewDto auctionViewDto){
        Optional<User> optionalUser = userRepository.findByUsername(auctionViewDto.getUsername());
        if (optionalUser.isEmpty())
            return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);

        Optional<Auction> optionalAuction = auctionRepository.findById(auctionViewDto.getAuctionId());
        if (optionalAuction.isEmpty())
            return new ResponseEntity<>("Auction not found", HttpStatus.BAD_REQUEST);

        AuctionView auctionView = new AuctionView(optionalUser.get(),optionalAuction.get());

        auctionViewRepository.save(auctionView);

        return new ResponseEntity<>("View was registered successfully",HttpStatus.OK);
    }
}
