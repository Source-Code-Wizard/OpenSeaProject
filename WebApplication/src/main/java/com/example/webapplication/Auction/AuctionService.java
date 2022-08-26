package com.example.webapplication.Auction;

import com.example.webapplication.Bid.Bid;
import com.example.webapplication.Bidder.Bidder;
import com.example.webapplication.Category.Category;
import com.example.webapplication.Category.CategoryRepository;
import com.example.webapplication.Message.Message;
import com.example.webapplication.Message.MessageService;
import com.example.webapplication.Seller.Seller;
import com.example.webapplication.User.User;
import com.example.webapplication.User.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;


import javax.persistence.criteria.Join;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AuctionService {
    private final AuctionRepository auctionRepository;
    private final CategoryRepository categoryRepository;

    private final UserRepository userRepository;

    private final MessageService messageService;

    @Autowired
    public AuctionService(AuctionRepository auctionRepository,CategoryRepository categoryRepository, UserRepository userRepository, MessageService messageService) {
        this.auctionRepository = auctionRepository;
        this.categoryRepository=categoryRepository;
        this.userRepository = userRepository;
        this.messageService = messageService;
    }

    public  ResponseEntity<?> getSpecificAuction(Long auctionId){
       Optional<Auction> optionalAuction= auctionRepository.findById(auctionId);
       if (optionalAuction.isPresent()){
           return new ResponseEntity<>(optionalAuction.get(), HttpStatus.OK);
       }
        return new ResponseEntity<>("No auction available", HttpStatus.BAD_REQUEST);
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
//            System.out.println("Print message: "+category.toString());
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
        System.out.println("CATEGORY IS "+categoryName);
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
        response.put("Auctions", Auctions);
        response.put("currentPage", pageWithResults.getNumber());
        response.put("totalItems", pageWithResults.getTotalElements());
        response.put("totalPages", pageWithResults.getTotalPages());
        return response;
    }

    public ResponseEntity<?> getSellerId(Long auctionId){
        Optional<Auction> auction = auctionRepository.findById(auctionId);
        if(auction.isPresent()){
            Auction auction1 = auction.get();
            System.out.println(auction1.getSeller().getId());
            return new ResponseEntity<>("ok.", HttpStatus.OK);
        }
        return new ResponseEntity<>("Auction not found", HttpStatus.BAD_REQUEST);
    }
//    @Autowired
//    private SimpMessagingTemplate simpMessagingTemplate;

//    public ResponseEntity<?> sellerSendsMessageAfterAuction(Auction auction, String message){
//        Optional<Auction> existingAuction = auctionRepository.findById(auction.getItemId());
//        if(existingAuction.isPresent()){
//            Auction newAuction = existingAuction.get();
//            if(newAuction.getAuctionEndTime().isBefore(LocalDateTime.now())){
//                if(auction.getNumOfBids() > 0){
//                    Long sellerId = auctionRepository.getSellerId(auction.getItemId());
//                    Optional<User> seller = userRepository.findById(sellerId);
//                    if(seller.isPresent()){
//                        User seller1 = seller.get();
//                        String sellerName = seller1.getName();
//
//                        List<Bid> bids = auction.getBidList();
//                        Optional<Bid> maxBid = bids.stream().max(Comparator.comparing(Bid::getMoneyAmount));
//
//                        if(maxBid.isPresent()){
//                            Bid bid = maxBid.get();
//                            String bidderName = bid.getBidder().getUser().getName();
//                            Message message1 = new Message(message, sellerName, bidderName);
//                            simpMessagingTemplate.convertAndSendToUser(message1.getReceiver(),"/chat/message", message);
//                            return new ResponseEntity<>("Message was sent.", HttpStatus.OK);
//
//                        }
//                        return new ResponseEntity<>("Bidder doss mot exist!.", HttpStatus.BAD_REQUEST);
//                    }
//                    return new ResponseEntity<>("Seller does not exist!", HttpStatus.BAD_REQUEST);
//                }
//                return new ResponseEntity<>("This auction has no bids.", HttpStatus.BAD_REQUEST);
//            }
//            return new ResponseEntity<>("This auction has not ended yet.", HttpStatus.BAD_REQUEST);
//        }
//        return new ResponseEntity<>("This auction doesn't exist!", HttpStatus.BAD_REQUEST);
//    }

    public ResponseEntity<?> bidderSendsMessageAfterAuction(Auction auction, String message){
        Optional<Auction> existingAuction = auctionRepository.findById(auction.getItemId());
        if(existingAuction.isPresent()){
            Auction newAuction = existingAuction.get();
            if(newAuction.getAuctionEndTime().isBefore(LocalDateTime.now())){
                if(auction.getNumOfBids() > 0){
//                    Long sellerId = auctionRepository.getSellerId(auction.getItemId());
//                    Optional<User> seller = userRepository.findById(sellerId);
                    Seller seller = auction.getSeller();
//                        return new ResponseEntity<>("Bidder doss mot exist!.", HttpStatus.BAD_REQUEST);

                    Long sellerId = seller.getId();
                    Optional<User> seller1 = userRepository.findById(sellerId);
                    if(seller1.isPresent()){
                        User validSeller = seller1.get();
                        String sellerName = validSeller.getName();
                        List<Bid> bids = auction.getBidList();

                        Optional<Bid> maxBid = bids.stream().max(Comparator.comparing(Bid::getMoneyAmount));
                        if(maxBid.isPresent()){
                            Bid bid = maxBid.get();
                            String bidderName = bid.getBidder().getUser().getName();
                            Message message1 = new Message(message, bidderName, sellerName);
                            messageService.sendMessage(message1);
                            return new ResponseEntity<>("Message was sent.", HttpStatus.OK);
//                            simpMessagingTemplate.convertAndSendToUser(message1.getReceiver(),"/chat/message", message);
                        }
                        return new ResponseEntity<>("Bidder does mot exist!.", HttpStatus.BAD_REQUEST);
                    }
                    return new ResponseEntity<>("Seller does not exist!", HttpStatus.BAD_REQUEST);
                }
                return new ResponseEntity<>("This auction has no bids.", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>("This auction has not ended yet.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("This auction doesn't exist!", HttpStatus.BAD_REQUEST);
    }
}