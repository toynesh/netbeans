/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mongotest;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.Date;
import org.bson.Document;

/**
 *
 * @author julius
 */
public class Mongotest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        // Creating a Mongo client 
        MongoClient mongo = new MongoClient("localhost", 27017);

        // Creating Credentials 
        MongoCredential credential;
        credential = MongoCredential.createCredential("knec", "1root2", "".toCharArray());
        System.out.println("Connected to the database successfully");

        // Accessing the database 
        MongoDatabase database = mongo.getDatabase("knecsms");
        System.out.println("Credentials ::" + credential);

        //Creating a collection 
        //database.createCollection("sms");
        //System.out.println("SMS Collection created successfully");

        // Retrieving a collection
        MongoCollection<Document> collection = database.getCollection("sms");
        System.out.println("Collection SMS selected successfully");

        Date now = new Date();
        Document newSMS = new Document(
                "time_recieved", now)
                .append("smsc", "SAFARICOM")
                .append("sender", "254763212335")
                .append("shortcode", 20076)
                .append("inmessage", "42705214013")
                .append("timesent", now)
                .append("outmessage", "Dear customer, kindly use the format [INDEX_NUMBER KCSE] or [INDEX_NUMBER KCPE]. Kindly verify this 42705214013 and try again.")
                .append("msgid", "1464023955")
                .append("sendresults", "OK")
                .append("deliverystatus", "MessageSent")
                .append("status", 0);
        collection.insertOne(newSMS);
        System.out.println("newSMS Document inserted successfully");
    }

}
