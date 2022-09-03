package com.example.webapplication.Bid;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@EqualsAndHashCode
@Getter
@Setter
@ToString
public class bidDTO {

    private String username;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime bidSubmittedTime;
    private Long auctionId;

    private double moneyOffered;

    public bidDTO() {}

    public bidDTO(String username, LocalDateTime bidSubmittedTime, Long auctionId,double moneyOffered) {
        this.username = username;
        this.bidSubmittedTime = bidSubmittedTime;
        this.auctionId = auctionId;
        this.moneyOffered=moneyOffered;
    }
}
