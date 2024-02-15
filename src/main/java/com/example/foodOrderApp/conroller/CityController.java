package com.example.foodOrderApp.conroller;

import com.example.foodOrderApp.entity.City;
import com.example.foodOrderApp.generator.PdfGenerator;
import com.example.foodOrderApp.service.CityService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/city")
public class CityController {

  @Autowired
  private CityService cityService;

  @PostMapping("/add-list")
  public ResponseEntity addCities(@RequestBody List<City> cities){
    cityService.addCities(cities);
    return new ResponseEntity(HttpStatus.CREATED);
  }

  @GetMapping
  public String mangeCity(Model model){
//    List<City> cityList = cityService.getAllCities();
//    model.addAttribute("cities",cityList);
//    return "cityTable :: citytable";
    return mangeCityPaged(model, 1);
  }

  @GetMapping("/add-city/{page}")
  public String addCity(Model model,@PathVariable("page") int page){
    model.addAttribute("currentPage",page);
    return "addCity :: cityform";
  }

  @PostMapping("/addcity/{page}")
  public String saveCity(HttpServletRequest request, Model model,@PathVariable int page){
    City city = City.builder().cityName(request.getParameter("city")).description(request.getParameter("description")).build();
    cityService.addCity(city);
//    List<City> cityList = cityService.getAllCities();
//    model.addAttribute("cities",cityList);
//    return "cityTable :: citytable";

    return mangeCityPaged(model, page);
  }

  @DeleteMapping("/delete-city/{id}/{page}")
  public String deleteCity(@PathVariable(name = "id") String id,Model model,@PathVariable("page") int page){
    System.out.println(id);
    cityService.deleteCityById(Long.parseLong(id));
//    List<City> cityList = cityService.getAllCities();
//    model.addAttribute("cities",cityList);
//    return "cityTable :: citytable";

    return mangeCityPaged(model, page);
  }

  @GetMapping("/update-city/{id}/{page}")
  public String updateForm(@PathVariable(name = "id")String id, Model model,@PathVariable("page") int page){
    City city = cityService.findCityById(Long.parseLong(id));
    System.out.println(city);
    model.addAttribute("city",city);
    model.addAttribute("currentPage",page);
    return "addCity :: updatecity";
  }

  @PatchMapping("/update/{id}/{page}")
  public String updateCity(@ModelAttribute("city") City city,Model model,@PathVariable("page") int page){
    City temp = cityService.findCityById(city.getId());
    temp.setCityName(city.getCityName());
    temp.setDescription(city.getDescription());
    cityService.addCity(temp);
//    List<City> cityList = cityService.getAllCities();
//    model.addAttribute("cities",cityList);
//    return "cityTable :: citytable";
    return mangeCityPaged(model, page);
  }

  @GetMapping("/page/{page}")
  public String mangeCityPaged(Model model, @PathVariable("page") int page){
    int currentPage = page;
    int pageSize = 5;
    Page<City> cityPage = cityService.getPaginatedCities(PageRequest.of(currentPage-1,pageSize));
    int totalPages = cityPage.getTotalPages();
    if (totalPages > 0) {
      List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
        .boxed()
        .collect(Collectors.toList());
      model.addAttribute("pageNumbers", pageNumbers);
    }
    model.addAttribute("currentPage", currentPage);
    model.addAttribute("totalPages", totalPages);
    model.addAttribute("totalItems", cityPage.getTotalElements());
    model.addAttribute("cities",cityPage.getContent());
    return "cityTable :: citytable";
  }

  @GetMapping("/pdf")
  public void generatePDf(HttpServletResponse response) throws IOException {
    response.setContentType("application/pdf");
    DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
    String currentDateTime = dateFormat.format(new Date());
    String headerkey = "Content-Disposition";
    String headervalue = "attachment; filename=city" + currentDateTime + ".pdf";
    response.setHeader(headerkey, headervalue);
    PdfGenerator generator = new PdfGenerator();
    generator.generatePdfForCity(cityService.getAllCities(), response);
  }

  private XSSFWorkbook workbook = new XSSFWorkbook();
  private XSSFSheet sheet;
  private void writeHeader() {
    sheet = workbook.createSheet("Cities");
    Row row = sheet.createRow(0);
    CellStyle style = workbook.createCellStyle();
    XSSFFont font = workbook.createFont();
    font.setBold(true);
    font.setFontHeight(16);
    style.setFont(font);
    createCell(row, 0, "ID", style);
    createCell(row, 1, "City Name", style);
    createCell(row, 2, "Description", style);
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
    for (City city : cityService.getAllCities()) {
      Row row = sheet.createRow(rowCount++);
      int columnCount = 0;
      createCell(row, columnCount++, city.getId(), style);
      createCell(row, columnCount++, city.getCityName(), style);
      createCell(row, columnCount++, city.getDescription(), style);
    }
  }

  @GetMapping("/excel")
  public void generateExcelFile(HttpServletResponse response) throws IOException {
    response.setContentType("application/octet-stream");
    DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
    String currentDateTime = dateFormat.format(new Date());
    String headerkey = "Content-Disposition";
    String headervalue = "attachment; filename=city" + currentDateTime + ".xlsx";
    response.setHeader(headerkey, headervalue);
    writeHeader();
    write();
    ServletOutputStream outputStream = response.getOutputStream();
    workbook.write(outputStream);
    workbook.close();
    outputStream.close();
  }
}
