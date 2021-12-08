package com.example.demo.repository;

import com.example.demo.model.AuctionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuctionItemRepository extends JpaRepository<AuctionItem, Long> {
    Optional<AuctionItem> findByItemId(String id);

    Optional<AuctionItem> findByAuctionItemId(Long id);

    Optional<AuctionItem> findFirstByOrderByReservePriceDesc();
}
