package za.co.ipay.retail.system.bizswitch;

public class MessageBuildException extends Exception
{
  public MessageBuildException(String message)
  {
    super(message);
  }

  public MessageBuildException(Exception exception) {
    super("An error occured while trying to build up a message from raw data", exception);
  }

  public MessageBuildException(String message, Exception exception) {
    super(message, exception);
  }
}