/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdslkenya.airtelrx.concurrent;

import com.pdslkenya.airtelrx.entity.Snd;
import com.pdslkenya.airtelrx.jpa.Bean;
import com.pdslkenya.airtelrx.jpa.util.Conf;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.log4j.Logger;

/**
 *
 * @author tesla
 */
public class Producer implements Runnable {

    private final Logger logger = Logger.getLogger(Producer.class.getName());
    protected LinkedBlockingQueue<Map<String, List<Snd>>> dataQ;
    protected Bean bean;

    public Producer(LinkedBlockingQueue<Map<String, List<Snd>>> dataQ, Bean bean) {
        this.dataQ = dataQ;
        this.bean = bean;
    }

    @Override
    public void run() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        Integer[] status = {0, 1};
        String query = "select t from Snd t where t.sndSmsc=:smsc and t.status in :st", id = "st";

        while (true) {
            // Get sms
            List<Snd> sms = bean.querySms(query, status, id);
            logger.info("############ List size: " + sms.size());
            Map<String, List<Snd>> data = new HashMap<>();
            List<Snd> qList = new ArrayList<>();
            List<Snd> sList = new ArrayList<>();
            if (!sms.isEmpty()) {
                for (Snd nd : sms) {
                    if (nd.getStatus() == 0) {
                        sList.add(nd);
                    } else {
                        qList.add(nd);
                    }
                }
                if (!sList.isEmpty()) {
                    data.put(Conf.sendKey, sList);
                }
                if (!qList.isEmpty()) {
                    data.put(Conf.queryKey, qList);
                }
                addToQueue(data);
            }
        }
    }

    public void addToQueue(Map<String, List<Snd>> wha) {
        try {
            dataQ.put(wha);
        } catch (Exception ex) {
            logger.error("Error in Queing. " + ex.getMessage(), ex);
        }

        try {
            Thread.sleep(60000);
        } catch (InterruptedException ex) {
            //java.util.logging.Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
            logger.error("Producer thread interrupted");
        }
    }

}
