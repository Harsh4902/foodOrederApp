package com.example.foodOrderApp.conroller;

import com.example.foodOrderApp.entity.Offer;
import com.example.foodOrderApp.service.OfferService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/offers")
public class OfferController {

    @Autowired
    private OfferService offerService;

    @GetMapping
    public String mangeOffers(Model model){
        return mangeOfferPaged(model, 1);
    }

    @GetMapping("/add-offer/{page}")
    public String addCity(Model model,@PathVariable("page") int page){
        model.addAttribute("currentPage",page);
        return "addOffer :: offerform";
    }

    @PostMapping("/addoffer/{page}")
    public String saveOffer(HttpServletRequest request,Model model,@PathVariable("page") int page) throws ParseException {
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
//        model.addAttribute("offers",offerService.getAllOffers());
//        return "offerTable :: offertable";
        return mangeOfferPaged(model, page);
    }

    @DeleteMapping("/delete-offer/{id}/{page}")
    public String deleteOffer(@PathVariable("id") String id,Model model,@PathVariable("page") int page){
        offerService.deletOfferById(Long.parseLong(id));
        return mangeOfferPaged(model, page);
    }

    @GetMapping("/update-offer/{id}/{page}")
    public String updateForm(@PathVariable("id") String id, Model model,@PathVariable("page") int page){
        Offer offer = offerService.gerOfferById(Long.parseLong(id));
        model.addAttribute("offer",offer);
        model.addAttribute("currentPage",page);
        return "addOffer :: updateoffer";
    }

    @PatchMapping("/update/{id}/{page}")
    public String updateOffer(@ModelAttribute("offer") Offer offer, Model model,HttpServletRequest request,@PathVariable("page") int page){
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
        return mangeOfferPaged(model,page);
    }

    @GetMapping("/page/{page}")
    public String mangeOfferPaged(Model model, @PathVariable("page") int page){

        int currentPage = page;
        int pageSize = 5;
        Page<Offer> offerPage = offerService.getPaginatedOffers(PageRequest.of(currentPage-1,pageSize));
        int totalPages = offerPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", offerPage.getTotalElements());
        model.addAttribute("offers",offerPage.getContent());
        return "offerTable :: offertable";
    }

}
