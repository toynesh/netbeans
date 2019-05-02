/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vend.manualvends;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author coolie
 */
@WebService(serviceName = "QueryMpesaCode")
public class QueryMpesaCode {

    /**
     * This is a sample web service operation
     *
     * @WebMethod(operationName = "hello") public String hello(@WebParam(name =
     * "name") String txt) { return "Hello " + txt + " !"; }
     */
    List<String> manualcodes = new ArrayList<>();

    @WebMethod(operationName = "querymanual")
    public List<String> querymanual(@WebParam(name = "mcode") String mcode) {
        callAllcodes();
        return manualcodes;
    }

    public void callAllcodes() {
        DataStore data = new DataStore();
        Connection con = data.connect();
        if (manualcodes.size() == 0) {
            try {
                String query = "SELECT cust_mpesa_code FROM manual_vend";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    //System.out.println("Saving Manual: " + rs.getString(1));
                    manualcodes.add(rs.getString(1));
                }
                //System.out.println("ManualCodes Array Size: " + manualcodes.size());
            } catch (SQLException ex) {
                //Logger.getLogger(ArrayCreator.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                String query = "SELECT mpesa_code FROM mpesa";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    //System.out.println("Saving Mpesa: " + rs.getString(1));
                    manualcodes.add(rs.getString(1));
                }

            } catch (SQLException ex) {
                //Logger.getLogger(ArrayCreator.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                String query = "SELECT mpesa_code FROM mpesa201707";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    //System.out.println("Saving OLD Mpesa: " + rs.getString(1));
                    manualcodes.add(rs.getString(1));
                }
                //System.out.println("Codes Array Size: " + oldmpesacodes.size());
            } catch (SQLException ex) {
                //Logger.getLogger(ArrayCreator.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            //System.out.println("OLDCodes Array Already Created: " + oldmpesacodes.size());
        }
        try {
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
