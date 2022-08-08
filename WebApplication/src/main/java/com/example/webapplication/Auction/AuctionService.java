package com.example.webapplication.Auction;

import com.example.webapplication.Category.Category;
import com.example.webapplication.Category.CategoryRepository;
import com.example.webapplication.User.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

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

        /* we retrive the information from the token payload and then register to base! */
        Auction auctionForRegistration = new Auction(
                auctionDTO.getName(),auctionDTO.getCurrently(),auctionDTO.getBuyPrice(),
                auctionDTO.getFirstBid(),auctionDTO.getNumOfBids(),auctionDTO.getLocation(),
                auctionDTO.getAuctionStartedTime(),auctionDTO.getAuctionEndTime(),auctionDTO.getDescription());

        List<String> categories =auctionDTO.getListOfCategories();
        Set<Category> categorySet = new HashSet<>();

        /* we add the categories */
        for (int categoryIndex = 0; categoryIndex < categories.size(); categoryIndex++) {
            String categoryName = categories.get(categoryIndex);
            Category category = categoryRepository.findByName(categoryName);
            categorySet.add(category);
            System.out.println("Print message: "+category.toString());
        }

        auctionForRegistration.setCategories(categorySet);

        /* save the complete auction to the dataBase! */
        return auctionRepository.save(auctionForRegistration);
    }

    public List<Auction> getAllActiveAuctions(){
        return auctionRepository.findAllWithAuctionEndTimeAfter(LocalDateTime.now());

    }

    public ResponseEntity<?> deleteAuction(Auction auctionToDelete){
        if(auctionToDelete.getAuctionStartedTime().isAfter(LocalDateTime.now()) || auctionToDelete.getNumOfBids() == 0){
            auctionRepository.deleteById(auctionToDelete.getItemId());
            return new ResponseEntity<>("Auction has been deleted!", HttpStatus.OK);
        }

        return new ResponseEntity<>("This auction can't be deleted!", HttpStatus.BAD_REQUEST);
    }

    public  ResponseEntity<?> editAuction(Auction auctionToEdit){
        Optional<Auction> existingAuction = auctionRepository.findById(auctionToEdit.getItemId());
        if(existingAuction.isPresent()){
            Auction pure = existingAuction.get();
            if(pure.getAuctionStartedTime().isAfter(LocalDateTime.now()) || pure.getNumOfBids() == 0){
                pure.setName(auctionToEdit.getName());
                pure.setAuctionEndTime(auctionToEdit.getAuctionEndTime());
                pure.setAuctionStartedTime(auctionToEdit.getAuctionStartedTime());
                pure.setCategories(auctionToEdit.getCategories());
                pure.setCurrently(auctionToEdit.getCurrently());
                pure.setBidList(auctionToEdit.getBidList());
                pure.setBuyPrice(auctionToEdit.getBuyPrice());
                pure.setDescription(auctionToEdit.getDescription());
                pure.setFirstBid(auctionToEdit.getFirstBid());
                pure.setLocation(auctionToEdit.getLocation());
                pure.setNumOfBids(auctionToEdit.getNumOfBids());
                return new ResponseEntity<>("Auction has been edited!", HttpStatus.OK);
            }
            return new ResponseEntity<>("This auction can't be edited!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("This auction doesn't exist!", HttpStatus.BAD_REQUEST);
    }

    public List<Auction>searchForAuction(String categoryName, Double price, String auctionLocation){

        AuctionSpecification auctionSpecification = new AuctionSpecification();

        if (!categoryName.isBlank() && categoryName!=null){
            auctionSpecification.add(new SearchCriteria("categories",categoryName,SearchOperation.IN));
        }
        if (price!=null){
            auctionSpecification.add(new SearchCriteria("currently",price,SearchOperation.LESS_THAN));
        }
        if(auctionLocation.isBlank() && auctionLocation!=null){
            auctionSpecification.add(new SearchCriteria("location",auctionLocation,SearchOperation.MATCH));
        }
        return auctionRepository.findAll(auctionSpecification);
    }
}