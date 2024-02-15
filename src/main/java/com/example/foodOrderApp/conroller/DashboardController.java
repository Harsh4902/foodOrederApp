package com.example.foodOrderApp.conroller;

import com.example.foodOrderApp.entity.*;
import com.example.foodOrderApp.repository.UserRepository;
import com.example.foodOrderApp.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class DashboardController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(){
        return "index";
    }

    @GetMapping("/register")
    public String registerPage(){
        return "registerRestaurant";
    }

    @GetMapping("/restaurants")
    public String manageRestaurants(Model model){
//        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
//        model.addAttribute("restaurants",restaurants);
//        return "restaurantTable :: restauranttable";
        return managePagedRestaurant(model,1);
    }

    @GetMapping("/page/{page}")
    public String managePagedRestaurant(Model model, @PathVariable("page") int page){

        int currentPage = page;
        int pageSize = 5;
        Page<Restaurant> restaurantPage = restaurantService.getPaginatedRestaurants(PageRequest.of(currentPage-1,pageSize));
        int totalPages = restaurantPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", restaurantPage.getTotalElements());
        model.addAttribute("restaurants",restaurantPage.getContent());
        return "restaurantTable :: restauranttable";
    }

    @GetMapping("/logout")
    public String logout(){
        return "login";
    }

    @PostMapping("register")
    @ResponseBody
    public ResponseEntity<HttpStatus> registerAdmin(@RequestBody User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
