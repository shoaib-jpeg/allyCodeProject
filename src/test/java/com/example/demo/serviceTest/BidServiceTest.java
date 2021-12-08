package com.example.demo.serviceTest;

import com.example.demo.controller.request.BidRequest;
import com.example.demo.controller.response.AuctionItemResponse;
import com.example.demo.model.AuctionItem;
import com.example.demo.model.Bid;
import com.example.demo.repository.AuctionItemRepository;
import com.example.demo.repository.BidRepository;
import com.example.demo.service.AuctionItemService;
import com.example.demo.service.BidService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BidServiceTest {
    @InjectMocks
    BidService bidService;

    @Mock
    AuctionItemRepository auctionItemRepository;

    @Mock
    BidRepository bidRepository;

    @BeforeAll
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    /*
     * TESTS METHOD: BidService.bidOnAuctionItem(BidRequest)
     * return message telling previous highest bidder that they have been outbid
     */
    @Test
    void addNewHighestBidToExistingItem() {
        AuctionItem auctionItem = new AuctionItem();
        auctionItem.setAuctionItemId(1L);
        auctionItem.setReservePrice(1000L);
        auctionItem.setItemId("test item");
        auctionItem.setItemDescription("test item description");

        when(auctionItemRepository.findByAuctionItemId(auctionItem.getAuctionItemId())).thenReturn(Optional.of(auctionItem));

        List<Bid> bids = new ArrayList<>();
        bids.add(new Bid(1L,	auctionItem.getAuctionItemId(), 20000L,	"ABC Dealership"));
        bids.add(new Bid(2L,	auctionItem.getAuctionItemId(),	22000L, "DEF Dealership"));
        bids.add(new Bid(3L,	auctionItem.getAuctionItemId(), 25000L,	"GHI Dealership"));
        bids.add(new Bid(4L,	auctionItem.getAuctionItemId(), 23500L,	"ABC Dealership"));

        when(bidRepository.findByAuctionItemId(auctionItem.getAuctionItemId())).thenReturn(Optional.of(bids.toArray(new Bid[bids.size()])));

        BidRequest bidRequest = new BidRequest();
        bidRequest.setAuctionItemId(auctionItem.getAuctionItemId());
        bidRequest.setBidderName("DEF Dealership");
        bidRequest.setMaxAutoBidAmount(30000L);
        Bid newBid = new Bid(bidRequest.getAuctionItemId(), bidRequest.getMaxAutoBidAmount(), bidRequest.getBidderName());

        when(bidRepository.save(newBid)).thenReturn(newBid);

        String res = bidService.bidOnAuctionItem(bidRequest);
        System.out.println("test result: \n" + res);
        assertNotNull(res);
    }

    /*
     * TESTS METHOD: BidService.bidOnAuctionItem(BidRequest)
     * return null since no exception is needed
     */
    @Test
    void addNewNonHighestBidToExistingItem() {
        AuctionItem auctionItem = new AuctionItem();
        auctionItem.setAuctionItemId(1L);
        auctionItem.setReservePrice(1000L);
        auctionItem.setItemId("test item");
        auctionItem.setItemDescription("test item description");

        when(auctionItemRepository.findByAuctionItemId(auctionItem.getAuctionItemId())).thenReturn(Optional.of(auctionItem));

        List<Bid> bids = new ArrayList<>();
        bids.add(new Bid(1L,	auctionItem.getAuctionItemId(), 20000L,	"ABC Dealership"));
        bids.add(new Bid(2L,	auctionItem.getAuctionItemId(),	22000L, "DEF Dealership"));
        bids.add(new Bid(3L,	auctionItem.getAuctionItemId(), 25000L,	"GHI Dealership"));
        bids.add(new Bid(4L,	auctionItem.getAuctionItemId(), 23500L,	"ABC Dealership"));

        when(bidRepository.findByAuctionItemId(auctionItem.getAuctionItemId())).thenReturn(Optional.of(bids.toArray(new Bid[bids.size()])));

        BidRequest bidRequest = new BidRequest();
        bidRequest.setAuctionItemId(auctionItem.getAuctionItemId());
        bidRequest.setBidderName("DEF Dealership");
        bidRequest.setMaxAutoBidAmount(24000L);
        Bid newBid = new Bid(bidRequest.getAuctionItemId(), bidRequest.getMaxAutoBidAmount(), bidRequest.getBidderName());

        when(bidRepository.save(newBid)).thenReturn(newBid);

        String res = bidService.bidOnAuctionItem(bidRequest);
        System.out.println("test result: \n" + res);
        assertNull(res);
    }

    /*
     * TESTS METHOD: BidService.bidOnAuctionItem(BidRequest)
     * return message stating that specified auction item was not found
     */
    @Test
    void addNewBidToNonExistentItem() {
        AuctionItem auctionItem = new AuctionItem();
        auctionItem.setAuctionItemId(5L);
        auctionItem.setReservePrice(1000L);
        auctionItem.setItemId("test item");
        auctionItem.setItemDescription("test item description");

        when(auctionItemRepository.findByAuctionItemId(auctionItem.getAuctionItemId())).thenReturn(Optional.of(auctionItem));

        List<Bid> bids = new ArrayList<>();
        bids.add(new Bid(1L,	auctionItem.getAuctionItemId(), 20000L,	"ABC Dealership"));
        bids.add(new Bid(2L,	auctionItem.getAuctionItemId(),	22000L, "DEF Dealership"));
        bids.add(new Bid(3L,	auctionItem.getAuctionItemId(), 25000L,	"GHI Dealership"));
        bids.add(new Bid(4L,	auctionItem.getAuctionItemId(), 23500L,	"ABC Dealership"));

        when(bidRepository.findByAuctionItemId(1L))
                .thenReturn(Optional.of(bids.toArray(new Bid[bids.size()])));

        BidRequest bidRequest = new BidRequest();
        bidRequest.setAuctionItemId(6L);
        bidRequest.setBidderName("DEF Dealership");
        bidRequest.setMaxAutoBidAmount(24000L);
        Bid newBid = new Bid(bidRequest.getAuctionItemId(), bidRequest.getMaxAutoBidAmount(), bidRequest.getBidderName());

        when(bidRepository.save(newBid)).thenReturn(newBid);

        String res = bidService.bidOnAuctionItem(bidRequest);
        System.out.println("test result: \n" + res);
        assertEquals("The auction item " + bidRequest.getAuctionItemId() + " does not exist", res);
    }

    /*
     * TESTS METHOD: BidService.bidOnAuctionItem(BidRequest)
     * return message telling bidder their bid is lower than the reserve price
     */
    @Test
    void addNewBidLessThanReservePrice() {
        AuctionItem auctionItem = new AuctionItem();
        auctionItem.setAuctionItemId(1L);
        auctionItem.setReservePrice(1000L);
        auctionItem.setItemId("test item");
        auctionItem.setItemDescription("test item description");

        when(auctionItemRepository.findByAuctionItemId(auctionItem.getAuctionItemId())).thenReturn(Optional.of(auctionItem));

        BidRequest bidRequest = new BidRequest();
        bidRequest.setAuctionItemId(auctionItem.getAuctionItemId());
        bidRequest.setBidderName("DEF Dealership");
        bidRequest.setMaxAutoBidAmount(400L);
        Bid newBid = new Bid(bidRequest.getAuctionItemId(), bidRequest.getMaxAutoBidAmount(), bidRequest.getBidderName());

        when(bidRepository.save(newBid)).thenReturn(newBid);

        String res = bidService.bidOnAuctionItem(bidRequest);
        System.out.println("test result: \n" + res);
        assertEquals("Your bid is lower than the reserve Price! Please bid a higher amount.", res);
    }
}
