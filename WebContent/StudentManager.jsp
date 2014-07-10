<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>Quan ly hoc vien</title>
<link type="text/css" rel="stylesheet" href="./css/style.css"></link>
<link type="text/css" rel="stylesheet" href="./css/home.css"></link>
</head>
<body onload="loadClassesStatus()">
<div id="big-bound">
	<div id="inbound">
		<div id="header">			
			<h1 id="head-title">He thong quan ly dao tao cao hoc</h1>			
		</div>
		<div id="prf-user">
		<div id="goto-home"><a href=home.jsp><img src="./img/home-icon.png" title="Back to home"></a></div>
		<div id="logout"><a href=Logout.jsp><img src="./img/inside-logout-icon.png" title="logout"></a></div>
		<div id="hello-user">
			<%
				if(session.getAttribute("username") == null){
					response.sendRedirect("Login.jsp");
				}else{
					String uid = session.getAttribute("username").toString();
					out.println("Hello " + uid);
				}
			%>
		</div>
		
		</div>
		<div class="clear"></div>

<form id="teacher-info" action="#">
<table border=0>
<tr><td>
Ho va ten: </td><td><input type="text" name="student_name" id="student_name" style="width:200px"/>
</td></tr>
<tr><td>
Ma hoc vien:</td><td><input type="text" name="student_id" id="student_id" style="width:200px"/>
</td></tr>
<tr><td>
Email:</td><td><input type="text" name="email" id="email" style="width:400px"/>
</td></tr>
<tr><td>
Dien thoai:</td><td><input type="text" name="phone" id="phone" style="width:400px"/>
</td></tr>
<tr><td>
Khoa:</td><td><select name="promotion" id="select-promotion" class="select-box min-width-140">
<option value="2003">2003</option>
<option value="2004">2003</option>
<option value="2005">2005</option>
<option value="2006">2006</option>
<option value="2007">2007</option>
<option value="2008">2008</option>
<option value="2009">2009</option>
<option value="2010">2010</option>
<option value="2011">2011</option>
<option value="2012">2012</option>
<option value="2013">2013</option>
<option value="2014">2014</option>
<option value="2015">2015</option>
<option value="2016">2016</option>
<option value="2017">2017</option>
<option value="2018">2018</option>
<option value="2019">2019</option>
<option value="2020">2020</option>
<option value="2021">2021</option>
<option value="2022">2022</option>
<option value="2023">2023</option>
<option value="2024">2024</option>
<option value="2025">2025</option>
<option value="2026">2026</option>
<option value="2027">2027</option>
<option value="2028">2028</option>
<option value="2029">2029</option>
<option value="2030">2030</option>
</select>
</td></tr>
<tr><td>
Lop:</td><td><select name="class" id="select-class" class="select-box min-width-140">

	</select></td>
</tr>
<tr><td></td><td>
<input type="button" name="add_student" value="Them" style="width:100px" onclick="addStudent()"/></td> </tr>
<tr>
<td>
<input type="button" name="load_student" value="Danh sach" style="width:100px" onclick="loadFilteredStudents()">
</td>
<td>
Loc theo lop <select name="select-filter" id="select-filter">
</select>
Loc theo tinh trang<select name="select-status" id="select-status"></select>
</td>

</tr>
</table>



</form>

<p id="test"></p>
<p id="student-table"></p>

<script type="text/javascript">
var professor_id = new Array();
var professor_name = new Array();
var class_id = new Array();
var class_name = new Array();
var status_id = new Array();
var status_name = new Array();

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

function loadClassesStatus(){
	if(xmlhttp){
		   var xmlStr = "<?xml version=" + "\"" + "1.0" + "\"" + " encoding=" + "\"" + "UTF-8" + "\"" + "?>";
		   xmlStr += "\n" + "<LoadClassesStatus>" + "\n" +  
		   "</LoadClassesStatus>";
		    //alert(xmlStr);
		    xmlhttp.open("POST","TeacherManager",true); //getname will be the servlet name
		    xmlhttp.onreadystatechange  = handleLoadClassesStatus;
		    //xmlhttp.onreadystatechange  = handleSubmitPoint;
		    xmlhttp.overrideMimeType('text/xml');
		    xmlhttp.setRequestHeader('Content-Type', 'text/xml; charset=utf-8');
		    xmlhttp.send(xmlStr); //Posting to Servlet
		    //xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
		    //xmlhttp.send("xml="+xml); //Posting to Servlet
	}	
}
function handleLoadClassesStatus(){
	if(xmlhttp.readyState == 4){
		if(xmlhttp.status == 200){
			var xmlData = xmlhttp.responseXML;
			
			var Cl = xmlData.getElementsByTagName("class");
			//alert(Cl.length);
			for(i = 0;i < Cl.length; i++){
				var classID = Cl[i].getElementsByTagName("id")[0].firstChild.nodeValue;
				var className = Cl[i].getElementsByTagName("name")[0].firstChild.nodeValue;
				class_id.push(classID);
				class_name.push(className);
				//alert(class_id + "\t" + class_name);
			}
			var SS = xmlData.getElementsByTagName("StudentStatus");
			//alert(Cl.length);
			status_id = new Array();
			status_name = new Array();
			for(i = 0;i < SS.length; i++){
				var statusID = SS[i].getElementsByTagName("id")[0].firstChild.nodeValue;
				var statusName = SS[i].getElementsByTagName("description")[0].firstChild.nodeValue;
				status_id.push(statusID);
				status_name.push(statusName);
				//alert(class_id + "\t" + class_name);
			}
			
			var select_class = document.getElementById("select-class");
			var select_filter = document.getElementById("select-filter");
			var select_status = document.getElementById("select-status");
			
			clearSelect(select_class);
			clearSelect(select_filter);
			clearSelect(select_status);
			
			//var L = select_class.options.length;
			//for(i = L-1; i >= 0; i--){
				//select_class.options.remove(i);
			//}
			
			select_filter.options[select_filter.options.length] = new Option("All",0);
			for(idx in class_id){
				var opt = new Option(class_name[idx],class_id[idx]);
				select_class.options[select_class.options.length] = opt;
				
				var opt1 = new Option(class_name[idx],class_id[idx]);
				select_filter.options[select_filter.options.length] = opt1;
			}
			
			select_status.options[select_status.options.length] = new Option("All",0);
			for(idx in status_id){
				var opt = new Option(status_name[idx],status_id[idx]);
				select_status.options[select_status.options.length] = opt;
			}
			select_status.value = 1;
			
		}
	}
}
function loadStudentsList(){
	//alert("loadTeachersList");
	if(xmlhttp){
		   var xmlStr = "<?xml version=" + "\"" + "1.0" + "\"" + " encoding=" + "\"" + "UTF-8" + "\"" + "?>";
		   xmlStr += "\n" + "<LoadStudentsList>" + "\n" +  
		   "</LoadStudentsList>";
		    //alert(xmlStr);
		    xmlhttp.open("POST","TeacherManager",true); //getname will be the servlet name
		    xmlhttp.onreadystatechange  = handleLoadStudentsList;
		    //xmlhttp.onreadystatechange  = handleSubmitPoint;
		    xmlhttp.overrideMimeType('text/xml');
		    xmlhttp.setRequestHeader('Content-Type', 'text/xml; charset=utf-8');
		    xmlhttp.send(xmlStr); //Posting to Servlet
		    //xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
		    //xmlhttp.send("xml="+xml); //Posting to Servlet
	}
}

function loadFilteredStudents(){
	//alert("loadTeachersList");
	if(xmlhttp){
		   var xmlStr = "<?xml version=" + "\"" + "1.0" + "\"" + " encoding=" + "\"" + "UTF-8" + "\"" + "?>";
		   xmlStr += "\n" + "<LoadFilteredStudentsList>" + "\n" +  
		   "<filteredByClass>" + document.getElementById("select-filter").value + "</filteredByClass>" +
		   "<filteredByStatus>" + document.getElementById("select-status").value + "</filteredByStatus>" +
		   "</LoadFilteredStudentsList>";
		    //alert(xmlStr);
		    xmlhttp.open("POST","TeacherManager",true); //getname will be the servlet name
		    xmlhttp.onreadystatechange  = handleLoadStudentsList;
		    //xmlhttp.onreadystatechange  = handleSubmitPoint;
		    xmlhttp.overrideMimeType('text/xml');
		    xmlhttp.setRequestHeader('Content-Type', 'text/xml; charset=utf-8');
		    xmlhttp.send(xmlStr); //Posting to Servlet
		    //xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
		    //xmlhttp.send("xml="+xml); //Posting to Servlet
	}
}
function clearSelect(select){
	for(i = select.options.length-1; i >= 0; i--){
		select.remove(i);
	}
}
function handleLoadStudentsList(){
	if(xmlhttp.readyState == 4){
		if(xmlhttp.status == 200){
			var xmlData = xmlhttp.responseXML;
			
			var Cl = xmlData.getElementsByTagName("class");
			//alert(Cl.length);
			class_id = new Array();
			class_name = new Array();
			for(i = 0;i < Cl.length; i++){
				var classID = Cl[i].getElementsByTagName("classID")[0].firstChild.nodeValue;
				var className = Cl[i].getElementsByTagName("className")[0].firstChild.nodeValue;
				class_id.push(classID);
				class_name.push(className);
				//alert(class_id + "\t" + class_name);
			}
			
			var SS = xmlData.getElementsByTagName("StudentStatus");
			//alert(Cl.length);
			status_id = new Array();
			status_name = new Array();
			for(i = 0;i < SS.length; i++){
				var statusID = SS[i].getElementsByTagName("id")[0].firstChild.nodeValue;
				var statusName = SS[i].getElementsByTagName("description")[0].firstChild.nodeValue;
				status_id.push(statusID);
				status_name.push(statusName);
				//alert(class_id + "\t" + class_name);
			}
			var filteredClassID = xmlData.getElementsByTagName("filteredByClass")[0].firstChild.nodeValue;
			var filteredStatusID = xmlData.getElementsByTagName("filteredByStatus")[0].firstChild.nodeValue;
			
			var select_class = document.getElementById("select-class");
			var select_filter = document.getElementById("select-filter");
			var select_status = document.getElementById("select-status");
			
			clearSelect(select_class);
			clearSelect(select_filter);
			clearSelect(select_status);
			
			select_filter.options[select_filter.options.length] = new Option("All",0);
			for(idx in class_id){
				var opt = new Option(class_name[idx],class_id[idx]);
				select_class.options[select_class.options.length] = opt;
				var opt1 = new Option(class_name[idx],class_id[idx]);
				select_filter.options[select_filter.options.length] = opt1;
			}
			select_filter.value = filteredClassID;
			//alert(status_id.length)
			select_status.options[select_status.options.length] = new Option("All",0);
			for(idx in status_id){
				var opt = new Option(status_name[idx],status_id[idx]);
				select_status.options[select_status.options.length] = opt;
			}
			select_status.value = filteredStatusID;

			var T = xmlData.getElementsByTagName("student");
			
			var tablecontents = "";
			//tablecontents += "<table class=" + '"' + "table table-bordered table-condensed" + '"' + "id=" + '"' + "userTable" + '"' + ">";
			tablecontents += "<table border=1>";
			tablecontents += "<thread>";
			tablecontents += "<th>ID</th>";	
			tablecontents += "<th>StudentID</th>";
			tablecontents += "<th>Student Name</th>";
			tablecontents += "<th>Prommotion</th>";
			tablecontents += "<th>Class</th>";
			tablecontents += "<th>Email</th>";
			tablecontents += "<th>Phone</th>";
			//tablecontents += "<th>Subject</th>";
			//tablecontents += "<th>Start Date</th>";
			//tablecontents += "<th>End Date</th>";
			//tablecontents += "<th>Type</th>";
			tablecontents += "<th>Status</th>";		
			tablecontents += "</thread>";
			tablecontents += "<tbody>";
			for( var i = 0; i < T.length; i++){
				tablecontents += "<tr id='tr" + T[i].getElementsByTagName("id")[0].firstChild.nodeValue + "'>";
				tablecontents += "<td>" + T[i].getElementsByTagName("id")[0].firstChild.nodeValue + "</td>" + "<td>" + T[i].getElementsByTagName("studentID")[0].firstChild.nodeValue + "</td>";
				tablecontents += "<td>" + decodeURIComponent(T[i].getElementsByTagName("name")[0].firstChild.nodeValue) + "</td>" + "<td>" + T[i].getElementsByTagName("promotion")[0].firstChild.nodeValue + "</td>";
				tablecontents += "<td>" + T[i].getElementsByTagName("class_student")[0].firstChild.nodeValue + "</td>" + "<td>" + T[i].getElementsByTagName("email")[0].firstChild.nodeValue + "</td>";
				//tablecontents += "<td>" + T[i].getElementsByTagName("classID")[0].firstChild.nodeValue + "</td>" + "<td>" + T[i].getElementsByTagName("email")[0].firstChild.nodeValue + "</td>";
				tablecontents += "<td>" + T[i].getElementsByTagName("phone")[0].firstChild.nodeValue + "</td>";  
				//tablecontents += "<td>" + decodeURIComponent(T[i].getElementsByTagName("subject")[0].firstChild.nodeValue) + "</td>";
				//tablecontents += "<td>" + T[i].getElementsByTagName("startDate")[0].firstChild.nodeValue + "</td>" + "<td>" + T[i].getElementsByTagName("endDate")[0].firstChild.nodeValue + "</td>";
				//tablecontents += "<td>" + T[i].getElementsByTagName("type")[0].firstChild.nodeValue + "</td>";
				tablecontents += "<td>" + T[i].getElementsByTagName("status")[0].firstChild.nodeValue + "</td>";
				
			
				tablecontents += "<td><a href=" + '"' + "EditStudent.jsp?name=" + 
						decodeURIComponent(T[i].getElementsByTagName("name")[0].firstChild.nodeValue) + 
						"&id=" + T[i].getElementsByTagName("id")[0].firstChild.nodeValue + 
						"&studentID=" + T[i].getElementsByTagName("studentID")[0].firstChild.nodeValue + 
						"&promotion=" + T[i].getElementsByTagName("promotion")[0].firstChild.nodeValue + 
						"&class_student=" + T[i].getElementsByTagName("class_student")[0].firstChild.nodeValue + 
						"&classID=" + T[i].getElementsByTagName("classID")[0].firstChild.nodeValue + 
						"&email=" + T[i].getElementsByTagName("email")[0].firstChild.nodeValue + 
						"&phone=" + T[i].getElementsByTagName("phone")[0].firstChild.nodeValue + 
						//"&subject=" + decodeURIComponent(T[i].getElementsByTagName("subject")[0].firstChild.nodeValue) + 
						//"&startDate=" + T[i].getElementsByTagName("startDate")[0].firstChild.nodeValue + 
						//"&endDate=" + T[i].getElementsByTagName("endDate")[0].firstChild.nodeValue + 
						//"&type=" + T[i].getElementsByTagName("type")[0].firstChild.nodeValue + 
						"&status=" + T[i].getElementsByTagName("status")[0].firstChild.nodeValue + 
															
						
						"\">Sua</td>";
				//tablecontents += "<td><input type=" + '"' + "button" + '"' + " value=" + '"' + "Delete" + '"' + " onclick=" + '"' + "deleteProfessor()" + '"' + "></td>";
				tablecontents += "<td><a href=" + '"' + "DeleteStudent.jsp?id=" + T[i].getElementsByTagName("id")[0].firstChild.nodeValue + "\">Xoa</td>";
				tablecontents += "</tr>";
			}
			tablecontents += "</tbody>";
			tablecontents += "</table>";
			document.getElementById("student-table").innerHTML = tablecontents;
		}
	}
	
}
	
function addStudent(){
	//document.getElementById("test").innerHTML = document.getElementById("teacher_name").value;
	
	if(xmlhttp){
			//alert("Add teacher");
		   var name = encodeURIComponent(document.getElementById("student_name").value);
		   var promotion = document.getElementById("select-promotion").value;
		   var className =  document.getElementById("select-class").value;
		   //var thesis_title =  document.getElementById("thesis_title").value;
		   var email =  document.getElementById("email").value;
		   var phone =  document.getElementById("phone").value;
		   var student_id =  document.getElementById("student_id").value;
		   if(student_id.length == 0){
			   student_id='-';
			   
		   }
		   if(email.length == 0){
			   email = '-';
		   }
		   if(phone.length == 0){
			   phone = '-';
		   }
		   //alert('student_id = ' + student_id);
		   //var type =  document.getElementById("type").value;
		   //var status  document.getElementById("status").value;
		   //var startDate  document.getElementById("startDate").value;
		   //var endDate  document.getElementById("endDate").value;
		   
		   var xmlStr = "<?xml version=" + "\"" + "1.0" + "\"" + " encoding=" + "\"" + "UTF-8" + "\"" + "?>";
		   xmlStr += "\n" + "<AddStudent>" + "\n";
		   xmlStr += "<name>" + encodeURIComponent(name) + "</name>";
		   
		   xmlStr += "<studentID>" + student_id+  "</studentID>";
		   xmlStr += "<promotion>" + promotion+  "</promotion>";
		   xmlStr += "<class_student>" +className +  "</class_student>";
		   xmlStr += "<email>" +email +  "</email>";
		   xmlStr += "<phone>" + phone+  "</phone>";
		   //xmlStr += "<subject>" +encodeURIComponent(thesis_title) +  "</subject>";
		   //xmlStr += "<startDate>" + +  "</startDate>";
		   //xmlStr += "<endDate>" + +  "</endDate>";
		   //xmlStr += "<type>" + +  "</type>";
		  // xmlStr += "<status>" + +  "</status>";
		  	   
							   
		  
		   xmlStr += "</AddStudent>";
		  // alert(xmlStr);
		    xmlhttp.open("POST","TeacherManager",true); //getname will be the servlet name
		    xmlhttp.onreadystatechange  = handleAddStudent;
		    //xmlhttp.onreadystatechange  = handleSubmitPoint;
		    xmlhttp.overrideMimeType('text/xml');
		    xmlhttp.setRequestHeader('Content-Type', 'text/xml');
		    xmlhttp.send(xmlStr); //Posting to Servlet

		    //xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
		    //xmlhttp.send("xml="+xml); //Posting to Servlet
	}
	
}	

function handleAddStudent(){
	//alert("handleAddStudent");
	//loadStudentsList();
	loadFilteredStudents();
	document.getElementById("student_name").value = "";
	document.getElementById("email").value = "";
	document.getElementById("phone").value = "";
}

</script>
</div>
</div>
</body>
</html>