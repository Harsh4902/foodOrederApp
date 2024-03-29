package com.example.foodOrderApp.service;

import com.example.foodOrderApp.entity.Area;
import com.example.foodOrderApp.entity.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AreaService {

  void addAreas(List<Area> areaList);
  List<Area> getAreas();

  Area addArea(Area area);

  void deleteArea(Long id);

  Area findAreaById(Long id);

  Page<Area> getPaginatedAreas(Pageable pageable);
}
