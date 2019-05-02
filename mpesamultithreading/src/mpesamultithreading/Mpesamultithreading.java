/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpesamultithreading;

import java.sql.Connection;
import java.sql.ResultSet;
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
public class Mpesamultithreading {

    /**
     * @param args the command line arguments
     */
    List<String> mpesacodes = new ArrayList<>();
    DataStore data = new DataStore();

    public static void main(String[] args) {
        // TODO code application logic here
        Mpesamultithreading mt = new Mpesamultithreading();        
        mt.callArray();
        String arrvl = "MBS1ZFCLQH";
        if (mt.mpesacodes.contains(arrvl)) {
            Logger.getLogger(Mpesamultithreading.class.getName()).log(Level.INFO, "In Array code>> : " + arrvl);
        } else {
            Logger.getLogger(Mpesamultithreading.class.getName()).log(Level.INFO, "Not in Array code>> : " + arrvl);
        }
        //mt.mpesacodes.add(arrvl);
        System.out.println("Codes Array Size after Current Add: " + mt.mpesacodes.size());

    }

    public void callArray() {
        if (mpesacodes.size() == 0) {
            try {
                Connection con = data.connect();
                String query = "SELECT mpesa_code FROM mpesa LIMIT 100";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    System.out.println("Saving: " + rs.getString(1));
                    mpesacodes.add(rs.getString(1));
                }
                System.out.println("Codes Array Size: " + mpesacodes.size());
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ArrayCreator.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            //System.out.println("Codes Array Already Created: " + mpesacodes.size());
        }
    }

}
