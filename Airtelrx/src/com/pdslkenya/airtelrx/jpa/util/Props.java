/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdslkenya.airtelrx.jpa.util;

/**
 *
 * @author rmwangi
 */

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Logger;

public class Props {
    protected String smscHost, smscPort, smscSysId, smscPasswd, smscType, sTone, sNpi, smscSC;
    private Properties props;
    private FileInputStream propsStream = null;
    private final Logger logger = Logger.getLogger(Props.class.getSimpleName());
    
    @SuppressWarnings({"UseOfSystemOutOrSystemErr", "unchecked"})
    public Props() throws Exception{
        try{
            props = new Properties();
            propsStream = new FileInputStream(Conf.SYSTEMCONFIGFILE);
            props.load(propsStream);
            
            smscHost = props.getProperty(Conf.SMSCHOSTADDRESS);
            smscPort = props.getProperty(Conf.SMSCHOSTPORT);
            smscSysId = props.getProperty(Conf.SMSCSYSTEMID);
            smscPasswd = props.getProperty(Conf.SMSCPASSWORD);
            smscType = props.getProperty(Conf.SMSCSYSTEMTYPE);
            sTone = props.getProperty(Conf.SMSCSOURCETON);
            sNpi = props.getProperty(Conf.SMSCSOURCENPI);
            smscSC = props.getProperty(Conf.SMSCSOURCEADDRESS);
            
        }  catch (IOException ioe) {
            logger.error("Exiting. Failed to load system properties: "
                    , ioe);
            throw new Exception("Unable to parse properties file: " + Conf.SYSTEMCONFIGFILE);
           
        } finally{
            if(propsStream != null){
                propsStream.close();
            }
        }
    }

    public String getSmscHost() {
        return smscHost;
    }

    public void setSmscHost(String smscHost) {
        this.smscHost = smscHost;
    }

    public String getSmscPort() {
        return smscPort;
    }

    public void setSmscPort(String smscPort) {
        this.smscPort = smscPort;
    }

    public String getSmscSysId() {
        return smscSysId;
    }

    public void setSmscSysId(String smscSysId) {
        this.smscSysId = smscSysId;
    }

    public String getSmscPasswd() {
        return smscPasswd;
    }

    public void setSmscPasswd(String smscPasswd) {
        this.smscPasswd = smscPasswd;
    }

    public String getSmscType() {
        return smscType;
    }

    public void setSmscType(String smscType) {
        this.smscType = smscType;
    }

    public String getsTone() {
        return sTone;
    }

    public void setsTone(String sTone) {
        this.sTone = sTone;
    }

    public String getsNpi() {
        return sNpi;
    }

    public void setsNpi(String sNpi) {
        this.sNpi = sNpi;
    }

    public String getSmscSC() {
        return smscSC;
    }

    public void setSmscSC(String smscSC) {
        this.smscSC = smscSC;
    }    
}
