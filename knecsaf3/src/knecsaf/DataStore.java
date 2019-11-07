package knecsaf;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoDatabase;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import static java.lang.Integer.parseInt;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.*;
import java.util.Scanner;
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

            //String myDriver = "org.gjt.mm.mysql.Driver";
            String myUrl = "jdbc:mysql://localhost/knecsms?autoReconnect=true&useSSL=false";
            //String myUrl = "jdbc:mysql://localhost/knecsms";
            //Class.forName(myDriver);
            //conn = DriverManager.getConnection(myUrl, "root", "1root2");
            conn = DriverManager.getConnection(myUrl, "pdsluser", "P@Dsl949022");

        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());

        }
        return conn;

    }

    public MongoDatabase mongoconnect() {
        //db.createUser({user:"knec", pwd:"1root2", roles:[{role:"dbAdmin", db:"knecsms"}]})
        MongoDatabase database = null;
        try {
            // TODO code application logic here
            // Creating a Mongo client 
            MongoClient mongo = new MongoClient("localhost", 27017);

            // Creating Credentials 
            MongoCredential credential;
            credential = MongoCredential.createCredential("knec", "1root2", "".toCharArray());
            System.out.println("Connected to the database successfully");

            // Accessing the database 
            database = mongo.getDatabase("knecsms");
            System.out.println("Credentials ::" + credential);
        } catch (Exception e) {
            System.err.println("Exception connecting to Mongo: " + e.getMessage());

        }
        return database;
    }

    public void createTables() {
        try {
            Connection con = connect();
            String sms = "CREATE TABLE IF NOT EXISTS sms ("
                    + "id int(11) NOT NULL AUTO_INCREMENT,"
                    + "time_recieved timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,"
                    + "smsc varchar(100) NOT NULL,"
                    + "sender varchar(200) NOT NULL,"
                    + "shortcode varchar(200) NOT NULL,"
                    + "inmessage text NOT NULL,"
                    + "timesent timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,"
                    + "outmessage text NOT NULL,"
                    + "msgid text NOT NULL,"
                    + "sendresults varchar(200) NOT NULL,"
                    + "deliverystatus varchar(200) NOT NULL DEFAULT 'MessageSent',"
                    + "status int(11) NOT NULL DEFAULT '0',"
                    + "PRIMARY KEY (id))";
            String inbox = "CREATE TABLE IF NOT EXISTS inbox ("
                    + "id int(11) NOT NULL AUTO_INCREMENT,"
                    + "time_recieved timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,"
                    + "timesent timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,"
                    + "smsc varchar(100) NOT NULL,"
                    + "osmsc varchar(100) NOT NULL,"
                    + "sender varchar(200) NOT NULL,"
                    + "reciever varchar(200) NOT NULL,"
                    + "message text NOT NULL,"
                    + "status int(11) NOT NULL DEFAULT '0',"
                    + "PRIMARY KEY (id))";
            Statement stm = con.createStatement();
            stm.execute(sms);
            stm.execute(inbox);
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void insert(String insert) {
        Connection conn = connect();
        PreparedStatement pstm = null;
        try {

            System.out.println(insert);
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

    public void terminalCMD(String command) {
        ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
        pb.redirectErrorStream(true);
        try {
            Process shell = pb.start();
        } catch (Exception exp) {
            System.out.println("Exception--->" + exp.getMessage());
        }
        // close the stream

        System.out.println("Done---->");
    }

    public String sendReq(String param) throws IOException {
        //String request = "http://172.27.116.42:8084/KnecResults/QueryResults?";
        String request = "http://localhost:8084/KnecResults/QueryResults?";
        URL url = new URL(request);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("charset", "utf-8");
        connection.setRequestProperty("Content-Length", "" + Integer.toString(param.getBytes().length));
        connection.setUseCaches(false);
        connection.setConnectTimeout(1000);

        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.writeBytes(param);
        out.flush();

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        StringBuffer response = new StringBuffer();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println(response.toString());
        out.close();
        connection.disconnect();
        return response.toString();
    }

    public String getMessage(String index) {
        String url = "index=" + index;
        String res;
        try {
            res = sendReq(url);
        } catch (IOException e) {
            e.printStackTrace();
            res = "FAILED";
        }
        return res;
    }

}
