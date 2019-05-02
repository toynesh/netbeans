/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.subscription;

import java.util.Iterator;
import javax.jws.WebService;
import javax.xml.ws.Holder;
import org.csapi.schema.parlayx.data.sync.v1_0.local.SyncOrderRelationResponse;
import org.csapi.schema.parlayx.data.v1_0.NamedParameterList;
import org.csapi.schema.parlayx.data.v1_0.ProductDetail;
import org.csapi.schema.parlayx.data.v1_0.UserID;

/**
 *
 * @author coolie
 */
@WebService(serviceName = "DataSyncService", portName = "DataSync", endpointInterface = "org.csapi.wsdl.parlayx.data.sync.v1_0.service.DataSync", targetNamespace = "http://www.csapi.org/wsdl/parlayx/data/sync/v1_0/service", wsdlLocation = "WEB-INF/wsdl/pdsl/osg_data_sync_service_1_0.wsdl")
public class pdsl {

    DataStore data = new DataStore();

    public int syncSubscriptionData(String msisdn, String serviceId, String productId, int updateType, ProductDetail productDetail) {
        System.out.println("Under syncSubscriptionData");
        System.out.println("SyncSubcription\n MSISDN " + msisdn + ":Service Id " + serviceId + ": ProductId " + productId + ": ProductDetails " + productDetail.getSubscriptionAddtionalInfo() + "," + " " + productDetail.getNotifySPURL() + ", " + productDetail.getSubscriptionValidTime() + ", " + productDetail.getNotifySPURL());

        return 0;
    }

    public int changeMSISDN(String msisdn, String newMSISDN, String timeStamp) {
        System.out.println("Under Change MSISDN");
        System.out.println(msisdn);
        System.out.println(newMSISDN);
        System.out.println(timeStamp);
        return 0;
    }

    public SyncOrderRelationResponse syncOrderRelation(UserID userID, String spID, String productID, String serviceID, String serviceList, int updateType, String updateTime, String updateDesc, String effectiveTime, String expiryTime, Holder<NamedParameterList> extensionInfo, Holder<Integer> result, Holder<String> resultDescription) {
        try {
            System.out.println("Under syncOrderRelation");
            System.out.println("User ID:\t" + userID.getID());
            System.out.println("Product ID:\t" + productID);
            System.out.println("Service ID:\t" + serviceID);
            System.out.println("Service List:\t" + serviceList);
            System.out.println("Update Type:\t" + updateType);
            System.out.println("Update Time:\t" + updateTime);
            System.out.println("Update Description:\t" + updateDesc);
            System.out.println("Effective Time:\t" + effectiveTime);
            System.out.println("Expiry Time:\t" + expiryTime);
            Iterator it = ((NamedParameterList) extensionInfo.value).getItem().iterator();

            System.out.println(((NamedParameterList) extensionInfo.value).getItem().toString());
            System.out.println("Result :\t" + result.value);
            System.out.println("Result Description:\t" + (String) resultDescription.value);
            String insert = "insert into subscription(subscription,productid,serviceid,servicelist,updatetime,updatedesc,effectivetime,expirytime)values ('" + userID.getID() + "','" + productID + "','" + serviceID + "','" + serviceList + "','" + updateTime + "','" + updateDesc + "','" + effectiveTime + "','" + expiryTime + "')";
            if (serviceID.equals("6014692000155840")) {
                insert = "insert into dalawasubscription(subscription,productid,serviceid,servicelist,updatetime,updatedesc,effectivetime,expirytime)values ('" + userID.getID() + "','" + productID + "','" + serviceID + "','" + serviceList + "','" + updateTime + "','" + updateDesc + "','" + effectiveTime + "','" + expiryTime + "')";
            }

            this.data.insert(insert);

            SyncOrderRelationResponse response = new SyncOrderRelationResponse();

            System.out.println("Result NEW!! Description:\t");

            return response;
        } catch (Exception ex) {
            SyncOrderRelationResponse response = new SyncOrderRelationResponse();

            System.out.println("Result Description:\t");

            return response;
        }
    }

    public void syncMSISDNChange(String msisdn, String newMSISDN, Holder<NamedParameterList> extensionInfo, Holder<Integer> result, Holder<String> resultDescription) {
        System.out.println("Under syncMSISDNChange");

        System.out.println(newMSISDN);
        System.out.println(((NamedParameterList) extensionInfo.value).getItem().toString());
        System.out.println(result.value);
        System.out.println((String) resultDescription.value);
    }

}
