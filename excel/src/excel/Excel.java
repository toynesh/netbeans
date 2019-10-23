/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package excel;

import com.pdslkenya.reconnectus.Ipay;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author juliuskun
 */
public class Excel {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        DataStore data = new DataStore();
        Excel reado = new Excel();
        MessageEditor msEObj = new MessageEditor();
        Sdp sdp = new Sdp();
        Connection con = null;
        Connection conn = null;
        PreparedStatement prep = null;
        PrintStream out = new PrintStream(new FileOutputStream("/var/www/html/vendit/output.txt"));
        System.setOut(out);
        String results = null;
        String mesg = null;
        String source = "safaricom";
        List<String> logmpesaexcel = new ArrayList<>();
        System.out.println("<br />---------------------------------------<br />");
        try {
            File f = new File("/opt/applications/upload/data.xls");
            Workbook wb = Workbook.getWorkbook(f);
            Sheet s = wb.getSheet(0);
            int row = s.getRows();
            int col = s.getColumns();

            String startDate = s.getCell(2, 3).getContents();
            String endDate = s.getCell(4, 3).getContents();

            System.out.println("creating logmpesaexcel ARRAY...");
            System.out.println("<br />---------------------------------------<br />");

            startDate = startDate.replaceAll("[']", "");
            endDate = endDate.replaceAll("[']", "");

            startDate = startDate.replaceAll("/", "-");
            endDate = endDate.replaceAll("/", "-");

            DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss");
            DateTime sdate = formatter.parseDateTime(startDate);
            DateTime edate = formatter.parseDateTime(endDate);

            DateTimeFormatter ffmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            String stdate = ffmt.print(sdate);
            String endate = ffmt.print(edate);

            System.out.println("StartDate:" + stdate + " EndDate:" + endate);
            con = data.vconnect();
            String query = "SELECT `mpesa_code`,`timestamp` FROM `mpesa` WHERE `timestamp` BETWEEN '" + stdate + "' AND '" + endate + "'";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                logmpesaexcel.add(rs.getString(1)); // add it to the result
            }
            System.out.println("lOGMpesa Array Size: " + logmpesaexcel.size());
            con.close();
            System.out.println("<br />---------------------------------------<br />");
            for (int j = 6; j < row; j++) {
                Cell receipt = s.getCell(0, j);
                Cell account = s.getCell(12, j);
                Cell info = s.getCell(10, j);
                Cell paidId = s.getCell(5, j);
                Cell paidOut = s.getCell(6, j);

                String ref = receipt.getContents();
                String meter = account.getContents();
                String[] ms = info.getContents().split(" - ");
                String msisdn = ms[0];
                String amount = paidId.getContents();
                String amountout = paidOut.getContents();
                String mpesaname = ms[1];
                amount = amount.replaceAll(",", "");
                amountout = amountout.replaceAll(",", "");
                if (mpesaname.equals("VENDIT LIMITED")) {
                    System.out.println("FLOATING");
                } else if ((amount == null) || (amount.isEmpty())) {
                    System.out.println("REVERSED TRANSACTION");
                } else if (!logmpesaexcel.contains(ref)) {
                    if (!amountout.startsWith("-")) {
                        String deliverystatus = "SmsDeliveryWaiting";
                        System.out.println("Checking if unprocessed:-" + ref + "|" + meter + "|" + msisdn + "|" + mpesaname + "|" + amount);

                        DateTime dt = new DateTime();
                        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss");
                        String datee = fmt.print(dt);
                        String tstamp = datee.replaceAll("-", "");
                        tstamp = tstamp.replaceAll(" ", "");

                        System.out.println("<br />---------------------------------------<br />");

                        con = data.vconnect();
                        try {
                            results = new Ipay().sendRequest(datee, msisdn, ref, mpesaname, amount, "25/08/14", meter);

                            results = results.replace("$", "KES");
                            results = results.replaceAll("FAIL", "OK");
                            System.out.println("RESULTS:" + results);
                            System.out.println("<br />---------------------------------------<br />");

                            String[] reslt = results.split("|");
                            if (results.toLowerCase().contains("timed out")) {
                                mesg = "Dear Customer,We are experiencing KPLC delays. Once the system is up we will resend your transaction";
                                try {
                                    sdp.sendSMS(msisdn, mesg, ref, "704307", "http://197.248.9.105:8084/VendITNotification/SmsNotificationService");
                                } catch (Exception xx) {
                                    deliverystatus = "SDPTIMEOUT";
                                }
                                msEObj.setStatus("delay");
                                String timeout = "insert into timeout (msisdn,mpesa_code,mpesa_sender,amount,tx_date,account) values (?,?,?,?,?,?)";
                                prep = con.prepareStatement(timeout);
                                prep.setString(1, msisdn);
                                prep.setString(2, ref);
                                prep.setString(3, mpesaname);
                                prep.setString(4, amount);
                                prep.setString(5, datee);
                                prep.setString(6, meter);
                                prep.execute();
                                prep.close();
                            } else if (results.startsWith("FAIL")) {
                                try {
                                    try {
                                        sdp.sendSMS(msisdn, results.substring(5), ref, "704307", "http://197.248.9.105:8084/VendITNotification/SmsNotificationService");
                                    } catch (Exception xx) {
                                        deliverystatus = "SDPTIMEOUT";
                                    }
                                    mesg = results.substring(5);
                                    msEObj.setStatus("error");
                                } catch (Exception ex) {
                                    mesg = results.substring(5);
                                    msEObj.setStatus("error");
                                }
                            } else if (results.startsWith("OK")) {
                                try {
                                    mesg = msEObj.editToken(results, meter, msisdn, ref, datee);
                                    if (mesg.equals("OK|MTRFE010: KPLC Specified transaction already processed. Queries ph:0709711000.")) {
                                        System.out.println("Message not sent out to customer on no FBE");
                                        System.out.println("<br />---------------------------------------<br />");
                                        //deliverystatus = "SmsNotSent";
                                    } else if (mesg.equals("OK|Duplicate mpesa_code. Queries ph:0707-373794.")) {
                                        System.out.println("Duplicate mpesa_code");
                                        System.out.println("<br />---------------------------------------<br />");
                                        //deliverystatus = "SmsNotSent";
                                    } else {
                                        try {
                                            sdp.sendSMS(msisdn, mesg, ref, "704307", "http://197.248.9.105:8084:8084/VendITNotification/SmsNotificationService");
                                        } catch (Exception xx) {
                                            deliverystatus = "SDPTIMEOUT";
                                        }
                                    }
                                    System.out.println("Phn:" + msisdn + " NEW MESSAGE:" + mesg);
                                    System.out.println("<br />---------------------------------------<br />");
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                            if (mesg.equals("OK|Duplicate mpesa_code. Queries ph:0707-373794.")) {
                                System.out.println("Duplicate mpesa_code");
                                System.out.println("<br />---------------------------------------<br />");
                            } else if (results.toLowerCase().contains("timed out")) {
                                System.out.println("Already saved in timeout");
                                System.out.println("<br />---------------------------------------<br />");
                            } else {
                                String values = "insert into mpesa(id,orig,tstamp,mpesa_code,mpesa_acc,mpesa_msisdn,mpesa_amt,mpesa_sender,msgid,message,originalmsg,msg_status,source,dlry_status) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                                prep = con.prepareStatement(values);
                                prep.setString(1, "3630");
                                prep.setString(2, "EXCELMPESA");
                                prep.setString(3, tstamp);
                                prep.setString(4, ref);
                                prep.setString(5, meter);
                                prep.setString(6, msisdn);
                                prep.setString(7, amount);
                                prep.setString(8, mpesaname);
                                prep.setString(9, ref);
                                prep.setString(10, mesg);
                                prep.setString(11, results);
                                prep.setString(12, msEObj.getStatus());
                                prep.setString(13, source);
                                prep.setString(14, deliverystatus);
                                prep.execute();
                                prep.close();

                                System.out.println("Message Status:--" + msEObj.getStatus());
                                System.out.println("<br />---------------------------------------<br />");

                            }
                            System.out.println("Message sent out");
                            System.out.println("<br />---------------------------------------<br />");
                        } catch (SQLException ex) {
                            System.out.println("Error on first vend try" + ex);
                            System.out.println("<br />---------------------------------------<br />");
                        }
                        con.close();

                        System.out.println("Payments Response:" + results);
                        System.out.println("<br />---------------------------------------<br />");
                    }else{
                        System.out.println("Transaction was reversed amount: "+amountout);
                    }
                } else {
                    System.out.println("The Loggger Transaction exists");
                    System.out.println("<br />---------------------------------------<br />");
                }
            }
            System.out.println("Logging completed!!!");
            try {
                conn = data.connect();
                String update = "update status set svalue = ?";
                PreparedStatement preparedStmt = conn.prepareStatement(update);
                preparedStmt.setString(1, "off");
                preparedStmt.executeUpdate();
                conn.close();
                System.out.println("Updated Status table");
            } catch (Exception ex) {
                Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, "db update error: " + ex);
                System.out.println("db update error: " + ex);
            }
        } catch (Exception e) {
            try {
                conn = data.connect();
                String update = "update status set svalue = ?";
                PreparedStatement preparedStmt = conn.prepareStatement(update);
                preparedStmt.setString(1, "off");
                preparedStmt.executeUpdate();
                conn.close();
                System.out.println("Updated Status table on exception");
            } catch (Exception ex) {
                Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, "db update error: " + ex);
                System.out.println("db update error: " + ex);
            }
            Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, "excel logger error: " + e);
            System.out.println("excel logger error: " + e);
        }
    }

}
