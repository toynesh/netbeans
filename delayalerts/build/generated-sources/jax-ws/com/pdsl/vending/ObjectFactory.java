
package com.pdsl.vending;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.pdsl.vending package. 
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

    private final static QName _PostpaidAccountQueryResponse_QNAME = new QName("http://vending.pdsl.com/", "postpaidAccountQueryResponse");
    private final static QName _PrepaidMeterQueryResponse_QNAME = new QName("http://vending.pdsl.com/", "prepaidMeterQueryResponse");
    private final static QName _AirtimeVend_QNAME = new QName("http://vending.pdsl.com/", "airtimeVend");
    private final static QName _PrepaidVendResponse_QNAME = new QName("http://vending.pdsl.com/", "prepaidVendResponse");
    private final static QName _PostpaidVendResponse_QNAME = new QName("http://vending.pdsl.com/", "postpaidVendResponse");
    private final static QName _PrepaidMeterQuery_QNAME = new QName("http://vending.pdsl.com/", "prepaidMeterQuery");
    private final static QName _PrepaidVend_QNAME = new QName("http://vending.pdsl.com/", "prepaidVend");
    private final static QName _AirtimeVendResponse_QNAME = new QName("http://vending.pdsl.com/", "airtimeVendResponse");
    private final static QName _PostpaidAccountQuery_QNAME = new QName("http://vending.pdsl.com/", "postpaidAccountQuery");
    private final static QName _PostpaidVend_QNAME = new QName("http://vending.pdsl.com/", "postpaidVend");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.pdsl.vending
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PrepaidVendResponse }
     * 
     */
    public PrepaidVendResponse createPrepaidVendResponse() {
        return new PrepaidVendResponse();
    }

    /**
     * Create an instance of {@link AirtimeVend }
     * 
     */
    public AirtimeVend createAirtimeVend() {
        return new AirtimeVend();
    }

    /**
     * Create an instance of {@link PostpaidAccountQueryResponse }
     * 
     */
    public PostpaidAccountQueryResponse createPostpaidAccountQueryResponse() {
        return new PostpaidAccountQueryResponse();
    }

    /**
     * Create an instance of {@link PrepaidMeterQueryResponse }
     * 
     */
    public PrepaidMeterQueryResponse createPrepaidMeterQueryResponse() {
        return new PrepaidMeterQueryResponse();
    }

    /**
     * Create an instance of {@link AirtimeVendResponse }
     * 
     */
    public AirtimeVendResponse createAirtimeVendResponse() {
        return new AirtimeVendResponse();
    }

    /**
     * Create an instance of {@link PostpaidAccountQuery }
     * 
     */
    public PostpaidAccountQuery createPostpaidAccountQuery() {
        return new PostpaidAccountQuery();
    }

    /**
     * Create an instance of {@link PostpaidVend }
     * 
     */
    public PostpaidVend createPostpaidVend() {
        return new PostpaidVend();
    }

    /**
     * Create an instance of {@link PrepaidVend }
     * 
     */
    public PrepaidVend createPrepaidVend() {
        return new PrepaidVend();
    }

    /**
     * Create an instance of {@link PrepaidMeterQuery }
     * 
     */
    public PrepaidMeterQuery createPrepaidMeterQuery() {
        return new PrepaidMeterQuery();
    }

    /**
     * Create an instance of {@link PostpaidVendResponse }
     * 
     */
    public PostpaidVendResponse createPostpaidVendResponse() {
        return new PostpaidVendResponse();
    }

    /**
     * Create an instance of {@link VendResponse }
     * 
     */
    public VendResponse createVendResponse() {
        return new VendResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PostpaidAccountQueryResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://vending.pdsl.com/", name = "postpaidAccountQueryResponse")
    public JAXBElement<PostpaidAccountQueryResponse> createPostpaidAccountQueryResponse(PostpaidAccountQueryResponse value) {
        return new JAXBElement<PostpaidAccountQueryResponse>(_PostpaidAccountQueryResponse_QNAME, PostpaidAccountQueryResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PrepaidMeterQueryResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://vending.pdsl.com/", name = "prepaidMeterQueryResponse")
    public JAXBElement<PrepaidMeterQueryResponse> createPrepaidMeterQueryResponse(PrepaidMeterQueryResponse value) {
        return new JAXBElement<PrepaidMeterQueryResponse>(_PrepaidMeterQueryResponse_QNAME, PrepaidMeterQueryResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AirtimeVend }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://vending.pdsl.com/", name = "airtimeVend")
    public JAXBElement<AirtimeVend> createAirtimeVend(AirtimeVend value) {
        return new JAXBElement<AirtimeVend>(_AirtimeVend_QNAME, AirtimeVend.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PrepaidVendResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://vending.pdsl.com/", name = "prepaidVendResponse")
    public JAXBElement<PrepaidVendResponse> createPrepaidVendResponse(PrepaidVendResponse value) {
        return new JAXBElement<PrepaidVendResponse>(_PrepaidVendResponse_QNAME, PrepaidVendResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PostpaidVendResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://vending.pdsl.com/", name = "postpaidVendResponse")
    public JAXBElement<PostpaidVendResponse> createPostpaidVendResponse(PostpaidVendResponse value) {
        return new JAXBElement<PostpaidVendResponse>(_PostpaidVendResponse_QNAME, PostpaidVendResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PrepaidMeterQuery }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://vending.pdsl.com/", name = "prepaidMeterQuery")
    public JAXBElement<PrepaidMeterQuery> createPrepaidMeterQuery(PrepaidMeterQuery value) {
        return new JAXBElement<PrepaidMeterQuery>(_PrepaidMeterQuery_QNAME, PrepaidMeterQuery.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PrepaidVend }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://vending.pdsl.com/", name = "prepaidVend")
    public JAXBElement<PrepaidVend> createPrepaidVend(PrepaidVend value) {
        return new JAXBElement<PrepaidVend>(_PrepaidVend_QNAME, PrepaidVend.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AirtimeVendResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://vending.pdsl.com/", name = "airtimeVendResponse")
    public JAXBElement<AirtimeVendResponse> createAirtimeVendResponse(AirtimeVendResponse value) {
        return new JAXBElement<AirtimeVendResponse>(_AirtimeVendResponse_QNAME, AirtimeVendResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PostpaidAccountQuery }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://vending.pdsl.com/", name = "postpaidAccountQuery")
    public JAXBElement<PostpaidAccountQuery> createPostpaidAccountQuery(PostpaidAccountQuery value) {
        return new JAXBElement<PostpaidAccountQuery>(_PostpaidAccountQuery_QNAME, PostpaidAccountQuery.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PostpaidVend }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://vending.pdsl.com/", name = "postpaidVend")
    public JAXBElement<PostpaidVend> createPostpaidVend(PostpaidVend value) {
        return new JAXBElement<PostpaidVend>(_PostpaidVend_QNAME, PostpaidVend.class, null, value);
    }

}
