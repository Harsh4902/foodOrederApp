package com.example.foodOrderApp.conroller;

import com.example.foodOrderApp.entity.City;
import com.example.foodOrderApp.entity.SubCategory;
import com.example.foodOrderApp.generator.PdfGenerator;
import com.example.foodOrderApp.service.CategoryService;
import com.example.foodOrderApp.service.SubCategoryService;
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
@RequestMapping("/subcategory")
public class SubCategoryController {

    @Autowired
    private SubCategoryService subCategoryService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String manageSubCatagory(Model model){
//        List<SubCategory> subCategories = subCategoryService.getAllSubCategories();
//        model.addAttribute("subcategories",subCategories);
//        return "subCategoryTable :: subcategorytable";
        return mangeSubCategoryPaged(model, 1);
    }

    @GetMapping("/add-subcategory/{page}")
    public String addSubCategory(Model model,@PathVariable("page") int page){
        model.addAttribute("currentPage",page);
        model.addAttribute("categories",categoryService.getAllCatagories());
        return "addSubCategory :: subcategoryform";
    }


    @PostMapping("/addsubcategory/{page}")
    public String saveSubCategory(HttpServletRequest request, Model model,@PathVariable("page") int page){
        SubCategory subcategory = SubCategory.builder().categoryName(request.getParameter("category")).subCategoryName(request.getParameter("name")).description(request.getParameter("description")).build();
        subCategoryService.addSubCategory(subcategory);
//        List<SubCategory> subCategories = subCategoryService.getAllSubCategories();
//        model.addAttribute("subcategories",subCategories);
//        return "subCategoryTable :: subcategorytable";
        return mangeSubCategoryPaged(model, page);
    }

    @DeleteMapping("/delete-subcategory/{id}/{page}")
    public String deleteSubCategory(@PathVariable(name = "id") String id, Model model,@PathVariable("page") int page){
        System.out.println(id);
        subCategoryService.deleteSubCategory(Long.parseLong(id));
//        List<SubCategory> subCategories = subCategoryService.getAllSubCategories();
//        model.addAttribute("subcategories",subCategories);
//        return "subCategoryTable :: subcategorytable";
        return mangeSubCategoryPaged(model, page);
    }

    @GetMapping("/update-subcategory/{id}/{page}")
    public String updateForm(@PathVariable(name = "id")String id, Model model,@PathVariable("page") int page){
        SubCategory subcategory = subCategoryService.findSubCategoryById(Long.parseLong(id));
        System.out.println(subcategory);
        model.addAttribute("subcategory",subcategory);
        model.addAttribute("currentPage",page);
        return "addSubCategory :: updatesubcategory";
    }

    @PatchMapping("/update/{id}/{page}")
    public String updateCategory(@ModelAttribute("subcategory") SubCategory subcategory,Model model,@PathVariable("page") int page){
        System.out.println(subcategory);
        SubCategory temp = subCategoryService.findSubCategoryById(subcategory.getId());
        temp.setSubCategoryName(subcategory.getSubCategoryName());
        temp.setDescription(subcategory.getDescription());
        subCategoryService.addSubCategory(temp);
//        List<SubCategory> subCategories = subCategoryService.getAllSubCategories();
//        model.addAttribute("subcategories",subCategories);
//        return "subCategoryTable :: subcategorytable";
        return mangeSubCategoryPaged(model, page);
    }

    @GetMapping("/page/{page}")
    public String mangeSubCategoryPaged(Model model, @PathVariable("page") int page){

        int currentPage = page;
        int pageSize = 5;
        Page<SubCategory> subCategoryPage = subCategoryService.getPaginatedSubCategories(PageRequest.of(currentPage-1,pageSize));
        int totalPages = subCategoryPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", subCategoryPage.getTotalElements());
        model.addAttribute("subcategories",subCategoryPage.getContent());
        return "subCategoryTable :: subcategorytable";
    }

    @GetMapping("/pdf")
    public void generatePDf(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
        String currentDateTime = dateFormat.format(new Date());
        String headerkey = "Content-Disposition";
        String headervalue = "attachment; filename=subcategory" + currentDateTime + ".pdf";
        response.setHeader(headerkey, headervalue);
        PdfGenerator generator = new PdfGenerator();
        generator.generatePdfForSubCategory(subCategoryService.getAllSubCategories(), response);
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
        createCell(row, 1, "Category Name", style);
        createCell(row,2,"Subcategory Name", style);
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
        for (SubCategory subCategory : subCategoryService.getAllSubCategories()) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, subCategory.getId(), style);
            createCell(row, columnCount++, subCategory.getCategoryName(), style);
            createCell(row, columnCount++, subCategory.getSubCategoryName(), style);
            createCell(row, columnCount++, subCategory.getDescription(), style);
        }
    }

    @GetMapping("/excel")
    public void generateExcelFile(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
        String currentDateTime = dateFormat.format(new Date());
        String headerkey = "Content-Disposition";
        String headervalue = "attachment; filename=subcategory" + currentDateTime + ".xlsx";
        response.setHeader(headerkey, headervalue);
        writeHeader();
        write();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

}
