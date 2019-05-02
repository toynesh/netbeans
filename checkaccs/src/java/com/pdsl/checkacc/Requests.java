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
 * @author coolie
 */
@WebService(serviceName = "Requests")
public class Requests {

    /**
     * This is a sample web service operation
     */
    ProcessRequest req = new ProcessRequest();

    @WebMethod(operationName = "checkAccount")
    public String _checkAccount(@WebParam(name = "account") String account) {
        String response = null;
        if (account.length() < 11) {
            response = this.req._getPostPaidDetailsAcc(account, "PdslAirtel", "00001");
        } else {
            response = this.req._getPrePaidDetailsAcc(account, "PdslAirtel", "00001");
        }
        System.out.println("Response:-" + response);
        return response;
    }
}
