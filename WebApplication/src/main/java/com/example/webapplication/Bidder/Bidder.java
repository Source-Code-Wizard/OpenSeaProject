package com.example.webapplication.Bidder;

import com.example.webapplication.User.User;
import javax.persistence.*;

@Entity
@Table(name = "Bidders")
public class Bidder {
    private Integer rating;
    @Id
    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "UserId", referencedColumnName = "userId"),
            @JoinColumn(name = "UserAddress", referencedColumnName = "address"),
            @JoinColumn(name = "UserCountry", referencedColumnName = "Country")
    }
    )
    private User user;

    public Bidder() {
    }

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
}
