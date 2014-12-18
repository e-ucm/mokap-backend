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

    if (user != null) {
        pageContext.setAttribute("user", user);
        AdminController fc = new MokapAdminController();
        if(fc.checkAllowedUser(user)){
            long id = Long.parseLong(request.getParameter("idfeat"));
            try{
                fc.unFeatureElement(id);
%>
                            <p>You unfeatured  the element. The results may take a while to update.</p>
                             <a href="admin.jsp">Back</a>
<%
            }catch(Exception e){
%>
                            <p>The arguments you supplied are not valid: <%= e.getMessage() %></p>
                             <a href="admin.jsp">Back</a>
<%
            }
        }else{

%>
                            <p>Your user is not allowed here!
                                <a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Sign in</a>
                                to continue.</p>
<%
        }


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