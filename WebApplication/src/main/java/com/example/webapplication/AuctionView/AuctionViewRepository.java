package com.example.webapplication.AuctionView;

import com.example.webapplication.Auction.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AuctionViewRepository extends JpaRepository<AuctionView,Long> {
    @Query("SELECT av FROM AuctionView av WHERE av.user.userId = :userId")
    List<AuctionView> findAuctionViewByUser(@Param("userId") Long userId);
}
