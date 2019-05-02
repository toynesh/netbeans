
package com.gmalto.test;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.gmalto.test package. 
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

    private final static QName _ParamsResponse_QNAME = new QName("http://test.gmalto.com/", "ParamsResponse");
    private final static QName _Params_QNAME = new QName("http://test.gmalto.com/", "Params");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.gmalto.test
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Params }
     * 
     */
    public Params createParams() {
        return new Params();
    }

    /**
     * Create an instance of {@link ParamsResponse }
     * 
     */
    public ParamsResponse createParamsResponse() {
        return new ParamsResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ParamsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://test.gmalto.com/", name = "ParamsResponse")
    public JAXBElement<ParamsResponse> createParamsResponse(ParamsResponse value) {
        return new JAXBElement<ParamsResponse>(_ParamsResponse_QNAME, ParamsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Params }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://test.gmalto.com/", name = "Params")
    public JAXBElement<Params> createParams(Params value) {
        return new JAXBElement<Params>(_Params_QNAME, Params.class, null, value);
    }

}
