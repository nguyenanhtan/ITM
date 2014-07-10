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
<title>Quan ly thong tin ve lop</title>
</head>
<body onload="loadClassesList()">
<%
if(session.getAttribute("username") == null){
	response.sendRedirect("Login.jsp");
}else{
	String uid = session.getAttribute("username").toString();
	out.println("Hello " + uid);
}
%>
<a href=Logout.jsp>Logout</a>
<h1>Quan ly thong tin ve lop</h1>

<form id="class-info" action="#"> 
<table border = 0>
<tr><td>Ten lop:</td><td>
<input type="text" name="class_name" id="class_name" style="width:200px"></td></tr>
<tr><td></td><td><input type="button" name="add_class" value="Them moi" style="width:100px" onclick="addClass()"></td></tr>
</table></form>
<p id="test"></p>
<p id="class-table"></p>

<a href="home.jsp">Back to Home</a>

<script type="text/javascript">

var class_id = new Array();
var class_name = new Array();


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


function loadClassesList(){
	
	if(xmlhttp){
		   var xmlStr = "<?xml version=" + "\"" + "1.0" + "\"" + " encoding=" + "\"" + "UTF-8" + "\"" + "?>";
		   xmlStr += "\n" + "<LoadClassesList>" + "\n" +  "</LoadClassesList>";
		  
		    xmlhttp.open("POST","TeacherManager",true); //getname will be the servlet name
		    
		    xmlhttp.onreadystatechange  = handleLoadClassesList;
		   
		    xmlhttp.overrideMimeType('text/xml');
		    xmlhttp.setRequestHeader('Content-Type', 'text/xml; charset=utf-8');
		    xmlhttp.send(xmlStr); //Posting to Servlet
		    
	}
}

function handleLoadClassesList(){
	//alert("aaaaaa");
	if(xmlhttp.readyState == 4){
		if(xmlhttp.status == 200){
			var xmlData = xmlhttp.responseXML;
			//alert(xmlhttp.responseText + "\n aaa \n" + xmlData);
			//alert(xmlhttp.getAllResponseHeaders());
			//alert(xmlhttp.responseXML.parseError.errorCode);
			var T = xmlData.getElementsByTagName("stclass");
			
			
			var tablecontents = "";
			tablecontents += "<table class=" + '"' + "table table-bordered table-condensed" + '"' + "id=" + '"' + "userTable" + '"' + ">";
			tablecontents += "<thread>";
			tablecontents += "<th>ID</th>";
			tablecontents += "<th>Ten lop</th>";
			tablecontents += "</thread>";
			tablecontents += "<tbody>";
			for(var i = 0; i < T.length; i++){
				tablecontents += "<tr id='tr" + T[i].getElementsByTagName("id")[0].firstChild.nodeValue + "'>";
				tablecontents += "<td>" + T[i].getElementsByTagName("id")[0].firstChild.nodeValue + "</td>" + "<td>" + decodeURIComponent(T[i].getElementsByTagName("name")[0].firstChild.nodeValue) + "</td>";
				tablecontents += "<td><a href=" + '"' + "EditClass.jsp?name=" + decodeURIComponent(T[i].getElementsByTagName("name")[0].firstChild.nodeValue) + 
						"&id=" + T[i].getElementsByTagName("id")[0].firstChild.nodeValue + "\">Sua</td>";
				tablecontents += "<td><a href=" + '"' + "DeleteClass.jsp?id=" + T[i].getElementsByTagName("id")[0].firstChild.nodeValue + "\">Xoa</td>";
				tablecontents += "</tr>";
			}
			tablecontents += "</tbody>";
			tablecontents += "</table>";
			document.getElementById("class-table").innerHTML = tablecontents;
		}
	}
	
}



function addClass(){
document.getElementById("test").innerHTML = document.getElementById("class_name").value;
	
	if(xmlhttp!= false){
		   var name = document.getElementById("class_name").value;
		   
		   //name = Utf8Coder.encode(name);
		   var xmlStr = "<?xml version=" + "\"" + "1.0" + "\"" + " encoding=" + "\"" + "UTF-8" + "\"" + "?>";
		   xmlStr += "\n" + "<AddClass>" + "\n";
		  xmlStr += "<name>" + encodeURIComponent(name) + "</name>";
		 
		   xmlStr += "</AddClass>";
		   xmlhttp.open("POST","TeacherManager",true); //getname will be the servlet name
		    xmlhttp.onreadystatechange  = handleAddClass;
		    //xmlhttp.onreadystatechange  = handleSubmitPoint;
		    xmlhttp.overrideMimeType('text/xml');
		    xmlhttp.setRequestHeader('Content-Type', 'text/xml');
		    xmlhttp.send(xmlStr); //Posting to Servlet

		    //xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
		    //xmlhttp.send("xml="+xml); //Posting to Servlet
	}
	
}
function handleAddClass(){
	loadClassesList();
}
</script>

</body>
</html>