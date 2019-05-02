/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.kplcsms;

import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Integer.parseInt;
import java.sql.Connection;
import java.sql.ResultSet;
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
            out.println("<form action='" + request.getContextPath() + "/Home' method='post'>");
            out.println("<div class='input-group'>");
            out.println("<input type='text' class='form-control' id='msisdn' name='msi' placeholder='E.g 254720640926'>");
            out.println("<span class='input-group-btn'>");
            out.println("<button class='btn btn-primary' type='submit'>Go!</button>");
            out.println("</span>");
            out.println("</div>");
            out.println("</form>");
            out.println("</div>");
            out.println("<hr>");
            out.println("<a data-toggle='collapse' href='.smsid'>");
            out.println("<div class='smsid collapse in'>");
            out.println("<i class='fas fa-quidditch'></i> Query By SMS ID <i class='fas fa-angle-double-right pull-right'></i>");
            out.println("</div>");
            out.println("<div class='smsid collapse'>");
            out.println("<i class='fas fa-quidditch'></i> Query By SMS ID <i class='fas fa-angle-double-down pull-right'></i>");
            out.println("</div>");
            out.println("</a>");
            out.println("<div class='smsid collapse' style='padding-top:4%'>");
            out.println("<form action='" + request.getContextPath() + "/Home' method='post'>");
            out.println("<div class='input-group'>");
            out.println("<input type='text' class='form-control' id='smsid' name='smsid' placeholder='E.g 674967'>");
            out.println("<span class='input-group-btn'>");
            out.println("<button class='btn btn-primary' type='submit'>Go!</button>");
            out.println("</span>");
            out.println("</div>");
            out.println("</form>");
            out.println("</div>");
            out.println("<hr>");
            out.println("<a data-toggle='collapse' href='.daterange'>");
            out.println("<div class='daterange collapse in'>");
            out.println("<i class='fas fa-clock'></i> Query By Send Date Rage <i class='fas fa-angle-double-right pull-right'></i>");
            out.println("</div>");
            out.println("<div class='daterange collapse'>");
            out.println("<i class='fas fa-clock'></i> Query By Send Date Rage <i class='fas fa-angle-double-down pull-right'></i>");
            out.println("</div>");
            out.println("</a>");
            out.println("<div class='daterange collapse' style='padding-top:4%'>");
            out.println("<form action='" + request.getContextPath() + "/Home' method='post'>");
            out.println("<div class='input-group' style='margin-bottom:4%'>");
            out.println("<span class='input-group-addon' id='basic-addon1'>StartDate:</span>");
            out.println("<input type='text' class='datepicker-here' data-language='en' data-position='top left' data-date-format='yyyy-mm-dd' name='sdate' placeholder='" + placeholderdate + "' aria-describedby='basic-addon1' required>");
            out.println("</div>");
            out.println("<div class='input-group' style='margin-bottom:2%'>");
            out.println("<span class='input-group-addon' id='basic-addon1'>End  Date:</span>");
            out.println("<input type='text' class='datepicker-here' data-language='en' data-position='top left' data-date-format='yyyy-mm-dd' name='edate' placeholder='" + placeholderdate + "' aria-describedby='basic-addon1' required>");
            out.println("</div>");
            out.println("<button class='btn btn-primary' type='submit'  style='margin-left:80%'>Go!</button>");
            out.println("</form>");
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");

            String addedurl = "";
            String limit = " limit 5";
            int pageno = 1;
            int pages = 1;
            if (null != request.getParameter("page")) {
                pageno = parseInt(request.getParameter("page"));
                int sindex = (pageno * 5) - 4;
                limit = " limit " + sindex + ",5";
            }
            String query = "SELECT MOBILE,SMS_ID,DATE_DUE,MESSAGES,DELIVERY_STATUS,DATE_SENT from KPLCBILLS order by ID desc" + limit + "";

            if (null != request.getParameter("msi")) {
                String msisdn = request.getParameter("msi");
                query = "SELECT MOBILE,SMS_ID,DATE_DUE,MESSAGES,DELIVERY_STATUS,DATE_SENT from KPLCBILLS where MOBILE='" + msisdn + "' order by ID desc" + limit + "";
                addedurl = "&msi=" + msisdn + "";
            }
            if (null != request.getParameter("smsid")) {
                String smsid = request.getParameter("smsid");
                query = "SELECT MOBILE,SMS_ID,DATE_DUE,MESSAGES,DELIVERY_STATUS,DATE_SENT from KPLCBILLS where SMS_ID='" + smsid + "' order by ID desc" + limit + "";
                addedurl = "&smsid=" + smsid + "";
            }
            if (null != request.getParameter("sdate")) {
                String sdate = request.getParameter("sdate");
                String edate = request.getParameter("edate");
                query = "SELECT MOBILE,SMS_ID,DATE_DUE,MESSAGES,DELIVERY_STATUS,DATE_SENT from KPLCBILLS where DATE_SENT between '" + sdate + "' and '" + edate + "' order by ID desc" + limit + "";
                addedurl = "&sdate=" + sdate + "&edate=" + edate + "";
            }
            System.out.println(query);
            try {
                Statement rowstm = con.createStatement();
                ResultSet rowrs = rowstm.executeQuery(query.replaceAll(limit, " "));
                int numrows = 0;
                while (rowrs.next()) {
                    numrows++;
                }
                System.out.println("Rows:" + numrows);
                pages = numrows / 5;
                if ((numrows % 5) != 0) {
                    pages = pages + 1;
                }
            } catch (SQLException ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Pages:" + pages);
            out.println("<div class='col-lg-9'>");
            out.println("<div class='panel panel-primary  table-responsive'>");
            out.println("<div class='panel-heading'><span><i class='far fa-circle faa-burst animated'></i></span><span class='pull-right'><i class='fas fa-ellipsis-h'></i></span></div>");
            out.println("<div class='panel-body'>");

            out.println("<table class='table table-bordered' style='font-size:11px'>");
            out.println("<thead>");
            out.println("<tr>");
            out.println("<th>Mobile</th>");
            out.println("<th>SMS ID</th>");
            out.println("<th>Due Date</th>");
            out.println("<th>Message</th>");
            out.println("<th>Delivery Status</th>");
            out.println("<th>Date Sent</th>");
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
                out.println("<td>" + rs.getString(6) + "</td>");
                out.println("</tr>");
            }
            out.println("</tbody>");
            out.println("</table>");

            //PURE PAGENATION
            out.println("<nav aria-label='...' class='text-center' id='pngn'>");
            out.println("<ul class='pagination'>");
            if (pageno > 1) {
                out.println("<li class='page-item'>");
                out.println("<a class='page-link' href='" + request.getContextPath() + "/Home?page=1" + addedurl + "#pngn' aria-label='Previous'>");
                out.println("<span aria-hidden='true'><i class='fas fa-fast-backward'></i></span>");
                out.println("<span class='sr-only'>Previous</span>");
                out.println("</a>");
                out.println("</li>");
            } else {
                out.println("<li class='page-item disabled'>");
                out.println("<a class='page-link' href='' aria-label='Previous'>");
                out.println("<span aria-hidden='true'><i class='fas fa-fast-backward'></i></span>");
                out.println("<span class='sr-only'>Previous</span>");
                out.println("</a>");
                out.println("</li>");
            }
            if (pages <= 1) {
                for (int i = 1; i <= 10; i++) {
                    out.println("<li class='page-item'><a class='page-link' href='" + request.getContextPath() + "/Home?" + i + addedurl + "#pngn'>" + i + "</a></li>");
                }
            } else {
                if ((pageno - 4) < 1) {
                    for (int i = 1; i <= 10; i++) {
                        if (i == pageno) {
                            out.println("<li class='page-item active'><a class='page-link' href='" + request.getContextPath() + "/Home?page=" + i + addedurl + "#pngn'>" + i + "</a></li>");
                        } else {
                            out.println("<li class='page-item'><a class='page-link' href='" + request.getContextPath() + "/Home?page=" + i + addedurl + "#pngn'>" + i + "</a></li>");
                        }
                    }
                } else if ((pageno + 5) > pages) {
                    for (int i = pages - 9; i <= pages; i++) {
                        if (i == pageno) {
                            out.println("<li class='page-item active'><a class='page-link' href='" + request.getContextPath() + "/Home?page=" + i + addedurl + "#pngn'>" + i + "</a></li>");
                        } else {
                            out.println("<li class='page-item'><a class='page-link' href='" + request.getContextPath() + "/Home?page=" + i + addedurl + "#pngn'>" + i + "</a></li>");
                        }
                    }
                } else {
                    for (int i = pageno - 4; i <= pageno + 5; i++) {
                        if (i == pageno) {
                            out.println("<li class='page-item active'><a class='page-link' href='" + request.getContextPath() + "/Home?page=" + i + addedurl + "#pngn'>" + i + "</a></li>");
                        } else {
                            out.println("<li class='page-item'><a class='page-link' href='" + request.getContextPath() + "/Home?page=" + i + addedurl + "#pngn'>" + i + "</a></li>");
                        }
                    }
                }
            }
            if ((pageno + 1) > pages) {
                out.println("<li class='page-item disabled'>");
                out.println("<a class='page-link' href='' aria-label='Next'>");
                out.println("<span aria-hidden='true'><i class='fas fa-fast-forward'></i></span>");
                out.println("<span class='sr-only'>Next</span>");
                out.println("</a>");
                out.println("</li>");
            } else {
                out.println("<li class='page-item'>");
                out.println("<a class='page-link' href='" + request.getContextPath() + "/Home?page=" + pages + addedurl + "#pngn' aria-label='Next'>");
                out.println("<span aria-hidden='true'><i class='fas fa-fast-forward'></i></span>");
                out.println("<span class='sr-only'>Next</span>");
                out.println("</a>");
                out.println("</li>");
            }
            out.println("</ul>");
            out.println("</nav> ");
            //END PAGENATION

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
