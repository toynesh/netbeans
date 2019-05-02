
/**
 * KenyaAPICallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package org.tempuri;

    /**
     *  KenyaAPICallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class KenyaAPICallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public KenyaAPICallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public KenyaAPICallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for sMSAPI method
            * override this method for handling normal response from sMSAPI operation
            */
           public void receiveResultsMSAPI(
                    org.tempuri.KenyaAPIStub.SMSAPIResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from sMSAPI operation
           */
            public void receiveErrorsMSAPI(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for mPESA_PDSL method
            * override this method for handling normal response from mPESA_PDSL operation
            */
           public void receiveResultmPESA_PDSL(
                    org.tempuri.KenyaAPIStub.MPESA_PDSLResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from mPESA_PDSL operation
           */
            public void receiveErrormPESA_PDSL(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for mKINFO method
            * override this method for handling normal response from mKINFO operation
            */
           public void receiveResultmKINFO(
                    org.tempuri.KenyaAPIStub.MKINFOResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from mKINFO operation
           */
            public void receiveErrormKINFO(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for uSSD_MENU method
            * override this method for handling normal response from uSSD_MENU operation
            */
           public void receiveResultuSSD_MENU(
                    org.tempuri.KenyaAPIStub.USSD_MENUResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from uSSD_MENU operation
           */
            public void receiveErroruSSD_MENU(java.lang.Exception e) {
            }
                


    }
    