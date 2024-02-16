package com.example.foodOrderApp.conroller;

import com.example.foodOrderApp.entity.Area;
import com.example.foodOrderApp.entity.Restaurant;
import com.example.foodOrderApp.entity.User;
import com.example.foodOrderApp.generator.PdfGenerator;
import com.example.foodOrderApp.repository.UserRepository;
import com.example.foodOrderApp.service.RestaurantService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/restaurant")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private XSSFWorkbook workbook = new XSSFWorkbook();
    private XSSFSheet sheet;

    @PostMapping("/add-list")
    public @ResponseBody ResponseEntity addRestaurants(@RequestBody List<Restaurant> restaurants){
        return new ResponseEntity(restaurantService.addRestaurants(restaurants),HttpStatus.CREATED);
    }

    @GetMapping("/register")
    public String register(){
        return "registerRestaurant";
    }

    @PostMapping("/add")
    public String addRestaurant(HttpServletRequest request){

        Restaurant restaurant = Restaurant.builder()
                .name(request.getParameter("name"))
                .email(request.getParameter("email"))
                .cityName(request.getParameter("city"))
                .areaName(request.getParameter("area"))
                .address(request.getParameter("address"))
                .contactNo(Long.parseLong(request.getParameter("phone")))
                .password(request.getParameter("password"))
                .build();

        User user = User.builder().email(restaurant.getEmail()).password(passwordEncoder.encode(restaurant.getPassword())).role("RESTAURANT").build();
        restaurantService.addRestaurant(restaurant);
        userRepository.save(user);
        return "redirect:/login";
    }

    @GetMapping("/pdf")
    public void generatePDf(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
        String currentDateTime = dateFormat.format(new Date());
        String headerkey = "Content-Disposition";
        String headervalue = "attachment; filename=restaurant" + currentDateTime + ".pdf";
        response.setHeader(headerkey, headervalue);
        PdfGenerator generator = new PdfGenerator();
        generator.generatePdfForRestaurant(restaurantService.getAllRestaurants(), response);
    }

    private void writeHeader() {

        String dateTime=new Date().toString().replaceAll(":", "_");
        sheet = workbook.createSheet("Restaurants"+dateTime);
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        createCell(row, 0, "ID", style);
        createCell(row, 1, "Restaurant Name", style);
        createCell(row, 2, "City Name", style);
        createCell(row, 3, "Area Name", style);
        createCell(row, 4, "Email", style);
        createCell(row, 5, "Contact No.", style);
        createCell(row, 6, "Adress", style);
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
        for (Restaurant restaurant : restaurantService.getAllRestaurants()) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, restaurant.getId(), style);
            createCell(row, columnCount++, restaurant.getName(), style);
            createCell(row, columnCount++, restaurant.getCityName(), style);
            createCell(row, columnCount++, restaurant.getAreaName(), style);
            createCell(row, columnCount++, restaurant.getEmail(), style);
            createCell(row, columnCount++, restaurant.getContactNo(), style);
            createCell(row, columnCount++, restaurant.getAddress(), style);

        }
    }

    @GetMapping("/excel")
    public void generateExcelFile(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
        String currentDateTime = dateFormat.format(new Date());
        String headerkey = "Content-Disposition";
        String headervalue = "attachment; filename=restaurant" + currentDateTime + ".xlsx";
        response.setHeader(headerkey, headervalue);
        writeHeader();
        write();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.close();
    }
}
