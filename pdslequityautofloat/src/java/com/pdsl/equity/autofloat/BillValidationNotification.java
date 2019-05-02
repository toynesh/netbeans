/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.equity.autofloat;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author coolie
 */
@WebService(serviceName = "BillValidationNotification")
public class BillValidationNotification {

    public BillValidationNotification() {
        callBankReference();
    }

    /**
     * This is a sample web service operation
     *
     * @param billNumber
     * @param username
     * @param password
     * @WebMethod(operationName = "hello") public String hello(@WebParam(name =
     * "name") String txt) { return "Hello " + txt + " !"; }
     */
    List<String> bankReference = new ArrayList<>();
    DataStore data = new DataStore();

    //@WebMethod(operationName = "validationReq")
    public ResMan remoteBillValidation(@WebParam(name = "request", targetNamespace = "") ValidationReq request) {
        String billNumber = request.getBillNumber();
        String username = request.getPassword();
        String password = request.getPassword();
        Logger.getLogger(BillValidationNotification.class.getName()).log(Level.INFO, "BillValidation In << BillNumber: " + billNumber);
        Logger.getLogger(BillValidationNotification.class.getName()).log(Level.INFO, "BillValidation In << Username: " + username);
        Logger.getLogger(BillValidationNotification.class.getName()).log(Level.INFO, "BillValidation In << Password: " + password);
        ResFormatter resformat = new ResFormatter();
        if (billNumber.equals("1001")) {
            String amount = "230.0";
            String billerCode = "123456";
            String createdOn = "2016-12-20";
            String status = "OK";
            String currencyCode = "KES";
            String customerName = "Test_Account_3";
            String customerRefNumber = billNumber;
            String type = "1";
            return resformat.CValidationXML(amount, billNumber, billerCode, createdOn, status, currencyCode, customerName, customerRefNumber, type);
        } else {
            String responseCode = "FALSE";
            String responseMessage = "BILL NOT FOUND";
            return resformat.DefaultXML(responseCode, responseMessage);
        }
    }

    public ResMan paymentNotification(@WebParam(name = "request", targetNamespace = "") NotificationReq request) {
        String billNumber = request.getBillNumber();
        String billAmount = request.getBillAmount();
        String customerRefNumber = request.getCustomerRefNumber();
        String bankreference = request.getBankreference();
        String paymentMode = request.getPaymentMode();
        String transactionDate = request.getTransactionDate();
        String phonenumber = request.getPhoneNumber();
        String debitaccount = request.getDebitAccount();
        String debitcustname = request.getDebitCustName();
        String username = request.getUserName();
        String password = request.getPassword();
        Logger.getLogger(BillValidationNotification.class.getName()).log(Level.INFO, "PaymentNotification In << BillNumber: " + billNumber);
        Logger.getLogger(BillValidationNotification.class.getName()).log(Level.INFO, "PaymentNotification In << BillAmount: " + billAmount);
        Logger.getLogger(BillValidationNotification.class.getName()).log(Level.INFO, "PaymentNotification In << CustomerRefNumber: " + customerRefNumber);
        Logger.getLogger(BillValidationNotification.class.getName()).log(Level.INFO, "PaymentNotification In << BankReference: " + bankreference);
        Logger.getLogger(BillValidationNotification.class.getName()).log(Level.INFO, "PaymentNotification In << PaymentMode: " + paymentMode);
        Logger.getLogger(BillValidationNotification.class.getName()).log(Level.INFO, "PaymentNotification In << TransactionDate: " + transactionDate);
        Logger.getLogger(BillValidationNotification.class.getName()).log(Level.INFO, "PaymentNotification In << PhoneNumber: " + phonenumber);
        Logger.getLogger(BillValidationNotification.class.getName()).log(Level.INFO, "PaymentNotification In << DebitAccount: " + debitaccount);
        Logger.getLogger(BillValidationNotification.class.getName()).log(Level.INFO, "PaymentNotification In << DebitCustname: " + debitcustname);
        Logger.getLogger(BillValidationNotification.class.getName()).log(Level.INFO, "PaymentNotification In << Username: " + username);
        Logger.getLogger(BillValidationNotification.class.getName()).log(Level.INFO, "PaymentNotification In << Password: " + password);

        ResFormatter resformat = new ResFormatter();
        if (!bankReference.contains(bankreference)) {
            bankReference.add(bankreference);
            String insert = "INSERT INTO `equityautofloat`.`transactions` (`billNumber`, `billAmount`, `customerRefNumber`, `bankreference`, `paymentMode`, `transactionDate`, `phonenumber`, `debitaccount`, `debitcustname`, `username`, `password`) "
                    + "VALUES ('" + billNumber + "', '" + billAmount + "', '" + customerRefNumber + "', '" + bankreference + "', '" + paymentMode + "', '" + transactionDate + "', '" + phonenumber + "', '" + debitaccount + "', '" + debitcustname + "', '" + username + "', '" + password + "')";
            data.insert(insert);
            String responseCode = "OK";
            String responseMessage = "SUCCESSFULL";
            return resformat.DefaultXML(responseCode, responseMessage);
        } else {
            String responseCode = "OK";
            String responseMessage = "DUPLICATE TRANSACTION";
            return resformat.DefaultXML(responseCode, responseMessage);
        }
    }

    public void callBankReference() {
        if (bankReference.size() == 0) {
            try {
                Connection con = data.connect();
                String query = "SELECT bankreference FROM transactions";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    System.out.println("Saving: " + rs.getString(1));
                    bankReference.add(rs.getString(1));
                }
                System.out.println("BankReference Array Size: " + bankReference.size());
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(BillValidationNotification.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("BankReference Array Already Created: " + bankReference.size());
        }
    }
    
    public String check(String tranparticular) {
        List<List<String>> agarray = new ArrayList<>();
        List<List<String>> vendorarray = new ArrayList<>();
        Connection con = this.data.connect();
        String res = "none";
        Logger.getLogger(BillValidationNotification.class.getName()).log(Level.INFO, "Matching Vendor in: " + tranparticular);
        try {
            String query = "select ag_code,ag_name,eazzybiz,cheque,rtgs from aggregators";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            ResultSetMetaData metadata = rs.getMetaData();
            int numcols = metadata.getColumnCount();
            while (rs.next()) {
                //System.out.println("Saving: " + rs.getString(1));
                List<String> row = new ArrayList<>(numcols); // new list per row
                int i = 1;
                while (i <= numcols) {  // don't skip the last column, use <=
                    row.add(rs.getString(i++));
                }
                agarray.add(row); // add it to the result
            }
            Logger.getLogger(BillValidationNotification.class.getName()).log(Level.INFO, "Number of Ags: " + agarray.size());

            String query2 = "select vendor_code,vendor_name,eazzybiz,cheque,rtgs from vendors";
            Statement st2 = con.createStatement();
            ResultSet rs2 = st2.executeQuery(query2);
            ResultSetMetaData metadata2 = rs2.getMetaData();
            int numcols2 = metadata2.getColumnCount();
            while (rs2.next()) {
                //System.out.println("Saving: " + rs.getString(1));
                List<String> row2 = new ArrayList<>(numcols); // new list per row
                int i = 1;
                while (i <= numcols2) {  // don't skip the last column, use <=
                    row2.add(rs2.getString(i++));
                }
                vendorarray.add(row2); // add it to the result
            }
            Logger.getLogger(BillValidationNotification.class.getName()).log(Level.INFO, "Number of Vendors: " + vendorarray.size());
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(BillValidationNotification.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (int y = 0; y < agarray.size(); y++) {
            String agcode = agarray.get(y).get(0);
            String agname = agarray.get(y).get(1);
            String eazzybiz = agarray.get(y).get(2);
            String cheque = agarray.get(y).get(3);
            String rtgs = agarray.get(y).get(4);
            try {
                if (tranparticular.contains(eazzybiz)) {
                    res = agname;
                    //Logger.getLogger(inbound.class.getName()).log(Level.INFO, "Found in: eazzybiz " + agname);
                }
            } catch (Exception ex) {
                //Logger.getLogger(inbound.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (tranparticular.contains(cheque)) {
                    res = agname;
                    //Logger.getLogger(inbound.class.getName()).log(Level.INFO, "Found in: cheque " + agname);
                }
            } catch (Exception ex) {
                //Logger.getLogger(inbound.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (tranparticular.contains(rtgs)) {
                    res = agname;
                    //Logger.getLogger(inbound.class.getName()).log(Level.INFO, "Found in: rtgs " + agname);
                }
            } catch (Exception ex) {
                //Logger.getLogger(inbound.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Logger.getLogger(BillValidationNotification.class.getName()).log(Level.INFO, "check res " + res);
        for (int x = 0; x < vendorarray.size(); x++) {
            String vcode = vendorarray.get(x).get(0);
            String vname = vendorarray.get(x).get(1);
            String eazzybiz = vendorarray.get(x).get(2);
            String cheque = vendorarray.get(x).get(3);
            String rtgs = vendorarray.get(x).get(4);
            try {
                if (tranparticular.contains(eazzybiz)) {
                    res = vname;
                    //Logger.getLogger(inbound.class.getName()).log(Level.INFO, "Found in: eazzybiz " + vname);
                }
            } catch (Exception ex) {
                //Logger.getLogger(inbound.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (tranparticular.contains(cheque)) {
                    res = vname;
                    //Logger.getLogger(inbound.class.getName()).log(Level.INFO, "Found in: cheque " + vname);
                }
            } catch (Exception ex) {
                //Logger.getLogger(inbound.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (tranparticular.contains(rtgs)) {
                    res = vname;
                    //Logger.getLogger(inbound.class.getName()).log(Level.INFO, "Found in: rtgs " + vname);
                }
            } catch (Exception ex) {
                //Logger.getLogger(inbound.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return res;
    }

    public String getShop(String code) {
        Connection con = this.data.connect();
        String resp = "none";
        try {
            String query = "select ag_code,ag_name from aggregators where ag_code='" + code + "' limit 1";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    resp = rs.getString(2);
                }
            } else {
                String query2 = "select vendor_code,vendor_name from vendors where vendor_code='" + code + "' limit 1";
                Statement st2 = con.createStatement();
                ResultSet rs2 = st2.executeQuery(query2);
                if (rs.isBeforeFirst()) {
                    resp = rs2.getString(2);
                }
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(BillValidationNotification.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resp;
    }

    public String sendReq(String param) throws IOException {
        String request = "http://172.27.116.36:8084/pdslvending/Autofloat?";
        URL url = new URL(request);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("charset", "utf-8");
        connection.setRequestProperty("Content-Length", "" + Integer.toString(param.getBytes().length));
        connection.setUseCaches(false);

        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.writeBytes(param);
        out.flush();

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        StringBuffer response = new StringBuffer();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //System.out.println(response.toString());
        out.close();
        connection.disconnect();
        return response.toString();
    }

    public String sendSubAgFloat(String vcode, String refe, String amount) {
        String url = "vcode=" + vcode + "&refe=" + refe + "&amount=" + amount + "";
        System.out.println(url);
        String res;
        try {
            res = sendReq(url);
        } catch (IOException e) {
            e.printStackTrace();
            res = "FAILED";
        }
        return res;
    }
}
