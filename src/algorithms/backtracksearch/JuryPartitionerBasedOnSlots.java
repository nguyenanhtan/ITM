package algorithms.backtracksearch;

import java.util.*;

import utils.Configure;
import utils.Utility;

import DataEntity.*;

import DataEntity.JuryDataForScheduling;

public class JuryPartitionerBasedOnSlots {

	/**
	 * @param args
	 * 
	 * 
	 */
	Vector<Teacher> hustTeachers = null;
	Vector<Teacher> nonHustTeachers = null;
	Vector<Teacher> AT = null;
	int m;
	int nbJuries;
	Vector[] domain;
	int[] x;
	int[] bestEval;
	int nbObj;
	int[] opt_x;
	HashMap[] occT;// occT[v].get(t) is the number of occurrences of teacher t in slot v
	int[] occSlot;// occSlot[sl] is the number of occurrences of slot sl in the schedule
	int[] maxOccSlot;
	HashSet[] teachersOfSlot;// teachersOfSlot[i] is the set of teachers assigned to slot i
	HashSet<Integer> fixedTeachers;
	
	HashMap[] matchJuryProfessors;// matchJuryProfessors[i].get(j) is the match score of jury i and professor j
	int[][] matchJuryJury;
	
	//int[] maxOccJ;
	Vector<JuryInfo> jury = null;
	Vector<Room> rooms;
	private int maxSlot;
	double t0;
	double timeLimit;
	boolean foundSolution;
	
	
	public boolean foundSolution(){
		return this.foundSolution;
	}
	public Vector<JuryDataForScheduling> partitionJury(Vector<JuryInfo> jury, Vector<Room> rooms, Vector<Teacher> hustTeachers, Vector<Teacher> nonHustTeachers, int timeLimit){
		System.out.println("JuryPartitionerBasedOnSlots::partitionJury");
		this.jury = jury;
		this.rooms = rooms;
		this.hustTeachers = hustTeachers;
		this.nonHustTeachers = nonHustTeachers;
		
		this.nbJuries = rooms.size();//nbJuries;
		this.timeLimit = timeLimit*1000;
		Vector<JuryDataForScheduling> juryDataArr = new Vector<JuryDataForScheduling>();
		
		int n = jury.size();
		int sz_of_jury = n/nbJuries;
		int d = n%nbJuries;
		int sz_of_nonHust = nonHustTeachers.size()/nbJuries;
		int dNonHust = nonHustTeachers.size()%nbJuries;
		
		AT = new Vector<Teacher>();
		for(int i = 0; i < hustTeachers.size(); i++){
			Teacher t = hustTeachers.get(i);
			AT.add(t);
		}
		for(int i = 0; i < nonHustTeachers.size(); i++){
			Teacher t = nonHustTeachers.get(i);
			AT.add(t);
		}

		JuryInfo[] arr = new JuryInfo[n];
		for(int i = 0; i < n; i++){
			JuryInfo J = jury.get(i);
			arr[i] = J;
			if(J.getSlotId() == 0) J.setSlotId(100000);
		}
		
		//sort arr
		for(int i = 0; i < n-1; i++)
			for(int j = i+1; j < n; j++)
				if(arr[i].getSlotId() > arr[j].getSlotId()){
					JuryInfo tj = arr[i]; arr[i] = arr[j]; arr[j] = tj;
				}
		for(int i = 0; i < n; i++){
			if(arr[i].getSlotId() == 100000) arr[i].setSlotId(0);
		}
		
		jury.clear();
		for(int i = 0; i < n; i++)
			jury.add(arr[i]);
		
		
		for(int i = 0; i < n; i++){
			JuryInfo J = jury.get(i);
			System.out.println(J.getSupervisorId() + "\t" + J.getExaminerId1() + "\t" + J.getExaminerId2() + "\t" + 
			J.getPresidentId() + 
					"\t" + J.getSecretaryId() + "\t" + J.getAdditionalMemberId() + "\t" + J.getSlotId() + "\t" + J.getRoomId());
		}
		
		
		matchJuryProfessors = Utility.computeMatchJuryProfessor(jury, AT);
		
		
		matchJuryJury = Utility.computeMatchJuryJury(jury);
		
		
		HashMap<Integer, Integer> occHT = new HashMap<Integer, Integer>();
		int maxTeacherID  = -1;
		Vector<Teacher> AT = new Vector<Teacher>();
		for(int i = 0; i < hustTeachers.size(); i++){
			Teacher t = hustTeachers.get(i);
			//occHT.put(t.getID(), 0);
			AT.add(t);
			if(t.getID() > maxTeacherID) maxTeacherID = t.getID();
		}
		for(int i = 0; i < nonHustTeachers.size(); i++){
			Teacher t = nonHustTeachers.get(i);
			//occHT.put(t.getID(), 0);
			AT.add(t);
			if(t.getID() > maxTeacherID) maxTeacherID = t.getID();
		}
		
		fixedTeachers = new HashSet<Integer>();
		for(int i = 0; i < jury.size(); i++){
			JuryInfo J = jury.get(i);
			if(J.getExaminerId1() > 0) fixedTeachers.add(J.getExaminerId1());
			if(J.getExaminerId2() > 0) fixedTeachers.add(J.getExaminerId2());
			if(J.getPresidentId() > 0) fixedTeachers.add(J.getPresidentId());
			if(J.getSecretaryId() > 0) fixedTeachers.add(J.getSecretaryId());
			if(J.getAdditionalMemberId() > 0) fixedTeachers.add(J.getAdditionalMemberId());
			
		}
		
		m = jury.size();
		x = new int[m];
		opt_x = new int[m];
		domain = new Vector[m];
		
		/*
		maxSlot = 0;
		for(int i = 0; i < rooms.size(); i++){
			Room r = rooms.get(i);
			int nbSlots = r.getMaxNbJuries();
			if(nbSlots > maxSlot) maxSlot = nbSlots;
		}
		*/
		
		if(jury.size()%rooms.size() == 0) maxSlot = jury.size()/rooms.size();
		else maxSlot = maxSlot = jury.size()/rooms.size() + 1;
		
		teachersOfSlot = new HashSet[maxSlot+1];
		for(int sl = 1; sl <= maxSlot; sl++){
			teachersOfSlot[sl] = new HashSet<Integer>();
		}
		
		for(int i = 0; i < jury.size(); i++){
			JuryInfo J = jury.get(i);
			if(J.getSlotId() > 0){
				//if(J.getSupervisorId() > 0) teachersOfSlot[J.getSlotId()].add(J.getSupervisorId());
				if(J.getExaminerId1() > 0) teachersOfSlot[J.getSlotId()].add(J.getExaminerId1());
				if(J.getExaminerId2() > 0) teachersOfSlot[J.getSlotId()].add(J.getExaminerId2());
				if(J.getPresidentId() > 0) teachersOfSlot[J.getSlotId()].add(J.getPresidentId());
				if(J.getSecretaryId() > 0) teachersOfSlot[J.getSlotId()].add(J.getSecretaryId());
				if(J.getAdditionalMemberId() > 0) teachersOfSlot[J.getSlotId()].add(J.getAdditionalMemberId());
			}
		}
		
		for(int i = 0; i < m; i++){
			JuryInfo J = jury.get(i);
			domain[i] = new Vector<Integer>();
			if(J.getSlotId() > 0){
				domain[i].add(J.getSlotId());
				opt_x[i] = J.getSlotId();
			}else{
				for(int j = 1; j <= maxSlot; j++ )
					domain[i].add(j);
			}
			
			System.out.print("domain[" + i + "] = ");
			for(int j = 0; j < domain[i].size(); j++){
				System.out.print(domain[i].get(j) + " ");
			}
			System.out.println();
		}
		
		
		
		occT = new HashMap[maxSlot+1];
		for(int v = 1; v <= maxSlot; v++){
			occT[v] = new HashMap<Integer, Integer>();
		}
		occSlot = new int[maxSlot+1];
		maxOccSlot = new int[maxSlot+1];
		for(int sl = 1; sl <= maxSlot; sl++){
			occSlot[sl] = 0;
			maxOccSlot[sl] = 0;
		}
		for(int i = 0; i < jury.size(); i++){
			JuryInfo J = jury.get(i);
			if(J.getSlotId() > 0) occSlot[J.getSlotId()]++;
		}
		
		/*
		for(int i = 0; i < rooms.size(); i++){
			Room r = rooms.get(i);
			int nbJuries = r.getMaxNbJuries();
			
		}
		for(int sl = 1; sl <= maxSlot; sl++){
			for(int j = 0; j < rooms.size(); j++){
				Room r = rooms.get(j);
				int nbJuries = r.getMaxNbJuries();
				if(nbJuries >= sl)
					maxOccSlot[sl]++;
			}
		}
		*/
		for(int sl = 1; sl <= maxSlot; sl++){
			maxOccSlot[sl] = rooms.size();
		}
		for(int sl = 1; sl <= maxSlot; sl++){
			System.out.println("occSlot[" + sl + "] = " + occSlot[sl] + ", maxOccSlot[" + sl + "] = " + maxOccSlot[sl]);
		}
		
		t0 = System.currentTimeMillis();
		System.out.println("JuryPartitionerBasedOnSlots::partitionJury, Starting Try, timelimit = " + timeLimit);
		nbObj = 1;
		bestEval = new int[nbObj];
		for(int i = 0; i < nbObj; i++)
			bestEval[i] = -100000;
		
		foundSolution = false;
		
		Try(0);
		System.out.println("JuryPartitionerBasedOnSlots::partitionJury, Try finished, timelimit = " + timeLimit);
		
		arr = new JuryInfo[m];
		for(int i = 0; i < m; i++){
			JuryInfo J = jury.get(i);
			J.setSlotId(opt_x[i]);
			arr[i] = J;
		}
		
		//sort arr
		for(int i = 0; i < m-1; i++)
			for(int j = i+1; j < m; j++)
				if(arr[i].getSlotId() > arr[j].getSlotId()){
					JuryInfo tj = arr[i]; arr[i] = arr[j]; arr[j] = tj;
				}
		
		System.out.println("RESULT:");
		for(int i = 0; i < n; i++){
			JuryInfo J = arr[i];
			System.out.println("Room " + J.getRoomId() + "\t" + J.getSupervisorId() + "\t" + J.getExaminerId1() + "\t" + J.getExaminerId2() + "\t" + 
			J.getPresidentId() + 
					"\t" + J.getSecretaryId() + "\t" + J.getAdditionalMemberId() + "\t" + J.getSlotId() + "\t" + J.getRoomId());
		}
		
		jury.clear();
		for(int i = 0; i < m; i++)
			jury.add(arr[i]);
		
		for(int sl = 1; sl <= maxSlot; sl++){
			Vector<JuryInfo> js = new Vector<JuryInfo>();
			for(int i = 0; i < jury.size(); i++){
				JuryInfo J = jury.get(i);
				if(J.getSlotId() == sl)
					js.add(J);
			}
			
			JuryDataForScheduling jdfs = new JuryDataForScheduling(js, hustTeachers, nonHustTeachers);
			
			//BackTrackSearchAssignTeachersSlotsRooms btsatsr = new BackTrackSearchAssignTeachersSlotsRooms();
			BackTrackSearchTeachersSlotsRoomsObjectiveMatch btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
			//btsatsr.search(js, rooms, hustTeachers, nonHustTeachers, timeLimit);
		}
		return juryDataArr;
	}

	private int[] eval(){
		int[] e = new int[nbObj];
		
		int bestMatchSubject = 0;
		for(int v = 1; v <= maxSlot; v++){
			Vector<Integer> jv = new Vector<Integer>();
			for(int i = 0; i < jury.size(); i++){
				if(x[i] == v)
					jv.add(i);
			}
			for(int i = 0; i < jv.size()-1; i++){
				int ii = jv.get(i);
				for(int j = i+1; j < jv.size(); j++){
					int jj = jv.get(j);
					bestMatchSubject += matchJuryJury[ii][jj];
					//System.out.println("v = " + v + ", ii = " + ii + ", jj = " + jj + ", match = " + matchJuryJury[ii][jj]);
				}
			}
		}
		e[0] = bestMatchSubject;
		
		return e;
	}
	private void updateBest(){
		//System.out.println("JuryPartitioner::updateBest...");
		boolean ok = true;
		foundSolution = true;

		int[] e = eval();
		//if(bestEval > e){
		if(Utility.compare(bestEval,e) == -1){//objective is to maximize to matches between juries assigned to the same slot
			System.out.println("JuryPartitioner::updateBest, bestEval = " + bestEval[0]  + 
					", new eval = " + e[0]);
			bestEval = e;
			for(int j = 0; j < m; j++)
				opt_x[j] = x[j];
		}
		
	}

	
	private void Try(int k){
		if(System.currentTimeMillis() - t0 > timeLimit) return;
		
		
		JuryInfo J = jury.get(k);
		
		Vector<Integer> D = (Vector<Integer>)domain[k];
		//System.out.println("Try(" + k + "), D.sz = " + D.size());
		for(int iv = 0; iv < D.size(); iv++){
			int v = (Integer)D.get(iv);
			boolean ok = true;
			// check is the assignment of jury k to slot v violates any constraint
			//if(J.getSupervisorId() > 0 && teachersOfSlot[v].contains(J.getSupervisorId())) ok = false;
			if(J.getExaminerId1() > 0 && teachersOfSlot[v].contains(J.getExaminerId1())) ok = false;
			if(J.getExaminerId2() > 0 && teachersOfSlot[v].contains(J.getExaminerId2())) ok = false;
			if(J.getPresidentId() > 0 && teachersOfSlot[v].contains(J.getPresidentId())) ok = false;
			if(J.getSecretaryId() > 0 && teachersOfSlot[v].contains(J.getSecretaryId())) ok = false;
			if(J.getAdditionalMemberId() > 0 && teachersOfSlot[v].contains(J.getAdditionalMemberId())) ok = false;
			if(occSlot[v] >= maxOccSlot[v]) ok = false;
			
			if(!ok){
				//System.out.println("Try(" + k + "), D.sz = " + D.size() + " --> continue, m = " + m);
				continue;
			}
			
			x[k] = v;
			occSlot[v] ++;
			
			int[] p = J.getJuryMemberIDs();
			for(int j = 0; j < p.length; j++){
				if(p[j] > 0){
					occT[v].put(p[j], occT[v].get(p[j]+1));
					if((Integer)occT[v].get(p[j]) == 1) teachersOfSlot[v].add(p[j]);
				}
				
			}
			
			if(k == m-1){
				updateBest();
			}else{
				Try(k+1);
			}

			for(int j = 0; j < p.length; j++){
				if(p[j] > 0){
					if((Integer)occT[v].get(p[j]) == 1) teachersOfSlot[v].remove(p[j]);
					occT[v].put(p[j], occT[v].get(p[j]-1));
				}
				
			}
			
			occSlot[v]--;
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
