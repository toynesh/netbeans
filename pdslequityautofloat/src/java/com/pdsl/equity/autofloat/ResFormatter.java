/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.equity.autofloat;

/**
 *
 * @author coolie
 */
public class ResFormatter {
    public ResMan CValidationXML(String amount, String billNumber, String billerCode, String createdOn, String status, String currencyCode, String customerName, String customerRefNumber, String type) {
        ResMan res = new ResMan();
        res.setAmount(amount);
        res.setBillNumber(billNumber);
        res.setBillerCode(billerCode);
        res.setCreatedOn(createdOn);
        res.setStatus(status);
        res.setCurrencyCode(currencyCode);
        res.setCustomerName(customerName);
        res.setCustomerRefNumber(customerRefNumber);
        res.setType(type);
        return res;
    }
    public ResMan DefaultXML(String responseCode, String responseMessage){
        ResMan res = new ResMan();
        res.setResponseCode(responseCode);
        res.setResponseMessage(responseMessage);
        return res;
    }
}
