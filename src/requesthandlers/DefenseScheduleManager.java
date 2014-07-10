package requesthandlers;



import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
//import java.util.HashMap;
//import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import algorithms.backtracksearch.*;

import utils.Configure;
import utils.Utility;
import utils.SendMail;

import DataEntity.*;
//import DataEntity.Jury;
//import DataEntity.JuryInfo;
//import DataEntity.Student;
//import DataEntity.Teacher;



public class DefenseScheduleManager extends HttpServlet {

	/**
	 * @param args
	 */
	/*
	private Connection cnn = null;
	private Statement stat = null;
	private PreparedStatement preparedStat = null;
	private ResultSet rs = null;
	
	//private String cnnStr = "jdbc:mysql://localhost:3306/shedulerdefense?user=root&password=";
	//private String cnnStr = "jdbc:mysql://localhost:3306/training_manager?user=root&password=";
	private String cnnStr = "jdbc:mysql://localhost:3306/itraining_management_db?user=root&password=";
	private String tblProfessors = "professors";
	private String tblStudents = "supervise_students";
	private String tblStudentDefense = "student_defense";
	*/
	
	public DefenseScheduleManager(){
		super();
		System.out.println("TeacherManager -> constructor");
	}
	public void init(ServletConfig config) throws ServletException { 
		super.init(config);
		System.out.println("CCMapManager -> init");
	}
 
	public void destroy() {
		System.out.print("Destroy");
	}
	
	/*
	public Vector<Teacher> getTeachers(){
		Vector<Teacher> teachers = new Vector<Teacher>();
		//System.out.println("Start JDBC");
		try{
			
			//System.out.println("Start JDBC");
			Class.forName("com.mysql.jdbc.Driver");
			//System.out.println("JDBC -->OK");
			cnn = DriverManager.getConnection(cnnStr);
			stat = cnn.createStatement();
			rs = stat.executeQuery("select * from " + tblProfessors);
			//rs = stat.executeQuery("select * from students");
			while(rs.next()){
				//System.out.println(rs.getString("name"));
				//System.out.println(rs.getInt("idgv"));
				String name = rs.getString("Name");
				int id = rs.getInt("ID");
				//int id = rs.getInt("idsv");
				Teacher t = new Teacher(id,name);
				teachers.add(t);
			}
			//out.println("server return results");
			
			System.out.println("get teachers successfully");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close();
		}
		return teachers;
	}
	public Vector<Student> getStudents(){
		Vector<Student> students = new Vector<Student>();
		//System.out.println("Start JDBC");
		try{
			
			//System.out.println("Start JDBC");
			Class.forName("com.mysql.jdbc.Driver");
			//System.out.println("JDBC -->OK");
			cnn = DriverManager.getConnection(cnnStr);
			stat = cnn.createStatement();
			rs = stat.executeQuery("select * from " + tblStudents);
			//rs = stat.executeQuery("select * from students");
			while(rs.next()){
				//System.out.println(rs.getString("name"));
				//System.out.println(rs.getInt("idgv"));
				String name = rs.getString("StudentName");
				//int id = Integer.valueOf(rs.getString("ID"));
				int id = rs.getInt("ID");
				//int id = rs.getInt("idsv");
				Student st = new Student(id,name);
				students.add(st);
			}
			//out.println("server return results");
			
			System.out.println("get students successfully");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close();
		}
		return students;
	}

	public Vector<Jury> getJuries(){
		Vector<Jury> juries = new Vector<Jury>();
		//System.out.println("Start JDBC");
		try{
			
			//System.out.println("Start JDBC");
			Class.forName("com.mysql.jdbc.Driver");
			//System.out.println("JDBC -->OK");
			cnn = DriverManager.getConnection(cnnStr);
			stat = cnn.createStatement();
			//rs = stat.executeQuery("SELECT S.ID as ID, S.Title as ThesisTitle, ST.ID as StudentID, ST.StudentName, P1.Name as Examiner1Name, P1.ID as Examiner1ID, " +
			//" P2.Name as Examiner2Name, P2.ID as Examiner2ID, P3.Name as PresidentName, P3.ID as PresidentID, " + 
			//		"P4.Name as SecretaryName, P4.ID as SecretaryID, P5.Name as MemberName, P5.ID as MemberID FROM " + 
			//" student_defense as S, supervise_students as ST, professors as P1, professors as P2, professors as P3, " + 
			//		" professors as P4, professors as P5 where S.examiner1 = P1.ID and S.examiner2 = P2.ID and S.president = P3.ID and " + 
			//" S.secretary = P4.ID and S.additionalMember = P5.ID  and ST.ID = S.StudentID");

			rs = stat.executeQuery("SELECT S.ID as ID, S.Title as ThesisTitle, ST.ID as StudentID, ST.StudentName, P1.Name as Examiner1Name, P1.ID as Examiner1ID, " +
			" P2.Name as Examiner2Name, P2.ID as Examiner2ID, P3.Name as PresidentName, P3.ID as PresidentID, " + 
					"P4.Name as SecretaryName, P4.ID as SecretaryID, P5.Name as MemberName, P5.ID as MemberID, S.Slot as Slot, S.Room as Room FROM " + 
			" student_defense as S, supervise_students as ST, professors as P1, professors as P2, professors as P3, " + 
					" professors as P4, professors as P5 where S.examiner1 = P1.ID and S.examiner2 = P2.ID and S.president = P3.ID and " + 
			" S.secretary = P4.ID and S.additionalMember = P5.ID  and ST.ID = S.StudentID");
			
			while(rs.next()){
				int id = rs.getInt("ID");
				int studentID = rs.getInt("StudentID");
				String studentName = rs.getString("StudentName");
				int examiner1ID = rs.getInt("Examiner1ID");
				String examiner1Name = rs.getString("Examiner1Name");
				int examiner2ID = rs.getInt("Examiner2ID");
				String examiner2Name = rs.getString("Examiner2Name");
				int presidentID = rs.getInt("PresidentID");
				String presidentName = rs.getString("Presidentname");
				int secretaryID = rs.getInt("SecretaryID");
				String secretaryName = rs.getString("SecretaryName");
				int memberID = rs.getInt("MemberID");
				String memberName = rs.getString("MemberName");
				int slot = rs.getInt("Slot");
				int room = rs.getInt("Room");
				
				String thesis_title = rs.getString("ThesisTitle");
				Jury jr = new Jury(studentID, studentName, thesis_title, examiner1ID, examiner1Name, examiner2ID,
						examiner2Name, presidentID, presidentName, secretaryID, secretaryName, memberID, memberName);
				
				
				juries.add(jr);
			}
			//out.println("server return results");
			
			System.out.println("get students successfully");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close();
		}
		return juries;
	}
	
	public Vector<JuryInfo> getListJuryInfo(String sortBy){
		Vector<JuryInfo> juries = new Vector<JuryInfo>();
		//System.out.println("Start JDBC");
		try{
			
			//System.out.println("Start JDBC");
			Class.forName("com.mysql.jdbc.Driver");
			//System.out.println("JDBC -->OK");
			cnn = DriverManager.getConnection(cnnStr);
			stat = cnn.createStatement();
			//rs = stat.executeQuery("SELECT S.ID as ID, S.Title as ThesisTitle, ST.ID as StudentID, ST.StudentName, P1.Name as Examiner1Name, P1.ID as Examiner1ID, " +
			//" P2.Name as Examiner2Name, P2.ID as Examiner2ID, P3.Name as PresidentName, P3.ID as PresidentID, " + 
			//		"P4.Name as SecretaryName, P4.ID as SecretaryID, P5.Name as MemberName, P5.ID as MemberID FROM " + 
			//" student_defense as S, supervise_students as ST, professors as P1, professors as P2, professors as P3, " + 
			//		" professors as P4, professors as P5 where S.examiner1 = P1.ID and S.examiner2 = P2.ID and S.president = P3.ID and " + 
			//" S.secretary = P4.ID and S.additionalMember = P5.ID  and ST.ID = S.StudentID");

			String sql = "SELECT S.ID as ID, S.Title as ThesisTitle, ST.ID as StudentID, ST.StudentName, P1.Name as Examiner1Name, P1.ID as Examiner1ID, " +
			" P2.Name as Examiner2Name, P2.ID as Examiner2ID, P3.Name as PresidentName, P3.ID as PresidentID, " + 
					"P4.Name as SecretaryName, P4.ID as SecretaryID, P5.Name as MemberName, P5.ID as MemberID, S.Slot as Slot, S.Room as Room, S.SessionID as SessionID FROM " + 
			" student_defense as S, supervise_students as ST, professors as P1, professors as P2, professors as P3, " + 
					" professors as P4, professors as P5 where S.examiner1 = P1.ID and S.examiner2 = P2.ID and S.president = P3.ID and " + 
			" S.secretary = P4.ID and S.additionalMember = P5.ID  and ST.ID = S.StudentID";
			
			if(sortBy.compareTo("StudentID") == 0){
				sql += " order by StudentID";
			}else if(sortBy.compareTo("Slot")==0){
				sql += " order by Slot";
			}else if(sortBy.compareTo("Room")==0){
				sql += " order by Room";
			}
			rs = stat.executeQuery(sql);
			
			while(rs.next()){
				int id = rs.getInt("ID");
				int studentID = rs.getInt("StudentID");
				String studentName = rs.getString("StudentName");
				int examiner1ID = rs.getInt("Examiner1ID");
				String examiner1Name = rs.getString("Examiner1Name");
				int examiner2ID = rs.getInt("Examiner2ID");
				String examiner2Name = rs.getString("Examiner2Name");
				int presidentID = rs.getInt("PresidentID");
				String presidentName = rs.getString("Presidentname");
				int secretaryID = rs.getInt("SecretaryID");
				String secretaryName = rs.getString("SecretaryName");
				int memberID = rs.getInt("MemberID");
				String memberName = rs.getString("MemberName");
				int slot = rs.getInt("Slot");
				int room = rs.getInt("Room");
				int id_data_set = rs.getInt("SessionID");
				String thesis_title = rs.getString("ThesisTitle");
				JuryInfo jr = new JuryInfo(id, studentName, thesis_title, "supervisor_null", examiner1Name,
						examiner2Name, presidentName, secretaryName, memberName,id_data_set);
				
				jr.setStudentID(studentID);
				jr.setSlotId(slot);
				jr.setRoomId(room);
				juries.add(jr);
			}
			//out.println("server return results");
			
			int n = juries.size();
			JuryInfo[] s_jury = new JuryInfo[n];
			for(int i = 0; i < n; i++){
				s_jury[i] = juries.get(i);
			}
			if(sortBy.compareTo("Room-Slot")==0){
				for(int i = 0; i < n-1; i++){
					for(int j = i+1; j < n; j++){
						if(s_jury[i].getRoomId() > s_jury[j].getRoomId() ||
							s_jury[i].getRoomId() == s_jury[j].getRoomId() && s_jury[i].getSlotId() > s_jury[j].getSlotId()	){
							JuryInfo t = s_jury[i]; s_jury[i] = s_jury[j]; s_jury[j] = t;
						}
					}
				}
			}else if(sortBy.compareTo("Slot-Room")==0){
				for(int i = 0; i < n-1; i++){
					for(int j = i+1; j < n; j++){
						if(s_jury[i].getSlotId() > s_jury[j].getSlotId() ||
							s_jury[i].getSlotId() == s_jury[j].getSlotId() && s_jury[i].getRoomId() > s_jury[j].getRoomId()	){
							JuryInfo t = s_jury[i]; s_jury[i] = s_jury[j]; s_jury[j] = t;
						}
					}
				}
				
			}
			juries.clear();
			for(int i = 0; i < n; i++){
				juries.add(s_jury[i]);
			}
			System.out.println("get students successfully");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close();
		}
		return juries;
	}
	 */
	
	
	public void processListTeachers(HttpServletResponse response){
		try{
			PrintWriter out = response.getWriter();
			System.out.println("getTeachers");
			Vector<Teacher> teachers = Utility.getTeachers();
			out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			//System.out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			out.println("<Teachers>");
			//System.out.println("<Teachers>");
			for(int i = 0; i < teachers.size(); i++){
				Teacher t = teachers.get(i);
				out.println("<teacher>" + "<id>" + t.getID() + "</id><name>" + t.getName() + "</name></teacher>");
				//System.out.println("<teacher>" + "<id>" + t.getID() + "</id><name>" + t.getName() + "</name></teacher>");
			}
			out.println("</Teachers>");
			//System.out.println("</Teachers>");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/*
	public void processListStudents(HttpServletResponse response){
		try{
			PrintWriter out = response.getWriter();
			System.out.println("getTeachers");
			Vector<Student> students = Utility.getStudents();
			out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			//System.out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			out.println("<Students>");
			//System.out.println("<Teachers>");
			for(int i = 0; i < students.size(); i++){
				Student t = students.get(i);
				out.println("<student>" + "<id>" + t.getID() + "</id><name>" + t.getName() + "</name></student>");
				//System.out.println("<teacher>" + "<id>" + t.getID() + "</id><name>" + t.getName() + "</name></teacher>");
			}
			out.println("</Students>");
			//System.out.println("</Teachers>");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	*/
	
	public void processListTeachersStudents(HttpServletResponse response){
		try{
			PrintWriter out = response.getWriter();
			System.out.println("getTeachersStudents");
			Vector<Teacher> teachers = Utility.getTeachers();
			Vector<Student> students = Utility.getStudents();
			System.out.println("number of teachers = " + teachers.size() + " number of students =" + students.size());
			
			out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			//System.out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			out.println("<TeachersStudents>");
			out.println("<Students>");
			//System.out.println("<Teachers>");
			for(int i = 0; i < students.size(); i++){
				Student t = students.get(i);
				out.println("<student>" + "<id>" + t.getID() + "</id><name>" + t.getName() + "</name></student>");
				//System.out.println("<teacher>" + "<id>" + t.getID() + "</id><name>" + t.getName() + "</name></teacher>");
			}
			out.println("</Students>");
			
			out.println("<Teachers>");
			//System.out.println("<Teachers>");
			for(int i = 0; i < teachers.size(); i++){
				Teacher t = teachers.get(i);
				out.println("<teacher>" + "<id>" + t.getID() + "</id><name>" + t.getName() + "</name></teacher>");
				//System.out.println("<teacher>" + "<id>" + t.getID() + "</id><name>" + t.getName() + "</name></teacher>");
			}
			out.println("</Teachers>");
			out.println("</TeachersStudents>");
			//System.out.println("</Teachers>");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void processListStudentsOfDepartment(Document doc, HttpServletResponse response){
		NodeList nl = doc.getElementsByTagName("department");
		Node nod = nl.item(0);
		Element e = (Element)nod;
		String dept = e.getChildNodes().item(0).getNodeValue();
		System.out.println("DefenseScheduleManager::processListStudentsOfDepartment, detp = " + dept);
		
		try{
			PrintWriter out = response.getWriter();
			//System.out.println("getTeachersStudents");
			Vector<JuryInfo> jury = Utility.getListJuryInfo("StudentID",dept);
			Vector<Student> students = Utility.getNotDefensedStudents();//list only student is in the status 2 (received subject)
			HashMap<Integer, String> mStudent = new HashMap<Integer, String>();
			for(int i = 0; i < students.size(); i++){
				Student st = students.get(i);
				mStudent.put(st.getID(), st.getName());
			}
			
			out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			//System.out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			out.println("<StudentsOfDepartment>");
			out.println("<Students>");
			//System.out.println("<Teachers>");
			for(int i = 0; i < jury.size(); i++){
				JuryInfo J = jury.get(i);
				int sid = J.getStudentID();
				out.println("<student>" + "<id>" + sid + "</id><name>" + mStudent.get(sid) + "</name></student>");
				//System.out.println("<teacher>" + "<id>" + t.getID() + "</id><name>" + t.getName() + "</name></teacher>");
			}
			out.println("</Students>");
			
			out.println("</StudentsOfDepartment>");
			//System.out.println("</Teachers>");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public void processListTeachersOfDepartment(Document doc, HttpServletResponse response){
		NodeList nl = doc.getElementsByTagName("department");
		Node nod = nl.item(0);
		Element e = (Element)nod;
		String dept = e.getChildNodes().item(0).getNodeValue();
		System.out.println("DefenseScheduleManager::processListTeachersOfDepartment, detp = " + dept);
		
		try{
			PrintWriter out = response.getWriter();
			//System.out.println("getTeachersStudents");
			/*
			Vector<JuryInfo> jury = Utility.getListJuryInfo("StudentID",dept);
			Vector<Student> students = Utility.getNotDefensedStudents();//list only student is in the status 2 (received subject)
			HashMap<Integer, String> mStudent = new HashMap<Integer, String>();
			for(int i = 0; i < students.size(); i++){
				Student st = students.get(i);
				mStudent.put(st.getID(), st.getName());
			}
			*/
			
			Vector<Teacher> teachers = Utility.getTeachers();
			Vector<Department> depts = Utility.getDepartments();
			Vector<Teacher> sel_teachers = new Vector<Teacher>();
			for(int i = 0; i < teachers.size(); i++){
				Teacher t = teachers.get(i);
				if(t.getDepartment().equals(dept) || dept.equals("All")){
					sel_teachers.add(t);
				}
			}
			
			out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			//System.out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			out.println("<TeachersOfDepartment>");
			out.println("<Teachers>");
			//System.out.println("<Teachers>");
			for(int i = 0; i < sel_teachers.size(); i++){
				Teacher t = sel_teachers.get(i);
				int tid = t.getID();
				out.println("<teacher>" + "<id>" + tid + "</id><name>" + t.getName() + "</name></teacher>");
				//System.out.println("<teacher>" + "<id>" + t.getID() + "</id><name>" + t.getName() + "</name></teacher>");
			}
			out.println("</Teachers>");
			
			out.println("</TeachersOfDepartment>");
			//System.out.println("</Teachers>");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void processListTeachersStudentsDepartments(HttpServletResponse response){
		try{
			PrintWriter out = response.getWriter();
			//System.out.println("getTeachersStudents");
			Vector<Teacher> teachers = Utility.getTeachers();
			//Vector<Student> students = Utility.getStudents();
			Vector<Student> students = Utility.getNotDefensedStudents();//list only student is in the status 2 (received subject)
			Vector<Department> departments = Utility.getDepartments();
			Vector<DefenseSession> sessions = Utility.getDefenseSessions(1);
			Vector<SubjectCategory> categories = Utility.getSubjectCategories();
			
			System.out.println("number of teachers = " + teachers.size() + " number of students =" + students.size());
			
			out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			//System.out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			out.println("<TeachersStudentsDepartments>");
			out.println("<Students>");
			//System.out.println("<Teachers>");
			for(int i = 0; i < students.size(); i++){
				Student t = students.get(i);
				out.println("<student>" + "<id>" + t.getID() + "</id><name>" + t.getName() + "</name></student>");
				//System.out.println("<teacher>" + "<id>" + t.getID() + "</id><name>" + t.getName() + "</name></teacher>");
			}
			out.println("</Students>");
			
			out.println("<Teachers>");
			//System.out.println("<Teachers>");
			for(int i = 0; i < teachers.size(); i++){
				Teacher t = teachers.get(i);
				out.println("<teacher>" + "<id>" + t.getID() + "</id><name>" + t.getName() + "</name></teacher>");
				//System.out.println("<teacher>" + "<id>" + t.getID() + "</id><name>" + t.getName() + "</name></teacher>");
			}
			out.println("</Teachers>");
			
			out.println("<Departments>");
			//System.out.println("<Teachers>");
			for(int i = 0; i < departments.size(); i++){
				Department dep = departments.get(i);
				out.println("<department>" + "<id>" + dep.getID() + "</id><name>" + dep.getName() + "</name></department>");
				//System.out.println("<teacher>" + "<id>" + t.getID() + "</id><name>" + t.getName() + "</name></teacher>");
			}
			out.println("</Departments>");			
			
			out.println("<subject-categories>");
			for(int i = 0; i < categories.size(); i++){
				SubjectCategory cat = categories.get(i);
				out.println("<subject-category>");
				out.println("<id>" + cat.getID() + "</id>");
				out.println("<name>" + cat.getName() + "</name>");
				out.println("</subject-category>");
			}
			out.println("</subject-categories>");
			
			out.println("<DefenseSession>");
			for(int i = 0; i < sessions.size(); i++){
				DefenseSession s = sessions.get(i);
				out.println("<defensesession>");
				out.println("<id>" + s.getID() + "</id>");
				out.println("<description>" + s.getDescription() + "</description>");
				out.println("<active>" + s.getActive() + "</active>");
				out.println("</defensesession>");
				
				System.out.println("<defensesession>");
				System.out.println("<id>" + s.getID() + "</id>");
				System.out.println("<description>" + s.getDescription() + "</description>");
				System.out.println("<active>" + s.getActive() + "</active>");
				System.out.println("</defensesession>");
			}
			out.println("</DefenseSession>");
			out.println("</TeachersStudentsDepartments>");
			//System.out.println("</Teachers>");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void processListTeachersStudentsDepartmentsRooms(HttpServletResponse response){
		try{
			PrintWriter out = response.getWriter();
			System.out.println("getTeachersStudents");
			Vector<Teacher> teachers = Utility.getTeachers();
			//Vector<Student> students = Utility.getStudents();
			Vector<Student> students = Utility.getNotDefensedStudents();//list only student is in the status 2 (received subject)
			Vector<Department> departments = Utility.getDepartments();
			Vector<DefenseSession> sessions = Utility.getDefenseSessions(1);
			Vector<Room> rooms = Utility.getRooms();
			
			System.out.println("number of teachers = " + teachers.size() + " number of students =" + students.size());
			
			out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			//System.out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			out.println("<TeachersStudentsDepartmentsRooms>");
			out.println("<Students>");
			//System.out.println("<Teachers>");
			for(int i = 0; i < students.size(); i++){
				Student t = students.get(i);
				out.println("<student>" + "<id>" + t.getID() + "</id><name>" + t.getName() + "</name></student>");
				//System.out.println("<teacher>" + "<id>" + t.getID() + "</id><name>" + t.getName() + "</name></teacher>");
			}
			out.println("</Students>");
			
			out.println("<Teachers>");
			//System.out.println("<Teachers>");
			for(int i = 0; i < teachers.size(); i++){
				Teacher t = teachers.get(i);
				out.println("<teacher>" + "<id>" + t.getID() + "</id><name>" + t.getName() + "</name></teacher>");
				//System.out.println("<teacher>" + "<id>" + t.getID() + "</id><name>" + t.getName() + "</name></teacher>");
			}
			out.println("</Teachers>");
			
			out.println("<Departments>");
			//System.out.println("<Teachers>");
			for(int i = 0; i < departments.size(); i++){
				Department dep = departments.get(i);
				out.println("<department>" + "<id>" + dep.getID() + "</id><name>" + dep.getName() + "</name></department>");
				//System.out.println("<teacher>" + "<id>" + t.getID() + "</id><name>" + t.getName() + "</name></teacher>");
			}
			out.println("</Departments>");			
			
			out.println("<Rooms>");
			//System.out.println("<Teachers>");
			for(int i = 0; i < rooms.size(); i++){
				Room r = rooms.get(i);
				out.println("<room>" + "<id>" + r.getID() + "</id><name>" + r.getDescription() + "</name></room>");
				//System.out.println("<teacher>" + "<id>" + t.getID() + "</id><name>" + t.getName() + "</name></teacher>");
			}
			out.println("</Rooms>");			

			out.println("<DefenseSession>");
			for(int i = 0; i < sessions.size(); i++){
				DefenseSession s = sessions.get(i);
				out.println("<defensesession>");
				out.println("<id>" + s.getID() + "</id>");
				out.println("<description>" + s.getDescription() + "</description>");
				out.println("<active>" + s.getActive() + "</active>");
				out.println("</defensesession>");
				
				System.out.println("<defensesession>");
				System.out.println("<id>" + s.getID() + "</id>");
				System.out.println("<description>" + s.getDescription() + "</description>");
				System.out.println("<active>" + s.getActive() + "</active>");
				System.out.println("</defensesession>");
			}
			out.println("</DefenseSession>");
			out.println("</TeachersStudentsDepartmentsRooms>");
			//System.out.println("</Teachers>");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void processLoadAJury(Document doc, HttpServletResponse response){
		NodeList nl = doc.getElementsByTagName("StudentID");
		Node nod = nl.item(0);
		Element e = (Element)nod;
		int studentID = Integer.valueOf(e.getChildNodes().item(0).getNodeValue());
		System.out.println("DefenseScheduleManager::LoadAJury, studentID = " + studentID);	
		
		JuryInfo jr = Utility.getJury(studentID);
		try{
			PrintWriter out = response.getWriter();
			out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			out.println("<Jury>");
			out.println("<StudentID>" + jr.getStudentID() + "</StudentID>");
			out.println("<StudentName>" + jr.getStudentName() + "</StudentName>");
			out.println("<ThesisTitle>" + jr.getTitle() + "</ThesisTitle>");
			out.println("<Examiner1ID>" + jr.getExaminerId1() + "</Examiner1ID>");
			out.println("<Examiner1Name>" + jr.getExaminerName1() + "</Examiner1Name>");
			out.println("<Examiner2ID>" + jr.getExaminerId2() + "</Examiner2ID>");
			out.println("<Examiner2Name>" + jr.getExaminerName2() + "</Examiner2Name>");
			out.println("<PresidentID>" + jr.getPresidentId() + "</PresidentID>");
			out.println("<PresidentName>" + jr.getPresidentName() + "</PresidentName>");
			out.println("<SecretaryID>" + jr.getSecretaryId() + "</SecretaryID>");
			out.println("<SecretaryName>" + jr.getSecretaryName() + "</SecretaryName>");
			out.println("<MemberID>" + jr.getAdditionalMemberId() + "</MemberID>");
			out.println("<MemberName>" + jr.getAdditionalMemberName() + "</MemberName>");
			out.println("<Slot>" + jr.getSlotId() + "</Slot>");
			out.println("<Room>" + jr.getRoomId() + "</Room>");
			out.println("</Jury>");
			
			
			out.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public void processListJury(Document doc, HttpServletResponse response){
		try{
			
			NodeList nl = doc.getElementsByTagName("SortBy");
			Node nod = nl.item(0);
			Element e = (Element)nod;
			String sortBy = e.getChildNodes().item(0).getNodeValue();
			System.out.println("DefenseScheduleManager::listJury, sort by = " + sortBy);
			
			nl = doc.getElementsByTagName("FilterBy");
			nod = nl.item(0);
			e = (Element)nod;
			String filterBy = e.getChildNodes().item(0).getNodeValue();
			System.out.println("DefenseScheduleManager::listJury, filter by = " + filterBy);
			
						
			
			PrintWriter out = response.getWriter();
			System.out.println("getJuries");
			//Vector<Jury> juries = getJuries();
			Vector<JuryInfo> juries = Utility.getListJuryInfo(sortBy,filterBy);
			Vector<Slot> slots = Utility.getSlots();
			HashMap<Integer, Slot> mSlot = new HashMap<Integer, Slot>();
			for(int i = 0; i < slots.size(); i++){
				Slot sl = slots.get(i);
				mSlot.put(sl.getID(), sl);
			}
			Slot sl = new Slot(0,0,"null",0);
			mSlot.put(sl.getID(), sl);
			System.out.println("number of juries = " + juries.size());
			
			out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			//System.out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			out.println("<Juries>");
			for(int i = 0; i < juries.size(); i++){
				JuryInfo jr = juries.get(i);
				out.println("<Jury>");
				out.println("<StudentID>" + jr.getStudentID() + "</StudentID>");
				out.println("<StudentName>" + jr.getStudentName() + "</StudentName>");
				out.println("<ThesisTitle>" + jr.getTitle() + "</ThesisTitle>");
				out.println("<SupervisorID>" + jr.getSupervisorId() + "</SupervisorID>");
				out.println("<SupervisorName>" + jr.getSupervisorName() + "</SupervisorName>");
				out.println("<Examiner1ID>" + jr.getExaminerId1() + "</Examiner1ID>");
				out.println("<Examiner1Name>" + jr.getExaminerName1() + "</Examiner1Name>");
				out.println("<Examiner2ID>" + jr.getExaminerId2() + "</Examiner2ID>");
				out.println("<Examiner2Name>" + jr.getExaminerName2() + "</Examiner2Name>");
				out.println("<PresidentID>" + jr.getPresidentId() + "</PresidentID>");
				out.println("<PresidentName>" + jr.getPresidentName() + "</PresidentName>");
				out.println("<SecretaryID>" + jr.getSecretaryId() + "</SecretaryID>");
				out.println("<SecretaryName>" + jr.getSecretaryName() + "</SecretaryName>");
				out.println("<MemberID>" + jr.getAdditionalMemberId() + "</MemberID>");
				out.println("<MemberName>" + jr.getAdditionalMemberName() + "</MemberName>");
				//out.println("<Slot>" + jr.getSlotId() + "</Slot>");
				int slidx = 0;
				String sldes = "-";
				if(mSlot.get(jr.getSlotId()) != null){
					slidx = mSlot.get(jr.getSlotId()).getSlotIndex();
					sldes = mSlot.get(jr.getSlotId()).getDescription();
				}
				out.println("<Slot>" + slidx + "</Slot>");
				out.println("<Slot-Description>" + sldes + "</Slot-Description>");
				out.println("<Room>" + jr.getRoomId() + "</Room>");
				//System.out.println("<Room>" + jr.getRoomId() + "</Room>");
				out.println("</Jury>");
			}
			out.println("</Juries>");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void processListJuryByStudents(Document doc, HttpServletResponse response){
		try{
			
			NodeList nl = doc.getElementsByTagName("SortBy");
			Node nod = nl.item(0);
			Element e = (Element)nod;
			String sortBy = e.getChildNodes().item(0).getNodeValue();
			System.out.println("DefenseScheduleManager::listJury, sort by = " + sortBy);
			
			nl = doc.getElementsByTagName("FilterBy");
			nod = nl.item(0);
			e = (Element)nod;
			String filterBy = e.getChildNodes().item(0).getNodeValue();
			System.out.println("DefenseScheduleManager::listJury, filter by = " + filterBy);
			
			nl = doc.getElementsByTagName("FilterByStudents");
			nod = nl.item(0);
			e = (Element)nod;
			String filterByStudents = e.getChildNodes().item(0).getNodeValue();
			System.out.println("DefenseScheduleManager::listJury, filter by students = " + filterByStudents);
						
			nl = doc.getElementsByTagName("session");
			nod = nl.item(0);
			e = (Element)nod;
			int sessionID = Integer.valueOf(e.getChildNodes().item(0).getNodeValue());
			System.out.println("DefenseScheduleManager::listJuryByStudents, sessionID = " + sessionID);
			
			PrintWriter out = response.getWriter();
			//System.out.println("getJuries");
			//Vector<Jury> juries = getJuries();
			//Vector<JuryInfo> juries = Utility.getListJuryInfo(sortBy,filterBy);
			Vector<JuryInfo> juries = Utility.getListJuryInfo(sortBy,filterBy,sessionID);
			JuryInfo selJ = null;
			if(!filterByStudents.equals("All")){
				int sid = Integer.valueOf(filterByStudents);
				for(int j = 0; j < juries.size(); j++){
					JuryInfo J = juries.get(j);
					if(J.getStudentID() == sid){
						selJ = J;
						break;
					}
				}
				juries.clear();
				juries.add(selJ);
			}
			
			Vector<Slot> slots = Utility.getSlots();
			HashMap<Integer, Slot> mSlot = new HashMap<Integer, Slot>();
			for(int i = 0; i < slots.size(); i++){
				Slot sl = slots.get(i);
				mSlot.put(sl.getID(), sl);
			}
			
			mSlot.put(0, new Slot(0,0,"null",0));
			//System.out.println("number of juries = " + juries.size());
			
			out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			//System.out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			
			
			Vector<Room> rooms = Utility.getRooms();
			HashMap<Integer, Room> mRoom = new HashMap<Integer,Room>();
			
			for(int i = 0; i < rooms.size(); i++){
				mRoom.put(rooms.get(i).getID(), rooms.get(i));
				//System.out.println(rooms.get(i).getID() + "---------------" + rooms.get(i).getDescription());
			}
			
			HashSet<Room> rooms_used = new HashSet<Room>();
			HashSet<Integer> rooms_id_used = new HashSet<Integer>();
			
			for(int i = 0; i < juries.size(); i++){
				JuryInfo jr = juries.get(i);
				int slotID = jr.getSlotId();
				int roomID = jr.getRoomId();
				System.out.println("DefenseSchedule::processListJuryByStudents, slotID = " + slotID + ", roomID = " + roomID);
				if(mRoom.get(roomID) != null) rooms_used.add(mRoom.get(roomID));
				if(roomID > 0) rooms_id_used.add(roomID);
				String sldes = "-";
				if(mSlot.get(slotID) != null) sldes = mSlot.get(slotID).getDescription(); 
				jr.setSlotDescription(sldes);
				String rdes = "-";
				if(mRoom.get(roomID) != null) rdes = mRoom.get(roomID).getDescription();
				jr.setRoomName(rdes);
			}
			HashMap<Integer, Integer> mRoomID = new HashMap<Integer, Integer>();
			int idx = 0;
			Iterator it = rooms_id_used.iterator();
			while(it.hasNext()){
				int rid = (Integer)it.next();
				idx++;
				mRoomID.put(rid, idx);
				System.out.println("DefenseSchedule::processListJuryByStudents, mRoomID.put(" + rid + "," + idx + ")");
			}
			for(int i = 0; i < juries.size(); i++){
				JuryInfo jr = juries.get(i);
				int newRoomID = 0;
				if(mRoomID.get(jr.getRoomId()) != null) newRoomID = mRoomID.get(jr.getRoomId());
				//jr.setRoomId(mRoomID.get(jr.getRoomId()));
				jr.setRoomId(newRoomID);
			}
			
			Vector<Room> v_rooms_used = Utility.collectVectorRooms(juries);
			Vector<Integer> v_slots_used = Utility.collectVectorSlots(juries);
			
			System.out.print("DefenseScheduleManager::processListJuryByStudents, v_rooms_used[" + v_rooms_used.size() + "] = ");
			for(int i = 0; i < v_rooms_used.size(); i++){
				Room r = v_rooms_used.get(i);
				System.out.println("DefenseSchedule::processListJuryByStudents, reset room_id, old r.id = " + r.getID());
				//r.setID(mRoomID.get(r.getID()));
				System.out.println("DefenseSchedule::processListJuryByStudents, reset room_id, new r.id = " + r.getID());
			}
			System.out.println();
			
			System.out.print("DefenseScheduleManager::processListJuryByStudents, v_slots_used[" + v_slots_used.size() + "] = ");
			for(int i = 0; i < v_slots_used.size(); i++){
				int sl = v_slots_used.get(i);
				System.out.print(sl + " ");
			}
			System.out.println();
			
			//String str = "<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>";
			out.println("<FullJuryInfo>");
			//out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			//System.out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");

			
			HashMap<Integer, String> mSlotDes = new HashMap<Integer, String>();
			for(int i = 0;  i < slots.size(); i++){
				Slot sl = slots.get(i);
				mSlotDes.put(sl.getSlotIndex(), sl.getDescription());
			}
			String str ="";
			str += "<Slots>\n";
			//for(int i = 1; i <= 20; i++){
			for(int i = 0; i < v_slots_used.size(); i++){
				int slid = v_slots_used.get(i);
				String s = mSlotDes.get(slid);//mSlot.get(i).getDescription();
				if(s == null) s = "-";
				str += "<slot>\n";
				str += "<id>" + slid + "</id>\n";
				str += "<name>" + s + "</name>\n";
				str += "</slot>\n";
			}
			str += "</Slots>\n";

			str += "<Rooms>\n";
			//Iterator it = rooms_used.iterator();
			//while(it.hasNext()){
			for(int i = 0; i < v_rooms_used.size(); i++){
				//Room r = (Room)it.next();
				Room r = v_rooms_used.get(i);
				System.out.println("return room[" + i + "] = " + r.getID() + "\t" + r.getDescription() + "\t" + r.getMaxNbJuries());
				str += "<room>\n";
				str += "<id>" + r.getID() + "</id>\n";
				str += "<name>" + r.getDescription() + "</name>\n";
				str += "<nb-juries>" + r.getMaxNbJuries() + "</nb-juries>\n";
				str += "<nb-hust>" + r.getMaxNbHustMembers() + "</nb-hust>\n";
				str += "<nb-nonhust>" + r.getMaxNbNonHustMembers() + "</nb-nonhust>\n";
				str += "</room>\n";
			}
			str += "</Rooms>\n";
			
			out.println(str);
			System.out.println("DefenseScheduleManager::getListJuryByStudents, str = " + str);
			
			out.println("<Juries>");
			for(int i = 0; i < juries.size(); i++){
				JuryInfo jr = juries.get(i);
				out.println("<Jury>");
				out.println("<StudentID>" + jr.getStudentID() + "</StudentID>");
				out.println("<StudentName>" + jr.getStudentName() + "</StudentName>");
				out.println("<ThesisTitle>" + jr.getTitle() + "</ThesisTitle>");
				out.println("<SupervisorID>" + jr.getSupervisorId() + "</SupervisorID>");
				out.println("<SupervisorName>" + jr.getSupervisorName() + "</SupervisorName>");
				out.println("<Examiner1ID>" + jr.getExaminerId1() + "</Examiner1ID>");
				out.println("<Examiner1Name>" + jr.getExaminerName1() + "</Examiner1Name>");
				out.println("<Examiner2ID>" + jr.getExaminerId2() + "</Examiner2ID>");
				out.println("<Examiner2Name>" + jr.getExaminerName2() + "</Examiner2Name>");
				out.println("<PresidentID>" + jr.getPresidentId() + "</PresidentID>");
				out.println("<PresidentName>" + jr.getPresidentName() + "</PresidentName>");
				out.println("<SecretaryID>" + jr.getSecretaryId() + "</SecretaryID>");
				out.println("<SecretaryName>" + jr.getSecretaryName() + "</SecretaryName>");
				out.println("<MemberID>" + jr.getAdditionalMemberId() + "</MemberID>");
				out.println("<MemberName>" + jr.getAdditionalMemberName() + "</MemberName>");
				//out.println("<Slot>" + jr.getSlotId() + "</Slot>");
				int slidx = 0;
				String sldes = "-";
				if(mSlot.get(jr.getSlotId()) != null){
					slidx = mSlot.get(jr.getSlotId()).getSlotIndex();
					sldes = mSlot.get(jr.getSlotId()).getDescription();
				}
				out.println("<Slot>" + slidx + "</Slot>");
				out.println("<Slot-Description>" + sldes + "</Slot-Description>");
				out.println("<Room>" + jr.getRoomId() + "</Room>");
				//System.out.println("<Room>" + jr.getRoomId() + "</Room>");
				out.println("</Jury>");
			}
			out.println("</Juries>");
			out.println("</FullJuryInfo>");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void processLoadStudentDefense(Document doc, HttpServletResponse response){
		try{
			
			NodeList nl = doc.getElementsByTagName("student-id");
			Node nod = nl.item(0);
			Element e = (Element)nod;
			int studentID = Integer.valueOf(e.getChildNodes().item(0).getNodeValue());
			
			
						
			nl = doc.getElementsByTagName("session-id");
			nod = nl.item(0);
			e = (Element)nod;
			int sessionID = Integer.valueOf(e.getChildNodes().item(0).getNodeValue());
			System.out.println("DefenseScheduleManager::listJury, sessionID = " + sessionID);
			
			PrintWriter out = response.getWriter();
			System.out.println("getJuries");
			//Vector<Jury> juries = getJuries();
			//Vector<JuryInfo> juries = Utility.getListJuryInfo(sortBy,filterBy);
			Vector<JuryInfo> juries = Utility.getListJuryInfo("StudentID","All");
			JuryInfo selJ = null;
			for(int i = 0; i < juries.size(); i++){
				JuryInfo J = juries.get(i);
				if(J.getStudentID() == studentID && J.getIdDataSet() == sessionID){
					selJ = J;
					break;
				}
			}
			
			out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			out.println("<FullJuryInfo>");
			if(selJ == null){
				out.println("<exist>" + "false" + "</exist>");
			}else{
				out.println("<exist>" + "true" + "</exist>");
				JuryInfo jr = selJ;
				out.println("<Jury>");
				out.println("<StudentID>" + jr.getStudentID() + "</StudentID>");
				out.println("<StudentName>" + jr.getStudentName() + "</StudentName>");
				out.println("<ThesisTitle>" + jr.getTitle() + "</ThesisTitle>");
				out.println("<SupervisorID>" + jr.getSupervisorId() + "</SupervisorID>");
				out.println("<SupervisorName>" + jr.getSupervisorName() + "</SupervisorName>");
				out.println("<Examiner1ID>" + jr.getExaminerId1() + "</Examiner1ID>");
				out.println("<Examiner1Name>" + jr.getExaminerName1() + "</Examiner1Name>");
				out.println("<Examiner2ID>" + jr.getExaminerId2() + "</Examiner2ID>");
				out.println("<Examiner2Name>" + jr.getExaminerName2() + "</Examiner2Name>");
				out.println("<PresidentID>" + jr.getPresidentId() + "</PresidentID>");
				out.println("<PresidentName>" + jr.getPresidentName() + "</PresidentName>");
				out.println("<SecretaryID>" + jr.getSecretaryId() + "</SecretaryID>");
				out.println("<SecretaryName>" + jr.getSecretaryName() + "</SecretaryName>");
				out.println("<MemberID>" + jr.getAdditionalMemberId() + "</MemberID>");
				out.println("<MemberName>" + jr.getAdditionalMemberName() + "</MemberName>");
				
				out.println("</Jury>");
			}
			Vector<StudentSubjectCategoryMatch> m = Utility.getStudentSubjectCategoryMatches();
			Vector<SubjectCategory> categories = Utility.getSubjectCategories();
			HashMap<Integer, String> mSubject = new HashMap<Integer, String>();
			for(int i = 0; i < categories.size(); i++){
				mSubject.put(categories.get(i).getID(),categories.get(i).getName());
			}
			
			Vector<StudentSubjectCategoryMatch> sel_sscm = new Vector<StudentSubjectCategoryMatch>();
			
			for(int i = 0; i < m.size(); i++){
				StudentSubjectCategoryMatch sscm = m.get(i);
				if(sscm.getStudentID() == studentID){
					sel_sscm.add(sscm);
				}
			}
			HashMap<Integer, Integer> mSubjectMatchScore = new HashMap<Integer, Integer>();
			for(int i = 0; i < sel_sscm.size(); i++){
				mSubjectMatchScore.put(sel_sscm.get(i).getSubjectCategoryID(), sel_sscm.get(i).getMatchScore());
			}
			for(int i = 0; i < categories.size(); i++){
				SubjectCategory sc = categories.get(i);
				out.println("<student-subject-category-match>");
				out.println("<student-id>" + 0 + "</student-id>");
				out.println("<subject-category-id>" + sc.getID() + "</subject-category-id>");
				out.println("<subject-category-name>" + sc.getName() + "</subject-category-name>");
				int score = Configure.maxMatchSubjectScore;
				if(mSubjectMatchScore.get(sc.getID()) != null)
					score = mSubjectMatchScore.get(sc.getID());
				out.println("<match-score>" + score + "</match-score>");
				out.println("</student-subject-category-match>");

			}
			/*
			if(sel_sscm.size() == 0){
				for(int i = 0; i < categories.size(); i++){
					SubjectCategory sc = categories.get(i);
					out.println("<student-subject-category-match>");
					out.println("<student-id>" + 0 + "</student-id>");
					out.println("<subject-category-id>" + sc.getID() + "</subject-category-id>");
					out.println("<subject-category-name>" + sc.getName() + "</subject-category-name>");
					out.println("<match-score>" + 10 + "</match-score>");
					out.println("</student-subject-category-match>");

				}
				
			}else{
				for(int i = 0; i < sel_sscm.size(); i++){
					StudentSubjectCategoryMatch sscm = sel_sscm.get(i);
					out.println("<student-subject-category-match>");
					out.println("<student-id>" + sscm.getStudentID() + "</student-id>");
					out.println("<subject-category-id>" + sscm.getSubjectCategoryID() + "</subject-category-id>");
					out.println("<subject-category-name>" + mSubject.get(sscm.getSubjectCategoryID()) + "</subject-category-name>");
					out.println("<match-score>" + sscm.getMatchScore() + "</match-score>");
					out.println("</student-subject-category-match>");
					
					System.out.println("<student-subject-category-match>");
					System.out.println("<student-id>" + sscm.getStudentID() + "</student-id>");
					System.out.println("<subject-category-id>" + sscm.getSubjectCategoryID() + "</subject-category-id>");
					System.out.println("<subject-category-name>" + mSubject.get(sscm.getSubjectCategoryID()) + "</subject-category-name>");
					System.out.println("<match-score>" + sscm.getMatchScore() + "</match-score>");
					System.out.println("</student-subject-category-match>");
					
				}
			}
			*/
			out.println("</FullJuryInfo>");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void processViewJury(Document doc, HttpServletResponse response){
		try{
			
			NodeList nl = doc.getElementsByTagName("SortBy");
			Node nod = nl.item(0);
			Element e = (Element)nod;
			String sortBy = e.getChildNodes().item(0).getNodeValue();
			System.out.println("DefenseScheduleManager::listJury, sort by = " + sortBy);
			
			nl = doc.getElementsByTagName("FilterBy");
			nod = nl.item(0);
			e = (Element)nod;
			String filterBy = e.getChildNodes().item(0).getNodeValue();
			System.out.println("DefenseScheduleManager::listJury, filter by = " + filterBy);
			
			PrintWriter out = response.getWriter();
			System.out.println("getJuries");
			//Vector<Jury> juries = getJuries();
			Vector<JuryInfo> juries = Utility.getListJuryInfo(sortBy,filterBy);
			System.out.println("number of juries = " + juries.size());
			
			Vector<Slot> slots = Utility.getSlots();
			Vector<Room> rooms = Utility.getRooms();
			HashMap<Integer, String> mSlot = new HashMap<Integer,String>();
			HashMap<Integer, String> mRoom = new HashMap<Integer,String>();
			for(int i = 0; i < slots.size(); i++){
				mSlot.put(slots.get(i).getID(), slots.get(i).getDescription());
				//System.out.println(slots.get(i).getID() + "-------------" + slots.get(i).getDescription());
			}
			for(int i = 0; i < rooms.size(); i++){
				mRoom.put(rooms.get(i).getID(), rooms.get(i).getDescription());
				//System.out.println(rooms.get(i).getID() + "---------------" + rooms.get(i).getDescription());
			}
			
			for(int i = 0; i < juries.size(); i++){
				JuryInfo jr = juries.get(i);
				int slotID = jr.getSlotId();
				int roomID = jr.getRoomId();
				jr.setSlotDescription(mSlot.get(slotID));
				jr.setRoomName(mRoom.get(roomID));
			}
			String str = "<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>";
			str += "<FullJuryInfo>";
			//out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			//System.out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			str += "<Juries>";
			//out.println("<Juries>");
			for(int i = 0; i < juries.size(); i++){
				JuryInfo jr = juries.get(i);
				str += "<Jury>";
				//out.println("<Jury>");
				str += "<StudentID>" + jr.getStudentID() + "</StudentID>\n";
				//out.println("<StudentID>" + jr.getStudentID() + "</StudentID>");
				str += "<StudentName>" + jr.getStudentName() + "</StudentName>\n";
				//out.println("<StudentName>" + jr.getStudentName() + "</StudentName>");
				str += "<ThesisTitle>" + jr.getTitle() + "</ThesisTitle>\n";
				//out.println("<ThesisTitle>" + jr.getTitle() + "</ThesisTitle>");
				str += "<Examiner1ID>" + jr.getExaminerId1() + "</Examiner1ID>\n";
				//out.println("<Examiner1ID>" + jr.getExaminerId1() + "</Examiner1ID>");
				str += "<Examiner1Name>" + jr.getExaminerName1() + "</Examiner1Name>\n";
				//out.println("<Examiner1Name>" + jr.getExaminerName1() + "</Examiner1Name>");
				str += "<Examiner2ID>" + jr.getExaminerId2() + "</Examiner2ID>\n";
				//out.println("<Examiner2ID>" + jr.getExaminerId2() + "</Examiner2ID>");
				str += "<Examiner2Name>" + jr.getExaminerName2() + "</Examiner2Name>";
				//out.println("<Examiner2Name>" + jr.getExaminerName2() + "</Examiner2Name>");
				str += "<PresidentID>" + jr.getPresidentId() + "</PresidentID>\n";
				//out.println("<PresidentID>" + jr.getPresidentId() + "</PresidentID>");
				str += "<PresidentName>" + jr.getPresidentName() + "</PresidentName>\n";
				//out.println("<PresidentName>" + jr.getPresidentName() + "</PresidentName>");
				str += "<SecretaryID>" + jr.getSecretaryId() + "</SecretaryID>\n";
				//out.println("<SecretaryID>" + jr.getSecretaryId() + "</SecretaryID>");
				str += "<SecretaryName>" + jr.getSecretaryName() + "</SecretaryName>\n";
				//out.println("<SecretaryName>" + jr.getSecretaryName() + "</SecretaryName>");
				str += "<MemberID>" + jr.getAdditionalMemberId() + "</MemberID>\n";
				//out.println("<MemberID>" + jr.getAdditionalMemberId() + "</MemberID>");
				str += "<MemberName>" + jr.getAdditionalMemberName() + "</MemberName>\n";
				//out.println("<MemberName>" + jr.getAdditionalMemberName() + "</MemberName>");
				str += "<Slot>" + jr.getSlotDescription() + "</Slot>\n";
				//out.println("<Slot>" + jr.getSlotDescription() + "</Slot>");
				str += "<Room>" + jr.getRoomName() + "</Room>\n";
				//out.println("<Room>" + jr.getRoomName() + "</Room>");

				str += "</Jury>";
				//out.println("</Jury>");
			}			
			str += "</Juries>";
			//out.println("</Juries>");
			//out.println(str);
			
			str += "<Teachers>\n";
			
			//out.println("<Teachers>");// list of jury of each teacher
			HashSet<Integer> professors = Utility.collectProfessorsJury(juries);
			//System.out.println("DefenseScheduleManager::processViewJury, juries.sz = " + juries.size() + " professors.sz = " + professors.size());
			Vector<Teacher> teachers = Utility.getTeachers();
			HashMap<Integer, Teacher> mTeachers = new HashMap<Integer, Teacher>();
			for(int i = 0; i < teachers.size(); i++){
				Teacher t = teachers.get(i);
				mTeachers.put(t.getID(), t);
				//System.out.println("DefenseScheduleManager::processViewJury, mTeacher.put(" + t.getID() + "," + t.getName() + ")");
			}
			Iterator ip = professors.iterator();
			while(ip.hasNext()){
				
				Integer p = (Integer)ip.next();
				//System.out.println("DefenseScheduleManager::processViewJury, p = " + p);
				Teacher t = mTeachers.get(p);
				str += "<Teacher>\n";
				//out.println("<Teacher>");
				str += "<Name>" + t.getName() + "</Name>\n";
				//out.println("<Name>");
				//out.println(t.getName());
				//out.println("</Name>");
				Vector<JuryInfo> jp = Utility.collectJuriesOfProfessor(juries, p);
				
				for(int i = 0; i < jp.size(); i++){
					JuryInfo jr = jp.get(i);
					str += "<JuryOfProfessor>\n";
					//out.println("<Jury>");
					str += "<StudentID>" + jr.getStudentID() + "</StudentID>\n";
					//out.println("<StudentID>" + jr.getStudentID() + "</StudentID>");
					str += "<StudentName>" + jr.getStudentName() + "</StudentName>\n";
					//out.println("<StudentName>" + jr.getStudentName() + "</StudentName>");
					str += "<ThesisTitle>" + jr.getTitle() + "</ThesisTitle>\n";
					//out.println("<ThesisTitle>" + jr.getTitle() + "</ThesisTitle>");
					str += "<Examiner1ID>" + jr.getExaminerId1() + "</Examiner1ID>\n";
					//out.println("<Examiner1ID>" + jr.getExaminerId1() + "</Examiner1ID>");
					str += "<Examiner1Name>" + jr.getExaminerName1() + "</Examiner1Name>\n";
					//out.println("<Examiner1Name>" + jr.getExaminerName1() + "</Examiner1Name>");
					str += "<Examiner2ID>" + jr.getExaminerId2() + "</Examiner2ID>\n";
					//out.println("<Examiner2ID>" + jr.getExaminerId2() + "</Examiner2ID>");
					 str += "<Examiner2Name>" + jr.getExaminerName2() + "</Examiner2Name>";
					//out.println("<Examiner2Name>" + jr.getExaminerName2() + "</Examiner2Name>");
					str += "<PresidentID>" + jr.getPresidentId() + "</PresidentID>\n";
					//out.println("<PresidentID>" + jr.getPresidentId() + "</PresidentID>");
					str += "<PresidentName>" + jr.getPresidentName() + "</PresidentName>\n";
					//out.println("<PresidentName>" + jr.getPresidentName() + "</PresidentName>");
					str += "<SecretaryID>" + jr.getSecretaryId() + "</SecretaryID>\n";
					//out.println("<SecretaryID>" + jr.getSecretaryId() + "</SecretaryID>");
					str += "<SecretaryName>" + jr.getSecretaryName() + "</SecretaryName>\n";
					//out.println("<SecretaryName>" + jr.getSecretaryName() + "</SecretaryName>");
					str += "<MemberID>" + jr.getAdditionalMemberId() + "</MemberID>\n";
					//out.println("<MemberID>" + jr.getAdditionalMemberId() + "</MemberID>");
					str += "<MemberName>" + jr.getAdditionalMemberName() + "</MemberName>\n";
					//out.println("<MemberName>" + jr.getAdditionalMemberName() + "</MemberName>");
					str += "<Slot>" + jr.getSlotDescription() + "</Slot>\n";
					//out.println("<Slot>" + jr.getSlotDescription() + "</Slot>");
					str += "<Room>" + jr.getRoomName() + "</Room>\n";
					//out.println("<Room>" + jr.getRoomName() + "</Room>");
					str += "</JuryOfProfessor>\n";
					//out.println("</JuryOfProfessor>");
				}
				
				str += "</Teacher>\n";
				//out.println("</Teacher>");
			}
			str += "</Teachers>\n";
			//out.println("</Teachers>");
			
			
			HashMap<Integer, Slot> mSlots = new HashMap<Integer,Slot>();
			HashMap<Integer, String> mSlotDescription = new HashMap<Integer, String>();
			for(int i = 0; i < slots.size(); i++){
				Slot sl = slots.get(i);
				mSlots.put(sl.getID(),sl);
				mSlotDescription.put(sl.getSlotIndex(), sl.getDescription());
			}
			
			
			//HashSet<Integer> slots_used = Utility.collectSlots(juries);
			HashSet<Integer> slots_used = Utility.collectSlotsIndex(juries, mSlots);
			
			
			int minSl = Utility.getMinSlot(slots_used);
			int maxSl = Utility.getMaxSlot(slots_used);
			
			str += "<SlotsUsed>\n";
			for(int slID = minSl; slID <= maxSl; slID++){
				//Slot sl = mSlots.get(slID);
				//str += "<Slot>" + sl.getDescription() + "</Slot>";
				String des = mSlotDescription.get(slID);
				str += "<Slot>" + des + "</Slot>";
			}
			str += "</SlotsUsed>\n";
			
			str += "<SlotsProfessor>\n";
			ip = professors.iterator();
			while(ip.hasNext()){
				
				Integer p = (Integer)ip.next();
				//System.out.println("DefenseScheduleManager::processViewJury, p = " + p);
				Teacher t = mTeachers.get(p);
				str += "<Teacher>\n";
				//out.println("<Teacher>");
				str += "<Name>" + t.getName() + "</Name>\n";
				//out.println("<Name>");
				//out.println(t.getName());
				//out.println("</Name>");
				Vector<JuryInfo> jp = Utility.collectJuriesOfProfessor(juries, p);
				for(int slID = minSl; slID <= maxSl; slID++){
					String roomProfessor = "-";
					for(int j = 0; j < jp.size(); j++){
						JuryInfo jr = jp.get(j);
						Slot sl = mSlots.get(jr.getSlotId());
						//if(jr.getSlotId() == slID){
						if(sl.getSlotIndex() == slID){
							roomProfessor = jr.getRoomName();
							break;
						}
					}
					str += "<Room>" + roomProfessor + "</Room>\n";
				}
				str += "</Teacher>\n";
			}	
			str += "</SlotsProfessor>\n";
			str += "</FullJuryInfo>";
			out.println(str);
			//System.out.println(str);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	
	public void processLoadJuryForRepair(Document doc, HttpServletResponse response){
		try{
			
			NodeList nl = doc.getElementsByTagName("SortBy");
			Node nod = nl.item(0);
			Element e = (Element)nod;
			String sortBy = e.getChildNodes().item(0).getNodeValue();
			System.out.println("DefenseScheduleManager::LoadJuryForRepair, sort by = " + sortBy);
			
			nl = doc.getElementsByTagName("FilterBy");
			nod = nl.item(0);
			e = (Element)nod;
			String filterBy = e.getChildNodes().item(0).getNodeValue();
			System.out.println("DefenseScheduleManager::LoadJuryForRepair, filter by = " + filterBy);
			
			PrintWriter out = response.getWriter();
			System.out.println("getJuries");
			//Vector<Jury> juries = getJuries();
			Vector<JuryInfo> juries = Utility.getListJuryInfo(sortBy,filterBy);
			System.out.println("number of juries = " + juries.size());
			
			Vector<Slot> slots = Utility.getSlots();
			Vector<Room> rooms = Utility.getRooms();
			HashMap<Integer, Slot> mSlot = new HashMap<Integer,Slot>();
			HashMap<Integer, Room> mRoom = new HashMap<Integer,Room>();
			for(int i = 0; i < slots.size(); i++){
				mSlot.put(slots.get(i).getID(), slots.get(i));
				//System.out.println(slots.get(i).getID() + "-------------" + slots.get(i).getDescription());
			}
			for(int i = 0; i < rooms.size(); i++){
				mRoom.put(rooms.get(i).getID(), rooms.get(i));
				//System.out.println(rooms.get(i).getID() + "---------------" + rooms.get(i).getDescription());
			}
			
			HashSet<Room> rooms_used = new HashSet<Room>();
			HashSet<Integer> rooms_id_used = new HashSet<Integer>();
			
			for(int i = 0; i < juries.size(); i++){
				JuryInfo jr = juries.get(i);
				int slotID = jr.getSlotId();
				int roomID = jr.getRoomId();
				if(mRoom.get(roomID) != null) rooms_used.add(mRoom.get(roomID));
				rooms_id_used.add(roomID);
				String sldes = "-";
				if(mSlot.get(slotID) != null) sldes = mSlot.get(slotID).getDescription(); 
				jr.setSlotDescription(sldes);
				String rdes = "-";
				if(mRoom.get(roomID) != null) rdes = mRoom.get(roomID).getDescription();
				jr.setRoomName(rdes);
			}
			String str = "<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>";
			str += "<FullJuryInfo>";
			//out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			//System.out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			
			str += "<Rooms>";
			Iterator it = rooms_used.iterator();
			while(it.hasNext()){
				Room r = (Room)it.next();
				str += "<room>";
				str += "<id>" + r.getID() + "</id>";
				str += "<name>" + r.getDescription() + "</name>";
				str += "</room>";
			}
			str += "</Rooms>";
			Vector<Slot> slots_used = new Vector<Slot>();
			for(int i = 0; i < slots.size(); i++){
				Slot sl = slots.get(i);
				if(rooms_id_used.contains(sl.getRoomID())){
					slots_used.add(sl);
				}
			}
			
			HashMap<Integer, String> mSlotDes = new HashMap<Integer, String>();
			HashSet<Integer> slot_index_used = new HashSet<Integer>();
			int minSlIdx = 1000;
			int maxSlIdx = -minSlIdx;
			for(int i = 0; i < slots_used.size(); i++){
				Slot sl = slots_used.get(i);
				mSlotDes.put(sl.getSlotIndex(),sl.getDescription());
				slot_index_used.add(sl.getSlotIndex());
				if(minSlIdx > sl.getSlotIndex()) minSlIdx = sl.getSlotIndex();
				if(maxSlIdx < sl.getSlotIndex()) maxSlIdx = sl.getSlotIndex();
			}
			str += "<Slots>";
			for(int i = 1; i <= 20; i++){
				String s = mSlotDes.get(i);
				if(s == null) s = "-";
				str += "<slot>";
				str += "<id>" + i + "</id>";
				str += "<name>" + s + "</name>";
				str += "</slot>";
			}
			str += "</Slots>";

			str += "<Juries>";
			//out.println("<Juries>");
			for(int i = 0; i < juries.size(); i++){
				JuryInfo jr = juries.get(i);
				str += "<Jury>";
				//out.println("<Jury>");
				str += "<StudentID>" + jr.getStudentID() + "</StudentID>\n";
				//out.println("<StudentID>" + jr.getStudentID() + "</StudentID>");
				str += "<StudentName>" + jr.getStudentName() + "</StudentName>\n";
				//out.println("<StudentName>" + jr.getStudentName() + "</StudentName>");
				str += "<ThesisTitle>" + jr.getTitle() + "</ThesisTitle>\n";
				//out.println("<ThesisTitle>" + jr.getTitle() + "</ThesisTitle>");
				str += "<SupervisorID>" + jr.getSupervisorId() + "</SupervisorID>\n";
				str += "<SupervisorName>" + jr.getSupervisorName() + "</SupervisorName>\n";
				
				str += "<Examiner1ID>" + jr.getExaminerId1() + "</Examiner1ID>\n";
				//out.println("<Examiner1ID>" + jr.getExaminerId1() + "</Examiner1ID>");
				str += "<Examiner1Name>" + jr.getExaminerName1() + "</Examiner1Name>\n";
				//out.println("<Examiner1Name>" + jr.getExaminerName1() + "</Examiner1Name>");
				str += "<Examiner2ID>" + jr.getExaminerId2() + "</Examiner2ID>\n";
				//out.println("<Examiner2ID>" + jr.getExaminerId2() + "</Examiner2ID>");
				str += "<Examiner2Name>" + jr.getExaminerName2() + "</Examiner2Name>";
				//out.println("<Examiner2Name>" + jr.getExaminerName2() + "</Examiner2Name>");
				str += "<PresidentID>" + jr.getPresidentId() + "</PresidentID>\n";
				//out.println("<PresidentID>" + jr.getPresidentId() + "</PresidentID>");
				str += "<PresidentName>" + jr.getPresidentName() + "</PresidentName>\n";
				//out.println("<PresidentName>" + jr.getPresidentName() + "</PresidentName>");
				str += "<SecretaryID>" + jr.getSecretaryId() + "</SecretaryID>\n";
				//out.println("<SecretaryID>" + jr.getSecretaryId() + "</SecretaryID>");
				str += "<SecretaryName>" + jr.getSecretaryName() + "</SecretaryName>\n";
				//out.println("<SecretaryName>" + jr.getSecretaryName() + "</SecretaryName>");
				str += "<MemberID>" + jr.getAdditionalMemberId() + "</MemberID>\n";
				//out.println("<MemberID>" + jr.getAdditionalMemberId() + "</MemberID>");
				str += "<MemberName>" + jr.getAdditionalMemberName() + "</MemberName>\n";
				//out.println("<MemberName>" + jr.getAdditionalMemberName() + "</MemberName>");
				int slidx = 0;
				if(mSlot.get(jr.getSlotId()) != null) slidx = mSlot.get(jr.getSlotId()).getSlotIndex();
				str += "<Slot>" + slidx + "</Slot>\n";
				//out.println("<Slot>" + jr.getSlotDescription() + "</Slot>");
				int rid = 0;
				if(mRoom.get(jr.getRoomId()) != null) rid = jr.getRoomId();
				str += "<Room>" + rid + "</Room>\n";
				//System.out.println("rid = " + rid);
				//out.println("<Room>" + jr.getRoomName() + "</Room>");

				str += "</Jury>";
				//out.println("</Jury>");
			}			
			str += "</Juries>";
			//out.println("</Juries>");
			//out.println(str);
			
			str += "</FullJuryInfo>";
			out.println(str);
			//System.out.println(str);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	/*
	private void processAddTeacher(Document doc){
		String name = "noname";
		NodeList nl = doc.getElementsByTagName("name");
		Node nod = nl.item(0);
		Element el = (Element)nod;
		
		name = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
		System.out.println("processAddTeacher, name = " + name);
		if(name == null){
			System.out.println("Name is null");
			return;
		}
		
		try{
			if(cnn == null){
				Class.forName("com.mysql.jdbc.Driver");
				cnn = DriverManager.getConnection(cnnStr);
				//stat = cnn.createStatement();
				String sql = "insert into " + tblProfessors + "(name) values(default,?)";
				preparedStat = cnn.prepareStatement("insert into " + tblProfessors + "(Name,Institute) values(?,?)");
				preparedStat.setString(1, name);
				preparedStat.setString(2, "HUST");
				preparedStat.executeUpdate();
				System.out.println("insert successfully");
				//stat.executeQuery(sql);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close();
		}
		
	}
	private void processAddStudent(Document doc){
		String name = "noname";
		NodeList nl = doc.getElementsByTagName("name");
		Node nod = nl.item(0);
		Element el = (Element)nod;		
		name = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
		
		nl = doc.getElementsByTagName("promotion");
		nod = nl.item(0);
		el = (Element)nod;		
		String promotion = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
		
		nl = doc.getElementsByTagName("class");
		nod = nl.item(0);
		el = (Element)nod;		
		String className = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();

		System.out.println("processAddStudent, name = " + name + " promotion = " + promotion + " class = " + className);
		if(name == null){
			System.out.println("Name is null");
			return;
		}
		
		try{
			if(cnn == null){
				Class.forName("com.mysql.jdbc.Driver");
				cnn = DriverManager.getConnection(cnnStr);
				//stat = cnn.createStatement();
				String sql = "insert into " + tblStudents + "(StudentName,Promotion,Class) values(default,?,?,?)";
				preparedStat = cnn.prepareStatement("insert into " + tblStudents + "(StudentName,Promotion,Class) values(?,?,?)");
				preparedStat.setString(1, name);
				preparedStat.setString(2, promotion);
				preparedStat.setString(3, className);
				preparedStat.executeUpdate();
				System.out.println("insert successfully");
				//stat.executeQuery(sql);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close();
		}
		
	}
	*/
	private void processAddJury(Document doc){
		
		NodeList nl = doc.getElementsByTagName("student");
		Node nod = nl.item(0);
		Element el = (Element)nod;		
		int studentID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); //nod.getNodeValue();
		
		nl = doc.getElementsByTagName("thesis_title");
		nod = nl.item(0);
		el = (Element)nod;		
		String thesis_title = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
		
		nl = doc.getElementsByTagName("supervisor");
		nod = nl.item(0);
		el = (Element)nod;		
		int supervisorID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); //nod.getNodeValue();
		
		nl = doc.getElementsByTagName("examiner1");
		nod = nl.item(0);
		el = (Element)nod;		
		int examiner1ID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); //nod.getNodeValue();

		nl = doc.getElementsByTagName("examiner2");
		nod = nl.item(0);
		el = (Element)nod;		
		int examiner2ID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); //nod.getNodeValue();
		
		nl = doc.getElementsByTagName("president");
		nod = nl.item(0);
		el = (Element)nod;		
		int presidentID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); //nod.getNodeValue();
		
		nl = doc.getElementsByTagName("secretary");
		nod = nl.item(0);
		el = (Element)nod;		
		int secretaryID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); //nod.getNodeValue();
		
		nl = doc.getElementsByTagName("member");
		nod = nl.item(0);
		el = (Element)nod;		
		int memberID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); //nod.getNodeValue();
		
		nl = doc.getElementsByTagName("session");
		nod = nl.item(0);
		el = (Element)nod;		
		int sessionID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); //nod.getNodeValue();

		System.out.println("TeacherManager::processAddJury: studentID = " + studentID + " thesis_title = " + 
		thesis_title + " examiner1 = " + examiner1ID + " examiner2 = " + examiner2ID);
		
		Utility.addJury(studentID, thesis_title, supervisorID, examiner1ID, examiner2ID, 
				presidentID, secretaryID, memberID, 0, 0, sessionID);
		/*
		try{
			if(cnn == null){
				Class.forName("com.mysql.jdbc.Driver");
				cnn = DriverManager.getConnection(cnnStr);
				preparedStat = cnn.prepareStatement("insert into " + tblStudentDefense + "(StudentID,Title,Examiner1,Examiner2,President," +
						"Secretary,AdditionalMember) " + " values(?,?,?,?,?,?,?)");
				preparedStat.setInt(1, studentID);
				preparedStat.setString(2, thesis_title);
				preparedStat.setInt(3, examiner1ID);
				preparedStat.setInt(4, examiner2ID);
				preparedStat.setInt(5, presidentID);
				preparedStat.setInt(6, secretaryID);
				preparedStat.setInt(7, memberID);				
				preparedStat.executeUpdate();
				System.out.println("insert into table " + tblStudentDefense + " successfully");
				//stat.executeQuery(sql);
				close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close();
		}
		*/
	}

	private void processAddJuryStudentSupervisor(Document doc){
		
		NodeList nl = doc.getElementsByTagName("student");
		Node nod = nl.item(0);
		Element el = (Element)nod;		
		int studentID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); //nod.getNodeValue();
		
		nl = doc.getElementsByTagName("thesis_title");
		nod = nl.item(0);
		el = (Element)nod;		
		String thesis_title = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
		
		nl = doc.getElementsByTagName("supervisor");
		nod = nl.item(0);
		el = (Element)nod;		
		int supervisorID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); //nod.getNodeValue();
		
		nl = doc.getElementsByTagName("student-subject-category-match");
		for(int i = 0; i < nl.getLength(); i++){
			nod = nl.item(i);
			el = (Element)nod;
			int student_id = Integer.valueOf(el.getElementsByTagName("student-id").item(0).getChildNodes().item(0).getNodeValue());
			int subject_category_id = Integer.valueOf(el.getElementsByTagName("subject-category-id").item(0).getChildNodes().item(0).getNodeValue());
			int matchScore = Integer.valueOf(el.getElementsByTagName("match-score").item(0).getChildNodes().item(0).getNodeValue());
			System.out.println("processAddJuryStudentSupervisor, id = " + student_id + ", category = " + subject_category_id + ", match = " + matchScore);
			int id = Utility.getIDStudentDefenseSubjectCategory(student_id, subject_category_id);
			if(id == -1)
				Utility.addStudentDefenseSubjectCategory(student_id, subject_category_id, matchScore);
			else
				Utility.updateStudentDefenseSubjectCategory(id, student_id, subject_category_id, matchScore);
		}
		
		
		nl = doc.getElementsByTagName("session");
		nod = nl.item(0);
		el = (Element)nod;		
		int sessionID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); //nod.getNodeValue();

		System.out.println("DefenseScheduleManager::processAddJuryStudentSupervisor: studentID = " + studentID + " thesis_title = " + 
		thesis_title);
		
		JuryInfo J = Utility.getJury(studentID, sessionID);
		if(J == null){
			System.out.println("DefenseScheduleManager::processAddJuryStudentSupervisor, not found --> add new");
			Utility.addJury(studentID, thesis_title, supervisorID, 0, 0, 0, 0, 0, 0, 0, sessionID);
		}else{
			System.out.println("DefenseScheduleManager::processAddJuryStudentSupervisor, found exist --> update");
			Utility.updateJury(J.getId(), studentID, thesis_title, supervisorID, 0, 0, 0, 0, 0, 0, 0, sessionID);
		}
		/*
		try{
			if(cnn == null){
				Class.forName("com.mysql.jdbc.Driver");
				cnn = DriverManager.getConnection(cnnStr);
				preparedStat = cnn.prepareStatement("insert into " + tblStudentDefense + "(StudentID,Title,Examiner1,Examiner2,President," +
						"Secretary,AdditionalMember) " + " values(?,?,?,?,?,?,?)");
				preparedStat.setInt(1, studentID);
				preparedStat.setString(2, thesis_title);
				preparedStat.setInt(3, examiner1ID);
				preparedStat.setInt(4, examiner2ID);
				preparedStat.setInt(5, presidentID);
				preparedStat.setInt(6, secretaryID);
				preparedStat.setInt(7, memberID);				
				preparedStat.executeUpdate();
				System.out.println("insert into table " + tblStudentDefense + " successfully");
				//stat.executeQuery(sql);
				close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close();
		}
		*/
	}

	private void processUpdateJury(Document doc){
		
		NodeList nl = doc.getElementsByTagName("student");
		Node nod = nl.item(0);
		Element el = (Element)nod;		
		int studentID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); //nod.getNodeValue();
		
		nl = doc.getElementsByTagName("thesis_title");
		nod = nl.item(0);
		el = (Element)nod;		
		String thesis_title = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
		
		nl = doc.getElementsByTagName("supervisor");
		nod = nl.item(0);
		el = (Element)nod;		
		int supervisorID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); //nod.getNodeValue();
		
		nl = doc.getElementsByTagName("examiner1");
		nod = nl.item(0);
		el = (Element)nod;		
		int examiner1ID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); //nod.getNodeValue();

		nl = doc.getElementsByTagName("examiner2");
		nod = nl.item(0);
		el = (Element)nod;		
		int examiner2ID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); //nod.getNodeValue();
		
		nl = doc.getElementsByTagName("president");
		nod = nl.item(0);
		el = (Element)nod;		
		int presidentID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); //nod.getNodeValue();
		
		nl = doc.getElementsByTagName("secretary");
		nod = nl.item(0);
		el = (Element)nod;		
		int secretaryID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); //nod.getNodeValue();
		
		nl = doc.getElementsByTagName("member");
		nod = nl.item(0);
		el = (Element)nod;		
		int memberID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); //nod.getNodeValue();
		
		System.out.println("TeacherManager::processAddJury: studentID = " + studentID + " thesis_title = " + 
		thesis_title + " examiner1 = " + examiner1ID + " examiner2 = " + examiner2ID);
		
		Utility.updateJury(studentID, thesis_title, supervisorID,examiner1ID, examiner2ID, presidentID, secretaryID, memberID);
		/*
		try{
			if(cnn == null){
				Class.forName("com.mysql.jdbc.Driver");
				cnn = DriverManager.getConnection(cnnStr);
				preparedStat = cnn.prepareStatement("insert into " + tblStudentDefense + "(StudentID,Title,Examiner1,Examiner2,President," +
						"Secretary,AdditionalMember) " + " values(?,?,?,?,?,?,?)");
				preparedStat.setInt(1, studentID);
				preparedStat.setString(2, thesis_title);
				preparedStat.setInt(3, examiner1ID);
				preparedStat.setInt(4, examiner2ID);
				preparedStat.setInt(5, presidentID);
				preparedStat.setInt(6, secretaryID);
				preparedStat.setInt(7, memberID);				
				preparedStat.executeUpdate();
				System.out.println("insert into table " + tblStudentDefense + " successfully");
				//stat.executeQuery(sql);
				close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close();
		}
		*/
	}
	
	/*
	private void processDeleteTeacher(Document doc, HttpServletResponse response){
		String id = "noname";
		NodeList nl = doc.getElementsByTagName("id");
		Node nod = nl.item(0);
		Element el = (Element)nod;
		
		id = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
		System.out.println("processDeleteTeacher, id = " + id);
		if(id == null){
			System.out.println("Name is null");
			return;
		}
		
		try{
			if(cnn == null){
				Class.forName("com.mysql.jdbc.Driver");
				cnn = DriverManager.getConnection(cnnStr);
				preparedStat = cnn.prepareStatement("delete from " + tblProfessors + " where ID = ?");
				preparedStat.setString(1, id);
				//preparedStat.setString(2, "1");
				preparedStat.executeUpdate();
				System.out.println("Delete successfully");
				//stat.executeQuery(sql);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close();
		}
		
	}
	
	private void processUpdateTeacher(Document doc, HttpServletResponse response){
		String name = "noname";
		String id = null;
		NodeList nl = doc.getElementsByTagName("name");
		Node nod = nl.item(0);
		Element el = (Element)nod;		
		name = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
		
		nl = doc.getElementsByTagName("id");
		nod = nl.item(0);
		el = (Element)nod;
		id = el.getChildNodes().item(0).getNodeValue();
		System.out.println("processDeleteTeacher, name = " + name + " id = " + id);
		if(name == null){
			System.out.println("Name is null");
			return;
		}
		
		try{
			if(cnn == null){
				Class.forName("com.mysql.jdbc.Driver");
				cnn = DriverManager.getConnection(cnnStr);
				preparedStat = cnn.prepareStatement("update " + tblProfessors + " set Name = ? where ID = ?");
				preparedStat.setString(1, name);
				preparedStat.setString(2, id);
				preparedStat.executeUpdate();
				System.out.println("Update successfully");
				//stat.executeQuery(sql);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close();
		}
		
	}
	 */
	
	private void processCheckListJury(Document doc, HttpServletResponse response){
		System.out.println("processCheckListJury");
		
		Vector<Teacher> teachers = Utility.getTeachers();
		HashMap<Integer, String> id2Name = new HashMap<Integer,String>();
		for(int i = 0; i < teachers.size(); i++){
			Teacher t = teachers.get(i);
			id2Name.put(t.getID(),t.getName());
		}
		Vector<JuryInfo> listJury = new Vector<JuryInfo>();
		
		NodeList juryNodeList = doc.getElementsByTagName("Jury");
		for(int i = 0; i < juryNodeList.getLength(); i++){
			Node juryNod = juryNodeList.item(i);
			Element juryElement = (Element)juryNod;
			
			
			NodeList nl = juryElement.getElementsByTagName("studentID");
			Node nod = nl.item(0);
			Element el = (Element)nod;		
			int studentID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); //nod.getNodeValue();
			
			nl = juryElement.getElementsByTagName("studentName");
			nod = nl.item(0);
			el = (Element)nod;		
			String studentName = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();

			nl = juryElement.getElementsByTagName("thesis_title");
			nod = nl.item(0);
			el = (Element)nod;		
			String thesis_title = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();

			//System.out.print(studentID + "\t" + studentName + "\t" + thesis_title);
			
			nl = juryElement.getElementsByTagName("supervisor");
			nod = nl.item(0);
			el = (Element)nod;		
			//String examiner1 = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int supervisorID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			nl = juryElement.getElementsByTagName("examiner1");
			nod = nl.item(0);
			el = (Element)nod;		
			//String examiner1 = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int examiner1ID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			nl = juryElement.getElementsByTagName("examiner2");
			nod = nl.item(0);
			el = (Element)nod;		
			//String examiner2 = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int examiner2ID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			nl = juryElement.getElementsByTagName("president");
			nod = nl.item(0);
			el = (Element)nod;		
			//String president = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int presidentID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			nl = juryElement.getElementsByTagName("secretary");
			nod = nl.item(0);
			el = (Element)nod;		
			//String secretary = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int secretaryID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); 
			
			nl = juryElement.getElementsByTagName("member");
			nod = nl.item(0);
			el = (Element)nod;		
			//String member = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int memberID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			/*
			JuryInfo jury = new JuryInfo(i,studentName,thesis_title,"null",examiner1,examiner2,president,secretary,member,0);
			
			jury.setStudentID(studentID);
			jury.setExaminerId1(name2ID.get(jury.getExaminerName1()));
			jury.setExaminerId2(name2ID.get(jury.getExaminerName2()));
			jury.setPresidentId(name2ID.get(jury.getPresidentName()));
			jury.setSecretaryId(name2ID.get(jury.getSecretaryName()));
			jury.setAdditionalMemberId(name2ID.get(jury.getAdditionalMemberName()));
			*/
			JuryInfo jury = new JuryInfo(i,studentID,thesis_title,examiner1ID,examiner2ID,presidentID,secretaryID,memberID,0);
			jury.setStudentName(studentName);
			jury.setExaminerName1(id2Name.get(examiner1ID));
			jury.setExaminerName2(id2Name.get(examiner2ID));
			jury.setPresidentName(id2Name.get(presidentID));
			jury.setSecretaryName(id2Name.get(secretaryID));
			jury.setAdditionalMemberName(id2Name.get(memberID));
			jury.setSupervisorId(supervisorID);
			jury.setSupervisorName(id2Name.get(supervisorID));
			
			nl = juryElement.getElementsByTagName("slot");
			nod = nl.item(0);
			el = (Element)nod;		
			int slot = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); //nod.getNodeValue();
			
			nl = juryElement.getElementsByTagName("room");
			nod = nl.item(0);
			el = (Element)nod;		
			int room = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); //nod.getNodeValue();
			
			jury.setSlotId(slot);
			jury.setRoomId(room);
			
			listJury.add(jury);
			
			
			System.out.println("TeacherManager::processCheckJuryJury: " + studentID + "\t" + studentName + "\t" + 
			thesis_title + "\t" + examiner1ID + "\t" + examiner2ID + "\t" + presidentID + "\t" + secretaryID +
			"\t" + memberID + "\t" + slot + "\t" + room);
		}
		
		try{
			PrintWriter out = response.getWriter();
			//String xml = Utility.checkConsistency(listJury);
			String xml = Utility.generateAndCheckConsistency(listJury);
			out.println(xml);
			System.out.println("TeacherManager::processCheckListJury --> Return to clients xml\n" + xml);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void processSortJury(Document doc, HttpServletResponse response){
		System.out.println("processCheckListJury");
		
		Vector<Teacher> teachers = Utility.getTeachers();
		HashMap<Integer, String> id2Name = new HashMap<Integer,String>();
		for(int i = 0; i < teachers.size(); i++){
			Teacher t = teachers.get(i);
			id2Name.put(t.getID(),t.getName());
		}
		Vector<JuryInfo> listJury = new Vector<JuryInfo>();
		
		NodeList nls = doc.getElementsByTagName("SortBy");
		Node nods = nls.item(0);
		Element es = (Element)nods;
		String sortBy = es.getChildNodes().item(0).getNodeValue();
		System.out.println("DefenseScheduleManager::processSortJury, sort by = " + sortBy);

		NodeList juryNodeList = doc.getElementsByTagName("Jury");
		for(int i = 0; i < juryNodeList.getLength(); i++){
			Node juryNod = juryNodeList.item(i);
			Element juryElement = (Element)juryNod;
			
			
			NodeList nl = juryElement.getElementsByTagName("studentID");
			Node nod = nl.item(0);
			Element el = (Element)nod;		
			int studentID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); //nod.getNodeValue();
			
			nl = juryElement.getElementsByTagName("studentName");
			nod = nl.item(0);
			el = (Element)nod;		
			String studentName = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();

			nl = juryElement.getElementsByTagName("thesis_title");
			nod = nl.item(0);
			el = (Element)nod;		
			String thesis_title = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();

			//System.out.print(studentID + "\t" + studentName + "\t" + thesis_title);
			
			nl = juryElement.getElementsByTagName("supervisor");
			nod = nl.item(0);
			el = (Element)nod;		
			//String examiner1 = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int supervisorID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			nl = juryElement.getElementsByTagName("examiner1");
			nod = nl.item(0);
			el = (Element)nod;		
			//String examiner1 = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int examiner1ID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			nl = juryElement.getElementsByTagName("examiner2");
			nod = nl.item(0);
			el = (Element)nod;		
			//String examiner2 = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int examiner2ID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			nl = juryElement.getElementsByTagName("president");
			nod = nl.item(0);
			el = (Element)nod;		
			//String president = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int presidentID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			nl = juryElement.getElementsByTagName("secretary");
			nod = nl.item(0);
			el = (Element)nod;		
			//String secretary = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int secretaryID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); 
			
			nl = juryElement.getElementsByTagName("member");
			nod = nl.item(0);
			el = (Element)nod;		
			//String member = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int memberID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			/*
			JuryInfo jury = new JuryInfo(i,studentName,thesis_title,"null",examiner1,examiner2,president,secretary,member,0);
			
			jury.setStudentID(studentID);
			jury.setExaminerId1(name2ID.get(jury.getExaminerName1()));
			jury.setExaminerId2(name2ID.get(jury.getExaminerName2()));
			jury.setPresidentId(name2ID.get(jury.getPresidentName()));
			jury.setSecretaryId(name2ID.get(jury.getSecretaryName()));
			jury.setAdditionalMemberId(name2ID.get(jury.getAdditionalMemberName()));
			*/
			JuryInfo jury = new JuryInfo(i,studentID,thesis_title,examiner1ID,examiner2ID,presidentID,secretaryID,memberID,0);
			jury.setStudentName(studentName);
			jury.setExaminerName1(id2Name.get(examiner1ID));
			jury.setExaminerName2(id2Name.get(examiner2ID));
			jury.setPresidentName(id2Name.get(presidentID));
			jury.setSecretaryName(id2Name.get(secretaryID));
			jury.setAdditionalMemberName(id2Name.get(memberID));
			jury.setSupervisorId(supervisorID);
			jury.setSupervisorName(id2Name.get(supervisorID));
			
			nl = juryElement.getElementsByTagName("slot");
			nod = nl.item(0);
			el = (Element)nod;		
			int slot = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); //nod.getNodeValue();
			
			nl = juryElement.getElementsByTagName("room");
			nod = nl.item(0);
			el = (Element)nod;		
			int room = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); //nod.getNodeValue();
			
			jury.setSlotId(slot);
			jury.setRoomId(room);
			
			listJury.add(jury);
			
			
			System.out.println("TeacherManager::processCheckJuryJury: " + studentID + "\t" + studentName + "\t" + 
			thesis_title + "\t" + examiner1ID + "\t" + examiner2ID + "\t" + presidentID + "\t" + secretaryID +
			"\t" + memberID + "\t" + slot + "\t" + room);
		}
		
		try{
			PrintWriter out = response.getWriter();
			//String xml = Utility.checkConsistency(listJury);
			String xml = Utility.generateAndCheckConsistency(listJury, sortBy);
			out.println(xml);
			System.out.println("TeacherManager::processCheckListJury --> Return to clients xml\n" + xml);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void processPrepareStore(Document doc, HttpServletResponse response){
		System.out.println("processPrepareStore");
		
		Vector<Teacher> teachers = Utility.getTeachers();
		HashMap<Integer, String> id2Name = new HashMap<Integer,String>();
		for(int i = 0; i < teachers.size(); i++){
			Teacher t = teachers.get(i);
			id2Name.put(t.getID(),t.getName());
		}
		Vector<JuryInfo> listJury = new Vector<JuryInfo>();
		
		NodeList juryNodeList = doc.getElementsByTagName("Jury");
		for(int i = 0; i < juryNodeList.getLength(); i++){
			Node juryNod = juryNodeList.item(i);
			Element juryElement = (Element)juryNod;
			
			
			NodeList nl = juryElement.getElementsByTagName("studentID");
			Node nod = nl.item(0);
			Element el = (Element)nod;		
			int studentID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); //nod.getNodeValue();
			
			nl = juryElement.getElementsByTagName("studentName");
			nod = nl.item(0);
			el = (Element)nod;		
			String studentName = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();

			nl = juryElement.getElementsByTagName("thesis_title");
			nod = nl.item(0);
			el = (Element)nod;		
			String thesis_title = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();

			//System.out.print(studentID + "\t" + studentName + "\t" + thesis_title);
			
			nl = juryElement.getElementsByTagName("supervisor");
			nod = nl.item(0);
			el = (Element)nod;		
			//String examiner1 = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int supervisorID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			nl = juryElement.getElementsByTagName("examiner1");
			nod = nl.item(0);
			el = (Element)nod;		
			//String examiner1 = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int examiner1ID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			nl = juryElement.getElementsByTagName("examiner2");
			nod = nl.item(0);
			el = (Element)nod;		
			//String examiner2 = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int examiner2ID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			nl = juryElement.getElementsByTagName("president");
			nod = nl.item(0);
			el = (Element)nod;		
			//String president = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int presidentID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			nl = juryElement.getElementsByTagName("secretary");
			nod = nl.item(0);
			el = (Element)nod;		
			//String secretary = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int secretaryID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); 
			
			nl = juryElement.getElementsByTagName("member");
			nod = nl.item(0);
			el = (Element)nod;		
			//String member = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int memberID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			/*
			JuryInfo jury = new JuryInfo(i,studentName,thesis_title,"null",examiner1,examiner2,president,secretary,member,0);
			
			jury.setStudentID(studentID);
			jury.setExaminerId1(name2ID.get(jury.getExaminerName1()));
			jury.setExaminerId2(name2ID.get(jury.getExaminerName2()));
			jury.setPresidentId(name2ID.get(jury.getPresidentName()));
			jury.setSecretaryId(name2ID.get(jury.getSecretaryName()));
			jury.setAdditionalMemberId(name2ID.get(jury.getAdditionalMemberName()));
			*/
			JuryInfo jury = new JuryInfo(i,studentID,thesis_title,examiner1ID,examiner2ID,presidentID,secretaryID,memberID,0);
			jury.setStudentName(studentName);
			jury.setExaminerName1(id2Name.get(examiner1ID));
			jury.setExaminerName2(id2Name.get(examiner2ID));
			jury.setPresidentName(id2Name.get(presidentID));
			jury.setSecretaryName(id2Name.get(secretaryID));
			jury.setAdditionalMemberName(id2Name.get(memberID));
			jury.setSupervisorId(supervisorID);
			jury.setSupervisorName(id2Name.get(supervisorID));
			
			nl = juryElement.getElementsByTagName("slot");
			nod = nl.item(0);
			el = (Element)nod;		
			int slot = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); //nod.getNodeValue();
			
			nl = juryElement.getElementsByTagName("room");
			nod = nl.item(0);
			el = (Element)nod;		
			int room = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); //nod.getNodeValue();
			
			jury.setSlotId(slot);
			jury.setRoomId(room);
			
			listJury.add(jury);
			
			
			//System.out.println("TeacherManager::processCheckJuryJury: " + studentID + "\t" + studentName + "\t" + 
			//thesis_title + "\t" + examiner1ID + "\t" + examiner2ID + "\t" + presidentID + "\t" + secretaryID +
			//"\t" + memberID + "\t" + slot + "\t" + room);
		}
		
		try{
			PrintWriter out = response.getWriter();
			//String xml = Utility.checkConsistency(listJury);
			//String xml = Utility.generateAndCheckConsistency(listJury);
			//out.println(xml);
			
			Vector<Room> v_rooms_used = Utility.collectVectorRooms(listJury);
			Vector<Integer> v_slots_used = Utility.collectVectorSlots(listJury);
			Vector<Slot> slots = Utility.getSlots();
			System.out.print("DefenseScheduleManager::processListJuryByStudents, v_rooms_used[" + v_rooms_used.size() + "] = ");
			for(int i = 0; i < v_rooms_used.size(); i++){
				Room r = v_rooms_used.get(i);
				System.out.print(r.getID() + " ");
			}
			System.out.println();
			
			System.out.print("DefenseScheduleManager::processListJuryByStudents, v_slots_used[" + v_slots_used.size() + "] = ");
			for(int i = 0; i < v_slots_used.size(); i++){
				int sl = v_slots_used.get(i);
				System.out.print(sl + " ");
			}
			System.out.println();
			
			//String str = "<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>";
			//out.println("<FullJuryInfo>");
			//out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			//System.out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");

			
			HashMap<Integer, String> mSlotDes = new HashMap<Integer, String>();
			for(int i = 0;  i < slots.size(); i++){
				Slot sl = slots.get(i);
				mSlotDes.put(sl.getSlotIndex(), sl.getDescription());
			}
			String str ="";
			str += "<Slots>";
			//for(int i = 1; i <= 20; i++){
			for(int i = 0; i < v_slots_used.size(); i++){
				int slid = v_slots_used.get(i);
				String s = mSlotDes.get(slid);//mSlot.get(i).getDescription();
				if(s == null) s = "-";
				str += "<slot>\n";
				str += "<id>" + slid + "</id>\n";
				str += "<name>" + s + "</name>\n";
				str += "</slot>\n";
			}
			str += "</Slots>\n";

			str += "<Rooms>\n";
			//Iterator it = rooms_used.iterator();
			//while(it.hasNext()){
			for(int i = 0; i < v_rooms_used.size(); i++){
				//Room r = (Room)it.next();
				Room r = v_rooms_used.get(i);
				//System.out.println("return room[" + i + "] = " + r.getID() + "\t" + r.getDescription() + "\t" + r.getMaxNbJuries());
				str += "<room>\n";
				str += "<id>" + r.getID() + "</id>\n";
				str += "<name>" + r.getDescription() + "</name>\n";
				str += "<nb-juries>" + r.getMaxNbJuries() + "</nb-juries>\n";
				str += "<nb-hust>" + r.getMaxNbHustMembers() + "</nb-hust>\n";
				str += "<nb-nonhust>" + r.getMaxNbNonHustMembers() + "</nb-nonhust>\n";
				str += "</room>\n";
			}
			str += "</Rooms>\n";
			
			out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			out.println("<PrepareStore>\n");
			out.println(str);
			out.println("</PrepareStore>\n");
			
			
			System.out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			System.out.println("<PrepareStore>\n");
			System.out.println(str);
			System.out.println("</PrepareStore>\n");
			//System.out.println("TeacherManager::processCheckListJury --> Return to clients xml\n" + xml);
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void processSuggestSlotRoom(Document doc, HttpServletResponse response){
		System.out.println("processSuggestSlotRoom");
		
		Vector<Teacher> teachers = Utility.getTeachers();
		HashMap<Integer, String> id2Name = new HashMap<Integer,String>();
		for(int i = 0; i < teachers.size(); i++){
			Teacher t = teachers.get(i);
			id2Name.put(t.getID(),t.getName());
		}
		Vector<JuryInfo> listJury = new Vector<JuryInfo>();
		NodeList nlStdID = doc.getElementsByTagName("selected-studentID");
		Node nodStdID = nlStdID.item(0);
		Element eStdID = (Element)nodStdID;
		int sel_studentID = Integer.valueOf(eStdID.getChildNodes().item(0).getNodeValue());
		
		NodeList juryNodeList = doc.getElementsByTagName("Jury");
		for(int i = 0; i < juryNodeList.getLength(); i++){
			Node juryNod = juryNodeList.item(i);
			Element juryElement = (Element)juryNod;
			
			
			NodeList nl = juryElement.getElementsByTagName("studentID");
			Node nod = nl.item(0);
			Element el = (Element)nod;		
			int studentID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); //nod.getNodeValue();
			
			nl = juryElement.getElementsByTagName("studentName");
			nod = nl.item(0);
			el = (Element)nod;		
			String studentName = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();

			nl = juryElement.getElementsByTagName("thesis_title");
			nod = nl.item(0);
			el = (Element)nod;		
			String thesis_title = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();

			//System.out.print(studentID + "\t" + studentName + "\t" + thesis_title);
			
			nl = juryElement.getElementsByTagName("supervisor");
			nod = nl.item(0);
			el = (Element)nod;		
			//String examiner1 = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int supervisorID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			nl = juryElement.getElementsByTagName("examiner1");
			nod = nl.item(0);
			el = (Element)nod;		
			//String examiner1 = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int examiner1ID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			nl = juryElement.getElementsByTagName("examiner2");
			nod = nl.item(0);
			el = (Element)nod;		
			//String examiner2 = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int examiner2ID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			nl = juryElement.getElementsByTagName("president");
			nod = nl.item(0);
			el = (Element)nod;		
			//String president = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int presidentID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			nl = juryElement.getElementsByTagName("secretary");
			nod = nl.item(0);
			el = (Element)nod;		
			//String secretary = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int secretaryID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); 
			
			nl = juryElement.getElementsByTagName("member");
			nod = nl.item(0);
			el = (Element)nod;		
			//String member = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int memberID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			/*
			JuryInfo jury = new JuryInfo(i,studentName,thesis_title,"null",examiner1,examiner2,president,secretary,member,0);
			
			jury.setStudentID(studentID);
			jury.setExaminerId1(name2ID.get(jury.getExaminerName1()));
			jury.setExaminerId2(name2ID.get(jury.getExaminerName2()));
			jury.setPresidentId(name2ID.get(jury.getPresidentName()));
			jury.setSecretaryId(name2ID.get(jury.getSecretaryName()));
			jury.setAdditionalMemberId(name2ID.get(jury.getAdditionalMemberName()));
			*/
			JuryInfo jury = new JuryInfo(i,studentID,thesis_title,examiner1ID,examiner2ID,presidentID,secretaryID,memberID,0);
			jury.setStudentName(studentName);
			jury.setExaminerName1(id2Name.get(examiner1ID));
			jury.setExaminerName2(id2Name.get(examiner2ID));
			jury.setPresidentName(id2Name.get(presidentID));
			jury.setSecretaryName(id2Name.get(secretaryID));
			jury.setAdditionalMemberName(id2Name.get(memberID));
			jury.setSupervisorId(supervisorID);
			jury.setSupervisorName(id2Name.get(supervisorID));
			
			nl = juryElement.getElementsByTagName("slot");
			nod = nl.item(0);
			el = (Element)nod;		
			int slot = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); //nod.getNodeValue();
			
			nl = juryElement.getElementsByTagName("room");
			nod = nl.item(0);
			el = (Element)nod;		
			int room = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); //nod.getNodeValue();
			
			jury.setSlotId(slot);
			jury.setRoomId(room);
			
			listJury.add(jury);
			
			
			System.out.println("DefenseJuryManager::processSuggestSlotRoom: " + studentID + "\t" + studentName + "\t" + 
			thesis_title + "\t" + examiner1ID + "\t" + examiner2ID + "\t" + presidentID + "\t" + secretaryID +
			"\t" + memberID + "\t" + slot + "\t" + room);
		}
		System.out.println("DefenseJuryManager::processSuggestSlotRoom --> selected studentID = " + sel_studentID);
		try{
			PrintWriter out = response.getWriter();
			//String xml = Utility.checkConsistency(listJury);
			Vector<SlotRoom> candidates = Utility.computeCandidateSlots(listJury, sel_studentID, 20, 20);
			String xml = "<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "UTF-8" + "\"" + "?>";
			xml += "<CandidateSlotRoom>\n";
			for(int i = 0; i < candidates.size(); i++){
				xml += "<SlotRoom>" + candidates.get(i).getSlotID() + "\t\t" + candidates.get(i).getRoomID() + "</SlotRoom>";
			}
			xml += "</CandidateSlotRoom>\n";
			out.println(xml);
			System.out.println("DefenseJuryManager::processSuggestSlotRoom --> Return to clients xml\n" + xml);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void processSearchSolution(Document doc, HttpServletResponse response){
		System.out.println("processSearchSolution");
		
		Vector<Teacher> teachers = Utility.getTeachers();
		HashMap<Integer, String> id2Name = new HashMap<Integer,String>();
		for(int i = 0; i < teachers.size(); i++){
			Teacher t = teachers.get(i);
			id2Name.put(t.getID(),t.getName());
		}
		Vector<JuryInfo> listJury = new Vector<JuryInfo>();
		
		NodeList juryNodeList = doc.getElementsByTagName("Jury");
		for(int i = 0; i < juryNodeList.getLength(); i++){
			Node juryNod = juryNodeList.item(i);
			Element juryElement = (Element)juryNod;
			
			
			NodeList nl = juryElement.getElementsByTagName("studentID");
			Node nod = nl.item(0);
			Element el = (Element)nod;		
			int studentID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); //nod.getNodeValue();
			
			nl = juryElement.getElementsByTagName("studentName");
			nod = nl.item(0);
			el = (Element)nod;		
			String studentName = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();

			nl = juryElement.getElementsByTagName("thesis_title");
			nod = nl.item(0);
			el = (Element)nod;		
			String thesis_title = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();

			//System.out.print(studentID + "\t" + studentName + "\t" + thesis_title);
			
			nl = juryElement.getElementsByTagName("supervisor");
			nod = nl.item(0);
			el = (Element)nod;		
			//String examiner1 = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int supervisorID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			nl = juryElement.getElementsByTagName("examiner1");
			nod = nl.item(0);
			el = (Element)nod;		
			//String examiner1 = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int examiner1ID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			nl = juryElement.getElementsByTagName("examiner2");
			nod = nl.item(0);
			el = (Element)nod;		
			//String examiner2 = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int examiner2ID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			nl = juryElement.getElementsByTagName("president");
			nod = nl.item(0);
			el = (Element)nod;		
			//String president = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int presidentID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			nl = juryElement.getElementsByTagName("secretary");
			nod = nl.item(0);
			el = (Element)nod;		
			//String secretary = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int secretaryID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); 
			
			nl = juryElement.getElementsByTagName("member");
			nod = nl.item(0);
			el = (Element)nod;		
			//String member = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int memberID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			/*
			JuryInfo jury = new JuryInfo(i,studentName,thesis_title,"null",examiner1,examiner2,president,secretary,member,0);
			
			jury.setStudentID(studentID);
			jury.setExaminerId1(name2ID.get(jury.getExaminerName1()));
			jury.setExaminerId2(name2ID.get(jury.getExaminerName2()));
			jury.setPresidentId(name2ID.get(jury.getPresidentName()));
			jury.setSecretaryId(name2ID.get(jury.getSecretaryName()));
			jury.setAdditionalMemberId(name2ID.get(jury.getAdditionalMemberName()));
			*/
			JuryInfo jury = new JuryInfo(i,studentID,thesis_title,examiner1ID,examiner2ID,presidentID,secretaryID,memberID,0);
			jury.setStudentName(studentName);
			jury.setExaminerName1(id2Name.get(examiner1ID));
			jury.setExaminerName2(id2Name.get(examiner2ID));
			jury.setPresidentName(id2Name.get(presidentID));
			jury.setSecretaryName(id2Name.get(secretaryID));
			jury.setAdditionalMemberName(id2Name.get(memberID));
			jury.setSupervisorId(supervisorID);
			jury.setSupervisorName(id2Name.get(supervisorID));
			listJury.add(jury);
			
			
			System.out.println("TeacherManager::SearchSolution, Jury: " + studentID + "\t" + studentName + "\t" + 
			thesis_title + "\t" + examiner1ID + "\t" + examiner2ID + "\t" + presidentID + "\t" + secretaryID +
			"\t" + memberID);
		}
		
		NodeList nl = doc.getElementsByTagName("slots");
		Node nod = nl.item(0);
		Element e = (Element)nod;
		int nbSlots = Integer.valueOf(e.getChildNodes().item(0).getNodeValue());

		nl = doc.getElementsByTagName("rooms");
		nod = nl.item(0);
		e = (Element)nod;
		int nbRooms = Integer.valueOf(e.getChildNodes().item(0).getNodeValue());
		
		nl = doc.getElementsByTagName("time");
		nod = nl.item(0);
		e = (Element)nod;
		int timeLimit = Integer.valueOf(e.getChildNodes().item(0).getNodeValue());
		
		nl = doc.getElementsByTagName("algorithm");
		nod = nl.item(0);
		e = (Element)nod;
		String algorithm = e.getChildNodes().item(0).getNodeValue();
		
		System.out.println("DefenseSchedulerManager::processSearchSolution, nbSlots = " + nbSlots + " nbRooms = " + nbRooms 
				+ " time-limit = " + timeLimit + " algorithm = " + algorithm);
		
		try{
			PrintWriter out = response.getWriter();
			if(algorithm.equals("TabuSearch")){
				algorithms.localsearch.NewSearch se = new algorithms.localsearch.NewSearch(listJury, teachers, nbSlots, nbRooms);
				se.localsearch(timeLimit);
			}else if(algorithm.equals("BackTrackSearch")){
				algorithms.backtracksearch.BackTrackSearch bs = new algorithms.backtracksearch.BackTrackSearch(6);
				bs.search(listJury, nbSlots, nbRooms, timeLimit);
			}
			//String xml = Utility.checkConsistency(listJury);
			String xml = Utility.generateAndCheckConsistency(listJury);
			out.println(xml);
			//System.out.println("TeacherManager::processCheckListJury --> Return to clients xml\n" + xml);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private void processLoadRoomsDepartments(Document doc, HttpServletResponse response){
		System.out.println("processLoadRoomsDepartments");
		
		Vector<Room> rooms = Utility.getRooms();
		Vector<Department> departments = Utility.getDepartments();
		Vector<DefenseSession> sessions = Utility.getDefenseSessions();
		HashMap<Integer, Integer> mSession = new HashMap<Integer, Integer>();
		for(int i = 0; i < sessions.size(); i++){
			mSession.put(sessions.get(i).getID(), sessions.get(i).getActive());
			//System.out.println("LoadRoomsDepartments ,mSession.put(" + sessions.get(i).getID() + "," + sessions.get(i).getActive());
		}
		try{
			PrintWriter out = response.getWriter();
			String xml = "<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "UTF-8" + "\"" + "?>";
			xml += "<rooms-departments>";
			for(int i = 0; i < departments.size(); i++){
				Department dep = departments.get(i);
				xml += "<department>" + dep.getName() + "</department>\n";
			}
			for(int i = 0; i < rooms.size(); i++){
				Room r = rooms.get(i);
				//System.out.println("LoadRoomsDepartments, r.sessionID = " + r.getSessionID());
				if(mSession.get(r.getSessionID()) == 1){
					xml += "<room>";
					xml += "<id>" + r.getID() + "</id>";
					xml += "<name>" + r.getDescription() + "</name>";
					xml += "</room>\n";
				}
			}
			xml += "</rooms-departments>";
			out.println(xml);
			System.out.println("TeacherManager::processCheckListJury --> Return to clients xml\n" + xml);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	private void processSearchSolutionJuryMembers(Document doc, HttpServletResponse response){
		System.out.println("processSearchSolutionJuryMembers");
		
		Vector<Teacher> teachers = Utility.getTeachers();
		HashMap<Integer, String> id2Name = new HashMap<Integer,String>();
		for(int i = 0; i < teachers.size(); i++){
			Teacher t = teachers.get(i);
			id2Name.put(t.getID(),t.getName());
		}
		Vector<JuryInfo> listJury = new Vector<JuryInfo>();
		
		NodeList juryNodeList = doc.getElementsByTagName("Jury");
		for(int i = 0; i < juryNodeList.getLength(); i++){
			Node juryNod = juryNodeList.item(i);
			Element juryElement = (Element)juryNod;
			
			
			NodeList nl = juryElement.getElementsByTagName("studentID");
			Node nod = nl.item(0);
			Element el = (Element)nod;		
			int studentID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); //nod.getNodeValue();
			
			nl = juryElement.getElementsByTagName("studentName");
			nod = nl.item(0);
			el = (Element)nod;		
			String studentName = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();

			nl = juryElement.getElementsByTagName("thesis_title");
			nod = nl.item(0);
			el = (Element)nod;		
			String thesis_title = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();

			//System.out.print(studentID + "\t" + studentName + "\t" + thesis_title);
			
			nl = juryElement.getElementsByTagName("supervisor");
			nod = nl.item(0);
			el = (Element)nod;		
			//String examiner1 = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int supervisorID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			nl = juryElement.getElementsByTagName("examiner1");
			nod = nl.item(0);
			el = (Element)nod;		
			//String examiner1 = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int examiner1ID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			nl = juryElement.getElementsByTagName("examiner2");
			nod = nl.item(0);
			el = (Element)nod;		
			//String examiner2 = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int examiner2ID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			nl = juryElement.getElementsByTagName("president");
			nod = nl.item(0);
			el = (Element)nod;		
			//String president = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int presidentID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			nl = juryElement.getElementsByTagName("secretary");
			nod = nl.item(0);
			el = (Element)nod;		
			//String secretary = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int secretaryID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); 
			
			nl = juryElement.getElementsByTagName("member");
			nod = nl.item(0);
			el = (Element)nod;		
			//String member = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int memberID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			nl = juryElement.getElementsByTagName("slot");
			nod = nl.item(0);
			el = (Element)nod;		
			//String member = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int slotID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			/*
			JuryInfo jury = new JuryInfo(i,studentName,thesis_title,"null",examiner1,examiner2,president,secretary,member,0);
			
			jury.setStudentID(studentID);
			jury.setExaminerId1(name2ID.get(jury.getExaminerName1()));
			jury.setExaminerId2(name2ID.get(jury.getExaminerName2()));
			jury.setPresidentId(name2ID.get(jury.getPresidentName()));
			jury.setSecretaryId(name2ID.get(jury.getSecretaryName()));
			jury.setAdditionalMemberId(name2ID.get(jury.getAdditionalMemberName()));
			*/
			JuryInfo jury = new JuryInfo(i,studentID,thesis_title,examiner1ID,examiner2ID,presidentID,secretaryID,memberID,0);
			jury.setStudentName(studentName);
			jury.setExaminerName1(id2Name.get(examiner1ID));
			jury.setExaminerName2(id2Name.get(examiner2ID));
			jury.setPresidentName(id2Name.get(presidentID));
			jury.setSecretaryName(id2Name.get(secretaryID));
			jury.setAdditionalMemberName(id2Name.get(memberID));
			jury.setSupervisorId(supervisorID);
			jury.setSupervisorName(id2Name.get(supervisorID));
			
			jury.setSlotId(slotID);
			listJury.add(jury);
			
			
			System.out.println("TeacherManager::SearchSolution, Jury: " + studentID + "\t" + studentName + "\t" + 
			thesis_title + "\t" + examiner1ID + "\t" + examiner2ID + "\t" + presidentID + "\t" + secretaryID +
			"\t" + memberID);
		}
		/*
		NodeList nl = doc.getElementsByTagName("slots");
		Node nod = nl.item(0);
		Element e = (Element)nod;
		int nbSlots = Integer.valueOf(e.getChildNodes().item(0).getNodeValue());

		nl = doc.getElementsByTagName("rooms");
		nod = nl.item(0);
		e = (Element)nod;
		int nbRooms = Integer.valueOf(e.getChildNodes().item(0).getNodeValue());
		*/
		
		NodeList nl = doc.getElementsByTagName("time");
		Node nod = nl.item(0);
		Element e = (Element)nod;
		int timeLimit = Integer.valueOf(e.getChildNodes().item(0).getNodeValue());
		
		nl = doc.getElementsByTagName("algorithm");
		nod = nl.item(0);
		e = (Element)nod;
		String algorithm = e.getChildNodes().item(0).getNodeValue();
		
		nl = doc.getElementsByTagName("jury-members");
		Vector<Integer> jury_members = new Vector<Integer>();
		nod = nl.item(0);
		e = (Element)nod;
		nl = e.getElementsByTagName("jury-member");
		System.out.println("processSearchSolutionJuryMembers --> jury-members = " + nl.getLength());
		for(int i = 0; i < nl.getLength(); i++){
			nod = nl.item(i);
			e = (Element)nod;
			int jm = Integer.valueOf(e.getChildNodes().item(0).getNodeValue());
			jury_members.add(jm);
		}
		//System.out.println("DefenseSchedulerManager::processSearchSolution, nbSlots = " + nbSlots + " nbRooms = " + nbRooms 
			//	+ " time-limit = " + timeLimit + " algorithm = " + algorithm);
		
		try{
			PrintWriter out = response.getWriter();
			boolean foundSolution = false;
			algorithms.backtracksearch.BackTrackSearchJuryMembers bs = new algorithms.backtracksearch.BackTrackSearchJuryMembers();
			if(algorithm.equals("TabuSearch")){
				//algorithms.localsearch.NewSearch se = new algorithms.localsearch.NewSearch(listJury, teachers, nbSlots, nbRooms);
				//se.localsearch(timeLimit);
			}else if(algorithm.equals("BackTrackSearch")){
				
				//bs.search(listJury, jury_members, teachers, timeLimit);
				bs.searchPartial(listJury, jury_members, teachers, timeLimit);
				
				foundSolution = bs.hasSolution();
			}
			String xml = "";
			if(foundSolution)
				//xml = Utility.checkConsistency(listJury);
				xml = Utility.generateAndCheckConsistency(listJury);
			else{
				xml = "<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "UTF-8" + "\"" + "?>";
				xml += "<return-search-solution>";
				xml += "<solution>";
				xml += "false";
				xml += "</solution>";
				xml += "<error>";
				xml += bs.getErrorMsg();
				xml += "</error>";
				xml += "</return-search-solution>";
			}
			out.println(xml);
			System.out.println("TeacherManager::processCheckListJury --> Return to clients xml\n" + xml);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	private void processSearchSolutionJuriesMembers(Document doc, HttpServletResponse response){
		System.out.println("processSearchSolutionJuriesMembers");
		
		Vector<Teacher> teachers = Utility.getTeachers();
		HashMap<Integer, String> id2Name = new HashMap<Integer,String>();
		for(int i = 0; i < teachers.size(); i++){
			Teacher t = teachers.get(i);
			id2Name.put(t.getID(),t.getName());
		}
		Vector<JuryInfo> listJury = new Vector<JuryInfo>();
		
		NodeList juryNodeList = doc.getElementsByTagName("Jury");
		for(int i = 0; i < juryNodeList.getLength(); i++){
			Node juryNod = juryNodeList.item(i);
			Element juryElement = (Element)juryNod;
			
			
			NodeList nl = juryElement.getElementsByTagName("studentID");
			Node nod = nl.item(0);
			Element el = (Element)nod;		
			int studentID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); //nod.getNodeValue();
			
			nl = juryElement.getElementsByTagName("studentName");
			nod = nl.item(0);
			el = (Element)nod;		
			String studentName = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();

			nl = juryElement.getElementsByTagName("thesis_title");
			nod = nl.item(0);
			el = (Element)nod;		
			String thesis_title = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();

			//System.out.print(studentID + "\t" + studentName + "\t" + thesis_title);
			
			nl = juryElement.getElementsByTagName("supervisor");
			nod = nl.item(0);
			el = (Element)nod;		
			//String examiner1 = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int supervisorID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			nl = juryElement.getElementsByTagName("examiner1");
			nod = nl.item(0);
			el = (Element)nod;		
			//String examiner1 = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int examiner1ID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			nl = juryElement.getElementsByTagName("examiner2");
			nod = nl.item(0);
			el = (Element)nod;		
			//String examiner2 = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int examiner2ID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			nl = juryElement.getElementsByTagName("president");
			nod = nl.item(0);
			el = (Element)nod;		
			//String president = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int presidentID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			nl = juryElement.getElementsByTagName("secretary");
			nod = nl.item(0);
			el = (Element)nod;		
			//String secretary = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int secretaryID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); 
			
			nl = juryElement.getElementsByTagName("member");
			nod = nl.item(0);
			el = (Element)nod;		
			//String member = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int memberID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			nl = juryElement.getElementsByTagName("slot");
			nod = nl.item(0);
			el = (Element)nod;		
			//String member = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int slotID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			nl = juryElement.getElementsByTagName("room");
			nod = nl.item(0);
			el = (Element)nod;		
			//String member = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int roomID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());

			/*
			JuryInfo jury = new JuryInfo(i,studentName,thesis_title,"null",examiner1,examiner2,president,secretary,member,0);
			
			jury.setStudentID(studentID);
			jury.setExaminerId1(name2ID.get(jury.getExaminerName1()));
			jury.setExaminerId2(name2ID.get(jury.getExaminerName2()));
			jury.setPresidentId(name2ID.get(jury.getPresidentName()));
			jury.setSecretaryId(name2ID.get(jury.getSecretaryName()));
			jury.setAdditionalMemberId(name2ID.get(jury.getAdditionalMemberName()));
			*/
			JuryInfo jury = new JuryInfo(i,studentID,thesis_title,examiner1ID,examiner2ID,presidentID,secretaryID,memberID,0);
			jury.setStudentName(studentName);
			jury.setExaminerName1(id2Name.get(examiner1ID));
			jury.setExaminerName2(id2Name.get(examiner2ID));
			jury.setPresidentName(id2Name.get(presidentID));
			jury.setSecretaryName(id2Name.get(secretaryID));
			jury.setAdditionalMemberName(id2Name.get(memberID));
			jury.setSupervisorId(supervisorID);
			jury.setSupervisorName(id2Name.get(supervisorID));
			
			jury.setSlotId(slotID);
			jury.setRoomId(roomID);
			
			listJury.add(jury);
			
			
			System.out.println("TeacherManager::SearchSolution, Jury: " + studentID + "\t" + studentName + "\t" + 
			thesis_title + "\t" + examiner1ID + "\t" + examiner2ID + "\t" + presidentID + "\t" + secretaryID +
			"\t" + memberID + "\t" + slotID + "\t" + roomID);
		}
		
		NodeList nlRoomInfo = doc.getElementsByTagName("room-info");
		Vector<Room> rooms = new Vector<Room>();
		for(int i = 0; i < nlRoomInfo.getLength(); i++){
			Node nod = nlRoomInfo.item(i);
			Element e = (Element)nod;
			int rid = Integer.valueOf(e.getElementsByTagName("id").item(0).getChildNodes().item(0).getNodeValue());
			int nbJuries = Integer.valueOf(e.getElementsByTagName("nbJuries").item(0).getChildNodes().item(0).getNodeValue());
			int nbNonHust = Integer.valueOf(e.getElementsByTagName("nbNonHust").item(0).getChildNodes().item(0).getNodeValue());
			int nbHust = Integer.valueOf(e.getElementsByTagName("nbHust").item(0).getChildNodes().item(0).getNodeValue());
			
			//Room r = new Room(0,"-",0);
			Room r = new Room(rid,"-",0);
			r.setNbJuries(nbJuries);
			r.setMaxNbNonHustMembers(nbNonHust);
			r.setMaxNbHustMembers(nbHust);
			System.out.println("rooms[" + i + "], nbJuries = " + nbJuries + ", nbNonHust = " + nbNonHust + ", nbHust = " + nbHust);
			rooms.add(r);
		}
		//if(true) return;
		
		NodeList nl = doc.getElementsByTagName("time");
		Node nod = nl.item(0);
		Element e = (Element)nod;
		int timeLimit = Integer.valueOf(e.getChildNodes().item(0).getNodeValue());
		
		nl = doc.getElementsByTagName("algorithm");
		nod = nl.item(0);
		e = (Element)nod;
		String algorithm = e.getChildNodes().item(0).getNodeValue();
		
		nl = doc.getElementsByTagName("jury-members");
		Vector<Integer> jury_members = new Vector<Integer>();
		nod = nl.item(0);
		e = (Element)nod;
		nl = e.getElementsByTagName("jury-member");
		System.out.println("processSearchSolutionJuryMembers --> jury-members = " + nl.getLength());
		for(int i = 0; i < nl.getLength(); i++){
			nod = nl.item(i);
			e = (Element)nod;
			int jm = Integer.valueOf(e.getChildNodes().item(0).getNodeValue());
			jury_members.add(jm);
		}
		
		nl = doc.getElementsByTagName("nb-juries");
		nod = nl.item(0);
		e = (Element)nod;
		int nbJuries = Integer.valueOf(e.getChildNodes().item(0).getNodeValue());
		
		System.out.println("DefenseSchedulerManager::processSearchSolutionJuriesMembers, nbJuries = " + nbJuries +		
				" time-limit = " + timeLimit + " algorithm = " + algorithm);
		
		Vector<Teacher> hustTeachers = new Vector<Teacher>();
		Vector<Teacher> nonHustTeachers = new Vector<Teacher>();
		Vector<Teacher> AT = new Vector<Teacher>();
		
		HashMap<Integer, Teacher> mTeacher = new HashMap<Integer, Teacher>();
		for(int i = 0; i < teachers.size(); i++){
			Teacher t = teachers.get(i);
			mTeacher.put(t.getID(), t);
		}
		
		for(int i = 0; i < jury_members.size(); i++){
			Teacher t = mTeacher.get(jury_members.get(i));
			System.out.println(t.getID() + "\t" + t.getInstitute());
			if(t.getInstitute().equals("HUST")){
				hustTeachers.add(t);
				//System.out.println("DefenseScheduleManager::processSearchSolutionJuriesMembers--> hust " + t.getName());
			}else{
				nonHustTeachers.add(t);
				//System.out.println("DefenseScheduleManager::processSearchSolutionJuriesMembers--> nonhust " + t.getName());
			}
			AT.add(t);
		}

		try{
			PrintWriter out = response.getWriter();
			boolean foundSolution = true;
			String retMsg = "non error";
			if(algorithm.equals("TabuSearch")){
				//algorithms.localsearch.NewSearch se = new algorithms.localsearch.NewSearch(listJury, teachers, nbSlots, nbRooms);
				//se.localsearch(timeLimit);
				
				JuryPartitionerBasedOnSlots jpbos = new JuryPartitionerBasedOnSlots();
				jpbos.partitionJury(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
			}else if(algorithm.equals("AssignSlots")){
				JuryPartitionerBasedOnSlots jpbos = new JuryPartitionerBasedOnSlots();
				jpbos.partitionJury(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
			}else if(algorithm.equals("AssignRooms")){
				BackTrackSearchTeachersSlotsRoomsObjectiveMatch btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
				btsatsr.setNbVariablesInstantiated(listJury.size());
				int[] order = new int[listJury.size()];
				for(int i = 0; i < listJury.size(); i++){
					order[i] = 7*i+6;
				}
				btsatsr.setOrder(order);
				btsatsr.setObjectiveType("movements");
				btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
			}else if(algorithm.equals("AssignPresidentRoom")){
				JuryPartitionerAdvanced JP = new JuryPartitionerAdvanced();
				Vector<JuryDataForScheduling> jdfs = JP.partitionJury(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
				HashMap[] matchScore = Utility.computeMatchJuryProfessor(listJury, AT);//JP.getMatchJuryProfessor();
				
				AssignTopExpert ate = new AssignTopExpert();
				ate.search(listJury, rooms, matchScore, hustTeachers, timeLimit);
				
				AssignSlot as = new AssignSlot();
				as.search(listJury, rooms, timeLimit);
				
			}else if(algorithm.equals("AssignExaminer1")){
				//HashMap[] matchScore = Utility.computeMatchJuryProfessor(listJury, AT);//JP.getMatchJuryProfessor();
				//AssignExaminer1 ae1 = new AssignExaminer1();
				//ae1.search(listJury, rooms, matchScore, nonHustTeachers, timeLimit);
				BackTrackSearchTeachersSlotsRoomsObjectiveMatch btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
				btsatsr.setNbVariablesInstantiated(listJury.size());
				int[] order = new int[listJury.size()];
				for(int i = 0; i < listJury.size(); i++){
					order[i] = 7*i;
				}
				btsatsr.setOrder(order);
				btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
			}else if(algorithm.equals("AssignExaminer2")){
				//HashMap[] matchScore = Utility.computeMatchJuryProfessor(listJury, AT) ;//JP.getMatchJuryProfessor();
				//AssignExaminer2 ae2 = new AssignExaminer2();
				//ae2.search(listJury, rooms, matchScore, hustTeachers, timeLimit);
				
				BackTrackSearchTeachersSlotsRoomsObjectiveMatch btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
				btsatsr.setNbVariablesInstantiated(listJury.size());
				int[] order = new int[listJury.size()];
				for(int i = 0; i < listJury.size(); i++){
					order[i] = 7*i+1;
				}
				btsatsr.setOrder(order);
				btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
			}else if(algorithm.equals("AssignAdditionalMember")){
				
				//HashMap[] matchScore = Utility.computeMatchJuryProfessor(listJury, AT); //JP.getMatchJuryProfessor();
				//AssignAdditionalMember aa = new AssignAdditionalMember();
				//aa.search(listJury, rooms, matchScore, nonHustTeachers, timeLimit);
				BackTrackSearchTeachersSlotsRoomsObjectiveMatch btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
				btsatsr.setNbVariablesInstantiated(listJury.size());
				int[] order = new int[listJury.size()];
				for(int i = 0; i < listJury.size(); i++){
					order[i] = 7*i+4;
				}
				btsatsr.setOrder(order);
				btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
			}else if(algorithm.equals("AssignPresident")){
				//HashMap[] matchScore = Utility.computeMatchJuryProfessor(listJury, AT); //JP.getMatchJuryProfessor();
				//AssignPresident ap = new AssignPresident();
				//ap.search(listJury, rooms, matchScore, hustTeachers, timeLimit);
				BackTrackSearchTeachersSlotsRoomsObjectiveMatch btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
				btsatsr.setNbVariablesInstantiated(listJury.size());
				int[] order = new int[listJury.size()];
				for(int i = 0; i < listJury.size(); i++){
					order[i] = 7*i+2;
				}
				btsatsr.setOrder(order);
				btsatsr.setObjectiveType("expert-level");
				btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
			}else if(algorithm.equals("AssignSecretary")){
				//HashMap[] matchScore = Utility.computeMatchJuryProfessor(listJury, AT); //JP.getMatchJuryProfessor();
				//AssignSecretary as = new AssignSecretary();
				//as.search(listJury, rooms, matchScore, hustTeachers, timeLimit);
				BackTrackSearchTeachersSlotsRoomsObjectiveMatch btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
				btsatsr.setNbVariablesInstantiated(listJury.size());
				int[] order = new int[listJury.size()];
				for(int i = 0; i < listJury.size(); i++){
					order[i] = 7*i+3;
				}
				btsatsr.setOrder(order);
				btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
			}else if(algorithm.equals("BackTrackSearch")){
				/*
				algorithms.backtracksearch.BackTrackSearchJuryMembers bs = new algorithms.backtracksearch.BackTrackSearchJuryMembers();
				bs.search(listJury, jury_members, teachers, timeLimit, nbJuries);
				foundSolution = bs.hasSolution();
				*/
				JuryPartitionerAdvanced JP = new JuryPartitionerAdvanced();

				//Vector<JuryDataForScheduling> jdfs = JP.partitionJury(listJury, hustTeachers, nonHustTeachers, nbJuries, timeLimit);
				Vector<JuryDataForScheduling> jdfs = JP.partitionJury(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
				
				for(int i = 0; i < jdfs.size(); i++){
					Vector<JuryInfo> jury = jdfs.get(i).getJuryList();
					Vector<Teacher> ht = jdfs.get(i).getHustTeachers();
					Vector<Teacher> nht = jdfs.get(i).getNonHustTeachers();
					Vector<Integer> T = new Vector<Integer>();
					for(int j = 0; j < ht.size(); j++)
						T.add(ht.get(j).getID());
					for(int j = 0; j < nht.size(); j++)
						T.add(nht.get(j).getID());
					/*
					//algorithms.backtracksearch.BackTrackSearchJuryMembers bs = new algorithms.backtracksearch.BackTrackSearchJuryMembers();
					bs.search(jury, T, teachers, timeLimit);
					if(!bs.hasSolution()){
						foundSolution = false;
						retMsg += bs.getErrorMsg() + "\n";
					}
					*/
					for(int j = 0; j < jury.size(); j++){
						JuryInfo ji = jury.get(j);
						ji.setRoomId(i+1);
					}
				}
			}else if(algorithm.equals("BackTrackSearchAssignTeachersSlotsRooms")){
				
				BackTrackSearchAssignTeachersSlotsRooms bs = new BackTrackSearchAssignTeachersSlotsRooms();
				bs.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
			}
			
			foundSolution = true;
			retMsg = "Not complete";
			String xml = "";
			if(foundSolution)
				//xml = Utility.checkConsistency(listJury);
				//xml = Utility.generateAndCheckConsistency(listJury);
				xml = Utility.generateAndCheckConsistency(listJury,"Not-Sort");
			else{
				xml = "<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "UTF-8" + "\"" + "?>";
				xml += "<return-search-solution>";
				xml += "<solution>";
				xml += "false";
				xml += "</solution>";
				xml += "<error>";
				xml += retMsg;
				xml += "</error>";
				xml += "</return-search-solution>";
			}
			out.println(xml);
			//System.out.println("TeacherManager::processCheckListJury --> Return to clients xml\n" + xml);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	private void processSearchSolutionJuriesMembersBasedOnSlots(Document doc, HttpServletResponse response){
		System.out.println("processSearchSolutionJuriesMembersBasedOnSlots");
		
		Vector<Teacher> teachers = Utility.getTeachers();
		HashMap<Integer, String> id2Name = new HashMap<Integer,String>();
		for(int i = 0; i < teachers.size(); i++){
			Teacher t = teachers.get(i);
			id2Name.put(t.getID(),t.getName());
		}
		Vector<JuryInfo> listJury = new Vector<JuryInfo>();
		
		NodeList juryNodeList = doc.getElementsByTagName("Jury");
		for(int i = 0; i < juryNodeList.getLength(); i++){
			Node juryNod = juryNodeList.item(i);
			Element juryElement = (Element)juryNod;
			
			
			NodeList nl = juryElement.getElementsByTagName("studentID");
			Node nod = nl.item(0);
			Element el = (Element)nod;		
			int studentID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); //nod.getNodeValue();
			
			nl = juryElement.getElementsByTagName("studentName");
			nod = nl.item(0);
			el = (Element)nod;		
			String studentName = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();

			nl = juryElement.getElementsByTagName("thesis_title");
			nod = nl.item(0);
			el = (Element)nod;		
			String thesis_title = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();

			//System.out.print(studentID + "\t" + studentName + "\t" + thesis_title);
			
			nl = juryElement.getElementsByTagName("supervisor");
			nod = nl.item(0);
			el = (Element)nod;		
			//String examiner1 = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int supervisorID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			nl = juryElement.getElementsByTagName("examiner1");
			nod = nl.item(0);
			el = (Element)nod;		
			//String examiner1 = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int examiner1ID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			nl = juryElement.getElementsByTagName("examiner2");
			nod = nl.item(0);
			el = (Element)nod;		
			//String examiner2 = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int examiner2ID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			nl = juryElement.getElementsByTagName("president");
			nod = nl.item(0);
			el = (Element)nod;		
			//String president = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int presidentID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			nl = juryElement.getElementsByTagName("secretary");
			nod = nl.item(0);
			el = (Element)nod;		
			//String secretary = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int secretaryID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); 
			
			nl = juryElement.getElementsByTagName("member");
			nod = nl.item(0);
			el = (Element)nod;		
			//String member = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int memberID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			nl = juryElement.getElementsByTagName("slot");
			nod = nl.item(0);
			el = (Element)nod;		
			//String member = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int slotID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			nl = juryElement.getElementsByTagName("room");
			nod = nl.item(0);
			el = (Element)nod;		
			//String member = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int roomID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());

			/*
			JuryInfo jury = new JuryInfo(i,studentName,thesis_title,"null",examiner1,examiner2,president,secretary,member,0);
			
			jury.setStudentID(studentID);
			jury.setExaminerId1(name2ID.get(jury.getExaminerName1()));
			jury.setExaminerId2(name2ID.get(jury.getExaminerName2()));
			jury.setPresidentId(name2ID.get(jury.getPresidentName()));
			jury.setSecretaryId(name2ID.get(jury.getSecretaryName()));
			jury.setAdditionalMemberId(name2ID.get(jury.getAdditionalMemberName()));
			*/
			JuryInfo jury = new JuryInfo(i,studentID,thesis_title,examiner1ID,examiner2ID,presidentID,secretaryID,memberID,0);
			jury.setStudentName(studentName);
			jury.setExaminerName1(id2Name.get(examiner1ID));
			jury.setExaminerName2(id2Name.get(examiner2ID));
			jury.setPresidentName(id2Name.get(presidentID));
			jury.setSecretaryName(id2Name.get(secretaryID));
			jury.setAdditionalMemberName(id2Name.get(memberID));
			jury.setSupervisorId(supervisorID);
			jury.setSupervisorName(id2Name.get(supervisorID));
			
			jury.setSlotId(slotID);
			jury.setRoomId(roomID);
			
			listJury.add(jury);
			
			
			System.out.println("TeacherManager::SearchSolution, Jury: " + studentID + "\t" + studentName + "\t" + 
			thesis_title + "\t" + examiner1ID + "\t" + examiner2ID + "\t" + presidentID + "\t" + secretaryID +
			"\t" + memberID + "\t" + slotID + "\t" + roomID);
		}
		/*
		NodeList nlRoomInfo = doc.getElementsByTagName("room-info");
		Vector<Room> rooms = new Vector<Room>();
		for(int i = 0; i < nlRoomInfo.getLength(); i++){
			Node nod = nlRoomInfo.item(i);
			Element e = (Element)nod;
			int rid = Integer.valueOf(e.getElementsByTagName("id").item(0).getChildNodes().item(0).getNodeValue());
			int nbJuries = Integer.valueOf(e.getElementsByTagName("nbJuries").item(0).getChildNodes().item(0).getNodeValue());
			int nbNonHust = Integer.valueOf(e.getElementsByTagName("nbNonHust").item(0).getChildNodes().item(0).getNodeValue());
			int nbHust = Integer.valueOf(e.getElementsByTagName("nbHust").item(0).getChildNodes().item(0).getNodeValue());
			
			//Room r = new Room(0,"-",0);
			Room r = new Room(rid,"-",0);
			r.setNbJuries(nbJuries);
			r.setMaxNbNonHustMembers(nbNonHust);
			r.setMaxNbHustMembers(nbHust);
			System.out.println("rooms[" + i + "], nbJuries = " + nbJuries + ", nbNonHust = " + nbNonHust + ", nbHust = " + nbHust);
			rooms.add(r);
		}
		*/
		//if(true) return;
		
		NodeList nl = doc.getElementsByTagName("time");
		Node nod = nl.item(0);
		Element e = (Element)nod;
		int timeLimit = Integer.valueOf(e.getChildNodes().item(0).getNodeValue());
		
		nl = doc.getElementsByTagName("algorithm");
		nod = nl.item(0);
		e = (Element)nod;
		String algorithm = e.getChildNodes().item(0).getNodeValue();
		
		nl = doc.getElementsByTagName("jury-members");
		Vector<Integer> jury_members = new Vector<Integer>();
		nod = nl.item(0);
		e = (Element)nod;
		nl = e.getElementsByTagName("jury-member");
		System.out.println("processSearchSolutionJuryMembers --> jury-members = " + nl.getLength());
		for(int i = 0; i < nl.getLength(); i++){
			nod = nl.item(i);
			e = (Element)nod;
			int jm = Integer.valueOf(e.getChildNodes().item(0).getNodeValue());
			jury_members.add(jm);
		}
		
		
		nl = doc.getElementsByTagName("nb-juries");
		nod = nl.item(0);
		e = (Element)nod;
		int nbJuries = Integer.valueOf(e.getChildNodes().item(0).getNodeValue());
		
		Vector<Room> rooms = new Vector<Room>();
		for(int i = 1; i <= nbJuries; i++){
			rooms.add(new Room(i,"-",0));
		}
		
		//System.out.println("DefenseSchedulerManager::processSearchSolutionJuriesMembersBasedOnSlot, nbJuries = " + nbJuries +		
			//	" time-limit = " + timeLimit + " algorithm = " + algorithm);
		
		Vector<Teacher> hustTeachers = new Vector<Teacher>();
		Vector<Teacher> nonHustTeachers = new Vector<Teacher>();
		Vector<Teacher> AT = new Vector<Teacher>();
		
		HashMap<Integer, Teacher> mTeacher = new HashMap<Integer, Teacher>();
		for(int i = 0; i < teachers.size(); i++){
			Teacher t = teachers.get(i);
			mTeacher.put(t.getID(), t);
		}
		
		for(int i = 0; i < jury_members.size(); i++){
			Teacher t = mTeacher.get(jury_members.get(i));
			System.out.println(t.getID() + "\t" + t.getInstitute());
			if(t.getInstitute().equals("HUST")){
				hustTeachers.add(t);
				//System.out.println("DefenseScheduleManager::processSearchSolutionJuriesMembers--> hust " + t.getName());
			}else{
				nonHustTeachers.add(t);
				//System.out.println("DefenseScheduleManager::processSearchSolutionJuriesMembers--> nonhust " + t.getName());
			}
			AT.add(t);
		}

		
		
		try{
			PrintWriter out = response.getWriter();
			boolean foundSolution = true;
			String retMsg = "non error";
			
			int n = listJury.size();
			int sz_of_jury = n/nbJuries;
			int d = n%nbJuries;

			for(int i = 0; i < rooms.size(); i++){
				Room r = rooms.get(i);
				r.setNbJuries(sz_of_jury);
				r.setMaxNbHustMembers(hustTeachers.size()/nbJuries);
				r.setMaxNbNonHustMembers(nonHustTeachers.size()/nbJuries);
			}
			for(int i = 0; i < d; i++){
				Room r  = rooms.get(i);
				r.setNbJuries(r.getMaxNbJuries()+1);
				
			}
			for(int i = 0; i < hustTeachers.size()%nbJuries; i++){
				Room r = rooms.get(i);
				r.setMaxNbHustMembers(r.getMaxNbHustMembers()+1);
			}
			for(int i = 0; i < nonHustTeachers.size()%nbJuries; i++){
				Room r = rooms.get(i);
				r.setMaxNbNonHustMembers(r.getMaxNbNonHustMembers()+1);
			}
			
			for(int i = 0; i < rooms.size(); i++){
				Room r = rooms.get(i);
				System.out.println("Room[" + i + "] " + ", nbJuries = " + r.getMaxNbJuries() + ", maxHust = " + r.getMaxNbHustMembers() + ", nonHust = " + r.getMaxNbNonHustMembers());
			}

			
			
			PreCheckFeasibility pc = new PreCheckFeasibility();
			//write data to text file
			pc.writeDataToFile(listJury, rooms, hustTeachers, nonHustTeachers, "jury.txt");
			
			if(!pc.check(listJury, rooms, hustTeachers, nonHustTeachers)){
				foundSolution = false;
				retMsg = pc.getMsg();
			}else{
				
			if(algorithm.equals("AssignAll")){
				System.out.println("DefenseScheduleManager::searchSolution, algorithms = AssignAll");
				//JuryPartitionerBasedOnSlots jpbos = new JuryPartitionerBasedOnSlots();
				//jpbos.partitionJury(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
				
				// assign president, rooms, slots
				AssignPresidentsRooms apr = new AssignPresidentsRooms();
				apr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
				if(!apr.foundSolution()){
					System.out.println("DefenseScheduleManager::processSearchSolutionJuriesMembersBasedOnSlots, start assigning presidents");

					BackTrackSearchTeachersSlotsRoomsObjectiveMatch btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
					btsatsr.setNbVariablesInstantiated(listJury.size());
					int[] order = new int[listJury.size()];
					for(int i = 0; i < listJury.size(); i++){
						order[i] = 7*i+2;
					}
					btsatsr.setOrder(order);
					btsatsr.setObjectiveType("expert-level");
					btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
					if(!btsatsr.foundSolution()){
						foundSolution = false;
						retMsg = "Khong the gan chu tich";
					}else{
						foundSolution = true;
					}
					
					if(foundSolution){
						System.out.println("DefenseScheduleManager::processSearchSolutionJuriesMembersBasedOnSlots, start assigning rooms");

							//BackTrackSearchTeachersSlotsRoomsObjectiveMatch btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
							//btsatsr.setNbVariablesInstantiated(listJury.size());
							//int[] order = new int[listJury.size()];
							for(int i = 0; i < listJury.size(); i++){
								order[i] = 7*i+6;
							}
							btsatsr.setOrder(order);
							btsatsr.setObjectiveType("movements");
							btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
							
							if(!btsatsr.foundSolution()){
								foundSolution = false;
								retMsg = "Khong the xep duoc phong (da xep duoc chu tich)";
							}else{
								foundSolution = true;
							}
							
							
					}
					if(foundSolution){
						System.out.println("DefenseScheduleManager::processSearchSolutionJuriesMembersBasedOnSlots, start assigning slots");
						//BackTrackSearchTeachersSlotsRoomsObjectiveMatch btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
						//btsatsr.setNbVariablesInstantiated(listJury.size());
						//int[] order = new int[listJury.size()];
						for(int i = 0; i < listJury.size(); i++){
							order[i] = 7*i+5;
						}
						btsatsr.setOrder(order);
						//btsatsr.setObjectiveType("movements");
						btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
						
						if(!btsatsr.foundSolution()){
							foundSolution = false;
							retMsg = "Khong the xep kip (da xep duoc chu tich va phong)";
						}else{
							foundSolution = true;
						}
					}

				}
				if(!foundSolution){
					//retMsg = "Khong the gan chu tich va phong truoc";
				}else{
					// assign examiner2
					System.out.println("DefenseScheduleManager::processSearchSolutionJuriesMembersBasedOnSlots, start assigning examiner 2");
					BackTrackSearchTeachersSlotsRoomsObjectiveMatch btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
					btsatsr.setNbVariablesInstantiated(listJury.size());
					int[] order = new int[listJury.size()];
					for(int i = 0; i < listJury.size(); i++){
						order[i] = 7*i+1;
					}
					btsatsr.setOrder(order);
					btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
					
					if(!btsatsr.foundSolution()){
						foundSolution = false;
						retMsg = "Khong the xep phan bien 2";
					}else{
						// assign secretary
						btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
						btsatsr.setNbVariablesInstantiated(listJury.size());
						//int[] order = new int[listJury.size()];
						
						System.out.println("DefenseScheduleManager::processSearchSolutionJuriesMembersBasedOnSlots, start assigning secretary");
						for(int i = 0; i < listJury.size(); i++){
							order[i] = 7*i+3;
						}
						btsatsr.setOrder(order);
						btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
						
						if(!btsatsr.foundSolution()){
							foundSolution = false;
							retMsg = "Khong the xep thu ky";
						}else{
							
						
							// assign examiner 1
							btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
							btsatsr.setNbVariablesInstantiated(listJury.size());
							//int[] order = new int[listJury.size()];
							
							System.out.println("DefenseScheduleManager::processSearchSolutionJuriesMembersBasedOnSlots, start assigning examiner 1");
							for(int i = 0; i < listJury.size(); i++){
								order[i] = 7*i;
							}
							btsatsr.setOrder(order);
							btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
							
							if(!btsatsr.foundSolution()){
								foundSolution = false;
								retMsg = "Khong the xep phan bien 1";
							}else{
								// assign additional members
								btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
								btsatsr.setNbVariablesInstantiated(listJury.size());
								//int[] order = new int[listJury.size()];
								System.out.println("DefenseScheduleManager::processSearchSolutionJuriesMembersBasedOnSlots, start assigning additional members");
								for(int i = 0; i < listJury.size(); i++){
									order[i] = 7*i+4;
								}
								btsatsr.setOrder(order);
								btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
								
								if(!btsatsr.foundSolution()){
									foundSolution = false;
									retMsg = "Khong the xep uy vien";
								}else{
									// balance examiner 1
									AssignExaminer1 ae1 = new AssignExaminer1();
									ae1.balances(listJury, nonHustTeachers);
									
									// balance examiner 2
									AssignExaminer2 ae2 = new AssignExaminer2();
									ae2.balances(listJury, hustTeachers);
								}
							}
						}
					}
				}
			}else if(algorithm.equals("AssignSlots")){
				/*
				JuryPartitionerBasedOnSlots jpbos = new JuryPartitionerBasedOnSlots();
				jpbos.partitionJury(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
				if(!jpbos.foundSolution()){
					foundSolution = false;
					retMsg = "Khong the xep kip";
				}
				*/
				System.out.println("DefenseScheduleManager::processSearchSolutionJuriesMembersBasedOnSlots, start assigning slots");
				BackTrackSearchTeachersSlotsRoomsObjectiveMatch btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
				btsatsr.setNbVariablesInstantiated(listJury.size());
				int[] order = new int[listJury.size()];
				for(int i = 0; i < listJury.size(); i++){
					order[i] = 7*i+5;
				}
				btsatsr.setOrder(order);
				//btsatsr.setObjectiveType("movements");
				btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
				
				if(!btsatsr.foundSolution()){
					foundSolution = false;
					retMsg = "Khong the xep kip";
				}
			}else if(algorithm.equals("AssignRooms")){
				System.out.println("DefenseScheduleManager::processSearchSolutionJuriesMembersBasedOnSlots, start assigning rooms");
				BackTrackSearchTeachersSlotsRoomsObjectiveMatch btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
				btsatsr.setNbVariablesInstantiated(listJury.size());
				int[] order = new int[listJury.size()];
				for(int i = 0; i < listJury.size(); i++){
					order[i] = 7*i+6;
				}
				btsatsr.setOrder(order);
				btsatsr.setObjectiveType("movements");
				btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
				
				if(!btsatsr.foundSolution()){
					foundSolution = false;
					retMsg = "Khong the xep phong";
				}
			}else if(algorithm.equals("AssignPresidentsRooms")){
				System.out.println("DefenseScheduleManager::processSearchSolutionJuriesMembersBasedOnSlots, start assigning presidents rooms");
				
				/*
				AssignPresidentsRooms apr = new AssignPresidentsRooms();
				//AssignPresidentsRoomsV2 apr = new AssignPresidentsRoomsV2();
				apr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
				*/
				
				PartitionJuriesToRoomsTeachersNotMove pjtr = new PartitionJuriesToRoomsTeachersNotMove();
				pjtr.setNbVariablesInstantiated(listJury.size());
				int[] order = new int[listJury.size()];
				for(int i = 0; i < listJury.size(); i++){
					order[i] = 7*i+6;
				}
				pjtr.setOrder(order);
				pjtr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
				//if(!apr.foundSolution()){
				if(!pjtr.foundSolution()){
					foundSolution = false;
					retMsg = "Khong the phan chia duoc phong";
					retMsg += "\n" + pjtr.getReturnedMsg();
				}else{
					JuryInfo[] sj = new JuryInfo[listJury.size()];
					for(int i = 0; i < listJury.size(); i++)
						sj[i] = listJury.get(i);
					for(int i = 0; i < sj.length-1; i++)
						for(int j = i+1; j < sj.length; j++)
							if(sj[i].getRoomId() > sj[j].getRoomId()){
								JuryInfo tj = sj[i]; sj[i] = sj[j]; sj[j] = tj;
							}
					listJury.clear();
					for(int i = 0; i < sj.length; i++)
						listJury.add(sj[i]);
					
					BackTrackSearchTeachersSlotsRoomsObjectiveMatchNotMove btsnm = new BackTrackSearchTeachersSlotsRoomsObjectiveMatchNotMove();
					btsnm.setNbVariablesInstantiated(listJury.size());
					order = new int[listJury.size()];
					for(int i = 0; i < listJury.size(); i++){
						order[i] = 7*i+2;
					}
					btsnm.setObjectiveType("expert-level");
					btsnm.setOrder(order);
					btsnm.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
					if(!btsnm.foundSolution()){
						foundSolution = false;
						retMsg += "Sau khi phan chia phong, khong the xep duoc chu tich";
						retMsg += "\n" + btsnm.getReturnedMsg();
					}
				}
				
				/*
				JuryPartitionerAdvanced JP = new JuryPartitionerAdvanced();
				Vector<JuryDataForScheduling> jdfs = JP.partitionJury(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
				HashMap[] matchScore = Utility.computeMatchJuryProfessor(listJury, AT);//JP.getMatchJuryProfessor();
				
				AssignTopExpert ate = new AssignTopExpert();
				ate.search(listJury, rooms, matchScore, hustTeachers, timeLimit);
				
				AssignSlot as = new AssignSlot();
				as.search(listJury, rooms, timeLimit);
				*/
			}else if(algorithm.equals("AssignExaminer1")){
				//HashMap[] matchScore = Utility.computeMatchJuryProfessor(listJury, AT);//JP.getMatchJuryProfessor();
				//AssignExaminer1 ae1 = new AssignExaminer1();
				//ae1.search(listJury, rooms, matchScore, nonHustTeachers, timeLimit);
				System.out.println("DefenseScheduleManager::processSearchSolutionJuriesMembersBasedOnSlots, start assigning examiner 1");
				BackTrackSearchTeachersSlotsRoomsObjectiveMatch btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatchNotMove();
				btsatsr.setNbVariablesInstantiated(listJury.size());
				int[] order = new int[listJury.size()];
				for(int i = 0; i < listJury.size(); i++){
					order[i] = 7*i;
				}
				btsatsr.setOrder(order);
				btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
				
				if(!btsatsr.foundSolution()){
					
					btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
					btsatsr.setNbVariablesInstantiated(listJury.size());
					order = new int[listJury.size()];
					for(int i = 0; i < listJury.size(); i++){
						order[i] = 7*i;
					}
					btsatsr.setOrder(order);
					btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
					
					if(!btsatsr.foundSolution()){
						foundSolution = false;
						retMsg = "Khong the xep phan bien 1";
					}
				}
			}else if(algorithm.equals("AssignExaminer2")){
				//HashMap[] matchScore = Utility.computeMatchJuryProfessor(listJury, AT) ;//JP.getMatchJuryProfessor();
				//AssignExaminer2 ae2 = new AssignExaminer2();
				//ae2.search(listJury, rooms, matchScore, hustTeachers, timeLimit);
				System.out.println("DefenseScheduleManager::processSearchSolutionJuriesMembersBasedOnSlots, start assigning examiner 2");
				BackTrackSearchTeachersSlotsRoomsObjectiveMatch btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatchNotMove();
				btsatsr.setNbVariablesInstantiated(listJury.size());
				int[] order = new int[listJury.size()];
				for(int i = 0; i < listJury.size(); i++){
					order[i] = 7*i+1;
				}
				btsatsr.setOrder(order);
				btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
				if(!btsatsr.foundSolution()){
					btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
					btsatsr.setNbVariablesInstantiated(listJury.size());
					order = new int[listJury.size()];
					for(int i = 0; i < listJury.size(); i++){
						order[i] = 7*i+1;
					}
					btsatsr.setOrder(order);
					btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
					if(!btsatsr.foundSolution()){
						foundSolution = false;
						retMsg = "Khong the xep phan bien 2";
					}
				}
			}else if(algorithm.equals("AssignAdditionalMember")){
				
				//HashMap[] matchScore = Utility.computeMatchJuryProfessor(listJury, AT); //JP.getMatchJuryProfessor();
				//AssignAdditionalMember aa = new AssignAdditionalMember();
				//aa.search(listJury, rooms, matchScore, nonHustTeachers, timeLimit);
				System.out.println("DefenseScheduleManager::processSearchSolutionJuriesMembersBasedOnSlots, start assigning additional members");
				BackTrackSearchTeachersSlotsRoomsObjectiveMatch btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatchNotMove();
				btsatsr.setNbVariablesInstantiated(listJury.size());
				int[] order = new int[listJury.size()];
				for(int i = 0; i < listJury.size(); i++){
					order[i] = 7*i+4;
				}
				btsatsr.setOrder(order);
				btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
				if(!btsatsr.foundSolution()){
					
					btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
					btsatsr.setNbVariablesInstantiated(listJury.size());
					order = new int[listJury.size()];
					for(int i = 0; i < listJury.size(); i++){
						order[i] = 7*i+4;
					}
					btsatsr.setOrder(order);
					btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
					
					if(!btsatsr.foundSolution()){
						foundSolution = false;
						retMsg = "Khong the xep uy vien";
					}
				}
			}else if(algorithm.equals("AssignPresident")){
				//HashMap[] matchScore = Utility.computeMatchJuryProfessor(listJury, AT); //JP.getMatchJuryProfessor();
				//AssignPresident ap = new AssignPresident();
				//ap.search(listJury, rooms, matchScore, hustTeachers, timeLimit);
				System.out.println("DefenseScheduleManager::processSearchSolutionJuriesMembersBasedOnSlots, start assigning presidents");
				BackTrackSearchTeachersSlotsRoomsObjectiveMatch btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
				btsatsr.setNbVariablesInstantiated(listJury.size());
				int[] order = new int[listJury.size()];
				for(int i = 0; i < listJury.size(); i++){
					order[i] = 7*i+2;
				}
				btsatsr.setOrder(order);
				btsatsr.setObjectiveType("expert-level");
				btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
				if(!btsatsr.foundSolution()){
					foundSolution = false;
					retMsg = "Khong the xep chu tich";
				}
			}else if(algorithm.equals("AssignSecretary")){
				//HashMap[] matchScore = Utility.computeMatchJuryProfessor(listJury, AT); //JP.getMatchJuryProfessor();
				//AssignSecretary as = new AssignSecretary();
				//as.search(listJury, rooms, matchScore, hustTeachers, timeLimit);
				System.out.println("DefenseScheduleManager::processSearchSolutionJuriesMembersBasedOnSlots, start assigning secretary");
				BackTrackSearchTeachersSlotsRoomsObjectiveMatch btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatchNotMove();
				btsatsr.setNbVariablesInstantiated(listJury.size());
				int[] order = new int[listJury.size()];
				for(int i = 0; i < listJury.size(); i++){
					order[i] = 7*i+3;
				}
				btsatsr.setOrder(order);
				btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
				if(!btsatsr.foundSolution()){
					btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
					btsatsr.setNbVariablesInstantiated(listJury.size());
					order = new int[listJury.size()];
					for(int i = 0; i < listJury.size(); i++){
						order[i] = 7*i+3;
					}
					btsatsr.setOrder(order);
					btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
					if(!btsatsr.foundSolution()){
						foundSolution = false;
						retMsg = "Khong the xep thu ky";
					}
				}
			}else if(algorithm.equals("AssignAllProfessorsNotMove")){
				/*	
				JuryPartitionerAdvanced JP = new JuryPartitionerAdvanced();

				Vector<JuryDataForScheduling> jdfs = JP.partitionJury(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
				
				for(int i = 0; i < jdfs.size(); i++){
					Vector<JuryInfo> jury = jdfs.get(i).getJuryList();
					Vector<Teacher> ht = jdfs.get(i).getHustTeachers();
					Vector<Teacher> nht = jdfs.get(i).getNonHustTeachers();
					Vector<Integer> T = new Vector<Integer>();
					for(int j = 0; j < ht.size(); j++)
						T.add(ht.get(j).getID());
					for(int j = 0; j < nht.size(); j++)
						T.add(nht.get(j).getID());
					for(int j = 0; j < jury.size(); j++){
						JuryInfo ji = jury.get(j);
						ji.setRoomId(i+1);
					}
				}
				*/
				PartitionJuriesToRoomsTeachersNotMove pjtr = new PartitionJuriesToRoomsTeachersNotMove();
				pjtr.setNbVariablesInstantiated(listJury.size());
				int[] order = new int[listJury.size()];
				for(int i = 0; i < listJury.size(); i++){
					order[i] = 7*i+6;
				}
				pjtr.setOrder(order);
				pjtr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
				//if(!apr.foundSolution()){
				if(!pjtr.foundSolution()){
					foundSolution = false;
					retMsg = "Khong the phan chia duoc phong";
					retMsg += "\n" + pjtr.getReturnedMsg();
				}else{
					JuryInfo[] sj = new JuryInfo[listJury.size()];
					for(int i = 0; i < listJury.size(); i++)
						sj[i] = listJury.get(i);
					for(int i = 0; i < sj.length-1; i++)
						for(int j = i+1; j < sj.length; j++)
							if(sj[i].getRoomId() > sj[j].getRoomId()){
								JuryInfo tj = sj[i]; sj[i] = sj[j]; sj[j] = tj;
							}
					listJury.clear();
					for(int i = 0; i < sj.length; i++)
						listJury.add(sj[i]);
					
					BackTrackSearchTeachersSlotsRoomsObjectiveMatchNotMove btsnm = new BackTrackSearchTeachersSlotsRoomsObjectiveMatchNotMove();
					btsnm.setNbVariablesInstantiated(listJury.size());
					order = new int[listJury.size()];
					for(int i = 0; i < listJury.size(); i++){
						order[i] = 7*i+2;
					}
					btsnm.setObjectiveType("expert-level");
					btsnm.setOrder(order);
					btsnm.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
					if(!btsnm.foundSolution()){
						foundSolution = false;
						retMsg += "Sau khi phan chia phong, khong the xep duoc chu tich";
						retMsg += "\n" + btsnm.getReturnedMsg();
					}
				}
				
				if(foundSolution){
					AssignPresidentsRooms apr = new AssignPresidentsRooms();
					//AssignPresidentsRoomsV2 apr = new AssignPresidentsRoomsV2();
					apr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
					
					//AssignPresidentsRooms apr = new AssignPresidentsRooms();
					//apr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
					if(!apr.foundSolution()){
						System.out.println("DefenseScheduleManager::processSearchSolutionJuriesMembersBasedOnSlots, AssignPresidentsRooms failed -> start assigning presidents");
	
						BackTrackSearchTeachersSlotsRoomsObjectiveMatch btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
						btsatsr.setNbVariablesInstantiated(listJury.size());
						order = new int[listJury.size()];
						for(int i = 0; i < listJury.size(); i++){
							order[i] = 7*i+2;
						}
						btsatsr.setOrder(order);
						btsatsr.setObjectiveType("expert-level");
						btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
						if(!btsatsr.foundSolution()){
							foundSolution = false;
							retMsg = "Khong the xep chu tich";
						}else{
							foundSolution = true;
						}
						
						if(foundSolution){
							System.out.println("DefenseScheduleManager::processSearchSolutionJuriesMembersBasedOnSlots, start assigning rooms");
	
								//BackTrackSearchTeachersSlotsRoomsObjectiveMatch btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
								//btsatsr.setNbVariablesInstantiated(listJury.size());
								//int[] order = new int[listJury.size()];
								for(int i = 0; i < listJury.size(); i++){
									order[i] = 7*i+6;
								}
								btsatsr.setOrder(order);
								btsatsr.setObjectiveType("movements");
								btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
								
								if(!btsatsr.foundSolution()){
									foundSolution = false;
									retMsg = "Khong the xep phong (xep duoc chu tich)";
								}else{
									foundSolution = true;
								}
								
								
						}
						
	
					}
					
					if(foundSolution){
						System.out.println("DefenseScheduleManager::processSearchSolutionJuriesMembersBasedOnSlots, start assigning slots");
						BackTrackSearchTeachersSlotsRoomsObjectiveMatch btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
						btsatsr.setNbVariablesInstantiated(listJury.size());
						order = new int[listJury.size()];
						for(int i = 0; i < listJury.size(); i++){
							order[i] = 7*i+5;
						}
						btsatsr.setOrder(order);
						//btsatsr.setObjectiveType("movements");
						btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
						
						if(!btsatsr.foundSolution()){
							foundSolution = false;
							retMsg = "Khong the xep kip (xep duoc chu tich va phong)";
						}else{
							foundSolution = true;
						}
					}
					
					if(foundSolution){
						AssignNonHustToJuryRooms AN = new AssignNonHustToJuryRooms();
						AN.assignNonHustMembersToJuryRooms(listJury, nonHustTeachers, rooms, timeLimit);
						
						if(AN.foundSolution()){
							AssignHustToJuryRooms A = new AssignHustToJuryRooms();
							A.assignHustMembersToJuryRooms(listJury, hustTeachers, rooms, timeLimit);
							
							if(!A.foundSolution()){
								foundSolution = false;
								retMsg = "Khong the xep het thanh vien trong truong";
							}
						}else{
							foundSolution = false;
							retMsg = "Khong the xep het thanh vien ngoai truong";
						}
					}else{
						retMsg = "Khong the xep chu tich va phong";
						foundSolution = false;
					}
				}
			}else if(algorithm.equals("BackTrackSearchAssignTeachersSlotsRooms")){
				
				BackTrackSearchAssignTeachersSlotsRooms bs = new BackTrackSearchAssignTeachersSlotsRooms();
				bs.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
			}
		}
		
			//foundSolution = true;
			//retMsg = "Not complete";
			String xml = "";
			if(foundSolution)
				//xml = Utility.checkConsistency(listJury);
				//xml = Utility.generateAndCheckConsistency(listJury);
				xml = Utility.generateAndCheckConsistency(listJury,"Not-Sort");
			else{
				xml = "<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "UTF-8" + "\"" + "?>";
				xml += "<return-search-solution>";
				xml += "<solution>";
				xml += "false";
				xml += "</solution>";
				xml += "<error>";
				xml += retMsg;
				xml += "</error>";
				xml += "</return-search-solution>";
			}
			out.println(xml);
			//System.out.println("TeacherManager::processCheckListJury --> Return to clients xml\n" + xml);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	private void processBalanceLoad(Document doc, HttpServletResponse response){
		System.out.println("processBalanceLoad");
		
		Vector<Teacher> teachers = Utility.getTeachers();
		HashMap<Integer, String> id2Name = new HashMap<Integer,String>();
		for(int i = 0; i < teachers.size(); i++){
			Teacher t = teachers.get(i);
			id2Name.put(t.getID(),t.getName());
		}
		Vector<JuryInfo> listJury = new Vector<JuryInfo>();
		
		NodeList juryNodeList = doc.getElementsByTagName("Jury");
		for(int i = 0; i < juryNodeList.getLength(); i++){
			Node juryNod = juryNodeList.item(i);
			Element juryElement = (Element)juryNod;
			
			
			NodeList nl = juryElement.getElementsByTagName("studentID");
			Node nod = nl.item(0);
			Element el = (Element)nod;		
			int studentID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); //nod.getNodeValue();
			
			nl = juryElement.getElementsByTagName("studentName");
			nod = nl.item(0);
			el = (Element)nod;		
			String studentName = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();

			nl = juryElement.getElementsByTagName("thesis_title");
			nod = nl.item(0);
			el = (Element)nod;		
			String thesis_title = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();

			//System.out.print(studentID + "\t" + studentName + "\t" + thesis_title);
			
			nl = juryElement.getElementsByTagName("supervisor");
			nod = nl.item(0);
			el = (Element)nod;		
			//String examiner1 = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int supervisorID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			nl = juryElement.getElementsByTagName("examiner1");
			nod = nl.item(0);
			el = (Element)nod;		
			//String examiner1 = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int examiner1ID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			nl = juryElement.getElementsByTagName("examiner2");
			nod = nl.item(0);
			el = (Element)nod;		
			//String examiner2 = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int examiner2ID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			nl = juryElement.getElementsByTagName("president");
			nod = nl.item(0);
			el = (Element)nod;		
			//String president = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int presidentID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			nl = juryElement.getElementsByTagName("secretary");
			nod = nl.item(0);
			el = (Element)nod;		
			//String secretary = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int secretaryID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); 
			
			nl = juryElement.getElementsByTagName("member");
			nod = nl.item(0);
			el = (Element)nod;		
			//String member = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int memberID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			nl = juryElement.getElementsByTagName("slot");
			nod = nl.item(0);
			el = (Element)nod;		
			//String member = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int slotID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			nl = juryElement.getElementsByTagName("room");
			nod = nl.item(0);
			el = (Element)nod;		
			//String member = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int roomID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());

			/*
			JuryInfo jury = new JuryInfo(i,studentName,thesis_title,"null",examiner1,examiner2,president,secretary,member,0);
			
			jury.setStudentID(studentID);
			jury.setExaminerId1(name2ID.get(jury.getExaminerName1()));
			jury.setExaminerId2(name2ID.get(jury.getExaminerName2()));
			jury.setPresidentId(name2ID.get(jury.getPresidentName()));
			jury.setSecretaryId(name2ID.get(jury.getSecretaryName()));
			jury.setAdditionalMemberId(name2ID.get(jury.getAdditionalMemberName()));
			*/
			JuryInfo jury = new JuryInfo(i,studentID,thesis_title,examiner1ID,examiner2ID,presidentID,secretaryID,memberID,0);
			jury.setStudentName(studentName);
			jury.setExaminerName1(id2Name.get(examiner1ID));
			jury.setExaminerName2(id2Name.get(examiner2ID));
			jury.setPresidentName(id2Name.get(presidentID));
			jury.setSecretaryName(id2Name.get(secretaryID));
			jury.setAdditionalMemberName(id2Name.get(memberID));
			jury.setSupervisorId(supervisorID);
			jury.setSupervisorName(id2Name.get(supervisorID));
			
			jury.setSlotId(slotID);
			jury.setRoomId(roomID);
			
			listJury.add(jury);
			
			
		}
		
		NodeList nl = doc.getElementsByTagName("time");
		Node nod = nl.item(0);
		Element e = (Element)nod;
		int timeLimit = Integer.valueOf(e.getChildNodes().item(0).getNodeValue());
		
		nl = doc.getElementsByTagName("algorithm");
		nod = nl.item(0);
		e = (Element)nod;
		String algorithm = e.getChildNodes().item(0).getNodeValue();
		
		nl = doc.getElementsByTagName("jury-members");
		Vector<Integer> jury_members = new Vector<Integer>();
		nod = nl.item(0);
		e = (Element)nod;
		nl = e.getElementsByTagName("jury-member");
		for(int i = 0; i < nl.getLength(); i++){
			nod = nl.item(i);
			e = (Element)nod;
			int jm = Integer.valueOf(e.getChildNodes().item(0).getNodeValue());
			jury_members.add(jm);
		}
		
		
		nl = doc.getElementsByTagName("nb-juries");
		nod = nl.item(0);
		e = (Element)nod;
		int nbJuries = Integer.valueOf(e.getChildNodes().item(0).getNodeValue());
		
		Vector<Room> rooms = new Vector<Room>();
		for(int i = 1; i <= nbJuries; i++){
			rooms.add(new Room(i,"-",0));
		}
		
		//System.out.println("DefenseSchedulerManager::processSearchSolutionJuriesMembersBasedOnSlot, nbJuries = " + nbJuries +		
			//	" time-limit = " + timeLimit + " algorithm = " + algorithm);
		
		Vector<Teacher> hustTeachers = new Vector<Teacher>();
		Vector<Teacher> nonHustTeachers = new Vector<Teacher>();
		Vector<Teacher> AT = new Vector<Teacher>();
		
		HashMap<Integer, Teacher> mTeacher = new HashMap<Integer, Teacher>();
		for(int i = 0; i < teachers.size(); i++){
			Teacher t = teachers.get(i);
			mTeacher.put(t.getID(), t);
		}
		
		for(int i = 0; i < jury_members.size(); i++){
			Teacher t = mTeacher.get(jury_members.get(i));
			System.out.println(t.getID() + "\t" + t.getInstitute());
			if(t.getInstitute().equals("HUST")){
				hustTeachers.add(t);
				//System.out.println("DefenseScheduleManager::processSearchSolutionJuriesMembers--> hust " + t.getName());
			}else{
				nonHustTeachers.add(t);
				//System.out.println("DefenseScheduleManager::processSearchSolutionJuriesMembers--> nonhust " + t.getName());
			}
			AT.add(t);
		}

		try{
			PrintWriter out = response.getWriter();
			boolean foundSolution = true;
			String retMsg = "non error";
			if(algorithm.equals("examiner1")){
				AssignExaminer1 ae1 = new AssignExaminer1();
				ae1.balances(listJury, nonHustTeachers);
			}else if(algorithm.equals("examiner2")){
				AssignExaminer2 ae2 = new AssignExaminer2();
				ae2.balances(listJury, hustTeachers);
			}			
			foundSolution = true;
			retMsg = "Not complete";
			String xml = "";
			if(foundSolution)
				//xml = Utility.checkConsistency(listJury);
				//xml = Utility.generateAndCheckConsistency(listJury);
				xml = Utility.generateAndCheckConsistency(listJury,"Not-Sort");
			else{
				xml = "<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "UTF-8" + "\"" + "?>";
				xml += "<return-search-solution>";
				xml += "<solution>";
				xml += "false";
				xml += "</solution>";
				xml += "<error>";
				xml += retMsg;
				xml += "</error>";
				xml += "</return-search-solution>";
			}
			out.println(xml);
			//System.out.println("TeacherManager::processCheckListJury --> Return to clients xml\n" + xml);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private void processConsolidateJuriesOfDepartments(Document doc, HttpServletResponse response){
		System.out.println("processConsolidateJuriesOfDepartments");
		Vector<Department> depts = Utility.getDepartments();
		Vector<JuryInfo> listAllJury = new Vector<JuryInfo>();
		int delta = 0;
		for(int i = 0; i < depts.size(); i++){
			String dept_name = depts.get(i).getName();
			//System.out.println("Dept " + dept_name);
			Vector<JuryInfo> info_dept = Utility.getListJuryInfo("All", dept_name);
			int minR = 100000;
			int maxR = -minR;
			for(int j = 0; j < info_dept.size(); j++){
				JuryInfo jr = info_dept.get(j);
				//System.out.println(jr.getSupervisorName() + "\t" + jr.getStudentName());
				int room = jr.getRoomId();
				if(room <= 0) continue;
				room += delta;
				jr.setRoomId(room);
				if(room < minR) minR = room;
				if(room > maxR) maxR = room;
				
				listAllJury.add(jr);
			}
			delta = maxR;
		}
		
		
		try{
			PrintWriter out = response.getWriter();
			String xml = Utility.checkConsistency(listAllJury);
			out.println(xml);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	private void processSaveSchedule(Document doc, HttpServletResponse response){
		System.out.println("processSaveSchedule");
		
		Vector<Teacher> teachers = Utility.getTeachers();
		HashMap<Integer, String> id2Name = new HashMap<Integer,String>();
		for(int i = 0; i < teachers.size(); i++){
			Teacher t = teachers.get(i);
			id2Name.put(t.getID(),t.getName());
		}
		Vector<JuryInfo> listJury = new Vector<JuryInfo>();
		
		NodeList juryNodeList = doc.getElementsByTagName("Jury");
		for(int i = 0; i < juryNodeList.getLength(); i++){
			Node juryNod = juryNodeList.item(i);
			Element juryElement = (Element)juryNod;
			
			
			NodeList nl = juryElement.getElementsByTagName("studentID");
			Node nod = nl.item(0);
			Element el = (Element)nod;		
			int studentID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); //nod.getNodeValue();
			
			nl = juryElement.getElementsByTagName("studentName");
			nod = nl.item(0);
			el = (Element)nod;		
			String studentName = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();

			nl = juryElement.getElementsByTagName("thesis_title");
			nod = nl.item(0);
			el = (Element)nod;		
			String thesis_title = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();

			//System.out.print(studentID + "\t" + studentName + "\t" + thesis_title);
			
			nl = juryElement.getElementsByTagName("supervisor");
			nod = nl.item(0);
			el = (Element)nod;		
			//String examiner1 = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int supervisorID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			nl = juryElement.getElementsByTagName("examiner1");
			nod = nl.item(0);
			el = (Element)nod;		
			//String examiner1 = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int examiner1ID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			nl = juryElement.getElementsByTagName("examiner2");
			nod = nl.item(0);
			el = (Element)nod;		
			//String examiner2 = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int examiner2ID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			nl = juryElement.getElementsByTagName("president");
			nod = nl.item(0);
			el = (Element)nod;		
			//String president = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int presidentID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			nl = juryElement.getElementsByTagName("secretary");
			nod = nl.item(0);
			el = (Element)nod;		
			//String secretary = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int secretaryID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); 
			
			nl = juryElement.getElementsByTagName("member");
			nod = nl.item(0);
			el = (Element)nod;		
			//String member = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int memberID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			/*
			JuryInfo jury = new JuryInfo(i,studentName,thesis_title,"null",examiner1,examiner2,president,secretary,member,0);
			
			jury.setStudentID(studentID);
			jury.setExaminerId1(name2ID.get(jury.getExaminerName1()));
			jury.setExaminerId2(name2ID.get(jury.getExaminerName2()));
			jury.setPresidentId(name2ID.get(jury.getPresidentName()));
			jury.setSecretaryId(name2ID.get(jury.getSecretaryName()));
			jury.setAdditionalMemberId(name2ID.get(jury.getAdditionalMemberName()));
			*/
			JuryInfo jury = new JuryInfo(i,studentID,thesis_title,examiner1ID,examiner2ID,presidentID,secretaryID,memberID,0);
			jury.setStudentName(studentName);
			jury.setExaminerName1(id2Name.get(examiner1ID));
			jury.setExaminerName2(id2Name.get(examiner2ID));
			jury.setPresidentName(id2Name.get(presidentID));
			jury.setSecretaryName(id2Name.get(secretaryID));
			jury.setAdditionalMemberName(id2Name.get(memberID));
			jury.setSupervisorId(supervisorID);
			jury.setSupervisorName(id2Name.get(supervisorID));
			
			nl = juryElement.getElementsByTagName("slot");
			nod = nl.item(0);
			el = (Element)nod;		
			int slot = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); //nod.getNodeValue();
			
			nl = juryElement.getElementsByTagName("slot-description");
			nod = nl.item(0);
			el = (Element)nod;		
			String slotDescription = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();

			/*
			nl = juryElement.getElementsByTagName("room");
			nod = nl.item(0);
			el = (Element)nod;		
			int room = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); //nod.getNodeValue();
			*/
			
			jury.setSlotId(slot);
			jury.setSlotDescription(slotDescription);
			//jury.setRoomId(room);
			
			listJury.add(jury);
			
			
			//System.out.println("DefenseScheduleManager::processSaveSchedule: " + studentID + "\t" + studentName + "\t" + 
			//thesis_title + "\t" + examiner1ID + "\t" + examiner2ID + "\t" + presidentID + "\t" + secretaryID +
			//"\t" + memberID + "\t" + slot + "\t" + room);
		}
		
		int sessionID = Integer.valueOf(doc.getElementsByTagName("session-id").item(0).getChildNodes().item(0).getNodeValue());
		String roomName = doc.getElementsByTagName("room-name").item(0).getChildNodes().item(0).getNodeValue();
		
		System.out.println("DefenseScheduleManager::processSaveSchedule, session-id = " + sessionID + " room-name = " + roomName);
		
		Utility.addRoom(roomName, sessionID);
		Vector<Room> rooms = Utility.getRooms(roomName,sessionID);
		Room newRoom = rooms.get(0);
		
		/*
		for(int i = 0; i < rooms.size(); i++){
			Room r = rooms.get(i);
			if(r.getDescription().equals(roomName) && r.getSessionID() == sessionID){
				newRoom = r;
				break;
			}
		}
		*/
		
		for(int i = 0; i < listJury.size(); i++){
			JuryInfo jr = listJury.get(i);
			Utility.addSlot(jr.getSlotId(), jr.getSlotDescription(), newRoom.getID());
			Vector<Slot> sls = Utility.getSlots(i+1, newRoom.getID());
			
			Slot sl = sls.get(0);
			jr.setSlotId(sl.getID());
			jr.setSlotDescription(sl.getDescription());
			jr.setRoomId(newRoom.getID());
			jr.setRoomName(roomName);
			
		}
		
		Utility.saveJury(listJury);
		
		
	}

	private void processSaveScheduleRoomsSlots(Document doc, HttpServletResponse response){
		System.out.println("processSaveScheduleRoomsSlots");
		
		Vector<Teacher> teachers = Utility.getTeachers();
		HashMap<Integer, String> id2Name = new HashMap<Integer,String>();
		for(int i = 0; i < teachers.size(); i++){
			Teacher t = teachers.get(i);
			id2Name.put(t.getID(),t.getName());
		}
		Vector<JuryInfo> listJury = new Vector<JuryInfo>();
		
		NodeList juryNodeList = doc.getElementsByTagName("Jury");
		for(int i = 0; i < juryNodeList.getLength(); i++){
			Node juryNod = juryNodeList.item(i);
			Element juryElement = (Element)juryNod;
			
			
			NodeList nl = juryElement.getElementsByTagName("studentID");
			Node nod = nl.item(0);
			Element el = (Element)nod;		
			int studentID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); //nod.getNodeValue();
			
			nl = juryElement.getElementsByTagName("studentName");
			nod = nl.item(0);
			el = (Element)nod;		
			String studentName = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();

			nl = juryElement.getElementsByTagName("thesis_title");
			nod = nl.item(0);
			el = (Element)nod;		
			String thesis_title = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();

			//System.out.print(studentID + "\t" + studentName + "\t" + thesis_title);
			
			nl = juryElement.getElementsByTagName("supervisor");
			nod = nl.item(0);
			el = (Element)nod;		
			//String examiner1 = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int supervisorID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			nl = juryElement.getElementsByTagName("examiner1");
			nod = nl.item(0);
			el = (Element)nod;		
			//String examiner1 = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int examiner1ID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			nl = juryElement.getElementsByTagName("examiner2");
			nod = nl.item(0);
			el = (Element)nod;		
			//String examiner2 = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int examiner2ID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			nl = juryElement.getElementsByTagName("president");
			nod = nl.item(0);
			el = (Element)nod;		
			//String president = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int presidentID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			nl = juryElement.getElementsByTagName("secretary");
			nod = nl.item(0);
			el = (Element)nod;		
			//String secretary = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int secretaryID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); 
			
			nl = juryElement.getElementsByTagName("member");
			nod = nl.item(0);
			el = (Element)nod;		
			//String member = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			int memberID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue());
			
			/*
			JuryInfo jury = new JuryInfo(i,studentName,thesis_title,"null",examiner1,examiner2,president,secretary,member,0);
			
			jury.setStudentID(studentID);
			jury.setExaminerId1(name2ID.get(jury.getExaminerName1()));
			jury.setExaminerId2(name2ID.get(jury.getExaminerName2()));
			jury.setPresidentId(name2ID.get(jury.getPresidentName()));
			jury.setSecretaryId(name2ID.get(jury.getSecretaryName()));
			jury.setAdditionalMemberId(name2ID.get(jury.getAdditionalMemberName()));
			*/
			JuryInfo jury = new JuryInfo(i,studentID,thesis_title,examiner1ID,examiner2ID,presidentID,secretaryID,memberID,0);
			jury.setStudentName(studentName);
			jury.setExaminerName1(id2Name.get(examiner1ID));
			jury.setExaminerName2(id2Name.get(examiner2ID));
			jury.setPresidentName(id2Name.get(presidentID));
			jury.setSecretaryName(id2Name.get(secretaryID));
			jury.setAdditionalMemberName(id2Name.get(memberID));
			jury.setSupervisorId(supervisorID);
			jury.setSupervisorName(id2Name.get(supervisorID));
			
			nl = juryElement.getElementsByTagName("slot");
			nod = nl.item(0);
			el = (Element)nod;		
			int slot = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); //nod.getNodeValue();
			
			//nl = juryElement.getElementsByTagName("slot-description");
			//nod = nl.item(0);
			//el = (Element)nod;		
			//String slotDescription = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();

			nl = juryElement.getElementsByTagName("room");
			nod = nl.item(0);
			el = (Element)nod;		
			int room = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); //nod.getNodeValue();
			
			jury.setSlotId(slot);
			//jury.setSlotDescription(slotDescription);
			jury.setRoomId(room);
			
			listJury.add(jury);
			
			
			//System.out.println("DefenseScheduleManager::processSaveSchedule: " + studentID + "\t" + studentName + "\t" + 
			//thesis_title + "\t" + examiner1ID + "\t" + examiner2ID + "\t" + presidentID + "\t" + secretaryID +
			//"\t" + memberID + "\t" + slot + "\t" + room);
		}
		
		int sessionID = Integer.valueOf(doc.getElementsByTagName("session-id").item(0).getChildNodes().item(0).getNodeValue());
		
		Vector<Slot> t_slots = new Vector<Slot>();
		NodeList nodeListSlot = doc.getElementsByTagName("slot-info");
		for(int i = 0; i < nodeListSlot.getLength(); i++){
			Node nodSlot = nodeListSlot.item(i);
			Element eSlot = (Element)nodSlot;
			int slotID = Integer.valueOf(eSlot.getElementsByTagName("id").item(0).getChildNodes().item(0).getNodeValue());
			String slotDescription = eSlot.getElementsByTagName("description").item(0).getChildNodes().item(0).getNodeValue();
			Slot sl = new Slot(slotID,-1,slotDescription,-1);
			t_slots.add(sl);
		}
		
		Vector<Room> t_rooms = new Vector<Room>();
		NodeList nodeListRoom = doc.getElementsByTagName("room-info");
		for(int i = 0; i < nodeListRoom.getLength(); i++){
			Node nodRoom = nodeListRoom.item(i);
			Element eRoom = (Element)nodRoom;
			int roomID = Integer.valueOf(eRoom.getElementsByTagName("id").item(0).getChildNodes().item(0).getNodeValue());
			String roomDescription = eRoom.getElementsByTagName("name").item(0).getChildNodes().item(0).getNodeValue();
			Room r = new Room(roomID,roomDescription, sessionID);
			t_rooms.add(r);
		}
		
		
		
		System.out.println("DefenseScheduleManager::processSaveSchedule, session-id = " + sessionID + " room_sz = " + t_rooms.size() + " slot_sz = " + t_slots.size());
		
		Vector<Room> rooms_db = new Vector<Room>();
		HashMap<Integer, Room> mRooms = new HashMap<Integer, Room>();
		
		HashMap<Room, HashMap<Integer, Slot>> mRoomSlot = new HashMap<Room, HashMap<Integer, Slot>>(); 
		for(int i = 0; i < t_rooms.size(); i++){
			Room r = t_rooms.get(i);
			Utility.addRoom(r.getDescription(), sessionID);
			Vector<Room> rooms = Utility.getRooms(r.getDescription(),sessionID);
			Room newRoom = rooms.get(0);
			rooms_db.add(newRoom);
			mRooms.put(r.getID(), newRoom);
			
			mRoomSlot.put(newRoom, new HashMap<Integer, Slot>());
			for(int j = 0; j < t_slots.size(); j++){
				Slot t_sl = t_slots.get(j);
				Utility.addSlot(t_sl.getID(), t_sl.getDescription(), newRoom.getID());
				Vector<Slot> sls = Utility.getSlots(t_sl.getID(), newRoom.getID());
				Slot sl = sls.get(0);
				HashMap<Integer, Slot> mSL = mRoomSlot.get(newRoom);
				mSL.put(t_sl.getID(), sl);
			}
		}
		
		
		/*
		for(int i = 0; i < rooms.size(); i++){
			Room r = rooms.get(i);
			if(r.getDescription().equals(roomName) && r.getSessionID() == sessionID){
				newRoom = r;
				break;
			}
		}
		*/
		
		for(int i = 0; i < listJury.size(); i++){
			JuryInfo jr = listJury.get(i);
			Room r = mRooms.get(jr.getRoomId());
			Slot sl = mRoomSlot.get(r).get(jr.getSlotId());
			//Utility.addSlot(jr.getSlotId(), jr.getSlotDescription(), newRoom.getID());
			//Vector<Slot> sls = Utility.getSlots(i+1, newRoom.getID());
			
			//Slot sl = sls.get(0);
			
			jr.setSlotId(sl.getID());
			jr.setSlotDescription(sl.getDescription());
			jr.setRoomId(r.getID());
			jr.setRoomName(r.getDescription());
			
		}
		
		Utility.saveJury(listJury);
		
		Utility.cleanRoomsSlots(sessionID);
		/*
		try{
			if(cnn == null){
				Class.forName("com.mysql.jdbc.Driver");
				cnn = DriverManager.getConnection(cnnStr);
				for(int i = 0; i < listJury.size(); i++){
					JuryInfo jr = listJury.get(i);
					String sql = "update " + tblStudentDefense + " set Slot = ? , Room = ? where StudentID = ?";
					String sql_print = "update " + tblStudentDefense + " set Slot = " + jr.getSlotId() + " , Room = " + jr.getRoomId() + " where StudentID = " + jr.getStudentID();
					preparedStat = cnn.prepareStatement(sql);
					preparedStat.setInt(1, jr.getSlotId());
					preparedStat.setInt(2, jr.getRoomId());
					preparedStat.setInt(3, jr.getStudentID());
					preparedStat.executeUpdate();
					System.out.println("Update student_defense " + jr.getStudentName() + " successfully with sql = " + sql_print);
					//stat.executeQuery(sql);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close();
		}
		*/
	}

	public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
		String webRootPath = getClass().getClassLoader().getResource(".").getPath();
		System.out.println("DefenseScheduleManager::doPost, path = " + webRootPath);
		try{
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/x-www-form-urlencoded;accept-charset=\"UTF-8\"");
			
			ServletInputStream in = request.getInputStream();
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			Document doc = builder.parse(in);
			doc.getDocumentElement().normalize();
	
			System.out.println("doPost, doc.NodeName = " + doc.getDocumentElement().getNodeName());
			
			if(doc.getDocumentElement().getNodeName().compareTo("AddTeacher") == 0){
				System.out.println("AddTeacher");
				//processAddTeacher(doc);
			}else if(doc.getDocumentElement().getNodeName().compareTo("LoadTeachersList") == 0){
				System.out.println("LoadTeachersList");
				//processListTeachers(response);
			}else if(doc.getDocumentElement().getNodeName().compareTo("DeleteTeacher") == 0){
				System.out.println("DeleteTeacher");
				//processDeleteTeacher(doc,response);
			}else if(doc.getDocumentElement().getNodeName().compareTo("UpdateTeacher") == 0){
				System.out.println("UpdateTeacher");
				//processUpdateTeacher(doc,response);
			}else if(doc.getDocumentElement().getNodeName().compareTo("LoadStudentDefense") == 0){
				System.out.println("LoadStudentDefense");
				processLoadStudentDefense(doc,response);
			}else if(doc.getDocumentElement().getNodeName().compareTo("AddStudent") == 0){
				System.out.println("AddStudent");
				//processAddStudent(doc);
			}else if(doc.getDocumentElement().getNodeName().compareTo("LoadTeachersStudentsDepartmentsList") == 0){
				System.out.println("LoadTeachersStudentsDepartmentsList");
				//processListTeachersStudents(response);
				processListTeachersStudentsDepartments(response);
			}else if(doc.getDocumentElement().getNodeName().compareTo("LoadStudentsOfDepartment") == 0){
				System.out.println("LoadStudentsOfDepartment");
				//processListTeachersStudents(response);
				processListStudentsOfDepartment(doc,response);
			}else if(doc.getDocumentElement().getNodeName().compareTo("LoadTeachersOfDepartment") == 0){
				System.out.println("LoadTeachersOfDepartment");
				//processListTeachersStudents(response);
				processListTeachersOfDepartment(doc,response);
			}else if(doc.getDocumentElement().getNodeName().compareTo("LoadTeachersStudentsDepartmentsRoomsList") == 0){
				System.out.println("LoadTeachersStudentsDepartmentsRoomsList");
				//processListTeachersStudents(response);
				processListTeachersStudentsDepartmentsRooms(response);
			}else if(doc.getDocumentElement().getNodeName().compareTo("AddJury") == 0){
				System.out.println("AddJury");
				processAddJury(doc);
			}else if(doc.getDocumentElement().getNodeName().compareTo("AddJuryStudentSupervisor") == 0){
				System.out.println("AddJuryStudentSupervisor");
				processAddJuryStudentSupervisor(doc);
			}else if(doc.getDocumentElement().getNodeName().compareTo("UpdateJury") == 0){
				System.out.println("UpdateJury");
				processUpdateJury(doc);
			}else if(doc.getDocumentElement().getNodeName().compareTo("LoadJury") == 0){
				System.out.println("LoadJury");				
				processListJury(doc,response);
			}else if(doc.getDocumentElement().getNodeName().compareTo("LoadJuryByStudents") == 0){
				System.out.println("LoadJury");				
				processListJuryByStudents(doc,response);
			}else if(doc.getDocumentElement().getNodeName().compareTo("LoadAJury") == 0){
				System.out.println("LoadAJury");				
				processLoadAJury(doc,response);
				
			}else if(doc.getDocumentElement().getNodeName().compareTo("CheckListJury") == 0){
				System.out.println("CheckListJury");
				processCheckListJury(doc,response);				
			}else if(doc.getDocumentElement().getNodeName().compareTo("SortJury") == 0){
				System.out.println("SortJury");
				processSortJury(doc,response);				
			}else if(doc.getDocumentElement().getNodeName().compareTo("SuggestSlotRoom") == 0){
				System.out.println("CheckListJury");
				processSuggestSlotRoom(doc,response);				
			}else if(doc.getDocumentElement().getNodeName().compareTo("SearchSolution") == 0){
				System.out.println("SearchSolution");
				processSearchSolution(doc,response);				
			}else if(doc.getDocumentElement().getNodeName().compareTo("SearchSolutionJuryMembers") == 0){
				System.out.println("SearchSolutionJuryMembers");
				processSearchSolutionJuryMembers(doc,response);				
			}else if(doc.getDocumentElement().getNodeName().compareTo("SearchSolutionJuriesMembers") == 0){
				System.out.println("SearchSolutionJuriesMembers");
				processSearchSolutionJuriesMembers(doc,response);				
			}else if(doc.getDocumentElement().getNodeName().compareTo("SearchSolutionJuriesMembersBasedOnSlots") == 0){
				System.out.println("SearchSolutionJuriesMembersBasedOnSlots");
				processSearchSolutionJuriesMembersBasedOnSlots(doc,response);				
			}else if(doc.getDocumentElement().getNodeName().compareTo("BalanceLoad") == 0){
				System.out.println("BalanceLoad");
				processBalanceLoad(doc,response);				
			}else if(doc.getDocumentElement().getNodeName().compareTo("ConsolidateJuriesOfDepartments") == 0){
				System.out.println("ConsolidateJuriesOfDepartments");
				processConsolidateJuriesOfDepartments(doc,response);				
			}else if(doc.getDocumentElement().getNodeName().compareTo("SaveSchedule") == 0){
				System.out.println("SaveSchedule");
				processSaveSchedule(doc,response);				
			}else if(doc.getDocumentElement().getNodeName().compareTo("PrepareStore") == 0){
				System.out.println("PrepareStore");
				processPrepareStore(doc,response);				
			}else if(doc.getDocumentElement().getNodeName().compareTo("SaveScheduleRoomsSlots") == 0){
				System.out.println("SaveScheduleRoomsSlots");
				processSaveScheduleRoomsSlots(doc,response);				
			}else if(doc.getDocumentElement().getNodeName().compareTo("ViewJury") == 0){
				System.out.println("ViewJury");
				processViewJury(doc,response);				
			}else if(doc.getDocumentElement().getNodeName().compareTo("LoadJuryForRepair") == 0){
				System.out.println("LoadJuryForRepair");
				processLoadJuryForRepair(doc,response);				
			}else if(doc.getDocumentElement().getNodeName().compareTo("load-rooms-departments") == 0){
				System.out.println("load-rooms-departments");
				processLoadRoomsDepartments(doc,response);				
			}else if(doc.getDocumentElement().getNodeName().compareTo("SendMail") == 0){
				System.out.println("DefenseScheduleManager::SendMail");
				SendMail m = new SendMail();
				m.processSendMail();				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/*
	void close(){
		System.out.println("cnn, stat, rs --> close()");
		try{
			if(rs != null){
				rs.close();
				rs = null;
			}
			if(stat != null){
				stat.close();
				stat = null;
			}
			if(cnn != null){
				cnn.close();
				cnn = null;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	*/
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DefenseScheduleManager tm = new DefenseScheduleManager();
		//tm.getTeachers();
		/*
		System.out.println("Test JDBC");
		try{
			Class.forName("com.mysql.jdbc.Driver");
			Connection cnn = DriverManager.getConnection("jdbc:mysql://localhost:3306/shedulerdefense?user=root&password=");
			Statement stat = cnn.createStatement();
			ResultSet rs = stat.executeQuery("select * from professors");
			while(rs.next()){
				System.out.println(rs.getString("name"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("JDBC OK");
		*/
	}

}
