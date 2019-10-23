/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.bulkvend;

/**
 *
 * @author julius
 */
public class Sell {

    String vcode = "9942";

    public String vendPostpaid(String meter, String amt, String phn) {
        String resp = "FAIL";
        String pdslerr = "";
        try {
            com.pdsl.vending.Requests_Service service = new com.pdsl.vending.Requests_Service();
            com.pdsl.vending.Requests port = service.getRequestsPort();
            // TODO initialize WS operation arguments here
            java.lang.String vendorcode = vcode;
            java.lang.String account = meter;
            java.lang.String amount = amt;
            java.lang.String phonenumber = phn;
            // TODO process result here
            com.pdsl.vending.VendResponse result = port.postpaidVend(vendorcode, account, amount, phonenumber);
            resp = result.getStdMsg();
            pdslerr = result.getPdslError();
            System.out.println("Result = " + resp);
            System.out.println("PDSLErr = " + pdslerr);
        } catch (Exception ex) {
            // TODO handle custom exceptions here
        }
        if (null != pdslerr) {
            if (pdslerr.equals("OutofFloat")) {
                //OtherInc oinc = new OtherInc();
                //String floatres = oinc.autoFloat(phn);
                //Logger.getLogger(Sell.class.getName()).log(Level.INFO, "Float Res: " + floatres);
                resp = "FAIL";
            } else if (pdslerr.contains("Unknown meter") || pdslerr.contains("Unregistered Meter") || pdslerr.contains("meter has accumulated") || pdslerr.contains("is blocked") || pdslerr.contains("Incorrect Meter Number")) {
                if (pdslerr.contains("meter has accumulated")) {
                    resp = "FAILED | " + pdslerr;
                } else if (pdslerr.contains("is blocked")) {
                    resp = "FAILED | " + pdslerr;
                } else {
                    resp = "FAILED |THE KPLC ACCOUNT / METER NUMBER " + meter + " IS NOT CORRECT. Please check the number and re-try again, or contact support for assistance at 0709711000.";
                }
            } else {
                resp = "FAIL";
            }
        }
        return resp;
    }

    public String vendPrepaid(String meter, String amt) {
        String resp = "FAIL";
        String pdslerr = "";
        try {
            com.pdsl.vending.Requests_Service service = new com.pdsl.vending.Requests_Service();
            com.pdsl.vending.Requests port = service.getRequestsPort();
            // TODO initialize WS operation arguments here
            java.lang.String vendorcode = vcode;
            java.lang.String account = meter;
            java.lang.String amount = amt;
            // TODO process result here
            com.pdsl.vending.VendResponse result = port.prepaidVend(vendorcode, account, amount);
            resp = result.getStdMsg();
            pdslerr = result.getPdslError();
            System.out.println("Result = " + resp);
            System.out.println("PDSLErr = " + pdslerr);
        } catch (Exception ex) {
            // TODO handle custom exceptions here
        }
        if (null != pdslerr) {
            if (pdslerr.equals("OutofFloat")) {
                //OtherInc oinc = new OtherInc();
                //String floatres = oinc.autoFloat(phn);
                //Logger.getLogger(Sell.class.getName()).log(Level.INFO, "Float Res: " + floatres);
                resp = "FAIL";
            } else if (pdslerr.contains("Unknown meter") || pdslerr.contains("Unregistered Meter") || pdslerr.contains("meter has accumulated") || pdslerr.contains("is blocked") || pdslerr.contains("Incorrect Meter Number")) {
                if (pdslerr.contains("meter has accumulated")) {
                    resp = "FAILED | " + pdslerr;
                } else if (pdslerr.contains("is blocked")) {
                    resp = "FAILED | " + pdslerr;
                } else {
                    resp = "FAILED |THE KPLC ACCOUNT / METER NUMBER " + meter + " IS NOT CORRECT. Please check the number and re-try again, or contact support for assistance at 0709711000.";
                }
            } else {
                resp = "FAIL";
            }
        }

        return resp;
    }

    public String vendAirtime(String meter, String amt) {
        String resp = "FAIL";
        String pdslerr = "";
        try {
            com.pdsl.vending.Requests_Service service = new com.pdsl.vending.Requests_Service();
            com.pdsl.vending.Requests port = service.getRequestsPort();
            // TODO initialize WS operation arguments here
            java.lang.String vendorcode = vcode;
            java.lang.String tokentype = meter;
            java.lang.String amount = amt;
            // TODO process result here
            com.pdsl.vending.VendResponse result = port.airtimeVend(vendorcode, tokentype, amount);
            resp = result.getStdMsg();
            pdslerr = result.getPdslError();
            System.out.println("Result = " + resp);
            System.out.println("PDSLErr = " + pdslerr);
        } catch (Exception ex) {
            // TODO handle custom exceptions here
        }
        if (null != pdslerr) {
            if (pdslerr.equals("OutofFloat")) {
                //OtherInc oinc = new OtherInc();
                //String floatres = oinc.autoFloat(phn);
                //Logger.getLogger(Sell.class.getName()).log(Level.INFO, "Float Res: " + floatres);
                resp = "FAIL";
            } else if (pdslerr.contains("Unknown meter") || pdslerr.contains("Unregistered Meter") || pdslerr.contains("meter has accumulated") || pdslerr.contains("is blocked") || pdslerr.contains("Incorrect Meter Number")) {
                resp = "FAILED | " + pdslerr;
                /*if (pdslerr.contains("meter has accumulated")) {
                    resp = "FAILED | " + pdslerr;
                } else if (pdslerr.contains("is blocked")) {
                    resp = "FAILED | " + pdslerr;
                } else {
                    resp = "FAILED |THE KPLC ACCOUNT / METER NUMBER " + meter + " IS NOT CORRECT. Please check the number and re-try again, or contact support for assistance at 0709711000.";
                }*/
            } else {
                resp = "FAIL";
            }
        }

        return resp;
    }
}
