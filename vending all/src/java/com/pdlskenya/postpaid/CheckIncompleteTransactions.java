package com.pdlskenya.postpaid;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class CheckIncompleteTransactions {
/*
	public static void main(String[] args) throws Exception{
		CheckIncompleteTransactions chqpendingobj = new CheckIncompleteTransactions();
		chqpendingobj.startcheck();
    }
*/
    static DataStore data = new DataStore();
    static Connection con = null;
    
public CheckIncompleteTransactions(){
	con = data.connect();
}

public void startcheck(){
	try {
        String query = "SELECT refnumber from transaction where status = 'revbackup'";
        Statement stm = con.createStatement();
        ResultSet rst = stm.executeQuery(query);
        while (rst.next()) {
        	String referenceno = rst.getString(1);
        	
        	 String query2 = "SELECT vendid, clientid, seqnumber, account_number, status, time, msisdn,terminal from transaction where refnumber = '"+referenceno+"' ORDER BY vendid DESC LIMIT 1";
             Statement stm2 = con.createStatement();
             ResultSet rst2 = stm2.executeQuery(query2);
             while (rst2.next()) {
             	int chvendid= rst2.getInt(1);
             	String chclient = rst2.getString(2);
             	String chseqno = rst2.getString(3);
             	String chaccno = rst2.getString(4);
             	String chstatus = rst2.getString(5);
             	String chstime = rst2.getString(6);
             	String chmsisdn = rst2.getString(7);
             	String chterm = rst2.getString(8);
                
             	if(chstatus.equals("revbackup")){
                    StartInitialPayReversalRequest objc =  new StartInitialPayReversalRequest();
                    objc.startInitialPayRevReq(referenceno, chclient, chterm, chaccno,chmsisdn);
                    System.out.println("Incomplete Reversal transactions scheduled"); 
             	}else{
             		System.out.println("No unhandled reversals");
             	}
             	
             }
        }

        
} catch (Exception ex) {
	System.out.println(ex);
}
}
}
