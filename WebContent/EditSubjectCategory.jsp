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
<title>Cap nhat thong tin ve nhom de tai</title>
</head>
<body onload="init()">
<%
if(session.getAttribute("username") == null){
	response.sendRedirect("Login.jsp");
}else{
	String uid = session.getAttribute("username").toString();
	out.println("Hello " + uid);
}
%>
<a href=Logout.jsp>Logout</a>
<form id="edit_subject_category" action="#" >
<table border=0>
<tr><td>ID:</td><td> <input type="text" name="id" id="id" value="<%=request.getParameter("id")%>"  readonly="readonly"/></td></tr>
<tr><td>Ten nhom de tai:</td><td> <input type="text" name="name" id="name" value="<%=request.getParameter("name")%>"/></td></tr>


<tr><td></td><td><input type="button" value="Cap nhat" style = "width:100px" onclick = "updateSubjectCategory()"/></td></tr>
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



function updateSubjectCategory(){
	//alert("update");
	//alert(encodeURIComponent(name));
	//alert("delete professor " + name);
	if(xmlhttp!=false){
		var name = encodeURIComponent(document.getElementById("name").value);
		
		var id = document.getElementById("id").value;
		
		
		//alert("Add teacher");
	   //var name = document.getElementById("teacher_name").value;
	   //name = Utf8Coder.encode(name);
	   var xmlStr = "<?xml version=" + "\"" + "1.0" + "\"" + " encoding=" + "\"" + "UTF-8" + "\"" + "?>";
	   xmlStr += "\n" + "<UpdateSubjectCategory>" + "\n";
	   xmlStr += "<id>" + id + "</id>";
	   xmlStr += "<name>" + name + "</name>";
	  	
	   xmlStr += "</UpdateSubjectCategory>";
	 
	    xmlhttp.open("POST","TeacherManager",true); //getname will be the servlet name
	    //alert("POST OK");
	    xmlhttp.onreadystatechange  = handleUpdateClass;
	    //alert("handleDeleteTeacher OK");
	    //xmlhttp.onreadystatechange  = handleSubmitPoint;
	    xmlhttp.overrideMimeType('text/xml');
	    //alert("overrideMimeType OK");
	    xmlhttp.setRequestHeader('Content-Type', 'text/xml');
	    //alert("setRequestHeader OK");
	    xmlhttp.send(xmlStr); //Posting to Servlet

	   
	}
	
}
function handleUpdateClass(){
	//alert("Update class successfully");
	window.location.href = "SubjectCategoriesManager.jsp";
}

function loadSubjectCategory(id){
	if(xmlhttp != false){
		   var xmlStr = "<?xml version=" + "\"" + "1.0" + "\"" + " encoding=" + "\"" + "UTF-8" + "\"" + "?>";
		   xmlStr += "\n" + "<LoadExistSubjectCategory>" + "\n" ;
		   xmlStr += "<id>" + id + "</id>";
		   xmlStr += "</LoadExistSubjectCategory>";
		    xmlhttp.open("POST","TeacherManager",true); //getname will be the servlet name
		    xmlhttp.onreadystatechange  = handleLoadExistSubjectCategory;
		 
		    xmlhttp.overrideMimeType('text/xml');
		    xmlhttp.setRequestHeader('Content-Type', 'text/xml; charset=utf-8');
		    xmlhttp.send(xmlStr); //Posting to Servlet
		  
	}
}

function handleLoadExistSubjectCategory(){
	if(xmlhttp.readyState == 4){
		if(xmlhttp.status == 200){
			var xmlData = xmlhttp.responseXML;

			var T = xmlData.getElementsByTagName("subject-category");
			//alert(T[0].getElementsByTagName("name")[0].firstChild.nodeValue);
			document.getElementById("name").value = decodeURIComponent(T[0].getElementsByTagName("name")[0].firstChild.nodeValue);
		}
	}
}


function init(){
	//loadDataBase();
	loadSubjectCategory("<%=request.getParameter("id")%>");
}

</script>




</body>
</html>