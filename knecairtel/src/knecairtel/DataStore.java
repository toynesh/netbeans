package knecairtel;

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
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, "Exception: " + e.getMessage());

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
            Logger.getLogger(DataStore.class.getName()).log(Level.INFO, "Connected to the database successfully");

            // Accessing the database 
            database = mongo.getDatabase("knecsms");
            Logger.getLogger(DataStore.class.getName()).log(Level.INFO, "Credentials ::" + credential);
        } catch (Exception e) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, "Exception connecting to Mongo: " + e.getMessage());

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
            String dlry = "CREATE TABLE IF NOT EXISTS dlry ("
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
            String sms78 = "CREATE TABLE IF NOT EXISTS sms78 ("
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
            String inbox78 = "CREATE TABLE IF NOT EXISTS inbox78 (" 
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
            String dlry78 = "CREATE TABLE IF NOT EXISTS dlry78 ("
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
            stm.execute(dlry);
            stm.execute(sms78);
            stm.execute(inbox78);
            stm.execute(dlry78);
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    public void terminalCMD(String command) {
        ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
        pb.redirectErrorStream(true);
        try {
            Process shell = pb.start();
        } catch (Exception exp) {
            Logger.getLogger(DataStore.class.getName()).log(Level.INFO, "Exception--->" + exp.getMessage());
        }
        // close the stream

        Logger.getLogger(DataStore.class.getName()).log(Level.INFO, "Done---->");
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

        Logger.getLogger(DataStore.class.getName()).log(Level.INFO, response.toString());
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

    public String sendMOReq(String param) throws IOException {
        String request = "http://localhost:88/inject_mo?";
        //String request = "http://localhost:8084/KnecResults/QueryResults?";
        URL url = new URL(request);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("charset", "utf-8");
        connection.setRequestProperty("Content-Length", "" + Integer.toString(param.getBytes().length));
        connection.setUseCaches(false);

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

        Logger.getLogger(DataStore.class.getName()).log(Level.INFO, response.toString());
        out.close();
        connection.disconnect();
        return response.toString();
    }

    public String getmoMessage() {
        String url = "short_message=Hello+from+SMPPSim&source_addr=4477665544&destination_addr=45454545%2C5555%2C6666&submit=Submit+Message&service_type=&source_addr_ton=1&source_addr_npi=1&dest_addr_ton=1&dest_addr_npi=1&esm_class=0&protocol_ID=&priority_flag=&registered_delivery_flag=0&data_coding=0&user_message_reference=&source_port=&destination_port=&sar_msg_ref_num=&sar_total_segments=&sar_segment_seqnum=&user_response_code=&privacy_indicator=&payload_type=&message_payload=&callback_num=&source_subaddress=&dest_subaddress=&language_indicator=&tlv1_tag=&tlv1_len=&tlv1_val=&tlv2_tag=&tlv2_len=&tlv2_val=&tlv3_tag=&tlv3_len=&tlv3_val=&tlv4_tag=&tlv4_len=&tlv4_val=&tlv5_tag=&tlv5_len=&tlv5_val=&tlv6_tag=&tlv6_len=&tlv6_val=&tlv7_tag=&tlv7_len=&tlv7_val=";
        String res;
        try {
            res = sendReq(url);
        } catch (IOException e) {
            e.printStackTrace();
            res = "FAILED";
        }
        return res;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Type name of number of MOS to be sent ");
        int ntms = parseInt(in.next());
        for (int i = 1; i <= ntms; i++) {
            try {
                String urlString = "http://localhost:88/inject_mo?short_message=35601211&source_addr=254728064120&destination_addr=20076&submit=Submit+Message&service_type=&source_addr_ton=1&source_addr_npi=1&dest_addr_ton=1&dest_addr_npi=1&esm_class=0&protocol_ID=&priority_flag=&registered_delivery_flag=0&data_coding=0&user_message_reference=&source_port=&destination_port=&sar_msg_ref_num=&sar_total_segments=&sar_segment_seqnum=&user_response_code=&privacy_indicator=&payload_type=&message_payload=&callback_num=&source_subaddress=&dest_subaddress=&language_indicator=&tlv1_tag=&tlv1_len=&tlv1_val=&tlv2_tag=&tlv2_len=&tlv2_val=&tlv3_tag=&tlv3_len=&tlv3_val=&tlv4_tag=&tlv4_len=&tlv4_val=&tlv5_tag=&tlv5_len=&tlv5_val=&tlv6_tag=&tlv6_len=&tlv6_val=&tlv7_tag=&tlv7_len=&tlv7_val=";
                URL url = new URL(urlString);
                URLConnection conn = url.openConnection();
                InputStream is = conn.getInputStream();
// Do what you want with that stream
            } catch (MalformedURLException ex) {
                Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
