package com.example.webapplication.Auction;

import com.example.webapplication.Bid.Bid;
import com.example.webapplication.Bid.BidRepository;
import com.example.webapplication.Bid.bidDTO;
import com.example.webapplication.Bidder.Bidder;
import com.example.webapplication.Bidder.BidderRepository;
import com.example.webapplication.Category.Category;
import com.example.webapplication.Category.CategoryRepository;
import com.example.webapplication.Message.MessageService;
import com.example.webapplication.Seller.Seller;
import com.example.webapplication.Seller.SellerRepository;
import com.example.webapplication.User.User;
import com.example.webapplication.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;


@Service
public class AuctionService {
    private final AuctionRepository auctionRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;
    private final MessageService messageService;

    private final BidderRepository bidderRepository;

    private final BidRepository bidRepository;

    @Autowired

    public AuctionService(AuctionRepository auctionRepository, CategoryRepository categoryRepository,
                          UserRepository userRepository, SellerRepository sellerRepository,
                          BidderRepository bidderRepository, BidRepository bidRepository, MessageService messageService) {
        this.auctionRepository = auctionRepository;
        this.categoryRepository=categoryRepository;
        this.userRepository=userRepository;
        this.sellerRepository=sellerRepository;
        this.messageService = messageService;
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
            Optional<Auction> optionalAuction = auctionRepository.findById(bidDTO.getAuctionId());
            if (optionalAuction.get().getCurrently()>=bidDTO.getMoneyOffered()){
                return new ResponseEntity<>("Offer can not be that low!", HttpStatus.BAD_REQUEST);
            }else if(optionalAuction.get().getBuyPrice()>=bidDTO.getMoneyOffered()){
                return new ResponseEntity<>("first bid must be higher than the buy price!", HttpStatus.BAD_REQUEST);
            }
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
            //System.out.println("Print message: "+category.toString());
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

    public List<Auction> getUsersAuctions(Long userId){
        List<Auction> usersAuctions = new ArrayList<>();
//        return auctionRepository.findUsersAuctions(userId);
        List<Auction> allAuctions = auctionRepository.findAll();
        for(int i = 0; i < allAuctions.size(); i++){
            if(allAuctions.get(i).getSeller().getId() == userId){
                usersAuctions.add(allAuctions.get(i));
            }
        }
        return usersAuctions;
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

        Pageable pageable = PageRequest.of(0, 20);

        AuctionSpecification auctionSpecification = new AuctionSpecification();
        List<Auction> Auctions = new ArrayList<Auction>();
        System.out.println("CATEGORY IS "+categoryName);
        /* we search based on the parameteres the user gave as input */
        if (categoryName!=null){
            System.out.println(categoryName);
            auctionSpecification.add(new SearchCriteria("categories",categoryName,SearchOperation.JOIN));
        }
        if (price!=null){
            auctionSpecification.add(new SearchCriteria("Currently",price,SearchOperation.LESS_THAN));
        }
        if(auctionLocation!=null){
            auctionSpecification.add(new SearchCriteria("location",auctionLocation,SearchOperation.MATCH));
        }
        if(description!=null){
            auctionSpecification.add(new SearchCriteria("description",description,SearchOperation.MATCH));
        }

        Page<Auction> pageWithResults = auctionRepository.findAll(auctionSpecification,pageable);

        /* we return the auctions that havent expired yet*/
        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i <pageWithResults.getContent().size(); i++) {
            boolean isActive = now.isBefore(pageWithResults.getContent().get(i).getAuctionEndTime());
            boolean hasStarted = now.isAfter(pageWithResults.getContent().get(i).getAuctionStartedTime());
            if ( hasStarted && isActive )
                Auctions.add(pageWithResults.getContent().get(i));
        }

        /* As a response, the server sends a map that contains the following attributes! */
        Map<String, Object> response = new HashMap<>();
        response.put("Auctions", Auctions);
        response.put("currentPage", pageWithResults.getNumber());
        response.put("totalItems", Auctions.size());
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

    public ResponseEntity<?> sellerSendsMessageAfterAuction(Auction auction, String message) {
        Optional<Auction> existingAuction = auctionRepository.findById(auction.getItemId());
        if (existingAuction.isPresent()) {
            Auction newAuction = existingAuction.get();
            if (newAuction.getAuctionEndTime().isBefore(LocalDateTime.now())) {
                if (newAuction.getNumOfBids() > 0) {
//                    Long sellerId = auctionRepository.getSellerId(auction.getItemId());
//                    Optional<User> seller = userRepository.findById(sellerId);
                    User seller = newAuction.getSeller().getUser();
//                    Seller seller = findSeller(auction.getItemId());
//                        return new ResponseEntity<>("Bidder doss mot exist!.", HttpStatus.BAD_REQUEST);
//                    String sellerName = seller.getUser().getName();
//                    Long sellerId = seller.getId();
//                    Optional<User> seller1 = userRepository.findById(sellerId);
//                    if (seller1.isPresent()) {
//                        User validSeller = seller1.get();
//                        String sellerName = validSeller.getName();

                    List<Bid> bids = newAuction.getBidList();
                    Optional<Bid> maxBid = bids.stream().max(Comparator.comparing(Bid::getMoneyAmount));
                    if (maxBid.isPresent()) {
                        Bid bid = maxBid.get();
                        Long bidderId = bid.getBidder().getId();
                        Optional<User> optionalBidder = userRepository.findById(bidderId);
                        if(optionalBidder.isPresent()){
                            User bidder = optionalBidder.get();
                            messageService.sendMessage(message, seller.getUsername(), bidder.getUsername());

//                            List<Message> bidderInbox = bidder.getInbox();
//                            bidderInbox.add(message1);
//                            bidder.setInbox(bidderInbox);
//
//                            List<Message> sellerOutbox = seller.getUser().getOutbox();
//                            sellerOutbox.add(message1);
//                            seller.getUser().setOutbox(sellerOutbox);
                            return new ResponseEntity<>("Message was sent.", HttpStatus.OK);
                        }
//                  }
                        return new ResponseEntity<>("Bidder does mot exist!.", HttpStatus.BAD_REQUEST);
                    }
                    return new ResponseEntity<>("Bid does not exist!", HttpStatus.BAD_REQUEST);
                }
                return new ResponseEntity<>("This auction has no bids.", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>("This auction has not ended yet.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("This auction doesn't exist!", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> deleteAll(){
        auctionRepository.deleteAll();
        return new ResponseEntity<>("ALL AUCTIONS ARE DELETED", HttpStatus.OK);
    }

}