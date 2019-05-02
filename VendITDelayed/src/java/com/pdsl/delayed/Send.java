/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.delayed;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import metro_sdp.Metro_sdp;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 *
 * @author juliuskun
 */
public class Send extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    Metro_sdp sdp = new Metro_sdp();
    DataStore data = new DataStore();
    MessageEditor msEObj = new MessageEditor();
    PreparedStatement prep = null;
    String mesg = null;
    DateTime dt = new DateTime();
    org.joda.time.format.DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss");
    String datee = fmt.print(dt);

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String encode = request.getCharacterEncoding();

        String address = request.getParameter("destination");
        String message = request.getParameter("message");
        String msgid = request.getParameter("msgid").replace("-", "");
        Logger.getLogger(Send.class.getName()).log(Level.INFO, "Delay message to:=" + address + " Message:" + message);
        push(address, message, msgid);
        out.println("Delay message to:=" + address + " Message:" + message);
        /*response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. 
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Send</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Send at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }*/
    }

    public void push(String mpesa_msisdn, String results, String msgid) {
        Connection con = null;
        con = this.data.connect();
        try {

            if (results.startsWith("FAIL")) {
                try {
                    mesg = this.msEObj.editToken(results, "delayed", mpesa_msisdn, msgid, datee);
                    this.sdp.sendSdpSms(mpesa_msisdn, mesg, msgid, "704307");
                    EquitelSMS esdp = new EquitelSMS("172.27.16.22", "EQUITELRX", "pdsl219", "pdsl219", mpesa_msisdn, mesg, "VendIT");
                    esdp.submitMessage();
                } catch (Exception ex) {
                }
            } else if (results.startsWith("OK")) {
                try {

                    mesg = this.msEObj.editToken(results, "delayed", mpesa_msisdn, msgid, datee);
                    this.sdp.sendSdpSms(mpesa_msisdn, mesg, msgid, "704307");
                    EquitelSMS esdp = new EquitelSMS("172.27.16.22", "EQUITELRX", "pdsl219", "pdsl219", mpesa_msisdn, mesg, "VendIT");
                    esdp.submitMessage();
                    //this.sdp.sendSdpSms(mpesa_msisdn, results.substring(3), msgid, "704307", "http://197.248.31.217:8086/paymentnotification/SmspaymentnotificationService");
                } catch (Exception ex) {
                }
            } else {
                mesg = this.msEObj.editToken(results, "delayed", mpesa_msisdn, msgid, datee);
                this.sdp.sendSdpSms(mpesa_msisdn, mesg, msgid, "704307");
                EquitelSMS esdp = new EquitelSMS("172.27.16.22", "EQUITELRX", "pdsl219", "pdsl219", mpesa_msisdn, mesg, "VendIT");
                esdp.submitMessage();
            }
            String insert2 = "insert into outbound (msisdn,message,mpesa_code) values (?,?,?)";
            this.prep = con.prepareStatement(insert2);
            this.prep.setString(1, mpesa_msisdn);
            this.prep.setString(2, mesg);
            this.prep.setString(3, msgid);
            this.prep.execute();
            this.prep.close();
            Logger.getLogger(Send.class.getName()).log(Level.INFO, "Message sent out");
        } catch (SQLException ex) {
            Logger.getLogger(Send.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.close();
                Logger.getLogger(Send.class.getName()).log(Level.INFO, "Connection closed on delayed KPLC...");
            } catch (SQLException ex) {
                Logger.getLogger(Send.class.getName()).log(Level.SEVERE, null, ex);
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
