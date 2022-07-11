package com.example.webapplication.Seller;

import com.example.webapplication.User.User;

import javax.persistence.*;

@Entity
@Table(name="Sellers")
public class Seller {
    private int rating;

    @Id
    private Long id;

    /*@Id
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name="Sellerid",
            referencedColumnName = "userId"
    )*/
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private User user;

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
}
