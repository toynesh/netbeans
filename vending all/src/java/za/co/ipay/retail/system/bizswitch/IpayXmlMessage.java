package za.co.ipay.retail.system.bizswitch;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class IpayXmlMessage extends Message
{
  protected Element ipayMsgElement;
  protected Element specificsElement;
  protected Map fieldMap;

  public IpayXmlMessage(Element ipayMsgElement)
  {
    this(ipayMsgElement, "0");
  }

  public IpayXmlMessage(Element ipayMsgElement, String sequenceNumber) {
    super(sequenceNumber, "IPAYXML");
    this.ipayMsgElement = ipayMsgElement;
  }

  public IpayXmlMessage(Element ipayMsgElement, Map fieldMap, String sequenceNumber) {
    super(sequenceNumber, "IPAYXML");
    this.ipayMsgElement = ipayMsgElement;
    this.fieldMap = fieldMap;
  }

  public Element getMessageElement() {
    return this.ipayMsgElement;
  }

  public byte[] getBytes() throws IOException {
    XMLOutputter xmlo = new XMLOutputter(Format.getPrettyFormat());
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    xmlo.output(this.ipayMsgElement, baos);
    return baos.toByteArray();
  }

  public String toString()
  {
    Document xmlDocument = new Document(this.ipayMsgElement);
    XMLOutputter xmlo = new XMLOutputter(Format.getPrettyFormat());
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try {
      xmlo.output(xmlDocument, baos);
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
    return new String(baos.toByteArray());
  }
}