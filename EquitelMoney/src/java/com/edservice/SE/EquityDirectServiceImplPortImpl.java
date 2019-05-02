package com.edservice.SE;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

@WebService(targetNamespace = "http://SE.edservice.com/", endpointInterface = "com.edservice.SE.EquityDirectServiceImpl", portName = "EquityDirectServiceImplPortImpl", serviceName = "EquityDirectServiceImplPortImplService")
public class EquityDirectServiceImplPortImpl implements EquityDirectServiceImpl {

    public PaymentNotificationResponse notifyPayment(PaymentNotificationRequest request) {
        System.out.println(request.getBillNumber());
        System.out.println(request.getBillAmount());
        System.out.println(request.getCustomerRefNumber());
        System.out.println(request.getBankreference());
        System.out.println(request.getPaymentMode());
        System.out.println(request.getTransactionDate());
        System.out.println(request.getPhonenumber());
        System.out.println(request.getDebitaccount());
        System.out.println(request.getDebitcustname());
        System.out.println(request.getUsername());
        System.out.println(request.getPassword());

        String billNumber = request.getBillNumber();
        String billAmount = request.getBillAmount();
        String customerRefNumber = request.getCustomerRefNumber();
        String bankreference = request.getBankreference();
        String paymentMode = request.getPaymentMode();
        String transactionDate = request.getTransactionDate();
        String phoneNumber = request.getPhonenumber();
        String debitAccount = request.getDebitaccount();
        String debitCustName = request.getDebitcustname();
        String username = request.getUsername();
        String password = request.getPassword();
        OtherInc incs = new OtherInc();

        if (!phoneNumber.isEmpty() && phoneNumber != null) {
            DateTime dt2 = new DateTime();
            DateTimeFormatter fmt2 = DateTimeFormat.forPattern("yyyy-MM-dd");
            DateTimeFormatter fmtcd = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            String date2 = fmt2.print(dt2);
            String cdate = fmtcd.print(dt2);
            String strippeddate = date2.replaceAll("-", "");

            String newbankref = bankreference.replaceAll(strippeddate, "");

            String balamt = billAmount;
            Logger.getLogger(EquityDirectServiceImplPortImpl.class.getName()).log(Level.INFO,
                    "Sending Equitel Ref::=::" + newbankref);

            try {
                String myDriver = "org.gjt.mm.mysql.Driver";
                String myUrl = "jdbc:mysql://localhost/equitel";
                try {
                    Class.forName(myDriver);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                Connection conn = DriverManager.getConnection(myUrl, "pdsluser", "P@Dsl949022");
                String query = "SELECT distinct 1 bankreference FROM equity WHERE bankreference = '" + newbankref + "'";
                Statement stm = conn.createStatement();
                ResultSet rs = stm.executeQuery(query);
                if (!rs.isBeforeFirst()) {
                    String payres = incs.sendPayment(billNumber, billAmount, phoneNumber);
                    System.out.println(payres);
                    String msg = "Dear Customer,We are experiencing KPLC delays. Once the system is up we will resend your transaction. Queries call 0709711000";
                    if (null != payres) {
                        if (!payres.equals("FAIL")) {
                            if (payres.startsWith("Token")) {
                                String pressplit[] = payres.split(":");
                                msg = "KPLC Mtr No:" + billNumber + "\nToken:" + pressplit[1].replaceAll(" Units", "")
                                        + "\nUnits:" + pressplit[2].replaceAll(" Elec", "") + "\nAmount:" + billAmount
                                        + "(Elec:" + pressplit[3].replaceAll(" Charges", "") + " Other Charges:"
                                        + pressplit[4] + ")" + "\nDate:" + cdate + "\nEquRef:" + newbankref;
                                balamt = "0";
                            } else if (payres.startsWith("Paid")) {
                                String pressplit[] = payres.split(":");
                                msg = "PAID KSH:" + billAmount + " FOR KPLC ACCOUNT:" + billNumber + " RECEIPT:"
                                        + pressplit[3];
                                balamt = "0";
                            } else {
                                msg = payres;
                            }
                        }
                    }
                    EquitelSMS esdp = new EquitelSMS("172.27.116.22", "EQUITELRX", "pdsl219", "pdsl219", phoneNumber,
                            msg, "VendIT");
                    esdp.submitMessage();
                    Sdp sdp = new Sdp();
                    try {
                        sdp.sendSMS(phoneNumber, msg, newbankref, "704307",
                                "http://197.248.9.105:8084/VendITNotification/SmsNotificationService");
                    } catch (Exception sdpexc) {
                    }
                    String insert = "insert into equity(billNumber,billAmount,balance,customerRefNumber,bankreference,paymentMode,transactionDate,phoneNumber,debitAccount,debitCustName,username,password,message) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";

                    PreparedStatement prep = conn.prepareStatement(insert);
                    prep.setString(1, billNumber);
                    prep.setString(2, billAmount);
                    prep.setString(3, balamt);
                    prep.setString(4, customerRefNumber);
                    prep.setString(5, newbankref);
                    prep.setString(6, paymentMode);
                    prep.setString(7, transactionDate);
                    prep.setString(8, phoneNumber);
                    prep.setString(9, debitAccount);
                    prep.setString(10, debitCustName);
                    prep.setString(11, username);
                    prep.setString(12, password);
                    prep.setString(13, msg);
                    prep.execute();
                    prep.close();
                } else {
                    Logger.getLogger(EquityDirectServiceImplPortImpl.class.getName()).log(Level.SEVERE,
                            "EXISTING Equitel Ref: " + newbankref);
                }
                conn.close();
            } catch (SQLException localSQLException) {
                localSQLException.printStackTrace();
            }
        }
        PaymentNotificationResponse res = new PaymentNotificationResponse();
        res.setResponseCode("OK");
        res.setResponseMessage("SUCCESSFULL");

        return res;

    }

    public BillQueryResponse queryBill(BillQueryRequest request) {
        BillQueryResponse res = new BillQueryResponse();
        res.setAmount("10000");
        return res;
    }
}
