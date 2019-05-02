/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdlskenya.postpaid;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.*;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author kariu
 */


public class StartPay {
   /*
       public static void main(String[] args) throws Exception{
        StartPay startpayobj = new StartPay();
        startpayobj.startPay();
    }
    */
	DataStore data = new DataStore();
	static Connection con = null;
	public StartPay(){
		con = data.connect();
	}
	private Object response;
	public Object getResponse() {
	    return response;
	}
	public void setResponse(Object response) {
	    this.response = response;
	}
    public Object startPay(final String newmsisdn, final String newaccount_no, final String newamount, final String newclient, final String newterm) throws Exception{
        final ClientEmulator client = new ClientEmulator();
        final DataStore data = new DataStore();
        
        ExecutorService service = Executors.newSingleThreadExecutor();
        try {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    // Task
                   
                    try {
                        System.out.println("starting vend request");
                        Object resp = client.payBill(newmsisdn, newaccount_no, newamount, newclient, newterm);
                        StartPay objsp = new StartPay();
                        objsp.setResponse(resp);
                        response = objsp.getResponse();
                       
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
                }
            };

            Future<?> f = service.submit(r);
            
            //System.out.println(new File(".").getAbsolutePath());
            //FileReader reader = new FileReader("src\\resource\\pdsl.properties");
            Properties prop = new Properties();
            prop.load(getClass().getResourceAsStream("pdsl.properties"));
           
        
        int timeout = Integer.parseInt( prop.getProperty("initialtimeout30sec"));
            f.get(timeout, TimeUnit.SECONDS);     // attempt the task for 30seconds
        }
        catch (final InterruptedException e) {
            // The thread was interrupted during sleep, wait or join
        	System.out.println("The thread was interrupted during sleep, wait or join");
        }
        catch (final TimeoutException e) {
            // Took too long!
        	System.out.println(" Took too long!");
          
            System.out.println("FirstTimeout at 5sec!!!");
            //save incomplete vending
            DateTime dt = new DateTime();
            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss Z");
            String str = fmt.print(dt);;
            String time = str;
            String seq = client.createSeq();
          
            Object resp = new ClientEmulator().error("Provider Unreachable");
          
            StartPay objsp = new StartPay();
            objsp.setResponse(resp);
            response = objsp.getResponse();
            String update = "update transaction set status = ? where refnumber = ?";
            PreparedStatement preparedStmt = con.prepareStatement(update);
            preparedStmt.setString(1, "incomplete");
            preparedStmt.setString(2, client.refe);
            preparedStmt.executeUpdate();
            System.out.println("Saved to database with incomplete status!!!");
            System.out.println("Starting PayReversalRequest!!!");
            Logger.getLogger(StartPay.class.getName()).log(Level.INFO, "Saved to database with incomplete status!!!  Starting PayReversalRequest!!!: "+response);
            
            StartInitialPayReversalRequest objc =  new StartInitialPayReversalRequest();
            objc.startInitialPayRevReq(client.refe, newclient, newterm, newaccount_no,newmsisdn);
            
        }
        catch (final ExecutionException e) {
            // An exception from within the Runnable task
        	System.out.println("An exception from within the Runnable task");
        }
        finally {
            service.shutdown();
        }
        
       return response; 
    }
}
