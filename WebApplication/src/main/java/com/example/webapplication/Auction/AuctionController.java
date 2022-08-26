package com.example.webapplication.Auction;

import com.example.webapplication.User.User;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auctions")
public class AuctionController {
    private final AuctionService auctionService;

    @Autowired
    public AuctionController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }


    @PostMapping("/register")
    public ResponseEntity<Auction> registerAuctionToBase(@RequestBody AuctionDTO auctionForRegistration) {
        return new ResponseEntity<>(auctionService.registerAuctionToBase(auctionForRegistration), HttpStatus.CREATED);
    }

    @GetMapping("/getAllActiveAuctions")
    public List<Auction> getAllActiveAuctions(){
        return auctionService.getAllActiveAuctions();
    }

    @CrossOrigin(origins = "https://localhost:3000/OpensSea/Auctions",allowCredentials = "true")
    @GetMapping("/getAuction/{auctionId}")
    public ResponseEntity<?> getSpecificAuction(@PathVariable("auctionId") String auctionId){
        return auctionService.getSpecificAuction(Long.parseLong(auctionId));
    }

    @GetMapping("/search")
    @CrossOrigin(origins = "https://localhost:3000",allowCredentials = "true")
    public Map<String, Object> searchForAuctions(@RequestParam(required = false)String category,
                                                 @RequestParam(required = false)Double price,
                                                 @RequestParam(required = false)String location,
                                                 @RequestParam(required = false)String description)
    {
        return auctionService.searchForAuction(category,price,location,description);
    }


    @DeleteMapping("/deleteAuction")
    public ResponseEntity<?> deleteAuction(@RequestBody Auction auctionForDelete){
        return new ResponseEntity<>(auctionService.deleteAuction(auctionForDelete),HttpStatus.OK);
    }


    @PostMapping("/editAuction")
    public ResponseEntity<?> editAuction(@RequestBody Auction auctionToEdit){
        return new ResponseEntity<>(auctionService.editAuction(auctionToEdit),HttpStatus.OK);
    }

    @GetMapping("/SellerId")
    public ResponseEntity<?> getSellerId(@RequestBody Long auctionId){
        return new ResponseEntity<>(auctionService.getSellerId(auctionId), HttpStatus.OK);
    }

    @MessageMapping("/chatAfterAuction")
//    public ResponseEntity<?> sellerSendsMessageAfterAuction(@RequestBody Auction auction, @RequestBody String message){
//        return new ResponseEntity<>(auctionService.sellerSendsMessageAfterAuction(auction, message), HttpStatus.OK);
//    }

    public ResponseEntity<?> bidderSendsMessageAfterAuction(@RequestBody Auction auction, @RequestBody String message){
        return new ResponseEntity<>(auctionService.bidderSendsMessageAfterAuction(auction, message), HttpStatus.OK);
    }
}