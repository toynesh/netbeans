/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdslvending.portal;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author coolie
 */
public class Login extends HttpServlet {

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

            String logout = "none";
            String notify = "";
            if (null != request.getParameter("logout")) {
                logout = request.getParameter("logout");
            }
            if (logout.equals("logout")) {
                HttpSession session = request.getSession();
                session.invalidate();
                notify = "LoggedOut!";
            }
            String self = "no";
            String uname = "";
            String upass = "";
            if (null != request.getParameter("self")) {
                self = request.getParameter("self");
                uname = request.getParameter("uname");
                upass = request.getParameter("upass");
                String userres = data.checkUser(uname, upass);
                if (userres.startsWith("found")) {
                    System.out.println("User Found");
                    String usrressplit[] = userres.split(":");
                    HttpSession session = request.getSession(false);
                    if (session == null) {
                        // Not created yet. Now do so yourself.
                        session = request.getSession();
                        session.setAttribute("fullname", usrressplit[1]);
                        session.setAttribute("vendorcode", usrressplit[2]);
                        session.setAttribute("id", usrressplit[3]);
                    } else {
                        // Already created.
                        session.invalidate();
                        session = request.getSession();
                        session.setAttribute("fullname", usrressplit[1]);
                        session.setAttribute("vendorcode", usrressplit[2]);
                        session.setAttribute("uid", usrressplit[3]);
                    }
                } else {
                    System.out.println("User Not Found");
                    notify = "Wrong Username or Password**!!";
                }
            }
            HttpSession session = request.getSession();
            if (null != session.getAttribute("vendorcode")) {
                String vcode = (String) session.getAttribute("vendorcode");
                //System.out.println("User already Loggedin");
                if (!vcode.equals("1000")) {
                    System.out.println("NEW USER LOGGED IN: " + session.getAttribute("vendorcode") + "||" + session.getAttribute("fullname"));
                    String redirectURL = "NormalUser";
                    response.sendRedirect(redirectURL);
                } else {
                    System.out.println("NEW USER LOGGED IN: " + session.getAttribute("vendorcode") + "||" + session.getAttribute("fullname"));
                    String redirectURL = "Admin";
                    response.sendRedirect(redirectURL);
                }
            } else {
                //System.out.println("User Not Loggedin");
            }

            out.println(data.loginheader);
            out.println("<div class='container' style='margin-top:2%'>");
            out.println("<div class='row'>");
            out.println("<div class='col-lg-4 col-lg-offset-4' >");
            out.println("<div id='addvendor'>");
            out.println("<h5><i class='fa fa-sign-in' aria-hidden='true'></i>&nbsp;&nbsp;<u>Login</u></h5>");
            out.println("<br />");
            out.println("<form action='" + request.getContextPath() + "/Login' method='post'>");
            out.println("<div class='form-group'>");
            out.println("<p class='text-danger'>" + notify + "</p>");
            out.println("<label for='uname'>User Name:</label>");
            out.println("<input type='text' class='form-control' id='uname' name='uname' placeholder='pdsl'>");
            out.println("<input type='text' style='display:none'  id='self' name='self' value='" + self + "'>");
            out.println("</div>");
            out.println("<div class='form-group'>");
            out.println("<label for='upass'>Password:</label>");
            out.println("<input type='password' class='form-control no-border' id='upass' name='upass' placeholder='*******'>");
            out.println("</div>");
            out.println("<button type='submit' class='btn btn-default'>Login</button>");
            out.println("</form>");
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
            out.println(data.footer);
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
