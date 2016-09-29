<%-- 
    Document   : response
    Created on : Sep 27, 2016, 12:37:50 PM
    Author     : mark
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP LZWTree Response</title>
    </head>
    <body>
        <h1>LZWTree Servlet Response</h1>
        <a href="#Bottom"><input type="button" value="To the Bottom"/></a>
        <p>${requestScope.message}</p>
        <hr/>
        <a name="Bottom"/>
        <a href="index.html"><input type="button" value="Back"/></a>
    </body>
</html>