package com.example.foodOrderApp.service;

import com.example.foodOrderApp.entity.Offer;

import java.util.List;

public interface OfferService {

    List<Offer> getAllOffers();
    List<Offer> getAllOffersByRestaurantName(String restaurantName);
    Offer addOffer(Offer offer);
    void deletOfferById(Long id);
    Offer gerOfferById(Long id);

}
