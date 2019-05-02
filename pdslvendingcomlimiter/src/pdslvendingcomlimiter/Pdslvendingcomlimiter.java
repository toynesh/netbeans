/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdslvendingcomlimiter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
public class Pdslvendingcomlimiter {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        DataStore data = new DataStore();
        DateTime dt = new DateTime();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy");
        String year = fmt.print(dt);
        DateTimeFormatter formatter = DateTimeFormat.forPattern("MMM");
        String month = formatter.print(dt);
        List<String> allaccounts = new ArrayList<>();
        List<String> allmeters = new ArrayList<>();

        try {
            Connection con = data.connect();
            //String query = "SELECT `tran_account` FROM (SELECT * `transactions" + month + year + "` WHERE)  WHERE  LENGTH(`mpesa_code`)<11 AND SUM(`tran_amt`)>100000";
            //System.out.println(query);
            String query = "SELECT `tran_account` FROM  `transactions" + month + year + "` WHERE LENGTH(`tran_account`)<11 AND `tran_account` IS NOT NULL";
            // System.out.println(query);
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(query);
            /*if (rs.isBeforeFirst()) {
                System.out.println("found");
            }*/
            while (rs.next()) {
                if (rs.getString(1).startsWith("saf_")) {

                } else if (rs.getString(1).startsWith("air_")) {

                } else {
                    allaccounts.add(rs.getString(1));
                }
            }
            System.out.println("Postpaid Array Size: " + allaccounts.size());

            String query2 = "SELECT `tran_account` FROM  `transactions" + month + year + "` WHERE LENGTH(`tran_account`)>10 AND `tran_account` IS NOT NULL";
            Statement stm2 = con.createStatement();
            ResultSet rs2 = stm2.executeQuery(query2);
            while (rs2.next()) {
                if (rs2.getString(1).startsWith("saf_")) {

                } else if (rs2.getString(1).startsWith("air_")) {

                } else {
                    allmeters.add(rs2.getString(1));
                }
            }
            System.out.println("Prepaid Array Size: " + allmeters.size());

            for (int y = 0; y < allaccounts.size(); y++) {
                String query3 = "SELECT SUM(`tran_amt`) FROM  `transactions" + month + year + "` WHERE `tran_account`='" + allaccounts.get(y) + "'";
                Statement stm3 = con.createStatement();
                ResultSet rs3 = stm3.executeQuery(query3);
                while (rs3.next()) {
                    if (rs3.getDouble(1) > 100000) {
                        System.out.println("Account with maxlimit detected");
                        try {
                            String values = "insert into comlimit" + month + year + "(tran_account) values (?)";
                            PreparedStatement prep = con.prepareStatement(values);
                            prep.setString(1, allaccounts.get(y));
                            prep.execute();
                            prep.close();
                        } catch (SQLException ex) {
                            
                        }
                    }
                }
            }
            for (int z = 0; z < allmeters.size(); z++) {
                String query4 = "SELECT SUM(`tran_amt`) FROM  `transactions" + month + year + "` WHERE `tran_account`='" + allmeters.get(z) + "'";
                Statement stm4 = con.createStatement();
                ResultSet rs4 = stm4.executeQuery(query4);
                while (rs4.next()) {
                    if (rs4.getDouble(1) > 50000) {
                        System.out.println("Account with maxlimit detected");
                        try {
                            String values = "insert into comlimit" + month + year + "(tran_account) values (?)";
                            PreparedStatement prep = con.prepareStatement(values);
                            prep.setString(1, allmeters.get(z));
                            prep.execute();
                            prep.close();
                        } catch (SQLException ex) {
                            
                        }

                    }
                }
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
