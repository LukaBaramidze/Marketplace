package com.example.demo.controller;

import com.example.demo.dto.ListingDto;
import com.example.demo.service.MarketService;
import com.example.demo.service.PhotoService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/photo")
public class PhotoController {

    private final MarketService marketService;
    private final PhotoService photoService;

    @Autowired
    public PhotoController(MarketService marketService, PhotoService photoService) {
        this.marketService = marketService;
        this.photoService = photoService;
    }

    @PostMapping
    public ResponseEntity<?> uploadPhoto(@RequestParam("photo") MultipartFile photo, @RequestParam("name") String name) {
        try {
            ListingDto listingDto = marketService.addPhotoToListing(name);
            photoService.storePhoto(photo, listingDto.getPhotoUrl());
            return ResponseEntity.noContent().build();
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllPhotos() {
        try {
            val photos = photoService.getAllPhotos();
            return ResponseEntity.ok(photos);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
