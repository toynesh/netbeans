/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdslvending.portal;

import java.io.IOException;
import java.io.PrintWriter;
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
import javax.servlet.http.HttpSession;

/**
 *
 * @author coolie
 */
public class Admin extends HttpServlet {

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
            /* TODO output your page here. You may use following sample code. */
            HttpSession session = request.getSession();
            if (null == session.getAttribute("vendorcode")) {
                String redirectURL = "Login";
                response.sendRedirect(redirectURL);
            }

            String fullname = (String) request.getSession().getAttribute("fullname");
            
            String reports = "<div class='col-lg-3  text-right'>"
                    + "<span><a href='#'><i class='fa fa-area-chart' aria-hidden='true'></i> Reports || Code:&nbsp</a></span>"
                    + "<span><select onchange=\"document.location.href='" + request.getContextPath() + "/AdminReports?vendorcode='+this.value\" style='text-align-last: right;'>"
                    + data.vendorlist()+ "</select></span>"
                    + "</div>";
            
            String useraction = "<div class='col-lg-3  text-right'>"
                    + "<select onchange='useraction(this);'  class='form-control' style='text-align-last: right;'>"
                    + "<option dir='rtl'>" + fullname + " </option>"
                    + "<option dir='rtl'>Change Password</option>"
                    + "<option dir='rtl' value='logout'>Logout</option>"
                    + "</select> "
                    + "</div>";

            String edit = "no";
            String vendorname = "";
            String vendorcode = "";
            String prepaid = "";
            String postpaid = "";
            String airtime = "";
            String mode = "";
            try {
                if (request.getParameter("edit").equals("yes")) {
                    edit = "yes";
                    vendorname = request.getParameter("vname");
                    vendorcode = request.getParameter("vcode");
                    prepaid = request.getParameter("prepaid");
                    postpaid = request.getParameter("postpaid");
                    airtime = request.getParameter("airtime");
                    mode = request.getParameter("status");
                }
            } catch (Exception e) {

            }
            out.println(data.headerP1 +reports+useraction + data.headerP2);
            out.println("<div class='container' style='margin-top:2%'>");
            out.println("<div class='row'>");
            out.println("<div class='col-lg-4'>");
            out.println("<div id='addvendor'>");
            out.println("<h5><i class='fa fa-pencil-square-o' aria-hidden='true'></i>&nbsp;&nbsp;<u> Add/Edit</u></h5>");
            out.println("<br />");
            out.println("<form action='/pdslvendingportal/SaveVendor' method='post'>");
            out.println("<div class='form-group'>");
            out.println("<label for='vendorname'>Name:</label>");
            out.println("<input type='text' class='form-control' id='vendorname' name='vendorname' value='" + vendorname + "' required>");
            out.println("<input type='text' style='display:none'  id='edit' name='edit' value='" + edit + "'>");
            out.println("</div>");
            out.println("<div class='form-group'>");
            out.println("<label for='vendorcode'>Code:</label>");
            out.println("<input type='text' class='form-control' style='width:50%' id='vendorcode' value='" + vendorcode + "' name='vendorcode' required>");
            out.println("</div>");
            out.println("<div class='form-group'>");
            out.println("<label for='newfloat'>New Float Amt:</label>");
            out.println("<input type='text' class='form-control' style='width:50%' id='newfloat' name='newfloat'>");
            out.println("</div>");
            out.println("<div class='form-group'>");
            out.println("<label for='floatref'>Reference</label>");
            out.println("<input type='text' class='form-control' style='width:50%' id='floatref' name='floatref' required>");
            out.println("</div>");
            out.println("<br />");
            out.println("<div class='radio'>");
            if (mode.equals("test")) {
                out.println("<label><input type='radio' name='mode' value='test' checked>Test Mode</label>");
            } else {
                out.println("<label><input type='radio' name='mode' value='test'>Test Mode</label>");
            }
            out.println("</div>");
            out.println("<div class='radio'>");
            if (mode.equals("live")) {
                out.println("<label><input type='radio' name='mode' value='live' checked>Live Mode</label>");
            } else {
                out.println("<label><input type='radio' name='mode' value='live'>Live Mode</label>");
            }
            out.println("</div>");
            out.println("<div class='radio disabled'>");
            if (mode.equals("disabled")) {
                out.println("<label><input type='radio' name='mode' value='disabled' checked>Disabled Mode</label>");
            } else {
                out.println("<label><input type='radio' name='mode' value='disabled'>Disabled Mode</label>");
            }
            out.println("</div>");
            out.println("<hr />");
            out.println("<div class='checkbox-inline'>");
            if (prepaid.equals("yes")) {
                out.println("<label><input type='checkbox' name='prepaid' value='yes' checked>PrePaid</label>");
            } else {
                out.println("<label><input type='checkbox' name='prepaid' value='yes'>PrePaid</label>");
            }
            out.println("</div>");
            out.println("<div class='checkbox-inline'>");
            if (postpaid.equals("yes")) {
                out.println("<label><input type='checkbox' name='postpaid' value='yes' checked>PostPaid</label>");
            } else {
                out.println("<label><input type='checkbox' name='postpaid' value='yes'>PostPaid</label>");
            }
            out.println("</div>");
            out.println("<div class='checkbox-inline disabled'>");
            if (airtime.equals("yes")) {
                out.println("<label><input type='checkbox' name='airtime' value='yes' checked>Airtime</label>");
            } else {
                out.println("<label><input type='checkbox' name='airtime' value='yes'>Airtime</label>");
            }
            out.println("</div>");
            out.println("<hr />");
            out.println("<button type='submit' class='btn btn-primary'>Save</button>");
            out.println("</form>");
            out.println("</div>");
            out.println("</div>");
            out.println("<div class='col-lg-8 table-responsive'>");
            out.println("<h5><i class='fa fa-bars' aria-hidden='true'></i>&nbsp;&nbsp;<u> All Sub-Aggregators</u></h5>");
            out.println("<table class='table table-bordered'>");
            out.println("<thead>");
            out.println("<tr>");
            out.println("<th>Name</th>");
            out.println("<th>Code</th>");
            out.println("<th class='text-center'>Float Balance</th>");
            out.println("<th class='text-center'>Commission (CurrMonth)</th>");
            out.println("<th class='text-center'>Status</th>");
            out.println("<th class='text-center'>VendsAllowed</th>");
            out.println("<th class='text-center'>Action</th>");
            out.println("</tr>");
            out.println("</thead>");
            out.println("<tbody style='color:black'>");
            Connection con = data.connect();
            try {
                String query = "SELECT vendor_name,vendor_code,prepaid,postpaid,airtime,status from vendors";
                Statement stm = con.createStatement();
                ResultSet rs = stm.executeQuery(query);
                while (rs.next()) {
                    out.println("<tr>");
                    out.println("<td>" + rs.getString(1) + "</td>");
                    out.println("<td>" + rs.getString(2) + "</td>");
                    String fb = data.getFloatBal(rs.getString(2));
                    double floatbal = Math.round(Double.parseDouble(fb) * 100.0) / 100.0;
                    String comm = data.getCurrentMonthCommission(rs.getString(2));
                    double commission = Math.round(Double.parseDouble(comm) * 100.0) / 100.0;
                    out.println("<td class='text-center'>" + floatbal + "</td>");
                    out.println("<td class=' text-center text-danger'>" +commission+ "</td>");
                    out.println("<td class='text-center warning'>" + rs.getString(6) + "</td>");
                    String allowedvends = "";
                    if (rs.getString(3).equals("yes")) {
                        allowedvends = allowedvends + "PrePaid";
                    }
                    if (rs.getString(4).equals("yes")) {
                        allowedvends = allowedvends + ",PostPaid";
                    }
                    if (rs.getString(5).equals("yes")) {
                        allowedvends = allowedvends + ",Airtime";
                    }
                    out.println("<td class='text-center text-success' style='font-size:12px'>" + allowedvends + "</td>");
                    out.println("<td class='text-center'><a href='/pdslvendingportal/Admin?edit=yes&vname=" + rs.getString(1) + "&vcode=" + rs.getString(2) + "&prepaid=" + rs.getString(3) + "&postpaid=" + rs.getString(4) + "&airtime=" + rs.getString(5) + "&status=" + rs.getString(6) + "'><u>Edit</u></a></td>");
                    out.println("</tr>");
                }
            } catch (SQLException ex) {
                Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
            }
            out.println("</tbody>");
            out.println("</table>");
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
            out.println(data.footer);
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
