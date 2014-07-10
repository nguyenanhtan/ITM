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
<title>Cap nhat thong tin phong</title>
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
<form id="edit_room" action="#" >
<table border=0>
<tr><td>ID:</td><td> <input type="text" name="room_id" id="room_id" value="<%=request.getParameter("id")%>"  readonly="readonly"/></td></tr>
<tr><td>Ten phong:</td><td> <input type="text" name="room_description" id="room_description" value="<%=request.getParameter("description")%>"/></td></tr>


<tr><td></td><td><input type="button" value="Cap nhat" style = "width:100px" onclick = "updateRoom()"/></td></tr>
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



function updateRoom(){
	//alert("update");
	//alert(encodeURIComponent(name));
	//alert("delete professor " + name);
	if(xmlhttp!=false){
		var description =document.getElementById("room_description").value;
		
		var id = document.getElementById("room_id").value;
		
		
		//alert("Add teacher");
	   //var name = document.getElementById("teacher_name").value;
	   //name = Utf8Coder.encode(name);
	   var xmlStr = "<?xml version=" + "\"" + "1.0" + "\"" + " encoding=" + "\"" + "UTF-8" + "\"" + "?>";
	   xmlStr += "\n" + "<UpdateRoom>" + "\n";
	   xmlStr += "<id>" + id + "</id>";
	   xmlStr += "<description>" + description + "</description>";
	  
	   xmlStr += "</UpdateRoom>";
	 
	    xmlhttp.open("POST","TeacherManager",true); //getname will be the servlet name
	    //alert("POST OK");
	    xmlhttp.onreadystatechange  = handleUpdateRoom;
	    //alert("handleDeleteTeacher OK");
	    //xmlhttp.onreadystatechange  = handleSubmitPoint;
	    xmlhttp.overrideMimeType('text/xml');
	    //alert("overrideMimeType OK");
	    xmlhttp.setRequestHeader('Content-Type', 'text/xml');
	    //alert("setRequestHeader OK");
	    xmlhttp.send(xmlStr); //Posting to Servlet

	   
	}
	
}
function handleUpdateRoom(){
	//alert("Update room successfully");
	window.location.href = "RoomsManager.jsp";
}

</script>




</body>
</html>