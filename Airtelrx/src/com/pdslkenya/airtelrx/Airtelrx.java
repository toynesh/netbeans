/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdslkenya.airtelrx;

/**
 *
 * @author tesla
 */
import com.pdslkenya.airtelrx.concurrent.Consumer;
import com.pdslkenya.airtelrx.concurrent.Producer;
import com.pdslkenya.airtelrx.entity.Snd;
import com.pdslkenya.airtelrx.jpa.Bean;
import com.pdslkenya.airtelrx.jpa.util.Conf;
import com.pdslkenya.airtelrx.jpa.util.Props;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.log4j.Logger;

public class Airtelrx {

    private static final Logger logger = Logger.getLogger(Airtelrx.class.getName());
    protected static Bean bean;
    public static Props props;

    public static Object lock = new Object();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        //System.out.println("Starting");
        logger.debug("-------------------------------------------------------");
        logger.debug("--------------- Airtelx Spooler ENGINE ----------------");
        logger.debug("-------------------------------------------------------");

        logger.debug("Initializing properties...");
        try {
            System.out.println("Loading...");
            props = new Props();
            LinkedBlockingQueue dataQ = new LinkedBlockingQueue<>(500);
            bean = new Bean();
            Executors.newSingleThreadExecutor().execute(new Producer(dataQ, bean));
            ExecutorService postExecs = Executors.newFixedThreadPool(Conf.MAXCONSUMERS);
            for (int i = 0; i < Conf.MAXCONSUMERS; i++) {
                postExecs.execute(new Consumer(dataQ, bean, props));
            }
            logger.info("Engine started!");
        } catch (Exception ex) {
            //System.out.println("Error!!!!");
            logger.fatal("Fatal Error. Engine Terminating!", ex);
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {                    
                    logger.info("Application stopped");
                }
            });
        }
    }
}
