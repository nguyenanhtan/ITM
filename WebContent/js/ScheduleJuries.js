var professor_id = new Array();
var professor_name = new Array();
var jury_professors = new Array();
var professors = new Array();
var student_id = new Array();
var student_name = new Array();
var departments_id = new Array();
var departments_name = new Array();
var session_id = new Array();
var session_description = new Array();
var sortBy = "Supervisor";

var juries = new Array();

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

function resetRoomInfo(){
	var tblR = document.getElementById("list-rooms");
	tblR.rows.length = 0;
}
function increaseRoomInfo(){
	var tblR = document.getElementById("list-rooms");
	var id = tblR.rows.length;
	addRowRoom(tblR,id,"",4,2,3);
}
function decreaseRoomInfo(){
	var tblR = document.getElementById("list-rooms");
	var id = tblR.rows.length;
	var r = tblR[tblR.rows.length];
	if(tblR.rows.length > 1)
		tblR.deleteRow(id-1);
}

function confirmSelectedMembers(){
	var table  = document.getElementById("list-jury");
	var sz = table.rows.length;
	for(i = 1; i < sz; i++){
		var sel_examiner1 = document.getElementById("examiner1-" + i);
		var sel_examiner2 = document.getElementById("examiner2-" + i);
		var sel_president = document.getElementById("president-" + i);
		var sel_secretary = document.getElementById("secretary-" + i);
		var sel_member = document.getElementById("member-" + i);
		
		sel_examiner1.options.length = 0;
		sel_examiner2.options.length = 0;
		sel_president.options.length = 0;
		sel_secretary.options.length = 0;
		sel_member.options.length = 0;
		
		sel_examiner1.options[sel_examiner1.options.length] = new Option("-",0);
		sel_examiner2.options[sel_examiner2.options.length] = new Option("-",0);
		sel_president.options[sel_president.options.length] = new Option("-",0);
		sel_secretary.options[sel_secretary.options.length] = new Option("-",0);
		sel_member.options[sel_member.options.length] = new Option("-",0);
		
		for(j = 0; j < jury_professors.length; j++){
			var p = jury_professors[j];
			sel_examiner1.options[sel_examiner1.options.length] = new Option(p.name,p.id);
			sel_examiner2.options[sel_examiner2.options.length] = new Option(p.name,p.id);
			sel_president.options[sel_president.options.length] = new Option(p.name,p.id);
			sel_secretary.options[sel_secretary.options.length] = new Option(p.name,p.id);
			sel_member.options[sel_member.options.length] = new Option(p.name,p.id);
		}
	}
}


function changeSelectStudent(){
	if(xmlhttp){
		   var xmlStr = "<?xml version=" + "\"" + "1.0" + "\"" + " encoding=" + "\"" + "UTF-8" + "\"" + "?>";
		   xmlStr += "\n" + "<LoadStudentDefense>" + "\n";
		   xmlStr += "<student-id>" + document.getElementById("select-student").value + "</student-id>";
		   xmlStr += "<session-id>" + document.getElementById("select-defense-session").value + "</session-id>";
		   xmlStr += "</LoadStudentDefense>";
		    //alert(xmlStr);
		    //xmlhttp.open("POST","TeacherManager",true); //getname will be the servlet name
		    xmlhttp.open("POST","DefenseScheduleManager",true); //getname will be the servlet name
		    xmlhttp.onreadystatechange  = handleChangeSelectStudent;
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
function handleChangeSelectStudent(){
	if(xmlhttp.readyState == 4){
		if(xmlhttp.status == 200){
			var xmlData = xmlhttp.responseXML;
			var exist = xmlData.getElementsByTagName("exist")[0].firstChild.nodeValue;
			//alert(exist);
			if(exist === 'true'){
				var jury = xmlData.getElementsByTagName("Jury")[0];
				//alert(xmlData.xml);
				var title = decodeURIComponent(jury.getElementsByTagName("ThesisTitle")[0].firstChild.nodeValue);
				var supervisorID = jury.getElementsByTagName("SupervisorID")[0].firstChild.nodeValue;
				var inp_title = document.getElementById("thesis_title");
				inp_title.value = title;
				document.getElementById("select-supervisor").value = supervisorID;
				
				// var tbl = document.getElementById("subject-category-match");
				// clearTable(tbl);
				var scm = xmlData.getElementsByTagName("student-subject-category-match");
				// for(i = 0; i < scm.length; i++){
				// 	var subject_category_id = scm[i].getElementsByTagName("subject-category-id")[0].firstChild.nodeValue;
				// 	var subject_category_name = decodeURIComponent(scm[i].getElementsByTagName("subject-category-name")[0].firstChild.nodeValue);
				// 	var match_score = scm[i].getElementsByTagName("match-score")[0].firstChild.nodeValue;
				// 	addRowSubjectCategoryMatch(tbl,subject_category_id,subject_category_name,match_score);					
				// }
			}else{
				document.getElementById("thesis_title").value = "";
				document.getElementById("select-supervisor").value = 0;
			}
		}
	}
}
function changeSelectDepartement(){
	if(xmlhttp){
		   var xmlStr = "<?xml version=" + "\"" + "1.0" + "\"" + " encoding=" + "\"" + "UTF-8" + "\"" + "?>";
		   xmlStr += "\n" + "<LoadStudentsOfDepartment>" + "\n";
		   xmlStr += "<department>" + document.getElementById("filter-by").value + "</department>";
		   xmlStr += "</LoadStudentsOfDepartment>";
		    //alert(xmlStr);
		    //xmlhttp.open("POST","TeacherManager",true); //getname will be the servlet name
		    xmlhttp.open("POST","DefenseScheduleManager",true); //getname will be the servlet name
		    xmlhttp.onreadystatechange  = handleChangeSelectDepartment;
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

function handleChangeSelectDepartment(){
	if(xmlhttp.readyState == 4){
		if(xmlhttp.status == 200){
			var xmlData = xmlhttp.responseXML;
			var students = xmlData.getElementsByTagName("student");
			var selFilterByStudent = document.getElementById("filter-by-students");
			selFilterByStudent.options.length = 0;
			selFilterByStudent.options[selFilterByStudent.options.length] = new Option("All","All");
			for(i = 0; i < students.length; i++){
				var sid = students[i].getElementsByTagName("id")[0].firstChild.nodeValue;
				var sname = decodeURIComponent(students[i].getElementsByTagName("name")[0].firstChild.nodeValue);
				
				selFilterByStudent.options[selFilterByStudent.options.length] = new Option(sname,sid);	
			}
		}
	}
}

function changeSelectDepartementTeacher(){
	if(xmlhttp){
		   var xmlStr = "<?xml version=" + "\"" + "1.0" + "\"" + " encoding=" + "\"" + "UTF-8" + "\"" + "?>";
		   xmlStr += "\n" + "<LoadTeachersOfDepartment>" + "\n";
		   xmlStr += "<department>" + document.getElementById("departments-teachers").value + "</department>";
		   xmlStr += "</LoadTeachersOfDepartment>";
		    //alert(xmlStr);
		    //xmlhttp.open("POST","TeacherManager",true); //getname will be the servlet name
		    xmlhttp.open("POST","DefenseScheduleManager",true); //getname will be the servlet name
		    xmlhttp.onreadystatechange  = handleChangeSelectDepartmentTeacher;
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

function handleChangeSelectDepartmentTeacher(){
	if(xmlhttp.readyState == 4){
		if(xmlhttp.status == 200){
			var xmlData = xmlhttp.responseXML;
			var T = xmlData.getElementsByTagName("teacher");
			var tbl = document.getElementById("jury-members");
			//selFilterByTeacher.options.length = 0;
			//selFilterByTeacher.options[selFilterByTeacher.options.length] = new Option("All","All");
			while(tbl.rows.length >= 1)
				tbl.deleteRow(0);
			
			for(i = 0; i < T.length; i++){
				var id = T[i].getElementsByTagName("id")[0].firstChild.nodeValue;
				var name = decodeURIComponent(T[i].getElementsByTagName("name")[0].firstChild.nodeValue);
				
				//selFilterByTeacher.options[selFilterByTeacher.options.length] = new Option(name,id);	
				addRowTeacher(id,name);
			}
			$("#jury-members").show(400);
		}
	}
}

function addRowTeacher(id,name){
	var tbl = document.getElementById("jury-members");
	var sz = tbl.rows.length;
	var row = tbl.insertRow(sz);
	
	var cell = row.insertCell(0);
	var e = document.createElement("input");
	e.setAttribute("id","id-" + sz);
	e.setAttribute("value",id);
	e.setAttribute("style","width:50px");
	e.setAttribute("readonly",true);
	cell.appendChild(e);
	
	cell = row.insertCell(1);
	e = document.createElement("input");
	e.setAttribute("id","name-" + sz);
	e.setAttribute("value",name);
	e.setAttribute("style","width:200px");
	e.setAttribute("readonly",true);
	cell.appendChild(e);
	
	cell = row.insertCell(2);
	e = document.createElement("input");
	e.setAttribute("type","checkbox");
	e.setAttribute("id","check-" + sz);
	e.setAttribute("style","width:50px");
	cell.appendChild(e);
}
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
	// loadJuryList();

}

function handleLoadDataBase(){
	//alert("handleLoadDataBase");
	if(xmlhttp.readyState == 4){
		if(xmlhttp.status == 200){
			var xmlData = xmlhttp.responseXML;
			// alert("handleLoadTeachersStudentsList: " + xmlData);
			var ST = xmlData.getElementsByTagName("student");
			for(i = 0; i < ST.length; i++){
				var id = ST[i].getElementsByTagName("id")[0].firstChild.nodeValue;
				var name = decodeURIComponent(ST[i].getElementsByTagName("name")[0].firstChild.nodeValue);
				
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

			var tblSubjectCategory = document.getElementById("subject-category-match");
			var categories = xmlData.getElementsByTagName("subject-category");
			//alert(categories.length);
			// for(i = 0; i < categories.length; i++){
			// 	var id = categories[i].getElementsByTagName("id")[0].firstChild.nodeValue;
			// 	var name = decodeURIComponent(categories[i].getElementsByTagName("name")[0].firstChild.nodeValue);
			// 	addRowSubjectCategoryMatch(tblSubjectCategory, id,name,10);
			// }

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
				// selectsupervisor.options[selectsupervisor.options.length] = new Option(professor_name[idx],professor_id[idx]);
				// selectpresident.options[selectpresident.options.length] = new Option(professor_name[idx],professor_id[idx]);
				/*selectsecretary.options[selectsecretary.options.length] = new Option(professor_name[idx],professor_id[idx]);
				selectmember.options[selectmember.options.length] = new Option(professor_name[idx],professor_id[idx]);
				selectexaminer1.options[selectexaminer1.options.length] = new Option(professor_name[idx],professor_id[idx]);
				selectexaminer2.options[selectexaminer2.options.length] = new Option(professor_name[idx],professor_id[idx]);*/
				
				//selectjurymembers.options[selectjurymembers.options.length] = new Option(professor_name[idx],professor_id[idx]);
				
				//addRowTeacher(professor_id[idx],professor_name[idx]);
			}
			
			var selectstudent = document.getElementById("select-student");
			for(idx in student_id){
				// selectstudent.options[selectstudent.options.length] = new Option(student_name[idx],student_id[idx]);
			}
			
			var selectdepartment = document.getElementById("filter-by");
			selectdepartment.options[selectdepartment.options.length] = new Option("All","All");
			for(idx in departments_id){
				selectdepartment.options[selectdepartment.options.length] = new Option(departments_name[idx],departments_name[idx]);
			}
			
			var selectfilterbystudents = document.getElementById("filter-by-students");
			selectfilterbystudents.options[selectfilterbystudents.options.length] = new Option("All","All");
			for(idx in student_id){
				selectfilterbystudents.options[selectfilterbystudents.options.length] = new Option(student_name[idx],student_id[idx]);
			}

			var selectdepartmentteacher = document.getElementById("departments-teachers");
			selectdepartmentteacher.options[selectdepartmentteacher.options.length] = new Option("None","None");
			selectdepartmentteacher.options[selectdepartmentteacher.options.length] = new Option("All","All");
			for(idx in departments_id){
				selectdepartmentteacher.options[selectdepartmentteacher.options.length] = new Option(departments_name[idx],departments_name[idx]);
			}
			loadJuryList();
			
		}
	}
	
	// loadJuryList();
}

function addMemberToJury(){
	var tbl = document.getElementById("jury-members");
	var sel_members = document.getElementById("selected-members");
	//alert(tbl.rows.length);
	for(i = 0; i < tbl.rows.length; i++){
		var chk = document.getElementById("check-" + i);
		if(chk.checked === true){
			var selectjurymember_name = document.getElementById("name-" + i).value;
			var selectjurymember_id = document.getElementById("id-" + i).value;
			
			sel_members.options[sel_members.options.length] = new Option(selectjurymember_name,selectjurymember_id);
			
			var p = findProfessor(selectjurymember_id);
			jury_professors.push(p);
		}
	}
	/*
	//var jm = document.getElementById("jury-members-xml").value;
	var selectjurymember_id = document.getElementById("jury-members").value;
	var p = findProfessor(selectjurymember_id);
	var selectjurymember_name = p.name;//document.getElementById("jury-members").value;
	//jm += '<jury-member>' + selectjurymembers_id + '</jury-member>' + "\n";
	//document.getElementById("jury-members-xml").value = jm;
	
	var sel_members = document.getElementById("selected-members");
	sel_members.options[sel_members.options.length] = new Option(selectjurymember_name,selectjurymember_id);
	
	jury_professors.push(p);
	*/
}

function clearTable(tbl){
	while(tbl.rows.length >= 2){
		tbl.deleteRow(1);
	}
}
function resetScheduleMembers(){
	var table = document.getElementById("list-jury");
	var sz = table.rows.length;

	for(i = 1; i < sz; i++){
		var examiner1 = document.getElementById("examiner1-" + i);
		var examiner2 = document.getElementById("examiner2-" + i);
		var president = document.getElementById("president-" + i);
		var secretary = document.getElementById("secretary-" + i);
		var member = document.getElementById("member-" + i);
		examiner1.options.selectedIndex = 0;
		examiner2.options.selectedIndex = 0;
		president.options.selectedIndex = 0;
		secretary.options.selectedIndex = 0;
		member.options.selectedIndex = 0;
	}
	//var selected_members = document.getElementById("selected-members");
	//selected_members.options.length = 0;
}
function resetSlots(){
	var table = document.getElementById("list-jury");
	var sz = table.rows.length;

	for(i = 1; i < sz; i++){
		var slot = document.getElementById("slot-" + i);
		slot.options.selectedIndex = 0;
	}
}
function resetRooms(){
	var table = document.getElementById("list-jury");
	var sz = table.rows.length;

	for(i = 1; i < sz; i++){
		var rooms = document.getElementById("room-" + i);
		rooms.options.selectedIndex = 0;
	}
}
function resetExaminer1(){
	var table = document.getElementById("list-jury");
	var sz = table.rows.length;

	for(i = 1; i < sz; i++){
		var examiner1 = document.getElementById("examiner1-" + i);
		examiner1.options.selectedIndex = 0;
	}
}
function resetExaminer2(){
	var table = document.getElementById("list-jury");
	var sz = table.rows.length;

	for(i = 1; i < sz; i++){
		var examiner2 = document.getElementById("examiner2-" + i);
		examiner2.options.selectedIndex = 0;
	}
}
function resetPresident(){
	var table = document.getElementById("list-jury");
	var sz = table.rows.length;

	for(i = 1; i < sz; i++){
		var president = document.getElementById("president-" + i);
		president.options.selectedIndex = 0;
	}
}
function resetSecretary(){
	var table = document.getElementById("list-jury");
	var sz = table.rows.length;

	for(i = 1; i < sz; i++){
		var secretary = document.getElementById("secretary-" + i);
		secretary.options.selectedIndex = 0;
	}
}
function resetAdditionalMember(){
	var table = document.getElementById("list-jury");
	var sz = table.rows.length;

	for(i = 1; i < sz; i++){
		var additionalmember = document.getElementById("member-" + i);
		additionalmember.options.selectedIndex = 0;
	}
}

function resetSelectedMembers(){
	var selected_members = document.getElementById("selected-members");
	selected_members.options.length = 0;
	
	jury_professors = new Array();
}

/*
function suggestSlotRoom(studentID){
	alert("suggest " + studentID);
}
*/

function addRowSubjectCategoryMatch(tbl, id, name, matchscore){
	var sz = tbl.rows.length;
	var row = tbl.insertRow(sz);
	
	var cell = row.insertCell(0);
	var e = document.createElement("input");
	e.type = "text";
	e.setAttribute("name","subject-category-id-" + sz);
	e.setAttribute("id","subject-category-id-" + sz);
	e.setAttribute("value",id);
	e.setAttribute("style","visibility:hidden");
	e.style.width="50px";	
	cell.appendChild(e);

	cell = row.insertCell(1);
	e = document.createElement("input");
	e.type = "text";
	e.setAttribute("name","subject-category-name-" + sz);
	e.setAttribute("id","subject-category-name-" + sz);
	e.setAttribute("value",name);
	e.style.width="200px";	
	cell.appendChild(e);

	cell = row.insertCell(2);
	e = document.createElement("input");
	e.type = "text";
	e.setAttribute("name","match-score-" + sz);
	e.setAttribute("id","match-score-" + sz);
	e.setAttribute("value",matchscore);
	e.style.width="50px";	
	cell.appendChild(e);

}

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
function addRowRoom(tbl, id, name, nbJuries, nbNonHust, nbHust){
	var sz = tbl.rows.length;
	var row = tbl.insertRow(sz);
	//alert("addRowRoom, sz = " + sz);
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

	cell = row.insertCell(2);
	e = document.createElement("input");
	e.type = "text";
	e.setAttribute("name","room-info-nbJuries-" + sz);
	e.setAttribute("id","room-info-nbJuries-" + sz);
	e.setAttribute("value",nbJuries);
	e.style.width="100px";	
	cell.appendChild(e);

	cell = row.insertCell(3);
	e = document.createElement("input");
	e.type = "text";
	e.setAttribute("name","room-info-nbNonHust-" + sz);
	e.setAttribute("id","room-info-nbNonHust-" + sz);
	e.setAttribute("value",nbNonHust);
	e.style.width="100px";	
	cell.appendChild(e);

	cell = row.insertCell(4);
	e = document.createElement("input");
	e.type = "text";
	e.setAttribute("name","room-info-nbHust-" + sz);
	e.setAttribute("id","room-info-nbHust-" + sz);
	e.setAttribute("value",nbHust);
	e.style.width="100px";	
	cell.appendChild(e);

}

function addRow(studentID, studentName, thesis_title, supervisorId, supervisorName, examiner1Id, examiner1Name, examiner2Id, 
		examiner2Name, 
		presidentId, presidentName, secretaryId, secretaryName, memberId, memberName, slot, room, color){
	
	
	var table = document.getElementById("list-jury");
	var sz = table.rows.length;
	var row = table.insertRow(sz);
	
	var cell = row.insertCell(0);
	var e = document.createElement("input");
	e.type = "text";
	e.setAttribute("name","index-" + sz);
	e.setAttribute("id","index-" + sz);
	e.setAttribute("value",sz);
	e.setAttribute("class","ipt-jury");	
	e.style.width="50px";
	e.style.color = color;
	
	cell.appendChild(e);
	
	var cell0 = row.insertCell(1);
	var e0 = document.createElement("input");
	e0.type = "text";
	e0.setAttribute("name","studentID-" + sz);
	e0.setAttribute("id","studentID-" + sz);
	e0.setAttribute("value",studentID);
	e0.setAttribute("class","ipt-jury");	
	e0.style.width="50px";
	e0.style.color = color;
	
	cell0.appendChild(e0);
	
	var cell1 = row.insertCell(2);
	var e1 = document.createElement("input");
	e1.type = "text";
	e1.setAttribute("name","studentName-" + sz);
	e1.setAttribute("id","studentName-" + sz);
	e1.setAttribute("value",studentName);
	e1.style.color = color;
	e1.setAttribute("class","ipt-jury");	
	cell1.appendChild(e1);
	
	
	var cell2 = row.insertCell(3);
	var e2 = document.createElement("input");
	e2.type = "text";
	e2.setAttribute("name","thesis-title-" + sz);
	e2.setAttribute("id","thesis-title-" + sz);
	e2.setAttribute("value",thesis_title);
	e2.style.color = color;
	e2.setAttribute("class","ipt-jury");	
	cell2.appendChild(e2);
	
	
	
	
	var cell3 = row.insertCell(4);
	var e3 = document.createElement("select");
	//e3.type = "text";
	e3.setAttribute("name","supervisor-" + sz);
	e3.setAttribute("id","supervisor-" + sz);
	e3.setAttribute("style","width:150px");
	e3.setAttribute("class","sel-jury");	
	//e3.setAttribute("value",examiner1);
	//e3.style.color = color;
	//for(j = 0; j < professor_id.length; j++){
	for(j in professor_id){	
		var opt = document.createElement("option");
		opt.setAttribute("value",professor_id[j]);
		//opt.setAttribute("id",sz+"-"+j);
		opt.setAttribute("label",professor_name[j]);
		opt.style.color='blue';
		opt.innerHTML = professor_name[j];
		if(professor_id[j] == supervisorId){
			opt.selected = true;
		
			e3.appendChild(opt);
		}
	}
	cell3.appendChild(e3);
	
	
	var cell4 = row.insertCell(5);
	var e4 = document.createElement("select");
	//e3.type = "text";
	e4.setAttribute("name","examiner1-" + sz);
	e4.setAttribute("id","examiner1-" + sz);
	e4.setAttribute("class","sel-jury");	
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

	
	var cell5 = row.insertCell(6);
	var e5 = document.createElement("select");
	//e3.type = "text";
	e5.setAttribute("name","examiner2-" + sz);
	e5.setAttribute("id","examiner2-" + sz);
	e5.setAttribute("class","sel-jury");	
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

	var cell6 = row.insertCell(7);
	var e6 = document.createElement("select");
	//e3.type = "text";
	e6.setAttribute("name","president-" + sz);
	e6.setAttribute("id","president-" + sz);
	e6.setAttribute("class","sel-jury");	
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
	
	var cell7 = row.insertCell(8);
	var e7 = document.createElement("select");
	//e3.type = "text";
	e7.setAttribute("name","secretary-" + sz);
	e7.setAttribute("id","secretary-" + sz);
	e7.setAttribute("class","sel-jury");	
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
	
	var cell8 = row.insertCell(9);
	var e8 = document.createElement("select");
	//e3.type = "text";
	e8.setAttribute("name","member-" + sz);
	e8.setAttribute("id","member-" + sz);
	e8.setAttribute("class","sel-jury");	
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
	
	
	var cell9 = row.insertCell(10);
	var e9 = document.createElement("select");
	e9.setAttribute("name","slot-" + sz);
	//e8.setAttribute("id","slot-" + sz);
	e9.setAttribute("id","slot-" + sz);
	e9.setAttribute("class","sel-jury");
	e9.style.width="100px";
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
	
	
	var cell10 = row.insertCell(11);
	var e10 = document.createElement("select");
	e10.setAttribute("name","room-" + sz);	
	e10.setAttribute("id","room-" + sz);
	e10.setAttribute("class","sel-jury");
	e10.style.width="100px";
	
	var optRoom;
	for(j = 0; j <= 200; j++){
		optRoom = document.createElement("option");
		optRoom.setAttribute("value",j);
		optRoom.innerHTML = j;
		optRoom.style.color='red';
		if(j == room)
			optRoom.selected = true;
		e10.appendChild(optRoom);
	}
	cell10.appendChild(e10);

	var cell11 = row.insertCell(12);
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
	
	var cell12 = row.insertCell(13);
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
	cell12.appendChild(deleteForm);
	
	var cell13 = row.insertCell(14);
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
	
	var cell14 = row.insertCell(15);
	var e14 = document.createElement("input");
	e14.type = "button";
	e14.setAttribute("name","delete-" + sz);
	e14.setAttribute("id","delete-" + sz);
	e14.setAttribute("value","Go bo khoi ds");
	// e14.style.width="50px";
	e14.onclick="delFromTable()";
	
	cell14.appendChild(e14);
}	

function loadJuryList(){	
	$("#box-list-jury").slideUp(300);
	if(document.getElementById("filter-by").value != '')
	{
		fb = document.getElementById("filter-by").value;
	}
	if(document.getElementById("filter-by-students").value!='')
	{
		fbs = document.getElementById("filter-by-students").value;
	}
	if(document.getElementById("select-defense-session").value!='')
	{
		ss = document.getElementById("select-defense-session").value;
	}
	xmlhttp = getXMLObject();
	if(xmlhttp){
		//alert("loadJuryList");
	   var xmlStr = "<?xml version=" + "\"" + "1.0" + "\"" + " encoding=" + "\"" + "UTF-8" + "\"" + "?>";
	   xmlStr += "\n" + "<LoadJuryByStudents>" + "\n";
	   xmlStr += "<SortBy>" + document.getElementById("sortby").value + "</SortBy>\n";
	   xmlStr += "<FilterBy>" + fb + "</FilterBy>\n";
	   xmlStr += "<FilterByStudents>" + fbs + "</FilterByStudents>\n";
	   xmlStr += "<session>" + ss +  "</session>";
	   xmlStr += "</LoadJuryByStudents>";
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
	var table = document.getElementById("list-jury");
	clearTable(table);
	// $("#box-list-jury").slideUp(300);
	//alert(xmlhttp.readyState+"/"+xmlhttp.status);
	if(xmlhttp.readyState == 4){
		if(xmlhttp.status == 200){
			var xmlData = xmlhttp.responseXML;
			var jr = xmlData.getElementsByTagName("Jury");
			var table = document.getElementById("list-jury");
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
			
			juries = new Array();
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
				
				var jury = new Object();
				jury.studentID = studentID;
				jury.supervisorId = supervisorId;
				jury.examiner1Id = examiner1Id;
				jury.examiner2Id = examiner2Id;
				jury.presidentId = presidentId;
				jury.secretaryId = secretaryId;
				jury.memberId = memberId;
				jury.slot = slot;
				jury.room = room;
				juries.push(jury);
				
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
						presidentId,presidentName,secretaryId,secretaryName,memberId,memberName,slot,room,'black');
			}			
			tablecontents += "</tbody>";
			tablecontents += "</table>";
			//document.getElementById("jury-table").innerHTML = tablecontents;
			if(jr.length > 0){		
				$('#mes-nojury').slideUp(400)
				$('#box-list-jury').slideDown(400);
			}else{
				$('#mes-nojury').slideDown(400)
				$('#box-list-jury').slideUp(400);
			}
			
		}
	}
	
}

function resetAllDisplayedList(){
	var table = document.getElementById("list-jury");
	clearTable(table);
	$("#box-list-jury").slideUp(300);
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
		   var tblSubjectMatch = document.getElementById("subject-category-match");
		   for(i = 1; i < tblSubjectMatch.rows.length; i++){
			   var subject_category_id = document.getElementById("subject-category-id-" + i).value;
			   var subject_category_name = document.getElementById("subject-category-name-" + i).value;
			   var matchScore = document.getElementById("match-score-" + i).value;
			   xmlStr += "<student-subject-category-match>";
			   xmlStr += "<student-id>" + name + "</student-id>";
			   xmlStr += "<subject-category-id>" + subject_category_id + "</subject-category-id>";
			   xmlStr += "<match-score>" + matchScore + "</match-score>";
			   xmlStr += "</student-subject-category-match>";
		   }
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
				var id = rooms[i].getElementsByTagName("id")[0].firstChild.nodeValue;
				var name = rooms[i].getElementsByTagName("name")[0].firstChild.nodeValue;
				var nbJuries = rooms[i].getElementsByTagName("nb-juries")[0].firstChild.nodeValue;
				var nbHust = rooms[i].getElementsByTagName("nb-hust")[0].firstChild.nodeValue;
				var nbNonHust = rooms[i].getElementsByTagName("nb-nonhust")[0].firstChild.nodeValue;
				addRowRoom(table_rooms,id,name,nbJuries,nbNonHust,nbHust);
			}

		}
	}
}

function sortJurySupervisor(){
	sortBy = "Supervisor";
	sortJury();
}
function sortJuryExaminer1(){
	sortBy = "Examiner1";
	sortJury();
}
function sortJuryExaminer2(){
	sortBy = "Examiner2";
	sortJury();
}
function sortJuryPresident(){
	sortBy = "President";
	sortJury();
}
function sortJurySecretary(){
	sortBy = "Secretary";
	sortJury();
}
function sortJuryAdditionalMember(){
	sortBy = "AdditionalMember";
	sortJury();
}
function sortJurySlot(){
	sortBy = "Slot-Room";
	sortJury();
}
function sortJuryRoom(){
	sortBy = "Room-Slot";
	sortJury();
}

function sortJury(){
	//alert("check consistency");
	if(xmlhttp){
		//alert("Add teacher");
		var table = document.getElementById("list-jury");
		var sz = table.rows.length;
		
		var xmlStr = "<?xml version=" + "\"" + "1.0" + "\"" + " encoding=" + "\"" + "UTF-8" + "\"" + "?>";

	   	xmlStr += "<SortJury>\n";
	   	//xmlStr += "<SortBy>" + document.getElementById("sortby").value + "</SortBy>\n";
	   	xmlStr += "<SortBy>" + sortBy + "</SortBy>\n";
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
		
		xmlStr += "</SortJury>\n";
	   	//alert(xmlStr);
	    //xmlhttp.open("POST","TeacherManager",true); //getname will be the servlet name
	    xmlhttp.open("POST","DefenseScheduleManager",true); //getname will be the servlet name
	    xmlhttp.onreadystatechange  = handleSortJury;
	    //xmlhttp.onreadystatechange  = handleSubmitPoint;
	    xmlhttp.overrideMimeType('text/xml');
	    xmlhttp.setRequestHeader('Content-Type', 'text/xml');
	    xmlhttp.send(xmlStr); //Posting to Servlet

	    //xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	    //xmlhttp.send("xml="+xml); //Posting to Servlet
	}
	
}

function handleSortJury(){
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
			
			/*
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
				var id = rooms[i].getElementsByTagName("id")[0].firstChild.nodeValue;
				var name = rooms[i].getElementsByTagName("name")[0].firstChild.nodeValue;
				var nbJuries = rooms[i].getElementsByTagName("nb-juries")[0].firstChild.nodeValue;
				var nbHust = rooms[i].getElementsByTagName("nb-hust")[0].firstChild.nodeValue;
				var nbNonHust = rooms[i].getElementsByTagName("nb-nonhust")[0].firstChild.nodeValue;
				addRowRoom(table_rooms,id,name,nbJuries,nbNonHust,nbHust);
			}
			*/
		}
	}
}

function prepareStore(){
	//alert("check consistency");
	if(xmlhttp){
		//alert("Add teacher");
		var table = document.getElementById("list-jury");
		var sz = table.rows.length;
		
		var xmlStr = "<?xml version=" + "\"" + "1.0" + "\"" + " encoding=" + "\"" + "UTF-8" + "\"" + "?>";

	   	xmlStr += "<PrepareStore>\n";
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
		
		xmlStr += "</PrepareStore>\n";
	   	//alert(xmlStr);
	    //xmlhttp.open("POST","TeacherManager",true); //getname will be the servlet name
	    xmlhttp.open("POST","DefenseScheduleManager",true); //getname will be the servlet name
	    xmlhttp.onreadystatechange  = handlePrepareStore;
	    //xmlhttp.onreadystatechange  = handleSubmitPoint;
	    xmlhttp.overrideMimeType('text/xml');
	    xmlhttp.setRequestHeader('Content-Type', 'text/xml');
	    xmlhttp.send(xmlStr); //Posting to Servlet

	    //xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	    //xmlhttp.send("xml="+xml); //Posting to Servlet
	}
	
}

function handlePrepareStore(){
	if(xmlhttp.readyState == 4){
		if(xmlhttp.status == 200){
			var xmlData = xmlhttp.responseXML;
			
			/*
			var jr = xmlData.getElementsByTagName("Jury");
			var table = document.getElementById("list-jury");
			clearTable(table);
			

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
				
				
				
				
				var color = 'black';
				if(conflict == 1) 
					color = 'red';
				
				//addRow(studentID,studentName,thesis_title,examiner1Name,examiner2Name,presidentName,secretaryName,memberName,slot,room,color);
				addRow(studentID,studentName,thesis_title,supervisorId,supervisorName,examiner1Id,examiner1Name,examiner2Id,examiner2Name,
						presidentId,presidentName,secretaryId,secretaryName,memberId,memberName,slot,room,color);
			}
			*/
			//alert(xmlData.xml);
			var slots = xmlData.getElementsByTagName("Slots")[0].getElementsByTagName("slot");
			//alert("slots = " + slots.length);
			var table_slots = document.getElementById("list-slots");
			clearTable(table_slots);
			for(i = 0; i < slots.length; i++){
				var slotId = slots[i].getElementsByTagName("id")[0].firstChild.nodeValue;
				addRowSlot(table_slots,slotId,"-");
			}
			
			var rooms = xmlData.getElementsByTagName("Rooms")[0].getElementsByTagName("room");
			var table_rooms = document.getElementById("list-rooms");
			clearTable(table_rooms);
			for(i = 0; i < rooms.length; i++){
				var id = rooms[i].getElementsByTagName("id")[0].firstChild.nodeValue;
				var name = "-";//rooms[i].getElementsByTagName("name")[0].firstChild.nodeValue;
				var nbJuries = rooms[i].getElementsByTagName("nb-juries")[0].firstChild.nodeValue;
				var nbHust = rooms[i].getElementsByTagName("nb-hust")[0].firstChild.nodeValue;
				var nbNonHust = rooms[i].getElementsByTagName("nb-nonhust")[0].firstChild.nodeValue;
				addRowRoom(table_rooms,id,name,nbJuries,nbNonHust,nbHust);
			}

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

	   	xmlStr += "<SearchSolutionJuriesMembersBasedOnSlots>\n";
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
		
		xmlStr += "<room-infos>";
		var tblR = document.getElementById("list-rooms");
		//alert(tblR.rows.length);
		for(i = 1; i < tblR.rows.length; i++){
			var rid = document.getElementById("room-info-id-" + i).value;
			var nbJuries = document.getElementById("room-info-nbJuries-" + i).value;
			var nbNonHust = document.getElementById("room-info-nbNonHust-" + i).value;
			var nbHust = document.getElementById("room-info-nbHust-" + i).value;
			xmlStr += "<room-info>";
			xmlStr += "<id>" + rid + "</id>";
			xmlStr += "<nbJuries>" + nbJuries + "</nbJuries>";
			xmlStr += "<nbNonHust>" + nbNonHust + "</nbNonHust>";
			xmlStr += "<nbHust>" + nbHust + "</nbHust>";
			xmlStr += "</room-info>";
		}
		xmlStr += "</room-infos>";
		
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
		xmlStr += "</SearchSolutionJuriesMembersBasedOnSlots>\n";
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
				msg = decodeURIComponent("Thong bao: " + msg);
				alert(msg);
			
				return;
			}else{
				msg = "Xep duoc lich";
			
				alert(msg);
				
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
				addRowSlot(table_slots,slotId,"-");
			}
			
			var rooms = xmlData.getElementsByTagName("room");
			var table_rooms = document.getElementById("list-rooms");
			clearTable(table_rooms);
			for(i = 0; i < rooms.length; i++){
				var id = rooms[i].getElementsByTagName("id")[0].firstChild.nodeValue;
				var name = rooms[i].getElementsByTagName("name")[0].firstChild.nodeValue;
				var nbJuries = rooms[i].getElementsByTagName("nb-juries")[0].firstChild.nodeValue;
				var nbHust = rooms[i].getElementsByTagName("nb-hust")[0].firstChild.nodeValue;
				var nbNonHust = rooms[i].getElementsByTagName("nb-nonhust")[0].firstChild.nodeValue;
				addRowRoom(table_rooms,id,name,nbJuries,nbNonHust,nbHust);
			}

		}
	}
}


function balanceLoad(){
	//alert("check consistency");
	if(xmlhttp){
		//alert("Add teacher");
		var table = document.getElementById("list-jury");
		var sz = table.rows.length;
		
		var xmlStr = "<?xml version=" + "\"" + "1.0" + "\"" + " encoding=" + "\"" + "UTF-8" + "\"" + "?>";

	   	xmlStr += "<BalanceLoad>\n";
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
		
		xmlStr += "<room-infos>";
		var tblR = document.getElementById("list-rooms");
		//alert(tblR.rows.length);
		for(i = 1; i < tblR.rows.length; i++){
			var rid = document.getElementById("room-info-id-" + i).value;
			var nbJuries = document.getElementById("room-info-nbJuries-" + i).value;
			var nbNonHust = document.getElementById("room-info-nbNonHust-" + i).value;
			var nbHust = document.getElementById("room-info-nbHust-" + i).value;
			xmlStr += "<room-info>";
			xmlStr += "<id>" + rid + "</id>";
			xmlStr += "<nbJuries>" + nbJuries + "</nbJuries>";
			xmlStr += "<nbNonHust>" + nbNonHust + "</nbNonHust>";
			xmlStr += "<nbHust>" + nbHust + "</nbHust>";
			xmlStr += "</room-info>";
		}
		xmlStr += "</room-infos>";
		
		//xmlStr += "<slots>" + document.getElementById("slots").value + "</slots>";
		//xmlStr += "<rooms>" + document.getElementById("rooms").value + "</rooms>";
		xmlStr += "<time>" + document.getElementById("time-limit").value + "</time>";
		xmlStr += "<algorithm>" + document.getElementById("balance").value + "</algorithm>";
		xmlStr += "<jury-members>";
	   	//xmlStr += document.getElementById("jury-members-xml").value;
	   	var selectedMembers = document.getElementById("selected-members");
	   	for(i = 0; i < selectedMembers.options.length; i++){
	   		var memberID = selectedMembers.options[i].value;
	   		xmlStr += '<jury-member>' + memberID + '</jury-member>' + "\n";
	   	}
	   	xmlStr += "</jury-members>";
	   	xmlStr += "<nb-juries>" + document.getElementById("nbr-juries").value + "</nb-juries>";
		xmlStr += "</BalanceLoad>\n";
	   	//alert(xmlStr);
	    //xmlhttp.open("POST","TeacherManager",true); //getname will be the servlet name
	    xmlhttp.open("POST","DefenseScheduleManager",true); //getname will be the servlet name
	    xmlhttp.onreadystatechange  = handleBalanceLoad;
	    //xmlhttp.onreadystatechange  = handleSubmitPoint;
	    xmlhttp.overrideMimeType('text/xml');
	    xmlhttp.setRequestHeader('Content-Type', 'text/xml');
	    xmlhttp.send(xmlStr); //Posting to Servlet

	    //xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	    //xmlhttp.send("xml="+xml); //Posting to Servlet
	}
	
}

function handleBalanceLoad(){
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
				addRowSlot(table_slots,slotId,"-");
			}
			
			var rooms = xmlData.getElementsByTagName("room");
			var table_rooms = document.getElementById("list-rooms");
			clearTable(table_rooms);
			for(i = 0; i < rooms.length; i++){
				var id = rooms[i].getElementsByTagName("id")[0].firstChild.nodeValue;
				var name = rooms[i].getElementsByTagName("name")[0].firstChild.nodeValue;
				var nbJuries = rooms[i].getElementsByTagName("nb-juries")[0].firstChild.nodeValue;
				var nbHust = rooms[i].getElementsByTagName("nb-hust")[0].firstChild.nodeValue;
				var nbNonHust = rooms[i].getElementsByTagName("nb-nonhust")[0].firstChild.nodeValue;
				addRowRoom(table_rooms,id,name,nbJuries,nbNonHust,nbHust);
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
			alert("Luu thanh cong!!")
		}
	}
}
$(document).ready(function(){
  	loadDataBase();
  	// loadJuryList();
});