package com.example.foodOrderApp.service;

import com.example.foodOrderApp.entity.City;

import java.util.List;

public interface CityService {

  void addCities(List<City> cities);
  List<City> getAllCities();
  City addCity(City city);

  City updateCity(City city);
  void deleteCity(City city);

  void deleteCityById(long id);

}
