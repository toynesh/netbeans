/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdlskenya.postpaid;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.jws.WebMethod;

/**
 *
 * @author kariu
 */
@WebService(serviceName = "subseqs")
public class subseqs {

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "sendSubseq")
    public Object sendSubseq() {
        Object response = null;
        SendSubReversals d = new SendSubReversals();
        try {
           response =  d.startsubrev();
        } catch (IOException ex) {
            Logger.getLogger(subseqs.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(subseqs.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }
}
