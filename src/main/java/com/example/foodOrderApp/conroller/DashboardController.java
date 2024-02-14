package com.example.foodOrderApp.conroller;

import com.example.foodOrderApp.entity.*;
import com.example.foodOrderApp.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class DashboardController {

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    @PostMapping("/authenticate")
    public String authenticate(HttpServletRequest request) {
        if (request.getParameter("email").equals("admin123@gmail.com") && request.getParameter("password").equals("Admin@123"))
            return "redirect:/dashboard";
        else {
            HttpSession session = request.getSession();
            session.setAttribute("restaurantname","RK");
            return "restaurantDashboard";
        }
    }

    @GetMapping()
    public String index(){
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
}
