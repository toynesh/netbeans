/**
 * DataSyncServiceSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4  Built on : Apr 26, 2008 (06:24:30 EDT)
 */
package org.csapi.www.wsdl.parlayx.data.sync.v1_0.service;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axis2.databinding.ADBException;
import org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncOrderRelationE;

/**
 *  DataSyncServiceSkeleton java skeleton for the axisService
 */
public class DataSyncServiceSkeleton
{
    
    /**
     * Auto generated method signature
     * 
                                 * @param syncMSISDNChange
     */
    
    public org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncMSISDNChangeResponseE syncMSISDNChange(
        org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncMSISDNChangeE syncMSISDNChange)
    {
        //TODO : fill this with the necessary business logic
        throw new java.lang.UnsupportedOperationException("Please implement " + this.getClass().getName()
            + "#syncMSISDNChange");
    }
    
    /**
     * Auto generated method signature
     * 
                                 * @param syncSubscriptionActive
     */
    
    public org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncSubscriptionActiveResponseE syncSubscriptionActive(
        org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncSubscriptionActiveE syncSubscriptionActive)
    {
        //TODO : fill this with the necessary business logic
        throw new java.lang.UnsupportedOperationException("Please implement " + this.getClass().getName()
            + "#syncSubscriptionActive");
    }
    
    /**
     * Auto generated method signature
     * 
                                 * @param changeMSISDN
     */
    
    public org.csapi.www.schema.parlayx.data.sync.v1_0.local.ChangeMSISDNResponseE changeMSISDN(
        org.csapi.www.schema.parlayx.data.sync.v1_0.local.ChangeMSISDNE changeMSISDN)
    {
        //TODO : fill this with the necessary business logic
        throw new java.lang.UnsupportedOperationException("Please implement " + this.getClass().getName()
            + "#changeMSISDN");
    }
    
    /**
     * Auto generated method signature
     * 
                                 * @param syncSubscriptionData
     */
    
    public org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncSubscriptionDataResponseE syncSubscriptionData(
        org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncSubscriptionDataE syncSubscriptionData)
    {
        //TODO : fill this with the necessary business logic
        throw new java.lang.UnsupportedOperationException("Please implement " + this.getClass().getName()
            + "#syncSubscriptionData");
    }
    
    /**
     * Auto generated method signature
     * 
                                 * @param syncOrderRelation
     */
    
    public org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncOrderRelationResponseE syncOrderRelation(
        org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncOrderRelationE syncOrderRelation)
    {
        //TODO : fill this with the necessary business logic
        org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncOrderRelationResponseE rspE =
            new org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncOrderRelationResponseE();
        org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncOrderRelationResponse rsp =
            new org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncOrderRelationResponse();
        rsp.setResult(0);
        rsp.setResultDescription("success");
        try
        {
            // print original request
            System.out.println("---receive request:---");
            System.out.println(syncOrderRelation.getSyncOrderRelation().getOMElement(SyncOrderRelationE.MY_QNAME,
                OMAbstractFactory.getSOAP11Factory()));
            
            // print every parameter
            System.out.println("detail info:");
            System.out.println("userID.type=" + syncOrderRelation.getSyncOrderRelation().getUserID().getType());
            System.out.println("userID.ID=" + syncOrderRelation.getSyncOrderRelation().getUserID().getID());
            System.out.println("spID=" + syncOrderRelation.getSyncOrderRelation().getSpID());
            System.out.println("productID=" + syncOrderRelation.getSyncOrderRelation().getProductID());
            System.out.println("serviceID=" + syncOrderRelation.getSyncOrderRelation().getServiceID());
            
            System.out.println("serviceList="
                + (null == syncOrderRelation.getSyncOrderRelation().getServiceList() ? ""
                    : syncOrderRelation.getSyncOrderRelation().getServiceList()));
            System.out.println("updateType=" + syncOrderRelation.getSyncOrderRelation().getUpdateType());
            System.out.println("updateTime=" + syncOrderRelation.getSyncOrderRelation().getUpdateTime());
            System.out.println("updateDesc="
                + (null == syncOrderRelation.getSyncOrderRelation().getUpdateDesc() ? ""
                    : syncOrderRelation.getSyncOrderRelation().getUpdateDesc()));
            System.out.println("effectiveTime="
                + (null == syncOrderRelation.getSyncOrderRelation().getEffectiveTime() ? ""
                    : syncOrderRelation.getSyncOrderRelation().getEffectiveTime()));
            System.out.println("expiryTime="
                + (null == syncOrderRelation.getSyncOrderRelation().getExpiryTime() ? ""
                    : syncOrderRelation.getSyncOrderRelation().getExpiryTime()));
            
            if (null != syncOrderRelation.getSyncOrderRelation().getExtensionInfo())
            {
                int i = 0;
                org.csapi.www.schema.parlayx.data.v1_0.NamedParameterList extList =
                    syncOrderRelation.getSyncOrderRelation().getExtensionInfo();
                for (org.csapi.www.schema.parlayx.data.v1_0.NamedParameter item : extList.getItem())
                {
                    System.out.println("ExtensionInfo" + i + ".key=" + item.getKey());
                    System.out.println("ExtensionInfo" + i + ".value=" + item.getValue());
                    i++;
                }
            }
        }
        catch (ADBException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            rsp.setResult(1);
            rsp.setResultDescription("decode failed.");
        }
        rspE.setSyncOrderRelationResponse(rsp);
        return rspE;
    }
    
}
