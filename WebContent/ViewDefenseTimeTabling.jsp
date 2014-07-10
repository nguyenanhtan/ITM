<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Hien thi thong tin ve xep lich cac hoi dong bao ve</title>
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

<h1>Hien thi thong tin ve xep lich cac hoi dong bao ve</h1>

<input type="button" value="View" onclick="loadJuryList()">
Filtered By <select id="filter-by">
</select>
Ordered By <select id="order-by">
<option value="Room-Slot">Phong-Kip</option>
</select>

<form id="teacher-info" action="#">
<ul>
<table id="list-jury">
<tr>
<td>
	ID
</td>
<td>
	Ho va ten
</td>
<td>
	Ten de tai
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
</ul>
</form>

<p id="test"></p>
<p id="juryOfProfessor"></p>
<p id="slotsOfProfessor"></p>

<form action="download.jsp" method="get">
Loc theo<select id="filter-for-excel" name="filter-for-excel"></select>
Sap xep theo<select id="order-for-excel" name="order-for-excel">
<option value="Room-Slot">Phong-Kip</option>
</select>
<input type="submit" value="Xuat ra file Excel">
</form>
<a href="home.jsp">Back to Home</a>

<script type="text/javascript">
var professor_id = new Array();
var professor_name = new Array();
var student_id = new Array();
var student_name = new Array();

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

function init(){
	loadRoomsDepartments();
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
	var e8 = document.createElement("input");
	e8.type = "text";
	e8.setAttribute("name","slot-" + sz);
	e8.setAttribute("id","slot-" + sz);
	e8.setAttribute("value",slot);
	e8.style.color = color;
	e8.style.width = "100px";
	cell8.appendChild(e8);

	var cell9 = row.insertCell(9);
	var e9 = document.createElement("input");
	e9.type = "text";
	e9.setAttribute("name","room-" + sz);
	e9.setAttribute("id","room-" + sz);
	e9.setAttribute("value",room);
	e9.style.color = color;
	e9.style.width = "100px";
	cell9.appendChild(e9);
	
}	

function sendMail(){
	if(xmlhttp){
		var xmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		xmlStr += "<SendMail></SendMail>";
		xmlhttp.open("POST","DefenseScheduleManager",true);
		xmlhttp.onreadystatechange  = handleSendMail;
		xmlhttp.overrideMimeType('text/xml');
	    xmlhttp.setRequestHeader('Content-Type', 'text/xml; charset=utf-8');
	    xmlhttp.send(xmlStr);
	}
}
function handleSendMail(){
	//alert("handle send mail");
}

function loadRoomsDepartments(){
	//alert("loadJuryList");
	if(xmlhttp){
		//alert("loadJuryList");
	   var xmlStr = "<?xml version=" + "\"" + "1.0" + "\"" + " encoding=" + "\"" + "UTF-8" + "\"" + "?>";
	   xmlStr += "\n" + "<load-rooms-departments>" + "\n";
	   xmlStr += "</load-rooms-departments>";
	   //alert(xmlStr);
	    //xmlhttp.open("POST","TeacherManager",true); //getname will be the servlet name
	    xmlhttp.open("POST","DefenseScheduleManager",true); //getname will be the servlet name
	    xmlhttp.onreadystatechange  = handleLoadRoomsDepartments;
	    //xmlhttp.onreadystatechange  = handleSubmitPoint;
	    xmlhttp.overrideMimeType('text/xml');
	    xmlhttp.setRequestHeader('Content-Type', 'text/xml; charset=utf-8');
	    xmlhttp.send(xmlStr); //Posting to Servlet

	    //xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	    //xmlhttp.send("xml="+xml); //Posting to Servlet
	}
	
}	
function handleLoadRoomsDepartments(){
	if(xmlhttp.readyState == 4){
		if(xmlhttp.status == 200){
			var xmlData = xmlhttp.responseXML;
			var dept = xmlData.getElementsByTagName("department");
			var selectDept = document.getElementById("filter-by");
			var selectFilterExcel = document.getElementById("filter-for-excel");
			
			selectDept.options[selectDept.options.length] = new Option("All","All");
			selectFilterExcel.options[selectFilterExcel.options.length] = new Option("All","All");
			
			for(i = 0; i < dept.length; i++){
				var deptName = dept[i].firstChild.nodeValue;
				selectDept.options[selectDept.options.length] = new Option(deptName,deptName);
				selectFilterExcel.options[selectFilterExcel.options.length] = new Option(deptName,deptName);
			}
			
			var rooms = xmlData.getElementsByTagName("room");
			for(i = 0; i < rooms.length; i++){
				var r_id = rooms[i].getElementsByTagName("id")[0].firstChild.nodeValue;
				var r_name = rooms[i].getElementsByTagName("name")[0].firstChild.nodeValue;
				selectDept.options[selectDept.options.length] = new Option(r_name,r_name);
				selectFilterExcel.options[selectFilterExcel.options.length] = new Option(r_name,r_name);
			}
		}
	}
}
function loadJuryList(){
	//alert("loadJuryList");
	if(xmlhttp){
		//alert("loadJuryList");
	   var xmlStr = "<?xml version=" + "\"" + "1.0" + "\"" + " encoding=" + "\"" + "UTF-8" + "\"" + "?>";
	   xmlStr += "\n" + "<ViewJury>" + "\n";
	   xmlStr += "<SortBy>" + document.getElementById("order-by").value + "</SortBy>\n";
	   xmlStr += "<FilterBy>" + document.getElementById("filter-by").value + "</FilterBy>\n";
	   xmlStr += "</ViewJury>";
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
			//alert(jr.length);
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
				

				addRow(studentID,studentName,thesis_title,examiner1Name,examiner2Name,presidentName,secretaryName,memberName,slot,room,'black');
			}
			
			
			var jrp = xmlData.getElementsByTagName("Teachers")[0].getElementsByTagName("Teacher");
			
			var tablecontents = "";
			//tablecontents += "<table class=" + '"' + "table table-bordered table-condensed" + '"' + "id=" + '"' + "userTable" + '"' + ">";
			tablecontents += "<table border=1>";
			//tablecontents += "<thread>";
			tablecontents += "<th>Ma hoc vien</th>";
			tablecontents += "<th>Ho ten hoc vien</th>";
			tablecontents += "<th>Ten de tai</th>";
			tablecontents += "<th>Phan bien 1</th>";
			tablecontents += "<th>Phan bien 2</th>";
			tablecontents += "<th>Chu tich</th>";
			tablecontents += "<th>Thu ky</th>";
			tablecontents += "<th>Uy vien</th>";
			tablecontents += "<th>Kip</th>";
			tablecontents += "<th>Phong</th>";
			//tablecontents += "</thread>";
			tablecontents += "<tbody>";
			
			
			for(i = 0; i < jrp.length; i++){
				var teacherName = decodeURIComponent(jrp[i].getElementsByTagName("Name")[0].firstChild.nodeValue);
				var jr = jrp[i].getElementsByTagName("JuryOfProfessor");
				tablecontents += "<tr id=\'" + teacherName + "\'>" + "<td>" + teacherName + "</td>";
				for(j = 0; j < jr.length; j++){
					var studentID = jr[j].getElementsByTagName("StudentID")[0].firstChild.nodeValue;
					var studentName = decodeURIComponent(jr[j].getElementsByTagName("StudentName")[0].firstChild.nodeValue);
					var thesis_title = decodeURIComponent(jr[j].getElementsByTagName("ThesisTitle")[0].firstChild.nodeValue);
					var examiner1Name = decodeURIComponent(jr[j].getElementsByTagName("Examiner1Name")[0].firstChild.nodeValue);
					var examiner2Name = decodeURIComponent(jr[j].getElementsByTagName("Examiner2Name")[0].firstChild.nodeValue);
					var presidentName = decodeURIComponent(jr[j].getElementsByTagName("PresidentName")[0].firstChild.nodeValue);
					var secretaryName = decodeURIComponent(jr[j].getElementsByTagName("SecretaryName")[0].firstChild.nodeValue);
					var memberName = decodeURIComponent(jr[j].getElementsByTagName("MemberName")[0].firstChild.nodeValue);
					var slot = jr[j].getElementsByTagName("Slot")[0].firstChild.nodeValue;
					var room = jr[j].getElementsByTagName("Room")[0].firstChild.nodeValue;
					
					tablecontents += "<tr id='tr" + studentID + "'>";
					tablecontents += "<td>" + studentID + "</td>" + "<td>" + 
					studentName + "</td>" + "<td>" + thesis_title + "</td>" + "<td>" + examiner1Name + "</td>" + 
					"<td>" + examiner2Name + "</td>" + "<td>" + presidentName + "</td>" + "<td>" + secretaryName + "</td>" + 
					"<td>" + memberName + "</td>" + "<td>" + slot + "</td>" + "<td>" +room + "</td>";
					//tablecontents += "<td><a href=" + '"' + "EditProfessor.jsp?name=" +  "\">Edit</td>";
					//tablecontents += "<td><input type=" + '"' + "button" + '"' + " value=" + '"' + "Delete" + '"' + " onclick=" + '"' + "deleteProfessor()" + '"' + "></td>";
					//tablecontents += "<td><a href=" + '"' + "DeleteProfessor.jsp?id=" + studentID + "\">Delete</td>";
					tablecontents += "</tr>";
					
					//addRow(studentID,studentName,thesis_title,examiner1Name,examiner2Name,presidentName,secretaryName,memberName);
					//addRow(studentID,studentName,thesis_title,examiner1Name,examiner2Name,presidentName,secretaryName,memberName,slot,room,'black');
				}
			}
			
			tablecontents += "</tbody>";
			tablecontents += "</table>";
			document.getElementById("juryOfProfessor").innerHTML = tablecontents;
			
			
			var slotsOfProfessor = "";
			slotsOfProfessor += "<table border=1>";
			var slots_used = xmlData.getElementsByTagName("SlotsUsed")[0].getElementsByTagName("Slot");
			slotsOfProfessor += "<td>" + "Giang vien" + "</td>";
			for(i = 0; i < slots_used.length; i++){
				slotsOfProfessor += "<td>" + slots_used[i].firstChild.nodeValue + "</td>";
			}
			slotsOfProfessor += "<tr>" + "</tr>";
			slotsOfProfessor += "<tr>" + "</tr>";
			var slotP = xmlData.getElementsByTagName("SlotsProfessor")[0].getElementsByTagName("Teacher");
			for(i = 0; i < slotP.length; i++){
				slotsOfProfessor += "<tr>";
				var name = decodeURIComponent(slotP[i].getElementsByTagName("Name")[0].firstChild.nodeValue);
				slotsOfProfessor += "<td>" + name + "</td>";
				var rooms = slotP[i].getElementsByTagName("Room");
				for(r = 0; r < rooms.length; r++){
					slotsOfProfessor += "<td>" + rooms[r].firstChild.nodeValue + "</td>";
				}
				slotsOfProfessor += "</tr>";
			}
			slotsOfProfessor += "</table>";
			document.getElementById("slotsOfProfessor").innerHTML = slotsOfProfessor;
		}
	}
	
}

</script>

</body>
</html>