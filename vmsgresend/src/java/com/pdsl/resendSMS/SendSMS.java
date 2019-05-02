/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.resendSMS;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    public SendSMS() {
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            /* TODO output your page here. You may use following sample code. 
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet SendSMS</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SendSMS at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");*/
            //http://172.27.116.21:8084/vmsgresend/SendSMS?msisdn=254728064120&correlator=JTEST&message=Testing msg resend
            Sdp sdp = new Sdp();
            String msisdn = request.getParameter("msisdn");
            String correlator = request.getParameter("correlator");
            String message = request.getParameter("message");
            Logger.getLogger(SendSMS.class.getName()).log(Level.INFO, "Resending:-" + msisdn + " Msg:" + message);

            sdp.sendSMS(msisdn, message, correlator, "704307", "http://197.248.9.105:8084/VendITNotification/SmsNotificationService");

            response.sendRedirect("https://vendit.pdslkenya.com/vendit/querymobile-exec.php?phone=" + msisdn);
        } catch (Exception ex) {
            Logger.getLogger(SendSMS.class.getName()).log(Level.SEVERE, null, ex);
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
