/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knecairtel;

import java.io.IOException;
import java.io.StringReader;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 *
 * /**
 *
 * @author coolie
 */
public class Charging {

    public String ChargeGW(String msisdn) {
        String response = "none";
        String shortCode ="20076";//
        DateTime idt = new DateTime();
        DateTimeFormatter ifmt = DateTimeFormat.forPattern("yyyyMMddHHmmss");
        String ctime = ifmt.print(idt);
        String XMLRequest = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:char=\"http://ChargingProcess/com/ibm/sdp/services/charging/abstraction/Charging\">\n"
                + "<soapenv:Header/>\n"
                + "<soapenv:Body>\n"
                + "<char:charge>\n"
                + "<inputMsg>\n"
                + "<operation>debit</operation>\n"
                + "<userId>" + msisdn + "</userId>\n"
                + "<contentId>KNECExamresults</contentId>\n"
                + "<itemName>test</itemName>\n"
                + "<contentDescription>test</contentDescription>\n"
                + "<circleId/>\n"
                + "<lineOfBusiness/>\n"
                + "<customerSegment/>\n"
                + "<contentMediaType>KNEC20076</contentMediaType>\n"
                + "<serviceId/>\n"
                + "<parentId/>\n"
                + "<actualPrice>25</actualPrice>\n"
                + "<basePrice></basePrice>\n"
                + "<discountApplied>0</discountApplied>\n"
                + "<paymentMethod/>\n"
                + "<revenuePercent/>\n"
                + "<netShare>0</netShare>\n"
                + "<cpId>13201966_PROFESSIONAL_DIGITAL_SYSTEM_LTD</cpId>\n"
                + "<customerClass/>\n"
                + "<eventType>Content Purchase</eventType>\n"
                + "<localTimeStamp>" + ctime + "</localTimeStamp>\n"
                + "<transactionId>1418808838563USSD</transactionId>\n"
                + "<subscriptionTypeCode>abcd</subscriptionTypeCode>\n"
                + "<subscriptionName>0</subscriptionName>\n"
                + "<parentType/>\n"
                + "<deliveryChannel>SMS</deliveryChannel>\n"
                + "<subscriptionExternalId>987654321</subscriptionExternalId>\n"
                + "<contentSize/>\n"
                + "<currency>Kshs</currency>\n"
                + "<copyrightId>mauj</copyrightId>\n"
                + "<cpTransactionId>1418808838563USSD</cpTransactionId>\n"
                + "<copyrightDescription>copyright</copyrightDescription>\n"
                + "<sMSkeyword>sms</sMSkeyword>\n"
                + "<srcCode>"+shortCode+"</srcCode>\n"
                + "<contentUrl>www.airtel.com</contentUrl>\n"
                + "<subscriptiondays>2</subscriptiondays>\n"
                + "</inputMsg>\n"
                + "</char:charge>\n"
                + "</soapenv:Body>\n"
                + "</soapenv:Envelope>";
        
        
        // Get target URL
        String strURL = "https://41.223.58.133:8443/ChargingServiceFlowWeb/sca/ChargingExport1";
        //String strURL = "https://41.223.58.139:8443/ChargingWeb/services/ChargingExport1_ChargingHttpPort";
        // Get SOAP action
        String strSoapAction = "http://ChargingProcess/com/ibm/sdp/services/charging/abstraction/Charging";
        String userCredentials = "3201966_PROFESSIONAL_DIGITAL_SYSTEM_LTD:iH8fc5#NK";
        //String userCredentials = "13201966_PROFESSIONAL_DIGITAL_SYSTEM_LTD:1ali3Kvx";
        String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));

        // Prepare HTTP post
        PostMethod post = new PostMethod(strURL);
        // Request content will be retrieved directly
        // from the input stream

        try {
            RequestEntity entity = new StringRequestEntity(XMLRequest, "text/xml", "UTF-8");

            post.setRequestEntity(entity);

            post.setRequestHeader("Authorization", basicAuth);

            // consult documentation for your web service
            post.setRequestHeader("SOAPAction", strSoapAction);
            // Get HTTP client
            HttpClient httpclient = new HttpClient();

            // Execute request            
            System.out.println("Sending Request: ");
            Logger.getLogger(Charging.class.getName()).log(Level.INFO, XMLRequest);
            int result = 0;
            try {
                result = httpclient.executeMethod(post);
            } catch (IOException ex) {
                Logger.getLogger(Charging.class.getName()).log(Level.SEVERE, null, ex);
            }
            // Display status code
            System.out.println(" ");
            System.out.println("============================================");
            System.out.println(" ");
            System.out.println("Response status code: " + result);
            // Display response
            //System.out.println("Response body: ");
            Logger.getLogger(Charging.class.getName()).log(Level.INFO, post.getResponseBodyAsString());
            response = post.getResponseBodyAsString();
        } catch (IOException ex) {
            Logger.getLogger(Charging.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // Release current connection to the connection pool once you are done
            post.releaseConnection();
        }

        return response;
    }
    public String ChargeGW78(String msisdn) {
        String response = "none";
        DateTime idt = new DateTime();
        DateTimeFormatter ifmt = DateTimeFormat.forPattern("yyyyMMddHHmmss");
        String ctime = ifmt.print(idt);
        String XMLRequest = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:char=\"http://ChargingProcess/com/ibm/sdp/services/charging/abstraction/Charging\">\n"
                + "<soapenv:Header/>\n"
                + "<soapenv:Body>\n"
                + "<char:charge>\n"
                + "<inputMsg>\n"
                + "<operation>debit</operation>\n"
                + "<userId>" + msisdn + "</userId>\n"
                + "<contentId>KNECExamresults</contentId>\n"
                + "<itemName>test</itemName>\n"
                + "<contentDescription>test</contentDescription>\n"
                + "<circleId/>\n"
                + "<lineOfBusiness/>\n"
                + "<customerSegment/>\n"
                + "<contentMediaType>KNEC20078</contentMediaType>\n"
                + "<serviceId/>\n"
                + "<parentId/>\n"
                + "<actualPrice>5</actualPrice>\n"
                + "<basePrice></basePrice>\n"
                + "<discountApplied>0</discountApplied>\n"
                + "<paymentMethod/>\n"
                + "<revenuePercent/>\n"
                + "<netShare>0</netShare>\n"
                + "<cpId>13201966_PROFESSIONAL_DIGITAL_SYSTEM_LTD</cpId>\n"
                + "<customerClass/>\n"
                + "<eventType>Content Purchase</eventType>\n"
                + "<localTimeStamp>" + ctime + "</localTimeStamp>\n"
                + "<transactionId>1418808838563USSD</transactionId>\n"
                + "<subscriptionTypeCode>abcd</subscriptionTypeCode>\n"
                + "<subscriptionName>0</subscriptionName>\n"
                + "<parentType/>\n"
                + "<deliveryChannel>SMS</deliveryChannel>\n"
                + "<subscriptionExternalId>987654321</subscriptionExternalId>\n"
                + "<contentSize/>\n"
                + "<currency>Kshs</currency>\n"
                + "<copyrightId>mauj</copyrightId>\n"
                + "<cpTransactionId>1418808838563USSD</cpTransactionId>\n"
                + "<copyrightDescription>copyright</copyrightDescription>\n"
                + "<sMSkeyword>sms</sMSkeyword>\n"
                + "<srcCode>"+20078+"</srcCode>\n"
                + "<contentUrl>www.airtel.com</contentUrl>\n"
                + "<subscriptiondays>2</subscriptiondays>\n"
                + "</inputMsg>\n"
                + "</char:charge>\n"
                + "</soapenv:Body>\n"
                + "</soapenv:Envelope>";
        
        
        // Get target URL
        String strURL = "https://41.223.58.133:8443/ChargingServiceFlowWeb/sca/ChargingExport1";
        //String strURL = "https://41.223.58.139:8443/ChargingWeb/services/ChargingExport1_ChargingHttpPort";
        // Get SOAP action
        String strSoapAction = "http://ChargingProcess/com/ibm/sdp/services/charging/abstraction/Charging";
        String userCredentials = "3201966_PROFESSIONAL_DIGITAL_SYSTEM_LTD:iH8fc5#NK";
        //String userCredentials = "13201966_PROFESSIONAL_DIGITAL_SYSTEM_LTD:1ali3Kvx";
        String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));

        // Prepare HTTP post
        PostMethod post = new PostMethod(strURL);
        // Request content will be retrieved directly
        // from the input stream

        try {
            RequestEntity entity = new StringRequestEntity(XMLRequest, "text/xml", "UTF-8");

            post.setRequestEntity(entity);

            post.setRequestHeader("Authorization", basicAuth);

            // consult documentation for your web service
            post.setRequestHeader("SOAPAction", strSoapAction);
            // Get HTTP client
            HttpClient httpclient = new HttpClient();

            // Execute request            
            System.out.println("Sending Request: ");
            Logger.getLogger(Charging.class.getName()).log(Level.INFO, XMLRequest);
            int result = 0;
            try {
                result = httpclient.executeMethod(post);
            } catch (IOException ex) {
                Logger.getLogger(Charging.class.getName()).log(Level.SEVERE, null, ex);
            }
            // Display status code
            System.out.println(" ");
            System.out.println("============================================");
            System.out.println(" ");
            System.out.println("Response status code: " + result);
            // Display response
            //System.out.println("Response body: ");
            Logger.getLogger(Charging.class.getName()).log(Level.INFO, post.getResponseBodyAsString());
            response = post.getResponseBodyAsString();
        } catch (IOException ex) {
            Logger.getLogger(Charging.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // Release current connection to the connection pool once you are done
            post.releaseConnection();
        }

        return response;
    }

    public String xmlGetCElement(String strMsg, String parameter) {
        String resp = "none";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(strMsg)));
            NodeList el = document.getElementsByTagName(parameter);
            resp = el.item(0).getTextContent();

        } catch (Exception e) {
            // e.printStackTrace();
        }
        return resp;
    }

    public static void main(String[] args) throws Exception {
        Charging ch = new Charging();
        String chres = ch.ChargeGW78("254789021421");
        String resstatus = ch.xmlGetCElement(chres, "status");
        System.out.println("Status: " + resstatus);
        String reserro = ch.xmlGetCElement(chres, "errorMessage");
        System.out.println("Charging Error: " + reserro);
        //System.out.println(data.getMessage("4444"));
    }
}
