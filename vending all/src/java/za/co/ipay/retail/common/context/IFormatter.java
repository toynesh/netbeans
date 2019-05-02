package za.co.ipay.retail.common.context;

import java.awt.Component;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.table.DefaultTableCellRenderer;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import za.co.ipay.retail.common.exception.IUserException;

public class IFormatter
{
  public final DecimalFormat currencyFormat = (DecimalFormat)NumberFormat.getCurrencyInstance();

  public final NumberFormat decimalFormat = NumberFormat.getNumberInstance();

  public final NumberFormat percentFormat = NumberFormat.getPercentInstance();

  public final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

  public final SimpleDateFormat reportDateFormat = new SimpleDateFormat("dd/MM/yyyy");

  public final SimpleDateFormat shortTimeFormat = new SimpleDateFormat("HH:mm:ss");

  public final SimpleDateFormat descriptiveShortDateFormat = new SimpleDateFormat("HH:mm:ss, dd MMM");

  public final SimpleDateFormat bizswitchMessageTimestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");

  public final SimpleDateFormat bizswitchMessageDateFormat = new SimpleDateFormat("yyyy-MM-dd");

  public final SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

  public IFormatter()
  {
    String currencySymbol = System.getProperty("currencySymbol");
    if (currencySymbol != null) {
      DecimalFormatSymbols symbols = new DecimalFormatSymbols();
      symbols.setCurrencySymbol(currencySymbol);
      this.currencyFormat.setDecimalFormatSymbols(symbols);
    }
    this.currencyFormat.setGroupingUsed(false);
    this.decimalFormat.setMinimumFractionDigits(2);
    this.decimalFormat.setMaximumFractionDigits(2);
    this.decimalFormat.setGroupingUsed(false);
    this.percentFormat.setMaximumFractionDigits(1);
  }

  public String format(Object object)
  {
    if (object == null) return null;
    if ((object instanceof String)) {
      String string = object.toString();
      if (string.length() == 0) return "";
      return string;
    }
    if ((object instanceof Throwable)) return formatExceptionMessage((Throwable)object);
    Method method = getNullableMethod(getClass(), "format", object.getClass());
    if (method != null) {
      try {
        return (String)method.invoke(this, new Object[] { object });
      } catch (InvocationTargetException e) {
        throw new RuntimeException(e.getCause());
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
    if ((object instanceof Component)) return formatComponent((Component)object);
    if ((object instanceof Event)) return formatClassName(object);
    return object.toString();
  }

  public String formatExceptionMessage(Throwable e)
  {
    if (e.getMessage() == null) return format(e.getClass());
    return e.getMessage();
  }

  public String formatVerboseExceptionException(Throwable e)
  {
    if (e.getMessage() == null) return format(e.getClass());
    return e.getMessage();
  }

  public Method getNullableMethod(Class targetClass, String methodName, Class parameterClass)
  {
    try
    {
      return targetClass.getMethod(methodName, new Class[] { parameterClass });
    } catch (Exception e) {
    }
    return null;
  }

  public String formatSpace(Object[] args)
  {
    return formatDelim(" ", args);
  }

  public String formatDelim(String delim, Object[] args)
  {
    StringBuffer buffer = new StringBuffer();
    for (Object object : args) {
      if (buffer.length() > 0) buffer.append(delim);
      buffer.append(format(object));
    }
    return buffer.toString();
  }

  public NumberFormat getNumberFormat() {
    return this.decimalFormat;
  }

  public String formatComponent(Component component)
  {
    return component.getName();
  }

  public String format(WindowEvent event)
  {
    return formatClassName(event.getWindow());
  }

  public String format(KeyEvent event)
  {
    return formatDelim(" ", new Object[] { event.getSource(), new Character(event.getKeyChar()), KeyEvent.getKeyModifiersText(event.getModifiers()) });
  }

  public String format(ActionEvent event)
  {
    return event.getActionCommand();
  }

  public String formatClassName(Object object)
  {
    if (object == null) return "(Class) null";
    return format(object.getClass());
  }

  public String format(Class classObject)
  {
    return formatClassName(classObject.getName());
  }

  public String formatClassName(String className)
  {
    int index = className.lastIndexOf(".");
    if (index > 0) return className.substring(index + 1);
    return className;
  }

  public String format(Element element)
  {
    XMLOutputter xmlo = new XMLOutputter(Format.getPrettyFormat());
    return xmlo.outputString(element);
  }

  public String formatTimestamp(Date date)
  {
    if (date == null) return "";
    if (IContext.context.localeDateFormat) DateFormat.getDateTimeInstance().format(date);
    return this.timestampFormat.format(date);
  }

  public String formatReportDate(Date date)
  {
    if (date == null) return "";
    if (IContext.context.localeDateFormat) DateFormat.getDateTimeInstance().format(date);
    return this.reportDateFormat.format(date);
  }

  public String formatDate(Date date)
  {
    if (date == null) return "";
    if (IContext.context.localeDateFormat) return DateFormat.getDateInstance().format(date);
    return this.dateFormat.format(date);
  }

  public Date parseBizswitchDate(String string)
    throws ParseException
  {
    return this.bizswitchMessageTimestampFormat.parse(string);
  }

  public Date parseNullableBizswitchDate(String string)
    throws ParseException
  {
    if ((string == null) || (string.length() == 0)) return null;
    return this.bizswitchMessageTimestampFormat.parse(string);
  }

  public String[] formatArray(Object[] args)
  {
    String[] array = new String[args.length];
    for (int i = 0; i < args.length; i++) {
      array[i] = formatSpace(new Object[] { args[i] });
    }
    return array;
  }

  public int getNullableArrayLength(Object[] array)
  {
    if (array == null) return 0;
    return array.length;
  }

  public String formatCurrency(double amount)
  {
    return this.currencyFormat.format(amount);
  }

  public String formatMoney(double amount)
  {
    return this.decimalFormat.format(amount);
  }

  public String formatPercent(double amount)
  {
    return this.percentFormat.format(amount);
  }

  public String formatDecimal(double amount)
  {
    return this.decimalFormat.format(amount);
  }

  public String formatSignedMoney(double amount)
  {
    if (amount > 0.0D) return "+" + formatMoney(amount);
    return formatMoney(amount);
  }

  public String formatBizswitchTimestamp(Date date)
  {
    return this.bizswitchMessageTimestampFormat.format(date);
  }

  public String formatBizswitch(Object value)
  {
    if (value == null) return "";
    if ((value instanceof Date)) return formatBizswitchTimestamp((Date)value);
    return value.toString();
  }

  public String formatAttributes(Map map)
  {
    StringBuffer buffer = new StringBuffer();
    for (Iterator i$ = map.keySet().iterator(); i$.hasNext(); ) { Object key = i$.next();
      Object value = map.get(key);
      String string = format(value);
      if (buffer.length() > 0) buffer.append(" ");
      buffer.append(format(key) + "=");
      if (string.indexOf(" ") > 0) buffer.append("'" + string + "'"); else
        buffer.append(string);
    }
    return buffer.toString();
  }

  public String format(String format, Object[] args)
  {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    printWriter.printf(format, args);
    printWriter.close();
    return stringWriter.toString();
  }

  public Number parseCurrency(String string)
    throws ParseException
  {
    return this.currencyFormat.parse(string);
  }

  public Number parseDecimal(String string)
    throws ParseException
  {
    return this.decimalFormat.parse(string);
  }

  public Object formatRenderer(DefaultTableCellRenderer cellRenderer, Object value)
  {
    if ((value instanceof Double)) {
      cellRenderer.setHorizontalTextPosition(4);
      cellRenderer.setHorizontalAlignment(4);
      return formatDecimal(((Double)value).doubleValue());
    }
    return value;
  }

  public String formatRelativeDate(Date date)
  {
    String dateFormat = "dd/MM/yy ";
    String timeFormat = "HH:mm";
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    Calendar today = Calendar.getInstance();
    if (calendar.get(1) == today.get(1)) {
      dateFormat = "dd MMM ";
    }
    return new SimpleDateFormat(dateFormat + timeFormat).format(date);
  }

  public String formatUserMessage(Throwable exception, String message, Object[] args)
  {
    if ((exception instanceof IUserException)) {
      IUserException userException = (IUserException)exception;
      return userException.getUserMessage();
    }
    if (message != null) {
      message = IContextConfiguration.formatter.format(message, args);
      if (message.startsWith("~")) return format(IContext.messages.systemErrorFormatTilde, new Object[] { message.substring(2) });
      return message;
    }
    return IContext.messages.systemError;
  }

  public String formatLogExceptionMessage(String message, Object[] args)
  {
    message = formatTildeUserExceptionMessage(message, IContext.messages.systemError, IContext.messages.systemErrorFormatTilde);
    if (args.length > 0) return message + ": " + formatSpace(args);
    return message;
  }

  public String formatTildeUserExceptionMessage(String message)
  {
    return formatTildeUserExceptionMessage(message, IContext.messages.systemError, IContext.messages.systemErrorFormatTilde);
  }

  public String formatTildeUserExceptionMessage(String message, String defaultMesssage, String tildeFormatMessage)
  {
    if (message == null) return defaultMesssage;
    if (message.startsWith("~")) return format(tildeFormatMessage, new Object[] { message.substring(2) });
    return message;
  }
}