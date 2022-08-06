package com.example.webapplication.Auction;

import com.example.webapplication.Category.Category;
import com.example.webapplication.Category.CategoryRepository;
import com.example.webapplication.User.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AuctionService {
    private final AuctionRepository auctionRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public AuctionService(AuctionRepository auctionRepository,CategoryRepository categoryRepository) {
        this.auctionRepository = auctionRepository;
        this.categoryRepository=categoryRepository;
    }

    public Auction registerAuctionToBase(AuctionDTO auctionDTO) {

        Auction auctionForRegistration = new Auction(
                auctionDTO.getName(),auctionDTO.getCurrently(),auctionDTO.getBuyPrice(),
                auctionDTO.getFirstBid(),auctionDTO.getNumOfBids(),auctionDTO.getLocation(),
                auctionDTO.getAuctionStartedTime(),auctionDTO.getAuctionEndTime(),auctionDTO.getDescription());

        List<String> categories =auctionDTO.getListOfCategories();
        Set<Category> categorySet = new HashSet<>();

        for (int categoryIndex = 0; categoryIndex < categories.size(); categoryIndex++) {
            String categoryName = categories.get(categoryIndex);
            Category category = categoryRepository.findByName(categoryName);
            categorySet.add(category);
        }

        auctionForRegistration.setCategories(categorySet);

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