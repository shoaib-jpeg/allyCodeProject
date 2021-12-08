package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bidId;

    @Column
    private Long auctionItemId;

    @Column
    private Long maxAutoBidAmount;

    @Column
    private String bidderName;

    public Bid(Long auctionItemId, Long maxAutoBidAmount, String bidderName) {
        this.auctionItemId = auctionItemId;
        this.maxAutoBidAmount = maxAutoBidAmount;
        this.bidderName = bidderName;
    }
}
