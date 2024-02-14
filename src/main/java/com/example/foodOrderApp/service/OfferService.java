package com.example.foodOrderApp.service;

import com.example.foodOrderApp.entity.Offer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OfferService {

    List<Offer> getAllOffers();
    List<Offer> getAllOffersByRestaurantName(String restaurantName);
    Offer addOffer(Offer offer);
    void deletOfferById(Long id);
    Offer gerOfferById(Long id);
    Page<Offer> getPaginatedOffers(Pageable pageable,String name);

    Page<Offer> getPaginatedOffers(Pageable pageable);

}
