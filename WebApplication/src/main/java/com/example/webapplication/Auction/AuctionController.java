package com.example.webapplication.Auction;

import com.example.webapplication.User.User;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @CrossOrigin(origins = "*")
    @PostMapping()
    public ResponseEntity<Auction> registerAuctionToBase(@RequestBody AuctionDTO auctionForRegistration) {
        return new ResponseEntity<>(auctionService.registerAuctionToBase(auctionForRegistration), HttpStatus.CREATED);
    }

    @GetMapping("/getAllActiveAuctions")
    public List<Auction> getAllActiveAuctions(){
        return auctionService.getAllActiveAuctions();
    }

    @GetMapping("/search")
    public Map<String, Object> searchForAuctions(@RequestParam(required = false)String category,
                                                 @RequestParam(required = false)Double price,
                                                 @RequestParam(required = false)String location,
                                                 @RequestParam(required = false)String description)
    {
        return auctionService.searchForAuction(category,price,location,description);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/deleteAuction")
    public ResponseEntity<?> deleteAuction(@RequestBody Auction auctionForDelete){
        return new ResponseEntity<>(auctionService.deleteAuction(auctionForDelete),HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/editAuction")
    public ResponseEntity<?> editAuction(@RequestBody Auction auctionToEdit){
        return new ResponseEntity<>(auctionService.editAuction(auctionToEdit),HttpStatus.OK);
    }
}