/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knecdatadump;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.Document;

/**
 *
 * @author julius
 */
public class Knecdatadump {

    /**
     * @param args the command line arguments
     */
    static DataStore data = new DataStore();
    static MongoDatabase database42 = data.mongoconnect("172.27.116.42");
    static MongoDatabase database44 = data.mongoconnect("172.27.116.44");
    static MongoDatabase database46 = data.mongoconnect("172.27.116.46");

    public static void main(String[] args) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<List<String>> databasesdata = new ArrayList<>();

        //LAST DATE
        String ltime = "none";
        try {
            Connection con = data.connect();
            Statement stm = con.createStatement();
            String query = "SELECT timesent from kcpe2019failed order by id desc limit 1";
            ResultSet rs = stm.executeQuery(query);
            while (rs.next()) {
                ltime = rs.getString(1);
            }
            Logger.getLogger(Knecdatadump.class.getName()).log(Level.INFO, "Last time..." + ltime);
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Knecdatadump.class.getName()).log(Level.SEVERE, null, ex);
        }

        //PULL DATA
        if (!ltime.equals("none")) {
            try {
                MongoCollection<Document> collection = database42.getCollection("sms");
                // Getting the iterable object 
                FindIterable<Document> iterDoc = collection.find();
                // Getting the iterator 
                int numcols = 11;
                for (Document doc : iterDoc) {
                    //System.out.println("Doc: " + doc);
                    //System.out.println(doc.getString("shortcode"));

                    List<String> row = new ArrayList<>(numcols);
                    String dt = doc.getString("time_recieved");
                    Date date = formatter.parse(dt);
                    Date date2 = formatter.parse(ltime);
                    // System.out.println(date);
                    row.add(doc.getString("time_recieved"));
                    row.add(doc.getString("smsc"));
                    row.add(doc.getString("sender"));
                    row.add(doc.getString("shortcode"));
                    row.add(doc.getString("inmessage"));
                    //Date dt2 = doc.getDate("timesent");
                    //String date2 = formatter.format(dt2);
                    //System.out.println(date2);
                    row.add(doc.getString("time_recieved"));
                    row.add(doc.getString("outmessage"));
                    row.add(doc.getString("msgid"));
                    row.add(doc.getString("sendresults"));
                    row.add(doc.getString("deliverystatus"));
                    row.add("0");
                    if (date.compareTo(date2) > 0) {
                        databasesdata.add(row);
                    }
                }
                Logger.getLogger(Knecdatadump.class.getName()).log(Level.INFO, "Array size: " + databasesdata.size());
            } catch (Exception mox) {
                Logger.getLogger(Knecdatadump.class.getName()).log(Level.WARNING, "Error pulling 42 data " + mox);
            }
            try {
                MongoCollection<Document> collection = database44.getCollection("sms");
                // Getting the iterable object 
                FindIterable<Document> iterDoc = collection.find();
                // Getting the iterator 
                int numcols = 11;
                for (Document doc : iterDoc) {
                    //System.out.println("Doc: " + doc);
                    //System.out.println(doc.getInteger("shortcode"));

                    List<String> row = new ArrayList<>(numcols);
                    String dt = doc.getString("time_recieved");
                    Date date = formatter.parse(dt);
                    Date date2 = formatter.parse(ltime);
                    // System.out.println(date);
                    row.add(doc.getString("time_recieved"));
                    row.add(doc.getString("smsc"));
                    row.add(doc.getString("sender"));
                    row.add(doc.getString("shortcode"));
                    row.add(doc.getString("inmessage"));
                    //Date dt2 = doc.getDate("timesent");
                    //String date2 = formatter.format(dt2);
                    //System.out.println(date2);
                    row.add(doc.getString("time_recieved"));
                    row.add(doc.getString("outmessage"));
                    row.add(doc.getString("msgid"));
                    row.add(doc.getString("sendresults"));
                    row.add(doc.getString("deliverystatus"));
                    row.add("0");
                    if (date.compareTo(date2) > 0) {
                        databasesdata.add(row);
                    }
                }
                Logger.getLogger(Knecdatadump.class.getName()).log(Level.INFO, "Array size: " + databasesdata.size());
            } catch (Exception mox) {
                Logger.getLogger(Knecdatadump.class.getName()).log(Level.WARNING, "Error pulling 44 data " + mox);
            }
            try {
                MongoCollection<Document> collection = database46.getCollection("sms");
                // Getting the iterable object 
                FindIterable<Document> iterDoc = collection.find();
                // Getting the iterator 
                int numcols = 11;
                for (Document doc : iterDoc) {
                    //System.out.println("Doc: " + doc);
                    //System.out.println(doc.getInteger("shortcode"));

                    List<String> row = new ArrayList<>(numcols);
                    String dt = doc.getString("time_recieved");
                    Date date = formatter.parse(dt);
                    Date date2 = formatter.parse(ltime);
                    // System.out.println(date);
                    row.add(doc.getString("time_recieved"));
                    row.add(doc.getString("smsc"));
                    row.add(doc.getString("sender"));
                    row.add(doc.getString("shortcode"));
                    row.add(doc.getString("inmessage"));
                    //Date dt2 = doc.getDate("timesent");
                    //String date2 = formatter.format(dt2);
                    //System.out.println(date2);
                    row.add(doc.getString("time_recieved"));
                    row.add(doc.getString("outmessage"));
                    row.add(doc.getString("msgid"));
                    row.add(doc.getString("sendresults"));
                    row.add(doc.getString("deliverystatus"));
                    row.add("0");
                    if (date.compareTo(date2) > 0) {
                        databasesdata.add(row);
                    }
                }
                Logger.getLogger(Knecdatadump.class.getName()).log(Level.INFO, "Array size: " + databasesdata.size());
            } catch (Exception mox) {
                Logger.getLogger(Knecdatadump.class.getName()).log(Level.WARNING, "Error pulling 46 data " + mox);
            }

            // TODO code application logic here
            /*try {
            //DELETE TABLES
            Connection con = data.connect();
            Statement stmt = con.createStatement();
            String sql = "DROP TABLE kcpe2019portaldata ";
            stmt.executeUpdate(sql);
            Logger.getLogger(Knecdatadump.class.getName()).log(Level.WARNING, "kcpe2019portaldata Table  deleted...");
            Statement stmt2 = con.createStatement();
            String sql2 = "DROP TABLE kcpe2019failedreg ";
            stmt2.executeUpdate(sql2);
            Logger.getLogger(Knecdatadump.class.getName()).log(Level.WARNING, "kcpe2019failedreg Table  deleted...");

            con.close();
        } catch (SQLException ex0) {
            Logger.getLogger(Knecdatadump.class.getName()).log(Level.WARNING, "Tables already deleted");
        }

        try {
            //CREATE TABLES
            Connection con = data.connect();
            String kcpe2019portaldata = "CREATE TABLE IF NOT EXISTS kcpe2019portaldata ("
                    + "id int(11) NOT NULL AUTO_INCREMENT,"
                    + "time_recieved varchar(200),"
                    + "smsc varchar(100),"
                    + "sender varchar(200),"
                    + "shortcode varchar(200),"
                    + "inmessage text,"
                    + "timesent varchar(200),"
                    + "outmessage text,"
                    + "msgid text,"
                    + "sendresults varchar(200),"
                    + "deliverystatus varchar(200),"
                    + "status int(11) NOT NULL DEFAULT '0',"
                    + "PRIMARY KEY (id))";
            String kcpe2019failedreg = "CREATE TABLE IF NOT EXISTS kcpe2019failed ("
                    + "id int(11) NOT NULL AUTO_INCREMENT,"
                    + "time_recieved varchar(200),"
                    + "smsc varchar(100),"
                    + "sender varchar(200),"
                    + "shortcode varchar(200),"
                    + "inmessage text,"
                    + "timesent varchar(200),"
                    + "outmessage text,"
                    + "msgid text,"
                    + "sendresults varchar(200),"
                    + "deliverystatus varchar(200) ,"
                    + "status int(11) NOT NULL DEFAULT '0',"
                    + "PRIMARY KEY (id))";
            Statement stm = con.createStatement();
            stm.execute(kcpe2019portaldata);
            stm.execute(kcpe2019failedreg);
            Logger.getLogger(Knecdatadump.class.getName()).log(Level.INFO, "Tables Created...");
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Knecdatadump.class.getName()).log(Level.SEVERE, null, ex);
        }*/
            for (int x = 0; x < databasesdata.size(); x++) {
                String time_recieved = databasesdata.get(x).get(0);
                String smsc = databasesdata.get(x).get(1);
                String sender = databasesdata.get(x).get(2);
                String shortcode = databasesdata.get(x).get(3);
                String inmessage = databasesdata.get(x).get(4);
                String timesent = databasesdata.get(x).get(5);
                String outmessage = databasesdata.get(x).get(6);
                String msgid = databasesdata.get(x).get(7);
                String sendresults = databasesdata.get(x).get(8);
                String deliverystatus = databasesdata.get(x).get(9);
                String status = databasesdata.get(x).get(10);
                if (!outmessage.toLowerCase().startsWith("dear customer, ")) {
                    if (sendresults != null) {
                        if (sendresults.equals("OK")) {
                            Logger.getLogger(Knecdatadump.class.getName()).log(Level.INFO, "Successful ");
                            String insert = "INSERT INTO `kcpe2019portaldata`(`time_recieved`, `smsc`, `sender`, `shortcode`, `inmessage`, `timesent`, `outmessage`, `msgid`, `sendresults`, `deliverystatus`, `status`) "
                                    + "VALUES ('" + time_recieved + "', '" + smsc + "', '" + sender + "', '" + shortcode + "', '" + inmessage + "', '" + timesent + "', '" + outmessage + "', '" + msgid + "', '" + sendresults + "', '" + deliverystatus + "', '" + status + "')";
                            data.insert(insert);
                        } else {
                            Logger.getLogger(Knecdatadump.class.getName()).log(Level.INFO, "Status not OK ");
                            String insert = "INSERT INTO `kcpe2019failed`(`time_recieved`, `smsc`, `sender`, `shortcode`, `inmessage`, `timesent`, `outmessage`, `msgid`, `sendresults`, `deliverystatus`, `status`) "
                                    + "VALUES ('" + time_recieved + "', '" + smsc + "', '" + sender + "', '" + shortcode + "', '" + inmessage + "', '" + timesent + "', '" + outmessage + "', '" + msgid + "', '" + sendresults + "', '" + deliverystatus + "', '" + status + "')";
                            data.insert(insert);
                        }
                    } else {
                        Logger.getLogger(Knecdatadump.class.getName()).log(Level.INFO, "Status is null ");
                        String insert = "INSERT INTO `kcpe2019failed`(`time_recieved`, `smsc`, `sender`, `shortcode`, `inmessage`, `timesent`, `outmessage`, `msgid`, `sendresults`, `deliverystatus`, `status`) "
                                + "VALUES ('" + time_recieved + "', '" + smsc + "', '" + sender + "', '" + shortcode + "', '" + inmessage + "', '" + timesent + "', '" + outmessage + "', '" + msgid + "', '" + sendresults + "', '" + deliverystatus + "', '" + status + "')";
                        data.insert(insert);
                    }
                } else {
                    Logger.getLogger(Knecdatadump.class.getName()).log(Level.INFO, "Starts with Dear customer ");
                    String insert = "INSERT INTO `kcpe2019failed`(`time_recieved`, `smsc`, `sender`, `shortcode`, `inmessage`, `timesent`, `outmessage`, `msgid`, `sendresults`, `deliverystatus`, `status`) "
                            + "VALUES ('" + time_recieved + "', '" + smsc + "', '" + sender + "', '" + shortcode + "', '" + inmessage + "', '" + timesent + "', '" + outmessage + "', '" + msgid + "', '" + sendresults + "', '" + deliverystatus + "', '" + status + "')";
                    data.insert(insert);
                }
            }
        
        }else{
            Logger.getLogger(Knecdatadump.class.getName()).log(Level.INFO, "Last time is empty");
        }
        System.out.println("====================Done===============");

    }

}
