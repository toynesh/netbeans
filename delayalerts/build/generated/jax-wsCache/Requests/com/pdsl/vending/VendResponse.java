
package com.pdsl.vending;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for vendResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="vendResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="account" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="floatBalance" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="pdslError" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="queryRes" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="stdMsg" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="vendRef" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "vendResponse", propOrder = {
    "account",
    "floatBalance",
    "pdslError",
    "queryRes",
    "stdMsg",
    "vendRef"
})
public class VendResponse {

    protected String account;
    protected String floatBalance;
    protected String pdslError;
    protected String queryRes;
    protected String stdMsg;
    protected String vendRef;

    /**
     * Gets the value of the account property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccount() {
        return account;
    }

    /**
     * Sets the value of the account property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccount(String value) {
        this.account = value;
    }

    /**
     * Gets the value of the floatBalance property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFloatBalance() {
        return floatBalance;
    }

    /**
     * Sets the value of the floatBalance property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFloatBalance(String value) {
        this.floatBalance = value;
    }

    /**
     * Gets the value of the pdslError property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPdslError() {
        return pdslError;
    }

    /**
     * Sets the value of the pdslError property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPdslError(String value) {
        this.pdslError = value;
    }

    /**
     * Gets the value of the queryRes property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQueryRes() {
        return queryRes;
    }

    /**
     * Sets the value of the queryRes property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQueryRes(String value) {
        this.queryRes = value;
    }

    /**
     * Gets the value of the stdMsg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStdMsg() {
        return stdMsg;
    }

    /**
     * Sets the value of the stdMsg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStdMsg(String value) {
        this.stdMsg = value;
    }

    /**
     * Gets the value of the vendRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVendRef() {
        return vendRef;
    }

    /**
     * Sets the value of the vendRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVendRef(String value) {
        this.vendRef = value;
    }

}
