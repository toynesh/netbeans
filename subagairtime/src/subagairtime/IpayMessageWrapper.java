package subagairtime;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class IpayMessageWrapper
{
  public byte[] wrap(byte[] msg)
  {
    int len = msg.length;
    if (len > 65535) {
      throw new IllegalArgumentException("Message length exceeds 65535 bytes.");
    }

    byte firstByte = (byte)(len >>> 8);
    byte secondByte = (byte)len;

    ByteArrayOutputStream baos = new ByteArrayOutputStream(len + 2);
    baos.write(firstByte);
    baos.write(secondByte);
    try {
      baos.write(msg);
    }
    catch (IOException ioe)
    {
    }
    return baos.toByteArray();
  }

  public byte[] unWrap(InputStream inputStream) throws IOException
  {
      System.out.println("Starting of Stream.");
    int firstByteValue = inputStream.read();
    if (firstByteValue == -1) {
      throw new IOException("End of Stream.");
    }
    firstByteValue <<= 8;

    int secondByteValue = inputStream.read();
    if (secondByteValue == -1) {
      throw new IOException("End of Stream.");
    }

    int len = firstByteValue + secondByteValue;

    byte[] message = new byte[len];

    int currentIndex = 0;
    while (true) {
      int requestLen = len - currentIndex;
      int readLen = inputStream.read(message, currentIndex, requestLen);
      if (readLen == requestLen)
      {
        break;
      }

      currentIndex += readLen;
      int nextByte = inputStream.read();
      if (nextByte == -1) {
        throw new IOException("End of Stream.");
      }
      message[(currentIndex++)] = ((byte)nextByte);
    }

    return message;
  }
}