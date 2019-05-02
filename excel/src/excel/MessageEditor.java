package excel;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class MessageEditor {

    private String status;

    public String editToken(String results, String mpesa_acc, String mpesa_msisdn, String mpesa_code, String date) {
        String str = results.substring(3);
        String[] strsplit = str.split(" ");
        String str1 = strsplit[0];
        String str2 = strsplit[1];
        String str3 = strsplit[2];

        System.out.println("String 1: " + str1 + " and String 2: " + str2 + " and String 3: " + str3);
        String wrd = null;
        try {
            if (str.startsWith("token")) {
                String token = results.substring(9, 33);
                String results2 = results.replaceAll("OK|token:" + token + " ", "");
                String[] amountsplit = results2.split(" ");
                String amount = amountsplit[0].substring(1);
                amount = amount.replaceAll("\\s+", "");
                String amountbal = amountsplit[1].substring(6);
                String charges = amountsplit[2].substring(8);
                String ref = amountsplit[3];
                String[] refsplit = ref.split(":");
                ref = refsplit[3];
                String units = refsplit[1];
                String[] unitsplit = units.split(" ");
                units = unitsplit[0];
                units = units.replaceAll("http", "");
                units = units.replaceAll("\\s+", "");
                String msg = null;
                if (mpesa_acc.equals("delayed")) {
                    msg = "KPLC Token:" + token + "\nUnits:" + units + "\nAmount:" + amount + "\n(Elec:" + amountbal + " Other Charges:" + charges + "\nDate:" + date + "\nRef:" + ref;
                } else {
                    msg = "KPLC Mtr No:" + mpesa_acc + "\nToken:" + token + "\nUnits:" + units + "\nAmount:" + amount + "\n(Elec:" + amountbal + " Other Charges:" + charges + "\nDate:" + date + "\nRef:" + ref;
                    setStatus("success");
                }
                wrd = msg;
            } else if (str.startsWith("account")) {
                wrd = "postaid success";
                String acc = strsplit[0];
                String amtS = strsplit[1];
                int stpos = amtS.indexOf("http");
                String unw = amtS.substring(stpos, stpos + 28);
                String amt = amtS.replaceAll(unw, "");

                String rcpNo = strsplit[2];
                String msg = "Paid " + amt + " for KPLC" + acc + " " + rcpNo;

                setStatus("success");
                wrd = msg.toUpperCase();
            } else if ((str1 + " " + str2).equals("The specified")) {
                wrd = "wrong meter";
                String mtr = strsplit[3];
                String msg = results;
                msg = "FAILED |THE KPLC ACCOUNT / METER NUMBER " + mtr + " IS NOT CORRECT. Please check the number and re-try again, or contact support for assistance at 0709711000.";
                //this.sdp.sendSdpSms(mpesa_msisdn, msg, mpesa_code, "704307");
                wrd = msg;
                setStatus("activation");
            } else if ((str1 + " " + str2).equals("There was")) {
                Logger.getLogger(MessageEditor.class.getName()).log(Level.INFO, "Kenya POWER DELAY");
                wrd = "delay";
                String msg = "OK| KPLC system delayed. Will keep trying & resend your transaction.Queries Call:0709711000.";
                Logger.getLogger(MessageEditor.class.getName()).log(Level.INFO, "Sent KPLC Delay message: " + msg);

                setStatus("delay");
                wrd = msg;
            } else if ((str1 + " " + str2).equals("Unknown meter")) {
                wrd = "wrong meter";
                String mtr = strsplit[3];
                String msg = results;
                msg = "FAILED |THE KPLC ACCOUNT / METER NUMBER " + mpesa_acc + " IS NOT CORRECT. Please check the number and re-try again, or contact support for assistance at 0709711000.";
                wrd = msg;
                setStatus("wrong meter");
            } else if ((str1 + " " + str2 + " " + str3).equals("Cannot find meter/account")) {
                wrd = "The meter/account number xxx is incorrect.";
                String mtr = strsplit[3];
                String msg = results;
                msg = "FAILED |THE KPLC ACCOUNT / METER NUMBER " + mpesa_acc + " IS NOT CORRECT. Please check the number and re-try again, or contact support for assistance at 0709711000.";
                setStatus("wrong meter");
                wrd = msg;
            } else if ((str1 + " " + str2).equals("The meter/account")) {
                wrd = "The meter/account number xxx is incorrect.";
                String mtr = strsplit[3];
                String msg = results;
                msg = "FAILED |THE KPLC ACCOUNT / METER NUMBER " + mpesa_acc + " IS NOT CORRECT. Please check the number and re-try again, or contact support for assistance at 0709711000.";
                setStatus("wrong meter");
                wrd = msg;
            } else if ((str1 + " " + str2).equals("The BaseCustomerList")) {
                wrd = "BaseCustomerList";
                String mtr = strsplit[3];
                String msg = results;
                if (mpesa_acc.length() < 11) {
                    msg = "OK|THE KPLC ACCOUNT NUMBER " + mpesa_acc + " YOU PROVIDED HAS MISSING DETAILS. Queries Call:0709711000.";
                } else {
                    msg = "OK|THE KPLC METER NUMBER " + mpesa_acc + " YOU PROVIDED HAS MISSING DETAILS. Queries Call:0709711000.";
                }
                wrd = msg;
                setStatus("wrong meter");
            } else if ((str1 + " " + str2).equals("KP system")) {
                Logger.getLogger(MessageEditor.class.getName()).log(Level.INFO, "Kenya POWER DELAY");
                wrd = "delay";
                String msg = "OK| KPLC system delayed. Will keep trying & resend your transaction.Queries Call:0709711000.";
                Logger.getLogger(MessageEditor.class.getName()).log(Level.INFO, "Sent KPLC Delay message: " + msg);

                setStatus("delay");
                wrd = msg;
            } else if ((str1 + " " + str2).equals("An unhandled")) {
                Logger.getLogger(MessageEditor.class.getName()).log(Level.INFO, "An unhandled program error has occured");
                wrd = "delay";
                String msg = "OK| KPLC system delayed. Will keep trying & resend your transaction.Queries Call:0709711000.";
                Logger.getLogger(MessageEditor.class.getName()).log(Level.INFO, "Sent KPLC Delay message: " + msg);

                setStatus("delay");
                wrd = msg;
            } else if ((str1 + " " + str2).equals("Request amount")) {
                Logger.getLogger(MessageEditor.class.getName()).log(Level.INFO, "minimum");
                wrd = "OK|Request amount is less than minimum Queries ph:0707-373794.";
                String msg = "OK|MTRFE010: KPLC Specified transaction already processed. Queries ph:0709711000.";
                Logger.getLogger(MessageEditor.class.getName()).log(Level.INFO, "Sent KPLC Delay message: " + msg);
                setStatus("fbe");
                wrd = msg;
            } else if ((str1 + " " + str2 + " " + str3).equals("iled: Amount requested")) {
                Logger.getLogger(MessageEditor.class.getName()).log(Level.INFO, "Insufficient on delay");
                wrd = "delay insufficient";
                String msg = "OK| Insufficient amount based on your KPLC account time based charge.  Queries Call:0709711000.";
                Logger.getLogger(MessageEditor.class.getName()).log(Level.INFO, "Sent KPLC Delay message: " + msg);

                setStatus("insufficient");
                wrd = msg;
            } else if ((str1 + " " + str2 + " " + str3).equals("iled: The specified")) {
                Logger.getLogger(MessageEditor.class.getName()).log(Level.INFO, "wrong meter on delay");
                wrd = "delay wrong meter";
                String msg = results;
                msg = "FAILED |THE KPLC ACCOUNT / METER NUMBER  IS NOT CORRECT. Please check the number and re-try again, or contact support for assistance at 0709711000.";
                setStatus("wrong meter");
                wrd = msg;
            } else if ((str1 + " " + str2 + " " + str3).equals("iled: The Meter")) {
                Logger.getLogger(MessageEditor.class.getName()).log(Level.INFO, "wrong meter on delay");
                wrd = "delay wrong meter";
                String msg = results;
                msg = "FAILED |THE KPLC ACCOUNT / METER NUMBER " + mpesa_acc + " IS NOT CORRECT. Please check the number and re-try again, or contact support for assistance at 0709711000.";
                setStatus("wrong meter");
                wrd = msg;
            } else if (str1.equals("iled:")) {
                Logger.getLogger(MessageEditor.class.getName()).log(Level.INFO, "Failed on Delay Due to float");
                //wrd = "Outta FLOAT!!";
                //String msg = "Error message on delay. Not sent to customer";
                //wrd = msg;
            } else if ((str1 + " " + str2).equals("The Meter")) {
                wrd = "BLOCKED";
                String mtr = strsplit[2];
                String msg = results;
                if (mpesa_acc.length() < 11) {
                    msg = "OK|The KPLC Account " + mtr + " is Blocked. Please check the number and try again or Contact Support services for assistance at 0709711000.";
                } else {
                    msg = "OK|The KPLC Meter " + mtr + " is Blocked. Please check the number and try again or Contact Support services for assistance at 0709711000.";
                }
                setStatus("error");
                wrd = msg;
            } else if ((str1 + " " + str2).equals("Token generation")) {
                wrd = "Token generation";
                String mtr = strsplit[2];
                String msg = "Dear Customer,We are experiencing KPLC delays. Once the system is up we will resend your transaction. Queries call 0709711000";

                setStatus("delay");
                wrd = msg;
            } else if ((str1 + " " + str2).equals("The system")) {
                wrd = "The system busy";
                String mtr = strsplit[2];
                String msg = "Dear Customer,We are experiencing KPLC delays. Once the system is up we will resend your transaction. Queries call 0709711000";

                setStatus("delay");
                wrd = msg;
            } else if ((str1 + " " + str2).equals("General Error")) {
                wrd = "General Error";
                String mtr = strsplit[2];
                String msg = "Dear Customer,We are experiencing KPLC delays. Once the system is up we will resend your transaction. Queries call 0709711000";

                setStatus("error");
                wrd = msg;
            } else if (str.startsWith("MTRFE013")) {
                wrd = "Insufficient";
                String inAmt = strsplit[6];
                String charge = strsplit[12];
                String msg = "FAILED | Your KPLC meter has accumulated a time based charge of " + charge + "; Please retry with an amount higher than  " + inAmt + "  or call us at 0709711000";

                setStatus("insufficient");
                wrd = msg;
            } else if (str.startsWith("MTRFE008")) {
                wrd = "Insufficient";
                String inAmt = strsplit[5];
                String charge = strsplit[15];
                String msg = "FAILED | Your KPLC meter has accumulated a time based charge; Please retry with an amount higher than  " + inAmt + "  or call us at 0709711000";
                setStatus("insufficient");
                wrd = msg;
            } else if (str.startsWith("MTRFE010")) {
                wrd = "No FBE Token is due Queries ph:0733-603040.";
                String msg = "OK|MTRFE010: KPLC Specified transaction already processed. Queries ph:0709711000.";

                setStatus("fbe");
                wrd = msg;
            } else if (str.startsWith("MTRFE027")) {
                wrd = "Cleared balance.";
                String msg = "OK|MTRFE027: You have only cleared your KPLC balance. Now you can purchase tokens. Queries ph:0709711000.";

                setStatus("insufficient");
                wrd = msg;
            } else {
                wrd = "not editted";
                wrd = results;
                setStatus("error");
            }
        } catch (Exception e) {
            Logger.getLogger(MessageEditor.class.getName()).log(Level.INFO, "Message editing error:" + e);
        }
        return wrd;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

    public static void main(String[] args) {
        String message1 = "OK|token:3900 4621 9523 6738 4026 Ksh:400.00 (elec:232.55 charges:167.45) units:27.9 http://gug.mobi/r/I5LBM358 ref:607612272431 mtr:..7347";
        String message2 = "OK|The specified MSNO 38155568324 did not pass the Luhn validation. Please specify a valid MSNO. ALERT ID-010072 Contact the Support services for assistance if required. Queries ph:0733-603040.";
        String message3 = "OK|MTRFE013-M1: There is an insufficient amount (148) for the time based charges (348). Either tender more money or request less units. Queries ph:0733-603040.";
        String message4 = "OK|The Meter '14104342101' Status was expected to be 'Active' but was found to be 'Blocked '. ALERT ID-010035 Contact the Support services for assistance if required. Queries ph:0733-603040.";
        String message5 = "OK|account:3625791-01 Ksh:1900.00 http://gug.mobi/r/IJCJ7FEE ref:601310545623 receipt:160131102470996129";
        String message6 = "OK|Cannot find meter/account number. Please correct and try again, we will add your 240/= to the next purchase. Queries ph:0733-603040.";
        String message7 = "OK|MTRFE010: No FBE Token is due Queries ph:0733-603040.";
        String message8 = "Failed: Amount requested too small. Try larger amount. Please contact your vendor.";
        String message9 = "Failed: The Meter 14252484648 is unknown/not registered. ALERT ID-010117 Contact the Support services for assistance if required. Please contact your vendor.";
        String message10 = "OK|MTRFE008: The debt amount due (720) is more than or equal to the purchase amount (400). This is not allowed. Please tender more money. Queries ph:0707-373794.";

        DateTime dt = new DateTime();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss");
        String datee = fmt.print(dt);
        MessageEditor msEObj = new MessageEditor();
        String ref = msEObj.editToken(message10, "37150903232", "254728064120", "VendIT", datee);
        System.out.println(ref);

        //EquitelSMS esdp = new EquitelSMS("192.168.0.217:13131", "EQUITELRX", "pdsl219", "pdsl219", "254765555050", ref, "VendIT");
        //esdp.submitMessage();
    }
}
