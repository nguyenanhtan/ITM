<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Quan ly thong tin ve dot bao ve</title>
</head>
<body onload ="loadDefenseSessionsList()">
<%
if(session.getAttribute("username") == null){
	response.sendRedirect("Login.jsp");
}else{
	String uid = session.getAttribute("username").toString();
	out.println("Hello " + uid);
}
%>
<a href=Logout.jsp>Logout</a>
<h1>Quan ly thong tin ve dot bao ve</h1>

<form id="defenseSessions-info" action="#"> 
<table border = 0>
<tr><td>Ngay:</td><td>
<input type="text" name="defenseSession_description" id="defenseSession_description" style="width:200px"></td></tr>
<tr><td>Active:</td><td><input type="text" name="defenseSession_active" id="defenseSession_active" style="width:100px"/>
</td></tr>
<tr><td></td><td><input type="button" name="add_defenseSession" value="Them dot bao ve" style="width:200px" onclick="addDefenseSession()"></td></tr>
</table></form>
<p id="test"></p>
<p id="defenseSession-table"></p>

<a href="home.jsp">Back to Home</a>

<script type="text/javascript">
var defenseSession_id = new Array();
var defenseSession_description = new Array();
var defenseSession_active = new Array();


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


function loadDefenseSessionsList(){
	
	if(xmlhttp){
		   var xmlStr = "<?xml version=" + "\"" + "1.0" + "\"" + " encoding=" + "\"" + "UTF-8" + "\"" + "?>";
		   xmlStr += "\n" + "<LoadDefenseSessionsList>" + "\n" +  
		   "</LoadDefenseSessionsList>";
		  
		    xmlhttp.open("POST","TeacherManager",true); //getname will be the servlet name
		    xmlhttp.onreadystatechange  = handleLoadDefenseSessionsList;
		   
		    xmlhttp.overrideMimeType('text/xml');
		    xmlhttp.setRequestHeader('Content-Type', 'text/xml; charset=utf-8');
		    xmlhttp.send(xmlStr); //Posting to Servlet
		    
	}
}

function handleLoadDefenseSessionsList(){
	if(xmlhttp.readyState == 4){
		if(xmlhttp.status == 200){
			var xmlData = xmlhttp.responseXML;
			var T = xmlData.getElementsByTagName("defenseSession");
			
			var tablecontents = "";
			tablecontents += "<table class=" + '"' + "table table-bordered table-condensed" + '"' + "id=" + '"' + "userTable" + '"' + ">";
			tablecontents += "<thread>";
			tablecontents += "<th>ID</th>";
			tablecontents += "<th>Ngay</th>";
			tablecontents += "<th>Active</th>";
			tablecontents += "</thread>";
			tablecontents += "<tbody>";
			for(var i = 0; i < T.length; i++){
				tablecontents += "<tr id='tr" + T[i].getElementsByTagName("id")[0].firstChild.nodeValue + "'>";
				tablecontents += "<td>" + T[i].getElementsByTagName("id")[0].firstChild.nodeValue + "</td>" + "<td>" + decodeURIComponent(T[i].getElementsByTagName("description")[0].firstChild.nodeValue) + "</td>" + "<td>" + T[i].getElementsByTagName("active")[0].firstChild.nodeValue + "</td>";
				tablecontents += "<td><a href=" + '"' + "EditDefenseSession.jsp?description=" + decodeURIComponent(T[i].getElementsByTagName("description")[0].firstChild.nodeValue) + 
						"&id=" + T[i].getElementsByTagName("id")[0].firstChild.nodeValue + "&active=" + T[i].getElementsByTagName("active")[0].firstChild.nodeValue+ "\">Sua</td>";
				tablecontents += "<td><a href=" + '"' + "DeleteDefenseSession.jsp?id=" + T[i].getElementsByTagName("id")[0].firstChild.nodeValue + "\">Xoa</td>";
				tablecontents += "</tr>";
			}
			tablecontents += "</tbody>";
			tablecontents += "</table>";
			document.getElementById("defenseSession-table").innerHTML = tablecontents;
		}
	}
	
}

function addDefenseSession(){
	document.getElementById("test").innerHTML = document.getElementById("defenseSession_description").value;
		
		if(xmlhttp!= false){
			   var description = document.getElementById("defenseSession_description").value;
			   var active = document.getElementById("defenseSession_active").value;
			   
			   //name = Utf8Coder.encode(name);
			   var xmlStr = "<?xml version=" + "\"" + "1.0" + "\"" + " encoding=" + "\"" + "UTF-8" + "\"" + "?>";
			   xmlStr += "\n" + "<AddDefenseSession>" + "\n";
			  xmlStr += "<description>" + encodeURIComponent(description) + "</description>";
			  xmlStr += "<active>" + active + "</active>";
			 
			   xmlStr += "</AddDefenseSession>";
			   xmlhttp.open("POST","TeacherManager",true); //getname will be the servlet name
			    xmlhttp.onreadystatechange  = handleAddDefenseSession;
			    //xmlhttp.onreadystatechange  = handleSubmitPoint;
			    xmlhttp.overrideMimeType('text/xml');
			    xmlhttp.setRequestHeader('Content-Type', 'text/xml');
			    xmlhttp.send(xmlStr); //Posting to Servlet

			    //xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
			    //xmlhttp.send("xml="+xml); //Posting to Servlet
		}
		
	}
	function handleAddDefenseSession(){
		loadDefenseSessionList();
	}

</script>

</body>
</html>