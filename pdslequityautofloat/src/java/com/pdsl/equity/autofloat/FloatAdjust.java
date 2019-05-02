/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.equity.autofloat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

/**
 *
 * @author koks
 */
public class FloatAdjust {

    public String sendPost(String amount, String date, String tranId, String clientid) throws Exception {
        String respo = "";
        String url = "http://41.204.194.188:9135/equitynotification/notification?";

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        // add header
        //post.setHeader("User-Agent", USER_AGENT);
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("tranDate", date));
        urlParameters.add(new BasicNameValuePair("tranAmount", amount));
        urlParameters.add(new BasicNameValuePair("tranCurrency", "KES"));
        urlParameters.add(new BasicNameValuePair("tranType", "CASH"));
        urlParameters.add(new BasicNameValuePair("tranParticular", "deposit"));
        urlParameters.add(new BasicNameValuePair("tranId", tranId));
        urlParameters.add(new BasicNameValuePair("refNo", clientid.trim()));
        // urlParameters.add(new BasicNameValuePair("clientId", "equity"));

        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        HttpResponse response = client.execute(post);
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + post.getEntity());
        System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        System.out.println(result.toString());
        respo = result.toString();
        return respo;

    }
}
