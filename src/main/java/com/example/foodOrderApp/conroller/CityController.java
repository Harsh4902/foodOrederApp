package com.example.foodOrderApp.conroller;

import com.example.foodOrderApp.entity.City;
import com.example.foodOrderApp.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/city-rest")
public class CityController {

  @Autowired
  private CityService cityService;

  @PostMapping("/add-list")
  public ResponseEntity addCities(@RequestBody List<City> cities){
    cityService.addCities(cities);
    return new ResponseEntity(HttpStatus.CREATED);
  }


}
