package com.example.foodOrderApp.repository;

import com.example.foodOrderApp.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplaintRepository extends JpaRepository<Complaint,Long> {
}
