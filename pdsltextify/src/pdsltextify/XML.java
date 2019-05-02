/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pdsltextify;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author hesbon
 */
public class XML {

    public ArrayList xmlGet(String xml, String root, String node1, String node2) {
        System.out.println("XML Parames: " + root + ":" + node1 + ":" + node2 + ":");
        ArrayList array = new ArrayList();
        try {
            DocumentBuilderFactory dbf
                    = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));

            Document doc = db.parse(is);
            NodeList nodes = doc.getElementsByTagName(root);

            // iterate the employees
            for (int i = 0; i < nodes.getLength(); i++) {
                Element element = (Element) nodes.item(i);

                NodeList name = element.getElementsByTagName(node1);
                Element line = (Element) name.item(0);
                array.add(getCharacterDataFromElement(line));
                System.out.println("Name: " + getCharacterDataFromElement(line));

                NodeList title = element.getElementsByTagName(node2);
                line = (Element) title.item(0);
                array.add(getCharacterDataFromElement(line));
                System.out.println("Message: " + getCharacterDataFromElement(line));
                return array;
            }
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(XML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XML.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static String getCharacterDataFromElement(Element e) {
        Node child = e.getFirstChild();
        if (child instanceof CharacterData) {
            CharacterData cd = (CharacterData) child;
            return cd.getData();
        }
        return "?";
    }

    public ArrayList xmlGet(String xml, String root, String node1) {
        System.out.println("XML Parames: " + root + ":" + node1);
        System.out.println("XML Parames: " + xml);
        ArrayList array = new ArrayList();
        try {
            DocumentBuilderFactory dbf
                    = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            String XString = xml.toString();
            XString = XString.replaceAll("[^\\x20-\\x7e\\x0A]", "");
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(XString));

            //Document doc = db.parse(XString);
            Document doc = db.parse(is);
            NodeList nodes = doc.getElementsByTagName(root);

            // iterate the employees
            for (int i = 0; i < nodes.getLength(); i++) {
                Element element = (Element) nodes.item(i);

                NodeList name = element.getElementsByTagName(node1);
                Element line = (Element) name.item(0);

                array.add(getCharacterDataFromElement(line));
                System.out.println("Message: " + getCharacterDataFromElement(line));
                /*
           NodeList title = element.getElementsByTagName(node2);
           line = (Element) title.item(0);
           array.add(getCharacterDataFromElement(line));
           System.out.println("Message: " + getCharacterDataFromElement(line));*/

                return array;
            }
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(XML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XML.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String xmlGetAttrib(String xml, String root, String node1, String attrib) {
        System.out.println("XML Parames: " + root + ":" + node1);
        ArrayList array = new ArrayList();
        //String attrib=null;
        try {
            DocumentBuilderFactory dbf
                    = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));

            Document doc = db.parse(is);
            NodeList nodes = doc.getElementsByTagName(root);

            // iterate the employees
            for (int i = 0; i < nodes.getLength(); i++) {
                Element element = (Element) nodes.item(i);

                NodeList name = element.getElementsByTagName(node1);
                Element line = (Element) name.item(0);
                attrib = line.getAttribute(attrib);
                array.add(getCharacterDataFromElement(line));
                System.out.println("Message: " + getCharacterDataFromElement(line));
                /*
           NodeList title = element.getElementsByTagName(node2);
           line = (Element) title.item(0);
           array.add(getCharacterDataFromElement(line));
           System.out.println("Message: " + getCharacterDataFromElement(line));*/

                return attrib;
            }
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(XML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XML.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String xmlGetCustomer(String strMsg) {
        String resp = "";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(strMsg)));
            NodeList el = document.getElementsByTagName("name");
            resp = el.item(0).getTextContent();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }
    public String xmlGetPhone(String strMsg) {
        String resp = "";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(strMsg)));
            NodeList el = document.getElementsByTagName("phoneNum");
            resp = el.item(0).getTextContent();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }
    public String xmlGetAccount(String strMsg) {
        String resp = "";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(strMsg)));
            NodeList el = document.getElementsByTagName("custAccNum");
            resp = el.item(0).getTextContent();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }
    public String xmlGetBalance(String strMsg) {
        String resp = "none";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(strMsg)));
            NodeList el = document.getElementsByTagName("balance");
            resp = el.item(0).getTextContent();

        } catch (Exception e) {
            //e.printStackTrace();
            resp = "none";
        }
        return resp;
    }
    
    public ArrayList xmlGetAttrib(String xml, String root, String node1) {
        System.out.println("XML Parames: " + root + ":" + node1);
        ArrayList array = new ArrayList();
        try {
            DocumentBuilderFactory dbf
                    = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));

            Document doc = db.parse(is);
            NodeList nodes = doc.getElementsByTagName(root);

            // iterate the employees
            for (int i = 0; i < nodes.getLength(); i++) {
                Element element = (Element) nodes.item(i);

                NodeList name = element.getElementsByTagName(node1);
                Element line = (Element) name.item(0);
                array.add(getCharacterDataFromElement(line));
                System.out.println("Message: " + getCharacterDataFromElement(line));
                /*
           NodeList title = element.getElementsByTagName(node2);
           line = (Element) title.item(0);
           array.add(getCharacterDataFromElement(line));
           System.out.println("Message: " + getCharacterDataFromElement(line));*/

                return array;
            }
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(XML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XML.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
