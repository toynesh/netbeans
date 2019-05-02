/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl;

import com.pdsl.datastore.DataStore;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.datatype.XMLGregorianCalendar;
import org.csapi.schema.parlayx.sms.notification.v2_2.local.NotifySmsDeliveryReceiptResponse;
import org.csapi.schema.parlayx.sms.notification.v2_2.local.NotifySmsReceptionResponse;
import org.csapi.schema.parlayx.sms.v2_2.DeliveryInformation;
import org.csapi.schema.parlayx.sms.v2_2.DeliveryStatus;
import org.csapi.schema.parlayx.sms.v2_2.SmsMessage;

/**
 *
 * @author julius
 */
@WebService(serviceName = "SmsNotificationService", portName = "SmsNotification", endpointInterface = "org.csapi.wsdl.parlayx.sms.notification.v2_2.service.SmsNotification", targetNamespace = "http://www.csapi.org/wsdl/parlayx/sms/notification/v2_2/service", wsdlLocation = "WEB-INF/wsdl/sms/196.201.216.14/SDP_SMS/parlayx_sms_notification_service_2_2.wsdl")
@HandlerChain(file = "handler-chain.xml")
public class sms {

    Logger log = Logger.getLogger(sms.class.getName());
    DataStore data = new DataStore();

    public NotifySmsReceptionResponse notifySmsReception(String correlator, SmsMessage message) {
        System.out.println(" Sender address " + message.getSenderAddress());
        System.out.println(" Activation Number  " + message.getSmsServiceActivationNumber());
        System.out.println(" Time Stamp " + message.getDateTime().toXMLFormat());

        NotifySmsReceptionResponse responseE = new NotifySmsReceptionResponse();

        return responseE;
    }

    public NotifySmsDeliveryReceiptResponse notifySmsDeliveryReceipt(String correlator, DeliveryInformation deliveryStatus) {
        NotifySmsDeliveryReceiptResponse responseE = new NotifySmsDeliveryReceiptResponse();

        String status = null;
        String code = null;
        Logger.getLogger(sms.class.getName()).log(Level.INFO, "........++++ " + deliveryStatus.getAddress());
        String[] number = deliveryStatus.getAddress().split(":");
        System.out.println("DeliveYENYEWE :" + deliveryStatus.getDeliveryStatus());
        try {
            status = deliveryStatus.getDeliveryStatus().value();
            System.out.println("Delivery Value :" + deliveryStatus.getDeliveryStatus());
            System.out.println("Delivery Address :" + deliveryStatus.getAddress());
        } catch (NullPointerException npe) {
            status = "Insufficient_Balance";
            System.out.println("Delivery Value :Null Status - " + status);
        }
        if (status.equals("DeliveredToTerminal")) {
            try {
                Connection con = this.data.connect();
                String query = "select verificationcode from subscription where subscription = '" + number[1] + "' order by update_time desc limit 1";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    code = rs.getString(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(sms.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Calendar c = Calendar.getInstance();
        c.setTime(new java.util.Date());
        c.add(5, 365);
        String output = sdf.format(c.getTime());
        System.out.println("Expiry Date: " + output);
        java.util.Date utilDate = null;
        try {
            utilDate = sdf.parse(output);
        } catch (ParseException ex) {
            Logger.getLogger(sms.class.getName()).log(Level.SEVERE, null, ex);
        }
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

        String insert = "";
        System.out.println("Correlator: " + correlator + " Delivery Status: " + status + " Number: " + deliveryStatus.getAddress());

        insert = "insert into deliveryProcess (msisdn,correlator,status,verificationcode,sub_expiry) values (" + number[1] + ",'" + correlator + "','" + status + "','" + code + "','" + sqlDate + "')";

        this.data.insert(insert);

        System.out.println(" Results for Delivery Receipt: " + responseE.toString());

        return responseE;
    }

    public void sendNormalMes(String number, String message, String scode) {
        try {
            System.out.println(message);
            String outmessage = message;
            String[] array = scode.split(":");
            scode = array[1];
            array = number.split(":");
            number = array[1];
            this.log.log(Level.INFO, message);

            message = URLEncoder.encode(message.toString(), "UTF-8");
            try {
                String username = "tester";
                String password = "tester";

                String host = "127.0.0.1";
                String port = "18009";

                String smic = "";
                String destination = scode;
                String from = number;

                String directives = "username=" + username;
                directives = directives + "&password=" + password;
                directives = directives + "&dlr-mask=31";
                directives = directives + "&smsc=" + smic;
                directives = directives + "&to=" + destination;
                directives = directives + "&from=254" + from;

                directives = directives + "&text=" + message;

                URL url = null;
                StringBuilder res = new StringBuilder();
                url = new URL("http://" + host + ":" + port + "/cgi-bin/sendsms?" + directives);

                URLConnection conn = url.openConnection();
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String response;
                while ((response = rd.readLine()) != null) {
                    res.append(response);
                }
                rd.close();
                this.log.log(Level.INFO, res.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(sms.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
