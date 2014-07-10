<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
<script src="./js/jquery-1.11.1.js"></script>
<script src="./js/script.js"></script>
<script src="./js/ScheduleJuries.js"></script>

<!-- <script src="http://code.jquery.com/jquery-1.11.0.min.js"></script> -->
<link type="text/css" rel="stylesheet" href="./css/style.css"></link>
<title>Quản lý thông tin các hội đồng bảo vệ</title>
</head>
<body><!--  onload="loadDataBase()"> -->
<div id="big-bound">
	<div id="inbound">
		<div id="header-block">
			<script type="text/javascript">
				writeHeader("<%=uid%>","Quản lý thông tin các hội đồng bảo vệ");
			</script>
		</div>

		<!-- <form id="teacher-info" action="#"> -->
		<div>		
<!-- 			<input type="button" name="load_jury" value="Nap danh sach hoi dong" style="width:200px" onclick="loadJuryList()"> -->
			<div id="selector-box">
				<span>Hoi dong: </span><select name="select-defense-session" id="select-defense-session"></select>
				<span>Sap xep theo: </span><select name="sortby" id="sortby" onchange="loadJuryList()">
					<option value="Supervisor">Giao vien huong dan</option>
					<option value="StudentID">Ma Sinh Vien</option>
					<option value="Slot">Kip</option>
					<option value="Room">Phong</option>
					<option value="Room-Slot">Phong-Kip</option>
					<option value="Slot-Room">Kip-Phong</option>
					<option value="Examiner1">Phan bien 1</option>
					<option value="Examiner2">Phan bien 2</option>
					<option value="President">Chu tich</option>
					<option value="Secretary">Thu ky</option>
					<option value="AdditionalMember">Uy vien</option>
					
				</select>
				<span>Loc theo: </span>
				<!-- <select name="filter-by" id="filter-by" onchange="changeSelectDepartement()"></select> -->
				<select name="filter-by" id="filter-by" onchange="loadJuryList()"></select>
				<select id="filter-by-students" onchange="loadJuryList()"></select>
			</div>
			<!-- <input type="button" value="Xoa tat ca danh sach dang hien thi" onclick="resetAllDisplayedList()"> -->
			<div id="mes-nojury">Không có hội đồng nào</div>
			<div id="box-list-jury">
				<table id="list-jury">
				<tr id="tr-head">
				<th>
					<p>STT</p>
				</th>
				<th>
					<p>SHHV</p>
				</th>
				<th>
					<p>Họ và tên</p>
				</th>
				<th>
					<p>Tên đề tài</p>
				</th>
				<th>
					<p>GVHD</p>
					<input type="button" title="Sort" class="btn-sort" onclick="sortJurySupervisor()">
					
				</th>
				<th>
					<p>Phản biện 1</p>
					<input type="button" title="Sort" class="btn-sort" onclick="sortJuryExaminer1()">
					<input type="button" title="Refresh" class="btn-refresh" onclick="resetExaminer1()">
				</th>
				<th>
					<p>Phản biện 2</p>
					<input type="button" title="Sort" class="btn-sort" onclick="sortJuryExaminer2()">
					<input type="button" title="Refresh" class="btn-refresh" onclick="resetExaminer2()">
				</th>

				<th>
					<p>Chủ tịch</p>
					<input type="button" title="Sort" class="btn-sort" onclick="sortJuryPresident()">
					<input type="button" title="Refresh" class="btn-refresh" onclick="resetPresident()">
				</th>

				<th>
					<p>Thư ký</p>
					<input type="button" title="Sort" class="btn-sort" onclick="sortJurySecretary()">
					<input type="button" title="Refresh" class="btn-refresh" onclick="resetSecretary()">
				</th>

				<th>
					<p>Ủy viên</p>
					<input type="button" title="Sort" class="btn-sort" onclick="sortJuryAdditionalMember()">
					<input type="button" title="Refresh" class="btn-refresh" onclick="resetAdditionalMember()">
				</th>

				<th>
					<p>Kíp</p>
					<input type="button" title="Sort" class="btn-sort" onclick="sortJurySlot()">
					<input type="button" title="Refresh" class="btn-refresh" onclick="resetSlots()">
				</th>
				<th>
					<p>Phòng</p>
					<input type="button" title="Sort" class="btn-sort" onclick="sortJuryRoom()">
					<input type="button" title="Refresh" class="btn-refresh" onclick="resetRooms()">
				</th>
				<th colspan='4'></th>
				</tr>

				</table>
			</div>
			
			<input type="button" value="Reset phan bien 1" onclick="resetExaminer1()" style="visibility:hidden">
			<input type="button" value="Reset phan bien 2" onclick="resetExaminer2()" style="visibility:hidden">
			<input type="button" value="Reset chu tich" onclick="resetPresident()" style="visibility:hidden">
			<input type="button" value="Reset thu ky" onclick="resetSecretary()" style="visibility:hidden">
			<input type="button" value="Reset uy vien" onclick="resetAdditionalMember()" style="visibility:hidden">
			<input type="button" value="Reset kip" onclick="resetSlots()" style="visibility:hidden">
			<input type="button" value="Reset phong" onclick="resetRooms()" style="visibility:hidden">
			<input type="button" value="Reset giang vien tung hoi dong" onclick="resetScheduleMembers()" style="visibility:hidden">
			<input type="button" value="Sap xep hoi dong" onclick="sortJury()" style="visibility:hidden">

			<input type="button" value="Hop nhat cac hoi dong bo mon" onclick="consolidateJuriesOfDepartments()" style="visibility:hidden">

			
		</div>
<!-- 		</form> -->
		<input type="button" value="Reset Phong" onclick="resetRoomInfo()" style="visibility:hidden">
		<input type="button" value="Them Phong" onclick="increaseRoomInfo()" style="visibility:hidden">
		<input type="button" value="Giam Phong" onclick="decreaseRoomInfo()" style="visibility:hidden">
		
		<div id="out-result">
			<table id="list-slots">
			<tr>
			<td>Kip</td><td>Thoi gian</td>
			</tr>
			</table>
			<table id="list-rooms">
			<tr><td>Phong</td><td>Ten</td><td>So SV</td><td>So GV ngoai truong</td><td>So GV trong Truong</td></tr>
			</table>		
			<input type="button" value="Luu lai" onclick="saveSchedule()">
			<input type="text" id="room-name" style="width:50px; visibility:hidden">
		</div>
		<div class="footer">
			<div id="box-select-teacher">
				<span>Chọn giảng viên vào hội đồng</span>
				<select id="departments-teachers" onchange="changeSelectDepartementTeacher()"></select>
				<table id="jury-members">
				</table>
				<input type="button" id="add-jury-member" value="Them vao hoi dong" onclick="addMemberToJury()">
				<select id="selected-members"></select>
				
				
				<input type="button" value="Chap nhan giang vien cho hoi dong" onclick="confirmSelectedMembers()">
				<input type="button" value="Reset giang vien da duoc chon" onclick="resetSelectedMembers()"><br>
			</div>
			<div id="box-method">
				
				<span>Số phòng</span>
				<input type="text" id="nbr-juries" name="nbr-juries" value="0" style="width:50px">
				<span>Thoi gian chay:</span>
				<input type="text" name="time-limit" id="time-limit" style="width:50px" value="10">
				<br>
				<span>Thuat toan:</span> 
				<select name="algorithm" id="algorithm">
				<option value="AssignSlots">Xep cac kip</option>
				<option value="AssignExaminer1">Xep phan bien 1</option>
				<option value="AssignExaminer2">Xep phan bien 2</option>
				<option value="AssignPresident">Xep Chu tich</option>
				<option value="AssignSecretary">Xep Thu ky</option>
				<option value="AssignAdditionalMember">Xep Uy vien</option>
				<option value="AssignRooms">Xep cac phong</option>
				<option value="AssignPresidentsRooms">Xep chu tich va phong (chu tich khong thay doi phong)</option>
				<option value="AssignAllProfessorsNotMove">Xep toan bo (giang vien khong thay doi phong)</option>
				<option value="AssignAll">Xep toan bo</option>
				</select>
				<br>
				<input type="button" value="Xep tu dong" onclick="searchSolution()">

				<input type="button" value="Can bang tai" onclick="balanceLoad()">
				<span>Theo: </span><select name="balance" id="balance">
				<option value="examiner1">Phan bien 1</option>
				<option value="examiner2">Phan bien 2</option>
				</select>

				<input type="button" value="Kiem tra tinh hop le" onclick="checkConsistency()">
				<input type="button" value="Chuan bi luu" onclick="prepareStore()">
			</div>
		</div>
		<p id="test"></p>
		<p id="jury-table"></p>
	</div>
</div>
<style type="text/css">
#tr-head{
	background-color: #C1D365;


}
#tr-head th{
	border: 1px solid #888;	
}
.ipt-jury{

}
.sel-jury{

}
#list-jury tr th p{
	float: left;
	padding-left: 5px;
	/*display: none;*/
}
#list-jury tr th input{
	
}
#list-jury tr th input.btn-sort{
	background: url('./img/sort-ascend.png');
	/* width: 6px; */
	/* padding: 0px 8px; */
	height: 17px;
	width: 17px;
	border: 1px solid #A0A0A0;
	border-radius: 3px;
	background-repeat: no-repeat;
	background-position: center;
	float: right;
	margin-left: 4px;
	margin-right: 4px;
	top: 3px;
	position: relative;
}
#list-jury tr th input.btn-refresh{
	background: url('./img/refresh.png');
	height: 17px;
	width: 17px;
	border: none;
	border-radius: 3px;
	background-repeat: no-repeat;
	background-position: center;
	float: right;
	top: 3px;
	position: relative;
}
#box-list-jury{
	overflow-x: scroll;
	display: none;
	margin-bottom: 10px;
}
#selector-box
{
	margin-bottom: 2px;
	padding-left: 3px;
}
#mes-nojury{
	width: 100%;
	text-align: center;
	padding: 7px 0;
	background: #D5ECA3;
	border-top: 1px solid #A0ABB6;
	border-bottom: 1px solid #A0ABB6;
	font-weight: bold;
	color: #DA0F0F;
	box-shadow: 0px 2px 6px #C5C5C5;
}
#box-select-teacher{
	border-right: 1px solid #cdcdcd;
}
#box-method, #box-select-teacher{
	width: 45%;
	float: left;
	padding: 5px;
	height: 70px;
}
.footer{
	position: fixed;
	bottom: 0;
	background: #CDDF8A;
	width: 100%;
	left: 0;
	padding: 0px 5%;
	border-top: 1px solid #9FA5AA;
	/* color: #fff; */
	box-shadow: 1px -1px 20px #C2C2C2;
	height: 80px;
}
#inbound{
	
}
#jury-members{
	position: fixed;
	top: 10px;
	background: #9AC1E4;
	height: 350px;
	display: block;
	overflow-y: scroll;
	border: 1px solid #cdcdcd;
	box-shadow: 3px 3px 10px #4F657A;
	display: none;
}
</style>
</body>
</html>
