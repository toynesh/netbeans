/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coophy.com;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author coolie
 */
public class ImportChannels {

    public static void main(String[] args) {
        List<String> linescontent = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("/home/coolie/Desktop/tv.txt"));
            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = br.readLine()) != null) {
                //System.out.println("Array Add Line: " + line);
                linescontent.add(line);
            }
            br.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ImportChannels.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ImportChannels.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Total Lines: " + linescontent.size());

        List<String> fullline = new ArrayList<>();
        int nextln = 2;
        String firstline = "";
        String secondline = "";
        for (int i = 0; i < linescontent.size(); i++) {
            System.out.println("Line: " + i);
            if (i == nextln) {
                secondline = linescontent.get(i);
                fullline.add(firstline + " url " + secondline);
                System.out.println("firstline: " + firstline);
                System.out.println("secondline: " + secondline);
                System.out.println("fullline: " + firstline + " url " + secondline);
                nextln = nextln + 2;
            } else {
                firstline = linescontent.get(i);
            }
        }
        System.out.println("Total full Lines: " + fullline.size());

        DataStore data = new DataStore();
        for (int i = 0; i < fullline.size(); i++) {
            String tvg_name = "";
            String tvg_logo = "";
            String group_title = "";
            String source_url = "";

            String namesplit[] = fullline.get(i).split("tvg-name=\"");
            String namesplit2[] = namesplit[1].split("\"");
            tvg_name = namesplit2[0];
            String logosplit[] = fullline.get(i).split("tvg-logo=\"");
            String logosplit2[] = logosplit[1].split("\"");
            tvg_logo = logosplit2[0];
            String groupsplit[] = fullline.get(i).split("group-title=\"");
            String groupsplit2[] = groupsplit[1].split("\"");
            group_title = groupsplit2[0];
            String urlsplit[] = fullline.get(i).split(" url ");
            source_url = urlsplit[1];

            String insert = "insert into chlist (tvg_name,tvg_logo,group_title,source_url) values (?,?,?,?)";
            try {
                Connection con = data.connect();
                PreparedStatement prep = con.prepareStatement(insert);
                prep.setString(1, tvg_name);
                prep.setString(2, tvg_logo);
                prep.setString(3, group_title);
                prep.setString(4, source_url);
                prep.execute();
                prep.close();
                con.close();
            } catch (SQLException sq) {}
            System.out.println("Added to DB Fulline: " + i);
        }
    }
}
