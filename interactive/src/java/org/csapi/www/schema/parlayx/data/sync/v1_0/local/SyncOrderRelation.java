
/**
 * SyncOrderRelation.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4  Built on : Apr 26, 2008 (06:25:17 EDT)
 */
            
                package org.csapi.www.schema.parlayx.data.sync.v1_0.local;
            

            /**
            *  SyncOrderRelation bean class
            */
        
        public  class SyncOrderRelation
        implements org.apache.axis2.databinding.ADBBean{
        /* This type was generated from the piece of schema that had
                name = syncOrderRelation
                Namespace URI = http://www.csapi.org/schema/parlayx/data/sync/v1_0/local
                Namespace Prefix = ns2
                */
            

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://www.csapi.org/schema/parlayx/data/sync/v1_0/local")){
                return "ns2";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        

                        /**
                        * field for UserID
                        */

                        
                                    protected org.csapi.www.schema.parlayx.data.v1_0.UserID localUserID ;
                                

                           /**
                           * Auto generated getter method
                           * @return org.csapi.www.schema.parlayx.data.v1_0.UserID
                           */
                           public  org.csapi.www.schema.parlayx.data.v1_0.UserID getUserID(){
                               return localUserID;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param UserID
                               */
                               public void setUserID(org.csapi.www.schema.parlayx.data.v1_0.UserID param){
                            
                                            this.localUserID=param;
                                    

                               }
                            

                        /**
                        * field for SpID
                        */

                        
                                    protected java.lang.String localSpID ;
                                

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getSpID(){
                               return localSpID;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param SpID
                               */
                               public void setSpID(java.lang.String param){
                            
                                            this.localSpID=param;
                                    

                               }
                            

                        /**
                        * field for ProductID
                        */

                        
                                    protected java.lang.String localProductID ;
                                

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getProductID(){
                               return localProductID;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ProductID
                               */
                               public void setProductID(java.lang.String param){
                            
                                            this.localProductID=param;
                                    

                               }
                            

                        /**
                        * field for ServiceID
                        */

                        
                                    protected java.lang.String localServiceID ;
                                

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getServiceID(){
                               return localServiceID;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ServiceID
                               */
                               public void setServiceID(java.lang.String param){
                            
                                            this.localServiceID=param;
                                    

                               }
                            

                        /**
                        * field for ServiceList
                        */

                        
                                    protected java.lang.String localServiceList ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localServiceListTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getServiceList(){
                               return localServiceList;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ServiceList
                               */
                               public void setServiceList(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localServiceListTracker = true;
                                       } else {
                                          localServiceListTracker = false;
                                              
                                       }
                                   
                                            this.localServiceList=param;
                                    

                               }
                            

                        /**
                        * field for UpdateType
                        */

                        
                                    protected int localUpdateType ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getUpdateType(){
                               return localUpdateType;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param UpdateType
                               */
                               public void setUpdateType(int param){
                            
                                            this.localUpdateType=param;
                                    

                               }
                            

                        /**
                        * field for UpdateTime
                        */

                        
                                    protected java.lang.String localUpdateTime ;
                                

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getUpdateTime(){
                               return localUpdateTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param UpdateTime
                               */
                               public void setUpdateTime(java.lang.String param){
                            
                                            this.localUpdateTime=param;
                                    

                               }
                            

                        /**
                        * field for UpdateDesc
                        */

                        
                                    protected java.lang.String localUpdateDesc ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localUpdateDescTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getUpdateDesc(){
                               return localUpdateDesc;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param UpdateDesc
                               */
                               public void setUpdateDesc(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localUpdateDescTracker = true;
                                       } else {
                                          localUpdateDescTracker = false;
                                              
                                       }
                                   
                                            this.localUpdateDesc=param;
                                    

                               }
                            

                        /**
                        * field for EffectiveTime
                        */

                        
                                    protected java.lang.String localEffectiveTime ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localEffectiveTimeTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getEffectiveTime(){
                               return localEffectiveTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param EffectiveTime
                               */
                               public void setEffectiveTime(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localEffectiveTimeTracker = true;
                                       } else {
                                          localEffectiveTimeTracker = false;
                                              
                                       }
                                   
                                            this.localEffectiveTime=param;
                                    

                               }
                            

                        /**
                        * field for ExpiryTime
                        */

                        
                                    protected java.lang.String localExpiryTime ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localExpiryTimeTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getExpiryTime(){
                               return localExpiryTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ExpiryTime
                               */
                               public void setExpiryTime(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localExpiryTimeTracker = true;
                                       } else {
                                          localExpiryTimeTracker = false;
                                              
                                       }
                                   
                                            this.localExpiryTime=param;
                                    

                               }
                            

                        /**
                        * field for ExtensionInfo
                        */

                        
                                    protected org.csapi.www.schema.parlayx.data.v1_0.NamedParameterList localExtensionInfo ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localExtensionInfoTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return org.csapi.www.schema.parlayx.data.v1_0.NamedParameterList
                           */
                           public  org.csapi.www.schema.parlayx.data.v1_0.NamedParameterList getExtensionInfo(){
                               return localExtensionInfo;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ExtensionInfo
                               */
                               public void setExtensionInfo(org.csapi.www.schema.parlayx.data.v1_0.NamedParameterList param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localExtensionInfoTracker = true;
                                       } else {
                                          localExtensionInfoTracker = false;
                                              
                                       }
                                   
                                            this.localExtensionInfo=param;
                                    

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
                       SyncOrderRelation.this.serialize(parentQName,factory,xmlWriter);
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
               

                   java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://www.csapi.org/schema/parlayx/data/sync/v1_0/local");
                   if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           namespacePrefix+":syncOrderRelation",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "syncOrderRelation",
                           xmlWriter);
                   }

               
                   }
               
                                            if (localUserID==null){
                                                 throw new org.apache.axis2.databinding.ADBException("userID cannot be null!!");
                                            }
                                           localUserID.serialize(new javax.xml.namespace.QName("http://www.csapi.org/schema/parlayx/data/sync/v1_0/local","userID"),
                                               factory,xmlWriter);
                                        
                                    namespace = "http://www.csapi.org/schema/parlayx/data/sync/v1_0/local";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"spID", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"spID");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("spID");
                                    }
                                

                                          if (localSpID==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("spID cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localSpID);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.csapi.org/schema/parlayx/data/sync/v1_0/local";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"productID", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"productID");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("productID");
                                    }
                                

                                          if (localProductID==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("productID cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localProductID);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.csapi.org/schema/parlayx/data/sync/v1_0/local";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"serviceID", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"serviceID");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("serviceID");
                                    }
                                

                                          if (localServiceID==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("serviceID cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localServiceID);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                              if (localServiceListTracker){
                                    namespace = "http://www.csapi.org/schema/parlayx/data/sync/v1_0/local";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"serviceList", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"serviceList");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("serviceList");
                                    }
                                

                                          if (localServiceList==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("serviceList cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localServiceList);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             }
                                    namespace = "http://www.csapi.org/schema/parlayx/data/sync/v1_0/local";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"updateType", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"updateType");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("updateType");
                                    }
                                
                                               if (localUpdateType==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("updateType cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localUpdateType));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.csapi.org/schema/parlayx/data/sync/v1_0/local";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"updateTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"updateTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("updateTime");
                                    }
                                

                                          if (localUpdateTime==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("updateTime cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localUpdateTime);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                              if (localUpdateDescTracker){
                                    namespace = "http://www.csapi.org/schema/parlayx/data/sync/v1_0/local";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"updateDesc", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"updateDesc");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("updateDesc");
                                    }
                                

                                          if (localUpdateDesc==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("updateDesc cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localUpdateDesc);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localEffectiveTimeTracker){
                                    namespace = "http://www.csapi.org/schema/parlayx/data/sync/v1_0/local";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"effectiveTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"effectiveTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("effectiveTime");
                                    }
                                

                                          if (localEffectiveTime==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("effectiveTime cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localEffectiveTime);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localExpiryTimeTracker){
                                    namespace = "http://www.csapi.org/schema/parlayx/data/sync/v1_0/local";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"expiryTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"expiryTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("expiryTime");
                                    }
                                

                                          if (localExpiryTime==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("expiryTime cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localExpiryTime);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localExtensionInfoTracker){
                                            if (localExtensionInfo==null){
                                                 throw new org.apache.axis2.databinding.ADBException("extensionInfo cannot be null!!");
                                            }
                                           localExtensionInfo.serialize(new javax.xml.namespace.QName("http://www.csapi.org/schema/parlayx/data/sync/v1_0/local","extensionInfo"),
                                               factory,xmlWriter);
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

                
                            elementList.add(new javax.xml.namespace.QName("http://www.csapi.org/schema/parlayx/data/sync/v1_0/local",
                                                                      "userID"));
                            
                            
                                    if (localUserID==null){
                                         throw new org.apache.axis2.databinding.ADBException("userID cannot be null!!");
                                    }
                                    elementList.add(localUserID);
                                
                                      elementList.add(new javax.xml.namespace.QName("http://www.csapi.org/schema/parlayx/data/sync/v1_0/local",
                                                                      "spID"));
                                 
                                        if (localSpID != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localSpID));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("spID cannot be null!!");
                                        }
                                    
                                      elementList.add(new javax.xml.namespace.QName("http://www.csapi.org/schema/parlayx/data/sync/v1_0/local",
                                                                      "productID"));
                                 
                                        if (localProductID != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localProductID));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("productID cannot be null!!");
                                        }
                                    
                                      elementList.add(new javax.xml.namespace.QName("http://www.csapi.org/schema/parlayx/data/sync/v1_0/local",
                                                                      "serviceID"));
                                 
                                        if (localServiceID != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localServiceID));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("serviceID cannot be null!!");
                                        }
                                     if (localServiceListTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.csapi.org/schema/parlayx/data/sync/v1_0/local",
                                                                      "serviceList"));
                                 
                                        if (localServiceList != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localServiceList));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("serviceList cannot be null!!");
                                        }
                                    }
                                      elementList.add(new javax.xml.namespace.QName("http://www.csapi.org/schema/parlayx/data/sync/v1_0/local",
                                                                      "updateType"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localUpdateType));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.csapi.org/schema/parlayx/data/sync/v1_0/local",
                                                                      "updateTime"));
                                 
                                        if (localUpdateTime != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localUpdateTime));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("updateTime cannot be null!!");
                                        }
                                     if (localUpdateDescTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.csapi.org/schema/parlayx/data/sync/v1_0/local",
                                                                      "updateDesc"));
                                 
                                        if (localUpdateDesc != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localUpdateDesc));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("updateDesc cannot be null!!");
                                        }
                                    } if (localEffectiveTimeTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.csapi.org/schema/parlayx/data/sync/v1_0/local",
                                                                      "effectiveTime"));
                                 
                                        if (localEffectiveTime != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localEffectiveTime));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("effectiveTime cannot be null!!");
                                        }
                                    } if (localExpiryTimeTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.csapi.org/schema/parlayx/data/sync/v1_0/local",
                                                                      "expiryTime"));
                                 
                                        if (localExpiryTime != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localExpiryTime));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("expiryTime cannot be null!!");
                                        }
                                    } if (localExtensionInfoTracker){
                            elementList.add(new javax.xml.namespace.QName("http://www.csapi.org/schema/parlayx/data/sync/v1_0/local",
                                                                      "extensionInfo"));
                            
                            
                                    if (localExtensionInfo==null){
                                         throw new org.apache.axis2.databinding.ADBException("extensionInfo cannot be null!!");
                                    }
                                    elementList.add(localExtensionInfo);
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
        public static SyncOrderRelation parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
            SyncOrderRelation object =
                new SyncOrderRelation();

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
                    
                            if (!"syncOrderRelation".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (SyncOrderRelation)org.csapi.www.schema.parlayx.data.sync.v1_0.local.ExtensionMapper.getTypeObject(
                                     nsUri,type,reader);
                              }
                        

                  }
                

                }

                

                
                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();
                

                 
                    
                    reader.next();
                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.csapi.org/schema/parlayx/data/sync/v1_0/local","userID").equals(reader.getName())){
                                
                                                object.setUserID(org.csapi.www.schema.parlayx.data.v1_0.UserID.Factory.parse(reader));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.csapi.org/schema/parlayx/data/sync/v1_0/local","spID").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setSpID(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.csapi.org/schema/parlayx/data/sync/v1_0/local","productID").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setProductID(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.csapi.org/schema/parlayx/data/sync/v1_0/local","serviceID").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setServiceID(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.csapi.org/schema/parlayx/data/sync/v1_0/local","serviceList").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setServiceList(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.csapi.org/schema/parlayx/data/sync/v1_0/local","updateType").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setUpdateType(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.csapi.org/schema/parlayx/data/sync/v1_0/local","updateTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setUpdateTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.csapi.org/schema/parlayx/data/sync/v1_0/local","updateDesc").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setUpdateDesc(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.csapi.org/schema/parlayx/data/sync/v1_0/local","effectiveTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setEffectiveTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.csapi.org/schema/parlayx/data/sync/v1_0/local","expiryTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setExpiryTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.csapi.org/schema/parlayx/data/sync/v1_0/local","extensionInfo").equals(reader.getName())){
                                
                                                object.setExtensionInfo(org.csapi.www.schema.parlayx.data.v1_0.NamedParameterList.Factory.parse(reader));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
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
           
          