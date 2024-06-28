package com.example.demo.controller;

import com.example.demo.dto.ListingDto;
import com.example.demo.service.MarketService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.val;

import java.util.Optional;

@RestController
@RequestMapping("/market")
public class MarketController {
    private final MarketService marketService;

    @Autowired
    public MarketController(MarketService marketService){
        this.marketService = marketService;
    }
    @GetMapping
    @ResponseBody
    public ResponseEntity<?> getAllListings(@RequestParam("pageNum") Optional<Integer> pageNum,
                                         @RequestParam("pageSize") Optional<Integer> pageSize){
        try {
            if (pageNum.isPresent() && pageSize.isPresent()) {
                int pageNumValue = pageNum.get();
                int pageSizeValue = pageSize.get();
                val listings = marketService.getListingsByPage(pageNumValue, pageSizeValue);
                return ResponseEntity.status(HttpStatus.OK).body(listings);
            } else {
                val listings = marketService.getAllListings();
                return ResponseEntity.status(HttpStatus.OK).body(listings);
            }
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }


    }

    @GetMapping("/{name}")
    @ResponseBody
    public ResponseEntity<?> getListing(@PathVariable("name") String name) {
        try {
            val temp = marketService.getListingByName(name);
            return ResponseEntity.status(HttpStatus.OK).body(temp);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<?> addListing(@RequestBody ListingDto listing) {
        try {
            val temp = marketService.addListing(listing);
            return ResponseEntity.status(HttpStatus.CREATED).body(temp);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{name}")
    @ResponseBody
    public ResponseEntity<?> updateListing(@PathVariable("name") String name, @RequestBody ListingDto listing) {
        try {
            val temp = marketService.updateListing(listing, name);
            return ResponseEntity.status(HttpStatus.CREATED).body(temp);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{name}")
    @ResponseBody
    public ResponseEntity<?> removeListing(@PathVariable("name") String name) {
        try {
            val temp = marketService.deleteListing(name);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(temp);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
