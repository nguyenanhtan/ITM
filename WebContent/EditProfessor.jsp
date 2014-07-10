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
<title>Cap nhat thong tin giang vien</title>
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
<form id="edit_teacher" action="#" >
<table border=0>
<tr><td>ID:</td><td> <input type="text" name="professor_id" id="professor_id" value="<%=request.getParameter("id")%>"  readonly="readonly"/></td></tr>
<tr><td>Ho va ten:</td><td> <input type="text" name="professor_name" id="professor_name"/></td></tr>
<tr><td>Truong:</td><td> <select id = "professor_institute">
<option value="HUST">Giang vien trong truong</option>
<option value="Non HUST">Giang vien ngoai truong</option>
</select>
<input type="text" id="institute-name" style="width:500px">
</td></tr>
<tr><td>Bo mon:</td><td> <select name="professor_department" id = "professor_department"  class="select-box min-width-140">

<!-- <option value = "KHMT">KHMT</option>
<option value = "CNPM">CNPM</option>
<option value = "HTTT">HTTT</option>
<option value = "KTMT">KTMT</option>
<option value = "TTM">TTM</option> -->
</select></td></tr>
<tr><td>Hoc ham/hoc vi<select id="degree">
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

<tr><td></td><td><input type="button" value="Cap nhat" style = "width:100px" onclick = "updateProfessor()"/></td></tr>
</table>
</form>
<a href="TeacherManager.jsp">Back to Home</a>
<script type="text/javascript">
var depart_name = new Array();
var depart_id=new Array();

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

			var selectdepart = document.getElementById("professor_department");
			
			for(var idx in depart_id){
				selectdepart.options[selectdepart.options.length] = new Option(depart_name[idx],depart_id[idx]);
				
			}
			
			
		}
	}
	
	
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

function clearTable(tbl){
	while(tbl.rows.length >= 2){
		tbl.deleteRow(1);
	}
}

function loadTeacher(id){
	if(xmlhttp != false){
		   var xmlStr = "<?xml version=" + "\"" + "1.0" + "\"" + " encoding=" + "\"" + "UTF-8" + "\"" + "?>";
		   xmlStr += "\n" + "<LoadExistTeacher>" + "\n" ;
		   xmlStr += "<id>" + id + "</id>";
		   xmlStr += "</LoadExistTeacher>";
		    xmlhttp.open("POST","TeacherManager",true); //getname will be the servlet name
		    xmlhttp.onreadystatechange  = handleLoadExistTeacher;
		 
		    xmlhttp.overrideMimeType('text/xml');
		    xmlhttp.setRequestHeader('Content-Type', 'text/xml; charset=utf-8');
		    xmlhttp.send(xmlStr); //Posting to Servlet
		  
	}
}

function handleLoadExistTeacher(){
	if(xmlhttp.readyState == 4){
		if(xmlhttp.status == 200){
			var xmlData = xmlhttp.responseXML;
			var selectdepart = document.getElementById("professor_department");
			//alert(xmlData.getElementsByTagName("department"));
			var Depart = xmlData.getElementsByTagName("depart");
			//alert("leng:::"+Depart.length);
			for(var i = 0; i < Depart.length; i++){
				//alert("hggggg");
				//alert(Depart[i].getElementsByTagName("id_depart")[0].firstChild.nodeValue);
				var id = Depart[i].getElementsByTagName("id_depart")[0].firstChild.nodeValue;
				var name = Depart[i].getElementsByTagName("name_depart")[0].firstChild.nodeValue;
				
				depart_name.push(name);
				depart_id.push(id);
			}

			
			
			for(var idx in depart_id){
				selectdepart.options[selectdepart.options.length] = new Option(depart_name[idx],depart_name[idx]);
				
			}
			var T = xmlData.getElementsByTagName("teacher");
			//alert(T[0].getElementsByTagName("name")[0].firstChild.nodeValue);
			document.getElementById("professor_name").value =decodeURIComponent(T[0].getElementsByTagName("name")[0].firstChild.nodeValue);
			document.getElementById("professor_institute").value=T[0].getElementsByTagName("institute")[0].firstChild.nodeValue;
			document.getElementById("institute-name").value =decodeURIComponent(T[0].getElementsByTagName("institute-name")[0].firstChild.nodeValue);
			document.getElementById("degree").value =decodeURIComponent(T[0].getElementsByTagName("degree")[0].firstChild.nodeValue);
			document.getElementById("expert-level").value=T[0].getElementsByTagName("expert-level")[0].firstChild.nodeValue;
			selectdepart.value=T[0].getElementsByTagName("department")[0].firstChild.nodeValue;
			
			var tbl = document.getElementById("subject-category-match");
			clearTable(tbl);
			var scm = xmlData.getElementsByTagName("professor-subject-category-match");
			for(i = 0; i < scm.length; i++){
				var subject_category_id = scm[i].getElementsByTagName("subject-category-id")[0].firstChild.nodeValue;
				var subject_category_name = decodeURIComponent(scm[i].getElementsByTagName("subject-category-name")[0].firstChild.nodeValue);
				var match_score = scm[i].getElementsByTagName("match-score")[0].firstChild.nodeValue;
				addRowSubjectCategoryMatch(tbl,subject_category_id,subject_category_name,match_score);					
			}

		}
	}
}


function init(){
	//loadDataBase();
	loadTeacher("<%=request.getParameter("id")%>");
}

function updateProfessor(){
	//alert("update");
	//alert(encodeURIComponent(name));
	//alert("delete professor " + name);
	if(xmlhttp!=false){
		var name = encodeURIComponent(document.getElementById("professor_name").value);
		var instituteName = encodeURIComponent(document.getElementById("institute-name").value);
		//alert(name);
		var id = document.getElementById("professor_id").value;
		//alert(id);
		var institute = document.getElementById("professor_institute").value;
		//alert(institute);
		var department =document.getElementById("professor_department").value;
		var degree = document.getElementById("degree").value;
		var expertLevel = document.getElementById("expert-level").value;
		
		//alert(department);
		//alert("Add teacher");
	   //var name = document.getElementById("teacher_name").value;
	   //name = Utf8Coder.encode(name);
	   var xmlStr = "<?xml version=" + "\"" + "1.0" + "\"" + " encoding=" + "\"" + "UTF-8" + "\"" + "?>";
	   xmlStr += "\n" + "<UpdateTeacher>" + "\n";
	   xmlStr += "<id>" + id + "</id>";
	   xmlStr += "<name>" + name + "</name>";
	   xmlStr += "<institute-name>" + instituteName + "</institute-name>";
	   xmlStr += "<institute>" + institute + "</institute>";
	   xmlStr += "<department>" + department + "</department>";
	   xmlStr += "<expert-level>" + expertLevel + "</expert-level>";
	   xmlStr += "<degree>" + degree + "</degree>";
	   
	   var tblSubjectMatch = document.getElementById("subject-category-match");
	   for(i = 1; i < tblSubjectMatch.rows.length; i++){
		   var subject_category_id = document.getElementById("subject-category-id-" + i).value;
		   var subject_category_name = document.getElementById("subject-category-name-" + i).value;
		   var matchScore = document.getElementById("match-score-" + i).value;
		   xmlStr += "<professor-subject-category-match>";
		   xmlStr += "<professor-id>" + id + "</professor-id>";
		   xmlStr += "<subject-category-id>" + subject_category_id + "</subject-category-id>";
		   xmlStr += "<match-score>" + matchScore + "</match-score>";
		   xmlStr += "</professor-subject-category-match>";
	   }

	   xmlStr += "</UpdateTeacher>";
	  // alert(xmlStr);
	    xmlhttp.open("POST","TeacherManager",true); //getname will be the servlet name
	    //alert("POST OK");
	    xmlhttp.onreadystatechange  = handleUpdateProfessor;
	    //alert("handleDeleteTeacher OK");
	    //xmlhttp.onreadystatechange  = handleSubmitPoint;
	    xmlhttp.overrideMimeType('text/xml');
	    //alert("overrideMimeType OK");
	    xmlhttp.setRequestHeader('Content-Type', 'text/xml');
	    //alert("setRequestHeader OK");
	    xmlhttp.send(xmlStr); //Posting to Servlet

	   
	}
	
}
function handleUpdateProfessor(){
	//alert("Update teacher successfully");
	window.location.href = "TeacherManager.jsp";
}

</script>




</body>
</html>