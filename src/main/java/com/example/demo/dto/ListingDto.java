package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListingDto {
    private String name;

    private Integer price;

    private String description;

    private Date submissionTime;

    private String photoUrl;
}
