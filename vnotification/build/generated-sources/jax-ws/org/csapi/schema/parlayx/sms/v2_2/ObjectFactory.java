
package org.csapi.schema.parlayx.sms.v2_2;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.csapi.schema.parlayx.sms.v2_2 package. 
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


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.csapi.schema.parlayx.sms.v2_2
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DeliveryInformation }
     * 
     */
    public DeliveryInformation createDeliveryInformation() {
        return new DeliveryInformation();
    }

    /**
     * Create an instance of {@link SmsMessage }
     * 
     */
    public SmsMessage createSmsMessage() {
        return new SmsMessage();
    }

}
