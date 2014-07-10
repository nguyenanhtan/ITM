<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@page import="java.net.URLEncoder" %>
<%@page import="java.net.URLDecoder" %>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>Cap nhat thong tin dot bao ve</title>
</head>
<body >
<%
if(session.getAttribute("username") == null){
	response.sendRedirect("Login.jsp");
}else{
	String uid = session.getAttribute("username").toString();
	out.println("Hello " + uid);
}
%>
<a href=Logout.jsp>Logout</a>
<form id="edit_defenseSession" action="#" >
<table border=0>
<tr><td>ID:</td><td> <input type="text" name="defenseSession_id" id="defenseSession_id" value="<%=request.getParameter("id")%>"  readonly="readonly"/></td></tr>
<tr><td>Ngay:</td><td> <input type="text" name="defenseSession_description" id="defenseSession_description" value="<%=request.getParameter("description")%>"/></td></tr>
<tr><td>Active:</td><td> <input type="text" name="defenseSession_active" id="defenseSession_active" value="<%=request.getParameter("active")%>"/></td></tr>


<tr><td></td><td><input type="button" value="Cap nhat" style = "width:100px" onclick = "updateDefenseSession()"/></td></tr>
</table>
</form>
<script type="text/javascript">


function getXMLObject()  //XML OBJECT
{
   var xmlHttp = false;
   try {
     xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");  // For Old Microsoft Browsers
   }
   catch (e) {
     try {
       xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");  // For Microsoft IE 6.0+
     }
     catch (e2) {
       xmlHttp = false;   // No Browser accepts the XMLHTTP Object then false
     }
   }
   if (!xmlHttp && typeof XMLHttpRequest != 'undefined') {
     xmlHttp = new XMLHttpRequest();        //For Mozilla, Opera Browsers
   }
   return xmlHttp;  // Mandatory Statement returning the ajax object created
}
 
var xmlhttp = new getXMLObject(); //xmlhttp holds the ajax object



function updateDefenseSession(){
	if(xmlhttp!=false){
		var description =document.getElementById("defenseSession_description").value;
		
		var id = document.getElementById("defenseSession_id").value;
		var active = document.getElementById("defenseSession_active").value;
		
		
		
	   var xmlStr = "<?xml version=" + "\"" + "1.0" + "\"" + " encoding=" + "\"" + "UTF-8" + "\"" + "?>";
	   xmlStr += "\n" + "<UpdateDefenseSession>" + "\n";
	   xmlStr += "<id>" + id + "</id>";
	   xmlStr += "<description>" + description + "</description>";
	   xmlStr += "<active>" + active + "</active>";
	  
	   xmlStr += "</UpdateDefenseSession>";
	 
	    xmlhttp.open("POST","TeacherManager",true); //getname will be the servlet name

	    xmlhttp.onreadystatechange  = handleUpdateDefenseSession;

	    xmlhttp.overrideMimeType('text/xml');
	    xmlhttp.setRequestHeader('Content-Type', 'text/xml');
	    xmlhttp.send(xmlStr); //Posting to Servlet

	   
	}
	
}
function handleUpdateDefenseSession(){
	//alert("Update defenseSession successfully");
	window.location.href = "DefenseSessionsManager.jsp";
}

</script>




</body>
</html>