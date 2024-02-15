package com.example.foodOrderApp.conroller;

import com.example.foodOrderApp.entity.Area;
import com.example.foodOrderApp.entity.Complaint;
import com.example.foodOrderApp.entity.Status;
import com.example.foodOrderApp.entity.SubCategory;
import com.example.foodOrderApp.generator.PdfGenerator;
import com.example.foodOrderApp.repository.ComplaintRepository;
import com.example.foodOrderApp.service.ComplaintService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import java.text.DateFormat;
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
//        model.addAttribute("complaints",complaintService.getAllComplaints());
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

    @GetMapping("/reply/{id}/{page}")
    public String getReplyPage(@PathVariable long id,@ModelAttribute("complaint") Complaint request,Model model,@PathVariable int page){
        Complaint complaint = complaintService.getComplaintById(id);
        model.addAttribute("complaint",complaint);
        model.addAttribute("currentPage",page);
        return "replyComplaint :: reply";
    }

    @PostMapping("/replycomplaint/{id}/{page}")
    public String reply(@ModelAttribute("complaint") Complaint reply, @PathVariable long id, @PathVariable int page, Model model){
        System.out.println(id);
        Complaint complaint = complaintService.getComplaintById(id);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        String replytDate = formatter.format(date);
        complaint.setReplyDate(replytDate);
        complaint.setReply(reply.getReply());
        complaint.setStatus(Status.RESOLVED);
        complaintService.addComplaint(complaint);
        return mangeComplaintsPaged(model, page);
    }

    @GetMapping("/pdf")
    public void generatePDf(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
        String currentDateTime = dateFormat.format(new Date());
        String headerkey = "Content-Disposition";
        String headervalue = "attachment; filename=complaint" + currentDateTime + ".pdf";
        response.setHeader(headerkey, headervalue);
        PdfGenerator generator = new PdfGenerator();
        generator.generatePdfForComplaints(complaintService.getAllComplaints(), response);
    }


    private XSSFWorkbook workbook = new XSSFWorkbook();
    private XSSFSheet sheet;
    private void writeHeader() {
        sheet = workbook.createSheet("Areas");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        createCell(row, 0, "ID", style);
        createCell(row, 1, "User Name", style);
        createCell(row, 2, "Subject", style);
        createCell(row, 3, "Complaint Date", style);
        createCell(row, 4, "Reply", style);
        createCell(row, 5, "Reply Date", style);
        createCell(row, 6, "Status", style);
        createCell(row, 7, "Attachment", style);
        createCell(row, 8, "Description", style);
    }
    private void createCell(Row row, int columnCount, Object valueOfCell, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (valueOfCell instanceof Integer) {
            cell.setCellValue((Integer) valueOfCell);
        } else if (valueOfCell instanceof Long) {
            cell.setCellValue((Long) valueOfCell);
        } else if (valueOfCell instanceof String) {
            cell.setCellValue((String) valueOfCell);
        } else {
            cell.setCellValue((Boolean) valueOfCell);
        }
        cell.setCellStyle(style);
    }
    private void write() {
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        for (Complaint complaint : complaintService.getAllComplaints()) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, complaint.getId(), style);
            createCell(row, columnCount++, complaint.getUserName(), style);
            createCell(row, columnCount++, complaint.getSubject(), style);
            createCell(row, columnCount++, complaint.getComplaintDate(), style);
            createCell(row, columnCount++, complaint.getReply(), style);
            createCell(row, columnCount++, complaint.getReplyDate(), style);
            createCell(row, columnCount++, complaint.getStatus().name(), style);
            createCell(row, columnCount++, complaint.getAttachment(), style);
            createCell(row, columnCount++, complaint.getDescription(), style);
        }
    }

    @GetMapping("/excel")
    public void generateExcelFile(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
        String currentDateTime = dateFormat.format(new Date());
        String headerkey = "Content-Disposition";
        String headervalue = "attachment; filename=complaint" + currentDateTime + ".xlsx";
        response.setHeader(headerkey, headervalue);
        writeHeader();
        write();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

}
