package com.example.foodOrderApp.service;

import com.example.foodOrderApp.entity.Complaint;
import com.example.foodOrderApp.repository.ComplaintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
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

    @Override
    public Page<Complaint> getPaginatedComplaints(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Complaint> complaints = complaintRepository.findAll();
        List<Complaint> list;

        if(complaints.size() < startItem)
            list = Collections.emptyList();
        else{
            int toIndex = Math.min(startItem + pageSize, complaints.size());
            list = complaints.subList(startItem, toIndex);
        }

        Page<Complaint> complaintPage = new PageImpl<>(list, PageRequest.of(currentPage,pageSize),complaints.size());
        return complaintPage;
    }
}
