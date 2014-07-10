<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Delete a jury</title>
</head>
<body>
<%
if(session.getAttribute("username") == null){
	response.sendRedirect("Login.jsp");
}else{
	String uid = session.getAttribute("username").toString();
	out.println("Hello " + uid);
}
%>
<a href=Logout.jsp>Logout</a>
<%@page import="utils.*" %>

<%
int studentID = Integer.valueOf(request.getParameter("studentID"));
Utility.deleteJury(studentID);
%>
Delete a jury successfully!!<br>
<a href='ScheduleJuryMembers.jsp'>Back</a>
</body>
</html>