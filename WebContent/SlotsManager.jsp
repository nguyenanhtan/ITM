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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Quan ly thong tin kip bao ve</title>
</head>
<body onload ="loadSlotsList()">
<%
if(session.getAttribute("username") == null){
	response.sendRedirect("Login.jsp");
}else{
	String uid = session.getAttribute("username").toString();
	out.println("Hello " + uid);
}
%>
<a href=Logout.jsp>Logout</a>

<h1>Quan ly thong tin kip bao ve</h1>

<input type="button" value="Xoa het kip" onclick="deleteAllSlots()">

<form id="teacher-info" action="#"> 
<table border = 0>
<tr><td>Thoi gian:</td><td>
<input type="text" name="slot_description" id="slot_description" style="width:200px"></td></tr>
<tr><td></td><td><input type="button" name="add_slot" value="Them moi" style="width:100px" onclick="addSlot()"></td></tr>
</table></form>
<p id="test"></p>
<p id="slot-table"></p>

<a href="home.jsp">Back to Home</a>

<script type="text/javascript">

var slot_id = new Array();
var slot_description = new Array();


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
       xmlHttp = false ;  // No Browser accepts the XMLHTTP Object then false
     }
   }
   if (!xmlHttp && typeof XMLHttpRequest != 'undefined') {
     xmlHttp = new XMLHttpRequest();        //For Mozilla, Opera Browsers
   }
   return xmlHttp;  // Mandatory Statement returning the ajax object created
}
 
var xmlhttp = new getXMLObject(); //xmlhttp holds the ajax object

function deleteAllSlots(){
	//alert("delete all slots");
	if(xmlhttp){
		   var xmlStr = "<?xml version=" + "\"" + "1.0" + "\"" + " encoding=" + "\"" + "UTF-8" + "\"" + "?>";
		   xmlStr += "\n" + "<DeleteAllSlots>" + "\n" +  
		   "</DeleteAllSlots>";
		  
		    xmlhttp.open("POST","TeacherManager",true); //getname will be the servlet name
		    xmlhttp.onreadystatechange  = handleDeleteAllSlots;
		   
		    xmlhttp.overrideMimeType('text/xml');
		    xmlhttp.setRequestHeader('Content-Type', 'text/xml; charset=utf-8');
		    xmlhttp.send(xmlStr); //Posting to Servlet
		    
	}
}

function handleDeleteAllSlots(){
	if(xmlhttp.readyState == 4){
		if(xmlhttp.status == 200){
			var xmlData = xmlhttp.responseXML;
			var tablecontents = "";
			tablecontents += "<table class=" + '"' + "table table-bordered table-condensed" + '"' + "id=" + '"' + "userTable" + '"' + ">";
			tablecontents += "<thread>";
			tablecontents += "<th>ID</th>";
			tablecontents += "<th>Ten phong</th>";
			tablecontents += "</thread>";
			tablecontents += "<tbody>";
			tablecontents += "</tbody>";
			tablecontents += "</table>";
			document.getElementById("slot-table").innerHTML = tablecontents;
		}
	}
	
}

function loadSlotsList(){
	
	if(xmlhttp){
		   var xmlStr = "<?xml version=" + "\"" + "1.0" + "\"" + " encoding=" + "\"" + "UTF-8" + "\"" + "?>";
		   xmlStr += "\n" + "<LoadSlotsList>" + "\n" +  
		   "</LoadSlotsList>";
		  
		    xmlhttp.open("POST","TeacherManager",true); //getname will be the servlet name
		    xmlhttp.onreadystatechange  = handleLoadSlotsList;
		   
		    xmlhttp.overrideMimeType('text/xml');
		    xmlhttp.setRequestHeader('Content-Type', 'text/xml; charset=utf-8');
		    xmlhttp.send(xmlStr); //Posting to Servlet
		    
	}
}

function handleLoadSlotsList(){
	if(xmlhttp.readyState == 4){
		if(xmlhttp.status == 200){
			var xmlData = xmlhttp.responseXML;
			var T = xmlData.getElementsByTagName("slot");
			
			var tablecontents = "";
			tablecontents += "<table class=" + '"' + "table table-bordered table-condensed" + '"' + "id=" + '"' + "userTable" + '"' + ">";
			tablecontents += "<thread>";
			tablecontents += "<th>ID</th>";
			tablecontents += "<th>Thoi gian</th>";
			tablecontents += "</thread>";
			tablecontents += "<tbody>";
			for(var i = 0; i < T.length; i++){
				tablecontents += "<tr id='tr" + T[i].getElementsByTagName("id")[0].firstChild.nodeValue + "'>";
				tablecontents += "<td>" + T[i].getElementsByTagName("id")[0].firstChild.nodeValue + "</td>" + "<td>" + decodeURIComponent(T[i].getElementsByTagName("description")[0].firstChild.nodeValue) + "</td>";
				tablecontents += "<td><a href=" + '"' + "EditSlot.jsp?description=" + decodeURIComponent(T[i].getElementsByTagName("description")[0].firstChild.nodeValue) + 
						"&id=" + T[i].getElementsByTagName("id")[0].firstChild.nodeValue + "\">Sua</td>";
				tablecontents += "<td><a href=" + '"' + "DeleteSlot.jsp?id=" + T[i].getElementsByTagName("id")[0].firstChild.nodeValue + "\">Xoa</td>";
				tablecontents += "</tr>";
			}
			tablecontents += "</tbody>";
			tablecontents += "</table>";
			document.getElementById("slot-table").innerHTML = tablecontents;
		}
	}
	
}

function addSlot(){
	document.getElementById("test").innerHTML = document.getElementById("slot_description").value;
		
		if(xmlhttp!= false){
			   var description = document.getElementById("slot_description").value;
			   
			   //name = Utf8Coder.encode(name);
			   var xmlStr = "<?xml version=" + "\"" + "1.0" + "\"" + " encoding=" + "\"" + "UTF-8" + "\"" + "?>";
			   xmlStr += "\n" + "<AddSlot>" + "\n";
			  xmlStr += "<description>" + encodeURIComponent(description) + "</description>";
			 
			   xmlStr += "</AddSlot>";
			   xmlhttp.open("POST","TeacherManager",true); //getname will be the servlet name
			    xmlhttp.onreadystatechange  = handleAddSlot;
			    //xmlhttp.onreadystatechange  = handleSubmitPoint;
			    xmlhttp.overrideMimeType('text/xml');
			    xmlhttp.setRequestHeader('Content-Type', 'text/xml');
			    xmlhttp.send(xmlStr); //Posting to Servlet

			    //xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
			    //xmlhttp.send("xml="+xml); //Posting to Servlet
		}
		
	}
	function handleAddSlot(){
		loadSlotsList();
	}
</script>

</body>
</html>