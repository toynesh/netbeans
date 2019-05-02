package za.co.ipay.retail.system.bizswitch;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import za.co.ipay.retail.common.context.IContextConfiguration;
import za.co.ipay.retail.common.context.IFormatter;
import za.co.ipay.retail.common.context.ILogger;

public class BizswitchConnectionManager
{
  private static String ipAddress;
  private static int port;
  private static int counter;
  private static Object counterLock = new Object();
  private static long seqNum;
  private static Object seqNumLock = new Object();

  public static BizswitchConnection getConnection(String connectionId)
    throws IOException
  {
    IContextConfiguration.logger.entering("BizswitchConnectionManager: ip=" + ipAddress + ", port=" + port + ", connectionId=" + connectionId);
    return new BizswitchConnection(ipAddress, port, connectionId);
  }

  public static String getReferenceNumber()
  {
    Calendar calendar = GregorianCalendar.getInstance();
    StringBuilder builder = new StringBuilder();
    try {
      builder.append(IContextConfiguration.formatter.format("%d%03d%02d%02d%04d", new Object[] { Integer.valueOf(calendar.get(1) % 10), Integer.valueOf(calendar.get(6)), Integer.valueOf(calendar.get(11)), Integer.valueOf(calendar.get(12)), Integer.valueOf(getNextCount()) }));
    }
    catch (IllegalArgumentException iae)
    {
      System.err.println("BizswitchConnectionManager: Error while building reference number: " + iae);
      iae.printStackTrace();
    }
    return builder.toString();
  }

  private static int getNextCount() {
    synchronized (counterLock) {
      counter += 1;
      if (counter == 10000) {
        counter = 0;
      }
      return counter;
    }
  }

  public static String getSequenceNumber() {
    return Long.toString(getNextSeqNum());
  }

  private static long getNextSeqNum() {
    synchronized (seqNumLock) {
      if (seqNum == 9223372036854775807L) {
        seqNum = 0L;
        return 9223372036854775807L;
      }
      return seqNum++;
    }
  }

  public static void setIpAddress(String theIpAddress) {
    IContextConfiguration.logger.entering("BizswitchConnectionManager: ip=" + theIpAddress);
    ipAddress = theIpAddress;
  }

  public static void setIpPort(int thePort) {
    IContextConfiguration.logger.entering("BizswitchConnectionManager: port=" + thePort);
    port = thePort;
  }
}