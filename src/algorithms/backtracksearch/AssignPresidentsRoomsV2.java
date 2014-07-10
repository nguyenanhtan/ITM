package algorithms.backtracksearch;

import java.util.*;

import utils.Utility;
import DataEntity.JuryInfo;
import DataEntity.Room;
import DataEntity.Teacher;

public class AssignPresidentsRoomsV2 {

	/**
	 * @param args
	 */
	
	private Vector<JuryInfo> jury;
	private Vector<Room> rooms;
	private Vector<Teacher> hustTeachers;
	private Vector<Teacher> nonHustTeachers;
	private Vector<Teacher> teachers;
	private HashMap<Integer, Teacher> mTeachers;
	private int timeLimit;
	int n;// size of jury;
	
	int[] x;// decision variables, size of x = n*7 (each jury has 7 decision vars: 5 jury members + slot + room)
	int[] order;// order of variables x to be instantiated
	boolean[] fixed;// fixed[i] = true -> x[order[i]] is preassigned
	int nbVars;// number of variables to be instantiated
	Vector[] domain;// domain[i] is the domain of x[i]
	int m;// size of x: m = 7*n;
	int[] opt_x;
	
	int[] opt_f;// optimal objective values vector
	int nbObj;// number of objectives = size of opt_f
	
	HashMap<Integer, Integer> occTeacher;// occTeacher.get(t) is the number of occurrences in the schedule
	HashMap<Integer, Integer>[] occTeacherRoom;// occTeacherRoom[i].get(t) is the number of occurrence of teacher t in room i
	HashMap<Integer, Integer>[] occTeacherSlot;// occTeacherSlot[i].get(t) is the number of occurrence of teacher t in slot i
	HashMap<Integer, Integer> occTeacherExaminer1;// occTeacherExaminer1.get(t) is the number of occurrences of t as examiner1
	HashMap<Integer, Integer> occTeacherExaminer2;// occTeacherExaminer2.get(t) is the number of occurrences of t as examiner1
	HashMap<Integer, Integer> occRoom;// occRoom.get(r) is the number of occurrences of room r in the schedule
	
	int[] countHustRoom;// countHustRoom[r] is the number of teachers assigned to room r
	int[] countNonHustRoom;// countNonHustRoom[r] is the number of teachers assigned to room r
	int[] countSlotRoom;// countSlotRoom[r] is the number of slot assigned to room r
	
	HashSet[] hustOfRoom;// hustOfRoom[r] is the set of teacher assigned to room r
	HashSet[] nonHustOfRoom;// nonHustOfRoom[r] is the set of teacher assigned to room r
	HashMap<Integer, HashSet<Integer>> hustOfSlot;// hustOfSlot.get(sl) is the set of teacher assigned to slot sl
	HashMap<Integer, HashSet<Integer>> nonHustOfSlot;// nonHustOfSlot.get(sl) is the set of non hust teachers assigned to slot sl
	
	
	HashMap<Integer, Integer> maxNonHustOfRoom;
	HashMap<Integer, Integer> maxHustOfRoom;
	HashMap<Integer, Integer> maxNbJuriesOfRoom;
	HashMap<Integer, Integer> maxOccTeacher;
	
	HashSet[] preAssignedPresidents;
	HashMap<JuryInfo, Integer> mJuryCluster;
	
	HashSet[] slotOfRoom;// slotOfRoom[r] is the set of slots assigned to room r
	
	HashMap[] matchScore;
	int[][] matchScoreJuryJury;
	HashMap<JuryInfo, Integer> mJury;
	
	int maxSlot;
	boolean stop;
	boolean foundSolution;
	double t0;
	int maxDept;
	
	private String objectiveType = "subjects-match";
	
	private void initObjective(){
		System.out.println("BackTrackSearchTeachersSlotsRoomsObjectiveMatch::initObjective()");
		nbObj = 2;
		opt_f = new int[nbObj];
		for(int i = 0; i < nbObj; i++) opt_f[i] = 1000000;
	}
	public void setObjectiveType(String objType){
		this.objectiveType = objType;
	}
	public boolean foundSolution(){
		return this.foundSolution;
	}
	
	private int selectMaxOcc(HashSet<Integer> T, HashMap<Integer, Integer> mOccT){
		int m = -1000;
		int sel_t = -1;
		Iterator it = T.iterator();
		while(it.hasNext()){
			int tid = (Integer)it.next();
			if(mOccT.get(tid) > m){
				m = mOccT.get(tid);
				sel_t = tid;
			}
		}
		if(m <= 0) return -1;
		return sel_t;
	}
	
	private Vector<JuryInfo> selectJuryOfPresident(HashSet<JuryInfo> candJ,int tid){
		Vector<JuryInfo> sel_j = new Vector<JuryInfo>();
		Iterator it = candJ.iterator();
		while(it.hasNext()){
			JuryInfo J = (JuryInfo)it.next();
			if(J.getPresidentId() == tid)
				sel_j.add(J);
		}
		return sel_j;
	}
	private JuryInfo selectMostMatchJury(HashSet<JuryInfo> candJ, Vector<JuryInfo> bin){
		JuryInfo sel_j = null;
		int match = 1000000;
		Iterator it = candJ.iterator();
		while(it.hasNext()){
			JuryInfo J = (JuryInfo)it.next();
			if(J.getPresidentId() > 0) continue;
			int mJ = mJury.get(J);
			int tsc = 0;
			for(int i = 0; i < bin.size(); i++){
				int mI = mJury.get(bin.get(i));
				int sc =  matchScoreJuryJury[mI][mJ];
				tsc += sc;
			}
			if(match > tsc){
				match = tsc;
				sel_j = J;
			}
		}
		return sel_j;
		
	}
	private Vector<JuryInfo> sortJury(Vector<JuryInfo> jury, Vector<Teacher> teachers, Vector<Room> rooms){
		HashMap<Integer, Integer> mOccT = new HashMap<Integer, Integer>();
		Room[] ar = new Room[rooms.size()];
		for(int i = 0; i < rooms.size(); i++)
			ar[i] = rooms.get(i);
		
		//sort rooms
		for(int i = 0; i < rooms.size()-1; i++)
			for(int j = i+1; j < rooms.size(); j++)
				if(ar[i].getMaxNbJuries() < ar[j].getMaxNbJuries()){
					Room tr = ar[i]; ar[i] = ar[j]; ar[j] = tr;
				}
		
		for(int i = 0; i < teachers.size(); i++)
			mOccT.put(teachers.get(i).getID(), 0);
		
		for(int i = 0; i < jury.size(); i++){
			JuryInfo J = jury.get(i);
			if(J.getPresidentId() > 0){
				if(mOccT.get(J.getPresidentId()) == null)
					mOccT.put(J.getPresidentId(), 0);
				else
					mOccT.put(J.getPresidentId(), mOccT.get(J.getPresidentId())+1);
			}
		}
		int po = 0;
		for(int i = 0; i < teachers.size(); i++)
			if(mOccT.get(teachers.get(i).getID()) > 0) po++;
		if(po > rooms.size()) return null;
		
		HashSet<Integer> candT = new HashSet<Integer>();
		for(int i = 0; i < teachers.size(); i++)
			candT.add(teachers.get(i).getID());
		
		HashSet<JuryInfo> candJ = new HashSet<JuryInfo>();
		for(int i = 0; i < jury.size(); i++){
			candJ.add(jury.get(i));
		}
		
		Vector<JuryInfo> sJury = new Vector<JuryInfo>();
		preAssignedPresidents = new HashSet[rooms.size()];
		for(int i = 0; i < rooms.size(); i++){
			preAssignedPresidents[i] = new HashSet<Integer>();
		}
		mJuryCluster = new HashMap<JuryInfo, Integer>();
		
		for(int i = 0; i < rooms.size(); i++){
			int sz = ar[i].getMaxNbJuries();
			Vector<JuryInfo> bin = new Vector<JuryInfo>();
			int tid = selectMaxOcc(candT, mOccT);
			candT.remove(tid);
			System.out.println("AssignPresidentsRoomsV2::sortJury, room " + i + ", sz = " + sz + ", tid = " + tid);
			if(tid > 0 && mOccT.get(tid) > sz) return null;
			
			if(tid > 0)
				preAssignedPresidents[i].add(tid);
			
			// add all jury having president tid into the list
				Vector<JuryInfo> sel_j = selectJuryOfPresident(candJ,tid);
				if(sel_j.size() > sz) return null;
				for(int k = 0; k < sel_j.size(); k++){
					bin.add(sel_j.get(k));
					candJ.remove(sel_j.get(k));
				}
				
				while(bin.size() < sz){
					JuryInfo J = selectMostMatchJury(candJ, bin);
					candJ.remove(J);
					bin.add(J);
					
				}
			for(int j = 0; j < bin.size(); j++){
				sJury.add(bin.get(j));
				mJuryCluster.put(bin.get(j), i);
			}
		}
		return sJury;
	}
	private void initDataStructuresBeforeSearch(Vector<JuryInfo> jury, Vector<Room> rooms, Vector<Teacher> hustTeachers, Vector<Teacher> nonHustTeachers, int timeLimit){
		this.jury = jury;
		this.rooms = rooms;
		this.hustTeachers = hustTeachers;
		this.nonHustTeachers = nonHustTeachers;
		this.timeLimit = timeLimit*1000;
		
		/*
		maxSlot = -1;
		for(int r = 0; r < rooms.size(); r++){
			int nbJuries = rooms.get(r).getMaxNbJuries();
			if(maxSlot < nbJuries) maxSlot = nbJuries;
		}
		*/
		
		
		
		if(jury.size()%rooms.size() == 0) maxSlot = jury.size()/rooms.size();
		else maxSlot = maxSlot = jury.size()/rooms.size() + 1;
		System.out.println("BackTrackSearchTeachersSlotsRoomsObjectiveMatch::search, maxSlot = " + maxSlot);
		
		teachers = new Vector<Teacher>();
		for(int i = 0; i < hustTeachers.size(); i++)
			teachers.add(hustTeachers.get(i));
		for(int i = 0; i < nonHustTeachers.size(); i++)
			teachers.add(nonHustTeachers.get(i));
		
		matchScoreJuryJury = Utility.computeMatchJuryJury(jury); 
		mJury = new HashMap<JuryInfo, Integer>();
		for(int i = 0; i < jury.size(); i++)
			mJury.put(jury.get(i), i);
		
		Vector<JuryInfo> tj = sortJury(jury, teachers, rooms);
		jury.clear();
		for(int i = 0; i < tj.size(); i++)
			jury.add(tj.get(i));
		
		
		System.out.println("AssignPresidentsRoomsV2::initDataStructures, after sorting jury");
		for(int i = 0; i < jury.size(); i++){
			JuryInfo J = jury.get(i);
			System.out.println(J.getPresidentId() + "\t" + J.getRoomId());
		}
		
		
		mTeachers = new HashMap<Integer, Teacher>();
		for(int i = 0; i < teachers.size(); i++){
			mTeachers.put(teachers.get(i).getID(), teachers.get(i));
		}
		matchScore = Utility.computeMatchJuryProfessor(jury, teachers);
		
				
		//System.out.println("matchScore.length = " + matchScore.length);
		n = jury.size();
		m = 7*n;
		x = new int[m];
		//order = new int[m];
		opt_x = new int[m];
		
		domain = new Vector[m];
		for(int i = 0; i < m; i++){
			//order[i] = i;
			domain[i] = new Vector<Integer>();
			int r = i/7;// row (of jury)
			int c = i%7;// column (among 5 members, 1 slot, 1 room)
			JuryInfo J = jury.get(r);
			if(c == 0){// examiner1
				if(J.getExaminerId1() > 0){
					domain[i].add(J.getExaminerId1());
					x[i] = J.getExaminerId1();
					opt_x[i] = x[i];
				}else{
					for(int j = 0; j < nonHustTeachers.size(); j++){
						Teacher t = nonHustTeachers.get(j);
						if(t.getID() != J.getSupervisorId()) domain[i].add(t.getID());
					}
				}
			}else if(c == 4){// additional member
				if(J.getAdditionalMemberId() > 0){
					domain[i].add(J.getAdditionalMemberId());
					x[i] = J.getAdditionalMemberId();
					opt_x[i] = x[i];
				}else{
					for(int j = 0; j < nonHustTeachers.size(); j++){
						Teacher t = nonHustTeachers.get(j);
						if(t.getID() != J.getSupervisorId()) domain[i].add(t.getID());
					}
				}
				
			}else if(c == 1){//  examiner2
				if(J.getExaminerId2() > 0){
					domain[i].add(J.getExaminerId2());
					x[i] = J.getExaminerId2();
					opt_x[i] = x[i];
				}else{
					for(int j = 0; j < hustTeachers.size(); j++){
						Teacher t = hustTeachers.get(j);
						if(t.getID() != J.getSupervisorId()) domain[i].add(t.getID());
					}
				}
			}else if(c == 2){// president
				if(J.getPresidentId() > 0){
					domain[i].add(J.getPresidentId());
					x[i] = J.getPresidentId();
					opt_x[i] = x[i];
				}else{
					for(int j = 0; j < hustTeachers.size(); j++){
						Teacher t = hustTeachers.get(j);
						if(t.getID() != J.getSupervisorId()) domain[i].add(t.getID());
					}
				}
			}else if(c == 3){// secretary
				if(J.getSecretaryId() > 0){
					domain[i].add(J.getSecretaryId());
					x[i] = J.getSecretaryId();
					opt_x[i] = x[i];
				}else{
					for(int j = 0; j < hustTeachers.size(); j++){
						Teacher t = hustTeachers.get(j);
						if(t.getID() != J.getSupervisorId()) domain[i].add(t.getID());
					}
				}
			
			}else if(c == 5){// var slot
				if(J.getSlotId() > 0){
					domain[i].add(J.getSlotId());
					x[i] = J.getSlotId();
					opt_x[i] = x[i];
				}else{
					for(int sl = 1; sl <= maxSlot; sl++)
						domain[i].add(sl);
				}
			}else if(c == 6){// var room
				if(J.getRoomId() > 0){
					domain[i].add(J.getRoomId());
					x[i] = J.getRoomId();
					opt_x[i] = x[i];
				}else{
					for(int j = 1; j <= rooms.size(); j++)
						domain[i].add(j);
				}
				/*
				System.out.print("domain[" + i + "] = ");
				for(int j = 0; j < domain[i].size(); j++)
					System.out.print(domain[i].get(j) + " ");
				System.out.println();
				*/
			}
			
			if(0 <= c && c <= 4){// if variables are jury members then sort them in an increasing order of matchScore
				//sort domain in an increasing of match level
				int[] a = new int[domain[i].size()];
				int[] match = new int[a.length];
				for(int j = 0; j < domain[i].size(); j++){
					a[j] = (Integer)domain[i].get(j);
					//System.out.println("r = " + r + ", a[" + j + "] = " + a[j]);
					//match[j] = (Integer)matchScore[r].get(a[j]);
					//if(objectiveType.equals("expert-level")){
						match[j] = mTeachers.get(a[j]).getExpertLevel();
						int cid = mJuryCluster.get(J);
						if(preAssignedPresidents[cid].contains(a[j])) match[j] = 0;// prioritize the stability of presidents 
							
					//}
				}
				
				for(int j1 = 0; j1 < a.length-1; j1++)
					for(int j2 = j1+1; j2 < a.length; j2++)
						if(match[j1] > match[j2]){
							int tmp = match[j1]; match[j1] = match[j2]; match[j2] = tmp;
							tmp = a[j1]; a[j1] = a[j2]; a[j2] = tmp;
						}
				domain[i].clear();
				for(int j = 0; j < a.length; j++)
					domain[i].add(a[j]);
				/*
				System.out.print("Domain[" + i + "] = ");
				for(int j = 0; j < a.length; j++){
					System.out.print("[" + a[j] + "," + match[j] + "] ");
				}
				System.out.println();
				*/
			}
		}
		/*
		for(int i = 0; i < order.length; i++){
			System.out.print("Domain[" + order[i] + "] = ");
			for(int j = 0; j < domain[order[i]].size(); j++)
				System.out.print(domain[order[i]].get(j) + " ");
			System.out.println();
		}
		*/
		
		//countHustRoom = new int[rooms.size()];
		//countNonHustRoom = new int[rooms.size()];
		//countSlotRoom = new int[rooms.size()];
		occTeacher = new HashMap<Integer, Integer>();
		occTeacherExaminer1 = new HashMap<Integer, Integer>();
		occTeacherExaminer2 = new HashMap<Integer, Integer>();
		
		for(int i = 0; i < nonHustTeachers.size(); i++){
			Teacher t = nonHustTeachers.get(i);
			occTeacher.put(t.getID(), 0);
			occTeacherExaminer1.put(t.getID(), 0);
		}
		for(int i = 0; i < hustTeachers.size(); i++){
			Teacher t = hustTeachers.get(i);
			occTeacher.put(t.getID(), 0);
			occTeacherExaminer2.put(t.getID(), 0);
		}
		
		
		occTeacherSlot = new HashMap[maxSlot+1];
		occTeacherRoom = new HashMap[rooms.size() + 1];
		for(int sl = 1; sl <= maxSlot; sl++){
			occTeacherSlot[sl] = new HashMap<Integer, Integer>();
			for(int i = 0; i < nonHustTeachers.size(); i++){
				Teacher t = nonHustTeachers.get(i);
				occTeacherSlot[sl].put(t.getID(), 0);
			}
			for(int i = 0; i < hustTeachers.size(); i++){
				Teacher t = hustTeachers.get(i);
				occTeacherSlot[sl].put(t.getID(), 0);
			}
		}
		for(int r = 1; r <= rooms.size(); r++){
			occTeacherRoom[r] = new HashMap<Integer, Integer>();
			for(int i = 0; i < nonHustTeachers.size(); i++){
				Teacher t = nonHustTeachers.get(i);
				occTeacherRoom[r].put(t.getID(), 0);
			}
			for(int i = 0; i < hustTeachers.size(); i++){
				Teacher t = hustTeachers.get(i);
				occTeacherRoom[r].put(t.getID(), 0);
			}
		}
		hustOfRoom = new HashSet[rooms.size()+1];
		nonHustOfRoom = new HashSet[rooms.size()+1];
		for(int ir = 1; ir <= rooms.size(); ir++){
			hustOfRoom[ir] = new HashSet<Integer>();
			nonHustOfRoom[ir] = new HashSet<Integer>();
		}
		
		maxNonHustOfRoom = new HashMap<Integer, Integer>();
		maxHustOfRoom = new HashMap<Integer, Integer>();
		maxNbJuriesOfRoom = new HashMap<Integer, Integer>();
		System.out.println("search, init data, rooms.sz = " + rooms.size() + ", maxSlot = " + maxSlot);
		for(int ir = 0; ir < rooms.size(); ir++){
			Room r = rooms.get(ir);
			//System.out.println("search, init data for maxNonHustOfRoom, rid = " + r.getID());
			maxNonHustOfRoom.put(r.getID(), r.getMaxNbNonHustMembers());
			maxHustOfRoom.put(r.getID(), r.getMaxNbHustMembers());
			maxNbJuriesOfRoom.put(r.getID(), r.getMaxNbJuries());
		}
		
		hustOfSlot = new HashMap<Integer, HashSet<Integer>>();
		nonHustOfSlot = new HashMap<Integer, HashSet<Integer>>();
		//for(int sl = 1; sl <= maxSlot; sl++){
		for(int sl = 1; sl <= 20; sl++){
			hustOfSlot.put(sl, new HashSet<Integer>());
			nonHustOfSlot.put(sl, new HashSet<Integer>());
		}
		
		slotOfRoom = new HashSet[rooms.size()+1];
		for(int ir = 1; ir <= rooms.size(); ir++){
			slotOfRoom[ir] = new HashSet<Integer>();
		}
		
		maxOccTeacher = new HashMap<Integer, Integer>();
		for(int i = 0; i < teachers.size(); i++){
			maxOccTeacher.put(teachers.get(i).getID(),maxSlot);
		}
		
		occRoom = new HashMap<Integer, Integer>();
		for(int r = 1; r <= 100; r++){
			occRoom.put(r, 0);
		}
		//initialize data structures for instantiated variables
		for(int i = 0; i < jury.size(); i++){
			JuryInfo J = jury.get(i);
			if(J.getExaminerId1() > 0){
				occTeacher.put(J.getExaminerId1(), occTeacher.get(J.getExaminerId1())+1);
				occTeacherExaminer1.put(J.getExaminerId1(), occTeacherExaminer1.get(J.getExaminerId1())+1);
				if(J.getSlotId() > 0){
					int slid = J.getSlotId();
					nonHustOfSlot.get(slid).add(J.getExaminerId1());
				}
				if(J.getRoomId() > 0){
					int rid = J.getRoomId();
					nonHustOfRoom[rid].add(J.getExaminerId1());
				}
			}
			if(J.getExaminerId2() > 0){
				occTeacher.put(J.getExaminerId2(), occTeacher.get(J.getExaminerId2())+1);
				occTeacherExaminer2.put(J.getExaminerId2(), occTeacherExaminer2.get(J.getExaminerId2())+1);
				if(J.getSlotId() > 0){
					int slid = J.getSlotId();
					hustOfSlot.get(slid).add(J.getExaminerId2());
				}
				if(J.getRoomId() > 0){
					int rid = J.getRoomId();
					hustOfRoom[rid].add(J.getExaminerId2());
				}
			}
			if(J.getPresidentId() > 0){
				occTeacher.put(J.getPresidentId(), occTeacher.get(J.getPresidentId())+1);
				//occTeacherExaminer1.put(J.getExaminerId1(), occTeacherExaminer1.get(J.getExaminerId1())+1);
				if(J.getSlotId() > 0){
					int slid = J.getSlotId();
					hustOfSlot.get(slid).add(J.getPresidentId());
				}
				if(J.getRoomId() > 0){
					int rid = J.getRoomId();
					hustOfRoom[rid].add(J.getPresidentId());
				}
			}
			if(J.getSecretaryId() > 0){
				occTeacher.put(J.getSecretaryId(), occTeacher.get(J.getSecretaryId())+1);
				//occTeacherExaminer1.put(J.getExaminerId1(), occTeacherExaminer1.get(J.getExaminerId1())+1);
				if(J.getSlotId() > 0){
					int slid = J.getSlotId();
					hustOfSlot.get(slid).add(J.getSecretaryId());
				}
				if(J.getRoomId() > 0){
					int rid = J.getRoomId();
					hustOfRoom[rid].add(J.getSecretaryId());
				}
			}
			if(J.getAdditionalMemberId() > 0){
				occTeacher.put(J.getAdditionalMemberId(), occTeacher.get(J.getAdditionalMemberId())+1);
				//occTeacherExaminer1.put(J.getExaminerId1(), occTeacherExaminer1.get(J.getExaminerId1())+1);
				if(J.getSlotId() > 0){
					int slid = J.getSlotId();
					nonHustOfSlot.get(slid).add(J.getAdditionalMemberId());
				}
				if(J.getRoomId() > 0){
					int rid = J.getRoomId();
					nonHustOfRoom[rid].add(J.getAdditionalMemberId());
				}
			}
			if(J.getSlotId() > 0 && J.getRoomId() > 0){
				slotOfRoom[J.getRoomId()].add(J.getSlotId());
			}
			if(J.getRoomId() > 0)
				occRoom.put(J.getRoomId(), occRoom.get(J.getRoomId())+1);
		}
		initObjective();
		/*
		for(int i = 0; i < rooms.size(); i++){
			Room r = rooms.get(i);
			System.out.println("Room " + r.getID() + ", nbJuries = " + maxNbJuriesOfRoom.get(r.getID()) + ", maxNonHust = " + 
			 maxNonHustOfRoom.get(r.getID()) + ", maxNbHust = " + maxHustOfRoom.get(r.getID()));
		}
		*/
		
		nbVars = jury.size()*2;
		order = new int[nbVars];
		int idx = -1;
		for(int i = 0; i < nbVars/2; i++){
			idx++;
			order[idx] = i*7+6;
		}
		for(int i = 0; i < nbVars/2; i++){
			idx++;
			order[idx] = i*7+2;
		}
		fixed = new boolean[nbVars];
		for(int i = 0; i < nbVars; i++)
			if(domain[order[i]].size() == 1){
				fixed[i] = true; 
				x[order[i]] = (Integer)domain[order[i]].get(0);
			}else fixed[i] = false;
		
		System.out.println("nbVars = " + nbVars + ": ");
		for(int i = 0; i < nbVars; i++) System.out.print(order[i] + " ");
		System.out.println();
		for(int i = 0; i < nbVars; i++){
			int var_idx = order[i];
			System.out.print(i + " -> domain[" + var_idx + "] = ");
			for(int j = 0;  j < domain[var_idx].size(); j++)
				System.out.print(domain[var_idx].get(j) + " ");
			System.out.println();
		}
		System.out.println("Start TRY....");
		maxDept = 0;
		/*
		for(int i = 0; i < n; i++){
			for(int p = 0; p < 7; p++){
				int k = 7*i+p;
				System.out.print("(");
				for(int j = 0; j < domain[k].size(); j++){
					System.out.print(domain[k].get(j) + " ");
				}
				System.out.print(")\t");
			}
			System.out.println();
		}
		*/

	}
	public void search(Vector<JuryInfo> jury, Vector<Room> rooms, Vector<Teacher> hustTeachers, Vector<Teacher> nonHustTeachers, int timeLimit){
		initDataStructuresBeforeSearch(jury, rooms, hustTeachers, nonHustTeachers, timeLimit);
		
		t0 = System.currentTimeMillis();
		stop = false;
		foundSolution = false;
		
		//for(int i = 0; i < nbVars; i++)
			//x[order[i]] = 0;
		
		TRY(0);
		
		if(foundSolution){
			
			System.out.println("After TRY, found solution!!!!!!!!!!!!");
			for(int i = 0; i < nbVars; i++){
				int k = order[i];
				int r = k/7;
				int c = k%7;
				JuryInfo J = jury.get(r);
				if(c == 0) J.setExaminerId1(opt_x[k]);
				else if(c == 1) J.setExaminerId2(opt_x[k]);
				else if(c == 2) J.setPresidentId(opt_x[k]);
				else if(c == 3) J.setSecretaryId(opt_x[k]);
				else if(c == 4) J.setAdditionalMemberId(opt_x[k]);
				else if(c == 5) J.setSlotId(opt_x[k]);
				else if(c == 6) J.setRoomId(opt_x[k]);
			}
		}else{
			System.out.println("After TRY, NOT found solution?????????????? maxDept = " + maxDept);
		}
	}
	
	private int[] eval(){
		//System.out.println("BackTrackSearchTeachersSlotsRoomsObjectiveMatch::eval()");
		int[] e = new int[nbObj];
		HashSet<Integer> pre = new HashSet<Integer>();
		for(int i = 0; i < nbVars/2; i++){
			int k = order[i+nbVars/2];
			pre.add(x[k]);
		}
		
		e[0] = pre.size();
		
			int match = 0;
			//for(int i = 0; i < m ;i++){
			for(int i = 0; i < nbVars ;i++){
				int k = order[i];
				int r = k/7;
				int c = k%7;
				if(c == 0 || c == 1){
					match += (Integer)matchScore[r].get(x[k]);
				}
			}
			e[1] = match;
		
		return e;
	}
	
	private int compare(int[] a, int[] b){
		int sz = a.length;
		for(int i = 0; i < sz; i++){
			if(a[i] < b[i]) return -1;
			else if(a[i] > b[i]) return 1;
		}
		return 0;
	}
	private void updateBest(){
		
		foundSolution = true;
		//stop = true;
		int[] e = eval();
		if(compare(opt_f,e) == 1){
			System.out.print("UPDATE BEST, opt_f = ");
			for(int i = 0; i < opt_f.length; i++) System.out.print(opt_f[i] + ",");
			System.out.print("  new evaluation = ");
			for(int i = 0; i < e.length; i++) System.out.print(e[i] + ",");
			System.out.println();
			for(int i = 0; i < e.length; i++)
				opt_f[i] = e[i];
			//for(int i = 0; i < m; i++){
			for(int j = 0; j < nbVars; j++){
				int i = order[j];
				opt_x[i] = x[i];
			}
		}
	}
	private boolean checkRowConsistencyJuryMembers(int r, int c, int v){
		boolean ok = true;
		for(int i = 0; i < 5; i++){
			int j = 7*r+i;
			if(x[j] > 0 && j != c && x[j] == v) return false;
		}
		return ok;
	}
	public void setNbVariablesInstantiated(int nbVars){
		this.nbVars = nbVars;
	}
	public void setOrder(int[] order){
		this.order = order;
	}
	private void TRY(int k){
		if(maxDept < k) maxDept = k;
		int var_idx = order[k];
		int r = var_idx/7;
		int c = var_idx%7;
		int dept = 100000;
		
		if(k >= dept)
			System.out.println("TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + " domain = " + 
		domain[var_idx].size());
		
		
		if(System.currentTimeMillis() - t0 > timeLimit) return;
		if(stop) return;
		
		if(fixed[k] == true){
			if(k < nbVars-1)
				TRY(k+1);
			else
				updateBest();
			return;
		}
		if(k >= dept){
			//stop = true;
		}
		
		for(int i = 0; i < domain[var_idx].size(); i++){
			int v = (Integer)domain[var_idx].get(i);
			if(k >= dept){
				//System.out.println("TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + ", v = " + v);
				/*
				for(int j = 0; j < n; j++){
					for(int p = 0; p < 7; p++)
						System.out.print(x[7*j+p] + "\t");
					System.out.println();
				}	
				*/
			}
			boolean ok = true;// check the consistency of value v to be assigned to x[var_idx]
			if(c < 5){// assign values to a jury member 
				ok = checkRowConsistencyJuryMembers(r,c,v);
				if(!ok && k >= dept) System.out.println("TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + ", v = " + v + ", FALSE consistency of jury");
				
				if(ok) ok = occTeacher.get(v) < maxOccTeacher.get(v);
				if(!ok && k >= dept) System.out.println("TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + ", v = " + v + ", FALSE occTeacher(" + v + ") = " + occTeacher.get(v) + " > maxOccTeacher(" + v + ") = " + maxOccTeacher.get(v));
				
				if(ok){
					if(x[r*7+6] > 0){// room has been assigned
						int rid = x[r*7+6]; 
						if(rid > 0){
							for(int ri = 1; ri <= rooms.size(); ri++){
								if(ri != rid && (hustOfRoom[ri].contains(v) || (nonHustOfRoom[ri].contains(v)))){
									ok = false;
									break;
								}
								
							}
							
						}
						/*
						if(c == 0 || c == 4 && !nonHustOfRoom[rid].contains(v) && 
								nonHustOfRoom[rid].size() >= maxNonHustOfRoom.get(rid)) ok = false;
						if(!ok && k >= dept) System.out.println("TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + ", v = " + v + 
								", FALSE nonHustOfRoom[" + rid + "].sz = " + nonHustOfRoom[rid].size());
						if(1 <= c && c <= 3 && !hustOfRoom[rid].contains(v) &&
								hustOfRoom[rid].size() >= maxHustOfRoom.get(rid)) ok = false;
						if(ok && k >= dept) System.out.println("TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + ", v = " + v + 
								", FALSE hustOfRoom[" + rid + "].sz = " + hustOfRoom[rid].size());
								*/
					}
					if(x[r*7+5] > 0){// slot has been assigned
						int slid = x[r*7+5]; 
						if(hustOfSlot.get(slid).contains(v)) ok = false;
						//if(!ok && k >= dept) System.out.println("TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + ", v = " + v + 
								//", FALSE hustOfSlot[" + slid + "].contains(" + v + ")");
						if(nonHustOfSlot.get(slid).contains(v)) ok = false;
						//if(!ok && k >= dept) System.out.println("TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + ", v = " + v + 
								//", FALSE nonHustOfSlot[" + slid + "].contains(" + v + ")");
					}
				}
			}else if(c == 5){// slot v is about to be assigned
				if(x[r*7+6] > 0){// room has been 
					int rid = x[r*7+6];
					if(slotOfRoom[rid].contains(v)) ok = false;
					if(!ok && k >= dept) System.out.println("TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + ", v = " + v + 
							", FALSE slotOfRoom[" + rid + "].contains(" + v + ")");
					
					if(!slotOfRoom[rid].contains(v) && slotOfRoom[rid].size() >= maxNbJuriesOfRoom.get(rid)) ok = false;
					if(!ok && k >= dept) System.out.println("TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + ", v = " + v + 
							", FALSE slotOfRoom[" + rid + "]sz = " + slotOfRoom[rid].size());	
				}
				for(int p = 0; p < 5; p++){
					if(x[r*7+p] > 0){
						int tid = x[r*7+p];
						if(hustOfSlot.get(v).contains(tid)) ok = false;
						if(!ok && k >= dept) System.out.println("TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + ", v = " + v + 
								", FALSE hustOfSlot[" + v + "].contains(" + tid + ")");
						if(nonHustOfSlot.get(v).contains(tid)) ok = false;
						if(!ok && k >= dept) System.out.println("TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + ", v = " + v + 
								", FALSE nonHustOfSlot[" + v + "].contains(" + tid + ")");
					}
				}
			}else{// room v is about to be assigned
				if(x[r*7+5] > 0){// slot has been assigned
					int slid = x[r*7+5];
					if(slotOfRoom[v].contains(slid)) ok = false;
					if(!ok && k >= dept) System.out.println("TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + ", v = " + v + 
							", FALSE slotOfRoom[" + v + "].contains(" + slid + ")");
				}
				int nbHust = 0;
				int nbNonHust = 0;
				for(int p = 0; p < 5; p++){
					if(x[r*7+p] > 0){
						int tid = x[r*7+p];
						if(p == 0 || p == 4){
							if(!nonHustOfRoom[v].contains(tid)) nbNonHust++;
						}else{
							if(!hustOfRoom[v].contains(tid)) nbHust++;
						}
					}
				}
				if(occRoom.get(v) >= rooms.get(v-1).getMaxNbJuries()) ok = false;
				if(ok){
					for(int ri = 1; ri <= rooms.size(); ri++) if(ri != v){
						for(int j = 0; j < 5; j++)
							if(x[r*7+j] > 0){
								int ti = x[r*7+j];
								if(hustOfRoom[ri].contains(ti) || nonHustOfRoom[ri].contains(ti)){
									ok = false;
									break;
								}
							}
						if(!ok) break;
					}
				}
				/*
				if(hustOfRoom[v].size() + nbHust > maxHustOfRoom.get(v)) ok = false;
				if(!ok && k >= dept) System.out.println("TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + ", v = " + v + 
						", FALSE hustOfRoom[" + v + "].sz = " + hustOfRoom[v].size() + ", nbHust = " + nbHust);
				if(nonHustOfRoom[v].size() + nbNonHust > maxNonHustOfRoom.get(v)) ok = false;
				if(!ok && k >= dept) System.out.println("TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + ", v = " + v + 
						", FALSE nonHustOfRoom[" + v + "].sz = " + nonHustOfRoom[v].size() + ", nbNonHust = " + nbNonHust);
				*/		
			}
			
			if(k >= dept){
				//System.out.println("TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + ", v = " + v + ", ok = " + ok);
				//System.out.println("TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + ", v = " + v + ", ok = " + ok);
				//stop = true;
			}
			
			if(!ok) continue;
			
			x[var_idx] = v;// ASSIGN
			if(k > maxDept) System.out.println("TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + ", v = " + v + ", ok = " + ok + " --> assign x[" + var_idx + "] = " + v);
			
			if(c == 0){
				if(x[r*7+5] > 0){
					int slid = x[r*7+5];
					nonHustOfSlot.get(slid).add(v);
				}
				if(x[r*7+6] > 0){
					int rid = x[r*7+6];
					if(occTeacherRoom[rid].get(v) == 0) nonHustOfRoom[rid].add(v);
					occTeacherRoom[rid].put(v, occTeacherRoom[rid].get(v)+1);
				}
				occTeacherExaminer1.put(v, occTeacherExaminer1.get(v)+1);
				occTeacher.put(v, occTeacher.get(v)+1);
			}else if(c == 1){
				if(x[r*7+5] > 0){
					int slid = x[r*7+5];
					hustOfSlot.get(slid).add(v);
				}
				if(x[r*7+6] > 0){
					int rid = x[r*7+6];
					if(occTeacherRoom[rid].get(v) == 0) hustOfRoom[rid].add(v);
					occTeacherRoom[rid].put(v, occTeacherRoom[rid].get(v)+1);
				}
				occTeacherExaminer2.put(v, occTeacherExaminer2.get(v)+1);
				occTeacher.put(v, occTeacher.get(v)+1);
			}else if(c == 2){
				if(x[r*7+5] > 0){
					int slid = x[r*7+5];
					hustOfSlot.get(slid).add(v);
				}
				if(x[r*7+6] > 0){
					int rid = x[r*7+6];
					if(occTeacherRoom[rid].get(v) == 0) hustOfRoom[rid].add(v);
					occTeacherRoom[rid].put(v, occTeacherRoom[rid].get(v)+1);
				}
				
				occTeacher.put(v, occTeacher.get(v)+1);
			}else if(c == 3){
				if(x[r*7+5] > 0){
					int slid = x[r*7+5];
					hustOfSlot.get(slid).add(v);
				}
				if(x[r*7+6] > 0){
					int rid = x[r*7+6];
					if(occTeacherRoom[rid].get(v) == 0) hustOfRoom[rid].add(v);
					occTeacherRoom[rid].put(v, occTeacherRoom[rid].get(v)+1);
				}
				
				occTeacher.put(v, occTeacher.get(v)+1);
			}else if(c == 4){
				if(x[r*7+5] > 0){
					int slid = x[r*7+5];
					nonHustOfSlot.get(slid).add(v);
				}
				if(x[r*7+6] > 0){
					int rid = x[r*7+6];
					if(occTeacherRoom[rid].get(v) == 0) nonHustOfRoom[rid].add(v);
					occTeacherRoom[rid].put(v, occTeacherRoom[rid].get(v)+1);
				}
				occTeacher.put(v, occTeacher.get(v)+1);
			}else if(c == 5){
				for(int p = 0; p < 5; p++){
					if(x[r*7+p] > 0){
						int tid = x[r*7+p];
						if(p == 0 || p == 4){
							nonHustOfSlot.get(v).add(tid);
						}else{
							hustOfSlot.get(v).add(tid);
						}
					}
				}
				if(x[r*7+6] > 0){
					int rid = x[r*7+6];
					slotOfRoom[rid].add(v);
				}
			}else if(c == 6){
				for(int p = 0; p < 5; p++){
					if(x[r*7+p] > 0){
						int tid = x[r*7+p];
						if(p == 0 || p == 4){
							if(occTeacherRoom[v].get(tid) == 0) nonHustOfRoom[v].add(tid);
						}else{
							if(occTeacherRoom[v].get(tid) == 0) hustOfRoom[v].add(tid);
						}
						occTeacherRoom[v].put(tid, occTeacherRoom[v].get(tid)+1);
					}
				}
				if(x[r*7+5] > 0){
					int slid = x[r*7+5];
					slotOfRoom[v].add(slid);
				}
				
				occRoom.put(v, occRoom.get(v)+1);
			}
			
			//if(k == m-1){
			if(k == nbVars-1){// instantiate nbVars necessary variables
				updateBest();
			}else{
				TRY(k+1);
			}

			// RECOVER status
			if(c == 0){
				if(x[r*7+5] > 0){
					int slid = x[r*7+5];
					nonHustOfSlot.get(slid).remove(v);
				}
				if(x[r*7+6] > 0){
					int rid = x[r*7+6];
					if(occTeacherRoom[rid].get(v) == 1) nonHustOfRoom[rid].remove(v);
					occTeacherRoom[rid].put(v, occTeacherRoom[rid].get(v)-1);
				}
				occTeacherExaminer1.put(v, occTeacherExaminer1.get(v)-1);
				occTeacher.put(v, occTeacher.get(v)-1);
			}else if(c == 1){
				if(x[r*7+5] > 0){
					int slid = x[r*7+5];
					hustOfSlot.get(slid).remove(v);
				}
				if(x[r*7+6] > 0){
					int rid = x[r*7+6];
					if(occTeacherRoom[rid].get(v) == 1) hustOfRoom[rid].remove(v);
					occTeacherRoom[rid].put(v, occTeacherRoom[rid].get(v)-1);
				}
				occTeacherExaminer2.put(v, occTeacherExaminer2.get(v)-1);
				occTeacher.put(v, occTeacher.get(v)-1);
			}else if(c == 2){
				if(x[r*7+5] > 0){
					int slid = x[r*7+5];
					hustOfSlot.get(slid).remove(v);
				}
				if(x[r*7+6] > 0){
					int rid = x[r*7+6];
					if(occTeacherRoom[rid].get(v) == 1) hustOfRoom[rid].remove(v);
					occTeacherRoom[rid].put(v, occTeacherRoom[rid].get(v)-1);
				}
				occTeacher.put(v, occTeacher.get(v)-1);
			}else if(c == 3){
				if(x[r*7+5] > 0){
					int slid = x[r*7+5];
					hustOfSlot.get(slid).remove(v);
				}
				if(x[r*7+6] > 0){
					int rid = x[r*7+6];
					if(occTeacherRoom[rid].get(v) == 1) hustOfRoom[rid].remove(v);
					occTeacherRoom[rid].put(v, occTeacherRoom[rid].get(v)-1);
				}
				occTeacher.put(v, occTeacher.get(v)-1);
			}else if(c == 4){
				if(x[r*7+5] > 0){
					int slid = x[r*7+5];
					nonHustOfSlot.get(slid).remove(v);
				}
				if(x[r*7+6] > 0){
					int rid = x[r*7+6];
					if(occTeacherRoom[rid].get(v) == 1) nonHustOfRoom[rid].remove(v);
					occTeacherRoom[rid].put(v, occTeacherRoom[rid].get(v)-1);
				}
				occTeacher.put(v, occTeacher.get(v)-1);
			}else if(c == 5){
				for(int p = 0; p < 5; p++){
					if(x[r*7+p] > 0){
						int tid = x[r*7+p];
						if(p == 0 || p == 4){
							nonHustOfSlot.get(v).remove(tid);
						}else{
							hustOfSlot.get(v).remove(tid);
						}
					}
				}
				if(x[r*7+6] > 0){
					int rid = x[r*7+6];
					slotOfRoom[rid].remove(v);
				}
			}else if(c == 6){
				for(int p = 0; p < 5; p++){
					if(x[r*7+p] > 0){
						int tid = x[r*7+p];
						if(p == 0 || p == 4){
							if(occTeacherRoom[v].get(tid) == 1) nonHustOfRoom[v].remove(tid);
						}else{
							if(occTeacherRoom[v].get(tid) == 1) hustOfRoom[v].remove(tid);
						}
						occTeacherRoom[v].put(tid, occTeacherRoom[v].get(tid)-1);
					}
				}
				if(x[r*7+5] > 0){
					int slid = x[r*7+5];
					slotOfRoom[v].remove(slid);
				}
				occRoom.put(v, occRoom.get(v)-1);
			}
			
			x[var_idx] = 0;// recover

			
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
