/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.judiciary.portal;

import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Integer.parseInt;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        HttpSession session = request.getSession();
        if (null == session.getAttribute("fullname")) {
            String redirectURL = "Login";
            response.sendRedirect(redirectURL);
        }
        String fullname = (String) request.getSession().getAttribute("fullname");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println(data.header);
            if (null != request.getParameter("outbox")) {
                out.println("<h2>Outbox</h2>");
                out.println("<span class='float-right'>Loggedin as: " + fullname + "&nbsp;&nbsp;&nbsp;&nbsp;<i class='fas fa-ellipsis-h'></i> <a href='" + request.getContextPath() + "/Login?logout=logout'> Signout</a></span>");
                out.println("<table class='table'>");
                out.println("<thead style='background-color:#36453B;color:#fff'>");
                out.println("<tr>");
                out.println("<th scope='col'>Phone</th>");
                out.println("<th scope='col'>Message</th>");
                out.println("<th scope='col'>LinkID</th>");
                out.println("<th scope='col'>Service</th>");
                out.println("<th scope='col'>MNO</th>");
                out.println("<th scope='col'>Status</th>");
                out.println("<th scope='col'>Date</th>");
                out.println("</tr>");
                out.println("</thead>");
                out.println("<tbody>");

                String limit = " limit 100000";
                String query = "SELECT msisdn,message,linkid,service,mno,deliverystatus,datecreated FROM judicialoutbox ORDER BY datecreated DESC" + limit + "";
                String oquery = URLEncoder.encode(query, "UTF-8");

                if (null != request.getParameter("phone")) {
                if (!request.getParameter("phone").equals("")) {
                    String phone = request.getParameter("phone");
                    query = "SELECT msisdn,message,linkid,service,mno,deliverystatus,datecreated FROM judicialoutbox WHERE  msisdn ='"+phone+"' ORDER BY datecreated DESC" + limit + "";
                    oquery = URLEncoder.encode(query, "UTF-8");
                } else {
                    //System.out.println("Null ref");
                }
            }
                
                Connection con = data.connect();
                int pages = 1;
                int nxtrow = 1;
                int pageno = 1;
                int btnid = 1;
                String pgquery = query;
                try {
                    Statement rowstm = con.createStatement();
                    ResultSet rowrs = rowstm.executeQuery(query);
                    int numrows = 0;
                    while (rowrs.next()) {
                        numrows++;
                    }
                    System.out.println("NumRows:" + numrows);
                    pages = numrows / 10;
                    if ((numrows % 10) != 0) {
                        pages = pages + 1;
                    }

                    if (null != request.getParameter("pageno")) {
                        //System.out.println("Page number not null:");
                        pageno = parseInt(request.getParameter("pageno"));
                        pages = parseInt(request.getParameter("pages"));
                        nxtrow = (pageno * 10) - 9;
                        String olimit = request.getParameter("limit");
                        // System.out.println("Original Limit:" + olimit);                    
                        btnid = parseInt(request.getParameter("btnid"));
                        limit = " limit " + nxtrow + ",10";
                        //System.out.println("New Limit:" + limit);
                        pgquery = java.net.URLDecoder.decode(request.getParameter("query"), "UTF-8");
                        //System.out.println("Original query:" + pgquery);
                        query = pgquery.replaceAll(olimit, limit);
                        //System.out.println("New query:" + query);

                    } else {
                        nxtrow = (pageno * 10) - 9;
                        String olimit = limit;
                        limit = " limit 10";
                        query = query.replaceAll(olimit, limit);
                    }
                    /*System.out.println("pageno:" + pageno);
                    System.out.println("nxtrow:" + nxtrow);
                    System.out.println("limit:" + limit);
                    System.out.println("Pages:" + pages);
                    System.out.println("Buttonid:" + btnid);*/
                    System.out.println("Portal Query:" + query);

                    Statement stm = con.createStatement();
                    ResultSet rs = stm.executeQuery(query);
                    while (rs.next()) {
                        out.println("<tr>");
                        out.println("<th scope='row'>" + rs.getString(1) + "</th>");
                        out.println("<td>" + rs.getString(2) + "</td>");
                        out.println("<td>" + rs.getString(3) + "</td>");
                        out.println("<td>" + rs.getString(4) + "</td>");
                        out.println("<td>" + rs.getString(5) + "</td>");
                        out.println("<td>" + rs.getString(6) + "</td>");
                        out.println("<td>" + rs.getString(7) + "</td>");
                        out.println("</tr>");
                    }
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
                }
                out.println("</tbody>");
                out.println("</table>");

                out.println("<div class='pull-right'>");
                //pages = 4;
                int spage = 1;
                int lpage = 5;
                if (pages <= 5) {
                    lpage = pages;
                }
                if (pageno > 3) {
                    spage = pageno - 2;
                    lpage = pageno + 2;
                    if (lpage >= pages) {
                        lpage = pages;
                    }
                }
                out.println("<nav aria-label='Page navigation example'>");
                out.println("<ul class='pagination'>");
                out.println("<li class='page-item'>");
                String prevbt = "btn" + 1;
                if (btnid > 1) {
                    prevbt = "btn" + (btnid - 1);
                }
                out.println("<a class='page-link' onclick=\"document.getElementById('" + prevbt + "').click();\" aria-label='Previous'>");
                out.println("<span aria-hidden='true'>&laquo;</span>");
                out.println("<span class='sr-only'>Previous</span>");
                out.println("</a>");
                out.println("</li>");

                for (int i = spage; i <= lpage; i++) {
                    String navlink = "" + i;
                    String itemclass = "page-item";
                    if (btnid == i) {
                        itemclass = "page-item active";
                        navlink = i + " <span class=\"sr-only\">(current)</span>";
                    }
                    //System.out.println("navlink:" + navlink);outbox
                    out.println("<form style='display:none'  action='" + request.getContextPath() + "/Home' method='post'>");
                     out.println("<input type='text' style='display:none' name='outbox' value='yes'>");
                    out.println("<input type='text' style='display:none' name='pages' value='" + pages + "'>");
                    out.println("<input type='text' style='display:none' name='pageno' value='" + i + "'>");
                    out.println("<input type='text' style='display:none' name='limit' value='" + limit + "'>");
                    out.println("<input type='text' style='display:none' name='oquery' value='" + oquery + "'>");
                    out.println("<input type='text' style='display:none' name='query' value='" + URLEncoder.encode(query, "UTF-8") + "'>");
                    out.println("<input type='text' style='display:none' name='btnid' value='" + i + "'>");
                    out.println("<button type='submit' style='display:none'  id='btn" + i + "' class='btn btn-default'>" + i + "</button>");
                    out.println("</form>");
                    out.println("<li class='" + itemclass + "'><a class='page-link' onclick=\"document.getElementById('btn" + i + "').click();\">" + navlink + "</a></li>");
                }

                out.println("<li class='page-item'>");
                String nextbt = "btn" + pages;
                if (btnid < pages) {
                    nextbt = "btn" + (btnid + 1);
                }
                out.println("<a class='page-link' onclick=\"document.getElementById('" + nextbt + "').click();\" aria-label='Next'>");
                out.println("<span aria-hidden='true'>&raquo;</span>");
                out.println("<span class='sr-only'>Next</span>");
                out.println("</a>");
                out.println("</li>");
                out.println("</ul>");
                out.println("</nav>");
                out.println("</div>");

                out.println("<div class='line'></div></div>");
            } else {
                //INBOX!!!
                out.println("<h2>Inbox</h2>");
                out.println("<span class='float-right'>Loggedin as: " + fullname + "&nbsp;&nbsp;&nbsp;&nbsp;<i class='fas fa-ellipsis-h'></i> <a href='" + request.getContextPath() + "/Login?logout=logout'> Signout</a></span>");
                out.println("<table class='table'>");
                out.println("<thead style='background-color:#36453B;color:#fff'>");
                out.println("<tr>");
                out.println("<th scope='col'>Phone</th>");
                out.println("<th scope='col'>Message</th>");
                out.println("<th scope='col'>LinkID</th>");
                out.println("<th scope='col'>MNO</th>");
                out.println("<th scope='col'>Status</th>");
                out.println("<th scope='col'>Date</th>");
                out.println("</tr>");
                out.println("</thead>");
                out.println("<tbody>");

                String limit = " limit 100000";
                String query = "SELECT msisdn,message,linkid,mno,status,datecreated FROM judicialinbox ORDER BY datecreated DESC" + limit + "";
                String oquery = URLEncoder.encode(query, "UTF-8");
                Connection con = data.connect();
                int pages = 1;
                int nxtrow = 1;
                int pageno = 1;
                int btnid = 1;
                String pgquery = query;
                try {
                    Statement rowstm = con.createStatement();
                    ResultSet rowrs = rowstm.executeQuery(query);
                    int numrows = 0;
                    while (rowrs.next()) {
                        numrows++;
                    }
                    System.out.println("NumRows:" + numrows);
                    pages = numrows / 10;
                    if ((numrows % 10) != 0) {
                        pages = pages + 1;
                    }

                    if (null != request.getParameter("pageno")) {
                        //System.out.println("Page number not null:");
                        pageno = parseInt(request.getParameter("pageno"));
                        pages = parseInt(request.getParameter("pages"));
                        nxtrow = (pageno * 10) - 9;
                        String olimit = request.getParameter("limit");
                        // System.out.println("Original Limit:" + olimit);                    
                        btnid = parseInt(request.getParameter("btnid"));
                        limit = " limit " + nxtrow + ",10";
                        //System.out.println("New Limit:" + limit);
                        pgquery = java.net.URLDecoder.decode(request.getParameter("query"), "UTF-8");
                        //System.out.println("Original query:" + pgquery);
                        query = pgquery.replaceAll(olimit, limit);
                        //System.out.println("New query:" + query);

                    } else {
                        nxtrow = (pageno * 10) - 9;
                        String olimit = limit;
                        limit = " limit 10";
                        query = query.replaceAll(olimit, limit);
                    }
                    /*System.out.println("pageno:" + pageno);
                    System.out.println("nxtrow:" + nxtrow);
                    System.out.println("limit:" + limit);
                    System.out.println("Pages:" + pages);
                    System.out.println("Buttonid:" + btnid);*/
                    System.out.println("Portal Query:" + query);

                    Statement stm = con.createStatement();
                    ResultSet rs = stm.executeQuery(query);
                    while (rs.next()) {
                        out.println("<tr>");
                        out.println("<th scope='row'>" + rs.getString(1) + "</th>");
                        out.println("<td>" + rs.getString(2) + "</td>");
                        out.println("<td>" + rs.getString(3) + "</td>");
                        out.println("<td>" + rs.getString(4) + "</td>");
                        out.println("<td>" + rs.getString(5) + "</td>");
                        out.println("<td>" + rs.getString(6) + "</td>");
                        out.println("</tr>");
                    }
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
                }
                out.println("</tbody>");
                out.println("</table>");
                
                out.println("<div class='pull-right'>");
                //pages = 4;
                int spage = 1;
                int lpage = 5;
                if (pages <= 5) {
                    lpage = pages;
                }
                if (pageno > 3) {
                    spage = pageno - 2;
                    lpage = pageno + 2;
                    if (lpage >= pages) {
                        lpage = pages;
                    }
                }
                out.println("<nav aria-label='Page navigation example'>");
                out.println("<ul class='pagination'>");
                out.println("<li class='page-item'>");
                String prevbt = "btn" + 1;
                if (btnid > 1) {
                    prevbt = "btn" + (btnid - 1);
                }
                out.println("<a class='page-link' onclick=\"document.getElementById('" + prevbt + "').click();\" aria-label='Previous'>");
                out.println("<span aria-hidden='true'>&laquo;</span>");
                out.println("<span class='sr-only'>Previous</span>");
                out.println("</a>");
                out.println("</li>");

                for (int i = spage; i <= lpage; i++) {
                    String navlink = "" + i;
                    String itemclass = "page-item";
                    if (btnid == i) {
                        itemclass = "page-item active";
                        navlink = i + " <span class=\"sr-only\">(current)</span>";
                    }
                    //System.out.println("navlink:" + navlink);
                    out.println("<form style='display:none'  action='" + request.getContextPath() + "/Home' method='post'>");
                    out.println("<input type='text' style='display:none' name='pages' value='" + pages + "'>");
                    out.println("<input type='text' style='display:none' name='pageno' value='" + i + "'>");
                    out.println("<input type='text' style='display:none' name='limit' value='" + limit + "'>");
                    out.println("<input type='text' style='display:none' name='oquery' value='" + oquery + "'>");
                    out.println("<input type='text' style='display:none' name='query' value='" + URLEncoder.encode(query, "UTF-8") + "'>");
                    out.println("<input type='text' style='display:none' name='btnid' value='" + i + "'>");
                    out.println("<button type='submit' style='display:none'  id='btn" + i + "' class='btn btn-default'>" + i + "</button>");
                    out.println("</form>");
                    out.println("<li class='" + itemclass + "'><a class='page-link' onclick=\"document.getElementById('btn" + i + "').click();\">" + navlink + "</a></li>");
                }

                out.println("<li class='page-item'>");
                String nextbt = "btn" + pages;
                if (btnid < pages) {
                    nextbt = "btn" + (btnid + 1);
                }
                out.println("<a class='page-link' onclick=\"document.getElementById('" + nextbt + "').click();\" aria-label='Next'>");
                out.println("<span aria-hidden='true'>&raquo;</span>");
                out.println("<span class='sr-only'>Next</span>");
                out.println("</a>");
                out.println("</li>");
                out.println("</ul>");
                out.println("</nav>");
                out.println("</div>");
                
                out.println("<div class='line'></div></div>");
            }
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
