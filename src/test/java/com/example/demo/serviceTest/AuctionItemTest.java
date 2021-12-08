package com.example.demo.serviceTest;

import com.example.demo.controller.request.AuctionItemRequest;
import com.example.demo.controller.response.AuctionItemResponse;
import com.example.demo.model.AuctionItem;
import com.example.demo.model.Bid;
import com.example.demo.model.Item;
import com.example.demo.repository.AuctionItemRepository;
import com.example.demo.repository.BidRepository;
import com.example.demo.service.AuctionItemService;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuctionItemTest {
    @InjectMocks
    AuctionItemService auctionItemService;

    @Mock
    AuctionItemRepository auctionItemRepository;

    @Mock
    BidRepository bidRepository;

    @BeforeAll
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    /*
    * TESTS METHOD: AuctionItemService.getAuctionItemById(Long)
    * return the metadata of the auction item as well as the highest bidder's name and amount
    */
    @Test
    void getAuctionItemWithBids() {
        List<AuctionItemResponse> auctionItemResponses = new ArrayList<>();
        AuctionItem auctionItem = new AuctionItem();
        auctionItem.setAuctionItemId(1L);
        auctionItem.setReservePrice(1000L);
        auctionItem.setItemId("abcd");
        auctionItem.setItemDescription("item description");

        when(auctionItemRepository.findByAuctionItemId(auctionItem.getAuctionItemId())).thenReturn(Optional.of(auctionItem));

        List<Long> bidIds = LongStream.rangeClosed(1, 9)
                .boxed().collect(Collectors.toList());
        List<String> bidderNames = new ArrayList<>();
        bidderNames.add("ABC Dealership");
        bidderNames.add("DEF Dealership");
        bidderNames.add("GHI Dealership");
        bidderNames.add("GHI Dealership");
        bidderNames.add("JKL Dealership");
        bidderNames.add("ABC Dealership");
        bidderNames.add("DEF Dealership");
        bidderNames.add("DEF Dealership");
        bidderNames.add("ABC Dealership");
        List<Long> bidAmounts = new ArrayList<>();
        bidAmounts.add(20000L);
        bidAmounts.add(22000L);
        bidAmounts.add(25000L);
        bidAmounts.add(21000L);
        bidAmounts.add(27000L);
        bidAmounts.add(28000L);
        bidAmounts.add(28000L);
        bidAmounts.add(30000L);
        bidAmounts.add(32000L);

        List<Bid> bidsList = new ArrayList<>();
        bidIds.stream().forEach(id -> {
            Bid bid = new Bid();
            bid.setBidId(id);
            bid.setAuctionItemId(auctionItem.getAuctionItemId());
            bid.setBidderName(bidderNames.get(id.intValue() - 1));
            bid.setMaxAutoBidAmount(bidAmounts.get(id.intValue() - 1));

            bidsList.add(bid);
        });

        Bid[] bidsArray = bidsList.toArray(new Bid[bidsList.size()]);

        when(bidRepository.findByAuctionItemId(auctionItem.getAuctionItemId())).thenReturn(Optional.of(bidsArray));

        AuctionItemResponse auctionItemResponse = auctionItemService.getAuctionItemById(auctionItem.getAuctionItemId());
        assertNotNull(auctionItemResponse);
        assertEquals(32000L, auctionItemResponse.getCurrentBid());
        assertEquals("ABC Dealership", auctionItemResponse.getBidderName());
    }

    /*
     * TESTS METHOD: AuctionItemService.getAuctionItemById(Long)
     * return null for auction items without bids
     */
    @Test
    void getAuctionItemWithNoBids() {
        AuctionItem auctionItem = new AuctionItem();
        auctionItem.setAuctionItemId(3L);
        auctionItem.setReservePrice(2500L);
        auctionItem.setItemId("efgh");
        auctionItem.setItemDescription("another item description");

        when(auctionItemRepository.findByAuctionItemId(auctionItem.getAuctionItemId())).thenReturn(Optional.of(auctionItem));

        Bid[] bidsArray = new Bid[0];

        when(bidRepository.findByAuctionItemId(auctionItem.getAuctionItemId())).thenReturn(Optional.of(bidsArray));

        AuctionItemResponse auctionItemResponse = auctionItemService.getAuctionItemById(1L);
        assertNull(auctionItemResponse);
    }

    /*THIS TEST IS NOT WORKING*/
    /*
     * TESTS METHOD: AuctionItemService.addAuctionItems(auctionItemRequest)
     * return null for auction items without bids
     */
    @Test
    void addAnAuctionItem() {
        AuctionItem auctionItem = new AuctionItem();
        auctionItem.setAuctionItemId(3L);
        auctionItem.setReservePrice(2500L);
        auctionItem.setItemId("test item");
        auctionItem.setItemDescription("test item description");

        when(auctionItemRepository.save(auctionItem)).thenReturn(auctionItem);
        when(auctionItemRepository.findByAuctionItemId(auctionItem.getAuctionItemId())).thenReturn(Optional.of(auctionItem));

        AuctionItemRequest auctionItemRequest = new AuctionItemRequest();
        auctionItemRequest.setReservePrice(auctionItem.getReservePrice());
        auctionItemRequest.setItem(new Item(auctionItem.getItemId(), auctionItem.getItemDescription()));
        AuctionItem res = auctionItemService.addAuctionItems(auctionItemRequest);

        assertEquals(3L, res.getAuctionItemId());
    }
}
