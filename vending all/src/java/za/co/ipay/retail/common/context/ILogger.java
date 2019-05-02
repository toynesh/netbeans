package za.co.ipay.retail.common.context;

import java.io.PrintStream;
import java.util.logging.Level;
import javax.swing.SwingUtilities;

public class ILogger
{
  protected String loggerName;
  protected Level level;

  public ILogger(String loggerName, Level level)
  {
    this.loggerName = loggerName;
    String levelKey = loggerName + "Logger";
    String levelValue = System.getProperty(levelKey, level.getName());
    level = Level.parse(levelValue);
    setLevel(level);
    System.out.println(levelKey + " = " + level + ", " + levelKey + " = " + levelValue + " " + isLoggable(Level.FINE));
  }

  public void finest(String string)
  {
    logp(Level.FINEST, string);
  }

  public void entering(String string)
  {
    logp(Level.FINER, "entering:" + string);
  }

  public void exiting(String string)
  {
    logp(Level.FINER, "exiting: " + string);
  }

  public void throwing(Throwable throwable)
  {
    logp(Level.FINER, "throwing: " + IContextConfiguration.formatter.formatExceptionMessage(throwable));
  }

  public void entering()
  {
    logp(Level.FINER, "entering");
  }

  protected void printStackTrace(Throwable throwable)
  {
    int limit = IContext.context.stackTraceLimit;
    StackTraceElement[] elements = throwable.getStackTrace();
    for (int i = 0; (i < limit) && (i < elements.length); i++) {
      StackTraceElement element = elements[i];
      if (element.getClassName().startsWith("java.awt")) break;
      System.out.println("  " + formatDelim(":", new Object[] { element.getClassName(), element.getMethodName(), Integer.valueOf(element.getLineNumber()) }));
    }
  }

  public void exiting()
  {
    logp(Level.FINER, "exiting");
  }

  public void comment(Object[] args)
  {
  }

  public void finest(Object[] args)
  {
    logp(Level.FINEST, formatArgs(args));
  }

  public void finer(Object[] args)
  {
    logp(Level.FINER, formatArgs(args));
  }

  public void fine(Object[] args)
  {
    logp(Level.FINE, formatArgs(args));
  }

  public void entering(Object[] args)
  {
    logp(Level.FINER, "entering: " + formatArgs(args));
  }

  public void enteringEdt(Object[] args)
  {
    logp(Level.FINER, "entering: " + formatArgs(args), !SwingUtilities.isEventDispatchThread());
  }

  public void exiting(Object[] args)
  {
    logp(Level.FINER, "exiting: " + formatArgs(args));
  }

  public void throwing(Throwable throwable, Object[] args)
  {
    logp(Level.FINER, "throwing: " + formatArgs(args));
  }

  public void exit(int exitCode, Object[] args)
  {
    Throwable e = new Throwable();
    e.printStackTrace();
    severe(new Object[] { e, args });
    System.exit(exitCode);
  }

  public void info(String string)
  {
    logp(Level.INFO, string);
  }

  public void warning(String string)
  {
    logp(Level.WARNING, string);
  }

  public void severe(String string)
  {
    logp(Level.SEVERE, string);
  }

  public void info(Object[] args)
  {
    logp(Level.INFO, formatArgs(args));
  }

  public void warning(Object[] args)
  {
    logp(Level.WARNING, formatArgs(args));
    if ((args.length > 0) && ((args[0] instanceof Exception)))
      logException((Exception)args[0]);
  }

  public void severe(Object[] args)
  {
    logp(Level.SEVERE, formatArgs(args));
    if ((args.length > 0) && ((args[0] instanceof Exception)))
      logException((Exception)args[0]);
  }

  public void logException(Throwable exception)
  {
    exception.printStackTrace();
  }

  public void logp(Level level, String string)
  {
    logp(level, string, false);
  }

  public void logp(Level level, String string, boolean trace)
  {
    if (isLoggable(level)) {
      Throwable throwable = new Throwable();
      StackTraceElement frame = inferCaller(throwable);
      string = IContextConfiguration.formatter.formatSpace(new Object[] { level, this.loggerName, IContextConfiguration.formatter.formatClassName(frame.getClassName()), frame.getMethodName(), Integer.valueOf(frame.getLineNumber()), string });
      if (SwingUtilities.isEventDispatchThread()) string = "IsEdt:" + string; else
        string = "NotEdt:" + string;
      System.out.println(string);
      if (trace) printStackTrace(throwable);
      System.out.flush();
    }
  }

  public String formatInstance(Object object)
  {
    if (object == null) return "null";
    return "(" + IContextConfiguration.formatter.format(object.getClass()) + ") " + IContextConfiguration.formatter.format(object);
  }

  public String formatArgs(Object[] args)
  {
    return formatDelim(":", args);
  }

  public String formatDelim(String delim, Object[] args)
  {
    StringBuffer buffer = new StringBuffer();
    for (Object object : args) {
      if (buffer.length() > 0) buffer.append(delim);
      buffer.append(formatInstance(object));
    }
    return buffer.toString();
  }

  public StackTraceElement inferCaller(Throwable throwable)
  {
    StackTraceElement[] elements = throwable.getStackTrace();
    for (StackTraceElement element : elements) {
      if (!element.getClassName().endsWith("Logger")) {
        return element;
      }
    }

    throw new RuntimeException();
  }

  public void println(Level level, Object[] args)
  {
    if ((level == null) || (isLoggable(level))) {
      System.out.println(IContextConfiguration.formatter.formatSpace(args));
      System.out.flush();
    }
  }

  protected void setLevel(Level level)
  {
    this.level = level;
  }

  protected boolean isLoggable(Level messageLevel)
  {
    Level loggerLevel = this.level;
    int messageLevelValue = messageLevel.intValue();
    int loggerLevelValue = loggerLevel.intValue();
    boolean value = false;
    if (messageLevel.intValue() >= loggerLevel.intValue()) value = true;
    return value;
  }
}