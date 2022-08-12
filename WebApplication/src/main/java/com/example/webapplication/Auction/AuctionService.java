package com.example.webapplication.Auction;

import com.example.webapplication.Category.Category;
import com.example.webapplication.Category.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Join;
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
        if(auctionToDelete.getAuctionStartedTime().isAfter(LocalDateTime.now()) || (auctionToDelete.getNumOfBids() == 0)){
            auctionRepository.delete(auctionToDelete);
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
                auctionRepository.save(pure);
                return new ResponseEntity<>("Auction has been edited!", HttpStatus.OK);
            }
            return new ResponseEntity<>("This auction can't be edited!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("This auction doesn't exist!", HttpStatus.BAD_REQUEST);
    }

    public Map<String, Object>searchForAuction(String categoryName, Double price, String auctionLocation,String description){

        Pageable pageable = PageRequest.of(0, 5);

        AuctionSpecification auctionSpecification = new AuctionSpecification();
        List<Auction> Auctions = new ArrayList<Auction>();
        System.out.println("CATEGOTY IS "+categoryName);
        /* we search based on the parameteres the user gave as input */
        if (categoryName!=null){
            System.out.println(categoryName);
           auctionSpecification.add(new SearchCriteria("categories",categoryName,SearchOperation.JOIN));
        }
        if (price!=null){
            auctionSpecification.add(new SearchCriteria("currently",price,SearchOperation.LESS_THAN));
        }
        if(auctionLocation!=null){
            auctionSpecification.add(new SearchCriteria("location",auctionLocation,SearchOperation.MATCH));
        }
        if(description!=null){
            auctionSpecification.add(new SearchCriteria("description",description,SearchOperation.MATCH));
        }
        Page<Auction> pageWithResults = auctionRepository.findAll(auctionSpecification,pageable);
        /* getContent() returns ONLY a list with the pages we need */
        Auctions = pageWithResults.getContent();

        /* As a response, the server sends a map that contains the following attributes! */
        Map<String, Object> response = new HashMap<>();
        response.put("tutorials", Auctions);
        response.put("currentPage", pageWithResults.getNumber());
        response.put("totalItems", pageWithResults.getTotalElements());
        response.put("totalPages", pageWithResults.getTotalPages());
        return response;
    }
}