<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="es.eucm.mokap.backend.controller.admin.AdminController" %>
<%@ page import="es.eucm.mokap.backend.controller.admin.MokapAdminController" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
</head>

<body>

<%
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    if (user == "mokap@mokap.es") {

    } else {
    %>
    <p>Hello!
        <a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Sign in</a>
        to continue.</p>
    <%
        }
    %>
    </body>
    </html>