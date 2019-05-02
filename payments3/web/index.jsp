<%-- 
    Document   : index
    Created on : Oct 19, 2011, 11:48:12 AM
    Author     : hesbon
--%>

<%@page import="com.pdsl.payments.DelayedKplc"%>
<%@page import="java.util.Enumeration"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
       <body>
        
        <% 
            try{
                DelayedKplc delayed=new DelayedKplc();
       //URI endpoint = new URI("http://172.31.183.50:8084/Notification/SmsNotificationService");
             Enumeration list=request.getParameterNames();
        while(list.hasMoreElements())      {
            String names=(String)list.nextElement();
            String values=request.getParameter(names).toString();
            System.out.println(" Parameters: "+names+" Values: "+values);
            //System.out.println("Values"+values);
        }

                    
       
       String address=request.getParameter("destination");
    //   String sender=request.getParameter("originator");
       String message=request.getParameter("message");
       String msgid=request.getParameter("msgid").replace("-","");
       //String spid=request.getParameter("spid");
     //  String port=request.getParameter("spid");
       delayed.push(address, message, msgid);
                   
       out.println("ok");
            }catch(Exception ex){
                out.println("fail");
                ex.printStackTrace();
            }
       
       
       %>      

    </body>
</html>
