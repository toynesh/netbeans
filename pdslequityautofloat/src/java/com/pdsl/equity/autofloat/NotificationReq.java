/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.equity.autofloat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author coolie
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NotificationReq", propOrder = {
    "billNumber",
    "billAmount",
    "customerRefNumber",
    "bankreference",
    "paymentMode",
    "transactionDate",
    "phonenumber",
    "debitaccount",
    "debitcustname",
    "username",
    "password"
})
public class NotificationReq {
    protected String billNumber;
    protected String billAmount;
    protected String customerRefNumber;
    protected String bankreference;
    protected String paymentMode;
    protected String transactionDate;
    protected String phonenumber;
    protected String debitaccount;
    protected String debitcustname;
    protected String username;
    protected String password;
    
    public String getBillNumber() {
        return billNumber;
    }
    public void setBillNumber(String value) {
        this.billNumber = value;
    }
    
    public String getBillAmount() {
        return billAmount;
    }
    public void setBillAmount(String value) {
        this.billAmount = value;
    }
    
    public String getCustomerRefNumber() {
        return customerRefNumber;
    }
    public void setCustomerRefNumber(String value) {
        this.customerRefNumber = value;
    }
    
    public String getBankreference() {
        return bankreference;
    }
    public void setBankreference(String value) {
        this.bankreference = value;
    }
    
    public String getPaymentMode() {
        return paymentMode;
    }
    public void setPaymentMode(String value) {
        this.paymentMode = value;
    }
    
    public String getTransactionDate() {
        return transactionDate;
    }
    public void setTransactionDate(String value) {
        this.transactionDate = value;
    }
    
    public String getPhoneNumber() {
        return phonenumber;
    }
    public void setPhoneNumber(String value) {
        this.phonenumber = value;
    }
    
    public String getDebitAccount() {
        return debitaccount;
    }
    public void setDebitAccount(String value) {
        this.debitaccount = value;
    }
    
    public String getDebitCustName() {
        return debitcustname;
    }
    public void setDebitCustName(String value) {
        this.debitcustname = value;
    }
    
    public String getUserName() {
        return username;
    }
    public void setUserName(String value) {
        this.username = value;
    }
    
    public String getPassword() {
        return password;
    }
    public void setPassword(String value) {
        this.password = value;
    }
}
