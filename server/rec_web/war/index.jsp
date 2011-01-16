<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="javax.jdo.PersistenceManager" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="singer.iReceipt" %>
<%@ page import="singer.PMF" %>
<%@ page import="google.proj.IDate;" %>


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
<p>Hello guest!
<a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Sign in</a> to browse your receipts.
</p>
<%
    }
%></div>
        </div>
<ul id="navigation">

                <li class="current"><a href="#">Home</a></li>
                <li class="on"><a href="/listview.jsp">Receipts</a></li>
                <li class="on"><a href="stats.jsp">Stats</a></li>
                <li class="on"><a href="">Setting</a></li>
 </ul>
<div id="content">
<table class="table3"><tr><td>
<table class="table3"><tr><div id="title">Ireceipt</div><div id="small_title">Smart solution for managing your rceipts and expenses.</div></tr><tr><td><h2>Save</h2>
     <p>Gain easy access to your receipts from anywhere at anytime.
     </p></td><td><h2>Manage</h2>
      <p class="small_title">Manage and Update your information online with full synchronization with your Android device.</p></td></tr><tr><td>
      <h2>Analize</h2>
      <p class="small_title">Enjoy smart analysis of your data, by different filters.</p></td><td> <h2>It's simple!</h2>
      <p class="small_title">Very easy to use interface. No new account is needed. Just use your google account to sign in. </p></td></tr></table>
 </td><td><img src="images/internet-statistics.png"/></td></tr></table>
     
        
       
     
       <h2>It's FREE!</h2>
  <p class="small_title">We offer our service free of charge!</p>
<br><br>
</div>
    <div id="footer">
        <div id="copyright">&copy; 2010 All Rights Reserved. Designed by the iReceipt team.</a>
    </div>
</div>
</div>
</div>
</body>
</html>
