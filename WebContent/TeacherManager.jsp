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
<title>Quan ly thong tin ve giang vien</title>
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

<h1>Quan ly thong tin ve giang vien</h1>

<form id="teacher-info" action="#"> 
<table border = 0>
<tr><td>Ho va ten:</td><td>
<input type="text" name="teacher_name" id="teacher_name" style="width:200px" ></td></tr>
<tr><td>Truong:</td><td><select id="institute" style="width:200px">
<option value="HUST">Giang vien trong truong</option>
<option value="Non HUST">Giang vien ngoai truong</option>
</select>
<input type="text" id="institute-name" value="-" style="width:500px" >
</td></tr>
<tr><td>Bo mon:</td><td><select name="department" id="department" style="width:100px">
<option value = "1">KHMT</option>
<option value = "2">CNPM</option>
<option value = "3">HTTT</option>
<option value = "4">KTMT</option>
<option value = "5">TTM</option>
</select></td></tr>
<tr><td>Hoc ham/hoc vi</td><td><select id="degree">
<option value="TS.">TS.</option>
<option value="PGS.TS.">PGS.TS.</option>
<option value="GS.TS.">GS.TS.</option>
</select></td></tr>
<tr><td>Muc do chuyen gia</td><td><input type="text" id="expert-level"></td></tr>
<tr>
<table id="subject-category-match">
<tr>
<td></td><td>Ten nhom de tai</td><td>Muc do phu hop</td>
</tr>
</table>
</tr>

<tr><td></td><td><input type="button" name="add_teacher" value="Them moi" style="width:100px" onclick="addTeacher()"></td></tr>
<tr><td></td><td><input type="button" name="list_teacher" value="Danh sach GV" style="width:100px" onclick="loadTeachersList()"></td></tr>
</table></form>
<p id="test"></p>
<p id="teacher-table"></p>

<a href="home.jsp">Back to Home</a>

<script type="text/javascript">
var teachers = new Array();

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
var depart_name = new Array();
var depart_id = new Array();

function init(){
	loadDataBase();
	//loadTeachersList();
}
function loadDataBase(){
	//alert("loadTeachersStudentsList");
	//xmlhttp = new getXMLObject(); //xmlhttp holds the ajax object
	if(xmlhttp){
		   var xmlStr = "<?xml version=" + "\"" + "1.0" + "\"" + " encoding=" + "\"" + "UTF-8" + "\"" + "?>";
		   xmlStr += "\n" + "<LoadDepartmentsList>" + "\n" +  
		   "</LoadDepartmentsList>";
		    
		    xmlhttp.open("POST","TeacherManager",true); //getname will be the servlet name
		    xmlhttp.onreadystatechange  = handleLoadDataBase;
		    //xmlhttp.onreadystatechange  = handleSubmitPoint;
		    xmlhttp.overrideMimeType('text/xml');
		    xmlhttp.setRequestHeader('Content-Type', 'text/xml; charset=utf-8');
		    xmlhttp.send(xmlStr); //Posting to Servlet
		    
	}else{
		alert("xmlhttp NULL");
	}
}

function handleLoadDataBase(){
	//alert("handleLoadDataBase");
	if(xmlhttp.readyState == 4){
		if(xmlhttp.status == 200){
			var xmlData = xmlhttp.responseXML;
			//alert("handleLoadTeachersStudentsList: " + xmlData);
			var Depart = xmlData.getElementsByTagName("department");
			for(var i = 0; i < Depart.length; i++){
				var id = Depart[i].getElementsByTagName("id")[0].firstChild.nodeValue;
				var name = Depart[i].getElementsByTagName("name")[0].firstChild.nodeValue;
				
				depart_name.push(name);
				depart_id.push(id);
			}

			/*
			var selectdepart = document.getElementById("professor_department");
			
			for(var idx in depart_id){
				selectdepart.options[selectdepart.options.length] = new Option(depart_name[idx],depart_id[idx]);
				
			}
			*/
			
			var tblSubjectCategory = document.getElementById("subject-category-match");
			var categories = xmlData.getElementsByTagName("subject-category");
			//alert(categories.length);
			for(i = 0; i < categories.length; i++){
				var id = categories[i].getElementsByTagName("id")[0].firstChild.nodeValue;
				var name = decodeURIComponent(categories[i].getElementsByTagName("name")[0].firstChild.nodeValue);
				addRowSubjectCategoryMatch(tblSubjectCategory, id,name,10);
			}
			
		}
	}
	
	
}

function loadTeachersList(){
	if(xmlhttp != false){
		//alert("aaaaaaaa");
		   var xmlStr = "<?xml version=" + "\"" + "1.0" + "\"" + " encoding=" + "\"" + "UTF-8" + "\"" + "?>";
		   xmlStr += "\n" + "<LoadTeachersList>" + "\n" +  
		 	"</LoadTeachersList>";
		    xmlhttp.open("POST","TeacherManager",true); //getname will be the servlet name
		    xmlhttp.onreadystatechange  = handleLoadTeachersList;
		    //xmlhttp.onreadystatechange  = handleSubmitPoint;
		    xmlhttp.overrideMimeType('text/xml');
		    xmlhttp.setRequestHeader('Content-Type', 'text/xml; charset=utf-8');
		    xmlhttp.send(xmlStr); //Posting to Servlet
		    //xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
		    //xmlhttp.send("xml="+xml); //Posting to Servlet
	}
}

function handleLoadTeachersList(){
	if(xmlhttp.readyState == 4){
		if(xmlhttp.status == 200){
			var xmlData = xmlhttp.responseXML;
			var T = xmlData.getElementsByTagName("teacher");
			
			var tablecontents = "";
			//tablecontents += "<table class=" + '"' + "table table-bordered table-condensed" + '"' + "id=" + '"' + "userTable" + '"' + ">";
			tablecontents += "<table border=1>";
			tablecontents += "<thread>";
			tablecontents += "<th>ID</th>";
			tablecontents += "<th>Ho va ten</th>";
			tablecontents += "<th>Truong</th>";
			tablecontents += "<th>Bo mon</th>";
			tablecontents += "<th>Hoc ham/hoc vi</th>";
			tablecontents += "<th>Muc chuyen gia</th>";
			tablecontents += "</thread>";
			tablecontents += "<tbody>";
			for(var i = 1; i < T.length; i++){
				//var teach_name = decodeURIComponent(T[i].getElementsByTagName("name")[0].firstChild.nodeValue); 
				//teachers.push(teacher_name);
				
					
				//alert(decodeURIComponent(T[i].getElementsByTagName("name")[0].firstChild.nodeValue));
				
				tablecontents += "<tr id='tr" + T[i].getElementsByTagName("id")[0].firstChild.nodeValue + "'>";
				tablecontents += "<td>" + T[i].getElementsByTagName("id")[0].firstChild.nodeValue + "</td>" + "<td>" + decodeURIComponent(T[i].getElementsByTagName("name")[0].firstChild.nodeValue) + "</td>";
				//tablecontents += "<td>" + T[i].getElementsByTagName("institute")[0].firstChild.nodeValue + "</td>";
				tablecontents += "<td>" + decodeURIComponent(T[i].getElementsByTagName("institute-name")[0].firstChild.nodeValue) + "</td>";
				tablecontents += "<td>" + T[i].getElementsByTagName("department")[0].firstChild.nodeValue + "</td>";
				tablecontents += "<td>" + T[i].getElementsByTagName("degree")[0].firstChild.nodeValue + "</td>";
				tablecontents += "<td>" + T[i].getElementsByTagName("expert-level")[0].firstChild.nodeValue + "</td>";
				
				//tablecontents += "<td><a charset="+"\""+"UTF-8"+"\"" +" href=" + '"' + "EditProfessor.jsp?name=" + encodeURIComponent( decodeURIComponent(T[i].getElementsByTagName("name")[0].firstChild.nodeValue)) + "&id=" + T[i].getElementsByTagName("id")[0].firstChild.nodeValue + "&institute="+T[i].getElementsByTagName("institute")[0].firstChild.nodeValue+"&department="+T[i].getElementsByTagName("department")[0].firstChild.nodeValue+"\">Edit</td>";	
				tablecontents += "<td><a charset="+"\""+"UTF-8"+"\"" +" href=" + '"' + "EditProfessor.jsp?name=" + 
						decodeURIComponent(T[i].getElementsByTagName("name")[0].firstChild.nodeValue) + "&id=" + 
						T[i].getElementsByTagName("id")[0].firstChild.nodeValue + "&institute="+
						T[i].getElementsByTagName("institute")[0].firstChild.nodeValue+"&instituteName="+
						decodeURIComponent(T[i].getElementsByTagName("institute-name")[0].firstChild.nodeValue)+"&department="+
						T[i].getElementsByTagName("department")[0].firstChild.nodeValue+"\">Sua</td>";
				//tablecontents += "<td><input type=" + '"' + "button" + '"' + " value=" + '"' + "Delete" + '"' + " onclick=" + '"' + "deleteProfessor()" + '"' + "></td>";
				tablecontents += "<td><a href=" + '"' + "DeleteProfessor.jsp?id=" + T[i].getElementsByTagName("id")[0].firstChild.nodeValue + "\">Xoa</td>";
				tablecontents += "</tr>";
			}
			tablecontents += "</tbody>";
			tablecontents += "</table>";
			document.getElementById("teacher-table").innerHTML = tablecontents;
		}
	}
}
	
function addTeacher(){
	document.getElementById("test").innerHTML = document.getElementById("teacher_name").value;
	
	if(xmlhttp!= false){
		   var name = document.getElementById("teacher_name").value;
		   var institute = document.getElementById("institute").value;
		   var department = document.getElementById("department").value;
		   var university = document.getElementById("institute-name").value;
		   var degree = document.getElementById("degree").value;
		   var expert = document.getElementById("expert-level").value;
		   //name = Utf8Coder.encode(name);
		   var xmlStr = "<?xml version=" + "\"" + "1.0" + "\"" + " encoding=" + "\"" + "UTF-8" + "\"" + "?>";
		   xmlStr += "\n" + "<AddTeacher>" + "\n";
		  xmlStr += "<name>" + encodeURIComponent(name) + "</name>";
		  xmlStr += "<institute-name>" + encodeURIComponent(university) + "</institute-name>";
		  // xmlStr += "<name>" + name + "</name>";
		   xmlStr += "<institute>" + institute + "</institute>";
		   xmlStr += "<department>" + department + "</department>";
		   xmlStr += "<degree>" + degree + "</degree>";
		   xmlStr += "<expert-level>" + expert + "</expert-level>";
		   
		   var tblSubjectMatch = document.getElementById("subject-category-match");
		   for(i = 1; i < tblSubjectMatch.rows.length; i++){
			   var subject_category_id = document.getElementById("subject-category-id-" + i).value;
			   var subject_category_name = document.getElementById("subject-category-name-" + i).value;
			   var matchScore = document.getElementById("match-score-" + i).value;
			   xmlStr += "<professor-subject-category-match>";
			   //xmlStr += "<professor-id>" + id + "</professor-id>";
			   xmlStr += "<subject-category-id>" + subject_category_id + "</subject-category-id>";
			   xmlStr += "<match-score>" + matchScore + "</match-score>";
			   xmlStr += "</professor-subject-category-match>";
		   }
		   
		   xmlStr += "</AddTeacher>";
		   xmlhttp.open("POST","TeacherManager",true); //getname will be the servlet name
		    xmlhttp.onreadystatechange  = handleAddTeacher;
		    //xmlhttp.onreadystatechange  = handleSubmitPoint;
		    xmlhttp.overrideMimeType('text/xml');
		    xmlhttp.setRequestHeader('Content-Type', 'text/xml');
		    xmlhttp.send(xmlStr); //Posting to Servlet

		    //xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
		    //xmlhttp.send("xml="+xml); //Posting to Servlet
	}
	
}	

function handleAddTeacher(){
	loadTeachersList();
}

function deleteProfessor(){
	alert("delete professor");
}

function addRowSubjectCategoryMatch(tbl, id, name, matchscore){
	var sz = tbl.rows.length;
	var row = tbl.insertRow(sz);
	
	var cell = row.insertCell(0);
	var e = document.createElement("input");
	e.type = "text";
	e.setAttribute("name","subject-category-id-" + sz);
	e.setAttribute("id","subject-category-id-" + sz);
	e.setAttribute("value",id);
	e.setAttribute("style","visibility:hidden");
	e.style.width="50px";	
	cell.appendChild(e);

	cell = row.insertCell(1);
	e = document.createElement("input");
	e.type = "text";
	e.setAttribute("name","subject-category-name-" + sz);
	e.setAttribute("id","subject-category-name-" + sz);
	e.setAttribute("value",name);
	e.style.width="200px";	
	cell.appendChild(e);

	cell = row.insertCell(2);
	e = document.createElement("input");
	e.type = "text";
	e.setAttribute("name","match-score-" + sz);
	e.setAttribute("id","match-score-" + sz);
	e.setAttribute("value",matchscore);
	e.style.width="50px";	
	cell.appendChild(e);

}

</script>
</body>
</html>