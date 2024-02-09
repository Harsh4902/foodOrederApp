package com.example.foodOrderApp.service;

import com.example.foodOrderApp.entity.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CityService {

  void addCities(List<City> cities);
  List<City> getAllCities();
  City addCity(City city);

  City updateCity(City city);
  void deleteCity(City city);

  void deleteCityById(long id);

  City findCityById(Long id);

  Page<City> getPaginatedCities(Pageable pageable);

}
