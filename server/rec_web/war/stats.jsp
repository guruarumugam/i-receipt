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
<%@ page import="java.util.Calendar" %>
<%@ page import="singer.store" %>
<%@ page import="java.util.TreeMap" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%@ page import="singer.compareDouble" %>
<%@ page import="singer.DateAndCost" %>
<%@ page import="java.util.ArrayList" %>


<html>
<head>

    <link type="text/css" rel="stylesheet" href="/stylesheets/secondary_style.css" />
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
response.sendRedirect("/index.jsp"); }
%></div>
        </div>

            <ul id="navigation">

                <li class="on"><a href="/index.jsp">Home</a></li>
                <li class="on"><a href="/listview.jsp">Receipts</a></li>
                <li class="current"><a href="#">Stats</a></li>
                <li class="on"><a href="#">Setting</a></li>
            

            </ul>
    <div id="content">


<!--Load the AJAX API-->
    <script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <script type="text/javascript">
    <%
    
	PersistenceManager pm = PMF.get().getPersistenceManager();
    Query query = pm.newQuery(iReceipt.class);
	query.setFilter("user_id == id");
	query.declareParameters("String id");
    List<iReceipt> receipts = (List<iReceipt>) query.execute(user.getUserId());
	double sum_shop=0,sum_dining=0,sum_travel=0,sum_car=0;
	double sum_rent=0,sum_groc=0,sum_present=0,sum_enter=0,sum_house=0,sum_other=0;

		for (iReceipt rec:receipts){
			if (rec.getCategory().equals("Shopping")){
				sum_shop+=rec.getTotal();
			}
			if (rec.getCategory().equals("Dining")){
				sum_dining+=rec.getTotal();
			}
			if (rec.getCategory().equals("Travel")){
				sum_travel+=rec.getTotal();
			}
			if (rec.getCategory().equals("Car")){
				sum_car+=rec.getTotal();
			}
			if (rec.getCategory().equals("Rent")){
				sum_rent+=rec.getTotal();
			}
			if (rec.getCategory().equals("Groceries")){
				sum_groc+=rec.getTotal();
			}
			if (rec.getCategory().equals("Presents")){
				sum_present+=rec.getTotal();
			}
			if (rec.getCategory().equals("Entertainment")){
				sum_enter+=rec.getTotal();
			}
			if (rec.getCategory().equals("Household goods")){
				sum_house+=rec.getTotal();
			}
			if (rec.getCategory().equals("Other")){
				sum_other+=rec.getTotal();
			}
		}

    pm.close();
%>
      // Load the Visualization API and the piechart package.
      google.load('visualization', '1', {'packages':['corechart']});
      
      // Set a callback to run when the Google Visualization API is loaded.
      google.setOnLoadCallback(drawChart);
      
      // Callback that creates and populates a data table, 
      // instantiates the pie chart, passes in the data and
      // draws it.
      function drawChart() {

      // Create our data table.
        var data = new google.visualization.DataTable();
        data.addColumn('string');
        data.addColumn('number');
        data.addRows([
          ['Shopping',<%=sum_shop%>],
          ['Dining', <%=sum_dining%>],
          ['Travel', <%=sum_travel%>],
		  ['Car',<%=sum_car%>],
          ['Rent',<%=sum_rent%>],
		  ['Groceries',<%=sum_groc%>],
		  ['Presents',<%=sum_present%>],
		  ['Entertainment',<%=sum_enter%>],
		  ['Household goods',<%=sum_house%>],
		  ['Other',<%=sum_other%>],
        ]);

        // Instantiate and draw our chart, passing in some options.
        var chart = new google.visualization.PieChart(document.getElementById('chart_cat'));
        chart.draw(data, {width:600, height: 360, is3D: true, title: 'Amount of Money Spent by Category'});
      }
    </script>
    
 
 
    <script type="text/javascript">
	<%
		double temp_cost;
		String value;
		int k;
		Map<String,Double> stores = new HashMap<String,Double>();
		compareDouble comp= new compareDouble(stores);
		TreeMap<String,Double> storesOrder = new TreeMap<String,Double>(comp);
		store[] listStores = new store[10];
		
		for (iReceipt rec:receipts){
			if (stores.containsKey(rec.getStoreName())){
				temp_cost=stores.get(rec.getStoreName())+rec.getTotal();
				stores.remove(rec.getStoreName());
				stores.put(rec.getStoreName(),temp_cost);
			}
			else{
				stores.put(rec.getStoreName(),rec.getTotal());
			}
		}
		//storesOrder.putAll(stores);
		for (String key2 : stores.keySet()) {
			storesOrder.put(key2,stores.get(key2));
		}
		int l=0,m=0;
		Double te=0.0;
		for (String key : storesOrder.keySet()) {
			if (l<10){
				listStores[l]= new store(key, storesOrder.get(key));
		
				l++;
			}	
		}
		if (l<10){
			for (m=l;m<10; m++){
				listStores[m]= new store("",0.0);
			}
		}
		
	%>
      google.load("visualization", "1", {packages:["corechart"]});
      google.setOnLoadCallback(drawChart);
      function drawChart() {
        var data = new google.visualization.DataTable();
        data.addColumn('string', 'Store Name');
        data.addColumn('number', 'Total Expenses in The Store');
        data.addRows(10);
        data.setValue(0, 0, "<%=listStores[0].getName()%>");
        data.setValue(0, 1, <%=listStores[0].getCost()%>);
        data.setValue(1, 0, "<%=listStores[1].getName()%>");
        data.setValue(1, 1, <%=listStores[1].getCost()%>);
        data.setValue(2, 0, "<%=listStores[2].getName()%>");
        data.setValue(2, 1, <%=listStores[2].getCost()%>);
        data.setValue(3, 0, "<%=listStores[3].getName()%>");
        data.setValue(3, 1, <%=listStores[3].getCost()%>);
		data.setValue(4, 0, "<%=listStores[4].getName()%>");
        data.setValue(4, 1, <%=listStores[4].getCost()%>);
		data.setValue(5, 0, "<%=listStores[5].getName()%>");
        data.setValue(5, 1, <%=listStores[5].getCost()%>);
		data.setValue(6, 0, "<%=listStores[6].getName()%>");
        data.setValue(6, 1, <%=listStores[6].getCost()%>);
		data.setValue(7, 0, "<%=listStores[7].getName()%>");
        data.setValue(7, 1, <%=listStores[7].getCost()%>);
		data.setValue(8, 0, "<%=listStores[8].getName()%>");
        data.setValue(8, 1, <%=listStores[8].getCost()%>);
		data.setValue(9, 0, "<%=listStores[9].getName()%>");
        data.setValue(9, 1, <%=listStores[9].getCost()%>);

        var chart = new google.visualization.ColumnChart(document.getElementById('chart_div3'));
        chart.draw(data, {width: 800, height: 480, title: 'Top 10 Store Total Expenses',
                          hAxis: {title: 'Store Name', titleTextStyle: {color: 'red'}}
                         });
      }
    </script>
    
    <script type='text/javascript'>
	
	<%
		Map<IDate,Double> datesCosts = new HashMap<IDate,Double>();
		Double costs;
		Boolean boo=false;
		Double tempC=0.0;
		IDate id= new IDate();
		for (iReceipt rec:receipts){
			if (datesCosts.size()>0){
				for (IDate k1 : datesCosts.keySet()){
					if (rec.getRdate().equals(k1)){
						boo=true;
						tempC=datesCosts.get(k1);
						id=k1;
					}
				}
			}
			if (boo){
				costs = tempC+rec.getTotal();
				datesCosts.remove(id);
				datesCosts.put(rec.getRdate(),costs);
			}
			else{
				datesCosts.put(rec.getRdate(),rec.getTotal());
			}
			boo=false;
		}
		
		int mapSize= datesCosts.size();
		DateAndCost[] arrayDateandCost = new DateAndCost[mapSize];
		int place=0;
		for (IDate keys : datesCosts.keySet()) {
			arrayDateandCost[place]=new DateAndCost(keys,datesCosts.get(keys));
			place++;
		}
		
	%>
	
      google.load('visualization', '1', {'packages':['annotatedtimeline']});
      google.setOnLoadCallback(drawChart);
      function drawChart() {
        var data = new google.visualization.DataTable();
        data.addColumn('date', 'Date');
        data.addColumn('number', 'Expense');
        data.addColumn('string', 'title1');
        data.addColumn('string', 'text1');
		
		<%for(int pl=0;pl<arrayDateandCost.length;pl++){
			 out.print("data.addRows([[new Date("+arrayDateandCost[pl].getDate().getYear()+","+ (arrayDateandCost[pl].getDate().getMonth()-1) +", "+ arrayDateandCost[pl].getDate().getDay()+"),"+arrayDateandCost[pl].getCost()+",undefined, undefined]]);");}%>


        var chart = new google.visualization.AnnotatedTimeLine(document.getElementById('chart_div4'));
		chart.draw(data, {displayAnnotations: true});	
      }
    </script>
    <div id="caption"> Statistics:</div>
    <div align="center" id="chart_cat"></div>
    
	<div align="center" id="chart_div3"></div>
    <br>
    <div align="center"  id="chart_div4" style='width: 800px; height: 360px;'></div>

    
    </div>
    
    
    <div id="footer">
        <div id="copyright">&copy; 2010 All Rights Reserved. Designed by the iReceipt team.</a>
    </div>
</div>

</body>
</html>
