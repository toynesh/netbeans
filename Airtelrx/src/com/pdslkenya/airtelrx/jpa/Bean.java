/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdslkenya.airtelrx.jpa;

//import aopcspooler.entities.WarehouseAccounts;
//import aopcspooler.entities.WhAccountsImportLog;
//import aopcspooler.jpa.services.SysParameterService;
//import aopcspooler.jpa.services.WareHouseAccountsService;
//import aopcspooler.jpa.services.WhAccountsImportLogService;
//import aopcspooler.util.Conf;
//import aopcspooler.util.pojos.AOPC_AccountSummary;
import com.pdslkenya.airtelrx.entity.Snd;
import java.util.List;
import java.util.Map;

/**
 *
 * @author rmwangi
 */
public class Bean {
    private final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Bean.class.getName());
    private EjbClass ejbClass;
    
    public Bean(){
        //ejbClass = new EjbClass();
    }
    
//    public boolean createObject(Object rec){     
//        ejbClass = new EjbClass();
//        return ejbClass.create(rec);
//    }
    
    public boolean updateObject(Object rec){
        ejbClass = new EjbClass();
        return ejbClass.update(rec);
    }
    
    public List<Snd> querySms(String q, Integer[] sts, String p){
        ejbClass = new EjbClass();
        return ejbClass.getManyFromQry(q, sts, p);
    }
}