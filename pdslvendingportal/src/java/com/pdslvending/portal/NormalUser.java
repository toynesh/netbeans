/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdslvending.portal;

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
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author coolie
 */
public class NormalUser extends HttpServlet {

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
            if (null == session.getAttribute("vendorcode")) {
                String redirectURL = "Login";
                response.sendRedirect(redirectURL);
            }

            String fullname = (String) request.getSession().getAttribute("fullname");
            String vendorcode = (String) request.getSession().getAttribute("vendorcode");

            String fb = data.getFloatBal(vendorcode);
            double floatbal = Math.round(Double.parseDouble(fb) * 100.0) / 100.0;
            String comm = data.getCurrentMonthCommission(vendorcode);
            double commission = Math.round(Double.parseDouble(comm) * 100.0) / 100.0;

            String nUserheaderP1 = "<!DOCTYPE html>"
                    + "<!--"
                    + "To change this license header, choose License Headers in Project Properties."
                    + "To change this template file, choose Tools | Templates"
                    + "and open the template in the editor."
                    + "-->"
                    + "<html>"
                    + "    <head>"
                    + "        <title>PDSL</title>"
                    + "        <meta charset='UTF-8'>"
                    + "        <meta name='viewport' content='width=device-width, initial-scale=1.0'>"
                    + "        <link rel='shortcut icon' href='img/head.png' type='image/png'>"
                    + "        <!-- Bootstrap -->"
                    + "        <link href='css/bootstrap.min.css' rel='stylesheet'>"
                    + "        <!-- manual CSS -->"
                    + "        <link href='css/pdsl.css' rel='stylesheet'>"
                    + ""
                    + "        <link href='https://fonts.googleapis.com/css?family=Muli' rel='stylesheet'>"
                    + "        <link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css'>"
                    + "    </head>"
                    + "    <body>"
                    + "        <div class='container'>"
                    + "            <div class='row' style='margin-top:1%'>"
                    + "                <div class='col-lg-12'>"
                    + "                    <h5 class='pull-right'><i class='fa fa-superpowers' aria-hidden='true'></i>&nbsp;&nbsp;" + fullname + "</h5>"
                    + "                </div>"
                    + "            </div>"
                    + "            <div class='row'>"
                    + "                <div class='col-lg-12'>"
                    + "                    <div class='row'>"
                    + "                        <div class='col-lg-3'>"
                    + "                            <a><i class='fa fa-balance-scale'></i> Float Balance <strong>KES:" + floatbal + "</strong></a>"
                    + "                        </div>"
                    + "                        <div class='col-lg-4 text-right'>"
                    + "                            <a><i class='fa fa-plus' aria-hidden='true'></i> Comm(CurrMonth) <strong>KES:" + commission + "</strong></a>"
                    + "                        </div>"
                    + "                        <div class='col-lg-2 text-right'>"
                    + "                            <a><i class='fa fa-area-chart' aria-hidden='true'></i> Reports</a>"
                    + "                        </div>";
            String useraction = "<div class='col-lg-3  text-right'>"
                    + "<select onchange='useraction(this);'  class='form-control' style='text-align-last: right;'>"
                    + "<option dir='rtl'>" + fullname + " </option>"
                    + "<option dir='rtl' value='chgpss'>Change Password</option>"
                    + "<option dir='rtl' value='logout'>Logout</option>"
                    + "</select> "
                    + "</div>";

            DateTime dt = new DateTime();
            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
            String currdate = fmt.print(dt);

            out.println(nUserheaderP1 + useraction + data.nUserheaderP2);
            out.println("<div class='container' style='margin-top:2%'>");
            out.println("<div class='row'>");
            out.println("<div class='col-lg-3'>");
            out.println("<div id='addvendor'>");
            out.println("<h5><i class='fa fa-pencil-square-o' aria-hidden='true'></i>&nbsp;&nbsp;<u> Query</u></h5>");
            out.println("<br />");
            out.println("<form action='" + request.getContextPath() + "/NormalUser' method='post'>");
            out.println("<div class='form-group'>");
            out.println("<label for='ref'>Reference</label>");
            out.println("<input type='text' class='form-control' id='ref' name='ref'>");
            out.println("</div>");
            out.println("<div class='form-group'>");
            out.println("<label for='ref'>Account/Meter</label>");
            out.println("<input type='text' class='form-control' id='account' name='account'>");
            out.println("</div>");
            out.println("<hr />");
            out.println("<div class='form-group'>");
            out.println("<label for='sdate'>Start Date</label>");
            out.println("<input value='" + currdate + "' name='sdate' type='date' placeholder='StartDate'/>");
            out.println("</div>");
            out.println("<div class='form-group'>");
            out.println("<label for='edate'>End Date</label>");
            out.println("<input value='" + currdate + "' name='edate' type='date' placeholder='EndDate'/>");
            out.println("</div>");
            out.println("<hr />");
            out.println("<div class='checkbox'>");
            out.println("<label><input type='checkbox' name='prepaid' value='yes'>PrePaid</label>");
            out.println("</div>");
            out.println("<div class='checkbox'>");
            out.println("<label><input type='checkbox' name='postpaid' value='yes'>PostPaid</label>");
            out.println("</div>");
            out.println("<div class='checkbox'>");
            out.println("<label><input type='checkbox' name='airtime' value='yes'>Airtime</label>");
            out.println("</div>");
            out.println("<div class='checkbox'>");
            out.println("<label><input type='checkbox' name='floathistory' value='yes'>Float History</label>");
            out.println("</div>");
            out.println("<div class='checkbox'>");
            out.println("<label><input type='checkbox' name='allvend' value='yes'>All</label>");
            out.println("</div>");
            out.println("<hr />");
            out.println("<button type='submit' class='btn btn-info btn-sm'>Search</button>");
            out.println("</form>");
            out.println("</div>");
            out.println("</div>");
            DateTime dt2 = new DateTime();
            DateTimeFormatter fmt2 = DateTimeFormat.forPattern("yyyy");
            String year = fmt2.print(dt2);
            DateTimeFormatter formatter = DateTimeFormat.forPattern("MMM");
            String month = formatter.print(dt2);

            String limit = " limit 100000";
            String query = "SELECT refnumber,tran_type,tran_account,tran_amt,tran_depo_amt,tran_commission,tran_response,tran_date,status from  transactions" + month + year + " where vendor_code=" + vendorcode + " order by id desc" + limit + "";
            String oquery = URLEncoder.encode(query, "UTF-8");
            if (null != request.getParameter("ref")) {
                if (!request.getParameter("ref").equals("")) {
                    query = "SELECT refnumber,tran_type,tran_account,tran_amt,tran_depo_amt,tran_commission,tran_response,tran_date,status from  transactions" + month + year + " where refnumber='" + request.getParameter("ref") + "' order by id desc" + limit + "";
                    oquery = URLEncoder.encode(query, "UTF-8");
                } else {
                    //System.out.println("Null ref");
                }
            }
            if (null != request.getParameter("account")) {
                if (!request.getParameter("account").equals("")) {
                    query = "SELECT refnumber,tran_type,tran_account,tran_amt,tran_depo_amt,tran_commission,tran_response,tran_date,status from  transactions" + month + year + " where tran_account='" + request.getParameter("account") + "' and vendor_code=" + vendorcode + " order by id desc" + limit + "";
                    oquery = URLEncoder.encode(query, "UTF-8");
                } else {
                    //System.out.println("Null ref");
                }
            }

            if (null != request.getParameter("sdate")) {
                if (!request.getParameter("sdate").equals(currdate)) {
                    DateTime dt3 = new DateTime(request.getParameter("sdate"));
                    DateTimeFormatter fmt3 = DateTimeFormat.forPattern("yyyy");
                    String qyear = fmt3.print(dt3);
                    DateTimeFormatter formatter3 = DateTimeFormat.forPattern("MMM");
                    String qmonth = formatter3.print(dt3);
                    String startdate = request.getParameter("sdate");
                    String enddate = request.getParameter("edate");
                    if (enddate.equals(currdate)) {
                        DateTime lastDayOfMonth = dt3.dayOfMonth().withMaximumValue();
                        enddate = fmt.print(lastDayOfMonth);
                    }
                    query = "SELECT refnumber,tran_type,tran_account,tran_amt,tran_depo_amt,tran_commission,tran_response,tran_date,status from  transactions" + qmonth + qyear + " where (tran_date between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59') and vendor_code=" + vendorcode + " order by id desc" + limit + "";

                    if (null != request.getParameter("ref")) {
                        if (!request.getParameter("ref").equals("")) {
                            query = "SELECT refnumber,tran_type,tran_account,tran_amt,tran_depo_amt,tran_commission,tran_response,tran_date,status from  (SELECT * from transactions" + qmonth + qyear + " where (tran_date between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59') and vendor_code=" + vendorcode + ")  as M where refnumber='" + request.getParameter("ref") + "'  order by id desc" + limit + "";
                        }
                    }
                    if (null != request.getParameter("account")) {
                        if (!request.getParameter("account").equals("")) {
                            query = "SELECT refnumber,tran_type,tran_account,tran_amt,tran_depo_amt,tran_commission,tran_response,tran_date,status from  (SELECT * from transactions" + qmonth + qyear + " where (tran_date between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59') and vendor_code=" + vendorcode + ")  as M where tran_account='" + request.getParameter("account") + "'  order by id desc" + limit + "";
                        }
                    }
                    if (null != request.getParameter("prepaid")) {
                        if (!request.getParameter("prepaid").equals("")) {
                            query = "SELECT refnumber,tran_type,tran_account,tran_amt,tran_depo_amt,tran_commission,tran_response,tran_date,status from  (SELECT * from transactions" + qmonth + qyear + " where (tran_date between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59') and vendor_code=" + vendorcode + ")  as M where tran_type='prepaid'  order by id desc" + limit + "";
                        }
                    }
                    if (null != request.getParameter("postpaid")) {
                        if (!request.getParameter("postpaid").equals("")) {
                            query = "SELECT refnumber,tran_type,tran_account,tran_amt,tran_depo_amt,tran_commission,tran_response,tran_date,status from  (SELECT * from transactions" + qmonth + qyear + " where (tran_date between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59') and vendor_code=" + vendorcode + ")  as M where tran_type='postpaid'  order by id desc" + limit + "";
                        }
                    }
                    if (null != request.getParameter("airtime")) {
                        if (!request.getParameter("airtime").equals("")) {
                            query = "SELECT refnumber,tran_type,tran_account,tran_amt,tran_depo_amt,tran_commission,tran_response,tran_date,status from  (SELECT * from transactions" + qmonth + qyear + " where (tran_date between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59') and vendor_code=" + vendorcode + ")  as M where tran_type='airtime'  order by id desc" + limit + "";
                        }
                    }
                    if (null != request.getParameter("floathistory")) {
                        if (!request.getParameter("floathistory").equals("")) {
                            query = "SELECT refnumber,tran_type,tran_account,tran_amt,tran_depo_amt,tran_commission,tran_response,tran_date,status from  (SELECT * from transactions" + qmonth + qyear + " where (tran_date between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59') and vendor_code=" + vendorcode + ")  as M where tran_type='FLOAT DEPOSIT' or tran_type='BALANCE BROUGHT FORWARD' or tran_type='COMMISSION LAST MONTH'  order by id desc" + limit + "";
                        }
                    }
                    if (null != request.getParameter("allvend")) {
                        if (!request.getParameter("allvend").equals("")) {
                            query = "SELECT refnumber,tran_type,tran_account,tran_amt,tran_depo_amt,tran_commission,tran_response,tran_date,status from  transactions" + qmonth + qyear + " where (tran_date between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59') and vendor_code=" + vendorcode + " order by id desc" + limit + "";
                        }
                    }
                    oquery = URLEncoder.encode(query, "UTF-8");
                    //System.out.println("MonthYear" + qmonth + qyear);
                    //System.out.println("enddate" + request.getParameter("edate"));
                } else {
                    //System.out.println("Null ref");
                }
            } else {
                if (null != request.getParameter("prepaid")) {
                    if (!request.getParameter("prepaid").equals("")) {
                        query = "SELECT refnumber,tran_type,tran_account,tran_amt,tran_depo_amt,tran_commission,tran_response,tran_date,status from  transactions" + month + year + " where tran_type='prepaid' and vendor_code=" + vendorcode + " order by id desc" + limit + "";
                        oquery = URLEncoder.encode(query, "UTF-8");
                    } else {
                        //System.out.println("Null ref");
                    }
                }
                if (null != request.getParameter("postpaid")) {
                    if (!request.getParameter("postpaid").equals("")) {
                        query = "SELECT refnumber,tran_type,tran_account,tran_amt,tran_depo_amt,tran_commission,tran_response,tran_date,status from  transactions" + month + year + " where tran_type='postpaid' and vendor_code=" + vendorcode + " order by id desc" + limit + "";
                        oquery = URLEncoder.encode(query, "UTF-8");
                    } else {
                        //System.out.println("Null ref");
                    }
                }
                if (null != request.getParameter("airtime")) {
                    if (!request.getParameter("airtime").equals("")) {
                        query = "SELECT refnumber,tran_type,tran_account,tran_amt,tran_depo_amt,tran_commission,tran_response,tran_date,status from  transactions" + month + year + " where tran_type='airtime' and vendor_code=" + vendorcode + " order by id desc" + limit + "";
                        oquery = URLEncoder.encode(query, "UTF-8");
                    } else {
                        //System.out.println("Null ref");
                    }
                }
                if (null != request.getParameter("floathistory")) {
                    if (!request.getParameter("floathistory").equals("")) {
                        query = "SELECT refnumber,tran_type,tran_account,tran_amt,tran_depo_amt,tran_commission,tran_response,tran_date,status from  (select * from transactions" + month + year + " where vendor_code=" + vendorcode + ") as v where tran_type='FLOAT DEPOSIT' or tran_type='BALANCE BROUGHT FORWARD' or tran_type='COMMISSION LAST MONTH' order by id desc" + limit + "";
                        oquery = URLEncoder.encode(query, "UTF-8");
                    } else {
                        //System.out.println("Null ref");
                    }
                }
                if (null != request.getParameter("allvend")) {
                    if (!request.getParameter("allvend").equals("")) {
                        query = "SELECT refnumber,tran_type,tran_account,tran_amt,tran_depo_amt,tran_commission,tran_response,tran_date,status from  transactions" + month + year + " where vendor_code=" + vendorcode + " order by id desc" + limit + "";
                        oquery = URLEncoder.encode(query, "UTF-8");
                    } else {
                        //System.out.println("Null ref");
                    }
                }
            }
            //
            if (null != request.getParameter("oquery")) {
                oquery = request.getParameter("oquery");
            }
            //System.out.println("Oquery:" + oquery);
            //System.out.println("query:" + query);
            out.println("<div class='col-lg-9 table-responsive'>");
            out.println("<h5><span><i class='fa fa-bars' aria-hidden='true'></i>&nbsp;&nbsp;<u> Transactions</u><span class='pull-right'>Export:&nbsp;&nbsp;<a href='" + request.getContextPath() + "/ExportExcel?query=" + oquery + "'><i class='fa fa-file-excel-o'></i > Excel</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href='" + request.getContextPath() + "/ExportPDF?query=" + oquery + "'><i class='fa fa-file-pdf-o'></i > PDF</a></span></span></h5>");
            out.println("<table class='table table-bordered' style='font-size:11px'>");
            out.println("<thead>");
            out.println("<tr>");
            out.println("<th>Date</th>");
            out.println("<th>Reference</th>");
            out.println("<th>Type</th>");
            out.println("<th>Account</th>");
            out.println("<th>SaleAmount</th>");
            out.println("<th>DepAmount</th>");
            out.println("<th>Commission</th>");
            out.println("<th>Response</th>");
            out.println("<th>Status</th>");
            out.println("</tr>");
            out.println("</thead>");
            out.println("<tbody style='color:black'>");

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
                    out.println("<td>" + rs.getString(8).substring(0, rs.getString(8).length() - 2) + "</td>");
                    String ref = "pdslauto";
                    if (null != rs.getString(1)) {
                        ref = rs.getString(1);
                    }
                    out.println("<td>" + ref + "</td>");
                    out.println("<td>" + rs.getString(2) + "</td>");
                    String acc = " ";
                    if (null != rs.getString(3)) {
                        acc = rs.getString(3);
                    }
                    out.println("<td>" + acc + "</td>");
                    String salamt = "0";
                    if (null != rs.getString(4)) {
                        salamt = rs.getString(4);
                    }
                    out.println("<td>" + salamt + "</td>");
                    String depamt = "0";
                    if (null != rs.getString(5)) {
                        depamt = rs.getString(5);
                    }
                    out.println("<td>" + depamt + "</td>");
                    out.println("<td>" + rs.getString(6) + "</td>");
                    String res = " ";
                    if (null != rs.getString(7)) {
                        res = rs.getString(7);
                    }
                    out.println("<td>" + res + "</td>");
                    String status = "completed";
                    if (rs.getString(9).equals("0")) {
                        status = "pending";
                    }
                    out.println("<td>" + status + "</td>");
                    out.println("</tr>");
                }
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
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
                out.println("<form style='display:none'  action='" + request.getContextPath() + "/NormalUser' method='post'>");
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
