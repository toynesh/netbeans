
package org.csapi.schema.parlayx.sms.send.v2_2.local;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import org.csapi.schema.parlayx.common.v2_1.ChargingInformation;
import org.csapi.schema.parlayx.common.v2_1.SimpleReference;
import org.csapi.schema.parlayx.sms.v2_2.SmsFormat;


/**
 * <p>Java class for sendSmsRingtone complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="sendSmsRingtone">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="addresses" type="{http://www.w3.org/2001/XMLSchema}anyURI" maxOccurs="unbounded"/>
 *         &lt;element name="senderName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="charging" type="{http://www.csapi.org/schema/parlayx/common/v2_1}ChargingInformation" minOccurs="0"/>
 *         &lt;element name="ringtone" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="smsFormat" type="{http://www.csapi.org/schema/parlayx/sms/v2_2}SmsFormat"/>
 *         &lt;element name="receiptRequest" type="{http://www.csapi.org/schema/parlayx/common/v2_1}SimpleReference" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sendSmsRingtone", propOrder = {
    "addresses",
    "senderName",
    "charging",
    "ringtone",
    "smsFormat",
    "receiptRequest"
})
public class SendSmsRingtone {

    @XmlElement(required = true)
    @XmlSchemaType(name = "anyURI")
    protected List<String> addresses;
    protected String senderName;
    protected ChargingInformation charging;
    @XmlElement(required = true)
    protected String ringtone;
    @XmlElement(required = true)
    protected SmsFormat smsFormat;
    protected SimpleReference receiptRequest;

    /**
     * Gets the value of the addresses property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the addresses property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAddresses().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getAddresses() {
        if (addresses == null) {
            addresses = new ArrayList<String>();
        }
        return this.addresses;
    }

    /**
     * Gets the value of the senderName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSenderName() {
        return senderName;
    }

    /**
     * Sets the value of the senderName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSenderName(String value) {
        this.senderName = value;
    }

    /**
     * Gets the value of the charging property.
     * 
     * @return
     *     possible object is
     *     {@link ChargingInformation }
     *     
     */
    public ChargingInformation getCharging() {
        return charging;
    }

    /**
     * Sets the value of the charging property.
     * 
     * @param value
     *     allowed object is
     *     {@link ChargingInformation }
     *     
     */
    public void setCharging(ChargingInformation value) {
        this.charging = value;
    }

    /**
     * Gets the value of the ringtone property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRingtone() {
        return ringtone;
    }

    /**
     * Sets the value of the ringtone property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRingtone(String value) {
        this.ringtone = value;
    }

    /**
     * Gets the value of the smsFormat property.
     * 
     * @return
     *     possible object is
     *     {@link SmsFormat }
     *     
     */
    public SmsFormat getSmsFormat() {
        return smsFormat;
    }

    /**
     * Sets the value of the smsFormat property.
     * 
     * @param value
     *     allowed object is
     *     {@link SmsFormat }
     *     
     */
    public void setSmsFormat(SmsFormat value) {
        this.smsFormat = value;
    }

    /**
     * Gets the value of the receiptRequest property.
     * 
     * @return
     *     possible object is
     *     {@link SimpleReference }
     *     
     */
    public SimpleReference getReceiptRequest() {
        return receiptRequest;
    }

    /**
     * Sets the value of the receiptRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link SimpleReference }
     *     
     */
    public void setReceiptRequest(SimpleReference value) {
        this.receiptRequest = value;
    }

}
