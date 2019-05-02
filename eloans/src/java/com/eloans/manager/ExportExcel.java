/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eloans.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JFileChooser;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author coolie
 */
public class ExportExcel extends HttpServlet {

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
                String query = request.getParameter("query");
                System.out.println("query:" + query);
                try {
                    Connection con = data.connect();
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery(query);

                    ResultSetMetaData metadata = rs.getMetaData();
                    int columnCount = metadata.getColumnCount();
                    System.out.println("Columns:" + columnCount);
                    HSSFWorkbook wb = new HSSFWorkbook();
                    HSSFSheet sheet = wb.createSheet("Excel Sheet");
                    HSSFRow rowhead = sheet.createRow((short) 0);
                    if (query.contains("paymenttrend")) {
                        rowhead.createCell((short) 0).setCellValue("Borrower");
                        rowhead.createCell((short) 1).setCellValue("Phone");
                        int x = 2;
                        for (int i = 2; i <= columnCount; i++) {
                            rowhead.createCell((short) x).setCellValue(metadata.getColumnLabel(i));
                            x++;
                        }
                    } else {
                        int x = 0;
                        for (int i = 1; i <= columnCount; i++) {
                            rowhead.createCell((short) x).setCellValue(metadata.getColumnLabel(i));
                            x++;
                        }
                    }

                    int index = 1;
                    while (rs.next()) {
                        if (query.contains("paymenttrend")) {
                            HSSFRow row = sheet.createRow((short) index);
                            String borrower = data.getBorrowername(rs.getString(1));
                            String bsplit[] = borrower.split(" - ");
                            row.createCell((short) 0).setCellValue(bsplit[1]);
                            row.createCell((short) 1).setCellValue(bsplit[0]);
                            for (int i = 2; i <= columnCount; i++) {
                                row.createCell((short) i).setCellValue(rs.getString(i));
                            }
                        } else {
                            HSSFRow row = sheet.createRow((short) index);
                            for (int i = 1; i <= columnCount; i++) {
                                row.createCell((short) i - 1).setCellValue(rs.getString(i));
                            }
                        }
                        index++;
                    }
                    DateTime dt = new DateTime();
                    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMddhhmmss");
                    String currdate = fmt.print(dt);
                    String filename = currdate + "eLoansreport.xls";
                    FileOutputStream fileOut = new FileOutputStream("/opt/applications/smpp/eloans/" + filename);
                    wb.write(fileOut);
                    fileOut.close();
                    rs.close();
                    con.close();

                    String redirectURL = "DownloadFile?ftdown=" + filename + "";
                    response.sendRedirect(redirectURL);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            /* TODO output your page here. You may use following sample code.
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ExportExcel</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ExportExcel at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>"); */
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
