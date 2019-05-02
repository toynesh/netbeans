/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.vending;

import static java.lang.Double.parseDouble;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;

/**
 *
 * @author coolie
 */
@WebService(serviceName = "Requests")
public class Requests {

    @Resource
    WebServiceContext wsContext;

    /**
     * This is a sample web service operation
     *
     * @WebMethod(operationName = "hello") public String hello(@WebParam(name =
     * "name") String txt) { return "Hello " + txt + " !"; }
     */
    DataStore data = new DataStore();
    ProcessRequest req = new ProcessRequest();

    //sample meters 1345445677433 , 68574638596433 accounts 27654358-03, 547675864-01 token type orange_100,saf_50
    @WebMethod(operationName = "prepaidVend")
    public VendResponse prepaidVend(@WebParam(name = "vendorcode") String vendorcode, @WebParam(name = "account") String account, @WebParam(name = "amount") String amount) {
        Logger.getLogger(Requests.class.getName()).log(Level.INFO, "\n >>PrePaidVend<< \n VendorCode: " + vendorcode + "\n Meter: " + account + "\n Amount: " + amount);
        FormatVendRes resformat = new FormatVendRes();

        MessageContext mc = wsContext.getMessageContext();
        HttpServletRequest req = (HttpServletRequest) mc.get(MessageContext.SERVLET_REQUEST);
        System.out.println("Client IP = " + req.getRemoteAddr());

        if (vendorcode == null || vendorcode.isEmpty() || vendorcode.equals("?") || vendorcode.equals("")) {
            Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, "EmptyVendorCode");
            return resformat.vendErrorXML("EmptyVendorCode");
        } else if (account == null || account.isEmpty() || account.equals("?") || account.equals("")) {
            Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, "EmptyMeter");
            return resformat.vendErrorXML("EmptyMeter");
        } else if (amount == null || amount.isEmpty() || amount.equals("?") || amount.equals("")) {
            Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, "EmptyAmount");
            return resformat.vendErrorXML("EmptyAmount");
        } else {
            if (data.chechVendor(vendorcode).equals("found")) {
                String prepaid = "";
                String postpaid = "";
                String airtime = "";
                String mode = "";
                String floatbal = "";
                try {
                    Connection con = data.connect();
                    String query = "SELECT `vendor_code`,`prepaid`,`postpaid`,`airtime`,`status` FROM `vendors` WHERE  `vendor_code`=" + vendorcode + "";
                    //System.out.println(query);
                    Statement stm = con.createStatement();
                    ResultSet rs = stm.executeQuery(query);
                    while (rs.next()) {
                        prepaid = rs.getString(2);
                        postpaid = rs.getString(3);
                        airtime = rs.getString(4);
                        mode = rs.getString(5);
                    }
                    con.close();
                } catch (SQLException ex) {

                }
                floatbal = data.getFloatBal(vendorcode);
                double fbal = parseDouble(floatbal);
                //Logger.getLogger(Requests.class.getName()).log(Level.INFO, "Float value:"+fbal);

                if (mode.equals("test")) {
                    Logger.getLogger(Requests.class.getName()).log(Level.INFO, "Token:29325208410603574761 Units:22.23 Elec:351.31 Charges:141.69");
                    return resformat.vendXML(account, "468576755990", "Token:29325208410603574761 Units:22.23 Elec:351.31 Charges:141.69", "1000");
                } else if (mode.equals("disabled")) {
                    Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, "VendorDisabled");
                    return resformat.vendErrorXML("VendorDisabled");
                } else {
                    /*Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, "LiveDisabled");
                    return resformat.vendErrorXML("LiveDisabled");
                }*/
                    //LIVE
                    if (prepaid.equals("yes")) {
                        if (data.chechIP(vendorcode, req.getRemoteAddr()).equals("found")) {
                            if (fbal > 0 && parseDouble(amount) < fbal) {
                                //!!!DONT FORGET ServiceNotAvailable                           
                                //INSERT TO DB
                                String ipayres = this.req._getPrePaidDetailsAcc(account, "PdslEquity", "00001");
                                ipayres = ipayres.replace("[", "");
                                ipayres = ipayres.replace("]", "");
                                if (ipayres.startsWith("Owner")) {
                                    //System.out.println("Starts with owner");
                                    String ipayres2 = "ServiceNotAvailable";
                                    try {
                                        ipayres2 = this.req._prePaidTokenBuy(vendorcode, "254728064120", account, amount, "PdslEquity", "00001");
                                    } catch (Exception ex) {
                                        Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, "Error Vending Prepaid Request" + ex);
                                    }
                                    if (ipayres2.startsWith("ServiceNotAvailable")) {
                                        Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, ipayres2);
                                        return resformat.vendErrorXML(ipayres2);
                                    } else if (ipayres2.startsWith("Unsuccessfulmsg:")) {
                                        Logger.getLogger(Requests.class.getName()).log(Level.INFO, ipayres2.replaceAll("Unsuccessfulmsg:", ""));
                                        return resformat.vendErrorXML(ipayres2.replaceAll("Unsuccessfulmsg:", ""));
                                    } else {
                                        String resarr[] = ipayres2.split(":");
                                        Logger.getLogger(Requests.class.getName()).log(Level.INFO, "Token:" + resarr[0] + " Units:" + resarr[1] + " Elec:" + resarr[3] + " Charges:" + resarr[4] + "");
                                        return resformat.vendXML(account, resarr[2], "Token:" + resarr[0] + " Units:" + resarr[1] + " Elec:" + resarr[3] + " Charges:" + resarr[4] + "", data.getFloatBal(vendorcode));
                                    }
                                } else {
                                    Logger.getLogger(Requests.class.getName()).log(Level.INFO, ipayres);
                                    return resformat.vendErrorXML(ipayres);
                                }

                            } else {
                                Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, "OutofFloat");
                                return resformat.vendErrorXML("OutofFloat");
                            }
                        } else {
                            Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, "IpNotWhitelisted");
                            return resformat.vendErrorXML("IpNotWhitelisted");
                        }
                    } else {
                        Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, "PrepaidVendDisabled");
                        return resformat.vendErrorXML("PrepaidVendDisabled");
                    }
                }
            } else {
                Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, "InvalidVendorCode");
                return resformat.vendErrorXML("InvalidVendorCode");
            }
        }
    }

    @WebMethod(operationName = "prepaidMeterQuery")
    public VendResponse prepaidMeterQuery(@WebParam(name = "vendorcode") String vendorcode, @WebParam(name = "meter") String meter) {
        Logger.getLogger(Requests.class.getName()).log(Level.INFO, "\n >>PrePaidMeterQuery<< \n VendorCode: " + vendorcode + "\n Meter: " + meter);
        FormatVendRes resformat = new FormatVendRes();

        MessageContext mc = wsContext.getMessageContext();
        HttpServletRequest req = (HttpServletRequest) mc.get(MessageContext.SERVLET_REQUEST);
        System.out.println("Client IP = " + req.getRemoteAddr());

        if (vendorcode == null || vendorcode.isEmpty() || vendorcode.equals("?") || vendorcode.equals("")) {
            Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, "EmptyVendorCode");
            return resformat.vendErrorXML("EmptyVendorCode");
        } else if (meter == null || meter.isEmpty() || meter.equals("?") || meter.equals("")) {
            Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, "EmptyMeter");
            return resformat.vendErrorXML("EmptyMeter");
        } else {
            if (data.chechVendor(vendorcode).equals("found")) {
                String prepaid = "";
                String postpaid = "";
                String airtime = "";
                String mode = "";
                String floatbal = "";
                try {
                    Connection con = data.connect();
                    String query = "SELECT `vendor_code`,`prepaid`,`postpaid`,`airtime`,`status` FROM `vendors` WHERE  `vendor_code`=" + vendorcode + "";
                    //System.out.println(query);
                    Statement stm = con.createStatement();
                    ResultSet rs = stm.executeQuery(query);
                    while (rs.next()) {
                        prepaid = rs.getString(2);
                        postpaid = rs.getString(3);
                        airtime = rs.getString(4);
                        mode = rs.getString(5);
                    }
                    con.close();
                } catch (SQLException ex) {

                }
                floatbal = data.getFloatBal(vendorcode);
                double fbal = parseDouble(floatbal);

                if (mode.equals("test")) {
                    Logger.getLogger(Requests.class.getName()).log(Level.INFO, "Meter " + meter + " Owner John Doe");
                    return resformat.vendQueryXML("Meter " + meter + " Owner John Doe");
                } else if (mode.equals("disabled")) {
                    Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, "VendorDisabled");
                    return resformat.vendErrorXML("VendorDisabled");
                } else {
                    /* Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, "LiveDisabled");
                    return resformat.vendErrorXML("LiveDisabled");
                } */
                    //LIVE
                    if (prepaid.equals("yes")) {
                        if (data.chechIP(vendorcode, req.getRemoteAddr()).equals("found")) {
                            //!!!DONT FORGET ServiceNotAvailable  
                            if (fbal > 0) {
                                String ipayres = this.req._getPrePaidDetailsAcc(meter, "PdslEquity", "00001");
                                ipayres = ipayres.replace("[", "");
                                ipayres = ipayres.replace("]", "");
                                if (ipayres.startsWith("Owner")) {
                                    Logger.getLogger(Requests.class.getName()).log(Level.INFO, "Meter " + meter + " " + ipayres);
                                    return resformat.vendQueryXML("Meter:" + meter + " " + ipayres);
                                } else {
                                    Logger.getLogger(Requests.class.getName()).log(Level.INFO, ipayres);
                                    return resformat.vendQueryXML(ipayres);
                                }
                            } else {
                                Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, "OutofFloat");
                                return resformat.vendErrorXML("OutofFloat");
                            }
                        } else {
                            Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, "IpNotWhitelisted");
                            return resformat.vendErrorXML("IpNotWhitelisted");
                        }

                    } else {
                        Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, "PrepaidVendDisabled");
                        return resformat.vendErrorXML("PrepaidVendDisabled");
                    }
                }
            } else {
                Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, "InvalidVendorCode");
                return resformat.vendErrorXML("InvalidVendorCode");
            }
        }
    }

    @WebMethod(operationName = "postpaidVend")
    public VendResponse postpaidVend(@WebParam(name = "vendorcode") String vendorcode, @WebParam(name = "account") String account, @WebParam(name = "amount") String amount, @WebParam(name = "phonenumber") String phonenumber) {
        Logger.getLogger(Requests.class.getName()).log(Level.INFO, "\n >>PostPaidVend<< \n VendorCode: " + vendorcode + "\n Account: " + account + "\n Amount: " + amount + "\n Phone: " + phonenumber);
        FormatVendRes resformat = new FormatVendRes();

        MessageContext mc = wsContext.getMessageContext();
        HttpServletRequest req = (HttpServletRequest) mc.get(MessageContext.SERVLET_REQUEST);
        System.out.println("Client IP = " + req.getRemoteAddr());

        //check float
        if (vendorcode == null || vendorcode.isEmpty() || vendorcode.equals("?") || vendorcode.equals("")) {
            Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, "EmptyVendorCode");
            return resformat.vendErrorXML("EmptyVendorCode");
        } else if (account == null || account.isEmpty() || account.equals("?") || account.equals("")) {
            Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, "EmptyAccount");
            return resformat.vendErrorXML("EmptyAccount");
        } else if (amount == null || amount.isEmpty() || amount.equals("?") || amount.equals("")) {
            Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, "EmptyAmount");
            return resformat.vendErrorXML("EmptyAmount");
        } else {
            if (data.chechVendor(vendorcode).equals("found")) {
                String prepaid = "";
                String postpaid = "";
                String airtime = "";
                String mode = "";
                String floatbal = "";
                try {
                    Connection con = data.connect();
                    String query = "SELECT `vendor_code`,`prepaid`,`postpaid`,`airtime`,`status` FROM `vendors` WHERE  `vendor_code`=" + vendorcode + "";
                    //System.out.println(query);
                    Statement stm = con.createStatement();
                    ResultSet rs = stm.executeQuery(query);
                    while (rs.next()) {
                        prepaid = rs.getString(2);
                        postpaid = rs.getString(3);
                        airtime = rs.getString(4);
                        mode = rs.getString(5);
                    }
                    con.close();
                } catch (SQLException ex) {

                }
                floatbal = data.getFloatBal(vendorcode);
                double fbal = parseDouble(floatbal);

                if (mode.equals("test")) {
                    Logger.getLogger(Requests.class.getName()).log(Level.INFO, "Paid " + amount + " For Account " + account);
                    return resformat.vendXML(account, "468576755990", "Paid " + amount + " For Account " + account + " Reciept:10895716", "1000");
                } else if (mode.equals("disabled")) {
                    Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, "VendorDisabled");
                    return resformat.vendErrorXML("VendorDisabled");
                } else {
                    /*Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, "LiveDisabled");
                    return resformat.vendErrorXML("LiveDisabled");
                } */
                    //LIVE
                    if (postpaid.equals("yes")) {
                        if (data.chechIP(vendorcode, req.getRemoteAddr()).equals("found")) {
                            if (fbal > 0 && parseDouble(amount) < fbal) {
                                String ipayres = this.req._getPostPaidDetailsAcc(account, "PdslEquity", "00001");
                                if (ipayres.startsWith("Owner")) {
                                    String ipayres2 = "ServiceNotAvailable";
                                    try {
                                        String phone = phonenumber;
                                        if (phonenumber == null || phonenumber.isEmpty() || phonenumber.equals("?") || phonenumber.equals("")) {
                                            phone = "";
                                        }
                                        ipayres2 = this.req._payPostpaidBill(vendorcode, phone, account, amount, "PdslEquity", "00001");
                                    } catch (Exception ex) {
                                        Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, "Error Vending Postpaid Request" + ex);
                                    }
                                    if (ipayres2.startsWith("ServiceNotAvailable")) {
                                        Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, ipayres2);
                                        return resformat.vendErrorXML(ipayres2);
                                    } else if (ipayres2.startsWith("Unsuccessfulmsg:")) {
                                        Logger.getLogger(Requests.class.getName()).log(Level.INFO, ipayres2.replaceAll("Unsuccessfulmsg:", ""));
                                        return resformat.vendErrorXML(ipayres2.replaceAll("Unsuccessfulmsg:", ""));
                                    } else {
                                        String resarr[] = ipayres2.split(":");
                                        String rctNum = resarr[0];
                                        Logger.getLogger(Requests.class.getName()).log(Level.INFO, "Paid:" + amount + " For Account:" + account + " Reciept:" + rctNum);
                                        return resformat.vendXML(account, resarr[1], "Paid:" + amount + " For Account:" + account + " Reciept:" + rctNum, data.getFloatBal(vendorcode));
                                    }
                                } else {
                                    Logger.getLogger(Requests.class.getName()).log(Level.INFO, ipayres);
                                    return resformat.vendErrorXML(ipayres);
                                }
                            } else {
                                Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, "OutofFloat");
                                return resformat.vendErrorXML("OutofFloat");
                            }
                        } else {
                            Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, "IpNotWhitelisted");
                            return resformat.vendErrorXML("IpNotWhitelisted");
                        }
                    } else {
                        Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, "PostPaidVendDisabled");
                        return resformat.vendErrorXML("PostPaidVendDisabled");
                    }
                }
            } else {
                Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, "InvalidVendorCode");
                return resformat.vendErrorXML("InvalidVendorCode");
            }
        }
    }

    @WebMethod(operationName = "postpaidAccountQuery")
    public VendResponse postpaidAccountQuery(@WebParam(name = "vendorcode") String vendorcode, @WebParam(name = "account") String account) {
        Logger.getLogger(Requests.class.getName()).log(Level.INFO, "\n >>PostPaidAccountQuery<< \n VendorCode: " + vendorcode + "\n Account: " + account);
        FormatVendRes resformat = new FormatVendRes();

        MessageContext mc = wsContext.getMessageContext();
        HttpServletRequest req = (HttpServletRequest) mc.get(MessageContext.SERVLET_REQUEST);
        System.out.println("Client IP = " + req.getRemoteAddr());

        if (vendorcode == null || vendorcode.isEmpty() || vendorcode.equals("?") || vendorcode.equals("")) {
            Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, "EmptyVendorCode");
            return resformat.vendErrorXML("EmptyVendorCode");
        } else if (account == null || account.isEmpty() || account.equals("?") || account.equals("")) {
            Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, "EmptyAccount");
            return resformat.vendErrorXML("EmptyAccount");
        } else {
            if (data.chechVendor(vendorcode).equals("found")) {
                String prepaid = "";
                String postpaid = "";
                String airtime = "";
                String mode = "";
                String floatbal = "";
                try {
                    Connection con = data.connect();
                    String query = "SELECT `vendor_code`,`prepaid`,`postpaid`,`airtime`,`status` FROM `vendors` WHERE  `vendor_code`=" + vendorcode + "";
                    //System.out.println(query);
                    Statement stm = con.createStatement();
                    ResultSet rs = stm.executeQuery(query);
                    while (rs.next()) {
                        prepaid = rs.getString(2);
                        postpaid = rs.getString(3);
                        airtime = rs.getString(4);
                        mode = rs.getString(5);
                    }
                    con.close();
                } catch (SQLException ex) {

                }
                floatbal = data.getFloatBal(vendorcode);
                double fbal = parseDouble(floatbal);

                if (mode.equals("test")) {
                    Logger.getLogger(Requests.class.getName()).log(Level.INFO, "Account " + account + " Owner John Doe Balance 3000" + " DueDate 2017-10-02");
                    return resformat.vendQueryXML("Account " + account + " Owner John Doe Balance 3000" + " DueDate 2017-10-02");
                } else if (mode.equals("disabled")) {
                    Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, "VendorDisabled");
                    return resformat.vendErrorXML("VendorDisabled");
                } else {
                    /*Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, "LiveDisabled");
                    return resformat.vendErrorXML("LiveDisabled");
                } */
                    //LIVE
                    if (postpaid.equals("yes")) {
                        if (data.chechIP(vendorcode, req.getRemoteAddr()).equals("found")) {
                            //!!!DONT FORGET ServiceNotAvailable
                            if (fbal > 0) {
                                String ipayres = this.req._getPostPaidDetailsAcc(account, "PdslEquity", "00001");
                                if (ipayres.startsWith("Owner")) {
                                    Logger.getLogger(Requests.class.getName()).log(Level.INFO, "Account " + account + " " + ipayres);
                                    return resformat.vendQueryXML("Account:" + account + " " + ipayres);
                                } else {
                                    Logger.getLogger(Requests.class.getName()).log(Level.INFO, ipayres);
                                    return resformat.vendErrorXML(ipayres);
                                }
                            } else {
                                Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, "OutofFloat");
                                return resformat.vendErrorXML("OutofFloat");
                            }
                        } else {
                            Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, "IpNotWhitelisted");
                            return resformat.vendErrorXML("IpNotWhitelisted");
                        }
                    } else {
                        Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, "PostPaidVendDisabled");
                        return resformat.vendErrorXML("PostPaidVendDisabled");
                    }
                }
            } else {
                Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, "InvalidVendorCode");
                return resformat.vendErrorXML("InvalidVendorCode");
            }
        }
    }

    @WebMethod(operationName = "airtimeVend")
    public VendResponse airtimeVend(@WebParam(name = "vendorcode") String vendorcode,
            @WebParam(name = "tokentype") String tokentype,
            @WebParam(name = "amount") String amount
    ) {
        Logger.getLogger(Requests.class.getName()).log(Level.INFO, "\n >>AirtimeVend<< \n VendorCode: " + vendorcode + "\n TokenType: " + tokentype + "\n Amount: " + amount);
        FormatVendRes resformat = new FormatVendRes();

        MessageContext mc = wsContext.getMessageContext();
        HttpServletRequest req = (HttpServletRequest) mc.get(MessageContext.SERVLET_REQUEST);
        System.out.println("Client IP = " + req.getRemoteAddr());

        //check float
        //eg tokentype orange_20
        if (vendorcode == null || vendorcode.isEmpty() || vendorcode.equals("?") || vendorcode.equals("")) {
            Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, "EmptyVendorCode");
            return resformat.vendErrorXML("EmptyVendorCode");
        } else if (tokentype == null || tokentype.isEmpty() || tokentype.equals("?") || tokentype.equals("")) {
            Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, "EmptyTokenType");
            return resformat.vendErrorXML("EmptyTokenType");
        } else if (amount == null || amount.isEmpty() || amount.equals("?") || amount.equals("")) {
            Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, "EmptyAmount");
            return resformat.vendErrorXML("EmptyAmount");
        } else {
            if (data.chechVendor(vendorcode).equals("found")) {
                String prepaid = "";
                String postpaid = "";
                String airtime = "";
                String mode = "";
                String floatbal = "";
                try {
                    Connection con = data.connect();
                    String query = "SELECT `vendor_code`,`prepaid`,`postpaid`,`airtime`,`status` FROM `vendors` WHERE  `vendor_code`=" + vendorcode + "";
                    //System.out.println(query);
                    Statement stm = con.createStatement();
                    ResultSet rs = stm.executeQuery(query);
                    while (rs.next()) {
                        prepaid = rs.getString(2);
                        postpaid = rs.getString(3);
                        airtime = rs.getString(4);
                        mode = rs.getString(5);
                    }
                    con.close();
                } catch (SQLException ex) {

                }
                floatbal = data.getFloatBal(vendorcode);
                double fbal = parseDouble(floatbal);

                if (mode.equals("test")) {
                    Logger.getLogger(Requests.class.getName()).log(Level.INFO, "Token:6787543234567890");
                    return resformat.vendXML(tokentype, "468576755990", "Token:6787543234567890", "1000");
                } else if (mode.equals("disabled")) {
                    Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, "VendorDisabled");
                    return resformat.vendErrorXML("VendorDisabled");
                } else {
                    /*Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, "LiveDisabled");
                    return resformat.vendErrorXML("LiveDisabled");
                } */
                    //LIVE
                    if (postpaid.equals("yes")) {
                        if (data.chechIP(vendorcode, req.getRemoteAddr()).equals("found")) {
                            String ttsplit[] = tokentype.split("_");
                            if (fbal > 0 && parseDouble(ttsplit[1]) < fbal) {
                                //!!!DONT FORGET ServiceNotAvailable
                                //INSERT TO DB
                                String ipayres = this.req._getPrePaidDetailsAcc("14140057598", "PdslEquity", "00001");
                                ipayres = ipayres.replace("[", "");
                                ipayres = ipayres.replace("]", "");
                                String account = "14140057598";
                                if (tokentype.startsWith("saf")) {
                                    account = "safaricom";
                                } else if (tokentype.startsWith("air")) {
                                    account = "airtel";
                                } else {
                                    account = "Orange";
                                }
                                if (ipayres.startsWith("Owner")) {
                                    String ipayres2 = "ServiceNotAvailable";
                                    try {
                                        ipayres2 = this.req._airTimeTokenBuy(vendorcode, "254728064120", account, tokentype, "PdslEquity", "00001");
                                    } catch (Exception ex) {
                                        Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, "Error Vending Airtime Request" + ex);
                                    }

                                    if (ipayres2.startsWith("ServiceNotAvailable")) {
                                        Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, ipayres2);
                                        return resformat.vendErrorXML(ipayres2);
                                    } else if (ipayres2.startsWith("Unsuccessfulmsg:")) {
                                        Logger.getLogger(Requests.class.getName()).log(Level.INFO, ipayres2.replaceAll("Unsuccessfulmsg:", ""));
                                        return resformat.vendErrorXML(ipayres2.replaceAll("Unsuccessfulmsg:", ""));
                                    } else {
                                        String resarr[] = ipayres2.split(":");
                                        String rctNum = resarr[0];
                                        Logger.getLogger(Requests.class.getName()).log(Level.INFO, "Token:" + resarr[0] + " Units:" + resarr[1] + "");
                                        return resformat.vendXML(account, resarr[2], "Token:" + resarr[0] + " Units:" + resarr[1] + "", data.getFloatBal(vendorcode));
                                    }
                                } else {
                                    Logger.getLogger(Requests.class.getName()).log(Level.INFO, ipayres);
                                    return resformat.vendErrorXML(ipayres);
                                }
                            } else {
                                Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, "OutofFloat");
                                return resformat.vendErrorXML("OutofFloat");
                            }
                        } else {
                            Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, "IpNotWhitelisted");
                            return resformat.vendErrorXML("IpNotWhitelisted");
                        }
                    } else {
                        Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, "AirtimeVendDisabled");
                        return resformat.vendErrorXML("AirtimeVendDisabled");
                    }
                }
            } else {
                Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, "InvalidVendorCode");
                return resformat.vendErrorXML("InvalidVendorCode");
            }
        }
    }

}
