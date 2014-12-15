<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
</head>

<body>

<%
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    if (user != null) {
        pageContext.setAttribute("user", user);
%>

<p>Hello, ${fn:escapeXml(user.nickname)}! (You can
    <a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">sign out</a>.)</p>
    <form action="/adminbk.jsp" method="get">
        <div><input type="text" name="guestbookName" value="${fn:escapeXml(guestbookName)}"/></div>
        <div><input type="submit" value="Switch Guestbook"/></div>
    </form>
<%
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