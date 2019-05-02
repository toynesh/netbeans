
package org.csapi.wsdl.parlayx.sms.send.v2_2.service;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import org.csapi.schema.parlayx.common.v2_1.ChargingInformation;
import org.csapi.schema.parlayx.common.v2_1.SimpleReference;
import org.csapi.schema.parlayx.sms.v2_2.DeliveryInformation;
import org.csapi.schema.parlayx.sms.v2_2.SmsFormat;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.6-1b01 
 * Generated source version: 2.2
 * 
 */
@WebService(name = "SendSms", targetNamespace = "http://www.csapi.org/wsdl/parlayx/sms/send/v2_2/interface")
@XmlSeeAlso({
    org.csapi.schema.parlayx.common.v2_1.ObjectFactory.class,
    org.csapi.schema.parlayx.sms.send.v2_2.local.ObjectFactory.class,
    org.csapi.schema.parlayx.sms.v2_2.ObjectFactory.class
})
public interface SendSms {


    /**
     * 
     * @param addresses
     * @param senderName
     * @param receiptRequest
     * @param charging
     * @param message
     * @return
     *     returns java.lang.String
     * @throws PolicyException
     * @throws ServiceException
     */
    @WebMethod
    @WebResult(name = "result", targetNamespace = "http://www.csapi.org/schema/parlayx/sms/send/v2_2/local")
    @RequestWrapper(localName = "sendSms", targetNamespace = "http://www.csapi.org/schema/parlayx/sms/send/v2_2/local", className = "org.csapi.schema.parlayx.sms.send.v2_2.local.SendSms")
    @ResponseWrapper(localName = "sendSmsResponse", targetNamespace = "http://www.csapi.org/schema/parlayx/sms/send/v2_2/local", className = "org.csapi.schema.parlayx.sms.send.v2_2.local.SendSmsResponse")
    public String sendSms(
        @WebParam(name = "addresses", targetNamespace = "http://www.csapi.org/schema/parlayx/sms/send/v2_2/local")
        List<String> addresses,
        @WebParam(name = "senderName", targetNamespace = "http://www.csapi.org/schema/parlayx/sms/send/v2_2/local")
        String senderName,
        @WebParam(name = "charging", targetNamespace = "http://www.csapi.org/schema/parlayx/sms/send/v2_2/local")
        ChargingInformation charging,
        @WebParam(name = "message", targetNamespace = "http://www.csapi.org/schema/parlayx/sms/send/v2_2/local")
        String message,
        @WebParam(name = "receiptRequest", targetNamespace = "http://www.csapi.org/schema/parlayx/sms/send/v2_2/local")
        SimpleReference receiptRequest)
        throws PolicyException, ServiceException
    ;

    /**
     * 
     * @param image
     * @param addresses
     * @param senderName
     * @param receiptRequest
     * @param charging
     * @param smsFormat
     * @return
     *     returns java.lang.String
     * @throws PolicyException
     * @throws ServiceException
     */
    @WebMethod
    @WebResult(name = "result", targetNamespace = "http://www.csapi.org/schema/parlayx/sms/send/v2_2/local")
    @RequestWrapper(localName = "sendSmsLogo", targetNamespace = "http://www.csapi.org/schema/parlayx/sms/send/v2_2/local", className = "org.csapi.schema.parlayx.sms.send.v2_2.local.SendSmsLogo")
    @ResponseWrapper(localName = "sendSmsLogoResponse", targetNamespace = "http://www.csapi.org/schema/parlayx/sms/send/v2_2/local", className = "org.csapi.schema.parlayx.sms.send.v2_2.local.SendSmsLogoResponse")
    public String sendSmsLogo(
        @WebParam(name = "addresses", targetNamespace = "http://www.csapi.org/schema/parlayx/sms/send/v2_2/local")
        List<String> addresses,
        @WebParam(name = "senderName", targetNamespace = "http://www.csapi.org/schema/parlayx/sms/send/v2_2/local")
        String senderName,
        @WebParam(name = "charging", targetNamespace = "http://www.csapi.org/schema/parlayx/sms/send/v2_2/local")
        ChargingInformation charging,
        @WebParam(name = "image", targetNamespace = "http://www.csapi.org/schema/parlayx/sms/send/v2_2/local")
        byte[] image,
        @WebParam(name = "smsFormat", targetNamespace = "http://www.csapi.org/schema/parlayx/sms/send/v2_2/local")
        SmsFormat smsFormat,
        @WebParam(name = "receiptRequest", targetNamespace = "http://www.csapi.org/schema/parlayx/sms/send/v2_2/local")
        SimpleReference receiptRequest)
        throws PolicyException, ServiceException
    ;

    /**
     * 
     * @param addresses
     * @param senderName
     * @param receiptRequest
     * @param charging
     * @param ringtone
     * @param smsFormat
     * @return
     *     returns java.lang.String
     * @throws PolicyException
     * @throws ServiceException
     */
    @WebMethod
    @WebResult(name = "result", targetNamespace = "http://www.csapi.org/schema/parlayx/sms/send/v2_2/local")
    @RequestWrapper(localName = "sendSmsRingtone", targetNamespace = "http://www.csapi.org/schema/parlayx/sms/send/v2_2/local", className = "org.csapi.schema.parlayx.sms.send.v2_2.local.SendSmsRingtone")
    @ResponseWrapper(localName = "sendSmsRingtoneResponse", targetNamespace = "http://www.csapi.org/schema/parlayx/sms/send/v2_2/local", className = "org.csapi.schema.parlayx.sms.send.v2_2.local.SendSmsRingtoneResponse")
    public String sendSmsRingtone(
        @WebParam(name = "addresses", targetNamespace = "http://www.csapi.org/schema/parlayx/sms/send/v2_2/local")
        List<String> addresses,
        @WebParam(name = "senderName", targetNamespace = "http://www.csapi.org/schema/parlayx/sms/send/v2_2/local")
        String senderName,
        @WebParam(name = "charging", targetNamespace = "http://www.csapi.org/schema/parlayx/sms/send/v2_2/local")
        ChargingInformation charging,
        @WebParam(name = "ringtone", targetNamespace = "http://www.csapi.org/schema/parlayx/sms/send/v2_2/local")
        String ringtone,
        @WebParam(name = "smsFormat", targetNamespace = "http://www.csapi.org/schema/parlayx/sms/send/v2_2/local")
        SmsFormat smsFormat,
        @WebParam(name = "receiptRequest", targetNamespace = "http://www.csapi.org/schema/parlayx/sms/send/v2_2/local")
        SimpleReference receiptRequest)
        throws PolicyException, ServiceException
    ;

    /**
     * 
     * @param requestIdentifier
     * @return
     *     returns java.util.List<org.csapi.schema.parlayx.sms.v2_2.DeliveryInformation>
     * @throws PolicyException
     * @throws ServiceException
     */
    @WebMethod
    @WebResult(name = "result", targetNamespace = "http://www.csapi.org/schema/parlayx/sms/send/v2_2/local")
    @RequestWrapper(localName = "getSmsDeliveryStatus", targetNamespace = "http://www.csapi.org/schema/parlayx/sms/send/v2_2/local", className = "org.csapi.schema.parlayx.sms.send.v2_2.local.GetSmsDeliveryStatus")
    @ResponseWrapper(localName = "getSmsDeliveryStatusResponse", targetNamespace = "http://www.csapi.org/schema/parlayx/sms/send/v2_2/local", className = "org.csapi.schema.parlayx.sms.send.v2_2.local.GetSmsDeliveryStatusResponse")
    public List<DeliveryInformation> getSmsDeliveryStatus(
        @WebParam(name = "requestIdentifier", targetNamespace = "http://www.csapi.org/schema/parlayx/sms/send/v2_2/local")
        String requestIdentifier)
        throws PolicyException, ServiceException
    ;

}
