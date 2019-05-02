/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gmalto.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author coolie
 */
@WebService(serviceName = "CurrentParams")
public class CurrentParams {

    /**
     * This is a sample web service operation
     
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }*/
    @WebMethod(operationName = "Params")
    public String Params(@WebParam(name = "getem") String getem) {
        String response = "none";
        try {
            FileReader reader = new FileReader("params.txt");
            BufferedReader bufferedReader = new BufferedReader(reader);
            response = bufferedReader.readLine();
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
