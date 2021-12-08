package com.example.demo.service;

import com.example.demo.controller.request.BidRequest;
import com.example.demo.model.AuctionItem;
import com.example.demo.model.Bid;
import com.example.demo.repository.AuctionItemRepository;
import com.example.demo.repository.BidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

@Service
public class BidService {

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private AuctionItemRepository auctionItemRepository;

    public String bidOnAuctionItem(BidRequest bidRequest) {
        System.out.println("bid: " + bidRequest.toString());
        Optional<AuctionItem> opt = auctionItemRepository.findByAuctionItemId(bidRequest.getAuctionItemId());
        if(opt.isPresent()) { //if auction item found
            AuctionItem auctionItem = opt.get();
            System.out.println("found auction item " + auctionItem.toString());

            //if bidder bids too low
            if(bidRequest.getMaxAutoBidAmount() < auctionItem.getReservePrice())
                return "Your bid is lower than the reserve Price! Please bid a higher amount.";

            Bid bid = new Bid(
                    bidRequest.getAuctionItemId(),
                    bidRequest.getMaxAutoBidAmount(),
                    bidRequest.getBidderName());
            Optional<Bid[]> bidOpt = bidRepository.findByAuctionItemId(bidRequest.getAuctionItemId());
            if(bidOpt.isPresent()) { // if there are previous bids on auction item
                Bid[] bids = bidOpt.get();
                Optional<Bid> currentHighestBid = Arrays.stream(bids)
                        .sorted(Comparator.comparing(Bid::getMaxAutoBidAmount))
                        .reduce((first, last) -> last);
                System.out.println("current highest bid " + currentHighestBid.get().getMaxAutoBidAmount() + " - " + currentHighestBid.get().getBidderName());

                System.out.println("adding bid " + bid.getMaxAutoBidAmount());
                bidRepository.save(bid);

                // if the new bid is highest bid
                if (bid.getMaxAutoBidAmount() > currentHighestBid.get().getMaxAutoBidAmount() + 1 &&
                        !bid.getBidderName().equals(currentHighestBid.get().getBidderName()))
                    return "Bidder " + currentHighestBid.get().getBidderName() +
                            " has been outbid by " + bid.getBidderName() + "!";

            } else { // if no other bids were made on auction item
                System.out.println("adding bid " + bid.getMaxAutoBidAmount());
                bidRepository.save(bid);
            }

            return null;
        }

        //if user bids on an auction item that doesn't exist
        return "The auction item " + bidRequest.getAuctionItemId() + " does not exist";
    }
}
