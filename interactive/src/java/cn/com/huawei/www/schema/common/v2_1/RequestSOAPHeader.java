
/**
 * RequestSOAPHeader.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4  Built on : Apr 26, 2008 (06:25:17 EDT)
 */
            
                package cn.com.huawei.www.schema.common.v2_1;
            

            /**
            *  RequestSOAPHeader bean class
            */
        
        public  class RequestSOAPHeader
        implements org.apache.axis2.databinding.ADBBean{
        /* This type was generated from the piece of schema that had
                name = RequestSOAPHeader
                Namespace URI = http://www.huawei.com.cn/schema/common/v2_1
                Namespace Prefix = ns1
                */
            

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://www.huawei.com.cn/schema/common/v2_1")){
                return "ns1";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        

                        /**
                        * field for SpId
                        */

                        
                                    protected java.lang.String localSpId ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localSpIdTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getSpId(){
                               return localSpId;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param SpId
                               */
                               public void setSpId(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localSpIdTracker = true;
                                       } else {
                                          localSpIdTracker = false;
                                              
                                       }
                                   
                                            this.localSpId=param;
                                    

                               }
                            

                        /**
                        * field for SpPassword
                        */

                        
                                    protected java.lang.String localSpPassword ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localSpPasswordTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getSpPassword(){
                               return localSpPassword;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param SpPassword
                               */
                               public void setSpPassword(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localSpPasswordTracker = true;
                                       } else {
                                          localSpPasswordTracker = false;
                                              
                                       }
                                   
                                            this.localSpPassword=param;
                                    

                               }
                            

                        /**
                        * field for ServiceId
                        */

                        
                                    protected java.lang.String localServiceId ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localServiceIdTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getServiceId(){
                               return localServiceId;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ServiceId
                               */
                               public void setServiceId(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localServiceIdTracker = true;
                                       } else {
                                          localServiceIdTracker = false;
                                              
                                       }
                                   
                                            this.localServiceId=param;
                                    

                               }
                            

                        /**
                        * field for TimeStamp
                        */

                        
                                    protected java.lang.String localTimeStamp ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localTimeStampTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getTimeStamp(){
                               return localTimeStamp;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param TimeStamp
                               */
                               public void setTimeStamp(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localTimeStampTracker = true;
                                       } else {
                                          localTimeStampTracker = false;
                                              
                                       }
                                   
                                            this.localTimeStamp=param;
                                    

                               }
                            

                        /**
                        * field for OA
                        */

                        
                                    protected java.lang.String localOA ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localOATracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getOA(){
                               return localOA;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param OA
                               */
                               public void setOA(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localOATracker = true;
                                       } else {
                                          localOATracker = false;
                                              
                                       }
                                   
                                            this.localOA=param;
                                    

                               }
                            

                        /**
                        * field for Oauth_token
                        */

                        
                                    protected java.lang.String localOauth_token ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localOauth_tokenTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getOauth_token(){
                               return localOauth_token;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Oauth_token
                               */
                               public void setOauth_token(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localOauth_tokenTracker = true;
                                       } else {
                                          localOauth_tokenTracker = false;
                                              
                                       }
                                   
                                            this.localOauth_token=param;
                                    

                               }
                            

                        /**
                        * field for FA
                        */

                        
                                    protected java.lang.String localFA ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localFATracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getFA(){
                               return localFA;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param FA
                               */
                               public void setFA(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localFATracker = true;
                                       } else {
                                          localFATracker = false;
                                              
                                       }
                                   
                                            this.localFA=param;
                                    

                               }
                            

                        /**
                        * field for Token
                        */

                        
                                    protected java.lang.String localToken ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localTokenTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getToken(){
                               return localToken;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Token
                               */
                               public void setToken(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localTokenTracker = true;
                                       } else {
                                          localTokenTracker = false;
                                              
                                       }
                                   
                                            this.localToken=param;
                                    

                               }
                            

                        /**
                        * field for Watcher
                        */

                        
                                    protected java.lang.String localWatcher ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localWatcherTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getWatcher(){
                               return localWatcher;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Watcher
                               */
                               public void setWatcher(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localWatcherTracker = true;
                                       } else {
                                          localWatcherTracker = false;
                                              
                                       }
                                   
                                            this.localWatcher=param;
                                    

                               }
                            

                        /**
                        * field for Presentity
                        */

                        
                                    protected java.lang.String localPresentity ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localPresentityTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getPresentity(){
                               return localPresentity;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Presentity
                               */
                               public void setPresentity(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localPresentityTracker = true;
                                       } else {
                                          localPresentityTracker = false;
                                              
                                       }
                                   
                                            this.localPresentity=param;
                                    

                               }
                            

                        /**
                        * field for AuthId
                        */

                        
                                    protected java.lang.String localAuthId ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localAuthIdTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getAuthId(){
                               return localAuthId;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param AuthId
                               */
                               public void setAuthId(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localAuthIdTracker = true;
                                       } else {
                                          localAuthIdTracker = false;
                                              
                                       }
                                   
                                            this.localAuthId=param;
                                    

                               }
                            

                        /**
                        * field for Linkid
                        */

                        
                                    protected java.lang.String localLinkid ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localLinkidTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getLinkid(){
                               return localLinkid;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Linkid
                               */
                               public void setLinkid(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localLinkidTracker = true;
                                       } else {
                                          localLinkidTracker = false;
                                              
                                       }
                                   
                                            this.localLinkid=param;
                                    

                               }
                            

                        /**
                        * field for Presentid
                        */

                        
                                    protected java.lang.String localPresentid ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localPresentidTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getPresentid(){
                               return localPresentid;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Presentid
                               */
                               public void setPresentid(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localPresentidTracker = true;
                                       } else {
                                          localPresentidTracker = false;
                                              
                                       }
                                   
                                            this.localPresentid=param;
                                    

                               }
                            

                        /**
                        * field for MsgType
                        */

                        
                                    protected int localMsgType ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localMsgTypeTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getMsgType(){
                               return localMsgType;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param MsgType
                               */
                               public void setMsgType(int param){
                            
                                       // setting primitive attribute tracker to true
                                       
                                               if (param==java.lang.Integer.MIN_VALUE) {
                                           localMsgTypeTracker = false;
                                              
                                       } else {
                                          localMsgTypeTracker = true;
                                       }
                                   
                                            this.localMsgType=param;
                                    

                               }
                            

     /**
     * isReaderMTOMAware
     * @return true if the reader supports MTOM
     */
   public static boolean isReaderMTOMAware(javax.xml.stream.XMLStreamReader reader) {
        boolean isReaderMTOMAware = false;
        
        try{
          isReaderMTOMAware = java.lang.Boolean.TRUE.equals(reader.getProperty(org.apache.axiom.om.OMConstants.IS_DATA_HANDLERS_AWARE));
        }catch(java.lang.IllegalArgumentException e){
          isReaderMTOMAware = false;
        }
        return isReaderMTOMAware;
   }
     
     
        /**
        *
        * @param parentQName
        * @param factory
        * @return org.apache.axiom.om.OMElement
        */
       public org.apache.axiom.om.OMElement getOMElement (
               final javax.xml.namespace.QName parentQName,
               final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{


        
               org.apache.axiom.om.OMDataSource dataSource =
                       new org.apache.axis2.databinding.ADBDataSource(this,parentQName){

                 public void serialize(org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
                       RequestSOAPHeader.this.serialize(parentQName,factory,xmlWriter);
                 }
               };
               return new org.apache.axiom.om.impl.llom.OMSourcedElementImpl(
               parentQName,factory,dataSource);
            
       }

         public void serialize(final javax.xml.namespace.QName parentQName,
                                       final org.apache.axiom.om.OMFactory factory,
                                       org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter)
                                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
                           serialize(parentQName,factory,xmlWriter,false);
         }

         public void serialize(final javax.xml.namespace.QName parentQName,
                               final org.apache.axiom.om.OMFactory factory,
                               org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter,
                               boolean serializeType)
            throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
            
                


                java.lang.String prefix = null;
                java.lang.String namespace = null;
                

                    prefix = parentQName.getPrefix();
                    namespace = parentQName.getNamespaceURI();

                    if ((namespace != null) && (namespace.trim().length() > 0)) {
                        java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
                        if (writerPrefix != null) {
                            xmlWriter.writeStartElement(namespace, parentQName.getLocalPart());
                        } else {
                            if (prefix == null) {
                                prefix = generatePrefix(namespace);
                            }

                            xmlWriter.writeStartElement(prefix, parentQName.getLocalPart(), namespace);
                            xmlWriter.writeNamespace(prefix, namespace);
                            xmlWriter.setPrefix(prefix, namespace);
                        }
                    } else {
                        xmlWriter.writeStartElement(parentQName.getLocalPart());
                    }
                
                  if (serializeType){
               

                   java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://www.huawei.com.cn/schema/common/v2_1");
                   if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           namespacePrefix+":RequestSOAPHeader",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "RequestSOAPHeader",
                           xmlWriter);
                   }

               
                   }
                if (localSpIdTracker){
                                    namespace = "http://www.huawei.com.cn/schema/common/v2_1";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"spId", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"spId");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("spId");
                                    }
                                

                                          if (localSpId==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("spId cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localSpId);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localSpPasswordTracker){
                                    namespace = "http://www.huawei.com.cn/schema/common/v2_1";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"spPassword", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"spPassword");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("spPassword");
                                    }
                                

                                          if (localSpPassword==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("spPassword cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localSpPassword);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localServiceIdTracker){
                                    namespace = "http://www.huawei.com.cn/schema/common/v2_1";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"serviceId", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"serviceId");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("serviceId");
                                    }
                                

                                          if (localServiceId==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("serviceId cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localServiceId);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localTimeStampTracker){
                                    namespace = "http://www.huawei.com.cn/schema/common/v2_1";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"timeStamp", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"timeStamp");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("timeStamp");
                                    }
                                

                                          if (localTimeStamp==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("timeStamp cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localTimeStamp);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localOATracker){
                                    namespace = "http://www.huawei.com.cn/schema/common/v2_1";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"OA", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"OA");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("OA");
                                    }
                                

                                          if (localOA==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("OA cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localOA);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localOauth_tokenTracker){
                                    namespace = "http://www.huawei.com.cn/schema/common/v2_1";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"oauth_token", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"oauth_token");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("oauth_token");
                                    }
                                

                                          if (localOauth_token==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("oauth_token cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localOauth_token);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localFATracker){
                                    namespace = "http://www.huawei.com.cn/schema/common/v2_1";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"FA", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"FA");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("FA");
                                    }
                                

                                          if (localFA==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("FA cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localFA);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localTokenTracker){
                                    namespace = "http://www.huawei.com.cn/schema/common/v2_1";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"token", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"token");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("token");
                                    }
                                

                                          if (localToken==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("token cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localToken);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localWatcherTracker){
                                    namespace = "http://www.huawei.com.cn/schema/common/v2_1";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"watcher", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"watcher");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("watcher");
                                    }
                                

                                          if (localWatcher==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("watcher cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localWatcher);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localPresentityTracker){
                                    namespace = "http://www.huawei.com.cn/schema/common/v2_1";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"presentity", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"presentity");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("presentity");
                                    }
                                

                                          if (localPresentity==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("presentity cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localPresentity);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localAuthIdTracker){
                                    namespace = "http://www.huawei.com.cn/schema/common/v2_1";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"authId", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"authId");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("authId");
                                    }
                                

                                          if (localAuthId==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("authId cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localAuthId);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localLinkidTracker){
                                    namespace = "http://www.huawei.com.cn/schema/common/v2_1";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"linkid", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"linkid");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("linkid");
                                    }
                                

                                          if (localLinkid==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("linkid cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localLinkid);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localPresentidTracker){
                                    namespace = "http://www.huawei.com.cn/schema/common/v2_1";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"presentid", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"presentid");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("presentid");
                                    }
                                

                                          if (localPresentid==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("presentid cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localPresentid);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localMsgTypeTracker){
                                    namespace = "http://www.huawei.com.cn/schema/common/v2_1";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"msgType", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"msgType");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("msgType");
                                    }
                                
                                               if (localMsgType==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("msgType cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localMsgType));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             }
                    xmlWriter.writeEndElement();
               

        }

         /**
          * Util method to write an attribute with the ns prefix
          */
          private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                      java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
              if (xmlWriter.getPrefix(namespace) == null) {
                       xmlWriter.writeNamespace(prefix, namespace);
                       xmlWriter.setPrefix(prefix, namespace);

              }

              xmlWriter.writeAttribute(namespace,attName,attValue);

         }

        /**
          * Util method to write an attribute without the ns prefix
          */
          private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                      java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
                if (namespace.equals(""))
              {
                  xmlWriter.writeAttribute(attName,attValue);
              }
              else
              {
                  registerPrefix(xmlWriter, namespace);
                  xmlWriter.writeAttribute(namespace,attName,attValue);
              }
          }


           /**
             * Util method to write an attribute without the ns prefix
             */
            private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                             javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

                java.lang.String attributeNamespace = qname.getNamespaceURI();
                java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
                if (attributePrefix == null) {
                    attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
                }
                java.lang.String attributeValue;
                if (attributePrefix.trim().length() > 0) {
                    attributeValue = attributePrefix + ":" + qname.getLocalPart();
                } else {
                    attributeValue = qname.getLocalPart();
                }

                if (namespace.equals("")) {
                    xmlWriter.writeAttribute(attName, attributeValue);
                } else {
                    registerPrefix(xmlWriter, namespace);
                    xmlWriter.writeAttribute(namespace, attName, attributeValue);
                }
            }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


         /**
         * Register a namespace prefix
         */
         private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
                java.lang.String prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null) {
                    prefix = generatePrefix(namespace);

                    while (xmlWriter.getNamespaceContext().getNamespaceURI(prefix) != null) {
                        prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                    }

                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }

                return prefix;
            }


  
        /**
        * databinding method to get an XML representation of this object
        *
        */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                    throws org.apache.axis2.databinding.ADBException{


        
                 java.util.ArrayList elementList = new java.util.ArrayList();
                 java.util.ArrayList attribList = new java.util.ArrayList();

                 if (localSpIdTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.huawei.com.cn/schema/common/v2_1",
                                                                      "spId"));
                                 
                                        if (localSpId != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localSpId));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("spId cannot be null!!");
                                        }
                                    } if (localSpPasswordTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.huawei.com.cn/schema/common/v2_1",
                                                                      "spPassword"));
                                 
                                        if (localSpPassword != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localSpPassword));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("spPassword cannot be null!!");
                                        }
                                    } if (localServiceIdTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.huawei.com.cn/schema/common/v2_1",
                                                                      "serviceId"));
                                 
                                        if (localServiceId != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localServiceId));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("serviceId cannot be null!!");
                                        }
                                    } if (localTimeStampTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.huawei.com.cn/schema/common/v2_1",
                                                                      "timeStamp"));
                                 
                                        if (localTimeStamp != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTimeStamp));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("timeStamp cannot be null!!");
                                        }
                                    } if (localOATracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.huawei.com.cn/schema/common/v2_1",
                                                                      "OA"));
                                 
                                        if (localOA != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localOA));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("OA cannot be null!!");
                                        }
                                    } if (localOauth_tokenTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.huawei.com.cn/schema/common/v2_1",
                                                                      "oauth_token"));
                                 
                                        if (localOauth_token != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localOauth_token));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("oauth_token cannot be null!!");
                                        }
                                    } if (localFATracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.huawei.com.cn/schema/common/v2_1",
                                                                      "FA"));
                                 
                                        if (localFA != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localFA));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("FA cannot be null!!");
                                        }
                                    } if (localTokenTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.huawei.com.cn/schema/common/v2_1",
                                                                      "token"));
                                 
                                        if (localToken != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localToken));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("token cannot be null!!");
                                        }
                                    } if (localWatcherTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.huawei.com.cn/schema/common/v2_1",
                                                                      "watcher"));
                                 
                                        if (localWatcher != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localWatcher));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("watcher cannot be null!!");
                                        }
                                    } if (localPresentityTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.huawei.com.cn/schema/common/v2_1",
                                                                      "presentity"));
                                 
                                        if (localPresentity != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPresentity));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("presentity cannot be null!!");
                                        }
                                    } if (localAuthIdTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.huawei.com.cn/schema/common/v2_1",
                                                                      "authId"));
                                 
                                        if (localAuthId != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAuthId));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("authId cannot be null!!");
                                        }
                                    } if (localLinkidTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.huawei.com.cn/schema/common/v2_1",
                                                                      "linkid"));
                                 
                                        if (localLinkid != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLinkid));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("linkid cannot be null!!");
                                        }
                                    } if (localPresentidTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.huawei.com.cn/schema/common/v2_1",
                                                                      "presentid"));
                                 
                                        if (localPresentid != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPresentid));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("presentid cannot be null!!");
                                        }
                                    } if (localMsgTypeTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.huawei.com.cn/schema/common/v2_1",
                                                                      "msgType"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localMsgType));
                            }

                return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());
            
            

        }

  

     /**
      *  Factory class that keeps the parse method
      */
    public static class Factory{

        
        

        /**
        * static method to create the object
        * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
        *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
        * Postcondition: If this object is an element, the reader is positioned at its end element
        *                If this object is a complex type, the reader is positioned at the end element of its outer element
        */
        public static RequestSOAPHeader parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
            RequestSOAPHeader object =
                new RequestSOAPHeader();

            int event;
            java.lang.String nillableValue = null;
            java.lang.String prefix ="";
            java.lang.String namespaceuri ="";
            try {
                
                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                
                if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                  java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                        "type");
                  if (fullTypeName!=null){
                    java.lang.String nsPrefix = null;
                    if (fullTypeName.indexOf(":") > -1){
                        nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                    }
                    nsPrefix = nsPrefix==null?"":nsPrefix;

                    java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);
                    
                            if (!"RequestSOAPHeader".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (RequestSOAPHeader)cn.com.huawei.www.schema.common.v2_1.ExtensionMapper.getTypeObject(
                                     nsUri,type,reader);
                              }
                        

                  }
                

                }

                

                
                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();
                

                 
                    
                    reader.next();
                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.huawei.com.cn/schema/common/v2_1","spId").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setSpId(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.huawei.com.cn/schema/common/v2_1","spPassword").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setSpPassword(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.huawei.com.cn/schema/common/v2_1","serviceId").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setServiceId(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.huawei.com.cn/schema/common/v2_1","timeStamp").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setTimeStamp(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.huawei.com.cn/schema/common/v2_1","OA").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setOA(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.huawei.com.cn/schema/common/v2_1","oauth_token").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setOauth_token(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.huawei.com.cn/schema/common/v2_1","FA").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setFA(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.huawei.com.cn/schema/common/v2_1","token").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setToken(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.huawei.com.cn/schema/common/v2_1","watcher").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setWatcher(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.huawei.com.cn/schema/common/v2_1","presentity").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setPresentity(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.huawei.com.cn/schema/common/v2_1","authId").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setAuthId(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.huawei.com.cn/schema/common/v2_1","linkid").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setLinkid(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.huawei.com.cn/schema/common/v2_1","presentid").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setPresentid(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.huawei.com.cn/schema/common/v2_1","msgType").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setMsgType(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                               object.setMsgType(java.lang.Integer.MIN_VALUE);
                                           
                                    }
                                  
                            while (!reader.isStartElement() && !reader.isEndElement())
                                reader.next();
                            
                                if (reader.isStartElement())
                                // A start element we are not expecting indicates a trailing invalid property
                                throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                            



            } catch (javax.xml.stream.XMLStreamException e) {
                throw new java.lang.Exception(e);
            }

            return object;
        }

        }//end of factory class

        

        }
           
          