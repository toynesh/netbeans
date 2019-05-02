
package org.csapi.schema.parlayx.data.sync.v1_0.local;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.csapi.schema.parlayx.data.v1_0.ProductDetail;


/**
 * <p>Java class for syncSubscriptionData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="syncSubscriptionData"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="MSISDN" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="serviceId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="productId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="updateType" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="productDetail" type="{http://www.csapi.org/schema/parlayx/data/v1_0}ProductDetail"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "syncSubscriptionData", propOrder = {
    "msisdn",
    "serviceId",
    "productId",
    "updateType",
    "productDetail"
})
public class SyncSubscriptionData {

    @XmlElement(name = "MSISDN", required = true)
    protected String msisdn;
    @XmlElement(required = true)
    protected String serviceId;
    @XmlElement(required = true)
    protected String productId;
    protected int updateType;
    @XmlElement(required = true)
    protected ProductDetail productDetail;

    /**
     * Gets the value of the msisdn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMSISDN() {
        return msisdn;
    }

    /**
     * Sets the value of the msisdn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMSISDN(String value) {
        this.msisdn = value;
    }

    /**
     * Gets the value of the serviceId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceId() {
        return serviceId;
    }

    /**
     * Sets the value of the serviceId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceId(String value) {
        this.serviceId = value;
    }

    /**
     * Gets the value of the productId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductId() {
        return productId;
    }

    /**
     * Sets the value of the productId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductId(String value) {
        this.productId = value;
    }

    /**
     * Gets the value of the updateType property.
     * 
     */
    public int getUpdateType() {
        return updateType;
    }

    /**
     * Sets the value of the updateType property.
     * 
     */
    public void setUpdateType(int value) {
        this.updateType = value;
    }

    /**
     * Gets the value of the productDetail property.
     * 
     * @return
     *     possible object is
     *     {@link ProductDetail }
     *     
     */
    public ProductDetail getProductDetail() {
        return productDetail;
    }

    /**
     * Sets the value of the productDetail property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProductDetail }
     *     
     */
    public void setProductDetail(ProductDetail value) {
        this.productDetail = value;
    }

}
