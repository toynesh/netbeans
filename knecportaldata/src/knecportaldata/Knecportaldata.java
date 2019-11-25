/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knecportaldata;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author julius
 */
public class Knecportaldata {

    /**
     * @param args the command line arguments
     */
    static DataStore data = new DataStore();

    public static void main(String[] args) {
        try {
            // TODO code application logic here
            Connection con = data.connect();
            /*Connection con44 = data.connect44();
            Connection con46 = data.connect46();

            //DELETE TABLES
            Statement stmt = con.createStatement();
            String sql = "DROP TABLE regportaldata ";
            stmt.executeUpdate(sql);
            System.out.println("regportaldata Table  deleted in given database...");
            Statement stmt2 = con.createStatement();
            String sql2 = "DROP TABLE failedreg ";
            stmt2.executeUpdate(sql2);
            System.out.println("failedreg Table  deleted in given database...");*/
            Statement stmt78 = con.createStatement();
            String sql78 = "DROP TABLE seveneightportaldata ";
            stmt78.executeUpdate(sql78);
            System.out.println("seveneightportaldata Table  deleted in given database...");

            //CREATE TABLES
            /*Statement cstmt = con.createStatement();
            String csql = "CREATE TABLE regportaldata AS SELECT * FROM sms WHERE outmessage NOT LIKE 'Dear customer, you have insufficient airtime%' AND sendresults='OK';";
            cstmt.executeUpdate(csql);
            System.out.println("Created regportaldata table...");
            Statement cstmt2 = con.createStatement();
            String csql2 = "CREATE TABLE failedreg AS SELECT * FROM sms WHERE outmessage LIKE 'Dear customer%';";
            cstmt2.executeUpdate(csql2);
            System.out.println("Created failedreg table...");*/
            Statement cstmt78 = con.createStatement();
            String csql78 = "CREATE TABLE seveneightportaldata AS SELECT * FROM sms WHERE id<0;";
            cstmt78.executeUpdate(csql78);
            System.out.println("Created seveneightportaldata table...");

            //INSERT INTO TABLES
            String isql78 = "INSERT INTO `seveneightportaldata`(`time_recieved`, `smsc`, `sender`, `shortcode`, `inmessage`, `timesent`, `outmessage`, `msgid`, `sendresults`, `deliverystatus`, `status`) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
            Statement statement78 = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            PreparedStatement pstmt78 = con.prepareStatement(isql78);
            ResultSet frs78 = statement78.executeQuery("SELECT `time_recieved`, `smsc`, `sender`, `reciever`, `message`, `timesent`, ' ', ' ', 'OK', 'Done', '1' FROM `inbox78`;");
            while (frs78.next()) {
                String time_recieved = frs78.getString(1);
                String smsc = frs78.getString(2);
                String sender = frs78.getString(3);
                String shortcode = frs78.getString(4);
                String inmessage = frs78.getString(5);
                String timesent = frs78.getString(6);
                String outmessage = frs78.getString(7);
                String msgid = frs78.getString(8);
                String sendresults = frs78.getString(9);
                String deliverystatus = frs78.getString(10);
                String status = frs78.getString(11);
                pstmt78.setString(1, time_recieved);
                pstmt78.setString(2, smsc);
                pstmt78.setString(3, sender);
                pstmt78.setString(4, shortcode);
                pstmt78.setString(5, inmessage);
                pstmt78.setString(6, timesent);
                pstmt78.setString(7, outmessage);
                pstmt78.setString(8, msgid);
                pstmt78.setString(9, sendresults);
                pstmt78.setString(10, deliverystatus);
                pstmt78.setString(11, status);
                pstmt78.executeUpdate();
            }
            System.out.println("Inserted into seveneightportaldata table from inbox78...");

            /*String isql = "INSERT INTO `regportaldata`(`time_recieved`, `smsc`, `sender`, `shortcode`, `inmessage`, `timesent`, `outmessage`, `msgid`, `sendresults`, `deliverystatus`, `status`) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
            Statement statement = con44.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            PreparedStatement pstmt = con.prepareStatement(isql);
            ResultSet rs = statement.executeQuery("SELECT `time_recieved`, `smsc`, `sender`, `shortcode`, `inmessage`, `timesent`, `outmessage`, `msgid`, `sendresults`, `deliverystatus`, `status` from sms where outmessage NOT LIKE 'Dear customer, you have insufficient airtime%' AND sendresults='OK';");
            while (rs.next()) {
                String time_recieved = rs.getString(1);
                String smsc = rs.getString(2);
                String sender = rs.getString(3);
                String shortcode = rs.getString(4);
                String inmessage = rs.getString(5);
                String timesent = rs.getString(6);
                String outmessage = rs.getString(7);
                String msgid = rs.getString(8);
                String sendresults = rs.getString(9);
                String deliverystatus = rs.getString(10);
                String status = rs.getString(11);
                pstmt.setString(1, time_recieved);
                pstmt.setString(2, smsc);
                pstmt.setString(3, sender);
                pstmt.setString(4, shortcode);
                pstmt.setString(5, inmessage);
                pstmt.setString(6, timesent);
                pstmt.setString(7, outmessage);
                pstmt.setString(8, msgid);
                pstmt.setString(9, sendresults);
                pstmt.setString(10, deliverystatus);
                pstmt.setString(11, status);
                pstmt.executeUpdate();
            }
            System.out.println("Inserted into regportaldata table from 44...");
            Statement statement2 = con46.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs2 = statement2.executeQuery("SELECT `time_recieved`, `smsc`, `sender`, `shortcode`, `inmessage`, `timesent`, `outmessage`, `msgid`, `sendresults`, `deliverystatus`, `status` from sms where outmessage NOT LIKE 'Dear customer, you have insufficient airtime%' AND sendresults='OK';");
            while (rs2.next()) {
                String time_recieved = rs2.getString(1);
                String smsc = rs2.getString(2);
                String sender = rs2.getString(3);
                String shortcode = rs2.getString(4);
                String inmessage = rs2.getString(5);
                String timesent = rs2.getString(6);
                String outmessage = rs2.getString(7);
                String msgid = rs2.getString(8);
                String sendresults = rs2.getString(9);
                String deliverystatus = rs2.getString(10);
                String status = rs2.getString(11);
                pstmt.setString(1, time_recieved);
                pstmt.setString(2, smsc);
                pstmt.setString(3, sender);
                pstmt.setString(4, shortcode);
                pstmt.setString(5, inmessage);
                pstmt.setString(6, timesent);
                pstmt.setString(7, outmessage);
                pstmt.setString(8, msgid);
                pstmt.setString(9, sendresults);
                pstmt.setString(10, deliverystatus);
                pstmt.setString(11, status);
                pstmt.executeUpdate();
            }
            System.out.println("Inserted into regportaldata table from 46...");
            //------------------------------------------------

            String fisql = "INSERT INTO `failedreg`(`time_recieved`, `smsc`, `sender`, `shortcode`, `inmessage`, `timesent`, `outmessage`, `msgid`, `sendresults`, `deliverystatus`, `status`) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
            Statement fstatement = con44.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            PreparedStatement fpstmt = con.prepareStatement(fisql);
            ResultSet frs = fstatement.executeQuery("SELECT `time_recieved`, `smsc`, `sender`, `shortcode`, `inmessage`, `timesent`, `outmessage`, `msgid`, `sendresults`, `deliverystatus`, `status` from sms where outmessage LIKE 'Dear customer%';");
            while (frs.next()) {
                String time_recieved = frs.getString(1);
                String smsc = frs.getString(2);
                String sender = frs.getString(3);
                String shortcode = frs.getString(4);
                String inmessage = frs.getString(5);
                String timesent = frs.getString(6);
                String outmessage = frs.getString(7);
                String msgid = frs.getString(8);
                String sendresults = frs.getString(9);
                String deliverystatus = frs.getString(10);
                String status = frs.getString(11);
                fpstmt.setString(1, time_recieved);
                fpstmt.setString(2, smsc);
                fpstmt.setString(3, sender);
                fpstmt.setString(4, shortcode);
                fpstmt.setString(5, inmessage);
                fpstmt.setString(6, timesent);
                fpstmt.setString(7, outmessage);
                fpstmt.setString(8, msgid);
                fpstmt.setString(9, sendresults);
                fpstmt.setString(10, deliverystatus);
                fpstmt.setString(11, status);
                fpstmt.executeUpdate();
            }
            System.out.println("Inserted into failedreg table from 44...");
            Statement fstatement2 = con46.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet frs2 = fstatement2.executeQuery("SELECT `time_recieved`, `smsc`, `sender`, `shortcode`, `inmessage`, `timesent`, `outmessage`, `msgid`, `sendresults`, `deliverystatus`, `status` from sms where outmessage LIKE 'Dear customer%';");
            while (frs2.next()) {
                String time_recieved = frs2.getString(1);
                String smsc = frs2.getString(2);
                String sender = frs2.getString(3);
                String shortcode = frs2.getString(4);
                String inmessage = frs2.getString(5);
                String timesent = frs2.getString(6);
                String outmessage = frs2.getString(7);
                String msgid = frs2.getString(8);
                String sendresults = frs2.getString(9);
                String deliverystatus = frs2.getString(10);
                String status = frs2.getString(11);
                fpstmt.setString(1, time_recieved);
                fpstmt.setString(2, smsc);
                fpstmt.setString(3, sender);
                fpstmt.setString(4, shortcode);
                fpstmt.setString(5, inmessage);
                fpstmt.setString(6, timesent);
                fpstmt.setString(7, outmessage);
                fpstmt.setString(8, msgid);
                fpstmt.setString(9, sendresults);
                fpstmt.setString(10, deliverystatus);
                fpstmt.setString(11, status);
                fpstmt.executeUpdate();
            }
            System.out.println("Inserted into failedreg table from 46...");*/

            con.close();
            /*con44.close();
            con46.close();*/
            System.out.println("====================Done===============");
        } catch (SQLException ex) {
            Logger.getLogger(Knecportaldata.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
