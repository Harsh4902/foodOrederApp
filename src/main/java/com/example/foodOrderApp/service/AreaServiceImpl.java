package com.example.foodOrderApp.service;

import com.example.foodOrderApp.entity.Area;
import com.example.foodOrderApp.repository.AreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AreaServiceImpl implements AreaService{

  @Autowired
  private AreaRepository areaRepository;
  @Override
  public void addAreas(List<Area> areaList) {
    areaRepository.saveAll(areaList);
  }

  @Override
  public List<Area> getAreas() {
    return areaRepository.findAll();
  }

  @Override
  public Area addArea(Area area) {
    return areaRepository.save(area);
  }

  @Override
  public void deleteArea(Long id) {
    areaRepository.deleteById(id);
  }

  @Override
  public Area findAreaById(Long id) {
    return areaRepository.findById(id).orElseThrow();
  }
}
