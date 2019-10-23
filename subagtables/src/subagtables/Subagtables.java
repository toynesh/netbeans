/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package subagtables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author julius
 */
public class Subagtables {

    /**
     * @param args the command line arguments
     */
    static DataStore data = new DataStore();
    public static void main(String[] args) {
        // TODO code application logic here
        DateTime dt = new DateTime();
        if (dt.dayOfMonth().get() == 1) {
            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy");
            String year = fmt.print(dt);
            DateTimeFormatter formatter = DateTimeFormat.forPattern("MMM");
            String month = formatter.print(dt);

            //create default user
            try {
                Connection con = data.connect();
                String query = "select id from transactions" + month + year + " ORDER BY id DESC LIMIT 1";
                Statement stm = con.createStatement();
                ResultSet rs = stm.executeQuery(query);
                if (rs.isBeforeFirst()) {
                    //System.out.println("SKIPPING CREATING TABLES");
                }
                con.close();
            } catch (SQLException emptydb) {
                //e.printStackTrace();
                System.out.println("CREATING TABLES");
                data.createTables(month + year);

                try {
                    Connection con = data.connect();
                    String query = "SELECT * from users";
                    Statement stm = con.createStatement();
                    ResultSet rs = stm.executeQuery(query);
                    if (!rs.isBeforeFirst()) {
                        String values = "insert into users(full_names,uname,upass,utype,vendor_code) values (?,?,?,?,?)";
                        try {
                            PreparedStatement prep = con.prepareStatement(values);
                            prep.setString(1, "Professional Digital Systems");
                            prep.setString(2, "pdsl");
                            prep.setString(3, "pdsl0000");
                            prep.setString(4, "admin");
                            prep.setInt(5, 1000);
                            prep.execute();
                            prep.close();
                        } catch (SQLException ex) {
                            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    Connection con = data.connect();
                    String values = "insert into reference(refe) values (?)";

                    PreparedStatement prep = con.prepareStatement(values);
                    prep.setString(1, "100000000001");
                    prep.execute();
                    prep.close();

                    con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            System.out.println(dt.dayOfMonth().get() + " Day of the month");
        }
    }
    
}
