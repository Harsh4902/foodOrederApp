package com.example.foodOrderApp.service;

import com.example.foodOrderApp.entity.City;
import com.example.foodOrderApp.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityServiceImpl implements CityService{

  @Autowired
  private CityRepository cityRepository;

  @Override
  public void addCities(List<City> cities) {
    cityRepository.saveAll(cities);
  }

  @Override
  public List<City> getAllCities() {
    return cityRepository.findAll();
  }

  @Override
  public City addCity(City city) {
    return cityRepository.save(city);
  }

  @Override
  public City updateCity(City city) {
    City temp = cityRepository.findById(city.getId()).orElseThrow();
    temp.setCityName(city.getCityName());
    temp.setDescription(city.getDescription());
    return cityRepository.save(temp);
  }

  @Override
  public void deleteCity(City city) {
    cityRepository.delete(city);
  }

  @Override
  public void deleteCityById(long id) {
    cityRepository.deleteById(id);
  }
}
