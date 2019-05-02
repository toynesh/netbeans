/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package revendinsuff;

import com.pdslkenya.reconnectus.Ipay;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author coolie
 */
public class Revendinsuff {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here        
        DataStore data = new DataStore();
        MessageEditor msEObj = new MessageEditor();
        Sdp sdp = new Sdp();
        List<String> phones = new ArrayList<>();
        List<String> codes = new ArrayList<>();
        List<String> addedphones = new ArrayList<>();
        List<String> succphones = new ArrayList<>();
        List<List<String>> mpesa = new ArrayList<>();
        List<List<String>> foundinsuff = new ArrayList<>();
        
        //55283
        String csvFile = "/opt/applications/revendinsuff/Consumer Mobile - Account balance within a date range.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] cells = line.split(cvsSplitBy);
                phones.add(cells[1]);
                List<String> row = new ArrayList<>();
                row.add(cells[1]);
                row.add(cells[2]);
                foundinsuff.add(row);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //id 55283

        try {
            Connection con = data.connect();
            String query = "SELECT mpesa_msisdn,message,mpesa_acc,timestamp,mpesa_code,mpesa_sender FROM mpesa ORDER BY timestamp DESC";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            ResultSetMetaData metadata = rs.getMetaData();
            int numcols = metadata.getColumnCount();
            while (rs.next()) {
                if (!rs.getString(5).contains("EQU_")) {
                    if (rs.getString(2).contains("KPLC meter has accumulated a time based charge")) {
                        System.out.println("Adding from Mpesa:>> " + rs.getString(1) + "-" + rs.getString(2) + "-" + rs.getString(3) + "-" + rs.getString(4) + "-" + rs.getString(5) + "-" + rs.getString(6));
                        codes.add(rs.getString(5));
                        List<String> row = new ArrayList<>(numcols);
                        int i = 1;
                        while (i <= numcols) {
                            row.add(rs.getString(i++));
                        }
                        mpesa.add(row);
                        System.out.println("");
                        System.out.println("====================================================================");
                    }
                }
            }
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            Connection con = data.connect();
            String query = "SELECT mpesa_msisdn,message,mpesa_acc,timestamp,mpesa_code,mpesa_sender FROM mpesa201707 ORDER BY timestamp DESC";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            ResultSetMetaData metadata = rs.getMetaData();
            int numcols = metadata.getColumnCount();
            while (rs.next()) {
                if (!rs.getString(5).contains("EQU_")) {
                    if (rs.getString(2).contains("KPLC meter has accumulated a time based charge")) {
                        System.out.println("Adding from mpesa201707:>> " + rs.getString(1) + "-" + rs.getString(2) + "-" + rs.getString(3) + "-" + rs.getString(4) + "-" + rs.getString(5) + "-" + rs.getString(6));
                        codes.add(rs.getString(5));
                        List<String> row = new ArrayList<>(numcols);
                        int i = 1;
                        while (i <= numcols) {
                            row.add(rs.getString(i++));
                        }
                        mpesa.add(row);
                        System.out.println("");
                        System.out.println("====================================================================");
                    }
                }
            }
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            Connection con = data.connect();
            String query = "SELECT mpesa_msisdn,message,mpesa_acc,timestamp,mpesa_code,mpesa_sender FROM mpesaoldbk WHERE message IS NOT NULL ORDER BY timestamp DESC";
            //String query = "SELECT mpesa_msisdn,message,mpesa_acc,timestamp,mpesa_code,mpesa_sender FROM mpesa201707 where timestamp>='2018-03-14 00:00:00' ORDER BY timestamp DESC";
            //String query = "SELECT mpesa_msisdn,message,mpesa_acc,timestamp,mpesa_code,mpesa_sender FROM mpesa201707 where timestamp<='2016-12-31 23:59:59' AND message IS NOT NULL ORDER BY timestamp DESC";
            //String query = "SELECT mpesa_msisdn,message,mpesa_acc,timestamp,mpesa_code,mpesa_sender FROM mpesa201707 where timestamp BETWEEN '2017-01-01 00:00:00' AND '2017-12-31 23:59:59'  ORDER BY timestamp DESC";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            ResultSetMetaData metadata = rs.getMetaData();
            int numcols = metadata.getColumnCount();
            while (rs.next()) {
                if (!rs.getString(5).contains("EQU_")) {
                    if (rs.getString(2).contains("KPLC meter has accumulated a time based charge")) {
                        System.out.println("Adding from mpesaOLD:>> " + rs.getString(1) + "-" + rs.getString(2) + "-" + rs.getString(3) + "-" + rs.getString(4) + "-" + rs.getString(5) + "-" + rs.getString(6));
                        codes.add(rs.getString(5));
                        List<String> row = new ArrayList<>(numcols);
                        int i = 1;
                        while (i <= numcols) {
                            row.add(rs.getString(i++));
                        }
                        mpesa.add(row);
                        System.out.println("");
                        System.out.println("====================================================================");
                    }
                }
            }
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        
        try {
            Connection con = data.connect();
            String query = "SELECT `idmpesa`,`mpesa_msisdn`,`tstamp` FROM `succinsuffrev` WHERE `idmpesa`>55283 ORDER BY `tstamp` DESC";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                System.out.println("Adding Successful: " + rs.getString(2));
                System.out.println("====================================================================");
                succphones.add(rs.getString(2));
            }
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        for (int x = 0; x < mpesa.size(); x++) {
            String msisdn = mpesa.get(x).get(0);
            if (phones.contains(msisdn)) {
                if (!addedphones.contains(msisdn)) {
                    System.out.println("Adding To revending List: " + msisdn);
                    System.out.println("====================================================================");
                    addedphones.add(msisdn);
                }
            }
        }
        double totalinsuff = 0.0;
        double totalsuccinsuff = 0.0;
        for (int z = 0; z < foundinsuff.size(); z++) {
            String phn = foundinsuff.get(z).get(0);
            double amt = Double.parseDouble(foundinsuff.get(z).get(1));

            if (addedphones.contains(phn)) {
                System.out.println("Summing insufficient amount for : " + phn + " Amount: " + amt);
                System.out.println("====================================================================");
                totalinsuff = totalinsuff + amt;
            }
            if (succphones.contains(phn)) {
                System.out.println("Summing SUCCESSVEND amount for : " + phn + " Amount: " + amt);
                System.out.println("====================================================================");
                totalsuccinsuff = totalsuccinsuff + amt;
            }
        }
        System.out.println("TOTAL INSUFFICIENT SIZE: " + addedphones.size());
        System.out.println("TOTAL SUCCESS SIZE: " + succphones.size());
        System.out.printf("TOTAL INSUFFICIENCE AMOUNT: %f\n", totalinsuff);
        System.out.printf("TOTAL SUCCESS VEND AMOUNT: %f\n", totalsuccinsuff);
       
        /*for (int y = 0; y < mpesa.size(); y++) {
            String msisdn = mpesa.get(y).get(0);
            String mpesa_acc = mpesa.get(y).get(2);
            String mpesa_code = mpesa.get(y).get(4) + "pb";
            String datee = mpesa.get(y).get(3);
            String mpesa_sender = mpesa.get(y).get(5);
            if (addedphones.contains(msisdn)) {
                if (!codes.contains(mpesa_code)) {
                    try {
                        //Sending to Ipay
                        System.out.println("Sending to Ipay Phone: " + msisdn);
                        System.out.println("====================================================================");
                        long startTime = System.nanoTime();
                        String results = "NONE";
                        try {
                            results = new Ipay().sendRequest(mpesa.get(y).get(3), msisdn, mpesa.get(y).get(4) + "pb", mpesa.get(y).get(5), "0", mpesa.get(y).get(3), mpesa.get(y).get(2));
                        } catch (Exception x) {
                        }
                        ///////do the thing////////////
                        long endTime = System.nanoTime();
                        long duration = (endTime - startTime);
                        System.out.println("Ipay Response Took " + duration / 1000000 / 1000 + " seconds");
                        System.out.println("");
                        System.out.println("====================================================================");
                        Logger.getLogger(Revendinsuff.class.getName()).log(Level.INFO, "IPAY Results:-" + results);
                        results = results.replace("$", "KES");
                        results = results.replaceAll("FAIL", "OK");
                        String[] reslt = results.split("|");
                        if (results.startsWith("OK")) {
                            String mesg = msEObj.editToken(results, mpesa_acc, msisdn, mpesa_code, datee);
                            if (mesg.contains("KPLC Mtr No") || mesg.contains("PAID")) {
                                long startTimeDBS = System.nanoTime();
                                Connection con = data.connect();
                                String values = "insert into mpesa(id,orig,tstamp,mpesa_code,mpesa_acc,mpesa_msisdn,mpesa_amt,mpesa_sender,msgid,message,originalmsg,msg_status,source,dlry_status) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                                PreparedStatement prep = con.prepareStatement(values);
                                prep.setString(1, "3630");
                                prep.setString(2, "MPESA");
                                prep.setString(3, datee);
                                prep.setString(4, mpesa_code);
                                prep.setString(5, mpesa_acc);
                                prep.setString(6, msisdn);
                                prep.setString(7, "0");
                                prep.setString(8, mpesa_sender);
                                prep.setString(9, mpesa_code);
                                prep.setString(10, mesg);
                                prep.setString(11, results);
                                prep.setString(12, "success");
                                prep.setString(13, "safaricom");
                                prep.setString(14, "SmsDeliveryWaiting");
                                prep.execute();
                                prep.close();

                                //in insuff table
                                String values2 = "insert into succinsuffrev(id,orig,tstamp,mpesa_code,mpesa_acc,mpesa_msisdn,mpesa_amt,mpesa_sender,msgid,message,originalmsg,msg_status,source,dlry_status) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                                PreparedStatement prep2 = con.prepareStatement(values2);
                                prep2.setString(1, "3630");
                                prep2.setString(2, "MPESA");
                                prep2.setString(3, datee);
                                prep2.setString(4, mpesa_code);
                                prep2.setString(5, mpesa_acc);
                                prep2.setString(6, msisdn);
                                prep2.setString(7, "0");
                                prep2.setString(8, mpesa_sender);
                                prep2.setString(9, mpesa_code);
                                prep2.setString(10, mesg);
                                prep2.setString(11, results);
                                prep2.setString(12, "success");
                                prep2.setString(13, "safaricom");
                                prep2.setString(14, "SmsDeliveryWaiting");
                                prep2.execute();
                                prep2.close();
                                con.close();
                                try {
                                    sdp.sendSMS(msisdn, mesg, mpesa_code, "704307", "http://197.248.9.105:8084/VendITNotification/SmsNotificationService");
                                } catch (Exception x) {
                                }
                                long endTimeDBS = System.nanoTime();
                                long durationDBS = (endTimeDBS - startTimeDBS);
                                System.out.println("");
                                System.out.println("====================================================================");
                                System.out.println("SENT TO CUSTOMER AND SAVED TO MPESA IN " + durationDBS / 1000000 / 1000 + " seconds");
                            } else {
                                System.out.println("");
                                System.out.println("====================================================================");
                                System.out.println("NOT SAVED AND NOT SENT TO CUSTOMER");
                            }
                        }
                    } catch (SQLException ex) {
                        System.out.println("");
                        System.out.println("====================================================================");
                        Logger.getLogger(Revendinsuff.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }*/
        System.out.println("====================END=======================");
    }

}
