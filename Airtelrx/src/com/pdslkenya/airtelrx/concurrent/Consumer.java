/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdslkenya.airtelrx.concurrent;

import com.pdslkenya.airtelrx.entity.Snd;
import com.pdslkenya.airtelrx.jpa.Bean;
import com.pdslkenya.airtelrx.jpa.util.Conf;
import com.pdslkenya.airtelrx.jpa.util.Props;
import com.pdslkenya.airtelrx.smpp.Bind;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.log4j.Logger;

/**
 *
 * @author tesla
 */
public class Consumer implements Runnable {
    
    private final Logger logger = Logger.getLogger(Consumer.class.getName());
    protected LinkedBlockingQueue<Map<String, List<Snd>>> dataQ;
    protected Bean bean;
    protected Props props;
    private Bind bind;
    
    public Consumer(LinkedBlockingQueue<Map<String, List<Snd>>> dataQ, Bean bean, Props props){
        this.dataQ = dataQ;
        this.bean = bean;
        this.props = props;
    }

    @Override
    public void run() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        while(true){
            try{
                if(!dataQ.isEmpty()){
                    Map<String, List<Snd>> mixedData = dataQ.take();
                    processData(mixedData);
                } 
            } catch(Exception ex){
                logger.error("Consumer exception, unable to fetch from un-empty queue: " + ex.getMessage(), ex);
            }
        }
    }
    
    public void processData(Map<String, List<Snd>> mixedData){
        if(mixedData.containsKey(Conf.queryKey)){
            List<Snd> qSMS = mixedData.get(Conf.queryKey);
            for(Snd nd : qSMS){
                try{
                    bind = new Bind(nd, bean);
                    bind.execute();
                    // wait for binding to complete
                    // canSendSMS = false, shouldWait = true;
                    while(bind.canSendSMS == false && bind.shouldWait == true){
                        try{
                            Thread.sleep(1);
                        } catch(Exception ex){
                            logger.error("Inturrupted while waiting for bind ", ex);
                        }
                    }
                    
                    if(bind.canSendSMS == true){
                        bind.query();
                    }
                } catch(Exception ex){
                    logger.error("Unable to query sms status ", ex);
                }
                
            }
            
        }
        
        if(mixedData.containsKey(Conf.sendKey)){
            List<Snd> sSMS = mixedData.get(Conf.sendKey);
            for(Snd nd : sSMS){
                try{
                    bind = new Bind(nd, bean);
                    bind.execute();
                    // wait for binding to complete
                    // canSendSMS = false, shouldWait = true;
                    int waitCounter = 0;
                    while((bind.canSendSMS == false && bind.shouldWait == true) && waitCounter < Conf.MAXCONSUMERWAITE){
                        try{
                            Thread.sleep(1);
                            
                        } catch(Exception ex){
                            logger.error("Inturrupted while waiting for bind ", ex);
                        }
                        waitCounter ++;
                    }
                    
                    if(bind.canSendSMS == true){
                        bind.send();
                    }
                } catch(Exception ex){
                    logger.error("Unable to query sms status ", ex);
                }
            }
        }
    }
}
