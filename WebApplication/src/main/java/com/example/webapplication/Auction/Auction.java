package com.example.webapplication.Auction;

import com.example.webapplication.AuctionView.AuctionView;
import com.example.webapplication.Bid.Bid;
import com.example.webapplication.Category.Category;
import com.example.webapplication.Seller.Seller;
import com.example.webapplication.User.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@EqualsAndHashCode
@Getter
@Setter
@ToString
@Table(name = "Auction")
@XmlRootElement(name = "Auction")
@XmlAccessorType(XmlAccessType.FIELD)
public class Auction {
    @Id
    @SequenceGenerator(
            name= "user_sequence", sequenceName = "user_sequence",allocationSize = 1
    )

    @GeneratedValue( strategy = GenerationType.SEQUENCE,generator = "user_sequence")
    @Column(name = "item_id")
    private Long itemId;

    @Column(columnDefinition="LONGTEXT",length = 65555)
    private String name;

    @Column(name="Currently")
    private double Currently;

    @Column(name="Latitude")
    private Double Latitude;

    @Column(name="Longtitude")
    private Double Longtitude;
    private double buyPrice;

    private double firstBid;

    private int numOfBids;

    @Column(columnDefinition="LONGTEXT",length = 65555)
    private String location;

    @Column(name="Started")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime auctionStartedTime;

    @Column(name="Ends")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime auctionEndTime;

    @ManyToOne
    @JoinColumn(name = "user_id" /*nullable = false*/)
    private Seller seller;

    @Column(name="description",columnDefinition="LONGTEXT",length = 65555)
    private String description;

    // one to many unidirectional mapping
    // default fetch type for OneToMany: LAZY
    @ToString.Exclude
    @XmlElementWrapper(name="Bids")
    @XmlElement(name="Bid")
    @OneToMany( mappedBy = "auction" ,cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    // we create to Bid-table a new column named:Auction_itemID which refers to Auction.itemId column
   // @JoinColumn(name = "Auction_itemID", referencedColumnName = "itemId")
    private List<Bid> bidList = new ArrayList<>();


    @XmlElementWrapper(name="Categories")
    @XmlElement(name="Category")
    @ManyToMany(fetch = FetchType.EAGER,targetEntity = Category.class)
    private Set<Category> categories = new HashSet<>();



   /* @OneToMany(mappedBy = "auction" ,cascade = CascadeType.ALL, fetch = FetchType.LAZY )
    @JsonIgnore
    private List<AuctionView> AuctionViews = new ArrayList<>();*/

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public Auction(){}

    public Auction(String name, double currently, double buyPrice, double firstBid, int numOfBids,
                   String location, LocalDateTime auctionStartedTime, LocalDateTime auctionEndTime,
                   String description,Double Longtitude,Double Latitude) {
        this.name = name;
        Currently = currently;
        this.buyPrice = buyPrice;
        this.firstBid = firstBid;
        this.numOfBids = numOfBids;
        this.location = location;
        this.auctionStartedTime = auctionStartedTime;
        this.auctionEndTime = auctionEndTime;
        this.description = description;
        this.Latitude=Latitude;
        this.Longtitude=Longtitude;
    }

    public Auction(long id, String name, double currently, double buyPrice, double firstBid, int numOfBids,
                   String location, LocalDateTime auctionStartedTime, LocalDateTime auctionEndTime,
                   String description) {
        this.itemId = id;
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

}
