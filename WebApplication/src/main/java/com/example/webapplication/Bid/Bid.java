package com.example.webapplication.Bid;

import com.example.webapplication.Bidder.Bidder;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table
public class Bid implements Serializable {

    @Id
    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "BidderUserId", referencedColumnName = "UserId"),
          /*  @JoinColumn(name = "BidderUserAddress", referencedColumnName = "UserAddress"),
            @JoinColumn(name = "BidderUserCountry", referencedColumnName = "UserCountry")*/
    })
    private Bidder bidder;
    private LocalDateTime localBidDateTime;

    private String BidderAddress;

    private String BidderCountry;
    private Integer moneyAmount;

    public Bid() {
    }

    public Bid(LocalDateTime localBidDateTime, String bidderAddress, String bidderCountry, Integer moneyAmount) {
        this.bidder = bidder;
        this.localBidDateTime = localBidDateTime;
        BidderAddress = bidderAddress;
        BidderCountry = bidderCountry;
        this.moneyAmount = moneyAmount;
    }

    public String getBidderAddress() {
        return BidderAddress;
    }

    public void setBidderAddress(String bidderAddress) {
        BidderAddress = bidderAddress;
    }

    public String getBidderCountry() {
        return BidderCountry;
    }

    public void setBidderCountry(String bidderCountry) {
        BidderCountry = bidderCountry;
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

    public Bidder getBidder() {
        return bidder;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bid)) return false;
        Bid bid = (Bid) o;
        return bidder.equals(bid.bidder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bidder);
    }
}
