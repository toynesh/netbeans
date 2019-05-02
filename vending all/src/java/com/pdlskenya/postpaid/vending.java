/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdlskenya.postpaid;

import static com.pdlskenya.postpaid.SendRequestsMethods.con;
import java.sql.Connection;
import java.text.DecimalFormat;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author koks
 */
//@XmlSeeAlso({org.apache.axiom.om.impl.llom.class})
@WebService(serviceName = "vending")
public class vending {
    
        ClientEmulator clientobj = new ClientEmulator();
        DataStore data = new DataStore();
        StartPay payobj = new StartPay();
        static Connection con = null;
        
        public vending(){
        con = data.connect();
	System.out.println("Creating tables if not exist");
	java.util.logging.Logger.getLogger(SendRequestsMethods.class.getName())
    .log(java.util.logging.Level.INFO, "Creating tables if not exist");
	data.createTables();
        }

    /**
     * This is for postpaid billing services
     * @param account_no
     * @param msisdn
     * @param amount
     */
    @WebMethod(operationName = "vend")
    public Object billSettlement(@WebParam(name = "account_no") String account_no, @WebParam(name = "mobile_no") String msisdn, @WebParam(name = "amount") String amount) {
        String client = "PdslEquity";
        String term = "00001";
        Object response = null;
        //ACC: 2232720-01
        double amounts = Double.parseDouble(amount);
        double conver = 100;
        double amounts2 = amounts * conver;
        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(0);
        String amount2 = String.valueOf(df.format(amounts2));
        try {
			response = payobj.startPay(msisdn, account_no, amount2, client, term);
		} catch (Exception e) {
			e.printStackTrace();
		}
     
        	return response;
    }

    /**
     * This is for checking account details
     * @param account_no
     */
    @WebMethod(operationName = "checkDetails")
    public Object accoutDetails(@WebParam(name = "account_no") String account_no) {
        String client = "PdslEquity";
                String term = "00001";
		Object response = clientobj.getDetailsAcc(account_no, client, term);
                //response = "test";
		return response;
    }
}
