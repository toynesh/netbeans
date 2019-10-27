/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdslkenya.airtelrx.jpa;

/**
 *
 * @author rmwangi
 */

//import aopcspooler.util.Conf;
import com.pdslkenya.airtelrx.jpa.util.Conf;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EmProvider {
    private static EmProvider provider;
    
    private EntityManagerFactory emf;
    
    private final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(EmProvider.class.getName());
    
    private EmProvider(){
        
    }
    
    public static EmProvider getInstance(){
        if(provider == null){
            provider = new EmProvider();
        }
        return provider;
    }
    
    public EntityManagerFactory getEntityManagerFactory(){
        
        try{
            if(emf == null){
                emf = Persistence.createEntityManagerFactory(Conf.PU);
            }
        } catch(Exception ex){
            logger.fatal("Failed to create EM factory", ex);
        }
        
        return emf;
    }
}
