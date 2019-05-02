package com.pdlskenya.postpaid;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

//@WebService(targetNamespace = "http://pdslipay/", portName = "SendRequestsMethodsPort", serviceName = "SendRequestsMethodsService")
public class SendRequestsMethods {
	

ClientEmulator clientobj = new ClientEmulator();
DataStore data = new DataStore();
StartPay payobj = new StartPay();
static Connection con = null;
public  SendRequestsMethods() throws IOException {
	con = data.connect();
	System.out.println("Creating tables if not exist");
	java.util.logging.Logger.getLogger(SendRequestsMethods.class.getName())
    .log(java.util.logging.Level.INFO, "Creating tables if not exist");
	data.createTables();
}

//bill payment
	@WebMethod(operationName = "startBillPayment", action = "urn:StartBillPayment")
	@RequestWrapper(className = "pdslipay.jaxws.StartBillPayment", localName = "startBillPayment", targetNamespace = "http://pdslipay/")
	@ResponseWrapper(className = "pdslipay.jaxws.StartBillPaymentResponse", localName = "startBillPaymentResponse", targetNamespace = "http://pdslipay/")
	@WebResult(name = "return")
	public String startBillPayment(@WebParam(name = "msisdn") String msisdn, @WebParam(name = "accountno") String account_no, @WebParam(name = "amount") String amount){
		String client = "PdslEquity";
        String term = "00001";
        String response = null;
        //ACC: 2232720-01
        double amounts = Double.parseDouble(amount);
        double conver = 100;
        double amounts2 = amounts * conver;
        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(0);
        String amount2 = String.valueOf(df.format(amounts2));
        try {
			//response = payobj.startPay(msisdn, account_no, amount2, client, term);
		} catch (Exception e) {
			e.printStackTrace();
		}
     
        	return response;
	}
//get account info
	@WebMethod(operationName = "getAccountDetails", action = "urn:GetAccountDetails")
	@RequestWrapper(className = "pdslipay.jaxws.GetAccountDetails", localName = "getAccountDetails", targetNamespace = "http://pdslipay/")
	@ResponseWrapper(className = "pdslipay.jaxws.GetAccountDetailsResponse", localName = "getAccountDetailsResponse", targetNamespace = "http://pdslipay/")
	@WebResult(name = "return")
	public Object getAccountDetails(@WebParam(name = "accountno") String account_no){
		String client = "PdslEquity";
                String term = "00001";
		Object response = clientobj.getDetailsAcc(account_no, client, term);
                //response = "test";
		return response;
	}
//get last pay request
	@WebMethod(operationName = "getpaylastrequest", action = "urn:Getpaylastrequest")
	@RequestWrapper(className = "pdslipay.jaxws.Getpaylastrequest", localName = "getpaylastrequest", targetNamespace = "http://pdslipay/")
	@ResponseWrapper(className = "pdslipay.jaxws.GetpaylastrequestResponse", localName = "getpaylastrequestResponse", targetNamespace = "http://pdslipay/")
	@WebResult(name = "return")
	public String getpaylastrequest(@WebParam(name = "provName") String provName, @WebParam(name = "provRef") String provRef ){
		String client = "PdslEquity";
        String term = "00001";
        String response = null;
        response = clientobj.getPayLastReq(client, term, provName, provRef);
        
		return response;
        
	}
//get providers	
	@WebMethod(operationName = "getProviderList", action = "urn:GetProviderList")
	@RequestWrapper(className = "pdslipay.jaxws.GetProviderList", localName = "getProviderList", targetNamespace = "http://pdslipay/")
	@ResponseWrapper(className = "pdslipay.jaxws.GetProviderListResponse", localName = "getProviderListResponse", targetNamespace = "http://pdslipay/")
	@WebResult(name = "return")
	public String getProviderList(){
		String client = "PdslEquity";
        String term = "00001";
		String response = null;
        
		response = clientobj.getProviderList(client, term);
		return response;
	}
	
	@WebMethod(operationName = "startSubsequents", action = "urn:StartSubsequents")
	@RequestWrapper(className = "pdslipay.jaxws.StartSubsequents", localName = "startSubsequents", targetNamespace = "http://pdslipay/")
	@ResponseWrapper(className = "pdslipay.jaxws.StartSubsequentsResponse", localName = "startSubsequentsResponse", targetNamespace = "http://pdslipay/")
	@WebResult(name = "return")
	public String startSubsequents(){
		String response = "sent";
		SendSubReversals d = new SendSubReversals();
		try {
			d.startsubrev();
		} catch (IOException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}
	
}
