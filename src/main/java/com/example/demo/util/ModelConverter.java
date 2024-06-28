package com.example.demo.util;

import com.example.demo.dto.CollectionListingDto;
import com.example.demo.dto.ListingDto;
import com.example.demo.model.Listing;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ModelConverter {
    public ListingDto convert(Listing listing){
        return ListingDto.builder()
                .name(listing.getName())
                .price(listing.getPrice())
                .description(listing.getDescription())
                .submissionTime(listing.getSubmissionTime())
                .photoUrl(listing.getPhotoUrl())
                .build();
    }
    public Listing convert(ListingDto listing){
        return Listing.builder()
                .name(listing.getName())
                .price(listing.getPrice())
                .description(listing.getDescription())
                .submissionTime(listing.getSubmissionTime())
                .photoUrl(listing.getPhotoUrl())
                .build();
    }
    public CollectionListingDto convert(List<Listing> list){
        return CollectionListingDto.builder()
                .dtoCollection(list.stream().map(this::convert).toList())
                .recordsAmount(list.size())
                .build();
    }


}
