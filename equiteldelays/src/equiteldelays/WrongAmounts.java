/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equiteldelays;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author julius
 */
public class WrongAmounts {

    public static void main(String[] args) {

        DataStore data = new DataStore();
        String meter = "04214805808";
        String amt = "99";
        String phn = "254726269826";
        String newbankref = "100782190760rev";
        String msg = "Dear Customer,We are experiencing KPLC delays. Once the system is up we will resend your transaction. Queries call 0709711000";
        System.out.println("Meter:" + meter + " Amount:" + amt + " Phone:" + phn);
        String payres = data.sendPayment(meter, amt, phn);
        //String payres = "Token:68975967403207175134 Units:12.71 Elec:127.16 Charges:70.84";
        if (null != payres) {
            if (!payres.equals("FAIL")) {
                DateTime dt = new DateTime();
                DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                String cd = fmt.print(dt);
                if (payres.startsWith("Token")) {
                    String pressplit[] = payres.split(":");
                    msg = "KPLC Mtr No:" + meter + "\nToken:" + pressplit[1].replaceAll(" Units", "")
                            + "\nUnits:" + pressplit[2].replaceAll(" Elec", "") + "\nAmount:" + amt + "(Elec:" + pressplit[3].replaceAll(" Charges", "") + " Other Charges:" + pressplit[4] + ")" + "\nDate:" + cd
                            + "\nEquRef:" + newbankref;
                    EquitelSMS esdp = new EquitelSMS("172.27.116.22", "AIRTELRX", "pdsl219", "pdsl219", phn, msg, "VendIT");
                    esdp.submitMessage();
                    Sdp sdp = new Sdp();
                    try {
                        sdp.sendSMS(phn, msg, newbankref, "704307", "http://197.248.9.105:8084/VendITNotification/SmsNotificationService");
                    } catch (Exception sdex) {
                    }
                } else if (payres.startsWith("Paid")) {
                    String pressplit[] = payres.split(":");
                    msg = "PAID KSH:" + amt + " FOR KPLC ACCOUNT:" + meter + " RECEIPT:"
                            + pressplit[3];
                    EquitelSMS esdp = new EquitelSMS("172.27.116.22", "AIRTELRX", "pdsl219", "pdsl219", phn, msg, "VendIT");
                    esdp.submitMessage();
                    Sdp sdp = new Sdp();
                    try {
                        sdp.sendSMS(phn, msg, newbankref, "704307", "http://197.248.9.105:8084/VendITNotification/SmsNotificationService");
                    } catch (Exception sdex) {
                    }
                } else {
                    msg = payres;
                }
            }
        }
    }

    public static void main1(String[] args) {
        // TODO code application logic here
        DataStore data = new DataStore();
        Connection con = data.connect();
        List<List<String>> allequitel = new ArrayList<>();
        List<List<String>> wrongamounts = new ArrayList<>();
        try {
            String query = "SELECT `equityid`,`billNumber`,`billAmount`,`balance`,`phoneNumber`,`bankreference`  FROM `equity`";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            ResultSetMetaData metadata = rs.getMetaData();
            int numcols = metadata.getColumnCount();
            while (rs.next()) {
                List<String> row = new ArrayList<>(numcols);
                int i = 1;
                while (i <= numcols) {
                    row.add(rs.getString(i++));
                }
                allequitel.add(row);
                System.out.println("Added: " + rs.getString(2));
                System.out.println("");
                System.out.println("====================================================================");
            }
        } catch (SQLException e) {
            Logger.getLogger(Equiteldelays.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
            }
        }

        String csvFile = "/home/julius/Desktop/wdata.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] cells = line.split(cvsSplitBy);
                List<String> row = new ArrayList<>();
                row.add(cells[2]);
                if (cells[2].equals("14283396373")) {
                    row.add("5940");
                } else {
                    row.add(cells[7]);
                }
                wrongamounts.add(row);
                System.out.println("Csv Added: " + cells[2]);
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
        System.out.println("Equitel Transactions:" + allequitel.size());
        System.out.println("Wrong :" + wrongamounts.size());
        List<String> sorted = new ArrayList<>();
        for (int y = 0; y < wrongamounts.size(); y++) {
            try {
                for (int x = 0; x < allequitel.size(); x++) {
                    String id = allequitel.get(x).get(0);
                    String meter = allequitel.get(x).get(1);
                    double bal = Double.parseDouble(allequitel.get(x).get(3));
                    String phn = allequitel.get(x).get(4);
                    String newbankref = allequitel.get(x).get(5);
                    if (meter.equals(wrongamounts.get(y).get(0))) {
                        if (!sorted.contains(meter)) {
                            sorted.add(meter);
                            System.out.println("Found");
                            String amt = wrongamounts.get(y).get(1);
                            String msg = "Dear Customer,We are experiencing KPLC delays. Once the system is up we will resend your transaction. Queries call 0709711000";
                            System.out.println("Meter:" + meter + " Amount:" + amt + " Phone:" + phn);
                            String payres = data.sendPayment(meter, amt, phn);
                            //String payres = null;
                            if (null != payres) {
                                if (!payres.equals("FAIL")) {
                                    DateTime dt = new DateTime();
                                    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                                    String cd = fmt.print(dt);
                                    if (payres.startsWith("Token")) {
                                        String pressplit[] = payres.split(":");
                                        msg = "KPLC Mtr No:" + meter + "\nToken:" + pressplit[1].replaceAll(" Units", "")
                                                + "\nUnits:" + pressplit[2].replaceAll(" Elec", "") + "\nAmount:" + amt + "(Elec:" + pressplit[3].replaceAll(" Charges", "") + " Other Charges:" + pressplit[4] + ")" + "\nDate:" + cd
                                                + "\nEquRef:" + newbankref;
                                        bal = 0.0;
                                        EquitelSMS esdp = new EquitelSMS("172.27.116.22", "AIRTELRX", "pdsl219", "pdsl219", phn, msg, "VendIT");
                                        esdp.submitMessage();
                                        Sdp sdp = new Sdp();
                                        try {
                                            sdp.sendSMS(phn, msg, newbankref, "704307", "http://197.248.9.105:8084/VendITNotification/SmsNotificationService");
                                        } catch (Exception sdex) {
                                        }
                                    } else if (payres.startsWith("Paid")) {
                                        String pressplit[] = payres.split(":");
                                        msg = "PAID KSH:" + amt + " FOR KPLC ACCOUNT:" + meter + " RECEIPT:"
                                                + pressplit[3];
                                        bal = 0.0;
                                        EquitelSMS esdp = new EquitelSMS("172.27.116.22", "AIRTELRX", "pdsl219", "pdsl219", phn, msg, "VendIT");
                                        esdp.submitMessage();
                                        Sdp sdp = new Sdp();
                                        try {
                                            sdp.sendSMS(phn, msg, newbankref, "704307", "http://197.248.9.105:8084/VendITNotification/SmsNotificationService");
                                        } catch (Exception sdex) {
                                        }
                                    } else {
                                        msg = payres;
                                    }
                                }
                            }
                        }
                    } else {
                        System.err.println("Not found!!");
                    }
                }
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }

        Logger.getLogger(Equiteldelays.class
                .getName()
        ).log(Level.INFO, "DONE PROCESSING EQUITEL DELAYS");
    }
}
