/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.equity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author koks
 */
public class inbound extends HttpServlet {

    PreparedStatement prep = null;
    Connection con = null;
    DataStore data = new DataStore();
    String resretun = "failed";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     *
     */
    protected String processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        Throwable localThrowable3 = null;
        //http://172.27.116.21:8084/equitynotification/notification?trancurrency=kes&phonenumber=254728064120&tranid=20181109S3269888test&refno=900620&tranparticular=ELKAMWA ENTERPRT-BY:BEN/731314349225/09-11-2017 16&debitaccount=900620&trantype=cash&debitcustname=EQUITEL&tranamount=1&trandate=25-09-2018 00:00:00

        try {
            Logger.getLogger(inbound.class.getName()).log(Level.INFO, "Equity Call");
            Enumeration enums = request.getParameterNames();
            while (enums.hasMoreElements()) {
                String values = "" + enums.nextElement();

                values = values + ":" + request.getParameter(values);

                Logger.getLogger(inbound.class.getName()).log(Level.INFO, values);
            }
            String trancurrency = request.getParameter("trancurrency");
            String phonenumber = request.getParameter("phonenumber");
            String tranid = request.getParameter("tranid");
            String refno = request.getParameter("refno");
            //Logger.getLogger(inbound.class.getName()).log(Level.INFO, "OREFNO=" + refno);
            String tranparticular = request.getParameter("tranparticular");
            String[] particularsplit = tranparticular.split("/");
            DateTime dt = new DateTime();
            DateTimeFormatter fmt = DateTimeFormat.forPattern("dd-MM-yyyy 00:00:00");
            String datee = fmt.print(dt);
            int leng = particularsplit.length;
            if ((leng > 2) && (!particularsplit[2].startsWith("RMT"))) {
                if (particularsplit[0].contains("EZBP")) {
                    tranid = particularsplit[2];
                } else {
                    tranid = particularsplit[1];
                }
                if ((leng > 3)) {
                    tranid = particularsplit[2];
                }
            }
            if (particularsplit[0].equals(refno)) {
                tranid = particularsplit[2];
            }
            String debitaccount = request.getParameter("debitaccount");
            String trantype = request.getParameter("trantype");
            String debitcustname = request.getParameter("debitcustname");
            String tranamount = request.getParameter("tranamount");
            String trandate = request.getParameter("trandate");

            DateTime dt2 = new DateTime();
            DateTimeFormatter fmt2 = DateTimeFormat.forPattern("yyyy-MM-dd");
            String date2 = fmt2.print(dt2);
            String strippeddate = date2.replaceAll("-", "");
            Logger.getLogger(inbound.class.getName()).log(Level.INFO, "Equity direct stripped date=" + strippeddate);
            if (tranid.startsWith(strippeddate)) {
                Logger.getLogger(inbound.class.getName()).log(Level.INFO, "TRANID=" + tranid);
                int dtl = strippeddate.length();
                int l_pos = tranid.indexOf("-");
                String newtranid = tranid.substring(dtl, l_pos);
                tranid = newtranid;
                Logger.getLogger(inbound.class.getName()).log(Level.INFO, "NEW TRANID=" + tranid);
            }

            String matchedshop = check(tranparticular);
            Logger.getLogger(inbound.class.getName()).log(Level.INFO, "Matched Vendor: " + matchedshop);
            if (!matchedshop.equals("none")) {
                refno = matchedshop;
            }
            if (trandate.equals("MPESA")) {
                trandate = datee;
            }
            if (trandate.equalsIgnoreCase(datee)) {
                this.con = this.data.connect();
                String values = "insert into transactions(phonenumber,trancurrency,tranid,refno,tranparticular,debitaccount,trantype,debitcustname,tranamount,trandate) values (?,?,?,?,?,?,?,?,?,?)";
                try {
                    String subfloat = "fail";
                    try {
                        subfloat = sendSubAgFloat(refno, tranid, tranamount);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    System.out.println("SUBAGRES: " + subfloat);
                    if (subfloat.trim().equals("success")) {
                        resretun = "Success|: VendorCode " + refno + " Floated with Kshs." + tranamount + " Ref: " + tranid;
                        String floatPdslEquity = new FloatAdjust().sendPost(tranamount, trandate, tranid, "PdslEquity");
                        System.out.println("Floating PDSLEQUITY RES: " + floatPdslEquity);
                    } else {
                        resretun = new FloatAdjust().sendPost(tranamount, trandate, tranid, refno);
                        if (resretun.contains("Failed Error")) {
                            String shopname = getShop(refno);
                            Logger.getLogger(inbound.class.getName()).log(Level.INFO, "SHOPNAME: " + shopname);
                            if (!shopname.equals("none")) {
                                resretun = new FloatAdjust().sendPost(tranamount, trandate, tranid, shopname);
                            }
                        }
                    }
                    this.prep = this.con.prepareStatement(values);
                    this.prep.setString(1, phonenumber);
                    this.prep.setString(2, trancurrency);
                    this.prep.setString(3, tranid);
                    this.prep.setString(4, refno);
                    this.prep.setString(5, tranparticular);
                    this.prep.setString(6, debitaccount);
                    this.prep.setString(7, trantype);
                    this.prep.setString(8, debitcustname);
                    this.prep.setString(9, tranamount);
                    this.prep.setString(10, trandate);
                    this.prep.execute();
                    this.prep.close();
                    //out.println(resretun);
                    Logger.getLogger(FloatAdjust.class.getName()).log(Level.INFO, "Res from Ipay " + resretun);
                    Logger.getLogger(inbound.class.getName()).log(Level.INFO, "Update Successful");
                } catch (SQLException ex) {
                    Logger.getLogger(inbound.class.getName()).log(Level.SEVERE, "TranId Already Exist");

                } catch (Exception ex) {
                    Logger.getLogger(inbound.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    this.con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(inbound.class.getName()).log(Level.SEVERE, null, ex);
                }
                out.write(resretun);
            }
        } catch (Throwable localThrowable1) {
            localThrowable3 = localThrowable1;
            throw localThrowable1;
        } finally {
            if (out != null) {
                if (localThrowable3 != null) {
                    try {
                        out.close();
                    } catch (Throwable localThrowable2) {
                        localThrowable3.addSuppressed(localThrowable2);
                    }
                } else {
                    out.close();
                }
            }
        }
        return resretun;
    }

    public String check(String tranparticular) {
        List<List<String>> agarray = new ArrayList<>();
        List<List<String>> vendorarray = new ArrayList<>();
        Connection con = this.data.connect();
        String res = "none";
        Logger.getLogger(inbound.class.getName()).log(Level.INFO, "Matching Vendor in: " + tranparticular);
        try {
            String query = "select ag_code,ag_name,eazzybiz,cheque,rtgs from aggregators";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            ResultSetMetaData metadata = rs.getMetaData();
            int numcols = metadata.getColumnCount();
            while (rs.next()) {
                //System.out.println("Saving: " + rs.getString(1));
                List<String> row = new ArrayList<>(numcols); // new list per row
                int i = 1;
                while (i <= numcols) {  // don't skip the last column, use <=
                    row.add(rs.getString(i++));
                }
                agarray.add(row); // add it to the result
            }
            Logger.getLogger(inbound.class.getName()).log(Level.INFO, "Number of Ags: " + agarray.size());

            String query2 = "select vendor_code,vendor_name,eazzybiz,cheque,rtgs from vendors";
            Statement st2 = con.createStatement();
            ResultSet rs2 = st2.executeQuery(query2);
            ResultSetMetaData metadata2 = rs2.getMetaData();
            int numcols2 = metadata2.getColumnCount();
            while (rs2.next()) {
                //System.out.println("Saving: " + rs.getString(1));
                List<String> row2 = new ArrayList<>(numcols); // new list per row
                int i = 1;
                while (i <= numcols2) {  // don't skip the last column, use <=
                    row2.add(rs2.getString(i++));
                }
                vendorarray.add(row2); // add it to the result
            }
            Logger.getLogger(inbound.class.getName()).log(Level.INFO, "Number of Vendors: " + vendorarray.size());
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(inbound.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (int y = 0; y < agarray.size(); y++) {
            String agcode = agarray.get(y).get(0);
            String agname = agarray.get(y).get(1);
            String eazzybiz = agarray.get(y).get(2);
            String cheque = agarray.get(y).get(3);
            String rtgs = agarray.get(y).get(4);
            try {
                if (tranparticular.contains(eazzybiz)) {
                    res = agname;
                    //Logger.getLogger(inbound.class.getName()).log(Level.INFO, "Found in: eazzybiz " + agname);
                }
            } catch (Exception ex) {
                //Logger.getLogger(inbound.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (tranparticular.contains(cheque)) {
                    res = agname;
                    //Logger.getLogger(inbound.class.getName()).log(Level.INFO, "Found in: cheque " + agname);
                }
            } catch (Exception ex) {
                //Logger.getLogger(inbound.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (tranparticular.contains(rtgs)) {
                    res = agname;
                    //Logger.getLogger(inbound.class.getName()).log(Level.INFO, "Found in: rtgs " + agname);
                }
            } catch (Exception ex) {
                //Logger.getLogger(inbound.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Logger.getLogger(inbound.class.getName()).log(Level.INFO, "check res " + res);
        for (int x = 0; x < vendorarray.size(); x++) {
            String vcode = vendorarray.get(x).get(0);
            String vname = vendorarray.get(x).get(1);
            String eazzybiz = vendorarray.get(x).get(2);
            String cheque = vendorarray.get(x).get(3);
            String rtgs = vendorarray.get(x).get(4);
            try {
                if (tranparticular.contains(eazzybiz)) {
                    res = vname;
                    //Logger.getLogger(inbound.class.getName()).log(Level.INFO, "Found in: eazzybiz " + vname);
                }
            } catch (Exception ex) {
                //Logger.getLogger(inbound.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (tranparticular.contains(cheque)) {
                    res = vname;
                    //Logger.getLogger(inbound.class.getName()).log(Level.INFO, "Found in: cheque " + vname);
                }
            } catch (Exception ex) {
                //Logger.getLogger(inbound.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (tranparticular.contains(rtgs)) {
                    res = vname;
                    //Logger.getLogger(inbound.class.getName()).log(Level.INFO, "Found in: rtgs " + vname);
                }
            } catch (Exception ex) {
                //Logger.getLogger(inbound.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return res;
    }

    public String getShop(String code) {
        Connection con = this.data.connect();
        String resp = "none";
        try {
            String query = "select ag_code,ag_name from aggregators where ag_code='" + code + "' limit 1";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    resp = rs.getString(2);
                }
            } else {
                String query2 = "select vendor_code,vendor_name from vendors where vendor_code='" + code + "' limit 1";
                Statement st2 = con.createStatement();
                ResultSet rs2 = st2.executeQuery(query2);
                if (rs.isBeforeFirst()) {
                    resp = rs2.getString(2);
                }
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(inbound.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resp;
    }

    public String sendReq(String param) throws IOException {
        String request = "http://172.27.116.36:8084/pdslvending/Autofloat?";
        URL url = new URL(request);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("charset", "utf-8");
        connection.setRequestProperty("Content-Length", "" + Integer.toString(param.getBytes().length));
        connection.setUseCaches(false);

        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.writeBytes(param);
        out.flush();

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        StringBuffer response = new StringBuffer();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //System.out.println(response.toString());
        out.close();
        connection.disconnect();
        return response.toString();
    }

    public String sendSubAgFloat(String vcode, String refe, String amount) {
        String url = "vcode=" + vcode + "&refe=" + refe + "&amount=" + amount + "";
        System.out.println(url);
        String res;
        try {
            res = sendReq(url);
        } catch (IOException e) {
            e.printStackTrace();
            res = "FAILED";
        }
        return res;
    }

    public static void main(String[] args) {
        inbound in = new inbound();
        in.sendSubAgFloat("101", "tfromapp", "1");
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
        String r = processRequest(request, response);
        PrintWriter out = response.getWriter();
        out.write(r);
        out.close();
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
        String r = processRequest(request, response);
        //response.getWriter().append(resretun);
        PrintWriter out = response.getWriter();
        out.write(r);
        out.close();
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
