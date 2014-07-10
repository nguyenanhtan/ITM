package DataEntity;

public class StudentStatus {

	/**
	 * @param args
	 */
	int id;
	String description;
	public StudentStatus(int id, String description){
		this.id = id;
		this.description = description;
	}
	public int getID(){ return id;}
	public String getDescription(){ return description;}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
