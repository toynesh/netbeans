/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.resendSMS;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 *
 * @author coolie
 */
public class Sdp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Sdp sdp = new Sdp();
        String result = sdp.sendSMS("254728064120", "OK| KPLC system delayed. Will keep trying & resend your transaction.Queries Call:0709711000.", "fromdb", "704307");
        Logger.getLogger(Sdp.class.getName()).log(Level.INFO, "Result: " + result);
    }
    public String sendSMS(String msisdn, String message, String correlator, String shortcode, String dlrurl) {
        String response = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        String ctime = sdf.format(Calendar.getInstance().getTime());
        String spid = null;
        String serviceid = null;
        String spPassword = null;
        try {
            //DB
            String myDriver = "org.gjt.mm.mysql.Driver";
            String myUrl = "jdbc:mysql://localhost/dspDelivery";
            Class.forName(myDriver);
            Connection con = DriverManager.getConnection(myUrl, "pdsluser", "P@Dsl949022");
            String query = "select spid,serviceid,password from scodeManager where accesscode='" + shortcode + "'";
            System.out.println(query);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                spid = rs.getString(1);
                serviceid = rs.getString(2);
                spPassword = rs.getString(3);
            }
            con.close();
            Logger.getLogger(Sdp.class.getName()).log(Level.INFO,"spID:"+spid+" serviceID:"+serviceid+" spPassword:"+spPassword);
            //MD5PASS
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashInBytes = md.digest((spid + spPassword + ctime).getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashInBytes) {
                sb.append(String.format("%02x", b));
            }
            //System.out.println(sb.toString());
            String MD5pass = sb.toString();
            String strURL = "http://svc.safaricom.com:8310/SendSmsService/services/SendSms";
            String xmlString = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\n"
                    + "xmlns:v2=\"http://www.huawei.com.cn/schema/common/v2_1\"\n"
                    + "xmlns:loc=\"http://www.csapi.org/schema/parlayx/sms/send/v2_2/local\">\n"
                    + "<soapenv:Header>\n"
                    + "<v2:RequestSOAPHeader>\n"
                    + "<v2:spId>" + spid + "</v2:spId>\n"
                    + "<v2:spPassword>" + MD5pass + "</v2:spPassword>\n"
                    + "<v2:serviceId>" + serviceid + "</v2:serviceId>\n"
                    + "<v2:timeStamp>" + ctime + "</v2:timeStamp>\n"
                    + "<!--mandatory if service is on-demand-->\n"
                    + "<v2:linkid>07201312390000000006</v2:linkid>\n"
                    + "<v2:OA>tel:254722123456</v2:OA>\n"
                    + "<v2:FA>tel:254722123456</v2:FA>\n"
                    + "</v2:RequestSOAPHeader>\n"
                    + "</soapenv:Header>\n"
                    + "<soapenv:Body>\n"
                    + "<loc:sendSms>\n"
                    + "<loc:addresses>tel:" + msisdn + "</loc:addresses>\n"
                    + "<!--Optional:-->\n"
                    + "<loc:senderName>" + shortcode + "</loc:senderName>\n"
                    + "<loc:message>" + StringEscapeUtils.escapeXml(message) + "</loc:message>\n"
                    + "<!--Optional:-->\n"
                    + "<loc:receiptRequest>\n"
                    + "<endpoint>" + dlrurl + "</endpoint>\n"
                    + "<interfaceName>SmsNotification</interfaceName>\n"
                    + "<correlator>" + correlator + "</correlator>\n"
                    + "</loc:receiptRequest>\n"
                    + "</loc:sendSms>\n"
                    + "</soapenv:Body>\n"
                    + "</soapenv:Envelope>";
            // Prepare HTTP post
            PostMethod post = new PostMethod(strURL);
            RequestEntity entity = new StringRequestEntity(xmlString, "text/xml", "UTF-8");
            post.setRequestEntity(entity);
            // Get HTTP client
            HttpClient httpclient = new HttpClient();
            // Execute request
            try {
                int result = httpclient.executeMethod(post);
                // Display status code
                Logger.getLogger(Sdp.class.getName()).log(Level.INFO, "Sms Response status code: " + result);
                // Display response
                //Logger.getLogger(Sdp.class.getName()).log(Level.INFO, "SMS Response body: " + post.getResponseBodyAsString());
                response = xmlGetResult(post.getResponseBodyAsString());
            } catch (IOException ex) {
                Logger.getLogger(Sdp.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                // Release current connection to the connection pool once you are done
                post.releaseConnection();
            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Sdp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Sdp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Sdp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Sdp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }
    public String sendSMS(String msisdn, String message, String correlator, String shortcode) {
        String response = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        String ctime = sdf.format(Calendar.getInstance().getTime());
        String spid = null;
        String serviceid = null;
        String spPassword = null;
        try {
            //DB
            String myDriver = "org.gjt.mm.mysql.Driver";
            String myUrl = "jdbc:mysql://localhost/dspDelivery";
            Class.forName(myDriver);
            Connection con = DriverManager.getConnection(myUrl, "pdsluser", "P@Dsl949022");
            String query = "select spid,serviceid,password from scodeManager where accesscode='" + shortcode + "'";
            System.out.println(query);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                spid = rs.getString(1);
                serviceid = rs.getString(2);
                spPassword = rs.getString(3);
            }
            con.close();
            System.out.println("spID:"+spid+" serviceID:"+serviceid+" spPassword:"+spPassword);
            //MD5PASS
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashInBytes = md.digest((spid + spPassword + ctime).getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashInBytes) {
                sb.append(String.format("%02x", b));
            }
            //System.out.println(sb.toString());
            String MD5pass = sb.toString();
            String strURL = "http://svc.safaricom.com:8310/SendSmsService/services/SendSms";
            String xmlString = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\n"
                    + "xmlns:v2=\"http://www.huawei.com.cn/schema/common/v2_1\"\n"
                    + "xmlns:loc=\"http://www.csapi.org/schema/parlayx/sms/send/v2_2/local\">\n"
                    + "<soapenv:Header>\n"
                    + "<v2:RequestSOAPHeader>\n"
                    + "<v2:spId>" + spid + "</v2:spId>\n"
                    + "<v2:spPassword>" + MD5pass + "</v2:spPassword>\n"
                    + "<v2:serviceId>" + serviceid + "</v2:serviceId>\n"
                    + "<v2:timeStamp>" + ctime + "</v2:timeStamp>\n"
                    + "<!--mandatory if service is on-demand-->\n"
                    + "<v2:linkid>07201312390000000006</v2:linkid>\n"
                    + "<v2:OA>tel:254722123456</v2:OA>\n"
                    + "<v2:FA>tel:254722123456</v2:FA>\n"
                    + "</v2:RequestSOAPHeader>\n"
                    + "</soapenv:Header>\n"
                    + "<soapenv:Body>\n"
                    + "<loc:sendSms>\n"
                    + "<loc:addresses>tel:" + msisdn + "</loc:addresses>\n"
                    + "<!--Optional:-->\n"
                    + "<loc:senderName>" + shortcode + "</loc:senderName>\n"
                    + "<loc:message>" + StringEscapeUtils.escapeXml(message) + "</loc:message>\n"
                    + "<!--Optional:-->\n"
                    + "<loc:receiptRequest>\n"
                    + "<endpoint>http://10.138.30.123:9080/notify</endpoint>\n"
                    + "<interfaceName>SmsNotification</interfaceName>\n"
                    + "<correlator>" + correlator + "</correlator>\n"
                    + "</loc:receiptRequest>\n"
                    + "</loc:sendSms>\n"
                    + "</soapenv:Body>\n"
                    + "</soapenv:Envelope>";
            // Prepare HTTP post
            PostMethod post = new PostMethod(strURL);
            RequestEntity entity = new StringRequestEntity(xmlString, "text/xml", "UTF-8");
            post.setRequestEntity(entity);
            // Get HTTP client
            HttpClient httpclient = new HttpClient();
            // Execute request
            try {
                int result = httpclient.executeMethod(post);
                // Display status code
                Logger.getLogger(Sdp.class.getName()).log(Level.INFO, "Sms Response status code: " + result);
                // Display response
                //Logger.getLogger(Sdp.class.getName()).log(Level.INFO, "SMS Response body: " + post.getResponseBodyAsString());
                response = xmlGetResult(post.getResponseBodyAsString());
            } catch (IOException ex) {
                Logger.getLogger(Sdp.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                // Release current connection to the connection pool once you are done
                post.releaseConnection();
            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Sdp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Sdp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Sdp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Sdp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }

    public String xmlGetResult(String strMsg) {
        String resp = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(strMsg)));
            NodeList el = document.getElementsByTagName("ns1:result");
            resp = el.item(0).getTextContent();

        } catch (Exception e) {
            // e.printStackTrace();
        }
        return resp;
    }
}
