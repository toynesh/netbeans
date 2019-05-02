
/**
 * ServiceException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4  Built on : Apr 26, 2008 (06:24:30 EDT)
 */

package org.csapi.www.wsdl.parlayx.sms.receive.v2_2.service;

public class ServiceException extends java.lang.Exception{
    
    private org.csapi.www.wsdl.parlayx.sms.receive.v2_2.service.ReceiveSmsServiceStub.ServiceExceptionE faultMessage;
    
    public ServiceException() {
        super("ServiceException");
    }
           
    public ServiceException(java.lang.String s) {
       super(s);
    }
    
    public ServiceException(java.lang.String s, java.lang.Throwable ex) {
      super(s, ex);
    }
    
    public void setFaultMessage(org.csapi.www.wsdl.parlayx.sms.receive.v2_2.service.ReceiveSmsServiceStub.ServiceExceptionE msg){
       faultMessage = msg;
    }
    
    public org.csapi.www.wsdl.parlayx.sms.receive.v2_2.service.ReceiveSmsServiceStub.ServiceExceptionE getFaultMessage(){
       return faultMessage;
    }
}
    