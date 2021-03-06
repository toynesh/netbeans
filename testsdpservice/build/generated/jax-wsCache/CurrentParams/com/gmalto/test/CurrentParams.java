
package com.gmalto.test;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.6-1b01 
 * Generated source version: 2.2
 * 
 */
@WebService(name = "CurrentParams", targetNamespace = "http://test.gmalto.com/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface CurrentParams {


    /**
     * 
     * @param getem
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "Params")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "Params", targetNamespace = "http://test.gmalto.com/", className = "com.gmalto.test.Params")
    @ResponseWrapper(localName = "ParamsResponse", targetNamespace = "http://test.gmalto.com/", className = "com.gmalto.test.ParamsResponse")
    @Action(input = "http://test.gmalto.com/CurrentParams/ParamsRequest", output = "http://test.gmalto.com/CurrentParams/ParamsResponse")
    public String params(
        @WebParam(name = "getem", targetNamespace = "")
        String getem);

}
