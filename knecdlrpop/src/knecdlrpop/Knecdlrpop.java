/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knecdlrpop;

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

/**
 *
 * @author coolie
 */
public class Knecdlrpop {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        DataStore data = new DataStore();
        List<List<String>> dlry = new ArrayList<>();
        List<String> portalmsgid = new ArrayList<>();
        Connection con44 = data.connect44();
        String query = "select timesent,smsc,sender,reciever,message,SUBSTRING(message,4,10) from dlry where timesent>'2018-12-21 00:00:00' AND smsc='SAFARICOM'";
        System.out.println(query);
        try {
            Statement st = con44.createStatement();
            ResultSet rs = st.executeQuery(query);
            ResultSetMetaData metadata = rs.getMetaData();
            int numcols = metadata.getColumnCount();
            while (rs.next()) {
                List<String> row = new ArrayList<>(numcols);
                int i = 1;
                while (i <= numcols) {
                    row.add(rs.getString(i++));
                }
                dlry.add(row);
            }
            con44.close();
        } catch (SQLException sq) {
            Logger.getLogger(Knecdlrpop.class.getName()).log(Level.SEVERE, "44QUERRY ERROR!!!!!!!!!!!!!!!!!!" + sq);
        }
        String pquery = "select msgid from kcseportaldata where smsc='SAFARICOM'";
        System.out.println(pquery);
        try {
            Connection con = data.connect();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(pquery);
            while (rs.next()) {
                portalmsgid.add(rs.getString(1));
            }
            con.close();
        } catch (SQLException sq) {
            Logger.getLogger(Knecdlrpop.class.getName()).log(Level.SEVERE, "PORTALQUERRY ERROR!!!!!!!!!!!!!!!!!!" + sq);
        }
        System.out.println("Saf Portal data size: " + portalmsgid);
        int wcount = 0;
        for (int y = 0; y < dlry.size(); y++) {
            String timesent = dlry.get(y).get(0);
            String smsc = dlry.get(y).get(1);
            String sender = dlry.get(y).get(2);
            String reciever = dlry.get(y).get(3);
            String message = dlry.get(y).get(4);
            String msid = dlry.get(y).get(5);

            if (!portalmsgid.contains(msid)) {
                String msgsplit[] = message.split("text:");
                String nmsplit[] = msgsplit[1].split(" ");
                String sname = "";
                if (nmsplit.length > 2) {
                    if (!nmsplit[2].startsWith("IND") && !nmsplit[2].startsWith("IN") && !nmsplit[2].startsWith("I")) {
                        sname = nmsplit[0] + " " + nmsplit[1] + " " + nmsplit[2];
                    }
                } else {
                    //KARPA ALICE TITO
                    // KARPA ALICE TITO               INDEX: 32546407020 MEAN GRADE D AGP 19 ENG D- KIS D- MAT E BIO D CHE E HIS D CRE C+ BUS D-  KNEC HELPLINE 0800724900
                    try {
                        sname = nmsplit[0] + " " + nmsplit[1];
                    } catch (IndexOutOfBoundsException ioob) {
                        ioob.printStackTrace();
                    }
                }

                System.out.println("Checking: " + sname);
                long startTime = System.nanoTime();
                String snd_txt = data.getMessage(sname);
                long endTime = System.nanoTime();
                long duration = (endTime - startTime);
                System.out.println("QUERY TOOK:" + duration + " nanoseconds");
                System.out.println("QUERY TOOK:" + duration / 1000000 + " milliseconds");
                System.out.println("QUERY TOOK:" + duration / 1000000 / 1000 + " seconds");
                System.out.println("Serverres: " + snd_txt);
                if (snd_txt.contains("does not exist")) {
                    System.out.println("Wrong Index");
                    wcount++;
                    //System.exit(0);
                } else {
                    String resplit[] = snd_txt.split("INDEX: ");
                    String consplit[] = resplit[1].split(" ");
                    String indexno = consplit[0];
                    String insert = "insert into kcseportaldata (`time_recieved`, `smsc`, `sender`, `shortcode`, `inmessage`, `timesent`, `outmessage`, `msgid`, `sendresults`, `deliverystatus`, `status`) values (?,?,?,?,?,?,?,?,?,?,?)";
                    try {
                        Connection con = data.connect();
                        PreparedStatement prep = con.prepareStatement(insert);
                        prep.setString(1, timesent);
                        prep.setString(2, smsc);
                        prep.setString(3, sender);
                        prep.setString(4, reciever);
                        prep.setString(5, indexno);
                        prep.setString(6, timesent);
                        prep.setString(7, snd_txt);
                        prep.setString(8, msid);
                        prep.setString(9, "OK");
                        prep.setString(10, "MessageSent");
                        prep.setInt(11, 0);
                        prep.execute();
                        prep.close();
                        con.close();
                    } catch (SQLException in) {
                        Logger.getLogger(Knecdlrpop.class.getName()).log(Level.SEVERE, "SAVING ERROR!!!!!!!!!!!!!!!!!!" + in);
                    }
                }
            } else {
                System.out.println("Message id exist: " + msid);
            }
        }
        System.out.println("Wrong Index count: " + wcount);
        System.out.println("Dlry count: " + dlry.size());

    }

}
