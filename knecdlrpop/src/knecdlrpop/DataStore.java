package knecdlrpop;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.*;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

/**
 *
 * @author julius
 */
public class DataStore {

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

    public Connection connect44() {
        Connection conn = null;
        try {

            //String myDriver = "org.gjt.mm.mysql.Driver";
            String myUrl = "jdbc:mysql://localhost/knecsms44?autoReconnect=true&useSSL=false";
            //String myUrl = "jdbc:mysql://localhost/knecsms44";
            //Class.forName(myDriver);
            //conn = DriverManager.getConnection(myUrl, "root", "1root2");
            conn = DriverManager.getConnection(myUrl, "pdsluser", "P@Dsl949022");

        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());

        }
        return conn;

    }

    public String sendReq(String param) throws IOException {
        //String request = "http://172.27.116.42:8084/KnecResults/QueryResults?";
        String request = "https://localhost:8081/KnecResults/QueryResults?";
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

    static {
        disableSslVerification();
    }

    private static void disableSslVerification() {
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

}
