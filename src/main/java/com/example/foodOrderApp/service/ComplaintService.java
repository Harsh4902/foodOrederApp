package com.example.foodOrderApp.service;

import com.example.foodOrderApp.entity.Complaint;

import java.util.List;

public interface ComplaintService {

    Complaint addComplaint(Complaint complaint);

    Complaint getComplaintById(Long id);

    List<Complaint> getAllComplaints();

}
