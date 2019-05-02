/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airtelmtcharging;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Base64;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import javax.net.ssl.HttpsURLConnection;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author coolie
 */
public class Airtelmtcharging {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String XMLSRequest = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:char=\"http://ChargingProcess/com/ibm/sdp/services/charging/abstraction/Charging\">\n"
                + "<soapenv:Header/>\n"
                + "<soapenv:Body>\n"
                + "<char:charge>\n"
                + "<inputMsg>\n"
                + "<operation>debit</operation>\n"
                + "<userId>254789021421</userId>\n"
                + "<contentId>test</contentId>\n"
                + "<itemName>test</itemName>\n"
                + "<contentDescription>test</contentDescription>\n"
                + "<circleId/>\n"
                + "<lineOfBusiness/>\n"
                + "<customerSegment/>\n"
                + "<contentMediaType>KNEC20078</contentMediaType>\n"
                + "<serviceId/>\n"
                + "<parentId/>\n"
                + "<actualPrice>20</actualPrice>\n"
                + "<basePrice></basePrice>\n"
                + "<discountApplied>0</discountApplied>\n"
                + "<paymentMethod/>\n"
                + "<revenuePercent/>\n"
                + "<netShare>0</netShare>\n"
                + "<cpId>13201966_PROFESSIONAL_DIGITAL_SYSTEM_LTD</cpId>\n"
                + "<customerClass/>\n"
                + "<eventType>Content Purchase</eventType>\n"
                + "<localTimeStamp>20141217123404</localTimeStamp>\n"
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
                + "<srcCode>50101</srcCode>\n"
                + "<contentUrl>www.airtel.com</contentUrl>\n"
                + "<subscriptiondays>2</subscriptiondays>\n"
                + "</inputMsg>\n"
                + "</char:charge>\n"
                + "</soapenv:Body>\n"
                + "</soapenv:Envelope>";
        try {

// Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

// Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

// Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

// Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

            URL url = new URL("https://41.223.58.133:8443/ChargingServiceFlowWeb/sca/ChargingExport1");
            URLConnection con = url.openConnection();

            //con.setRequestMethod("POST");
            String userCredentials = "3201966_PROFESSIONAL_DIGITAL_SYSTEM_LTD:iH8fc5#NK";
            String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));

            con.setRequestProperty("Authorization", basicAuth);
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.29 Safari/537.36");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            con.setRequestProperty("Content-Type", "application/soap+xml; charset=utf-8");

            con.setDoOutput(true);
            OutputStream out = con.getOutputStream(); //get output Stream from con
            out.write(XMLSRequest.getBytes("utf-8"));
            System.out.println("Sending Request:");
            System.out.println(XMLSRequest);
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            out.close();
            //con.disconnect();
            System.out.println(response.toString());
            /*Reader reader = new InputStreamReader(con.getInputStream());
            while (true) {
                int ch = reader.read();
                if (ch == -1) {
                    break;
                }
                System.out.print((char) ch);
            }*/
        } catch (MalformedURLException ex) {
            Logger.getLogger(Airtelmtcharging.class.getName()).log(Level.SEVERE, "MalformedURLException: " + ex);
        } catch (IOException ex) {
            Logger.getLogger(Airtelmtcharging.class.getName()).log(Level.SEVERE, "IOException: " + ex);
        } catch (KeyManagementException ex) {
            Logger.getLogger(Airtelmtcharging.class.getName()).log(Level.SEVERE, "KeyManagementException: " + ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Airtelmtcharging.class.getName()).log(Level.SEVERE, "NoSuchAlgorithmException: " + ex);
        }
    }

}
