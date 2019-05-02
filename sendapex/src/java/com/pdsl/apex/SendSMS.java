/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.apex;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import metro_sdp.Metro_sdp;

/**
 *
 * @author coolie
 */
public class SendSMS extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        //http://172.27.116.22:8081/sendapex/SendSMS?msisdn=254728064120&correlator=JTEST&message=Testing msg resend
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        DataStore data = new DataStore();
        Metro_sdp sdp = new Metro_sdp();
        
        try {
            String msisdn = request.getParameter("msisdn");
            String correlator = request.getParameter("correlator");
            String message = request.getParameter("message");
            Logger.getLogger(SendSMS.class.getName()).log(Level.INFO, "Sending:-" + msisdn + " Msg:" + message);

            List<String> safprefix = new ArrayList<>();
            Connection con = data.prefixconnect();
            String query2 = "SELECT DISTINCT prefix FROM safaricom";
            Statement st2 = con.createStatement();
            ResultSet rs2 = st2.executeQuery(query2);
            while (rs2.next()) {
                //System.out.println("Saving All: " + rs2.getString(1));
                safprefix.add(rs2.getString(1));
            }
            //System.out.println("Safprefix Array Size: " + safprefix.size());
            con.close();
            msisdn = msisdn.replace("+", "");
            String msisdnprefix = msisdn.substring(0, 6);
            if (safprefix.contains(msisdnprefix.substring(3))) {

                sdp.sendSdpSms(msisdn, message, correlator, "706815", "http://197.248.9.106:8081/pdslsmsAPIdlrnotification/SmsNotificationService");
            } else {
                EquitelSMS esdp = new EquitelSMS("172.27.116.22", "EQUITELRX", "pdsl219", "pdsl219", msisdn, message, "APEXGROUP");
                esdp.submitMessage();
            }
            safprefix.clear();
            out.println("OK");
        } catch (Exception e) {
            out.println("FAIL");
            e.printStackTrace();
        }
        /*try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. 
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet SendSMS</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SendSMS at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }*/
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
