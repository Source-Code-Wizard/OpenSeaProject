package com.example.webapplication.Bidder;

import com.example.webapplication.Auction.Auction;
import com.example.webapplication.Bid.Bid;
import com.example.webapplication.Seller.Seller;
import com.example.webapplication.User.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Table(name = "Bidders")
public class Bidder implements Serializable {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false,name = "bidder_id")
    private Long id;
/*
    @OneToOne
    @MapsId
    @JsonIgnore
    @JoinColumn(name = "user_id")*/
   /* @OneToOne(cascade = CascadeType.ALL ,fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;*/

    @OneToMany(mappedBy = "bidder" ,cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    // we create to auction-table a new column named:Seller_ID which refers to Seller.sellerId column
    //@JoinColumn(name = "Seller_ID", referencedColumnName = "SellerId")
    @JsonManagedReference
    private List<Bid> bidsList = new ArrayList<>();
   /* @OneToOne(mappedBy = "bidder",cascade = CascadeType.ALL)
    private Bid bid;*/



    private String BidderAddress;

    private String BidderCountry;

    private Integer rating;

    public Bidder(Integer rating, Long id, String bidderAddress, String bidderCountry) {
        this.rating = rating;
        this.id = id;
        BidderAddress = bidderAddress;
        BidderCountry = bidderCountry;
    }

    public Bidder() {
    }
    //public User getUser() {
        //return user;
    //}

    public Bidder(Integer rating) {
        this.rating = rating;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Bidder{" +
                "rating=" + rating +
                '}';
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof Bidder)) return false;
//        Bidder bidder = (Bidder) o;
//        return user.equals(bidder.user);
//    }

//    @Override
//    public int hashCode() {
//        return Objects.hash(user);
//    }
}
