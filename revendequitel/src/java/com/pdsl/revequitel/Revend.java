/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.revequitel;

import java.io.IOException;
import java.io.PrintWriter;
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
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author coolie
 */
public class Revend extends HttpServlet {

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
        String phn = "none";
        try {
            /* TODO output your page here. You may use following sample code. */
            if (null != request.getParameter("tid")) {
                String id = request.getParameter("tid");
                if (null != request.getParameter("meter") || !request.getParameter("meter").equals("")) {
                    String meter = request.getParameter("meter");
                    Connection con = data.connect();
                    try {
                        String query = "SELECT `equityid`,`billNumber`,`billAmount`,`balance`,`phoneNumber`,`bankreference`,`message`  FROM `equity` WHERE `equityid`=" + id + "";
                        Statement st = con.createStatement();
                        ResultSet rs = st.executeQuery(query);
                        while (rs.next()) {
                            String amt = rs.getString(3);
                            double bal = rs.getDouble(4);
                            phn = rs.getString(5);
                            String newbankref = rs.getString(6);
                            if (bal > 0) {
                                if (rs.getString(7).contains("The meter has accumulated a time based")) {
                                    String sumquery = "SELECT SUM(`balance`)  FROM `equity` WHERE `phoneNumber`=" + phn + "";
                                    Statement sumst = con.createStatement();
                                    ResultSet sumrs = sumst.executeQuery(sumquery);
                                    while (sumrs.next()) {
                                        amt = sumrs.getString(1);
                                    }
                                }
                                String msg = "Dear Customer,We are experiencing KPLC delays. Once the system is up we will resend your transaction. Queries call 0709711000";
                                String payres = data.sendPayment(meter, amt, phn);
                                if (null != payres) {
                                    if (!payres.equals("FAIL")) {
                                        DateTime dt = new DateTime();
                                        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                                        String cd = fmt.print(dt);
                                        if (payres.startsWith("Token")) {
                                            String pressplit[] = payres.split(":");
                                            msg = "KPLC Mtr No:" + meter + "\nToken:" + pressplit[1].replaceAll(" Units", "")
                                                    + "\nUnits:" + pressplit[2].replaceAll(" Elec", "") + "\nAmount:" + amt + "(Elec:" + pressplit[3].replaceAll(" Charges", "") + " Other Charges:" + pressplit[4] + ")" + "\nDate:" + cd
                                                    + "\nEquRef:" + newbankref;
                                            bal = 0.0;
                                            EquitelSMS esdp = new EquitelSMS("172.27.116.22", "EQUITELRX", "pdsl219", "pdsl219", phn, msg, "VendIT");
                                            esdp.submitMessage();
                                            Sdp sdp = new Sdp();
                                            try {
                                                sdp.sendSMS(phn, msg, newbankref, "704307", "http://197.248.9.105:8084/VendITNotification/SmsNotificationService");
                                            } catch (Exception sdex) {
                                            }
                                        } else if (payres.startsWith("Paid")) {
                                            String pressplit[] = payres.split(":");
                                            msg = "PAID KSH:" + amt + " FOR KPLC ACCOUNT:" + meter + " RECEIPT:"
                                                    + pressplit[3];
                                            bal = 0.0;
                                            EquitelSMS esdp = new EquitelSMS("172.27.116.22", "EQUITELRX", "pdsl219", "pdsl219", phn, msg, "VendIT");
                                            esdp.submitMessage();
                                            Sdp sdp = new Sdp();
                                            try {
                                                sdp.sendSMS(phn, msg, newbankref, "704307", "http://197.248.9.105:8084/VendITNotification/SmsNotificationService");
                                            } catch (Exception sdex) {
                                            }
                                        } else {
                                            if (payres.contains("The meter has accumulated a time based")) {
                                                bal = Double.parseDouble(amt);
                                            }
                                            msg = payres;
                                        }
                                    }
                                }
                                if (rs.getString(7).contains("The meter has accumulated a time based")) {
                                    String update2 = "update equity set balance = ? WHERE phoneNumber = ?";
                                    PreparedStatement preparedStmt2 = con.prepareStatement(update2);
                                    preparedStmt2.setDouble(1, 0.0);
                                    preparedStmt2.setString(2, phn);
                                    preparedStmt2.executeUpdate();
                                    System.out.println("Updated Equitel  table Balance");
                                }
                                String update = "update equity set balance = ?,message = ? WHERE equityid = ?";
                                PreparedStatement preparedStmt = con.prepareStatement(update);
                                preparedStmt.setDouble(1, bal);
                                preparedStmt.setString(2, msg);
                                preparedStmt.setString(3, id);
                                preparedStmt.executeUpdate();
                                System.out.println("Updated Equitel table");
                            }
                            //System.out.println("Adding: " + rs.getString(1));

                        }
                    } catch (SQLException e) {
                        Logger.getLogger(Revend.class.getName()).log(Level.SEVERE, null, e);
                    } finally {
                        try {
                            con.close();
                        } catch (SQLException e) {
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Revend.class.getName()).log(Level.SEVERE, null, ex);
        }
        String redirectURL = "https://vendit.pdslkenya.com/vendit/queryequitel.php";
        response.sendRedirect(redirectURL);
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
