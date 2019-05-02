/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl;

import com.pdsl.datastore.DataStore;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import org.csapi.schema.parlayx.sms.notification.v2_2.local.NotifySmsDeliveryReceiptResponse;
import org.csapi.schema.parlayx.sms.notification.v2_2.local.NotifySmsReceptionResponse;

/**
 *
 * @author juliuskun
 */
@WebService(serviceName = "SmsNotificationService", portName = "SmsNotification", endpointInterface = "org.csapi.wsdl.parlayx.sms.notification.v2_2.service.SmsNotification", targetNamespace = "http://www.csapi.org/wsdl/parlayx/sms/notification/v2_2/service", wsdlLocation = "WEB-INF/wsdl/sms/parlayx_sms_notification_service_2_2.wsdl")
@HandlerChain(file = "handler-chain.xml")
public class sms {

    Logger log = Logger.getLogger(sms.class.getName());
    DataStore data = new DataStore();

    public NotifySmsReceptionResponse notifySmsReception(java.lang.String correlator, org.csapi.schema.parlayx.sms.v2_2.SmsMessage message) {
        System.out.println(" Sender address " + message.getSenderAddress());
        System.out.println(" Activation Number  " + message.getSmsServiceActivationNumber());
        System.out.println(" Time Stamp " + message.getDateTime().toXMLFormat());
        NotifySmsReceptionResponse responseE = new NotifySmsReceptionResponse();
        return responseE;
    }

    public NotifySmsDeliveryReceiptResponse notifySmsDeliveryReceipt(java.lang.String correlator, org.csapi.schema.parlayx.sms.v2_2.DeliveryInformation deliveryStatus) {
        NotifySmsDeliveryReceiptResponse responseE = new NotifySmsDeliveryReceiptResponse();
        String status = "NotDeliveredToTerminal";
        String realstatus = "EMPTY";
        String number[] = deliveryStatus.getAddress().split(":");
        try {
            status = deliveryStatus.getDeliveryStatus().value();
            realstatus = deliveryStatus.getDeliveryStatus().toString();
            Logger.getLogger(sms.class.getName()).log(Level.INFO, "VendIT Delivery Value :" + deliveryStatus.getDeliveryStatus());
            Logger.getLogger(sms.class.getName()).log(Level.INFO, "VendIT Delivery Address :" + deliveryStatus.getAddress());

        } catch (NullPointerException npe) {
             Logger.getLogger(sms.class.getName()).log(Level.SEVERE, "Empty Delivery Response!");
            //npe.printStackTrace();
        }
        String msisdn = deliveryStatus.getAddress().replaceAll("tel:", "");
        String insert = "insert into dlr_status (msisdn,correlator,status,realstatus) values ('" +msisdn+ "','" + correlator + "','" + status + "','" + realstatus + "')";
        data.vinsert(insert);

        return responseE;

    }
}
