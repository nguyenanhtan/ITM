package DataEntity;


public class DefenseSession {

	/**
	 * @param args
	 */
	private int id;
	private String description;
	private int active;
	
	public DefenseSession(){
		this.id = -1; 
		this.description = "";
		this.active=-1;
	}
	public DefenseSession(int id, String description, int active){
		this.id = id; 
		this.description = description;
		this.active=active;
	}
	
	public void setID(int id){
		this.id = id;
	}
	public int getID(){
		return id;
	}
	public void setDescription(String description){
		this.description = description;
	}
	public String getDescription(){
		return description;
	}
	
	public void setActive(int active){
		this.active = active;
	}
	public int getActive(){
		return active;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}