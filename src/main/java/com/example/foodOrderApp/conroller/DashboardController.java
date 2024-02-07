package com.example.foodOrderApp.conroller;

import com.example.foodOrderApp.entity.Area;
import com.example.foodOrderApp.entity.City;
import com.example.foodOrderApp.service.AreaService;
import com.example.foodOrderApp.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private CityService cityService;

    @Autowired
    private AreaService areaService;
    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    @GetMapping("/index")
    public String index(){
        return "index";
    }

    @GetMapping("/city")
    public String mangeCity(Model model){
        List<City> cityList = cityService.getAllCities();
        model.addAttribute("cities",cityList);
        return "cityTable :: citytable";
    }

    @GetMapping("/area")
    public String mageArea(Model model){
        List<Area> areaList = areaService.getAreas();
        model.addAttribute("areas",areaList);
        return "areaTable :: areatable";
    }

    @GetMapping("/add-area")
    public String addArea(){
        return "addArea :: areaform";
    }

    @GetMapping("/add-city")
    public String addCity(){
        return "addCity :: cityform";
    }
}
