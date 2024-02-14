package com.example.foodOrderApp.conroller;

import com.example.foodOrderApp.entity.Area;
import com.example.foodOrderApp.entity.City;
import com.example.foodOrderApp.service.AreaService;
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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/area")
public class AreaController {

  @Autowired
  private AreaService areaService;

  @PostMapping("/add")
  public ResponseEntity addAreas(@RequestBody List<Area> areaList){
    areaService.addAreas(areaList);
    return new ResponseEntity(HttpStatus.OK);
  }

  @GetMapping()
  public String mageArea(Model model){
//    List<Area> areaList = areaService.getAreas();
//    model.addAttribute("areas",areaList);
//    return "areaTable :: areatable";

    return mangeAreaPaged(model,1);
  }

  @GetMapping("/add-area/{page}")
  public String addArea(Model model,@PathVariable("page") int page){
    model.addAttribute("currentPage",page);
    return "addArea :: areaform";
  }

  @PostMapping("/addarea/{page}")
  public String saveArea(HttpServletRequest request, Model model,@PathVariable("page") int page){
    Area area = Area.builder().cityName(request.getParameter("city")).areaName(request.getParameter("area")).description(request.getParameter("description")).build();
    areaService.addArea(area);
//    List<Area> areaList = areaService.getAreas();
//    model.addAttribute("areas",areaList);
//    return "areaTable :: areatable";
    return mangeAreaPaged(model,page);
  }

  @DeleteMapping("/delete-area/{id}/{page}")
  public String deleteArea(@PathVariable(name = "id") String id,Model model,@PathVariable("page") int page){
    System.out.println(id);
    areaService.deleteArea(Long.parseLong(id));
//    List<Area> areaList = areaService.getAreas();
//    model.addAttribute("areas",areaList);
//    return "areaTable :: areatable";
    return mangeAreaPaged(model,page);
  }

  @GetMapping("/update-area/{id}/{page}")
  public String updateForm(@PathVariable(name = "id")String id, Model model,@PathVariable("page") int page){
    Area area = areaService.findAreaById(Long.parseLong(id));
    System.out.println(area);
    model.addAttribute("area",area);
    model.addAttribute("currentPage",page);
    return "addArea :: updatearea";
  }

  @PatchMapping("/update/{id}/{page}")
  public String updateCity(@ModelAttribute("area") Area area,Model model,@PathVariable("page") int page){
    System.out.println(area);
    Area temp = areaService.findAreaById(area.getId());
    temp.setAreaName(area.getAreaName());
    temp.setDescription(area.getDescription());
    areaService.addArea(temp);
//    List<Area> areaList = areaService.getAreas();
//    model.addAttribute("areas",areaList);
//    return "areaTable :: areatable";
    return mangeAreaPaged(model,page);
  }

  @GetMapping("/page/{page}")
  public String mangeAreaPaged(Model model, @PathVariable("page") int page){

    int currentPage = page;
    int pageSize = 5;
    Page<Area> areaPage = areaService.getPaginatedAreas(PageRequest.of(currentPage-1,pageSize));
    int totalPages = areaPage.getTotalPages();
    if (totalPages > 0) {
      List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
        .boxed()
        .collect(Collectors.toList());
      model.addAttribute("pageNumbers", pageNumbers);
    }
    model.addAttribute("currentPage", currentPage);
    model.addAttribute("totalPages", totalPages);
    model.addAttribute("totalItems", areaPage.getTotalElements());
    model.addAttribute("areas",areaPage.getContent());
    return "areaTable :: areatable";
  }
}
