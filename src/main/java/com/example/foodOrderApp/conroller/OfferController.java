package com.example.foodOrderApp.conroller;

import com.example.foodOrderApp.entity.Offer;
import com.example.foodOrderApp.service.OfferService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/offers")
public class OfferController {

    @Autowired
    private OfferService offerService;

    @GetMapping
    public String mangeOffers(Model model){
        model.addAttribute("offers",offerService.getAllOffers());
        return "offerTable :: offertable";
    }

    @GetMapping("/add-offer")
    public String addCity(){
        return "addOffer :: offerform";
    }

    @PostMapping("/addoffer")
    public String saveOffer(HttpServletRequest request,Model model) throws ParseException {
        Offer offer = Offer.builder()
                .offerName(request.getParameter("name"))
                .categoryName(request.getParameter("category"))
                .subCategoryName(request.getParameter("subcategory"))
                .discount(Integer.parseInt(request.getParameter("discount")))
                .description(request.getParameter("description"))
                .startDate(request.getParameter("start"))
                .endDate(request.getParameter("end"))
                .build();

        offerService.addOffer(offer);
        model.addAttribute("offers",offerService.getAllOffers());
        return "offerTable :: offertable";
    }

    @DeleteMapping("/delete-offer/{id}")
    public String deleteOffer(@PathVariable("id") String id,Model model){
        offerService.deletOfferById(Long.parseLong(id));
        model.addAttribute("offers",offerService.getAllOffers());
        return "offerTable :: offertable";
    }

    @GetMapping("/update-offer/{id}")
    public String updateForm(@PathVariable("id") String id, Model model){
        Offer offer = offerService.gerOfferById(Long.parseLong(id));
        model.addAttribute("offer",offer);
        return "addOffer :: updateoffer";
    }

    @PatchMapping("/update/{id}")
    public String updateOffer(@ModelAttribute("offer") Offer offer, Model model,HttpServletRequest request){
        System.out.println(request.getParameter("category"));
        System.out.println(request.getParameter("subcategory"));
        Offer temp = offerService.gerOfferById(offer.getId());
        temp.setCategoryName(request.getParameter("category"));
        temp.setSubCategoryName(request.getParameter("subcategory"));
        temp.setOfferName(offer.getOfferName());
        temp.setDiscount(offer.getDiscount());
        temp.setDescription(offer.getDescription());
        temp.setStartDate(offer.getStartDate());
        temp.setEndDate(offer.getEndDate());
        System.out.println(temp);
        offerService.addOffer(temp);
        model.addAttribute("offers",offerService.getAllOffers());
        return "offerTable :: offertable";
    }

}
