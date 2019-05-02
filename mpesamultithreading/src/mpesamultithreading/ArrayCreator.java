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
public class ArrayCreator {
    private static ArrayCreator arraycreaterInstance = null;
    List<String> mpesacodes = new ArrayList<>();
    DataStore data = new DataStore();

    private ArrayCreator() {
        // Exists only to defeat instantiation.
    }

    public static ArrayCreator getCArrayCreatorInstance() {
        if (arraycreaterInstance == null) {
            arraycreaterInstance = new ArrayCreator();
        }
        return arraycreaterInstance;
    }

    public void callArray() {
        if (mpesacodes.size() == 0) {
            try {
                Connection con = data.connect();
                String query = "SELECT mpesa_code FROM mpesa";
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
        }else{
            System.out.println("Codes Array Already Created" + mpesacodes.size());
        }
    }
}
