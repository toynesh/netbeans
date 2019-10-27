/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdslkenya.airtelrx.jpa.util;

/**
 *
 * @author rmwangi
 * This class provides the 
 */
public class Conf {
    public static final String PU = "AirtelrxPU";
    
    public static final String SYSTEMCONFIGFILE = "/home/tesla/airtelrx/engine.cfg";//"/etc/airtelrx/engine.cfg";
    
    public static final String SMSCHOSTADDRESS = "SMSC.HOST.ADDRESS";
    
    public static final String SMSCHOSTPORT = "SMSC.HOST.PORT";
    
    public static final String SMSCSYSTEMID = "SMSC.SYSTEM.ID";
    
    public static final String SMSCPASSWORD = "SMSC.PASSWORD";
    
    public static final String SMSCSYSTEMTYPE = "SMSC.SYSTEM.TYPE";
    
    public static final String SMSCSOURCETON = "SMSC.SOURCE.TON";
    
    public static final String SMSCSOURCENPI = "SMSC.SOURCE.NPI";
    
    public static final String SMSCSOURCEADDRESS = "SMSC.SOURCE.ADDRESS";
    
    //public static final String SYS_SWAPDIR = "/app/airtelrx/swap/";
    public static final int STATUSNOTSENT = 0;
    
    public static final int STATUSSENT = 1;
    
    public static final String queryKey = "q", sendKey = "s";
    
    public static final int MAXCONSUMERS = 100, MAXCONSUMERWAITE=333;
}
