package com.example.foodOrderApp.conroller;

import com.example.foodOrderApp.entity.Area;
import com.example.foodOrderApp.service.AreaService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    List<Area> areaList = areaService.getAreas();
    model.addAttribute("areas",areaList);
    return "areaTable :: areatable";
  }

  @GetMapping("/add-area")
  public String addArea(){
    return "addArea :: areaform";
  }

  @PostMapping("/addarea")
  public String saveArea(HttpServletRequest request, Model model){
    Area area = Area.builder().cityName(request.getParameter("city")).areaName(request.getParameter("area")).description(request.getParameter("description")).build();
    areaService.addArea(area);
    List<Area> areaList = areaService.getAreas();
    model.addAttribute("areas",areaList);
    return "areaTable :: areatable";
  }

  @DeleteMapping("/delete-area/{id}")
  public String deleteArea(@PathVariable(name = "id") String id,Model model){
    System.out.println(id);
    areaService.deleteArea(Long.parseLong(id));
    List<Area> areaList = areaService.getAreas();
    model.addAttribute("areas",areaList);
    return "areaTable :: areatable";
  }
}
