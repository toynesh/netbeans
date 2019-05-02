/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvirtual;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author juliuskun
 */
public class Enquire {
    
    public String sendReq()throws IOException  {
    String request = "http://172.27.16.21:8084/listen/Back";
    URL url = new URL(request);
    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
    connection.setDoOutput(true);
    connection.setDoInput(true);
    connection.setInstanceFollowRedirects(false);
    connection.setRequestMethod("POST");
    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    connection.setRequestProperty("charset", "utf-8");
    
    connection.setUseCaches(false);
    
    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
    
    out.flush();
    
    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    
    StringBuffer response = new StringBuffer();
    String inputLine;
    while ((inputLine = in.readLine()) != null) {
      response.append(inputLine);
    }
    in.close();
    
    System.out.println(response.toString());
    out.close();
    connection.disconnect();
    return response.toString();
  }
}