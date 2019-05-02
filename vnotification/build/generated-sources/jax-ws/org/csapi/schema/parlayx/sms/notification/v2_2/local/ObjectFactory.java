
package org.csapi.schema.parlayx.sms.notification.v2_2.local;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.csapi.schema.parlayx.sms.notification.v2_2.local package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _NotifySmsReception_QNAME = new QName("http://www.csapi.org/schema/parlayx/sms/notification/v2_2/local", "notifySmsReception");
    private final static QName _NotifySmsReceptionResponse_QNAME = new QName("http://www.csapi.org/schema/parlayx/sms/notification/v2_2/local", "notifySmsReceptionResponse");
    private final static QName _NotifySmsDeliveryReceipt_QNAME = new QName("http://www.csapi.org/schema/parlayx/sms/notification/v2_2/local", "notifySmsDeliveryReceipt");
    private final static QName _NotifySmsDeliveryReceiptResponse_QNAME = new QName("http://www.csapi.org/schema/parlayx/sms/notification/v2_2/local", "notifySmsDeliveryReceiptResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.csapi.schema.parlayx.sms.notification.v2_2.local
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link NotifySmsReception }
     * 
     */
    public NotifySmsReception createNotifySmsReception() {
        return new NotifySmsReception();
    }

    /**
     * Create an instance of {@link NotifySmsReceptionResponse }
     * 
     */
    public NotifySmsReceptionResponse createNotifySmsReceptionResponse() {
        return new NotifySmsReceptionResponse();
    }

    /**
     * Create an instance of {@link NotifySmsDeliveryReceipt }
     * 
     */
    public NotifySmsDeliveryReceipt createNotifySmsDeliveryReceipt() {
        return new NotifySmsDeliveryReceipt();
    }

    /**
     * Create an instance of {@link NotifySmsDeliveryReceiptResponse }
     * 
     */
    public NotifySmsDeliveryReceiptResponse createNotifySmsDeliveryReceiptResponse() {
        return new NotifySmsDeliveryReceiptResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NotifySmsReception }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.csapi.org/schema/parlayx/sms/notification/v2_2/local", name = "notifySmsReception")
    public JAXBElement<NotifySmsReception> createNotifySmsReception(NotifySmsReception value) {
        return new JAXBElement<NotifySmsReception>(_NotifySmsReception_QNAME, NotifySmsReception.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NotifySmsReceptionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.csapi.org/schema/parlayx/sms/notification/v2_2/local", name = "notifySmsReceptionResponse")
    public JAXBElement<NotifySmsReceptionResponse> createNotifySmsReceptionResponse(NotifySmsReceptionResponse value) {
        return new JAXBElement<NotifySmsReceptionResponse>(_NotifySmsReceptionResponse_QNAME, NotifySmsReceptionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NotifySmsDeliveryReceipt }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.csapi.org/schema/parlayx/sms/notification/v2_2/local", name = "notifySmsDeliveryReceipt")
    public JAXBElement<NotifySmsDeliveryReceipt> createNotifySmsDeliveryReceipt(NotifySmsDeliveryReceipt value) {
        return new JAXBElement<NotifySmsDeliveryReceipt>(_NotifySmsDeliveryReceipt_QNAME, NotifySmsDeliveryReceipt.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NotifySmsDeliveryReceiptResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.csapi.org/schema/parlayx/sms/notification/v2_2/local", name = "notifySmsDeliveryReceiptResponse")
    public JAXBElement<NotifySmsDeliveryReceiptResponse> createNotifySmsDeliveryReceiptResponse(NotifySmsDeliveryReceiptResponse value) {
        return new JAXBElement<NotifySmsDeliveryReceiptResponse>(_NotifySmsDeliveryReceiptResponse_QNAME, NotifySmsDeliveryReceiptResponse.class, null, value);
    }

}
