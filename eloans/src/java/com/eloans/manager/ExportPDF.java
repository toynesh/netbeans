/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eloans.manager;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author coolie
 */
public class ExportPDF extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    DataStore data = new DataStore();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            if (null == request.getParameter("query")) {
                String redirectURL = "Home";
                response.sendRedirect(redirectURL);
            } else {
                DateTime dt = new DateTime();
                DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMddhhmmss");
                String currdate = fmt.print(dt);
                String filename = currdate + "eLoansreport.pdf";
                String query = request.getParameter("query");
                //System.out.println(query);
                /* Step-2: Initialize PDF documents - logical objects */
                Document PDFLogReport = new Document(PageSize.A4, 0f, 0f, 15f, 0f);

                try {
                    PdfWriter.getInstance(PDFLogReport, new FileOutputStream("/opt/applications/smpp/eloans/" + filename));
                    PDFLogReport.open();
                    Paragraph paragraph1 = new Paragraph("ELOANS Report");
                    paragraph1.setSpacingAfter(10f);
                    paragraph1.setAlignment(Element.ALIGN_CENTER);
                    PDFLogReport.add(paragraph1);

                    //we have name columns in our table 
                    Connection con = data.connect();
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery(query);

                    ResultSetMetaData metadata = rs.getMetaData();
                    int columnCount = metadata.getColumnCount();
                    if (query.contains("paymenttrend")) {
                        columnCount = columnCount+1;
                    }
                    System.out.println("Columns:" + columnCount);
                    PdfPTable LogTable = new PdfPTable(columnCount);
                    Font font = FontFactory.getFont(FontFactory.HELVETICA, 8);

                    //create a cell object 
                    PdfPCell table_cell;
                    if (query.contains("paymenttrend")) {
                        String Borrower0 = "Borrower";
                        table_cell = new PdfPCell(new Phrase(Borrower0, font));
                        LogTable.addCell(table_cell);
                        String Phone0 = "Phone";
                        table_cell = new PdfPCell(new Phrase(Phone0, font));
                        LogTable.addCell(table_cell);
                        for (int i = 2; i <= columnCount-1; i++) {
                            String clm = metadata.getColumnLabel(i);
                            table_cell = new PdfPCell(new Phrase(clm, font));
                            LogTable.addCell(table_cell);
                        }
                    } else {
                        for (int i = 1; i <= columnCount; i++) {
                            String clm = metadata.getColumnLabel(i);
                            table_cell = new PdfPCell(new Phrase(clm, font));
                            LogTable.addCell(table_cell);
                        }
                    }
                   while (rs.next()) {
                        if (query.contains("paymenttrend")) {
                            String borrower = data.getBorrowername(rs.getString(1));
                            String bsplit[] = borrower.split(" - ");
                            String Borrower = bsplit[1];
                            table_cell = new PdfPCell(new Phrase(Borrower, font));
                            LogTable.addCell(table_cell);
                            String Phone = bsplit[0];
                            table_cell = new PdfPCell(new Phrase(Phone, font));
                            LogTable.addCell(table_cell);
                            for (int i = 2; i <= columnCount-1; i++) {
                                String clm = rs.getString(i);
                                table_cell = new PdfPCell(new Phrase(clm, font));
                                LogTable.addCell(table_cell);
                            }
                        } else {
                            for (int i = 1; i <= columnCount; i++) {
                                String clm = rs.getString(i);
                                table_cell = new PdfPCell(new Phrase(clm, font));
                                LogTable.addCell(table_cell);
                            }
                        }
                    }
                    /* Attach report table to PDF */
                    PDFLogReport.add(LogTable);
                    PDFLogReport.close();
                    /* Close all DB related objects */
                    rs.close();
                    st.close();
                    con.close();

                } catch (DocumentException ex) {
                    Logger.getLogger(ExportPDF.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(ExportPDF.class.getName()).log(Level.SEVERE, null, ex);
                }
                String redirectURL = "DownloadFile?ftdown=" + filename + "";
                response.sendRedirect(redirectURL);
            }

            /* TODO output your page here. You may use following sample code. 
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ExportPDF</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ExportPDF at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");*/
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>    

}
