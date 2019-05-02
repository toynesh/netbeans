package com.pdlskenya.postpaid;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class SendRequests {

	DataStore data = new DataStore();
	StartPay payobject =  new StartPay();
	static Connection con = null;
	
	public SendRequests(){
		con = data.connect();
		}
	
	public void sendPayRequests(int reqno){
		try {
        	String query = "SELECT * from requests where status = 'pending' AND reqid = "+reqno+"";
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(query);
            while (rs.next()) { 
            	int reqid = rs.getInt("reqid");
            	String clientid = rs.getString("clientid");
            	String account_number = rs.getString("account_number");
            	String amount = rs.getString("amount");
            	String msisdn = rs.getString("msisdn");
            	String terminal = rs.getString("terminal");
            	//payobject.startPay(msisdn, account_number, amount, clientid, terminal, reqid);
            	
            	String update = "update requests set status = ? where reqid = ?";
                PreparedStatement preparedStmt = con.prepareStatement(update);
                preparedStmt.setString(1, "sent");
                preparedStmt.setInt(2, reqid);
                preparedStmt.executeUpdate();
            }
        }catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
