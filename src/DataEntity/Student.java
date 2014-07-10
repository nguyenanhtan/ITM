package DataEntity;
import java.util.*;

public class Student {

	/**
	 * @param args
	 */
	private int id;
	private String name;
	private String studentID;
	private String promotion;
	private int classID;
	private String class_Student;
	private String email;
	private String phone;
	private String subject;
	private Date startDate;
	private Date endDate;
	private String type;
	private int status;
	
	public Student(int id, String name){
		this.id = id; this.name = name;
	}
	public Student(){
		this.id = 0;
		this.name = "";
		this.studentID="";
		this.promotion="";
		this.class_Student="";
		this.email="";
		this.phone="";
		this.subject="";
		this.startDate= new Date();
		this.endDate=new Date();
		this.type="";
		this.status = 0;
		
	}
	public Student(int id, String name, String studentID, String promotion, int classID, String class_Student, String email, String phone, String subject, Date startDate, Date endDate, String type, int status){
		this.id = id;
		this.name = name;
		this.studentID=studentID;
		this.promotion=promotion;
		this.classID = classID;
		this.class_Student=class_Student;
		this.email=email;
		this.phone=phone;
		this.subject=subject;
		this.startDate=startDate;
		this.endDate=endDate;
		this.type=type;
		this.status = status;
		
	}
	public void setID(int id){
		this.id = id;
	}
	public int getID(){
		return id;
	}
	public void setName(String name){
		this.name = name;
	}
	public String getName(){
		return name;
	}
	
	
	public String getStudentId(){ return studentID;}
	public String getPromotion(){return promotion;}
	public String getClass_Student(){return class_Student;}
	public int getClassID(){ return classID;}
	public String getEmail(){return email;}
	public String getPhone(){return phone;}
	public String getSubject(){return subject;}
	public Date getStartDate(){return startDate;}
	public Date getEndDate(){return endDate;}
	public String getType(){return type;}
	public int getStatus(){return status;}
	
	
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
