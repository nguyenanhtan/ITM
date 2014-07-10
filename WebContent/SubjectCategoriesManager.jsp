<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Quan ly thong tin ve nhom de tai</title>
</head>
<body onload ="loadSubjectCategoriesList()">
<%
if(session.getAttribute("username") == null){
	response.sendRedirect("Login.jsp");
}else{
	String uid = session.getAttribute("username").toString();
	out.println("Hello " + uid);
}
%>
<a href=Logout.jsp>Logout</a>
<h1>Quan ly thong tin ve nhom de tai</h1>

<form id="subject-category-info" action="#"> 
<table border = 0>
<tr><td>Ten nhom de tai:</td><td>
<input type="text" name="subject-category-name" id="subject-category-name" style="width:200px"></td></tr>
<tr><td></td><td><input type="button" value="Them" style="width:200px" onclick="addSubjectCategory()"></td></tr>
</table></form>
<p id="test"></p>
<p id="subject-categories-table"></p>

<a href="home.jsp">Back to Home</a>

<script type="text/javascript">
var category_id = new Array();
var category_description = new Array();


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


function loadSubjectCategoriesList(){
	
	if(xmlhttp){
		   var xmlStr = "<?xml version=" + "\"" + "1.0" + "\"" + " encoding=" + "\"" + "UTF-8" + "\"" + "?>";
		   xmlStr += "\n" + "<LoadSubjectCategoriesList>" + "\n" +  
		   "</LoadSubjectCategoriesList>";
		  
		    xmlhttp.open("POST","TeacherManager",true); //getname will be the servlet name
		    xmlhttp.onreadystatechange  = handleLoadSubjectCategoriesList;
		   
		    xmlhttp.overrideMimeType('text/xml');
		    xmlhttp.setRequestHeader('Content-Type', 'text/xml; charset=utf-8');
		    xmlhttp.send(xmlStr); //Posting to Servlet
		    
	}
}

function handleLoadSubjectCategoriesList(){
	if(xmlhttp.readyState == 4){
		if(xmlhttp.status == 200){
			var xmlData = xmlhttp.responseXML;
			var T = xmlData.getElementsByTagName("subject-category");
			
			var tablecontents = "";
			tablecontents += "<table class=" + '"' + "table table-bordered table-condensed" + '"' + "id=" + '"' + "userTable" + '"' + ">";
			tablecontents += "<thread>";
			tablecontents += "<th>ID</th>";
			tablecontents += "<th>Ten</th>";
			tablecontents += "</thread>";
			tablecontents += "<tbody>";
			for(var i = 0; i < T.length; i++){
				tablecontents += "<tr id='tr" + T[i].getElementsByTagName("id")[0].firstChild.nodeValue + "'>";
				tablecontents += "<td>" + T[i].getElementsByTagName("id")[0].firstChild.nodeValue + "</td>" + "<td>" + decodeURIComponent(T[i].getElementsByTagName("name")[0].firstChild.nodeValue) + "</td>";
				tablecontents += "<td><a href=" + '"' + "EditSubjectCategory.jsp?name=" + decodeURIComponent(T[i].getElementsByTagName("name")[0].firstChild.nodeValue) + 
						"&id=" + T[i].getElementsByTagName("id")[0].firstChild.nodeValue + "\">Sua</td>";
				tablecontents += "<td><a href=" + '"' + "DeleteSubjectCategory.jsp?id=" + T[i].getElementsByTagName("id")[0].firstChild.nodeValue + "\">Xoa</td>";
				tablecontents += "</tr>";
			}
			tablecontents += "</tbody>";
			tablecontents += "</table>";
			document.getElementById("subject-categories-table").innerHTML = tablecontents;
		}
	}
	
}

function addSubjectCategory(){
	//document.getElementById("test").innerHTML = document.getElementById("defenseSession_description").value;
		
		if(xmlhttp!= false){
			   var name = document.getElementById("subject-category-name").value;
			   
			   var xmlStr = "<?xml version=" + "\"" + "1.0" + "\"" + " encoding=" + "\"" + "UTF-8" + "\"" + "?>";
			   xmlStr += "\n" + "<AddSubjectCategory>" + "\n";
			  xmlStr += "<name>" + encodeURIComponent(name) + "</name>";
			 
			   xmlStr += "</AddSubjectCategory>";
			   xmlhttp.open("POST","TeacherManager",true); //getname will be the servlet name
			    xmlhttp.onreadystatechange  = handleAddSubjectCategory;
			    //xmlhttp.onreadystatechange  = handleSubmitPoint;
			    xmlhttp.overrideMimeType('text/xml');
			    xmlhttp.setRequestHeader('Content-Type', 'text/xml');
			    xmlhttp.send(xmlStr); //Posting to Servlet

			    //xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
			    //xmlhttp.send("xml="+xml); //Posting to Servlet
		}
		
	}
	function handleAddSubjectCategory(){
		loadSubjectCategoriesList();
	}

</script>

</body>
</html>