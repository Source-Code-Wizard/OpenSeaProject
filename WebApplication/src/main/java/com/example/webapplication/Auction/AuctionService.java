package com.example.webapplication.Auction;

import com.example.webapplication.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuctionService {
    private final AuctionRepository auctionRepository;

    @Autowired
    public AuctionService(AuctionRepository auctionRepository) {
        this.auctionRepository = auctionRepository;
    }

    public Auction registerAuctionToBase(Auction auctionForRegistration) {
        return auctionRepository.save(auctionForRegistration);
    }

    public List<Auction> getAllActiveAuctions(){
        return auctionRepository.findAllWithAuctionEndTimeAfter(LocalDateTime.now());

    }

    public ResponseEntity<?> deleteAuction(Auction auctionToDelete){
        auctionRepository.deleteById(auctionToDelete.getItemId());
        return new ResponseEntity<>("Auction has been deleted!", HttpStatus.OK);
    }
}