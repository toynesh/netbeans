/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdslvending.portal;

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
                String redirectURL = "NormalUser";
                response.sendRedirect(redirectURL);
            } else {
                String query = request.getParameter("query");
                System.out.println("query:" + query);
                try {
                    Connection con = data.connect();
                    PreparedStatement psmnt = null;
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery(query);

                    HSSFWorkbook wb = new HSSFWorkbook();
                    HSSFSheet sheet = wb.createSheet("Excel Sheet");
                    HSSFRow rowhead = sheet.createRow((short) 0);
                    rowhead.createCell((short) 0).setCellValue("Date");
                    rowhead.createCell((short) 1).setCellValue("Reference");
                    rowhead.createCell((short) 2).setCellValue("Type");
                    rowhead.createCell((short) 3).setCellValue("Account");
                    rowhead.createCell((short) 4).setCellValue("SaleAmount");
                    rowhead.createCell((short) 5).setCellValue("DepAmount");
                    rowhead.createCell((short) 6).setCellValue("Commission");
                    if (query.contains("vendor_code")) {
                        rowhead.createCell((short) 7).setCellValue("SubAG");
                    } else {
                        rowhead.createCell((short) 7).setCellValue("Response");
                    }
                    rowhead.createCell((short) 8).setCellValue("Status");

                    int index = 1;
                    while (rs.next()) {

                        HSSFRow row = sheet.createRow((short) index);
                        row.createCell((short) 0).setCellValue(rs.getString(8).substring(0, rs.getString(8).length() - 2));
                        row.createCell((short) 1).setCellValue(rs.getString(1));
                        row.createCell((short) 2).setCellValue(rs.getString(2));
                        String acc = " ";
                        if (null != rs.getString(3)) {
                            acc = rs.getString(3);
                        }
                        row.createCell((short) 3).setCellValue(acc);
                        row.createCell((short) 4).setCellValue(rs.getInt(4));
                        row.createCell((short) 5).setCellValue(rs.getString(5));
                        row.createCell((short) 6).setCellValue(rs.getString(6));
                        String res = " ";
                        if (null != rs.getString(7)) {
                            if (query.contains("vendor_code")) {
                                res = rs.getString(7)+"("+data.getVendorNameByCode(rs.getString(7))+")";
                            } else {
                                res = rs.getString(7);
                            }
                        }
                        row.createCell((short) 7).setCellValue(res);
                        String status = "completed";
                        if (rs.getString(9).equals("0")) {
                            status = "pending";
                        }
                        row.createCell((short) 8).setCellValue(status);
                        index++;
                    }
                    DateTime dt = new DateTime();
                    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMddhhmmss");
                    String currdate = fmt.print(dt);
                    String filename = currdate + "PDSLreport.xls";
                    FileOutputStream fileOut = new FileOutputStream("/home/pdsladmin/agentreports/" + filename);
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
