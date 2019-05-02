/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.sms;

import java.io.IOException;
import java.io.PrintWriter;
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
public class Single extends HttpServlet {

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
            if (null != request.getParameter("send")) {
                String phn = request.getParameter("phn");
                String mesg = request.getParameter("mesg");
                String stime = request.getParameter("stime");
                stime = stime.replaceAll("  ", " ");
                DateTime cdtime = new DateTime();
                DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss");
                DateTime st = formatter.parseDateTime(stime);
                int res = cdtime.compareTo(st);
                //res = 0 if date1 is equal to date2;
                //res < 0 if date1 is before date2;
                //res > 0 if date1 is after date2
                System.out.println(res);
            }
            out.println(data.head("Home"));
            out.println("<div class='col-sm-10'>");
            out.println("<div class='row' style='height:92%'>");
            out.println("<div class='col-sm-12'>");
            out.println("<div class='row' style='margin-top:10%'>");
            out.println("<div class='col-md-8 offset-md-2'>");
            out.println("<form action='Single' method='post'>");
            out.println("<div class='form-group'>");
            out.println("<label for='mesg' class='pdslcolor' style='font-family: \"Poiret One\", cursive;'><h4><strong><i class='fas fa-comments faa-flash animated'></i> Enter Message here ...</strong></h4></label>");
            out.println("<textarea class='form-control' name='mesg' id='mesg' rows='10'></textarea>");
            out.println("</div>");
            out.println("<div class='form-row align-items-center'>");
            out.println("<div class='col-sm-6 my-1'>");
            out.println("<div class='form-group'>");
            out.println("<label for='mesg' class='pdslcolor' style='font-family: \"Poiret One\", cursive;'><strong>Phone</strong></h4></label>");
            out.println("<input type='text' name='phn' class='form-control' placeholder='2547...'>");
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
            out.println("<div class='form-row align-items-center'>");
            out.println("<div class='col-sm-6 my-1'>");
            out.println("<div class='input-group'>");
            out.println("<div class='input-group-prepend'>");
            out.println("<label class='input-group-text pdslcolor' for='datetimepicker10'><strong> Schedule: </strong></label>");
            out.println("</div>");
            DateTime cdtime = new DateTime();
            DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss");
            String str = formatter.print(cdtime);
            out.println("<input type='text' class='form-control' name='stime' id='datetimepicker10' value='" + str + "'>");
            out.println("<div class='input-group-append'>");
            out.println("<span class='input-group-text pdslcolor'><i class='fas fa-calendar-alt'></i></span>");
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
            out.println("<div class='col-auto my-1'>");
            out.println("<button type='submit' name='send' class='btn btn-primary'><strong> SEND </strong></button>");
            out.println("</div>");
            out.println("</div>");
            out.println("</form>");
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
            out.println("<div class='row bg-secondary' style=';height:8%'>");
            out.println("<div class='col-sm-12'>");
            DateTime cyr = new DateTime();
            DateTimeFormatter yfm = DateTimeFormat.forPattern("yyyy");
            String yr = yfm.print(cyr);
            out.println("<p class='mt-3 text-center text-white'>&copy; " + yr + " by Professional Digital Systems Ltd.</p>");
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
            out.println(data.foot("Home"));
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
