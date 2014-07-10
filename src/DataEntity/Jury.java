package DataEntity;

public class Jury {

	/**
	 * @param args
	 */
	private int studentID;
	private String studentName;
	private String thesis_title;
	private int examiner1ID;
	private String examiner1Name;
	private int examiner2ID;
	private String examiner2Name;
	private int presidentID;
	private String presidentName;
	private int secretaryID;
	private String secretaryName;
	private int memberID;
	private String memberName;
	
	public Jury(int studentID, String studentName, String thesis_title, int examiner1ID, String examiner1Name, int examiner2ID, 
			String examiner2Name, int presidentID, String presidentName, int secretaryID, String secretaryName, int memberID, String memberName){
		this.studentID = studentID;
		this.studentName = studentName;
		this.thesis_title = thesis_title;
		this.examiner1ID = examiner1ID;
		this.examiner1Name = examiner1Name;
		this.examiner2ID = examiner2ID;
		this.examiner2Name = examiner2Name;
		this.presidentID = presidentID;
		this.presidentName = presidentName;
		this.secretaryID = secretaryID;
		this.secretaryName = secretaryName;
		this.memberID = memberID;
		this.memberName = memberName;				
	}
	
	public int getStudentID(){
		return studentID;
	}
	public String getStudentName(){
		return studentName;
	}
	public String getThesisTitle(){ return thesis_title;}
	public int getExaminer1ID(){ return examiner1ID;}
	public String getExaminer1Name(){ return examiner1Name;}
	public int getExaminer2ID(){ return examiner2ID;}
	public String getExaminer2Name(){ return examiner2Name;}
	public int getPresidentID(){ return presidentID;}
	public String getPresidentName(){ return presidentName;}
	public int getSecretaryID(){ return secretaryID;}
	public String getSecretaryName(){ return secretaryName;}
	public int getMemberID(){ return memberID;}
	public String getMemberName(){ return memberName;}
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
