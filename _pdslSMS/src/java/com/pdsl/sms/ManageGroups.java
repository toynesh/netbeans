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
import java.util.ArrayList;
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
public class ManageGroups extends HttpServlet {

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
            
            String clientid = "0";
            String groupid = "0";
            String groupname = "";
            String contacts = "";
            if (null != request.getParameter("cid")) {
                clientid = request.getParameter("cid");
            }
            if (null != request.getParameter("gid")) {
                groupid = request.getParameter("gid");
            }
            if (null != request.getParameter("gname")) {
                data.createTables();
                groupname = request.getParameter("gname");
                contacts = request.getParameter("contacts");
                if (null != request.getParameter("edit") && !groupid.equals("0")) {
                    try {
                        Connection con = data.connect();
                        String update = "update pdslSMSgroups set clientid = ?,groupname = ?,contacts = ? WHERE id = ?";
                        PreparedStatement preparedStmt = con.prepareStatement(update);
                        preparedStmt.setString(1, clientid);
                        preparedStmt.setString(2, groupname);
                        preparedStmt.setString(3, contacts);
                        preparedStmt.setInt(4, Integer.parseInt(groupid));
                        preparedStmt.executeUpdate();
                        con.close();
                        System.out.println("Updated Groups table");
                    } catch (SQLException ex) {
                        Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    String values = "insert into pdslSMSgroups(clientid,groupname,contacts) values (?,?,?)";
                    try {
                        Connection con = data.connect();
                        PreparedStatement prep = con.prepareStatement(values);
                        prep.setString(1, clientid);
                        prep.setString(2, groupname);
                        prep.setString(3, contacts);
                        prep.execute();
                        prep.close();
                        con.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            out.println(data.head("ManageGroups"));
            out.println("<div class='col-sm-10'>");
            out.println("<div class='row' style='height:92%'>");
            out.println("<div class='col-sm-12'>");
            out.println("<div class='row' style='margin-top:10%'>");
            out.println("<div class='col-md-5 offset-md-2'>");
            out.println("<label for='' class='pdslcolor' style='font-family: \"Poiret One\", cursive;'><h4><strong><i class='fas fa-cogs faa-wrench animated'></i> Manage Groups</strong></h4></label>");
            out.println("</div>");
            out.println("</div>");
            out.println("<div class='row'>");
            out.println("<div class='col-md-5 offset-md-2'>");
            out.println("<ul class='nav nav-tabs' role='tablist'>");
            out.println("<li class='nav-item active'>");
            out.println("<a class='nav-link active btn btn-primary mr-1' href='#new' role='tab' data-toggle='tab'>New</a>");
            out.println("</li>");
            out.println("<li class='nav-item'>");
            out.println("<a class='nav-link btn btn-secondary' href='#edit' role='tab' data-toggle='tab' id='gedit'>Edit</a>");
            out.println("</li>");
            out.println("</ul>");
            out.println("</div>");
            out.println("</div>");
            out.println("<!-- Tab panes -->");
            out.println("<div class='tab-content'>");
            out.println("<div role='tabpanel' class='tab-pane active in' id='new'>");
            out.println("<form action='ManageGroups' method='post' class='mt-2'>");
            out.println("<div class='row mt-2'>");
            out.println("<div class='col-md-5 offset-md-2'>");
            out.println("<select onchange=\"document.location.href='" + request.getContextPath() + "/ManageGroups?cid='+this.value\" class='custom-select custom-select-md mt-3'>");
            if (!clientid.equals("0")) {
                out.println("<option value='0'>Select Client</option>");
            } else {
                out.println("<option selected value='0'>Select Client</option>");
            }
            List<List<String>> clients = data.getClients();
            for (int y = 0; y < clients.size(); y++) {
                if (clientid.equals(clients.get(y).get(0))) {
                    out.println("<option selected value='" + clients.get(y).get(0) + "'>" + clients.get(y).get(2) + "</option>");
                } else {
                    out.println("<option value='" + clients.get(y).get(0) + "'>" + clients.get(y).get(2) + "</option>");
                }
            }
            out.println("</select>");
            out.println("</div>");
            out.println("</div>");
            out.println("<div class='row'>");
            out.println("<div class='col-md-5 offset-md-2'>");
            out.println("<label for='gname' class='pdslcolor mt-3' style='font-family: \"Poiret One\", cursive;'><strong>Group Name</strong></label>");
            out.println("<input type='text' name='gname' class='form-control'>");
            out.println("<input type='text' name='cid' value='"+clientid+"' style='display:none'>");
            out.println("</div>");
            out.println("</div>");
            out.println("<div class='row mt-4'>");
            out.println("<div class='col-md-5 offset-md-2'>");
            out.println("<label for='contacts' class='pdslcolor'><strong>Paste Contacts like (Phone:Name,)</strong></label>");
            out.println("<textarea class='form-control' name='contacts' id='contacts' rows='6'>254709711000:Professional Digital Systems,\n254709711000:Professional Digital Systems,</textarea>");
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
            out.println("<form action='ManageGroups' method='post' class='mt-2'>");
            out.println("<div class='row'>");
            out.println("<div class='col-md-5 offset-md-2'>");
            out.println("<select onchange=\"document.location.href='" + request.getContextPath() + "/ManageGroups?gid='+this.value\" class='custom-select custom-select-md mt-3'>");
            if (!groupid.equals("0")) {
                out.println("<option value='0'>Select Group</option>");
            } else {
                out.println("<option selected value='0'>Select Group</option>");
            }
            List<List<String>> groups = data.getGroups();
            for (int y = 0; y < groups.size(); y++) {
                if (groupid.equals(groups.get(y).get(0))) {
                    clientid = groups.get(y).get(1);
                    contacts = groups.get(y).get(3);
                    groupname = groups.get(y).get(2);
                    out.println("<option selected value='" + groups.get(y).get(0) + "'>" + groups.get(y).get(2) + "</option>");
                } else {
                    out.println("<option value='" + groups.get(y).get(0) + "'>" + groups.get(y).get(2) + "</option>");
                }
            }
            out.println("</select>");
            out.println("</div>");
            out.println("</div>");            
            out.println("<div class='row'>");
            out.println("<div class='col-md-5 offset-md-2'>");
            out.println("<label for='cname' class='pdslcolor mt-3' style='font-family: \"Poiret One\", cursive;'><strong>Client Name</strong></label>");
            if(!clientid.equals("0")){
            List<String> client = data.getClientById(clientid);
            out.println("<input type='text' name='cname' value='"+client.get(2)+"' class='form-control' disabled>");
            }else{
               out.println("<input type='text' name='cname' value='Select a Group' class='form-control' disabled>"); 
            }
            out.println("</div>");
            out.println("</div>");
            out.println("<div class='row mt-2'>");
            out.println("<div class='col-md-5 offset-md-2'>");
            out.println("<label for='mask' class='pdslcolor'><strong>Paste Contacts like (Phone:Name,)</strong></label>");
            out.println("<textarea class='form-control' name='contacts' id='contacts' rows='6'>"+contacts+"</textarea>");
            out.println("<input type='text' name='gid' value='"+groupid+"' style='display:none'>");
            out.println("<input type='text' name='cid' value='"+clientid+"' style='display:none'>");
            out.println("<input type='text' name='gname' value='"+groupname+"' style='display:none'>");
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
            if (!groupid.equals("0")) {
                out.println("<script>");
                out.println("window.onload = function() {");
                out.println("var gedit = document.getElementById('gedit');");
                out.println("gedit.click();");
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
            out.println(data.foot("ManageGroups"));
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
