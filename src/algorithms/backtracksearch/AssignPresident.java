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

public class AssignPresident {

	/**
	 * @param args
	 */
	int[] x;// x[i] is the  non hust professor (examiner 2) to which jury i is assigned
	int[] opt_x;
	double t0;
	double timeLimit;
	
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
		for(int i = 0; i < jury.size(); i++){
			e[1] += (Integer)matchScore[i].get(x[i]);
		}
		
		return e;
	}
	private void updateBest(){
		int[] e = eval();
		if(Utility.compare(bestEval,e) == 1){
			System.out.println("AssignPresident::updateBest, bestEval = " + bestEval[0] + "," + bestEval[1] + ", newEval = " + e[0] + "," + e[1]);
			for(int j = 0; j < nbObj; j++){
				bestEval[j] = e[j];
			}
			for(int j = 0; j < n; j++)
				opt_x[j] = x[j];
		}
	}
	private void TRY(int k){
		if(System.currentTimeMillis() - t0 > timeLimit) return;
		JuryInfo J = jury.get(k);
		//System.out.println("TRY(" + k + "), domain.sz = " + domain[k].size());
		
		for(int i = 0; i < domain[k].size(); i++){
			int v = (Integer)domain[k].get(i);
			boolean ok = true;
			int rid = J.getRoomId();
			Room r = mRoom.get(rid); 
			if(!hustOfRoom.get(rid).contains(v) && hustOfRoom.get(rid).size() >= r.getMaxNbHustMembers()) ok = false;
			//System.out.println("TRY(" + k + "), domain.sz = " + domain[k].size() + ", checkRoomContains(" + v + ")");
			
			if(ok) ok = !checkRoomContains(rid,v);
			
			if(!ok) continue;
			
			x[k] = v;
			//System.out.println("AssignExaminer2::TRY(" + k + "),  v =" + v);
			occ.put(v, occ.get(v)+1);
			if(occ.get(v) == 1) hustOfRoom.get(rid).add(v);
			
			if(k == n-1){
				updateBest();
			}else{
				TRY(k+1);
			}
			if(occ.get(v) == 1) hustOfRoom.get(rid).remove(v);
			occ.put(v, occ.get(v)-1);
			
		}
	}
	public void search(Vector<JuryInfo> jury, Vector<Room> rooms, HashMap[] matchScore, Vector<Teacher> hustTeachers, int timeLimit){
		this.timeLimit = timeLimit*1000;
		this.hust = hustTeachers;
		this.jury = jury;
		this.rooms = rooms;
		this.matchScore = matchScore;
		n = jury.size();
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
			domain[i] = new Vector<Integer>();
			JuryInfo J = jury.get(i);
			if(J.getPresidentId() > 0){
				domain[i].add(J.getPresidentId());
			}else{
				for(int j = 0; j < hust.size(); j++){
					boolean ok = true;
					if(J.getSupervisorId() == hust.get(j).getID()) ok = false;
					if(J.getExaminerId2() > 0 && J.getExaminerId2() == hust.get(j).getID()) ok = false;
					if(J.getSecretaryId() > 0 && J.getSecretaryId() == hust.get(j).getID()) ok = false;
					if(J.getRoomId() > 0 && supervisors.get(J.getRoomId()).contains(hust.get(j).getID())) ok = false;
					if(ok) domain[i].add(hust.get(j).getID());
				}
				int sz = domain[i].size();
				int[] a = new int[sz];
				int[] match = new int[sz];
				for(int k = 0; k < sz; k++){
					a[k] = (Integer)domain[i].get(k);
					match[k] = (Integer)matchScore[i].get(a[k]);
				}
				for(int k1 = 0; k1 < sz-1; k1++)
					for(int k2 = k1+1; k2 < sz; k2++)
						if(match[k1] > match[k2]){
							int tmp = match[k1]; match[k1] = match[k2]; match[k2] = tmp;
							tmp = a[k1]; a[k1] = a[k2]; a[k2] = tmp;
						}
				domain[i].clear();
				for(int k = 0; k < sz; k++)
					domain[i].add(a[k]);
			}
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
		
		System.out.println("AssignPresident::search Start TRY.....");
		TRY(0);
		System.out.println("AssignPresident::search TRY finished.....");
		
		for(int i = 0; i < n; i++){
			JuryInfo J = jury.get(i);
			J.setPresidentId(opt_x[i]);
		}
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
