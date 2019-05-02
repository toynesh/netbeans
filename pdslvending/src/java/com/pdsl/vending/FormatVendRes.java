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
public class FormatVendRes {
    public VendResponse vendXML(String account, String ref, String stdmsg, String floatbalance) {
        VendResponse res = new VendResponse();
        res.setAccount(account);
        res.setVendRef(ref);
        res.setStdMsg(stdmsg);
        res.setFloatBalance(floatbalance);
        return res;
    }
    public VendResponse vendErrorXML(String pdslerror) {
        VendResponse res = new VendResponse();
        res.setPdslError(pdslerror);
        return res;
    }
    public VendResponse vendQueryXML(String queryres) {
        VendResponse res = new VendResponse();
        res.setQueryRes(queryres);
        return res;
    }

}
