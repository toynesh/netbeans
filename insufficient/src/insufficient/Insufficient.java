/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package insufficient;

import java.sql.*;
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
public class Insufficient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        DataStore data = new DataStore();

        try {
            Connection con = data.connect();
            DateTime dt = new DateTime();
            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
            String datee = fmt.print(dt);
            List<String> dstmeters = new ArrayList<>();

            String query0 = "SELECT DISTINCT `mpesa_acc` FROM `insufficient` WHERE `flag` =0";
            Statement st0 = con.createStatement();
            ResultSet rs0 = st0.executeQuery(query0);
            while (rs0.next()) {
                System.out.println("Saving: " + rs0.getString(1));
                dstmeters.add(rs0.getString(1));
            }
            System.out.println("Total Meters: " + dstmeters.size());

            for (int y = 0; y < dstmeters.size(); y++) {
                System.out.println("Checking :=" + dstmeters.get(y));
                String query2 = "SELECT count(*), SUM(`mpesa_amt`) FROM `insufficient` WHERE `mpesa_acc` ='" + dstmeters.get(y) + "' AND `flag` =0";
                Statement st2 = con.createStatement();
                ResultSet rs2 = st2.executeQuery(query2);
                while (rs2.next()) {
                    if (rs2.getInt(1) > 1) {
                        System.out.println("Meter :" + dstmeters.get(y) + " Sum:" + rs2.getDouble(2));
                        String query = "SELECT `mpesa_code`,`mpesa_msisdn`,`mpesa_sender` FROM `mpesa` WHERE `mpesa_acc` ='" + dstmeters.get(y) + "' ORDER BY timestamp DESC LIMIT 1";
                        Statement st = con.createStatement();
                        ResultSet rs = st.executeQuery(query);
                        while (rs.next()) {                            
                            String update = "update insufficient set flag = ? WHERE mpesa_acc = ?";
                            PreparedStatement preparedStmt = con.prepareStatement(update);
                            preparedStmt.setInt(1, 1);
                            preparedStmt.setString(2, dstmeters.get(y));
                            preparedStmt.executeUpdate();
                            System.out.println("Updated Insufficient FLAG");
                            
                            
                            String res = data.sendPayment(datee, rs.getString(1) + "COMB", dstmeters.get(y), rs.getString(2), Double.toString(rs2.getDouble(2)), rs.getString(3));
                            System.out.println("VENDIT Resp: " + res);
                        }

                    }
                }

            }
            //
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Insufficient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
