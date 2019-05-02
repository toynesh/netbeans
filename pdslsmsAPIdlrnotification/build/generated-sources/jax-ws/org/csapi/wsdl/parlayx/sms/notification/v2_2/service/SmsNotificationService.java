
package org.csapi.wsdl.parlayx.sms.notification.v2_2.service;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.6-1b01 
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "SmsNotificationService", targetNamespace = "http://www.csapi.org/wsdl/parlayx/sms/notification/v2_2/service", wsdlLocation = "file:/home/coolie/NetBeansProjects/pdslsmsAPIdlrnotification/src/conf/xml-resources/web-services/sms/wsdl/parlayx_sms_notification_service_2_2.wsdl")
public class SmsNotificationService
    extends Service
{

    private final static URL SMSNOTIFICATIONSERVICE_WSDL_LOCATION;
    private final static WebServiceException SMSNOTIFICATIONSERVICE_EXCEPTION;
    private final static QName SMSNOTIFICATIONSERVICE_QNAME = new QName("http://www.csapi.org/wsdl/parlayx/sms/notification/v2_2/service", "SmsNotificationService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("file:/home/coolie/NetBeansProjects/pdslsmsAPIdlrnotification/src/conf/xml-resources/web-services/sms/wsdl/parlayx_sms_notification_service_2_2.wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        SMSNOTIFICATIONSERVICE_WSDL_LOCATION = url;
        SMSNOTIFICATIONSERVICE_EXCEPTION = e;
    }

    public SmsNotificationService() {
        super(__getWsdlLocation(), SMSNOTIFICATIONSERVICE_QNAME);
    }

    public SmsNotificationService(WebServiceFeature... features) {
        super(__getWsdlLocation(), SMSNOTIFICATIONSERVICE_QNAME, features);
    }

    public SmsNotificationService(URL wsdlLocation) {
        super(wsdlLocation, SMSNOTIFICATIONSERVICE_QNAME);
    }

    public SmsNotificationService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, SMSNOTIFICATIONSERVICE_QNAME, features);
    }

    public SmsNotificationService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public SmsNotificationService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns SmsNotification
     */
    @WebEndpoint(name = "SmsNotification")
    public SmsNotification getSmsNotification() {
        return super.getPort(new QName("http://www.csapi.org/wsdl/parlayx/sms/notification/v2_2/service", "SmsNotification"), SmsNotification.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns SmsNotification
     */
    @WebEndpoint(name = "SmsNotification")
    public SmsNotification getSmsNotification(WebServiceFeature... features) {
        return super.getPort(new QName("http://www.csapi.org/wsdl/parlayx/sms/notification/v2_2/service", "SmsNotification"), SmsNotification.class, features);
    }

    private static URL __getWsdlLocation() {
        if (SMSNOTIFICATIONSERVICE_EXCEPTION!= null) {
            throw SMSNOTIFICATIONSERVICE_EXCEPTION;
        }
        return SMSNOTIFICATIONSERVICE_WSDL_LOCATION;
    }

}
