package DataEntity;

public class StudentSubjectCategoryMatch {

	/**
	 * @param args
	 */
	private int id;
	private int studentID;
	private int subjectCategoryID;
	private int matchScore;
	public StudentSubjectCategoryMatch(int id, int studentID, int subjectCategoryID, int matchScore){
		this.id = id;
		this.studentID = studentID;
		this.subjectCategoryID = subjectCategoryID;
		this.matchScore = matchScore;
	}
	public int getID(){ return this.id;}
	public int getStudentID(){ return this.studentID;}
	public int getSubjectCategoryID(){ return this.subjectCategoryID;}
	public int getMatchScore(){ return this.matchScore;}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
