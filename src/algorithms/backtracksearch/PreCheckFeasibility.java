package algorithms.backtracksearch;

import DataEntity.*;
import java.util.*;
import utils.Utility;
import java.io.*;

public class PreCheckFeasibility {

	/**
	 * @param args
	 */
	
	private String retMsg = "";
	
	public String getMsg(){
		return this.retMsg;
	}
	public void writeDataToFile(Vector<JuryInfo> jury, Vector<Room> rooms, Vector<Teacher> hust, Vector<Teacher> nonHust, String fn){
		Vector<Teacher> teachers = new Vector<Teacher>();
		String path = getClass().getClassLoader().getResource(".").getPath();
		fn = path + "/" + fn;
		
		try{
			PrintWriter out = new PrintWriter(new FileWriter(fn));
			out.println("juries");
			out.println(jury.size());
			for(int i = 0; i < jury.size(); i++){
				JuryInfo J = jury.get(i);
				out.println(J.getSupervisorId() + " " + J.getExaminerId1() + " " + J.getExaminerId2() + 
						" " + J.getPresidentId() + " " + J.getSecretaryId() + " " + J.getAdditionalMemberId() + 
						" " + J.getSlotId() + " " + J.getRoomId());
			}
			out.println("hust professors");
			out.println(hust.size());
			for(int i = 0; i < hust.size(); i++){
				Teacher t = hust.get(i);
				out.print(t.getID() + " ");
				teachers.add(t);
			}
			out.println();
			out.println("non hust professors");
			out.println(nonHust.size());
			for(int i = 0; i < nonHust.size(); i++){
				Teacher t = nonHust.get(i);
				out.print(t.getID() + " ");
				teachers.add(t);
			}
			out.println();
			out.println("subject match between professors and student");
			HashMap[] m = Utility.computeMatchJuryProfessor(jury, teachers);
			for(int j = 0; j < jury.size(); j++){
				
				for(int i = 0; i < teachers.size(); i++){
					Teacher t = teachers.get(i);
					int score = (Integer)m[j].get(t.getID());
					out.println(j + " " + t.getID() + " " + score);
				}
			}
			int nbRooms = rooms.size();
			int nbSlots = 0;
			for(int i = 0; i < rooms.size(); i++){
				Room r = rooms.get(i);
				if(nbSlots < r.getMaxNbJuries()) nbSlots = r.getMaxNbJuries();
			}
			out.println("number of slots & number of rooms");
			out.println(nbSlots + " " + nbRooms);
			out.close();
		}catch(Exception ex){
			
		}
	}
	public boolean check(Vector<JuryInfo> jury, Vector<Room> rooms, Vector<Teacher> hust, Vector<Teacher> nonHust){
		Vector<Integer> teachers = new Vector<Integer>();
		HashSet<Integer> hustID = new HashSet<Integer>();
		HashSet<Integer> nonHustID = new HashSet<Integer>();
		
		for(int i = 0; i < hust.size(); i++){
			teachers.add(hust.get(i).getID());
			hustID.add(hust.get(i).getID());
		}
		for(int i = 0; i < nonHust.size(); i++){
			teachers.add(nonHust.get(i).getID());
			nonHustID.add(nonHust.get(i).getID());
		}
		
		Vector<Teacher> allTeachers = Utility.getTeachers();
		
		HashMap<Integer, Teacher> mT = new HashMap<Integer, Teacher>();
		for(int i = 0; i < allTeachers.size(); i++)
			mT.put(allTeachers.get(i).getID(), allTeachers.get(i));
		
		Vector<Teacher> T = new Vector<Teacher>();
		for(int i = 0; i < jury.size(); i++){
			JuryInfo J = jury.get(i);
			int[] tid = J.getJuryMemberIDs();
			for(int j = 0; j < tid.length; j++)
				if(tid[j] > 0 && !teachers.contains(tid[j])){
					T.add(mT.get(tid[j]));
				}
		}

		if(T.size() > 0){
			retMsg = "Thanh vien: \n";
			for(int i = 0; i < T.size(); i++){
				Teacher t = T.get(i);
				retMsg += t.getName() + "\n";
			}
			retMsg += " khong duoc du tinh truoc tham gia vao hoi dong?????";
			return false;
		}
		
		for(int i = 0; i < rooms.size(); i++){
			Room r = rooms.get(i);
			if(r.getMaxNbHustMembers() < 3){
				int k = 3*rooms.size();
				retMsg = "So giang vien trong truong la " + hust.size() + " (nho hon " + k + ") khong du de xep lich:\n";
				for(int j = 0; j < hust.size(); j++){
					Teacher t = hust.get(j);
					retMsg += t.getName() + "\n";
				}
				return false;
			}
			if(r.getMaxNbNonHustMembers() < 2){
				int k = 2*rooms.size();
				retMsg = "So giang vien ngoai truong la " + nonHust.size() + " (nho hon " + k + ") khong du de xep lich:\n";
				for(int j = 0; j < nonHust.size(); j++){
					Teacher t = nonHust.get(j);
					retMsg += t.getName() + "\n";
				}
				
				return false;
			}
		}
		
		for(int i = 0; i < jury.size(); i++){
			JuryInfo J = jury.get(i);
			int[] p = J.getJuryMemberIDs();
			for(int j = 0; j < p.length; j++){
				if(p[j] > 0){
					if(p[j] == J.getSupervisorId()){
						Teacher t = mT.get(p[j]);
						retMsg = "Hoi dong cua SV " + J.getStudentID() + " khong hop le: GV huong dan " + t.getName() + " trung voi thanh vien hoi dong???";
						return false;
					}
					for(int j1 = j+1; j1 < p.length; j1++)
						if(p[j1] > 0 && p[j] == p[j1]){
							retMsg = "Hoi dong cua SV " + J.getStudentID() + " khong hop le: GV trung nhau ????";
							return false;
						}
					
					Teacher t = mT.get(p[j]);
					if(j == 0){
						if(!nonHustID.contains(p[j])){
							retMsg = "Hoi dong cua SV " + J.getStudentID() + " khong hop le: phan bien 1 " + t.getName() + " khong la GV ngoai truong ???";
							return false;
						}
					}else if(j == 1){
						if(!hustID.contains(p[j])){
							retMsg = "Hoi dong cua SV " + J.getStudentID() + " khong hop le: phan bien 2 " + t.getName() + "  khong la GV trong truong ???";
							System.out.println(retMsg + ", p[" + j + "] = " + p[j] + ", hust = ");
							for(int j1 = 0; j1 < hust.size(); j1++)
								System.out.println(hust.get(j1).getID() + ",");
							
							return false;
						}
					}else if(j == 2){
						if(!hustID.contains(p[j])){
							retMsg = "Hoi dong cua SV " + J.getStudentID() + " khong hop le: chu tich " + t.getName() + "  khong la GV trong truong ???";
							return false;
						}
					}else if(j == 3){
						if(!hustID.contains(p[j])){
							retMsg = "Hoi dong cua SV " + J.getStudentID() + " khong hop le: thu ky " + t.getName() + "  khong la GV trong truong ???";
							return false;
						}
					}else if(j == 4){
						if(!nonHustID.contains(p[j])){
							retMsg = "Hoi dong cua SV " + J.getStudentID() + " khong hop le: uy vien " + t.getName() + "  khong la GV ngoai truong ???";
							return false;
						}
					}
				}
			}
		}
		
		return true;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
