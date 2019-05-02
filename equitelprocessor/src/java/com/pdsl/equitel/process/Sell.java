/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.equitel.process;

import com.pdsl.vending.VendResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.SOAPMessage;

/**
 *
 * @author coolie
 */
public class Sell extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected String processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String resp = "FAIL";
        String pdslerr = "";
        try {
            /* TODO output your page here. You may use following sample code. */
            //http://172.27.116.36:8084/equitelprocessor/Sell?meter=2232720-01&amt=1&phn=254728064120
            String vcode = "900620";
            String meter = request.getParameter("meter");
            String amt = request.getParameter("amt");
            String phn = "";
            if (null != request.getParameter("phn")) {
                phn = request.getParameter("phn");
            }
            if (meter.length() < 11) {
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
                    } else if (pdslerr.contains("Unknown meter") || pdslerr.contains("Unregistered Meter") || pdslerr.contains("meter has accumulated") || pdslerr.contains("is blocked")|| pdslerr.contains("Incorrect Meter Number")) {
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

            } else {
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
                    } else if (pdslerr.contains("Unknown meter") || pdslerr.contains("Unregistered Meter") || pdslerr.contains("meter has accumulated") || pdslerr.contains("is blocked")|| pdslerr.contains("Incorrect Meter Number")) {
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
            }
        } catch (Exception ex) {
            Logger.getLogger(Sell.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resp;
    }

    public static void main(String arg[]) {
        try {
            /*try {
            com.pdsl.vending.Requests_Service service = new com.pdsl.vending.Requests_Service();
            com.pdsl.vending.Requests port = service.getRequestsPort();
            // TODO initialize WS operation arguments here
            java.lang.String vendorcode = "900620";
            java.lang.String account = "33";
            java.lang.String amount = "1";
            java.lang.String phonenumber = "254728064120";
            // TODO process result here queryRes
            VendResponse result = port.postpaidVend(vendorcode, account, amount, phonenumber);
            String vendres = result.getStdMsg();
            System.out.println("Result = " + vendres);
            
            } catch (Exception ex) {
            // TODO handle custom exceptions here
            }*/
            OtherInc oinc = new OtherInc();
            String floatres = oinc.autoFloat("123456");
            Logger.getLogger(Sell.class.getName()).log(Level.INFO, "Float Res: " + floatres);
        } catch (Exception ex) {
            Logger.getLogger(Sell.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String r = processRequest(request, response);
        PrintWriter out = response.getWriter();
        out.write(r);
        out.close();
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String r = processRequest(request, response);
        PrintWriter out = response.getWriter();
        out.write(r);
        out.close();
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
