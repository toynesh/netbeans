package za.co.ipay.retail.system.bizswitch;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import org.apache.tools.bzip2.CBZip2InputStream;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import za.co.ipay.retail.common.config.IConfiguration;
import za.co.ipay.retail.common.context.IContext;
import za.co.ipay.retail.common.exception.ICommsException;

public class IpayXmlMessageFactory
{
  public static final String CLASS_REVISION = "1.4";
  public static final String CLASS_ID = "IpayXmlMessageFactory.java,v 1.4 2008/11/11 11:31:44 evans Exp";
  public static final String CLASS_LAST_AUTHOR = "evans";
  protected static IpayXmlMessageFactory instance = null;

  public static IpayXmlMessageFactory getInstance()
  {
    if (instance == null) instance = new IpayXmlMessageFactory();
    return instance;
  }

  public Message buildMessage(byte[] bytes, Map fieldMap, long sequenceNumber)
    throws ICommsException, IOException
  {
    ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
    String compressAlg = IContext.configuration.compression;
    if (!"NONE".equals(compressAlg)) {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      byte[] uncompressBuffer = new byte[4096];
//      InputStream is;
      InputStream is;
      if ("BZIP2".equals(compressAlg)) {
        if (bytes.length < 2) {
          throw new RuntimeException("BZIP2 compressed message has invalid length");
        }

        byte[] bzBytes = new byte[2];
        bais.read(bzBytes);
        if (!new String(bzBytes, "US-ASCII").equals("BZ")) {
          throw new RuntimeException("Message is not BZIP2 compressed");
        }
        is = new CBZip2InputStream(bais);
      } else {
        is = null;
      }

      int bytesRead = is.read(uncompressBuffer);
      while (bytesRead != -1) {
        baos.write(uncompressBuffer, 0, bytesRead);
        bytesRead = is.read(uncompressBuffer);
      }
is.close();

      bytes = baos.toByteArray();

      SAXBuilder saxBuilder = new SAXBuilder(false);
      Element rootElement;
      try {
        rootElement = saxBuilder.build(new ByteArrayInputStream(bytes)).getRootElement();
      } catch (JDOMException jde) {
        throw new ICommsException(jde, "~ building IpayXmlMessage from bytes", new Object[] { bytes });
      } catch (IOException ioe) {
        throw new ICommsException(ioe, "~ building IpayXmlMessage from bytes", new Object[] { bytes });
      }
      Message msg = new IpayXmlMessage(rootElement, fieldMap, String.valueOf(sequenceNumber));
      return msg;
    }
    SAXBuilder saxBuilder = new SAXBuilder(false);
    Element rootElement;
    try
    {
      rootElement = saxBuilder.build(bais).getRootElement();
    } catch (JDOMException jde) {
      throw new ICommsException(jde, "~ building IpayXmlMessage from bytes", new Object[] { bytes });
    } catch (IOException ioe) {
      throw new ICommsException(ioe, "~ building IpayXmlMessage from bytes", new Object[] { bytes });
    }
    Message msg = new IpayXmlMessage(rootElement, fieldMap, String.valueOf(sequenceNumber));
    return msg;
  }
}