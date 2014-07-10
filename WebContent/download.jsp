<%@page import="java.io.FileInputStream"%>
<%@page import="utils.*" %>
<%
if(session.getAttribute("username") == null){
	response.sendRedirect("Login.jsp");
}else{
	String uid = session.getAttribute("username").toString();
	out.println("Hello " + uid);
}
%>
<a href=Logout.jsp>Logout</a>
<%

//String fn = "/Users/dungpq/research/projects/ITrainingManager/test.txt";
//String fn = "/Users/dungpq/research/projects/ITrainingManager/jury.xls";

Utility u = new Utility();
//String path = u.getClass().getClassLoader().getResource(".").getPath();
String path = Configure.path;//"/home/sdh/tmp/";
String fn = path + "schedule.xls";

String orderBy = request.getParameter("order-for-excel");
String filterBy = request.getParameter("filter-for-excel");
//Utility.createDefenseScheduleExcel(fn, filterBy, orderBy);
Utility.createDefenseScheduleExcelFull(fn, filterBy, orderBy);
System.out.println("excel file created!!!!");

response.setContentType("APPLICATION/OCTET-STREAM");
response.setHeader("Content-Disposition","attachement;filename=\"" + "downloaded-jury.xls" + "\"");
java.io.FileInputStream in = new java.io.FileInputStream(fn);
int i;
byte[] buf = new byte[1024000];
java.io.OutputStream os = response.getOutputStream();
while((i = in.read(buf)) != -1){
	os.write(buf,0,i);
}
in.close();
os.flush();
os.close();
%>