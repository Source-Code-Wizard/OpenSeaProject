package com.example.webapplication.Bid;

import com.example.webapplication.Auction.Auction;
import com.example.webapplication.Bidder.Bidder;
import com.example.webapplication.Seller.Seller;
import com.example.webapplication.User.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class Bid implements Serializable,Comparable<Bid> {
   /* @Id
    @OneToOne
    @JoinColumn(name = "BidderUserId", referencedColumnName = "user_id")
    private Bidder bidder;*/
   @Override
   public int compareTo(Bid d) {
       return (int) (d.getMoneyAmount()-this.getMoneyAmount());
   }
    @Id
    @SequenceGenerator(
           name= "user_sequence", sequenceName = "user_sequence",allocationSize = 1
    )
    @GeneratedValue( strategy = GenerationType.SEQUENCE,generator = "user_sequence")
    @Column(name="bid_id")
    private Long bid_id;

  /*  @Column(name = "user_id")
    private Long id;

    @OneToOne
    @MapsId
    @JsonIgnore
    @JoinColumn(name = "user_id")*/

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bidder_id", referencedColumnName = "bidder_id")
    private Bidder bidder;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime localBidDateTime;

    private Double moneyAmount;
    private String bidderUsername;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "item_id" /*nullable = false*/)
    private Auction auction;

    public Bid() {
    }

    public Bid(LocalDateTime localBidDateTime,  double moneyAmount,String bidderUsername) {
        this.localBidDateTime = localBidDateTime;
        this.moneyAmount = moneyAmount;
        this.bidderUsername = bidderUsername;
    }


}
