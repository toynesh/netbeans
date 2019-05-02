/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xyz.sms;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.json.*;

/**
 *
 * @author Julius
 */
@WebServlet("/upload")
@MultipartConfig
public class SendSMS extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>XYZ SMS</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SendSMS at " + request.getContextPath() + "</h1>");

            if (null != request.getParameter("description")) {
                String description = request.getParameter("description"); // Retrieves <input type="text" name="description">
                Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
                String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
                InputStream fileContent = filePart.getInputStream();

                System.out.println("Uploaded file:" + fileName);
                String hpath = System.getProperty("user.home");
                System.out.println("Home Path:" + hpath);

                for (Part part : request.getParts()) {
                    if (part != null && part.getSize() > 0) {
                        String contentType = part.getContentType();
                        System.out.println("Content-Type:" + contentType);
                        if (!contentType.equalsIgnoreCase("text/csv")) {
                            out.println("<h3 style='color:red'>Please Upload a a valid csv File !!</h3>");
                            continue;
                        }
                        part.write(hpath + "/" + fileName);
                    }
                }

                String csvFile = hpath + "/" + fileName;
                BufferedReader br = null;
                String line = "";
                String cvsSplitBy = ",";
                try {
                    br = new BufferedReader(new FileReader(csvFile));
                    int counter = 1;
                    String username = "sandbox";
                    String apiKey = "2f779a3c1302141097c4ea4094f2b9c2a68a5c0187285defcd8645c9d3211277";
                    JSONArray recipients = new JSONArray();
                    while ((line = br.readLine()) != null) {
                        // use comma as separator
                        String[] cells = line.split(cvsSplitBy);
                        if (counter > 1) {
                            //System.out.println("Sending Airtime to: " + cells[0] + " Phone-Number: " + cells[1] + " Amount: " + cells[2]);
                            out.println("<b>Sending Airtime to: </b>" + cells[0] + " <b>Phone-Number: </b>" + cells[1] + " <b>Amount: </b>" + cells[2] + "<br>");
                            recipients.put(new JSONObject().put("phoneNumber", "+"+cells[1]).put("amount", "KES "+cells[2]));
                        }
                        counter++;
                    }
                    String recipientStringFormat = recipients.toString();
                    System.out.println("JSONString: "+recipientStringFormat);
                    AfricasTalkingGateway gateway = new AfricasTalkingGateway(username, apiKey, "sandbox");
                    try {
                        JSONArray results = gateway.sendAirtime(recipientStringFormat);
                        int length = results.length();
                        out.println("<br><br><h2>AfricasTalking Response:</h2></<br>");
                        for (int i = 0; i < length; i++) {
                            JSONObject result = results.getJSONObject(i);
                            System.out.println(result.getString("status"));
                            out.println(result.getString("status")+ "<br>");
                            System.out.println(result.getString("amount"));
                            out.println(result.getString("amount")+ "<br>");
                            System.out.println(result.getString("phoneNumber"));
                            out.println(result.getString("phoneNumber")+ "<br>");
                            System.out.println(result.getString("discount"));
                            out.println(result.getString("discount")+ "<br>");
                            System.out.println(result.getString("requestId"));
                            out.println(result.getString("requestId")+ "<br>");
                            //Error message is important when the status is not Success
                            System.out.println(result.getString("errorMessage"));
                            out.println(result.getString("errorMessage")+ "<br>");
                            out.println("==============================================<br>");
                        }
                    } catch (Exception e) {
                        System.out.println("AfricasTalkingErr: "+e.getMessage());
                        out.println("<h3 style='color:red'>AfricasTalkingError: "+e.getMessage()+"</h3>");
                    }
                } catch (FileNotFoundException e) {
                    //e.printStackTrace();
                    System.out.println("Invalid or No file Uploaded");
                    out.println("<h3><span style='color:red'>Invalid or No file Uploaded!!</span> <span>Ensure the Server Root Directory has write permissions</span></h3>" + "Java Error: " + e);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException ex) {
                    Logger.getLogger(SendSMS.class.getName()).log(Level.SEVERE, "Json Err" + ex);
                } finally {
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e) {
                            out.println("<h3 style='color:red'>An error Occurred While Sending SMS</h3>" + "Java Error: " + e);
                            e.printStackTrace();
                        }
                    }
                }
            }

            out.println("<p style='color:red'>>>Upload the Employee csv file and Click send SMS<<</p>");
            out.println("<form action='" + request.getContextPath() + "/SendSMS' method='post' enctype='multipart/form-data'>");
            out.println("<input type='text' name='description' style='display:none' />");
            out.println("<input type='file' name='file' /><br><br>");
            out.println("<input type='submit' value='Send SMS' />");
            out.println("</form>");

            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
