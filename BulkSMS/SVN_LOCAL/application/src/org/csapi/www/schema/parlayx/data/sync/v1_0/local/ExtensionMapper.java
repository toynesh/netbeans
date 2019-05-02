
/**
 * ExtensionMapper.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4  Built on : Apr 26, 2008 (06:25:17 EDT)
 */

            package org.csapi.www.schema.parlayx.data.sync.v1_0.local;
            /**
            *  ExtensionMapper class
            */
        
        public  class ExtensionMapper{

          public static java.lang.Object getTypeObject(java.lang.String namespaceURI,
                                                       java.lang.String typeName,
                                                       javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{

              
                  if (
                  "http://www.csapi.org/schema/parlayx/data/sync/v1_0/local".equals(namespaceURI) &&
                  "changeMSISDN".equals(typeName)){
                   
                            return  org.csapi.www.schema.parlayx.data.sync.v1_0.local.ChangeMSISDN.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://www.csapi.org/schema/parlayx/data/sync/v1_0/local".equals(namespaceURI) &&
                  "syncOrderRelation".equals(typeName)){
                   
                            return  org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncOrderRelation.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://www.csapi.org/schema/parlayx/data/sync/v1_0/local".equals(namespaceURI) &&
                  "syncOrderRelationResponse".equals(typeName)){
                   
                            return  org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncOrderRelationResponse.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://www.csapi.org/schema/parlayx/data/sync/v1_0/local".equals(namespaceURI) &&
                  "syncMSISDNChange".equals(typeName)){
                   
                            return  org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncMSISDNChange.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://www.csapi.org/schema/parlayx/data/sync/v1_0/local".equals(namespaceURI) &&
                  "syncSubscriptionActive".equals(typeName)){
                   
                            return  org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncSubscriptionActive.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://www.csapi.org/schema/parlayx/data/v1_0".equals(namespaceURI) &&
                  "ProductDetail".equals(typeName)){
                   
                            return  org.csapi.www.schema.parlayx.data.v1_0.ProductDetail.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://www.csapi.org/schema/parlayx/data/sync/v1_0/local".equals(namespaceURI) &&
                  "syncSubscriptionActiveResponse".equals(typeName)){
                   
                            return  org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncSubscriptionActiveResponse.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://www.csapi.org/schema/parlayx/data/v1_0".equals(namespaceURI) &&
                  "NamedParameterList".equals(typeName)){
                   
                            return  org.csapi.www.schema.parlayx.data.v1_0.NamedParameterList.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://www.csapi.org/schema/parlayx/data/v1_0".equals(namespaceURI) &&
                  "NamedParameter".equals(typeName)){
                   
                            return  org.csapi.www.schema.parlayx.data.v1_0.NamedParameter.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://www.csapi.org/schema/parlayx/data/sync/v1_0/local".equals(namespaceURI) &&
                  "syncSubscriptionData".equals(typeName)){
                   
                            return  org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncSubscriptionData.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://www.csapi.org/schema/parlayx/data/sync/v1_0/local".equals(namespaceURI) &&
                  "syncSubscriptionDataResponse".equals(typeName)){
                   
                            return  org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncSubscriptionDataResponse.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://www.csapi.org/schema/parlayx/data/v1_0".equals(namespaceURI) &&
                  "UserID".equals(typeName)){
                   
                            return  org.csapi.www.schema.parlayx.data.v1_0.UserID.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://www.csapi.org/schema/parlayx/data/sync/v1_0/local".equals(namespaceURI) &&
                  "syncMSISDNChangeResponse".equals(typeName)){
                   
                            return  org.csapi.www.schema.parlayx.data.sync.v1_0.local.SyncMSISDNChangeResponse.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://www.csapi.org/schema/parlayx/data/sync/v1_0/local".equals(namespaceURI) &&
                  "changeMSISDNResponse".equals(typeName)){
                   
                            return  org.csapi.www.schema.parlayx.data.sync.v1_0.local.ChangeMSISDNResponse.Factory.parse(reader);
                        

                  }

              
             throw new org.apache.axis2.databinding.ADBException("Unsupported type " + namespaceURI + " " + typeName);
          }

        }
    