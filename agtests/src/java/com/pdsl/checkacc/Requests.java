/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.checkacc;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author juliuskun
 */
@WebService(serviceName = "Requests")
public class Requests {

    /**
     * This is a sample web service operation
     *
     * @WebMethod(operationName = "hello") public String hello(@WebParam(name =
     * "name") String txt) { return "Hello " + txt + " !";
    }
     */
    ProcessRequest req = new ProcessRequest();

    @WebMethod(operationName = "checkAccount")
    public String _checkAccount(@WebParam(name = "account") String account) {
        String response = null;
        if (account.length()<11) {
            //response = this.req._getPostPaidDetailsAcc(account, "EquityBank", "00001");//test
            response = this.req._getPostPaidDetailsAcc(account, "Cellulant", "00001");
        } else {
            //response = this.req._getPrePaidDetailsAcc(account, "EquityBank", "00001");//test
            response = this.req._getPrePaidDetailsAcc(account, "Cellulant", "00001");
        }
        System.out.println("Response:-" + response);
        return response;
    }
    public static void main(String[] args){
        Requests req = new Requests();
        req._checkAccount("22327205");
    }
}
