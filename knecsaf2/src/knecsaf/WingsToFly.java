/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knecsaf;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import static java.lang.Integer.parseInt;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author coolie
 */
public class WingsToFly {

    public static void main(String[] args) {
        WingsToFly data = new WingsToFly();

        String fileName = "/home/coolie/Desktop/wingstofly.txt";
        String line = null;
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            PrintWriter writer = new PrintWriter("/home/coolie/Desktop/WOFL.csv", "UTF-8");
            writer.println("EGFStudentCode,Index Number,Grade,AGP,Subject,Subject,Subject,Subject,Subject,Subject,Subject,Subject");
            int ind = 1;
            while ((line = bufferedReader.readLine()) != null) {
                String linesplit[] = line.split(">>");
                String stindex = linesplit[0];
                String efcode = linesplit[1];
                System.out.println("Checking: " + ind + " " + stindex);
                String snd_txt = data.getMessage(stindex);
                System.out.println("Serverres: " + snd_txt);
                if (snd_txt.contains("does not exist")) {
                    writer.println(efcode + "," + stindex + ",wrong index number,,,,,,,,,");
                } else {
                    String resplit[] = snd_txt.split("INDEX: ");
                    String rstr = resplit[1].replaceAll("  KNEC HELPLINE 0800724900", "");
                    rstr = rstr.replaceAll(stindex + " MEAN GRADE ", "");
                    System.out.println("String to split: " + rstr);
                    String splited[] = rstr.split(" ");
                    System.out.println("Splitted Size: " + splited.length);
                    String grade = splited[0];
                    String agp = splited[2];
                    String Subject1 = splited[3] + " " + splited[4];
                    String Subject2 = splited[5] + " " + splited[6];
                    String Subject3 = splited[7] + " " + splited[8];
                    String Subject4 = splited[9] + " " + splited[10];
                    String Subject5 = splited[11] + " " + splited[12];
                    String Subject6 = splited[13] + " " + splited[14];
                    String Subject7 = " ";
                    String Subject8 = " ";
                    if (splited.length == 19) {
                        Subject7 = splited[15] + " " + splited[16];
                        Subject8 = splited[17] + " " + splited[18];
                    }
                    System.out.println(efcode + "||" + stindex + "||" + grade + "||" + agp + "||" + Subject1 + "||" + Subject2 + "||" + Subject3 + "||" + Subject4 + "||" + Subject5 + "||" + Subject6 + "||" + Subject7 + "||" + Subject8);
                    writer.println(efcode + "," + stindex + "," + grade + "," + agp + "," + Subject1 + "," + Subject2 + "," + Subject3 + "," + Subject4 + "," + Subject5 + "," + Subject6 + "," + Subject7 + "," + Subject8);
                }
                ind++;
            }
            writer.close();
            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");
        } catch (IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");
        }
    }

    public String sendReq(String param) throws IOException {
        String request = "http://172.27.116.42:8084/KnecResults/QueryResults?";
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
