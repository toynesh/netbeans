<%-- 
    Document   : vendithome
    Created on : Sep 12, 2017, 11:21:32 AM
    Author     : juliuskun
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String server = request.getLocalAddr();
    String redirectURL = "https://vendit.pdslkenya.com/vendit/";
    response.sendRedirect(redirectURL);
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
    </body>
</html>
