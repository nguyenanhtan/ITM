
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%
	String uid	= "";
	if(session.getAttribute("username") == null){
		response.sendRedirect("Login.jsp");
	}else{
		uid = session.getAttribute("username").toString();	
	}
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Hệ thống quản lý đào tạo cao học</title>
<script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
<script src="./js/script.js"></script>
<script src="./js/home.js"></script>

<link type="text/css" rel="stylesheet" href="./css/style.css"></link>
<link type="text/css" rel="stylesheet" href="./css/home.css"></link>
</head>
<body>


<div id="big-bound">
	<div id="inbound">
		<div id="header-block">
			<script type="text/javascript">
				writeHeader("<%=uid%>","Hệ thống quản lý đào tạo cao học");
			</script>
		</div>
		<div class="clear"></div>
		<ul id="list-function">
			<li>				
				<a href=StudentManager.jsp>Thông tin về học viên</a>
			</li>
			<li>
				<a href=ScheduleJuries.jsp>Xếp lịch</a>
			</li>
			<li>
				<a href=ViewDefenseTimeTabling.jsp>Hiển thị thông tin chi tiết hội đồng bảo vệ</a>
			</li>
			<li>
				<a href=ClassesManager.jsp>Thông tin về lớp</a>
			</li>
			<li>
				<a href=SubjectCategoriesManager.jsp>Thông tin về các nhóm đề tài</a>
			</li>
			<li>
				<a href=TeacherManager.jsp>Thông tin về giảng viên</a>
			</li>
			<li>
				<a href=SlotsManager.jsp>Thông tin về kíp bảo vệ</a>
			</li>
			<li>
				<a href=RoomsManager.jsp>Thông tin về phòng</a>
			</li>
			<li>
				<a href=DefenseSessionsManager.jsp>Thông tin các đợt bảo vệ</a>
			</li>
			<li>
				<a href=AssignmentSubject.jsp>Giao đề tài</a>
			</li>
			<div class="clear"></div>
		</ul>
		
	</div>
</div>

</body>
</html>