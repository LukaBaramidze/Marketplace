package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CollectionListingDto {
    private Collection<ListingDto> dtoCollection;
    private Integer recordsAmount;
}
