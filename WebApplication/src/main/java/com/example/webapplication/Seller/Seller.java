
package com.example.webapplication.Seller;

import com.example.webapplication.Bid.Bid;
import com.example.webapplication.User.User;
import com.example.webapplication.Auction.Auction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="Sellers")
public class Seller implements Serializable {
    private int rating;
    /*@Id
    @OneToOne
    @JoinColumn(name = "SellerId", referencedColumnName = "userId")
    private User user;*/

    @Id
    @Column(name = "user_id")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;


    @OneToMany(mappedBy = "seller" ,cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    // we create to auction-table a new column named:Seller_ID which refers to Seller.sellerId column
    //@JoinColumn(name = "Seller_ID", referencedColumnName = "SellerId")
    private List<Auction> sellersAuctions = new ArrayList<>();

    public Seller() {
    }

    public Seller(int rating) {
        this.rating = rating;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Seller{" +
                "rating=" + rating +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Seller)) return false;
        Seller seller = (Seller) o;
        return getRating() == seller.getRating() && user.equals(seller.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRating(), user);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

