package com.example.foodOrderApp.conroller;

import com.example.foodOrderApp.entity.Area;
import com.example.foodOrderApp.generator.PdfGenerator;
import com.example.foodOrderApp.service.AreaService;
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
@RequestMapping("/area")
public class AreaController {

  @Autowired
  private AreaService areaService;

  @Autowired
  private CityService cityService;

  private XSSFWorkbook workbook = new XSSFWorkbook();
  private XSSFSheet sheet;

  @PostMapping("/add")
  public ResponseEntity addAreas(@RequestBody List<Area> areaList){
    areaService.addAreas(areaList);
    return new ResponseEntity(HttpStatus.OK);
  }

  @GetMapping()
  public String mageArea(Model model){
//    List<Area> areaList = areaService.getAreas();
//    model.addAttribute("areas",areaList);
//    return "areaTable :: areatable";

    return mangeAreaPaged(model,1);
  }

  @GetMapping("/add-area/{page}")
  public String addArea(Model model,@PathVariable("page") int page){
    model.addAttribute("currentPage",page);
    model.addAttribute("cities",cityService.getAllCities());
    return "addArea :: areaform";
  }

  @PostMapping("/addarea/{page}")
  public String saveArea(HttpServletRequest request, Model model,@PathVariable("page") int page){
    Area area = Area.builder().cityName(request.getParameter("city")).areaName(request.getParameter("area")).description(request.getParameter("description")).build();
    areaService.addArea(area);
//    List<Area> areaList = areaService.getAreas();
//    model.addAttribute("areas",areaList);
//    return "areaTable :: areatable";
    return mangeAreaPaged(model,page);
  }

  @DeleteMapping("/delete-area/{id}/{page}")
  public String deleteArea(@PathVariable(name = "id") String id,Model model,@PathVariable("page") int page){
    System.out.println(id);
    areaService.deleteArea(Long.parseLong(id));
//    List<Area> areaList = areaService.getAreas();
//    model.addAttribute("areas",areaList);
//    return "areaTable :: areatable";
    return mangeAreaPaged(model,page);
  }

  @GetMapping("/update-area/{id}/{page}")
  public String updateForm(@PathVariable(name = "id")String id, Model model,@PathVariable("page") int page){
    Area area = areaService.findAreaById(Long.parseLong(id));
    System.out.println(area);
    model.addAttribute("area",area);
    model.addAttribute("currentPage",page);
    return "addArea :: updatearea";
  }

  @PatchMapping("/update/{id}/{page}")
  public String updateCity(@ModelAttribute("area") Area area,Model model,@PathVariable("page") int page){
    System.out.println(area);
    Area temp = areaService.findAreaById(area.getId());
    temp.setAreaName(area.getAreaName());
    temp.setDescription(area.getDescription());
    areaService.addArea(temp);
//    List<Area> areaList = areaService.getAreas();
//    model.addAttribute("areas",areaList);
//    return "areaTable :: areatable";
    return mangeAreaPaged(model,page);
  }

  @GetMapping("/page/{page}")
  public String mangeAreaPaged(Model model, @PathVariable("page") int page){

    int currentPage = page;
    int pageSize = 5;
    Page<Area> areaPage = areaService.getPaginatedAreas(PageRequest.of(currentPage-1,pageSize));
    int totalPages = areaPage.getTotalPages();
    if (totalPages > 0) {
      List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
        .boxed()
        .collect(Collectors.toList());
      model.addAttribute("pageNumbers", pageNumbers);
    }
    model.addAttribute("currentPage", currentPage);
    model.addAttribute("totalPages", totalPages);
    model.addAttribute("totalItems", areaPage.getTotalElements());
    model.addAttribute("areas",areaPage.getContent());
    return "areaTable :: areatable";
  }

  @GetMapping("/pdf")
  public void generatePDf(HttpServletResponse response) throws IOException {
    response.setContentType("application/pdf");
    DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
    String currentDateTime = dateFormat.format(new Date());
    String headerkey = "Content-Disposition";
    String headervalue = "attachment; filename=area" + currentDateTime + ".pdf";
    response.setHeader(headerkey, headervalue);
    PdfGenerator generator = new PdfGenerator();
    generator.generatePdfForArea(areaService.getAreas(), response);
  }

  private void writeHeader() {
    String dateTime=new Date().toString().replaceAll(":", "_");
    sheet = workbook.createSheet("Areas"+dateTime);
    Row row = sheet.createRow(0);
    CellStyle style = workbook.createCellStyle();
    XSSFFont font = workbook.createFont();
    font.setBold(true);
    font.setFontHeight(16);
    style.setFont(font);
    createCell(row, 0, "ID", style);
    createCell(row, 1, "City Name", style);
    createCell(row, 2, "Area Name", style);
    createCell(row, 3, "Description", style);
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
    for (Area area : areaService.getAreas()) {
      Row row = sheet.createRow(rowCount++);
      int columnCount = 0;
      createCell(row, columnCount++, area.getId(), style);
      createCell(row, columnCount++, area.getCityName(), style);
      createCell(row, columnCount++, area.getAreaName(), style);
      createCell(row, columnCount++, area.getDescription(), style);
    }
  }

  @GetMapping("/excel")
  public void generateExcelFile(HttpServletResponse response) throws IOException {
    response.setContentType("application/octet-stream");
    DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
    String currentDateTime = dateFormat.format(new Date());
    String headerkey = "Content-Disposition";
    String headervalue = "attachment; filename=area" + currentDateTime + ".xlsx";
    response.setHeader(headerkey, headervalue);
    writeHeader();
    write();
    ServletOutputStream outputStream = response.getOutputStream();
    workbook.write(outputStream);
    outputStream.close();
  }
}
