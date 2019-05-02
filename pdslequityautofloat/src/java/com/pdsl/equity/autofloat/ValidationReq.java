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
@XmlType(name = "ValidationReq", propOrder = {
    "billNumber",
    "username",
    "password"
})
public class ValidationReq {
    protected String billNumber;
    protected String username;
    protected String password;
    
    public String getBillNumber() {
        return billNumber;
    }
    public void setBillNumber(String value) {
        this.billNumber = value;
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
