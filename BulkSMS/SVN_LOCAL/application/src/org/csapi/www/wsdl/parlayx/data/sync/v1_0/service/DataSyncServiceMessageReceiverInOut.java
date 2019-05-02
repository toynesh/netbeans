
/**
 * DataSyncServiceMessageReceiverInOut.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4  Built on : Apr 26, 2008 (06:24:30 EDT)
 */
        package org.csapi.www.wsdl.parlayx.data.sync.v1_0.service;

        /**
        *  DataSyncServiceMessageReceiverInOut message receiver
        */

        public class DataSyncServiceMessageReceiverInOut extends org.apache.axis2.receivers.AbstractInOutMessageReceiver{


        public void invokeBusinessLogic(org.apache.axis2.context.MessageContext msgContext, org.apache.axis2.context.MessageContext newMsgContext)
        throws org.apache.axis2.AxisFault{

        try {

        // get the implementation class for the Web Service
        Object obj = getTheImplementationObject(msgContext);

        DataSyncServiceSkeleton skel = (DataSyncServiceSkeleton)obj;
        //Out Envelop
        org.apache.axiom.soap.SOAPEnvelope envelope = null;
        //Find the axisOperation that has been set by the Dispatch phase.
        org.apache.axis2.description.AxisOperation op = msgContext.getOperationContext().getAxisOperation();
        if (op == null) {
        throw new org.apache.axis2.AxisFault("Operation is not located, if this is doclit style the SOAP-ACTION should specified via the SOAP Action to use the RawXMLProvider");
        }

        java.lang.String methodName;
        if((op.getName() != null) && ((methodName = org.apache.axis2.util.JavaUtils.xmlNameToJava(op.getName().getLocalPart())) != null)){

        

            if("syncMSISDNChange".equals(methodName)){
                
                org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncMSISDNChangeResponseE syncMSISDNChangeResponse1 = null;
	                        org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncMSISDNChangeE wrappedParam =
                                                             (org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncMSISDNChangeE)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncMSISDNChangeE.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               syncMSISDNChangeResponse1 =
                                                   
                                                   
                                                         skel.syncMSISDNChange(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), syncMSISDNChangeResponse1, false);
                                    } else 

            if("syncSubscriptionActive".equals(methodName)){
                
                org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncSubscriptionActiveResponseE syncSubscriptionActiveResponse3 = null;
	                        org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncSubscriptionActiveE wrappedParam =
                                                             (org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncSubscriptionActiveE)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncSubscriptionActiveE.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               syncSubscriptionActiveResponse3 =
                                                   
                                                   
                                                         skel.syncSubscriptionActive(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), syncSubscriptionActiveResponse3, false);
                                    } else 

            if("changeMSISDN".equals(methodName)){
                
                org.csapi.www.schema.parlayx.data.sync.v1_0.local.ChangeMSISDNResponseE changeMSISDNResponse5 = null;
	                        org.csapi.www.schema.parlayx.data.sync.v1_0.local.ChangeMSISDNE wrappedParam =
                                                             (org.csapi.www.schema.parlayx.data.sync.v1_0.local.ChangeMSISDNE)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    org.csapi.www.schema.parlayx.data.sync.v1_0.local.ChangeMSISDNE.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               changeMSISDNResponse5 =
                                                   
                                                   
                                                         skel.changeMSISDN(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), changeMSISDNResponse5, false);
                                    } else 

            if("syncSubscriptionData".equals(methodName)){
                
                org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncSubscriptionDataResponseE syncSubscriptionDataResponse7 = null;
	                        org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncSubscriptionDataE wrappedParam =
                                                             (org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncSubscriptionDataE)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncSubscriptionDataE.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               syncSubscriptionDataResponse7 =
                                                   
                                                   
                                                         skel.syncSubscriptionData(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), syncSubscriptionDataResponse7, false);
                                    } else 

            if("syncOrderRelation".equals(methodName)){
                
                org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncOrderRelationResponseE syncOrderRelationResponse9 = null;
	                        org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncOrderRelationE wrappedParam =
                                                             (org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncOrderRelationE)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncOrderRelationE.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               syncOrderRelationResponse9 =
                                                   
                                                   
                                                         skel.syncOrderRelation(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), syncOrderRelationResponse9, false);
                                    
            } else {
              throw new java.lang.RuntimeException("method not found");
            }
        

        newMsgContext.setEnvelope(envelope);
        }
        }
        catch (java.lang.Exception e) {
        throw org.apache.axis2.AxisFault.makeFault(e);
        }
        }
        
        //
            private  org.apache.axiom.om.OMElement  toOM(org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncMSISDNChangeE param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncMSISDNChangeE.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncMSISDNChangeResponseE param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncMSISDNChangeResponseE.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncSubscriptionActiveE param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncSubscriptionActiveE.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncSubscriptionActiveResponseE param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncSubscriptionActiveResponseE.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(org.csapi.www.schema.parlayx.data.sync.v1_0.local.ChangeMSISDNE param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(org.csapi.www.schema.parlayx.data.sync.v1_0.local.ChangeMSISDNE.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(org.csapi.www.schema.parlayx.data.sync.v1_0.local.ChangeMSISDNResponseE param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(org.csapi.www.schema.parlayx.data.sync.v1_0.local.ChangeMSISDNResponseE.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncSubscriptionDataE param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncSubscriptionDataE.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncSubscriptionDataResponseE param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncSubscriptionDataResponseE.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncOrderRelationE param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncOrderRelationE.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncOrderRelationResponseE param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncOrderRelationResponseE.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, org.csapi.www.schema.parlayx.data.sync.v1_0.local.ChangeMSISDNResponseE param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(org.csapi.www.schema.parlayx.data.sync.v1_0.local.ChangeMSISDNResponseE.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private org.csapi.www.schema.parlayx.data.sync.v1_0.local.ChangeMSISDNResponseE wrapchangeMSISDN(){
                                org.csapi.www.schema.parlayx.data.sync.v1_0.local.ChangeMSISDNResponseE wrappedElement = new org.csapi.www.schema.parlayx.data.sync.v1_0.local.ChangeMSISDNResponseE();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncOrderRelationResponseE param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncOrderRelationResponseE.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncOrderRelationResponseE wrapsyncOrderRelation(){
                                org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncOrderRelationResponseE wrappedElement = new org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncOrderRelationResponseE();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncSubscriptionDataResponseE param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncSubscriptionDataResponseE.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncSubscriptionDataResponseE wrapsyncSubscriptionData(){
                                org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncSubscriptionDataResponseE wrappedElement = new org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncSubscriptionDataResponseE();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncSubscriptionActiveResponseE param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncSubscriptionActiveResponseE.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncSubscriptionActiveResponseE wrapsyncSubscriptionActive(){
                                org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncSubscriptionActiveResponseE wrappedElement = new org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncSubscriptionActiveResponseE();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncMSISDNChangeResponseE param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncMSISDNChangeResponseE.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncMSISDNChangeResponseE wrapsyncMSISDNChange(){
                                org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncMSISDNChangeResponseE wrappedElement = new org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncMSISDNChangeResponseE();
                                return wrappedElement;
                         }
                    


        /**
        *  get the default envelope
        */
        private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory){
        return factory.getDefaultEnvelope();
        }


        private  java.lang.Object fromOM(
        org.apache.axiom.om.OMElement param,
        java.lang.Class type,
        java.util.Map extraNamespaces) throws org.apache.axis2.AxisFault{

        try {
        
                if (org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncMSISDNChangeE.class.equals(type)){
                
                           return org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncMSISDNChangeE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncMSISDNChangeResponseE.class.equals(type)){
                
                           return org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncMSISDNChangeResponseE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncSubscriptionActiveE.class.equals(type)){
                
                           return org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncSubscriptionActiveE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncSubscriptionActiveResponseE.class.equals(type)){
                
                           return org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncSubscriptionActiveResponseE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (org.csapi.www.schema.parlayx.data.sync.v1_0.local.ChangeMSISDNE.class.equals(type)){
                
                           return org.csapi.www.schema.parlayx.data.sync.v1_0.local.ChangeMSISDNE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (org.csapi.www.schema.parlayx.data.sync.v1_0.local.ChangeMSISDNResponseE.class.equals(type)){
                
                           return org.csapi.www.schema.parlayx.data.sync.v1_0.local.ChangeMSISDNResponseE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncSubscriptionDataE.class.equals(type)){
                
                           return org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncSubscriptionDataE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncSubscriptionDataResponseE.class.equals(type)){
                
                           return org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncSubscriptionDataResponseE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncOrderRelationE.class.equals(type)){
                
                           return org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncOrderRelationE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncOrderRelationResponseE.class.equals(type)){
                
                           return org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncOrderRelationResponseE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
        } catch (java.lang.Exception e) {
        throw org.apache.axis2.AxisFault.makeFault(e);
        }
           return null;
        }



    

        /**
        *  A utility method that copies the namepaces from the SOAPEnvelope
        */
        private java.util.Map getEnvelopeNamespaces(org.apache.axiom.soap.SOAPEnvelope env){
        java.util.Map returnMap = new java.util.HashMap();
        java.util.Iterator namespaceIterator = env.getAllDeclaredNamespaces();
        while (namespaceIterator.hasNext()) {
        org.apache.axiom.om.OMNamespace ns = (org.apache.axiom.om.OMNamespace) namespaceIterator.next();
        returnMap.put(ns.getPrefix(),ns.getNamespaceURI());
        }
        return returnMap;
        }

        private org.apache.axis2.AxisFault createAxisFault(java.lang.Exception e) {
        org.apache.axis2.AxisFault f;
        Throwable cause = e.getCause();
        if (cause != null) {
            f = new org.apache.axis2.AxisFault(e.getMessage(), cause);
        } else {
            f = new org.apache.axis2.AxisFault(e.getMessage());
        }

        return f;
    }

        }//end of class
    