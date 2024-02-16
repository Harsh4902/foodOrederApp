package com.example.foodOrderApp.conroller;

import com.example.foodOrderApp.entity.Area;
import com.example.foodOrderApp.entity.Offer;
import com.example.foodOrderApp.generator.PdfGenerator;
import com.example.foodOrderApp.service.OfferService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
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

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/offers")
public class OfferController {

    @Autowired
    private OfferService offerService;

    private XSSFWorkbook workbook = new XSSFWorkbook();
    private XSSFSheet sheet;

    @GetMapping
    public String mangeOffers(Model model){
        return mangeOfferPaged(model, 1);
    }

    @GetMapping("/add-offer/{page}")
    public String addCity(Model model,@PathVariable("page") int page){
        model.addAttribute("currentPage",page);
        return "addOffer :: offerform";
    }

    @PostMapping("/addoffer/{page}")
    public String saveOffer(HttpServletRequest request,Model model,@PathVariable("page") int page) throws ParseException {
        Offer offer = Offer.builder()
                .offerName(request.getParameter("name"))
                .categoryName(request.getParameter("category"))
                .subCategoryName(request.getParameter("subcategory"))
                .discount(Integer.parseInt(request.getParameter("discount")))
                .description(request.getParameter("description"))
                .startDate(request.getParameter("start"))
                .endDate(request.getParameter("end"))
                .build();

        offerService.addOffer(offer);
//        model.addAttribute("offers",offerService.getAllOffers());
//        return "offerTable :: offertable";
        return mangeOfferPaged(model, page);
    }

    @DeleteMapping("/delete-offer/{id}/{page}")
    public String deleteOffer(@PathVariable("id") String id,Model model,@PathVariable("page") int page){
        offerService.deletOfferById(Long.parseLong(id));
        return mangeOfferPaged(model, page);
    }

    @GetMapping("/update-offer/{id}/{page}")
    public String updateForm(@PathVariable("id") String id, Model model,@PathVariable("page") int page){
        Offer offer = offerService.gerOfferById(Long.parseLong(id));
        model.addAttribute("offer",offer);
        model.addAttribute("currentPage",page);
        return "addOffer :: updateoffer";
    }

    @PatchMapping("/update/{id}/{page}")
    public String updateOffer(@ModelAttribute("offer") Offer offer, Model model,HttpServletRequest request,@PathVariable("page") int page){
        System.out.println(request.getParameter("category"));
        System.out.println(request.getParameter("subcategory"));
        Offer temp = offerService.gerOfferById(offer.getId());
        temp.setCategoryName(request.getParameter("category"));
        temp.setSubCategoryName(request.getParameter("subcategory"));
        temp.setOfferName(offer.getOfferName());
        temp.setDiscount(offer.getDiscount());
        temp.setDescription(offer.getDescription());
        temp.setStartDate(offer.getStartDate());
        temp.setEndDate(offer.getEndDate());
        System.out.println(temp);
        offerService.addOffer(temp);
        return mangeOfferPaged(model,page);
    }

    @GetMapping("/page/{page}")
    public String mangeOfferPaged(Model model, @PathVariable("page") int page){

        int currentPage = page;
        int pageSize = 5;
        Page<Offer> offerPage = offerService.getPaginatedOffers(PageRequest.of(currentPage-1,pageSize));
        int totalPages = offerPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", offerPage.getTotalElements());
        model.addAttribute("offers",offerPage.getContent());
        return "offerTable :: offertable";
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
        generator.generatePdfForOffers(offerService.getAllOffers(), response);
    }

    private void writeHeader() {
        String dateTime=new Date().toString().replaceAll(":", "_");
        sheet = workbook.createSheet("Offers"+dateTime);
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        int columnCount = 0;
        createCell(row, columnCount++, "ID",style);
        createCell(row, columnCount++, "Offer Name",style);
        createCell(row, columnCount++, "Category Name",style);
        createCell(row, columnCount++, "Subcategory Name",style);
        createCell(row, columnCount++, "Restaurant Name",style);
        createCell(row, columnCount++, "Discount",style);
        createCell(row, columnCount++, "Start Date", style);
        createCell(row, columnCount++, "End Date",style);
        createCell(row, columnCount++, "Description",style);
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
        for (Offer offer : offerService.getAllOffers()) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, offer.getId(),style);
            createCell(row, columnCount++, offer.getOfferName(),style);
            createCell(row, columnCount++, offer.getOfferName(),style);
            createCell(row, columnCount++, offer.getSubCategoryName(),style);
            createCell(row, columnCount++, offer.getRestaurantName(),style);
            createCell(row, columnCount++, offer.getDiscount(),style);
            createCell(row, columnCount++, offer.getStartDate(), style);
            createCell(row, columnCount++, offer.getEndDate(),style);
            createCell(row, columnCount++, offer.getDescription(),style);
        }
    }

    @GetMapping("/excel")
    public void generateExcelFile(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
        String currentDateTime = dateFormat.format(new Date());
        String headerkey = "Content-Disposition";
        String headervalue = "attachment; filename=offer" + currentDateTime + ".xlsx";
        response.setHeader(headerkey, headervalue);
        writeHeader();
        write();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.close();
    }

}
