/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.moserver;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.smpp.debug.Debug;
import org.smpp.debug.Event;
import org.smpp.debug.FileDebug;
import org.smpp.debug.FileEvent;
import org.smpp.pdu.DeliverSM;
import org.smpp.pdu.PDUException;
import org.smpp.pdu.WrongLengthOfStringException;
import org.smpp.smscsim.DeliveryInfoSender;
import org.smpp.smscsim.PDUProcessorGroup;
import org.smpp.smscsim.SMSCListener;
import org.smpp.smscsim.SMSCListenerImpl;
import org.smpp.smscsim.ShortMessageStore;
import org.smpp.smscsim.SimulatorPDUProcessor;
import org.smpp.smscsim.SimulatorPDUProcessorFactory;
import org.smpp.smscsim.util.Table;

/**
 *
 * @author julius
 */
public class Inbound extends HttpServlet {
    
     /**
     * Name of file with user (client) authentication information.
     */
    static String usersFileName = System.getProperty("usersFileName", "/opt/applications/smppclients/users.txt");

    /**
     * Directory for creating of debug and event files.
     */
    static final String dbgDir = "/opt/applications/";

    /**
     * The debug object.
     */
    static Debug debug = new FileDebug(dbgDir, "sim.dbg");

    /**
     * The event object.
     */
    static Event event = new FileEvent(dbgDir, "sim.evt");

    public static final int DSIM = 16;
    public static final int DSIMD = 17;
    public static final int DSIMD2 = 18;

    boolean keepRunning = true;
    private SMSCListener smscListener = null;
    private SimulatorPDUProcessorFactory factory = null;
    private PDUProcessorGroup processors = null;
    private ShortMessageStore messageStore = null;
    private DeliveryInfoSender deliveryInfoSender = null;
    private Table users = null;
    private boolean displayInfo = true;

    protected void start() throws IOException {
        if (smscListener == null) {
            //System.out.print("Enter port number> ");
            int port =8090; //Integer.parseInt(keyboard.readLine());
            System.out.print("Starting listener... ");
            smscListener = new SMSCListenerImpl(port, true);
            processors = new PDUProcessorGroup();
            messageStore = new ShortMessageStore();
            deliveryInfoSender = new DeliveryInfoSender();
            deliveryInfoSender.start();
            users = new Table(usersFileName);
            factory = new SimulatorPDUProcessorFactory(processors, messageStore, deliveryInfoSender, users);
            factory.setDisplayInfo(displayInfo);
            smscListener.setPDUProcessorFactory(factory);
            smscListener.start();
            System.out.println("started.");
        } else {
            System.out.println("Listener is already running.");
        }
    }
    protected void listClients() {
        if (smscListener != null) {
            synchronized (processors) {
                int procCount = processors.count();
                if (procCount > 0) {
                    SimulatorPDUProcessor proc;
                    for (int i = 0; i < procCount; i++) {
                        proc = (SimulatorPDUProcessor) processors.get(i);
                        System.out.print(proc.getSystemId());
                        if (!proc.isActive()) {
                            System.out.println(" (inactive)");
                        } else {
                            System.out.println();
                        }
                    }
                } else {
                    System.out.println("No client connected.");
                }
            }
        } else {
            System.out.println("You must start listener first.");
        }
    }
    protected void sendMessage(String shortCode, String msisdn, String message) throws IOException {
        if (smscListener != null) {
            int procCount = processors.count();
            if (procCount > 0) {
                String client;
                SimulatorPDUProcessor proc;
                listClients();
                if (procCount > 1) {
                    //System.out.print("Type name of the destination> ");
                    //client = keyboard.readLine();
                    client = "pavel";
                } else {
                    proc = (SimulatorPDUProcessor) processors.get(0);
                    client = proc.getSystemId();
                }
                for (int i = 0; i < procCount; i++) {
                    proc = (SimulatorPDUProcessor) processors.get(i);
                    if (proc.getSystemId().equals(client)) {
                        if (proc.isActive()) {
                            //System.out.print("Type the message> ");
                            //String message = keyboard.readLine();
                            DeliverSM request = new DeliverSM();
                            try {
                                request.setSourceAddr(shortCode);
                                request.setDestAddr(msisdn);
                                request.setShortMessage(message);
                                proc.serverRequest(request);
                                System.out.println("Message sent.");
                            } catch (WrongLengthOfStringException e) {
                                System.out.println("Message sending failed");
                                event.write(e, "");
                            } catch (IOException ioe) {
                            } catch (PDUException pe) {
                            }
                        } else {
                            System.out.println("This session is inactive.");
                        }
                    }
                }
            } else {
                System.out.println("No client connected.");
            }
        } else {
            System.out.println("You must start listener first.");
        }
    }

    //private static final Logger logger = LogManager.getLogger(Inbound.class);
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //response.setContentType("text/html;charset=UTF-8");
        try {
            //http://localhost:8080/knecmoserver/Inbound?shortCode=20076&msisdn=254789021421&message=14333106033
            //http://172.27.116.42:8084/knecmoserver/Inbound?shortCode=22261&msisdn=254789021421&message=14333106033
            String shortCode = request.getParameter("shortCode");
            String msisdn = request.getParameter("msisdn");
            String message = request.getParameter("message");
            System.out.println("shortCode:" + shortCode);
            System.out.println("msisdn:" + msisdn);
            System.out.println("message:" + message);
            start();
            sendMessage(shortCode, msisdn, message);

        } catch (Exception ex) {

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
