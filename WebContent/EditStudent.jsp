<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Cap nhat thong tin hoc vien</title>
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
<tr><td>
ID:</td><td><input type="text" name="id" id="id" style="width:200px" value="<%=request.getParameter("id")%>"/>
</td></tr>
<tr><td>
Ho va ten:</td><td><input type="text" name="name" id="name" style="width:200px" value="<%=request.getParameter("name")%>"/>
</td></tr>
<tr><td>
Ma hoc vien:</td><td><input type="text" name="studentId" id="studentId" value="<%=request.getParameter("studentID")%>" style="width:200px"/>
</td></tr>

<tr><td>
Email:</td><td><input type="text" name="email" id="email" style="width:400px" value="<%=request.getParameter("email")%>"/>
</td></tr>
<tr><td>
Dien thoai:</td><td><input type="text" name="phone" id="phone" style="width:400px" value="<%=request.getParameter("phone")%>"/>
</td></tr>
<tr><td>
Khoa:</td><td><select name="promotion" id="select-promotion" class="select-box min-width-140" >
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
<tr><td>
Tinh trang:</td><td><select name="status" id="select-status"></select>
</td></tr>
<tr><td></td><td>
<tr><td></td><td><input type="button" value="Cap nhat" style = "width:100px" onclick = "updateStudent()"/></td></tr>
</table>
</form>
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

var student_status_id = new Array();
var student_status_description = new Array();

var class_id = new Array();
var class_name = new Array();

function loadStudent(id){
	if(xmlhttp != false){
		   var xmlStr = "<?xml version=" + "\"" + "1.0" + "\"" + " encoding=" + "\"" + "UTF-8" + "\"" + "?>";
		   xmlStr += "\n" + "<LoadExistStudent>" + "\n" ;
		   xmlStr += "<id>" + id + "</id>";
		   xmlStr += "</LoadExistStudent>";
		    xmlhttp.open("POST","TeacherManager",true); //getname will be the servlet name
		    xmlhttp.onreadystatechange  = handleLoadExistStudent;
		 
		    xmlhttp.overrideMimeType('text/xml');
		    xmlhttp.setRequestHeader('Content-Type', 'text/xml; charset=utf-8');
		    xmlhttp.send(xmlStr); //Posting to Servlet
		  
	}
}

function handleLoadExistStudent(){
	if(xmlhttp.readyState == 4){
		if(xmlhttp.status == 200){
			var xmlData = xmlhttp.responseXML;
			var Cl = xmlData.getElementsByTagName("class");
			//alert(Cl.length);
			for(i = 0;i < Cl.length; i++){
				var classID = Cl[i].getElementsByTagName("classID")[0].firstChild.nodeValue;
				var className = Cl[i].getElementsByTagName("className")[0].firstChild.nodeValue;
				class_id.push(classID);
				class_name.push(className);
				//alert(class_id + "\t" + class_name);
			}
			var select_class = document.getElementById("select-class");
			select_class.options.length = 0;
			for(idx in class_id){
				var opt = new Option(class_name[idx],class_id[idx]);
				select_class.options[select_class.options.length] = opt;
			}
			var SS = xmlData.getElementsByTagName("StudentStatus");
			//alert(SS.length);
			for(i = 0; i < SS.length; i++){
				var id = SS[i].getElementsByTagName("id")[0].firstChild.nodeValue;
				var des = SS[i].getElementsByTagName("description")[0].firstChild.nodeValue;
				student_status_id.push(id);
				student_status_description.push(des);
			}
			
			var select_student_status = document.getElementById("select-status");
			for(id in student_status_id){
				var opt = new Option(student_status_description[id],student_status_id[id]);
				//if(student_status_id == St_status)
					//opt.selected = true;
				select_student_status.options[select_student_status.options.length] = opt;			
			}
						
			var St = xmlData.getElementsByTagName("student");
			//alert(T[0].getElementsByTagName("name")[0].firstChild.nodeValue);
			document.getElementById("name").value =decodeURIComponent(St[0].getElementsByTagName("name")[0].firstChild.nodeValue);
			document.getElementById("id").value=St[0].getElementsByTagName("id")[0].firstChild.nodeValue;
			document.getElementById("id").readonly = "readonly";
			document.getElementById("studentId").value=St[0].getElementsByTagName("studentID")[0].firstChild.nodeValue;
			//document.getElementById("thesis_title").value=St[0].getElementsByTagName("subject")[0].firstChild.nodeValue;
			document.getElementById("email").value=St[0].getElementsByTagName("email")[0].firstChild.nodeValue;
			document.getElementById("phone").value=St[0].getElementsByTagName("phone")[0].firstChild.nodeValue;
			document.getElementById("select-promotion").value=St[0].getElementsByTagName("promotion")[0].firstChild.nodeValue;
			document.getElementById("select-class").value=St[0].getElementsByTagName("classID")[0].firstChild.nodeValue;
			document.getElementById("select-status").value=St[0].getElementsByTagName("status")[0].firstChild.nodeValue;
			var St_status = St[0].getElementsByTagName("status")[0].firstChild.nodeValue;	 
			
	

		}
	}
}


function init(){
	//loadDataBase();
	loadStudent("<%=request.getParameter("id")%>");
	document.getElementById("select-promotion").value = "<%=request.getParameter("promotion")%>";
	document.getElementById("select-class").value = "<%=request.getParameter("class_student")%>";
	
}

function updateStudent(){
	//alert(encodeURIComponent(name));
	//alert("delete professor " + name);
	if(xmlhttp!=false){
		var name = encodeURIComponent(document.getElementById("name").value);
		
		var id = document.getElementById("id").value;
		
		var studentID = document.getElementById("studentId").value;
		
		//var subject =document.getElementById("thesis_title").value;
		var email =document.getElementById("email").value;
		var phone =document.getElementById("phone").value;
		var promotion =document.getElementById("select-promotion").value;
		var class_student =document.getElementById("select-class").value;	
		
		if(email.length == 0) email = "-";
		if(phone.length == 0)phone = "-";
		if(studentID.length == 0) studentID = "-";
		if(name.length == 0) name = "-";
		
	   var xmlStr = "<?xml version=" + "\"" + "1.0" + "\"" + " encoding=" + "\"" + "UTF-8" + "\"" + "?>";
	   xmlStr += "\n" + "<UpdateStudent>" + "\n";
	   xmlStr += "<id>" + id + "</id>";
	   xmlStr += "<name>" + encodeURIComponent(name) + "</name>";
	   
	   xmlStr += "<studentID>" + studentID+  "</studentID>";
	   xmlStr += "<promotion>" + promotion+  "</promotion>";
	   xmlStr += "<class_student>" +class_student +  "</class_student>";
	   xmlStr += "<email>" +email +  "</email>";
	   xmlStr += "<phone>" + phone+  "</phone>";
	   //xmlStr += "<subject>" +encodeURIComponent(subject) +  "</subject>";
	   xmlStr += "<status>" + document.getElementById("select-status").value + "</status>";
	   xmlStr += "</UpdateStudent>";
	  // alert(xmlStr);
	    xmlhttp.open("POST","TeacherManager",true); //getname will be the servlet name
	    //alert("POST OK");
	    xmlhttp.onreadystatechange  = handleUpdateStudent;
	    //alert("handleDeleteTeacher OK");
	    //xmlhttp.onreadystatechange  = handleSubmitPoint;
	    xmlhttp.overrideMimeType('text/xml');
	    //alert("overrideMimeType OK");
	    xmlhttp.setRequestHeader('Content-Type', 'text/xml');
	    //alert("setRequestHeader OK");
	    xmlhttp.send(xmlStr); //Posting to Servlet

	   
	}
	
}
function handleUpdateStudent(){
	//alert("Update student successfully");
	window.location.href = "StudentManager.jsp";
}

</script>

</body>
</html>