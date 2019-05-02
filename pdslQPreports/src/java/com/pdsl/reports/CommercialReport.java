/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.reports;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author julius
 */
public class CommercialReport extends HttpServlet {

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
            if (null == session.getAttribute("retailer")) {
                String redirectURL = "Login";
                response.sendRedirect(redirectURL);
            } else {
                String fullname = (String) request.getSession().getAttribute("fullname");
                String rtlr = (String) request.getSession().getAttribute("retailer");
                String retailer = "none";
                if (!rtlr.equals("Admin")) {
                    retailer = rtlr;
                }
                if (null != request.getParameter("posdud")) {
                    String cm = request.getParameter("mnth");
                    String redirectURL = "/pdslQPreports/Home?mw=" + cm + "&a";
                    response.sendRedirect(redirectURL);
                }
                if (null != request.getParameter("evgud")) {
                    String cm = request.getParameter("mnth");
                    String redirectURL = "/pdslQPreports/EVGGraph?mw=" + cm + "";
                    response.sendRedirect(redirectURL);
                }
                if (null != request.getParameter("adowntime")) {
                    String cm = request.getParameter("mnth");
                    String redirectURL = "/pdslQPreports/DowntimeTable?mw=" + cm + "";
                    response.sendRedirect(redirectURL);
                }
                if (null != request.getParameter("news")) {
                    String redirectURL = "/pdslQPreports/News";
                    response.sendRedirect(redirectURL);
                }
                String mnth = "none";
                String rtype = "none";
                String gmnth = "none";

                String err = "";

                if (null != request.getParameter("mnth")) {
                    if (!request.getParameter("mnth").equals("")) {
                        gmnth = request.getParameter("mnth");
                    } else {
                        err = "<h6 class='text-center text-info'>Invalid Month!</h6>";
                    }
                } else {
                    err = "<h6 class='text-center text-info'>Invalid Month!</h6>";
                }
                if (null != request.getParameter("retailer")) {
                    if (!request.getParameter("retailer").equals("")) {
                        retailer = request.getParameter("retailer");
                    } else {
                        err = "<h6 class='text-center text-info'>Invalid Retailer!</h6>";
                    }
                } else {
                    err = "<h6 class='text-center text-info'>Invalid Retailer!</h6>";
                }
                if (null != request.getParameter("rtype")) {
                    if (!request.getParameter("rtype").equals("")) {
                        rtype = request.getParameter("rtype");
                    } else {
                        err = "<h6 class='text-center text-info'>Invalid Report Type!</h6>";
                    }
                } else {
                    err = "<h6 class='text-center text-info'>Invalid Report Type!</h6>";
                }
                //System.out.println("Month: "+gmnth);
                if (!gmnth.equals("none")) {
                    DateTimeFormatter otfm = DateTimeFormat.forPattern("MMM yyyy");
                    DateTime odt = otfm.parseDateTime(gmnth);
                    DateTimeFormatter fm = DateTimeFormat.forPattern("yyyy-MM");
                    mnth = fm.print(odt);
                }
                out.println(data.handleHeader("CommercialReport", retailer, rtype, gmnth, fullname));
                if (rtype.equals("postpaid") || rtype.equals("prepaid")) {
                    out.println("<div class='col-lg-12 table-responsive'>");
                } else {
                    out.println("<div id='tprint' class='col-lg-12 table-responsive'>");
                }
                if (rtype.equals("postpaid")) {
                    String query = "SELECT datesold,txtype,reference,retailvalue,mancovalue,commissionvalue from  commercialTx" + retailer + mnth.replaceAll("-", "") + " WHERE txtype='b'";
                    Connection con = data.connect();
                    double rvaluesum = 0.0;
                    double mvaluesum = 0.0;
                    double cvaluesum = 0.0;
                    try {
                        Statement stm = con.createStatement();
                        ResultSet rs = stm.executeQuery(query);
                        if (!rs.isBeforeFirst()) {
                            err = "<h6 class='text-center text-info'>No Records Found for the search criteria Retailer: <span class='text-danger'>'" + retailer + "'</span>, Month: <span class='text-danger'>'" + gmnth + "'</span>, ReportType: <span class='text-danger'>'" + rtype + "'</span></h6>";
                        } else {
                            out.println("<h6 class='text-center text-info'>Retailer: <span class='text-danger'>'" + retailer + "'</span> Month: <span class='text-danger'>'" + mnth + "'</span> ReportType: <span class='text-danger'>'" + rtype + "'</span></h6>");
                            out.println("<div class='achievements-wrapper'>");
                            out.println("<table class='table table-bordered fixed-header' style='font-size:12px'>");
                            out.println("<thead>");
                            out.println("<tr>");
                            out.println("<th>Date</th>");
                            out.println("<th>Type</th>");
                            out.println("<th>Reference</th>");
                            out.println("<th>RetailValue</th>");
                            out.println("<th>MancoValue</th>");
                            out.println("<th>Commission</th>");
                            out.println("</tr>");
                            out.println("</thead>");
                            out.println("<tbody>");
                            while (rs.next()) {
                                out.println("<tr>");
                                out.println("<td>" + rs.getString(1) + "</td>");
                                out.println("<td>" + rs.getString(2) + "</td>");
                                out.println("<td>" + rs.getString(3) + "</td>");
                                out.println("<td>" + rs.getString(4) + "</td>");
                                out.println("<td>" + rs.getString(5) + "</td>");
                                out.println("<td>" + rs.getString(6) + "</td>");
                                out.println("</tr>");
                                rvaluesum = rvaluesum + Double.parseDouble(rs.getString(4));
                                mvaluesum = mvaluesum + Double.parseDouble(rs.getString(5));
                                cvaluesum = cvaluesum + Double.parseDouble(rs.getString(6));
                            }
                            out.println("</tbody>");
                            out.println("</table>");
                            out.println("</div>");
                        }
                    } catch (SQLException ex) {
                        err = "<h6 class='text-center text-info'>No Records Found for the search criteria Retailer: <span class='text-danger'>'" + retailer + "'</span>, Month: <span class='text-danger'>'" + gmnth + "'</span>, ReportType: <span class='text-danger'>'" + rtype + "'</span></h6>";
                        //Logger.getLogger(CommercialReport.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        try {
                            con.close();
                        } catch (SQLException ex) {
                        }
                    }
                    out.println(err);
                    DecimalFormat formatter = new DecimalFormat("#,###.00");
                    out.println("<h4 style ='margin-top:2%;'>Sum RetailValue: <span style ='color:red'>" + formatter.format(rvaluesum) + " /=</span><br />Sum MancoValue: <span style ='color:red'>" + formatter.format(mvaluesum) + " /=</span><br />Sum Commission: <span style ='color:red'>" + formatter.format(cvaluesum) + " /=</span></h4>");
                } else if (rtype.equals("prepaid")) {
                    String query = "SELECT datesold,txtype,reference,retailvalue,mancovalue,commissionvalue from  commercialTx" + retailer + mnth.replaceAll("-", "") + " WHERE txtype='e'";
                    Connection con = data.connect();
                    double rvaluesum = 0.0;
                    double mvaluesum = 0.0;
                    double cvaluesum = 0.0;
                    try {
                        Statement stm = con.createStatement();
                        ResultSet rs = stm.executeQuery(query);
                        if (!rs.isBeforeFirst()) {
                            err = "<h6 class='text-center text-info'>No Records Found for the search criteria Retailer: <span class='text-danger'>'" + retailer + "'</span>, Month: <span class='text-danger'>'" + gmnth + "'</span>, ReportType: <span class='text-danger'>'" + rtype + "'</span></h6>";
                        } else {
                            out.println("<h6 class='text-center text-info'>Retailer: <span class='text-danger'>'" + retailer + "'</span> Month: <span class='text-danger'>'" + gmnth + "'</span> ReportType: <span class='text-danger'>'" + rtype + "'</span></h6>");
                            out.println("<div class='achievements-wrapper'>");
                            out.println("<table class='table table-bordered fixed-header' style='font-size:12px'>");
                            out.println("<thead>");
                            out.println("<tr>");
                            out.println("<th>Date</th>");
                            out.println("<th>Type</th>");
                            out.println("<th>Reference</th>");
                            out.println("<th>RetailValue</th>");
                            out.println("<th>MancoValue</th>");
                            out.println("<th>Commission</th>");
                            out.println("</tr>");
                            out.println("</thead>");
                            out.println("<tbody>");
                            while (rs.next()) {
                                out.println("<tr>");
                                out.println("<td>" + rs.getString(1) + "</td>");
                                out.println("<td>" + rs.getString(2) + "</td>");
                                out.println("<td>" + rs.getString(3) + "</td>");
                                out.println("<td>" + rs.getString(4) + "</td>");
                                out.println("<td>" + rs.getString(5) + "</td>");
                                out.println("<td>" + rs.getString(6) + "</td>");
                                out.println("</tr>");
                                rvaluesum = rvaluesum + Double.parseDouble(rs.getString(4));
                                mvaluesum = mvaluesum + Double.parseDouble(rs.getString(5));
                                cvaluesum = cvaluesum + Double.parseDouble(rs.getString(6));
                            }
                            out.println("</tbody>");
                            out.println("</table>");
                            out.println("</div>");
                        }
                    } catch (SQLException ex) {
                        err = "<h6 class='text-center text-info'>No Records Found for the search criteria Retailer: <span class='text-danger'>'" + retailer + "'</span>, Month: <span class='text-danger'>'" + gmnth + "'</span>, ReportType: <span class='text-danger'>'" + rtype + "'</span></h6>";
                        //Logger.getLogger(CommercialReport.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        try {
                            con.close();
                        } catch (SQLException ex) {
                        }
                    }
                    out.println(err);
                    DecimalFormat formatter = new DecimalFormat("#,###.00");
                    out.println("<h4 style ='margin-top:2%;'>Sum RetailValue: <span style ='color:red'>" + formatter.format(rvaluesum) + " /=</span><br />Sum MancoValue: <span style ='color:red'>" + formatter.format(mvaluesum) + " /=</span><br />Sum Commission: <span style ='color:red'>" + formatter.format(cvaluesum) + " /=</span></h4>");
                } else if (rtype.equals("sales")) {
                    out.println("<div class='row'>");
                    out.println("<div class='col-lg-12 table-responsive'>");
                    out.println("<h6 class='text-center text-info text-capitalize'>Month: <span class='text-danger'>'" + gmnth + "'</span> ReportType: <span class='text-danger'>'Retailer Sales'</span></h6>");
                    out.println("<table class='table table-bordered fixed-header text-right' style='font-size:12px'>");
                    out.println("<thead>");
                    out.println("<tr>");
                    out.println("<th colspan='18' class='text-center' style='border: solid;'>Total Retailer Sales </th>");
                    out.println("</tr>");
                    out.println("<tr>");
                    out.println("<th style='border: solid;'>&nbsp;</th>");
                    out.println("<th style='border: solid;'>Retailer</th>");
                    out.println("<th colspan='4' class='text-center' style='border: solid;'>PostPaid</th>");
                    out.println("<th colspan='4' class='text-center' style='border: solid;'>PrePaid</th>");
                    out.println("<th colspan='4' class='text-center' style='border: solid;'>Totals</th>");
                    out.println("</tr>");
                    out.println("<tr>");
                    out.println("<th style='border: solid;'>No.</th>");
                    out.println("<th style='border-right: solid;border-bottom: solid;'>Name</th>");
                    out.println("<th style='border-bottom: solid;'>Retail Value (Kshs.)</th>");
                    out.println("<th style='border-bottom: solid;'>Transactions</th>");
                    out.println("<th style='border-bottom: solid;'>Average Value</th>");
                    out.println("<th style='border-right: solid;border-bottom: solid;'>Commission</th>");
                    out.println("<th style='border-bottom: solid;'>Retail Value (Kshs.)</th>");
                    out.println("<th style='border-bottom: solid;'>Transactions</th>");
                    out.println("<th style='border-bottom: solid;'>Average Value</th>");
                    out.println("<th style='border-right: solid;border-bottom: solid;'>Commission</th>");
                    out.println("<th style='border-bottom: solid;'>Total Retail Value (Kshs.)</th>");
                    out.println("<th style='border-bottom: solid;'>Total Transactions</th>");
                    out.println("<th style='border-bottom: solid;'>Average Value</th>");
                    out.println("<th style='border-right: solid;border-bottom: solid;'>Total Commission</th>");
                    out.println("</tr>");
                    out.println("</thead>");
                    out.println("<tbody>");
                    double fbretailvalue = 0.0;
                    int fbtrx = 0;
                    double fbavgv = 0.0;
                    double fbcomm = 0.0;
                    double feretailvalue = 0.0;
                    int fetrx = 0;
                    double feavgv = 0.0;
                    double fecomm = 0.0;
                    double ftretailvalue = 0.0;
                    int fttrx = 0;
                    double ftavgv = 0.0;
                    double ftcomm = 0.0;

                    String query = "SELECT `retailer` FROM `retailers` WHERE `retailer` LIKE '" + retailer.substring(0, 6) + "%'";
                    Connection con = data.connect();
                    DecimalFormat formatter = new DecimalFormat("#,###.00");
                    int rind = 1;
                    try {
                        System.out.println(query);
                        Statement st = con.createStatement();
                        ResultSet rs = st.executeQuery(query);
                        DateTimeFormatter otfm = DateTimeFormat.forPattern("yyyy-MM");
                        DateTime odt = otfm.parseDateTime(mnth);
                        DateTimeFormatter yfmt = DateTimeFormat.forPattern("yyyy");
                        String yy = yfmt.print(odt);
                        while (rs.next()) {
                            double bretailvalue = 0.0;
                            int btrx = 0;
                            double bavgv = 0.0;
                            double bcomm = 0.0;
                            double eretailvalue = 0.0;
                            int etrx = 0;
                            double eavgv = 0.0;
                            double ecomm = 0.0;
                            double tretailvalue = 0.0;
                            int ttrx = 0;
                            double tavgv = 0.0;
                            double tcomm = 0.0;

                            int rowCount = -1;
                            try {
                                Statement s = con.createStatement();
                                ResultSet r = s.executeQuery("SELECT COUNT(*) FROM `commercialTx" + rs.getString(1) + mnth.replaceAll("-", "") + "` WHERE `txtype`='b'");
                                // get the number of rows from the result set
                                r.next();
                                rowCount = r.getInt(1);
                            } catch (SQLException exxx) {
                                //err = "<h6 class='text-center text-info'>No Records Found for the search criteria Retailer: <span class='text-danger'>'" + retailer + "'</span>, Month: <span class='text-danger'>'" + gmnth + "'</span>, ReportType: <span class='text-danger'>'" + rtype + "'</span></h6>";
                            }

                            String query2 = "SELECT COALESCE(SUM(`retailvalue`),0.0),COALESCE(SUM(`commissionvalue`),0.0) FROM `commercialTx" + rs.getString(1) + mnth.replaceAll("-", "") + "` WHERE `txtype`='b'";
                            try {
                                Statement st2 = con.createStatement();
                                ResultSet rs2 = st2.executeQuery(query2);
                                while (rs2.next()) {
                                    bretailvalue = Double.parseDouble(rs2.getString(1));
                                    bcomm = Double.parseDouble(rs2.getString(2));
                                }
                            } catch (SQLException exxx) {
                            }

                            int erowCount = -1;
                            try {
                                Statement es = con.createStatement();
                                ResultSet er = es.executeQuery("SELECT COUNT(*) FROM `commercialTx" + rs.getString(1) + mnth.replaceAll("-", "") + "` WHERE `txtype`='e'");
                                // get the number of rows from the result set
                                er.next();
                                erowCount = er.getInt(1);
                            } catch (SQLException exc) {
                                //err = "<h6 class='text-center text-info'>No Records Found for the search criteria Retailer: <span class='text-danger'>'" + retailer + "'</span>, Month: <span class='text-danger'>'" + gmnth + "'</span>, ReportType: <span class='text-danger'>'" + rtype + "'</span></h6>";
                            }

                            String query3 = "SELECT COALESCE(SUM(`retailvalue`),0.0),COALESCE(SUM(`commissionvalue`),0.0) FROM `commercialTx" + rs.getString(1) + mnth.replaceAll("-", "") + "` WHERE `txtype`='e'";
                            try {
                                Statement st3 = con.createStatement();
                                ResultSet rs3 = st3.executeQuery(query3);
                                while (rs3.next()) {
                                    eretailvalue = Double.parseDouble(rs3.getString(1));
                                    ecomm = Double.parseDouble(rs3.getString(2));
                                }
                            } catch (SQLException exxx) {
                                //err = "<h6 class='text-center text-info'>No Records Found for the search criteria Retailer: <span class='text-danger'>'" + retailer + "'</span>, Month: <span class='text-danger'>'" + gmnth + "'</span>, ReportType: <span class='text-danger'>'" + rtype + "'</span></h6>";
                            }
                            if (rowCount > 0) {
                                btrx = rowCount;
                            }
                            if (btrx == 0) {
                                bavgv = 0.0;
                            } else {
                                bavgv = bretailvalue / btrx;
                            }
                            if (erowCount > 0) {
                                etrx = erowCount;
                            }
                            if (etrx == 0) {
                                eavgv = 0.0;
                            } else {
                                eavgv = eretailvalue / etrx;
                            }

                            tretailvalue = bretailvalue + eretailvalue;
                            ttrx = btrx + etrx;
                            if (bavgv == 0.0) {
                                tavgv = eavgv;
                            } else if (eavgv == 0.0) {
                                tavgv = eavgv;
                            } else if (bavgv > 0.0 && eavgv > 0.0) {
                                tavgv = (bavgv + eavgv) / 2;
                            }

                            tcomm = bcomm + ecomm;

                            out.println("<tr>");
                            out.println("<td style='border-left: solid;border-right: solid;'>" + rind + "</td>");
                            out.println("<td style='border-right: solid;'>" + rs.getString(1) + "</td>");
                            out.println("<td>" + formatter.format(bretailvalue) + "</td>");
                            out.println("<td>" + btrx + "</td>");
                            out.println("<td>" + formatter.format(bavgv) + "</td>");
                            out.println("<td style='border-right: solid;'>" + formatter.format(bcomm) + "</td>");
                            out.println("<td>" + formatter.format(eretailvalue) + "</td>");
                            out.println("<td>" + etrx + "</td>");
                            out.println("<td>" + formatter.format(eavgv) + "</td>");
                            out.println("<td style='border-right: solid;'>" + formatter.format(ecomm) + "</td>");
                            out.println("<td>" + formatter.format(tretailvalue) + "</td>");
                            out.println("<td>" + ttrx + "</td>");
                            out.println("<td>" + formatter.format(tavgv) + "</td>");
                            out.println("<td style='border-right: solid;'>" + formatter.format(tcomm) + "</td>");
                            out.println("</tr>");

                            fbretailvalue = fbretailvalue + bretailvalue;
                            fbtrx = fbtrx + btrx;
                            fbavgv = fbavgv + bavgv;
                            fbcomm = fbcomm + bcomm;
                            feretailvalue = feretailvalue + eretailvalue;
                            fetrx = fetrx + etrx;
                            feavgv = feavgv + eavgv;
                            fecomm = fecomm + ecomm;
                            ftretailvalue = ftretailvalue + tretailvalue;
                            fttrx = fttrx + ttrx;
                            ftavgv = ftavgv + tavgv;
                            ftcomm = ftcomm + tcomm;
                            rind++;
                        }
                    } catch (SQLException ex) {
                        //err = "<h6 class='text-center text-info'>No Records Found for the search criteria Retailer: <span class='text-danger'>'" + retailer + "'</span>, Month: <span class='text-danger'>'" + gmnth + "'</span>, ReportType: <span class='text-danger'>'" + rtype + "'</span></h6>";
                        //Logger.getLogger(CommercialReport.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    ftavgv = ftavgv / rind;
                    out.println("<tr>");
                    out.println("<td style='border: solid;'>&nbsp;</td>");
                    out.println("<td class='srtotals' style='border-top: solid;border-bottom: solid;border-right: solid;'>Totals</td>");
                    out.println("<td class='srtotals' style='border-top: solid;border-bottom: solid;'>" + formatter.format(fbretailvalue) + "</td>");
                    out.println("<td class='srtotals' style='border-top: solid;border-bottom: solid;'>" + fbtrx + "</td>");
                    out.println("<td class='srtotals' style='border-top: solid;border-bottom: solid;'>" + formatter.format(fbavgv) + "</td>");
                    out.println("<td class='srtotals' style='border-right: solid;border-top: solid;border-bottom: solid;'>" + formatter.format(fbcomm) + "</td>");
                    out.println("<td class='srtotals' style='border-top: solid;border-bottom: solid;'>" + formatter.format(feretailvalue) + "</td>");
                    out.println("<td class='srtotals' style='border-top: solid;border-bottom: solid;'>" + fetrx + "</td>");
                    out.println("<td class='srtotals' style='border-top: solid;border-bottom: solid;'>" + formatter.format(feavgv) + "</td>");
                    out.println("<td class='srtotals' style='border-right: solid;border-top: solid;border-bottom: solid;'>" + formatter.format(fecomm) + "</td>");
                    out.println("<td class='srtotals' style='border-top: solid;border-bottom: solid;'>" + formatter.format(ftretailvalue) + "</td>");
                    out.println("<td class='srtotals' style='border-top: solid;border-bottom: solid;'>" + fttrx + "</td>");
                    out.println("<td class='srtotals' style='border-top: solid;border-bottom: solid;'>" + formatter.format(ftavgv) + "</td>");
                    out.println("<td class='srtotals' style='border-right: solid;border-top: solid;border-bottom: solid;'>" + formatter.format(ftcomm) + "</td>");
                    out.println("</tr>");
                    out.println("</tbody>");
                    out.println("</table>");
                    out.println(err);
                    out.println("</div>");
                    out.println("</div>");

                    out.println("<div class='row' style='margin-top:2%;margin-bottom:4%;'>");
                    out.println("<div class='col-lg-6 col-lg-offset-3'>");
                    out.println("<div class='horizontalLine'></div>");
                    out.println("<h3 class='text-info text-center text-capitalize'>Notes</h3>");
                    out.println("<div class='horizontalLine'></div>");
                    data.createTables(mnth.replaceAll("-", ""));
                    String nquery = "SELECT `notes` FROM `ttrsalesnotes" + mnth.replaceAll("-", "") + "` WHERE `retailer` ='" + retailer.substring(0, 6) + "'";
                    try {
                        Statement st = con.createStatement();
                        ResultSet rs = st.executeQuery(nquery);
                        while (rs.next()) {
                            out.println(rs.getString(1));
                        }
                        con.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        try {
                            con.close();
                        } catch (SQLException ex) {
                        }
                    }
                    out.println("</div>");
                    out.println("</div>");
                } else if (rtype.equals("salesp")) {
                    if (retailer.startsWith("Select")) {
                        out.println("<h6 class='text-center text-danger'>Please select a Retailer</h6>");
                    } else {
                        Connection con = data.connect();
                        String rtlrsP = "create table if not exists rtlrsP(id INT NOT NULL AUTO_INCREMENT, rtime varchar(200), totalsales double default 0, primary key(id), unique(rtime))";
                        try {
                            Statement stm = con.createStatement();
                            stm.execute(rtlrsP);
                        } catch (SQLException ex) {
                        }
                        DateTimeFormatter ttfm = DateTimeFormat.forPattern("yyyy-MM");
                        DateTime ddt = ttfm.parseDateTime(mnth);
                        DateTime ccmonth = ddt.minusYears(1);
                        for (int i = 1; i <= 13; i++) {
                            DateTimeFormatter cmfmt = DateTimeFormat.forPattern("yyyy-MM");
                            String cmdt = cmfmt.print(ccmonth);

                            String query = "SELECT `retailer` FROM `retailers` WHERE `retailer` LIKE '" + retailer.substring(0, 6) + "%'";
                            try {
                                System.out.println(query);
                                Statement st = con.createStatement();
                                ResultSet rs = st.executeQuery(query);
                                double ftretailvalue = 0.0;
                                while (rs.next()) {
                                    System.out.println("Current Retailer: " + rs.getString(1));
                                    System.out.println("Month: " + cmdt);
                                    double bretailvalue = 0.0;
                                    double eretailvalue = 0.0;
                                    double tretailvalue = 0.0;

                                    String query2 = "SELECT COALESCE(SUM(`retailvalue`),0.0) FROM `commercialTx" + rs.getString(1) + cmdt.replaceAll("-", "") + "` WHERE `txtype`='b'";
                                    try {
                                        Statement st2 = con.createStatement();
                                        ResultSet rs2 = st2.executeQuery(query2);
                                        while (rs2.next()) {
                                            bretailvalue = Double.parseDouble(rs2.getString(1));
                                        }
                                    } catch (SQLException ex) {
                                    }
                                    System.out.println("Sum Postpaid: " + bretailvalue);
                                    String query3 = "SELECT COALESCE(SUM(`retailvalue`),0.0) FROM `commercialTx" + rs.getString(1) + cmdt.replaceAll("-", "") + "` WHERE `txtype`='e'";
                                    try {
                                        Statement st3 = con.createStatement();
                                        ResultSet rs3 = st3.executeQuery(query3);
                                        while (rs3.next()) {
                                            eretailvalue = Double.parseDouble(rs3.getString(1));
                                        }
                                    } catch (SQLException ex) {
                                    }
                                    System.out.println("Sum Prepaid: " + eretailvalue);
                                    tretailvalue = bretailvalue + eretailvalue;
                                    ftretailvalue = ftretailvalue + tretailvalue;
                                }
                                System.out.println("TotalSales: " + ftretailvalue);
                                try {
                                    String values = "insert into rtlrsP(rtime,totalsales) values (?,?)";
                                    PreparedStatement prep = con.prepareStatement(values);
                                    prep.setString(1, retailer.substring(0, 6) + cmdt.replaceAll("-", ""));
                                    prep.setDouble(2, ftretailvalue);
                                    prep.execute();
                                    prep.close();
                                } catch (SQLException ex) {
                                    try {
                                        String update = "update rtlrsP set totalsales = ? WHERE rtime = ?";
                                        PreparedStatement preparedStmt = con.prepareStatement(update);
                                        preparedStmt.setDouble(1, ftretailvalue);
                                        preparedStmt.setString(2, retailer.substring(0, 6) + cmdt.replaceAll("-", ""));
                                        preparedStmt.executeUpdate();
                                        System.out.println("Updated: " + retailer.substring(0, 6) + cmdt.replaceAll("-", ""));
                                    } catch (SQLException xx) {
                                    }
                                    //Logger.getLogger(Imports.class.getName()).log(Level.SEVERE, null, ex);
                                }

                            } catch (SQLException ex) {
                                err = "<h6 class='text-center text-info'>No Records Found for the search criteria Retailer: <span class='text-danger'>'" + retailer + "'</span>, Month: <span class='text-danger'>'" + gmnth + "'</span>, ReportType: <span class='text-danger'>'" + rtype + "'</span></h6>";
                                //Logger.getLogger(CommercialReport.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            ccmonth = ccmonth.plusMonths(1);
                        }
                        //display
                        String query0 = "SELECT `rtime` FROM `rtlrsP` WHERE `rtime` = '" + retailer.substring(0, 6) + mnth.replaceAll("-", "") + "'";
                        try {
                            //System.out.println(query0);
                            Statement st0 = con.createStatement();
                            ResultSet rs0 = st0.executeQuery(query0);
                            if (rs0.isBeforeFirst()) {
                                //found
                                if (!mnth.equals("none")) {
                                    DateTimeFormatter otfm = DateTimeFormat.forPattern("yyyy-MM");
                                    DateTime odt = otfm.parseDateTime(mnth);
                                    DateTime cmonth = odt.minusYears(1);
                                    DateTime cmonth2 = odt.minusYears(1);
                                    DateTime cmonth3 = odt.minusYears(1);
                                    DateTime cmonth4 = odt.minusYears(1);
                                    out.println("<div class='row'>");
                                    out.println("<div class='col-lg-12 table-responsive'>");
                                    out.println("<h6 class='text-center text-info text-capitalize'>ReportType: <span class='text-danger'>'Total Sales Performance'</span></h6>");
                                    out.println("<table class='table table-bordered fixed-header' style='font-size:12px'>");
                                    out.println("<thead>");
                                    out.println("<tr>");
                                    out.println("<th colspan='13' class='text-center' style='border: none;'>Total Sales</th>");
                                    out.println("</tr>");
                                    out.println("<tr>");
                                    out.println("<th>Month</th>");
                                    List<String> mtharr = new ArrayList<>();
                                    for (int i = 1; i <= 13; i++) {
                                        DateTimeFormatter yfmt = DateTimeFormat.forPattern("yy");
                                        String ydt = yfmt.print(cmonth);
                                        out.println("<th>" + cmonth.toString("MMM") + "/" + ydt + "</th>");
                                        mtharr.add(cmonth.toString());
                                        cmonth = cmonth.plusMonths(1);
                                    }
                                    out.println("</tr>");
                                    out.println("</thead>");
                                    out.println("<tbody>");
                                    out.println("<tr>");
                                    out.println("<td>Total sales</td>");
                                    DecimalFormat formatter = new DecimalFormat("#,###.00");
                                    for (int i = 1; i <= 13; i++) {
                                        DateTimeFormatter sfmt = DateTimeFormat.forPattern("yyyy-MM");
                                        String monthn = sfmt.print(cmonth2);
                                        String querya1 = "SELECT `rtime`,`totalsales` FROM `rtlrsP` WHERE `rtime` ='" + retailer.substring(0, 6) + monthn.replaceAll("-", "") + "'";
                                        //System.out.println(querya1);
                                        Statement sta = con.createStatement();
                                        ResultSet rsa = sta.executeQuery(querya1);
                                        if (rsa.isBeforeFirst()) {
                                            while (rsa.next()) {
                                                out.println("<td>" + formatter.format(rsa.getDouble(2)) + "</td>");
                                            }
                                        } else {
                                            out.println("<td>" + formatter.format(0.0) + "</td>");
                                        }
                                        cmonth2 = cmonth2.plusMonths(1);
                                    }
                                    out.println("</tr>");
                                    out.println("<tr>");
                                    out.println("<td>Percentage (%)  Growth in Total  Sales </td>");
                                    double prevmnthv = 0.0;
                                    for (int i = 1; i <= 13; i++) {
                                        DateTimeFormatter sfmt = DateTimeFormat.forPattern("yyyy-MM");
                                        String monthn = sfmt.print(cmonth3);
                                        String querya2 = "SELECT `rtime`,`totalsales` FROM `rtlrsP` WHERE `rtime` ='" + retailer.substring(0, 6) + monthn.replaceAll("-", "") + "'";
                                        //System.out.println(querya2);
                                        Statement sta = con.createStatement();
                                        ResultSet rsa = sta.executeQuery(querya2);
                                        if (rsa.isBeforeFirst()) {
                                            while (rsa.next()) {
                                                double res = 0.0;
                                                //System.out.println(">>>>Previous Month:"+formatter.format(prevmnthv));
                                                //System.out.println(">>>>Current Month:"+formatter.format(rsa.getDouble(2)));
                                                if (rsa.getDouble(2) > 0.0) {
                                                    res = (rsa.getDouble(2) - prevmnthv) / rsa.getDouble(2);
                                                } else {
                                                    res = (rsa.getDouble(2) - prevmnthv) / 1;
                                                }
                                                //System.out.println(">>>>Aveg Diff:"+formatter.format(res));
                                                DecimalFormat decim = new DecimalFormat("0.00");
                                                out.println("<td>" + decim.format(res * 100) + "%</td>");
                                                prevmnthv = rsa.getDouble(2);
                                            }
                                        } else {
                                            out.println("<td>" + formatter.format(0.0) + "%</td>");
                                        }
                                        cmonth3 = cmonth3.plusMonths(1);
                                    }
                                    out.println("</tr>");
                                    out.println("</tbody>");
                                    out.println("</table>");
                                    out.println("</div>");
                                    out.println("</div>");

                                    out.println("<div class='row'>");
                                    out.println("<div class='col-lg-12'>");
                                    out.println("<script type = 'text/javascript' src = 'https://www.google.com/jsapi'>");
                                    out.println("</script>");
                                    out.println("<script type = 'text/javascript'>");
                                    out.println("google.charts.load('current', {packages: ['corechart']});");
                                    out.println("</script>");
                                    out.println("<div id = 'container' style = 'width: 100%; height: 400px; margin: 0 auto'>");
                                    out.println("</div>");
                                    out.println("<script language = 'JavaScript'>");
                                    out.println("function drawChart() {");
                                    out.println("var data = new google.visualization.DataTable();");
                                    out.println("data.addColumn('string', 'Month');");
                                    out.println("data.addColumn('number', 'Sales');");
                                    out.println("data.addRows([");
                                    for (int i = 1; i <= 13; i++) {
                                        DateTimeFormatter sfmt = DateTimeFormat.forPattern("yyyy-MM");
                                        String monthn = sfmt.print(cmonth4);
                                        DateTimeFormatter yfmt = DateTimeFormat.forPattern("yyyy");
                                        String yn = yfmt.print(cmonth4);
                                        DateTimeFormatter mfmt = DateTimeFormat.forPattern("MM");
                                        String mn = mfmt.print(cmonth4);
                                        DateTimeFormatter gtfm = DateTimeFormat.forPattern("MMM yyyy");
                                        String gmn = gtfm.print(cmonth4);
                                        String querya1 = "SELECT `rtime`,`totalsales` FROM `rtlrsP` WHERE `rtime` ='" + retailer.substring(0, 6) + monthn.replaceAll("-", "") + "'";
                                        //System.out.println(mn);
                                        Statement sta = con.createStatement();
                                        ResultSet rsa = sta.executeQuery(querya1);
                                        if (rsa.isBeforeFirst()) {
                                            while (rsa.next()) {
                                                out.println("['" + gmn + "'," + rsa.getDouble(2) + "],");
                                            }
                                        } else {
                                            out.println("['" + gmn + "',0.0],");
                                        }
                                        cmonth4 = cmonth4.plusMonths(1);
                                    }
                                    out.println("]);");
                                    // Set chart options
                                    out.println("var options = {'title' : 'Total Monthly Sales',");
                                    out.println("hAxis: {");
                                    out.println("title: 'Month'");
                                    out.println("},");
                                    out.println("vAxis: {");
                                    out.println("title: 'Amount'");
                                    out.println("},");
                                    out.println("pointSize: 10,");
                                    out.println(" 'height':400");
                                    out.println("};");

                                    // Instantiate and draw the chart.
                                    out.println("var chart = new google.visualization.LineChart(document.getElementById('container'));");
                                    out.println("chart.draw(data, options);");
                                    out.println(" }");
                                    out.println("google.charts.setOnLoadCallback(drawChart);");
                                    out.println("</script>");
                                    out.println("</div>");
                                    out.println("</div>");

                                    out.println("<div class='row' style='margin-top:2%;margin-bottom:4%;'>");
                                    out.println("<div class='col-lg-6 col-lg-offset-3'>");
                                    out.println("<div class='horizontalLine'></div>");
                                    out.println("<h3 class='text-info text-center text-capitalize'>Notes</h3>");
                                    out.println("<div class='horizontalLine'></div>");
                                    data.createTables(mnth.replaceAll("-", ""));
                                    String query = "SELECT `notes` FROM `salesnotes" + mnth.replaceAll("-", "") + "` WHERE `retailer` ='" + retailer.substring(0, 6) + "'";
                                    try {
                                        Statement st = con.createStatement();
                                        ResultSet rs = st.executeQuery(query);
                                        while (rs.next()) {
                                            out.println(rs.getString(1));
                                        }
                                        con.close();
                                    } catch (SQLException ex) {
                                        Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    out.println("</div>");
                                    out.println("</div>");
                                } else {
                                    //err = "<h6 class='text-center text-info'>No Records Found for the search criteria Retailer: <span class='text-danger'>'" + retailer + "'</span>, Month: <span class='text-danger'>'" + mnth + "'</span>, ReportType: <span class='text-danger'>'" + rtype + "'</span></h6>";
                                }
                            }

                        } catch (SQLException ex) {
                            err = "<h6 class='text-center text-info'>No Records Found for the search criteria Retailer: <span class='text-danger'>'" + retailer + "'</span>, Month: <span class='text-danger'>'" + gmnth + "'</span>, ReportType: <span class='text-danger'>'" + rtype + "'</span></h6>";
                            //Logger.getLogger(CommercialReport.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        try {
                            con.close();
                        } catch (SQLException ex) {
                        }

                    }
                    out.println(err);
                } else {
                    err = "<h6 class='text-center text-info'>No Records Found for the search criteria Retailer: <span class='text-danger'>'" + retailer + "'</span>, Month: <span class='text-danger'>'" + gmnth + "'</span>, ReportType: <span class='text-danger'>'" + rtype + "'</span></h6>";
                    out.println(err);
                }
                out.println("</div>");
                out.println(data.footer);
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
