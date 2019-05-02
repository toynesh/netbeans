package za.co.ipay.retail.common.context;

import java.io.PrintStream;
import java.util.logging.Level;

public class IContextConfiguration
{
  public static IFormatter formatter = new IFormatter();

  public static final ILogger logger = new ILogger("trace", Level.INFO);

  public final String ftpUpdateUrlPrefix = "ftp://posupdate:up2date@";

  public final int ftpUpdateExitCode = 8;

  public final int fatalErrorExitCode = 1;

  public final int cancelStartupExitCode = 2;

  public final int maxLoginAttempts = 3;

  public final int maxConnectionAttempts = 3;

  public final int maxVendReversalAttempts = 3;

  public static final String releaseVersion = getStringProperty("releaseVersion", "3.53");

  public static final boolean printOptions = getBooleanProperty("printOptions", false);

  public static final boolean osNameLinux = System.getProperty("os.name").toLowerCase().startsWith("linux");

  public final String localStorageDirectory = getStringProperty("localStorageDirectory", "/tmp/storage");

  public final String localKeyStoreFile = getStringProperty("localKeyStoreFile", "keystores/client.private");

  public final String connectCommand = getStringProperty("connectCommand", "bin/restart-pppd.sh");

  public final int connectionRetry = getIntProperty("connectionRetry", 2);

  public final int connectionRetryDuration = getIntProperty("connectionRetryDuration", 1000);

  public final boolean confirmDefaultTokenTechReprint = getBooleanProperty("confirmDefaultTokenTechReprint", false);

  public final boolean confirmVendPrinted = getBooleanProperty("confirmVendPrinted", false);

  public final String currencySymbol = getStringProperty("currencySymbol", null);

  public final String logoImage400 = getStringProperty("defaultLogo400", "/za/co/ipay/retail/common/resource/ipaylogo400.png");

  public final String logoImage600 = getStringProperty("defaultLogo600", "/za/co/ipay/retail/common/resource/ipaylogo600.png");

  public final boolean allowEmptyPaymentInfo = getBooleanProperty("allowEmptyPaymentInfo", true);

  public final boolean allowPartialPaymentInfo = getBooleanProperty("allowPartialPaymentInfo", true);

  public final boolean allowVendMultipleToken = getBooleanProperty("allowVendMultipleToken", false);

  public final String autoLogonUsername = getStringProperty("autoLogonUsername", null);

  public final boolean captureNotificationInfo = getBooleanProperty("captureNotificationInfo", false);

  public final int defaultFrameWidth = getIntProperty("defaultFrameWidth", 800);

  public final int defaultFrameHeight = getIntProperty("defaultFrameHeight", 600);

  public final boolean disableKeyChangeReq = getBooleanProperty("disableKeyChangeReq", false);

  public final boolean edfCustomisation = getBooleanProperty("edfCustomisation", false);

  public final boolean elecDenominations = getBooleanProperty("elecDenominations", false);

  public final boolean enableElecVendMeter = getBooleanProperty("enableElecVendMeter", false);

  public final boolean enableElecVendUnits = getBooleanProperty("enableElecVendUnits", false);

  public final boolean enableElecVendReprint = getBooleanProperty("enableElecVendReprint", false);

  public final boolean enableElecVendTest = getBooleanProperty("enableElecVendTest", false);

  public final boolean enableElecVendReplace = getBooleanProperty("enableElecVendReplace", false);

  public final boolean enableElecVendMagToken = getBooleanProperty("enableElecVendMagToken", false);

  public final boolean enableEskomReceiptHeaders = getBooleanProperty("enableEskomReceiptHeaders", false);

  public final boolean enableNetworkTab = getBooleanProperty("enableNetworkTab", false);

  public final boolean enableTokenTab = getBooleanProperty("enableTokenTab", false);

  public final boolean enableWriteCard = getBooleanProperty("enableWriteCard", false);

  public final boolean exitOnClose = getBooleanProperty("exitOnClose", false);

  public final boolean forceCustInfo = getBooleanProperty("forceCustInfo", false);

  public final boolean forceKeyChangeReq = getBooleanProperty("forceKeyChangeReq", false);

  public final boolean forceMeterInfo = getBooleanProperty("forceMeterInfo", false);

  public final boolean forceMagCard = getBooleanProperty("forceMagCard", false);

  public final boolean forceMagToken = getBooleanProperty("forceMagToken", false);

  public final int newsItemLimit = getIntProperty("newsItemLimit", 10);

  public final boolean localeDateFormat = getBooleanProperty("localeDateFormat", false);

  public final String lookupServerName = getStringProperty("lookupServerName", null);

  public final String netPrinter = getStringProperty("netPrinter", null);

  public final String netPrinterDevice = getStringProperty("netPrinterDevice", null);

  public final boolean netPrinterU210 = getBooleanProperty("netPrinterU210", false);

  public final int netPrinterPort = getIntProperty("netPrinterPort", 9000);

  public final boolean nullableMeterParameters = getBooleanProperty("nullableMeterParameters", true);

  public final int magCardReadMillis = getIntProperty("magCardReadMillis", 2000);

  public final boolean marketingMessageAbove = getBooleanProperty("marketingMessageAbove", false);

  public final boolean marketingMessageMiddle = getBooleanProperty("marketingMessageMiddle", false);

  public final boolean maximiseWindow = getBooleanProperty("maximiseWindow", false);

  public final boolean printCustAddr = getBooleanProperty("printCustAddr", false);

  public int printerSleepPeriodU210 = getIntProperty("printerSleepPeriodU210", 200);

  public int printerSleepPeriodT70 = getIntProperty("printerSleepPeriodT70", 0);

  public final boolean printMeterInfo = getBooleanProperty("printMeterInfo", false);

  public final boolean printVat = getBooleanProperty("printVat", true);

  public final boolean printRemaining = getBooleanProperty("printRemaining", true);

  public final String profile = getStringProperty("profile", null);

  public final boolean screenSizeWindow = getBooleanProperty("screenSizeWindow", false);

  public final String remoteKeyStoreFile = getStringProperty("remoteKeyStoreFile", "keystores/client.public");

  public final boolean rxtxParallelPort = getBooleanProperty("rxtxParallelPort", false);

  public final boolean rxtxSerialPort = getBooleanProperty("rxtxSerialPort", false);

  public final boolean selectConnection = getBooleanProperty("selectConnection", false);

  public final boolean selectPrinter = getBooleanProperty("selectPrinter", false);

  public final boolean simulateVendDebtRctNum = getBooleanProperty("simulateVendDebtRctNum", false);

  public final boolean simulateVendFixedRctNum = getBooleanProperty("simulateVendFixedRctNum", false);

  public final boolean simulateVendCommsError = getBooleanProperty("simulateVendCommsError", false);

  public final boolean simulateVendPrintError = getBooleanProperty("simulateVendPrintError", false);

  public final boolean simulateVendMagCardError = getBooleanProperty("simulateVendMagCardError", false);

  public final String simulateVendReversalInvalidRef = getStringProperty("simulateVendReversalInvalidRef", null);

  public final boolean simulateVendReversalCommsError = getBooleanProperty("simulateVendReversalCommsError", false);

  public final boolean simulateVendReversalResultCode004 = getBooleanProperty("simulateVendReversalResultCode004", false);

  public final boolean simulateVendReversalPrintError = getBooleanProperty("simulateVendReversalPrintError", false);

  public final boolean simulateMagCardSwiped = getBooleanProperty("simulateMagCardSwiped", false);

  public final boolean simulateMessage = getBooleanProperty("simulateMessage", false);

  public final int scrollBarHeight = getIntProperty("scrollBarHeight", 20);

  public final boolean suppressWriteCard = getBooleanProperty("suppressWriteCard", false);

  public final boolean suppressLogonMessage = getBooleanProperty("suppressLogonMessage", false);

  public final boolean suppressNews = getBooleanProperty("suppressNews", false);

  public final int stackTraceLimit = getIntProperty("stackTraceLimit", 8);

  public final boolean suppressChangedMeterInfo = getBooleanProperty("suppressChangedMeterInfo", false);

  public final boolean suppressFtpUpdate = getBooleanProperty("suppressFtpUpdate", false);

  public final boolean suppressLoadReversal = getBooleanProperty("suppressLoadReversal", false);

  public final boolean suppressCookieXmlEncryption = getBooleanProperty("suppressCookieXmlEncryption", false);

  public final boolean suppressPaperOut = getBooleanProperty("suppressPaperOut", true);

  public final boolean suppressCellVendPanel = getBooleanProperty("suppressCellVendPanel", false);

  public final boolean suppressRetailerMessage = getBooleanProperty("suppressRetailerMessage", false);

  public final String testMarketingMessage = getStringProperty("testMarketingMessage", null);

  public final String testMeter1 = getStringProperty("testMeter1", null);

  public final String testTrack2 = getStringProperty("testTrack2", null);

  public final String updateDestinationDir = getStringProperty("updateDestinationDir", "C:/ipay/incoming", "~/.ipay/incoming");

  public final String updateArchiveDir = getStringProperty("updateArchiveDir", "C:/ipayposupdate/archive", "~/.ipayposupdate/archive");

  public final int vendReversalCapacity = getIntProperty("vendReversalCapacity", 5);

  public final int vendReversalClearCapacity = getIntProperty("vendReversalClearCapacity", 20);

  public final int vendReversalLogonLimit = getIntProperty("vendReversalLogonLimit", 1);

  public final int vendReversalRetry = getIntProperty("vendReversalRetry", 0);

  public final boolean maliCustomisation = getBooleanProperty("maliCustomisation", false);

  public final boolean kplcCustomisation = getBooleanProperty("kplcCustomisation", false);

  public final boolean testPrinter = getBooleanProperty("testPrinter", false);

  public final boolean validateCellZa = getBooleanProperty("validateCellZa", false);

  protected static String getStringProperty(String key, String defaultValue)
  {
    String value = System.getProperty(key);
    printOption(key, value, defaultValue);
    if (value != null) {
      return value;
    }
    return defaultValue;
  }

  protected static String getStringProperty(String key, String defaultValue, String defaultLinuxValue)
  {
    if (osNameLinux) {
      defaultValue = defaultLinuxValue;
    }
    return getStringProperty(key, defaultValue);
  }

  protected static int getIntProperty(String key, int defaultValue)
  {
    String property = System.getProperty(key);
    printOption(key, property, Integer.valueOf(defaultValue));
    if (property != null) {
      try {
        return Integer.parseInt(property);
      }
      catch (Exception e) {
        logger.warning(new Object[] { e });
      }
    }
    return defaultValue;
  }

  protected static boolean getBooleanProperty(String key, boolean defaultValue)
  {
    String property = System.getProperty(key);
    printOption(key, property, Boolean.valueOf(defaultValue));
    if (property != null) {
      try {
        return Boolean.parseBoolean(property);
      }
      catch (Exception e) {
        logger.warning(new Object[] { e });
      }
    }
    return defaultValue;
  }

  protected static void printOption(String key, Object value, Object defaultValue)
  {
    if (printOptions)
      if (value == null)
        System.out.println(key + " = " + defaultValue + " (default)");
      else
        System.out.println(key + " = " + value + ", default = " + defaultValue);
  }
}