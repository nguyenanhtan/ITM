package DataEntity;

public class Teacher {

	/**
	 * @param args
	 */
	private int	id;
	private String name;
	private String institute;
	private String instituteName;
	private String department;
	private int expertLevel;
	private String degree;
	
	public Teacher()
	{
		id=0;
		name ="";
		institute ="";
		department="";
	}
	public Teacher(int id, String name, String institute,String department, String instituteName, String degree){
		this.id = id;
		this.name = name;
		this.institute = institute;
		this.department = department;
		this.instituteName = instituteName;
		this.degree = degree;
	}
	
	public String getDegree(){ return this.degree;}
	
	public Teacher(int id, String name){
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
	public void setInsitute(String institute){
		this.institute = institute;
	}
	public String getInstitute(){
		return institute;
	}
	public void setDepartment(String department){
		this.department = department;
	}
	public String getDepartment(){
		return department;
	}
	public String getInstituteName(){ return this.instituteName;}
	
	public int getExpertLevel(){ return this.expertLevel;}
	public void setExpertLevel(int expertLevel){ this.expertLevel = expertLevel;}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
