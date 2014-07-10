package requesthandlers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import utils.Utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Vector;
import DataEntity.*;

import java.util.*;
import java.net.URLEncoder;
import java.net.URLDecoder;
import utils.Configure;
public class TeacherManager extends HttpServlet {

	/**
	 * @param args
	 */
	private Connection cnn = null;
	private Statement stat = null;
	private PreparedStatement preparedStat = null;
	private ResultSet rs = null;
	
	//private String cnnStr = "jdbc:mysql://localhost:3306/shedulerdefense?user=root&password=";
	//private String cnnStr = "jdbc:mysql://localhost:3306/training_manager?user=root&password=";
	//private String cnnStr = "jdbc:mysql://localhost:3306/itraining_management_db?user=root&password=";
	/*
	private String cnnStr = "jdbc:mysql://localhost:3306/itraining_management_db?user=sdh&password=sdh@)!#&useUnicode=true&characterEncoding=UTF-8";
	private String tblProfessors = "professors";
	private String tblStudents = "supervise_students";
	private String tblStudentDefense = "student_defense";
	private String tblDepartments = "departments";
	private String tblRooms="rooms";
	private String tblSlots = "slots";
	private String tblDefenseSessions = "defensesession";
	private String tblClasses = "classes";
	*/
	
	public TeacherManager(){
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
	public Vector<Classes> getClasses(){
		Vector<Classes> classes = new Vector<Classes>();
		//System.out.println("Start JDBC");
		try{
			
			//System.out.println("Start JDBC");
			Class.forName("com.mysql.jdbc.Driver");
			//System.out.println("JDBC -->OK");
			cnn = DriverManager.getConnection(Configure.cnnStr);
			stat = cnn.createStatement();
			rs = stat.executeQuery("select * from " + Configure.tblClasses);
			//rs = stat.executeQuery("select * from students");
			while(rs.next()){
				
				String name = rs.getString("Name");
				
				int id = rs.getInt("ID");
				
				Classes st = new Classes(id,name);
				classes.add(st);
			}
			//out.println("server return results");
			
			System.out.println("get classes successfully");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close();
		}
		return classes;
	}
	*/
	
	/*

	public Vector<Teacher> getTeachers(){
		Vector<Teacher> teachers = new Vector<Teacher>();
		//System.out.println("Start JDBC");
		try{
			
			//System.out.println("Start JDBC");
			Class.forName("com.mysql.jdbc.Driver");
			//System.out.println("JDBC -->OK");
			cnn = DriverManager.getConnection(Configure.cnnStr);
			stat = cnn.createStatement();
			//rs = stat.executeQuery("select * from " + tblProfessors );
			String sqlStatement= "select professors.ID, professors.Name, professors.Institute, departments.Name from " + Configure.tblProfessors;
			sqlStatement += " inner join departments on ";
			sqlStatement += "professors.Department = departments.ID order by professors.Name ASC";
			rs = stat.executeQuery(sqlStatement);
			while(rs.next()){
				
				String name = rs.getString("professors.Name") ;
				int id = rs.getInt("professors.ID");
				
				String institute = rs.getString("professors.Institute");
				String department = rs.getString("departments.Name");
				
				//int id = rs.getInt("idsv");
				Teacher t = new Teacher(id,name,institute,department);
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
	*/
	
	/*
	public Vector<Student> getStudents(){
		Vector<Student> students = new Vector<Student>();
		Vector<Classes> classes = Utility.getClasses();
		HashMap<Integer, String> mClass = new HashMap<Integer,String>();
		for(int i = 0; i < classes.size(); i++){
			Classes cl = classes.get(i);
			mClass.put(cl.getID(), cl.getName());
		}
		//System.out.println("Start JDBC");
		try{
			
			//System.out.println("Start JDBC");
			Class.forName("com.mysql.jdbc.Driver");
			//System.out.println("JDBC -->OK");
			cnn = DriverManager.getConnection(Configure.cnnStr);
			stat = cnn.createStatement();
			rs = stat.executeQuery("select * from " + Configure.tblStudents);
			//rs = stat.executeQuery("select * from students");
			while(rs.next()){
				//System.out.println(rs.getString("name"));
				//System.out.println(rs.getInt("idgv"));
				String name = rs.getString("StudentName");
				//int id = Integer.valueOf(rs.getString("ID"));
				int id = rs.getInt("ID");
				String studentID=rs.getString("StudentID");
				String promotion = rs.getString("Promotion");
				//String class_student = rs.getString("Class");
				int classID = rs.getInt("Class");
				String className = mClass.get(classID);
				String email = rs.getString("Email");
				String phone = rs.getString("Phone");
				String subject = rs.getString("Subject");
				Date startDate = rs.getDate("StartDate");
				Date endDate = rs.getDate("EndDate");
				String type = rs.getString("Type");
				int status = rs.getInt("Status");
				//int id = rs.getInt("idsv");
				Student st = new Student(id,name,studentID,promotion,classID,className,email,phone,subject,startDate,endDate,type,status);
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
	*/
	
	/*
	public Vector<Student> getStudentsOfClass(int classID, int statusID){
		//if(classID == 0) return getStudents();// return all students
		
		Vector<Student> students = new Vector<Student>();
		Vector<Classes> classes = Utility.getClasses();
		HashMap<Integer, String> mClass = new HashMap<Integer,String>();
		for(int i = 0; i < classes.size(); i++){
			Classes cl = classes.get(i);
			mClass.put(cl.getID(), cl.getName());
		}
		//System.out.println("Start JDBC");
		try{
			
			//System.out.println("Start JDBC");
			Class.forName("com.mysql.jdbc.Driver");
			//System.out.println("JDBC -->OK");
			cnn = DriverManager.getConnection(Configure.cnnStr);
			stat = cnn.createStatement();
			String sql = "select * from " + Configure.tblStudents;
			if(classID > 0 && statusID > 0) sql += " where " + " Class = " + classID + " and " + " Status = " + statusID;
			else if(classID > 0) sql += " where Class = " + classID;
			else if(statusID > 0) sql += " where Status = " + statusID;
			rs = stat.executeQuery(sql);
			//rs = stat.executeQuery("select * from " + tblStudents + " where Class = " + classID);
			//rs = stat.executeQuery("select * from students");
			while(rs.next()){
				//System.out.println(rs.getString("name"));
				//System.out.println(rs.getInt("idgv"));
				String name = rs.getString("StudentName");
				//int id = Integer.valueOf(rs.getString("ID"));
				int id = rs.getInt("ID");
				String studentID=rs.getString("StudentID");
				String promotion = rs.getString("Promotion");
				//String class_student = rs.getString("Class");
				int r_classID = rs.getInt("Class");
				String className = mClass.get(r_classID);
				String email = rs.getString("Email");
				String phone = rs.getString("Phone");
				String subject = rs.getString("Subject");
				Date startDate = rs.getDate("StartDate");
				Date endDate = rs.getDate("EndDate");
				String type = rs.getString("Type");
				int status = rs.getInt("Status");
				//int id = rs.getInt("idsv");
				Student st = new Student(id,name,studentID,promotion,r_classID,className,email,phone,subject,startDate,endDate,type,status);
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
	*/
	
	/*
	public Vector<Student> getStudents(int status){
		Vector<Student> students = new Vector<Student>();
		Vector<Classes> classes = Utility.getClasses();
		HashMap<Integer, String> mClass = new HashMap<Integer,String>();
		for(int i = 0; i < classes.size(); i++){
			Classes cl = classes.get(i);
			mClass.put(cl.getID(), cl.getName());
		}
		//System.out.println("Start JDBC");
		try{
			
			//System.out.println("Start JDBC");
			Class.forName("com.mysql.jdbc.Driver");
			//System.out.println("JDBC -->OK");
			cnn = DriverManager.getConnection(Configure.cnnStr);
			stat = cnn.createStatement();
			rs = stat.executeQuery("select * from " + Configure.tblStudents + " where Status = " + status);
			//rs = stat.executeQuery("select * from students");
			while(rs.next()){
				//System.out.println(rs.getString("name"));
				//System.out.println(rs.getInt("idgv"));
				String name = rs.getString("StudentName");
				//int id = Integer.valueOf(rs.getString("ID"));
				int id = rs.getInt("ID");
				String studentID=rs.getString("StudentID");
				String promotion = rs.getString("Promotion");
				int classID = rs.getInt("Class");
				String class_student = mClass.get(classID);
				String email = rs.getString("Email");
				String phone = rs.getString("Phone");
				String subject = rs.getString("Subject");
				Date startDate = rs.getDate("StartDate");
				Date endDate = rs.getDate("EndDate");
				String type = rs.getString("Type");
				//int status = rs.getInt("Status");
				//int id = rs.getInt("idsv");
				Student st = new Student(id,name,studentID,promotion,classID,class_student,email,phone,subject,startDate,endDate,type,status);
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
	*/
	
	/*
	public Vector<Jury> getJuries(){
		Vector<Jury> juries = new Vector<Jury>();
		//System.out.println("Start JDBC");
		try{
			
			//System.out.println("Start JDBC");
			Class.forName("com.mysql.jdbc.Driver");
			//System.out.println("JDBC -->OK");
			cnn = DriverManager.getConnection(Configure.cnnStr);
			stat = cnn.createStatement();
			rs = stat.executeQuery("SELECT S.ID as ID, S.Title as ThesisTitle, ST.ID as StudentID, ST.StudentName, P1.Name as Examiner1Name, P1.ID as Examiner1ID, " +
			" P2.Name as Examiner2Name, P2.ID as Examiner2ID, P3.Name as PresidentName, P3.ID as PresidentID, " + 
					"P4.Name as SecretaryName, P4.ID as SecretaryID, P5.Name as MemberName, P5.ID as MemberID FROM " + 
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
	*/
	
	/*
	public Vector<Room> getRooms(){
		Vector<Room> rooms = new Vector<Room>();
		//System.out.println("Start JDBC");
		try{
			
			//System.out.println("Start JDBC");
			Class.forName("com.mysql.jdbc.Driver");
			//System.out.println("JDBC -->OK");
			cnn = DriverManager.getConnection(Configure.cnnStr);
			stat = cnn.createStatement();
			rs = stat.executeQuery("select * from " + Configure.tblRoom);
			//rs = stat.executeQuery("select * from students");
			while(rs.next()){
				
				String name = rs.getString("Description");
				
				int id = rs.getInt("ID");
				int sessionID = rs.getInt("DefenseSessionID");
				
				Room st = new Room(id,name,sessionID);
				rooms.add(st);
			}
			//out.println("server return results");
			
			System.out.println("get rooms successfully");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close();
		}
		return rooms;
	}
	*/
	
	/*
	public Vector<Slot> getSlots(){
		Vector<Slot> slots = new Vector<Slot>();
		//System.out.println("Start JDBC");
		try{
			
			//System.out.println("Start JDBC");
			Class.forName("com.mysql.jdbc.Driver");
			//System.out.println("JDBC -->OK");
			cnn = DriverManager.getConnection(Configure.cnnStr);
			stat = cnn.createStatement();
			rs = stat.executeQuery("select * from " + Configure.tblSlot);
			//rs = stat.executeQuery("select * from students");
			while(rs.next()){
				
				String description = rs.getString("Description");
				int id = rs.getInt("ID");
				int slotIndex = rs.getInt("SlotIndex");
				int roomID = rs.getInt("RoomID");
				//int id = rs.getInt("idsv");
				Slot sl = new Slot(id,slotIndex,description,roomID);
				slots.add(sl);
			}
			//out.println("server return results");
			
			System.out.println("get slots successfully");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close();
		}
		return slots;
	}
	*/
	
	/*
	public Vector<DefenseSession> getDefenseSessions(){
		Vector<DefenseSession> defenseSessions = new Vector<DefenseSession>();
		//System.out.println("Start JDBC");
		try{
			
			//System.out.println("Start JDBC");
			Class.forName("com.mysql.jdbc.Driver");
			//System.out.println("JDBC -->OK");
			cnn = DriverManager.getConnection(Configure.cnnStr);
			stat = cnn.createStatement();
			rs = stat.executeQuery("select * from " + Configure.tblDefenseSession);
			//rs = stat.executeQuery("select * from students");
			while(rs.next()){
				
				String name = rs.getString("Description");
				
				int id = rs.getInt("ID");
				int active = rs.getInt("Active");
				
				DefenseSession st = new DefenseSession(id,name,active);
				defenseSessions.add(st);
			}
			//out.println("server return results");
			
			System.out.println("get defenseSession successfully");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close();
		}
		return defenseSessions;
	}
	*/
	
	/*
	public Vector<Department> getDepartments(){
		Vector<Department> departments = new Vector<Department>();
		//System.out.println("Start JDBC");
		try{
			
			//System.out.println("Start JDBC");
			Class.forName("com.mysql.jdbc.Driver");
			//System.out.println("JDBC -->OK");
			cnn = DriverManager.getConnection(Configure.cnnStr);
			stat = cnn.createStatement();
			//rs = stat.executeQuery("select * from " + tblProfessors );
			String sqlStatement= "select * from " + Configure.tblDepartment;
			
			rs = stat.executeQuery(sqlStatement);
			while(rs.next()){
				
				String name = rs.getString("Name") ;
				int id = rs.getInt("ID");
				
				
				//int id = rs.getInt("idsv");
				Department t = new Department(id,name);
				departments.add(t);
			}
			//out.println("server return results");
			
			System.out.println("get teachers successfully");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close();
		}
		return departments;
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
				out.println("<teacher>" + "<id>" + t.getID() + "</id><name>" + t.getName() + 
						"</name><institute>" + t.getInstitute()+"</institute><institute-name>" + t.getInstituteName()+"</institute-name><degree>" + t.getDegree()+"</degree><department>" + 
						t.getDepartment() +"</department>" + "<expert-level>" + t.getExpertLevel() + 
						"</expert-level>" + "</teacher>");
				//System.out.println("<teacher>" + "<id>" + t.getID() + "</id><name>" + t.getName() + "</name></teacher>");
			}
			out.println("</Teachers>");
			//System.out.println("</Teachers>");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	public void processExistTeacher(Document doc,HttpServletResponse response){
		NodeList nl = doc.getElementsByTagName("id");
		Node nod = nl.item(0);
		Element el = (Element)nod;
		
		int id = Integer.parseInt(el.getChildNodes().item(0).getNodeValue());
		
		Teacher t= new Teacher();
		
		try{
			PrintWriter out = response.getWriter();
			
			
			
			//System.out.println("Start JDBC");
			try{
				
				//System.out.println("Start JDBC");
				Class.forName("com.mysql.jdbc.Driver");
				//System.out.println("JDBC -->OK");
				cnn = DriverManager.getConnection(Configure.cnnStr);
				//stat = cnn.createStatement();
				preparedStat = cnn.prepareStatement("select professors.ID, professors.Name, professors.Institute, professors.InstituteName, professors.Degree, professors.ExpertLevel, departments.Name from professors  inner join departments on professors.Department = departments.ID Where professors.ID = ?");
								
				preparedStat.setInt(1, id);
				rs = preparedStat.executeQuery();
				if (rs.next())
				{
					String name = rs.getString("professors.Name") ;
					
					
					String institute = rs.getString("professors.Institute");
					String instituteName = rs.getString("professors.InstituteName");
					String department = rs.getString("departments.Name");
					int expertLevel = rs.getInt("ExpertLevel");
					String degree = rs.getString("Degree");
					//int id = rs.getInt("idsv");
					t = new Teacher(id,name,institute,department,instituteName, degree);
					t.setExpertLevel(expertLevel);
				}
				
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				close();
			}
			Vector<Department> departments = Utility.getDepartments();
			
			
			out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			out.println("<LoadExistTeacher>");		
			out.println("<teacher>" + "<id>" + t.getID() + "</id><name>" + t.getName() + "</name><institute>" + 
			t.getInstitute()+
					"</institute>" + "<institute-name>" + 
			t.getInstituteName()+
			"</institute-name>" + 
			"<degree>" + t.getDegree() +"</degree>" +
			"<department>" + t.getDepartment() +"</department>" +
							 "<expert-level>" + t.getExpertLevel() + "</expert-level>" + "</teacher>");
			
			out.println("<Departments>");
			//System.out.println("<Teachers>");
			for(int i = 0; i < departments.size(); i++){
				Department d = departments.get(i);
				out.println("<depart>" + "<id_depart>" + d.getID() + "</id_depart><name_depart>" +d.getName() + "</name_depart></depart>");
				//System.out.println("<teacher>" + "<id>" + t.getID() + "</id><name>" + t.getName() + "</name></teacher>");
			}
			out.println("</Departments>");
			
			Vector<ProfessorSubjectCategoryMatch> m = Utility.getProfessorSubjectCategoryMatches();
			Vector<SubjectCategory> categories = Utility.getSubjectCategories();
			HashMap<Integer, String> mSubject = new HashMap<Integer, String>();
			for(int i = 0; i < categories.size(); i++){
				mSubject.put(categories.get(i).getID(),categories.get(i).getName());
			}
			
			Vector<ProfessorSubjectCategoryMatch> sel_pscm = new Vector<ProfessorSubjectCategoryMatch>();
			
			for(int i = 0; i < m.size(); i++){
				ProfessorSubjectCategoryMatch pscm = m.get(i);
				if(pscm.getProfessorID() == id){
					sel_pscm.add(pscm);
				}
			}
			HashMap<Integer, Integer> mSubjectMatchScore = new HashMap<Integer, Integer>();
			for(int i = 0; i < sel_pscm.size(); i++){
				mSubjectMatchScore.put(sel_pscm.get(i).getSubjectCategoryID(), sel_pscm.get(i).getMatchScore());
			}
			for(int i = 0; i < categories.size(); i++){
				SubjectCategory sc = categories.get(i);
				out.println("<professor-subject-category-match>");
				out.println("<professor-id>" + 0 + "</professor-id>");
				out.println("<subject-category-id>" + sc.getID() + "</subject-category-id>");
				out.println("<subject-category-name>" + sc.getName() + "</subject-category-name>");
				int score = Configure.maxMatchSubjectScore;
				if(mSubjectMatchScore.get(sc.getID()) != null)
					score = mSubjectMatchScore.get(sc.getID());
				out.println("<match-score>" + score + "</match-score>");
				out.println("</professor-subject-category-match>");

			}

			
			out.println("</LoadExistTeacher>");
			//System.out.println("</Teachers>");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void processExistStudent(Document doc,HttpServletResponse response){
		NodeList nl = doc.getElementsByTagName("id");
		Node nod = nl.item(0);
		Element el = (Element)nod;
		
		int id = Integer.parseInt(el.getChildNodes().item(0).getNodeValue());
		
		Student t= new Student();
		Vector<Classes> classes = Utility.getClasses();
		HashMap<Integer, String> mClass = new HashMap<Integer,String>();
		for(int i = 0; i < classes.size(); i++){
			Classes cl = classes.get(i);
			mClass.put(cl.getID(), cl.getName());
		}
		try{
			PrintWriter out = response.getWriter();
			
			
			
			//System.out.println("Start JDBC");
			try{
				
				//System.out.println("Start JDBC");
				Class.forName("com.mysql.jdbc.Driver");
				//System.out.println("JDBC -->OK");
				cnn = DriverManager.getConnection(Configure.cnnStr);
				
				preparedStat = cnn.prepareStatement("select * from supervise_students Where ID = ?");
				
				preparedStat.setInt(1, id);
				rs = preparedStat.executeQuery();
				
			
				//rs = stat.executeQuery("select * from students");
				while(rs.next()){
					//System.out.println(rs.getString("name"));
					//System.out.println(rs.getInt("idgv"));
					String name = rs.getString("StudentName");
					
					String studentID=rs.getString("StudentID");
					String promotion = rs.getString("Promotion");
					int classID = rs.getInt("Class");
					String class_student = mClass.get(classID);
					String email = rs.getString("Email");
					String phone = rs.getString("Phone");
					String subject = rs.getString("Subject");
					Date startDate = rs.getDate("StartDate");
					Date endDate = rs.getDate("EndDate");
					String type = rs.getString("Type");
					int status = rs.getInt("Status");
					//int id = rs.getInt("idsv");
					t = new Student(id,name,studentID,promotion,classID,class_student,email,phone,subject,startDate,endDate,type,status);
					
				}
				
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				close();
			}
			
			Vector<StudentStatus> ss = Utility.getStudentStatus();
			
			out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			out.println("<LoadExistStudent>");		
			
			out.println("<Classes>");
			for(int i = 0; i < classes.size(); i++){
				Classes cl = classes.get(i);
				out.println("<class>");
				out.println("<classID>" + cl.getID() + "</classID><className>" + cl.getName() + "</className>");
				System.out.println("<classID>" + cl.getID() + "</classID><className>" + cl.getName() + "</className>");
				out.println("</class>");
			}
			out.println("</Classes>");
			
			out.println("<student>" + "<id>" + t.getID() + "</id><studentID>"+ t.getStudentId()+"</studentID><name>" + 
			t.getName() + "</name><promotion>" + t.getPromotion() + "</promotion><classID>" + t.getClassID() + "</classID><class_student>"+t.getClass_Student()+
			"</class_student><email>"+t.getEmail()+"</email><phone>"+t.getPhone()+"</phone><subject>"+t.getSubject()+
			"</subject><startDate>"+t.getStartDate()+"</startDate><endDate>"+t.getEndDate()+"</endDate><type>"+t.getType()+
			"</type><status>"+t.getStatus()+"</status></student>");
			
			out.println("<StudentStatusList>");
			for(int i = 0; i < ss.size(); i++){
				StudentStatus ssi = ss.get(i);
				out.println("<StudentStatus>");
				out.println("<id>" + ssi.getID() + "</id>");
				out.println("<description>" + ssi.getDescription() + "</description>");
				out.println("</StudentStatus>");
				//System.out.println("<id>" + ssi.getID() + "</id>");
				//System.out.println("<description>" + ssi.getDescription() + "</description>");
			}
			out.println("</StudentStatusList>");
			out.println("</LoadExistStudent>");
			//System.out.println("</Teachers>");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void processListStudents(HttpServletResponse response){
		try{
			PrintWriter out = response.getWriter();
			System.out.println("getStudents");
			Vector<Student> students = Utility.getStudents();
			Vector<Classes> classes = Utility.getClasses();
			Vector<StudentStatus> ss = Utility.getStudentStatus();
			
			//Vector<Student> students = getStudents(2);
			
			out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			//System.out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			out.println("<ClassStudents>");
			out.println("<Classes>");
			for(int i = 0; i < classes.size(); i++){
				Classes cl = classes.get(i);
				out.println("<class>");
				out.println("<classID>" + cl.getID() + "</classID><className>" + cl.getName() + "</className>");
				System.out.println("<classID>" + cl.getID() + "</classID><className>" + cl.getName() + "</className>");
				out.println("</class>");
			}
			out.println("</Classes>");
			
			out.println("<StudentStatusList>");
			for(int i = 0; i < ss.size(); i++){
				StudentStatus ssi = ss.get(i);
				out.println("<StudentStatus>");
				out.println("<id>" + ssi.getID() + "</id>");
				out.println("<description>" + ssi.getDescription() + "</description>");
				out.println("</StudentStatus>");
				System.out.println("<id>" + ssi.getID() + "</id>");
				System.out.println("<description>" + ssi.getDescription() + "</description>");
			}
			out.println("</StudentStatusList>");
			
			out.println("<Students>");
			//System.out.println("<Teachers>");
			for(int i = 0; i < students.size(); i++){
				Student t = students.get(i);
				out.println("<student>" + "<id>" + t.getID() + "</id><studentID>"+ t.getStudentId()+"</studentID><name>" + t.getName() + "</name><promotion>" + t.getPromotion() + "</promotion><classID>" + t.getClassID() + "</classID><class_student>"+t.getClass_Student()+"</class_student><email>"+t.getEmail()+"</email><phone>"+t.getPhone()+"</phone><subject>"+t.getSubject()+"</subject><startDate>"+t.getStartDate()+"</startDate><endDate>"+t.getEndDate()+"</endDate><type>"+t.getType()+"</type><status>"+t.getStatus()+"</status></student>");
				//System.out.println("<teacher>" + "<id>" + t.getID() + "</id><name>" + t.getName() + "</name></teacher>");
			}
			out.println("</Students>");
			out.println("</ClassStudents>");
			//System.out.println("</Teachers>");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void processListFilteredStudents(Document doc, HttpServletResponse response){
		NodeList nl = doc.getElementsByTagName("filteredByClass");
		Node nod = nl.item(0);
		Element e = (Element)nod;
		int classID = Integer.valueOf(e.getChildNodes().item(0).getNodeValue());
		System.out.println("processListFilteredStudents, filteredByClass = " + classID);
		
		nl = doc.getElementsByTagName("filteredByStatus");
		nod = nl.item(0);
		e = (Element)nod;
		int statusID = Integer.valueOf(e.getChildNodes().item(0).getNodeValue());
		System.out.println("processListFilteredStudents, filteredByStatus = " + statusID);		
		
		try{
			PrintWriter out = response.getWriter();
			System.out.println("getStudents");
			Vector<Student> students = Utility.getStudentsOfClass(classID,statusID);
			Vector<Classes> classes = Utility.getClasses();
			Vector<StudentStatus> ss = Utility.getStudentStatus();
			
			//Vector<Student> students = getStudents(2);
			
			out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			//System.out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			out.println("<ClassStudents>");
			out.println("<Classes>");
			for(int i = 0; i < classes.size(); i++){
				Classes cl = classes.get(i);
				out.println("<class>");
				out.println("<classID>" + cl.getID() + "</classID><className>" + cl.getName() + "</className>");
				System.out.println("<classID>" + cl.getID() + "</classID><className>" + cl.getName() + "</className>");
				out.println("</class>");
			}
			out.println("</Classes>");
			
			out.println("<StudentStatusList>");
			for(int i = 0; i < ss.size(); i++){
				StudentStatus ssi = ss.get(i);
				out.println("<StudentStatus>");
				out.println("<id>" + ssi.getID() + "</id>");
				out.println("<description>" + ssi.getDescription() + "</description>");
				out.println("</StudentStatus>");
				System.out.println("<id>" + ssi.getID() + "</id>");
				System.out.println("<description>" + ssi.getDescription() + "</description>");
			}
			out.println("</StudentStatusList>");
			
			out.println("<Students>");
			//System.out.println("<Teachers>");
			for(int i = 0; i < students.size(); i++){
				Student t = students.get(i);
				out.println("<student>" + "<id>" + t.getID() + "</id><studentID>"+ t.getStudentId()+"</studentID><name>" + t.getName() + "</name><promotion>" + t.getPromotion() + "</promotion><classID>" + t.getClassID() + "</classID><class_student>"+t.getClass_Student()+"</class_student><email>"+t.getEmail()+"</email><phone>"+t.getPhone()+"</phone><subject>"+t.getSubject()+"</subject><startDate>"+t.getStartDate()+"</startDate><endDate>"+t.getEndDate()+"</endDate><type>"+t.getType()+"</type><status>"+t.getStatus()+"</status></student>");
				System.out.println("<student>" + "<id>" + t.getID() + "</id><studentID>"+ t.getStudentId()+"</studentID><name>" + t.getName() + "</name><promotion>" + t.getPromotion() + "</promotion><classID>" + t.getClassID() + "</classID><class_student>"+t.getClass_Student()+"</class_student><email>"+t.getEmail()+"</email><phone>"+t.getPhone()+"</phone><subject>"+t.getSubject()+"</subject><startDate>"+t.getStartDate()+"</startDate><endDate>"+t.getEndDate()+"</endDate><type>"+t.getType()+"</type><status>"+t.getStatus()+"</status></student>");
			}
			out.println("</Students>");
			
			out.println("<filteredByClass>" + classID + "</filteredByClass>");
			out.println("<filteredByStatus>" + statusID + "</filteredByStatus>");
			
			out.println("</ClassStudents>");
			//System.out.println("</Teachers>");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
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
				out.println("<teacher>" + "<id>" + t.getID() + "</id><name>" + t.getName() + "</name><institute>" + t.getInstitute()+"</institute><department>" + t.getDepartment() +"</department></teacher>");
				//System.out.println("<teacher>" + "<id>" + t.getID() + "</id><name>" + t.getName() + "</name></teacher>");
			}
			out.println("</Teachers>");
			out.println("</TeachersStudents>");
			//System.out.println("</Teachers>");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void processListJury(HttpServletResponse response){
		try{
			PrintWriter out = response.getWriter();
			System.out.println("getJuries");
			Vector<Jury> juries = Utility.getJuries();
			System.out.println("number of juries = " + juries.size());
			
			out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			//System.out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			out.println("<Juries>");
			for(int i = 0; i < juries.size(); i++){
				Jury jr = juries.get(i);
				out.println("<Jury>");
				out.println("<StudentID>" + jr.getStudentID() + "</StudentID>");
				out.println("<StudentName>" + jr.getStudentName() + "</StudentName>");
				out.println("<ThesisTitle>" + jr.getThesisTitle() + "</ThesisTitle>");
				out.println("<Examiner1ID>" + jr.getExaminer1ID() + "</Examiner1ID>");
				out.println("<Examiner1Name>" + jr.getExaminer1Name() + "</Examiner1Name>");
				out.println("<Examiner2ID>" + jr.getExaminer2ID() + "</Examiner2ID>");
				out.println("<Examiner2Name>" + jr.getExaminer2Name() + "</Examiner2Name>");
				out.println("<PresidentID>" + jr.getPresidentID() + "</PresidentID>");
				out.println("<PresidentName>" + jr.getPresidentName() + "</PresidentName>");
				out.println("<SecretaryID>" + jr.getSecretaryID() + "</SecretaryID>");
				out.println("<SecretaryName>" + jr.getSecretaryName() + "</SecretaryName>");
				out.println("<MemberID>" + jr.getMemberID() + "</MemberID>");
				out.println("<MemberName>" + jr.getMemberName() + "</MemberName>");
				out.println("</Jury>");
			}
			out.println("</Juries>");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public void processListRooms(HttpServletResponse response){
		try{
			PrintWriter out = response.getWriter();
			System.out.println("getRooms");
			Vector<Room> rooms = Utility.getRooms();
			out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			//System.out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			out.println("<Rooms>");
			
			for(int i = 0; i < rooms.size(); i++){
				Room t = rooms.get(i);
				out.println("<room>" + "<id>" + t.getID() + "</id><description>" + t.getDescription() + "</description></room>");
				//System.out.println("<teacher>" + "<id>" + t.getID() + "</id><name>" + t.getName() + "</name></teacher>");
			}
			out.println("</Rooms>");
			//System.out.println("</Teachers>");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public void processListSlots(HttpServletResponse response){
		try{
			PrintWriter out = response.getWriter();
			System.out.println("getSlots");
			Vector<Slot> slots = Utility.getSlots();
			out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			//System.out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			out.println("<Slots>");
			
			for(int i = 0; i < slots.size(); i++){
				Slot t = slots.get(i);
				out.println("<slot>" + "<id>" + t.getID() + "</id><description>" + t.getDescription() + "</description></slot>");
				//System.out.println("<teacher>" + "<id>" + t.getID() + "</id><name>" + t.getName() + "</name></teacher>");
			}
			out.println("</Slots>");
			//System.out.println("</Teachers>");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void processListDefenseSessions(HttpServletResponse response){
		try{
			PrintWriter out = response.getWriter();
			System.out.println("getDefenseSessions");
			Vector<DefenseSession> defenseSessions = Utility.getDefenseSessions();
			out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			//System.out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			out.println("<DefenseSessions>");
			
			for(int i = 0; i < defenseSessions.size(); i++){
				DefenseSession t = defenseSessions.get(i);
				out.println("<defenseSession>" + "<id>" + t.getID() + "</id><description>" + t.getDescription() + "</description><active>"+t.getActive()+"</active></defenseSession>");
				//System.out.println("<teacher>" + "<id>" + t.getID() + "</id><name>" + t.getName() + "</name></teacher>");
			}
			out.println("</DefenseSessions>");
			//System.out.println("</Teachers>");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public void processListDepartment(HttpServletResponse response){
		String webRootPath = getClass().getClassLoader().getResource(".").getPath();
		
		System.out.println("TeacherManager::processListDepartments, path = " + webRootPath);
		try{
			PrintWriter out = response.getWriter();
			System.out.println("getDepartment");
			Vector<Department> departments = Utility.getDepartments();
			Vector<SubjectCategory> categories = Utility.getSubjectCategories();
			
			out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			//System.out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			
			out.println("<Departments-Subject-Categories>");
			out.println("<Departments>");
			//System.out.println("<Teachers>");
			for(int i = 0; i < departments.size(); i++){
				Department t = departments.get(i);
				out.println("<department>" + "<id>" + t.getID() + "</id><name>" + t.getName() + "</name></department>");
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
				//System.out.println("<name>" + cat.getName() + "</name>");
			}
			out.println("</subject-categories>");
			out.println("</Departments-Subject-Categories>");
			//System.out.println("</Teachers>");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void processAddTeacher(Document doc) {
		String name = "noname";
		String institute ="-";
		int department=0;
		String degree = "-";
		NodeList nl = doc.getElementsByTagName("name");
		Node nod = nl.item(0);
		Element el = (Element)nod;
		
		name = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
		
		String instituteName = doc.getElementsByTagName("institute-name").item(0).getChildNodes().item(0).getNodeValue();
		/*
		try {
			name = URLDecoder.decode(name, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		*/
//		//name = name.getBytes("ISO-8859-1", "UTF-8");
//		try {
//			name = new String(name.getBytes("UTF-8"));
//		} catch (UnsupportedEncodingException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		
		institute = doc.getElementsByTagName("institute").item(0).getChildNodes().item(0).getNodeValue();
		department = Integer.parseInt(doc.getElementsByTagName("department").item(0).getChildNodes().item(0).getNodeValue());
		degree = doc.getElementsByTagName("degree").item(0).getChildNodes().item(0).getNodeValue();
		int expert = Integer.parseInt(doc.getElementsByTagName("expert-level").item(0).getChildNodes().item(0).getNodeValue());
		
		System.out.println("processAddTeacher, name = " + name);
		if(name == null && institute =="" && department < 1){
			System.out.println("Name is null");
			return;
		}
		
		try{
			if(cnn == null){
				Class.forName("com.mysql.jdbc.Driver");
				cnn = DriverManager.getConnection(Configure.cnnStr);
				//stat = cnn.createStatement();
				
				preparedStat = cnn.prepareStatement("insert into " + Configure.tblProfessors + "(Name,Institute, Department, InstituteName, Degree, ExpertLevel) values(?,?,?,?,?,?)");
				preparedStat.setString(1, name);
				preparedStat.setString(2, institute);
				preparedStat.setInt(3, department);
				preparedStat.setString(4, instituteName);
				preparedStat.setString(5, degree);
				preparedStat.setInt(6, expert);
				preparedStat.executeUpdate();
				System.out.println("insert successfully");
				//stat.executeQuery(sql);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close();
		}
		
		Vector<Teacher> nT = Utility.getTeachers(name, institute, instituteName, department);
		int professor_id = nT.get(0).getID();
		
		nl = doc.getElementsByTagName("professor-subject-category-match");
		for(int i = 0; i < nl.getLength(); i++){
			nod = nl.item(i);
			el = (Element)nod;
			//int professor_id = Integer.valueOf(el.getElementsByTagName("professor-id").item(0).getChildNodes().item(0).getNodeValue());
			int subject_category_id = Integer.valueOf(el.getElementsByTagName("subject-category-id").item(0).getChildNodes().item(0).getNodeValue());
			int matchScore = Integer.valueOf(el.getElementsByTagName("match-score").item(0).getChildNodes().item(0).getNodeValue());
			System.out.println("AddTeacher, id = " + professor_id + ", category = " + subject_category_id + ", match = " + matchScore);
			int tid = Utility.getIDProfessorSubjectCategory(professor_id, subject_category_id);
			if(tid == -1)
				Utility.addProfessorSubjectCategory(professor_id, subject_category_id, matchScore);
			else
				Utility.updateProfessorSubjectCategory(tid, professor_id, subject_category_id, matchScore);
		}

	}
	private void processAddStudent(Document doc){
		String name = "noname";
		NodeList nl = doc.getElementsByTagName("name");
		Node nod = nl.item(0);
		Element el = (Element)nod;		
		name = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
		
		try {
			name = URLDecoder.decode(name, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		nl = doc.getElementsByTagName("promotion");
		nod = nl.item(0);
		el = (Element)nod;		
		String promotion = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
		
		nl = doc.getElementsByTagName("class_student");
		nod = nl.item(0);
		el = (Element)nod;		
		String className = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
		
		nl = doc.getElementsByTagName("studentID");
		nod = nl.item(0);
		el = (Element)nod;		
		String studentID = el.getChildNodes().item(0).getNodeValue();
		
		
		nl = doc.getElementsByTagName("email");
		nod = nl.item(0);
		el = (Element)nod;		
		String email = el.getChildNodes().item(0).getNodeValue();
		
		nl = doc.getElementsByTagName("phone");
		nod = nl.item(0);
		el = (Element)nod;		
		String phone = el.getChildNodes().item(0).getNodeValue();
		
		/*
		nl = doc.getElementsByTagName("subject");
		nod = nl.item(0);
		el = (Element)nod;		
		String subject = el.getChildNodes().item(0).getNodeValue();
		
		try {
			subject = URLDecoder.decode(subject, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		*/
		
//		nl = doc.getElementsByTagName("startDate");
//		nod = nl.item(0);
//		el = (Element)nod;		
//		String startDate = el.getChildNodes().item(0).getNodeValue();
//		
//		nl = doc.getElementsByTagName("endDate");
//		nod = nl.item(0);
//		el = (Element)nod;		
//		String endDate = el.getChildNodes().item(0).getNodeValue();
//		nl = doc.getElementsByTagName("type");
//		nod = nl.item(0);
//		el = (Element)nod;		
//		String type = el.getChildNodes().item(0).getNodeValue();
//		
//		nl = doc.getElementsByTagName("status");
//		nod = nl.item(0);
//		el = (Element)nod;		
//		String status = el.getChildNodes().item(0).getNodeValue();

		System.out.println("processAddStudent, name = " + name + " promotion = " + promotion + " class = " + className);
		if(name == null){
			System.out.println("Name is null");
			return;
		}
		
		try{
			if(cnn == null){
				Class.forName("com.mysql.jdbc.Driver");
				cnn = DriverManager.getConnection(Configure.cnnStr);
				//stat = cnn.createStatement();
				String sql = "insert into " + Configure.tblStudents + "(StudentName,Promotion,Class,Email,Phone,StudentID,Subject) values(?,?,?,?,?,?,?)";
				preparedStat = cnn.prepareStatement("insert into " + Configure.tblStudents + "(StudentName,Promotion,Class,Email,Phone,StudentID,Status) values(?,?,?,?,?,?,?)");
				preparedStat.setString(1, name);
				preparedStat.setString(2, promotion);
				preparedStat.setString(3, className);
				preparedStat.setString(4, email);
				preparedStat.setString(5, phone);
				preparedStat.setString(6, studentID);
				preparedStat.setString(7, "1");
				
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
	
	private void processAddRoom(Document doc) {
		String description = "noname";
		
		NodeList nl = doc.getElementsByTagName("description");
		Node nod = nl.item(0);
		Element el = (Element)nod;
		
		description = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
		try {
			description = URLDecoder.decode(description, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

			
			
		System.out.println("processAddRoom, name = " + description);
		if(description == null ){
			System.out.println("Name is null");
			return;
		}
		
		try{
			if(cnn == null){
				Class.forName("com.mysql.jdbc.Driver");
				cnn = DriverManager.getConnection(Configure.cnnStr);
				//stat = cnn.createStatement();
				
				preparedStat = cnn.prepareStatement("insert into " + Configure.tblRoom + "(Description) values(?)");
				preparedStat.setString(1, description);
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
	
	private void processAddSlot(Document doc) {
		String description = "noname";
		
		NodeList nl = doc.getElementsByTagName("description");
		Node nod = nl.item(0);
		Element el = (Element)nod;
		
		description = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
		try {
			description = URLDecoder.decode(description, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

			
			
		System.out.println("processAddSlot, name = " + description);
		if(description == null ){
			System.out.println("Name is null");
			return;
		}
		
		try{
			if(cnn == null){
				Class.forName("com.mysql.jdbc.Driver");
				cnn = DriverManager.getConnection(Configure.cnnStr);
				//stat = cnn.createStatement();
				
				preparedStat = cnn.prepareStatement("insert into " + Configure.tblSlot + "(Description) values(?)");
				preparedStat.setString(1, description);
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
	
	
	private void processAddDefenseSession(Document doc) {
		String description = "noname";
		int active =-1;
		NodeList nl = doc.getElementsByTagName("description");
		Node nod = nl.item(0);
		Element el = (Element)nod;
		
		description = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
		try {
			description = URLDecoder.decode(description, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

			
		active = Integer.parseInt(doc.getElementsByTagName("active").item(0).getChildNodes().item(0).getNodeValue());	
		System.out.println("processAddDefenseSession, name = " + description);
		if(description == null && active >0 ){
			System.out.println("Name is null");
			return;
		}
		
		try{
			if(cnn == null){
				Class.forName("com.mysql.jdbc.Driver");
				cnn = DriverManager.getConnection(Configure.cnnStr);
				//stat = cnn.createStatement();
				
				preparedStat = cnn.prepareStatement("insert into " + Configure.tblDefenseSession + "(Description, Active) values(?,?)");
				preparedStat.setString(1, description);
				preparedStat.setInt(2, active);
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
	

	private void processAddJury(Document doc){
		
		NodeList nl = doc.getElementsByTagName("student");
		Node nod = nl.item(0);
		Element el = (Element)nod;		
		int studentID = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); //nod.getNodeValue();
		
		nl = doc.getElementsByTagName("thesis_title");
		nod = nl.item(0);
		el = (Element)nod;		
		String thesis_title = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
		
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
		
		try{
			if(cnn == null){
				Class.forName("com.mysql.jdbc.Driver");
				cnn = DriverManager.getConnection(Configure.cnnStr);
				preparedStat = cnn.prepareStatement("insert into " + Configure.tblStudentDefense + "(StudentID,Title,Examiner1,Examiner2,President," +
						"Secretary,AdditionalMember) " + " values(?,?,?,?,?,?,?)");
				preparedStat.setInt(1, studentID);
				preparedStat.setString(2, thesis_title);
				preparedStat.setInt(3, examiner1ID);
				preparedStat.setInt(4, examiner2ID);
				preparedStat.setInt(5, presidentID);
				preparedStat.setInt(6, secretaryID);
				preparedStat.setInt(7, memberID);				
				preparedStat.executeUpdate();
				System.out.println("insert into table " + Configure.tblStudentDefense + " successfully");
				//stat.executeQuery(sql);
				close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close();
		}
		
	}

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
				cnn = DriverManager.getConnection(Configure.cnnStr);
				preparedStat = cnn.prepareStatement("delete from " + Configure.tblProfessors + " where ID = ?");
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
	private void processDeleteStudent(Document doc, HttpServletResponse response){
		String id = "noname";
		NodeList nl = doc.getElementsByTagName("id");
		Node nod = nl.item(0);
		Element el = (Element)nod;
		
		id = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
		System.out.println("processaDeleteStudentr, id = " + id);
		if(id == null){
			System.out.println("Name is null");
			return;
		}
		
		try{
			if(cnn == null){
				Class.forName("com.mysql.jdbc.Driver");
				cnn = DriverManager.getConnection(Configure.cnnStr);
				preparedStat = cnn.prepareStatement("delete from " + Configure.tblStudents + " where ID = ?");
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
	
	private void processDeleteRoom(Document doc, HttpServletResponse response){
		String id = "noname";
		NodeList nl = doc.getElementsByTagName("id");
		Node nod = nl.item(0);
		Element el = (Element)nod;
		
		id = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
		System.out.println("processaDeleteRoom, id = " + id);
		if(id == null){
			System.out.println("Name is null");
			return;
		}
		
		try{
			if(cnn == null){
				Class.forName("com.mysql.jdbc.Driver");
				cnn = DriverManager.getConnection(Configure.cnnStr);
				preparedStat = cnn.prepareStatement("delete from " + Configure.tblRoom + " where ID = ?");
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
	
	private void processDeleteAllRooms(Document doc, HttpServletResponse response){
		Utility.deleteAllRooms();
	}

	private void processDeleteSlot(Document doc, HttpServletResponse response){
		String id = "noname";
		NodeList nl = doc.getElementsByTagName("id");
		Node nod = nl.item(0);
		Element el = (Element)nod;
		
		id = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
		System.out.println("processaDeleteSlot, id = " + id);
		if(id == null){
			System.out.println("Name is null");
			return;
		}
		
		try{
			if(cnn == null){
				Class.forName("com.mysql.jdbc.Driver");
				cnn = DriverManager.getConnection(Configure.cnnStr);
				preparedStat = cnn.prepareStatement("delete from " + Configure.tblSlot + " where ID = ?");
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
	private void processDeleteAllSlots(Document doc, HttpServletResponse response){
		Utility.deleteAllSlots();
	}

	private void processDeleteDefenseSession(Document doc, HttpServletResponse response){
		String id = "noname";
		NodeList nl = doc.getElementsByTagName("id");
		Node nod = nl.item(0);
		Element el = (Element)nod;
		
		id = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
		System.out.println("processaDeleteDefenseSession, id = " + id);
		if(id == null){
			System.out.println("Name is null");
			return;
		}
		
		try{
			if(cnn == null){
				Class.forName("com.mysql.jdbc.Driver");
				cnn = DriverManager.getConnection(Configure.cnnStr);
				preparedStat = cnn.prepareStatement("delete from " + Configure.tblDefenseSession + " where ID = ?");
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
		int id = -1;
		String institute ="";
		String depart_Str ="";
		int department=0;
		int expertLevel = 1;
		NodeList nl = doc.getElementsByTagName("name");
		Node nod = nl.item(0);
		Element el = (Element)nod;		
		
		name = el.getChildNodes().item(0).getNodeValue();
		/*
		try {
			name = URLDecoder.decode(name, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		*/
		
		institute = doc.getElementsByTagName("institute").item(0).getChildNodes().item(0).getNodeValue();
		String instituteName = doc.getElementsByTagName("institute-name").item(0).getChildNodes().item(0).getNodeValue();
		//department = Integer.parseInt(doc.getElementsByTagName("department").item(0).getChildNodes().item(0).getNodeValue());
		depart_Str = doc.getElementsByTagName("department").item(0).getChildNodes().item(0).getNodeValue();
		expertLevel = Integer.valueOf(doc.getElementsByTagName("expert-level").item(0).getChildNodes().item(0).getNodeValue());
		String degree = doc.getElementsByTagName("degree").item(0).getChildNodes().item(0).getNodeValue();
		
		department = Utility.getDepartmentID(depart_Str);
		
		nl = doc.getElementsByTagName("id");
		nod = nl.item(0);
		el = (Element)nod;
		id = Integer.parseInt(el.getChildNodes().item(0).getNodeValue());
		System.out.println("processDeleteTeacher, name = " + name + " id = " + id);
		if(name == null){
			System.out.println("Name is null");
			return;
		}
		
		nl = doc.getElementsByTagName("professor-subject-category-match");
		for(int i = 0; i < nl.getLength(); i++){
			nod = nl.item(i);
			el = (Element)nod;
			int professor_id = Integer.valueOf(el.getElementsByTagName("professor-id").item(0).getChildNodes().item(0).getNodeValue());
			int subject_category_id = Integer.valueOf(el.getElementsByTagName("subject-category-id").item(0).getChildNodes().item(0).getNodeValue());
			int matchScore = Integer.valueOf(el.getElementsByTagName("match-score").item(0).getChildNodes().item(0).getNodeValue());
			System.out.println("UpdateTeacher, id = " + professor_id + ", category = " + subject_category_id + ", match = " + matchScore);
			int tid = Utility.getIDProfessorSubjectCategory(professor_id, subject_category_id);
			if(tid == -1)
				Utility.addProfessorSubjectCategory(professor_id, subject_category_id, matchScore);
			else
				Utility.updateProfessorSubjectCategory(tid, professor_id, subject_category_id, matchScore);
		}

		try{
			if(cnn == null){
				Class.forName("com.mysql.jdbc.Driver");
				cnn = DriverManager.getConnection(Configure.cnnStr);
				preparedStat = cnn.prepareStatement("update " + Configure.tblProfessors + " set Name = ?"+",Institute = ?" + ", InstituteName = ?" + " ,Degree = ?, Department = ?" + ", ExpertLevel = ? " + " where ID = ?");
				preparedStat.setString(1, name);
				preparedStat.setString(2, institute);
				preparedStat.setString(3, instituteName);
				preparedStat.setString(4, degree);
				preparedStat.setInt(5, department);
				preparedStat.setInt(6, expertLevel);
				preparedStat.setInt(7, id);
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
	
	
	private void processUpdateStudent(Document doc, HttpServletResponse response){
		String name = "noname";
		int id = -1;
		NodeList nl = doc.getElementsByTagName("name");
		Node nod = nl.item(0);
		Element el = (Element)nod;		
		name = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
		
		try {
			name = URLDecoder.decode(name, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		nl = doc.getElementsByTagName("promotion");
		nod = nl.item(0);
		el = (Element)nod;		
		String promotion = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
		
		nl = doc.getElementsByTagName("class_student");
		nod = nl.item(0);
		el = (Element)nod;		
		String className = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
		
		nl = doc.getElementsByTagName("studentID");
		nod = nl.item(0);
		el = (Element)nod;		
		String studentID = el.getChildNodes().item(0).getNodeValue();
		
		
		nl = doc.getElementsByTagName("email");
		nod = nl.item(0);
		el = (Element)nod;		
		String email = el.getChildNodes().item(0).getNodeValue();
		
		nl = doc.getElementsByTagName("phone");
		nod = nl.item(0);
		el = (Element)nod;		
		String phone = el.getChildNodes().item(0).getNodeValue();

		nl = doc.getElementsByTagName("status");
		nod = nl.item(0);
		el = (Element)nod;		
		String status = el.getChildNodes().item(0).getNodeValue();		
		/*
		nl = doc.getElementsByTagName("subject");
		nod = nl.item(0);
		el = (Element)nod;		
		String subject = el.getChildNodes().item(0).getNodeValue();
		
		try {
			subject = URLDecoder.decode(subject, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		*/
		
		nl = doc.getElementsByTagName("id");
		nod = nl.item(0);
		el = (Element)nod;
		id = Integer.parseInt(el.getChildNodes().item(0).getNodeValue());
		System.out.println("processUpdateStudent, name = " + name + " id = " + id);
		if(name == null){
			System.out.println("Name is null");
			return;
		}
		
		try{
			if(cnn == null){
				Class.forName("com.mysql.jdbc.Driver");
				cnn = DriverManager.getConnection(Configure.cnnStr);
				preparedStat = cnn.prepareStatement("update " + Configure.tblStudents + " set StudentName = ?"+",Promotion = ?" +",Class = ?" + ",StudentID = ?" +",Email = ?" +",Phone = ?" +",	Status = ?" + " where ID = ?");
				preparedStat.setString(1, name);
				preparedStat.setString(2, promotion);
				preparedStat.setString(3, className);
				preparedStat.setString(4, studentID);
				preparedStat.setString(5, email);
				preparedStat.setString(6, phone);
				preparedStat.setString(7, status);
				//preparedStat.setString(7, subject);
				preparedStat.setInt(8, id);
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

	private void processUpdateRoom(Document doc, HttpServletResponse response){
		String description = "noname";
		int id = -1;
		
		
		NodeList nl = doc.getElementsByTagName("description");
		Node nod = nl.item(0);
		Element el = (Element)nod;		
		
		description = el.getChildNodes().item(0).getNodeValue();
		try {
			description = URLDecoder.decode(description, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		nl = doc.getElementsByTagName("id");
		nod = nl.item(0);
		el = (Element)nod;
		id = Integer.parseInt(el.getChildNodes().item(0).getNodeValue());
		
		if(description == null){
			System.out.println("Name is null");
			return;
		}
		
		try{
			if(cnn == null){
				Class.forName("com.mysql.jdbc.Driver");
				cnn = DriverManager.getConnection(Configure.cnnStr);
				preparedStat = cnn.prepareStatement("update " + Configure.tblRoom + " set Description = ?"+ " where ID = ?");
				preparedStat.setString(1, description);
				preparedStat.setInt(2, id);
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
	private void processUpdateSlot(Document doc, HttpServletResponse response){
		String description = "noname";
		int id = -1;
		
		
		NodeList nl = doc.getElementsByTagName("description");
		Node nod = nl.item(0);
		Element el = (Element)nod;		
		
		description = el.getChildNodes().item(0).getNodeValue();
		try {
			description = URLDecoder.decode(description, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		nl = doc.getElementsByTagName("id");
		nod = nl.item(0);
		el = (Element)nod;
		id = Integer.parseInt(el.getChildNodes().item(0).getNodeValue());
		
		if(description == null){
			System.out.println("Name is null");
			return;
		}
		
		try{
			if(cnn == null){
				Class.forName("com.mysql.jdbc.Driver");
				cnn = DriverManager.getConnection(Configure.cnnStr);
				preparedStat = cnn.prepareStatement("update " + Configure.tblSlot + " set Description = ?"+ " where ID = ?");
				preparedStat.setString(1, description);
				preparedStat.setInt(2, id);
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
	
	private void processUpdateDefenseSession(Document doc, HttpServletResponse response){
		String description = "noname";
		int id = -1;
		int active=-1;
		
		NodeList nl = doc.getElementsByTagName("description");
		Node nod = nl.item(0);
		Element el = (Element)nod;		
		
		description = el.getChildNodes().item(0).getNodeValue();
		try {
			description = URLDecoder.decode(description, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		nl = doc.getElementsByTagName("id");
		nod = nl.item(0);
		el = (Element)nod;
		id = Integer.parseInt(el.getChildNodes().item(0).getNodeValue());
		
		nl = doc.getElementsByTagName("active");
		nod = nl.item(0);
		el = (Element)nod;
		active = Integer.parseInt(el.getChildNodes().item(0).getNodeValue());
		
		if(description == null && active == -1){
			System.out.println("Name is null");
			return;
		}
		
		try{
			if(cnn == null){
				Class.forName("com.mysql.jdbc.Driver");
				cnn = DriverManager.getConnection(Configure.cnnStr);
				preparedStat = cnn.prepareStatement("update " + Configure.tblDefenseSession + " set Description = ?"+ ", Active = ?"+ " where ID = ?");
				preparedStat.setString(1, description);
				preparedStat.setInt(2, active);
				preparedStat.setInt(3, id);
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
	
	
	/*
	public int getDepartmentID(String department){
		int depart = 0;
		//System.out.println("Start JDBC");
		try{
			
			//System.out.println("Start JDBC");
			Class.forName("com.mysql.jdbc.Driver");
			//System.out.println("JDBC -->OK");
			cnn = DriverManager.getConnection(Configure.cnnStr);
			//stat = cnn.createStatement();
			preparedStat = cnn.prepareStatement("select ID from departments Where Name = ?");
			preparedStat.setString(1, department);
			rs = preparedStat.executeQuery();
			if (rs.next())
			{
				depart = rs.getInt("ID");
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close();
		}
		return depart;
	}
	*/
	/*
	private void processCheckListJury(Document doc, HttpServletResponse response){
		System.out.println("processCheckListJury");
		
		Vector<Teacher> teachers = getTeachers();
		HashMap<String, Integer> name2ID = new HashMap<String, Integer>();
		for(int i = 0; i < teachers.size(); i++){
			Teacher t = teachers.get(i);
			name2ID.put(t.getName(), t.getID());
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
			
			nl = juryElement.getElementsByTagName("examiner1");
			nod = nl.item(0);
			el = (Element)nod;		
			String examiner1 = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
	
			nl = juryElement.getElementsByTagName("examiner2");
			nod = nl.item(0);
			el = (Element)nod;		
			String examiner2 = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			
			nl = juryElement.getElementsByTagName("president");
			nod = nl.item(0);
			el = (Element)nod;		
			String president = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			
			nl = juryElement.getElementsByTagName("secretary");
			nod = nl.item(0);
			el = (Element)nod;		
			String secretary = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			
			nl = juryElement.getElementsByTagName("member");
			nod = nl.item(0);
			el = (Element)nod;		
			String member = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			
			nl = juryElement.getElementsByTagName("slot");
			nod = nl.item(0);
			el = (Element)nod;		
			int slot = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); //nod.getNodeValue();
			
			nl = juryElement.getElementsByTagName("room");
			nod = nl.item(0);
			el = (Element)nod;		
			int room = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); //nod.getNodeValue();

			JuryInfo jury = new JuryInfo(i,studentName,thesis_title,"null",examiner1,examiner2,president,secretary,member,0);
			
			jury.setStudentID(studentID);
			jury.setExaminerId1(name2ID.get(jury.getExaminerName1()));
			jury.setExaminerId2(name2ID.get(jury.getExaminerName2()));
			jury.setPresidentId(name2ID.get(jury.getPresidentName()));
			jury.setSecretaryId(name2ID.get(jury.getSecretaryName()));
			jury.setAdditionalMemberId(name2ID.get(jury.getAdditionalMemberName()));
			
			jury.setSlotId(slot);
			jury.setRoomId(room);
			
			listJury.add(jury);
			
			
			System.out.println("TeacherManager::processCheckJuryJury: " + studentID + "\t" + studentName + "\t" + 
			thesis_title + "\t" + examiner1 + "\t" + examiner2 + "\t" + president + "\t" + secretary +
			"\t" + member + "\t" + slot + "\t" + room);
		}
		
		try{
			PrintWriter out = response.getWriter();
			String xml = Utility.checkConsistency(listJury);
			out.println(xml);
			System.out.println("TeacherManager::processCheckListJury --> Return to clients xml\n" + xml);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	*/
	
	/*
	private void processSearchSolution(Document doc, HttpServletResponse response){
		System.out.println("processSearchSolution");
		
		Vector<Teacher> teachers = getTeachers();
		HashMap<String, Integer> name2ID = new HashMap<String, Integer>();
		for(int i = 0; i < teachers.size(); i++){
			Teacher t = teachers.get(i);
			name2ID.put(t.getName(), t.getID());
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
			
			nl = juryElement.getElementsByTagName("examiner1");
			nod = nl.item(0);
			el = (Element)nod;		
			String examiner1 = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
	
			nl = juryElement.getElementsByTagName("examiner2");
			nod = nl.item(0);
			el = (Element)nod;		
			String examiner2 = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			
			nl = juryElement.getElementsByTagName("president");
			nod = nl.item(0);
			el = (Element)nod;		
			String president = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			
			nl = juryElement.getElementsByTagName("secretary");
			nod = nl.item(0);
			el = (Element)nod;		
			String secretary = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			
			nl = juryElement.getElementsByTagName("member");
			nod = nl.item(0);
			el = (Element)nod;		
			String member = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
			

			JuryInfo jury = new JuryInfo(i,studentName,thesis_title,"null",examiner1,examiner2,president,secretary,member,0);
			
			jury.setStudentID(studentID);
			jury.setExaminerId1(name2ID.get(jury.getExaminerName1()));
			jury.setExaminerId2(name2ID.get(jury.getExaminerName2()));
			jury.setPresidentId(name2ID.get(jury.getPresidentName()));
			jury.setSecretaryId(name2ID.get(jury.getSecretaryName()));
			jury.setAdditionalMemberId(name2ID.get(jury.getAdditionalMemberName()));
			
			
			listJury.add(jury);
			
			
			System.out.println("TeacherManager::processCheckJuryJury: " + studentID + "\t" + studentName + "\t" + 
			thesis_title + "\t" + examiner1 + "\t" + examiner2 + "\t" + president + "\t" + secretary +
			"\t" + member);
		}
		
		NodeList nl = doc.getElementsByTagName("slots");
		Node nod = nl.item(0);
		Element e = (Element)nod;
		int nbSlots = Integer.valueOf(e.getChildNodes().item(0).getNodeValue());

		nl = doc.getElementsByTagName("rooms");
		nod = nl.item(0);
		e = (Element)nod;
		int nbRooms = Integer.valueOf(e.getChildNodes().item(0).getNodeValue());
		
		try{
			PrintWriter out = response.getWriter();
			algorithms.localsearch.NewSearch se = new algorithms.localsearch.NewSearch(listJury, teachers, nbSlots, nbRooms);
			se.localsearch(1000);
			String xml = Utility.checkConsistency(listJury);
			out.println(xml);
			System.out.println("TeacherManager::processCheckListJury --> Return to clients xml\n" + xml);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	*/
	
	private void processAddClass(Document doc) {
		String name = "noname";
		
		NodeList nl = doc.getElementsByTagName("name");
		Node nod = nl.item(0);
		Element el = (Element)nod;
		
		name = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
		try {
			name = URLDecoder.decode(name, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

			
			
		System.out.println("processAddRoom, name = " + name);
		if(name == null ){
			System.out.println("Name is null");
			return;
		}
		
		try{
			if(cnn == null){
				Class.forName("com.mysql.jdbc.Driver");
				cnn = DriverManager.getConnection(Configure.cnnStr);
				//stat = cnn.createStatement();
				
				preparedStat = cnn.prepareStatement("insert into " + Configure.tblClasses + "(Name) values(?)");
				preparedStat.setString(1, name);
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

	private void processAddSubjectCategory(Document doc, HttpServletResponse response) {
		String name = "noname";
		
		NodeList nl = doc.getElementsByTagName("name");
		Node nod = nl.item(0);
		Element el = (Element)nod;
		name = el.getChildNodes().item(0).getNodeValue();
		System.out.println("TeacherManager::processAddSubjectCategory, name = " + name);
		Utility.addSubJectCategory(name);
	}

	public void processLoadSubjectCategories(Document doc, HttpServletResponse response){
		try{
			PrintWriter out = response.getWriter();
			Vector<SubjectCategory> categories = Utility.getSubjectCategories();
			out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			//System.out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			out.println("<subject-categories>");
			
			for(int i = 0; i < categories.size(); i++){
				SubjectCategory cat = categories.get(i);
				out.println("<subject-category>" + "<id>" + cat.getID() + "</id><name>" + cat.getName() + "</name></subject-category>");
			}
			out.println("</subject-categories>");
			//System.out.println("</Teachers>");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void processLoadExistSubjectCategory(Document doc, HttpServletResponse response){
		int id = Integer.valueOf(doc.getElementsByTagName("id").item(0).getChildNodes().item(0).getNodeValue());
		System.out.println("Teacher::processLoadExistSubjectCategories, id = " + id);
		
		try{
			PrintWriter out = response.getWriter();
			Vector<SubjectCategory> categories = Utility.getSubjectCategories();
			out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			//System.out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			out.println("<subject-categories>");
			
			for(int i = 0; i < categories.size(); i++){
				SubjectCategory cat = categories.get(i);
				if(cat.getID() == id){
					out.println("<subject-category>" + "<id>" + cat.getID() + "</id><name>" + cat.getName() + "</name></subject-category>");
					break;
				}
			}
			out.println("</subject-categories>");
			//System.out.println("</Teachers>");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void processListClasses(HttpServletResponse response){
		try{
			PrintWriter out = response.getWriter();
			System.out.println("getClasses");
			Vector<Classes> classes = Utility.getClasses();
			out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			//System.out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			out.println("<StClasses>");
			
			for(int i = 0; i < classes.size(); i++){
				Classes t = classes.get(i);
				out.println("<stclass>" + "<id>" + t.getID() + "</id><name>" + t.getName() + "</name></stclass>");
				System.out.println("<stclass>" + "<id>" + t.getID() + "</id><name>" + t.getName() + "</name></stclass>");
			}
			out.println("</StClasses>");
			//System.out.println("</Teachers>");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void processListClassesStatus(HttpServletResponse response){
		try{
			PrintWriter out = response.getWriter();
			System.out.println("getClasses");
			Vector<Classes> classes = Utility.getClasses();
			Vector<StudentStatus> ss = Utility.getStudentStatus();
			
			out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			//System.out.println("<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>");
			out.println("<ClassesStatus>");
			
			for(int i = 0; i < classes.size(); i++){
				Classes t = classes.get(i);
				out.println("<class>" + "<id>" + t.getID() + "</id><name>" + t.getName() + "</name></class>");
				System.out.println("<stclass>" + "<id>" + t.getID() + "</id><name>" + t.getName() + "</name></stclass>");
			}
			
			out.println("<StudentStatusList>");
			for(int i = 0; i < ss.size(); i++){
				StudentStatus ssi = ss.get(i);
				out.println("<StudentStatus>");
				out.println("<id>" + ssi.getID() + "</id>");
				out.println("<description>" + ssi.getDescription() + "</description>");
				out.println("</StudentStatus>");
				//System.out.println("<id>" + ssi.getID() + "</id>");
				//System.out.println("<description>" + ssi.getDescription() + "</description>");
			}
			out.println("</StudentStatusList>");
			
			out.println("</ClassesStatus>");
			//System.out.println("</Teachers>");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void processUpdateClass(Document doc, HttpServletResponse response){
		String name = "noname";
		int id = -1;
		
		
		NodeList nl = doc.getElementsByTagName("name");
		Node nod = nl.item(0);
		Element el = (Element)nod;		
		
		name = el.getChildNodes().item(0).getNodeValue();
		try {
			name = URLDecoder.decode(name, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		nl = doc.getElementsByTagName("id");
		nod = nl.item(0);
		el = (Element)nod;
		id = Integer.parseInt(el.getChildNodes().item(0).getNodeValue());
		
		if(name == null){
			System.out.println("Name is null");
			return;
		}
		
		try{
			if(cnn == null){
				Class.forName("com.mysql.jdbc.Driver");
				cnn = DriverManager.getConnection(Configure.cnnStr);
				preparedStat = cnn.prepareStatement("update " + Configure.tblClasses + " set Name = ?"+ " where ID = ?");
				preparedStat.setString(1, name);
				preparedStat.setInt(2, id);
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
	private void processDeleteSubjectCategory(Document doc, HttpServletResponse response){
		int id = -1;
		NodeList nl = doc.getElementsByTagName("id");
		Node nod = nl.item(0);
		Element el = (Element)nod;
		
		id = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); //nod.getNodeValue();
		System.out.println("processaDeleteSubjectCategory, id = " + id);
		
		Utility.deleteSubjectCategory(id);
	}

	private void processUpdateSubjectCategory(Document doc, HttpServletResponse response){
		int id = -1;
		String name = "noname";
		NodeList nl = doc.getElementsByTagName("id");
		Node nod = nl.item(0);
		Element el = (Element)nod;
		
		id = Integer.valueOf(el.getChildNodes().item(0).getNodeValue()); //nod.getNodeValue();
		
		nl = doc.getElementsByTagName("name");
		nod = nl.item(0);
		el = (Element)nod;
		
		name = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();

		System.out.println("processaUpdateSubjectCategory, id = " + id + " name = " + name);
		
		Utility.updateSubjectCategory(id,name);
	}

	private void processDeleteClass(Document doc, HttpServletResponse response){
		String id = "noname";
		NodeList nl = doc.getElementsByTagName("id");
		Node nod = nl.item(0);
		Element el = (Element)nod;
		
		id = el.getChildNodes().item(0).getNodeValue(); //nod.getNodeValue();
		System.out.println("processaDeleteClass, id = " + id);
		if(id == null){
			System.out.println("Name is null");
			return;
		}
		
		try{
			if(cnn == null){
				Class.forName("com.mysql.jdbc.Driver");
				cnn = DriverManager.getConnection(Configure.cnnStr);
				preparedStat = cnn.prepareStatement("delete from " + Configure.tblClasses + " where ID = ?");
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
	
	public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
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
				processAddTeacher(doc);
			}else if(doc.getDocumentElement().getNodeName().compareTo("LoadTeachersList") == 0){
				System.out.println("LoadTeachersList");
				processListTeachers(response);
			}else if(doc.getDocumentElement().getNodeName().compareTo("DeleteTeacher") == 0){
				System.out.println("DeleteTeacher");
				processDeleteTeacher(doc,response);
			}else if(doc.getDocumentElement().getNodeName().compareTo("DeleteStudent") == 0){
				System.out.println("DeleteStudent");
				processDeleteStudent(doc,response);
			}else if(doc.getDocumentElement().getNodeName().compareTo("DeleteRoom") == 0){
				System.out.println("DeleteRoom");
				processDeleteRoom(doc,response);
			}else if(doc.getDocumentElement().getNodeName().compareTo("DeleteAllRooms") == 0){
				System.out.println("DeleteAllRooms");
				processDeleteAllRooms(doc,response);
			}else if(doc.getDocumentElement().getNodeName().compareTo("DeleteSlot") == 0){
				System.out.println("DeleteSlot");
				processDeleteSlot(doc,response);
			}else if(doc.getDocumentElement().getNodeName().compareTo("DeleteAllSlots") == 0){
				System.out.println("DeleteAllSlots");
				processDeleteAllSlots(doc,response);
			}else if(doc.getDocumentElement().getNodeName().compareTo("DeleteDefenseSession") == 0){
				System.out.println("DeleteDefenseSession");
				processDeleteDefenseSession(doc,response);
			}else if(doc.getDocumentElement().getNodeName().compareTo("UpdateTeacher") == 0){
				System.out.println("UpdateTeacher");
				processUpdateTeacher(doc,response);
			}else if(doc.getDocumentElement().getNodeName().compareTo("UpdateStudent") == 0){
				System.out.println("UpdateStudent");
				processUpdateStudent(doc,response);
			}else if(doc.getDocumentElement().getNodeName().compareTo("UpdateRoom") == 0){
				System.out.println("UpdateRoom");
				processUpdateRoom(doc,response);
			}else if(doc.getDocumentElement().getNodeName().compareTo("UpdateSlot") == 0){
				System.out.println("UpdateSlot");
				processUpdateSlot(doc,response);
			}else if(doc.getDocumentElement().getNodeName().compareTo("UpdateDefenseSession") == 0){
				System.out.println("UpdateDefenseSession");
				processUpdateDefenseSession(doc,response);
			}else if(doc.getDocumentElement().getNodeName().compareTo("LoadStudentsList") == 0){
				System.out.println("LoadStudentsList");
				processListStudents(response);
			}else if(doc.getDocumentElement().getNodeName().compareTo("LoadFilteredStudentsList") == 0){
				System.out.println("LoadFilteredStudents");
				processListFilteredStudents(doc,response);
			}else if(doc.getDocumentElement().getNodeName().compareTo("AddStudent") == 0){
				System.out.println("AddStudent");
				processAddStudent(doc);
				
			}else if(doc.getDocumentElement().getNodeName().compareTo("AddDefenseSession") == 0){
				System.out.println("AddDefenseSession");
				processAddDefenseSession(doc);
			}else if(doc.getDocumentElement().getNodeName().compareTo("AddRoom") == 0){
				System.out.println("AddRoom");
				processAddRoom(doc);
			}else if(doc.getDocumentElement().getNodeName().compareTo("AddSlot") == 0){
				System.out.println("AddSlot");
				processAddSlot(doc);
			}else if(doc.getDocumentElement().getNodeName().compareTo("LoadTeachersStudentsList") == 0){
				System.out.println("LoadTeachersStudentsList");
				processListTeachersStudents(response);
			}else if(doc.getDocumentElement().getNodeName().compareTo("AddJury") == 0){
				System.out.println("AddJury");
				processAddJury(doc);
			}else if(doc.getDocumentElement().getNodeName().compareTo("LoadJury") == 0){
				System.out.println("LoadJury");
				processListJury(response);
			
				
			}else if(doc.getDocumentElement().getNodeName().compareTo("LoadRoomsList") == 0){
				System.out.println("LoadRoomsList");
				processListRooms(response);	
				
			}else if(doc.getDocumentElement().getNodeName().compareTo("LoadSlotsList") == 0){
				System.out.println("LoadSlotsList");
				processListSlots(response);	
			
			}else if(doc.getDocumentElement().getNodeName().compareTo("LoadDefenseSessionsList") == 0){
				System.out.println("LoadDefenseSessionsList");
				processListDefenseSessions(response);	
				
			}else if(doc.getDocumentElement().getNodeName().compareTo("LoadExistTeacher") == 0){
				System.out.println("LoadExistTeacher");
				processExistTeacher(doc,response);	
			}else if(doc.getDocumentElement().getNodeName().compareTo("LoadDepartmentsList") == 0){
				System.out.println("LoadDepartmentList");
				processListDepartment(response);		
				
			
			}else if(doc.getDocumentElement().getNodeName().compareTo("LoadExistStudent") == 0){
				System.out.println("LoadExistStudent");
				processExistStudent(doc,response);
				
			}else if(doc.getDocumentElement().getNodeName().compareTo("CheckListJury") == 0){
				System.out.println("CheckListJury");
				//processCheckListJury(doc,response);				
			}else if(doc.getDocumentElement().getNodeName().compareTo("SearchSolution") == 0){
				System.out.println("CheckListJury");
				//processSearchSolution(doc,response);				
			}else if(doc.getDocumentElement().getNodeName().compareTo("AddSubjectCategory") == 0){
				System.out.println("AddSubjectCategory");
				processAddSubjectCategory(doc,response);				
			}else if(doc.getDocumentElement().getNodeName().compareTo("DeleteSubjectCategory") == 0){
				System.out.println("DeleteSubjectCategory");
				processDeleteSubjectCategory(doc,response);				
			}else if(doc.getDocumentElement().getNodeName().compareTo("UpdateSubjectCategory") == 0){
				System.out.println("UpdateSubjectCategory");
				processUpdateSubjectCategory(doc,response);				
			}else if(doc.getDocumentElement().getNodeName().compareTo("LoadSubjectCategoriesList") == 0){
				System.out.println("LoadSubjectCategoriesList");
				processLoadSubjectCategories(doc,response);				
			}else if(doc.getDocumentElement().getNodeName().compareTo("LoadExistSubjectCategory") == 0){
				System.out.println("LoadExistSubjectCategory");
				processLoadExistSubjectCategory(doc,response);				
			}else if(doc.getDocumentElement().getNodeName().compareTo("AddClass") == 0){
				System.out.println("AddClass");
				processAddClass(doc);
			}else if(doc.getDocumentElement().getNodeName().compareTo("LoadClassesList") == 0){
				System.out.println("LoadClassesList");
				processListClasses(response);	
			}else if(doc.getDocumentElement().getNodeName().compareTo("LoadClassesStatus") == 0){
				System.out.println("LoadClassesList");
				processListClassesStatus(response);	
			}else if(doc.getDocumentElement().getNodeName().compareTo("UpdateClass") == 0){
				System.out.println("UpdateClass");
				processUpdateClass(doc,response);
			}else if(doc.getDocumentElement().getNodeName().compareTo("DeleteClass") == 0){
				System.out.println("DeleteClass");
				processDeleteClass(doc,response);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
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
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//TeacherManager tm = new TeacherManager();
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
