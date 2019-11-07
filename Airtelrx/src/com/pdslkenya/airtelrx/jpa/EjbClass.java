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

import java.util.Arrays;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class EjbClass {
    private final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(EjbClass.class.getName());
    
    private EntityManager em;
    private EmProvider emf;
    private String message;

    public EjbClass() {
        try {
            emf = EmProvider.getInstance();
            this.em = emf.getEntityManagerFactory().createEntityManager();
        } catch (Exception e) {
            //e.printStackTrace();
            logger.error("Failed to initialize entity manager", e);
        }
    }
    
    public boolean update(Object entity) {
        boolean res = false;
        try {
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
            res = true;
        } catch (Exception e) {
            logger.fatal("Error in updating Object: " + e.getMessage(), e);
        } finally {
            if (em != null) {
                if (em.isOpen()) {
                    em.close();
                }
            }
            return res;
        }
    }
    
    public <T extends Object> T getManyFromQry(String query, Integer[] status, String param) {
        T res = null;
        try {

            logger.info("EM isOpen:" + em.isOpen());
            Query qry = em.createQuery(query);
            qry.setParameter(param, Arrays.asList(status));
            qry.setParameter("smsc", "AIRTELRX");
            res = (T) qry.getResultList();
        } catch (Exception e) {
            logger.fatal("Error in updating Ob ject: " + e.getMessage(), e);
        } finally {
            if (em != null) {
                if (em.isOpen()) {
                    em.close();
                }
            }
        }
        return res;
    }

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public EmProvider getEmf() {
        return emf;
    }

    public void setEmf(EmProvider emf) {
        this.emf = emf;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
