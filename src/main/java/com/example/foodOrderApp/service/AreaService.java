package com.example.foodOrderApp.service;

import com.example.foodOrderApp.entity.Area;

import java.util.List;

public interface AreaService {

  void addAreas(List<Area> areaList);
  List<Area> getAreas();

  Area addArea(Area area);

  void deleteArea(Long id);

}
