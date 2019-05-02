
package com.vend.manualvends;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.vend.manualvends package. 
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

    private final static QName _CallAllcodesResponse_QNAME = new QName("http://manualvends.vend.com/", "callAllcodesResponse");
    private final static QName _CallAllcodes_QNAME = new QName("http://manualvends.vend.com/", "callAllcodes");
    private final static QName _Querymanual_QNAME = new QName("http://manualvends.vend.com/", "querymanual");
    private final static QName _QuerymanualResponse_QNAME = new QName("http://manualvends.vend.com/", "querymanualResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.vend.manualvends
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CallAllcodesResponse }
     * 
     */
    public CallAllcodesResponse createCallAllcodesResponse() {
        return new CallAllcodesResponse();
    }

    /**
     * Create an instance of {@link CallAllcodes }
     * 
     */
    public CallAllcodes createCallAllcodes() {
        return new CallAllcodes();
    }

    /**
     * Create an instance of {@link Querymanual }
     * 
     */
    public Querymanual createQuerymanual() {
        return new Querymanual();
    }

    /**
     * Create an instance of {@link QuerymanualResponse }
     * 
     */
    public QuerymanualResponse createQuerymanualResponse() {
        return new QuerymanualResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CallAllcodesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://manualvends.vend.com/", name = "callAllcodesResponse")
    public JAXBElement<CallAllcodesResponse> createCallAllcodesResponse(CallAllcodesResponse value) {
        return new JAXBElement<CallAllcodesResponse>(_CallAllcodesResponse_QNAME, CallAllcodesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CallAllcodes }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://manualvends.vend.com/", name = "callAllcodes")
    public JAXBElement<CallAllcodes> createCallAllcodes(CallAllcodes value) {
        return new JAXBElement<CallAllcodes>(_CallAllcodes_QNAME, CallAllcodes.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Querymanual }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://manualvends.vend.com/", name = "querymanual")
    public JAXBElement<Querymanual> createQuerymanual(Querymanual value) {
        return new JAXBElement<Querymanual>(_Querymanual_QNAME, Querymanual.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QuerymanualResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://manualvends.vend.com/", name = "querymanualResponse")
    public JAXBElement<QuerymanualResponse> createQuerymanualResponse(QuerymanualResponse value) {
        return new JAXBElement<QuerymanualResponse>(_QuerymanualResponse_QNAME, QuerymanualResponse.class, null, value);
    }

}
