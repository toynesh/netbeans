package delayalerts;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataStore {

    public Connection conn = null;
    Properties prop = new Properties();

    public DataStore() {
        createTables();
    }

    public Connection connect() {
        Connection conn = null;
        try {
            MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();
            dataSource.setServerName("127.0.0.1");
            dataSource.setDatabaseName("mobile_wallet");
            dataSource.setUser("pdsluser");
            dataSource.setPassword("P@Dsl949022");
            System.out.println("Connecting to the database");
            conn = dataSource.getConnection();

        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
        return conn;
    }

    public Connection connectvendit() {
        Connection conn = null;
        try {
            MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();
            dataSource.setServerName("127.0.0.1");
            dataSource.setDatabaseName("sms_login");
            dataSource.setUser("pdsluser");
            dataSource.setPassword("P@Dsl949022");
            System.out.println("Connecting to vendit users database...");
            conn = dataSource.getConnection();
            this.conn = conn;
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
        return conn;
    }

    public void createTables() {
        try {
            Connection con = connect();
            String delivery = "create table if not exists dlr_status(dlr_id INT NOT NULL AUTO_INCREMENT, msisdn varchar(50), correlator varchar(50), status varchar(200), dlr_time timestamp NULL DEFAULT CURRENT_TIMESTAMP, primary key(dlr_id))";
            Statement stm = con.createStatement();
            stm.execute(delivery);
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
    }
}
