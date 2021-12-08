package com.example.demo.service;

import com.example.demo.controller.request.AuctionItemRequest;
import com.example.demo.controller.response.AuctionItemResponse;
import com.example.demo.model.AuctionItem;
import com.example.demo.model.Bid;
import com.example.demo.model.Item;
import com.example.demo.repository.AuctionItemRepository;
import com.example.demo.repository.BidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuctionItemService {

    @Autowired
    private AuctionItemRepository auctionItemRepository;

    @Autowired
    private BidRepository bidRepository;

    public List<AuctionItemResponse> getAuctionItems() {
        List<AuctionItem> res = auctionItemRepository.findAll();
        List<AuctionItemResponse> auctionItemsList = new ArrayList<>();


        res.stream().forEach(item -> {
            AuctionItemResponse auctionItemResponse = new AuctionItemResponse();
            auctionItemResponse.setAuctionItemId(item.getAuctionItemId());
            auctionItemResponse.setReserePrice(item.getReservePrice());

            Item itm = new Item(item.getItemId(), item.getItemDescription());
            auctionItemResponse.setItem(itm);

            Bid noBid = new Bid(item.getAuctionItemId(), 0L, "N/A");

            //get the highest bid
            Optional<Bid[]> bidsOpt = bidRepository.findByAuctionItemId(item.getAuctionItemId());
            if(bidsOpt.isPresent()) {
                Bid[] bidsOnItem = bidsOpt.get();
                System.out.println("---------START--------");
                Arrays.stream(bidsOnItem).forEach(b -> System.out.println(b.toString()));
                System.out.println("---------END--------");
                if(bidsOnItem.length > 0) {
                    Bid currentHighestBid = Arrays.stream(bidsOnItem)
                            .sorted(Comparator.comparing(Bid::getMaxAutoBidAmount))
                            .reduce((first, last) -> last).get();

                    auctionItemResponse.setCurrentBid(currentHighestBid.getMaxAutoBidAmount());
                    auctionItemResponse.setBidderName(currentHighestBid.getBidderName());
                } else {
                    auctionItemResponse.setCurrentBid(noBid.getMaxAutoBidAmount());
                    auctionItemResponse.setBidderName(noBid.getBidderName());
                }
            } else {
                auctionItemResponse.setCurrentBid(noBid.getMaxAutoBidAmount());
                auctionItemResponse.setBidderName(noBid.getBidderName());
            }
            auctionItemsList.add(auctionItemResponse);
        });

        return auctionItemsList;
    }

    public AuctionItemResponse getAuctionItemById(Long id) {
        Optional<AuctionItem> res = auctionItemRepository.findByAuctionItemId(id);

        if(res.isPresent()) { //if auction item exists
            AuctionItem item = res.get();
            AuctionItemResponse auctionItemResponse = new AuctionItemResponse();
            auctionItemResponse.setAuctionItemId(item.getAuctionItemId());
            auctionItemResponse.setReserePrice(item.getReservePrice());

            Item itm = new Item(item.getItemId(), item.getItemDescription());
            auctionItemResponse.setItem(itm);

            Bid noBid = new Bid(item.getAuctionItemId(), 0L, "N/A");

            //get the highest bid on auction item found above
            Optional<Bid[]> bidsOpt = bidRepository.findByAuctionItemId(item.getAuctionItemId());
            if (bidsOpt.isPresent()) { // if bids exists on auction item
                Bid[] bidsOnItem = bidsOpt.get();
                System.out.println("---------START--------");
                Arrays.stream(bidsOnItem).forEach(b -> System.out.println(b.toString()));
                System.out.println("---------END--------");
                if (bidsOnItem.length > 0) { // if bids exists on auction item
                    Bid currentHighestBid = Arrays.stream(bidsOnItem)
                            .sorted(Comparator.comparing(Bid::getMaxAutoBidAmount))
                            .reduce((first, last) -> last).get();

                    auctionItemResponse.setCurrentBid(currentHighestBid.getMaxAutoBidAmount());
                    auctionItemResponse.setBidderName(currentHighestBid.getBidderName());
                } else {
                    auctionItemResponse.setCurrentBid(noBid.getMaxAutoBidAmount());
                    auctionItemResponse.setBidderName(noBid.getBidderName());
                }
            } else {
                auctionItemResponse.setCurrentBid(noBid.getMaxAutoBidAmount());
                auctionItemResponse.setBidderName(noBid.getBidderName());
            }
            return auctionItemResponse;
        }
        return null;
    }

    public AuctionItem addAuctionItems(AuctionItemRequest auctionItemRequest) {
        AuctionItem auctionItem = new AuctionItem(
                auctionItemRequest.getReservePrice(),
                auctionItemRequest.getItem().getItemId(),
                auctionItemRequest.getItem().getDescription()
                );
        System.out.println("-------------------");
        AuctionItem newAuctionItem = auctionItemRepository.save(auctionItem);
        System.out.println(newAuctionItem);
        Optional<AuctionItem> res = auctionItemRepository.findByAuctionItemId(newAuctionItem.getAuctionItemId());
        return res.get();
    }
}
