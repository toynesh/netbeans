/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.sdp;

import com.pdsl.datastore.DataStore;
import com.pdsl.sms;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author coffee
 */
public class LinkIdController {
    
    DataStore data=new DataStore();
    Logger log=Logger.getLogger(sms.class.getName());
    
    
    public void insert(String linkid,String msisdn,String scode,String correlator,String message){
      
            String insert="insert into ondemand (linkid,msisdn,message,scode,correlator) values ('"+linkid+"',"+msisdn+",'"+message+"','"+scode+"','"+correlator+"')";
            data.insert(insert);
             /*if(scode.equals("22260")||scode.equals("22225") ){
            //if(scode.equals("22260") ){
                System.out.println("Scode is "+scode);
            sendNormalMes(msisdn,message,linkid,scode);
            }*/
            
    }
    
    
             public void sendNormalMes(String number,String message,String linkid,String scode){
    try{
            
        System.out.println("Message under sendNormal: "+message);
    String outmessage=message;
     // String[] array=scode.split(":");
        //scode=array[1];
       // array=number.split(":");
       // number=array[1];
          log.log(Level.INFO,message);
          
       message = URLEncoder.encode(message.toString(),"UTF-8");
       //System.out.println(message);
      

      // boolean test=true;
     // while(test){
        try{

            //    System.out.println("Am here");

           String username="tester";
           String password="tester";
           //String host="192.168.100.202";
           String host="127.0.0.1";
           String port="8084";
           //String from="ICOW";//
    //       String from=prop.getProperty("scode.alphanumeric");
          // String binfo=prop.getProperty("scode.binfo");//"6463";//
           
           //String from="5024";//prop.getProperty("wingo.shortcode");
           //   String from=prop.getProperty("wingo.shortcode");
           //  String to="+254735764849";
            String smic="";
            String destination=number;
            String from=number;
            
            String directives = "msisdn=" +destination;
            directives += "&message=" + message;
            directives += "&scode="+scode;
            directives += "&linkid="+linkid;
            //directives += "&to=" + destination;
            //directives += "&from=254" + from;
      
           // directives += "&text=" + message;
            //directives += "&text=" + encodedurl;


    //        System.out.println(host+":"+port+":"+directives+"directives");
            URL url=null;
    StringBuilder res=new StringBuilder();
            url= new URL("http://"+host+":"+port+"/muzikit/muzikit?" + directives);
             //	System.out.println("Url content "+url.getContent());
              //  System.out.println("Url stirng  "+url.toString());
            URLConnection conn = url.openConnection();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response;
            while ((response = rd.readLine()) != null)
                    res.append(response);
            rd.close();
       log.log(Level.INFO,res.toString());     
            
            //System.out.println(res.toString());


        }catch(Exception e){
        e.printStackTrace();
       }
        }catch(UnsupportedEncodingException ex){
             Logger.getLogger(sms.class.getName()).log(Level.SEVERE, null, ex);
   }
            /*PreparedStatement prep=data.connect().prepareStatement(insert);
            prep.setString(1, linkid);
            prep.setString(2, msisdn);
            prep.setString(3, correlator);
            prep.executeQuery();
            prep.close();
          */  
        
    }
    
    
    
}
