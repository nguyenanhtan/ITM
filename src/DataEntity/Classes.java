package DataEntity;

public class Classes {

	/**
	 * @param args
	 */
	private int id;
	private String name;
	
	
	public Classes(int id, String name){
		this.id = id; 
		this.name = name;
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
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}