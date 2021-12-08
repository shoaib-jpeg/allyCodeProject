package com.example.demo.controller;

import com.example.demo.controller.request.BidRequest;
import com.example.demo.model.Bid;
import com.example.demo.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "api/bids")
public class BidController {

    @Autowired
    private BidService bidService;

    /*
    * Add bid to specified auction item
    * */
    @PostMapping
    public ResponseEntity<Bid> bidOnAuctionItem(@RequestBody BidRequest bidRequest) {
        System.out.println("////////////////" + bidRequest.toString());
        String res = bidService.bidOnAuctionItem(bidRequest);
        return new ResponseEntity(res, HttpStatus.OK);
    }
}
