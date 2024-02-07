package com.example.foodOrderApp.repository;

import com.example.foodOrderApp.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City,Long> {

  City findCityByCityName(String name);

}
