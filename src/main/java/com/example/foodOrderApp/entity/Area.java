package com.example.foodOrderApp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Area {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String areaName;

  private String cityName;
  private String description;

  @Override
  public String toString() {
    return "Area{" +
            "id=" + id +
            ", areaName='" + areaName + '\'' +
            ", cityName='" + cityName + '\'' +
            ", description='" + description + '\'' +
            '}';
  }
}
