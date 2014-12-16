<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="es.eucm.mokap.backend.controller.admin.AdminController" %>
<%@ page import="es.eucm.mokap.backend.controller.admin.MokapAdminController" %>
<%@ page import="es.eucm.mokap.backend.model.search.SearchParams" %>
<%@ page import="es.eucm.mokap.backend.model.search.SearchParamsFactory" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
</head>

<body>

<%
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    if (user == "mokap@mokap.es") {
        pageContext.setAttribute("user", user);
        AdminController fc = new MokapAdminController();
        SearchParams sp = SearchParamsFactory.createFeaturedSearch(null);
        String featTable = fc.getFeaturedResourcesAsTable(sp);

%>

<p>Hello, ${fn:escapeXml(user.nickname)}! (You can
    <a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">sign out</a>.)</p>
    <hr>
    <%= featTable %>
    <hr>
    <form id="addfeatured" name="addfeatured" method="post" action="addfeatured.jsp">
    Insert the ID: <input type="text" id="idfeat" name="idfeat"/><br>
    Insert the feat. category: <input type="text" id="catfeat" name="catfeat"/><br>
    <input type="submit"/>
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