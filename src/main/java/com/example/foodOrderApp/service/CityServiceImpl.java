package com.example.foodOrderApp.service;

import com.example.foodOrderApp.entity.City;
import com.example.foodOrderApp.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
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

  @Override
  public City findCityById(Long id) {
    return cityRepository.findById(id).orElseThrow();
  }

  @Override
  public Page<City> getPaginatedCities(Pageable pageable) {
    int pageSize = pageable.getPageSize();
    int currentPage = pageable.getPageNumber();
    int startItem = currentPage * pageSize;
    List<City> cities = cityRepository.findAll();
    List<City> list;

    if(cities.size() < startItem)
      list = Collections.emptyList();
    else{
      int toIndex = Math.min(startItem + pageSize, cities.size());
      list = cities.subList(startItem, toIndex);
    }

    Page<City> cityPage = new PageImpl<>(list, PageRequest.of(currentPage,pageSize),cities.size());
    return cityPage;
  }
}
