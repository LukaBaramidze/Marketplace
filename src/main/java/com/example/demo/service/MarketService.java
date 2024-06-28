package com.example.demo.service;

import com.example.demo.dto.CollectionListingDto;
import com.example.demo.dto.ListingDto;
import com.example.demo.model.Listing;
import com.example.demo.repository.MarketRepository;
import com.example.demo.util.ModelConverter;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MarketService {
    private final ModelConverter modelConverter;
    private final MarketRepository marketRepository;

    private static final String PHOTO_PREFIX = "-image";

    @Autowired
    public MarketService(ModelConverter modelConverter, MarketRepository marketRepository){
        this.modelConverter = modelConverter;
        this.marketRepository = marketRepository;
    }

    public CollectionListingDto getListingsByPage(int pageNum, int pageSize){
        Sort sort = Sort.by("submissionTime").descending();
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        val Listings = marketRepository.findAll(pageable).stream().toList();
        return modelConverter.convert(Listings);
    }

    public CollectionListingDto getAllListings(){
        val Listings = marketRepository.findAll().stream().toList();
        return modelConverter.convert(Listings);
    }

    public ListingDto getListingByName(String listingName){
        val Listing = marketRepository.findByName(listingName)
                .orElseThrow(() -> new EntityNotFoundException("Listing:" + listingName + " not found"));
        return modelConverter.convert(Listing);
    }

    public ListingDto addListing(ListingDto listing){
        Listing convertedListing = modelConverter.convert(listing);
        convertedListing.setSubmissionTime(new Date());
        marketRepository.save(convertedListing);
        return listing;
    }
    @Transactional
    public ListingDto updateListing(ListingDto newListing, String name){
        try {
            val listing = getListingByName(name);
            Listing oldlisting = modelConverter.convert(listing);
            oldlisting.setName(newListing.getName());
            oldlisting.setPrice(newListing.getPrice());
            oldlisting.setDescription(newListing.getDescription());
            oldlisting.setSubmissionTime(newListing.getSubmissionTime());
            oldlisting.setPhotoUrl(newListing.getPhotoUrl());
            deleteListing(name);
            return modelConverter.convert(marketRepository.save(oldlisting));
        } catch (EntityNotFoundException ignored) {
            return null;
        }
    }
    @Transactional
    public ListingDto addPhotoToListing(String name){
        try {
            val listing = getListingByName(name);
            Listing oldlisting = modelConverter.convert(listing);
            oldlisting.setPhotoUrl(name + PHOTO_PREFIX);
            deleteListing(name);
            return modelConverter.convert(marketRepository.save(oldlisting));
        } catch (EntityNotFoundException ignored) {
            return null;
        }
    }

    @Transactional
    public int deleteListing(String name) {
        return marketRepository.deleteByName(name);
    }
}
