
package com.edservice.SE;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.edservice.SE package. 
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

    private final static QName _NotifyPayment_QNAME = new QName("http://SE.edservice.com/", "notifyPayment");
    private final static QName _NotifyPaymentResponse_QNAME = new QName("http://SE.edservice.com/", "notifyPaymentResponse");
    private final static QName _QueryBill_QNAME = new QName("http://SE.edservice.com/", "queryBill");
    private final static QName _QueryBillResponse_QNAME = new QName("http://SE.edservice.com/", "queryBillResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.edservice.SE
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link NotifyPayment }
     * 
     */
    public NotifyPayment createNotifyPayment() {
        return new NotifyPayment();
    }

    /**
     * Create an instance of {@link NotifyPaymentResponse }
     * 
     */
    public NotifyPaymentResponse createNotifyPaymentResponse() {
        return new NotifyPaymentResponse();
    }

    /**
     * Create an instance of {@link QueryBill }
     * 
     */
    public QueryBill createQueryBill() {
        return new QueryBill();
    }

    /**
     * Create an instance of {@link QueryBillResponse }
     * 
     */
    public QueryBillResponse createQueryBillResponse() {
        return new QueryBillResponse();
    }

    /**
     * Create an instance of {@link BillQueryRequest }
     * 
     */
    public BillQueryRequest createBillQueryRequest() {
        return new BillQueryRequest();
    }

    /**
     * Create an instance of {@link BillQueryResponse }
     * 
     */
    public BillQueryResponse createBillQueryResponse() {
        return new BillQueryResponse();
    }

    /**
     * Create an instance of {@link DataSet }
     * 
     */
    public DataSet createDataSet() {
        return new DataSet();
    }

    /**
     * Create an instance of {@link PaymentNotificationRequest }
     * 
     */
    public PaymentNotificationRequest createPaymentNotificationRequest() {
        return new PaymentNotificationRequest();
    }

    /**
     * Create an instance of {@link PaymentNotificationResponse }
     * 
     */
    public PaymentNotificationResponse createPaymentNotificationResponse() {
        return new PaymentNotificationResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NotifyPayment }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://SE.edservice.com/", name = "notifyPayment")
    public JAXBElement<NotifyPayment> createNotifyPayment(NotifyPayment value) {
        return new JAXBElement<NotifyPayment>(_NotifyPayment_QNAME, NotifyPayment.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NotifyPaymentResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://SE.edservice.com/", name = "notifyPaymentResponse")
    public JAXBElement<NotifyPaymentResponse> createNotifyPaymentResponse(NotifyPaymentResponse value) {
        return new JAXBElement<NotifyPaymentResponse>(_NotifyPaymentResponse_QNAME, NotifyPaymentResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryBill }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://SE.edservice.com/", name = "queryBill")
    public JAXBElement<QueryBill> createQueryBill(QueryBill value) {
        return new JAXBElement<QueryBill>(_QueryBill_QNAME, QueryBill.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryBillResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://SE.edservice.com/", name = "queryBillResponse")
    public JAXBElement<QueryBillResponse> createQueryBillResponse(QueryBillResponse value) {
        return new JAXBElement<QueryBillResponse>(_QueryBillResponse_QNAME, QueryBillResponse.class, null, value);
    }

}
