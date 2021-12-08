package com.example.demo.controllerTest;

import com.example.demo.controller.AuctionItemController;
import com.example.demo.controller.request.AuctionItemRequest;
import com.example.demo.controller.response.AuctionItemResponse;
import com.example.demo.model.AuctionItem;
import com.example.demo.model.Item;
import com.example.demo.service.AuctionItemService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuctionItemControllerTest {

    @InjectMocks
    AuctionItemController auctionItemController;

    @Mock
    AuctionItemService auctionItemService;

    @BeforeAll
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    /*
     * TESTS METHOD: AuctionItemController.getAuctionItem(Long)
     * Should return a message stating that no auction item was found"
     */
    @Test
    void getNonExistentAuctionItem() {
        AuctionItemResponse auctionItemResponse = new AuctionItemResponse();
        auctionItemResponse.setAuctionItemId(4L);
        auctionItemResponse.setReserePrice(5000L);
        auctionItemResponse.setItem(new Item("test item", "test item description"));
        auctionItemResponse.setBidderName("N/A");
        auctionItemResponse.setCurrentBid(0L);

        when(auctionItemService.getAuctionItemById(4L)).thenReturn(auctionItemResponse);

        ResponseEntity<AuctionItemResponse> response = auctionItemController.getAuctionItem(5L);

        System.out.println("test result: \n" + response.getBody());
        assertEquals("No auction item found.", response.getBody());
    }

    /*
     * TESTS METHOD: AuctionItemController.addAuctionItems(Long)
     * Should return status 400 stating invalid parameters
     */
    @Test
    void addAuctionItemInvalid() {
        AuctionItemRequest auctionItemRequest = new AuctionItemRequest();
        auctionItemRequest.setItem(new Item("test item", "test item description"));

        ResponseEntity<AuctionItem> response = auctionItemController.addAuctionItems(auctionItemRequest);
        assertNotNull(response);
        System.out.println("test result: \n" + response.getBody());
        assertEquals("{\"error\":\"400 BAD REQUEST: invalid data provided\"}", response.getBody());
    }
}
