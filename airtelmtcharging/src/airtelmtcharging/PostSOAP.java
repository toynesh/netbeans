/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airtelmtcharging;
import java.io.File;
import java.util.Base64;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.FileRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
/**
 *
 * @author coolie
 */
public class PostSOAP {
     public static void main(String[] args) throws Exception {

        // Get target URL
        String strURL = "https://41.223.58.133:8443/ChargingServiceFlowWeb/sca/ChargingExport1";
        // Get SOAP action
        String strSoapAction = "http://ChargingProcess/com/ibm/sdp/services/charging/abstraction/Charging";
        String userCredentials = "3201966_PROFESSIONAL_DIGITAL_SYSTEM_LTD:iH8fc5#NK";
        String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));

        
        // Get file to be posted
        String strXMLFilename = "/home/coolie/Desktop/Requestxml.txt";
        File input = new File(strXMLFilename);
        // Prepare HTTP post
        PostMethod post = new PostMethod(strURL);
        // Request content will be retrieved directly
        // from the input stream
        RequestEntity entity = new FileRequestEntity(input, "text/xml; charset=ISO-8859-1");
       
        post.setRequestEntity(entity);
        
        post.setRequestHeader("Authorization", basicAuth);
      
        // consult documentation for your web service
        post.setRequestHeader("SOAPAction", strSoapAction);
        // Get HTTP client
        HttpClient httpclient = new HttpClient();
        
        // Execute request
        try {
            int result = httpclient.executeMethod(post);
            // Display status code
            System.out.println("Response status code: " + result);
            // Display response
            System.out.println("Response body: ");
            System.out.println(post.getResponseBodyAsString());
        } finally {
            // Release current connection to the connection pool once you are done
            post.releaseConnection();
        }
    }
}
