
package org.csapi.schema.parlayx.data.sync.v1_0.local;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.csapi.schema.parlayx.data.sync.v1_0.local package. 
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

    private final static QName _SyncSubscriptionData_QNAME = new QName("http://www.csapi.org/schema/parlayx/data/sync/v1_0/local", "syncSubscriptionData");
    private final static QName _SyncSubscriptionDataResponse_QNAME = new QName("http://www.csapi.org/schema/parlayx/data/sync/v1_0/local", "syncSubscriptionDataResponse");
    private final static QName _ChangeMSISDN_QNAME = new QName("http://www.csapi.org/schema/parlayx/data/sync/v1_0/local", "changeMSISDN");
    private final static QName _ChangeMSISDNResponse_QNAME = new QName("http://www.csapi.org/schema/parlayx/data/sync/v1_0/local", "changeMSISDNResponse");
    private final static QName _SyncOrderRelation_QNAME = new QName("http://www.csapi.org/schema/parlayx/data/sync/v1_0/local", "syncOrderRelation");
    private final static QName _SyncOrderRelationResponse_QNAME = new QName("http://www.csapi.org/schema/parlayx/data/sync/v1_0/local", "syncOrderRelationResponse");
    private final static QName _SyncMSISDNChange_QNAME = new QName("http://www.csapi.org/schema/parlayx/data/sync/v1_0/local", "syncMSISDNChange");
    private final static QName _SyncMSISDNChangeResponse_QNAME = new QName("http://www.csapi.org/schema/parlayx/data/sync/v1_0/local", "syncMSISDNChangeResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.csapi.schema.parlayx.data.sync.v1_0.local
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SyncSubscriptionData }
     * 
     */
    public SyncSubscriptionData createSyncSubscriptionData() {
        return new SyncSubscriptionData();
    }

    /**
     * Create an instance of {@link SyncSubscriptionDataResponse }
     * 
     */
    public SyncSubscriptionDataResponse createSyncSubscriptionDataResponse() {
        return new SyncSubscriptionDataResponse();
    }

    /**
     * Create an instance of {@link ChangeMSISDN }
     * 
     */
    public ChangeMSISDN createChangeMSISDN() {
        return new ChangeMSISDN();
    }

    /**
     * Create an instance of {@link ChangeMSISDNResponse }
     * 
     */
    public ChangeMSISDNResponse createChangeMSISDNResponse() {
        return new ChangeMSISDNResponse();
    }

    /**
     * Create an instance of {@link SyncOrderRelation }
     * 
     */
    public SyncOrderRelation createSyncOrderRelation() {
        return new SyncOrderRelation();
    }

    /**
     * Create an instance of {@link SyncOrderRelationResponse }
     * 
     */
    public SyncOrderRelationResponse createSyncOrderRelationResponse() {
        return new SyncOrderRelationResponse();
    }

    /**
     * Create an instance of {@link SyncMSISDNChange }
     * 
     */
    public SyncMSISDNChange createSyncMSISDNChange() {
        return new SyncMSISDNChange();
    }

    /**
     * Create an instance of {@link SyncMSISDNChangeResponse }
     * 
     */
    public SyncMSISDNChangeResponse createSyncMSISDNChangeResponse() {
        return new SyncMSISDNChangeResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SyncSubscriptionData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.csapi.org/schema/parlayx/data/sync/v1_0/local", name = "syncSubscriptionData")
    public JAXBElement<SyncSubscriptionData> createSyncSubscriptionData(SyncSubscriptionData value) {
        return new JAXBElement<SyncSubscriptionData>(_SyncSubscriptionData_QNAME, SyncSubscriptionData.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SyncSubscriptionDataResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.csapi.org/schema/parlayx/data/sync/v1_0/local", name = "syncSubscriptionDataResponse")
    public JAXBElement<SyncSubscriptionDataResponse> createSyncSubscriptionDataResponse(SyncSubscriptionDataResponse value) {
        return new JAXBElement<SyncSubscriptionDataResponse>(_SyncSubscriptionDataResponse_QNAME, SyncSubscriptionDataResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ChangeMSISDN }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.csapi.org/schema/parlayx/data/sync/v1_0/local", name = "changeMSISDN")
    public JAXBElement<ChangeMSISDN> createChangeMSISDN(ChangeMSISDN value) {
        return new JAXBElement<ChangeMSISDN>(_ChangeMSISDN_QNAME, ChangeMSISDN.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ChangeMSISDNResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.csapi.org/schema/parlayx/data/sync/v1_0/local", name = "changeMSISDNResponse")
    public JAXBElement<ChangeMSISDNResponse> createChangeMSISDNResponse(ChangeMSISDNResponse value) {
        return new JAXBElement<ChangeMSISDNResponse>(_ChangeMSISDNResponse_QNAME, ChangeMSISDNResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SyncOrderRelation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.csapi.org/schema/parlayx/data/sync/v1_0/local", name = "syncOrderRelation")
    public JAXBElement<SyncOrderRelation> createSyncOrderRelation(SyncOrderRelation value) {
        return new JAXBElement<SyncOrderRelation>(_SyncOrderRelation_QNAME, SyncOrderRelation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SyncOrderRelationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.csapi.org/schema/parlayx/data/sync/v1_0/local", name = "syncOrderRelationResponse")
    public JAXBElement<SyncOrderRelationResponse> createSyncOrderRelationResponse(SyncOrderRelationResponse value) {
        return new JAXBElement<SyncOrderRelationResponse>(_SyncOrderRelationResponse_QNAME, SyncOrderRelationResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SyncMSISDNChange }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.csapi.org/schema/parlayx/data/sync/v1_0/local", name = "syncMSISDNChange")
    public JAXBElement<SyncMSISDNChange> createSyncMSISDNChange(SyncMSISDNChange value) {
        return new JAXBElement<SyncMSISDNChange>(_SyncMSISDNChange_QNAME, SyncMSISDNChange.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SyncMSISDNChangeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.csapi.org/schema/parlayx/data/sync/v1_0/local", name = "syncMSISDNChangeResponse")
    public JAXBElement<SyncMSISDNChangeResponse> createSyncMSISDNChangeResponse(SyncMSISDNChangeResponse value) {
        return new JAXBElement<SyncMSISDNChangeResponse>(_SyncMSISDNChangeResponse_QNAME, SyncMSISDNChangeResponse.class, null, value);
    }

}
