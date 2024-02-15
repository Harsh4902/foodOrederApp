package com.example.foodOrderApp.generator;

import com.example.foodOrderApp.entity.*;
import com.lowagie.text.*;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PdfGenerator {

    //generator for cities
    public void generatePdfForCity(List<City> cities, HttpServletResponse response) throws IOException {
        // Creating the Object of Document
        Document document = new Document(PageSize.A4);
        // Getting instance of PdfWriter
        PdfWriter.getInstance(document, response.getOutputStream());
        // Opening the created document to change it
        document.open();
        // Creating font
        // Setting font style and size
        Font fontTiltle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        fontTiltle.setSize(20);
        // Creating paragraph
        Paragraph paragraph1 = new Paragraph("List of Cities", fontTiltle);
        // Aligning the paragraph in the document
        paragraph1.setAlignment(Paragraph.ALIGN_CENTER);
        // Adding the created paragraph in the document
        document.add(paragraph1);
        // Creating a table of the 4 columns
        PdfPTable table = new PdfPTable(3);
        // Setting width of the table, its columns and spacing
        table.setWidthPercentage(100f);
        table.setWidths(new int[] {3,3,3});
        table.setSpacingBefore(5);
        // Create Table Cells for the table header
        PdfPCell cell = new PdfPCell();
        // Setting the background color and padding of the table cell
        cell.setBackgroundColor(CMYKColor.GRAY);
        cell.setPadding(5);
        // Creating font
        // Setting font style and size
        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        font.setColor(CMYKColor.WHITE);
        // Adding headings in the created table cell or  header
        // Adding Cell to table
        cell.setPhrase(new Phrase("ID", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("City Name", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Description", font));
        table.addCell(cell);
        // Iterating the list of students
        for (City city : cities) {
            table.addCell(""+city.getId());
            table.addCell(city.getCityName());
            table.addCell(city.getDescription());
        }
        document.add(table);
        document.close();
    }

    //generator for Area
    public void generatePdfForArea(List<Area> areas, HttpServletResponse response) throws IOException {
        // Creating the Object of Document
        Document document = new Document(PageSize.A4);
        // Getting instance of PdfWriter
        PdfWriter.getInstance(document, response.getOutputStream());
        // Opening the created document to change it
        document.open();
        // Creating font
        // Setting font style and size
        Font fontTiltle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        fontTiltle.setSize(20);
        // Creating paragraph
        Paragraph paragraph1 = new Paragraph("List of Area", fontTiltle);
        // Aligning the paragraph in the document
        paragraph1.setAlignment(Paragraph.ALIGN_CENTER);
        // Adding the created paragraph in the document
        document.add(paragraph1);
        // Creating a table of the 4 columns
        PdfPTable table = new PdfPTable(4);
        // Setting width of the table, its columns and spacing
        table.setWidthPercentage(100f);
        table.setWidths(new int[] {3,3,3,3});
        table.setSpacingBefore(5);
        // Create Table Cells for the table header
        PdfPCell cell = new PdfPCell();
        // Setting the background color and padding of the table cell
        cell.setBackgroundColor(CMYKColor.GRAY);
        cell.setPadding(5);
        // Creating font
        // Setting font style and size
        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        font.setColor(CMYKColor.WHITE);
        // Adding headings in the created table cell or  header
        // Adding Cell to table
        cell.setPhrase(new Phrase("ID", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("City Name", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Area Name", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Description", font));
        table.addCell(cell);
        // Iterating the list of students
        for (Area area : areas) {
            table.addCell(""+area.getId());
            table.addCell(area.getCityName());
            table.addCell(area.getAreaName());
            table.addCell(area.getDescription());
        }
        document.add(table);
        document.close();
    }

    //generator for Category
    public void generatePdfForCategory(List<Category> categories, HttpServletResponse response) throws IOException {
        // Creating the Object of Document
        Document document = new Document(PageSize.A4);
        // Getting instance of PdfWriter
        PdfWriter.getInstance(document, response.getOutputStream());
        // Opening the created document to change it
        document.open();
        // Creating font
        // Setting font style and size
        Font fontTiltle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        fontTiltle.setSize(20);
        // Creating paragraph
        Paragraph paragraph1 = new Paragraph("List of Categories", fontTiltle);
        // Aligning the paragraph in the document
        paragraph1.setAlignment(Paragraph.ALIGN_CENTER);
        // Adding the created paragraph in the document
        document.add(paragraph1);
        // Creating a table of the 4 columns
        PdfPTable table = new PdfPTable(3);
        // Setting width of the table, its columns and spacing
        table.setWidthPercentage(100f);
        table.setWidths(new int[] {3,3,3,});
        table.setSpacingBefore(5);
        // Create Table Cells for the table header
        PdfPCell cell = new PdfPCell();
        // Setting the background color and padding of the table cell
        cell.setBackgroundColor(CMYKColor.GRAY);
        cell.setPadding(5);
        // Creating font
        // Setting font style and size
        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        font.setColor(CMYKColor.WHITE);
        // Adding headings in the created table cell or  header
        // Adding Cell to table
        cell.setPhrase(new Phrase("ID", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Category Name", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Description", font));
        table.addCell(cell);
        // Iterating the list of students
        for (Category category : categories) {
            table.addCell(""+category.getId());
            table.addCell(category.getName());
            table.addCell(category.getDescription());
        }
        document.add(table);
        document.close();
    }

    //generator for SubCategory
    public void generatePdfForSubCategory(List<SubCategory> subCategories, HttpServletResponse response) throws IOException {
        // Creating the Object of Document
        Document document = new Document(PageSize.A4);
        // Getting instance of PdfWriter
        PdfWriter.getInstance(document, response.getOutputStream());
        // Opening the created document to change it
        document.open();
        // Creating font
        // Setting font style and size
        Font fontTiltle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        fontTiltle.setSize(20);
        // Creating paragraph
        Paragraph paragraph1 = new Paragraph("List of Subcategories", fontTiltle);
        // Aligning the paragraph in the document
        paragraph1.setAlignment(Paragraph.ALIGN_CENTER);
        // Adding the created paragraph in the document
        document.add(paragraph1);
        // Creating a table of the 4 columns
        PdfPTable table = new PdfPTable(4);
        // Setting width of the table, its columns and spacing
        table.setWidthPercentage(100f);
        table.setWidths(new int[] {3,3,3,3});
        table.setSpacingBefore(5);
        // Create Table Cells for the table header
        PdfPCell cell = new PdfPCell();
        // Setting the background color and padding of the table cell
        cell.setBackgroundColor(CMYKColor.GRAY);
        cell.setPadding(5);
        // Creating font
        // Setting font style and size
        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        font.setColor(CMYKColor.WHITE);
        // Adding headings in the created table cell or  header
        // Adding Cell to table
        cell.setPhrase(new Phrase("ID", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Category Name", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Subategory Name", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Description", font));
        table.addCell(cell);
        // Iterating the list of students
        for (SubCategory subCategory : subCategories) {
            table.addCell(""+subCategory.getId());
            table.addCell(subCategory.getCategoryName());
            table.addCell(subCategory.getSubCategoryName());
            table.addCell(subCategory.getDescription());
        }
        document.add(table);
        document.close();
    }

    //generator for restaurants
    public void generatePdfForRestaurant(List<Restaurant> restaurants, HttpServletResponse response) throws IOException {
        // Creating the Object of Document
        Document document = new Document(PageSize.A4);
        // Getting instance of PdfWriter
        PdfWriter.getInstance(document, response.getOutputStream());
        // Opening the created document to change it
        document.open();
        // Creating font
        // Setting font style and size
        Font fontTiltle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        fontTiltle.setSize(20);
        // Creating paragraph
        Paragraph paragraph1 = new Paragraph("List of Restaurants", fontTiltle);
        // Aligning the paragraph in the document
        paragraph1.setAlignment(Paragraph.ALIGN_CENTER);
        // Adding the created paragraph in the document
        document.add(paragraph1);
        // Creating a table of the 4 columns
        PdfPTable table = new PdfPTable(7);
        // Setting width of the table, its columns and spacing
        table.setWidthPercentage(100f);
        table.setWidths(new int[] {3,3,3,3,3,3,3});
        table.setSpacingBefore(5);
        // Create Table Cells for the table header
        PdfPCell cell = new PdfPCell();
        // Setting the background color and padding of the table cell
        cell.setBackgroundColor(CMYKColor.GRAY);
        cell.setPadding(5);
        // Creating font
        // Setting font style and size
        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        font.setColor(CMYKColor.WHITE);
        // Adding headings in the created table cell or  header
        // Adding Cell to table
        cell.setPhrase(new Phrase("ID", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Restaurant Name", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("City Name", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Area Name", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Email", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Contact No.", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Address", font));
        table.addCell(cell);
        // Iterating the list of students
        for (Restaurant restaurant : restaurants) {
            table.addCell(""+restaurant.getId());
            table.addCell(restaurant.getName());
            table.addCell(restaurant.getCityName());
            table.addCell(restaurant.getAreaName());
            table.addCell(restaurant.getEmail());
            table.addCell(""+restaurant.getContactNo());
            table.addCell(restaurant.getAddress());
        }
        document.add(table);
        document.close();
    }

    //generator for Products
    public void generatePdfForProduct(List<Product> products, HttpServletResponse response) throws IOException {
        // Creating the Object of Document
        Document document = new Document(PageSize.A4);
        // Getting instance of PdfWriter
        PdfWriter.getInstance(document, response.getOutputStream());
        // Opening the created document to change it
        document.open();
        // Creating font
        // Setting font style and size
        Font fontTiltle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        fontTiltle.setSize(20);
        // Creating paragraph
        Paragraph paragraph1 = new Paragraph("List of Restaurants", fontTiltle);
        // Aligning the paragraph in the document
        paragraph1.setAlignment(Paragraph.ALIGN_CENTER);
        // Adding the created paragraph in the document
        document.add(paragraph1);
        // Creating a table of the 4 columns
        PdfPTable table = new PdfPTable(7);
        // Setting width of the table, its columns and spacing
        table.setWidthPercentage(100f);
        table.setWidths(new int[] {3,3,3,3,3,3,3});
        table.setSpacingBefore(5);
        // Create Table Cells for the table header
        PdfPCell cell = new PdfPCell();
        // Setting the background color and padding of the table cell
        cell.setBackgroundColor(CMYKColor.GRAY);
        cell.setPadding(5);
        // Creating font
        // Setting font style and size
        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        font.setColor(CMYKColor.WHITE);
        // Adding headings in the created table cell or  header
        // Adding Cell to table
        cell.setPhrase(new Phrase("ID", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Name", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Category Name", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Subcategory Name", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("ImageURL", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Price", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Description", font));
        table.addCell(cell);
        // Iterating the list of students
        for (Product product : products) {
            table.addCell(""+product.getId());
            table.addCell(product.getProductName());
            table.addCell(product.getCategoryName());
            table.addCell(product.getSubCategoryName());
            table.addCell(product.getImageURL());
            table.addCell(""+product.getPrice());
            table.addCell(product.getDescription());
        }
        document.add(table);
        document.close();
    }

    //generator for Complaints
    public void generatePdfForComplaints(List<Complaint> complaints, HttpServletResponse response) throws IOException {
        // Creating the Object of Document
        Document document = new Document(PageSize.A4);
        // Getting instance of PdfWriter
        PdfWriter.getInstance(document, response.getOutputStream());
        // Opening the created document to change it
        document.open();
        // Creating font
        // Setting font style and size
        Font fontTiltle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        fontTiltle.setSize(20);
        // Creating paragraph
        Paragraph paragraph1 = new Paragraph("List of Restaurants", fontTiltle);
        // Aligning the paragraph in the document
        paragraph1.setAlignment(Paragraph.ALIGN_CENTER);
        // Adding the created paragraph in the document
        document.add(paragraph1);
        // Creating a table of the 4 columns
        PdfPTable table = new PdfPTable(9);
        // Setting width of the table, its columns and spacing
        table.setWidthPercentage(100f);
        table.setWidths(new int[] {3,3,3,3,3,3,3,3,3});
        table.setSpacingBefore(5);
        // Create Table Cells for the table header
        PdfPCell cell = new PdfPCell();
        // Setting the background color and padding of the table cell
        cell.setBackgroundColor(CMYKColor.GRAY);
        cell.setPadding(5);
        // Creating font
        // Setting font style and size
        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        font.setColor(CMYKColor.WHITE);
        // Adding headings in the created table cell or  header
        // Adding Cell to table
        cell.setPhrase(new Phrase("ID", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("User Name", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Subject", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Complaint Date", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Reply", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Reply Date", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Status", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Attachment", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Description", font));
        table.addCell(cell);
        // Iterating the list of students
        for (Complaint complaint : complaints) {
            table.addCell(""+complaint.getId());
            table.addCell(complaint.getUserName());
            table.addCell(complaint.getSubject());
            table.addCell(complaint.getComplaintDate());
            table.addCell(complaint.getReply());
            table.addCell(complaint.getReplyDate());
            table.addCell(complaint.getStatus().name());
            table.addCell(complaint.getAttachment());
            table.addCell(complaint.getDescription());
        }
        document.add(table);
        document.close();
    }

    //generator for Offers
    public void generatePdfForOffers(List<Offer> offers, HttpServletResponse response) throws IOException {
        // Creating the Object of Document
        Document document = new Document(PageSize.A4);
        // Getting instance of PdfWriter
        PdfWriter.getInstance(document, response.getOutputStream());
        // Opening the created document to change it
        document.open();
        // Creating font
        // Setting font style and size
        Font fontTiltle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        fontTiltle.setSize(20);
        // Creating paragraph
        Paragraph paragraph1 = new Paragraph("List of Restaurants", fontTiltle);
        // Aligning the paragraph in the document
        paragraph1.setAlignment(Paragraph.ALIGN_CENTER);
        // Adding the created paragraph in the document
        document.add(paragraph1);
        // Creating a table of the 4 columns
        PdfPTable table = new PdfPTable(9);
        // Setting width of the table, its columns and spacing
        table.setWidthPercentage(100f);
        table.setWidths(new int[] {3,3,3,3,3,3,3,3,3});
        table.setSpacingBefore(5);
        // Create Table Cells for the table header
        PdfPCell cell = new PdfPCell();
        // Setting the background color and padding of the table cell
        cell.setBackgroundColor(CMYKColor.GRAY);
        cell.setPadding(5);
        // Creating font
        // Setting font style and size
        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        font.setColor(CMYKColor.WHITE);
        // Adding headings in the created table cell or  header
        // Adding Cell to table
        cell.setPhrase(new Phrase("ID", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("offer Name", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Category Name", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Subcategory Name", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Restaurant Name", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Discount", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Start Date", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("End Date", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Description", font));
        table.addCell(cell);
        // Iterating the list of students
        for (Offer offer : offers) {
            table.addCell(""+offer.getId());
            table.addCell(offer.getOfferName());
            table.addCell(offer.getCategoryName());
            table.addCell(offer.getSubCategoryName());
            table.addCell(offer.getRestaurantName());
            table.addCell(""+offer.getDiscount());
            table.addCell(offer.getStartDate());
            table.addCell(offer.getEndDate());
            table.addCell(offer.getDescription());
        }
        document.add(table);
        document.close();
    }

}
