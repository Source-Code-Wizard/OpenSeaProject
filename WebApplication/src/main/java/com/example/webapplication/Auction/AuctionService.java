package com.example.webapplication.Auction;

import com.example.webapplication.Bid.Bid;
import com.example.webapplication.Bid.BidSortByMoney;
import com.example.webapplication.Bid.bidDTO;
import com.example.webapplication.Bid.BidRepository;
import com.example.webapplication.Bidder.Bidder;
import com.example.webapplication.Bidder.BidderRepository;
import com.example.webapplication.Category.Category;
import com.example.webapplication.Category.CategoryRepository;
import com.example.webapplication.Seller.Seller;
import com.example.webapplication.Seller.SellerRepository;
import com.example.webapplication.User.User;
import com.example.webapplication.User.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.*;
import java.lang.*;
import java.time.LocalDateTime;


@Service
public class AuctionService {
    private final AuctionRepository auctionRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;

    private final BidderRepository bidderRepository;

    private final BidRepository bidRepository;

    @Autowired
    public AuctionService(AuctionRepository auctionRepository, CategoryRepository categoryRepository,
                          UserRepository userRepository, SellerRepository sellerRepository,
                          BidderRepository bidderRepository, BidRepository bidRepository) {
        this.auctionRepository = auctionRepository;
        this.categoryRepository=categoryRepository;
        this.userRepository=userRepository;
        this.sellerRepository=sellerRepository;
        this.bidRepository=bidRepository;
        this.bidderRepository = bidderRepository;
    }

    public  ResponseEntity<?> getSpecificAuction(Long auctionId){
       Optional<Auction> optionalAuction= auctionRepository.findById(auctionId);
       if (optionalAuction.isPresent()){
           return new ResponseEntity<>(optionalAuction.get(), HttpStatus.OK);
       }
        return new ResponseEntity<>("No auction available", HttpStatus.BAD_REQUEST);
    }

    public  ResponseEntity<?> placeBid(bidDTO bidDTO){

        System.out.println(bidDTO.toString());
        String userName = bidDTO.getUsername();
        Optional<User> potenialUser = userRepository.findByUsername(userName);
        User officialUser;
        if (potenialUser.isPresent()){
            System.out.println("63");
            officialUser=potenialUser.get();

            Optional<Bidder> optionalBidder = bidderRepository.findById(officialUser.getUserId());
            Bidder newBidder;
            if (optionalBidder.isPresent()){
                newBidder = optionalBidder.get();
            }else{
                newBidder = new Bidder(0,officialUser.getUserId(),officialUser.getAddress(),officialUser.getCountry());
                //officialUser.setBidder(newBidder);
                bidderRepository.save(newBidder);
            }

            Optional<Auction> optionalAuction = auctionRepository.findById(bidDTO.getAuctionId());
            System.out.println(optionalAuction.get().getItemId());

            Optional<Bid> optionalBid = bidRepository.myFind(optionalAuction.get().getItemId(), newBidder.getId());
            Bid newBid;
            if (optionalBid.isPresent()){
                newBid=optionalBid.get();
                newBid.setLocalBidDateTime(bidDTO.getBidSubmittedTime());
                newBid.setMoneyAmount(bidDTO.getMoneyOffered());
                newBid.setBidderUsername(officialUser.getUsername());
            }else newBid = new Bid(bidDTO.getBidSubmittedTime(),bidDTO.getMoneyOffered(),officialUser.getUsername());

            System.out.println("85");
            newBidder.getBidsList().add(newBid);
            //newBidder.setUser(officialUser);
            newBid.setBidder(newBidder);

            //officialUser.setBidder(newBidder);
            userRepository.save(officialUser);


            if (optionalAuction.isPresent()){
                Auction auctionPure= optionalAuction.get();
                System.out.println(auctionPure.toString());
                //bidRepository.save(newBid);

                /* we update the bids of the specific auction*/
                List<Bid> updatedAuctionBidList=new ArrayList<>();
                if (auctionPure.getBidList().isEmpty())
                {
                    updatedAuctionBidList.add(newBid);
                    auctionPure.setFirstBid(newBid.getMoneyAmount());
                    System.out.println("the list was emplty");
                }
                else {
                    /* check if this specific biddder has already a bid and if so, update it*/
                    for (int bid = 0; bid <auctionPure.getBidList().size() ; bid++) {
                        Bid oldBid = auctionPure.getBidList().get(bid);
                        if (oldBid.getBidder().getId()==newBid.getBidder().getId()){
                            System.out.println("old bid is removed");
                            continue;
                        }else{
                            updatedAuctionBidList.add(oldBid);
                        }
                    }
                    updatedAuctionBidList.add(newBid);
                }


                auctionPure.setBidList(updatedAuctionBidList);

                Collections.sort(updatedAuctionBidList);
                for (int i = 0; i <updatedAuctionBidList.size() ; i++) {
                    System.out.println(updatedAuctionBidList.get(i).getMoneyAmount());
                }
                auctionPure.setCurrently(updatedAuctionBidList.get(0).getMoneyAmount());
                auctionPure.setNumOfBids(updatedAuctionBidList.size());

                //auctionPure.getBidList().add(newBid);
                //auctionPure.setBidList(auctionPure.getBidList());

                newBid.setAuction(auctionPure);
               // bidRepository.save(newBid);

                auctionRepository.save(auctionPure);
                return new ResponseEntity<>("Pid was placed succesfully", HttpStatus.CREATED);
            }
        }

        return new ResponseEntity<>("Pid cant be placed", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> registerAuctionToBase(AuctionDTO auctionDTO) {

        System.out.println(auctionDTO.getSeller_id());

        //Seller seller = new Seller(0,auctionDTO.getSeller_id());

        Optional<Seller> optionalSeller = sellerRepository.findById(auctionDTO.getSeller_id());
        Seller seller;
        List<Auction> sellersAuctionList;
        if (optionalSeller.isPresent()){
            seller=optionalSeller.get();
        }else{
            seller = new Seller(0,auctionDTO.getSeller_id());
        }
        sellersAuctionList=seller.getSellersAuctions();

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

        List<Auction> updatedSellerAuctionList = new ArrayList<>();
        for (int i = 0; i <sellersAuctionList.size() ; i++) {
            updatedSellerAuctionList.add(sellersAuctionList.get(i));
        }

        /* we update seller's list and we save seller to the database*/
        updatedSellerAuctionList.add(auctionForRegistration);
        seller.setSellersAuctions(sellersAuctionList);


        /* we update the one to one (User-Seller) relationship with the latest updates*/
        Optional<User> userWhoIsAlsoSeller = userRepository.findById(auctionDTO.getSeller_id());
        if (userWhoIsAlsoSeller.isPresent()){
            User userWhoIsAlsoSellerPure = userWhoIsAlsoSeller.get();
            seller.setUser(userWhoIsAlsoSellerPure);
            seller.setUsername(userWhoIsAlsoSellerPure.getUsername());
            userWhoIsAlsoSellerPure.setSeller(seller);
            userRepository.save(userWhoIsAlsoSellerPure);
        }
        /* save seller to database! */
        //sellerRepository.save(seller);

        /* we set the seller of this auction! */
        auctionForRegistration.setSeller(seller);

        /* save the complete auction to the dataBase! */

        return new ResponseEntity<>(auctionRepository.save(auctionForRegistration), HttpStatus.CREATED);
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

    public ResponseEntity<?> deleteAll(){
        auctionRepository.deleteAll();
        return new ResponseEntity<>("ALL AUCTIONS ARE DELETED", HttpStatus.OK);
    }
    public  ResponseEntity<?> findSeller(Long auctionId){
        Optional<Auction> optionalAuction = auctionRepository.findById(auctionId);
        if (optionalAuction.isPresent()){
            Auction originalAuction = optionalAuction.get();
            System.out.println(originalAuction.getSeller().toString());
            Seller auctionSeller = originalAuction.getSeller();
            System.out.println(auctionSeller.getUser().toString());
            return new ResponseEntity<>("Seller was found", HttpStatus.OK);
        }
        return new ResponseEntity<>("Seller was not found", HttpStatus.OK);
    }
}