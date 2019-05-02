package za.co.ipay.retail.system.bizswitch;

import java.io.IOException;

public abstract class Message
{
  public static final String CLASS_REVISION = "1.3";
  public static final String CLASS_ID = "Message.java,v 1.3 2008/12/29 10:32:09 evans Exp";
  public static final String CLASS_LAST_AUTHOR = "evans";
  public final String sequenceNumber;
  public final String protocol;
  public int nextIndex;

  public Message(String sequenceNumber, String protocol)
  {
    this.protocol = protocol;
    this.sequenceNumber = sequenceNumber;
  }

  public abstract byte[] getBytes()
    throws IOException;
}