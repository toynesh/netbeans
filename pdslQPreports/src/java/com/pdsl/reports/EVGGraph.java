/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.reports;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author julius
 */
public class EVGGraph extends HttpServlet {

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
                String ret = (String) request.getSession().getAttribute("retailer");
                String rtlr = "Select...";
                if (!ret.equals("Admin")) {
                    rtlr = ret;
                }
                if (null != request.getParameter("retailer")) {
                    if (!request.getParameter("retailer").equals("")) {
                        rtlr = request.getParameter("retailer");
                    }
                }
                DateTime rdt = new DateTime().minusMonths(1);
                DateTimeFormatter rfm = DateTimeFormat.forPattern("MMM yyyy");
                String currdt = rfm.print(rdt);
                if (null != request.getParameter("mw")) {
                    currdt = request.getParameter("mw");
                }
                out.println(data.handleHeader("EVGGraph", rtlr, "none", currdt, fullname));
                out.println("<div id='tprint' class='col-lg-12' style='margin-top:1%'>");
                //String seldate = "2018-04-01";
                //DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
                //DateTime sdate = formatter.parseDateTime(seldate);
                DateTimeFormatter formatter = DateTimeFormat.forPattern("MMM yyyy");
                DateTime sdate = formatter.parseDateTime(currdt);
                DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM");
                String str = fmt.print(sdate);
                Connection con = data.connect();
                String stt = "no";
                String query0 = "SELECT `id`,`otime`,`status`,`notes` FROM `evg" + str.replaceAll("-", "") + "` ORDER BY `id` DESC LIMIT 1";
                try {
                    Statement st0 = con.createStatement();
                    ResultSet rs0 = st0.executeQuery(query0);
                    if (rs0.isBeforeFirst()) {
                        stt = "yes";
                    }
                } catch (SQLException exxx) {
                }
                if (stt.equals("no")) {
                    out.println("<h3 class='text-center text-primary' style='font-family: \'Poiret One\', cursive;'>KPLC Prepaid Uptime</h3>");
                    out.println("<h6 style='color:red;text-align:center;margin-top:10%'>No Records Found for the Selected Month " + currdt + "!</h6>");
                } else {
                    out.println("<div class='row'>");
                    out.println("<div class='col-lg-8  col-lg-offset-2'>");
                    out.println("<h4 class='text-primary text-center '>VendIT Prepaid Uptime</h4>");
                    out.println("<div class='horizontalLine'></div>");
                    DateTimeFormatter ttfm = DateTimeFormat.forPattern("yyyy-MM-dd");
                    DateTime ddt = ttfm.parseDateTime(str + "-01");
                    DateTime lastDate = ddt.dayOfMonth().withMaximumValue();
                    //System.out.println("Last Date: "+lastDate);
                    double ttbmonthdowntime = 0.0;
                    double ttbmonthaccb = 0.0;
                    double ttbmonthapp = 0.0;
                    int nodays = 0;
                    List<List<String>> table = new ArrayList<>();
                    for (DateTime date = ddt; date.isBefore(lastDate.plusDays(1)); date = date.plusDays(1)) {
                        String thstr = ttfm.print(date);
                        System.out.println("Month Date: " + thstr);
                        double bhrs = 0;
                        double bmins = 0;
                        double bsecs = 0;
                        double achrs = 0;
                        double acmins = 0;
                        double acsecs = 0;
                        double aphrs = 0;
                        double apmins = 0;
                        double apsecs = 0;
                        try {
                            String query = "SELECT `id`,`otime`,`hduration`,`mduration`,`sduration`,`status`,`notes` FROM `evg" + str.replaceAll("-", "") + "` WHERE `status`='PROBLEM' AND `otime` LIKE '" + thstr + "%' ORDER BY `id` DESC";
                            Statement st = con.createStatement();
                            ResultSet rs = st.executeQuery(query);
                            while (rs.next()) {
                                bhrs = bhrs + rs.getInt(3);
                                bmins = bmins + rs.getInt(4);
                                bsecs = bsecs + rs.getInt(5);
                            }
                        } catch (SQLException x) {
                        }
                        try {
                            String query = "SELECT `id`,`otime`,`hduration`,`mduration`,`sduration`,`status`,`notes` FROM `backend" + str.replaceAll("-", "") + "` WHERE `status`='PROBLEM' AND `otime` LIKE '" + thstr + "%' ORDER BY `id` DESC";
                            Statement st = con.createStatement();
                            ResultSet rs = st.executeQuery(query);
                            while (rs.next()) {
                                achrs = achrs + rs.getInt(3);
                                acmins = acmins + rs.getInt(4);
                                acsecs = acsecs + rs.getInt(5);
                            }
                        } catch (SQLException x) {
                            x.printStackTrace();
                        }
                        try {
                            String query = "SELECT `id`,`otime`,`hduration`,`mduration`,`sduration`,`status`,`notes` FROM `bizswitch" + str.replaceAll("-", "") + "` WHERE `status`='PROBLEM' AND `otime` LIKE '" + thstr + "%' ORDER BY `id` DESC";
                            Statement st = con.createStatement();
                            ResultSet rs = st.executeQuery(query);
                            while (rs.next()) {
                                aphrs = aphrs + rs.getInt(3);
                                apmins = apmins + rs.getInt(4);
                                apsecs = apsecs + rs.getInt(5);
                            }
                        } catch (SQLException x) {
                        }
                        double totalb = bsecs + (bmins * 60) + (bhrs * 60 * 60);
                        double totalac = acsecs + (acmins * 60) + (achrs * 60 * 60);
                        double totalap = apsecs + (apmins * 60) + (aphrs * 60 * 60);
                        ttbmonthdowntime = ttbmonthdowntime + totalb;
                        ttbmonthaccb = ttbmonthaccb + totalac;
                        ttbmonthapp = ttbmonthapp + totalap;
                        System.out.println("Bs: " + totalb);
                        System.out.println("ACs: " + totalac);
                        System.out.println("APs: " + totalap);
                        int secspd = 24 * 60 * 60;
                        DecimalFormat formatn = new DecimalFormat("#,###.00");
                        List<String> daterow = new ArrayList<>();
                        daterow.add(thstr);
                        daterow.add(formatn.format(((secspd - totalb) / secspd) * 100));
                        daterow.add(formatn.format(((secspd - totalac) / secspd) * 100));
                        daterow.add(formatn.format(((secspd - totalap) / secspd) * 100));
                        table.add(daterow);
                        nodays++;
                    }
                    out.println("<table class='table table-bordered fixed-header' style='font-size:12px'>");
                    out.println("<thead>");
                    out.println("<tr>");
                    out.println("<th>Date</th>");
                    out.println("<th>KPLC Prepaid EVG </th>");
                    out.println("<th>KPLC Prepaid Backend Server</th>");
                    out.println("<th>PDSL Bizswitch Application Status</th>");
                    out.println("</tr>");
                    out.println("</thead>");
                    out.println("<tbody>");
                    for (int y = 0; y < table.size(); y++) {
                        out.println("<tr>");
                        out.println("<td>" + table.get(y).get(0) + "</td>");
                        out.println("<td>" + table.get(y).get(1) + "%</td>");
                        out.println("<td>" + table.get(y).get(2) + "%</td>");
                        out.println("<td>" + table.get(y).get(3) + "%</td>");
                        out.println("</tr>");
                    }
                    out.println("</tbody>");
                    out.println("</table>");

                    out.println("</div>");
                    out.println("</div>");
                    out.println("<div class='row'>");
                    out.println("<div class='col-lg-12'>");
                    out.println("<div id = 'mcontainer' style = 'width: 100%; height: 400px;'></div>");
                    out.println("<script type = 'text/javascript'>");
                    out.println("google.charts.load('current', {packages: ['corechart']});");
                    out.println("</script>");
                    out.println("<script type='text/javascript'>");
                    out.println("function drawChart() {");
                    // Define the chart to be drawn.
                    out.println("var data = google.visualization.arrayToDataTable([");
                    out.println("['Month', 'Prepaid', 'Backend Server', 'PDSL'],");
                    for (int y = 0; y < table.size(); y++) {
                        DateTimeFormatter fh = DateTimeFormat.forPattern("yyyy-MM-dd");
                        DateTime tdate = fh.parseDateTime(table.get(y).get(0));
                        DateTimeFormatter dfmt = DateTimeFormat.forPattern("dd MMM");
                        String dstr = dfmt.print(tdate);
                        out.println("['" + dstr + "', " + table.get(y).get(1) + ", " + table.get(y).get(2) + ", " + table.get(y).get(3) + "],");
                    }
                    /*out.println("['Jun 01', 900, 390, 1600],");
                out.println("['Jun 02', 1000, 400, 1600],");
                out.println("['Jun 03', 1170, 440, 1600],");
                out.println("['Jun 04', 1250, 480, 1600],");
                out.println("['Jun 05', 1530, 540, 1600]");*/
                    out.println("]);");

                    out.println("var options = {title: 'VendIT Prepaid Uptime graph (in Percentage)',");
                    out.println("hAxis: {");
                    out.println("title: 'Days'");
                    out.println("},");
                    out.println("vAxis: {");
                    out.println("title: 'Uptime %'");
                    out.println("},");
                    out.println("};");

                    // Instantiate and draw the chart.
                    out.println("var chart = new google.visualization.ColumnChart(document.getElementById('mcontainer'));");
                    out.println("chart.draw(data, options);");
                    out.println("}");
                    out.println("google.charts.setOnLoadCallback(drawChart);");
                    out.println("</script>");

                    out.println("</div>");
                    out.println("</div>");

                    out.println("<div class='row' style='margin-top:2%;margin-bottom:4%;'>");
                    out.println("<div class='col-lg-8 col-lg-offset-2'>");
                    out.println("<div class='horizontalLine'></div>");
                    out.println("<h5 class='text-primary text-center text-uppercase'>KPLC PREPAID TOTAL VENDIT UPTIME PERCENTAGE " + currdt + "</h5>");
                    out.println("<table class='table table-bordered fixed-header text-right' style='font-size:15px'>");
                    out.println("<thead>");
                    out.println("<tr>");
                    out.println("<th class='text-center' style='border-top: solid;border-left: solid;'>Lose of Service Time</th>");
                    out.println("<th class='text-center' style='border-top: solid;'>Total Down Time</th>");
                    out.println("<th class='text-center' style='border-top: solid;border-right: solid;'>Uptime %</th>");
                    out.println("</tr>");
                    out.println("</thead>");
                    out.println("<tbody>");
                    DecimalFormat formatn = new DecimalFormat("#,###.00");
                    out.println("<tr>");
                    System.out.println("Total Days: " + nodays);
                    double ttmhrs = nodays * 24;
                    Duration bd = new Duration((int) ttbmonthdowntime * 1000L);  // milliseconds
                    Period bp = bd.toPeriod(PeriodType.yearDayTime());
                    Duration ad = new Duration((int) ttbmonthaccb * 1000L);  // milliseconds
                    Period ap = ad.toPeriod(PeriodType.yearDayTime());
                    Duration appd = new Duration((int) ttbmonthapp * 1000L);  // milliseconds
                    Period appp = appd.toPeriod(PeriodType.yearDayTime());
                    out.println("<td class='text-center' style='border-left:solid;'>EVG Connection Status (KPLC)</td>");
                    out.println("<td class='text-center'>" + bp.getHours() + "Hrs " + bp.getMinutes() + "Mins " + bp.getSeconds() + "Secs</td>");
                    out.println("<td class='text-center' style='border-right:solid;'>" + formatn.format(((ttmhrs - (ttbmonthdowntime / 60) / 60) / ttmhrs) * 100) + "</td>");
                    out.println("</tr>");
                    out.println("<tr>");
                    out.println("<td class='text-center' style='border-left:solid;'>Backend Status (KPLC)</td>");
                    out.println("<td class='text-center'>" + ap.getHours() + "Hrs " + ap.getMinutes() + "Mins " + ap.getSeconds() + "Secs</td>");
                    out.println("<td class='text-center' style='border-right:solid;'>" + formatn.format(((ttmhrs - (ttbmonthaccb / 60) / 60) / ttmhrs) * 100) + "</td>");
                    out.println("</tr>");
                    out.println("<tr>");
                    out.println("<td class='text-center' style='border-left: solid;border-bottom: solid;'>EVG Application Status (PDSL)</td>");
                    out.println("<td class='text-center' style='border-bottom: solid;'>" + appp.getHours() + "Hrs " + appp.getMinutes() + "Mins " + appp.getSeconds() + "Secs</td>");
                    out.println("<td class='text-center' style='border-right: solid;border-bottom: solid;'>" + formatn.format(((ttmhrs - (ttbmonthapp / 60) / 60) / ttmhrs) * 100) + "</td>");
                    out.println("</tr>");
                    out.println("</tbody>");
                    out.println("</table>");
                    out.println("</div>");
                    out.println("</div>");
                }
                try {
                    con.close();
                } catch (SQLException ef) {
                }
                out.println("</div>");
                out.println("</div>");
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
