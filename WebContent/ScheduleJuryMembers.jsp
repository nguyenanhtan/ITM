<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Quan ly thong tin hoi dong bao ve</title>
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
<h1>Quan ly thong tin cac hoi dong bao ve</h1>

<form id="teacher-info" action="#">
<ul>
<div style="border:1px solid blue">
Ho va ten hoc vien: <select name="student" id="select-student" class="select-box min-width-140">
	</select>
Ten de tai: <input type="text" name="thesis_title" id="thesis_title" style="width:400px">
Hoi dong: <select name="select-defense-session" id="select-defense-session"></select>


<table id="jury-info">
<tr>
<td>
	GV huong dan: <br>
	<select name="supervisor" id="select-supervisor" class="select-box min-width-140">
	</select>
</td>
<td>
	
	<select name="examiner1" id="select-examiner1" class="select-box min-width-140" style="visibility:hidden">
	</select>
</td>
<td>
	
	<select name="examiner2" id="select-examiner2" class="select-box min-width-140" style="visibility:hidden">
	</select>
</td>

<td>
	
	<select name="president" id="select-president" class="select-box min-width-140" style="visibility:hidden">
	</select>
</td>

<td>
	
	<select name="secretary" id="select-secretary" class="select-box min-width-140" style="visibility:hidden">
	</select>
</td>

<td>
	
	<select name="member" id="select-member" class="select-box min-width-140" style="visibility:hidden">
	</select>
</td>

</tr>
</table>


<input type="button" name="add_jury" value="Them hoi dong" style="width:100px" onclick="addJury()"><br>
</div>

<input type="button" name="load_jury" value="Danh sach hoi dong" style="width:200px" onclick="loadJuryList()">
Sap xep theo: <select name="sortby" id="sortby">
	<option value="Supervisor">Giao vien huong dan</option>
	<option value="StudentID">StudentID</option>
	<option value="Slot">Slot</option>
	<option value="Room">Room</option>
	<option value="Room-Slot">Room-Slot</option>
	<option value="Slot-Room">Slot-Room</option>
</select>
Loc theo: <select name="filter-by" id="filter-by"></select>

<table id="list-jury">
<tr>
<td>
	ID
</td>
<td>
	Hoc vien
</td>
<td>
	Ten de tai
</td>
<td>
	GV huong dan
</td>
<td>
	Phan bien 1
</td>
<td>
	Phan bien 2
</td>

<td>
	Chu tich
</td>

<td>
	Thu ky
</td>

<td>
	Uy vien
</td>

<td>
	Kip
</td>
<td>
	Thoi gian
</td>

</tr>

</table>
<tr>
Chon GV vao hoi dong
<select id="jury-members">
</select>
<input type="button" id="add-jury-member" value="Them vao hoi dong" onclick="addMemberToJury()">
<select id="selected-members"></select>

</tr>

<input type="button" value="Reset" onclick="resetSchedule()">
<input type="button" value="Hop nhat cac hoi dong bo mon" onclick="consolidateJuriesOfDepartments()" style="visibility:hidden">
<br>
<input type="button" value="Xep tu dong" onclick="searchSolution()">
Thoi gian chay: <input type="text" name="time-limit" id="time-limit" style="width:50px" value="10">
Thuat toan: <select name="algorithm" id="algorithm">
<option value="BackTrackSearch">BackTrackSearch</option>
<option value="TabuSearch">TabuSearch</option>

</select>
<input type="button" value="Kiem tra tinh hop le" onclick="checkConsistency()">
</ul>
</form>
<table>
<tr><td>
<input type="button" value="Luu lai" onclick="saveSchedule()">
<input type="text" id="room-name" style="width:50px">
</td></tr>
</table>
<p id="test"></p>
<p id="jury-table"></p>

<a href="home.jsp">Back to Home</a>

<script type="text/javascript">
var professor_id = new Array();
var professor_name = new Array();
var professors = new Array();
var student_id = new Array();
var student_name = new Array();
var departments_id = new Array();
var departments_name = new Array();
var session_id = new Array();
var session_description = new Array();

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

function findProfessor(id){
	//alert("findProfessor with id = " + id + " professors.length = " + professors.length);
	for(ip = 0; ip < professors.length; ip++){
		var p = professors[ip];
		if(p.id === id){
			//alert("found p at index " + ip);
			return p;
		}	
	}
	return null;
}
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

			professors = new Array();
			var T = xmlData.getElementsByTagName("teacher");
			for(i = 0; i < T.length; i++){
				var id = T[i].getElementsByTagName("id")[0].firstChild.nodeValue;
				var name = T[i].getElementsByTagName("name")[0].firstChild.nodeValue;
				name = decodeURIComponent(name);
				professor_name.push(name);
				professor_id.push(id);
				
				var p = new Object();
				p.id = id;
				p.name = name;
				professors.push(p);
			}
			
			var Departments = xmlData.getElementsByTagName("department");
			for(i = 0; i < Departments.length; i++){
				var id = Departments[i].getElementsByTagName("id")[0].firstChild.nodeValue;
				var name = Departments[i].getElementsByTagName("name")[0].firstChild.nodeValue;
				name = decodeURIComponent(name);
				departments_id.push(id);
				departments_name.push(name);
			}

			var DefenseSessions = xmlData.getElementsByTagName("defensesession");
			for(i = 0; i < DefenseSessions.length; i++){
				var id = DefenseSessions[i].getElementsByTagName("id")[0].firstChild.nodeValue;
				var des = DefenseSessions[i].getElementsByTagName("description")[0].firstChild.nodeValue;
				session_id.push(id);
				session_description.push(des);
			}
			var select_session = document.getElementById("select-defense-session");
			for(id in session_id){
				var opt = new Option(session_description[id],session_id[id]);
				select_session.options[select_session.options.length] = opt;
			}
			
			//alert("handleLoadTeacgersStudentsList size = " + professor_name.length + "+" + student_name.length);
			var selectsupervisor = document.getElementById("select-supervisor");
			var selectpresident = document.getElementById("select-president");
			var selectsecretary = document.getElementById("select-secretary");
			var selectmember = document.getElementById("select-member");
			var selectexaminer1 = document.getElementById("select-examiner1");
			var selectexaminer2 = document.getElementById("select-examiner2");
			
			var selectjurymembers = document.getElementById("jury-members");
			
			for(idx in professor_id){
				selectsupervisor.options[selectsupervisor.options.length] = new Option(professor_name[idx],professor_id[idx]);
				selectpresident.options[selectpresident.options.length] = new Option(professor_name[idx],professor_id[idx]);
				selectsecretary.options[selectsecretary.options.length] = new Option(professor_name[idx],professor_id[idx]);
				selectmember.options[selectmember.options.length] = new Option(professor_name[idx],professor_id[idx]);
				selectexaminer1.options[selectexaminer1.options.length] = new Option(professor_name[idx],professor_id[idx]);
				selectexaminer2.options[selectexaminer2.options.length] = new Option(professor_name[idx],professor_id[idx]);
				
				selectjurymembers.options[selectjurymembers.options.length] = new Option(professor_name[idx],professor_id[idx]);
			}
			
			var selectstudent = document.getElementById("select-student");
			for(idx in student_id){
				selectstudent.options[selectstudent.options.length] = new Option(student_name[idx],student_id[idx]);
			}
			
			var selectdepartment = document.getElementById("filter-by");
			selectdepartment.options[selectdepartment.options.length] = new Option("All","All");
			for(idx in departments_id){
				selectdepartment.options[selectdepartment.options.length] = new Option(departments_name[idx],departments_name[idx]);
			}
		}
	}
	
	//loadJuryList();
}

function addMemberToJury(){
	//var jm = document.getElementById("jury-members-xml").value;
	var selectjurymember_id = document.getElementById("jury-members").value;
	var p = findProfessor(selectjurymember_id);
	var selectjurymember_name = p.name;//document.getElementById("jury-members").value;
	//jm += '<jury-member>' + selectjurymembers_id + '</jury-member>' + "\n";
	//document.getElementById("jury-members-xml").value = jm;
	
	var sel_members = document.getElementById("selected-members");
	sel_members.options[sel_members.options.length] = new Option(selectjurymember_name,selectjurymember_id);
	
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
		//var room = document.getElementById("room-" + i);
		slot.options.selectedIndex = 0;
		//room.options.selectedIndex = 0;
		
		var examiner1 = document.getElementById("examiner1-"+i);
		var examiner2 = document.getElementById("examiner2-"+i);
		var president = document.getElementById("president-"+i);
		var secretary = document.getElementById("secretary-"+i);
		var member = document.getElementById("member-"+i);
		
		examiner1.options.selectedIndex = 0;
		examiner2.options.selectedIndex = 0;
		president.options.selectedIndex = 0;
		secretary.options.selectedIndex = 0;
		member.options.selectedIndex = 0;
	}
}
/*
function suggestSlotRoom(studentID){
	alert("suggest " + studentID);
}
*/
function addRow(studentID, studentName, thesis_title, supervisorId, supervisorName, examiner1Id, examiner1Name, examiner2Id, 
		examiner2Name, 
		presidentId, presidentName, secretaryId, secretaryName, memberId, memberName, slot, slotDescription, room, color){
	
	
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
	var e3 = document.createElement("select");
	//e3.type = "text";
	e3.setAttribute("name","supervisor-" + sz);
	e3.setAttribute("id","supervisor-" + sz);
	//e3.setAttribute("value",examiner1);
	//e3.style.color = color;
	//for(j = 0; j < professor_id.length; j++){
	for(j in professor_id){	
		var opt = document.createElement("option");
		opt.setAttribute("value",professor_id[j]);
		//opt.setAttribute("id",sz+"-"+j);
		opt.setAttribute("label",professor_name[j]);
		opt.style.color='blue';
		if(professor_id[j] == supervisorId)
			opt.selected = true;
		opt.innerHTML = professor_name[j];
		e3.appendChild(opt);
	}
	cell3.appendChild(e3);

	var cell4 = row.insertCell(4);
	var e4 = document.createElement("select");
	//e3.type = "text";
	e4.setAttribute("name","examiner1-" + sz);
	e4.setAttribute("id","examiner1-" + sz);
	//e3.setAttribute("value",examiner1);
	//e3.style.color = color;
	//for(j = 0; j < professor_id.length; j++){
	for(j in professor_id){	
		var opt = document.createElement("option");
		opt.setAttribute("value",professor_id[j]);
		//opt.setAttribute("id",sz+"-"+j);
		opt.setAttribute("label",professor_name[j]);
		opt.style.color='blue';
		if(professor_id[j] == examiner1Id)
			opt.selected = true;
		opt.innerHTML = professor_name[j];
		e4.appendChild(opt);
	}
	cell4.appendChild(e4);

	
	var cell5 = row.insertCell(5);
	var e5 = document.createElement("select");
	//e3.type = "text";
	e5.setAttribute("name","examiner2-" + sz);
	e5.setAttribute("id","examiner2-" + sz);
	//e3.setAttribute("value",examiner1);
	//e3.style.color = color;
	//for(j = 0; j < professor_id.length; j++){
	for(j in professor_id){	
		var opt = document.createElement("option");
		opt.setAttribute("value",professor_id[j]);
		//opt.setAttribute("id",sz+"-"+j);
		opt.setAttribute("label",professor_name[j]);
		opt.style.color='blue';
		if(professor_id[j] == examiner2Id)
			opt.selected = true;
		opt.innerHTML = professor_name[j];
		e5.appendChild(opt);
	}
	cell5.appendChild(e5);

	var cell6 = row.insertCell(6);
	var e6 = document.createElement("select");
	//e3.type = "text";
	e6.setAttribute("name","president-" + sz);
	e6.setAttribute("id","president-" + sz);
	//e3.setAttribute("value",examiner1);
	//e3.style.color = color;
	//for(j = 0; j < professor_id.length; j++){
	for(j in professor_id){	
		var opt = document.createElement("option");
		opt.setAttribute("value",professor_id[j]);
		//opt.setAttribute("id",sz+"-"+j);
		opt.setAttribute("label",professor_name[j]);
		opt.style.color='blue';
		if(professor_id[j] == presidentId)
			opt.selected = true;
		opt.innerHTML = professor_name[j];
		e6.appendChild(opt);
	}
	cell6.appendChild(e6);
	
	var cell7 = row.insertCell(7);
	var e7 = document.createElement("select");
	//e3.type = "text";
	e7.setAttribute("name","secretary-" + sz);
	e7.setAttribute("id","secretary-" + sz);
	//e3.setAttribute("value",examiner1);
	//e3.style.color = color;
	//for(j = 0; j < professor_id.length; j++){
	for(j in professor_id){	
		var opt = document.createElement("option");
		opt.setAttribute("value",professor_id[j]);
		//opt.setAttribute("id",sz+"-"+j);
		opt.setAttribute("label",professor_name[j]);
		opt.style.color='blue';
		if(professor_id[j] == secretaryId)
			opt.selected = true;
		opt.innerHTML = professor_name[j];
		e7.appendChild(opt);
	}
	cell7.appendChild(e7);
	
	var cell8 = row.insertCell(8);
	var e8 = document.createElement("select");
	//e3.type = "text";
	e8.setAttribute("name","member-" + sz);
	e8.setAttribute("id","member-" + sz);
	//e3.setAttribute("value",examiner1);
	//e3.style.color = color;
	//for(j = 0; j < professor_id.length; j++){
	for(j in professor_id){	
		var opt = document.createElement("option");
		opt.setAttribute("value",professor_id[j]);
		//opt.setAttribute("id",sz+"-"+j);
		opt.setAttribute("label",professor_name[j]);
		opt.style.color='blue';
		if(professor_id[j] == memberId)
			opt.selected = true;
		opt.innerHTML = professor_name[j];
		e8.appendChild(opt);
	}
	cell8.appendChild(e8);
	
	
	var cell9 = row.insertCell(9);
	var e9 = document.createElement("select");
	e9.setAttribute("name","slot-" + sz);
	//e8.setAttribute("id","slot-" + sz);
	e9.setAttribute("id","slot-" + sz);
	e9.style.width="50px";
	cell9.appendChild(e9);
	
	
	for(j = 0; j <= 20; j++){
		var opt = document.createElement("option");
		opt.setAttribute("value",j);
		opt.setAttribute("id",sz+"-"+j);
		opt.setAttribute("name",sz+"-"+j);
		opt.style.color='blue';
		if(j == slot)
			opt.selected = true;
		opt.innerHTML = j;
		e9.appendChild(opt);
	}
	//cell8.appendChild(e8);

	var cell10 = row.insertCell(10);
	var e10 = document.createElement("input");
	e10.type="text";
	e10.setAttribute("name","slot-description-" + sz);
	//e8.setAttribute("id","slot-" + sz);
	e10.setAttribute("id","slot-desciption-" + sz);
	e10.style.width="100px";
	e10.value = slotDescription;
	cell10.appendChild(e10);

	
	
	/*
	var cell11 = row.insertCell(11);
	var editForm = document.createElement("FORM");
	editForm.name = 'Edit';
	editForm.method = 'POST';
	editForm.action = 'UpdateJury.jsp';
	//e10.setAttribute("value","<a href=home.jsp>Edit</a>");
	var editBt = document.createElement('INPUT');
	editBt.type = 'submit';
	editBt.value = 'Sua';
	var stdID = document.createElement('INPUT');
	stdID.type = 'hidden';
	stdID.name = "studentID";
	stdID.id = "studentID";
	//stdID.value = "\""+ studentID + "\"";
	stdID.value = studentID;
	editForm.appendChild(stdID);
	editForm.appendChild(editBt);
	cell11.appendChild(editForm);
	*/
	
	var cell11 = row.insertCell(11);
	var deleteForm = document.createElement("FORM");
	deleteForm.name = "Delete";
	deleteForm.method = "POST";
	deleteForm.action = "DeleteJury.jsp";
	var deleteBt = document.createElement("INPUT");
	deleteBt.type = "submit";
	deleteBt.value = "Xoa";
	var stdIDdeleted = document.createElement("INPUT");
	stdIDdeleted.type = "hidden";
	stdIDdeleted.name = "studentID";
	stdIDdeleted.id = "studentID";
	//stdIDdeleted.value = "\"" + studentID + "\"";
	stdIDdeleted.value = studentID;
	deleteForm.appendChild(stdIDdeleted);
	deleteForm.appendChild(deleteBt);
	cell11.appendChild(deleteForm);
	
	/*
	var cell13 = row.insertCell(13);
	var e13 = document.createElement("input");
	e13.type = "button";
	e13.setAttribute("name","suggestSlotRoom-"+sz);
	e13.setAttribute("id","suggestSlotRoom-"+sz);
	e13.value = "Goi y kip-phong";
	//e13.setAttribute("onclick","suggestSlotRoom(" + studentID + ")");
	e13.onclick = function(){
		//alert("suggest slot-room for " + studentID);
		suggestSlotRoom(studentID);
	}
	cell13.appendChild(e13);
	
	
	var cell14 = row.insertCell(14);
	var e14 = document.createElement("select");
	e14.setAttribute("name","room-" + sz);	
	e14.setAttribute("id","room-" + sz);
	e14.style.width="50px";
	e14.style.visibility="hidden";
	
	var optRoom;
	for(j = 0; j <= 20; j++){
		optRoom = document.createElement("option");
		optRoom.setAttribute("value",j);
		optRoom.innerHTML = j;
		optRoom.style.color='red';
		if(j == room)
			optRoom.selected = true;
		e14.appendChild(optRoom);
	}
	cell14.appendChild(e14);

	/*
	var cell13 = row.insertCell(13);
	var suggestForm = document.createElement("FORM");
	suggestForm.name = "SuggestSlot";
	suggestForm.method = "POST";
	suggestForm.action = "DefenseJuryManager.jsp";
	var suggestBt = document.createElement("INPUT");
	suggestBt.type = "submit";
	suggestBt.value = "Suggest Slot";
	var stdIDSuggested = document.createElement("INPUT");
	stdIDSuggested.type = "hidden";
	stdIDSuggested.name = "studentID";
	stdIDSuggested.id = "studentID";
	//stdIDdeleted.value = "\"" + studentID + "\"";
	stdIDSuggested.value = studentID;
	suggestForm.appendChild(stdIDSuggested);
	suggestForm.appendChild(suggestBt);
	cell13.appendChild(suggestForm);
	*/
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
				var supervisorName = decodeURIComponent(jr[i].getElementsByTagName("SupervisorName")[0].firstChild.nodeValue);
				var supervisorId = jr[i].getElementsByTagName("SupervisorID")[0].firstChild.nodeValue;
				var examiner1Name = decodeURIComponent(jr[i].getElementsByTagName("Examiner1Name")[0].firstChild.nodeValue);
				var examiner1Id = jr[i].getElementsByTagName("Examiner1ID")[0].firstChild.nodeValue;
				var examiner2Name = decodeURIComponent(jr[i].getElementsByTagName("Examiner2Name")[0].firstChild.nodeValue);
				var examiner2Id = jr[i].getElementsByTagName("Examiner2ID")[0].firstChild.nodeValue;
				var presidentName = decodeURIComponent(jr[i].getElementsByTagName("PresidentName")[0].firstChild.nodeValue);
				var presidentId = jr[i].getElementsByTagName("PresidentID")[0].firstChild.nodeValue;
				var secretaryName = decodeURIComponent(jr[i].getElementsByTagName("SecretaryName")[0].firstChild.nodeValue);
				var secretaryId = jr[i].getElementsByTagName("SecretaryID")[0].firstChild.nodeValue;
				var memberName = decodeURIComponent(jr[i].getElementsByTagName("MemberName")[0].firstChild.nodeValue);
				var memberId = jr[i].getElementsByTagName("MemberID")[0].firstChild.nodeValue;
				var slot = jr[i].getElementsByTagName("Slot")[0].firstChild.nodeValue;
				var slotDescription = jr[i].getElementsByTagName("Slot-Description")[0].firstChild.nodeValue;
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
				addRow(studentID,studentName,thesis_title,supervisorId, supervisorName, examiner1Id,examiner1Name,examiner2Id,examiner2Name,
						presidentId,presidentName,secretaryId,secretaryName,memberId,memberName,slot,slotDescription,room,'black');
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
		   var title = encodeURIComponent(document.getElementById("thesis_title").value);
		   var supervisor = document.getElementById("select-supervisor").value;
		   //var examiner1 = document.getElementById("select-examiner1").value;
		   //var examiner2 = document.getElementById("select-examiner2").value;
		   //var president = document.getElementById("select-president").value;
		   //var secretary = document.getElementById("select-secretary").value;
		   //var member = document.getElementById("select-member").value;
		   var sessionID = document.getElementById("select-defense-session").value;
		   
		   if(title.length == 0) title = '-';
		   
		   //name = Utf8Coder.encode(name);
		   var xmlStr = "<?xml version=" + "\"" + "1.0" + "\"" + " encoding=" + "\"" + "UTF-8" + "\"" + "?>";
		   xmlStr += "\n" + "<AddJuryStudentSupervisor>" + "\n";
		   xmlStr += "<student>" + name + "</student>";
		   xmlStr += "<thesis_title>" + title + "</thesis_title>";
		   xmlStr += "<supervisor>" + supervisor + "</supervisor>";
		   //xmlStr += "<examiner1>" + examiner1 + "</examiner1>";
		   //xmlStr += "<examiner2>" + examiner2 + "</examiner2>";
		   //xmlStr += "<president>" + president + "</president>";
		   //xmlStr += "<secretary>" + secretary + "</secretary>";
		   //xmlStr += "<member>" + member + "</member>";
		   xmlStr += "<session>" + sessionID + "</session>";
		   xmlStr += "</AddJuryStudentSupervisor>";
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
	document.getElementById("thesis_title").value = "";
}

function consolidateJuriesOfDepartments(){
	if(xmlhttp){
		//alert("Add teacher");

		
		var xmlStr = "<?xml version=" + "\"" + "1.0" + "\"" + " encoding=" + "\"" + "UTF-8" + "\"" + "?>";

	   	xmlStr += "<ConsolidateJuriesOfDepartments>\n";
		
		xmlStr += "</ConsolidateJuriesOfDepartments>\n";
	   	//alert(xmlStr);
	    //xmlhttp.open("POST","TeacherManager",true); //getname will be the servlet name
	    xmlhttp.open("POST","DefenseScheduleManager",true); //getname will be the servlet name
	    xmlhttp.onreadystatechange  = handleConsolidateOfJuriesDepartments;
	    //xmlhttp.onreadystatechange  = handleSubmitPoint;
	    xmlhttp.overrideMimeType('text/xml');
	    xmlhttp.setRequestHeader('Content-Type', 'text/xml');
	    xmlhttp.send(xmlStr); //Posting to Servlet

	    //xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	    //xmlhttp.send("xml="+xml); //Posting to Servlet
	}
		
}

function handleConsolidateOfJuriesDepartments(){
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
			
			//var tablecontents = "";
			//tablecontents += "<table class=" + '"' + "table table-bordered table-condensed" + '"' + "id=" + '"' + "userTable" + '"' + ">";
			//tablecontents += "<thread>";
			//tablecontents += "<th>StudentID</th>";
			//tablecontents += "<th>StudentName</th>";
			//tablecontents += "<th>ThesisTitle</th>";
			//tablecontents += "<th>Examiner1</th>";
			//tablecontents += "<th>Examiner2</th>";
			//tablecontents += "<th>President</th>";
			//tablecontents += "<th>Secretary</th>";
			//tablecontents += "<th>Member</th>";
			//tablecontents += "</thread>";
			//tablecontents += "<tbody>";
			for(i = 0; i < jr.length; i++){
				var studentID = jr[i].getElementsByTagName("StudentID")[0].firstChild.nodeValue;
				var studentName = jr[i].getElementsByTagName("StudentName")[0].firstChild.nodeValue;
				var thesis_title = decodeURIComponent(jr[i].getElementsByTagName("ThesisTitle")[0].firstChild.nodeValue);
				var supervisorName = decodeURIComponent(jr[i].getElementsByTagName("SupervisorName")[0].firstChild.nodeValue);
				var supervisorId = jr[i].getElementsByTagName("SupervisorID")[0].firstChild.nodeValue;
				var examiner1Name = decodeURIComponent(jr[i].getElementsByTagName("Examiner1Name")[0].firstChild.nodeValue);
				var examiner1Id = jr[i].getElementsByTagName("Examiner1ID")[0].firstChild.nodeValue;
				var examiner2Name = decodeURIComponent(jr[i].getElementsByTagName("Examiner2Name")[0].firstChild.nodeValue);
				var examiner2Id = jr[i].getElementsByTagName("Examiner2ID")[0].firstChild.nodeValue;
				var presidentName = decodeURIComponent(jr[i].getElementsByTagName("PresidentName")[0].firstChild.nodeValue);
				var presidentId = jr[i].getElementsByTagName("PresidentID")[0].firstChild.nodeValue;
				var secretaryName = decodeURIComponent(jr[i].getElementsByTagName("SecretaryName")[0].firstChild.nodeValue);
				var secretaryId = jr[i].getElementsByTagName("SecretaryID")[0].firstChild.nodeValue;
				var memberName = decodeURIComponent(jr[i].getElementsByTagName("MemberName")[0].firstChild.nodeValue);
				var memberId = jr[i].getElementsByTagName("MemberID")[0].firstChild.nodeValue;
				var slot = jr[i].getElementsByTagName("Slot")[0].firstChild.nodeValue;
				var room = jr[i].getElementsByTagName("Room")[0].firstChild.nodeValue;
				var conflict = jr[i].getElementsByTagName("Conflict")[0].firstChild.nodeValue;
				
				//tablecontents += "<tr id='tr" + studentID + "'>";
				//tablecontents += "<td>" + studentID + "</td>" + "<td>" + 
				//studentName + "</td>" + "<td>" + thesis_title + "</td>" + "<td>" + examiner1Name + "</td>" + 
				//"<td>" + examiner2Name + "</td>" + "<td>" + presidentName + "</td>" + "<td>" + secretaryName + "</td>" + 
				//"<td>" + memberName + "</td>";
				//tablecontents += "<td><a href=" + '"' + "EditProfessor.jsp?name=" +  "\">Edit</td>";
				//tablecontents += "<td><input type=" + '"' + "button" + '"' + " value=" + '"' + "Delete" + '"' + " onclick=" + '"' + "deleteProfessor()" + '"' + "></td>";
				//tablecontents += "<td><a href=" + '"' + "DeleteProfessor.jsp?id=" + studentID + "\">Delete</td>";
				//tablecontents += "</tr>";
				
				var color = 'black';
				if(conflict == 1) 
					color = 'red';
				
				//addRow(studentID,studentName,thesis_title,examiner1Name,examiner2Name,presidentName,secretaryName,memberName,slot,room,color);
				addRow(studentID,studentName,thesis_title,supervisorId, supervisorName, examiner1Id,examiner1Name,examiner2Id,examiner2Name,
						presidentId,presidentName,secretaryId,secretaryName,memberId,memberName,slot,room,color);
			}
			//tablecontents += "</tbody>";
			//tablecontents += "</table>";
			//document.getElementById("jury-table").innerHTML = tablecontents;
		}
	}

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
		   	var supervisor = document.getElementById("supervisor-" + i).value;
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
		   	xmlStr += "<supervisor>" + supervisor + "</supervisor>";
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
			
			//var tablecontents = "";
			//tablecontents += "<table class=" + '"' + "table table-bordered table-condensed" + '"' + "id=" + '"' + "userTable" + '"' + ">";
			//tablecontents += "<thread>";
			//tablecontents += "<th>StudentID</th>";
			//tablecontents += "<th>StudentName</th>";
			//tablecontents += "<th>ThesisTitle</th>";
			//tablecontents += "<th>Examiner1</th>";
			//tablecontents += "<th>Examiner2</th>";
			//tablecontents += "<th>President</th>";
			//tablecontents += "<th>Secretary</th>";
			//tablecontents += "<th>Member</th>";
			//tablecontents += "</thread>";
			//tablecontents += "<tbody>";
			for(i = 0; i < jr.length; i++){
				var studentID = jr[i].getElementsByTagName("StudentID")[0].firstChild.nodeValue;
				var studentName = jr[i].getElementsByTagName("StudentName")[0].firstChild.nodeValue;
				var thesis_title = jr[i].getElementsByTagName("ThesisTitle")[0].firstChild.nodeValue;
				var supervisorName = decodeURIComponent(jr[i].getElementsByTagName("SupervisorName")[0].firstChild.nodeValue);
				var supervisorId = jr[i].getElementsByTagName("SupervisorID")[0].firstChild.nodeValue;
				var examiner1Name = decodeURIComponent(jr[i].getElementsByTagName("Examiner1Name")[0].firstChild.nodeValue);
				var examiner1Id = jr[i].getElementsByTagName("Examiner1ID")[0].firstChild.nodeValue;
				var examiner2Name = decodeURIComponent(jr[i].getElementsByTagName("Examiner2Name")[0].firstChild.nodeValue);
				var examiner2Id = jr[i].getElementsByTagName("Examiner2ID")[0].firstChild.nodeValue;
				var presidentName = decodeURIComponent(jr[i].getElementsByTagName("PresidentName")[0].firstChild.nodeValue);
				var presidentId = jr[i].getElementsByTagName("PresidentID")[0].firstChild.nodeValue;
				var secretaryName = decodeURIComponent(jr[i].getElementsByTagName("SecretaryName")[0].firstChild.nodeValue);
				var secretaryId = jr[i].getElementsByTagName("SecretaryID")[0].firstChild.nodeValue;
				var memberName = decodeURIComponent(jr[i].getElementsByTagName("MemberName")[0].firstChild.nodeValue);
				var memberId = jr[i].getElementsByTagName("MemberID")[0].firstChild.nodeValue;
				var slot = jr[i].getElementsByTagName("Slot")[0].firstChild.nodeValue;
				var room = jr[i].getElementsByTagName("Room")[0].firstChild.nodeValue;
				var conflict = jr[i].getElementsByTagName("Conflict")[0].firstChild.nodeValue;
				
				
				//tablecontents += "<tr id='tr" + studentID + "'>";
				//tablecontents += "<td>" + studentID + "</td>" + "<td>" + 
				//studentName + "</td>" + "<td>" + thesis_title + "</td>" + "<td>" + examiner1Name + "</td>" + 
				//"<td>" + examiner2Name + "</td>" + "<td>" + presidentName + "</td>" + "<td>" + secretaryName + "</td>" + 
				//"<td>" + memberName + "</td>";
				//tablecontents += "<td><a href=" + '"' + "EditProfessor.jsp?name=" +  "\">Edit</td>";
				//tablecontents += "<td><input type=" + '"' + "button" + '"' + " value=" + '"' + "Delete" + '"' + " onclick=" + '"' + "deleteProfessor()" + '"' + "></td>";
				//tablecontents += "<td><a href=" + '"' + "DeleteProfessor.jsp?id=" + studentID + "\">Delete</td>";
				//tablecontents += "</tr>";
				
				var color = 'black';
				if(conflict == 1) 
					color = 'red';
				
				//addRow(studentID,studentName,thesis_title,examiner1Name,examiner2Name,presidentName,secretaryName,memberName,slot,room,color);
				addRow(studentID,studentName,thesis_title,supervisorId,supervisorName,examiner1Id,examiner1Name,examiner2Id,examiner2Name,
						presidentId,presidentName,secretaryId,secretaryName,memberId,memberName,slot,room,color);
			}
			//tablecontents += "</tbody>";
			//tablecontents += "</table>";
			//document.getElementById("jury-table").innerHTML = tablecontents;
		}
	}
}

function suggestSlotRoom(studentID){
	//alert("suggest for " + studentID);
	if(xmlhttp){
		//alert("Add teacher");
		var table = document.getElementById("list-jury");
		var sz = table.rows.length;
		
		var xmlStr = "<?xml version=" + "\"" + "1.0" + "\"" + " encoding=" + "\"" + "UTF-8" + "\"" + "?>";

	   	xmlStr += "<SuggestSlotRoom>\n";
	   	xmlStr += "<selected-studentID>" + studentID + "</selected-studentID>";
	   	//xmlStr += "<Jury>";
		for(i = 1; i < sz; i++){
			var id = document.getElementById("studentID-" + i).value;
			//alert("id = " + id);
		   	var name = document.getElementById("studentName-" + i).value;
		   	var title = document.getElementById("thesis-title-" + i).value;
		   	var supervisor = document.getElementById("supervisor-" + i).value;
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
		   	xmlStr += "<supervisor>" + supervisor + "</supervisor>";
		   	xmlStr += "<examiner1>" + examiner1 + "</examiner1>";
		   	xmlStr += "<examiner2>" + examiner2 + "</examiner2>";
		   	xmlStr += "<president>" + president + "</president>";
		   	xmlStr += "<secretary>" + secretary + "</secretary>";
		   	xmlStr += "<member>" + member + "</member>";
		   	xmlStr += "<slot>" + slot + "</slot>";
		   	xmlStr += "<room>" + room + "</room>";
		   	xmlStr += "</Jury>";
		}
		//xmlStr += "</Jury>";
		xmlStr += "</SuggestSlotRoom>\n";
	   	//alert(xmlStr);
	    //xmlhttp.open("POST","TeacherManager",true); //getname will be the servlet name
	    xmlhttp.open("POST","DefenseScheduleManager",true); //getname will be the servlet name
	    xmlhttp.onreadystatechange  = handleSuggestSlotRoom;
	    //xmlhttp.onreadystatechange  = handleSubmitPoint;
	    xmlhttp.overrideMimeType('text/xml');
	    xmlhttp.setRequestHeader('Content-Type', 'text/xml');
	    xmlhttp.send(xmlStr); //Posting to Servlet

	    //xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	    //xmlhttp.send("xml="+xml); //Posting to Servlet
	}
	
}

function handleSuggestSlotRoom(){
	if(xmlhttp.readyState == 4){
		if(xmlhttp.status == 200){
			var xmlData = xmlhttp.responseXML;
	
			var candidates = xmlData.getElementsByTagName("SlotRoom");
			var suggest = "Slot\t\tRoom\n";
			for(i = 0; i < candidates.length; i++){
				var slotroom = candidates[i].firstChild.nodeValue;
				suggest += slotroom + "\n";
			}
			alert(suggest);
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

	   	xmlStr += "<SearchSolutionJuryMembers>\n";
		for(i = 1; i < sz; i++){
			var id = document.getElementById("studentID-" + i).value;
			//alert("id = " + id);
		   	var name = document.getElementById("studentName-" + i).value;
		   	var title = document.getElementById("thesis-title-" + i).value;
		   	var supervisor = document.getElementById("supervisor-" + i).value;
		   	var examiner1 = document.getElementById("examiner1-" + i).value;
		   	var examiner2 = document.getElementById("examiner2-" + i).value;
		   	var president = document.getElementById("president-" + i).value;
		   	var secretary = document.getElementById("secretary-" + i).value;
		   	var member = document.getElementById("member-" + i).value;
		   	var slot = document.getElementById("slot-" + i).value;
		   	//var room = document.getElementById("room-" + i).value;
		   
		   	//name = Utf8Coder.encode(name);
		   	xmlStr += "\n" + "<Jury>" + "\n";
		   	xmlStr += "<studentID>" + id + "</studentID>";
		   	xmlStr += "<studentName>" + name + "</studentName>";
		   	xmlStr += "<thesis_title>" + title + "</thesis_title>";
		   	xmlStr += "<supervisor>" + supervisor + "</supervisor>";
		   	xmlStr += "<examiner1>" + examiner1 + "</examiner1>";
		   	xmlStr += "<examiner2>" + examiner2 + "</examiner2>";
		   	xmlStr += "<president>" + president + "</president>";
		   	xmlStr += "<secretary>" + secretary + "</secretary>";
		   	xmlStr += "<member>" + member + "</member>";
		   	xmlStr += "<slot>" + slot + "</slot>";
		   	//xmlStr += "<room>" + room + "</room>";
		   	
		   	xmlStr += "</Jury>";
		}
		
		//xmlStr += "<slots>" + document.getElementById("slots").value + "</slots>";
		//xmlStr += "<rooms>" + document.getElementById("rooms").value + "</rooms>";
		xmlStr += "<time>" + document.getElementById("time-limit").value + "</time>";
		xmlStr += "<algorithm>" + document.getElementById("algorithm").value + "</algorithm>";
		xmlStr += "<jury-members>";
	   	//xmlStr += document.getElementById("jury-members-xml").value;
	   	var selectedMembers = document.getElementById("selected-members");
	   	for(i = 0; i < selectedMembers.options.length; i++){
	   		var memberID = selectedMembers.options[i].value;
	   		xmlStr += '<jury-member>' + memberID + '</jury-member>' + "\n";
	   	}
	   	xmlStr += "</jury-members>";
		xmlStr += "</SearchSolutionJuryMembers>\n";
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
			
			var sol = xmlData.getElementsByTagName("solution")[0].firstChild.nodeValue;
			if(sol === "false"){
				
				var msg = xmlData.getElementsByTagName("error")[0].firstChild.nodeValue;
				alert(msg);
			
				return;
			}
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
			
			//var tablecontents = "";
			//tablecontents += "<table class=" + '"' + "table table-bordered table-condensed" + '"' + "id=" + '"' + "userTable" + '"' + ">";
			//tablecontents += "<thread>";
			//tablecontents += "<th>StudentID</th>";
			//tablecontents += "<th>StudentName</th>";
			//tablecontents += "<th>ThesisTitle</th>";
			//tablecontents += "<th>Examiner1</th>";
			//tablecontents += "<th>Examiner2</th>";
			//tablecontents += "<th>President</th>";
			//tablecontents += "<th>Secretary</th>";
			//tablecontents += "<th>Member</th>";
			//tablecontents += "</thread>";
			//tablecontents += "<tbody>";
			for(i = 0; i < jr.length; i++){
				var studentID = jr[i].getElementsByTagName("StudentID")[0].firstChild.nodeValue;
				var studentName = jr[i].getElementsByTagName("StudentName")[0].firstChild.nodeValue;
				var thesis_title = jr[i].getElementsByTagName("ThesisTitle")[0].firstChild.nodeValue;
				var supervisorName = decodeURIComponent(jr[i].getElementsByTagName("SupervisorName")[0].firstChild.nodeValue);
				var supervisorId = jr[i].getElementsByTagName("SupervisorID")[0].firstChild.nodeValue;
				var examiner1Name = decodeURIComponent(jr[i].getElementsByTagName("Examiner1Name")[0].firstChild.nodeValue);
				var examiner1Id = jr[i].getElementsByTagName("Examiner1ID")[0].firstChild.nodeValue;
				var examiner2Name = decodeURIComponent(jr[i].getElementsByTagName("Examiner2Name")[0].firstChild.nodeValue);
				var examiner2Id = jr[i].getElementsByTagName("Examiner2ID")[0].firstChild.nodeValue;
				var presidentName = decodeURIComponent(jr[i].getElementsByTagName("PresidentName")[0].firstChild.nodeValue);
				var presidentId = jr[i].getElementsByTagName("PresidentID")[0].firstChild.nodeValue;
				var secretaryName = decodeURIComponent(jr[i].getElementsByTagName("SecretaryName")[0].firstChild.nodeValue);
				var secretaryId = jr[i].getElementsByTagName("SecretaryID")[0].firstChild.nodeValue;
				var memberName = decodeURIComponent(jr[i].getElementsByTagName("MemberName")[0].firstChild.nodeValue);
				var memberId = jr[i].getElementsByTagName("MemberID")[0].firstChild.nodeValue;
				var slot = jr[i].getElementsByTagName("Slot")[0].firstChild.nodeValue;
				var room = jr[i].getElementsByTagName("Room")[0].firstChild.nodeValue;
				var conflict = jr[i].getElementsByTagName("Conflict")[0].firstChild.nodeValue;
				
				//tablecontents += "<tr id='tr" + studentID + "'>";
				//tablecontents += "<td>" + studentID + "</td>" + "<td>" + 
				//studentName + "</td>" + "<td>" + thesis_title + "</td>" + "<td>" + examiner1Name + "</td>" + 
				//"<td>" + examiner2Name + "</td>" + "<td>" + presidentName + "</td>" + "<td>" + secretaryName + "</td>" + 
				//"<td>" + memberName + "</td>";
				//tablecontents += "<td><a href=" + '"' + "EditProfessor.jsp?name=" +  "\">Edit</td>";
				//tablecontents += "<td><input type=" + '"' + "button" + '"' + " value=" + '"' + "Delete" + '"' + " onclick=" + '"' + "deleteProfessor()" + '"' + "></td>";
				//tablecontents += "<td><a href=" + '"' + "DeleteProfessor.jsp?id=" + studentID + "\">Delete</td>";
				//tablecontents += "</tr>";
				
				var color = 'black';
				if(conflict == 1) 
					color = 'red';
				
				//addRow(studentID,studentName,thesis_title,examiner1Name,examiner2Name,presidentName,secretaryName,memberName,slot,room,color);
				addRow(studentID,studentName,thesis_title,supervisorId, supervisorName, examiner1Id,examiner1Name,examiner2Id,examiner2Name,
						presidentId,presidentName,secretaryId,secretaryName,memberId,memberName,slot,room,color);
			}
			//tablecontents += "</tbody>";
			//tablecontents += "</table>";
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
		   	var name = encodeURIComponent(document.getElementById("studentName-" + i).value);
		   	var title = encodeURIComponent(document.getElementById("thesis-title-" + i).value);
		   	var supervisor = document.getElementById("supervisor-" + i).value;
		   	var examiner1 = document.getElementById("examiner1-" + i).value;
		   	var examiner2 = document.getElementById("examiner2-" + i).value;
		   	var president = document.getElementById("president-" + i).value;
		   	var secretary = document.getElementById("secretary-" + i).value;
		   	var member = document.getElementById("member-" + i).value;
		   	var slot = document.getElementById("slot-" + i).value;
		   	//var room = document.getElementById("room-" + i).value;
		   	var slotDescription = document.getElementById("slot-desciption-" + i).value;
		   
		   	//name = Utf8Coder.encode(name);
		   	xmlStr += "\n" + "<Jury>" + "\n";
		   	xmlStr += "<studentID>" + id + "</studentID>";
		   	xmlStr += "<studentName>" + name + "</studentName>";
		   	xmlStr += "<thesis_title>" + title + "</thesis_title>";
		   	xmlStr += "<supervisor>" + supervisor + "</supervisor>";
		   	xmlStr += "<examiner1>" + examiner1 + "</examiner1>";
		   	xmlStr += "<examiner2>" + examiner2 + "</examiner2>";
		   	xmlStr += "<president>" + president + "</president>";
		   	xmlStr += "<secretary>" + secretary + "</secretary>";
		   	xmlStr += "<member>" + member + "</member>";
		   	xmlStr += "<slot>" + slot + "</slot>";
		   	xmlStr += "<slot-description>" + slotDescription + "</slot-description>";
		   	//xmlStr += "<room>" + room + "</room>";
		   	xmlStr += "</Jury>";
		}
		
		//xmlStr += "<slots>" + document.getElementById("slots").value + "</slots>";
		//xmlStr += "<rooms>" + document.getElementById("rooms").value + "</rooms>";
		
		xmlStr += "<session-id>" + document.getElementById("select-defense-session").value + "</session-id>";
		xmlStr += "<room-name>" + document.getElementById("room-name").value + "</room-name>";
		
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