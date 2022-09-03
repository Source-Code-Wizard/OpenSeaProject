package com.example.webapplication.Bid;


import com.example.webapplication.Auction.AuctionDTO;
import com.example.webapplication.Auction.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bid")
@CrossOrigin(origins = "https://localhost:3000",allowCredentials = "true")
public class bidController {

    private final AuctionService auctionService;

    @Autowired
    public bidController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @PostMapping
    public ResponseEntity<?> registerAuctionToBase(@RequestBody bidDTO bid) {
        return new ResponseEntity<>(auctionService.placeBid(bid), HttpStatus.CREATED);
    }
}
