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
import static java.lang.Integer.parseInt;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author julius
 */
public class VendRequest extends HttpServlet {

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
            } else {

                String fullname = (String) request.getSession().getAttribute("fullname");
                String clientId = (String) request.getSession().getAttribute("clientId");
                String clientCode = (String) request.getSession().getAttribute("clientCode");

                String groupId = "";
                if (null != request.getParameter("gid")) {
                    groupId = request.getParameter("gid");
                } else {
                    String query = "SELECT id from groups where clientId=" + clientId + " AND status=0 LIMIT 1";
                    try {
                        Connection con = data.connect();
                        Statement stm = con.createStatement();
                        ResultSet rs = stm.executeQuery(query);
                        while (rs.next()) {
                            groupId = rs.getString(1);
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(Groups.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                String group = data.getGroupById(groupId);
                String groupsplit[] = group.split(":");
                String groupName = groupsplit[2];
                String groupType = groupsplit[3];
                String groupStatus = groupsplit[4];

                Double floatBalance = Double.parseDouble(data.getFloatBal(parseInt(clientId)));
                Double groupAmount = data.getGroupSum(groupId);
                DecimalFormat formatter = new DecimalFormat("#,###.00");

                out.println(data.pageHeader(true, fullname));
                out.println("<section class='page-section about-heading'>");
                out.println("<div class='container'>");
                out.println("<div class='about-heading-content'>");
                out.println("<div class='row'>");
                out.println("<div class='col-xl-12 col-lg-12 mx-auto'>");
                out.println("<div class='bg-faded rounded p-5 mt-3'>");
                out.println("<h2 class='section-heading mb-4'>");
                out.println("<span class='section-heading-upper'>Bulk Vend<span class='float-right'>Float Balance: " + formatter.format(floatBalance) + "</span></span>");
                out.println("</h2>");
                out.println("<form action='VendRequest' method='POST'>");
                out.println("<input type='text' name='vend' style='display:none' />");
                out.println("<div class='form-group row'>");
                out.println("<label for='groupName' class='col-sm-1 col-form-label'>Group</label>");
                out.println("<div class='col-sm-5'>");
                out.println("<select class='custom-select mr-sm-2' name='gid' onchange=\"if (this.value) window.location.href='VendRequest?gid='+this.value\">");
                String selquery = "SELECT id,groupName, groupType, status from groups where clientId=" + clientId + " AND status=0 order by id desc";

                try {
                    Connection con = data.connect();
                    Statement stm = con.createStatement();
                    ResultSet rs = stm.executeQuery(selquery);
                    while (rs.next()) {
                        if (rs.getString(1).equals(groupId)) {
                            out.println("<option selected value='" + rs.getString(1) + "'>" + rs.getString(2) + "</option>");
                        } else {
                            out.println("<option value='" + rs.getString(1) + "'>" + rs.getString(2) + "</option>");
                        }
                    }
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Groups.class.getName()).log(Level.SEVERE, null, ex);
                }

                out.println(
                        "</select>");
                if (groupAmount > floatBalance) {
                    out.println("<span class='float-right text-danger'> Sum Vend Amount: " + formatter.format(groupAmount) + "<span> Balance after Vend: " + formatter.format(floatBalance - groupAmount) + "</span></span>");
                } else {
                    out.println("<span class='float-right'> Sum Vend Amount: " + formatter.format(groupAmount) + "<span> Balance after Vend: " + formatter.format(floatBalance - groupAmount) + "</span></span>");
                }

                out.println(
                        "</div>");
                out.println(
                        "</div>");
                if (groupAmount < floatBalance) {
                    out.println("<div class='form-group row'>");
                    out.println("<label for='groupName' class='col-sm-1 col-form-label'>&nbsp&nbsp&nbsp&nbsp</label>");
                    out.println("<div class='col-sm-5'>");
                    out.println("&nbsp&nbsp&nbsp&nbsp&nbsp<input type='checkbox' class='form-check-input' name='sendSMS'>");
                    out.println("<label class='form-check-label' for='sendSMS'>Send SMS</label>");
                    out.println("</div>");
                    out.println("</div>");
                    out.println("<div class='form-group row'>");
                    out.println("<div class='col-sm-10'>");
                    out.println("<button type='submit' value='submit' class='btn btn-primary'>Vend</button>");
                    out.println("</div>");
                    out.println("</div>");
                }

                out.println("</form>");
                out.println(
                        "<h2 class='section-heading mb-4'>");
                out.println("<span class='section-heading-lower text-center'> Vending " + groupType + "</span>");
                out.println("</h2>");
                out.println("<table class='table table-bordered'>");
                out.println("<thead class='thead-dark'>");
                out.println("<tr>");
                out.println("<th scope='col'>#</th>");
                out.println("<th scope='col'>Full Name</th>");
                out.println("<th scope='col'>Phone</th>");
                if (groupType.equals(
                        "Postpaid")) {
                    out.println("<th scope='col'>Account</th>");
                } else if (groupType.equals(
                        "Prepaid")) {
                    out.println("<th scope='col'>Meter</th>");
                } else {
                    out.println("<th scope='col'>Provider</th>");
                }

                out.println("<th scope='col'>Amount</th>");
                out.println("</tr>");
                out.println("</thead>");
                out.println("<tbody>");
                String limit = " limit 100000";
                String query = "SELECT id,fullName,phone,meter,amount,status from meters where status=0 AND groupId=" + groupId + " order by id desc" + limit + "";

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

                if (null != request.getParameter("vend")) {
                    Sell sell = new Sell();
                    boolean sms = false;
                    if (null != request.getParameter("sendSMS")) {
                        sms = true;
                    }
                    DateTime dt = new DateTime();
                    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy");
                    String year = fmt.print(dt);
                    DateTimeFormatter formatter2 = DateTimeFormat.forPattern("MMM");
                    String month = formatter2.print(dt);
                    DateTimeFormatter tformatter = DateTimeFormat.forPattern("hh");
                    String time = tformatter.print(dt);
                    DateTimeFormatter cfmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                    String cd = cfmt.print(dt);

                    String query2 = "SELECT id,fullName,phone,meter,amount,status from meters where status=0 AND groupId=" + groupId + "";
                    if (groupType.equals("Postpaid")) {
                        try {
                            Connection con = data.connect();
                            Statement stm = con.createStatement();
                            ResultSet rs = stm.executeQuery(query2);
                            while (rs.next()) {
                                String phn = rs.getString(3);
                                String meter = rs.getString(4);
                                String amt = rs.getString(5);
                                String msg = "Dear Customer,We are experiencing KPLC delays. Once the system is up we will resend your transaction. Queries call 0709711000";
                                String payres = sell.vendPostpaid(meter, amt, phn);
                                if (null != payres) {
                                    if (!payres.equals("FAIL")) {
                                        msg = payres;
                                        /*String pressplit[] = payres.split(":");
                                    msg = "PAID KSH:" + amt + " FOR KPLC ACCOUNT:" + meter + " RECEIPT:"
                                            + pressplit[3];*/
                                        if (sms) {
                                            Sdp sdp = new Sdp();
                                            try {
                                                sdp.sendSMS(phn, msg, "bulkvend", "704307", "http://197.248.9.105:8084/VendITNotification/SmsNotificationService");
                                            } catch (Exception sdex) {
                                            }
                                        }
                                    }
                                }
                                if(!msg.startsWith("Paid")){
                                    amt ="0";
                                }
                                String values = "insert into transactions" + month + year + "(clientId,clientCode,groupType,meter,amount,vendResponse) values (?,?,?,?,?,?)";
                                PreparedStatement prep = con.prepareStatement(values);
                                prep.setString(1, clientId);
                                prep.setString(2, clientCode);
                                prep.setString(3, groupType);
                                prep.setString(4, meter);
                                prep.setString(5, amt);
                                prep.setString(6, msg);
                                prep.execute();
                                prep.close();
                                
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(VendRequest.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else if (groupType.equals("Prepaid")) {
                        try {
                            Connection con = data.connect();
                            Statement stm = con.createStatement();
                            ResultSet rs = stm.executeQuery(query);
                            while (rs.next()) {
                                String phn = rs.getString(3);
                                String meter = rs.getString(4);
                                String amt = rs.getString(5);
                                String msg = "Dear Customer,We are experiencing KPLC delays. Once the system is up we will resend your transaction. Queries call 0709711000";
                                String payres = sell.vendPrepaid(meter, amt);
                                if (null != payres) {
                                    if (!payres.equals("FAIL")) {
                                        msg = payres;
                                        /*String pressplit[] = payres.split(":");
                                    msg = "KPLC Mtr No:" + meter + "\nToken:" + pressplit[1].replaceAll(" Units", "")
                                            + "\nUnits:" + pressplit[2].replaceAll(" Elec", "") + "\nAmount:" + amt + "(Elec:" + pressplit[3].replaceAll(" Charges", "") + " Other Charges:" + pressplit[4] + ")" + "\nDate:" + cd;*/
                                        if (sms) {
                                            Sdp sdp = new Sdp();
                                            try {
                                                sdp.sendSMS(phn, msg, "bulkvend", "704307", "http://197.248.9.105:8084/VendITNotification/SmsNotificationService");
                                            } catch (Exception sdex) {
                                            }
                                        }
                                    }
                                }                                
                                if(!msg.startsWith("Token")){
                                    amt ="0";
                                }
                                String values = "insert into transactions" + month + year + "(clientId,clientCode,groupType,meter,amount,vendResponse) values (?,?,?,?,?,?)";
                                PreparedStatement prep = con.prepareStatement(values);
                                prep.setString(1, clientId);
                                prep.setString(2, clientCode);
                                prep.setString(3, groupType);
                                prep.setString(4, meter);
                                prep.setString(5, amt);
                                prep.setString(6, msg);
                                prep.execute();
                                prep.close();
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(Groups.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        try {
                            Connection con = data.connect();
                            Statement stm = con.createStatement();
                            ResultSet rs = stm.executeQuery(query);
                            while (rs.next()) {
                                String phn = rs.getString(3);
                                String meter = rs.getString(5);
                                String amtsplit[] = rs.getString(5).split("_");
                                String amt = amtsplit[1];
                                String msg = "Dear Customer,We are experiencing KPLC delays. Once the system is up we will resend your transaction. Queries call 0709711000";
                                String payres = sell.vendAirtime(meter, amt);
                                if (null != payres) {
                                    if (!payres.equals("FAIL")) {
                                        String pressplit[] = payres.split(":");
                                        msg = payres;
                                        if (sms) {
                                            Sdp sdp = new Sdp();
                                            try {
                                                sdp.sendSMS(phn, msg, "bulkvend", "704307", "http://197.248.9.105:8084/VendITNotification/SmsNotificationService");
                                            } catch (Exception sdex) {
                                            }
                                        }
                                    }
                                }
                                if(!msg.startsWith("Token")){
                                    amt ="0";
                                }
                                String values = "insert into transactions" + month + year + "(clientId,clientCode,groupType,meter,amount,vendResponse) values (?,?,?,?,?,?)";
                                PreparedStatement prep = con.prepareStatement(values);
                                prep.setString(1, clientId);
                                prep.setString(2, clientCode);
                                prep.setString(3, groupType);
                                prep.setString(4, meter);
                                prep.setString(5, amt);
                                prep.setString(6, msg);
                                prep.execute();
                                prep.close();
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(Groups.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
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
