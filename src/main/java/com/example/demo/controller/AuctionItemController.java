package com.example.demo.controller;

import com.example.demo.controller.request.AuctionItemRequest;
import com.example.demo.controller.request.BidRequest;
import com.example.demo.controller.response.AuctionItemResponse;
import com.example.demo.model.AuctionItem;
import com.example.demo.service.AuctionItemService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/auctionItems")
public class AuctionItemController {

    Gson gson = new Gson();

    @Autowired
    private AuctionItemService auctionItemService;

    /*
    * Get all auction items with their highest bid
    * */
    @GetMapping
    public ResponseEntity<AuctionItem> getAuctionItems() {
        List<AuctionItemResponse> auctionItemsList = auctionItemService.getAuctionItems();

        if(auctionItemsList.size() == 0) // if auctionItems table is empty
            return new ResponseEntity(gson.toJson("No auction items are listed."), HttpStatus.OK);

        return new ResponseEntity(auctionItemsList, HttpStatus.OK);
    }

    /*
    * Get auction item with specified id
    * */
    @GetMapping(path = "/{auctionItemId}")
    public ResponseEntity<AuctionItemResponse> getAuctionItem(@PathVariable("auctionItemId") Long id) {
        System.out.println("////////////////" + id);
        AuctionItemResponse res = auctionItemService.getAuctionItemById(id);

        if(res == null) //if an non-existent id was provided
            return new ResponseEntity("No auction item found.", HttpStatus.OK);

        return new ResponseEntity(res, HttpStatus.OK);
    }

    /*
    * Add a new auction item
    * */
    @PostMapping
    public ResponseEntity<AuctionItem> addAuctionItems(@RequestBody AuctionItemRequest auctionItemRequest) {
        System.out.println("////////////////" + auctionItemRequest.toString());

        if(auctionItemRequest.getReservePrice() == null ||
           auctionItemRequest.getItem().getItemId() == null) {
            HashMap<String, String> map = new HashMap<>();
            map.put("error","400 BAD REQUEST: invalid data provided"); // if invalid request is provided
            return new ResponseEntity(gson.toJson(map), HttpStatus.BAD_REQUEST);
        }

        AuctionItem res = auctionItemService.addAuctionItems(auctionItemRequest);

        HashMap<String, Long> auctionItem = new HashMap<>();
        auctionItem.put("auctionItemId", res.getAuctionItemId());
        return new ResponseEntity(auctionItem, HttpStatus.CREATED);
    }
}
