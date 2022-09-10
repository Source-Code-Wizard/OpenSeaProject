package com.example.webapplication.Matrix_factorization;

import com.example.webapplication.Auction.Auction;
import com.example.webapplication.Auction.AuctionRepository;
import com.example.webapplication.User.User;
import com.example.webapplication.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recommendation")
@CrossOrigin(origins = "https://localhost:3000",allowCredentials = "true")
public class MatrixFactorizationController {

    private final MatrixFactorization MF;
    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;

    @Autowired
    public MatrixFactorizationController(MatrixFactorization MF,AuctionRepository auctionRepository,UserRepository userRepository) {
        this.MF = MF;
        this.auctionRepository=auctionRepository;
        this.userRepository= userRepository;
    }

    @GetMapping("/auctions")
    List<Auction> getAuctionsOrdered(@RequestParam(required = false)String username){

        //System.out.println("USERNAME IS "+username);

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty())
            return new ArrayList<>();

        if (MF.auction_ids==null) return new ArrayList<>();
        List<Auction> auctions = new ArrayList<>();
        List<Long> auction_ids = MF.auction_recommendations(optionalUser.get().getUserId(), 5); //return top 5 recommendations
        for (Long id:auction_ids){
            auctions.add(auctionRepository.findById(id).get());
        }
        return auctions;
    }

}
