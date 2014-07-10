package DataEntity;

public class SubjectCategory {

	/**
	 * @param args
	 */
	
	private int id;
	private String name;
	
	public SubjectCategory(int id, String name){
		this.id = id;
		this.name = name;
		
	}
	public int getID(){ return this.id;}
	public String getName(){ return this.name;}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
