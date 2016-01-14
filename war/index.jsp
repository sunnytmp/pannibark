<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<TITLE>
Pig Latin Script Engine Registration Form. Pig Latin As A Service.
</TITLE>
<link rel="stylesheet" href="style.css" type="text/css" />
<img border="0" src="menu.png" usemap = #example alt="Pulpit rock" align="top">
<map name=example>
<area shape=Rect Coords=0,0,40,40 Href="../piggyrun/Login.html">
<area shape=Rect Coords=239,14,360,34 Href="http://pig.apache.org/docs/r0.10.0/basic.html">
<area shape="Rect" coords="462,13,539,22" onclick="javascript:alert('This Feature Is Only Available On Enterprise Version \n Please contact us at info@riverlog.com.'); return false;" alt="Move Forward""/>
<area shape="Rect" coords="549,12,584,19" onclick="javascript:alert('Pig Latin Engine SaaS Version 3.1 \n For Enterprise Version, Please contact us at info@riverlog.com.'); return false;" alt="Move Forward""/>
<area shape="Rect" coords="369,14,452,21" onclick="javascript:alert('Please Log In, To Collaboratively Develop'); return false;" alt="Move Forward""/>
<area shape="Rect" coords="54,13,145,17" onclick="javascript:alert('Please Register To Access Query Window'); return false;" alt="Move Forward""/>

</map> 
<Head>

<h1><CENTER>Pig Latin Script Engine Registration Form. Pig As SaaS</CENTER> </h1>
</head>
<body>
<CENTER>
 <%!
 String token = "n";
String subscriptionduration= "n";
String paymentamount ="n";
String payerid ="n";
String itemamount="n";
String evalid = "n";

 %>
<%
if (request.getParameter("tokent") != null) {
  token = request.getParameter("tokent");
}
 if (request.getParameter("subscriptiondurationt") != null){
  subscriptionduration = request.getParameter("subscriptiondurationt");
 }
 if (request.getParameter("paymentamountt")!= null) {
  paymentamount = request.getParameter("paymentamountt");
 }
 if (request.getParameter("payeridt") != null){
  payerid = request.getParameter("payeridt");
 }
 if ( request.getParameter("itemamountt") != null) {
  itemamount = request.getParameter("itemamountt");
 }
 /* 
 out.println( "this is the value of post " + token);
out.println( "this is the value of post " + subscriptionduration);
out.println( "this is the value of post " + paymentamount);
out.println( "this is the value of post " + payerid);
out.println( "this is the value of post " + itemamount);
*/
if(token.toUpperCase().equals("N") ){
	if (request.getParameter("evalid") != null) {
	 evalid = request.getParameter("evalid");
	} else{
	 out.println("Hey Wassup?...You are not supposed to be here..");
	 return;
  }
}else {
	
	if (token == null ||  payerid == null 
	   || request.getParameter("paymentamountt")== null ||  request.getParameter("itemamountt") == null) {
		response.sendRedirect("http://ec2-52-88-98-175.us-west-2.compute.amazonaws.com:8080/piggyrun/Login.html"); 
	   }
}
     %>

<img border="0" src="piggy.png" alt="Pulpit rock" align="left" width="304" height="228">
<div style="position: relative; left: 0; top: 0;">
<img border="0" src="imagine.png" alt="Pulpit rock" align="right" width="304" height="228"> 
</div>
<P>

<form ACTION="../piggyrun/RegistrationServlet" METHOD="POST"> 
    
<div id="mainDiv">
         <div id="borderLeft">
		 <label for="name">First Name:</label>
<input id="name" name="firstname" type="text" autofocus required
title="must be alphanumeric in 6-12 chars">  
<BR>
<BR>
 <label for="name">Last Name:</label>
<input id="name" name="lastname" type="text">
<Br>
<Br>

<label for="emailid">Email ID:</label>
<input id="emailid" name="emailid" type="text" onblur="checkEmailStud()" required> 
 
<BR>
<BR>
<label for="confemailid">Confirm Email ID:</label>
<input id="confemailid" name="confemailid" type="text" onblur="checkEmailEquality()" required> 
<BR>
<BR>
<label for="password">Password</label>
<input id="password" name="password" type="password" required> 
<BR>
<BR>
<label for="confpassword">Confirm Password:</label>
<input type="password" name="confpassword" required> 
<BR>
<BR>

<label for="countrycombo">Select Country:</label>
<select id="choice" name="choice">
    <option value="0" selected="selected">Choose...</option>
    <option value="USA">U.S.A</option>
    <option value="United Kingdom">United Kingdom</option>
    <option value="India">India</option>
</select>
<BR>
<BR>
<label for="agreement">I Agree:</label>
<input type="checkbox" required>I have read and agree to this Terms of Service.
<BR>
<% if (evalid.trim().equals("True") ) { %>
<input type="hidden" name="tokent" value="<%= token %>">
<input type="hidden" name="subscriptiondurationt" value="<%= subscriptionduration %>">
<input type="hidden" name="paymentamountt" value="<%= paymentamount %>">
<input type="hidden" name="payeridt" value="<%= payerid %>">
<input type="hidden" name="itemamountt" value="<%= itemamount %>">
<% } else{ %>
<input type="hidden" name="evalid" value="<%= evalid.trim() %>">
<% } %>
<input type="submit" name="submit" value="Submit" class="btn"/>
</div>
		
</div><!--end mainDiv-->
</form> 
<script type="text/javascript">
<!--
<!-- if (window.location != "") {  -->
	<!-- window.location.href = ""; -->
<!--	}  -->


</CENTER>
</body>
</HTML>