/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.vendit.sms;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author coolie
 */
public class PhonePrefixes {

    public String checkPhonePrefix(String inputPhoneNumber) {
        String res = null;
        String validPhoneNumber = null;
        Pattern patternSaf = Pattern.compile("^(?:254|\\+254|0)?(7(?:(?:[129][0-9])|(?:0[0-8])|(4[0-9]))[0-9]{6})$");
        Matcher matcherSaf = patternSaf.matcher(inputPhoneNumber);

        //Airtel
        Pattern patternArt = Pattern.compile("^(?:254|\\+254|0)?(7(?:(?:[3][0-9])|(?:5[0-9])|(8[0-9]))[0-9]{6})$");
        Matcher matcherArt = patternArt.matcher(inputPhoneNumber);

        //Orange/Telkom
        Pattern patternOrg = Pattern.compile("^(?:254|\\+254|0)?(77[0-9][0-9]{6})$");
        Matcher matcherOrg = patternOrg.matcher(inputPhoneNumber);

        //Equitel
        Pattern patternEqui = Pattern.compile("^(?:254|\\+254|0)?(76[0-9][0-9]{6})");
        Matcher matcherEqui = patternEqui.matcher(inputPhoneNumber);

        //JTL
        Pattern patternJTL = Pattern.compile("^(?:254|\\+254|0)?(74[47][0-9]{6})");
        Matcher matcherJTL = patternJTL.matcher(inputPhoneNumber);

        if (matcherJTL.matches()) {
            validPhoneNumber = "254" + matcherJTL.group(1);
            System.out.println("JTL " + validPhoneNumber);
            res = "JTL";
        } else if (matcherSaf.matches()) {
            validPhoneNumber = "254" + matcherSaf.group(1);
            System.out.println("SAF " + validPhoneNumber);
            res = "SAFARICOM";
        } else if (matcherArt.matches()) {
            validPhoneNumber = "254" + matcherArt.group(1);
            System.out.println("AIRTEL " + validPhoneNumber);
            res = "AIRTELRX";
        } else if (matcherOrg.matches()) {
            validPhoneNumber = "254" + matcherOrg.group(1);
            System.out.println("ORANGE " + validPhoneNumber);
            res = "TELKOMRX";

        } else if (matcherEqui.matches()) {
            validPhoneNumber = "254" + matcherEqui.group(1);
            System.out.println("Equi " + validPhoneNumber);
            res = "EQUITELRX";

        } else {
            if (inputPhoneNumber.startsWith("25420")) {
                System.out.println("Orange " + validPhoneNumber);
                res = "TELKOMRX";
            } else {
                validPhoneNumber = inputPhoneNumber;
                System.out.println("HATUJUI " + validPhoneNumber);
            }
        }
        return res;
    }
    public static void main(String[] args) {
        PhonePrefixes prfx = new PhonePrefixes();
        System.out.println(prfx.checkPhonePrefix("254728064120"));
    }
}
