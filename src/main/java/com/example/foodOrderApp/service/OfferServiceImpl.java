package com.example.foodOrderApp.service;

import com.example.foodOrderApp.entity.Offer;
import com.example.foodOrderApp.repository.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfferServiceImpl implements OfferService{

    @Autowired
    private OfferRepository offerRepository;

    @Override
    public List<Offer> getAllOffers() {
        return offerRepository.findAll();
    }

    @Override
    public List<Offer> getAllOffersByRestaurantName(String restaurantName) {
        return offerRepository.findAllByRestaurantName(restaurantName);
    }

    @Override
    public Offer addOffer(Offer offer) {
        return offerRepository.save(offer);
    }

    @Override
    public void deletOfferById(Long id) {
        offerRepository.deleteById(id);
    }

    @Override
    public Offer gerOfferById(Long id) {
        return offerRepository.findById(id).orElseThrow();
    }
}
