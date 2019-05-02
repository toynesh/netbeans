/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl;

import com.pdsl.datastore.DataStore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import metro_sdp.Metro_sdp;
import org.csapi.schema.parlayx.sms.notification.v2_2.local.NotifySmsDeliveryReceiptResponse;
import org.csapi.schema.parlayx.sms.notification.v2_2.local.NotifySmsReceptionResponse;

/**
 *
 * @author coolie
 */
@WebService(serviceName = "SmsNotificationService", portName = "SmsNotification", endpointInterface = "org.csapi.wsdl.parlayx.sms.notification.v2_2.service.SmsNotification", targetNamespace = "http://www.csapi.org/wsdl/parlayx/sms/notification/v2_2/service", wsdlLocation = "WEB-INF/wsdl/sms/parlayx_sms_notification_service_2_2.wsdl")
@HandlerChain(file = "handler-chain.xml")
public class sms {

    Logger log = Logger.getLogger(sms.class.getName());
    DataStore data = new DataStore();

    public NotifySmsReceptionResponse notifySmsReception(java.lang.String correlator, org.csapi.schema.parlayx.sms.v2_2.SmsMessage message) {
        //System.out.println(" Sender address " + message.getSenderAddress());
        //System.out.println(" Activation Number  " + message.getSmsServiceActivationNumber());
        //System.out.println(" Time Stamp " + message.getDateTime().toXMLFormat());
        NotifySmsReceptionResponse responseE = new NotifySmsReceptionResponse();
        return responseE;
    }

    public NotifySmsDeliveryReceiptResponse notifySmsDeliveryReceipt(java.lang.String correlator, org.csapi.schema.parlayx.sms.v2_2.DeliveryInformation deliveryStatus) {
        NotifySmsDeliveryReceiptResponse responseE = new NotifySmsDeliveryReceiptResponse();
        String status = null;
        String number[] = deliveryStatus.getAddress().split(":");
        try {
            status = deliveryStatus.getDeliveryStatus().value();
            Logger.getLogger(sms.class.getName()).log(Level.INFO, "KPLCBillAlerts Delivery Value :" + deliveryStatus.getDeliveryStatus());
            Logger.getLogger(sms.class.getName()).log(Level.INFO, "KPLCBillAlerts Delivery Address :" + deliveryStatus.getAddress());

        } catch (NullPointerException npe) {
            status = "NotDeliveredToTerminal";
        }
        String msisdn = deliveryStatus.getAddress().replaceAll("tel:", "");
        String insert = "insert into dlr_status (msisdn,correlator,status) values ('" +msisdn+ "','" + correlator + "','" + status + "')";
        data.insert(insert);

        return responseE;

    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        Metro_sdp sdp = new Metro_sdp();
        sdp.sendSdpSms("254728064120", "Hi Julius 2", "kplcdlr", "706805", "http://197.248.9.106:8081/kpcldlrnotification/SmsNotificationService");
    }
    
}
