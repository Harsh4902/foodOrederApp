package com.example.foodOrderApp.conroller;

import com.example.foodOrderApp.entity.Complaint;
import com.example.foodOrderApp.entity.Status;
import com.example.foodOrderApp.entity.SubCategory;
import com.example.foodOrderApp.repository.ComplaintRepository;
import com.example.foodOrderApp.service.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    @PostMapping("/add")
    public String addComplaint(@ModelAttribute("complaint") Complaint request) throws IOException {
//        StringBuilder fileNames = new StringBuilder();
//        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
//        fileNames.append(file.getOriginalFilename());
//        Files.write(fileNameAndPath, file.getBytes());
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
        return "addComplaint";
    }

    @GetMapping("/manage")
    public String manageComplaints(Model model){
        model.addAttribute("complaints",complaintService.getAllComplaints());
        return mangeComplaintsPaged(model,1);
    }

    @GetMapping("/page/{page}")
    public String mangeComplaintsPaged(Model model, @PathVariable("page") int page){

        int currentPage = page;
        int pageSize = 5;
        Page<Complaint> complaintPage = complaintService.getPaginatedComplaints(PageRequest.of(currentPage-1,pageSize));
        int totalPages = complaintPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", complaintPage.getTotalElements());
        model.addAttribute("complaints",complaintPage.getContent());
        return "complaintsTable :: complaintstable";
    }

}
