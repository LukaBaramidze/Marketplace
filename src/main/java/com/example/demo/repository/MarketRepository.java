package com.example.demo.repository;

import com.example.demo.model.Listing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarketRepository extends JpaRepository<Listing, Integer> {
    Optional<Listing> findByName(String name);


    int deleteByName(String name);
}
