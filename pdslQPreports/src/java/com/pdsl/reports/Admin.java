/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.reports;

import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Paths;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author julius
 */
@WebServlet("/upload")
@MultipartConfig
public class Admin extends HttpServlet {

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
                String retlr = (String) request.getSession().getAttribute("retailer");
                if (!retlr.equals("Admin")) {
                    String redirectURL = "Login";
                    response.sendRedirect(redirectURL);
                }
                String outp = "";
                String uptype = "billuptime";
                if (null != request.getParameter("uptype")) {
                    if (!request.getParameter("uptype").equals("")) {
                        uptype = request.getParameter("uptype");
                        System.out.println(uptype);
                    } else {
                        //err = "<h3 class='text-center text-info'>Report Type!</h3>";
                    }
                } else {
                    //err = "<h3 class='text-center text-info'>Report Type!</h3>";
                }

                if (null != request.getParameter("nsupdate")) {
                    System.out.println("nsupdate");
                    String dtype = request.getParameter("dtype");
                    String currdate = request.getParameter("currdate");
                    if (dtype.equals("ttrsalesnotes")) {
                        String query = "SELECT `id` FROM `ttrsalesnotes" + currdate.replaceAll("-", "") + "`";
                        List<String> rids = new ArrayList<>();
                        try {
                            Connection con = data.connect();
                            Statement st = con.createStatement();
                            ResultSet rs = st.executeQuery(query);
                            while (rs.next()) {
                                rids.add(rs.getString(1));
                            }
                            con.close();
                        } catch (SQLException ex) {
                            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        for (int y = 0; y < rids.size(); y++) {
                            String update = "update ttrsalesnotes" + currdate.replaceAll("-", "") + " set notes = ? WHERE id = ?";
                            System.out.println(update);
                            try {
                                Connection con = data.connect();
                                PreparedStatement preparedStmt = con.prepareStatement(update);
                                preparedStmt.setString(1, request.getParameter(rids.get(y) + "notes"));
                                preparedStmt.setString(2, rids.get(y));
                                preparedStmt.executeUpdate();
                                con.close();
                                System.out.println(dtype);
                                System.out.println(rids.get(y) + " Updated(" + request.getParameter(rids.get(y) + "notes") + ")");
                            } catch (SQLException ex) {
                                Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    } else if (dtype.equals("salespnotes")) {
                        String query = "SELECT `id` FROM `salesnotes" + currdate.replaceAll("-", "") + "`";
                        List<String> rids = new ArrayList<>();
                        try {
                            Connection con = data.connect();
                            Statement st = con.createStatement();
                            ResultSet rs = st.executeQuery(query);
                            while (rs.next()) {
                                rids.add(rs.getString(1));
                            }
                            con.close();
                        } catch (SQLException ex) {
                            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        for (int y = 0; y < rids.size(); y++) {
                            String update = "update salesnotes" + currdate.replaceAll("-", "") + " set notes = ? WHERE id = ?";
                            System.out.println(update);
                            try {
                                Connection con = data.connect();
                                PreparedStatement preparedStmt = con.prepareStatement(update);
                                preparedStmt.setString(1, request.getParameter(rids.get(y) + "notes"));
                                preparedStmt.setString(2, rids.get(y));
                                preparedStmt.executeUpdate();
                                con.close();
                                System.out.println(dtype);
                                System.out.println(rids.get(y) + " Updated(" + request.getParameter(rids.get(y) + "notes") + ")");
                            } catch (SQLException ex) {
                                Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    } else {
                        String query = "SELECT `id` FROM `bill" + currdate.replaceAll("-", "") + "` ORDER BY `id` DESC";
                        if (dtype.equals("prepaid")) {
                            query = "SELECT `id` FROM `evg" + currdate.replaceAll("-", "") + "` ORDER BY `id` DESC";
                        }
                        List<String> rids = new ArrayList<>();
                        try {
                            Connection con = data.connect();
                            Statement st = con.createStatement();
                            ResultSet rs = st.executeQuery(query);
                            while (rs.next()) {
                                rids.add(rs.getString(1));
                            }
                            con.close();
                        } catch (SQLException ex) {
                            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        for (int y = 0; y < rids.size(); y++) {
                            String update = "update bill" + currdate.replaceAll("-", "") + " set notes = ? WHERE id = ?";
                            if (dtype.equals("prepaid")) {
                                update = "update evg" + currdate.replaceAll("-", "") + " set notes = ? WHERE id = ?";
                            }
                            System.out.println(update);
                            try {
                                Connection con = data.connect();
                                PreparedStatement preparedStmt = con.prepareStatement(update);
                                preparedStmt.setString(1, request.getParameter(rids.get(y) + "notes"));
                                preparedStmt.setString(2, rids.get(y));
                                preparedStmt.executeUpdate();
                                con.close();
                                System.out.println(rids.get(y) + " Updated(" + request.getParameter(rids.get(y) + "notes") + ")");
                            } catch (SQLException ex) {
                                Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
                if (null != request.getParameter("newsnotes")) {
                    System.out.println("notes");
                    String notes = request.getParameter("newsnotes");
                    String update = "update news set notes = ? WHERE id = ?";
                    try {
                        Connection con = data.connect();
                        PreparedStatement preparedStmt = con.prepareStatement(update);
                        preparedStmt.setString(1, notes);
                        preparedStmt.setInt(2, 1);
                        preparedStmt.executeUpdate();
                        con.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                out.println(data.adminhandleHeader(uptype));
                out.println("<div class='col-lg-9 table-responsive' style='margin-top:3%'>");

                if (null != request.getParameter("evgdescription")) {
                    String description = request.getParameter("evgdescription"); // Retrieves <input type="text" name="description">
                    Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
                    String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
                    InputStream fileContent = filePart.getInputStream();

                    System.out.println("Uploaded file:" + fileName);
                    String hpath = System.getProperty("user.home");
                    System.out.println("Home Path:" + hpath);

                    for (Part part : request.getParts()) {
                        if (part != null && part.getSize() > 0) {
                            String contentType = part.getContentType();
                            System.out.println("Content-Type:" + contentType);
                            /*if (!contentType.equalsIgnoreCase("text/csv")) {
                            out.println("<h3 style='color:red'>Please Upload a a valid csv File !!</h3>");
                            continue;
                        }*/
                            part.write(hpath + "/" + fileName);
                        }
                    }

                    String csvFile = hpath + "/" + fileName;
                    BufferedReader cbr = null;
                    BufferedReader br = null;
                    String cline = "";
                    String line = "";
                    String cvsSplitBy = ",";
                    try {
                        cbr = new BufferedReader(new FileReader(csvFile));
                        int cn = 1;
                        while ((cline = cbr.readLine()) != null) {
                            String[] cells = cline.split(cvsSplitBy);
                            if (cn == 2) {
                                String otime = cells[0].replace("\"", "");
                                DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                                DateTime sdate = formatter.parseDateTime(otime);
                                DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM");
                                String str = fmt.print(sdate);
                                System.out.println("Checking Table:" + "evg" + str.replaceAll("-", ""));
                                if (uptype.equals("evguptime")) {
                                    if (checkTableExist("evg" + str.replaceAll("-", ""))) {
                                        System.out.println("EVG True....");
                                        try {
                                            Connection con = data.connect();
                                            Statement stmt = con.createStatement();
                                            String sql = "DROP TABLE IF EXISTS evg" + str.replaceAll("-", "") + " ";
                                            stmt.executeUpdate(sql);
                                            con.close();
                                        } catch (SQLException ex) {
                                            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                }
                                if (uptype.equals("billuptime")) {
                                    if (checkTableExist("bill" + str.replaceAll("-", ""))) {
                                        System.out.println("Bill True....");
                                        try {
                                            Connection con = data.connect();
                                            Statement stmt = con.createStatement();
                                            String sql = "DROP TABLE IF EXISTS bill" + str.replaceAll("-", "") + " ";
                                            stmt.executeUpdate(sql);
                                            con.close();
                                        } catch (SQLException ex) {
                                            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                }
                                if (uptype.equals("bizswtime")) {
                                    if (checkTableExist("bizswitch" + str.replaceAll("-", ""))) {
                                        System.out.println("Bizswitch True....");
                                        try {
                                            Connection con = data.connect();
                                            Statement stmt = con.createStatement();
                                            String sql = "DROP TABLE IF EXISTS bizswitch" + str.replaceAll("-", "") + " ";
                                            stmt.executeUpdate(sql);
                                            con.close();
                                        } catch (SQLException ex) {
                                            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                }
                                if (uptype.equals("backend")) {
                                    if (checkTableExist("backend" + str.replaceAll("-", ""))) {
                                        System.out.println("Backend True....");
                                        try {
                                            Connection con = data.connect();
                                            Statement stmt = con.createStatement();
                                            String sql = "DROP TABLE IF EXISTS backend" + str.replaceAll("-", "") + " ";
                                            stmt.executeUpdate(sql);
                                            con.close();
                                        } catch (SQLException ex) {
                                            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                }
                                break;
                            }
                            cn++;
                        }

                        br = new BufferedReader(new FileReader(csvFile));
                        int counter = 1;
                        if (uptype.equals("evguptime")) {
                            while ((line = br.readLine()) != null) {
                                String[] cells = line.split(cvsSplitBy);
                                if (counter > 1) {
                                    String otime = cells[0].replace("\"", "");
                                    String status = cells[2].replace("\"", "");
                                    String severity = cells[3].replace("\"", "");
                                    String duration = cells[4].replace("\"", "");

                                    System.out.println(otime + "|" + status + "|" + severity + "|" + duration);

                                    String dduration = "0";
                                    String hduration = "0";
                                    String mduration = "0";
                                    String sduration = "0";
                                    if (duration.contains("d")) {
                                        if (duration.contains("h")) {
                                            if (duration.contains("m")) {
                                                if (duration.contains("s")) {
                                                    System.out.println("Comparison 1");
                                                    String[] dsplit = duration.split(" ");
                                                    for (int in = 0; in < dsplit.length; in++) {
                                                        if (dsplit[in].contains("d")) {
                                                            dduration = dsplit[in].replaceAll("d", "");
                                                        }
                                                        if (dsplit[in].contains("h")) {
                                                            hduration = dsplit[in].replaceAll("h", "");
                                                        }
                                                        if (dsplit[in].contains("m")) {
                                                            mduration = dsplit[in].replaceAll("m", "");
                                                        }
                                                        if (dsplit[in].contains("s")) {
                                                            sduration = dsplit[in].replaceAll("s", "");
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (duration.contains("d")) {
                                        if (duration.contains("h")) {
                                            if (duration.contains("m")) {
                                                System.out.println("Comparison 2");
                                                String[] dsplit = duration.split(" ");
                                                for (int in = 0; in < dsplit.length; in++) {
                                                    if (dsplit[in].contains("d")) {
                                                        dduration = dsplit[in].replaceAll("d", "");
                                                    }
                                                    if (dsplit[in].contains("h")) {
                                                        hduration = dsplit[in].replaceAll("h", "");
                                                    }
                                                    if (dsplit[in].contains("m")) {
                                                        mduration = dsplit[in].replaceAll("m", "");
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (duration.contains("d")) {
                                        if (duration.contains("h")) {
                                            if (!duration.contains("m")) {
                                                if (!duration.contains("s")) {
                                                    System.out.println("Comparison 3");
                                                    String[] dsplit = duration.split(" ");
                                                    for (int in = 0; in < dsplit.length; in++) {
                                                        if (dsplit[in].contains("d")) {
                                                            dduration = dsplit[in].replaceAll("d", "");
                                                        }
                                                        if (dsplit[in].contains("h")) {
                                                            hduration = dsplit[in].replaceAll("h", "");
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (duration.contains("d")) {
                                        if (!duration.contains("h")) {
                                            if (duration.contains("m")) {
                                                if (!duration.contains("s")) {
                                                    System.out.println("Comparison 4");
                                                    String[] dsplit = duration.split(" ");
                                                    for (int in = 0; in < dsplit.length; in++) {
                                                        if (dsplit[in].contains("d")) {
                                                            dduration = dsplit[in].replaceAll("d", "");
                                                        }
                                                        if (dsplit[in].contains("m")) {
                                                            mduration = dsplit[in].replaceAll("m", "");
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (!duration.contains("d")) {
                                        if (duration.contains("h")) {
                                            if (duration.contains("m")) {
                                                if (duration.contains("s")) {
                                                    System.out.println("Comparison 5");
                                                    String[] dsplit = duration.split(" ");
                                                    hduration = dsplit[0].replaceAll("h", "");
                                                    mduration = dsplit[1].replaceAll("m", "");
                                                    sduration = dsplit[2].replaceAll("s", "");
                                                }
                                            }
                                        }
                                    }
                                    if (!duration.contains("d")) {
                                        if (duration.contains("h")) {
                                            if (duration.contains("m")) {
                                                if (!duration.contains("s")) {
                                                    System.out.println("Comparison 6");
                                                    String[] dsplit = duration.split(" ");
                                                    hduration = dsplit[0].replaceAll("h", "");
                                                    mduration = dsplit[1].replaceAll("m", "");
                                                }
                                            }
                                        }
                                    }
                                    if (!duration.contains("d")) {
                                        if (!duration.contains("h")) {
                                            if (duration.contains("m")) {
                                                if (duration.contains("s")) {
                                                    System.out.println("Comparison 7");
                                                    String[] dsplit = duration.split(" ");
                                                    mduration = dsplit[0].replaceAll("m", "");
                                                    sduration = dsplit[1].replaceAll("s", "");
                                                }
                                            }
                                        }
                                    }
                                    if (duration.contains("d")) {
                                        if (!duration.contains("h")) {
                                            if (!duration.contains("m")) {
                                                if (!duration.contains("s")) {
                                                    System.out.println("Comparison 8");
                                                    dduration = duration.replaceAll("d", "");
                                                }
                                            }
                                        }
                                    }
                                    if (duration.contains("h")) {
                                        if (!duration.contains("d")) {
                                            if (!duration.contains("m")) {
                                                if (!duration.contains("s")) {
                                                    System.out.println("Comparison 9");
                                                    hduration = duration.replaceAll("h", "");
                                                }
                                            }
                                        }
                                    }
                                    if (duration.contains("m")) {
                                        if (!duration.contains("d")) {
                                            if (!duration.contains("h")) {
                                                if (!duration.contains("s")) {
                                                    System.out.println("Comparison 10");
                                                    mduration = duration.replaceAll("m", "");
                                                }
                                            }
                                        }
                                    }
                                    if (duration.contains("s")) {
                                        if (!duration.contains("d")) {
                                            if (!duration.contains("m")) {
                                                if (!duration.contains("h")) {
                                                    System.out.println("Comparison 11");
                                                    sduration = duration.replaceAll("s", "");
                                                }
                                            }
                                        }
                                    }
                                    //System.out.println("Date: " + otime);
                                    DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                                    DateTime sdate = formatter.parseDateTime(otime);
                                    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM");
                                    String str = fmt.print(sdate);

                                    //System.out.println("TableDate: " + str);
                                    try {
                                        Connection con = data.connect();
                                        String balcheck = "create table if not exists evg" + str.replaceAll("-", "") + "(id INT NOT NULL AUTO_INCREMENT, otime varchar(200), status varchar(200), severity varchar(200),duration varchar(200), dduration varchar(50), hduration varchar(50), mduration varchar(50), sduration varchar(50), notes text, primary key(id), unique(otime))";
                                        Statement stm = con.createStatement();
                                        stm.execute(balcheck);
                                        con.close();
                                    } catch (SQLException ex) {
                                        //Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    String values = "insert into evg" + str.replaceAll("-", "") + "(otime,status,severity,duration,dduration,hduration,mduration,sduration) values (?,?,?,?,?,?,?,?)";
                                    try {
                                        Connection con = data.connect();
                                        PreparedStatement prep = con.prepareStatement(values);
                                        prep.setString(1, otime);
                                        prep.setString(2, status);
                                        prep.setString(3, severity);
                                        prep.setString(4, duration);
                                        prep.setString(5, dduration);
                                        prep.setString(6, hduration);
                                        prep.setString(7, mduration);
                                        prep.setString(8, sduration);
                                        prep.execute();
                                        prep.close();
                                        con.close();
                                        outp = "<h3><span style='color:red'> " + fileName + " >>>Uptime File Uploaded!!</h3>";
                                    } catch (SQLException ex) {
                                        //Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                counter++;
                            }

                        } else if (uptype.equals("billuptime")) {
                            while ((line = br.readLine()) != null) {
                                String[] cells = line.split(cvsSplitBy);
                                if (counter > 1) {
                                    String otime = cells[0].replace("\"", "");
                                    String status = cells[2].replace("\"", "");
                                    String severity = cells[3].replace("\"", "");
                                    String duration = cells[4].replace("\"", "");

                                    System.out.println(otime + "|" + status + "|" + severity + "|" + duration);

                                    String dduration = "0";
                                    String hduration = "0";
                                    String mduration = "0";
                                    String sduration = "0";
                                    if (duration.contains("d")) {
                                        if (duration.contains("h")) {
                                            if (duration.contains("m")) {
                                                if (duration.contains("s")) {
                                                    System.out.println("Comparison 1");
                                                    String[] dsplit = duration.split(" ");
                                                    for (int in = 0; in < dsplit.length; in++) {
                                                        if (dsplit[in].contains("d")) {
                                                            dduration = dsplit[in].replaceAll("d", "");
                                                        }
                                                        if (dsplit[in].contains("h")) {
                                                            hduration = dsplit[in].replaceAll("h", "");
                                                        }
                                                        if (dsplit[in].contains("m")) {
                                                            mduration = dsplit[in].replaceAll("m", "");
                                                        }
                                                        if (dsplit[in].contains("s")) {
                                                            sduration = dsplit[in].replaceAll("s", "");
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (duration.contains("d")) {
                                        if (duration.contains("h")) {
                                            if (duration.contains("m")) {
                                                System.out.println("Comparison 2");
                                                String[] dsplit = duration.split(" ");
                                                for (int in = 0; in < dsplit.length; in++) {
                                                    if (dsplit[in].contains("d")) {
                                                        dduration = dsplit[in].replaceAll("d", "");
                                                    }
                                                    if (dsplit[in].contains("h")) {
                                                        hduration = dsplit[in].replaceAll("h", "");
                                                    }
                                                    if (dsplit[in].contains("m")) {
                                                        mduration = dsplit[in].replaceAll("m", "");
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (duration.contains("d")) {
                                        if (duration.contains("h")) {
                                            if (!duration.contains("m")) {
                                                if (!duration.contains("s")) {
                                                    System.out.println("Comparison 3");
                                                    String[] dsplit = duration.split(" ");
                                                    for (int in = 0; in < dsplit.length; in++) {
                                                        if (dsplit[in].contains("d")) {
                                                            dduration = dsplit[in].replaceAll("d", "");
                                                        }
                                                        if (dsplit[in].contains("h")) {
                                                            hduration = dsplit[in].replaceAll("h", "");
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (duration.contains("d")) {
                                        if (!duration.contains("h")) {
                                            if (duration.contains("m")) {
                                                if (!duration.contains("s")) {
                                                    System.out.println("Comparison 4");
                                                    String[] dsplit = duration.split(" ");
                                                    for (int in = 0; in < dsplit.length; in++) {
                                                        if (dsplit[in].contains("d")) {
                                                            dduration = dsplit[in].replaceAll("d", "");
                                                        }
                                                        if (dsplit[in].contains("m")) {
                                                            mduration = dsplit[in].replaceAll("m", "");
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (!duration.contains("d")) {
                                        if (duration.contains("h")) {
                                            if (duration.contains("m")) {
                                                if (duration.contains("s")) {
                                                    System.out.println("Comparison 5");
                                                    String[] dsplit = duration.split(" ");
                                                    hduration = dsplit[0].replaceAll("h", "");
                                                    mduration = dsplit[1].replaceAll("m", "");
                                                    sduration = dsplit[2].replaceAll("s", "");
                                                }
                                            }
                                        }
                                    }
                                    if (!duration.contains("d")) {
                                        if (duration.contains("h")) {
                                            if (duration.contains("m")) {
                                                if (!duration.contains("s")) {
                                                    System.out.println("Comparison 6");
                                                    String[] dsplit = duration.split(" ");
                                                    hduration = dsplit[0].replaceAll("h", "");
                                                    mduration = dsplit[1].replaceAll("m", "");
                                                }
                                            }
                                        }
                                    }
                                    if (!duration.contains("d")) {
                                        if (!duration.contains("h")) {
                                            if (duration.contains("m")) {
                                                if (duration.contains("s")) {
                                                    System.out.println("Comparison 7");
                                                    String[] dsplit = duration.split(" ");
                                                    mduration = dsplit[0].replaceAll("m", "");
                                                    sduration = dsplit[1].replaceAll("s", "");
                                                }
                                            }
                                        }
                                    }
                                    if (duration.contains("d")) {
                                        if (!duration.contains("h")) {
                                            if (!duration.contains("m")) {
                                                if (!duration.contains("s")) {
                                                    System.out.println("Comparison 8");
                                                    dduration = duration.replaceAll("d", "");
                                                }
                                            }
                                        }
                                    }
                                    if (duration.contains("h")) {
                                        if (!duration.contains("d")) {
                                            if (!duration.contains("m")) {
                                                if (!duration.contains("s")) {
                                                    System.out.println("Comparison 9");
                                                    hduration = duration.replaceAll("h", "");
                                                }
                                            }
                                        }
                                    }
                                    if (duration.contains("m")) {
                                        if (!duration.contains("d")) {
                                            if (!duration.contains("h")) {
                                                if (!duration.contains("s")) {
                                                    System.out.println("Comparison 10");
                                                    mduration = duration.replaceAll("m", "");
                                                }
                                            }
                                        }
                                    }
                                    if (duration.contains("s")) {
                                        if (!duration.contains("d")) {
                                            if (!duration.contains("m")) {
                                                if (!duration.contains("h")) {
                                                    System.out.println("Comparison 11");
                                                    sduration = duration.replaceAll("s", "");
                                                }
                                            }
                                        }
                                    }
                                    //System.out.println("Date: " + otime);
                                    DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                                    DateTime sdate = formatter.parseDateTime(otime);
                                    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM");
                                    String str = fmt.print(sdate);

                                    //System.out.println("TableDate: " + str);
                                    try {
                                        Connection con = data.connect();
                                        String balcheck = "create table if not exists bill" + str.replaceAll("-", "") + "(id INT NOT NULL AUTO_INCREMENT, otime varchar(200), status varchar(200), severity varchar(200),duration varchar(200), dduration varchar(50), hduration varchar(50), mduration varchar(50), sduration varchar(50), notes text, primary key(id), unique(otime))";
                                        Statement stm = con.createStatement();
                                        stm.execute(balcheck);
                                        con.close();
                                    } catch (SQLException ex) {
                                        //Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    String values = "insert into bill" + str.replaceAll("-", "") + "(otime,status,severity,duration,dduration,hduration,mduration,sduration) values (?,?,?,?,?,?,?,?)";
                                    try {
                                        Connection con = data.connect();
                                        PreparedStatement prep = con.prepareStatement(values);
                                        prep.setString(1, otime);
                                        prep.setString(2, status);
                                        prep.setString(3, severity);
                                        prep.setString(4, duration);
                                        prep.setString(5, dduration);
                                        prep.setString(6, hduration);
                                        prep.setString(7, mduration);
                                        prep.setString(8, sduration);
                                        prep.execute();
                                        prep.close();
                                        con.close();
                                        outp = "<h3><span style='color:red'> " + fileName + " >>>Uptime File Uploaded!!</h3>";
                                    } catch (SQLException ex) {
                                        //Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                counter++;
                            }
                        } else if (uptype.equals("bizswtime")) {
                            while ((line = br.readLine()) != null) {
                                String[] cells = line.split(cvsSplitBy);
                                if (counter > 1) {
                                    String otime = cells[0].replace("\"", "");
                                    String status = cells[2].replace("\"", "");
                                    String severity = cells[3].replace("\"", "");
                                    String duration = cells[4].replace("\"", "");

                                    System.out.println(otime + "|" + status + "|" + severity + "|" + duration);

                                    String dduration = "0";
                                    String hduration = "0";
                                    String mduration = "0";
                                    String sduration = "0";
                                    if (duration.contains("d")) {
                                        if (duration.contains("h")) {
                                            if (duration.contains("m")) {
                                                if (duration.contains("s")) {
                                                    System.out.println("Comparison 1");
                                                    String[] dsplit = duration.split(" ");
                                                    for (int in = 0; in < dsplit.length; in++) {
                                                        if (dsplit[in].contains("d")) {
                                                            dduration = dsplit[in].replaceAll("d", "");
                                                        }
                                                        if (dsplit[in].contains("h")) {
                                                            hduration = dsplit[in].replaceAll("h", "");
                                                        }
                                                        if (dsplit[in].contains("m")) {
                                                            mduration = dsplit[in].replaceAll("m", "");
                                                        }
                                                        if (dsplit[in].contains("s")) {
                                                            sduration = dsplit[in].replaceAll("s", "");
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (duration.contains("d")) {
                                        if (duration.contains("h")) {
                                            if (duration.contains("m")) {
                                                System.out.println("Comparison 2");
                                                String[] dsplit = duration.split(" ");
                                                for (int in = 0; in < dsplit.length; in++) {
                                                    if (dsplit[in].contains("d")) {
                                                        dduration = dsplit[in].replaceAll("d", "");
                                                    }
                                                    if (dsplit[in].contains("h")) {
                                                        hduration = dsplit[in].replaceAll("h", "");
                                                    }
                                                    if (dsplit[in].contains("m")) {
                                                        mduration = dsplit[in].replaceAll("m", "");
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (duration.contains("d")) {
                                        if (duration.contains("h")) {
                                            if (!duration.contains("m")) {
                                                if (!duration.contains("s")) {
                                                    System.out.println("Comparison 3");
                                                    String[] dsplit = duration.split(" ");
                                                    for (int in = 0; in < dsplit.length; in++) {
                                                        if (dsplit[in].contains("d")) {
                                                            dduration = dsplit[in].replaceAll("d", "");
                                                        }
                                                        if (dsplit[in].contains("h")) {
                                                            hduration = dsplit[in].replaceAll("h", "");
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (duration.contains("d")) {
                                        if (!duration.contains("h")) {
                                            if (duration.contains("m")) {
                                                if (!duration.contains("s")) {
                                                    System.out.println("Comparison 4");
                                                    String[] dsplit = duration.split(" ");
                                                    for (int in = 0; in < dsplit.length; in++) {
                                                        if (dsplit[in].contains("d")) {
                                                            dduration = dsplit[in].replaceAll("d", "");
                                                        }
                                                        if (dsplit[in].contains("m")) {
                                                            mduration = dsplit[in].replaceAll("m", "");
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (!duration.contains("d")) {
                                        if (duration.contains("h")) {
                                            if (duration.contains("m")) {
                                                if (duration.contains("s")) {
                                                    System.out.println("Comparison 5");
                                                    String[] dsplit = duration.split(" ");
                                                    hduration = dsplit[0].replaceAll("h", "");
                                                    mduration = dsplit[1].replaceAll("m", "");
                                                    sduration = dsplit[2].replaceAll("s", "");
                                                }
                                            }
                                        }
                                    }
                                    if (!duration.contains("d")) {
                                        if (duration.contains("h")) {
                                            if (duration.contains("m")) {
                                                if (!duration.contains("s")) {
                                                    System.out.println("Comparison 6");
                                                    String[] dsplit = duration.split(" ");
                                                    hduration = dsplit[0].replaceAll("h", "");
                                                    mduration = dsplit[1].replaceAll("m", "");
                                                }
                                            }
                                        }
                                    }
                                    if (!duration.contains("d")) {
                                        if (!duration.contains("h")) {
                                            if (duration.contains("m")) {
                                                if (duration.contains("s")) {
                                                    System.out.println("Comparison 7");
                                                    String[] dsplit = duration.split(" ");
                                                    mduration = dsplit[0].replaceAll("m", "");
                                                    sduration = dsplit[1].replaceAll("s", "");
                                                }
                                            }
                                        }
                                    }
                                    if (duration.contains("d")) {
                                        if (!duration.contains("h")) {
                                            if (!duration.contains("m")) {
                                                if (!duration.contains("s")) {
                                                    System.out.println("Comparison 8");
                                                    dduration = duration.replaceAll("d", "");
                                                }
                                            }
                                        }
                                    }
                                    if (duration.contains("h")) {
                                        if (!duration.contains("d")) {
                                            if (!duration.contains("m")) {
                                                if (!duration.contains("s")) {
                                                    System.out.println("Comparison 9");
                                                    hduration = duration.replaceAll("h", "");
                                                }
                                            }
                                        }
                                    }
                                    if (duration.contains("m")) {
                                        if (!duration.contains("d")) {
                                            if (!duration.contains("h")) {
                                                if (!duration.contains("s")) {
                                                    System.out.println("Comparison 10");
                                                    mduration = duration.replaceAll("m", "");
                                                }
                                            }
                                        }
                                    }
                                    if (duration.contains("s")) {
                                        if (!duration.contains("d")) {
                                            if (!duration.contains("m")) {
                                                if (!duration.contains("h")) {
                                                    System.out.println("Comparison 11");
                                                    sduration = duration.replaceAll("s", "");
                                                }
                                            }
                                        }
                                    }
                                    //System.out.println("Date: " + otime);
                                    DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                                    DateTime sdate = formatter.parseDateTime(otime);
                                    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM");
                                    String str = fmt.print(sdate);

                                    //System.out.println("TableDate: " + str);
                                    try {
                                        Connection con = data.connect();
                                        String balcheck = "create table if not exists bizswitch" + str.replaceAll("-", "") + "(id INT NOT NULL AUTO_INCREMENT, otime varchar(200), status varchar(200), severity varchar(200),duration varchar(200), dduration varchar(50), hduration varchar(50), mduration varchar(50), sduration varchar(50), notes text, primary key(id), unique(otime))";
                                        Statement stm = con.createStatement();
                                        stm.execute(balcheck);
                                        con.close();
                                    } catch (SQLException ex) {
                                        //Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    String values = "insert into bizswitch" + str.replaceAll("-", "") + "(otime,status,severity,duration,dduration,hduration,mduration,sduration) values (?,?,?,?,?,?,?,?)";
                                    try {
                                        Connection con = data.connect();
                                        PreparedStatement prep = con.prepareStatement(values);
                                        prep.setString(1, otime);
                                        prep.setString(2, status);
                                        prep.setString(3, severity);
                                        prep.setString(4, duration);
                                        prep.setString(5, dduration);
                                        prep.setString(6, hduration);
                                        prep.setString(7, mduration);
                                        prep.setString(8, sduration);
                                        prep.execute();
                                        prep.close();
                                        con.close();
                                        outp = "<h3><span style='color:red'> " + fileName + " >>>Uptime File Uploaded!!</h3>";
                                    } catch (SQLException ex) {
                                        //Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                counter++;
                            }
                        } else if (uptype.equals("backend")) {
                            while ((line = br.readLine()) != null) {
                                String[] cells = line.split(cvsSplitBy);
                                if (counter > 1) {
                                    String otime = cells[0].replace("\"", "");
                                    String status = cells[2].replace("\"", "");
                                    String severity = cells[3].replace("\"", "");
                                    String duration = cells[4].replace("\"", "");

                                    System.out.println(otime + "|" + status + "|" + severity + "|" + duration);

                                    String dduration = "0";
                                    String hduration = "0";
                                    String mduration = "0";
                                    String sduration = "0";
                                    if (duration.contains("d")) {
                                        if (duration.contains("h")) {
                                            if (duration.contains("m")) {
                                                if (duration.contains("s")) {
                                                    System.out.println("Comparison 1");
                                                    String[] dsplit = duration.split(" ");
                                                    for (int in = 0; in < dsplit.length; in++) {
                                                        if (dsplit[in].contains("d")) {
                                                            dduration = dsplit[in].replaceAll("d", "");
                                                        }
                                                        if (dsplit[in].contains("h")) {
                                                            hduration = dsplit[in].replaceAll("h", "");
                                                        }
                                                        if (dsplit[in].contains("m")) {
                                                            mduration = dsplit[in].replaceAll("m", "");
                                                        }
                                                        if (dsplit[in].contains("s")) {
                                                            sduration = dsplit[in].replaceAll("s", "");
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (duration.contains("d")) {
                                        if (duration.contains("h")) {
                                            if (duration.contains("m")) {
                                                System.out.println("Comparison 2");
                                                String[] dsplit = duration.split(" ");
                                                for (int in = 0; in < dsplit.length; in++) {
                                                    if (dsplit[in].contains("d")) {
                                                        dduration = dsplit[in].replaceAll("d", "");
                                                    }
                                                    if (dsplit[in].contains("h")) {
                                                        hduration = dsplit[in].replaceAll("h", "");
                                                    }
                                                    if (dsplit[in].contains("m")) {
                                                        mduration = dsplit[in].replaceAll("m", "");
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (duration.contains("d")) {
                                        if (duration.contains("h")) {
                                            if (!duration.contains("m")) {
                                                if (!duration.contains("s")) {
                                                    System.out.println("Comparison 3");
                                                    String[] dsplit = duration.split(" ");
                                                    for (int in = 0; in < dsplit.length; in++) {
                                                        if (dsplit[in].contains("d")) {
                                                            dduration = dsplit[in].replaceAll("d", "");
                                                        }
                                                        if (dsplit[in].contains("h")) {
                                                            hduration = dsplit[in].replaceAll("h", "");
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (duration.contains("d")) {
                                        if (!duration.contains("h")) {
                                            if (duration.contains("m")) {
                                                if (!duration.contains("s")) {
                                                    System.out.println("Comparison 4");
                                                    String[] dsplit = duration.split(" ");
                                                    for (int in = 0; in < dsplit.length; in++) {
                                                        if (dsplit[in].contains("d")) {
                                                            dduration = dsplit[in].replaceAll("d", "");
                                                        }
                                                        if (dsplit[in].contains("m")) {
                                                            mduration = dsplit[in].replaceAll("m", "");
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (!duration.contains("d")) {
                                        if (duration.contains("h")) {
                                            if (duration.contains("m")) {
                                                if (duration.contains("s")) {
                                                    System.out.println("Comparison 5");
                                                    String[] dsplit = duration.split(" ");
                                                    hduration = dsplit[0].replaceAll("h", "");
                                                    mduration = dsplit[1].replaceAll("m", "");
                                                    sduration = dsplit[2].replaceAll("s", "");
                                                }
                                            }
                                        }
                                    }
                                    if (!duration.contains("d")) {
                                        if (duration.contains("h")) {
                                            if (duration.contains("m")) {
                                                if (!duration.contains("s")) {
                                                    System.out.println("Comparison 6");
                                                    String[] dsplit = duration.split(" ");
                                                    hduration = dsplit[0].replaceAll("h", "");
                                                    mduration = dsplit[1].replaceAll("m", "");
                                                }
                                            }
                                        }
                                    }
                                    if (!duration.contains("d")) {
                                        if (!duration.contains("h")) {
                                            if (duration.contains("m")) {
                                                if (duration.contains("s")) {
                                                    System.out.println("Comparison 7");
                                                    String[] dsplit = duration.split(" ");
                                                    mduration = dsplit[0].replaceAll("m", "");
                                                    sduration = dsplit[1].replaceAll("s", "");
                                                }
                                            }
                                        }
                                    }
                                    if (duration.contains("d")) {
                                        if (!duration.contains("h")) {
                                            if (!duration.contains("m")) {
                                                if (!duration.contains("s")) {
                                                    System.out.println("Comparison 8");
                                                    dduration = duration.replaceAll("d", "");
                                                }
                                            }
                                        }
                                    }
                                    if (duration.contains("h")) {
                                        if (!duration.contains("d")) {
                                            if (!duration.contains("m")) {
                                                if (!duration.contains("s")) {
                                                    System.out.println("Comparison 9");
                                                    hduration = duration.replaceAll("h", "");
                                                }
                                            }
                                        }
                                    }
                                    if (duration.contains("m")) {
                                        if (!duration.contains("d")) {
                                            if (!duration.contains("h")) {
                                                if (!duration.contains("s")) {
                                                    System.out.println("Comparison 10");
                                                    mduration = duration.replaceAll("m", "");
                                                }
                                            }
                                        }
                                    }
                                    if (duration.contains("s")) {
                                        if (!duration.contains("d")) {
                                            if (!duration.contains("m")) {
                                                if (!duration.contains("h")) {
                                                    System.out.println("Comparison 11");
                                                    sduration = duration.replaceAll("s", "");
                                                }
                                            }
                                        }
                                    }
                                    //System.out.println("Date: " + otime);
                                    DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                                    DateTime sdate = formatter.parseDateTime(otime);
                                    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM");
                                    String str = fmt.print(sdate);

                                    //System.out.println("TableDate: " + str);
                                    try {
                                        Connection con = data.connect();
                                        String balcheck = "create table if not exists backend" + str.replaceAll("-", "") + "(id INT NOT NULL AUTO_INCREMENT, otime varchar(200), status varchar(200), severity varchar(200),duration varchar(200), dduration varchar(50), hduration varchar(50), mduration varchar(50), sduration varchar(50), notes text, primary key(id), unique(otime))";
                                        Statement stm = con.createStatement();
                                        stm.execute(balcheck);
                                        con.close();
                                    } catch (SQLException ex) {
                                        //Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    String values = "insert into backend" + str.replaceAll("-", "") + "(otime,status,severity,duration,dduration,hduration,mduration,sduration) values (?,?,?,?,?,?,?,?)";
                                    try {
                                        Connection con = data.connect();
                                        PreparedStatement prep = con.prepareStatement(values);
                                        prep.setString(1, otime);
                                        prep.setString(2, status);
                                        prep.setString(3, severity);
                                        prep.setString(4, duration);
                                        prep.setString(5, dduration);
                                        prep.setString(6, hduration);
                                        prep.setString(7, mduration);
                                        prep.setString(8, sduration);
                                        prep.execute();
                                        prep.close();
                                        con.close();
                                        outp = "<h3><span style='color:red'> " + fileName + " >>>Uptime File Uploaded!!</h3>";
                                    } catch (SQLException ex) {
                                        //Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                counter++;
                            }
                        } else if (uptype.equals("balcheck")) {
                            while ((line = br.readLine()) != null) {
                                String[] cells = line.split(cvsSplitBy);
                                if (counter > 1) {
                                    String otime = cells[0].replace("\"", "");
                                    String status = cells[2].replace("\"", "");
                                    String severity = cells[3].replace("\"", "");
                                    String duration = cells[4].replace("\"", "");

                                    System.out.println(otime + "|" + status + "|" + severity + "|" + duration);

                                    String dduration = "0";
                                    String hduration = "0";
                                    String mduration = "0";
                                    String sduration = "0";
                                    if (duration.contains("d")) {
                                        if (duration.contains("h")) {
                                            if (duration.contains("m")) {
                                                if (duration.contains("s")) {
                                                    System.out.println("Comparison 1");
                                                    String[] dsplit = duration.split(" ");
                                                    for (int in = 0; in < dsplit.length; in++) {
                                                        if (dsplit[in].contains("d")) {
                                                            dduration = dsplit[in].replaceAll("d", "");
                                                        }
                                                        if (dsplit[in].contains("h")) {
                                                            hduration = dsplit[in].replaceAll("h", "");
                                                        }
                                                        if (dsplit[in].contains("m")) {
                                                            mduration = dsplit[in].replaceAll("m", "");
                                                        }
                                                        if (dsplit[in].contains("s")) {
                                                            sduration = dsplit[in].replaceAll("s", "");
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (duration.contains("d")) {
                                        if (duration.contains("h")) {
                                            if (duration.contains("m")) {
                                                System.out.println("Comparison 2");
                                                String[] dsplit = duration.split(" ");
                                                for (int in = 0; in < dsplit.length; in++) {
                                                    if (dsplit[in].contains("d")) {
                                                        dduration = dsplit[in].replaceAll("d", "");
                                                    }
                                                    if (dsplit[in].contains("h")) {
                                                        hduration = dsplit[in].replaceAll("h", "");
                                                    }
                                                    if (dsplit[in].contains("m")) {
                                                        mduration = dsplit[in].replaceAll("m", "");
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (duration.contains("d")) {
                                        if (duration.contains("h")) {
                                            if (!duration.contains("m")) {
                                                if (!duration.contains("s")) {
                                                    System.out.println("Comparison 3");
                                                    String[] dsplit = duration.split(" ");
                                                    for (int in = 0; in < dsplit.length; in++) {
                                                        if (dsplit[in].contains("d")) {
                                                            dduration = dsplit[in].replaceAll("d", "");
                                                        }
                                                        if (dsplit[in].contains("h")) {
                                                            hduration = dsplit[in].replaceAll("h", "");
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (duration.contains("d")) {
                                        if (!duration.contains("h")) {
                                            if (duration.contains("m")) {
                                                if (!duration.contains("s")) {
                                                    System.out.println("Comparison 4");
                                                    String[] dsplit = duration.split(" ");
                                                    for (int in = 0; in < dsplit.length; in++) {
                                                        if (dsplit[in].contains("d")) {
                                                            dduration = dsplit[in].replaceAll("d", "");
                                                        }
                                                        if (dsplit[in].contains("m")) {
                                                            mduration = dsplit[in].replaceAll("m", "");
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (!duration.contains("d")) {
                                        if (duration.contains("h")) {
                                            if (duration.contains("m")) {
                                                if (duration.contains("s")) {
                                                    System.out.println("Comparison 5");
                                                    String[] dsplit = duration.split(" ");
                                                    hduration = dsplit[0].replaceAll("h", "");
                                                    mduration = dsplit[1].replaceAll("m", "");
                                                    sduration = dsplit[2].replaceAll("s", "");
                                                }
                                            }
                                        }
                                    }
                                    if (!duration.contains("d")) {
                                        if (duration.contains("h")) {
                                            if (duration.contains("m")) {
                                                if (!duration.contains("s")) {
                                                    System.out.println("Comparison 6");
                                                    String[] dsplit = duration.split(" ");
                                                    hduration = dsplit[0].replaceAll("h", "");
                                                    mduration = dsplit[1].replaceAll("m", "");
                                                }
                                            }
                                        }
                                    }
                                    if (!duration.contains("d")) {
                                        if (!duration.contains("h")) {
                                            if (duration.contains("m")) {
                                                if (duration.contains("s")) {
                                                    System.out.println("Comparison 7");
                                                    String[] dsplit = duration.split(" ");
                                                    mduration = dsplit[0].replaceAll("m", "");
                                                    sduration = dsplit[1].replaceAll("s", "");
                                                }
                                            }
                                        }
                                    }
                                    if (duration.contains("d")) {
                                        if (!duration.contains("h")) {
                                            if (!duration.contains("m")) {
                                                if (!duration.contains("s")) {
                                                    System.out.println("Comparison 8");
                                                    dduration = duration.replaceAll("d", "");
                                                }
                                            }
                                        }
                                    }
                                    if (duration.contains("h")) {
                                        if (!duration.contains("d")) {
                                            if (!duration.contains("m")) {
                                                if (!duration.contains("s")) {
                                                    System.out.println("Comparison 9");
                                                    hduration = duration.replaceAll("h", "");
                                                }
                                            }
                                        }
                                    }
                                    if (duration.contains("m")) {
                                        if (!duration.contains("d")) {
                                            if (!duration.contains("h")) {
                                                if (!duration.contains("s")) {
                                                    System.out.println("Comparison 10");
                                                    mduration = duration.replaceAll("m", "");
                                                }
                                            }
                                        }
                                    }
                                    if (duration.contains("s")) {
                                        if (!duration.contains("d")) {
                                            if (!duration.contains("m")) {
                                                if (!duration.contains("h")) {
                                                    System.out.println("Comparison 11");
                                                    sduration = duration.replaceAll("s", "");
                                                }
                                            }
                                        }
                                    }
                                    //System.out.println("Date: " + otime);
                                    DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                                    DateTime sdate = formatter.parseDateTime(otime);
                                    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM");
                                    String str = fmt.print(sdate);

                                    //System.out.println("TableDate: " + str);
                                    try {
                                        Connection con = data.connect();
                                        String balcheck = "create table if not exists balcheck(id INT NOT NULL AUTO_INCREMENT, otime varchar(200), status varchar(200), severity varchar(200),duration varchar(200), dduration varchar(50), hduration varchar(50), mduration varchar(50), sduration varchar(50), notes text, primary key(id), unique(otime))";
                                        Statement stm = con.createStatement();
                                        stm.execute(balcheck);
                                        con.close();
                                    } catch (SQLException ex) {
                                        //Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    String values = "insert into balcheck(otime,status,severity,duration,dduration,hduration,mduration,sduration) values (?,?,?,?,?,?,?,?)";
                                    try {
                                        Connection con = data.connect();
                                        PreparedStatement prep = con.prepareStatement(values);
                                        prep.setString(1, otime);
                                        prep.setString(2, status);
                                        prep.setString(3, severity);
                                        prep.setString(4, duration);
                                        prep.setString(5, dduration);
                                        prep.setString(6, hduration);
                                        prep.setString(7, mduration);
                                        prep.setString(8, sduration);
                                        prep.execute();
                                        prep.close();
                                        con.close();
                                        outp = "<h3><span style='color:red'> " + fileName + " >>>Uptime File Uploaded!!</h3>";
                                    } catch (SQLException ex) {
                                        //Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                counter++;
                            }
                        }

                    } catch (FileNotFoundException e) {
                        //e.printStackTrace();
                        System.out.println("Invalid or No file Uploaded");
                        outp = "<h3><span style='color:red'>Invalid or No file Uploaded!!</span> <span>Ensure the Server Root Directory has write permissions</span></h3>" + "Java Error: " + e;
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (br != null) {
                            try {
                                br.close();
                            } catch (IOException e) {
                                outp = "<h3 style='color:red'>An error Occurred While Saving file</h3>" + "Java Error: " + e;
                                //out.println("<h3 style='color:red'>An error Occurred While Saving file</h3>" + "Java Error: " + e);
                                e.printStackTrace();
                            }
                        }
                    }
                }
                if (null != request.getParameter("salesdescription")) {
                    String description = request.getParameter("salesdescription"); // Retrieves <input type="text" name="description">
                    //Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
                    List<Part> fileParts = request.getParts().stream().filter(part -> "file".equals(part.getName())).collect(Collectors.toList());
                    for (Part filePart : fileParts) {
                        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
                        InputStream fileContent = filePart.getInputStream();

                        System.out.println("Uploaded file:" + fileName);
                        String hpath = System.getProperty("user.home");
                        System.out.println("Home Path:" + hpath);

                        /*for (Part part : request.getParts()) {
                        if (part != null && part.getSize() > 0) {
                            String contentType = part.getContentType();
                            System.out.println("Content-Type:" + contentType);
                            if (!contentType.equalsIgnoreCase("text/csv")) {
                            out.println("<h3 style='color:red'>Please Upload a a valid csv File !!</h3>");
                            continue;
                        }
                            part.write(hpath + "/" + fileName);
                        }
                    }*/
                        filePart.write(hpath + "/" + fileName);
                        String csvFile = hpath + "/" + fileName;
                        BufferedReader br = null;
                        String line = "";
                        String cvsSplitBy = "	";
                        Connection con = data.connect();
                        try {
                            br = new BufferedReader(new FileReader(csvFile));
                            int ind = 1;
                            while ((line = br.readLine()) != null) {
                                // use comma as separator
                                String[] cells = line.split(cvsSplitBy);
                                if (ind > 1) {
                                    String datesold = cells[0].replace("\"", "");
                                    String retailer = cells[1].replace("\"", "");
                                    DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm a");
                                    DateTime sdate = formatter.parseDateTime(datesold);
                                    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM");
                                    String str = fmt.print(sdate);
                                    data.createTables(retailer + "<<" + str.replaceAll("-", ""));

                                    String rvalues = "insert into retailers(retailer) values (?)";
                                    try {
                                        PreparedStatement rprep = con.prepareStatement(rvalues);
                                        rprep.setString(1, retailer);
                                        rprep.execute();
                                        rprep.close();
                                    } catch (SQLException ex) {
                                        //Logger.getLogger(Imports.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    String txtype = cells[2].replace("\"", "");
                                    String reference = cells[3].replace("\"", "");
                                    String retailvalue = cells[4].replace("\"", "");
                                    String mancovalue = cells[5].replace("\"", "");
                                    String commissionvalue = cells[6].replace("\"", "");

                                    if (retailvalue.equals("")) {
                                        retailvalue = "0.0";
                                    }
                                    if (mancovalue.equals("")) {
                                        mancovalue = "0.0";
                                    }
                                    if (commissionvalue.equals("")) {
                                        commissionvalue = "0.0";
                                    }

                                    System.out.println(datesold + "|" + retailer + "|" + txtype + "|" + reference + "|" + retailvalue + "|" + mancovalue + "|" + commissionvalue);

                                    try {
                                        String values = "insert into commercialTx" + retailer + str.replaceAll("-", "") + "(datesold,retailer,txtype,reference,retailvalue,mancovalue,commissionvalue) values (?,?,?,?,?,?,?)";
                                        PreparedStatement prep = con.prepareStatement(values);
                                        prep.setString(1, datesold);
                                        prep.setString(2, retailer);
                                        prep.setString(3, txtype);
                                        prep.setString(4, reference);
                                        prep.setString(5, retailvalue);
                                        prep.setString(6, mancovalue);
                                        prep.setString(7, commissionvalue);
                                        prep.execute();
                                        prep.close();
                                    } catch (SQLException ex) {
                                        outp = "<p class='text-success'>++++++>>Error Occured While Uploading " + ex + "  <<++++++!!</p>";
                                        out.println(outp);
                                        //break;
                                        //Logger.getLogger(Imports.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                ind++;
                            }
                        } catch (FileNotFoundException e) {
                            outp = "<h3><span style='color:red'>Invalid or No file Uploaded!!</span> <span>Ensure the Server Root Directory has write permissions</span></h3>" + "Java Error: " + e;
                            out.println(outp);
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                con.close();
                            } catch (SQLException ex) {
                            }
                            if (br != null) {
                                try {
                                    br.close();
                                } catch (IOException e) {
                                    outp = "<h3 style='color:red'>An error Occurred While Saving file</h3>" + "Java Error: " + e;
                                    out.println(outp);
                                    e.printStackTrace();
                                }
                            }
                        }
                        outp = "<h3><span style='color:red'>Done Uploading " + fileName + " >>>!!</h3>";
                        out.println(outp);
                        System.out.println("Done Uploading " + fileName + " >>>!!");
                    }
                }
                if (uptype.equals("billuptime")) {
                    out.println("<p style='color:red'>>>Upload the Zabbix postpaid file and Click Save</p>");
                    out.println("<form action='" + request.getContextPath() + "/Admin' method='post' enctype='multipart/form-data'>");
                    out.println("<input type='text' name='evgdescription' style='display:none' />");
                    out.println("<input type='text' name='uptype' value='" + uptype + "' style='display:none' />");
                    out.println("<input type='file' name='file' /><br><br>");
                    out.println("<input type='submit' value='Save' />");
                    out.println("</form>");
                } else if (uptype.equals("evguptime")) {
                    out.println("<p style='color:red'>>>Upload the Zabbix EVG(prepaid) file and Click Save</p>");
                    out.println("<form action='" + request.getContextPath() + "/Admin' method='post' enctype='multipart/form-data'>");
                    out.println("<input type='text' name='evgdescription' style='display:none' />");
                    out.println("<input type='text' name='uptype' value='" + uptype + "' style='display:none' />");
                    out.println("<input type='file' name='file' /><br><br>");
                    out.println("<input type='submit' value='Save' />");
                    out.println("</form>");
                } else if (uptype.equals("balcheck")) {
                    out.println("<p style='color:red'>>>Upload the KPLC Account Balance Check file and Click Save</p>");
                    out.println("<form action='" + request.getContextPath() + "/Admin' method='post' enctype='multipart/form-data'>");
                    out.println("<input type='text' name='evgdescription' style='display:none' />");
                    out.println("<input type='text' name='uptype' value='" + uptype + "' style='display:none' />");
                    out.println("<input type='file' name='file' /><br><br>");
                    out.println("<input type='submit' value='Save' />");
                    out.println("</form>");
                } else if (uptype.equals("backend")) {
                    out.println("<p style='color:red'>>>Upload the KPLC Backend Status file and Click Save</p>");
                    out.println("<form action='" + request.getContextPath() + "/Admin' method='post' enctype='multipart/form-data'>");
                    out.println("<input type='text' name='evgdescription' style='display:none' />");
                    out.println("<input type='text' name='uptype' value='" + uptype + "' style='display:none' />");
                    out.println("<input type='file' name='file' /><br><br>");
                    out.println("<input type='submit' value='Save' />");
                    out.println("</form>");
                } else if (uptype.equals("bizswtime")) {
                    out.println("<p style='color:red'>>>Upload the Zabbix BizswitchGraph file and Click Save</p>");
                    out.println("<form action='" + request.getContextPath() + "/Admin' method='post' enctype='multipart/form-data'>");
                    out.println("<input type='text' name='evgdescription' style='display:none' />");
                    out.println("<input type='text' name='uptype' value='" + uptype + "' style='display:none' />");
                    out.println("<input type='file' name='file' /><br><br>");
                    out.println("<input type='submit' value='Save' />");
                    out.println("</form>");
                } else if (uptype.equals("notes")) {
                    if (null != request.getParameter("notesdate")) {
                        out.println("<form class='form-horizontal' action='/pdslQPreports/Admin' method='post'>");
                        out.println("<div class='form-group'>");
                        out.println("<label for='datet' class='col-sm-2 control-label'>Month</label>");
                        out.println("<div class='col-sm-4'>");
                        String notesdate = request.getParameter("notesdate");
                        String dtype = request.getParameter("dtype");
                        System.out.println(dtype);
                        DateTimeFormatter formatter = DateTimeFormat.forPattern("MMM yyyy");
                        DateTime ndate = formatter.parseDateTime(notesdate);
                        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM");
                        String currdate = fmt.print(ndate);
                        //
                        out.println("<div class='form-group'>");
                        out.println("<div class='input-group date' id='datetimepicker10'>");
                        out.println("<input type='text' name='notesdate' value='" + notesdate + "' class='form-control' />");
                        out.println("<span class='input-group-addon'>");
                        out.println("<span class='glyphicon glyphicon-calendar'>");
                        out.println("</span>");
                        out.println("</span>");
                        out.println("</div>");
                        out.println("</div>");
                        //"
                        out.println("<input type='text' style='display:none' name='uptype' value='" + uptype + "'>");
                        if (dtype.equals("postpaid")) {
                            out.println("<label class='radio-inline'>");
                            out.println("<input type='radio' name='dtype' value='postpaid' checked> Postpaid");
                            out.println("</label>");
                            out.println("<label class='radio-inline'>");
                            out.println("<input type='radio' name='dtype' value='prepaid'> Prepaid");
                            out.println("</label>");
                            out.println("<label class='radio-inline'>");
                            out.println("<input type='radio' name='dtype' value='ttrsalesnotes'> Total Retail Sales");
                            out.println("</label>");
                            out.println("<label class='radio-inline'>");
                            out.println("<input type='radio' name='dtype' value='salespnotes'> Performance");
                            out.println("</label>");
                        } else if (dtype.equals("ttrsalesnotes")) {
                            out.println("<label class='radio-inline'>");
                            out.println("<input type='radio' name='dtype' value='postpaid'> Postpaid");
                            out.println("</label>");
                            out.println("<label class='radio-inline'>");
                            out.println("<input type='radio' name='dtype' value='prepaid'> Prepaid");
                            out.println("</label>");
                            out.println("<label class='radio-inline'>");
                            out.println("<input type='radio' name='dtype' value='ttrsalesnotes' checked> Total Retail Sales");
                            out.println("</label>");
                            out.println("<label class='radio-inline'>");
                            out.println("<input type='radio' name='dtype' value='salespnotes'> Performance");
                            out.println("</label>");
                        } else if (dtype.equals("salespnotes")) {
                            out.println("<label class='radio-inline'>");
                            out.println("<input type='radio' name='dtype' value='postpaid'> Postpaid");
                            out.println("</label>");
                            out.println("<label class='radio-inline'>");
                            out.println("<input type='radio' name='dtype' value='prepaid'> Prepaid");
                            out.println("</label>");
                            out.println("<label class='radio-inline'>");
                            out.println("<input type='radio' name='dtype' value='ttrsalesnotes'> Total Retail Sales");
                            out.println("</label>");
                            out.println("<label class='radio-inline'>");
                            out.println("<input type='radio' name='dtype' value='salespnotes' checked> Performance");
                            out.println("</label>");
                        } else {
                            out.println("<label class='radio-inline'>");
                            out.println("<input type='radio' name='dtype' value='postpaid'> Postpaid");
                            out.println("</label>");
                            out.println("<label class='radio-inline'>");
                            out.println("<input type='radio' name='dtype' value='prepaid' checked> Prepaid");
                            out.println("</label>");
                            out.println("<label class='radio-inline'>");
                            out.println("<input type='radio' name='dtype' value='ttrsalesnotes'> Total Retail Sales");
                            out.println("</label>");
                            out.println("<label class='radio-inline'>");
                            out.println("<input type='radio' name='dtype' value='salespnotes'> Performance");
                            out.println("</label>");
                        }
                        out.println("</div>");
                        out.println("</div>");
                        out.println("<div class='form-group'>");
                        out.println("<div class='col-sm-offset-2 col-sm-10'>");
                        out.println("<button type='submit' class='btn btn-default'>Go!</button>");
                        out.println("</div>");
                        out.println("</div>");
                        out.println("</form>");
                        if (dtype.equals("ttrsalesnotes")) {
                            data.createTables(currdate.replaceAll("-", ""));
                            String query = "SELECT `retailer` FROM `retailers`";
                            try {
                                Connection con = data.connect();
                                Statement st = con.createStatement();
                                ResultSet rs = st.executeQuery(query);
                                while (rs.next()) {
                                    try {
                                        String values = "insert into ttrsalesnotes" + currdate.replaceAll("-", "") + "(retailer,fullname,notes) values (?,?,?)";
                                        PreparedStatement prep = con.prepareStatement(values);
                                        prep.setString(1, rs.getString(1).substring(0, 6));
                                        prep.setString(2, rs.getString(1));
                                        prep.setString(3, "None");
                                        prep.execute();
                                        prep.close();
                                    } catch (SQLException exxx) {
                                        //Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                con.close();
                            } catch (SQLException ex) {
                                Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            String query2 = "SELECT `id`,`fullname`,`notes` FROM `ttrsalesnotes" + currdate.replaceAll("-", "") + "`";
                            out.println("<form class='form-horizontal'action='/pdslQPreports/Admin' method='post'>");
                            out.println("<table class='table table-bordered' style='font-size:12px'>");
                            out.println("<thead>");
                            out.println("<tr>");
                            out.println("<th>Retailer</th>");
                            out.println("<th>Notes</th>");
                            out.println("</tr>");
                            out.println("</thead>");
                            out.println("<tbody>");
                            try {
                                Connection con = data.connect();
                                Statement st2 = con.createStatement();
                                ResultSet rs2 = st2.executeQuery(query2);
                                while (rs2.next()) {
                                    out.println("<tr>");
                                    out.println("<td>" + rs2.getString(2) + "</td>");
                                    String nvl = " ";
                                    if (rs2.getString(3) != null) {
                                        nvl = rs2.getString(3);
                                    }
                                    out.println("<td><textarea id='" + rs2.getString(1) + "noise' class='widgEditor nothing' name='" + rs2.getString(1) + "notes'>" + nvl + "</textarea></td>");
                                    out.println("</tr>");
                                }
                                con.close();
                            } catch (SQLException ex) {
                                Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            out.println("</tbody>");
                            out.println("</table>");
                            out.println("<input type='text' style='display:none' name='currdate' value='" + currdate + "'>");
                            out.println("<input type='text' style='display:none' name='dtype' value='" + dtype + "'>");
                            out.println("<input type='text' style='display:none' name='nsupdate' value='yes'>");
                            out.println("<button type='submit' class='btn btn-lg btn-danger'>&nbsp;&nbsp;&nbsp;Save&nbsp;&nbsp;&nbsp;</button><hr />");
                            out.println("</form>");
                        } else if (dtype.equals("salespnotes")) {
                            data.createTables(currdate.replaceAll("-", ""));
                            String query = "SELECT `retailer` FROM `retailers`";
                            try {
                                Connection con = data.connect();
                                Statement st = con.createStatement();
                                ResultSet rs = st.executeQuery(query);
                                while (rs.next()) {
                                    try {
                                        String values = "insert into salesnotes" + currdate.replaceAll("-", "") + "(retailer,fullname,notes) values (?,?,?)";
                                        PreparedStatement prep = con.prepareStatement(values);
                                        prep.setString(1, rs.getString(1).substring(0, 6));
                                        prep.setString(2, rs.getString(1));
                                        prep.setString(3, "None");
                                        prep.execute();
                                        prep.close();
                                    } catch (SQLException exxx) {
                                        //Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                con.close();
                            } catch (SQLException ex) {
                                Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            String query2 = "SELECT `id`,`fullname`,`notes` FROM `salesnotes" + currdate.replaceAll("-", "") + "`";
                            out.println("<form class='form-horizontal'action='/pdslQPreports/Admin' method='post'>");
                            out.println("<table class='table table-bordered' style='font-size:12px'>");
                            out.println("<thead>");
                            out.println("<tr>");
                            out.println("<th>Retailer</th>");
                            out.println("<th>Notes</th>");
                            out.println("</tr>");
                            out.println("</thead>");
                            out.println("<tbody>");
                            try {
                                Connection con = data.connect();
                                Statement st2 = con.createStatement();
                                ResultSet rs2 = st2.executeQuery(query2);
                                while (rs2.next()) {
                                    out.println("<tr>");
                                    out.println("<td>" + rs2.getString(2) + "</td>");
                                    String nvl = " ";
                                    if (rs2.getString(3) != null) {
                                        nvl = rs2.getString(3);
                                    }
                                    out.println("<td><textarea id='" + rs2.getString(1) + "noise' class='widgEditor nothing' name='" + rs2.getString(1) + "notes'>" + nvl + "</textarea></td>");
                                    out.println("</tr>");
                                }
                                con.close();
                            } catch (SQLException ex) {
                                Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            out.println("</tbody>");
                            out.println("</table>");
                            out.println("<input type='text' style='display:none' name='currdate' value='" + currdate + "'>");
                            out.println("<input type='text' style='display:none' name='dtype' value='" + dtype + "'>");
                            out.println("<input type='text' style='display:none' name='nsupdate' value='yes'>");
                            out.println("<button type='submit' class='btn btn-lg btn-danger'>&nbsp;&nbsp;&nbsp;Save&nbsp;&nbsp;&nbsp;</button><hr />");
                            out.println("</form>");
                        } else {
                            String query = "SELECT `id`,`otime`,`status`,`duration`,`mduration`,`notes` FROM `bill" + currdate.replaceAll("-", "") + "` WHERE `status`='PROBLEM' AND `mduration`>= 5 ORDER BY `id` DESC";
                            if (dtype.equals("prepaid")) {
                                query = "SELECT `id`,`otime`,`status`,`duration`,`mduration`,`notes` FROM `evg" + currdate.replaceAll("-", "") + "` WHERE  `status`='PROBLEM' AND `mduration`>= 5 ORDER BY `id` DESC";
                            }
                            System.out.println(query);
                            out.println("<form class='form-horizontal'action='/pdslQPreports/Admin' method='post'>");
                            out.println("<table class='table table-bordered' style='font-size:12px'>");
                            out.println("<thead>");
                            out.println("<tr>");
                            out.println("<th>Date</th>");
                            out.println("<th>Status</th>");
                            out.println("<th>Duration</th>");
                            out.println("<th>Notes</th>");
                            out.println("</tr>");
                            out.println("</thead>");
                            out.println("<tbody>");
                            try {
                                Connection con = data.connect();
                                Statement st = con.createStatement();
                                ResultSet rs = st.executeQuery(query);
                                while (rs.next()) {
                                    out.println("<tr>");
                                    out.println("<td>" + rs.getString(2) + "</td>");
                                    out.println("<td>" + rs.getString(3) + "</td>");
                                    out.println("<td>" + rs.getString(4) + "</td>");
                                    String nvl = " ";
                                    if (rs.getString(6) != null) {
                                        nvl = rs.getString(6);
                                    }
                                    out.println("<td><textarea class='form-control' name='" + rs.getString(1) + "notes'>" + nvl + "</textarea></td>");
                                    out.println("</tr>");
                                }
                                con.close();
                            } catch (SQLException ex) {
                                Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            out.println("</tbody>");
                            out.println("</table>");
                            out.println("<input type='text' style='display:none' name='currdate' value='" + currdate + "'>");
                            out.println("<input type='text' style='display:none' name='dtype' value='" + dtype + "'>");
                            out.println("<input type='text' style='display:none' name='nsupdate' value='yes'>");
                            out.println("<button type='submit' class='btn btn-lg btn-danger'>&nbsp;&nbsp;&nbsp;Save&nbsp;&nbsp;&nbsp;</button><hr />");
                            out.println("</form>");
                        }

                    } else {
                        out.println("<form class='form-horizontal'action='/pdslQPreports/Admin' method='post'>");
                        out.println("<div class='form-group'>");
                        out.println("<label for='datet' class='col-sm-2 control-label'>Month</label>");
                        out.println("<div class='col-sm-4'>");
                        DateTime odt = new DateTime().minusMonths(1);
                        DateTimeFormatter fm = DateTimeFormat.forPattern("MMM yyyy");
                        String currdate = fm.print(odt);
                        //
                        out.println("<div class='form-group'>");
                        out.println("<div class='input-group date' id='datetimepicker10'>");
                        out.println("<input type='text' name='notesdate' value='" + currdate + "' class='form-control' />");
                        out.println("<span class='input-group-addon'>");
                        out.println("<span class='glyphicon glyphicon-calendar'>");
                        out.println("</span>");
                        out.println("</span>");
                        out.println("</div>");
                        out.println("</div>");
                        //"
                        out.println("<input type='text' style='display:none' name='uptype' value='" + uptype + "'>");
                        out.println("<label class='radio-inline'>");
                        out.println("<input type='radio' name='dtype' value='postpaid' checked> Postpaid");
                        out.println("</label>");
                        out.println("<label class='radio-inline'>");
                        out.println("<input type='radio' name='dtype' value='prepaid'> Prepaid");
                        out.println("</label>");
                        out.println("<label class='radio-inline'>");
                        out.println("<input type='radio' name='dtype' value='ttrsalesnotes'> Total Retail Sales");
                        out.println("</label>");
                        out.println("<label class='radio-inline'>");
                        out.println("<input type='radio' name='dtype' value='salespnotes'> Performance");
                        out.println("</label>");
                        out.println("</div>");
                        out.println("</div>");
                        out.println("<div class='form-group'>");
                        out.println("<div class='col-sm-offset-2 col-sm-10'>");
                        out.println("<button type='submit' class='btn btn-default'>Go!</button>");
                        out.println("</div>");
                        out.println("</div>");
                        out.println("</form>");
                    }
                } else if (uptype.equals("sales")) {
                    if (null == request.getParameter("salesdescription")) {
                        out.println("<p style='color:red'>>>Upload the Sales file(s) and Click Save</p>");
                        out.println("<form action='" + request.getContextPath() + "/Admin' method='post' enctype='multipart/form-data'>");
                        out.println("<input type='text' name='salesdescription' style='display:none' />");
                        out.println("<input type='text' name='uptype' value='" + uptype + "' style='display:none' />");
                        out.println("<input type='file' name='file' multiple='multiple' /><br><br>");
                        out.println("<input type='submit' value='Save' />");
                        out.println("</form>");
                    }
                } else if (uptype.equals("news")) {
                    String query = "SELECT `id`,`notes` FROM `news` WHERE `id`=1";
                    out.println("<form class='form-horizontal'action='/pdslQPreports/Admin' method='post'>");
                    try {
                        Connection con = data.connect();
                        Statement st = con.createStatement();
                        ResultSet rs = st.executeQuery(query);
                        if (rs.isBeforeFirst()) {
                            while (rs.next()) {
                                out.println("<textarea id='newsnotes' class='widgEditor nothing' name='newsnotes'>" + rs.getString(2) + "</textarea>");
                            }
                        } else {
                            String values = "insert into news(notes) values (?)";
                            PreparedStatement prep = con.prepareStatement(values);
                            prep.setString(1, "None");
                            prep.execute();
                            prep.close();
                            out.println("<textarea id='newsnotes' class='widgEditor nothing' name='newsnotes'>None</textarea>");
                        }
                        con.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    out.println("<button type='submit' class='btn btn-default'>Go!</button>");
                    out.println("</form>");
                }

                out.println(outp);
                System.out.println(outp);

                out.println("</div>");
                out.println(data.footer);
            }
        }
    }

    public boolean checkTableExist(String tbl) {
        try {
            Connection con = data.connect();
            String query = "SELECT * FROM `" + tbl + "` LIMIT 1";
            System.out.println(query);
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(query);
            if (rs.isBeforeFirst()) {
                return true;
            }
            con.close();
        } catch (SQLException ex) {
            //Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
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
