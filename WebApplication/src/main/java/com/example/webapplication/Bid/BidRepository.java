package com.example.webapplication.Bid;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BidRepository extends JpaRepository<Bid,Long> {
    @Query("SELECT b FROM Bid b WHERE b.auction.itemId = :itemId and b.bidder.id =:bidderId")
    Optional<Bid> myFind(@Param("itemId") Long itemId,@Param("bidderId") Long bidderId);

}
