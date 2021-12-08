package com.example.demo.controller.response;

import com.example.demo.model.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuctionItemResponse {
    private Long auctionItemId;
    private Long reserePrice;
    private Item item;
    private Long currentBid;
    private String bidderName;
}
