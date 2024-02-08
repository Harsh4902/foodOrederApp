package com.example.foodOrderApp.service;

import com.example.foodOrderApp.entity.Complaint;
import com.example.foodOrderApp.repository.ComplaintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComplaintServiceImpl implements ComplaintService{

    @Autowired
    private ComplaintRepository complaintRepository;

    @Override
    public Complaint addComplaint(Complaint complaint) {
        return complaintRepository.save(complaint);
    }

    @Override
    public Complaint getComplaintById(Long id) {
        return complaintRepository.findById(id).orElseThrow();
    }

    @Override
    public List<Complaint> getAllComplaints() {
        return complaintRepository.findAll();
    }
}
