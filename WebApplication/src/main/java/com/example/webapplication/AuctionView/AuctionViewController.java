package com.example.webapplication.AuctionView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auctionViews")
@CrossOrigin(origins = "https://localhost:3000",allowCredentials = "true")
public class AuctionViewController {

    private final AuctionViewService auctionViewService;

    @Autowired
    public AuctionViewController(AuctionViewService auctionViewService) {
        this.auctionViewService = auctionViewService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BIDDER')")
    @PostMapping("/postView")
    public ResponseEntity<?> postAuctionView(@RequestBody AuctionViewDto auctionViewDto){
        System.out.println(auctionViewDto.toString());
        return new ResponseEntity<>(auctionViewService.registerAuctionView(auctionViewDto), HttpStatus.CREATED);
    }

}
