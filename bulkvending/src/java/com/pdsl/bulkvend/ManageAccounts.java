/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.bulkvend;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

/**
 *
 * @author julius
 */
@WebServlet("/upload")
@MultipartConfig
public class ManageAccounts extends HttpServlet {

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
            if (null == session.getAttribute("clientCode")) {
                String redirectURL = "Home?login";
                response.sendRedirect(redirectURL);
            }
            String groupId = "";
            if (null != request.getParameter("gid")) {
                groupId = request.getParameter("gid");
            } else {
                String redirectURL = "Groups";
                response.sendRedirect(redirectURL);
            }
            //System.out.println("GroupId:" + groupId);

            if (null != request.getParameter("idd")) {
                String meterId = request.getParameter("idd");
                String query = "SELECT id, status from meters where id=" + meterId + " ";
                int status = 0;
                try {
                    Connection con = this.data.connect();
                    //System.out.println(query);
                    Statement stm = con.createStatement();
                    ResultSet rs = stm.executeQuery(query);
                    while (rs.next()) {
                        status = rs.getInt(2);
                    }
                    con.close();
                } catch (SQLException ex) {
                    //Logger.getLogger(Groups.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (status == 0) {
                    try {
                        Connection con = this.data.connect();
                        String update = "update meters set status = ? WHERE id = ?";
                        PreparedStatement preparedStmt = con.prepareStatement(update);
                        preparedStmt.setInt(1, 1);
                        preparedStmt.setString(2, meterId);
                        preparedStmt.executeUpdate();
                        System.out.println("Updated record status to disabled");
                    } catch (SQLException ex) {
                        //Logger.getLogger(Groups.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    String redirectURL = "ManageAccounts?gid=" + groupId;
                    response.sendRedirect(redirectURL);
                } else {
                    try {
                        Connection con = this.data.connect();
                        String update = "update meters set status = ? WHERE id = ?";
                        PreparedStatement preparedStmt = con.prepareStatement(update);
                        preparedStmt.setInt(1, 0);
                        preparedStmt.setString(2, meterId);
                        preparedStmt.executeUpdate();
                        System.out.println("Updated record status to enable");
                    } catch (SQLException ex) {
                        //Logger.getLogger(Groups.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    String redirectURL = "ManageAccounts?gid=" + groupId;
                    response.sendRedirect(redirectURL);
                }
            }

            String fullname = (String) request.getSession().getAttribute("fullname");
            String clientId = (String) request.getSession().getAttribute("clientId");
            String clientCode = (String) request.getSession().getAttribute("clientCode");

            String group = data.getGroupById(groupId);
            String groupsplit[] = group.split(":");
            String groupName = groupsplit[2];
            String groupType = groupsplit[3];
            String groupStatus = groupsplit[4];
            if (null != request.getParameter("accountslist")) {
                Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
                String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
                InputStream fileContent = filePart.getInputStream();

                System.out.println("Uploaded file:" + fileName);
                String hpath = System.getProperty("user.home");
                System.out.println("Home Path:" + hpath);

                for (Part part : request.getParts()) {
                    if (part != null && part.getSize() > 0) {
                        String contentType = part.getContentType();
                        System.out.println("Content-Type:" + contentType);
                        /*if (!contentType.equalsIgnoreCase("text/csv")) {
                            out.println("<h3 style='color:red'>Please Upload a a valid csv File !!</h3>");
                            continue;
                        }*/
                        part.write(hpath + "/" + fileName);
                    }
                }
                String csvFile = hpath + "/" + fileName;
                BufferedReader cbr = null;
                BufferedReader br = null;
                String cline = "";
                String line = "";
                String cvsSplitBy = ",";
                try {
                    Connection con = data.connect();
                    cbr = new BufferedReader(new FileReader(csvFile));
                    int om = 0;
                    while ((cline = cbr.readLine()) != null) {
                        if (om > 0) {
                            String[] cells = cline.split(cvsSplitBy);
                            String cname = cells[0];
                            String phone = cells[1];
                            String meter = cells[2];
                            String amount = cells[3];
                            String values = "insert into meters(clientCode,groupId,groupName,groupType,fullName,phone,meter,amount) values (?,?,?,?,?,?,?,?)";
                            try {
                                PreparedStatement prep = con.prepareStatement(values);
                                prep.setString(1, clientCode);
                                prep.setString(2, groupId);
                                prep.setString(3, groupName);
                                prep.setString(4, groupType);
                                prep.setString(5, cname);
                                prep.setString(6, phone);
                                prep.setString(7, meter);
                                prep.setString(8, amount);
                                prep.execute();
                                prep.close();
                            } catch (SQLException ex) {
                                Logger.getLogger(Groups.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        om++;
                    }
                    con.close();
                } catch (FileNotFoundException e) {
                    //e.printStackTrace();
                    System.out.println("Invalid or No file Uploaded");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SQLException ex) {
                    Logger.getLogger(ManageAccounts.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                String redirectURL = "ManageAccounts?gid=" + groupId;
                response.sendRedirect(redirectURL);
            }

            out.println(data.pageHeader(true, fullname));
            out.println("<section class='page-section about-heading'>");
            out.println("<div class='container'>");
            out.println("<div class='about-heading-content'>");
            out.println("<div class='row'>");
            out.println("<div class='col-xl-12 col-lg-12 mx-auto'>");
            out.println("<div class='bg-faded rounded p-5 mt-3'>");
            out.println("<h2 class='section-heading mb-4'>");
            out.println("<span class='section-heading-upper'>Manage " + groupName + "</span>");
            out.println("</h2>");
            out.println("<form action='ManageAccounts' method='post' enctype='multipart/form-data'>");
            out.println("<input type='text' name='accountslist' style='display:none' />");
            out.println("<input type='text' name='gid' value='" + groupId + "' style='display:none' />");
            out.println("<div class='form-group row'>");
            String downloadFile = "#";
            String title = "";
            boolean tokentype = false;
            if (groupType.equals("Postpaid")) {
                out.println("<label for='groupName' class='col-sm-2 col-form-label'>Accounts</label>");
                downloadFile = "Postpaid.csv";
                title = "Postpaid Accounts";
            } else if (groupType.equals("Prepaid")) {
                out.println("<label for='groupName' class='col-sm-2 col-form-label'>Meters</label>");
                downloadFile = "Prepaid.csv";
                title = "Prepaid Meters";
            } else {
                out.println("<label for='groupName' class='col-sm-2 col-form-label'>Phone Numbers</label>");
                downloadFile = "Airtime.csv";
                title = "Phone Numbers";
                tokentype = true;
            }
            out.println("<div class='col-sm-6'>");
            out.println("<input type='file' class='form-control-file' name='file'>");
            out.println("<small id='listHelp' class='form-text text-muted'>NB: The systems only accepts data in this format: <a href='DownloadFile?ftdown=" + downloadFile + "'>>>Download File Format</a></small>");
            if (tokentype) {
                out.println("<small id='ttypeHelp' class='form-text text-muted'>Download: <a href='DownloadFile?ftdown=AirtimeTokenTypes.pdf'>>>Airtime Token Types</a></small>");
            }
            out.println("</div>");
            out.println("</div>");
            out.println("<div class='form-group row'>");
            out.println("<div class='col-sm-10'>");
            out.println("<button type='submit' value='save' class='btn btn-primary'>Upload</button>");
            out.println("</div>");
            out.println("</div>");
            out.println("</form>");
            out.println("<h2 class='section-heading mb-4'>");
            out.println("<span class='section-heading-lower text-center'>" + title + "</span>");
            out.println("</h2>");
            out.println("<table class='table table-bordered'>");
            out.println("<thead class='thead-dark'>");
            out.println("<tr>");
            out.println("<th scope='col'>#</th>");
            out.println("<th scope='col'>Full Name</th>");
            out.println("<th scope='col'>Phone</th>");
            if (groupType.equals("Postpaid")) {
                out.println("<th scope='col'>Account</th>");
            } else if (groupType.equals("Prepaid")) {
                out.println("<th scope='col'>Meter</th>");
            } else {
                out.println("<th scope='col'>Provider</th>");
            }
            out.println("<th scope='col'>Amount</th>");
            out.println("<th scope='col'>Action</th>");
            out.println("</tr>");
            out.println("</thead>");
            out.println("<tbody>");
            String limit = " limit 100000";
            String query = "SELECT id,fullName,phone,meter,amount,status from meters where groupId=" + groupId + " order by id desc" + limit + "";
            try {
                Connection con = data.connect();
                Statement stm = con.createStatement();
                ResultSet rs = stm.executeQuery(query);
                int count = 1;
                while (rs.next()) {
                    out.println("<tr>");
                    out.println("<th scope='row'>" + count + "</th>");
                    out.println("<td>" + rs.getString(2) + "</td>");
                    out.println("<td>" + rs.getString(3) + "</td>");
                    out.println("<td>" + rs.getString(4) + "</td>");
                    out.println("<td>" + rs.getString(5) + "</td>");
                    out.println("<td>");
                    out.println("<div class='list-group list-group-horizontal-sm'>");
                    if (rs.getInt(6) == 0) {
                        out.println("<a href='ManageAccounts?gid=" + groupId + "&idd=" + rs.getString(1) + "' class='list-group-item list-group-item-action list-group-item-warning'>");
                        out.println("Click to Disable");
                    } else {
                        out.println("<a href='ManageAccounts?gid=" + groupId + "&idd=" + rs.getString(1) + "' class='list-group-item list-group-item-action list-group-item-info'>");
                        out.println("Click to enable");
                    }
                    out.println("</a>");
                    out.println("</div>");
                    out.println("</td>");
                    out.println("</tr>");
                    count++;
                }
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(Groups.class.getName()).log(Level.SEVERE, null, ex);
            }
            out.println("</tbody>");
            out.println("</table>");
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
            out.println("</section>");
            out.println(data.pageFoorter(""));
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
