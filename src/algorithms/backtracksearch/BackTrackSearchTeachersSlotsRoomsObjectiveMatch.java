package algorithms.backtracksearch;

import java.util.*;

import utils.Utility;
import DataEntity.JuryInfo;
import DataEntity.Room;
import DataEntity.Teacher;

public class BackTrackSearchTeachersSlotsRoomsObjectiveMatch extends BasicBackTrackSearch{

	/**
	 * @param args
	 */
	
	/*
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
	HashMap<Integer, Integer> occRoom;// occRoom.get(r) is the number of occurrences of rooms
	HashMap<Integer, Integer> occSlot;// occSlot.get(sl) is the number of occurrences of slots
	
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
	
	HashSet[] slotOfRoom;// slotOfRoom[r] is the set of slots assigned to room r
	
	HashMap[] matchScore;
	int maxSlot;
	boolean stop;
	boolean foundSolution;
	double t0;
	int maxDept;
	
	//data structures for storing initial state
	HashMap<JuryInfo, Vector> input_value;
	
	private String objectiveType = "subjects-match";
	*/
	
	/*
	private void initObjective(){
		System.out.println("BackTrackSearchTeachersSlotsRoomsObjectiveMatch::initObjective()");
		nbObj = 1;
		opt_f = new int[nbObj];
		for(int i = 0; i < nbObj; i++) opt_f[i] = 1000000;
	}
	public void setObjectiveType(String objType){
		this.objectiveType = objType;
	}
	public boolean foundSolution(){
		return this.foundSolution;
	}
	
	public void backupInitValues(Vector<JuryInfo> jury){
		input_value = new HashMap<JuryInfo, Vector>();
		
		for(int i = 0; i < jury.size(); i++){
			JuryInfo J = jury.get(i);
			Vector<Integer> V = new Vector<Integer>();
			V.add(J.getExaminerId1());
			V.add(J.getExaminerId2());
			V.add(J.getPresidentId());
			V.add(J.getSecretaryId());
			V.add(J.getAdditionalMemberId());
			V.add(J.getSlotId());
			V.add(J.getRoomId());
			
			input_value.put(J, V);
		}
	}
	
	public void restore(Vector<JuryInfo> jury){
		for(int i = 0; i < jury.size(); i++){
			JuryInfo J = jury.get(i);
			Vector<Integer> V = (Vector<Integer>)input_value.get(J);
			J.setExaminerId1(V.get(0));
			J.setExaminerId2(V.get(1));
			J.setPresidentId(V.get(2));
			J.setSecretaryId(V.get(3));
			J.setAdditionalMemberId(V.get(4));
			J.setSlotId(V.get(5));
			J.setRoomId(V.get(6));
		}
	}
	*/
	
	/*
	private void initDataStructuresBeforeSearch(Vector<JuryInfo> jury, Vector<Room> rooms, Vector<Teacher> hustTeachers, Vector<Teacher> nonHustTeachers, int timeLimit){
		this.jury = jury;
		this.rooms = rooms;
		this.hustTeachers = hustTeachers;
		this.nonHustTeachers = nonHustTeachers;
		this.timeLimit = timeLimit*1000;
		
		
		
		if(jury.size()%rooms.size() == 0) maxSlot = jury.size()/rooms.size();
		else maxSlot = maxSlot = jury.size()/rooms.size() + 1;
		System.out.println("BackTrackSearchTeachersSlotsRoomsObjectiveMatch::initDataStructuresBeforeSearch, maxSlot = " + maxSlot);
		
		teachers = new Vector<Teacher>();
		for(int i = 0; i < hustTeachers.size(); i++)
			teachers.add(hustTeachers.get(i));
		for(int i = 0; i < nonHustTeachers.size(); i++)
			teachers.add(nonHustTeachers.get(i));
		
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
				
			}
			
			if(0 <= c && c <= 4){// if variables are jury members then sort them in an increasing order of matchScore
				//sort domain in an increasing of match level
				int[] a = new int[domain[i].size()];
				int[] match = new int[a.length];
				for(int j = 0; j < domain[i].size(); j++){
					a[j] = (Integer)domain[i].get(j);
					//System.out.println("r = " + r + ", a[" + j + "] = " + a[j]);
					match[j] = (Integer)matchScore[r].get(a[j]);
					if(objectiveType.equals("expert-level")){
						match[j] = mTeachers.get(a[j]).getExpertLevel();
					}
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
				
			}
		}
		
		
		
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
		
		occRoom = new HashMap<Integer, Integer>();
		for(int i = 0; i < rooms.size(); i++){
			Room r = rooms.get(i);
			occRoom.put(r.getID(), 0);
		}
		occSlot = new HashMap<Integer, Integer>();
		for(int sl = 1; sl <= maxSlot; sl++)
			occSlot.put(sl, 0);
		
		hustOfRoom = new HashSet[rooms.size()+1];
		nonHustOfRoom = new HashSet[rooms.size()+1];
		for(int ir = 1; ir <= rooms.size(); ir++){
			hustOfRoom[ir] = new HashSet<Integer>();
			nonHustOfRoom[ir] = new HashSet<Integer>();
		}
		
		maxNonHustOfRoom = new HashMap<Integer, Integer>();
		maxHustOfRoom = new HashMap<Integer, Integer>();
		maxNbJuriesOfRoom = new HashMap<Integer, Integer>();
		//System.out.println("search, init data, rooms.sz = " + rooms.size() + ", maxSlot = " + maxSlot);
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
			if(J.getSlotId() > 0){
				occSlot.put(J.getSlotId(), occSlot.get(J.getSlotId())+1);
			}
			if(J.getRoomId() > 0){
				occRoom.put(J.getRoomId(), occRoom.get(J.getRoomId())+1);
			}
		}
		initObjective();
		

	}
	*/
	
	public void search(Vector<JuryInfo> jury, Vector<Room> rooms, Vector<Teacher> hustTeachers, Vector<Teacher> nonHustTeachers, int timeLimit){
		System.out.println("BackTrackSearchTeachersSlotsRoomsObjectiveMatch::search...");
		super.search(jury, rooms, hustTeachers, nonHustTeachers, timeLimit);
		
		/*
		System.out.println("BackTrackSearchTeachersSlotsRoomsObjectiveMatch::search...");
		initDataStructuresBeforeSearch(jury, rooms, hustTeachers, nonHustTeachers, timeLimit);
		
		t0 = System.currentTimeMillis();
		stop = false;
		foundSolution = false;
		
		// collect non-instantiated variables
		Vector<Integer> X = new Vector<Integer>();
		for(int k = 0; k < nbVars; k++){
			//System.out.println("x[" + order[k] + "] = " + x[order[k]]);
			if(x[order[k]] == 0)
				X.add(order[k]);
		}
		nbVars = X.size();
		order = new int[nbVars];
		for(int k = 0; k < X.size(); k++)
			order[k] = X.get(k);
		
		if(nbVars == 0){
			foundSolution = true;
			return;
		}
		System.out.println("BackTrackSearchTeachersSlotsRoomsObjectiveMatch::search, nbVars = " + nbVars);
		TRY(0);
	
		if(foundSolution){
			System.out.println("BackTrackSearchTeachersSlotsRoomsObjectiveMatch::search --> After TRY, found solution!!!!!!!!!!!!");
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
			System.out.println("BackTrackSearchTeachersSlotsRoomsObjectiveMatch::search --> After TRY, NOT found solution?????????????? maxDept = " + maxDept);
		}
		*/
	}
	
	protected int[] eval(){
		//System.out.println("BackTrackSearchTeachersSlotsRoomsObjectiveMatch::eval(), objectiveType = " + objectiveType);
		int[] e = new int[nbObj];
		if(objectiveType.equals("movements")){
			int consecutive = 0;
			HashMap<Integer, int[]> present = new HashMap<Integer, int[]>();
			for(int i = 0; i < teachers.size(); i++){
				Teacher t = teachers.get(i);
				present.put(t.getID(), new int[maxSlot+1]);
			}
			for(int i = 0; i < jury.size(); i++){
				JuryInfo J = jury.get(i);
				int[] tid = J.getJuryMemberIDs();
				if(J.getRoomId() > 0 && J.getSlotId() > 0){
					for(int j = 0; j < tid.length; j++)if(tid[j] > 0){
						present.get(tid[j])[J.getSlotId()] = J.getRoomId();
					}
				}
			}
			for(int i = 0; i < teachers.size(); i++){
				int tid = teachers.get(i).getID();
				int[] p = present.get(tid);
				for(int j = 1; j < maxSlot; j++){
					if(p[j] > 0 && p[j+1] > 0 && p[j] != p[j+1])
						consecutive++;
				}
			}
			e[0] = consecutive;
		}else if(objectiveType.equals("expert-level")){
			e[0] = 0;
			for(int i = 0; i < jury.size(); i++){
				JuryInfo J = jury.get(i);
				e[0] += mTeachers.get(x[7*i+2]).getExpertLevel();
			}
		}else{
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
			e[0] = match;
		}
		return e;
	}
	/*
	private int compare(int[] a, int[] b){
		int sz = a.length;
		for(int i = 0; i < sz; i++){
			if(a[i] < b[i]) return -1;
			else if(a[i] > b[i]) return 1;
		}
		return 0;
	}
	*/
	
	/*
	private void updateBest(){
		
		foundSolution = true;
		//stop = true;
		int[] e = eval();
		if(compare(opt_f,e) == 1){
			System.out.print("BackTrackSearchTeachersSlotsRoomsObjectiveMatch::UPDATE BEST, opt_f = ");
			for(int i = 0; i < opt_f.length; i++) System.out.print(opt_f[i] + ",");
			System.out.print("  new eval = ");
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
	*/
	private boolean checkRowConsistencyJuryMembers(int r, int v, int c){
		boolean ok = true;
		for(int i = 0; i < 5; i++){
			int j = 7*r+i;
			if(x[j] > 0 && x[j] == v && i != c) return false;
		}
		return ok;
	}
	public void setNbVariablesInstantiated(int nbVars){
		this.nbVars = nbVars;
	}
	public void setOrder(int[] order){
		this.order = order;
	}
	
	protected boolean checkFeasible(int r, int c, int v, int k, int dept){
		int var_idx = 7*r+c;
		boolean ok = true;// check the consistency of value v to be assigned to x[var_idx]
		if(c < 5){// assign values to a jury member 
			ok = checkRowConsistencyJuryMembers(r,v,c);
			if(!ok && k >= dept) System.out.println("BackTrackSearchTeachersSlotsRoomsObjectiveMatch::TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + ", v = " + v + ", FALSE consistency of jury");
			
			if(ok) ok = occTeacher.get(v) < maxOccTeacher.get(v);
			if(!ok && k >= dept) System.out.println("BackTrackSearchTeachersSlotsRoomsObjectiveMatch::TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + ", v = " + v + ", FALSE occTeacher(" + 
			v + ") = " + occTeacher.get(v) + " >= maxOccTeacher(" + v + ") = " + maxOccTeacher.get(v));
			if(ok){
				if(x[r*7+6] > 0){// room has been assigned
					int rid = x[r*7+6]; 
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
					if(!ok && k >= dept) System.out.println("BackTrackSearchTeachersSlotsRoomsObjectiveMatch::TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + ", v = " + v + 
							", FALSE hustOfSlot[" + slid + "].contains(" + v + ")");
					if(nonHustOfSlot.get(slid).contains(v)) ok = false;
					if(!ok && k >= dept) System.out.println("BackTrackSearchTeachersSlotsRoomsObjectiveMatch::TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + ", v = " + v + 
							", FALSE nonHustOfSlot[" + slid + "].contains(" + v + ")");
				}
				//if(occRoom.get(v) >= maxNbJuriesOfRoom.get(v)) ok = false;
				
			}
		}else if(c == 5){// slot v is about to be assigned
			if(x[r*7+6] > 0){// room has been 
				int rid = x[r*7+6];
				if(slotOfRoom[rid].contains(v)) ok = false;
				if(!ok && k >= dept) System.out.println("BackTrackSearchTeachersSlotsRoomsObjectiveMatch::TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + ", v = " + v + 
						", FALSE slotOfRoom[" + rid + "].contains(" + v + ")");
				
				if(!slotOfRoom[rid].contains(v) && slotOfRoom[rid].size() >= maxNbJuriesOfRoom.get(rid)) ok = false;
				if(!ok && k >= dept) System.out.println("BackTrackSearchTeachersSlotsRoomsObjectiveMatch::TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + ", v = " + v + 
						", FALSE slotOfRoom[" + rid + "]sz = " + slotOfRoom[rid].size());	
			}
			for(int p = 0; p < 5; p++){
				if(x[r*7+p] > 0){
					int tid = x[r*7+p];
					if(hustOfSlot.get(v).contains(tid)) ok = false;
					if(!ok && k >= dept) System.out.println("BackTrackSearchTeachersSlotsRoomsObjectiveMatch::TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + ", v = " + v + 
							", FALSE hustOfSlot[" + v + "].contains(" + tid + ")");
					if(nonHustOfSlot.get(v).contains(tid)) ok = false;
					if(!ok && k >= dept) System.out.println("BackTrackSearchTeachersSlotsRoomsObjectiveMatch::TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + ", v = " + v + 
							", FALSE nonHustOfSlot[" + v + "].contains(" + tid + ")");
				}
			}
			if(ok)if(occSlot.get(v) >= rooms.size()) ok = false;
			if(!ok && k >= dept) System.out.println("BackTrackSearchTeachersSlotsRoomsObjectiveMatch::TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + ", v = " + v + 
					", FALSE occSlot[" + v + "] = " + occSlot.get(v) + " >= rooms.size() = " + rooms.size());
			
		}else{// room v is about to be assigned
			//System.out.println("BackTrackSearchTeachersSlotsRoomsObjectiveMatch::checkFeasible, oocRoom[" + v + "] = " + 
					//occRoom.get(v) + ", maxNbJuriesOfRoom[" + v + "] = " + maxNbJuriesOfRoom.get(v));
			if(x[r*7+5] > 0){// slot has been assigned
				int slid = x[r*7+5];
				if(slotOfRoom[v].contains(slid)) ok = false;
				if(!ok && k >= dept) System.out.println("TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + ", v = " + v + 
						", FALSE slotOfRoom[" + v + "].contains(" + slid + ")");
			}
			if(ok)if(occRoom.get(v) >= maxNbJuriesOfRoom.get(v)) ok = false;
			if(!ok && k >= dept) System.out.println("BackTrackSearchTeachersSlotsRoomsObjectiveMatch::TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + ", v = " + v + 
					", FALSE occRoom[" + v + "] = " + occRoom.get(v) + " >= maxNbJuriesOfRoom[" + v + "] = " + maxNbJuriesOfRoom.get(v));
			/*
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
			
			if(hustOfRoom[v].size() + nbHust > maxHustOfRoom.get(v)) ok = false;
			if(!ok && k >= dept) System.out.println("TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + ", v = " + v + 
					", FALSE hustOfRoom[" + v + "].sz = " + hustOfRoom[v].size() + ", nbHust = " + nbHust);
			if(nonHustOfRoom[v].size() + nbNonHust > maxNonHustOfRoom.get(v)) ok = false;
			if(!ok && k >= dept) System.out.println("TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + ", v = " + v + 
					", FALSE nonHustOfRoom[" + v + "].sz = " + nonHustOfRoom[v].size() + ", nbNonHust = " + nbNonHust);
					*/
		}
		
		if(k >= dept){
			System.out.println("TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + ", v = " + v + ", ok = " + ok);
			//System.out.println("TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + ", v = " + v + ", ok = " + ok);
			//stop = true;
		}
		
		return ok;
	}
	
	protected void propagate(int r, int c, int v, int k, int dept){
		JuryInfo J = jury.get(r);
		
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
			occSlot.put(v, occSlot.get(v)+1);
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
			
			if(occSupervisorRoom[v].get(J.getSupervisorId()) == null)
				occSupervisorRoom[v].put(J.getSupervisorId(),1);
			else
				occSupervisorRoom[v].put(J.getSupervisorId(),occSupervisorRoom[v].get(J.getSupervisorId())+1);
			supervisorsOfRoom[v].add(J.getSupervisorId());
		}

	}
	
	protected void recoverWhenBackTrack(int r, int c, int v, int k, int dept){
		JuryInfo J = jury.get(r);
		//System.out.println("Recover when backtrack...");
		int var_idx = 7*r+c;
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
			occSlot.put(v, occSlot.get(v)-1);
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
			
			occSupervisorRoom[v].put(J.getSupervisorId(),occSupervisorRoom[v].get(J.getSupervisorId())-1);
			if(occSupervisorRoom[v].get(J.getSupervisorId()) == 0)
				supervisorsOfRoom[v].remove(J.getSupervisorId());
		}
		
		x[var_idx] = 0;// recover

	}
	protected void TRY(int k){
		if(maxDept < k) maxDept = k;
		int var_idx = order[k];
		//if(x[var_idx] > 0){
			//TRY(k+1);
			//return;
		//}
		int r = var_idx/7;
		int c = var_idx%7;
		int dept = 100000;
		
		if(k >= dept)
			System.out.println("BackTrackSearchTeachersSlotsRoomsObjectiveMatch::TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + " domain = " + 
		domain[var_idx].size());
		
		
		if(System.currentTimeMillis() - t0 > timeLimit) return;
		if(stop) return;
		
		if(k >= dept){
			//stop = true;
		}
		
		for(int i = 0; i < domain[var_idx].size(); i++){
			int v = (Integer)domain[var_idx].get(i);
			if(k >= dept){
				//System.out.println("TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + ", v = " + v);
				//for(int j = 0; j < n; j++){
					//for(int p = 0; p < 7; p++)
						//System.out.print(x[7*j+p] + "\t");
					//System.out.println();
				//}	
			}
			
			/*
			boolean ok = true;// check the consistency of value v to be assigned to x[var_idx]
			if(c < 5){// assign values to a jury member 
				ok = checkRowConsistencyJuryMembers(r,v,c);
				if(!ok && k >= dept) System.out.println("TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + ", v = " + v + ", FALSE consistency of jury");
				
				if(ok) ok = occTeacher.get(v) < maxOccTeacher.get(v);
				if(!ok && k >= dept) System.out.println("TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + ", v = " + v + ", FALSE occTeacher(" + 
				v + ") = " + occTeacher.get(v) + " >= maxOccTeacher(" + v + ") = " + maxOccTeacher.get(v));
				if(ok){
					if(x[r*7+6] > 0){// room has been assigned
						int rid = x[r*7+6]; 
						
					}
					if(x[r*7+5] > 0){// slot has been assigned
						int slid = x[r*7+5]; 
						if(hustOfSlot.get(slid).contains(v)) ok = false;
						if(!ok && k >= dept) System.out.println("TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + ", v = " + v + 
								", FALSE hustOfSlot[" + slid + "].contains(" + v + ")");
						if(nonHustOfSlot.get(slid).contains(v)) ok = false;
						if(!ok && k >= dept) System.out.println("TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + ", v = " + v + 
								", FALSE nonHustOfSlot[" + slid + "].contains(" + v + ")");
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
				if(ok)if(occSlot.get(v) >= rooms.size()) ok = false;
				
			}else{// room v is about to be assigned
				if(x[r*7+5] > 0){// slot has been assigned
					int slid = x[r*7+5];
					if(slotOfRoom[v].contains(slid)) ok = false;
					if(!ok && k >= dept) System.out.println("TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + ", v = " + v + 
							", FALSE slotOfRoom[" + v + "].contains(" + slid + ")");
				}
				if(ok)if(occRoom.get(v) >= rooms.get(v-1).getMaxNbJuries()) ok = false;
				
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
				
			}
			
			if(k >= dept){
				System.out.println("TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + ", v = " + v + ", ok = " + ok);
				//System.out.println("TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + ", v = " + v + ", ok = " + ok);
				//stop = true;
			}
			*/
			
			boolean ok = checkFeasible(r,c,v,k,dept);
			
			//if(i == domain[var_idx].size()-1 && !ok) stop = true;
			
			if(!ok) continue;
			
			x[var_idx] = v;// ASSIGN
			
			/*
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
				occSlot.put(v, occSlot.get(v)+1);
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
			*/
			
			propagate(r,c,v,k,dept);
			
			//if(k == m-1){
			if(k == nbVars-1){// instantiate nbVars necessary variables
				updateBest();
			}else{
				TRY(k+1);
			}

			// RECOVER status
			recoverWhenBackTrack(r,c,v,k,dept);
			
			/*
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
				occSlot.put(v, occSlot.get(v)-1);
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
			*/
			
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
