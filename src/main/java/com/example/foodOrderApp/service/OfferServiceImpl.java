package com.example.foodOrderApp.service;

import com.example.foodOrderApp.entity.Offer;
import com.example.foodOrderApp.repository.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
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

    @Override
    public Page<Offer> getPaginatedOffers(Pageable pageable,String name) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Offer> offers = offerRepository.findAllByRestaurantName(name);
        List<Offer> list;

        if(offers.size() < startItem)
            list = Collections.emptyList();
        else{
            int toIndex = Math.min(startItem + pageSize, offers.size());
            list = offers.subList(startItem, toIndex);
        }

        return new PageImpl<>(list, PageRequest.of(currentPage,pageSize),offers.size());
    }

    @Override
    public Page<Offer> getPaginatedOffers(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Offer> offers = offerRepository.findAll();
        List<Offer> list;

        if(offers.size() < startItem)
            list = Collections.emptyList();
        else{
            int toIndex = Math.min(startItem + pageSize, offers.size());
            list = offers.subList(startItem, toIndex);
        }

        return new PageImpl<>(list, PageRequest.of(currentPage,pageSize),offers.size());
    }
}
