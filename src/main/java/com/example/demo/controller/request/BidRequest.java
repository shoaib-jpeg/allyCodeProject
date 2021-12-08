package com.example.demo.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BidRequest {
    private Long auctionItemId;
    private Long maxAutoBidAmount;
    private String bidderName;
}
