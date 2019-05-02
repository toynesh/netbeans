
/**
 * SmsNotificationManagerServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4  Built on : Apr 26, 2008 (06:24:30 EDT)
 */

    package org.csapi.www.wsdl.parlayx.sms.notification_manager.v2_3.service;

    /**
     *  SmsNotificationManagerServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class SmsNotificationManagerServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public SmsNotificationManagerServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public SmsNotificationManagerServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for stopSmsNotification method
            * override this method for handling normal response from stopSmsNotification operation
            */
           public void receiveResultstopSmsNotification(
                    org.csapi.www.wsdl.parlayx.sms.notification_manager.v2_3.service.SmsNotificationManagerServiceStub.StopSmsNotificationResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from stopSmsNotification operation
           */
            public void receiveErrorstopSmsNotification(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for startSmsNotification method
            * override this method for handling normal response from startSmsNotification operation
            */
           public void receiveResultstartSmsNotification(
                    org.csapi.www.wsdl.parlayx.sms.notification_manager.v2_3.service.SmsNotificationManagerServiceStub.StartSmsNotificationResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from startSmsNotification operation
           */
            public void receiveErrorstartSmsNotification(java.lang.Exception e) {
            }
                


    }
    