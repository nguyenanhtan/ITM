<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Defense Management</title>
</head>
<body onload="loadDataBase()">
<%
if(session.getAttribute("username") == null){
	response.sendRedirect("Login.jsp");
}else{
	String uid = session.getAttribute("username").toString();
	out.println("Hello " + uid);
}
%>
<a href=Logout.jsp>Logout</a>
<h1>Defense Management</h1>

<form id="teacher-info" action="#">
<ul>
Name<select name="student" id="select-student" class="select-box min-width-140">
	</select>
Title<input type="text" name="thesis_title" id="thesis_title" style="width:400px">

<div>
<table id="jury-info">
<tr>
<td>
	Examiner 1<br>
	<select name="examiner1" id="select-examiner1" class="select-box min-width-140">
	</select>
</td>
<td>
	Examiner 2<br>
	<select name="examiner2" id="select-examiner2" class="select-box min-width-140">
	</select>
</td>

<td>
	President<br>
	<select name="president" id="select-president" class="select-box min-width-140">
	</select>
</td>

<td>
	Secretary<br>
	<select name="secretary" id="select-secretary" class="select-box min-width-140">
	</select>
</td>

<td>
	Member<br>
	<select name="member" id="select-member" class="select-box min-width-140">
	</select>
</td>

</tr>
</table>
</div>

<input type="button" name="add_jury" value="AddJury" style="width:100px" onclick="addJury()">
<input type="button" name="load_jury" value="LoadJury" style="width:100px" onclick="loadJuryList()">
Sorted by<select name="sortby" id="sortby">
	<option value="StudentID">StudentID</option>
	<option value="Slot">Slot</option>
	<option value="Room">Room</option>
	<option value="Room-Slot">Room-Slot</option>
	<option value="Slot-Room">Slot-Room</option>
</select>
Filter by<select name="filter-by" id="filter-by"></select>

<table id="list-jury">
<tr>
<td>
	ID
</td>
<td>
	Name
</td>
<td>
	Thesis title
</td>
<td>
	Examiner 1
</td>
<td>
	Examiner 2
</td>

<td>
	President
</td>

<td>
	Secretary
</td>

<td>
	Member
</td>

<td>
	Slot
</td>
<td>
	Room
</td>

</tr>

</table>
<input type="button" value="Check consistency" onclick="checkConsistency()">
<input type="button" value="Save" onclick="saveSchedule()">
<input type="button" value="Reset" onclick="resetSchedule()"><br>
<input type="button" value="Search Solution" onclick="searchSolution()">
Number of Slots <input type="text" name="slots" id="slots" style="width:50px" value="8">
Number of Rooms <input type="text" name="rooms" id="rooms" style="width:50px" value="8">
</ul>
</form>

<p id="test"></p>
<p id="jury-table"></p>

<a href="home.jsp">Back to Home</a>

<script type="text/javascript">
var professor_id = new Array();
var professor_name = new Array();
var student_id = new Array();
var student_name = new Array();
var departments_id = new Array();
var departments_name = new Array();

function getXMLObject()  //XML OBJECT
{
   var xmlHttp = false;
   try {
     xmlHttp = new ActiveXObject("Msxml2.XMLHTTP")  // For Old Microsoft Browsers
   }
   catch (e) {
     try {
       xmlHttp = new ActiveXObject("Microsoft.XMLHTTP")  // For Microsoft IE 6.0+
     }
     catch (e2) {
       xmlHttp = false   // No Browser accepts the XMLHTTP Object then false
     }
   }
   if (!xmlHttp && typeof XMLHttpRequest != 'undefined') {
     xmlHttp = new XMLHttpRequest();        //For Mozilla, Opera Browsers
   }
   return xmlHttp;  // Mandatory Statement returning the ajax object created
}
 
//alert("getXMLObject"); 
var xmlhttp = new getXMLObject(); //xmlhttp holds the ajax object
//alert("finished getXMLObject");

function loadDataBase(){
	//alert("loadTeachersStudentsList");
	//xmlhttp = new getXMLObject(); //xmlhttp holds the ajax object
	if(xmlhttp){
		   var xmlStr = "<?xml version=" + "\"" + "1.0" + "\"" + " encoding=" + "\"" + "UTF-8" + "\"" + "?>";
		   xmlStr += "\n" + "<LoadTeachersStudentsDepartmentsList>" + "\n" +  
		   "</LoadTeachersStudentsDepartmentsList>";
		    //alert(xmlStr);
		    //xmlhttp.open("POST","TeacherManager",true); //getname will be the servlet name
		    xmlhttp.open("POST","DefenseScheduleManager",true); //getname will be the servlet name
		    xmlhttp.onreadystatechange  = handleLoadDataBase;
		    //xmlhttp.onreadystatechange  = handleSubmitPoint;
		    xmlhttp.overrideMimeType('text/xml');
		    xmlhttp.setRequestHeader('Content-Type', 'text/xml; charset=utf-8');
		    xmlhttp.send(xmlStr); //Posting to Servlet
		    //xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
		    //xmlhttp.send("xml="+xml); //Posting to Servlet
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
			var ST = xmlData.getElementsByTagName("student");
			for(i = 0; i < ST.length; i++){
				var id = ST[i].getElementsByTagName("id")[0].firstChild.nodeValue;
				var name = ST[i].getElementsByTagName("name")[0].firstChild.nodeValue;
				
				student_name.push(name);
				student_id.push(id);
			}

			var T = xmlData.getElementsByTagName("teacher");
			for(i = 0; i < T.length; i++){
				var id = T[i].getElementsByTagName("id")[0].firstChild.nodeValue;
				var name = T[i].getElementsByTagName("name")[0].firstChild.nodeValue;
				name = decodeURIComponent(name);
				professor_name.push(name);
				professor_id.push(id);
			}
			
			var Departments = xmlData.getElementsByTagName("department");
			for(i = 0; i < Departments.length; i++){
				var id = Departments[i].getElementsByTagName("id")[0].firstChild.nodeValue;
				var name = Departments[i].getElementsByTagName("name")[0].firstChild.nodeValue;
				name = decodeURIComponent(name);
				departments_id.push(id);
				departments_name.push(name);
			}

			//alert("handleLoadTeacgersStudentsList size = " + professor_name.length + "+" + student_name.length);
			var selectpresident = document.getElementById("select-president");
			var selectsecretary = document.getElementById("select-secretary");
			var selectmember = document.getElementById("select-member");
			var selectexaminer1 = document.getElementById("select-examiner1");
			var selectexaminer2 = document.getElementById("select-examiner2");
			for(idx in professor_id){
				selectpresident.options[selectpresident.options.length] = new Option(professor_name[idx],professor_id[idx]);
				selectsecretary.options[selectsecretary.options.length] = new Option(professor_name[idx],professor_id[idx]);
				selectmember.options[selectmember.options.length] = new Option(professor_name[idx],professor_id[idx]);
				selectexaminer1.options[selectexaminer1.options.length] = new Option(professor_name[idx],professor_id[idx]);
				selectexaminer2.options[selectexaminer2.options.length] = new Option(professor_name[idx],professor_id[idx]);
			}
			
			var selectstudent = document.getElementById("select-student");
			for(idx in student_id){
				selectstudent.options[selectstudent.options.length] = new Option(student_name[idx],student_id[idx]);
			}
			
			var selectdepartment = document.getElementById("filter-by");
			selectdepartment.options[selectdepartment.options.length] = new Option("All",0);
			for(idx in departments_id){
				selectdepartment.options[selectdepartment.options.length] = new Option(departments_name[idx],departments_name[idx]);
			}
		}
	}
	
	
}
/*	
function addRow(studentID, studentName, thesis_title, examiner1, examiner2, president, secretary, member){
	
	
	var table = document.getElementById("list-jury");
	var sz = table.rows.length;
	var row = table.insertRow(sz);
	
	//alert("sz = " + sz + " id = " + studentID + " name = " + studentName + " thesis_title = " + thesis_title + " examiner1 = " + examiner1 + 
		//	" examiner2 = " + examiner2 + " president = " + president + " secretary = " + secretary + " member = " + member);
	
	var cell0 = row.insertCell(0);
	var e0 = document.createElement("input");
	e0.type = "text";
	e0.setAttribute("name","studentID-" + sz);
	e0.setAttribute("id","studentID-" + sz);
	e0.setAttribute("value",studentID);
	cell0.appendChild(e0);
	
	var cell1 = row.insertCell(1);
	var e1 = document.createElement("input");
	e1.type = "text";
	e1.setAttribute("name","studentName-" + sz);
	e1.setAttribute("id","studentName-" + sz);
	e1.setAttribute("value",studentName);
	cell1.appendChild(e1);
	
	
	var cell2 = row.insertCell(2);
	var e2 = document.createElement("input");
	e2.type = "text";
	e2.setAttribute("name","thesis-title-" + sz);
	e2.setAttribute("id","thesis-title-" + sz);
	e2.setAttribute("value",thesis_title);
	cell2.appendChild(e2);
	

	var cell3 = row.insertCell(3);
	var e3 = document.createElement("input");
	e3.type = "text";
	e3.setAttribute("name","examiner1-" + sz);
	e3.setAttribute("id","examiner1-" + sz);
	e3.setAttribute("value",examiner1);
	cell3.appendChild(e3);

	
	var cell4 = row.insertCell(4);
	var e4 = document.createElement("input");
	e4.type = "text";
	e4.setAttribute("name","examiner2-" + sz);
	e4.setAttribute("id","examiner2-" + sz);
	e4.setAttribute("value",examiner2);
	cell4.appendChild(e4);

	var cell5 = row.insertCell(5);
	var e5 = document.createElement("input");
	e5.type = "text";
	e5.setAttribute("name","president-" + sz);
	e5.setAttribute("id","president-" + sz);
	e5.setAttribute("value",president);
	cell5.appendChild(e5);
	
	var cell6 = row.insertCell(6);
	var e6 = document.createElement("input");
	e6.type = "text";
	e6.setAttribute("name","secretary-" + sz);
	e6.setAttribute("id","secretary-" + sz);
	e6.setAttribute("value",secretary);
	cell6.appendChild(e6);
	
	var cell7 = row.insertCell(7);
	var e7 = document.createElement("input");
	e7.type = "text";
	e7.setAttribute("name","member-" + sz);
	e7.setAttribute("id","member-" + sz);
	e7.setAttribute("value",member);
	cell7.appendChild(e7);
	
	var cell8 = row.insertCell(8);
	var e8 = document.createElement("select");
	e8.setAttribute("name","slot-" + sz);
	//e8.setAttribute("id","slot-" + sz);
	e8.setAttribute("id","slot-" + sz);
	e8.style.width="50px";
	cell8.appendChild(e8);
	
	
	for(j = 1; j <= 20; j++){
		var opt = document.createElement("option");
		opt.setAttribute("value",j);
		opt.setAttribute("id",sz+"-"+j);
		opt.setAttribute("name",sz+"-"+j);
		opt.style.color='blue';
		opt.innerHTML = j;
		e8.appendChild(opt);
	}
	//cell8.appendChild(e8);
	
	
	var cell9 = row.insertCell(9);
	var e9 = document.createElement("select");
	e9.setAttribute("name","room-" + sz);	
	e9.setAttribute("id","room-" + sz);
	e9.style.width="50px";
	
	var optRoom;
	for(j = 1; j <= 20; j++){
		optRoom = document.createElement("option");
		optRoom.setAttribute("value",j);
		optRoom.innerHTML = j;
		optRoom.style.color='red';
		e9.appendChild(optRoom);
	}
	cell9.appendChild(e9);

}	
*/

function clearTable(tbl){
	while(tbl.rows.length >= 2){
		tbl.deleteRow(1);
	}
}
function resetSchedule(){
	var table = document.getElementById("list-jury");
	var sz = table.rows.length;

	for(i = 1; i < sz; i++){
		var slot = document.getElementById("slot-" + i);
		var room = document.getElementById("room-" + i);
		slot.options.selectedIndex = 0;
		room.options.selectedIndex = 0;
	}
}
function addRow(studentID, studentName, thesis_title, examiner1, examiner2, president, secretary, member, slot, room, color){
	
	
	var table = document.getElementById("list-jury");
	var sz = table.rows.length;
	var row = table.insertRow(sz);
	
	//alert("sz = " + sz + " id = " + studentID + " name = " + studentName + " thesis_title = " + thesis_title + " examiner1 = " + examiner1 + 
			//" examiner2 = " + examiner2 + " president = " + president + " secretary = " + secretary + " member = " + member + " slot = " + 
			//slot + " room = " + room + " color = " + color);
	
	var cell0 = row.insertCell(0);
	var e0 = document.createElement("input");
	e0.type = "text";
	e0.setAttribute("name","studentID-" + sz);
	e0.setAttribute("id","studentID-" + sz);
	e0.setAttribute("value",studentID);
	e0.style.width="50px";
	e0.style.color = color;
	
	cell0.appendChild(e0);
	
	var cell1 = row.insertCell(1);
	var e1 = document.createElement("input");
	e1.type = "text";
	e1.setAttribute("name","studentName-" + sz);
	e1.setAttribute("id","studentName-" + sz);
	e1.setAttribute("value",studentName);
	e1.style.color = color;
	cell1.appendChild(e1);
	
	
	var cell2 = row.insertCell(2);
	var e2 = document.createElement("input");
	e2.type = "text";
	e2.setAttribute("name","thesis-title-" + sz);
	e2.setAttribute("id","thesis-title-" + sz);
	e2.setAttribute("value",thesis_title);
	e2.style.color = color;
	cell2.appendChild(e2);
	

	var cell3 = row.insertCell(3);
	var e3 = document.createElement("input");
	e3.type = "text";
	e3.setAttribute("name","examiner1-" + sz);
	e3.setAttribute("id","examiner1-" + sz);
	e3.setAttribute("value",examiner1);
	e3.style.color = color;
	cell3.appendChild(e3);

	
	var cell4 = row.insertCell(4);
	var e4 = document.createElement("input");
	e4.type = "text";
	e4.setAttribute("name","examiner2-" + sz);
	e4.setAttribute("id","examiner2-" + sz);
	e4.setAttribute("value",examiner2);
	e4.style.color = color;
	cell4.appendChild(e4);

	var cell5 = row.insertCell(5);
	var e5 = document.createElement("input");
	e5.type = "text";
	e5.setAttribute("name","president-" + sz);
	e5.setAttribute("id","president-" + sz);
	e5.setAttribute("value",president);
	e5.style.color = color;
	cell5.appendChild(e5);
	
	var cell6 = row.insertCell(6);
	var e6 = document.createElement("input");
	e6.type = "text";
	e6.setAttribute("name","secretary-" + sz);
	e6.setAttribute("id","secretary-" + sz);
	e6.setAttribute("value",secretary);
	e6.style.color = color;
	cell6.appendChild(e6);
	
	var cell7 = row.insertCell(7);
	var e7 = document.createElement("input");
	e7.type = "text";
	e7.setAttribute("name","member-" + sz);
	e7.setAttribute("id","member-" + sz);
	e7.setAttribute("value",member);
	e7.style.color = color;
	cell7.appendChild(e7);
	
	var cell8 = row.insertCell(8);
	var e8 = document.createElement("select");
	e8.setAttribute("name","slot-" + sz);
	//e8.setAttribute("id","slot-" + sz);
	e8.setAttribute("id","slot-" + sz);
	e8.style.width="50px";
	cell8.appendChild(e8);
	
	
	for(j = 0; j <= 20; j++){
		var opt = document.createElement("option");
		opt.setAttribute("value",j);
		opt.setAttribute("id",sz+"-"+j);
		opt.setAttribute("name",sz+"-"+j);
		opt.style.color='blue';
		if(j == slot)
			opt.selected = true;
		opt.innerHTML = j;
		e8.appendChild(opt);
	}
	//cell8.appendChild(e8);
	
	
	var cell9 = row.insertCell(9);
	var e9 = document.createElement("select");
	e9.setAttribute("name","room-" + sz);	
	e9.setAttribute("id","room-" + sz);
	e9.style.width="50px";
	
	var optRoom;
	for(j = 0; j <= 20; j++){
		optRoom = document.createElement("option");
		optRoom.setAttribute("value",j);
		optRoom.innerHTML = j;
		optRoom.style.color='red';
		if(j == room)
			optRoom.selected = true;
		e9.appendChild(optRoom);
	}
	cell9.appendChild(e9);

	var cell10 = row.insertCell(10);
	var editForm = document.createElement("FORM");
	editForm.name = 'Edit';
	editForm.method = 'POST';
	editForm.action = 'UpdateJury.jsp';
	//e10.setAttribute("value","<a href=home.jsp>Edit</a>");
	var editBt = document.createElement('INPUT');
	editBt.type = 'submit';
	editBt.value = 'Edit';
	var stdID = document.createElement('INPUT');
	stdID.type = 'hidden';
	stdID.name = "studentID";
	stdID.id = "studentID";
	//stdID.value = "\""+ studentID + "\"";
	stdID.value = studentID;
	editForm.appendChild(stdID);
	editForm.appendChild(editBt);
	cell10.appendChild(editForm);
	
	var cell11 = row.insertCell(11);
	var deleteForm = document.createElement("FORM");
	deleteForm.name = "Delete";
	deleteForm.method = "POST";
	deleteForm.action = "DeleteJury.jsp";
	var deleteBt = document.createElement("INPUT");
	deleteBt.type = "submit";
	deleteBt.value = "Delete";
	var stdIDdeleted = document.createElement("INPUT");
	stdIDdeleted.type = "hidden";
	stdIDdeleted.name = "studentID";
	stdIDdeleted.id = "studentID";
	//stdIDdeleted.value = "\"" + studentID + "\"";
	stdIDdeleted.value = studentID;
	deleteForm.appendChild(stdIDdeleted);
	deleteForm.appendChild(deleteBt);
	cell11.appendChild(deleteForm);
}	

function loadJuryList(){
	if(xmlhttp){
		//alert("loadJuryList");
	   var xmlStr = "<?xml version=" + "\"" + "1.0" + "\"" + " encoding=" + "\"" + "UTF-8" + "\"" + "?>";
	   xmlStr += "\n" + "<LoadJury>" + "\n";
	   xmlStr += "<SortBy>" + document.getElementById("sortby").value + "</SortBy>\n";
	   xmlStr += "<FilterBy>" + document.getElementById("filter-by").value + "</FilterBy>\n";
	   xmlStr += "</LoadJury>";
	   //alert(xmlStr);
	    //xmlhttp.open("POST","TeacherManager",true); //getname will be the servlet name
	    xmlhttp.open("POST","DefenseScheduleManager",true); //getname will be the servlet name
	    xmlhttp.onreadystatechange  = handleLoadJury;
	    //xmlhttp.onreadystatechange  = handleSubmitPoint;
	    xmlhttp.overrideMimeType('text/xml');
	    xmlhttp.setRequestHeader('Content-Type', 'text/xml; charset=utf-8');
	    xmlhttp.send(xmlStr); //Posting to Servlet

	    //xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	    //xmlhttp.send("xml="+xml); //Posting to Servlet
	}
	
}	
function handleLoadJury(){
	if(xmlhttp.readyState == 4){
		if(xmlhttp.status == 200){
			var xmlData = xmlhttp.responseXML;
			
			var jr = xmlData.getElementsByTagName("Jury");
			var table = document.getElementById("list-jury");
			clearTable(table);
			
			//var studentIDs = xmlData.getElementsByTagName("StudentID");
			//var studentNames = xmlData.getElementsByTagName("StudentName");
			//var thesis_titles = xmlData.getElementsByTagName("ThesisTitle");
			//var examiner1Names = xmlData.getElementsByTagName("Examiner1Name");
			//var examiner2Names = xmlData.getElementsByTagName("Examiner2Name");
			//var presidentNames = xmlData.getElementsByTagName("PresidentName");
			//var secretaryNames = xmlData.getElementsByTagName("SecretaryName");
			//var memberNames = xmlData.getElementsByTagName("MemberName");
			
			var tablecontents = "";
			tablecontents += "<table class=" + '"' + "table table-bordered table-condensed" + '"' + "id=" + '"' + "userTable" + '"' + ">";
			tablecontents += "<thread>";
			tablecontents += "<th>StudentID</th>";
			tablecontents += "<th>StudentName</th>";
			tablecontents += "<th>ThesisTitle</th>";
			tablecontents += "<th>Examiner1</th>";
			tablecontents += "<th>Examiner2</th>";
			tablecontents += "<th>President</th>";
			tablecontents += "<th>Secretary</th>";
			tablecontents += "<th>Member</th>";
			tablecontents += "</thread>";
			tablecontents += "<tbody>";
			for(i = 0; i < jr.length; i++){
				var studentID = jr[i].getElementsByTagName("StudentID")[0].firstChild.nodeValue;
				var studentName = decodeURIComponent(jr[i].getElementsByTagName("StudentName")[0].firstChild.nodeValue);
				var thesis_title = decodeURIComponent(jr[i].getElementsByTagName("ThesisTitle")[0].firstChild.nodeValue);
				var examiner1Name = decodeURIComponent(jr[i].getElementsByTagName("Examiner1Name")[0].firstChild.nodeValue);
				var examiner2Name = decodeURIComponent(jr[i].getElementsByTagName("Examiner2Name")[0].firstChild.nodeValue);
				var presidentName = decodeURIComponent(jr[i].getElementsByTagName("PresidentName")[0].firstChild.nodeValue);
				var secretaryName = decodeURIComponent(jr[i].getElementsByTagName("SecretaryName")[0].firstChild.nodeValue);
				var memberName = decodeURIComponent(jr[i].getElementsByTagName("MemberName")[0].firstChild.nodeValue);
				var slot = jr[i].getElementsByTagName("Slot")[0].firstChild.nodeValue;
				var room = jr[i].getElementsByTagName("Room")[0].firstChild.nodeValue;
				
				tablecontents += "<tr id='tr" + studentID + "'>";
				tablecontents += "<td>" + studentID + "</td>" + "<td>" + 
				studentName + "</td>" + "<td>" + thesis_title + "</td>" + "<td>" + examiner1Name + "</td>" + 
				"<td>" + examiner2Name + "</td>" + "<td>" + presidentName + "</td>" + "<td>" + secretaryName + "</td>" + 
				"<td>" + memberName + "</td>";
				tablecontents += "<td><a href=" + '"' + "EditProfessor.jsp?name=" +  "\">Edit</td>";
				//tablecontents += "<td><input type=" + '"' + "button" + '"' + " value=" + '"' + "Delete" + '"' + " onclick=" + '"' + "deleteProfessor()" + '"' + "></td>";
				tablecontents += "<td><a href=" + '"' + "DeleteProfessor.jsp?id=" + studentID + "\">Delete</td>";
				tablecontents += "</tr>";
				
				//addRow(studentID,studentName,thesis_title,examiner1Name,examiner2Name,presidentName,secretaryName,memberName);
				addRow(studentID,studentName,thesis_title,examiner1Name,examiner2Name,presidentName,secretaryName,memberName,slot,room,'black');
			}
			tablecontents += "</tbody>";
			tablecontents += "</table>";
			//document.getElementById("jury-table").innerHTML = tablecontents;
		}
	}
	
}
function addJury(){
	//document.getElementById("test").innerHTML = document.getElementById("teacher_name").value;
	
	if(xmlhttp){
			//alert("Add teacher");
		   var name = document.getElementById("select-student").value;
		   var title = document.getElementById("thesis_title").value;
		   var examiner1 = document.getElementById("select-examiner1").value;
		   var examiner2 = document.getElementById("select-examiner2").value;
		   var president = document.getElementById("select-president").value;
		   var secretary = document.getElementById("select-secretary").value;
		   var member = document.getElementById("select-member").value;
		   
		   //name = Utf8Coder.encode(name);
		   var xmlStr = "<?xml version=" + "\"" + "1.0" + "\"" + " encoding=" + "\"" + "UTF-8" + "\"" + "?>";
		   xmlStr += "\n" + "<AddJury>" + "\n";
		   xmlStr += "<student>" + name + "</student>";
		   xmlStr += "<thesis_title>" + title + "</thesis_title>";
		   xmlStr += "<examiner1>" + examiner1 + "</examiner1>";
		   xmlStr += "<examiner2>" + examiner2 + "</examiner2>";
		   xmlStr += "<president>" + president + "</president>";
		   xmlStr += "<secretary>" + secretary + "</secretary>";
		   xmlStr += "<member>" + member + "</member>";
		   xmlStr += "</AddJury>";
		   //alert(xmlStr);
		    //xmlhttp.open("POST","TeacherManager",true); //getname will be the servlet name
		    xmlhttp.open("POST","DefenseScheduleManager",true); //getname will be the servlet name
		    xmlhttp.onreadystatechange  = handleAddJury;
		    //xmlhttp.onreadystatechange  = handleSubmitPoint;
		    xmlhttp.overrideMimeType('text/xml');
		    xmlhttp.setRequestHeader('Content-Type', 'text/xml');
		    xmlhttp.send(xmlStr); //Posting to Servlet

		    //xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
		    //xmlhttp.send("xml="+xml); //Posting to Servlet
	}
	
}	

function handleAddJury(){
	loadJuryList();
}

function checkConsistency(){
	//alert("check consistency");
	if(xmlhttp){
		//alert("Add teacher");
		var table = document.getElementById("list-jury");
		var sz = table.rows.length;
		
		var xmlStr = "<?xml version=" + "\"" + "1.0" + "\"" + " encoding=" + "\"" + "UTF-8" + "\"" + "?>";

	   	xmlStr += "<CheckListJury>\n";
		for(i = 1; i < sz; i++){
			var id = document.getElementById("studentID-" + i).value;
			//alert("id = " + id);
		   	var name = document.getElementById("studentName-" + i).value;
		   	var title = document.getElementById("thesis-title-" + i).value;
		   	var examiner1 = document.getElementById("examiner1-" + i).value;
		   	var examiner2 = document.getElementById("examiner2-" + i).value;
		   	var president = document.getElementById("president-" + i).value;
		   	var secretary = document.getElementById("secretary-" + i).value;
		   	var member = document.getElementById("member-" + i).value;
		   	var slot = document.getElementById("slot-" + i).value;
		   	var room = document.getElementById("room-" + i).value;
		   
		   	//name = Utf8Coder.encode(name);
		   	xmlStr += "\n" + "<Jury>" + "\n";
		   	xmlStr += "<studentID>" + id + "</studentID>";
		   	xmlStr += "<studentName>" + name + "</studentName>";
		   	xmlStr += "<thesis_title>" + title + "</thesis_title>";
		   	xmlStr += "<examiner1>" + examiner1 + "</examiner1>";
		   	xmlStr += "<examiner2>" + examiner2 + "</examiner2>";
		   	xmlStr += "<president>" + president + "</president>";
		   	xmlStr += "<secretary>" + secretary + "</secretary>";
		   	xmlStr += "<member>" + member + "</member>";
		   	xmlStr += "<slot>" + slot + "</slot>";
		   	xmlStr += "<room>" + room + "</room>";
		   	xmlStr += "</Jury>";
		}
		
		xmlStr += "</CheckListJury>\n";
	   	//alert(xmlStr);
	    //xmlhttp.open("POST","TeacherManager",true); //getname will be the servlet name
	    xmlhttp.open("POST","DefenseScheduleManager",true); //getname will be the servlet name
	    xmlhttp.onreadystatechange  = handleCheckConsistency;
	    //xmlhttp.onreadystatechange  = handleSubmitPoint;
	    xmlhttp.overrideMimeType('text/xml');
	    xmlhttp.setRequestHeader('Content-Type', 'text/xml');
	    xmlhttp.send(xmlStr); //Posting to Servlet

	    //xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	    //xmlhttp.send("xml="+xml); //Posting to Servlet
	}
	
}

function handleCheckConsistency(){
	if(xmlhttp.readyState == 4){
		if(xmlhttp.status == 200){
			var xmlData = xmlhttp.responseXML;
			//alert(xmlData);
			//var table = document.getElementById("list-jury");
			
			
			var jr = xmlData.getElementsByTagName("Jury");
			var table = document.getElementById("list-jury");
			clearTable(table);
			
			//alert("Get " + jr.length + " items");
			
			//var studentIDs = xmlData.getElementsByTagName("StudentID");
			//var studentNames = xmlData.getElementsByTagName("StudentName");
			//var thesis_titles = xmlData.getElementsByTagName("ThesisTitle");
			//var examiner1Names = xmlData.getElementsByTagName("Examiner1Name");
			//var examiner2Names = xmlData.getElementsByTagName("Examiner2Name");
			//var presidentNames = xmlData.getElementsByTagName("PresidentName");
			//var secretaryNames = xmlData.getElementsByTagName("SecretaryName");
			//var memberNames = xmlData.getElementsByTagName("MemberName");
			
			var tablecontents = "";
			tablecontents += "<table class=" + '"' + "table table-bordered table-condensed" + '"' + "id=" + '"' + "userTable" + '"' + ">";
			tablecontents += "<thread>";
			tablecontents += "<th>StudentID</th>";
			tablecontents += "<th>StudentName</th>";
			tablecontents += "<th>ThesisTitle</th>";
			tablecontents += "<th>Examiner1</th>";
			tablecontents += "<th>Examiner2</th>";
			tablecontents += "<th>President</th>";
			tablecontents += "<th>Secretary</th>";
			tablecontents += "<th>Member</th>";
			tablecontents += "</thread>";
			tablecontents += "<tbody>";
			for(i = 0; i < jr.length; i++){
				var studentID = jr[i].getElementsByTagName("StudentID")[0].firstChild.nodeValue;
				var studentName = jr[i].getElementsByTagName("StudentName")[0].firstChild.nodeValue;
				var thesis_title = jr[i].getElementsByTagName("ThesisTitle")[0].firstChild.nodeValue;
				var examiner1Name = jr[i].getElementsByTagName("Examiner1Name")[0].firstChild.nodeValue;
				var examiner2Name = jr[i].getElementsByTagName("Examiner2Name")[0].firstChild.nodeValue;
				var presidentName = jr[i].getElementsByTagName("PresidentName")[0].firstChild.nodeValue;
				var secretaryName = jr[i].getElementsByTagName("SecretaryName")[0].firstChild.nodeValue;
				var memberName = jr[i].getElementsByTagName("MemberName")[0].firstChild.nodeValue;
				var slot = jr[i].getElementsByTagName("Slot")[0].firstChild.nodeValue;
				var room = jr[i].getElementsByTagName("Room")[0].firstChild.nodeValue;
				var conflict = jr[i].getElementsByTagName("Conflict")[0].firstChild.nodeValue;
				
				tablecontents += "<tr id='tr" + studentID + "'>";
				tablecontents += "<td>" + studentID + "</td>" + "<td>" + 
				studentName + "</td>" + "<td>" + thesis_title + "</td>" + "<td>" + examiner1Name + "</td>" + 
				"<td>" + examiner2Name + "</td>" + "<td>" + presidentName + "</td>" + "<td>" + secretaryName + "</td>" + 
				"<td>" + memberName + "</td>";
				tablecontents += "<td><a href=" + '"' + "EditProfessor.jsp?name=" +  "\">Edit</td>";
				//tablecontents += "<td><input type=" + '"' + "button" + '"' + " value=" + '"' + "Delete" + '"' + " onclick=" + '"' + "deleteProfessor()" + '"' + "></td>";
				tablecontents += "<td><a href=" + '"' + "DeleteProfessor.jsp?id=" + studentID + "\">Delete</td>";
				tablecontents += "</tr>";
				
				var color = 'black';
				if(conflict == 1) 
					color = 'red';
				
				addRow(studentID,studentName,thesis_title,examiner1Name,examiner2Name,presidentName,secretaryName,memberName,slot,room,color);
				
			}
			tablecontents += "</tbody>";
			tablecontents += "</table>";
			//document.getElementById("jury-table").innerHTML = tablecontents;
		}
	}
}


function searchSolution(){
	//alert("check consistency");
	if(xmlhttp){
		//alert("Add teacher");
		var table = document.getElementById("list-jury");
		var sz = table.rows.length;
		
		var xmlStr = "<?xml version=" + "\"" + "1.0" + "\"" + " encoding=" + "\"" + "UTF-8" + "\"" + "?>";

	   	xmlStr += "<SearchSolution>\n";
		for(i = 1; i < sz; i++){
			var id = document.getElementById("studentID-" + i).value;
			//alert("id = " + id);
		   	var name = document.getElementById("studentName-" + i).value;
		   	var title = document.getElementById("thesis-title-" + i).value;
		   	var examiner1 = document.getElementById("examiner1-" + i).value;
		   	var examiner2 = document.getElementById("examiner2-" + i).value;
		   	var president = document.getElementById("president-" + i).value;
		   	var secretary = document.getElementById("secretary-" + i).value;
		   	var member = document.getElementById("member-" + i).value;
		   	var slot = document.getElementById("slot-" + i).value;
		   	var room = document.getElementById("room-" + i).value;
		   
		   	//name = Utf8Coder.encode(name);
		   	xmlStr += "\n" + "<Jury>" + "\n";
		   	xmlStr += "<studentID>" + id + "</studentID>";
		   	xmlStr += "<studentName>" + name + "</studentName>";
		   	xmlStr += "<thesis_title>" + title + "</thesis_title>";
		   	xmlStr += "<examiner1>" + examiner1 + "</examiner1>";
		   	xmlStr += "<examiner2>" + examiner2 + "</examiner2>";
		   	xmlStr += "<president>" + president + "</president>";
		   	xmlStr += "<secretary>" + secretary + "</secretary>";
		   	xmlStr += "<member>" + member + "</member>";
		   	xmlStr += "<slot>" + slot + "</slot>";
		   	xmlStr += "<room>" + room + "</room>";
		   	xmlStr += "</Jury>";
		}
		
		xmlStr += "<slots>" + document.getElementById("slots").value + "</slots>";
		xmlStr += "<rooms>" + document.getElementById("rooms").value + "</rooms>";
		
		xmlStr += "</SearchSolution>\n";
	   	//alert(xmlStr);
	    //xmlhttp.open("POST","TeacherManager",true); //getname will be the servlet name
	    xmlhttp.open("POST","DefenseScheduleManager",true); //getname will be the servlet name
	    xmlhttp.onreadystatechange  = handleSearchSolution;
	    //xmlhttp.onreadystatechange  = handleSubmitPoint;
	    xmlhttp.overrideMimeType('text/xml');
	    xmlhttp.setRequestHeader('Content-Type', 'text/xml');
	    xmlhttp.send(xmlStr); //Posting to Servlet

	    //xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	    //xmlhttp.send("xml="+xml); //Posting to Servlet
	}
	
}

function handleSearchSolution(){
	if(xmlhttp.readyState == 4){
		if(xmlhttp.status == 200){
			var xmlData = xmlhttp.responseXML;
			//alert(xmlData);
			//var table = document.getElementById("list-jury");
			
			
			var jr = xmlData.getElementsByTagName("Jury");
			var table = document.getElementById("list-jury");
			clearTable(table);
			
			//alert("Get " + jr.length + " items");
			
			//var studentIDs = xmlData.getElementsByTagName("StudentID");
			//var studentNames = xmlData.getElementsByTagName("StudentName");
			//var thesis_titles = xmlData.getElementsByTagName("ThesisTitle");
			//var examiner1Names = xmlData.getElementsByTagName("Examiner1Name");
			//var examiner2Names = xmlData.getElementsByTagName("Examiner2Name");
			//var presidentNames = xmlData.getElementsByTagName("PresidentName");
			//var secretaryNames = xmlData.getElementsByTagName("SecretaryName");
			//var memberNames = xmlData.getElementsByTagName("MemberName");
			
			var tablecontents = "";
			tablecontents += "<table class=" + '"' + "table table-bordered table-condensed" + '"' + "id=" + '"' + "userTable" + '"' + ">";
			tablecontents += "<thread>";
			tablecontents += "<th>StudentID</th>";
			tablecontents += "<th>StudentName</th>";
			tablecontents += "<th>ThesisTitle</th>";
			tablecontents += "<th>Examiner1</th>";
			tablecontents += "<th>Examiner2</th>";
			tablecontents += "<th>President</th>";
			tablecontents += "<th>Secretary</th>";
			tablecontents += "<th>Member</th>";
			tablecontents += "</thread>";
			tablecontents += "<tbody>";
			for(i = 0; i < jr.length; i++){
				var studentID = jr[i].getElementsByTagName("StudentID")[0].firstChild.nodeValue;
				var studentName = jr[i].getElementsByTagName("StudentName")[0].firstChild.nodeValue;
				var thesis_title = jr[i].getElementsByTagName("ThesisTitle")[0].firstChild.nodeValue;
				var examiner1Name = jr[i].getElementsByTagName("Examiner1Name")[0].firstChild.nodeValue;
				var examiner2Name = jr[i].getElementsByTagName("Examiner2Name")[0].firstChild.nodeValue;
				var presidentName = jr[i].getElementsByTagName("PresidentName")[0].firstChild.nodeValue;
				var secretaryName = jr[i].getElementsByTagName("SecretaryName")[0].firstChild.nodeValue;
				var memberName = jr[i].getElementsByTagName("MemberName")[0].firstChild.nodeValue;
				var slot = jr[i].getElementsByTagName("Slot")[0].firstChild.nodeValue;
				var room = jr[i].getElementsByTagName("Room")[0].firstChild.nodeValue;
				var conflict = jr[i].getElementsByTagName("Conflict")[0].firstChild.nodeValue;
				
				tablecontents += "<tr id='tr" + studentID + "'>";
				tablecontents += "<td>" + studentID + "</td>" + "<td>" + 
				studentName + "</td>" + "<td>" + thesis_title + "</td>" + "<td>" + examiner1Name + "</td>" + 
				"<td>" + examiner2Name + "</td>" + "<td>" + presidentName + "</td>" + "<td>" + secretaryName + "</td>" + 
				"<td>" + memberName + "</td>";
				tablecontents += "<td><a href=" + '"' + "EditProfessor.jsp?name=" +  "\">Edit</td>";
				//tablecontents += "<td><input type=" + '"' + "button" + '"' + " value=" + '"' + "Delete" + '"' + " onclick=" + '"' + "deleteProfessor()" + '"' + "></td>";
				tablecontents += "<td><a href=" + '"' + "DeleteProfessor.jsp?id=" + studentID + "\">Delete</td>";
				tablecontents += "</tr>";
				
				var color = 'black';
				if(conflict == 1) 
					color = 'red';
				
				addRow(studentID,studentName,thesis_title,examiner1Name,examiner2Name,presidentName,secretaryName,memberName,slot,room,color);
				
			}
			tablecontents += "</tbody>";
			tablecontents += "</table>";
			//document.getElementById("jury-table").innerHTML = tablecontents;
		}
	}
}

function saveSchedule(){
	//alert("check consistency");
	if(xmlhttp){
		//alert("Add teacher");
		var table = document.getElementById("list-jury");
		var sz = table.rows.length;
		
		var xmlStr = "<?xml version=" + "\"" + "1.0" + "\"" + " encoding=" + "\"" + "UTF-8" + "\"" + "?>";

	   	xmlStr += "<SaveSchedule>\n";
		for(i = 1; i < sz; i++){
			var id = document.getElementById("studentID-" + i).value;
			//alert("id = " + id);
		   	var name = document.getElementById("studentName-" + i).value;
		   	var title = document.getElementById("thesis-title-" + i).value;
		   	var examiner1 = document.getElementById("examiner1-" + i).value;
		   	var examiner2 = document.getElementById("examiner2-" + i).value;
		   	var president = document.getElementById("president-" + i).value;
		   	var secretary = document.getElementById("secretary-" + i).value;
		   	var member = document.getElementById("member-" + i).value;
		   	var slot = document.getElementById("slot-" + i).value;
		   	var room = document.getElementById("room-" + i).value;
		   
		   	//name = Utf8Coder.encode(name);
		   	xmlStr += "\n" + "<Jury>" + "\n";
		   	xmlStr += "<studentID>" + id + "</studentID>";
		   	xmlStr += "<studentName>" + name + "</studentName>";
		   	xmlStr += "<thesis_title>" + title + "</thesis_title>";
		   	xmlStr += "<examiner1>" + examiner1 + "</examiner1>";
		   	xmlStr += "<examiner2>" + examiner2 + "</examiner2>";
		   	xmlStr += "<president>" + president + "</president>";
		   	xmlStr += "<secretary>" + secretary + "</secretary>";
		   	xmlStr += "<member>" + member + "</member>";
		   	xmlStr += "<slot>" + slot + "</slot>";
		   	xmlStr += "<room>" + room + "</room>";
		   	xmlStr += "</Jury>";
		}
		
		xmlStr += "<slots>" + document.getElementById("slots").value + "</slots>";
		xmlStr += "<rooms>" + document.getElementById("rooms").value + "</rooms>";
		
		xmlStr += "</SaveSchedule>\n";
	   	//alert(xmlStr);
	    //xmlhttp.open("POST","TeacherManager",true); //getname will be the servlet name
	    xmlhttp.open("POST","DefenseScheduleManager",true); //getname will be the servlet name
	    xmlhttp.onreadystatechange  = handleSaveSchedule;
	    //xmlhttp.onreadystatechange  = handleSubmitPoint;
	    xmlhttp.overrideMimeType('text/xml');
	    xmlhttp.setRequestHeader('Content-Type', 'text/xml');
	    xmlhttp.send(xmlStr); //Posting to Servlet

	    //xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	    //xmlhttp.send("xml="+xml); //Posting to Servlet
	}
	
}

function handleSaveSchedule(){
	if(xmlhttp.readyState == 4){
		if(xmlhttp.status == 200){
			var xmlData = xmlhttp.responseXML;
		}
	}
}

</script>

</body>
</html>