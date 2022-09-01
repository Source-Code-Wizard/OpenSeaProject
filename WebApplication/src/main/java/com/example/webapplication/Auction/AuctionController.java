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
@CrossOrigin(origins = "https://localhost:3000/OpensSea/Auctions",allowCredentials = "true")
public class AuctionController {
    private final AuctionService auctionService;

    @Autowired
    public AuctionController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }


    @PreAuthorize("hasAnyAuthority('ADMIN', 'SELLER')")
    @PostMapping()
    public ResponseEntity<?> registerAuctionToBase(@RequestBody AuctionDTO auctionForRegistration) {
        return new ResponseEntity<>(auctionService.registerAuctionToBase(auctionForRegistration), HttpStatus.CREATED);
    }

    @GetMapping("/getAllActiveAuctions")
    public List<Auction> getAllActiveAuctions(){
        return auctionService.getAllActiveAuctions();
    }

    /* FUNCTION FOR TESTING */
    @GetMapping("/findSeller/{auctionId}")
    public ResponseEntity<?> findSeller(@PathVariable("auctionId") String auctionId){
        return auctionService.findSeller(Long.parseLong(auctionId));
    }

    @GetMapping("/getAuction/{auctionId}")
    public ResponseEntity<?> getSpecificAuction(@PathVariable("auctionId") String auctionId){
        return auctionService.getSpecificAuction(Long.parseLong(auctionId));
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<?> deleteAuctions(){
        return new ResponseEntity<>(auctionService.deleteAll(), HttpStatus.OK);
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
}