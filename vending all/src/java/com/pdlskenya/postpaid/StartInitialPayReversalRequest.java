/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdlskenya.postpaid;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author kariu
 */
public class StartInitialPayReversalRequest{
	
	final ClientEmulator client = new ClientEmulator();
	final DataStore data = new DataStore();
	static Connection con = null;
	
	public StartInitialPayReversalRequest(){
		con = data.connect();
	}
	
    public  void startInitialPayRevReq(String originalRef, String Oclient, String term, final String account_no, final String msisdn) throws Exception{
    	final String originalrefere = originalRef;
    	final String originalclient = Oclient;
    	final String originalterm = term;
    	final String originalaccount_no = account_no;
    ExecutorService service = Executors.newSingleThreadExecutor();
        try {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                  
                    try {
						client.initialPayRevReq(originalrefere, originalclient, originalterm, account_no, msisdn);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
            };

            Future<?> f = service.submit(r);
            
           //Properties prop = new Properties();
           //FileReader reader = new FileReader("src\\resource\\pdsl.properties");
           Properties prop = new Properties();
           prop.load(getClass().getResourceAsStream("pdsl.properties"));
           //prop.load(reader);
        
        int timeout1 = Integer.parseInt( prop.getProperty("firstreversaltimeout15sec"));

            f.get(timeout1, TimeUnit.SECONDS);     // attempt the task for 15seconds
        }
        catch (final InterruptedException e) {
            // The thread was interrupted during sleep, wait or join
        }
        catch (final TimeoutException e) {
            // Took too long!
        	String update = "update transaction set status = ? where refnumber = ?";
            PreparedStatement preparedStmt = con.prepareStatement(update);
            preparedStmt.setString(1, "initialpayrevincomplete");
            preparedStmt.setString(2, originalrefere);
            preparedStmt.executeUpdate();
            System.out.println("Saved to database with incomplete status!!!");
            System.out.println("Starting PayReversalRequest!!!");
            Logger.getLogger(StartPay.class.getName()).log(Level.INFO, "Initial pay reversal incompete!!!");
          //Save in reversals table
        	DateTime dt = new DateTime();
            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss Z");
            String str = fmt.print(dt);
            String time = str;
            String repC = client.createRepCount();
            String seq = client.createSeq();
            String insert = "INSERT INTO `vending`.`reversals` (`repcount`,`clientid`, `seqnumber`, `original_ref`, `status`, `refnumber`, `terminal`, `msisdn`, `time`)"
                    + " VALUES ('"+repC+"','" + originalclient + "', " + seq + ", " + originalrefere + ", 'incomplete', '" + originalrefere + "', '" + term + "', '" + msisdn + "', '" + time + "')";
            data.insert(insert);
            
            
        }
        catch (final ExecutionException e) {
            // An exception from within the Runnable task
           
        }
        finally {
            service.shutdown();
        }
    }

	
   
}
