/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdslvending.portal;

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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
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
                String redirectURL = "NormalUser";
                response.sendRedirect(redirectURL);
            } else {
                DateTime dt = new DateTime();
                DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMddhhmmss");
                String currdate = fmt.print(dt);
                String filename = currdate + "PDSLreport.pdf";
                String query = request.getParameter("query");
                String fullname = (String) request.getSession().getAttribute("fullname");
                //System.out.println(query);
                /* Step-2: Initialize PDF documents - logical objects */
                Document PDFLogReport = new Document(PageSize.A4, 0f, 0f, 15f, 0f);

                try {
                    PdfWriter.getInstance(PDFLogReport, new FileOutputStream("/home/pdsladmin/agentreports/" + filename));
                    PDFLogReport.open();
                    Paragraph paragraph1 = new Paragraph("PDSL " + fullname + " Transaction(s) Report");
                    paragraph1.setSpacingAfter(10f);
                    paragraph1.setAlignment(Element.ALIGN_CENTER);
                    PDFLogReport.add(paragraph1);
                    //we have nime columns in our table  
                    PdfPTable LogTable = new PdfPTable(9);
                    Font font = FontFactory.getFont(FontFactory.HELVETICA, 8);
                    //create a cell object  
                    PdfPCell table_cell;
                    String Date0 = "Date";
                    table_cell = new PdfPCell(new Phrase(Date0, font));
                    LogTable.addCell(table_cell);
                    String Reference0 = "Reference";
                    table_cell = new PdfPCell(new Phrase(Reference0, font));
                    LogTable.addCell(table_cell);
                    String Type0 = "Type";
                    table_cell = new PdfPCell(new Phrase(Type0, font));
                    LogTable.addCell(table_cell);
                    String Account0 = "Account";
                    table_cell = new PdfPCell(new Phrase(Account0, font));
                    LogTable.addCell(table_cell);
                    String SaleAmount0 = "SaleAmount";
                    table_cell = new PdfPCell(new Phrase(SaleAmount0, font));
                    LogTable.addCell(table_cell);
                    String DepAmount0 = "DepAmount";
                    table_cell = new PdfPCell(new Phrase(DepAmount0, font));
                    LogTable.addCell(table_cell);
                    String Commission0 = "Commission";
                    table_cell = new PdfPCell(new Phrase(Commission0, font));
                    LogTable.addCell(table_cell);
                    String Response0 = "Response";
                    if (query.contains("vendor_code,")) {
                        Response0 = "SubAG";
                    }
                    table_cell = new PdfPCell(new Phrase(Response0, font));
                    LogTable.addCell(table_cell);
                    String Status0 = "Status";
                    table_cell = new PdfPCell(new Phrase(Status0, font));
                    LogTable.addCell(table_cell);

                    List<List<String>> vendors = data.getVendors(query);

                    Connection con = data.connect();
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery(query);
                    while (rs.next()) {
                        String Date = rs.getString(8).substring(0, rs.getString(8).length() - 2);
                        table_cell = new PdfPCell(new Phrase(Date, font));
                        LogTable.addCell(table_cell);
                        String Reference = rs.getString(1);
                        table_cell = new PdfPCell(new Phrase(Reference, font));
                        LogTable.addCell(table_cell);
                        String Type = rs.getString(2);
                        table_cell = new PdfPCell(new Phrase(Type, font));
                        LogTable.addCell(table_cell);
                        String acc = " ";
                        if (null != rs.getString(3)) {
                            acc = rs.getString(3);
                        }
                        String Account = acc;
                        table_cell = new PdfPCell(new Phrase(Account, font));
                        LogTable.addCell(table_cell);
                        String SaleAmount = rs.getString(4);
                        table_cell = new PdfPCell(new Phrase(SaleAmount, font));
                        LogTable.addCell(table_cell);
                        String DepAmount = rs.getString(5);
                        table_cell = new PdfPCell(new Phrase(DepAmount, font));
                        LogTable.addCell(table_cell);
                        String Commission = rs.getString(6);
                        table_cell = new PdfPCell(new Phrase(Commission, font));
                        LogTable.addCell(table_cell);
                        String res = " ";
                        if (null != rs.getString(7)) {
                            if (query.contains("vendor_code,")) {
                                for (int y = 0; y < vendors.size(); y++) {
                                    if (rs.getString(7).equals(vendors.get(y).get(0))) {
                                        res = rs.getString(7) + "(" + vendors.get(y).get(1) + ")";
                                    }
                                }
                            } else {
                                res = rs.getString(7);
                            }
                        }
                        String Response = res;
                        table_cell = new PdfPCell(new Phrase(Response, font));
                        LogTable.addCell(table_cell);
                        String status = "completed";
                        if (rs.getString(9).equals("0")) {
                            status = "pending";
                        }
                        String Status = status;
                        table_cell = new PdfPCell(new Phrase(Status, font));
                        LogTable.addCell(table_cell);
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
