/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equiteldelays;

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
 * @author coolie
 */
public class Equiteldelays {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        DataStore data = new DataStore();
        Connection con = data.connect();
        List<List<String>> delayed = new ArrayList<>();
        try {
            String query = "SELECT `equityid`,`billNumber`,`billAmount`,`balance`,`phoneNumber`,`bankreference`  FROM `equity` WHERE `message` LIKE '%We are experiencing KPLC delays%'";
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
                delayed.add(row);
                System.out.println("Added: " + rs.getString(2));
                System.out.println("");
                System.out.println("====================================================================");
            }
            for (int x = 0; x < delayed.size(); x++) {
                String id = delayed.get(x).get(0);
                String meter = delayed.get(x).get(1);
                String amt = delayed.get(x).get(2);
                double bal = Double.parseDouble(delayed.get(x).get(3));
                String phn = delayed.get(x).get(4);
                String newbankref = delayed.get(x).get(5);
                if (bal > 0) {
                    String msg = "Dear Customer,We are experiencing KPLC delays. Once the system is up we will resend your transaction. Queries call 0709711000";
                    String payres = data.sendPayment(meter, amt, phn);
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
                                EquitelSMS esdp = new EquitelSMS("172.27.116.22", "EQUITELRX", "pdsl219", "pdsl219", phn, msg, "VendIT");
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
                                EquitelSMS esdp = new EquitelSMS("172.27.116.22", "EQUITELRX", "pdsl219", "pdsl219", phn, msg, "VendIT");
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
                    String update = "update equity set balance = ?,message = ? WHERE equityid = ?";
                    PreparedStatement preparedStmt = con.prepareStatement(update);
                    preparedStmt.setDouble(1, bal);
                    preparedStmt.setString(2, msg);
                    preparedStmt.setString(3, id);
                    preparedStmt.executeUpdate();
                    System.out.println("Updated Equitel table");
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(Equiteldelays.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
            }
        }
        Logger.getLogger(Equiteldelays.class.getName()).log(Level.INFO, "DONE PROCESSING EQUITEL DELAYS");
    }

}
