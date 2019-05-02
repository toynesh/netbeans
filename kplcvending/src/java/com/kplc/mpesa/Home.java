/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kplc.mpesa;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
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
public class Home extends HttpServlet {

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
        Connection con = data.connect();
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println(data.header);

            DateTime sdt = new DateTime();
            DateTimeFormatter sfmt = DateTimeFormat.forPattern("dd-mm-yyyy");
            String placeholderdate = sfmt.print(sdt);
            out.println("<div class='container-fluid' style='margin-top:2%'>");
            out.println("<div class='row'>");
            out.println("<div class='col-lg-3'>");
            out.println("<div id='navcontainer'>");
            out.println("<a data-toggle='collapse' href='.mobilenumber'>");
            out.println("<div class='mobilenumber collapse in'>");
            out.println("<i class='fas fa-phone'></i> Query By Mobile Number <i class='fas fa-angle-double-right pull-right'></i>");
            out.println("</div> ");
            out.println("<div class='mobilenumber collapse'>");
            out.println("<i class='fas fa-phone'></i> Query By Mobile Number <i class='fas fa-angle-double-down pull-right'></i>");
            out.println("</div>");
            out.println("</a>");
            out.println("<div class='mobilenumber collapse' style='padding-top:4%'>");
            out.println("<form action='#' method='post'>");
            out.println("<div class='input-group'>");
            out.println("<input type='text' class='form-control' id='msisdn' name='msisdn' placeholder='E.g 254720640926'>");
            out.println("<span class='input-group-btn'>");
            out.println("<button class='btn btn-primary' type='button'>Go!</button>");
            out.println("</span>");
            out.println("</div>");
            out.println("</form>");
            out.println("</div>");
            out.println("<hr>");
            out.println("<a data-toggle='collapse' href='.meternumber'>");
            out.println("<div class='meternumber collapse in'>");
            out.println("<i class='fas fa-tachometer-alt'></i> Query By Meter Number <i class='fas fa-angle-double-right pull-right'></i>");
            out.println("</div>");
            out.println("<div class='meternumber collapse'>");
            out.println("<i class='fas fa-tachometer-alt'></i> Query By Meter Number <i class='fas fa-angle-double-down pull-right'></i>");
            out.println("</div>");
            out.println("</a>");
            out.println("<div class='meternumber collapse' style='padding-top:4%'>");
            out.println("<form action='#' method='post'>");
            out.println("<div class='input-group'>");
            out.println("<input type='text' class='form-control' id='meter' name='meter' placeholder='E.g 14763391074'>");
            out.println("<span class='input-group-btn'>");
            out.println("<button class='btn btn-primary' type='button'>Go!</button>");
            out.println("</span>");
            out.println("</div>");
            out.println("</form>");
            out.println("</div>");
            out.println("<hr>");
            out.println("<a data-toggle='collapse' href='.accountnumber'>");
            out.println("<div class='accountnumber collapse in'>");
            out.println("<i class='fab fa-accusoft'></i> Query By Account Number <i class='fas fa-angle-double-right pull-right'></i>");
            out.println("</div> ");
            out.println("<div class='accountnumber collapse'>");
            out.println("<i class='fab fa-accusoft'></i> Query By Account Number <i class='fas fa-angle-double-down pull-right'></i>");
            out.println("</div>");
            out.println("</a>");
            out.println("<div class='accountnumber collapse' style='padding-top:4%'>");
            out.println("<form action='#' method='post'>");
            out.println("<div class='input-group'>");
            out.println("<input type='text' class='form-control' id='meter' name='meter' placeholder='E.g 2586079-02'>");
            out.println("<span class='input-group-btn'>");
            out.println("<button class='btn btn-primary' type='button'>Go!</button>");
            out.println("</span>");
            out.println("</div>");
            out.println("</form>");
            out.println("</div>");
            out.println("<hr>");
            out.println("<a data-toggle='collapse' href='.mpesacode'>");
            out.println("<div class='mpesacode collapse in'>");
            out.println("<i class='fas fa-money-bill-alt'></i> Query By Mpesa Code <i class='fas fa-angle-double-right pull-right'></i>");
            out.println("</div> ");
            out.println("<div class='mpesacode collapse'>");
            out.println("<i class='fas fa-money-bill-alt'></i> Query By Mpesa Code <i class='fas fa-angle-double-down pull-right'></i>");
            out.println("</div>");
            out.println("</a>");
            out.println("<div class='mpesacode collapse' style='padding-top:4%'>");
            out.println("<form action='#' method='post'>");
            out.println("<div class='input-group'>");
            out.println("<input type='text' class='form-control' id='mpesacode' name='mpesacode' placeholder='E.g QUYTREWTY0I'>");
            out.println("<span class='input-group-btn'>");
            out.println("<button class='btn btn-primary' type='button'>Go!</button>");
            out.println("</span>");
            out.println("</div>");
            out.println("</form>");
            out.println("</div>");
            out.println("<hr>");
            out.println("<a data-toggle='collapse' href='.daterange'>");
            out.println("<div class='daterange collapse in'>");
            out.println("<i class='fas fa-clock'></i> Query By Date Rage <i class='fas fa-angle-double-right pull-right'></i>");
            out.println("</div>");
            out.println("<div class='daterange collapse'>");
            out.println("<i class='fas fa-clock'></i> Query By Date Rage <i class='fas fa-angle-double-down pull-right'></i>");
            out.println("</div>");
            out.println("</a>");
            out.println("<div class='daterange collapse' style='padding-top:4%'>");
            out.println("<form action='#' method='post'>");
            out.println("<div class='input-group' style='margin-bottom:4%'>");
            out.println("<span class='input-group-addon' id='basic-addon1'>StartDate:</span>");
            out.println("<input type='text' class='datepicker-here' data-language='en' data-position='top left' data-date-format='dd-mm-yyyy' name='sdate' placeholder='" + placeholderdate + "' aria-describedby='basic-addon1'>");
            out.println("</div>");
            out.println("<div class='input-group' style='margin-bottom:2%'>");
            out.println("<span class='input-group-addon' id='basic-addon1'>End  Date:</span>");
            out.println("<input type='text' class='datepicker-here' data-language='en' data-position='top left' data-date-format='dd-mm-yyyy' name='edate' placeholder='" + placeholderdate + "' aria-describedby='basic-addon1'>");
            out.println("</div>");
            out.println("<button class='btn btn-primary' type='button'  style='margin-left:80%'>Go!</button>");
            out.println("</form>");
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");

            String limit = " limit 5";
            String query = "SELECT names,mobile,meter,ref,message,time from kplcTransactions order by id desc" + limit + "";

            out.println("<div class='col-lg-9'>");
            out.println("<div class='panel panel-primary  table-responsive'>");
            out.println("<div class='panel-heading'><span>Mpesa Transactions</span><span class='pull-right'>Export:&nbsp;&nbsp;<a href='" + request.getContextPath() + "/ExportExcel?query=''><i class='far fa-file-excel'></i> Excel</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href='" + request.getContextPath() + "/ExportPDF?query=''><i class='far fa-file-pdf'></i> PDF</a></span></span></div>");
            out.println("<div class='panel-body'>");
            out.println("<table class='table table-bordered' style='font-size:11px'>");
            out.println("<thead>");
            out.println("<tr>");
            out.println("<th>Name</th>");
            out.println("<th>Mobile</th>");
            out.println("<th>Meter/Account</th>");
            out.println("<th>Code</th>");
            out.println("<th>Message</th>");
            out.println("<th>MessageID</th>");
            out.println("<th>Source</th>");
            out.println("<th>Time</th>");
            out.println("<th>Action</th>");
            out.println("</tr>");
            out.println("</thead>");
            out.println("<tbody style='color:black'>");

            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(query);
            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getString(1) + "</td>");
                out.println("<td>" + rs.getString(2) + "</td>");
                out.println("<td>" + rs.getString(3) + "</td>");
                out.println("<td>" + rs.getString(4) + "</td>");
                out.println("<td>" + rs.getString(5) + "</td>");
                out.println("<td></td>");
                out.println("<td></td>");
                out.println("<td>" + rs.getString(6) + "</td>");
                out.println("<td><a class='text-danger'>Resend</a></td>");
                out.println("</tr>");
            }
            out.println("</tbody>");
            out.println("</table>");

            out.println("<nav aria-label='...' class='text-center'>");
            out.println("<ul class='pagination pagination-sm'>");
            out.println("<li class='page-item disabled'>");
            out.println("<a class='page-link' href='#' aria-label='Previous'>");
            out.println("<span aria-hidden='true'>&laquo;</span>");
            out.println("<span class='sr-only'>Previous</span>");
            out.println("</a>");
            out.println("</li>");
            out.println("<li class='page-item'><a class='page-link' href='#'>1</a></li>");
            out.println("<li class='page-item'><a class='page-link' href='#'>2</a></li>");
            out.println("<li class='page-item'><a class='page-link' href='#'>3</a></li>");
            out.println("<li class='page-item'><a class='page-link' href='#'>4</a></li>");
            out.println("<li class='page-item'><a class='page-link' href='#'>5</a></li>");
            out.println("<li class='page-item'><a class='page-link' href='#'>6</a></li>");
            out.println("<li class='page-item'><a class='page-link' href='#'>7</a></li>");
            out.println("<li class='page-item'><a class='page-link' href='#'>8</a></li>");
            out.println("<li class='page-item'><a class='page-link' href='#'>9</a></li>");
            out.println("<li class='page-item'><a class='page-link' href='#'>10</a></li>");
            out.println("<li class='page-item'>");
            out.println("<a class='page-link' href='#' aria-label='Next'>");
            out.println("<span aria-hidden='true'>&raquo;</span>");
            out.println("<span class='sr-only'>Next</span>");
            out.println("</a>");
            out.println("</li>");
            out.println("</ul>");
            out.println("</nav> ");

            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
            out.println(data.footer);
        } catch (SQLException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            }
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
