
package com.gmalto.test;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Params complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Params">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="getem" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Params", propOrder = {
    "getem"
})
public class Params {

    protected String getem;

    /**
     * Gets the value of the getem property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGetem() {
        return getem;
    }

    /**
     * Sets the value of the getem property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGetem(String value) {
        this.getem = value;
    }

}
