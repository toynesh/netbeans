
package com.pdsl.vending;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for airtimeVend complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="airtimeVend">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="vendorcode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tokentype" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="amount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "airtimeVend", propOrder = {
    "vendorcode",
    "tokentype",
    "amount"
})
public class AirtimeVend {

    protected String vendorcode;
    protected String tokentype;
    protected String amount;

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
     * Gets the value of the tokentype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTokentype() {
        return tokentype;
    }

    /**
     * Sets the value of the tokentype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTokentype(String value) {
        this.tokentype = value;
    }

    /**
     * Gets the value of the amount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAmount() {
        return amount;
    }

    /**
     * Sets the value of the amount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAmount(String value) {
        this.amount = value;
    }

}
