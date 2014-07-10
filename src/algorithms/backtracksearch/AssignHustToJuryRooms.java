package algorithms.backtracksearch;

import java.util.*;
import DataEntity.*;

public class AssignHustToJuryRooms {

	/**
	 * @param args
	 */
	
	private Vector<JuryInfo> jury;
	Vector<Room> rooms;
	private int n;// size of jury
	private Vector<Teacher> hust;
	private int m;// size of nonHust
	int[] x;// x[i] is the jury-room to which the teacher nonHust[i] is assigned
	int[] opt_x;
	Vector[] domain;// domain[i] is the domain of x[i];
	int bestEval;
	int nbJuries;
	int[] count;// count[v] is the number of teachers in jury-room v
	HashMap<Integer, Integer> present = null;
	double t0;
	double timeLimit;
	boolean foundSolution;
	public AssignHustToJuryRooms(){
		
	}
	
	public boolean foundSolution(){ return this.foundSolution;}
	
	/*
	public void assignHustMembersToJuryRooms(Vector<JuryInfo> jury, Vector<Teacher> hust, int nbJuries, int timeLimit){
		
		this.jury = jury;
		this.timeLimit = 1000*timeLimit;// convert seconds into milliseconds
		n = jury.size();
		m = hust.size();
		this.hust = hust;
		this.nbJuries = nbJuries;
		System.out.println("AssignHustMembersToJuryRooms::assignHustMembersToJuryRooms, jury.sz = " + n + 
				", hust.sz = " + m + ", nbJuries = " + nbJuries);
		present = new HashMap<Integer, Integer>();
		for(int i = 0; i < hust.size(); i++){
			Teacher t = hust.get(i);
			present.put(t.getID(), -1);
		}
		
		HashSet[] M = new HashSet[nbJuries+1];// M[v] is the set of members assigned to room v
		for(int v = 1; v <= nbJuries; v++){
			M[v] = new HashSet<Integer>();
		}
		for(int i = 0; i < n; i++){
			JuryInfo J = jury.get(i);
			if(J.getExaminerId2() > 0){
				M[J.getRoomId()].add(J.getExaminerId2());
			}
			if(J.getPresidentId() > 0){
				M[J.getRoomId()].add(J.getPresidentId());
			}
			if(J.getSecretaryId() > 0){
				M[J.getRoomId()].add(J.getSecretaryId());
			}
		}

		count = new int[nbJuries+1];
		for(int v = 1; v <= nbJuries; v++)
			count[v] = M[v].size();//0;
	
		HashSet[] supervisors = new HashSet[nbJuries+1];
		for(int v= 1; v <= nbJuries; v++){
			supervisors[v] = new HashSet<Integer>();
		}
		for(int i = 0; i < n; i++){
			JuryInfo J = jury.get(i);
			int r = J.getRoomId();
			supervisors[r].add(J.getSupervisorId());
		}
		
		for(int i = 0; i < n; i++){
			JuryInfo J = jury.get(i);
			if(J.getExaminerId2() > 0){
				present.put(J.getExaminerId2(), i);
				//count[J.getRoomId()]++;
			}
			if(J.getPresidentId() > 0){
				present.put(J.getPresidentId(), i);
				//count[J.getRoomId()]++;
			}
			if(J.getSecretaryId() > 0){
				present.put(J.getSecretaryId(), i);
				//count[J.getRoomId()]++;
			}
		}
		
		for(int v = 1; v <= nbJuries; v++){
			System.out.println("count[" + v + "] = " + count[v]);
		}
		x = new int[m];
		opt_x = new int[m];
		domain = new Vector[m];
		for(int i = 0; i < m; i++){
				domain[i] = new Vector<Integer>();
				int tid = hust.get(i).getID();
				if(present.get(tid) < 0){
					for(int v = 1; v <= nbJuries; v++){
						if(!supervisors[v].contains(tid)) domain[i].add(v);
					}
				}else{
					JuryInfo J = jury.get(present.get(tid));
					domain[i].add(J.getRoomId());
				}
		}
		
		System.out.print("AssignNonHustToJuryRooms:: before TRY, hust to be assigned = ");
		for(int i = 0; i < m; i++)
			if(present.get(hust.get(i).getID()) < 0)
				System.out.print(hust.get(i).getName() + ",");
		System.out.println();
		
		t0 = System.currentTimeMillis();
		bestEval = 10000;
		foundSolution = false;
		TRY(0);
		System.out.println("AssignHustMembersToJuryRooms::assign --> RESULT (" + foundSolution + ")");
		for(int i = 0; i < m; i++){
			System.out.print(opt_x[i] + ",");
		}
		System.out.println();
		if(!foundSolution) return;
		
		
		for(int i = 0; i < m; i++){
			Teacher t = hust.get(i);
			if(present.get(t.getID()) < 0){
				int v = opt_x[i];// room-jury to which non hust i is assigned
				M[v].add(t.getID());
			}
		}
		
		for(int v = 1; v <= nbJuries; v++){
			//to be complete here
			Vector<JuryInfo> jv = new Vector<JuryInfo>();
			for(int i = 0; i < n; i++){
				JuryInfo J = jury.get(i);
				if(J.getRoomId() == v){
					jv.add(J);
				}
			}
			// assign members of M[v] to jv in order to balance
			AssignHustToAJuryRoom A = new AssignHustToAJuryRoom();
			A.assign(jv, M[v], timeLimit);
		}
	}
	*/
	public void assignHustMembersToJuryRooms(Vector<JuryInfo> jury, Vector<Teacher> hust, Vector<Room> rooms, double timeLimit){
		
		this.jury = jury;
		this.timeLimit = 1000*timeLimit;// convert seconds into milliseconds
		n = jury.size();
		m = hust.size();
		this.hust = hust;
		this.rooms = rooms;
		this.nbJuries = rooms.size();//nbJuries;
		System.out.println("AssignHustMembersToJuryRooms::assignHustMembersToJuryRooms, jury.sz = " + n + 
				", hust.sz = " + m + ", nbJuries = " + nbJuries);
		
		for(int i = 0; i < n; i++){
			JuryInfo J = jury.get(i);
			System.out.println(J.getSupervisorId() + "\t" + J.getExaminerId1() + "\t" + J.getExaminerId2() + "\t" + J.getPresidentId() + 
					"\t" + J.getSecretaryId() + "\t" + J.getAdditionalMemberId() + "\t" + J.getSlotId() + "\t" + J.getRoomId());
		}
		present = new HashMap<Integer, Integer>();
		for(int i = 0; i < hust.size(); i++){
			Teacher t = hust.get(i);
			present.put(t.getID(), -1);
		}
		
		HashMap<Integer, Teacher> mTeacher = new HashMap<Integer, Teacher>();
		for(int i = 0; i < hust.size(); i++){
			mTeacher.put(hust.get(i).getID(), hust.get(i));
		}
		
		HashSet[] M = new HashSet[nbJuries+1];// M[v] is the set of members assigned to room v
		for(int v = 1; v <= nbJuries; v++){
			M[v] = new HashSet<Integer>();
		}
		for(int i = 0; i < n; i++){
			JuryInfo J = jury.get(i);
			if(J.getExaminerId2() > 0){
				M[J.getRoomId()].add(J.getExaminerId2());
			}
			if(J.getPresidentId() > 0){
				M[J.getRoomId()].add(J.getPresidentId());
			}
			if(J.getSecretaryId() > 0){
				M[J.getRoomId()].add(J.getSecretaryId());
			}
		}

		count = new int[nbJuries+1];
		for(int v = 1; v <= nbJuries; v++)
			count[v] = M[v].size();//0;
	
		HashSet[] supervisors = new HashSet[nbJuries+1];
		for(int v= 1; v <= nbJuries; v++){
			supervisors[v] = new HashSet<Integer>();
		}
		for(int i = 0; i < n; i++){
			JuryInfo J = jury.get(i);
			int r = J.getRoomId();
			supervisors[r].add(J.getSupervisorId());
		}
		
		for(int i = 0; i < n; i++){
			JuryInfo J = jury.get(i);
			if(J.getExaminerId2() > 0){
				present.put(J.getExaminerId2(), i);
				//count[J.getRoomId()]++;
			}
			if(J.getPresidentId() > 0){
				present.put(J.getPresidentId(), i);
				//count[J.getRoomId()]++;
			}
			if(J.getSecretaryId() > 0){
				present.put(J.getSecretaryId(), i);
				//count[J.getRoomId()]++;
			}
		}
		
		System.out.print("Hust = ");
		for(int i = 0; i < m; i++){
			System.out.print(hust.get(i).getID() + ",");
		}
		System.out.println();
		
		for(int v = 1; v <= nbJuries; v++){
			System.out.println("count[" + v + "] = " + count[v] + ", maxNbHust = " + rooms.get(v-1).getMaxNbHustMembers());
			Iterator it = supervisors[v].iterator();
			System.out.print("supervisor[" + v + "] = ");
			while(it.hasNext()){
				int s = (Integer)it.next();
				System.out.print(s + ",");
			}
			System.out.println();
		}
		x = new int[m];
		opt_x = new int[m];
		domain = new Vector[m];
		for(int i = 0; i < m; i++){
				domain[i] = new Vector<Integer>();
				int tid = hust.get(i).getID();
				if(present.get(tid) < 0){
					for(int v = 1; v <= nbJuries; v++){
						if(!supervisors[v].contains(tid) || rooms.get(v-1).getMaxNbHustMembers() >= 4) 
							domain[i].add(v);
					}
				}else{
					JuryInfo J = jury.get(present.get(tid));
					domain[i].add(J.getRoomId());
				}
		}
		/*
		System.out.print("AssignNonHustToJuryRooms:: before TRY, hust to be assigned = ");
		for(int i = 0; i < m; i++)
			if(present.get(hust.get(i).getID()) < 0)
				System.out.print(hust.get(i).getName() + ",");
		System.out.println();
		*/
		System.out.println("AssingHustToJuryRooms, number of hust members = " + m + ", number of rooms = " + rooms.size());
		for(int i = 0; i < m; i++){
			System.out.print("domain[" + i + ",ID:" + hust.get(i).getID() + "] = ");
			for(int j = 0; j < domain[i].size(); j++) System.out.print(domain[i].get(j) + " ");
			System.out.println();
		}
		t0 = System.currentTimeMillis();
		bestEval = 10000;
		foundSolution = false;
		TRY(0);
		System.out.println("AssignHustMembersToJuryRooms::assign --> RESULT (" + foundSolution + ")");
		for(int i = 0; i < m; i++){
			System.out.print(opt_x[i] + ",");
		}
		System.out.println();
		if(!foundSolution) return;
		
		
		for(int i = 0; i < m; i++){
			Teacher t = hust.get(i);
			if(present.get(t.getID()) < 0){
				int v = opt_x[i];// room-jury to which non hust i is assigned
				M[v].add(t.getID());
			}
		}
		
		for(int v = 1; v <= nbJuries; v++){
			//to be complete here
			Vector<JuryInfo> jv = new Vector<JuryInfo>();
			for(int i = 0; i < n; i++){
				JuryInfo J = jury.get(i);
				if(J.getRoomId() == v){
					jv.add(J);
				}
			}
			// assign members of M[v] to jv in order to balance
			AssignHustToAJuryRoom A = new AssignHustToAJuryRoom();
			A.assign(jv, M[v], timeLimit);
			if(!A.foundSolution()) return;
			
			for(int i = 0; i < jv.size(); i++){
				JuryInfo J = jv.get(i);
				J.setSlotId(i+1);
				int p = J.getPresidentId();
				int s = J.getSecretaryId();
				Teacher tp = mTeacher.get(p);
				Teacher ts = mTeacher.get(s);
				if(tp.getExpertLevel() > ts.getExpertLevel()){
					J.setPresidentId(s);
					J.setSecretaryId(p);
				}
			}
		}
	}
	public boolean canAssignHustMembersToJuryRooms(Vector<JuryInfo> jury, Vector<Teacher> hust, Vector<Room> rooms, double timeLimit){
		
		this.jury = jury;
		this.timeLimit = 1000*timeLimit;// convert seconds into milliseconds
		n = jury.size();
		m = hust.size();
		this.hust = hust;
		this.rooms = rooms;
		this.nbJuries = rooms.size();//nbJuries;
		//System.out.println("AssignHustMembersToJuryRooms::assignHustMembersToJuryRooms, jury.sz = " + n + 
				//", hust.sz = " + m + ", nbJuries = " + nbJuries);
		present = new HashMap<Integer, Integer>();
		for(int i = 0; i < hust.size(); i++){
			Teacher t = hust.get(i);
			present.put(t.getID(), -1);
		}
		
		HashSet[] M = new HashSet[nbJuries+1];// M[v] is the set of members assigned to room v
		for(int v = 1; v <= nbJuries; v++){
			M[v] = new HashSet<Integer>();
		}
		for(int i = 0; i < n; i++){
			JuryInfo J = jury.get(i);
			if(J.getExaminerId2() > 0){
				M[J.getRoomId()].add(J.getExaminerId2());
			}
			if(J.getPresidentId() > 0){
				M[J.getRoomId()].add(J.getPresidentId());
			}
			if(J.getSecretaryId() > 0){
				M[J.getRoomId()].add(J.getSecretaryId());
			}
		}

		count = new int[nbJuries+1];
		for(int v = 1; v <= nbJuries; v++)
			count[v] = M[v].size();//0;
	
		HashSet[] supervisors = new HashSet[nbJuries+1];
		for(int v= 1; v <= nbJuries; v++){
			supervisors[v] = new HashSet<Integer>();
		}
		for(int i = 0; i < n; i++){
			JuryInfo J = jury.get(i);
			int r = J.getRoomId();
			supervisors[r].add(J.getSupervisorId());
		}
		
		for(int i = 0; i < n; i++){
			JuryInfo J = jury.get(i);
			if(J.getExaminerId2() > 0){
				present.put(J.getExaminerId2(), i);
				//count[J.getRoomId()]++;
			}
			if(J.getPresidentId() > 0){
				present.put(J.getPresidentId(), i);
				//count[J.getRoomId()]++;
			}
			if(J.getSecretaryId() > 0){
				present.put(J.getSecretaryId(), i);
				//count[J.getRoomId()]++;
			}
		}
		
		//System.out.print("Hust = ");
		//for(int i = 0; i < m; i++){
			//System.out.print(hust.get(i).getID() + ",");
		//}
		//System.out.println();
		
		for(int v = 1; v <= nbJuries; v++){
			//System.out.println("count[" + v + "] = " + count[v] + ", maxNbHust = " + rooms.get(v-1).getMaxNbHustMembers());
			Iterator it = supervisors[v].iterator();
			//System.out.println("supervisor[" + v + "] = ");
			//while(it.hasNext()){
				//int s = (Integer)it.next();
				//System.out.print(s + ",");
			//}
			//System.out.println();
		}
		x = new int[m];
		opt_x = new int[m];
		domain = new Vector[m];
		for(int i = 0; i < m; i++){
				domain[i] = new Vector<Integer>();
				int tid = hust.get(i).getID();
				if(present.get(tid) < 0){
					for(int v = 1; v <= nbJuries; v++){
						if(!supervisors[v].contains(tid) || rooms.get(v-1).getMaxNbHustMembers() >= 4) 
							domain[i].add(v);
					}
				}else{
					JuryInfo J = jury.get(present.get(tid));
					domain[i].add(J.getRoomId());
				}
		}
		/*
		System.out.print("AssignNonHustToJuryRooms:: before TRY, hust to be assigned = ");
		for(int i = 0; i < m; i++)
			if(present.get(hust.get(i).getID()) < 0)
				System.out.print(hust.get(i).getName() + ",");
		System.out.println();
		*/
		
		/*
		System.out.println("AssingHustToJuryRooms, number of hust members = " + m + ", number of rooms = " + rooms.size());
		for(int i = 0; i < m; i++){
			System.out.print("domain[" + i + ",ID:" + hust.get(i).getID() + "] = ");
			for(int j = 0; j < domain[i].size(); j++) System.out.print(domain[i].get(j) + " ");
			System.out.println();
		}
		*/
		
		t0 = System.currentTimeMillis();
		bestEval = 10000;
		foundSolution = false;
		TRY(0);
		/*
		System.out.println("AssignHustMembersToJuryRooms::assign --> RESULT (" + foundSolution + ")");
		for(int i = 0; i < m; i++){
			System.out.print(opt_x[i] + ",");
		}
		System.out.println();
		*/
		if(!foundSolution) return false;
		else return true;
	}

	private int eval(){
		return 0;
	}
	private void updateBest(){
		int e = eval();
		if(bestEval > e){
			//System.out.println("AssignHustToJuryRooms::assign, updateBest, bestEval = " + bestEval + ", e = " + e);
			foundSolution = true;
			bestEval = e;
			for(int i = 0; i < m; i++){
				opt_x[i] = x[i];
			}
		}
	}
	private void TRY(int k){
		if(System.currentTimeMillis() - t0 > timeLimit) return;
		if(bestEval == 0) return;
		Teacher t = hust.get(k);
		int tid = t.getID();
		
		//System.out.println("TRY(" + k + "), tid = " + tid + ", domain sz = " + domain[k].size());
		/*
		if(present.get(tid) > 0){
			if(k == m-1)
				updateBest();
			else
				TRY(k+1);
		}else{
		*/
			for(int j = 0; j < domain[k].size(); j++){
				int v = (Integer)domain[k].get(j);
				if(count[v] >= rooms.get(v-1).getMaxNbHustMembers() && present.get(tid) < 0) continue;
				x[k] = v;
				if(present.get(tid) < 0)
					count[v]++;
				if(k == m-1)
					updateBest();
				else
					TRY(k+1);
				if(present.get(tid) < 0)
					count[v]--;
				
			}
		//}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
