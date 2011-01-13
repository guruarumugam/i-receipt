<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="javax.jdo.PersistenceManager" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="javax.jdo.Query"%>
<%@ page import="singer.iReceipt" %>
<%@ page import="singer.PMF" %>
<%@ page import="google.proj.IDate" %>
<%@ page import="java.lang.Integer"%>


<html>
<head>
    <link type="text/css" rel="stylesheet" href="/stylesheets/style.css" />
</head>
<body>
<%long resaults_per_page=10;
long num_of_entries=-1;%>
 <%int prev_page=0; 
		if(request.getParameter("last_page_to")!=null)
		prev_page=Integer.parseInt(request.getParameter("last_page_to")); %>
        
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
response.sendRedirect("/index.jsp"); }
%></div>
        </div>

            <ul id="navigation">

                <li class="on"><a href="/index.jsp">Home</a></li>
                <li class="current"><a href="/listview.jsp">Receipts</a></li>
                <li class="on"><a href="stats.jsp">Stats</a></li>
                <li class="on"><a href="">Setting</a></li>

            </ul>
    <%
    PersistenceManager pm = PMF.get().getPersistenceManager();
     Query query = pm.newQuery(iReceipt.class);
	 query.setFilter("user_id == id");
	 query.declareParameters("String id");
	 List<iReceipt> receipts;
 	 if(num_of_entries==-1){
		 receipts = (List<iReceipt>) query.execute(user.getEmail());
		 if (receipts.isEmpty()) 
     		 num_of_entries=0;
		 else
		     num_of_entries=receipts.size();
	 }
	 query.setRange(prev_page*resaults_per_page,(prev_page+1)* resaults_per_page);
	 receipts = (List<iReceipt>) query.execute(user.getEmail());
   
	
	
	
	int i=0;
    if (receipts.isEmpty()) {
%> <div id="content">
<p>no receipts</p>
<%
    } else {%>
     <div id="content">
   <div id="itsthetable"><table width="70%" align="center" summary="receits"><caption>
    Receipts List:
    </caption>
    <thead><tr><th scope="col">Date</th><th scope="col">Store Name</th><th scope="col">Total</th><th scope="col">Category</th></tr></thead><tfoot><tr><th scope="row">Total:</th><td colspan="4"><%=num_of_entries%> receipts</td></tr></tfoot>
<tbody>
	<%for (iReceipt r : receipts) {
		if (i%2==0){%>
			 <tr>
           <%}else{%>
           	<tr class="odd">
            <%}%>
             <Th scope="row"><a href="<%="/recview.jsp?id="+pm.getObjectId(r)%>"><%=r.getDate_s()%></a></th><td><%=r.getStoreName()%></td><td><%=r.getTotal()%></td><td><%=r.getCategory()%></td></tr>
             
 <%i++;
        }%>
        </tbody></table></div>
        <div id=tnav>
        <table align="center" class="table2">
        <tr><th scope="col"><%if(prev_page!=0) {%><a href="<%="/listview.jsp?last_page_to="+(prev_page-1)%>">prev</a><%}%></th><th scope="col"> page <%=((prev_page+1)+" of "+(num_of_entries/resaults_per_page+1))%></th><th scope="col"><%if((num_of_entries/resaults_per_page+1)!=(prev_page+1)){%><a href="<%="/listview.jsp?last_page_to="+(prev_page+1)%>">next</a><%}%></th></tr>
        </table>
      </div>
        <%
    }
    pm.close();
%>
    
    
    
    
     </div>
      

    </div>
    <div id="footer">
        <div id="copyright">&copy; 2010 All Rights Reserved. Designed by the iReceipt team.</a>
    </div>
</div>

</body>
</html>
