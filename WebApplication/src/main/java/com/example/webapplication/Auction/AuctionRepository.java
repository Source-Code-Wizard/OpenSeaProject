package com.example.webapplication.Auction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AuctionRepository extends JpaRepository<Auction,Long> , JpaSpecificationExecutor<Auction>, PagingAndSortingRepository<Auction, Long> {
    @Query("SELECT a.auctionEndTime FROM Auction a WHERE a.auctionEndTime >= :now")
    List<Auction> findAllWithAuctionEndTimeAfter(@Param("now") LocalDateTime now);

//    @Query("SELECT a.seller_id FROM Auction a WHERE a.item_id = :auction_id")
//    Long getSellerId(@Param("auction_id") Long auction_id);
//    @Query("SELECT a FROM Auction a WHERE a.seller_id = :userId")
//    List<Auction> findUsersAuctions(@Param("userId") Long userId);

    Page<Auction> findAll(Specification<Auction> sp, Pageable pageable);

    Optional<Auction> findById(Long id);
}
