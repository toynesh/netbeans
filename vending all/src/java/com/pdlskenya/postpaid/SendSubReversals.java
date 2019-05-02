package com.pdlskenya.postpaid;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
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

public class SendSubReversals {
	
	DataStore data = new DataStore();
	ClientEmulator client = new ClientEmulator();
	static Connection con = null;
	public SendSubReversals(){
		con = data.connect();
	}
	String response;
	
	public String[][] getRecordReversals() {
        String[][] str = new String[0][0];
         int r = 0;
      try {
      String query = "SELECT * from reversals where status = 'incomplete'";
      Statement stm = con.createStatement();
      ResultSet rs = stm.executeQuery(query);
       while (rs.next()) r++;
       rs.first();
        int c = rs.getMetaData().getColumnCount();
       str = (String[][])null;
       str = new String[r][c];
 
        for (int i = 0; i < r; i++)
       {
         for (int j = 0; j < c; j++) {
            str[i][j] = rs.getString(j + 1);
        }
              rs.next();
            }
  
      } catch (SQLException se) {
             se.printStackTrace();
      }
     return str;
   }
        Object resp = null;
	public Object startsubrev() throws IOException, SQLException{
		String MessageDetails[][] = new String[0][0];
		SendSubReversals d = new SendSubReversals();
        MessageDetails = d.getRecordReversals();
        
        if (MessageDetails.length == 0) {
            resp = client.error("No incomplete SubReversals");
            }
       
        int i,j,k=0;

         for(i=MessageDetails.length-1; i>=0; i--){ k=i;
 
        //System.out.println("ReversalId!=" + MessageDetails[i][0]);
          final String subclient = MessageDetails[i][2];
     	  final String originalRef = MessageDetails[i][4];
          final String refnumber = MessageDetails[i][6];
     	  final String subterm = MessageDetails[i][9];
     	  final String originalTime = MessageDetails[i][7];
     	  final String submsisdn = MessageDetails[i][8];
          final String revvvid = MessageDetails[i][0];
         // System.out.print("revid\t"+revvvid+" client\t"+subclient+"\t"+originalRef+"\t"+refnumber+"\t"+subterm+"\t"+originalTime+"\t"+submsisdn);
        //  System.out.println();
          
          ExecutorService service = Executors.newSingleThreadExecutor();
          try {
              Runnable r = new Runnable() {
                  @Override
                  public void run() {
                      // Task
                     
                      try {
                      	resp= client.subsequentPayRevReq(originalRef, originalTime, subclient, subterm,submsisdn,revvvid);
                         
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
             
          
          int timeout = Integer.parseInt( prop.getProperty("subseqreversaltimeout20sec"));
              f.get(timeout, TimeUnit.SECONDS);     // attempt the task for 30seconds
          }
          catch (final InterruptedException e) {
              // The thread was interrupted during sleep, wait or join
          	System.out.println("The thread was interrupted during sleep, wait or join");
          }
          catch (final TimeoutException e) {
              // Took too long!
          	System.out.println(" Took too long!");
          	System.out.println("Subseq Reversal incomplete");
              //Save in reversals table
          	
          	  DateTime dt = new DateTime();
              DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss Z");
              String str = fmt.print(dt);
              String time = str;
              String repC = client.createRepCount();
              String seq = client.createSeq();
                
                String update = "update reversals set repcount = ?, seqnumber = ?,time = ?, schedule = ? where reversalid = ?";
                PreparedStatement preparedStmt = con.prepareStatement(update);
                preparedStmt.setString(1, repC);
                preparedStmt.setString(2, seq);
                preparedStmt.setString(3, time);
                preparedStmt.setString(4, "started");
                preparedStmt.setString(5, revvvid);
                preparedStmt.executeUpdate();
                resp = client.error("Provider Unreachable");
             
          }
          catch (final ExecutionException e) {
              // An exception from within the Runnable task
          	System.out.println("An exception from within the Runnable task");
          }
          finally {
              service.shutdown();
          }
          
            }
	return resp;	
	}
	
	public static void main(String[] arg) throws IOException, SQLException{
		SendSubReversals d = new SendSubReversals();
		d.startsubrev();
	}

}
