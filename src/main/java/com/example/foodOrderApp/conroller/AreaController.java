package com.example.foodOrderApp.conroller;

import com.example.foodOrderApp.entity.Area;
import com.example.foodOrderApp.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/area-rest")
public class AreaController {

  @Autowired
  private AreaService areaService;

  @PostMapping("/add")
  public ResponseEntity addAreas(@RequestBody List<Area> areaList){
    areaService.addAreas(areaList);
    return new ResponseEntity(HttpStatus.OK);
  }

}
