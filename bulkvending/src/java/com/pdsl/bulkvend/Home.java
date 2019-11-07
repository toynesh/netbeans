/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.bulkvend;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author julius
 */
public class Home extends HttpServlet {

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
            String notify = "";
            if (null != request.getParameter("logout")) {
                 HttpSession session = request.getSession();
                session.invalidate();
                notify = "LoggedOut!";
            }
            if (null != request.getParameter("login")) {
                notify="Please Login!";
            }
            String uName = "";
            String uPass = "";
            if (null != request.getParameter("uName")) {
                uName = request.getParameter("uName");
                uPass = request.getParameter("uPass");
                String userres = data.checkUser(uName, uPass);
                if (userres.startsWith("found")) {
                    System.out.println("User Found");
                    String usrressplit[] = userres.split(":");
                    HttpSession session = request.getSession(false);
                    if (session == null) {
                        // Not created yet. Now do so yourself.
                        session = request.getSession();
                        session.setAttribute("clientId", usrressplit[1]);                        
                        session.setAttribute("clientCode", usrressplit[2]);
                        session.setAttribute("fullname", usrressplit[3]);
                        session.setAttribute("uName", usrressplit[4]);
                    } else {
                        // Already created.
                        session.invalidate();
                        session = request.getSession();
                        session.setAttribute("clientId", usrressplit[1]);                        
                        session.setAttribute("clientCode", usrressplit[2]);
                        session.setAttribute("fullname", usrressplit[3]);
                        session.setAttribute("uName", usrressplit[4]);
                    }
                } else {
                    System.out.println("User Not Found");
                    notify = "Unknown Username or Password**!!";
                }
            }

            HttpSession session = request.getSession();
            if (null != session.getAttribute("uName")) {
                String fullname = (String) request.getSession().getAttribute("fullname");
                out.println(data.pageHeader(true,fullname));
                System.out.println("NEW CLIENT LOGGED IN: " + session.getAttribute("uName") + "||" + session.getAttribute("fullname"));
            } else {
                out.println(data.pageHeader(false,notify));
                //System.out.println("User Not Loggedin");
            }
            out.println("<section class='page-section clearfix'>");
            out.println("<div class='container'>");
            out.println("<div class='intro'>");
            out.println("<img class='intro-img img-fluid mb-3 mb-lg-0 rounded' src='img/bkg.jpg' alt=''>");
            out.println("<div class='intro-text left-0 text-center bg-faded p-5 rounded'>");
            out.println("<h2 class='section-heading mb-4'>");
            out.println("<span class='section-heading-upper'>Bulk Vend</span>");
            out.println("<span class='section-heading-lower'>Electricity & Airtime</span>");
            out.println("</h2>");
            out.println("<p class='mb-3'>A convinient way for you to purchase pre-paid tokens or settle post paid bills in bulk.</p>");
            out.println("<div class='intro-button mx-auto'>");
            out.println("<a class='btn btn-primary btn-xl' href='#'>Visit Us Today!</a>");
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
            out.println("</section>");

            out.println(data.pageFoorter(notify));
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
