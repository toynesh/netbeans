package knecsafdlry;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
        createTables();

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

    public void createTables() {
        try {
            Connection con = connect();
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
            Statement stm = con.createStatement();
            stm.execute(dlry);
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
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
}
