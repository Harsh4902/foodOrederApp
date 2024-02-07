package com.example.foodOrderApp.conroller;

import com.example.foodOrderApp.entity.City;
import com.example.foodOrderApp.service.CityService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/city")
public class CityController {

  @Autowired
  private CityService cityService;

  @PostMapping("/add-list")
  public ResponseEntity addCities(@RequestBody List<City> cities){
    cityService.addCities(cities);
    return new ResponseEntity(HttpStatus.CREATED);
  }

  @GetMapping
  public String mangeCity(Model model){
    List<City> cityList = cityService.getAllCities();
    model.addAttribute("cities",cityList);
    return "cityTable :: citytable";
  }

  @GetMapping("/add-city")
  public String addCity(){
    return "addCity :: cityform";
  }

  @PostMapping("/addcity")
  public String saveCity(HttpServletRequest request, Model model){
    City city = City.builder().cityName(request.getParameter("city")).description(request.getParameter("description")).build();
    cityService.addCity(city);
    List<City> cityList = cityService.getAllCities();
    model.addAttribute("cities",cityList);
    return "cityTable :: citytable";
  }

  @DeleteMapping("/delete-city/{id}")
  public String deleteCity(@PathVariable(name = "id") String id,Model model){
    System.out.println(id);
    cityService.deleteCityById(Long.parseLong(id));
    List<City> cityList = cityService.getAllCities();
    model.addAttribute("cities",cityList);
    return "cityTable :: citytable";
  }

}
