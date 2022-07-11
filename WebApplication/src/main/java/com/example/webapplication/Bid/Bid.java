package com.example.webapplication.Bid;

import com.example.webapplication.Bidder.Bidder;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table
public class Bid implements Serializable {

    @Id
    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "BidderUserId", referencedColumnName = "UserId"),
            @JoinColumn(name = "BidderUserAddress", referencedColumnName = "UserAddress"),
            @JoinColumn(name = "BidderUserCountry", referencedColumnName = "UserCountry")
    })
    private Bidder bidder;
    private LocalDateTime localBidDateTime;
    private Integer moneyAmount;

    public Bid() {
    }

    public Bid(LocalDateTime localBidDateTime, Integer moneyAmount) {
        this.localBidDateTime = localBidDateTime;
        this.moneyAmount = moneyAmount;
    }

    public LocalDateTime getLocalBidDateTime() {
        return localBidDateTime;
    }

    public void setLocalBidDateTime(LocalDateTime localBidDateTime) {
        this.localBidDateTime = localBidDateTime;
    }

    public Integer getMoneyAmount() {
        return moneyAmount;
    }

    public void setMoneyAmount(Integer moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    @Override
    public String toString() {
        return "Bid{" +
                "localBidDateTime=" + localBidDateTime +
                ", moneyAmount=" + moneyAmount +
                '}';
    }
}
