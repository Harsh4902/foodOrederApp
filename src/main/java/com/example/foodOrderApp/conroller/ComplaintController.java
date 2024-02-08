package com.example.foodOrderApp.conroller;

import com.example.foodOrderApp.entity.Complaint;
import com.example.foodOrderApp.entity.Status;
import com.example.foodOrderApp.repository.ComplaintRepository;
import com.example.foodOrderApp.service.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/complaints")
public class ComplaintController {

    private static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads/complaints";

    @Autowired
    private ComplaintService complaintService;

    @GetMapping
    public String complaintPage(Model model){
        model.addAttribute("complaint",new Complaint());
        return "addComplaint";
    }

    @PostMapping
    public void addComplaint(@ModelAttribute("complaint") Complaint request, @RequestParam("attachment")MultipartFile file) throws IOException {
        StringBuilder fileNames = new StringBuilder();
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
        fileNames.append(file.getOriginalFilename());
        Files.write(fileNameAndPath, file.getBytes());
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        String complaintDate = formatter.format(date);
        Complaint complaint = Complaint.builder()
                .userName(request.getUserName())
                .subject(request.getSubject())
                .description(request.getDescription())
                .complaintDate(complaintDate)
                .status(Status.PENDING)
                .build();
        complaintService.addComplaint(complaint);
    }

    @GetMapping("/manage")
    public String manageComplaints(Model model){
        model.addAttribute("complaints",complaintService.getAllComplaints());
        return "complaintsTable :: complaintstable";
    }

}
