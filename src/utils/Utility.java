package utils;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

import DataEntity.*;

import  org.apache.poi.hssf.usermodel.*;

//import com.apple.eawt.Application;

import java.net.URLDecoder;
import java.io.*;
public class Utility {

	/**
	 * @param args
	 */
	//private Connection cnn = null;
	//private Statement stat = null;
	//private PreparedStatement preparedStat = null;
	//private ResultSet rs = null;
	
	public static Vector<SlotRoom> computeCandidateSlots(Vector<JuryInfo> listJury, int studentID, int nbSlots, int nbRooms){
		Vector<SlotRoom> cand = new Vector<SlotRoom>();
		int sz = listJury.size();
		boolean[][] conflict = new boolean[sz][sz];
		for(int i = 0; i < sz-1; i++){
			JuryInfo jury_i = listJury.get(i);
			
			for(int j = i+1; j < sz; j++){// consider only juries which are already assigned slots
				JuryInfo jury_j = listJury.get(j);
				
				conflict[i][j] = jury_i.conflict(jury_j);
				conflict[j][i] = conflict[i][j];
			}
		}
		int sel_jury = -1;
		for(int i = 0; i < sz; i++){
			JuryInfo jr = listJury.get(i);
			if(jr.getStudentID() == studentID){
				sel_jury = i;
				break;
			}
		}
		if(sel_jury == -1) return null;
		
		for(int sl = 1; sl <= nbSlots; sl++){
			for(int r = 1; r < nbRooms; r++){
				boolean ok = true;
				for(int i = 0; i < sz; i++){
					JuryInfo jr = listJury.get(i);
					if(jr.getStudentID() != studentID){
						if(conflict[i][sel_jury] && jr.getSlotId() == sl){
							ok = false;
							break;
						}
						if(jr.getSlotId() == sl && jr.getRoomId() == r){
							ok = false;
							break;
						}
					}
				}
				if(ok){
					SlotRoom sr = new SlotRoom(sl,r);
					cand.add(sr);
				}
			}
		}
		return cand;
	}	
	private static boolean checkJuryConsistent(JuryInfo jury){
		
		int[] p = new int[6];
		p[0] = jury.getSupervisorId();
		p[1] = jury.getExaminerId1();
		p[2] = jury.getExaminerId2();
		p[3] = jury.getPresidentId();
		p[4] = jury.getSecretaryId();
		p[5] = jury.getAdditionalMemberId();
		//System.out.print("Utility::checkJuryConsistent, p = ");
		for(int i = 0; i < 6; i++)
			System.out.print(p[i] + ",");
		System.out.println();
		
		for(int i = 0; i < 5; i++)
			for(int j = i+1; j < 6; j++)
				if(p[i] == p[j] && p[i] > 0 && p[j] > 0)
					return false;
		return true;
	}
	
	public static String checkConsistency(Vector<JuryInfo> listJury){
		int sz = listJury.size();
		boolean[][] conflict = new boolean[sz][sz];
		boolean[] jury_conflict = new boolean[sz];
		for(int i = 0; i < sz; i++){
			jury_conflict[i] = false;
			jury_conflict[i] = !checkJuryConsistent(listJury.get(i));
		}
		
		String r_xml = "<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>";
		r_xml += "<return-search-solution>";
		r_xml += "<ListJury>\n";
		for(int i = 0; i < sz-1; i++){
			JuryInfo jury_i = listJury.get(i);
			
			
			if(jury_i.getSlotId() > 0)for(int j = i+1; j < sz; j++){// consider only juries which are already assigned slots
				JuryInfo jury_j = listJury.get(j);
				if(jury_j.getSlotId() == 0) continue;// consider only juries which are already assigned slots
				conflict[i][j] = jury_i.conflict(jury_j);
				if(conflict[i][j] && jury_i.getSlotId() == jury_j.getSlotId() && 
						jury_i.getSlotId() > 0 && jury_j.getSlotId() > 0){
					jury_conflict[i] = true;
					jury_conflict[j] = true;
				}					
				if(jury_i.getSlotId()==jury_j.getSlotId() && jury_i.getRoomId() == jury_j.getRoomId() && 
						jury_i.getSlotId() > 0 && jury_j.getSlotId() > 0 
						&& jury_i.getRoomId() > 0 && jury_j.getRoomId() > 0){
					jury_conflict[i] = true;
					jury_conflict[j] = true;
				}
			}
		}
		
		for(int i = 0; i < sz; i++){
			JuryInfo jr = listJury.get(i);
			r_xml += "<Jury>";
			r_xml += "<StudentID>" + jr.getStudentID() + "</StudentID>\n";
			r_xml += "<StudentName>" + jr.getStudentName() + "</StudentName>\n";
			r_xml += "<ThesisTitle>" + jr.getTitle() + "</ThesisTitle>\n";
			r_xml += "<SupervisorID>" + jr.getSupervisorId() + "</SupervisorID>\n";
			r_xml += "<SupervisorName>" + jr.getSupervisorName() + "</SupervisorName>\n";
			r_xml += "<Examiner1ID>" + jr.getExaminerId1() + "</Examiner1ID>\n";
			r_xml += "<Examiner1Name>" + jr.getExaminerName1() + "</Examiner1Name>\n";
			r_xml += "<Examiner2ID>" + jr.getExaminerId2() + "</Examiner2ID>\n";
			r_xml += "<Examiner2Name>" + jr.getExaminerName2() + "</Examiner2Name>\n";
			r_xml += "<PresidentID>" + jr.getPresidentId() + "</PresidentID>\n";
			r_xml += "<PresidentName>" + jr.getPresidentName() + "</PresidentName>\n";
			r_xml += "<SecretaryID>" + jr.getSecretaryId() + "</SecretaryID>\n";
			r_xml += "<SecretaryName>" + jr.getSecretaryName() + "</SecretaryName>\n";
			r_xml += "<MemberID>" + jr.getAdditionalMemberId() + "</MemberID>\n";
			r_xml += "<MemberName>" + jr.getAdditionalMemberName() + "</MemberName>\n";
			r_xml += "<Slot>" + jr.getSlotId() + "</Slot>\n";
			r_xml += "<Room>" + jr.getRoomId() + "</Room>\n";
			if(jury_conflict[i]){
				r_xml += "<Conflict>1</Conflict>";
			}else
				r_xml += "<Conflict>0</Conflict>";
			
			r_xml += "</Jury>";
		}
		r_xml += "</ListJury>";
		
		r_xml += "<error>" + "khong co loi" + "</error>";
		r_xml += "<return-search-solution>";
		//System.out.println(r_xml);
		
		return r_xml;
	}
	
	
	public static String generateAndCheckConsistency(Vector<JuryInfo> listJury){
		int sz = listJury.size();
		
		String err_des = "";
		int err_count = 0;
		
		boolean[][] conflict = new boolean[sz][sz];
		boolean[] jury_conflict = new boolean[sz];
		for(int i = 0; i < sz; i++){
			jury_conflict[i] = false;
			jury_conflict[i] = !checkJuryConsistent(listJury.get(i));
			if(jury_conflict[i]){
				err_count++;
				err_des += "Loi " + err_count + ": Hoi dong cua SV ID = " + listJury.get(i).getStudentID() + " khong hop le\n";
			}
		}
		
		
		
		String r_xml = "<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>";
		//r_xml += "<ListJury>\n";
		r_xml += "<returned-results>\n";
		r_xml += "<solution>";
		r_xml += "true";
		r_xml += "</solution>";
		for(int i = 0; i < sz-1; i++){
			JuryInfo jury_i = listJury.get(i);
			
			
			if(jury_i.getSlotId() > 0)for(int j = i+1; j < sz; j++){// consider only juries which are already assigned slots
				JuryInfo jury_j = listJury.get(j);
				
				if(jury_j.getSlotId() == 0) continue;// consider only juries which are already assigned slots
				conflict[i][j] = jury_i.conflict(jury_j);
				/*
				if(conflict[i][j]){
					err_count++;
					err_des += "Loi " + err_count + ": Hoi dong cua SV ID " + listJury.get(i).getStudentID() + 
							" va SV ID " + listJury.get(j).getStudentID() + " xung dot\n";
				}
				*/
				if(conflict[i][j] && jury_i.getSlotId() == jury_j.getSlotId() && 
						jury_i.getSlotId() > 0 && jury_j.getSlotId() > 0){
					jury_conflict[i] = true;
					jury_conflict[j] = true;
					
					err_count++;
					err_des += "Loi " + err_count + ": Hoi dong cua SV ID " + listJury.get(i).getStudentID() + 
							" va SV ID " + listJury.get(j).getStudentID() + " xung dot\n";
				}					
				if(jury_i.getSlotId()==jury_j.getSlotId() && jury_i.getRoomId() == jury_j.getRoomId() && 
						jury_i.getSlotId() > 0 && jury_j.getSlotId() > 0 
						&& jury_i.getRoomId() > 0 && jury_j.getRoomId() > 0){
					jury_conflict[i] = true;
					jury_conflict[j] = true;
					
					err_count++;
					err_des += "Loi " + err_count + ": Hoi dong cua SV ID " + listJury.get(i).getStudentID() + 
							" va SV ID " + listJury.get(j).getStudentID() + " xung dot\n";
				}
			}
		}
		
		// sort by Room-Slot
		//int sz = listJury.size();
		JuryInfo[] J = new JuryInfo[sz];
		for(int i = 0; i < sz; i++)
			J[i] = listJury.get(i);
		
		for(int i = 0; i < sz-1; i++)
			for(int j = i+1; j < sz; j++)
				if(J[i].getRoomId() > J[j].getRoomId() || (J[i].getRoomId() == J[j].getRoomId() && 
				J[i].getSlotId() > J[j].getSlotId())){
					JuryInfo tj = J[i]; J[i] = J[j]; J[j] = tj;
				}
		
		
		for(int i = 0; i < sz; i++){
			JuryInfo jr = J[i];//listJury.get(i);
			r_xml += "<Jury>";
			r_xml += "<StudentID>" + jr.getStudentID() + "</StudentID>\n";
			r_xml += "<StudentName>" + jr.getStudentName() + "</StudentName>\n";
			r_xml += "<ThesisTitle>" + jr.getTitle() + "</ThesisTitle>\n";
			r_xml += "<SupervisorID>" + jr.getSupervisorId() + "</SupervisorID>\n";
			r_xml += "<SupervisorName>" + jr.getSupervisorName() + "</SupervisorName>\n";
			r_xml += "<Examiner1ID>" + jr.getExaminerId1() + "</Examiner1ID>\n";
			r_xml += "<Examiner1Name>" + jr.getExaminerName1() + "</Examiner1Name>\n";
			r_xml += "<Examiner2ID>" + jr.getExaminerId2() + "</Examiner2ID>\n";
			r_xml += "<Examiner2Name>" + jr.getExaminerName2() + "</Examiner2Name>\n";
			r_xml += "<PresidentID>" + jr.getPresidentId() + "</PresidentID>\n";
			r_xml += "<PresidentName>" + jr.getPresidentName() + "</PresidentName>\n";
			r_xml += "<SecretaryID>" + jr.getSecretaryId() + "</SecretaryID>\n";
			r_xml += "<SecretaryName>" + jr.getSecretaryName() + "</SecretaryName>\n";
			r_xml += "<MemberID>" + jr.getAdditionalMemberId() + "</MemberID>\n";
			r_xml += "<MemberName>" + jr.getAdditionalMemberName() + "</MemberName>\n";
			r_xml += "<Slot>" + jr.getSlotId() + "</Slot>\n";
			r_xml += "<Room>" + jr.getRoomId() + "</Room>\n";
			if(jury_conflict[i]){
				r_xml += "<Conflict>1</Conflict>";
			}else
				r_xml += "<Conflict>0</Conflict>";
			
			r_xml += "</Jury>";
		}
		
		
		Vector<Integer> slots = collectVectorSlots(listJury);
		Vector<Room> rooms = collectVectorRooms(listJury);
		for(int i = 0; i < slots.size(); i++){
			r_xml += "<slots>";
			r_xml += slots.get(i);
			r_xml += "</slots>";
		}
		
		for(int i = 0; i < rooms.size(); i++){
			Room r = rooms.get(i);
			r_xml += "<room>";
			r_xml += "<id>" + r.getID() + "</id>";
			r_xml += "<name>" + r.getDescription() + "</name>";
			r_xml += "<nb-juries>" + r.getMaxNbJuries() + "</nb-juries>";
			r_xml += "<nb-hust>" + r.getMaxNbHustMembers() + "</nb-hust>";
			r_xml += "<nb-nonhust>" + r.getMaxNbNonHustMembers() + "</nb-nonhust>";
			r_xml += "</room>";
		}
		
		//r_xml += "</ListJury>";
		
		if(err_count == 0) err_des = "HOP LE";
		
		r_xml += "<error>" + err_des + "</error>\n";
		
		r_xml += "</returned-results>";
		//System.out.println(r_xml);
		
		return r_xml;
	}

	public static String generateAndCheckConsistency(Vector<JuryInfo> listJury, String sortBy){
		int sz = listJury.size();
		// sort by Room-Slot
		//int sz = listJury.size();
		JuryInfo[] J = new JuryInfo[sz];
		for(int i = 0; i < sz; i++)
			J[i] = listJury.get(i);
		
		if(sortBy.equals("Room-Slot")){
			for(int i = 0; i < sz-1; i++)
				for(int j = i+1; j < sz; j++)
					if(J[i].getRoomId() > J[j].getRoomId() || (J[i].getRoomId() == J[j].getRoomId() && 
					J[i].getSlotId() > J[j].getSlotId())){
						JuryInfo tj = J[i]; J[i] = J[j]; J[j] = tj;
					}
		}else if(sortBy.equals("Slot-Room")){
			for(int i = 0; i < sz-1; i++)
				for(int j = i+1; j < sz; j++)
					if(J[i].getSlotId() > J[j].getSlotId() || (J[i].getSlotId() == J[j].getSlotId() && 
					J[i].getRoomId() > J[j].getRoomId())){
						JuryInfo tj = J[i]; J[i] = J[j]; J[j] = tj;
					}
			
		}else if(sortBy.equals("Supervisor")){
			for(int i = 0; i < sz-1; i++)
				for(int j = i+1; j < sz; j++)
					if(J[i].getSupervisorId() > J[j].getSupervisorId()){
						JuryInfo tj = J[i]; J[i] = J[j]; J[j] = tj;
					}
		}else if(sortBy.equals("StudentID")){
			for(int i = 0; i < sz-1; i++)
				for(int j = i+1; j < sz; j++)
					if(J[i].getStudentID() > J[j].getStudentID()){
						JuryInfo tj = J[i]; J[i] = J[j]; J[j] = tj;
					}
		}else if(sortBy.equals("Examiner1")){
			for(int i = 0; i < sz-1; i++)
				for(int j = i+1; j < sz; j++)
					if(J[i].getExaminerId1() > J[j].getExaminerId1()){
						JuryInfo tj = J[i]; J[i] = J[j]; J[j] = tj;
					}
		}else if(sortBy.equals("Examiner2")){
			for(int i = 0; i < sz-1; i++)
				for(int j = i+1; j < sz; j++)
					if(J[i].getExaminerId2() > J[j].getExaminerId2()){
						JuryInfo tj = J[i]; J[i] = J[j]; J[j] = tj;
					}
		}else if(sortBy.equals("President")){
			for(int i = 0; i < sz-1; i++)
				for(int j = i+1; j < sz; j++)
					if(J[i].getPresidentId() > J[j].getPresidentId()){
						JuryInfo tj = J[i]; J[i] = J[j]; J[j] = tj;
					}
		}else if(sortBy.equals("Secretary")){
			for(int i = 0; i < sz-1; i++)
				for(int j = i+1; j < sz; j++)
					if(J[i].getSecretaryId() > J[j].getSecretaryId()){
						JuryInfo tj = J[i]; J[i] = J[j]; J[j] = tj;
					}
		}else if(sortBy.equals("AdditionalMember")){
			for(int i = 0; i < sz-1; i++)
				for(int j = i+1; j < sz; j++)
					if(J[i].getAdditionalMemberId() > J[j].getAdditionalMemberId()){
						JuryInfo tj = J[i]; J[i] = J[j]; J[j] = tj;
					}
		}
		
		
		String err_des = "";
		int err_count = 0;
		
		boolean[][] conflict = new boolean[sz][sz];
		boolean[] jury_conflict = new boolean[sz];
		for(int i = 0; i < sz; i++){
			jury_conflict[i] = false;
			jury_conflict[i] = !checkJuryConsistent(listJury.get(i));
			if(jury_conflict[i]){
				err_count++;
				err_des += "Loi " + err_count + ": Hoi dong cua SV ID = " + listJury.get(i).getStudentID() + " khong hop le\n";
			}
		}
		
		
		
		String r_xml = "<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>";
		//r_xml += "<ListJury>\n";
		r_xml += "<returned-results>\n";
		r_xml += "<solution>";
		r_xml += "true";
		r_xml += "</solution>";
		for(int i = 0; i < sz-1; i++){
			JuryInfo jury_i = listJury.get(i);
			
			
			if(jury_i.getSlotId() > 0)for(int j = i+1; j < sz; j++){// consider only juries which are already assigned slots
				JuryInfo jury_j = listJury.get(j);
				
				if(jury_j.getSlotId() == 0) continue;// consider only juries which are already assigned slots
				conflict[i][j] = jury_i.conflict(jury_j);
				/*
				if(conflict[i][j]){
					err_count++;
					err_des += "Loi " + err_count + ": Hoi dong cua SV ID " + listJury.get(i).getStudentID() + 
							" va SV ID " + listJury.get(j).getStudentID() + " xung dot\n";
				}
				*/
				if(conflict[i][j] && jury_i.getSlotId() == jury_j.getSlotId() && 
						jury_i.getSlotId() > 0 && jury_j.getSlotId() > 0){
					jury_conflict[i] = true;
					jury_conflict[j] = true;
					
					err_count++;
					err_des += "Loi " + err_count + ": Hoi dong cua SV ID " + listJury.get(i).getStudentID() + 
							" va SV ID " + listJury.get(j).getStudentID() + " xung dot\n";
				}					
				if(jury_i.getSlotId()==jury_j.getSlotId() && jury_i.getRoomId() == jury_j.getRoomId() && 
						jury_i.getSlotId() > 0 && jury_j.getSlotId() > 0 
						&& jury_i.getRoomId() > 0 && jury_j.getRoomId() > 0){
					jury_conflict[i] = true;
					jury_conflict[j] = true;
					
					err_count++;
					err_des += "Loi " + err_count + ": Hoi dong cua SV ID " + listJury.get(i).getStudentID() + 
							" va SV ID " + listJury.get(j).getStudentID() + " xung dot\n";
				}
			}
		}
		
		

		
		for(int i = 0; i < sz; i++){
			JuryInfo jr = J[i];//listJury.get(i);
			r_xml += "<Jury>";
			r_xml += "<StudentID>" + jr.getStudentID() + "</StudentID>\n";
			r_xml += "<StudentName>" + jr.getStudentName() + "</StudentName>\n";
			r_xml += "<ThesisTitle>" + jr.getTitle() + "</ThesisTitle>\n";
			r_xml += "<SupervisorID>" + jr.getSupervisorId() + "</SupervisorID>\n";
			r_xml += "<SupervisorName>" + jr.getSupervisorName() + "</SupervisorName>\n";
			r_xml += "<Examiner1ID>" + jr.getExaminerId1() + "</Examiner1ID>\n";
			r_xml += "<Examiner1Name>" + jr.getExaminerName1() + "</Examiner1Name>\n";
			r_xml += "<Examiner2ID>" + jr.getExaminerId2() + "</Examiner2ID>\n";
			r_xml += "<Examiner2Name>" + jr.getExaminerName2() + "</Examiner2Name>\n";
			r_xml += "<PresidentID>" + jr.getPresidentId() + "</PresidentID>\n";
			r_xml += "<PresidentName>" + jr.getPresidentName() + "</PresidentName>\n";
			r_xml += "<SecretaryID>" + jr.getSecretaryId() + "</SecretaryID>\n";
			r_xml += "<SecretaryName>" + jr.getSecretaryName() + "</SecretaryName>\n";
			r_xml += "<MemberID>" + jr.getAdditionalMemberId() + "</MemberID>\n";
			r_xml += "<MemberName>" + jr.getAdditionalMemberName() + "</MemberName>\n";
			r_xml += "<Slot>" + jr.getSlotId() + "</Slot>\n";
			r_xml += "<Room>" + jr.getRoomId() + "</Room>\n";
			if(jury_conflict[i]){
				r_xml += "<Conflict>1</Conflict>";
			}else
				r_xml += "<Conflict>0</Conflict>";
			
			r_xml += "</Jury>";
		}
		
		
		Vector<Integer> slots = collectVectorSlots(listJury);
		Vector<Room> rooms = collectVectorRooms(listJury);
		for(int i = 0; i < slots.size(); i++){
			r_xml += "<slots>";
			r_xml += slots.get(i);
			r_xml += "</slots>";
		}
		
		for(int i = 0; i < rooms.size(); i++){
			Room r = rooms.get(i);
			r_xml += "<room>";
			r_xml += "<id>" + r.getID() + "</id>";
			r_xml += "<name>" + r.getDescription() + "</name>";
			r_xml += "<nb-juries>" + r.getMaxNbJuries() + "</nb-juries>";
			r_xml += "<nb-hust>" + r.getMaxNbHustMembers() + "</nb-hust>";
			r_xml += "<nb-nonhust>" + r.getMaxNbNonHustMembers() + "</nb-nonhust>";
			r_xml += "</room>";
		}
		
		//r_xml += "</ListJury>";
		
		if(err_count == 0) err_des = "HOP LE";
		
		r_xml += "<error>" + err_des + "</error>\n";
		
		r_xml += "</returned-results>";
		//System.out.println(r_xml);
		
		return r_xml;
	}

	public static String checkConsistency(List<JuryInfo> listJury){
		int sz = listJury.size();
		boolean[][] conflict = new boolean[sz][sz];
		boolean[] jury_conflict = new boolean[sz];
		for(int i = 0; i < sz; i++)
			jury_conflict[i] = false;
		
		String r_xml = "<?xml version="+"\"" + "1.0" + "\"" + " encoding=" + "\"" + "ISO-8859-1" + "\"" + "?>";
		r_xml += "<ListJury>\n";
		for(int i = 0; i < sz-1; i++){
			JuryInfo jury_i = listJury.get(i);
			for(int j = i+1; j < sz; j++){
				JuryInfo jury_j = listJury.get(j);
				conflict[i][j] = jury_i.conflict(jury_j);
				if(conflict[i][j] && jury_i.getSlotId() == jury_j.getSlotId()){
					jury_conflict[i] = true;
					jury_conflict[j] = true;
				}					
				if(jury_i.getSlotId()==jury_j.getSlotId() && jury_i.getRoomId() == jury_j.getRoomId()){
					jury_conflict[i] = true;
					jury_conflict[j] = true;
				}
			}
		}
		
		for(int i = 0; i < sz; i++){
			JuryInfo jr = listJury.get(i);
			r_xml += "<Jury>";
			r_xml += "<StudentID>" + jr.getStudentID() + "</StudentID>\n";
			r_xml += "<StudentName>" + jr.getStudentName() + "</StudentName>\n";
			r_xml += "<ThesisTitle>" + jr.getTitle() + "</ThesisTitle>\n";
			r_xml += "<Examiner1ID>" + jr.getExaminerId1() + "</Examiner1ID>\n";
			r_xml += "<Examiner1Name>" + jr.getExaminerName1() + "</Examiner1Name>\n";
			r_xml += "<Examiner2ID>" + jr.getExaminerId2() + "</Examiner2ID>\n";
			r_xml += "<Examiner2Name>" + jr.getExaminerName2() + "</Examiner2Name>\n";
			r_xml += "<PresidentID>" + jr.getPresidentId() + "</PresidentID>\n";
			r_xml += "<PresidentName>" + jr.getPresidentName() + "</PresidentName>\n";
			r_xml += "<SecretaryID>" + jr.getSecretaryId() + "</SecretaryID>\n";
			r_xml += "<SecretaryName>" + jr.getSecretaryName() + "</SecretaryName>\n";
			r_xml += "<MemberID>" + jr.getAdditionalMemberId() + "</MemberID>\n";
			r_xml += "<MemberName>" + jr.getAdditionalMemberName() + "</MemberName>\n";
			r_xml += "<Slot>" + jr.getSlotId() + "</Slot>\n";
			r_xml += "<Room>" + jr.getRoomId() + "</Room>\n";
			if(jury_conflict[i]){
				r_xml += "<Conflict>1</Conflict>";
			}else
				r_xml += "<Conflict>0</Conflict>";
			
			r_xml += "</Jury>";
		}
		r_xml += "</ListJury>";
		
		System.out.println(r_xml);
		
		return r_xml;
	}
	
	private static void close(Connection cnn, Statement stat, ResultSet rs){
		//System.out.println("cnn, stat, rs --> close()");
		try{
			if(rs != null){
				rs.close();
				rs = null;
			}
			if(stat != null){
				stat.close();
				stat = null;
			}
			if(cnn != null){
				cnn.close();
				cnn = null;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static Vector<StudentStatus> getStudentStatus(){
		Vector<StudentStatus> student_status = new Vector<StudentStatus>();
		Connection cnn = null;
		Statement stat = null;
		PreparedStatement preparedStat = null;
		ResultSet rs = null;

		//System.out.println("Start JDBC");
		try{
			
			//System.out.println("Start JDBC");
			Class.forName("com.mysql.jdbc.Driver");
			//System.out.println("JDBC -->OK");
			cnn = DriverManager.getConnection(Configure.cnnStr);
			stat = cnn.createStatement();
			rs = stat.executeQuery("select * from " + Configure.tblStudentStatus);
			//rs = stat.executeQuery("select * from students");
			while(rs.next()){
				//System.out.println(rs.getString("name"));
				//System.out.println(rs.getInt("idgv"));
				String description = rs.getString("Description");
				int id = rs.getInt("ID");
				//int id = rs.getInt("idsv");
				StudentStatus ss = new StudentStatus(id,description);
				student_status.add(ss);
			}
			//out.println("server return results");
			
			//System.out.println("get teachers successfully");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(cnn,stat,rs);
		}
		return student_status;
		
	}
	
	public static Vector<Slot> getSlots(int inp_slotIndex, int inp_roomID){
		Vector<Slot> slots = new Vector<Slot>();
		Connection cnn = null;
		Statement stat = null;
		PreparedStatement preparedStat = null;
		ResultSet rs = null;

		//System.out.println("Start JDBC");
		try{
			
			//System.out.println("Start JDBC");
			Class.forName("com.mysql.jdbc.Driver");
			//System.out.println("JDBC -->OK");
			cnn = DriverManager.getConnection(Configure.cnnStr);
			stat = cnn.createStatement();
			rs = stat.executeQuery("select * from " + Configure.tblSlot + " where SlotIndex = " + inp_slotIndex + " and roomID = " + inp_roomID);
			//rs = stat.executeQuery("select * from students");
			while(rs.next()){
				//System.out.println(rs.getString("name"));
				//System.out.println(rs.getInt("idgv"));
				String description = rs.getString("Description");
				int id = rs.getInt("ID");
				int slotIndex = rs.getInt("SlotIndex");
				int roomID = rs.getInt("RoomID");
				//int id = rs.getInt("idsv");
				Slot sl = new Slot(id,slotIndex,description,roomID);
				slots.add(sl);
			}
			
			//repair ID of slots so that they are 1, 2, ....
			//for(int i = 0; i < slots.size(); i++){
				//Slot sl = slots.get(i);
				//sl.setID(i+1);
			//}
			//out.println("server return results");
			
			//System.out.println("get teachers successfully");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(cnn,stat,rs);
		}
		return slots;
		
	}

	public static Vector<Slot> getSlots(){
		Vector<Slot> slots = new Vector<Slot>();
		Connection cnn = null;
		Statement stat = null;
		PreparedStatement preparedStat = null;
		ResultSet rs = null;

		//System.out.println("Start JDBC");
		try{
			
			//System.out.println("Start JDBC");
			Class.forName("com.mysql.jdbc.Driver");
			//System.out.println("JDBC -->OK");
			cnn = DriverManager.getConnection(Configure.cnnStr);
			stat = cnn.createStatement();
			rs = stat.executeQuery("select * from " + Configure.tblSlot);
			//rs = stat.executeQuery("select * from students");
			while(rs.next()){
				//System.out.println(rs.getString("name"));
				//System.out.println(rs.getInt("idgv"));
				String description = rs.getString("Description");
				int id = rs.getInt("ID");
				int slotIndex = rs.getInt("SlotIndex");
				int roomID = rs.getInt("RoomID");
				//int id = rs.getInt("idsv");
				Slot sl = new Slot(id,slotIndex,description,roomID);
				slots.add(sl);
			}
			
			//repair ID of slots so that they are 1, 2, ....
			//for(int i = 0; i < slots.size(); i++){
				//Slot sl = slots.get(i);
				//sl.setID(i+1);
			//}
			//out.println("server return results");
			
			//System.out.println("get teachers successfully");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(cnn,stat,rs);
		}
		return slots;
		
	}
	public static Vector<DefenseSession> getDefenseSessions(int is_active){
		Vector<DefenseSession> sessions = new Vector<DefenseSession>();
		Connection cnn = null;
		Statement stat = null;
		PreparedStatement preparedStat = null;
		ResultSet rs = null;

		//System.out.println("Start JDBC");
		try{
			
			//System.out.println("Start JDBC");
			Class.forName("com.mysql.jdbc.Driver");
			//System.out.println("JDBC -->OK");
			cnn = DriverManager.getConnection(Configure.cnnStr);
			stat = cnn.createStatement();
			rs = stat.executeQuery("select * from " + Configure.tblDefenseSession + " where Active = " + is_active);
			//rs = stat.executeQuery("select * from students");
			while(rs.next()){
				//System.out.println(rs.getString("name"));
				//System.out.println(rs.getInt("idgv"));
				String description = rs.getString("Description");
				int id = rs.getInt("ID");
				int active = rs.getInt("Active");
				//int id = rs.getInt("idsv");
				DefenseSession s = new DefenseSession(id,description,active);
				sessions.add(s);
			}
			

			//out.println("server return results");
			
			//System.out.println("get teachers successfully");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(cnn,stat,rs);
		}
		return sessions;
		
	}

	public static Vector<DefenseSession> getDefenseSessions(){
		Vector<DefenseSession> sessions = new Vector<DefenseSession>();
		Connection cnn = null;
		Statement stat = null;
		PreparedStatement preparedStat = null;
		ResultSet rs = null;

		//System.out.println("Start JDBC");
		try{
			
			//System.out.println("Start JDBC");
			Class.forName("com.mysql.jdbc.Driver");
			//System.out.println("JDBC -->OK");
			cnn = DriverManager.getConnection(Configure.cnnStr);
			stat = cnn.createStatement();
			rs = stat.executeQuery("select * from " + Configure.tblDefenseSession);
			//rs = stat.executeQuery("select * from students");
			while(rs.next()){
				//System.out.println(rs.getString("name"));
				//System.out.println(rs.getInt("idgv"));
				String description = rs.getString("Description");
				int id = rs.getInt("ID");
				int active = rs.getInt("Active");
				//int id = rs.getInt("idsv");
				DefenseSession s = new DefenseSession(id,description,active);
				sessions.add(s);
			}
			

			//out.println("server return results");
			
			//System.out.println("get teachers successfully");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(cnn,stat,rs);
		}
		return sessions;
		
	}
	
	public static void addSubJectCategory(String name){
		Connection cnn = null;
		Statement stat = null;
		PreparedStatement preparedStat = null;
		ResultSet rs = null;
		try{
			if(cnn == null){
				Class.forName("com.mysql.jdbc.Driver");
				cnn = DriverManager.getConnection(Configure.cnnStr);
				preparedStat = cnn.prepareStatement("insert into " + Configure.tblSubjectCategories + "(Name) " + " values(?)");
				preparedStat.setString(1, name);
				preparedStat.executeUpdate();
				close(cnn,stat,rs);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(cnn, stat,rs);
		}		

	}
	public static Vector<SubjectCategory> getSubjectCategories(){
		Vector<SubjectCategory> categories = new Vector<SubjectCategory>();
		
		Connection cnn = null;
		Statement stat = null;
		PreparedStatement preparedStat = null;
		ResultSet rs = null;
		
		//System.out.println("Start JDBC");
		try{
			
			//System.out.println("Start JDBC");
			Class.forName("com.mysql.jdbc.Driver");
			//System.out.println("JDBC -->OK");
			cnn = DriverManager.getConnection(Configure.cnnStr);
			stat = cnn.createStatement();
			rs = stat.executeQuery("select * from " + Configure.tblSubjectCategories);
			while(rs.next()){
				String name = rs.getString("Name");
				int id = rs.getInt("ID");
				SubjectCategory cat = new SubjectCategory(id,name);
				//System.out.println("Utility::getSubjectCategory, GOT " + id + "," + name);
				categories.add(cat);
			}
			
			//repair ID of rooms so that they are 1, 2, 3, ...
			//for(int i = 0; i < rooms.size(); i++){
				//Room r = rooms.get(i);
				//r.setID(i+1);
			//}
			
			//out.println("server return results");
			
			//System.out.println("get teachers successfully");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(cnn,stat,rs);
		}
		return categories;
		
	}

	public static Vector<Room> getRooms(){
		Vector<Room> rooms = new Vector<Room>();
		
		Connection cnn = null;
		Statement stat = null;
		PreparedStatement preparedStat = null;
		ResultSet rs = null;
		
		//System.out.println("Start JDBC");
		try{
			
			//System.out.println("Start JDBC");
			Class.forName("com.mysql.jdbc.Driver");
			//System.out.println("JDBC -->OK");
			cnn = DriverManager.getConnection(Configure.cnnStr);
			stat = cnn.createStatement();
			rs = stat.executeQuery("select * from " + Configure.tblRoom);
			//rs = stat.executeQuery("select * from students");
			while(rs.next()){
				//System.out.println(rs.getString("name"));
				//System.out.println(rs.getInt("idgv"));
				String description = rs.getString("Description");
				int id = rs.getInt("ID");
				int sessionID = rs.getInt("DefenseSessionID");
				//int id = rs.getInt("idsv");
				Room r = new Room(id,description,sessionID);
				//System.out.println("Utility::getRooms, GOT " + id + "," + description + "," + sessionID);
				rooms.add(r);
			}
			
			//repair ID of rooms so that they are 1, 2, 3, ...
			//for(int i = 0; i < rooms.size(); i++){
				//Room r = rooms.get(i);
				//r.setID(i+1);
			//}
			
			//out.println("server return results");
			
			//System.out.println("get teachers successfully");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(cnn,stat,rs);
		}
		return rooms;
		
	}
	public static Vector<StudentSubjectCategoryMatch> getStudentSubjectCategoryMatches(){
		Vector<StudentSubjectCategoryMatch> list = new Vector<StudentSubjectCategoryMatch>();
		
		Connection cnn = null;
		Statement stat = null;
		PreparedStatement preparedStat = null;
		ResultSet rs = null;
		
		//System.out.println("Start JDBC");
		try{
			
			//System.out.println("Start JDBC");
			Class.forName("com.mysql.jdbc.Driver");
			//System.out.println("JDBC -->OK");
			cnn = DriverManager.getConnection(Configure.cnnStr);
			stat = cnn.createStatement();
			rs = stat.executeQuery("select * from " + Configure.tblStudentDefenseSubjectCategory);
			
			while(rs.next()){
				int id = rs.getInt("ID");
				int studentID = rs.getInt("StudentID");
				int subjectCategoryID = rs.getInt("SubjectCategoryID");
				int matchScore = rs.getInt("MatchScore");
				if(matchScore == 0) matchScore = Configure.maxMatchSubjectScore;
				StudentSubjectCategoryMatch e = new StudentSubjectCategoryMatch(id,studentID,subjectCategoryID, matchScore);
				list.add(e);
			}
			
			//repair ID of rooms so that they are 1, 2, 3, ...
			//for(int i = 0; i < rooms.size(); i++){
				//Room r = rooms.get(i);
				//r.setID(i+1);
			//}
			
			//out.println("server return results");
			
			//System.out.println("get teachers successfully");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(cnn,stat,rs);
		}
		return list;
		
	}
	public static Vector<StudentSubjectCategoryMatch> getStudentSubjectCategoryMatches(int studID){
		Vector<StudentSubjectCategoryMatch> list = new Vector<StudentSubjectCategoryMatch>();
		
		Connection cnn = null;
		Statement stat = null;
		PreparedStatement preparedStat = null;
		ResultSet rs = null;
		
		//System.out.println("Start JDBC");
		try{
			
			//System.out.println("Start JDBC");
			Class.forName("com.mysql.jdbc.Driver");
			//System.out.println("JDBC -->OK");
			cnn = DriverManager.getConnection(Configure.cnnStr);
			stat = cnn.createStatement();
			rs = stat.executeQuery("select * from " + Configure.tblStudentDefenseSubjectCategory + " where StudentID = " + studID);
			
			while(rs.next()){
				int id = rs.getInt("ID");
				int studentID = rs.getInt("StudentID");
				int subjectCategoryID = rs.getInt("SubjectCategoryID");
				int matchScore = rs.getInt("MatchScore");
				if(matchScore == 0) matchScore = Configure.maxMatchSubjectScore;
				
				StudentSubjectCategoryMatch e = new StudentSubjectCategoryMatch(id,studentID,subjectCategoryID, matchScore);
				list.add(e);
			}
			
			//repair ID of rooms so that they are 1, 2, 3, ...
			//for(int i = 0; i < rooms.size(); i++){
				//Room r = rooms.get(i);
				//r.setID(i+1);
			//}
			
			//out.println("server return results");
			
			//System.out.println("get teachers successfully");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(cnn,stat,rs);
		}
		return list;
		
	}

	public static Vector<ProfessorSubjectCategoryMatch> getProfessorSubjectCategoryMatches(){
		Vector<ProfessorSubjectCategoryMatch> list = new Vector<ProfessorSubjectCategoryMatch>();
		
		Connection cnn = null;
		Statement stat = null;
		PreparedStatement preparedStat = null;
		ResultSet rs = null;
		
		//System.out.println("Start JDBC");
		try{
			
			//System.out.println("Start JDBC");
			Class.forName("com.mysql.jdbc.Driver");
			//System.out.println("JDBC -->OK");
			cnn = DriverManager.getConnection(Configure.cnnStr);
			stat = cnn.createStatement();
			rs = stat.executeQuery("select * from " + Configure.tblProfessorSubjectCategory);
			
			while(rs.next()){
				int id = rs.getInt("ID");
				int professorID = rs.getInt("ProfessorID");
				int subjectCategoryID = rs.getInt("SubjectCategoryID");
				int matchScore = rs.getInt("MatchScore");
				if(matchScore == 0) matchScore = Configure.maxMatchSubjectScore;
				ProfessorSubjectCategoryMatch e = new ProfessorSubjectCategoryMatch(id,professorID,subjectCategoryID, matchScore);
				list.add(e);
			}
			
			//repair ID of rooms so that they are 1, 2, 3, ...
			//for(int i = 0; i < rooms.size(); i++){
				//Room r = rooms.get(i);
				//r.setID(i+1);
			//}
			
			//out.println("server return results");
			
			//System.out.println("get teachers successfully");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(cnn,stat,rs);
		}
		return list;
		
	}
	public static Vector<ProfessorSubjectCategoryMatch> getProfessorSubjectCategoryMatches(int profID){
		Vector<ProfessorSubjectCategoryMatch> list = new Vector<ProfessorSubjectCategoryMatch>();
		
		Connection cnn = null;
		Statement stat = null;
		PreparedStatement preparedStat = null;
		ResultSet rs = null;
		
		//System.out.println("Start JDBC");
		try{
			
			//System.out.println("Start JDBC");
			Class.forName("com.mysql.jdbc.Driver");
			//System.out.println("JDBC -->OK");
			cnn = DriverManager.getConnection(Configure.cnnStr);
			stat = cnn.createStatement();
			rs = stat.executeQuery("select * from " + Configure.tblProfessorSubjectCategory + " where ProfessorID = " + profID);
			
			while(rs.next()){
				int id = rs.getInt("ID");
				int professorID = rs.getInt("ProfessorID");
				int subjectCategoryID = rs.getInt("SubjectCategoryID");
				int matchScore = rs.getInt("MatchScore");
				if(matchScore == 0) matchScore = Configure.maxMatchSubjectScore;
				ProfessorSubjectCategoryMatch e = new ProfessorSubjectCategoryMatch(id,professorID,subjectCategoryID, matchScore);
				list.add(e);
			}
			
			//repair ID of rooms so that they are 1, 2, 3, ...
			//for(int i = 0; i < rooms.size(); i++){
				//Room r = rooms.get(i);
				//r.setID(i+1);
			//}
			
			//out.println("server return results");
			
			//System.out.println("get teachers successfully");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(cnn,stat,rs);
		}
		return list;
		
	}

	public static Vector<Room> getRooms(String roomName, int sessionID){
		Vector<Room> rooms = new Vector<Room>();
		
		Connection cnn = null;
		Statement stat = null;
		PreparedStatement preparedStat = null;
		ResultSet rs = null;
		
		//System.out.println("Start JDBC");
		try{
			
			//System.out.println("Start JDBC");
			Class.forName("com.mysql.jdbc.Driver");
			//System.out.println("JDBC -->OK");
			cnn = DriverManager.getConnection(Configure.cnnStr);
			stat = cnn.createStatement();
			rs = stat.executeQuery("select * from " + Configure.tblRoom + " where Description = '" + roomName + "' and DefenseSessionID = " + sessionID);
			//rs = stat.executeQuery("select * from students");
			while(rs.next()){
				//System.out.println(rs.getString("name"));
				//System.out.println(rs.getInt("idgv"));
				String description = rs.getString("Description");
				int id = rs.getInt("ID");
				//int sessionID = rs.getInt("DefenseSessionID");
				//int id = rs.getInt("idsv");
				Room r = new Room(id,description,sessionID);
				//System.out.println("Utility::getRooms, GOT " + id + "," + description + "," + sessionID);
				rooms.add(r);
			}
			
			//repair ID of rooms so that they are 1, 2, 3, ...
			//for(int i = 0; i < rooms.size(); i++){
				//Room r = rooms.get(i);
				//r.setID(i+1);
			//}
			
			//out.println("server return results");
			
			//System.out.println("get teachers successfully");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(cnn,stat,rs);
		}
		return rooms;
		
	}

	public static Vector<Department> getDepartments(){
		Vector<Department> departments = new Vector<Department>();
		
		Connection cnn = null;
		Statement stat = null;
		PreparedStatement preparedStat = null;
		ResultSet rs = null;
		
		//System.out.println("Start JDBC");
		try{
			
			//System.out.println("Start JDBC");
			Class.forName("com.mysql.jdbc.Driver");
			//System.out.println("JDBC -->OK");
			cnn = DriverManager.getConnection(Configure.cnnStr);
			stat = cnn.createStatement();
			rs = stat.executeQuery("select * from " + Configure.tblDepartment);
			//rs = stat.executeQuery("select * from students");
			while(rs.next()){
				//System.out.println(rs.getString("name"));
				//System.out.println(rs.getInt("idgv"));
				String name = rs.getString("Name");
				int id = rs.getInt("ID");
				//int id = rs.getInt("idsv");
				Department dep = new Department(id,name);
				departments.add(dep);
			}
			//out.println("server return results");
			
			//System.out.println("get teachers successfully");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(cnn,stat,rs);
		}
		return departments;
		
	}
	
	public static Vector<Teacher> getTeachers(String name, String institute, String instituteName, int department){
		Vector<Teacher> teachers = new Vector<Teacher>();
		Connection cnn = null;
		Statement stat = null;
		PreparedStatement preparedStat = null;
		ResultSet rs = null;
		
		//System.out.println("Start JDBC");
		try{
			
			//System.out.println("Start JDBC");
			Class.forName("com.mysql.jdbc.Driver");
			//System.out.println("JDBC -->OK");
			cnn = DriverManager.getConnection(Configure.cnnStr);
			stat = cnn.createStatement();
			rs = stat.executeQuery("select * from " + Configure.tblProfessors + " where Name = " + "'" + name + "'" + 
			" and Institute = '" + institute + "' and InstituteName = '" + instituteName + 
			"' and Department = " + department + " order by Name");
			//rs = stat.executeQuery("select * from students");
			
			Vector<Department> departments = getDepartments();
			HashMap<Integer,String> mDep = new HashMap<Integer,String>();
			for(int i = 0; i < departments.size(); i++){
				Department dep = departments.get(i);
				mDep.put(dep.getID(), dep.getName());
			}
			//Teacher t0 = new Teacher(0,"-","-","-","-","-");// dummy teacher
			//teachers.add(t0);
			
			while(rs.next()){
				//System.out.println(rs.getString("name"));
				//System.out.println(rs.getInt("idgv"));
				String rname = rs.getString("Name");
				int id = rs.getInt("ID");
				int depID = rs.getInt("Department");
				String rinstitute = rs.getString("Institute");
				String rinstituteName = rs.getString("InstituteName");
				int expertLevel = rs.getInt("ExpertLevel");
				String degree = rs.getString("Degree");
				//int id = rs.getInt("idsv");
				Teacher t = new Teacher(id,rname,rinstitute,mDep.get(depID),rinstituteName,degree);
				t.setExpertLevel(expertLevel);
				teachers.add(t);
			}
			//out.println("server return results");
			
			System.out.println("get teachers successfully");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(cnn,stat,rs);
		}
		return teachers;
	}
	
	public static Vector<Teacher> getTeachers(){
		
		
		Vector<Teacher> teachers = new Vector<Teacher>();
		Connection cnn = null;
		Statement stat = null;
		PreparedStatement preparedStat = null;
		ResultSet rs = null;
		
		//System.out.println("Start JDBC");
		try{
			
			//System.out.println("Start JDBC");
			Class.forName("com.mysql.jdbc.Driver");
			//System.out.println("JDBC -->OK");
			cnn = DriverManager.getConnection(Configure.cnnStr);
			stat = cnn.createStatement();
			rs = stat.executeQuery("select * from " + Configure.tblProfessors + " order by Name");
			//rs = stat.executeQuery("select * from students");
			
			Vector<Department> departments = getDepartments();
			HashMap<Integer,String> mDep = new HashMap<Integer,String>();
			for(int i = 0; i < departments.size(); i++){
				Department dep = departments.get(i);
				mDep.put(dep.getID(), dep.getName());
			}
			Teacher t0 = new Teacher(0,"-","-","-","-","-");// dummy teacher
			teachers.add(t0);
			
			while(rs.next()){
				//System.out.println(rs.getString("name"));
				//System.out.println(rs.getInt("idgv"));
				String name = rs.getString("Name");
				int id = rs.getInt("ID");
				int depID = rs.getInt("Department");
				String institute = rs.getString("Institute");
				String instituteName = rs.getString("InstituteName");
				int expertLevel = rs.getInt("ExpertLevel");
				String degree = rs.getString("Degree");
				//int id = rs.getInt("idsv");
				Teacher t = new Teacher(id,name,institute,mDep.get(depID),instituteName,degree);
				t.setExpertLevel(expertLevel);
				teachers.add(t);
			}
			//out.println("server return results");
			
			//System.out.println("get teachers successfully");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(cnn,stat,rs);
		}
		return teachers;
	}
	public static Vector<Teacher> getHustTeachers(){
		
		
		Vector<Teacher> teachers = new Vector<Teacher>();
		Connection cnn = null;
		Statement stat = null;
		PreparedStatement preparedStat = null;
		ResultSet rs = null;
		
		//System.out.println("Start JDBC");
		try{
			
			//System.out.println("Start JDBC");
			Class.forName("com.mysql.jdbc.Driver");
			//System.out.println("JDBC -->OK");
			cnn = DriverManager.getConnection(Configure.cnnStr);
			stat = cnn.createStatement();
			rs = stat.executeQuery("select * from " + Configure.tblProfessors + " order by Name");
			//rs = stat.executeQuery("select * from students");
			
			Vector<Department> departments = getDepartments();
			HashMap<Integer,String> mDep = new HashMap<Integer,String>();
			for(int i = 0; i < departments.size(); i++){
				Department dep = departments.get(i);
				mDep.put(dep.getID(), dep.getName());
			}
			Teacher t0 = new Teacher(0,"-","-","-","-","-");// dummy teacher
			teachers.add(t0);
			
			while(rs.next()){
				//System.out.println(rs.getString("name"));
				//System.out.println(rs.getInt("idgv"));
				String name = rs.getString("Name");
				int id = rs.getInt("ID");
				int depID = rs.getInt("Department");
				String institute = rs.getString("Institute");
				String instituteName = rs.getString("InstituteName");
				int expertLevel = rs.getInt("ExpertLevel");
				String degree = rs.getString("Degree");
				//int id = rs.getInt("idsv");
				Teacher t = new Teacher(id,name,institute,mDep.get(depID),instituteName,degree);
				t.setExpertLevel(expertLevel);
				if(institute.equals("HUST"))
					teachers.add(t);
			}
			//out.println("server return results");
			
			//System.out.println("get teachers successfully");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(cnn,stat,rs);
		}
		return teachers;
	}
	public static Vector<Teacher> getNonHustTeachers(){
		
		
		Vector<Teacher> teachers = new Vector<Teacher>();
		Connection cnn = null;
		Statement stat = null;
		PreparedStatement preparedStat = null;
		ResultSet rs = null;
		
		//System.out.println("Start JDBC");
		try{
			
			//System.out.println("Start JDBC");
			Class.forName("com.mysql.jdbc.Driver");
			//System.out.println("JDBC -->OK");
			cnn = DriverManager.getConnection(Configure.cnnStr);
			stat = cnn.createStatement();
			rs = stat.executeQuery("select * from " + Configure.tblProfessors + " order by Name");
			//rs = stat.executeQuery("select * from students");
			
			Vector<Department> departments = getDepartments();
			HashMap<Integer,String> mDep = new HashMap<Integer,String>();
			for(int i = 0; i < departments.size(); i++){
				Department dep = departments.get(i);
				mDep.put(dep.getID(), dep.getName());
			}
			Teacher t0 = new Teacher(0,"-","-","-","-","-");// dummy teacher
			teachers.add(t0);
			
			while(rs.next()){
				//System.out.println(rs.getString("name"));
				//System.out.println(rs.getInt("idgv"));
				String name = rs.getString("Name");
				int id = rs.getInt("ID");
				int depID = rs.getInt("Department");
				String institute = rs.getString("Institute");
				String instituteName = rs.getString("InstituteName");
				int expertLevel = rs.getInt("ExpertLevel");
				String degree = rs.getString("Degree");
				//int id = rs.getInt("idsv");
				Teacher t = new Teacher(id,name,institute,mDep.get(depID),instituteName,degree);
				t.setExpertLevel(expertLevel);
				if(!institute.equals("HUST"))
					teachers.add(t);
			}
			//out.println("server return results");
			
			//System.out.println("get teachers successfully");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(cnn,stat,rs);
		}
		return teachers;
	}

	public static Vector<Student> getStudents(){
		Vector<Student> students = new Vector<Student>();
		Connection cnn = null;
		Statement stat = null;
		PreparedStatement preparedStat = null;
		ResultSet rs = null;
		
		//System.out.println("Start JDBC");
		try{
			
			//System.out.println("Start JDBC");
			Class.forName("com.mysql.jdbc.Driver");
			//System.out.println("JDBC -->OK");
			cnn = DriverManager.getConnection(Configure.cnnStr);
			stat = cnn.createStatement();
			rs = stat.executeQuery("select * from " + Configure.tblStudents);
			//rs = stat.executeQuery("select * from students");
			Student st0 = new Student(0,"-");//dummy student
			students.add(st0);
			while(rs.next()){
				//System.out.println(rs.getString("name"));
				//System.out.println(rs.getInt("idgv"));
				String name = rs.getString("StudentName");
				//int id = Integer.valueOf(rs.getString("ID"));
				int id = rs.getInt("ID");
				//int id = rs.getInt("idsv");
				Student st = new Student(id,name);
				students.add(st);
			}
			//out.println("server return results");
			
			System.out.println("get students successfully");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(cnn,stat,rs);
		}
		return students;
	}

	public static Vector<Student> getNotDefensedStudents(){
		Vector<Student> students = new Vector<Student>();
		Connection cnn = null;
		Statement stat = null;
		PreparedStatement preparedStat = null;
		ResultSet rs = null;
		
		//System.out.println("Start JDBC");
		try{
			
			//System.out.println("Start JDBC");
			Class.forName("com.mysql.jdbc.Driver");
			//System.out.println("JDBC -->OK");
			cnn = DriverManager.getConnection(Configure.cnnStr);
			stat = cnn.createStatement();
			rs = stat.executeQuery("select * from " + Configure.tblStudents + " order by StudentName");
			//rs = stat.executeQuery("select * from students");
			Student st0 = new Student(0,"-");//dummy student
			students.add(st0);
			while(rs.next()){
				//System.out.println(rs.getString("name"));
				//System.out.println(rs.getInt("idgv"));
				String name = rs.getString("StudentName");
				//int id = Integer.valueOf(rs.getString("ID"));
				int id = rs.getInt("ID");
				int status = rs.getInt("Status");
				//int id = rs.getInt("idsv");
				if(status == 3) continue;
				Student st = new Student(id,name);
				students.add(st);
			}
			//out.println("server return results");
			
			System.out.println("get students successfully");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(cnn,stat,rs);
		}
		//sortByName(students);
		return students;
	}
	/*
	private static void sortByName(Vector<Student> students){
		int sz = students.size();
		String[] names = new String[sz];
		Student[] sorted_full_names = new Student[sz];
		//Vector<String> names = new Vector<String>();
		for(int i = 0; i < sz; i++){
			Student st = students.get(i);
			String fn = st.getName();
			int idx = fn.indexOf(" "); cannot be applied because encoded
			String name = fn.substring(idx,fn.length());
			names[i] = name;
			sorted_full_names[i] = st;
		}
		for(int i = 0; i < sz-1; i++){
			for(int j = i+1; j < sz; j++){
				//String name_i = names.get(i);
				//String name_j = names.get(j);
				if(names[i].compareTo(names[j]) < 0){
					String tmp = names[i]; names[i]= names[j]; names[j] = tmp;
					Student tmpStudent = sorted_full_names[i]; sorted_full_names[i] = sorted_full_names[j]; sorted_full_names[j] = tmpStudent;
				}
			}
		}
		students.clear();
		for(int i = 0; i < sz; i++){
			students.add(sorted_full_names[i]);
		}
	}
	*/
	public static Vector<Jury> getJuries(){
		Vector<Jury> juries = new Vector<Jury>();
		Connection cnn = null;
		Statement stat = null;
		PreparedStatement preparedStat = null;
		ResultSet rs = null;

		//System.out.println("Start JDBC");
		try{
			
			//System.out.println("Start JDBC");
			Class.forName("com.mysql.jdbc.Driver");
			//System.out.println("JDBC -->OK");
			cnn = DriverManager.getConnection(Configure.cnnStr);
			stat = cnn.createStatement();
			//rs = stat.executeQuery("SELECT S.ID as ID, S.Title as ThesisTitle, ST.ID as StudentID, ST.StudentName, P1.Name as Examiner1Name, P1.ID as Examiner1ID, " +
			//" P2.Name as Examiner2Name, P2.ID as Examiner2ID, P3.Name as PresidentName, P3.ID as PresidentID, " + 
			//		"P4.Name as SecretaryName, P4.ID as SecretaryID, P5.Name as MemberName, P5.ID as MemberID FROM " + 
			//" student_defense as S, supervise_students as ST, professors as P1, professors as P2, professors as P3, " + 
			//		" professors as P4, professors as P5 where S.examiner1 = P1.ID and S.examiner2 = P2.ID and S.president = P3.ID and " + 
			//" S.secretary = P4.ID and S.additionalMember = P5.ID  and ST.ID = S.StudentID");

			rs = stat.executeQuery("SELECT S.ID as ID, S.Title as ThesisTitle, ST.ID as StudentID, ST.StudentName, P1.Name as Examiner1Name, P1.ID as Examiner1ID, " +
			" P2.Name as Examiner2Name, P2.ID as Examiner2ID, P3.Name as PresidentName, P3.ID as PresidentID, " + 
					"P4.Name as SecretaryName, P4.ID as SecretaryID, P5.Name as MemberName, P5.ID as MemberID, S.Slot as Slot, S.Room as Room FROM " + 
			" student_defense as S, supervise_students as ST, professors as P1, professors as P2, professors as P3, " + 
					" professors as P4, professors as P5 where S.examiner1 = P1.ID and S.examiner2 = P2.ID and S.president = P3.ID and " + 
			" S.secretary = P4.ID and S.additionalMember = P5.ID  and ST.ID = S.StudentID");
			
			while(rs.next()){
				int id = rs.getInt("ID");
				int studentID = rs.getInt("StudentID");
				String studentName = rs.getString("StudentName");
				int examiner1ID = rs.getInt("Examiner1ID");
				String examiner1Name = rs.getString("Examiner1Name");
				int examiner2ID = rs.getInt("Examiner2ID");
				String examiner2Name = rs.getString("Examiner2Name");
				int presidentID = rs.getInt("PresidentID");
				String presidentName = rs.getString("Presidentname");
				int secretaryID = rs.getInt("SecretaryID");
				String secretaryName = rs.getString("SecretaryName");
				int memberID = rs.getInt("MemberID");
				String memberName = rs.getString("MemberName");
				int slot = rs.getInt("Slot");
				int room = rs.getInt("Room");
				
				String thesis_title = rs.getString("ThesisTitle");
				Jury jr = new Jury(studentID, studentName, thesis_title, examiner1ID, examiner1Name, examiner2ID,
						examiner2Name, presidentID, presidentName, secretaryID, secretaryName, memberID, memberName);
				
				
				juries.add(jr);
			}
			//out.println("server return results");
			
			System.out.println("get students successfully");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(cnn,stat,rs);
		}
		return juries;
	}

	
	public static JuryInfo getJury(int studentID){
		Vector<JuryInfo> juries = getListJuryInfo("studentID","All");
		JuryInfo jury = null;
		for(int i = 0; i < juries.size(); i++){
			JuryInfo jr = juries.get(i);
			if(jr.getStudentID() == studentID){
				jury = jr;
				break;
			}
		}
		return jury;
	}
	public static JuryInfo getJury(int studentID, int sessionID){
		Vector<JuryInfo> juries = getListJuryInfo();
		System.out.println("Utility::getJury(" + studentID + "," + sessionID + ", all juries size = " + juries.size());
		JuryInfo jury = null;
		for(int i = 0; i < juries.size(); i++){
			JuryInfo jr = juries.get(i);
			if(jr.getStudentID() == studentID && jr.getIdDataSet() == sessionID){
				jury = jr;
				break;
			}
		}
		return jury;
	}

	public static Vector<JuryInfo> getListJuryInfoOld(String sortBy, String filterBy){
		System.out.println("Utility::getListJuryInfo, sortBy " + sortBy + " filterBy " + filterBy);
		Vector<JuryInfo> juries = new Vector<JuryInfo>();
		Connection cnn = null;
		Statement stat = null;
		PreparedStatement preparedStat = null;
		ResultSet rs = null;

		//System.out.println("Start JDBC");
		try{
			
			//System.out.println("Start JDBC");
			Class.forName("com.mysql.jdbc.Driver");
			//System.out.println("JDBC -->OK");
			cnn = DriverManager.getConnection(Configure.cnnStr);
			stat = cnn.createStatement();
			//rs = stat.executeQuery("SELECT S.ID as ID, S.Title as ThesisTitle, ST.ID as StudentID, ST.StudentName, P1.Name as Examiner1Name, P1.ID as Examiner1ID, " +
			//" P2.Name as Examiner2Name, P2.ID as Examiner2ID, P3.Name as PresidentName, P3.ID as PresidentID, " + 
			//		"P4.Name as SecretaryName, P4.ID as SecretaryID, P5.Name as MemberName, P5.ID as MemberID FROM " + 
			//" student_defense as S, supervise_students as ST, professors as P1, professors as P2, professors as P3, " + 
			//		" professors as P4, professors as P5 where S.examiner1 = P1.ID and S.examiner2 = P2.ID and S.president = P3.ID and " + 
			//" S.secretary = P4.ID and S.additionalMember = P5.ID  and ST.ID = S.StudentID");

			String sql = "SELECT S.ID as ID, S.Title as ThesisTitle, ST.ID as StudentID, ST.StudentName, P1.Name as Examiner1Name, P0.ID as SupervisorID, P1.ID as Examiner1ID, " +
			" P2.Name as Examiner2Name, P2.ID as Examiner2ID, P3.Name as PresidentName, P3.ID as PresidentID, " + 
					"P4.Name as SecretaryName, P4.ID as SecretaryID, P5.Name as MemberName, P5.ID as MemberID, S.Slot as Slot, S.Room as Room, S.SessionID as SessionID FROM " + 
			" student_defense as S, supervise_students as ST, professors as P0, professors as P1, professors as P2, professors as P3, " + 
					" professors as P4, professors as P5 where S.Supervisor = P0.ID and S.examiner1 = P1.ID and S.examiner2 = P2.ID and S.president = P3.ID and " + 
			" S.secretary = P4.ID and S.additionalMember = P5.ID  and ST.ID = S.StudentID";
			
			if(sortBy.compareTo("StudentID") == 0){
				sql += " order by StudentID";
			}else if(sortBy.compareTo("Slot")==0){
				sql += " order by Slot";
			}else if(sortBy.compareTo("Room")==0){
				sql += " order by Room";
			}
			rs = stat.executeQuery(sql);
			
			Vector<Slot> slots = getSlots();
			Vector<Room> rooms = getRooms();
			Vector<Teacher> teachers = getTeachers();
			
			HashMap<Integer, String> mDepart = new HashMap<Integer,String>();
			for(int i = 0; i < teachers.size(); i++){
				Teacher t = teachers.get(i);
				mDepart.put(t.getID(), t.getDepartment());
			}
			
			HashMap<Integer, Slot> mSlots = new HashMap<Integer, Slot>();
			HashMap<Integer, Room> mRooms = new HashMap<Integer,Room>();
			for(int i = 0; i < slots.size(); i++){
				Slot sl = slots.get(i);
				mSlots.put(sl.getID(), sl);
			}
			for(int i = 0; i < rooms.size(); i++){
				Room r = rooms.get(i);
				mRooms.put(r.getID(), r);
			}
			while(rs.next()){
				int id = rs.getInt("ID");
				int studentID = rs.getInt("StudentID");
				String studentName = rs.getString("StudentName");
				int supervisorID = rs.getInt("SupervisorID");
				int examiner1ID = rs.getInt("Examiner1ID");
				String examiner1Name = rs.getString("Examiner1Name");
				int examiner2ID = rs.getInt("Examiner2ID");
				String examiner2Name = rs.getString("Examiner2Name");
				int presidentID = rs.getInt("PresidentID");
				String presidentName = rs.getString("Presidentname");
				int secretaryID = rs.getInt("SecretaryID");
				String secretaryName = rs.getString("SecretaryName");
				int memberID = rs.getInt("MemberID");
				String memberName = rs.getString("MemberName");
				int slot = rs.getInt("Slot");
				int room = rs.getInt("Room");
				int id_data_set = rs.getInt("SessionID");
				String thesis_title = rs.getString("ThesisTitle");
				JuryInfo jr = new JuryInfo(id, studentName, thesis_title, "supervisor_null", examiner1Name,
						examiner2Name, presidentName, secretaryName, memberName,id_data_set);
				
				jr.setExaminerId1(examiner1ID);
				jr.setExaminerId2(examiner2ID);
				jr.setPresidentId(presidentID);
				jr.setSecretaryId(secretaryID);
				jr.setAdditionalMemberId(memberID);
				
				jr.setStudentID(studentID);
				jr.setSlotId(slot);
				if(slot > 0) jr.setSlotDescription(mSlots.get(slot).getDescription());else jr.setSlotDescription("-");
				jr.setRoomId(room);
				if(room > 0) jr.setRoomName(mRooms.get(room).getDescription());else jr.setRoomName("-");
				
				
				if(filterBy.compareTo("All") != 0){
					if(mDepart.get(supervisorID).compareTo(filterBy) == 0)
						juries.add(jr);
				}
				else{
					juries.add(jr);
				}
			}
			
			
			//out.println("server return results");
			
			int n = juries.size();
			JuryInfo[] s_jury = new JuryInfo[n];
			for(int i = 0; i < n; i++){
				s_jury[i] = juries.get(i);
			}
			if(sortBy.compareTo("Room-Slot")==0){
				for(int i = 0; i < n-1; i++){
					for(int j = i+1; j < n; j++){
						if(s_jury[i].getRoomId() > s_jury[j].getRoomId() ||
							s_jury[i].getRoomId() == s_jury[j].getRoomId() && s_jury[i].getSlotId() > s_jury[j].getSlotId()	){
							JuryInfo t = s_jury[i]; s_jury[i] = s_jury[j]; s_jury[j] = t;
						}
					}
				}
			}else if(sortBy.compareTo("Slot-Room")==0){
				for(int i = 0; i < n-1; i++){
					for(int j = i+1; j < n; j++){
						if(s_jury[i].getSlotId() > s_jury[j].getSlotId() ||
							s_jury[i].getSlotId() == s_jury[j].getSlotId() && s_jury[i].getRoomId() > s_jury[j].getRoomId()	){
							JuryInfo t = s_jury[i]; s_jury[i] = s_jury[j]; s_jury[j] = t;
						}
					}
				}				
			}
			juries.clear();
			for(int i = 0; i < n; i++){
				juries.add(s_jury[i]);
			}
			System.out.println("get students successfully");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(cnn,stat,rs);
		}
		return juries;
	}
	
	public static Vector<JuryInfo> getListTempJuryInfo(String sortBy, String filterBy){
		System.out.println("Utility::getListTempJuryInfo, sortBy " + sortBy + " filterBy " + filterBy);
		Vector<JuryInfo> juries = new Vector<JuryInfo>();
		Connection cnn = null;
		Statement stat = null;
		PreparedStatement preparedStat = null;
		ResultSet rs = null;

	
		try{
			

			Class.forName("com.mysql.jdbc.Driver");
	
			cnn = DriverManager.getConnection(Configure.cnnStr);
			stat = cnn.createStatement();
			

			//String sql = "SELECT S.ID as ID, S.Title as ThesisTitle, ST.ID as StudentID, ST.StudentName, P1.Name as Examiner1Name, P0.ID as SupervisorID, P1.ID as Examiner1ID, " +
			//" P2.Name as Examiner2Name, P2.ID as Examiner2ID, P3.Name as PresidentName, P3.ID as PresidentID, " + 
			//		"P4.Name as SecretaryName, P4.ID as SecretaryID, P5.Name as MemberName, P5.ID as MemberID, S.Slot as Slot, S.Room as Room, S.SessionID as SessionID FROM " + 
			//" student_defense as S, supervise_students as ST, professors as P0, professors as P1, professors as P2, professors as P3, " + 
			//		" professors as P4, professors as P5 where S.Supervisor = P0.ID and S.examiner1 = P1.ID and S.examiner2 = P2.ID and S.president = P3.ID and " + 
			//" S.secretary = P4.ID and S.additionalMember = P5.ID  and ST.ID = S.StudentID";
			String sql = "select * from " + Configure.tblStudentDefense;
			
			if(sortBy.compareTo("StudentID") == 0){
				sql += " order by StudentID";
			}else if(sortBy.compareTo("Slot")==0){
				sql += " order by Slot";
			}else if(sortBy.compareTo("Room")==0){
				sql += " order by Room";
			}
			rs = stat.executeQuery(sql);
			
			Vector<Slot> slots = getSlots();
			Vector<Room> rooms = getRooms();
			Vector<Teacher> teachers = getTeachers();
			Vector<Student> students = getStudents();
			Vector<DefenseSession> sessions = getDefenseSessions(1);
			HashSet<Integer> active_sessions = new HashSet<Integer>();
			for(int i = 0; i < sessions.size(); i++){
				active_sessions.add(sessions.get(i).getID());
			}
			
			HashMap<Integer, String> mStudent = new HashMap<Integer, String>();
			for(int i = 0; i < students.size(); i++){
				Student std = students.get(i);
				mStudent.put(std.getID(), std.getName());
			}
			HashMap<Integer, String> mDepart = new HashMap<Integer,String>();
			HashMap<Integer, String> mTeacher = new HashMap<Integer, String>();
			for(int i = 0; i < teachers.size(); i++){
				Teacher t = teachers.get(i);
				mDepart.put(t.getID(), t.getDepartment());
				mTeacher.put(t.getID(), t.getName());
			}
			
			HashMap<Integer, Slot> mSlots = new HashMap<Integer, Slot>();
			HashMap<Integer, Room> mRooms = new HashMap<Integer,Room>();
			for(int i = 0; i < slots.size(); i++){
				Slot sl = slots.get(i);
				mSlots.put(sl.getID(), sl);
			}
			for(int i = 0; i < rooms.size(); i++){
				Room r = rooms.get(i);
				mRooms.put(r.getID(), r);
			}
			while(rs.next()){
				int id = rs.getInt("ID");
				int studentID = rs.getInt("StudentID");
				//String studentName = rs.getString("StudentName");
				int supervisorID = rs.getInt("Supervisor");
				int examiner1ID = rs.getInt("Examiner1");
				//String examiner1Name = rs.getString("Examiner1Name");
				int examiner2ID = rs.getInt("Examiner2");
				//String examiner2Name = rs.getString("Examiner2Name");
				int presidentID = rs.getInt("President");
				//String presidentName = rs.getString("Presidentname");
				int secretaryID = rs.getInt("Secretary");
				//String secretaryName = rs.getString("SecretaryName");
				int memberID = rs.getInt("AdditionalMember");
				//String memberName = rs.getString("MemberName");
				int slot = rs.getInt("Slot");
				int room = rs.getInt("Room");
				int id_data_set = rs.getInt("SessionID");
				String thesis_title = rs.getString("Title");
				
				/*
				JuryInfo jr = new JuryInfo(id, studentName, thesis_title, "supervisor_null", examiner1Name,
						examiner2Name, presidentName, secretaryName, memberName,id_data_set);
				
				jr.setExaminerId1(examiner1ID);
				jr.setExaminerId2(examiner2ID);
				jr.setPresidentId(presidentID);
				jr.setSecretaryId(secretaryID);
				jr.setAdditionalMemberId(memberID);
				*/
				JuryInfo jr = new JuryInfo(id,studentID,thesis_title,supervisorID,examiner1ID,examiner2ID,presidentID,secretaryID,memberID,id_data_set);
				//jr.setStudentID(studentID);
				
				jr.setStudentName(mStudent.get(studentID));
				jr.setSupervisorName(mTeacher.get(supervisorID));
				jr.setExaminerName1(mTeacher.get(examiner1ID));
				jr.setExaminerName2(mTeacher.get(examiner2ID));
				jr.setPresidentName(mTeacher.get(presidentID));
				jr.setSecretaryName(mTeacher.get(secretaryID));
				jr.setAdditionalMemberName(mTeacher.get(memberID));
				
				jr.setSlotId(slot);
				if(slot > 0) jr.setSlotDescription(mSlots.get(slot).getDescription());else jr.setSlotDescription("-");
				jr.setRoomId(room);
				if(room > 0) jr.setRoomName(mRooms.get(room).getDescription());else jr.setRoomName("-");
				
				System.out.println("Utility::getListJuryInfo, active_sessions.sz = " + 
				active_sessions.size() + " id_data_set = " + id_data_set);
				if(!active_sessions.contains(id_data_set)) continue;
				
				if(filterBy.compareTo("All") != 0){
					if(mDepart.get(supervisorID).compareTo(filterBy) == 0)
						juries.add(jr);
				}
				else{
					juries.add(jr);
				}
			}
			
			
			//out.println("server return results");
			
			int n = juries.size();
			JuryInfo[] s_jury = new JuryInfo[n];
			for(int i = 0; i < n; i++){
				s_jury[i] = juries.get(i);
			}
			if(sortBy.compareTo("Room-Slot")==0){
				for(int i = 0; i < n-1; i++){
					for(int j = i+1; j < n; j++){
						if(s_jury[i].getRoomId() > s_jury[j].getRoomId() ||
							s_jury[i].getRoomId() == s_jury[j].getRoomId() && s_jury[i].getSlotId() > s_jury[j].getSlotId()	){
							JuryInfo t = s_jury[i]; s_jury[i] = s_jury[j]; s_jury[j] = t;
						}
					}
				}
			}else if(sortBy.compareTo("Slot-Room")==0){
				for(int i = 0; i < n-1; i++){
					for(int j = i+1; j < n; j++){
						if(s_jury[i].getSlotId() > s_jury[j].getSlotId() ||
							s_jury[i].getSlotId() == s_jury[j].getSlotId() && s_jury[i].getRoomId() > s_jury[j].getRoomId()	){
							JuryInfo t = s_jury[i]; s_jury[i] = s_jury[j]; s_jury[j] = t;
						}
					}
				}				
			}
			juries.clear();
			for(int i = 0; i < n; i++){
				juries.add(s_jury[i]);
			}
			System.out.println("get students successfully");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(cnn,stat,rs);
		}
		return juries;
	}
	
	public static Vector<JuryInfo> getListJuryInfo(String sortBy, String filterBy, int sessionID){
		Vector<JuryInfo> jury = getListJuryInfo(sortBy, filterBy);
		Vector<JuryInfo> r_jury = new Vector<JuryInfo>();
		for(int i = 0; i < jury.size(); i++){
			JuryInfo J = jury.get(i);
			if(J.getIdDataSet() == sessionID)
				r_jury.add(J);
		}
		return r_jury;
	}
	public static Vector<JuryInfo> getListJuryInfo(String sortBy, String filterBy){
		//System.out.println("Utility::getListJuryInfo, sortBy = " + sortBy + " filterBy = " + filterBy);
		Vector<JuryInfo> juries = new Vector<JuryInfo>();
		Connection cnn = null;
		Statement stat = null;
		PreparedStatement preparedStat = null;
		ResultSet rs = null;

	
		try{
			

			Class.forName("com.mysql.jdbc.Driver");
	
			cnn = DriverManager.getConnection(Configure.cnnStr);
			stat = cnn.createStatement();
			

			//String sql = "SELECT S.ID as ID, S.Title as ThesisTitle, ST.ID as StudentID, ST.StudentName, P1.Name as Examiner1Name, P0.ID as SupervisorID, P1.ID as Examiner1ID, " +
			//" P2.Name as Examiner2Name, P2.ID as Examiner2ID, P3.Name as PresidentName, P3.ID as PresidentID, " + 
			//		"P4.Name as SecretaryName, P4.ID as SecretaryID, P5.Name as MemberName, P5.ID as MemberID, S.Slot as Slot, S.Room as Room, S.SessionID as SessionID FROM " + 
			//" student_defense as S, supervise_students as ST, professors as P0, professors as P1, professors as P2, professors as P3, " + 
			//		" professors as P4, professors as P5 where S.Supervisor = P0.ID and S.examiner1 = P1.ID and S.examiner2 = P2.ID and S.president = P3.ID and " + 
			//" S.secretary = P4.ID and S.additionalMember = P5.ID  and ST.ID = S.StudentID";
			String sql = "select * from " + Configure.tblStudentDefense;
			
			if(sortBy.compareTo("StudentID") == 0){
				sql += " order by StudentID";
			}else if(sortBy.compareTo("Slot")==0){
				sql += " order by Slot";
			}else if(sortBy.compareTo("Room")==0){
				sql += " order by Room";
			}else if(sortBy.compareTo("Supervisor")==0){
				sql += " order by Supervisor";
			}
			rs = stat.executeQuery(sql);
			
			Vector<Slot> slots = getSlots();
			Vector<Room> rooms = getRooms();
			Vector<Teacher> teachers = getTeachers();
			Vector<Student> students = getStudents();
			Vector<DefenseSession> sessions = getDefenseSessions(1);
			HashSet<Integer> active_sessions = new HashSet<Integer>();
			for(int i = 0; i < sessions.size(); i++){
				active_sessions.add(sessions.get(i).getID());
				System.out.println("Utility::getListJury, active session " + sessions.get(i).getID());
			}
			
			HashMap<Integer, String> mStudent = new HashMap<Integer, String>();
			for(int i = 0; i < students.size(); i++){
				Student std = students.get(i);
				mStudent.put(std.getID(), std.getName());
			}
			HashMap<Integer, String> mDepart = new HashMap<Integer,String>();
			HashMap<Integer, String> mTeacher = new HashMap<Integer, String>();
			for(int i = 0; i < teachers.size(); i++){
				Teacher t = teachers.get(i);
				mDepart.put(t.getID(), t.getDepartment());
				mTeacher.put(t.getID(), t.getName());
				//System.out.println("Utility::getListJuryInfo, mTeacher.put(" + t.getID() + "," + t.getName());
			}
			
			HashMap<Integer, Slot> mSlots = new HashMap<Integer, Slot>();
			HashMap<Integer, Room> mRooms = new HashMap<Integer,Room>();
			for(int i = 0; i < slots.size(); i++){
				Slot sl = slots.get(i);
				mSlots.put(sl.getID(), sl);
				//System.out.println("Utility::getListJuryInfo, mSlot.put(" + sl.getID() + ")");
				
			}
			//System.out.println("Utility::getListJuryInfo, rooms.sz = " + rooms.size());
			
			for(int i = 0; i < rooms.size(); i++){
				Room r = rooms.get(i);
				mRooms.put(r.getID(), r);
				//System.out.println("Utility::getListJuryInfo, mRooms.put(" + r.getID() + ")");
			}
			while(rs.next()){
				int id = rs.getInt("ID");
				int studentID = rs.getInt("StudentID");
				//String studentName = rs.getString("StudentName");
				int supervisorID = rs.getInt("Supervisor");
				int examiner1ID = rs.getInt("Examiner1");
				//String examiner1Name = rs.getString("Examiner1Name");
				int examiner2ID = rs.getInt("Examiner2");
				//String examiner2Name = rs.getString("Examiner2Name");
				int presidentID = rs.getInt("President");
				//String presidentName = rs.getString("Presidentname");
				int secretaryID = rs.getInt("Secretary");
				//String secretaryName = rs.getString("SecretaryName");
				int memberID = rs.getInt("AdditionalMember");
				//String memberName = rs.getString("MemberName");
				int slot = rs.getInt("Slot");
				int room = rs.getInt("Room");
				int id_data_set = rs.getInt("SessionID");
				String thesis_title = rs.getString("Title");
				
				//System.out.println("Utility::getListJuryInfo, studentID = " + studentID + " roomID = " + room + " slotID = " + slot);
				
				/*
				JuryInfo jr = new JuryInfo(id, studentName, thesis_title, "supervisor_null", examiner1Name,
						examiner2Name, presidentName, secretaryName, memberName,id_data_set);
				
				jr.setExaminerId1(examiner1ID);
				jr.setExaminerId2(examiner2ID);
				jr.setPresidentId(presidentID);
				jr.setSecretaryId(secretaryID);
				jr.setAdditionalMemberId(memberID);
				*/
				JuryInfo jr = new JuryInfo(id,studentID,thesis_title,supervisorID,examiner1ID,examiner2ID,presidentID,secretaryID,memberID,id_data_set);
				//jr.setStudentID(studentID);
				
				jr.setStudentName(mStudent.get(studentID));
				jr.setSupervisorName(mTeacher.get(supervisorID));
				jr.setExaminerName1(mTeacher.get(examiner1ID));
				jr.setExaminerName2(mTeacher.get(examiner2ID));
				jr.setPresidentName(mTeacher.get(presidentID));
				jr.setSecretaryName(mTeacher.get(secretaryID));
				jr.setAdditionalMemberName(mTeacher.get(memberID));
				
				if(mSlots.get(slot) == null) slot = 0;
				jr.setSlotId(slot);
				if(slot > 0 && mSlots.get(slot) != null) jr.setSlotDescription(mSlots.get(slot).getDescription());else jr.setSlotDescription("-");
				if(mRooms.get(room) == null) room = 0;
				jr.setRoomId(room);
				if(room > 0 && mRooms.get(room) != null) jr.setRoomName(mRooms.get(room).getDescription());else jr.setRoomName("-");
				
				//System.out.println("Utility::getListJuryInfo, active_sessions.sz = " + 
				//active_sessions.size() + " id_data_set = " + id_data_set);
				if(!active_sessions.contains(id_data_set)) continue;
				
				if(filterBy.compareTo("All") != 0){
					if(mDepart.get(supervisorID).compareTo(filterBy) == 0)
						juries.add(jr);
					else if(jr.getRoomName().compareTo(filterBy) == 0){
						juries.add(jr);
					}
				}
				else{
					juries.add(jr);
				}
			}
			
			
			//out.println("server return results");
			
			int n = juries.size();
			JuryInfo[] s_jury = new JuryInfo[n];
			for(int i = 0; i < n; i++){
				s_jury[i] = juries.get(i);
				Slot sl = mSlots.get(s_jury[i].getSlotId());
				int sli = 0;
				if(sl != null) sli = sl.getSlotIndex();
				s_jury[i].setSlotIndex(sli);
			}
			if(sortBy.compareTo("Room-Slot")==0){
				for(int i = 0; i < n-1; i++){
					for(int j = i+1; j < n; j++){
						//if(s_jury[i].getRoomId() > s_jury[j].getRoomId() ||
							//s_jury[i].getRoomId() == s_jury[j].getRoomId() && s_jury[i].getSlotId() > s_jury[j].getSlotId()	){
						if(s_jury[i].getRoomId() > s_jury[j].getRoomId() ||
								s_jury[i].getRoomId() == s_jury[j].getRoomId() && s_jury[i].getSlotIndex() > s_jury[j].getSlotIndex()	){
								
							JuryInfo t = s_jury[i]; s_jury[i] = s_jury[j]; s_jury[j] = t;
						}
					}
				}
			}else if(sortBy.compareTo("Slot-Room")==0){
				for(int i = 0; i < n-1; i++){
					for(int j = i+1; j < n; j++){
						//if(s_jury[i].getSlotId() > s_jury[j].getSlotId() ||
							//s_jury[i].getSlotId() == s_jury[j].getSlotId() && s_jury[i].getRoomId() > s_jury[j].getRoomId()	){
						if(s_jury[i].getSlotIndex() > s_jury[j].getSlotIndex() ||
							s_jury[i].getSlotIndex() == s_jury[j].getSlotIndex() && s_jury[i].getRoomId() > s_jury[j].getRoomId()	){
								JuryInfo t = s_jury[i]; s_jury[i] = s_jury[j]; s_jury[j] = t;
						}
					}
				}				
			}
			juries.clear();
			for(int i = 0; i < n; i++){
				juries.add(s_jury[i]);
			}
			//System.out.println("get students successfully");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(cnn,stat,rs);
		}
		return juries;
	}

	public static Vector<JuryInfo> getListJuryInfo(){
		//System.out.println("Utility::getListJuryInfo, sortBy = " + sortBy + " filterBy = " + filterBy);
		Vector<JuryInfo> juries = new Vector<JuryInfo>();
		Connection cnn = null;
		Statement stat = null;
		PreparedStatement preparedStat = null;
		ResultSet rs = null;

	
		try{
			

			Class.forName("com.mysql.jdbc.Driver");
	
			cnn = DriverManager.getConnection(Configure.cnnStr);
			stat = cnn.createStatement();
			

			//String sql = "SELECT S.ID as ID, S.Title as ThesisTitle, ST.ID as StudentID, ST.StudentName, P1.Name as Examiner1Name, P0.ID as SupervisorID, P1.ID as Examiner1ID, " +
			//" P2.Name as Examiner2Name, P2.ID as Examiner2ID, P3.Name as PresidentName, P3.ID as PresidentID, " + 
			//		"P4.Name as SecretaryName, P4.ID as SecretaryID, P5.Name as MemberName, P5.ID as MemberID, S.Slot as Slot, S.Room as Room, S.SessionID as SessionID FROM " + 
			//" student_defense as S, supervise_students as ST, professors as P0, professors as P1, professors as P2, professors as P3, " + 
			//		" professors as P4, professors as P5 where S.Supervisor = P0.ID and S.examiner1 = P1.ID and S.examiner2 = P2.ID and S.president = P3.ID and " + 
			//" S.secretary = P4.ID and S.additionalMember = P5.ID  and ST.ID = S.StudentID";
			String sql = "select * from " + Configure.tblStudentDefense;
			
			rs = stat.executeQuery(sql);
			
			Vector<Slot> slots = getSlots();
			Vector<Room> rooms = getRooms();
			Vector<Teacher> teachers = getTeachers();
			Vector<Student> students = getStudents();
			Vector<DefenseSession> sessions = getDefenseSessions(1);
			HashSet<Integer> active_sessions = new HashSet<Integer>();
			for(int i = 0; i < sessions.size(); i++){
				active_sessions.add(sessions.get(i).getID());
				System.out.println("Utility::getListJury, active session " + sessions.get(i).getID());
			}
			
			HashMap<Integer, String> mStudent = new HashMap<Integer, String>();
			for(int i = 0; i < students.size(); i++){
				Student std = students.get(i);
				mStudent.put(std.getID(), std.getName());
			}
			HashMap<Integer, String> mDepart = new HashMap<Integer,String>();
			HashMap<Integer, String> mTeacher = new HashMap<Integer, String>();
			for(int i = 0; i < teachers.size(); i++){
				Teacher t = teachers.get(i);
				mDepart.put(t.getID(), t.getDepartment());
				mTeacher.put(t.getID(), t.getName());
				//System.out.println("Utility::getListJuryInfo, mTeacher.put(" + t.getID() + "," + t.getName());
			}
			
			HashMap<Integer, Slot> mSlots = new HashMap<Integer, Slot>();
			HashMap<Integer, Room> mRooms = new HashMap<Integer,Room>();
			for(int i = 0; i < slots.size(); i++){
				Slot sl = slots.get(i);
				mSlots.put(sl.getID(), sl);
				//System.out.println("Utility::getListJuryInfo, mSlot.put(" + sl.getID() + ")");
				
			}
			//System.out.println("Utility::getListJuryInfo, rooms.sz = " + rooms.size());
			
			for(int i = 0; i < rooms.size(); i++){
				Room r = rooms.get(i);
				mRooms.put(r.getID(), r);
				//System.out.println("Utility::getListJuryInfo, mRooms.put(" + r.getID() + ")");
			}
			while(rs.next()){
				int id = rs.getInt("ID");
				int studentID = rs.getInt("StudentID");
				//String studentName = rs.getString("StudentName");
				int supervisorID = rs.getInt("Supervisor");
				int examiner1ID = rs.getInt("Examiner1");
				//String examiner1Name = rs.getString("Examiner1Name");
				int examiner2ID = rs.getInt("Examiner2");
				//String examiner2Name = rs.getString("Examiner2Name");
				int presidentID = rs.getInt("President");
				//String presidentName = rs.getString("Presidentname");
				int secretaryID = rs.getInt("Secretary");
				//String secretaryName = rs.getString("SecretaryName");
				int memberID = rs.getInt("AdditionalMember");
				//String memberName = rs.getString("MemberName");
				int slot = rs.getInt("Slot");
				int room = rs.getInt("Room");
				int id_data_set = rs.getInt("SessionID");
				String thesis_title = rs.getString("Title");
				
				//System.out.println("Utility::getListJuryInfo, studentID = " + studentID + " roomID = " + room + " slotID = " + slot);
				
				/*
				JuryInfo jr = new JuryInfo(id, studentName, thesis_title, "supervisor_null", examiner1Name,
						examiner2Name, presidentName, secretaryName, memberName,id_data_set);
				
				jr.setExaminerId1(examiner1ID);
				jr.setExaminerId2(examiner2ID);
				jr.setPresidentId(presidentID);
				jr.setSecretaryId(secretaryID);
				jr.setAdditionalMemberId(memberID);
				*/
				JuryInfo jr = new JuryInfo(id,studentID,thesis_title,supervisorID,examiner1ID,examiner2ID,presidentID,secretaryID,memberID,id_data_set);
				//jr.setStudentID(studentID);
				
				jr.setStudentName(mStudent.get(studentID));
				jr.setSupervisorName(mTeacher.get(supervisorID));
				jr.setExaminerName1(mTeacher.get(examiner1ID));
				jr.setExaminerName2(mTeacher.get(examiner2ID));
				jr.setPresidentName(mTeacher.get(presidentID));
				jr.setSecretaryName(mTeacher.get(secretaryID));
				jr.setAdditionalMemberName(mTeacher.get(memberID));
				
				if(mSlots.get(slot) == null) slot = 0;
				jr.setSlotId(slot);
				if(slot > 0 && mSlots.get(slot) != null) jr.setSlotDescription(mSlots.get(slot).getDescription());else jr.setSlotDescription("-");
				if(mRooms.get(room) == null) room = 0;
				jr.setRoomId(room);
				if(room > 0 && mRooms.get(room) != null) jr.setRoomName(mRooms.get(room).getDescription());else jr.setRoomName("-");
				
				//System.out.println("Utility::getListJuryInfo, active_sessions.sz = " + 
				//active_sessions.size() + " id_data_set = " + id_data_set);
				if(!active_sessions.contains(id_data_set)) continue;
				
				juries.add(jr);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(cnn,stat,rs);
		}
		return juries;
	}
	
	public static void saveJury(Vector<JuryInfo> listJury){
		Connection cnn = null;
		Statement stat = null;
		PreparedStatement preparedStat = null;
		ResultSet rs = null;
		try{
			if(cnn == null){
				Class.forName("com.mysql.jdbc.Driver");
				cnn = DriverManager.getConnection(Configure.cnnStr);
				for(int i = 0; i < listJury.size(); i++){
					JuryInfo jr = listJury.get(i);
					String sql = "update " + Configure.tblStudentDefense + " set Slot = ? , Room = ? , Title = ?, Supervisor = ?, Examiner1 = ?," + 
					" Examiner2 = ?, President = ?, Secretary = ?, AdditionalMember = ? where StudentID = ?";
					String sql_print = "update " + Configure.tblStudentDefense + " set Slot = " + jr.getSlotId() + " , Room = " + jr.getRoomId() + " where StudentID = " + jr.getStudentID();
					preparedStat = cnn.prepareStatement(sql);
					preparedStat.setInt(1, jr.getSlotId());
					preparedStat.setInt(2, jr.getRoomId());
					preparedStat.setString(3, jr.getTitle());
					preparedStat.setInt(4, jr.getSupervisorId());
					preparedStat.setInt(5, jr.getExaminerId1());
					preparedStat.setInt(6, jr.getExaminerId2());
					preparedStat.setInt(7, jr.getPresidentId());
					preparedStat.setInt(8, jr.getSecretaryId());
					preparedStat.setInt(9, jr.getAdditionalMemberId());
					preparedStat.setInt(10, jr.getStudentID());
					preparedStat.executeUpdate();
					System.out.println("Update student_defense " + jr.getStudentName() + " successfully with sql = " + sql_print);
					//stat.executeQuery(sql);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(cnn,stat,rs);
		}		
	}
	public static void addJury(int studentID, String thesis_title, int supervisorID, int examiner1ID, int examiner2ID,
			int presidentID, int secretaryID, int memberID, int slotID, int roomID, int sessionID){
		Connection cnn = null;
		Statement stat = null;
		PreparedStatement preparedStat = null;
		ResultSet rs = null;
		try{
			if(cnn == null){
				Class.forName("com.mysql.jdbc.Driver");
				cnn = DriverManager.getConnection(Configure.cnnStr);
				preparedStat = cnn.prepareStatement("insert into " + Configure.tblStudentDefense + "(StudentID,Title,Supervisor," +
						"Examiner1,Examiner2,President," +
						"Secretary,AdditionalMember, Slot, Room, SessionID) " + " values(?,?,?,?,?,?,?,?,?,?,?)");
				preparedStat.setInt(1, studentID);
				preparedStat.setString(2, thesis_title);
				preparedStat.setInt(3, supervisorID);
				preparedStat.setInt(4, examiner1ID);
				preparedStat.setInt(5, examiner2ID);
				preparedStat.setInt(6, presidentID);
				preparedStat.setInt(7, secretaryID);
				preparedStat.setInt(8, memberID);	
				preparedStat.setInt(9, slotID);
				preparedStat.setInt(10, roomID);
				preparedStat.setInt(11, sessionID);	
				preparedStat.executeUpdate();
				System.out.println("insert into table " + Configure.tblStudentDefense + " successfully");
				//stat.executeQuery(sql);
				close(cnn,stat,rs);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(cnn, stat,rs);
		}		
	}

	public static void updateJury(int id, int studentID, String thesis_title, int supervisorID, int examiner1ID, int examiner2ID,
			int presidentID, int secretaryID, int memberID, int slotID, int roomID, int sessionID){
		Connection cnn = null;
		Statement stat = null;
		PreparedStatement preparedStat = null;
		ResultSet rs = null;
		try{
			if(cnn == null){
				Class.forName("com.mysql.jdbc.Driver");
				cnn = DriverManager.getConnection(Configure.cnnStr);
				preparedStat = cnn.prepareStatement("update " + Configure.tblStudentDefense + " set StudentID = ?" +
				", Title = ?, Supervisor = ?," + "Examiner1 = ?, Examiner2 = ?, President = ?," +
						"Secretary = ?, AdditionalMember = ?, Slot = ?, Room = ?, SessionID = ? where ID = ?");
				preparedStat.setInt(1, studentID);
				preparedStat.setString(2, thesis_title);
				preparedStat.setInt(3, supervisorID);
				preparedStat.setInt(4, examiner1ID);
				preparedStat.setInt(5, examiner2ID);
				preparedStat.setInt(6, presidentID);
				preparedStat.setInt(7, secretaryID);
				preparedStat.setInt(8, memberID);	
				preparedStat.setInt(9, slotID);
				preparedStat.setInt(10, roomID);
				preparedStat.setInt(11, sessionID);	
				preparedStat.setInt(12, id);
				preparedStat.executeUpdate();
				System.out.println("update table " + Configure.tblStudentDefense + " successfully");
				//stat.executeQuery(sql);
				close(cnn,stat,rs);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(cnn, stat,rs);
		}		
	}

	public static int getIDStudentDefenseSubjectCategory(int studentID, int subjectCategoryID){
		Connection cnn = null;
		Statement stat = null;
		PreparedStatement preparedStat = null;
		ResultSet rs = null;
		int id = -1;
		try{
			if(cnn == null){
				Class.forName("com.mysql.jdbc.Driver");
				cnn = DriverManager.getConnection(Configure.cnnStr);
				stat = cnn.createStatement();
				String sql = "select * from " + Configure.tblStudentDefenseSubjectCategory + " where StudentID = " + studentID + " and SubjectCategoryID = " + subjectCategoryID;
				rs = stat.executeQuery(sql);
				
				while(rs.next()){
					id = rs.getInt("ID");
					break;
				}
				close(cnn,stat,rs);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(cnn, stat,rs);
		}
		return id;
	}
	public static int getIDProfessorSubjectCategory(int professorID, int subjectCategoryID){
		Connection cnn = null;
		Statement stat = null;
		PreparedStatement preparedStat = null;
		ResultSet rs = null;
		int id = -1;
		try{
			if(cnn == null){
				Class.forName("com.mysql.jdbc.Driver");
				cnn = DriverManager.getConnection(Configure.cnnStr);
				stat = cnn.createStatement();
				String sql = "select * from " + Configure.tblProfessorSubjectCategory + 
						" where ProfessorID = " + professorID + " and SubjectCategoryID = " + 
						subjectCategoryID;
				rs = stat.executeQuery(sql);
				
				while(rs.next()){
					id = rs.getInt("ID");
					break;
				}
				close(cnn,stat,rs);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(cnn, stat,rs);
		}
		return id;
	}

	public static void addStudentDefenseSubjectCategory(int studentID, int subjectCategoryID, int matchScore){
		Connection cnn = null;
		Statement stat = null;
		PreparedStatement preparedStat = null;
		ResultSet rs = null;
		try{
			if(cnn == null){
				Class.forName("com.mysql.jdbc.Driver");
				cnn = DriverManager.getConnection(Configure.cnnStr);
				preparedStat = cnn.prepareStatement("insert into " + Configure.tblStudentDefenseSubjectCategory + "(StudentID, SubjectCategoryID, MatchScore) " + " values(?,?,?)");
				preparedStat.setInt(1, studentID);
				preparedStat.setInt(2, subjectCategoryID);
				preparedStat.setInt(3, matchScore);
				preparedStat.executeUpdate();
				System.out.println("insert into table " + Configure.tblStudentDefenseSubjectCategory + " successfully");
				//stat.executeQuery(sql);
				close(cnn,stat,rs);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(cnn, stat,rs);
		}		
	}
	public static void addProfessorSubjectCategory(int professorID, int subjectCategoryID, int matchScore){
		Connection cnn = null;
		Statement stat = null;
		PreparedStatement preparedStat = null;
		ResultSet rs = null;
		try{
			if(cnn == null){
				Class.forName("com.mysql.jdbc.Driver");
				cnn = DriverManager.getConnection(Configure.cnnStr);
				preparedStat = cnn.prepareStatement("insert into " + Configure.tblProfessorSubjectCategory + "(ProfessorID, SubjectCategoryID, MatchScore) " + " values(?,?,?)");
				preparedStat.setInt(1, professorID);
				preparedStat.setInt(2, subjectCategoryID);
				preparedStat.setInt(3, matchScore);
				preparedStat.executeUpdate();
				//System.out.println("insert into table " + Configure.tblStudentDefenseSubjectCategory + " successfully");
				//stat.executeQuery(sql);
				close(cnn,stat,rs);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(cnn, stat,rs);
		}		
	}

	public static void updateStudentDefenseSubjectCategory(int id, int studentID, int subjectCategoryID, int matchScore){
		Connection cnn = null;
		Statement stat = null;
		PreparedStatement preparedStat = null;
		ResultSet rs = null;
		try{
			if(cnn == null){
				Class.forName("com.mysql.jdbc.Driver");
				cnn = DriverManager.getConnection(Configure.cnnStr);
				preparedStat = cnn.prepareStatement("update " + Configure.tblStudentDefenseSubjectCategory + "" +
						" set StudentID = ?, SubjectCategoryID = ?, MatchScore = ? where ID = ?");
				preparedStat.setInt(1, studentID);
				preparedStat.setInt(2, subjectCategoryID);
				preparedStat.setInt(3, matchScore);
				preparedStat.setInt(4, id);
				preparedStat.executeUpdate();
				System.out.println("insert into table " + Configure.tblStudentDefenseSubjectCategory + " successfully");
				//stat.executeQuery(sql);
				close(cnn,stat,rs);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(cnn, stat,rs);
		}		
	}

	public static void updateProfessorSubjectCategory(int id, int professorID, int subjectCategoryID, int matchScore){
		Connection cnn = null;
		Statement stat = null;
		PreparedStatement preparedStat = null;
		ResultSet rs = null;
		try{
			if(cnn == null){
				Class.forName("com.mysql.jdbc.Driver");
				cnn = DriverManager.getConnection(Configure.cnnStr);
				preparedStat = cnn.prepareStatement("update " + Configure.tblProfessorSubjectCategory + "" +
						" set ProfessorID = ?, SubjectCategoryID = ?, MatchScore = ? where ID = ?");
				preparedStat.setInt(1, professorID);
				preparedStat.setInt(2, subjectCategoryID);
				preparedStat.setInt(3, matchScore);
				preparedStat.setInt(4, id);
				preparedStat.executeUpdate();
				//System.out.println("insert into table " + Configure.tblStudentDefenseSubjectCategory + " successfully");
				//stat.executeQuery(sql);
				close(cnn,stat,rs);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(cnn, stat,rs);
		}		
	}

	public static void addRoom(String roomName, int sessionID){
		Connection cnn = null;
		Statement stat = null;
		PreparedStatement preparedStat = null;
		ResultSet rs = null;
		try{
			if(cnn == null){
				Class.forName("com.mysql.jdbc.Driver");
				cnn = DriverManager.getConnection(Configure.cnnStr);
				preparedStat = cnn.prepareStatement("insert into " + Configure.tblRoom + "(Description, DefenseSessionID) " + " values(?,?)");
				preparedStat.setString(1, roomName);
				preparedStat.setInt(2, sessionID);	
				preparedStat.executeUpdate();
				System.out.println("insert into table " + Configure.tblRoom + " successfully");
				//stat.executeQuery(sql);
				close(cnn,stat,rs);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(cnn, stat,rs);
		}		
	}
	
	public static void addSlot(int slotIndex, String description, int roomID){
		Connection cnn = null;
		Statement stat = null;
		PreparedStatement preparedStat = null;
		ResultSet rs = null;
		try{
			if(cnn == null){
				Class.forName("com.mysql.jdbc.Driver");
				cnn = DriverManager.getConnection(Configure.cnnStr);
				preparedStat = cnn.prepareStatement("insert into " + Configure.tblSlot + "(SlotIndex, Description, RoomID) " + " values(?,?,?)");
				preparedStat.setInt(1, slotIndex);
				preparedStat.setString(2, description);
				preparedStat.setInt(3, roomID);	
				preparedStat.executeUpdate();
				System.out.println("insert into table " + Configure.tblSlot + " successfully");
				//stat.executeQuery(sql);
				close(cnn,stat,rs);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(cnn, stat,rs);
		}		
	}

	public static void updateJury(int studentID, String thesis_title, int supervisorID, int examiner1ID, int examiner2ID,
			int presidentID, int secretaryID, int memberID){
		Connection cnn = null;
		Statement stat = null;
		PreparedStatement preparedStat = null;
		ResultSet rs = null;
		try{
			if(cnn == null){
				Class.forName("com.mysql.jdbc.Driver");
				cnn = DriverManager.getConnection(Configure.cnnStr);
				preparedStat = cnn.prepareStatement("update " + Configure.tblStudentDefense + " set Title = ?, " +
						"Supervisor = ?, Examiner1 = ?," +
						"Examiner2 = ?, President = ?," +
						"Secretary = ?, AdditionalMember = ? where StudentID = ?");
				preparedStat.setInt(8, studentID);
				preparedStat.setString(1, thesis_title);
				preparedStat.setInt(2, supervisorID);
				preparedStat.setInt(3, examiner1ID);
				preparedStat.setInt(4, examiner2ID);
				preparedStat.setInt(5, presidentID);
				preparedStat.setInt(6, secretaryID);
				preparedStat.setInt(7, memberID);				
				preparedStat.executeUpdate();
				System.out.println("update table " + Configure.tblStudentDefense + " successfully");
				//stat.executeQuery(sql);
				close(cnn,stat,rs);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(cnn, stat,rs);
		}		
	}

	public static void deleteJury(int studentID){
		Connection cnn = null;
		Statement stat = null;
		PreparedStatement preparedStat = null;
		ResultSet rs = null;
		try{
			if(cnn == null){
				Class.forName("com.mysql.jdbc.Driver");
				cnn = DriverManager.getConnection(Configure.cnnStr);
				preparedStat = cnn.prepareStatement("delete from " + Configure.tblStudentDefense + " where StudentID = ?");
				preparedStat.setInt(1, studentID);
							
				preparedStat.executeUpdate();
				System.out.println("delete  from table " + Configure.tblStudentDefense + " successfully");
				//stat.executeQuery(sql);
				close(cnn,stat,rs);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(cnn, stat,rs);
		}		
	}
	public static void deleteSubjectCategory(int id){
		Connection cnn = null;
		Statement stat = null;
		PreparedStatement preparedStat = null;
		ResultSet rs = null;
		try{
			if(cnn == null){
				Class.forName("com.mysql.jdbc.Driver");
				cnn = DriverManager.getConnection(Configure.cnnStr);
				preparedStat = cnn.prepareStatement("delete from " + Configure.tblSubjectCategories + " where ID = ?");
				preparedStat.setInt(1, id);
							
				preparedStat.executeUpdate();
				close(cnn,stat,rs);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(cnn, stat,rs);
		}		
	}
	public static void updateSubjectCategory(int id, String name){
		Connection cnn = null;
		Statement stat = null;
		PreparedStatement preparedStat = null;
		ResultSet rs = null;
		try{
			if(cnn == null){
				Class.forName("com.mysql.jdbc.Driver");
				cnn = DriverManager.getConnection(Configure.cnnStr);
				preparedStat = cnn.prepareStatement("update " + Configure.tblSubjectCategories + " set Name = ? where ID = ?");
				preparedStat.setString(1, name);
				preparedStat.setInt(2, id);			
				preparedStat.executeUpdate();
				close(cnn,stat,rs);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(cnn, stat,rs);
		}		
	}
	
	public static void deleteAllRooms(){
		Connection cnn = null;
		Statement stat = null;
		PreparedStatement preparedStat = null;
		ResultSet rs = null;
		try{
			if(cnn == null){
				Class.forName("com.mysql.jdbc.Driver");
				cnn = DriverManager.getConnection(Configure.cnnStr);
				preparedStat = cnn.prepareStatement("delete from " + Configure.tblRoom);
							
				preparedStat.executeUpdate();
				close(cnn,stat,rs);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(cnn, stat,rs);
		}		
	}
	public static void deleteAllSlots(){
		Connection cnn = null;
		Statement stat = null;
		PreparedStatement preparedStat = null;
		ResultSet rs = null;
		try{
			if(cnn == null){
				Class.forName("com.mysql.jdbc.Driver");
				cnn = DriverManager.getConnection(Configure.cnnStr);
				preparedStat = cnn.prepareStatement("delete from " + Configure.tblSlot);
							
				preparedStat.executeUpdate();
				close(cnn,stat,rs);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(cnn, stat,rs);
		}		
	}

	public static void cleanRoomsSlots(int sessionID){
		Vector<JuryInfo> jury = Utility.getListJuryInfo();
		Vector<Room> rooms = Utility.getRooms();
		Vector<Slot> slots = Utility.getSlots();
		HashSet<Integer> room_id_used = new HashSet<Integer>();
		HashSet<Integer> slot_id_used = new HashSet<Integer>();
		
		for(int i = 0; i < jury.size(); i++){
			JuryInfo J = jury.get(i);
			room_id_used.add(J.getRoomId());
			slot_id_used.add(J.getSlotId());
		}
		
		HashSet<Integer> roomRemoved = new HashSet<Integer>();
		for(int i = 0; i < rooms.size(); i++){
			Room r = rooms.get(i);
			if(r.getSessionID() == sessionID && !room_id_used.contains(r.getID())){
				Utility.deleteRoom(r.getID());
				roomRemoved.add(r.getID());
			}
		}
		
		for(int i = 0; i < slots.size(); i++){
			Slot sl = slots.get(i);
			if(!slot_id_used.contains(sl.getID()) && roomRemoved.contains(sl.getRoomID())){
				Utility.deleteSlot(sl.getID());
			}
		}
	}
	public static void deleteRoom(int id){
		Connection cnn = null;
		Statement stat = null;
		PreparedStatement preparedStat = null;
		ResultSet rs = null;
		try{
			if(cnn == null){
				Class.forName("com.mysql.jdbc.Driver");
				cnn = DriverManager.getConnection(Configure.cnnStr);
				preparedStat = cnn.prepareStatement("delete from " + Configure.tblRoom + " where ID = ?");
				preparedStat.setInt(1, id);			
				preparedStat.executeUpdate();
				close(cnn,stat,rs);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(cnn, stat,rs);
		}		
	}
	public static void deleteSlot(int id){
		Connection cnn = null;
		Statement stat = null;
		PreparedStatement preparedStat = null;
		ResultSet rs = null;
		try{
			if(cnn == null){
				Class.forName("com.mysql.jdbc.Driver");
				cnn = DriverManager.getConnection(Configure.cnnStr);
				preparedStat = cnn.prepareStatement("delete from " + Configure.tblSlot + " where ID = ?");
				preparedStat.setInt(1, id);				
				preparedStat.executeUpdate();
				close(cnn,stat,rs);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(cnn, stat,rs);
		}		
	}
	public static HashSet<Integer> collectProfessorsJury(Vector<JuryInfo> listJury){
		HashSet<Integer> professors = new HashSet<Integer>();
		for(int i = 0; i < listJury.size(); i++){
			JuryInfo jr = listJury.get(i);
			System.out.println("Utility::collectProfessorsJury, jr[" + i + "] = " + jr.getExaminerId1() + ", " + 
			jr.getExaminerId2() + ", " + jr.getPresidentId() );
			professors.add(jr.getExaminerId1());
			professors.add(jr.getExaminerId2());
			professors.add(jr.getPresidentId());
			professors.add(jr.getSecretaryId());
			professors.add(jr.getAdditionalMemberId());
		}
		return professors;
	}
	public static Vector<JuryInfo> collectJuriesOfProfessor(Vector<JuryInfo> listJury, int pid){
		Vector<JuryInfo> jr = new Vector<JuryInfo>();
		for(int i = 0; i < listJury.size(); i++){
			JuryInfo j = listJury.get(i);
			if(j.juryProfessor(pid))
				jr.add(j);
		}
		return jr;
	}
	public static HashSet<Integer> collectSlots(Vector<JuryInfo> listJury){
		HashSet<Integer> slots = new HashSet<Integer>();
		for(int i = 0; i < listJury.size(); i++){
			JuryInfo jr = listJury.get(i);
			slots.add(jr.getSlotId());
		}
		return slots;
	}
	public static HashSet<Integer> collectSlotsIndex(Vector<JuryInfo> listJury, HashMap<Integer,Slot> mSlots){
		HashSet<Integer> slots = new HashSet<Integer>();
		for(int i = 0; i < listJury.size(); i++){
			JuryInfo jr = listJury.get(i);
			Slot sl = mSlots.get(jr.getSlotId());
			if(sl != null) slots.add(sl.getSlotIndex());
		}
		return slots;
	}

	public static Vector<Integer> collectVectorSlots(Vector<JuryInfo> jury){
		Vector<Integer> slots = new Vector<Integer>();
		HashSet<Integer> s = collectSlots(jury);
		int[] arr = new int[s.size()];
		Iterator it = s.iterator();
		int idx = -1;
		while(it.hasNext()){
			int sl = (Integer)it.next();
			idx++;
			arr[idx] = sl;
		}
		for(int i = 0; i < arr.length-1; i++)
			for(int j = i+1; j < arr.length; j++)
				if(arr[i] > arr[j]){
					int tmp = arr[i]; arr[i] = arr[j]; arr[j] = tmp;
				}
		for(int i = 0; i < arr.length; i++)
			slots.add(arr[i]);
		return slots;
	}	
	
	public static HashSet<Integer> collectRooms(Vector<JuryInfo> listJury){
		HashSet<Integer> rooms = new HashSet<Integer>();
		for(int i = 0; i < listJury.size(); i++){
			JuryInfo jr = listJury.get(i);
			rooms.add(jr.getRoomId());
		}
		return rooms;
	}
	public static Vector<Room> collectVectorRooms(Vector<JuryInfo> jury){
		Vector<Room> rooms = new Vector<Room>();
		Vector<Room> rooms_db = Utility.getRooms();
		HashMap<Integer, Room> mRoom = new HashMap<Integer, Room>();
		for(int i = 0; i < rooms_db.size(); i++){
			Room r = rooms_db.get(i);
			mRoom.put(r.getID(), r);
		}
		HashSet<Integer> rid = collectRooms(jury);
		int[] arr = new int[rid.size()];
		Iterator it = rid.iterator();
		int idx = -1;
		while(it.hasNext()){
			int sl = (Integer)it.next();
			idx++;
			arr[idx] = sl;
		}
		for(int i = 0; i < arr.length-1; i++)
			for(int j = i+1; j < arr.length; j++)
				if(arr[i] > arr[j]){
					int tmp = arr[i]; arr[i] = arr[j]; arr[j] = tmp;
				}
		for(int i = 0; i < arr.length; i++){
			String des = "-";
			if(mRoom.get(arr[i]) != null) des = mRoom.get(arr[i]).getDescription(); 
			Room r = new Room(arr[i],des,0);
			int nbJuries = 0;
			HashSet<Integer> hustJ = new HashSet<Integer>();
			HashSet<Integer> nonHustJ = new HashSet<Integer>();
			for(int j = 0; j < jury.size(); j++){
				JuryInfo J = jury.get(j);
				if(J.getRoomId() == r.getID()){
					nbJuries++;
					hustJ.add(J.getExaminerId2());
					hustJ.add(J.getPresidentId());
					hustJ.add(J.getSecretaryId());
					nonHustJ.add(J.getExaminerId1());
					nonHustJ.add(J.getAdditionalMemberId());
				}
			}
			r.setMaxNbHustMembers(hustJ.size());
			r.setMaxNbNonHustMembers(nonHustJ.size());
			r.setNbJuries(nbJuries);
			rooms.add(r);
		}
		return rooms;
	}	
	public static int getMinSlot(HashSet<Integer> slots){
		int min = 100000;
		Iterator it = slots.iterator();
		while(it.hasNext()){
			Integer sl = (Integer)it.next();
			if(min > sl) min = sl;
		}
		return min;
	}
	public static int getMaxSlot(HashSet<Integer> slots){
		int max = -100000;
		Iterator it = slots.iterator();
		while(it.hasNext()){
			Integer sl = (Integer)it.next();
			if(max < sl) max = sl;
		}
		return max;
	}

	public static void createDefenseScheduleExcel(String fn, String filterBy, String orderBy){
		Vector<JuryInfo> listJury = getListJuryInfo(orderBy,filterBy);
		
		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFSheet sheet = hwb.createSheet("juryinfo");
	
		HSSFRow rowHeader = sheet.createRow((short)0);
		HSSFCell[] cell = new HSSFCell[9];
		
		HSSFFont font = hwb.createFont();
		//font.setFontHeightInPoints((short)24);
		//font.setFontName("Courier New");
		//font.setItalic(true);
		//font.setStrikeout(true);
		font.setBoldweight((short)2);
		
		rowHeader.createCell((short)0).setCellValue("Ho ten Sinh vien");
		rowHeader.createCell((short)1).setCellValue("Ten de tail");
		rowHeader.createCell((short)2).setCellValue("Phan bien 1");
		rowHeader.createCell((short)3).setCellValue("Phan vien 2");
		rowHeader.createCell((short)4).setCellValue("Chu tich");
		rowHeader.createCell((short)5).setCellValue("Thu ky");
		rowHeader.createCell((short)6).setCellValue("Uy vien");
		rowHeader.createCell((short)7).setCellValue("Kip");
		rowHeader.createCell((short)8).setCellValue("Phong");
		
		HSSFCellStyle style = hwb.createCellStyle();
		style.setFont(font);
		style.setWrapText(true);
		
		for(int i = 0; i < 9; i++){
			rowHeader.getCell(i).setCellStyle(style);
		}
		
		for(int i = 0; i < listJury.size(); i++){
			JuryInfo jr = listJury.get(i);
			HSSFRow row = sheet.createRow((short)i+2);
			String studentName = jr.getStudentName();
			String thesis_title = jr.getTitle();
			String examiner1Name = jr.getExaminerName1();
			String examiner2Name = jr.getExaminerName2();
			String presidentName = jr.getPresidentName();
			String secretaryName = jr.getSecretaryName();
			String memberName = jr.getAdditionalMemberName();
			try{
				studentName = URLDecoder.decode(studentName,"UTF-8");
				thesis_title = URLDecoder.decode(thesis_title,"UTF-8");
				examiner1Name = URLDecoder.decode(examiner1Name,"UTF-8");
				examiner2Name = URLDecoder.decode(examiner2Name,"UTF-8");
				presidentName = URLDecoder.decode(presidentName,"UTF-8");
				secretaryName = URLDecoder.decode(secretaryName,"UTF-8");
				memberName = URLDecoder.decode(memberName,"UTF-8");
			}catch(Exception ex){
				ex.printStackTrace();
			}
	
			row.createCell((short)0).setCellValue(studentName);
			row.createCell((short)1).setCellValue(thesis_title);
			
			row.createCell((short)2).setCellValue(examiner1Name);
			row.createCell((short)3).setCellValue(examiner2Name);
			row.createCell((short)4).setCellValue(presidentName);
			row.createCell((short)5).setCellValue(secretaryName);
			row.createCell((short)6).setCellValue(memberName);
			row.createCell((short)7).setCellValue(jr.getSlotDescription());
			row.createCell((short)8).setCellValue(jr.getRoomName());
			
			for(int j = 0; j < 9; j++)
				row.getCell(j).setCellStyle(style);
		}
		for(int i = 0; i < 7; i++)
			sheet.setColumnWidth(i, (short) ( ( 50 * 8 ) / ( (double) 1 / 15 ) ));
		for(int i = 7; i < 9; i++)
			sheet.setColumnWidth(i, (short) ( ( 50 * 8 ) / ( (double) 1 / 5 ) ));
		try{
			FileOutputStream fileOut = new FileOutputStream(fn);
			hwb.write(fileOut);
			fileOut.close();
			System.out.println("generate excel file successfully");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void createDefenseScheduleExcelFull(String fn, String filterBy, String orderBy){
		System.out.println("Utility::createDefenseScheduleExcelFull, filename = " + fn);
		Vector<JuryInfo> listJury = getListJuryInfo(orderBy,filterBy);
		Vector<Teacher> teachers = getTeachers();
		HashMap<Integer, Teacher> mT = new HashMap<Integer, Teacher>();
		for(int i = 0; i < teachers.size(); i++)
			mT.put(teachers.get(i).getID(), teachers.get(i));
		
		//Utility u = new Utility();
		//fn = u.getClass().getClassLoader().getResource(".").getPath() + fn;
		
		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFSheet sheet = hwb.createSheet("juryinfo");
	
		HSSFRow rowHeader = sheet.createRow((short)0);
		HSSFCell[] cell = new HSSFCell[19];
		
		HSSFFont font = hwb.createFont();
		//font.setFontHeightInPoints((short)24);
		//font.setFontName("Courier New");
		//font.setItalic(true);
		//font.setStrikeout(true);
		font.setBoldweight((short)2);
		
		rowHeader.createCell((short)0).setCellValue("Ho ten Sinh vien");
		rowHeader.createCell((short)1).setCellValue("Ten de tai");
		rowHeader.createCell((short)2).setCellValue("Phan bien 1");
		rowHeader.createCell((short)3).setCellValue("Co Quan");
		rowHeader.createCell((short)4).setCellValue("Hoc ham/hoc vi");
		rowHeader.createCell((short)5).setCellValue("Phan vien 2");
		rowHeader.createCell((short)6).setCellValue("Co Quan");
		rowHeader.createCell((short)7).setCellValue("Hoc ham/hoc vi");
		rowHeader.createCell((short)8).setCellValue("Chu tich");
		rowHeader.createCell((short)9).setCellValue("Co Quan");
		rowHeader.createCell((short)10).setCellValue("Hoc ham/hoc vi");
		rowHeader.createCell((short)11).setCellValue("Thu ky");
		rowHeader.createCell((short)12).setCellValue("Co Quan");
		rowHeader.createCell((short)13).setCellValue("Hoc ham/hoc vi");
		rowHeader.createCell((short)14).setCellValue("Uy vien");
		rowHeader.createCell((short)15).setCellValue("Co Quan");
		rowHeader.createCell((short)16).setCellValue("Hoc ham/hoc vi");
		rowHeader.createCell((short)17).setCellValue("Kip");
		rowHeader.createCell((short)18).setCellValue("Phong");
		
		HSSFCellStyle style = hwb.createCellStyle();
		style.setFont(font);
		style.setWrapText(true);
		
		for(int i = 0; i < 19; i++){
			rowHeader.getCell(i).setCellStyle(style);
		}
		
		for(int i = 0; i < listJury.size(); i++){
			JuryInfo jr = listJury.get(i);
			HSSFRow row = sheet.createRow((short)i+2);
			
			String studentName = jr.getStudentName();
			String thesis_title = jr.getTitle();
			String examiner1Name = jr.getExaminerName1();
			String examiner2Name = jr.getExaminerName2();
			String presidentName = jr.getPresidentName();
			String secretaryName = jr.getSecretaryName();
			String memberName = jr.getAdditionalMemberName();
			try{
				studentName = URLDecoder.decode(studentName,"UTF-8");
				thesis_title = URLDecoder.decode(thesis_title,"UTF-8");
				examiner1Name = URLDecoder.decode(examiner1Name,"UTF-8");
				examiner2Name = URLDecoder.decode(examiner2Name,"UTF-8");
				presidentName = URLDecoder.decode(presidentName,"UTF-8");
				secretaryName = URLDecoder.decode(secretaryName,"UTF-8");
				memberName = URLDecoder.decode(memberName,"UTF-8");
			
	
				Teacher T = null;
				String instituteName = "";
				String degree = "";
				
				row.createCell((short)0).setCellValue(studentName);
				row.createCell((short)1).setCellValue(thesis_title);
				
				row.createCell((short)2).setCellValue(examiner1Name);
				T = mT.get(jr.getExaminerId1());
				instituteName = URLDecoder.decode(T.getInstituteName(),"UTF-8");
				degree = T.getDegree();
				row.createCell((short)3).setCellValue(instituteName);
				row.createCell((short)4).setCellValue(degree);
				
				row.createCell((short)5).setCellValue(examiner2Name);
				T = mT.get(jr.getExaminerId2());
				instituteName = URLDecoder.decode(T.getInstituteName(),"UTF-8");
				degree = T.getDegree();
				row.createCell((short)6).setCellValue(instituteName);
				row.createCell((short)7).setCellValue(degree);
				
				row.createCell((short)8).setCellValue(presidentName);
				T = mT.get(jr.getPresidentId());
				instituteName = URLDecoder.decode(T.getInstituteName(),"UTF-8");
				degree = T.getDegree();
				row.createCell((short)9).setCellValue(instituteName);
				row.createCell((short)10).setCellValue(degree);
				
				row.createCell((short)11).setCellValue(secretaryName);
				T = mT.get(jr.getSecretaryId());
				instituteName = URLDecoder.decode(T.getInstituteName(),"UTF-8");
				degree = T.getDegree();
				row.createCell((short)12).setCellValue(instituteName);
				row.createCell((short)13).setCellValue(degree);
				
				row.createCell((short)14).setCellValue(memberName);
				T = mT.get(jr.getAdditionalMemberId());
				instituteName = URLDecoder.decode(T.getInstituteName(),"UTF-8");
				degree = T.getDegree();
				row.createCell((short)15).setCellValue(instituteName);
				row.createCell((short)16).setCellValue(degree);
				
				row.createCell((short)17).setCellValue(jr.getSlotDescription());
				row.createCell((short)18).setCellValue(jr.getRoomName());
				
				for(int j = 0; j < 19; j++)
					row.getCell(j).setCellStyle(style);
			
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		for(int i = 0; i < 17; i++)
			sheet.setColumnWidth(i, (short) ( ( 50 * 8 ) / ( (double) 1 / 15 ) ));
		for(int i = 17; i < 19; i++)
			sheet.setColumnWidth(i, (short) ( ( 50 * 8 ) / ( (double) 1 / 5 ) ));
		try{
			FileOutputStream fileOut = new FileOutputStream(fn);
			hwb.write(fileOut);
			fileOut.close();
			System.out.println("generate excel file successfully located at: " + fn);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static int compare(int[] a, int[] b){
		int n = a.length;
		for(int i = 0; i < n; i++)
			if(a[i] > b[i]) return 1;
			else if(a[i] < b[i]) return -1;
		return 0;
	}
	
	
	public static int getMin(int a, int b){
		return a < b ? a : b;
	}
	private static int computeMatchScore(int[] sm, int[] pm){
		//System.out.println("JuryPartitionerAdvanced::computeMatchScore");
		//System.out.print("sm = "); for(int i = 0; i < sm.length; i++) System.out.print(sm[i] + " "); System.out.println();
		//System.out.print("pm = "); for(int i = 0; i < pm.length; i++) System.out.print(pm[i] + " "); System.out.println();
		int n = sm.length;
		int minScore = 10000;
		for(int i = 0; i < n; i++){
			int d = Math.abs(sm[i]-pm[i]) + (getMin(sm[i],pm[i])-1);
			//System.out.println(sm[i] + "," + pm[i] + "," + d);
			if(d < minScore) minScore = d;
		}
		//System.out.println("JuryPartitionerAdvanced::computeMatchScore, rs = " + minScore);
		return minScore;
	}
	private static int[] getStudentMatchScore(int studentID, Vector<StudentSubjectCategoryMatch> sscm, Vector<SubjectCategory> categories){
		int[] sm = new int[categories.size()];
		for(int i = 0; i < sm.length; i++)
			sm[i] = Configure.maxMatchSubjectScore;
		
		for(int j = 0; j < categories.size(); j++){
			int cID = categories.get(j).getID();
			for(int k = 0; k < sscm.size(); k++){
				StudentSubjectCategoryMatch ssc = sscm.get(k);
				if(ssc.getStudentID() == studentID && ssc.getSubjectCategoryID() == cID){
					sm[j] = ssc.getMatchScore();
					break;
				}
			}
		}
		return sm;
	}
	private static int[] getProfessorMatchScore(int professorID, Vector<ProfessorSubjectCategoryMatch> pscm, Vector<SubjectCategory> categories){
		int[] pm = new int[categories.size()];
		for(int i = 0; i < pm.length; i++)
			pm[i] = Configure.maxMatchSubjectScore;
		for(int j = 0; j < categories.size(); j++){
			int cID = categories.get(j).getID();
			for(int k = 0; k < pscm.size(); k++){
				ProfessorSubjectCategoryMatch psc = pscm.get(k);
				if(psc.getProfessorID() == professorID && psc.getSubjectCategoryID() == cID){
					pm[j] = psc.getMatchScore();
					break;
				}
			}
		}
		return pm;
	}
	private static int computeMatchScore(int studentID, int professorID, Vector<StudentSubjectCategoryMatch> sscm, Vector<ProfessorSubjectCategoryMatch> pscm, Vector<SubjectCategory> categories){
		int[] sm = getStudentMatchScore(studentID, sscm, categories);
		int[] pm = getProfessorMatchScore(professorID, pscm, categories);
		
		
		return computeMatchScore(sm,pm);
	}

	public static HashMap[] computeMatchJuryProfessor(Vector<JuryInfo> jury, Vector<Teacher> teachers){
		HashMap[] matchJuryProfessors = new HashMap[jury.size()];
		Vector<StudentSubjectCategoryMatch> sscm = Utility.getStudentSubjectCategoryMatches();
		Vector<ProfessorSubjectCategoryMatch> spcm = Utility.getProfessorSubjectCategoryMatches();
		Vector<SubjectCategory> categories = Utility.getSubjectCategories();
		
		for(int i = 0; i < jury.size(); i++){
			JuryInfo J = jury.get(i);
			matchJuryProfessors[i] = new HashMap<Integer, Integer>();
			for(int j = 0; j < teachers.size(); j++){
				Teacher t = teachers.get(j);
				int matchScore = computeMatchScore(J.getStudentID(), t.getID(), sscm,spcm,categories);
				matchJuryProfessors[i].put(t.getID(), matchScore);
				//System.out.println("Utility::computeMatchJuryProfessor, matchScore[" + i + "][" + t.getID() + "] = " + matchJuryProfessors[i].get(t.getID()));
			}
		}

		return matchJuryProfessors;
	}
	
	public static int[][] computeMatchJuryJury(Vector<JuryInfo> jury){
		int n = jury.size();
		int [][] matchJuryJury = new int[n][n];
		Vector<StudentSubjectCategoryMatch> sscm = Utility.getStudentSubjectCategoryMatches();
		
		Vector<SubjectCategory> categories = Utility.getSubjectCategories();
	
		for(int i = 0; i < n-1; i++){
			int[] smi = getStudentMatchScore(jury.get(i).getStudentID(),sscm,categories);
			for(int j = i+1; j < n; j++){
				int[] smj = getStudentMatchScore(jury.get(j).getStudentID(),sscm,categories);
				int score = computeMatchScore(smi,smj);
				matchJuryJury[i][j] = score;
				matchJuryJury[j][i] = score;
			}
		}
		/*
		for(int i = 0; i < n; i++){
			for(int j = 0; j < n; j++)
				System.out.print(matchJuryJury[i][j] + " ");
			System.out.println();
		}
		*/
		return matchJuryJury;
	}
	public static Vector<Classes> getClasses(){
		Connection cnn = null;
		Statement stat = null;
		PreparedStatement preparedStat = null;
		ResultSet rs = null;
		
		Vector<Classes> classes = new Vector<Classes>();
		//System.out.println("Start JDBC");
		try{
			
			//System.out.println("Start JDBC");
			Class.forName("com.mysql.jdbc.Driver");
			//System.out.println("JDBC -->OK");
			cnn = DriverManager.getConnection(Configure.cnnStr);
			stat = cnn.createStatement();
			rs = stat.executeQuery("select * from " + Configure.tblClasses);
			//rs = stat.executeQuery("select * from students");
			while(rs.next()){
				
				String name = rs.getString("Name");
				
				int id = rs.getInt("ID");
				
				Classes st = new Classes(id,name);
				classes.add(st);
			}
			//out.println("server return results");
			
			System.out.println("get classes successfully");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(cnn,stat,rs);
		}
		return classes;
	}
	public static int getDepartmentID(String department){
		int depart = 0;
		Connection cnn = null;
		Statement stat = null;
		PreparedStatement preparedStat = null;
		ResultSet rs = null;
		//System.out.println("Start JDBC");
		try{
			
			//System.out.println("Start JDBC");
			Class.forName("com.mysql.jdbc.Driver");
			//System.out.println("JDBC -->OK");
			cnn = DriverManager.getConnection(Configure.cnnStr);
			//stat = cnn.createStatement();
			preparedStat = cnn.prepareStatement("select ID from departments Where Name = ?");
			preparedStat.setString(1, department);
			rs = preparedStat.executeQuery();
			if (rs.next())
			{
				depart = rs.getInt("ID");
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(cnn,stat,rs);
		}
		return depart;
	}
	public static Vector<Student> getStudents(int status){
		
		Connection cnn = null;
		Statement stat = null;
		PreparedStatement preparedStat = null;
		ResultSet rs = null;
		
		Vector<Student> students = new Vector<Student>();
		Vector<Classes> classes = Utility.getClasses();
		HashMap<Integer, String> mClass = new HashMap<Integer,String>();
		for(int i = 0; i < classes.size(); i++){
			Classes cl = classes.get(i);
			mClass.put(cl.getID(), cl.getName());
		}
		//System.out.println("Start JDBC");
		try{
			
			//System.out.println("Start JDBC");
			Class.forName("com.mysql.jdbc.Driver");
			//System.out.println("JDBC -->OK");
			cnn = DriverManager.getConnection(Configure.cnnStr);
			stat = cnn.createStatement();
			rs = stat.executeQuery("select * from " + Configure.tblStudents + " where Status = " + status);
			//rs = stat.executeQuery("select * from students");
			while(rs.next()){
				//System.out.println(rs.getString("name"));
				//System.out.println(rs.getInt("idgv"));
				String name = rs.getString("StudentName");
				//int id = Integer.valueOf(rs.getString("ID"));
				int id = rs.getInt("ID");
				String studentID=rs.getString("StudentID");
				String promotion = rs.getString("Promotion");
				int classID = rs.getInt("Class");
				String class_student = mClass.get(classID);
				String email = rs.getString("Email");
				String phone = rs.getString("Phone");
				String subject = rs.getString("Subject");
				Date startDate = rs.getDate("StartDate");
				Date endDate = rs.getDate("EndDate");
				String type = rs.getString("Type");
				//int status = rs.getInt("Status");
				//int id = rs.getInt("idsv");
				Student st = new Student(id,name,studentID,promotion,classID,class_student,email,phone,subject,startDate,endDate,type,status);
				students.add(st);
			}
			//out.println("server return results");
			
			System.out.println("get students successfully");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(cnn,stat,rs);
		}
		return students;
	}
	public static Vector<Student> getStudentsOfClass(int classID, int statusID){
		//if(classID == 0) return getStudents();// return all students
		Connection cnn = null;
		Statement stat = null;
		PreparedStatement preparedStat = null;
		ResultSet rs = null;
		
		Vector<Student> students = new Vector<Student>();
		Vector<Classes> classes = Utility.getClasses();
		HashMap<Integer, String> mClass = new HashMap<Integer,String>();
		for(int i = 0; i < classes.size(); i++){
			Classes cl = classes.get(i);
			mClass.put(cl.getID(), cl.getName());
		}
		//System.out.println("Start JDBC");
		try{
			
			//System.out.println("Start JDBC");
			Class.forName("com.mysql.jdbc.Driver");
			//System.out.println("JDBC -->OK");
			cnn = DriverManager.getConnection(Configure.cnnStr);
			stat = cnn.createStatement();
			String sql = "select * from " + Configure.tblStudents;
			if(classID > 0 && statusID > 0) sql += " where " + " Class = " + classID + " and " + " Status = " + statusID;
			else if(classID > 0) sql += " where Class = " + classID;
			else if(statusID > 0) sql += " where Status = " + statusID;
			rs = stat.executeQuery(sql);
			//rs = stat.executeQuery("select * from " + tblStudents + " where Class = " + classID);
			//rs = stat.executeQuery("select * from students");
			while(rs.next()){
				//System.out.println(rs.getString("name"));
				//System.out.println(rs.getInt("idgv"));
				String name = rs.getString("StudentName");
				//int id = Integer.valueOf(rs.getString("ID"));
				int id = rs.getInt("ID");
				String studentID=rs.getString("StudentID");
				String promotion = rs.getString("Promotion");
				//String class_student = rs.getString("Class");
				int r_classID = rs.getInt("Class");
				String className = mClass.get(r_classID);
				String email = rs.getString("Email");
				String phone = rs.getString("Phone");
				String subject = rs.getString("Subject");
				Date startDate = rs.getDate("StartDate");
				Date endDate = rs.getDate("EndDate");
				String type = rs.getString("Type");
				int status = rs.getInt("Status");
				//int id = rs.getInt("idsv");
				Student st = new Student(id,name,studentID,promotion,r_classID,className,email,phone,subject,startDate,endDate,type,status);
				students.add(st);
			}
			//out.println("server return results");
			
			System.out.println("get students successfully");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(cnn,stat,rs);
		}
		return students;
	}
	
	public static void readConfigParameters(String fn){
		try{
			BufferedReader in = new BufferedReader(new FileReader(fn));
			String line = "";
			String delimiter = "=";
			while((line=in.readLine())!=null){
				int idx = line.indexOf(delimiter);
				String tag = line.substring(0,idx);
				String val = line.substring(idx+1,line.length());
				System.out.println("tag = " + tag + ", val = " + val);
				if(tag.equals("DB_DRIVER")){ 
					
				}else if(tag.equals("DB_PATH")){
					Configure.cnnStr = val;
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public static void test(){
		System.out.println("Utility::test, Configure.path = " + Configure.path);
		Configure.path = "xyz";
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Utility.createDefenseScheduleExcel("jury.xls");
		Utility u = new Utility();
		String dir = u.getClass().getClassLoader().getResource(".").getPath();
		System.out.println(u.getClass().getClassLoader().getResource(".").getPath());
		
		Utility.readConfigParameters(dir + "/" + "config.txt");
	}

}
