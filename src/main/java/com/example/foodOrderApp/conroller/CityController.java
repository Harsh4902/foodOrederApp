package com.example.foodOrderApp.conroller;

import com.example.foodOrderApp.entity.City;
import com.example.foodOrderApp.service.CityService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
//    List<City> cityList = cityService.getAllCities();
//    model.addAttribute("cities",cityList);
//    return "cityTable :: citytable";
    return mangeCityPaged(model, 1);
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

  @DeleteMapping("/delete-city/{id}/{page}")
  public String deleteCity(@PathVariable(name = "id") String id,Model model,@PathVariable("page") int page){
    System.out.println(id);
    cityService.deleteCityById(Long.parseLong(id));
//    List<City> cityList = cityService.getAllCities();
//    model.addAttribute("cities",cityList);
//    return "cityTable :: citytable";

    return mangeCityPaged(model, page);
  }

  @GetMapping("/update-city/{id}/{page}")
  public String updateForm(@PathVariable(name = "id")String id, Model model,@PathVariable("page") int page){
    City city = cityService.findCityById(Long.parseLong(id));
    System.out.println(city);
    model.addAttribute("city",city);
    model.addAttribute("currentPage",page);
    return "addCity :: updatecity";
  }

  @PatchMapping("/update/{id}/{page}")
  public String updateCity(@ModelAttribute("city") City city,Model model,@PathVariable("page") int page){
    City temp = cityService.findCityById(city.getId());
    temp.setCityName(city.getCityName());
    temp.setDescription(city.getDescription());
    cityService.addCity(temp);
//    List<City> cityList = cityService.getAllCities();
//    model.addAttribute("cities",cityList);
//    return "cityTable :: citytable";
    return mangeCityPaged(model, page);
  }

  @GetMapping("/page/{page}")
  public String mangeCityPaged(Model model, @PathVariable("page") int page){
    int currentPage = page;
    int pageSize = 5;
    Page<City> cityPage = cityService.getPaginatedCities(PageRequest.of(currentPage-1,pageSize));
    int totalPages = cityPage.getTotalPages();
    if (totalPages > 0) {
      List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
        .boxed()
        .collect(Collectors.toList());
      model.addAttribute("pageNumbers", pageNumbers);
    }
    model.addAttribute("currentPage", currentPage);
    model.addAttribute("totalPages", totalPages);
    model.addAttribute("totalItems", cityPage.getTotalElements());
    model.addAttribute("cities",cityPage.getContent());
    return "cityTable :: citytable";
  }
}
