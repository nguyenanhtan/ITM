package algorithms.backtracksearch;

import java.util.*;

import DataEntity.*;

public class AssignNonHustToJuryRooms {

	/**
	 * @param args
	 */
	
	private Vector<JuryInfo> jury;
	Vector<Room> rooms;
	private int n;// size of jury
	private Vector<Teacher> nonHust;
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
	public AssignNonHustToJuryRooms(){
		
	}
	
	public boolean foundSolution(){ return this.foundSolution;}
	
	/*
	public void assignNonHustMembersToJuryRooms(Vector<JuryInfo> jury, Vector<Teacher> nonHust, int nbJuries, double timeLimit){
		
		this.jury = jury;
		this.timeLimit = 1000*timeLimit;// convert seconds into milliseconds
		n = jury.size();
		m = nonHust.size();
		this.nonHust = nonHust;
		this.nbJuries = nbJuries;
		System.out.println("AssignNonHustMembersToJuryRooms::assignNonHustMembersToJuryRooms, jury.sz = " + n + 
				", nonHust.sz = " + m + ", nbJuries = " + nbJuries);
		present = new HashMap<Integer, Integer>();
		for(int i = 0; i < nonHust.size(); i++){
			Teacher t = nonHust.get(i);
			present.put(t.getID(), -1);
		}
		
		HashSet[] M = new HashSet[nbJuries+1];// M[v] is the set of members assigned to room v
		for(int v = 1; v <= nbJuries; v++){
			M[v] = new HashSet<Integer>();
		}
		for(int i = 0; i < n; i++){
			JuryInfo J = jury.get(i);
			if(J.getExaminerId1() > 0){
				M[J.getRoomId()].add(J.getExaminerId1());
			}
			if(J.getAdditionalMemberId() > 0){
				M[J.getRoomId()].add(J.getAdditionalMemberId());
			}
		}

		count = new int[nbJuries+1];
		for(int v = 1; v <= nbJuries; v++)
			count[v] = M[v].size();//0;
	
		for(int i = 0; i < n; i++){
			JuryInfo J = jury.get(i);
			if(J.getExaminerId1() > 0){
				present.put(J.getExaminerId1(), i);
				//count[J.getRoomId()]++;
			}
			if(J.getAdditionalMemberId() > 0){
				present.put(J.getAdditionalMemberId(), i);
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
				Teacher t = nonHust.get(i);
				if(present.get(t.getID()) < 0){
					for(int v = 1; v <= nbJuries; v++)
						domain[i].add(v);
				}else{
					JuryInfo J = jury.get(present.get(t.getID()));
					domain[i].add(J.getRoomId());
				}
		}
		
		System.out.print("AssignNonHustToJuryRooms:: before TRY, NonHust to be assigned = ");
		for(int i = 0; i < m; i++)
			if(present.get(nonHust.get(i).getID()) < 0)
				System.out.print(nonHust.get(i).getName() + ",");
		System.out.println();
		
		t0 = System.currentTimeMillis();
		bestEval = 10000;
		foundSolution = false;
		TRY(0);
		System.out.println("AssignNonHustMembersToJuryRooms::assign --> RESULT (" + foundSolution + ")");
		for(int i = 0; i < m; i++){
			System.out.print(opt_x[i] + ",");
		}
		System.out.println();
		if(!foundSolution) return;
		
		
		for(int i = 0; i < m; i++){
			Teacher t = nonHust.get(i);
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
			AssignNonHustToAJuryRoom A = new AssignNonHustToAJuryRoom();
			A.assign(jv, M[v], timeLimit);
		}
	}
	*/
	
	public void assignNonHustMembersToJuryRooms(Vector<JuryInfo> jury, Vector<Teacher> nonHust, Vector<Room> rooms, double timeLimit){
		
		this.jury = jury;
		this.timeLimit = 1000*timeLimit;// convert seconds into milliseconds
		n = jury.size();
		m = nonHust.size();
		this.nonHust = nonHust;
		this.rooms = rooms;
		this.nbJuries = rooms.size();//nbJuries;
		//System.out.println("AssignNonHustMembersToJuryRooms::assignNonHustMembersToJuryRooms, jury.sz = " + n + 
				//", nonHust.sz = " + m + ", nbJuries = " + nbJuries);
		present = new HashMap<Integer, Integer>();
		for(int i = 0; i < nonHust.size(); i++){
			Teacher t = nonHust.get(i);
			present.put(t.getID(), -1);
		}
		
		HashSet[] M = new HashSet[nbJuries+1];// M[v] is the set of members assigned to room v
		for(int v = 1; v <= nbJuries; v++){
			M[v] = new HashSet<Integer>();
		}
		for(int i = 0; i < n; i++){
			JuryInfo J = jury.get(i);
			if(J.getExaminerId1() > 0){
				M[J.getRoomId()].add(J.getExaminerId1());
			}
			if(J.getAdditionalMemberId() > 0){
				M[J.getRoomId()].add(J.getAdditionalMemberId());
			}
		}
		HashSet[] supervisors = new HashSet[nbJuries+1];
		for(int v= 1; v <= nbJuries; v++){
			supervisors[v] = new HashSet<Integer>();
		}
		for(int i = 0; i < n; i++){
			JuryInfo J = jury.get(i);
			int r = J.getRoomId();
			supervisors[r].add(J.getSupervisorId());
		}

		count = new int[nbJuries+1];
		for(int v = 1; v <= nbJuries; v++)
			count[v] = M[v].size();//0;
	
		for(int i = 0; i < n; i++){
			JuryInfo J = jury.get(i);
			if(J.getExaminerId1() > 0){
				present.put(J.getExaminerId1(), i);
				//count[J.getRoomId()]++;
			}
			if(J.getAdditionalMemberId() > 0){
				present.put(J.getAdditionalMemberId(), i);
				//count[J.getRoomId()]++;
			}
		}
		
		//for(int v = 1; v <= nbJuries; v++){
			//System.out.println("count[" + v + "] = " + count[v]);
		//}
		x = new int[m];
		opt_x = new int[m];
		domain = new Vector[m];
		for(int i = 0; i < m; i++){
				domain[i] = new Vector<Integer>();
				Teacher t = nonHust.get(i);
				if(present.get(t.getID()) < 0){
					for(int v = 1; v <= nbJuries; v++)
						if(!supervisors[v].contains(t.getID()) || rooms.get(v-1).getMaxNbNonHustMembers() >= 3) 
							domain[i].add(v);
				}else{
					JuryInfo J = jury.get(present.get(t.getID()));
					domain[i].add(J.getRoomId());
				}
		}
		/*
		System.out.print("AssignNonHustToJuryRooms:: before TRY, NonHust to be assigned = ");
		for(int i = 0; i < m; i++)
			if(present.get(nonHust.get(i).getID()) < 0)
				System.out.print(nonHust.get(i).getName() + ",");
		System.out.println();
		*/
		t0 = System.currentTimeMillis();
		bestEval = 10000;
		foundSolution = false;
		TRY(0);
		/*
		System.out.println("AssignNonHustMembersToJuryRooms::assign --> RESULT (" + foundSolution + ")");
		for(int i = 0; i < m; i++){
			System.out.print(opt_x[i] + ",");
		}
		System.out.println();
		*/
		if(!foundSolution) return;
		
		
		for(int i = 0; i < m; i++){
			Teacher t = nonHust.get(i);
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
			AssignNonHustToAJuryRoom A = new AssignNonHustToAJuryRoom();
			A.assign(jv, M[v], timeLimit);
		}
	}

	public boolean canAssignNonHustMembersToJuryRooms(Vector<JuryInfo> jury, Vector<Teacher> nonHust, Vector<Room> rooms, double timeLimit){
		
		this.jury = jury;
		this.timeLimit = 1000*timeLimit;// convert seconds into milliseconds
		n = jury.size();
		m = nonHust.size();
		this.nonHust = nonHust;
		this.rooms = rooms;
		this.nbJuries = rooms.size();//nbJuries;
		//System.out.println("AssignNonHustMembersToJuryRooms::assignNonHustMembersToJuryRooms, jury.sz = " + n + 
			//	", nonHust.sz = " + m + ", nbJuries = " + nbJuries);
		present = new HashMap<Integer, Integer>();
		for(int i = 0; i < nonHust.size(); i++){
			Teacher t = nonHust.get(i);
			present.put(t.getID(), -1);
		}
		
		HashSet[] M = new HashSet[nbJuries+1];// M[v] is the set of members assigned to room v
		for(int v = 1; v <= nbJuries; v++){
			M[v] = new HashSet<Integer>();
		}
		for(int i = 0; i < n; i++){
			JuryInfo J = jury.get(i);
			if(J.getExaminerId1() > 0){
				M[J.getRoomId()].add(J.getExaminerId1());
			}
			if(J.getAdditionalMemberId() > 0){
				M[J.getRoomId()].add(J.getAdditionalMemberId());
			}
		}
		HashSet[] supervisors = new HashSet[nbJuries+1];
		for(int v= 1; v <= nbJuries; v++){
			supervisors[v] = new HashSet<Integer>();
		}
		for(int i = 0; i < n; i++){
			JuryInfo J = jury.get(i);
			int r = J.getRoomId();
			supervisors[r].add(J.getSupervisorId());
		}

		count = new int[nbJuries+1];
		for(int v = 1; v <= nbJuries; v++)
			count[v] = M[v].size();//0;
	
		for(int i = 0; i < n; i++){
			JuryInfo J = jury.get(i);
			if(J.getExaminerId1() > 0){
				present.put(J.getExaminerId1(), i);
				//count[J.getRoomId()]++;
			}
			if(J.getAdditionalMemberId() > 0){
				present.put(J.getAdditionalMemberId(), i);
				//count[J.getRoomId()]++;
			}
		}
		
		//for(int v = 1; v <= nbJuries; v++){
			//System.out.println("count[" + v + "] = " + count[v]);
		//}
		x = new int[m];
		opt_x = new int[m];
		domain = new Vector[m];
		for(int i = 0; i < m; i++){
				domain[i] = new Vector<Integer>();
				Teacher t = nonHust.get(i);
				if(present.get(t.getID()) < 0){
					for(int v = 1; v <= nbJuries; v++)
						if(!supervisors[v].contains(t.getID()) || rooms.get(v-1).getMaxNbNonHustMembers() >= 3) domain[i].add(v);
				}else{
					JuryInfo J = jury.get(present.get(t.getID()));
					domain[i].add(J.getRoomId());
				}
		}
		/*
		System.out.print("AssignNonHustToJuryRooms:: before TRY, NonHust to be assigned = ");
		for(int i = 0; i < m; i++)
			if(present.get(nonHust.get(i).getID()) < 0)
				System.out.print(nonHust.get(i).getName() + ",");
		System.out.println();
		*/
		
		t0 = System.currentTimeMillis();
		bestEval = 10000;
		foundSolution = false;
		
		TRY(0);
		
		/*
		System.out.println("AssignNonHustMembersToJuryRooms::assign --> RESULT (" + foundSolution + ")");
		for(int i = 0; i < m; i++){
			System.out.print(opt_x[i] + ",");
		}
		System.out.println();
		*/
		return foundSolution;
		
		
	}
	
	
	private int eval(){
		return 0;
	}
	private void updateBest(){
		int e = eval();
		if(bestEval > e){
			//System.out.println("AssignNonHustToJuryRoom::assign, updateBest, bestEval = " + bestEval + ", e = " + e);
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
		Teacher t = nonHust.get(k);
		int tid = t.getID();
		
		System.out.println("TRY(" + k + "), tid = " + tid + ", domain sz = " + domain[k].size());
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
				if(count[v] >= rooms.get(v-1).getMaxNbNonHustMembers() && present.get(tid) < 0) continue;
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
