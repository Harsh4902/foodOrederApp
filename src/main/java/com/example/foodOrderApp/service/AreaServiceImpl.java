package com.example.foodOrderApp.service;

import com.example.foodOrderApp.entity.Area;
import com.example.foodOrderApp.entity.City;
import com.example.foodOrderApp.repository.AreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
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

  @Override
  public Page<Area> getPaginatedAreas(Pageable pageable) {
    int pageSize = pageable.getPageSize();
    int currentPage = pageable.getPageNumber();
    int startItem = currentPage * pageSize;
    List<Area> areas = areaRepository.findAll();
    List<Area> list;

    if(areas.size() < startItem)
      list = Collections.emptyList();
    else{
      int toIndex = Math.min(startItem + pageSize, areas.size());
      list = areas.subList(startItem, toIndex);
    }

    Page<Area> areaPage = new PageImpl<>(list, PageRequest.of(currentPage,pageSize),areas.size());
    return areaPage;
  }
}
