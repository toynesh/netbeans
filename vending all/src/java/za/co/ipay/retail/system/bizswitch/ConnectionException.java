package za.co.ipay.retail.system.bizswitch;

public class ConnectionException extends Exception
{
  public ConnectionException()
  {
  }

  public ConnectionException(String msg)
  {
    super(msg);
  }

  public ConnectionException(Exception exception) {
    super("A connection error occured", exception);
  }

  public ConnectionException(String message, Exception exception) {
    super(message, exception);
  }
}