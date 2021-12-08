package com.example.demo.controller.request;

import com.example.demo.model.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuctionItemRequest {
    private Long reservePrice;
    private Item item;
}
