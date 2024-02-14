package com.example.foodOrderApp.conroller;

import com.example.foodOrderApp.entity.Restaurant;
import com.example.foodOrderApp.entity.User;
import com.example.foodOrderApp.repository.UserRepository;
import com.example.foodOrderApp.service.RestaurantService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/restaurant")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/add-list")
    public @ResponseBody ResponseEntity addRestaurants(@RequestBody List<Restaurant> restaurants){
        return new ResponseEntity(restaurantService.addRestaurants(restaurants),HttpStatus.CREATED);
    }

    @GetMapping("/register")
    public String register(){
        return "registerRestaurant";
    }

    @PostMapping("/add")
    public String addRestaurant(HttpServletRequest request){

        Restaurant restaurant = Restaurant.builder()
                .name(request.getParameter("name"))
                .email(request.getParameter("email"))
                .cityName(request.getParameter("city"))
                .areaName(request.getParameter("area"))
                .address(request.getParameter("address"))
                .contactNo(Long.parseLong(request.getParameter("phone")))
                .password(request.getParameter("password"))
                .build();

        User user = User.builder().email(restaurant.getEmail()).password(passwordEncoder.encode(restaurant.getPassword())).role("RESTAURANT").build();
        restaurantService.addRestaurant(restaurant);
        userRepository.save(user);
        return "redirect:/login";
    }

}
