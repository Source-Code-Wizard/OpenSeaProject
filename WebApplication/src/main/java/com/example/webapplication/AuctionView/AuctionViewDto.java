package com.example.webapplication.AuctionView;

import lombok.*;

import javax.persistence.Table;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class AuctionViewDto {
    private String username;
    private Long auctionId;

    public AuctionViewDto(String username, Long auctionId) {
        this.username = username;
        this.auctionId = auctionId;
    }
}
