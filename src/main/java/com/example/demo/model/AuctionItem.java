package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
public class AuctionItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long auctionItemId;

    @Column
    private Long reservePrice = 0L;

    @Column
    private String itemId;

    @Column
    private String itemDescription;

    public AuctionItem(Long reservePrice, String itemId, String itemDescription) {
        this.reservePrice = reservePrice;
        this.itemId = itemId;
        this.itemDescription = itemDescription;
    }
}
