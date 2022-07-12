package com.example.webapplication.Auction;

import com.example.webapplication.Bid.Bid;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Auction")
public class Auction {
    @Id
    @SequenceGenerator(
            name= "user_sequence", sequenceName = "user_sequence",allocationSize = 1
    )

    @GeneratedValue( strategy = GenerationType.SEQUENCE,generator = "user_sequence")
    private Long itemId;

    private String name;
    private String category;

    @Column(name="Currently")
    private double currentOffer;

    private double buyPrice;

    private double firstBid;

    private int numOfBids;

    private String location;

    @Column(name="Started")
    private LocalDateTime auctionStartedTime;

    @Column(name="Ends")
    private LocalDateTime auctionEndTime;

    @Column(name="Description")
    private String description;

    // one to many unidirectional mapping
    // default fetch type for OneToMany: LAZY
    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    // we create to Bid-table a new column named:Auction_itemID which refers to Auction.itemId column
    @JoinColumn(name = "Auction_itemID", referencedColumnName = "itemId")
    private List<Bid> bidList = new ArrayList<>();

    public Auction(){}

    public Auction(Long itemId, String name, String category, double currentOffer, double buyPrice,
                   double firstBid, int numOfBids, String location, LocalDateTime auctionStartedTime,
                   LocalDateTime auctionEndTime, String description) {
        this.itemId = itemId;
        this.name = name;
        this.category = category;
        this.currentOffer = currentOffer;
        this.buyPrice = buyPrice;
        this.firstBid = firstBid;
        this.numOfBids = numOfBids;
        this.location = location;
        this.auctionStartedTime = auctionStartedTime;
        this.auctionEndTime = auctionEndTime;
        this.description = description;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getCurrentOffer() {
        return currentOffer;
    }

    public void setCurrentOffer(double currentOffer) {
        this.currentOffer = currentOffer;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public double getFirstBid() {
        return firstBid;
    }

    public void setFirstBid(double firstBid) {
        this.firstBid = firstBid;
    }

    public int getNumOfBids() {
        return numOfBids;
    }

    public void setNumOfBids(int numOfBids) {
        this.numOfBids = numOfBids;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getAuctionStartedTime() {
        return auctionStartedTime;
    }

    public void setAuctionStartedTime(LocalDateTime auctionStartedTime) {
        this.auctionStartedTime = auctionStartedTime;
    }

    public LocalDateTime getAuctionEndTime() {
        return auctionEndTime;
    }

    public void setAuctionEndTime(LocalDateTime auctionEndTime) {
        this.auctionEndTime = auctionEndTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
