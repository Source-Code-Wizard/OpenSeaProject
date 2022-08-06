package com.example.webapplication.Auction;

import com.example.webapplication.Bid.Bid;
import com.example.webapplication.Category.Category;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Auction")
public class Auction {
    @Id
    @SequenceGenerator(
            name= "user_sequence", sequenceName = "user_sequence",allocationSize = 1
    )

    @GeneratedValue( strategy = GenerationType.SEQUENCE,generator = "user_sequence")
    private Long itemId;

    @Column(columnDefinition="LONGTEXT",length = 65555)
    private String name;

    @Column(name="Currently")
    private double Currently;

    private double buyPrice;

    private double firstBid;

    private int numOfBids;

    @Column(columnDefinition="LONGTEXT",length = 65555)
    private String location;

    @Column(name="Started")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime auctionStartedTime;

    @Column(name="Ends")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime auctionEndTime;

    @Column(name="description",columnDefinition="LONGTEXT",length = 65555)
    private String description;

    // one to many unidirectional mapping
    // default fetch type for OneToMany: LAZY
    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    // we create to Bid-table a new column named:Auction_itemID which refers to Auction.itemId column
    @JoinColumn(name = "Auction_itemID", referencedColumnName = "itemId")
    private List<Bid> bidList = new ArrayList<>();


    @ManyToMany(fetch = FetchType.EAGER,targetEntity = Category.class)
    private Set<Category> categories = new HashSet<>();


    public Auction(){}

    public Auction(String name, double currently, double buyPrice, double firstBid, int numOfBids,
                   String location, LocalDateTime auctionStartedTime, LocalDateTime auctionEndTime,
                   String description) {
        this.name = name;
        Currently = currently;
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

    public double getCurrently() {
        return Currently;
    }

    public void setCurrently(double currently) {
        Currently = currently;
    }

    public List<Bid> getBidList() {
        return bidList;
    }

    public void setBidList(List<Bid> bidList) {
        this.bidList = bidList;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
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
