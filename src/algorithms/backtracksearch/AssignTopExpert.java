package algorithms.backtracksearch;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import DataEntity.JuryDataForScheduling;
import DataEntity.JuryInfo;
import DataEntity.Room;
import DataEntity.Teacher;
import java.util.*;
import utils.Utility;

public class AssignTopExpert {

	/**
	 * @param args
	 */
	private int[] x;// x[i] is the  jury to which the top hust expert i is assigned
	private int[] opt_x;
	private double t0;
	private double timeLimit;
	private boolean stop;
	
	private Vector<Teacher> hust;
	private Vector<JuryInfo> jury;
	private Vector<Room> rooms;
	private HashMap[] matchScore;// matchScore[i].get(j) is the match score of jury i and teacher j
	private int n;
	private Vector[] domain;
	
	private int nbObj;
	private int[] bestEval;
	
	private HashMap<Integer, Room> mRoom;
	private HashMap<Integer, HashSet<Integer>> hustOfRoom;// nonHustOfRoom[v] is the set of teachers assigned to room v
	private HashMap<Integer, Integer> occ;// occ[t] is the number of occurrences of teacher t in the jury
	private HashMap<Integer, HashSet<Integer>> supervisors;// supervisors[r] is the set of supervisors assigned to room r
	
	private boolean checkRoomContains(int rid, int v){
		for(int i = 0; i < rooms.size(); i++){
			Room r = rooms.get(i);
			if(r.getID() != rid && hustOfRoom.get(r.getID()).contains(v))
				return true;
		}
		return false;
	}
	
	private int[] eval(){
		//System.out.print("eval x = "); for(int i = 0; i < n; i++) System.out.print(x[i] + " "); System.out.println();
		int[] e = new int[nbObj];
		int min = 10000;
		int max = -min;
		for(int i = 0; i < hust.size(); i++){
			Teacher t = hust.get(i);
			if(occ.get(t.getID()) < min) min = occ.get(t.getID());
			if(occ.get(t.getID()) > max) max = occ.get(t.getID());
		}
		e[0] = max - min;
		
		e[1] = 0;
		for(int i = 0; i < hust.size(); i++){
			Teacher t = hust.get(i);
			e[1] += (Integer)matchScore[x[i]].get(t.getID());
		}
		return e;
	}
	private void updateBest(){
		int[] e = eval();
		stop = true;
		if(Utility.compare(bestEval,e) == 1){
			System.out.println("AssignExaminer2::updateBest, bestEval = " + bestEval[0] + "," + bestEval[1] + ", newEval = " + e[0] + "," + e[1]);
			for(int j = 0; j < nbObj; j++){
				bestEval[j] = e[j];
				if(bestEval[j] == 0) stop = false;
			}
			for(int j = 0; j < n; j++)
				opt_x[j] = x[j];
		}
	}
	private boolean checkExist(int k, int v){
		for(int i = 0; i < k; i++)
			if(x[i] == v) return true;
		return false;
	}
	private void TRY(int k){
		if(System.currentTimeMillis() - t0 > timeLimit) return;
		if(stop) return;
		
		Teacher t = hust.get(k);
		//System.out.println("TRY(" + k + "), domain.sz = " + domain[k].size());
		
		for(int i = 0; i < domain[k].size(); i++){
			int v = (Integer)domain[k].get(i);// jury to be assigned to teacher t
			JuryInfo J = jury.get(v);
			boolean ok = true;

			if(J.getRoomId() > 0){
				int rid = J.getRoomId();
				if(!hustOfRoom.get(rid).contains(t.getID()) && hustOfRoom.get(rid).size() >= mRoom.get(rid).getMaxNbHustMembers()) ok = false;
				
				if(ok) ok = !checkRoomContains(rid, t.getID());
				if(ok) ok = hustOfRoom.get(rid).size() == 0;
				//if(ok) ok = !checkExist(k,v);
			}
			if(J.getSupervisorId() == t.getID()) ok = false;
			
			
			
			if(!ok) continue;
			
			x[k] = v;// teacher k is assigned to jury v
			
			//System.out.println("AssignExaminer2::TRY(" + k + "),  v =" + v);
			occ.put(t.getID(), occ.get(t.getID())+1);
			if(occ.get(t.getID()) == 1 && J.getRoomId() > 0) hustOfRoom.get(J.getRoomId()).add(t.getID());
			
			if(k == n-1){
				updateBest();
			}else{
				TRY(k+1);
			}
			if(occ.get(t.getID()) == 1 && J.getRoomId() > 0) hustOfRoom.get(J.getRoomId()).remove(t.getID());
			occ.put(t.getID(), occ.get(t.getID())-1);
			
		}
	}
	public void search(Vector<JuryInfo> jury, Vector<Room> rooms, HashMap[] matchScore, Vector<Teacher> hustTeacher, int timeLimit){
		this.timeLimit = timeLimit*1000;
		//this.hust = hust;
		this.jury = jury;
		this.rooms = rooms;
		this.matchScore = matchScore;
		
		this.hust = new Vector<Teacher>();
		
		
		
		Teacher[] teachers = new Teacher[hustTeacher.size()];
		for(int i = 0; i < hustTeacher.size(); i++){
			teachers[i] = hustTeacher.get(i);
		}
		//sort and select rooms.size() top expert hust
		for(int i = 0; i < hust.size()-1; i++)
			for(int j = i+1; j < hust.size(); j++)
				if(teachers[i].getExpertLevel() > teachers[j].getExpertLevel()){
					Teacher tmp = teachers[i]; teachers[i] = teachers[j]; teachers[j] = tmp;
				}
		//retrieve rooms.size() top hust experts
		hust.clear();
		for(int i = 0; i < rooms.size(); i++){
			hust.add(teachers[i]);
			System.out.println("AssignTopExpert, select top " + teachers[i].getID());
		}
		
		n = hust.size();
		x = new int[n];
		opt_x = new int[n];
		
		supervisors = new HashMap<Integer, HashSet<Integer>>();
		for(int i = 0; i < rooms.size(); i++){
			int rid = rooms.get(i).getID();
			supervisors.put(rid, new HashSet<Integer>());
		}
		
		for(int i = 0; i < n; i++){
			JuryInfo J = jury.get(i);
			if(J.getRoomId() > 0){
				int rid = J.getRoomId();
				supervisors.get(rid).add(J.getSupervisorId());
			}
		}
		
		domain = new Vector[n];
		for(int i = 0; i < n; i++){
			Teacher t = hust.get(i);
			domain[i] = new Vector<Integer>();
			boolean ok = true;
			for(int j = 0; j < jury.size(); j++){
				JuryInfo J = jury.get(i);
				if(J.getPresidentId() == t.getID()){
					domain[i].add(j);
					ok = false;
					break;
				}
			}
			
			if(ok){
				for(int j = 0; j < jury.size(); j++){
					JuryInfo J = jury.get(j);
					if(J.getRoomId() > 0){
						int rid = J.getRoomId();
						if(!supervisors.get(rid).contains(t.getID())) domain[i].add(j);
					}
					
				}
				int[] a = new int[domain[i].size()];
				int[] match = new int[domain[i].size()];
				for(int j = 0; j < a.length; j++){
					a[j] = (Integer)domain[i].get(j);
					match[j] = (Integer)matchScore[a[j]].get(t.getID());
				}
				for(int j1 = 0; j1 < match.length-1; j1++)
					for(int j2 = j1+1; j2 < match.length; j2++)
						if(match[j1] > match[j2]){
							int tmp = match[j1]; match[j1] = match[j2];match[j2] = tmp;
							tmp = a[j1]; a[j1] = a[j2]; a[j2] = tmp;
						}
				domain[i].clear();
				for(int j = 0; j < a.length; j++)
					domain[i].add(a[j]);
			}
			
			System.out.print("Domain[" + t.getID() + "] = ");
			for(int j = 0; j < domain[i].size(); j++){
				System.out.print(domain[i].get(j) + " ");
			}
			System.out.println();
		}
		
		//System.out.println("AssignExaminer1::search, n  = " + n);
		//for(int i = 0; i < n; i++)
			//System.out.println("AssignExaminer1::search, domain[" + i + "].sz = " + domain[i].size());
		
		occ = new HashMap<Integer, Integer>();
		for(int i = 0; i < hust.size(); i++){
			//System.out.println("AssignExaminer2::search, occ.put(" + hust.get(i).getID() + ")");
			occ.put(hust.get(i).getID(), 0);
		}
		
		mRoom = new HashMap<Integer, Room>();
		hustOfRoom = new HashMap<Integer, HashSet<Integer>>();
		for(int i = 0; i < rooms.size(); i++){
			hustOfRoom.put(rooms.get(i).getID(), new HashSet<Integer>());
			mRoom.put(rooms.get(i).getID(), rooms.get(i));
		}
		
		for(int i = 0; i < n; i++){
			JuryInfo J = jury.get(i);
			if(J.getRoomId() > 0){
				if(J.getExaminerId2() > 0) hustOfRoom.get(J.getRoomId()).add(J.getExaminerId2());
				if(J.getPresidentId() > 0) hustOfRoom.get(J.getRoomId()).add(J.getPresidentId());
				if(J.getSecretaryId() > 0) hustOfRoom.get(J.getRoomId()).add(J.getSecretaryId());
			}
			if(J.getExaminerId2() > 0) occ.put(J.getExaminerId2(), occ.get(J.getExaminerId2())+1);
			if(J.getPresidentId() > 0) occ.put(J.getPresidentId(), occ.get(J.getPresidentId())+1);
			if(J.getSecretaryId() > 0) occ.put(J.getSecretaryId(), occ.get(J.getSecretaryId())+1);
		}

		nbObj = 2;
		bestEval = new int[nbObj];
		for(int i = 0; i < nbObj; i++)
			bestEval[i] = 1000000;
		
		t0 = System.currentTimeMillis();
		stop = false;
		
		System.out.println("AssignTopExpert::search Start TRY.....");
		TRY(0);
		System.out.println("AssignTopExpert::search TRY finished.....");
		
		for(int i = 0; i < n; i++){
			System.out.println("opt_x[" + i + "] = " + opt_x[i]);
			Teacher t = hust.get(i);
			int j = opt_x[i];
			JuryInfo J = jury.get(j);
			if(J.getPresidentId() == 0)
				J.setPresidentId(t.getID());
		}
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
