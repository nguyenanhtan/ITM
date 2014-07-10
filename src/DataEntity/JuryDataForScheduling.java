package DataEntity;

import java.util.Vector;

public class JuryDataForScheduling {

	/**
	 * @param args
	 */
	private Vector<JuryInfo> jury;
	private Vector<Teacher> hustTeachers;
	private Vector<Teacher> nonHustTeachers;
	
	public JuryDataForScheduling(Vector<JuryInfo> jury, Vector<Teacher> hustTeachers, Vector<Teacher> nonHustTeachers){
		this.jury = jury;
		this.hustTeachers = hustTeachers;
		this.nonHustTeachers = nonHustTeachers;
	}
	
	public Vector<JuryInfo> getJuryList(){ return this.jury;}
	public Vector<Teacher> getHustTeachers(){ return this.hustTeachers;}
	public Vector<Teacher> getNonHustTeachers(){ return this.nonHustTeachers;}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
