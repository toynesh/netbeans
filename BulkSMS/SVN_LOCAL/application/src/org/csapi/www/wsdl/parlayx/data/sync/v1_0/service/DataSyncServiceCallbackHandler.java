
/**
 * DataSyncServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4  Built on : Apr 26, 2008 (06:24:30 EDT)
 */

    package org.csapi.www.wsdl.parlayx.data.sync.v1_0.service;

    /**
     *  DataSyncServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class DataSyncServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public DataSyncServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public DataSyncServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for syncMSISDNChange method
            * override this method for handling normal response from syncMSISDNChange operation
            */
           public void receiveResultsyncMSISDNChange(
                    org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncMSISDNChangeResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from syncMSISDNChange operation
           */
            public void receiveErrorsyncMSISDNChange(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for syncSubscriptionActive method
            * override this method for handling normal response from syncSubscriptionActive operation
            */
           public void receiveResultsyncSubscriptionActive(
                    org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncSubscriptionActiveResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from syncSubscriptionActive operation
           */
            public void receiveErrorsyncSubscriptionActive(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for changeMSISDN method
            * override this method for handling normal response from changeMSISDN operation
            */
           public void receiveResultchangeMSISDN(
                    org.csapi.www.schema.parlayx.data.sync.v1_0.local.ChangeMSISDNResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from changeMSISDN operation
           */
            public void receiveErrorchangeMSISDN(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for syncSubscriptionData method
            * override this method for handling normal response from syncSubscriptionData operation
            */
           public void receiveResultsyncSubscriptionData(
                    org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncSubscriptionDataResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from syncSubscriptionData operation
           */
            public void receiveErrorsyncSubscriptionData(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for syncOrderRelation method
            * override this method for handling normal response from syncOrderRelation operation
            */
           public void receiveResultsyncOrderRelation(
                    org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncOrderRelationResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from syncOrderRelation operation
           */
            public void receiveErrorsyncOrderRelation(java.lang.Exception e) {
            }
                


    }
    