/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.julius.sdpservice;

import javax.jws.WebService;
import org.csapi.wsdl.parlayx.sms.send.v2_2.service.PolicyException;
import org.csapi.wsdl.parlayx.sms.send.v2_2.service.ServiceException;

/**
 *
 * @author coolie
 */
@WebService(serviceName = "SendSmsService", portName = "SendSms", endpointInterface = "org.csapi.wsdl.parlayx.sms.send.v2_2.service.SendSms", targetNamespace = "http://www.csapi.org/wsdl/parlayx/sms/send/v2_2/service", wsdlLocation = "WEB-INF/wsdl/juliussdp/parlayx_sms_send_service_2_2.wsdl")
public class juliussdp {

    public java.lang.String sendSms(java.util.List<java.lang.String> addresses, java.lang.String senderName, org.csapi.schema.parlayx.common.v2_1.ChargingInformation charging, java.lang.String message, org.csapi.schema.parlayx.common.v2_1.SimpleReference receiptRequest) throws PolicyException, ServiceException {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public java.lang.String sendSmsLogo(java.util.List<java.lang.String> addresses, java.lang.String senderName, org.csapi.schema.parlayx.common.v2_1.ChargingInformation charging, byte[] image, org.csapi.schema.parlayx.sms.v2_2.SmsFormat smsFormat, org.csapi.schema.parlayx.common.v2_1.SimpleReference receiptRequest) throws ServiceException, PolicyException {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public java.lang.String sendSmsRingtone(java.util.List<java.lang.String> addresses, java.lang.String senderName, org.csapi.schema.parlayx.common.v2_1.ChargingInformation charging, java.lang.String ringtone, org.csapi.schema.parlayx.sms.v2_2.SmsFormat smsFormat, org.csapi.schema.parlayx.common.v2_1.SimpleReference receiptRequest) throws ServiceException, PolicyException {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public java.util.List<org.csapi.schema.parlayx.sms.v2_2.DeliveryInformation> getSmsDeliveryStatus(java.lang.String requestIdentifier) throws ServiceException, PolicyException {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }
    
}
