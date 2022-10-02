package com.example.webapplication.Auction;

import com.example.webapplication.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auctions")
@CrossOrigin(origins = "https://localhost:3000/OpensSea/Auctions",allowCredentials = "true")
public class AuctionController {
    private final AuctionService auctionService;
    private final UserService userService;

    @Autowired
    public AuctionController(AuctionService auctionService, UserService userService) {
        this.auctionService = auctionService;
        this.userService = userService;
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

    @GetMapping("/getUsersAuctions/{userId}")
    public List<Auction> getUsersAuctions(@PathVariable("userId") String userId){
        return auctionService.getUsersAuctions(Long.parseLong(userId));
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
                                                 @RequestParam(required = false)Double currently,
                                                 @RequestParam(required = false)String location,
                                                 @RequestParam(required = false)String description)
    {
        return auctionService.searchForAuction(category,currently,location,description);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'SELLER')")
    @DeleteMapping("/deleteAuction/{itemId}")
    public ResponseEntity<?> deleteAuction(@PathVariable("itemId") String itemId){
        return auctionService.deleteAuction(Long.parseLong(itemId));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'SELLER')")
    @PostMapping("/editAuction/{itemId}")
    public ResponseEntity<?> editAuction(@PathVariable("itemId") String itemId,
                                         @RequestBody EditAuctionDTO auction){
        System.out.println(auction.getName());
        return auctionService.editAuction(Long.parseLong(itemId), auction.getName(), auction.getAuctionEndTime(), auction.getListOfCategories(), auction.getDescription(), auction.getLocation());
    }

    @GetMapping("/SellerId")
    public ResponseEntity<?> getSellerId(@RequestBody Long auctionId){
        return new ResponseEntity<>(auctionService.getSellerId(auctionId), HttpStatus.OK);
    }

    @GetMapping("/AuctionId")
    public ResponseEntity<?> getAuctionId(String name, String buyPrice, String sellerId){
        return auctionService.getAuctionId(name, Double.parseDouble(buyPrice), Long.parseLong(sellerId));
    }

    @PostMapping("/chatAfterAuction")
////    public ResponseEntity<?> sellerSendsMessageAfterAuction(@RequestBody Auction auction, @RequestBody String message){
////        return new ResponseEntity<>(auctionService.sellerSendsMessageAfterAuction(auction, message), HttpStatus.OK);
////    }
//
    public ResponseEntity<?> sellerSendsMessageAfterAuction(@RequestBody Auction auction, @RequestBody String message){
        return new ResponseEntity<>(auctionService.sellerSendsMessageAfterAuction(auction, message), HttpStatus.OK);
    }

//    @GetMapping("/getUserId/{username}")
////    @PreAuthorize("hasAuthority('ADMIN')")
//    public ResponseEntity<?> getUserId(@PathVariable("username") String username){
//        System.out.println(username);
//        return userService.getUserId(username);
//    }
}