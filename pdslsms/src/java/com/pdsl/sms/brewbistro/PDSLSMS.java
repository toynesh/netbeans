/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.sms.brewbistro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import metro_sdp.Metro_sdp;

/**
 *
 * @author coolie
 */
@WebService(serviceName = "PDSLSMS")
public class PDSLSMS {

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "sendSMS")
    public String sendSMS(@WebParam(name = "ServiceID") String serviceid, @WebParam(name = "msisdn") String msisdn, @WebParam(name = "CustomerName") String customer, @WebParam(name = "message") String message, @WebParam(name = "correlator") String correlator) {
        Metro_sdp sdp = new Metro_sdp();
        DataStore data = new DataStore();
        String response = "Error";
        if (serviceid == null || serviceid.isEmpty() || serviceid.equals("?")) {
            response = "EmptyServiceID";
        } else if (msisdn == null || msisdn.isEmpty() || msisdn.equals("?")) {
            response = "EmptyMsisdn";
        } else if (customer == null || customer.isEmpty() || customer.equals("?")) {
            response = "EmptyCustomerName";
        } else if (message == null || message.isEmpty() || message.equals("?")) {
            response = "EmptyMessage";
        } else if (correlator == null || correlator.isEmpty() || correlator.equals("?")) {
            response = " EmptyCorrelator";
        } else if (!data.checkCorr(correlator).equals("none")) {
            response = "DuplicateCorrelator";
        } else if (!serviceid.equals("6014702000147264")) {
            response = "InvalidServiceID";
        } else {
            try {
                List<String> safprefix = new ArrayList<>();
                Connection con = data.prefixconnect();
                String query2 = "SELECT DISTINCT prefix FROM safaricom";
                Statement st2 = con.createStatement();
                ResultSet rs2 = st2.executeQuery(query2);
                while (rs2.next()) {
                    //System.out.println("Saving All: " + rs2.getString(1));
                    safprefix.add(rs2.getString(1));
                }
                System.out.println("Safprefix Array Size: " + safprefix.size());
                con.close();

                msisdn = msisdn.replace("+", "");
                String msisdnprefix = msisdn.substring(0, 6);
                Logger.getLogger(PDSLSMS.class.getName()).log(Level.INFO, "Prefix is: " + msisdnprefix.substring(3));
                if (safprefix.contains(msisdnprefix.substring(3))) {
                    Connection conn = data.connect();
                    PreparedStatement prep = null;
                    String values = "insert into transactions(msisdn,customer,message,serviceid,correlator,prsp) values (?,?,?,?,?,?)";
                    prep = conn.prepareStatement(values);
                    prep.setString(1, msisdn);
                    prep.setString(2, customer);
                    prep.setString(3, message);
                    prep.setString(4, serviceid);
                    prep.setString(5, correlator);
                    prep.setString(6, "safaricom");
                    prep.execute();
                    prep.close();
                    conn.close();
                    sdp.sendSdpSms(msisdn, message, correlator, "706814", "http://197.248.9.106:8081/bbnotification/SmsNotificationService");
                } else {
                    Connection conn = data.connect();
                    PreparedStatement prep = null;
                    String values = "insert into transactions(msisdn,customer,message,serviceid,correlator,prsp) values (?,?,?,?,?,?)";
                    prep = conn.prepareStatement(values);
                    prep.setString(1, msisdn);
                    prep.setString(2, customer);
                    prep.setString(3, message);
                    prep.setString(4, serviceid);
                    prep.setString(5, correlator);
                    prep.setString(6, "others");
                    prep.execute();
                    prep.close();
                    conn.close();
                    EquitelSMS esdp = new EquitelSMS("172.27.116.22", "EQUITELRX", "pdsl219", "pdsl219", msisdn, message, "BREWBISTRO");
                    esdp.submitMessage();

                    String insert = "insert into dlr_status (msisdn,correlator,status) values ('" + msisdn + "','" + correlator + "','DeliveredToTerminal')";
                    data.bbinsert(insert);
                }

                Logger.getLogger(PDSLSMS.class.getName()).log(Level.INFO, "BREWBISTRO SMS SENT!!");
                safprefix.clear();
                response = "Success";
            } catch (SQLException ex) {
                Logger.getLogger(PDSLSMS.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Logger.getLogger(PDSLSMS.class.getName()).log(Level.INFO, "BREWBISTRO SEND SMS RESPONSE!!" + response);
        return response;
    }

    @WebMethod(operationName = "checkSMSDelivery")
    public String checkSMSDelivery(@WebParam(name = "ServiceID") String serviceid, @WebParam(name = "correlator") String correlator) {
        DataStore data = new DataStore();
        String response = "SMSNotDeliveredToTerminal";
        if (!serviceid.equals("6014702000147264")) {
            response = "InvalidServiceID";
        } else if (serviceid == null || serviceid.isEmpty() || serviceid.equals("?")) {
            response = "EmptyServiceID";
        } else if (correlator == null || correlator.isEmpty() || correlator.equals("?")) {
            response = " EmptyCorrelator";
        } else {
            if (data.getDlrStatus(correlator).equals("DeliveredToTerminal")) {
                response = "SMSDeliveredToTerminal";
            } else if (data.getDlrStatus(correlator).equals("notfound")) {
                response = "InvalidCorrelator";
            } else {
                response = "SMSNotDeliveredToTerminal";
            }
        }
        return response;
    }
}
