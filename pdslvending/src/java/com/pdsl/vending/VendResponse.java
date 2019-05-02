/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.vending;

/**
 *
 * @author coolie
 */
public class VendResponse {

    private String account;
    private String vendref;
    private String stdmsg;
    private String floatbalance;
    private String pdslerror;
    private String queryres;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getVendRef() {
        return vendref;
    }

    public void setVendRef(String vendref) {
        this.vendref = vendref;
    }

    public String getStdMsg() {
        return stdmsg;
    }

    public void setStdMsg(String stdmsg) {
        this.stdmsg = stdmsg;
    }

    public String getFloatBalance() {
        return floatbalance;
    }

    public void setFloatBalance(String floatbalance) {
        this.floatbalance = floatbalance;
    }
    public String getPdslError() {
        return pdslerror;
    }

    public void setPdslError(String pdslerror) {
        this.pdslerror = pdslerror;
    }
    public String getQueryRes() {
        return queryres;
    }
    public void setQueryRes(String queryres) {
        this.queryres = queryres;
    }
}
