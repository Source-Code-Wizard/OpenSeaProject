package com.example.webapplication.Auction;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AuctionDTO {
    private String name;

    private double Currently;

    private double buyPrice;

    private double firstBid;

    private int numOfBids;

    private String location;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime auctionStartedTime;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime auctionEndTime;

    private String description;

    private List<String> listOfCategories = new ArrayList<>();

    private Long seller_id;

    public AuctionDTO(String name, double currently, double buyPrice, double firstBid, int numOfBids,
                      String location, LocalDateTime auctionEndTime,
                      String description, Long seller_id ,List<String> listOfCategories) {
        this.name = name;
        Currently = currently;
        this.buyPrice = buyPrice;
        this.firstBid = firstBid;
        this.numOfBids = numOfBids;
        this.location = location;
        this.auctionStartedTime = LocalDateTime.now();
        this.auctionEndTime = auctionEndTime;
        this.description = description;
        this.listOfCategories = listOfCategories;
        this.seller_id=seller_id;
    }
}
