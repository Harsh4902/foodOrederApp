package com.example.foodOrderApp.service;

import com.example.foodOrderApp.entity.Complaint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ComplaintService {

    Complaint addComplaint(Complaint complaint);

    Complaint getComplaintById(Long id);

    List<Complaint> getAllComplaints();

    Page<Complaint> getPaginatedComplaints(Pageable pageable);

}
