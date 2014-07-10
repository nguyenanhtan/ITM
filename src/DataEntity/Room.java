package DataEntity;

public class Room {

	/**
	 * @param args
	 */
	int id;
	String description;
	int sessionID;
	
	int maxNbNonHustMembers;
	int maxNbHustMembers;
	int maxNbJuries;
	
	public Room(int id, String description, int sessionID){
		this.id = id;
		this.description = description;
		this.sessionID = sessionID;
	}
	public int getID(){ return id;}
	public void setID(int id){
		this.id = id;
	}
	public void setSessionID(int sessionID){
		this.sessionID = sessionID;
	}
	public int getSessionID(){ return this.sessionID;}
	
	public String getDescription(){ return description;}
	
	public int getMaxNbJuries(){ return this.maxNbJuries;}
	public void setNbJuries(int maxNbJuries){ this.maxNbJuries = maxNbJuries;}
	public int getMaxNbHustMembers(){ return this.maxNbHustMembers;}
	public void setMaxNbHustMembers(int maxNbHustMembers){ this.maxNbHustMembers = maxNbHustMembers;}
	public int getMaxNbNonHustMembers(){ return this.maxNbNonHustMembers;}
	public void setMaxNbNonHustMembers(int maxNbNonHustMembers){ this.maxNbNonHustMembers = maxNbNonHustMembers;}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
