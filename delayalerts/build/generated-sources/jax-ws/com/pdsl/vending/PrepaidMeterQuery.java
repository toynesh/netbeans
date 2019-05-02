
package com.pdsl.vending;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for prepaidMeterQuery complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="prepaidMeterQuery">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="vendorcode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="meter" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "prepaidMeterQuery", propOrder = {
    "vendorcode",
    "meter"
})
public class PrepaidMeterQuery {

    protected String vendorcode;
    protected String meter;

    /**
     * Gets the value of the vendorcode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVendorcode() {
        return vendorcode;
    }

    /**
     * Sets the value of the vendorcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVendorcode(String value) {
        this.vendorcode = value;
    }

    /**
     * Gets the value of the meter property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMeter() {
        return meter;
    }

    /**
     * Sets the value of the meter property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMeter(String value) {
        this.meter = value;
    }

}
