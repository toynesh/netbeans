package knecdatadump;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoDatabase;
import java.sql.Connection;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author julius
 */
public class DataStore {

    public DataStore() {
        //createTables();

    }

    public Connection connect() {
        Connection conn = null;
        try {
            String myDriver = "org.gjt.mm.mysql.Driver";
            String myUrl = "jdbc:mysql://localhost/knecsms";
            Class.forName(myDriver);
            //conn = DriverManager.getConnection(myUrl, "root", "juliusroot2");
            conn = DriverManager.getConnection(myUrl, "pdsluser", "P@Dsl949022");

        } catch (Exception e) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, "Exception: " + e.getMessage());
        }
        return conn;

    }

    public MongoDatabase mongoconnect(String host) {
        //db.createUser({user:"knec", pwd:"1root2", roles:[{role:"dbAdmin", db:"knecsms"}]})
        MongoDatabase database = null;
        try {
            // TODO code application logic here
            // Creating a Mongo client 
            MongoClient mongo = new MongoClient(host, 27017);

            // Creating Credentials 
            MongoCredential credential;
            credential = MongoCredential.createCredential("knec", "1root2", "".toCharArray());
            Logger.getLogger(DataStore.class.getName()).log(Level.INFO, "Connected to the database successfully");

            // Accessing the database 
            database = mongo.getDatabase("knecsms");
            Logger.getLogger(DataStore.class.getName()).log(Level.INFO, "Credentials ::" + credential);
        } catch (Exception e) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, "Exception connecting to Mongo: " + e.getMessage());

        }
        return database;
    }
    public void insert(String insert) {
        Connection conn = connect();
        PreparedStatement pstm = null;
        try {

            Logger.getLogger(DataStore.class.getName()).log(Level.INFO, insert);
            pstm = conn.prepareStatement(insert);
            pstm.execute();
            pstm.close();
        } catch (SQLException ex) {
        } finally {
            try {
                pstm.close();
                conn.close();

            } catch (SQLException ex) {
                //ex.printStackTrace();
            }
        }

    }

}
