<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Chinh sua thong tin hoi dong bao ve da duoc sap xep</title>
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
<h1>Chinh sua thong tin hoi dong bao ve da duoc sap xep</h1>

<form id="teacher-info" action="#">
<ul>
<div style="border:1px solid blue" style="visibility:hidden">
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
	Phong
</td>

</tr>

</table>
<tr>
<!--  Chon GV vao hoi dong-->
<select id="jury-members" style="visibility:hidden">
</select>
<input type="button" id="add-jury-member" value="Them vao hoi dong" style="visibility:hidden" onclick="addMemberToJury()">
<select id="selected-members" style="visibility:hidden"></select>
<!--  So hoi dong--> 
<input type="text" id="nbr-juries" name="nbr-juries" value="0" style="visibility:hidden">
</tr>

<input type="button" value="Reset" onclick="resetSchedule()" style="visibility:hidden">
<input type="button" value="Hop nhat cac hoi dong bo mon" onclick="consolidateJuriesOfDepartments()" style="visibility:hidden">
<br>
<input type="button" value="Xep tu dong" onclick="searchSolution()" style="visibility:hidden">
<!--  Thoi gian chay:--> 
<input type="text" name="time-limit" id="time-limit" style="width:50px; visibility:hidden" value="10">
<!--  Thuat toan:--> 
<select name="algorithm" id="algorithm" style="visibility:hidden">
<option value="BackTrackSearch">BackTrackSearch</option>
<option value="TabuSearch">TabuSearch</option>

</select>


<input type="button" value="Kiem tra tinh hop le" onclick="checkConsistency()">
</ul>
</form>
<table id="list-slots">
<tr>
<td>Kip</td><td>Thoi gian</td>
</tr>
</table>
<table id="list-rooms">
<tr><td>Phong</td><td>Ten</td></tr>
</table>

<table>

<tr><td>
<input type="button" value="Luu lai" onclick="saveSchedule()">
<input type="text" id="room-name" style="width:50px; visibility:hidden">
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
var rooms_id = new Array();
var rooms_name = new Array();
var session_id = new Array();
var session_description = new Array();
var roomsArr = new Array();
var slotsArr = new Array();

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
		   xmlStr += "\n" + "<LoadTeachersStudentsDepartmentsRoomsList>" + "\n" +  
		   "</LoadTeachersStudentsDepartmentsRoomsList>";
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
			var rooms = xmlData.getElementsByTagName("room");
			for(i = 0; i < rooms.length; i++){
				var id = rooms[i].getElementsByTagName("id")[0].firstChild.nodeValue;
				var name = rooms[i].getElementsByTagName("name")[0].firstChild.nodeValue;
				name = decodeURIComponent(name);
				rooms_id.push(id);
				rooms_name.push(name);
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
			for(idx in rooms_id){
				selectdepartment.options[selectdepartment.options.length] = new Option(rooms_name[idx],rooms_name[idx]);
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
	var selected_members = document.getElementById("selected-members");
	selected_members.options.length = 0;
}
/*
function suggestSlotRoom(studentID){
	alert("suggest " + studentID);
}
*/

function addRowSlot(tbl, id, name){
	var sz = tbl.rows.length;
	var row = tbl.insertRow(sz);
	
	var cell = row.insertCell(0);
	var e = document.createElement("input");
	e.type = "text";
	e.setAttribute("name","slot-info-id-" + sz);
	e.setAttribute("id","slot-info-id-" + sz);
	e.setAttribute("value",id);
	e.style.width="50px";	
	cell.appendChild(e);

	cell = row.insertCell(1);
	e = document.createElement("input");
	e.type = "text";
	e.setAttribute("name","slot-info-description-" + sz);
	e.setAttribute("id","slot-info-description-" + sz);
	e.setAttribute("value",name);
	e.style.width="100px";	
	cell.appendChild(e);
}
function addRowRoom(tbl, id, name){
	var sz = tbl.rows.length;
	var row = tbl.insertRow(sz);
	
	var cell = row.insertCell(0);
	var e = document.createElement("input");
	e.type = "text";
	e.setAttribute("name","room-info-id-" + sz);
	e.setAttribute("id","room-info-id-" + sz);
	e.setAttribute("value",id);
	e.style.width="50px";	
	cell.appendChild(e);

	cell = row.insertCell(1);
	e = document.createElement("input");
	e.type = "text";
	e.setAttribute("name","room-info-name-" + sz);
	e.setAttribute("id","room-info-name-" + sz);
	e.setAttribute("value",name);
	e.style.width="100px";	
	cell.appendChild(e);
}

function addRow(studentID, studentName, thesis_title, supervisorId, supervisorName, examiner1Id, examiner1Name, examiner2Id, 
		examiner2Name, 
		presidentId, presidentName, secretaryId, secretaryName, memberId, memberName, slot, room, roomsArr, color){
	
	
	var table = document.getElementById("list-jury");
	var sz = table.rows.length;
	var row = table.insertRow(sz);
	
	//alert("sz = " + sz + " id = " + studentID + " name = " + studentName + " thesis_title = " + thesis_title + " examiner1 = " + examiner1 + 
			//" examiner2 = " + examiner2 + " president = " + president + " secretary = " + secretary + " member = " + member + " slot = " + 
			//slot + " room = " + room + " color = " + color);
	
	//alert("room = " + room);
	
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
	var e10 = document.createElement("select");
	e10.setAttribute("name","room-" + sz);	
	e10.setAttribute("id","room-" + sz);
	e10.style.width="50px";
	
	var optRoom;
	//for(j = 0; j <= 200; j++){
	for(j = 0; j < roomsArr.length; j++){	
		optRoom = document.createElement("option");
		optRoom.setAttribute("value",roomsArr[j].id);
		optRoom.innerHTML = roomsArr[j].id;
		optRoom.style.color='red';
		if(roomsArr[j].id == room)
			optRoom.selected = true;
		e10.appendChild(optRoom);
	}
	cell10.appendChild(e10);

	
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
	   xmlStr += "\n" + "<LoadJuryForRepair>" + "\n";
	   xmlStr += "<SortBy>" + document.getElementById("sortby").value + "</SortBy>\n";
	   xmlStr += "<FilterBy>" + document.getElementById("filter-by").value + "</FilterBy>\n";
	   xmlStr += "</LoadJuryForRepair>";
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
			
			var tbl_rooms = document.getElementById("list-rooms");
			clearTable(tbl_rooms);
			roomsArr = new Array();
			var roomsNode = xmlData.getElementsByTagName("Rooms")[0].getElementsByTagName("room");
			for(i = 0; i < roomsNode.length; i++){
				var r_id = roomsNode[i].getElementsByTagName("id")[0].firstChild.nodeValue;
				var r_name = roomsNode[i].getElementsByTagName("name")[0].firstChild.nodeValue;
				var r = new Object();
				r.id = r_id;
				r.name = r_name;
				roomsArr.push(r);
				addRowRoom(tbl_rooms,r.id,r.name);
			}
			
			var tbl_slots = document.getElementById("list-slots");
			clearTable(tbl_slots);
			
			slotsArr = new Array();
			var slotsNode = xmlData.getElementsByTagName("Slots")[0].getElementsByTagName("slot");
			for(i = 0; i < slotsNode.length; i++){
				var sl_id = slotsNode[i].getElementsByTagName("id")[0].firstChild.nodeValue;
				var sl_name = slotsNode[i].getElementsByTagName("name")[0].firstChild.nodeValue;
				var sl = new Object();
				sl.id = sl_id;
				sl.name = sl_name;
				slotsArr.push(sl);
				addRowSlot(tbl_slots,sl.id,sl.name);
			}
			
			var jr = xmlData.getElementsByTagName("Jury");
			var table = document.getElementById("list-jury");
			clearTable(table);
			
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
				//var slotDescription = jr[i].getElementsByTagName("Slot-Description")[0].firstChild.nodeValue;
				var room = jr[i].getElementsByTagName("Room")[0].firstChild.nodeValue;
				
				addRow(studentID,studentName,thesis_title,supervisorId, supervisorName, examiner1Id,examiner1Name,examiner2Id,examiner2Name,
						presidentId,presidentName,secretaryId,secretaryName,memberId,memberName,slot,room,roomsArr,'black');
			}
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
						presidentId,presidentName,secretaryId,secretaryName,memberId,memberName,slot,room,roomsArr,color);
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

	   	xmlStr += "<SearchSolutionJuriesMembers>\n";
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
		   	//xmlStr += "<slot>" + slot + "</slot>";
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
	   	xmlStr += "<nb-juries>" + document.getElementById("nbr-juries").value + "</nb-juries>";
		xmlStr += "</SearchSolutionJuriesMembers>\n";
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
			
			var slots = xmlData.getElementsByTagName("slots");
			var table_slots = document.getElementById("list-slots");
			clearTable(table_slots);
			for(i = 0; i < slots.length; i++){
				var slotId = slots[i].firstChild.nodeValue;
				addRowSlot(table_slots,slotId);
			}
			
			var rooms = xmlData.getElementsByTagName("rooms");
			var table_rooms = document.getElementById("list-rooms");
			clearTable(table_rooms);
			for(i = 0; i < rooms.length; i++){
				var roomId = rooms[i].firstChild.nodeValue;
				addRowRoom(table_rooms,roomId);
			}

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

	   	xmlStr += "<SaveScheduleRoomsSlots>\n";
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
		   	xmlStr += "</Jury>\n";
		}
		
		//xmlStr += "<slots>" + document.getElementById("slots").value + "</slots>";
		//xmlStr += "<rooms>" + document.getElementById("rooms").value + "</rooms>";
		var table_slots = document.getElementById("list-slots");
		var sz_slots = table_slots.rows.length;
		for(i = 1; i < sz_slots; i++){
			var slotID = document.getElementById("slot-info-id-" + i).value;
			var slotDescription = document.getElementById("slot-info-description-" + i).value;
		
			xmlStr += "<slot-info>";
			xmlStr += "<id>" + slotID + "</id>";
			xmlStr += "<description>" + slotDescription + "</description>";
			xmlStr += "</slot-info>\n";
		}
		
		var table_rooms = document.getElementById("list-rooms");
		var sz_rooms = table_rooms.rows.length;
		for(i = 1; i < sz_rooms; i++){
			var roomID = document.getElementById("room-info-id-" + i).value;
			var roomDescription = document.getElementById("room-info-name-" + i).value;
		
			xmlStr += "<room-info>";
			xmlStr += "<id>" + roomID + "</id>";
			xmlStr += "<name>" + roomDescription + "</name>";
			xmlStr += "</room-info>\n";
		}
		
		xmlStr += "<session-id>" + document.getElementById("select-defense-session").value + "</session-id>";
		
		xmlStr += "</SaveScheduleRoomsSlots>\n";
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

function saveScheduleOld(){
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