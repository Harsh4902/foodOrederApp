package com.example.foodOrderApp.conroller;

import com.example.foodOrderApp.entity.*;
import com.example.foodOrderApp.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    @GetMapping("/index")
    public String index(){
        return "index";
    }

    @GetMapping("/restaurants")
    public String manageRestaurants(Model model){
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        model.addAttribute("restaurants",restaurants);
        return "restaurantTable :: restauranttable";
    }
}
