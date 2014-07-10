package utils;

public class Configure {

	/**
	 * @param args
	 */
	//public static String cnnStr = "jdbc:mysql://localhost:3306/itraining_management_db?user=root&password=";
	//public static String cnnStr = "jdbc:mysql://localhost:3306/itm?user=root&password=";
	public static String cnnStr = "jdbc:mysql://localhost:3306/itm_server?user=root&password=1234567890";
	//public static String cnnStr = "jdbc:mysql://localhost:3306/itm_test?user=root&password=1234567890";
	public static String path = "";
	
	//public static String cnnStr = "jdbc:mysql://202.191.59.203:3306/itraining_management_db?user=sdh&password=sdh@)!#";
	
	//public static String cnnStr = "jdbc:mysql://localhost:3306/itraining_management_db?user=sdh&password=sdh@)!#";
	//public static String path = "/home/sdh/tmp/";
	
	public static String tblProfessors = "professors";
	public static String tblStudents = "supervise_students";
	public static String tblStudentDefense = "student_defense";
	public static String tblSlot = "slots";
	public static String tblRoom = "rooms";
	public static String tblDepartment = "departments";
	public static String tblStudentStatus = "student_status";
	public static String tblDefenseSession = "defensesession";
	public static String tblClasses = "classes";
	public static String tblSubjectCategories = "subject_categories";
	public static String tblStudentDefenseSubjectCategory = "student_defense_subject_category";
	public static String tblProfessorSubjectCategory = "professor_subject_category";
	
	public static String tblTempStudentDefense = "temp_student_defense";
	
	
	public static int maxMatchSubjectScore = 10;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Configure");
		
		Configure.path = "abc";
		System.out.println(Configure.path);
		
		Configure.path = "def";
		System.out.println(Configure.path);
		
	}

}

