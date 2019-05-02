/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.sms;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
            String clientname = "";
            String shortcode = "";
            String masking = "";
            String clientid = "0";
            if (null != request.getParameter("cid")) {
                clientid = request.getParameter("cid");
            }
            if (null != request.getParameter("cname")) {
                data.createTables();
                clientname = request.getParameter("cname");
                shortcode = request.getParameter("scode");
                masking = request.getParameter("mask");
                if (null != request.getParameter("edit") && !clientid.equals("0")) {
                    try {
                        Connection con = data.connect();
                        String update = "update pdslSMSclients set accesscode = ?,cname = ?,masking = ? WHERE id = ?";
                        PreparedStatement preparedStmt = con.prepareStatement(update);
                        preparedStmt.setString(1, shortcode);
                        preparedStmt.setString(2, clientname);
                        preparedStmt.setString(3, masking);
                        preparedStmt.setInt(4, Integer.parseInt(clientid));
                        preparedStmt.executeUpdate();
                        con.close();
                        System.out.println("Updated Clients table");
                    } catch (SQLException ex) {
                        Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    String values = "insert into pdslSMSclients(accesscode,cname,masking) values (?,?,?)";
                    try {
                        Connection con = data.connect();
                        PreparedStatement prep = con.prepareStatement(values);
                        prep.setString(1, shortcode);
                        prep.setString(2, clientname);
                        prep.setString(3, masking);
                        prep.execute();
                        prep.close();
                        con.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            out.println(data.head("Admin"));
            out.println("<div class='col-sm-10'>");
            out.println("<div class='row' style='height:92%'>");
            out.println("<div class='col-sm-12'>");
            out.println("<div class='row' style='margin-top:10%'>");
            out.println("<div class='col-md-5 offset-md-2'>");
            out.println("<label for='' class='pdslcolor' style='font-family: \"Poiret One\", cursive;'><h4><strong><i class='fas fa-cogs faa-wrench animated'></i> Manage Clients</strong></h4></label>");
            out.println("</div>");
            out.println("</div>");
            out.println("<div class='row'>");
            out.println("<div class='col-md-5 offset-md-2'>");
            out.println("<ul class='nav nav-tabs' role='tablist'>");
            out.println("<li class='nav-item active'>");
            out.println("<a class='nav-link active btn btn-primary mr-1' href='#new' role='tab' data-toggle='tab'>New</a>");
            out.println("</li>");
            out.println("<li class='nav-item'>");
            out.println("<a class='nav-link btn btn-secondary' href='#edit' role='tab' data-toggle='tab' id='cedit'>Edit</a>");
            out.println("</li>");
            out.println("</ul>");
            out.println("</div>");
            out.println("</div>");
            out.println("<!-- Tab panes -->");
            out.println("<div class='tab-content'>");
            out.println("<div role='tabpanel' class='tab-pane active in' id='new'>");
            out.println("<form action='Admin' method='post' class='mt-2'>");
            out.println("<div class='row'>");
            out.println("<div class='col-md-5 offset-md-2'>");
            out.println("<label for='cname' class='pdslcolor mt-3' style='font-family: \"Poiret One\", cursive;'><strong>Client Name</strong></label>");
            out.println("<input type='text' name='cname' class='form-control'>");
            out.println("</div>");
            out.println("</div>");
            out.println("<div class='row mt-2'>");
            out.println("<div class='col-md-5 offset-md-2'>");
            out.println("<label for='scode' class='pdslcolor' style='font-family: \"Poiret One\", cursive;'><strong>AccessCode</strong></label>");
            out.println("<input type='text' name='scode' class='form-control'>");
            out.println("</div>");
            out.println("</div>");
            out.println("<div class='row mt-2'>");
            out.println("<div class='col-md-5 offset-md-2'>");
            out.println("<label for='mask' class='pdslcolor' style='font-family: \"Poiret One\", cursive;'><strong>Masking</strong></label>");
            out.println("<input type='text' name='mask' class='form-control'>");
            out.println("</div>");
            out.println("</div>");
            out.println("<div class='row mt-2'>");
            out.println("<div class='col-md-5 offset-md-2'>");
            out.println("<button type='submit' class='btn btn-primary'><strong> SAVE </strong></button>");
            out.println("</div>");
            out.println("</div>");
            out.println("</form>");
            out.println("</div>");
            out.println("<div role='tabpanel' class='tab-pane fade' id='edit'>");
            out.println("<form action='Admin' method='post' class='mt-2'>");
            out.println("<div class='row'>");
            out.println("<div class='col-md-5 offset-md-2'>");
            out.println("<select onchange=\"document.location.href='" + request.getContextPath() + "/Admin?cid='+this.value\" class='custom-select custom-select-md mt-3'>");
            if (!clientid.equals("0")) {
                out.println("<option value='0'>Select Client</option>");
            } else {
                out.println("<option selected value='0'>Select Client</option>");
            }
            List<List<String>> clients = data.getClients();
            String ecname = "";
            String escode = "";
            String emask = "";
            for (int y = 0; y < clients.size(); y++) {
                if (clientid.equals(clients.get(y).get(0))) {
                    out.println("<option selected value='" + clients.get(y).get(0) + "'>" + clients.get(y).get(2) + "</option>");
                    ecname = clients.get(y).get(2);
                    escode = clients.get(y).get(1);
                    emask = clients.get(y).get(3);
                } else {
                    out.println("<option value='" + clients.get(y).get(0) + "'>" + clients.get(y).get(2) + "</option>");
                }
            }
            out.println("</select>");
            out.println("</div>");
            out.println("</div>");
            out.println("<div class='row mt-2'>");
            out.println("<div class='col-md-5 offset-md-2'>");
            out.println("<label for='cname' class='pdslcolor' style='font-family: \"Poiret One\", cursive;'><strong>Client Name</strong></label>");
            out.println("<input type='text' name='cname' value='"+ecname+"' class='form-control'>");
            out.println("</div>");
            out.println("</div>");
            out.println("<div class='row mt-2'>");
            out.println("<div class='col-md-5 offset-md-2'>");
            out.println("<label for='scode' class='pdslcolor' style='font-family: \"Poiret One\", cursive;'><strong>AccessCode</strong></label>");
            out.println("<input type='text' name='scode' value='"+escode+"' class='form-control'>");
            out.println("</div>");
            out.println("</div>");
            out.println("<div class='row mt-2'>");
            out.println("<div class='col-md-5 offset-md-2'>");
            out.println("<label for='mask' class='pdslcolor' style='font-family: \"Poiret One\", cursive;'><strong>Masking</strong></label>");
            out.println("<input type='text' name='mask'  value='"+emask+"' class='form-control'>");
            out.println("<input type='text' name='cid' value='"+clientid+"' style='display:none'>");
            out.println("</div>");
            out.println("</div>");
            out.println("<div class='row mt-2 mb-2'>");
            out.println("<div class='col-md-5 offset-md-2'>");
            out.println("<button type='submit' name='edit' class='btn btn-primary'><strong> SAVE </strong></button>");
            out.println("</div>");
            out.println("</div>");
            out.println("</form>");
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
            if (!clientid.equals("0")) {
                out.println("<script>");
                out.println("window.onload = function() {");
                out.println("var cedit = document.getElementById('cedit');");
                out.println("cedit.click();");
                out.println("};");
                out.println("</script>");
            }
            out.println("<div class='row bg-secondary' style=';height:8%'>");
            out.println("<div class='col-sm-12'>");
            DateTime cyr = new DateTime();
            DateTimeFormatter yfm = DateTimeFormat.forPattern("yyyy");
            String yr = yfm.print(cyr);
            out.println("<p class='mt-3 text-center text-white'>&copy; " + yr + " by Professional Digital Systems Ltd.</p>");
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
            out.println(data.foot("Admin"));
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
