package com.example.foodOrderApp.conroller;


import com.example.foodOrderApp.entity.Area;
import com.example.foodOrderApp.entity.Product;
import com.example.foodOrderApp.generator.PdfGenerator;
import com.example.foodOrderApp.service.ProductService;
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
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
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
@RequestMapping("/products")
public class ProductController {

    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads/products";

    @Autowired
    private ProductService productService;

    private XSSFWorkbook workbook = new XSSFWorkbook();
    private XSSFSheet sheet;

    @GetMapping
    public String manageProducts(Model model){
        return mangeProductPaged(model,1);
    }

    @GetMapping("/add-product/{page}")
    public String productFrom(Model model,@PathVariable int page){
        model.addAttribute("currentPage",page);
        return "addProduct :: productform";
    }

    @PostMapping("/addproduct/{page}")
    public String addProduct(HttpServletRequest request, Model model, @RequestParam("image")MultipartFile file,@PathVariable("page") int page) throws IOException {
        StringBuilder fileNames = new StringBuilder();
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
        fileNames.append(file.getOriginalFilename());
        Files.write(fileNameAndPath, file.getBytes());
        model.addAttribute("msg", "Uploaded images: " + fileNames.toString());
        Product product = Product.builder()
                .productName(request.getParameter("name"))
                .categoryName(request.getParameter("category"))
                .subCategoryName(request.getParameter("subCategory"))
                .price(Integer.parseInt(request.getParameter("price")))
                .imageURL(fileNames.toString())
                .description(request.getParameter("description"))
                .build();
        productService.addProduct(product);
//        model.addAttribute("products",productService.getAllProducts());
//        return "productTable :: producttable";
        return mangeProductPaged(model,page);
    }

    @DeleteMapping("/delete-product/{id}/{page}")
    public String deleteCity(@PathVariable(name = "id") String id,Model model,@PathVariable("page") int page){
       productService.deleteProductById(Long.parseLong(id));
       return mangeProductPaged(model,page);
    }

    @GetMapping("/update-product/{id}/{page}")
    public String updateForm(@PathVariable(name = "id")String id, Model model,@PathVariable("page") int page){
        Product product = productService.getProductById(Long.parseLong(id));
        System.out.println(product);
        model.addAttribute("product",product);
        model.addAttribute("currentPage",page);
        return "addProduct :: updateproduct";
    }

    @PatchMapping("/update/{id}/{page}")
    public String updateProduct(@ModelAttribute("product") Product product,Model model,@RequestParam("image") MultipartFile file,@PathVariable("page") int page) throws IOException{
        StringBuilder fileNames = new StringBuilder();
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
        fileNames.append(file.getOriginalFilename());
        Files.write(fileNameAndPath, file.getBytes());

        Product temp = productService.getProductById(product.getId());
        temp.setProductName(product.getProductName());
        temp.setPrice(product.getPrice());
        temp.setDescription(product.getDescription());
        temp.setImageURL(fileNames.toString());
        return mangeProductPaged(model,page);
    }

    @GetMapping("/page/{page}")
    public String mangeProductPaged(Model model, @PathVariable("page") int page){

        int currentPage = page;
        int pageSize = 5;
        Page<Product> productPage = productService.getPaginatedProduct(PageRequest.of(currentPage-1,pageSize));
        int totalPages = productPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
              .boxed()
              .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", productPage.getTotalElements());
        model.addAttribute("products",productPage.getContent());
        return "productTable :: producttable";
    }

    @GetMapping("/image/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) throws MalformedURLException {
        Path root = Paths.get(UPLOAD_DIRECTORY);
        Path file = root.resolve(filename);
        Resource files = new UrlResource(file.toUri());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + files.getFilename() + "\"").body(files);
    }

    @GetMapping("/pdf")
    public void generatePDf(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
        String currentDateTime = dateFormat.format(new Date());
        String headerkey = "Content-Disposition";
        String headervalue = "attachment; filename=product" + currentDateTime + ".pdf";
        response.setHeader(headerkey, headervalue);
        PdfGenerator generator = new PdfGenerator();
        generator.generatePdfForProduct(productService.getAllProducts(), response);
    }

    private void writeHeader() {
        String dateTime=new Date().toString().replaceAll(":", "_");
        sheet = workbook.createSheet("Products"+dateTime);
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        createCell(row, 0, "ID", style);
        createCell(row, 1, "Name", style);
        createCell(row, 2, "Category Name", style);
        createCell(row, 3, "Subcategory Name", style);
        createCell(row, 4, "ImageURL", style);
        createCell(row, 5, "Price", style);
        createCell(row, 6, "Description", style);
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
        for (Product product : productService.getAllProducts()) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, product.getId(), style);
            createCell(row, columnCount++, product.getProductName(), style);
            createCell(row, columnCount++, product.getCategoryName(), style);
            createCell(row, columnCount++, product.getSubCategoryName(), style);
            createCell(row, columnCount++, product.getImageURL(), style);
            createCell(row, columnCount++,  product.getPrice(), style);
            createCell(row, columnCount++, product.getDescription(), style);
        }
    }

    @GetMapping("/excel")
    public void generateExcelFile(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
        String currentDateTime = dateFormat.format(new Date());
        String headerkey = "Content-Disposition";
        String headervalue = "attachment; filename=product" + currentDateTime + ".xlsx";
        response.setHeader(headerkey, headervalue);
        writeHeader();
        write();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.close();
    }
}
