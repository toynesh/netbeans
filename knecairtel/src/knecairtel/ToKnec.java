/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knecairtel;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONObject;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author julius
 */
public class ToKnec {

    public static String postInbox(String smsLinkId, String msisdn, String message) throws IOException {
        JSONArray jsonArray = new JSONArray();
        Date dt = new Date();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("requestType", "inbox");
        jsonObject.put("message", message);
        jsonObject.put("timeSent", "Date(-" + dt.getTime() + "-0000)");
        jsonObject.put("msisdn", msisdn);
        jsonObject.put("smsLinkId", smsLinkId);

        System.out.println(jsonObject);

        //{"password":"MlpopVUs8I3X","timeSent":"Date(-1549956454486-0000)","smsLinkId":"1236547555","message":"123456","msisdn":"254721178823","userName":"pdsl"}
        //{"result":"OK","status":"0"}{"result":"Error message","status":"1"}
        JSONObject errJson = new JSONObject();
        errJson.put("responseMessage", "UNAVAILABLE");
        errJson.put("responseStatus", "2");

        System.out.println(jsonObject);

        //{"password":"MlpopVUs8I3X","timeSent":"Date(-1549956454486-0000)","smsLinkId":"1236547555","message":"123456","msisdn":"254721178823","userName":"pdsl"}
        //{"result":"OK","status":"0"}{"result":"Error message","status":"1"}
        jsonArray.put(jsonObject);

        String requestJson = jsonArray.toString().replaceAll("[\\[\\]]", "");

        try {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, requestJson);

            Request request = new Request.Builder()
                    //.url( "http://knec.pdslkenya.com:8084/KnecShortcodeService/SendSMS")
                    .url("http://172.27.116.42:8084/KnecShortcodeService/PostInbox")
                    .post(body)
                    .addHeader("content-type", "application/json")
                    //.addHeader("authorization", "Bearer "+authenticate())
                    .addHeader("cache-control", "no-cache")
                    .build();

            Response response = client.newCall(request).execute();
            ResponseBody responseBody = response.body();
            String content = responseBody.string();
            JSONObject jsonObjectR = null;
            String responseStatus = null;
            String responseMessage = null;
            if (content.isEmpty() || content.equals("") || content == null) {
                System.out.println("CONTENT is null");
                responseMessage = "EMPTY";
            } else {

                try {
                    jsonObjectR = new JSONObject(content);
                    System.out.println("CONTENT " + content);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("CONTENT is BAAAD");
                    return errJson.toString();
                }
                responseStatus = jsonObjectR.getString("status");
                responseMessage = jsonObjectR.getString("result");

            }

            System.out.println("RESPONSE STUTUS " + responseStatus + " MESSAGE " + responseMessage);
            return responseMessage;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return errJson.toString();
    }
    private static final AtomicLong LAST_TIME_MS = new AtomicLong();

    public static long uniqueCurrentTimeMS() {
        long now = System.currentTimeMillis();
        while (true) {
            long lastTime = LAST_TIME_MS.get();
            if (lastTime >= now) {
                now = lastTime + 1;
            }
            if (LAST_TIME_MS.compareAndSet(lastTime, now)) {
                return now;
            }
        }
    }
    
    public static void main(String[] args) {
        try {
            ToKnec tk =  new ToKnec();
            tk.postInbox("ARTURE","254780922453","Here we gp");
        } catch (IOException ex) {
            Logger.getLogger(ToKnec.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
