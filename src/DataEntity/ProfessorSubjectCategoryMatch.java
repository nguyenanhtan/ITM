package DataEntity;

public class ProfessorSubjectCategoryMatch {

	/**
	 * @param args
	 */
	private int id;
	private int professorID;
	private int subjectCategoryID;
	private int matchScore;
	public ProfessorSubjectCategoryMatch(int id, int professorID, int subjectCategoryID, int matchScore){
		this.id = id;
		this.professorID = professorID;
		this.subjectCategoryID = subjectCategoryID;
		this.matchScore = matchScore;
	}
	public int getID(){ return this.id;}
	public int getProfessorID(){ return this.professorID;}
	public int getSubjectCategoryID(){ return this.subjectCategoryID;}
	public int getMatchScore(){ return this.matchScore;}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
