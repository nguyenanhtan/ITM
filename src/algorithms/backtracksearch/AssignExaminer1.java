package algorithms.backtracksearch;

import java.util.Vector;

import DataEntity.JuryDataForScheduling;
import DataEntity.JuryInfo;
import DataEntity.Room;
import DataEntity.Teacher;
import java.util.*;
import utils.Utility;

public class AssignExaminer1 {

	/**
	 * @param args
	 */
	int[] x;// x[i] is the  non hust professor (examiner 1) to which jury i is assigned
	int[] opt_x;
	double t0;
	double timeLimit;
	
	private Vector<Teacher> nonHust;
	private Vector<JuryInfo> jury;
	private Vector<Room> rooms;
	private HashMap[] matchScore;// matchScore[i].get(j) is the match score of jury i and teacher j
	private int n;
	private Vector[] domain;
	
	private int nbObj;
	private int[] bestEval;
	
	private HashMap<Integer, Room> mRoom;
	private HashMap<Integer, HashSet<Integer>> nonHustOfRoom;// nonHustOfRoom[v] is the set of teachers assigned to room v
	private HashMap<Integer, Integer> occ;// occ[t] is the number of occurrences of teacher t in the jury
	
	private boolean checkRoomContains(int rid, int v){
		for(int i = 0; i < rooms.size(); i++){
			Room r = rooms.get(i);
			if(r.getID() != rid && nonHustOfRoom.get(r.getID()).contains(v))
				return true;
		}
		return false;
	}
	
	private int[] eval(){
		//System.out.print("eval x = "); for(int i = 0; i < n; i++) System.out.print(x[i] + " "); System.out.println();
		int[] e = new int[nbObj];
		int min = 10000;
		int max = -min;
		for(int i = 0; i < nonHust.size(); i++){
			Teacher t = nonHust.get(i);
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
			System.out.println("AssignExaminer1::updateBest, bestEval = " + bestEval[0] + "," + bestEval[1] + ", newEval = " + e[0] + "," + e[1]);
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
			System.out.println("TRY(" + k + "), domain.sz = " + domain[k].size() + ", v = " + v + ", rid = " + rid);
			if(!nonHustOfRoom.get(rid).contains(v) && nonHustOfRoom.get(rid).size() >= (r.getMaxNbJuries()+1)/2) ok = false;
			//System.out.println("TRY(" + k + "), domain.sz = " + domain[k].size() + ", checkRoomContains(" + v + ")");
			if(ok) ok = !checkRoomContains(rid,v);
			
			if(!ok) continue;
			
			x[k] = v;
			occ.put(v, occ.get(v)+1);
			if(occ.get(v) == 1) nonHustOfRoom.get(rid).add(v);
			
			if(k == n-1){
				updateBest();
			}else{
				TRY(k+1);
			}
			if(occ.get(v) == 1) nonHustOfRoom.get(rid).remove(v);
			occ.put(v, occ.get(v)-1);
			
		}
	}
	
	private boolean balance(Vector<JuryInfo> jury, HashMap<Integer, Integer> mOccE1, HashMap<Integer, Integer> mOccA){
		for(int i = 0; i < jury.size(); i++){
			JuryInfo J = jury.get(i);
			int e1 = J.getExaminerId1();
			int a = J.getAdditionalMemberId();
			//if(Math.abs(mOccE1.get(e1)-mOccE1.get(a)) > 1){
			if(mOccE1.get(e1)-mOccE1.get(a) > 1){
				J.setExaminerId1(a);
				J.setAdditionalMemberId(e1);
				mOccE1.put(e1, mOccE1.get(e1)-1);
				mOccE1.put(a, mOccE1.get(a)+1);
				System.out.println("AssignExaminer1::balance --> swap " + e1 + " and " + a + " in jury " + i);
				//return true;
			}
		}
		return false;
	}
	public void balances(Vector<JuryInfo> jury, Vector<Teacher> nonHustTeachers){
		this.jury = jury;
		//this.nonHust = nonHustTeachers;
		HashMap<Integer, Integer> mOccE1 = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> mOccA = new HashMap<Integer, Integer>();
		for(int i = 0; i < jury.size(); i++){
			JuryInfo J = jury.get(i);
			int e1 = J.getExaminerId1();
			int a = J.getAdditionalMemberId();
			if(mOccE1.get(e1) == null) mOccE1.put(e1, 1);
			else mOccE1.put(e1, mOccE1.get(e1)+1);
			if(mOccA.get(e1) == null) mOccA.put(e1, 0);
			
			if(mOccA.get(a) == null) mOccA.put(a, 1);
			else mOccA.put(a, mOccA.get(a)+1);
			if(mOccE1.get(a) == null) mOccE1.put(a, 0);
		}
		balance(jury,mOccE1,mOccA);
		/*
		int count = 0;
		while(true && count <= 30){
			if(!balance(jury,mOccE1,mOccA))
				break;
			count++;
		}
		*/
	}
	public void search(Vector<JuryInfo> jury, Vector<Room> rooms, HashMap[] matchScore, Vector<Teacher> nonHustTeachers, int timeLimit){
		this.timeLimit = timeLimit*1000;
		this.nonHust = nonHustTeachers;
		this.jury = jury;
		this.rooms = rooms;
		this.matchScore = matchScore;
		n = jury.size();
		x = new int[n];
		opt_x = new int[n];
		domain = new Vector[n];
		for(int i = 0; i < n; i++){
			domain[i] = new Vector<Integer>();
			JuryInfo J = jury.get(i);
			if(J.getExaminerId1() > 0){
				domain[i].add(J.getExaminerId1());
			}else{
				for(int j = 0; j < nonHust.size(); j++)
					domain[i].add(nonHust.get(j).getID());
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
		for(int i = 0; i < nonHust.size(); i++){
			occ.put(nonHust.get(i).getID(), 0);
		}
		
		mRoom = new HashMap<Integer, Room>();
		nonHustOfRoom = new HashMap<Integer, HashSet<Integer>>();
		for(int i = 0; i < rooms.size(); i++){
			nonHustOfRoom.put(rooms.get(i).getID(), new HashSet<Integer>());
			mRoom.put(rooms.get(i).getID(), rooms.get(i));
		}
		
		for(int i = 0; i < n; i++){
			JuryInfo J = jury.get(i);
			if(J.getRoomId() > 0){
				if(J.getExaminerId1() > 0) nonHustOfRoom.get(J.getRoomId()).add(J.getExaminerId1());
				if(J.getAdditionalMemberId() > 0) nonHustOfRoom.get(J.getRoomId()).add(J.getAdditionalMemberId());
			}
			if(J.getExaminerId1() > 0) occ.put(J.getExaminerId1(), occ.get(J.getExaminerId1())+1);
			if(J.getAdditionalMemberId() > 0) occ.put(J.getAdditionalMemberId(), occ.get(J.getAdditionalMemberId())+1);
		}

		nbObj = 2;
		bestEval = new int[nbObj];
		for(int i = 0; i < nbObj; i++)
			bestEval[i] = 1000000;
		
		t0 = System.currentTimeMillis();
		
		System.out.println("AssignExaminer1::search Start TRY.....");
		TRY(0);
		System.out.println("AssignExaminer1::search TRY finished.....");
		
		for(int i = 0; i < n; i++){
			JuryInfo J = jury.get(i);
			J.setExaminerId1(opt_x[i]);
		}
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
