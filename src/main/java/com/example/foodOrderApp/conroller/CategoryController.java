package com.example.foodOrderApp.conroller;

import com.example.foodOrderApp.entity.Category;
import com.example.foodOrderApp.entity.City;
import com.example.foodOrderApp.generator.PdfGenerator;
import com.example.foodOrderApp.service.CategoryService;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    private XSSFWorkbook workbook = new XSSFWorkbook();
    private XSSFSheet sheet;

    @GetMapping("/add-category/{page}")
    public String addCategory(Model model,@PathVariable("page") int page){
        model.addAttribute("currentPage",page);
        return "addCategory :: categoryform";
    }

    @GetMapping
    public String manageCatagory(Model model){
//        List<Category> categories = categoryService.getAllCatagories();
//        model.addAttribute("catagories",categories);
//        return "catagoryTable :: catagorytable";
        return mangeCategoryPaged(model, 1);
    }

    @PostMapping("/addcategory/{page}")
    public String saveCategory(HttpServletRequest request, Model model,@PathVariable("page") int page){
        Category category = Category.builder().name(request.getParameter("name")).description(request.getParameter("description")).build();
        categoryService.addCatagory(category);
//        List<Category> categories = categoryService.getAllCatagories();
//        model.addAttribute("catagories",categories);
//        return "catagoryTable :: catagorytable";
        return mangeCategoryPaged(model, page);
    }

    @DeleteMapping("/delete-category/{id}/{page}")
    public String deleteCategory(@PathVariable(name = "id") String id, Model model,@PathVariable("page") int page){
        System.out.println(id);
        categoryService.deleteCategory(Long.parseLong(id));
//        List<Category> categories = categoryService.getAllCatagories();
//        model.addAttribute("catagories",categories);
//        return "catagoryTable :: catagorytable";
        return mangeCategoryPaged(model,page);
    }

    @GetMapping("/update-category/{id}/{page}")
    public String updateForm(@PathVariable(name = "id")String id, Model model,@PathVariable("page") int page){
        Category category = categoryService.findCategoryById(Long.parseLong(id));
        System.out.println(category);
        model.addAttribute("category",category);
        model.addAttribute("currentPage",page);
        return "addCategory :: updatecategory";
    }

    @PatchMapping("/update/{id}/{page}")
    public String updateCategory(@ModelAttribute("category") Category category,Model model,@PathVariable("page") int page){
        Category temp = categoryService.findCategoryById(category.getId());
        temp.setName(category.getName());
        temp.setDescription(category.getDescription());
        categoryService.addCatagory(temp);
//        List<Category> categories = categoryService.getAllCatagories();
//        model.addAttribute("catagories",categories);
//        return "catagoryTable :: catagorytable";
        return mangeCategoryPaged(model,page);
    }

    @GetMapping("/page/{page}")
    public String mangeCategoryPaged(Model model, @PathVariable("page") int page){

        int currentPage = page;
        int pageSize = 5;
        Page<Category> categoryPage = categoryService.getPaginatedCategories(PageRequest.of(currentPage-1,pageSize));
        int totalPages = categoryPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", categoryPage.getTotalElements());
        model.addAttribute("catagories",categoryPage.getContent());
        return "catagoryTable :: catagorytable";
    }

    @GetMapping("/pdf")
    public void generatePDf(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
        String currentDateTime = dateFormat.format(new Date());
        String headerkey = "Content-Disposition";
        String headervalue = "attachment; filename=category" + currentDateTime + ".pdf";
        response.setHeader(headerkey, headervalue);
        PdfGenerator generator = new PdfGenerator();
        generator.generatePdfForCategory(categoryService.getAllCatagories(), response);
    }

    private void writeHeader() {
        String dateTime=new Date().toString().replaceAll(":", "_");
        sheet = workbook.createSheet("Categories"+dateTime);
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        createCell(row, 0, "ID", style);
        createCell(row, 1, "Category Name", style);
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
        for (Category category : categoryService.getAllCatagories()) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, category.getId(), style);
            createCell(row, columnCount++, category.getName(), style);
            createCell(row, columnCount++, category.getDescription(), style);
        }
    }

    @GetMapping("/excel")
    public void generateExcelFile(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
        String currentDateTime = dateFormat.format(new Date());
        String headerkey = "Content-Disposition";
        String headervalue = "attachment; filename=category" + currentDateTime + ".xlsx";
        response.setHeader(headerkey, headervalue);
        writeHeader();
        write();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.close();
    }
}
