<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="utils.*" 
import="java.util.*"
import="DataEntity.*"
import="java.net.*"
%>    

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Update Jury</title>
</head>
<body onload="loadJury()">
<%
if(session.getAttribute("username") == null){
	response.sendRedirect("Login.jsp");
}else{
	String uid = session.getAttribute("username").toString();
	out.println("Hello " + uid);
}
%>
<a href=Logout.jsp>Logout</a>

<table id="jury">
<tr>
<td>Ma hoc vien</td><td><input type="text" id="student-id"></td>
</tr>
<tr>
<td>Ho va ten</td><td><input type="text" id="student"></td>
</tr>
<tr>
<td>Ten de tai</td><td><input type="text" id="thesis_title" style="width:500px"></td>
</tr>
<tr>
<td>GV huong dan</td><td><select id="select-supervisor"></select></td>
</tr>
<tr>
<td>Phan bien 1</td><td><select id="select-examiner1"></select></td>
</tr>
<tr>
<td>Phan bien 2</td><td><select id="select-examiner2"></select></td>
</tr>
<tr>
<td>Chu tich</td><td><select id="select-president"></select></td>
</tr>
<tr>
<td>Thu ky</td><td><select id="select-secretary"></select></td>
</tr>
<tr>
<td>Uy vien</td><td><select id="select-member"></select></td>
</tr>

</table>
<input type="button" value="Cap nhat" onclick="updateJury()">
<a href="ScheduleJuries.jsp">Back</a>
<script type="text/javascript">
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
 
var xmlhttp = new getXMLObject(); //xmlhttp holds the ajax object

var professor_id = new Array();
var professor_name = new Array();

function loadJury(){
	
	<%
	int studentID = Integer.valueOf(request.getParameter("studentID"));
	JuryInfo jr = Utility.getJury(studentID);
	
	Vector<Teacher> teachers = Utility.getTeachers();
	System.out.println("loadJury, get teachers.size = " + teachers.size() + ", student name = " + jr.getStudentName());
	%>
	var supervisorId = <%=jr.getSupervisorId()%>
	var examiner1Id = <%=jr.getExaminerId1()%>
	var examiner2Id = <%=jr.getExaminerId2()%>
	var presidentId = <%=jr.getPresidentId()%>
	var secretaryId = <%=jr.getSecretaryId()%>
	var memberId = <%=jr.getAdditionalMemberId()%>
	<%
	for(int i = 0; i < teachers.size(); i++){
		Teacher t = teachers.get(i);
		%>
		var p_id = <%=t.getID()%>;
		var p_name = "<%=t.getName()%>";
		p_name = decodeURIComponent(p_name);
		professor_id.push(p_id);
		professor_name.push(p_name);

		
		<%
	}
	%>
	var studentId = document.getElementById("student-id");
	studentId.value = <%=jr.getStudentID()%>;
	var studentName = document.getElementById("student");
	studentName.value = "<%=jr.getStudentName()%>";
	studentName.value = decodeURIComponent(studentName.value);
	document.getElementById("thesis_title").value = decodeURIComponent("<%=jr.getTitle()%>");
	var selectsupervisor = document.getElementById("select-supervisor");
	var selectpresident = document.getElementById("select-president");
	var selectsecretary = document.getElementById("select-secretary");
	var selectmember = document.getElementById("select-member");
	var selectexaminer1 = document.getElementById("select-examiner1");
	var selectexaminer2 = document.getElementById("select-examiner2");
	for(idx in professor_id){
		var opt_supervisor = new Option(professor_name[idx],professor_id[idx])
		selectsupervisor.options[selectsupervisor.options.length] = opt_supervisor;
		if(professor_id[idx] == supervisorId) opt_supervisor.selected = true;

		var opt_president = new Option(professor_name[idx],professor_id[idx])
		selectpresident.options[selectpresident.options.length] = opt_president;
		if(professor_id[idx] == presidentId) opt_president.selected = true;
		
		var opt_secretary = new Option(professor_name[idx],professor_id[idx]);
		selectsecretary.options[selectsecretary.options.length] = opt_secretary;
		if(professor_id[idx] == secretaryId) opt_secretary.selected = true;
		
		var opt_member = new Option(professor_name[idx],professor_id[idx]);
		selectmember.options[selectmember.options.length] = opt_member;
		if(professor_id[idx] == memberId) opt_member.selected = true;
		
		var opt_examiner1 = new Option(professor_name[idx],professor_id[idx]);
		selectexaminer1.options[selectexaminer1.options.length] = opt_examiner1;
		if(professor_id[idx] == examiner1Id) opt_examiner1.selected = true;
		
		var opt_examiner2 = new Option(professor_name[idx],professor_id[idx]);
		selectexaminer2.options[selectexaminer2.options.length] = opt_examiner2;
		if(professor_id[idx] == examiner2Id) opt_examiner2.selected = true;
	}
}

function updateJury(){
	
	if(xmlhttp){
		//alert("Add teacher");
	   var name = document.getElementById("student-id").value;
	   var title = encodeURIComponent(document.getElementById("thesis_title").value);
	   var supervisor = document.getElementById("select-supervisor").value;
	   var examiner1 = document.getElementById("select-examiner1").value;
	   var examiner2 = document.getElementById("select-examiner2").value;
	   var president = document.getElementById("select-president").value;
	   var secretary = document.getElementById("select-secretary").value;
	   var member = document.getElementById("select-member").value;
	   
	   //name = Utf8Coder.encode(name);
	   var xmlStr = "<?xml version=" + "\"" + "1.0" + "\"" + " encoding=" + "\"" + "UTF-8" + "\"" + "?>";
	   xmlStr += "\n" + "<UpdateJury>" + "\n";
	   xmlStr += "<student>" + name + "</student>";
	   xmlStr += "<thesis_title>" + title + "</thesis_title>";
	   xmlStr += "<supervisor>" + supervisor + "</supervisor>";
	   xmlStr += "<examiner1>" + examiner1 + "</examiner1>";
	   xmlStr += "<examiner2>" + examiner2 + "</examiner2>";
	   xmlStr += "<president>" + president + "</president>";
	   xmlStr += "<secretary>" + secretary + "</secretary>";
	   xmlStr += "<member>" + member + "</member>";
	   xmlStr += "</UpdateJury>";
	   //alert(xmlStr);
	    //xmlhttp.open("POST","TeacherManager",true); //getname will be the servlet name
	    xmlhttp.open("POST","DefenseScheduleManager",true); //getname will be the servlet name
	    xmlhttp.onreadystatechange  = handleUpdateJury;
	    //xmlhttp.onreadystatechange  = handleSubmitPoint;
	    xmlhttp.overrideMimeType('text/xml');
	    xmlhttp.setRequestHeader('Content-Type', 'text/xml');
	    xmlhttp.send(xmlStr); //Posting to Servlet

	    //xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	    //xmlhttp.send("xml="+xml); //Posting to Servlet


	}	
	
}

function handleUpdateJury(){
	//window.location.href = "DefenseJuryManager.jsp";
	window.location.href = "ScheduleJuries.jsp";
	if(xmlhttp.readyState == 4){
		if(xmlhttp.status == 200){
			var xmlData = xmlhttp.responseXML;
			var jr = xmlData.getElementsByTagName("Jury");
			//for(i = 0; i < jr.length; i++){
				var studentID = jr[0].getElementsByTagName("StudentID")[0].firstChild.nodeValue;
				var studentName = jr[0].getElementsByTagName("StudentName")[0].firstChild.nodeValue;
				var thesis_title = jr[0].getElementsByTagName("ThesisTitle")[0].firstChild.nodeValue;
				var examiner1Name = jr[0].getElementsByTagName("Examiner1Name")[0].firstChild.nodeValue;
				var examiner2Name = jr[0].getElementsByTagName("Examiner2Name")[0].firstChild.nodeValue;
				var presidentName = jr[0].getElementsByTagName("PresidentName")[0].firstChild.nodeValue;
				var secretaryName = jr[0].getElementsByTagName("SecretaryName")[0].firstChild.nodeValue;
				var memberName = jr[0].getElementsByTagName("MemberName")[0].firstChild.nodeValue;
				var slot = jr[0].getElementsByTagName("Slot")[0].firstChild.nodeValue;
				var room = jr[0].getElementsByTagName("Room")[0].firstChild.nodeValue;
			//}	
			
		}
	}
}
</script>

</body>
</html>