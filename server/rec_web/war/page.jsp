<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="javax.jdo.PersistenceManager" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="singer.iReceipt" %>
<%@ page import="singer.PMF" %>
<%@ page import="google.proj.IDate" %>


<html>
<head>
       <link type="text/css" rel="stylesheet" href="/stylesheets/style.css" />
</head>
<body>
<div id="wrapper">
    <div id="header">

        <div id="logo">
        </div>
        <div id="updates">
      
        </div>
        <div id="login">
        <div id="loginwelcome"><%
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    if (user != null) {
%>
<p>Hello, <%= user.getNickname() %>! (You can
<a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">sign out</a>.)</p>
<%
    } else {
%>
<p>Hello!
<a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Sign in</a>
</p>
<%
    }
%></div>
        </div>

            <ul id="navigation">

                <li class="on"><a href="#">Home</a></li>
                <li class="on"><a href="#">Receipts</a></li>
                <li class="on"><a href="#">Stats</a></li>
                <li class="on"><a href="#">Setting</a></li>
            

            </ul>
    <div id="content">
    
<% 
    PersistenceManager pm = PMF.get().getPersistenceManager();
    String query = "select from " + iReceipt.class.getName();
	// need to get r from the id i get from singer page 
    List<iReceipt> receipts = (List<iReceipt>) pm.newQuery(query).execute();
	int i=0;
    if (receipts.isEmpty()) {
%>
<p>no receipts</p>
<%
    } else {%>
<% for (iReceipt r : receipts) { r.setStoreName("apple"); r.setNotes("aasdasd asd  asd asd as das das da sda sd asd as da sd");%>
<p align="center" style="font:Arial, Helvetica, sans-serif;color:#5C7B2F; font-size:40px" > <%= r.getStoreName() + " " +r.getDate_s() %></p> 
 
<p align="center" style="font:Arial, Helvetica, sans-serif;color:#5C7B2F; font-size:22px" > Price: <%=r.getTotal()%> Cat: <%=r.getCategory()%></p>
 
 
<div id="content"><img align="absbottom" src="/images/r1.jpg" width="250" height="750" />
<table align="center"  cellspacing="100" id="header"> <tr> <td width="250"> <img src="images/notes.png"> <br> <strong style="font:'Times New Roman', Times, serif"> <%= r.getNotes()%> </strong> </td>
<td>&nbsp;</td>
 </tr>
 </table>
        <%
    }}
    pm.close();
%>
    

      

</div>
<div id="footer">
        <div id="copyright">&copy; 2010 All Rights Reserved. Designed by singer noiman</a>.
    </div>
</div>

</body>
</html>
