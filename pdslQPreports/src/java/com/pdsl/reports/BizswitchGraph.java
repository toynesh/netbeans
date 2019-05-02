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
public class BizswitchGraph extends HttpServlet {

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
                out.println(data.handleHeader("BizswitchGraph", rtlr, "none", currdt, fullname));
                out.println("<button style='margin-top:3%' class='btn btn-primary pull-right' id=\"print\" onclick=\"printContent('tprint');\" ><i class='fas fa-print'></i> Print</button><br /><br />");
                out.println("<div id='tprint' class='col-lg-9' style='margin-top:1%'>");
                //String seldate = "2018-04-01";
                //DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
                //DateTime sdate = formatter.parseDateTime(seldate);
                DateTimeFormatter formatter = DateTimeFormat.forPattern("MMM yyyy");
                DateTime sdate = formatter.parseDateTime(currdt);
                DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM");
                String str = fmt.print(sdate);
                Connection con = data.connect();
                String stt = "no";
                String query0 = "SELECT `id`,`otime`,`status`,`notes` FROM `bizswitch" + str.replaceAll("-", "") + "` ORDER BY `id` DESC LIMIT 1";
                try {
                    Statement st0 = con.createStatement();
                    ResultSet rs0 = st0.executeQuery(query0);
                    if (rs0.isBeforeFirst()) {
                        stt = "yes";
                    }
                } catch (SQLException exxx) {
                }
                if (stt.equals("no")) {
                    out.println("<h3 class='text-center text-primary' style='font-family: \'Poiret One\', cursive;'>Bizswitch Uptime</h3>");
                    out.println("<h6 style='color:red;text-align:center;margin-top:10%'>No Records Found for the Selected Month " + currdt + "!</h6>");
                } else {
                    out.println("<div id = 'mapcontainer' style = 'width: 100%; height: 1200px;'>");
                    out.println("</div>");
                    out.println("<h3 class='text-center text-primary' style='font-family: 'Poiret One', cursive;'>Bizswitch Uptime</h3>");
                    out.println("<script type='text/javascript'>");
                    out.println("google.charts.load('current', {packages: ['timeline']});");
                    out.println("google.charts.setOnLoadCallback(drawChart);");
                    out.println("function drawChart() {");

                    out.println("var container = document.getElementById('mapcontainer');");
                    out.println("var chart = new google.visualization.Timeline(container);");
                    out.println("var dataTable = new google.visualization.DataTable();");
                    out.println("dataTable.addColumn({type: 'string', id: 'Time'});");
                    out.println("dataTable.addColumn({type: 'string', id: 'Date'});");
                    out.println("dataTable.addColumn({type: 'date', id: 'Start'});");
                    out.println("dataTable.addColumn({type: 'date', id: 'End'});");
                    out.println("dataTable.addRows([");
                    String query = "SELECT `id`,`otime`,`status`,`notes` FROM `bizswitch" + str.replaceAll("-", "") + "` ORDER BY `id` DESC";

                    int rowCount = -1;
                    try {
                        Statement s = con.createStatement();
                        ResultSet r = s.executeQuery("SELECT COUNT(*) FROM `bizswitch" + str.replaceAll("-", "") + "`");
                        // get the number of rows from the result set
                        r.next();
                        rowCount = r.getInt(1);
                    } catch (SQLException exxx) {
                    }
                    try {
                        Statement st = con.createStatement();
                        ResultSet rs = st.executeQuery(query);
                        String prevd = str + "-01 00:00:00";
                        System.out.println("Row Count: " + rowCount);
                        int counter = 1;
                        String pstatus = "OK";
                        while (rs.next()) {
                            //yyyy-MM-dd HH:mm:ss
                            DateTimeFormatter potfm = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                            DateTime podt = potfm.parseDateTime(prevd);
                            DateTimeFormatter pdfmt = DateTimeFormat.forPattern("yyyy-MM-dd");
                            String pdt = pdfmt.print(podt);
                            DateTimeFormatter pdyfmt = DateTimeFormat.forPattern("dd");
                            String pdy = pdyfmt.print(podt);
                            DateTimeFormatter phfmt = DateTimeFormat.forPattern("HH");
                            String phh = phfmt.print(podt);
                            DateTimeFormatter pmfmt = DateTimeFormat.forPattern("mm");
                            String pmm = pmfmt.print(podt);
                            pmm = pmm.replaceAll("00", "0");
                            DateTimeFormatter psfmt = DateTimeFormat.forPattern("ss");
                            String pss = psfmt.print(podt);
                            pss = pss.replaceAll("00", "0");
                            DateTimeFormatter otfm = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                            DateTime odt = otfm.parseDateTime(rs.getString(2));
                            DateTimeFormatter dfmt = DateTimeFormat.forPattern("yyyy-MM-dd");
                            String dt = dfmt.print(odt);
                            DateTimeFormatter dyfmt = DateTimeFormat.forPattern("dd");
                            String dy = dyfmt.print(odt);
                            DateTimeFormatter hfmt = DateTimeFormat.forPattern("HH");
                            String hh = hfmt.print(odt);
                            DateTimeFormatter mfmt = DateTimeFormat.forPattern("mm");
                            String mm = mfmt.print(odt);
                            DateTimeFormatter sfmt = DateTimeFormat.forPattern("ss");
                            String ss = sfmt.print(odt);
                            String note = "";
                            if (rs.getString(4) != null) {
                                if (!rs.getString(4).equals(" ")) {
                                    if (!rs.getString(4).equals("")) {
                                        note = " (" + rs.getString(4) + ")";
                                    }
                                }
                            }

                            String status = "PROBLEM";
                            if (rs.getString(3).equals("PROBLEM")) {
                                status = "OK";
                            }
                            String ostatus = "OK";
                            if (pstatus.equals("PROBLEM")) {
                                ostatus = "PROBLEM";
                            }
                            String lstatus = "PROBLEM";
                            if (status.equals("PROBLEM")) {
                                lstatus = "OK";
                            }
                            if (counter == rowCount) {
                                if (dt.equals(pdt)) {
                                    out.println("['" + odt.toString("MMM") + " " + dy + "', '" + status + note + "', new Date(0, 0, 0, " + phh + ", " + pmm + ", " + pss + "), new Date(0, 0, 0, " + hh + ", " + mm + ", " + ss + ")],");
                                    out.println("['" + odt.toString("MMM") + " " + dy + "', '" + lstatus + note + "', new Date(0, 0, 0, " + hh + ", " + mm + ", " + ss + "), new Date(0, 0, 0, 23, 59, 59)]]);");
                                    //System.out.println("['" + odt.toString("MMM") + " " + dy + "', '" + status + note + "', new Date(0, 0, 0, " + phh + ", " + pmm + ", " + pss + "), new Date(0, 0, 0, " + hh + ", " + mm + ", " + ss + ")],");
                                    //System.out.println("['" + odt.toString("MMM") + " " + dy + "', '" + lstatus + note + "', new Date(0, 0, 0, " + hh + ", " + mm + ", " + ss + "), new Date(0, 0, 0, 23, 59, 59)]]);");

                                } else {
                                    int prvd = Integer.parseInt(dy) - 1;
                                    int yestd = Integer.parseInt(pdy);
                                    if (prvd > 1) {
                                        if (prvd != yestd) {
                                            //System.out.println(prvd);
                                            if (prvd < 10) {
                                                out.println("['" + sdate.toString("MMM") + " 0" + prvd + note + "', 'OK', new Date(0, 0, 0, 00, 0, 0), new Date(0, 0, 0, 23, 59, 59)],");
                                            } else {
                                                out.println("['" + sdate.toString("MMM") + " " + prvd + note + "', 'OK', new Date(0, 0, 0, 00, 0, 0), new Date(0, 0, 0, 23, 59, 59)],");
                                            }
                                        }
                                    }
                                    out.println("['" + podt.toString("MMM") + " " + pdy + "', '" + ostatus + note + "', new Date(0, 0, 0, " + phh + ", " + pmm + ", " + pss + "), new Date(0, 0, 0, 23, 59, 59)],");
                                    out.println("['" + odt.toString("MMM") + " " + dy + "', '" + status + note + "', new Date(0, 0, 0, 00, 0, 0), new Date(0, 0, 0, " + hh + ", " + mm + ", " + ss + ")],");
                                    out.println("['" + odt.toString("MMM") + " " + dy + "', '" + lstatus + note + "', new Date(0, 0, 0, " + hh + ", " + mm + ", " + ss + "), new Date(0, 0, 0, 23, 59, 59)]]);");
                                    //System.out.println("['" + pdt + "', '" + ostatus + note + "', new Date(0, 0, 0, " + phh + ", " + pmm + ", " + pss + "), new Date(0, 0, 0, 23, 59, 59)],");
                                    //System.out.println("['" + odt.toString("MMM") + " " + dy + "', '" + status + note + "', new Date(0, 0, 0, 00, 0, 0), new Date(0, 0, 0, " + hh + ", " + mm + ", " + ss + ")],");
                                    //System.out.println("['" + odt.toString("MMM") + " " + dy + "', '" + lstatus + note + "', new Date(0, 0, 0, " + hh + ", " + mm + ", " + ss + "), new Date(0, 0, 0, 23, 59, 59)]]);");
                                }
                            } else {
                                if (dt.equals(pdt)) {
                                    out.println("['" + odt.toString("MMM") + " " + dy + "', '" + status + note + "', new Date(0, 0, 0, " + phh + ", " + pmm + ", " + pss + "), new Date(0, 0, 0, " + hh + ", " + mm + ", " + ss + ")],");
                                    //System.out.println("['" + odt.toString("MMM") + " " + dy + "', '" + status + note + "', new Date(0, 0, 0, " + phh + ", " + pmm + ", " + pss + "), new Date(0, 0, 0, " + hh + ", " + mm + ", " + ss + ")],");
                                } else {
                                    int prvd = Integer.parseInt(dy) - 1;
                                    int yestd = Integer.parseInt(pdy);
                                    if (prvd > 1) {
                                        if (prvd != yestd) {
                                            //System.out.println(prvd);
                                            if (prvd < 10) {//sdate
                                                out.println("['" + sdate.toString("MMM") + " 0" + prvd + note + "', 'OK', new Date(0, 0, 0, 00, 0, 0), new Date(0, 0, 0, 23, 59, 59)],");
                                            } else {
                                                out.println("['" + sdate.toString("MMM") + " " + prvd + note + "', 'OK', new Date(0, 0, 0, 00, 0, 0), new Date(0, 0, 0, 23, 59, 59)],");
                                            }
                                        }
                                    }
                                    out.println("['" + podt.toString("MMM") + " " + pdy + "', '" + ostatus + note + "', new Date(0, 0, 0, " + phh + ", " + pmm + ", " + pss + "), new Date(0, 0, 0, 23, 59, 59)],");
                                    out.println("['" + odt.toString("MMM") + " " + dy + "', '" + status + note + "', new Date(0, 0, 0, 00, 0, 0), new Date(0, 0, 0, " + hh + ", " + mm + ", " + ss + ")],");
                                    //System.out.println("['" + odt.toString("MMM") + " " + dy + "', '" + status + note + "', new Date(0, 0, 0, 00, 0, 0), new Date(0, 0, 0, " + hh + ", " + mm + ", " + ss + ")],");
                                }
                            }
                            prevd = rs.getString(2);
                            pstatus = rs.getString(3);
                            counter++;
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    out.println("var options = {");
                    out.println("timeline: {colorByColumnLabel: true, rowLabelStyle: { fontSize: 11 }, barLabelStyle: { fontSize: 10 }}");
                    out.println("};");

                    out.println("chart.draw(dataTable, options);");
                    out.println("}");
                    out.println("</script>");
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
