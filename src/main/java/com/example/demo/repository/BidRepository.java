package com.example.demo.repository;

import com.example.demo.model.AuctionItem;
import com.example.demo.model.Bid;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
//    @Query("SELECT auction_item_id, item_id, item_description,\n" +
//            "\t(SELECT max(max_auto_bid_amount)\n" +
//            "\t\tfrom BID \n" +
//            "\t\twhere auction_item_id = AUCTION_ITEM.auction_item_id\n" +
//            "\t) as max\n" +
//            "\tfrom AUCTION_ITEM where auction_item_id = \"" + "\"")
    Optional<Bid[]> findByAuctionItemId(Long id);
}
