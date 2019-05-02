package com.pdlskenya.postpaid;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class SubseqrevTimer {

	ClientEmulator client = new ClientEmulator();
	static DataStore data = new DataStore();
    static Connection con = null;

	public SubseqrevTimer() throws IOException{
		con = data.connect();
		
		Properties prop = new Properties();
        prop.load(getClass().getResourceAsStream("pdsl.properties"));
     int reptime = Integer.parseInt( prop.getProperty("subseqreversaltimeout20sec"));
		
		final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleWithFixedDelay(new Runnable()
          {
              
             
            @Override
            public void run()
            { 
            	try{
            	String query = "SELECT clientid, original_ref,refnumber,terminal,time,msisdn,reversalid from reversals where status = 'incomplete'";
	            Statement stm = con.createStatement();
	            ResultSet rst = stm.executeQuery(query);
	            while (rst.next()) {
	            	String subclient = rst.getString(1);
	            	String originalRef = rst.getString(2);
	            	String refnumber = rst.getString(3);
	            	String subterm = rst.getString(4);
	            	String originalTime = rst.getString(5);
	            	String submsisdn = rst.getString(6);
	            	String revvvid = rst.getString(7);
	            	
	            	client.subsequentPayRevReq(originalRef, originalTime, subclient, subterm,submsisdn,revvvid);
	            }
            	}catch (Exception ex){
            		System.out.println(ex);
            	}
                
               }
          }, 0, reptime, TimeUnit.SECONDS);
	}
}
