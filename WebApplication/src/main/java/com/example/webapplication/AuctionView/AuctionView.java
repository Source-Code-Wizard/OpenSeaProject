package com.example.webapplication.AuctionView;

import com.example.webapplication.Auction.Auction;
import com.example.webapplication.Seller.Seller;
import com.example.webapplication.User.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class AuctionView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",referencedColumnName = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id",referencedColumnName = "item_id")
    private Auction auction;

    public AuctionView(User user, Auction auction) {
        this.user = user;
        this.auction = auction;
    }

    public AuctionView() {
    }
}
