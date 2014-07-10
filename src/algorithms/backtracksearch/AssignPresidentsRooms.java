package algorithms.backtracksearch;

import java.util.*;

import utils.Utility;
import DataEntity.JuryInfo;
import DataEntity.Room;
import DataEntity.Teacher;

public class AssignPresidentsRooms {

	/**
	 * @param args
	 */
	
	private Vector<JuryInfo> jury;
	private Vector<Room> rooms;
	private Vector<Teacher> hustTeachers;
	private Vector<Teacher> nonHustTeachers;
	private Vector<Teacher> sel_presidents;
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
	
	HashSet[] slotOfRoom;// slotOfRoom[r] is the set of slots assigned to room r
	
	HashMap[] matchScore;
	int maxSlot;
	boolean stop;
	boolean foundSolution;
	double t0;
	int maxDept;
	
	private String objectiveType = "subjects-match";
	
	private void initObjective(){
		System.out.println("AssignPresidentsRooms::initObjective()");
		nbObj = 1;
		opt_f = new int[nbObj];
		for(int i = 0; i < nbObj; i++) opt_f[i] = 1000000;
	}
	public void setObjectiveType(String objType){
		this.objectiveType = objType;
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
		System.out.println("AssignPresidentsRooms::search, maxSlot = " + maxSlot);
		
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
			for(int j = 0; j < domain[order[j]].size(); j++)
				System.out.print(domain[order[j]].get(j) + " ");
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
		}
		initObjective();
		/*
		for(int i = 0; i < rooms.size(); i++){
			Room r = rooms.get(i);
			System.out.println("Room " + r.getID() + ", nbJuries = " + maxNbJuriesOfRoom.get(r.getID()) + ", maxNonHust = " + 
			 maxNonHustOfRoom.get(r.getID()) + ", maxNbHust = " + maxHustOfRoom.get(r.getID()));
		}
		
		System.out.println("nbVars = " + nbVars + ": ");
		for(int i = 0; i < nbVars; i++) System.out.print(order[i] + " ");
		System.out.println();
		System.out.println("Start TRY....");
		maxDept = 0;
		
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
	
	private Vector<Teacher> selectPresidents(){
		Vector<Teacher> presidents = new Vector<Teacher>();
		int szh = hustTeachers.size();
		Teacher[] at = new Teacher[szh];
		for(int i = 0; i < szh; i++)
			at[i] = hustTeachers.get(i);
		
		// sort in an decreasing order of expert level
		for(int i = 0; i < szh-1; i++)
			for(int j = i+1; j < szh; j++)
				if(at[i].getExpertLevel() > at[j].getExpertLevel()){
					Teacher t = at[i]; at[i] = at[j]; at[j] = t;
				}
		for(int i = 0; i < rooms.size(); i++)
			presidents.add(at[i]);
		
		return presidents;
	}
	public void search(Vector<JuryInfo> jury, Vector<Room> rooms, Vector<Teacher> hustTeachers, Vector<Teacher> nonHustTeachers, int timeLimit){
		initDataStructuresBeforeSearch(jury, rooms, hustTeachers, nonHustTeachers, timeLimit);
		sel_presidents = selectPresidents();
		
		for(int i = 0; i < sel_presidents.size(); i++){
			Teacher t = sel_presidents.get(i);
			maxHustOfRoom.put(t.getID(), n/rooms.size());
		}
		int d = n%sel_presidents.size();
		for(int i = 0; i < d; i++){
			Teacher t = sel_presidents.get(i);
			maxHustOfRoom.put(t.getID(), maxHustOfRoom.get(t.getID())+1);
		}
		for(int i = 0; i < sel_presidents.size(); i++){
			Teacher t = sel_presidents.get(i);
			System.out.println("AssignPresidentsRooms, sel_presidents[" + i + "] = " + t.getID() + ", maxOcc = " + maxHustOfRoom.get(t.getID()));
		}
		nbVars = n;
		order = new int[nbVars];
		for(int i = 0; i < nbVars; i++)
			order[i] = i*7+2;
		
		t0 = System.currentTimeMillis();
		stop = false;
		foundSolution = false;
		
		TRY(0);
	
		if(foundSolution){
			System.out.println("AssignPresidentsRooms, After TRY, found solution!!!!!!!!!!!!");
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
			
			/*
			for(int i = 0; i < sel_presidents.size(); i++){
				Teacher t = sel_presidents.get(i);
				int sl = 0;
				for(int j = 0; j < n; j++){
					JuryInfo J = jury.get(j);
					if(J.getPresidentId() == t.getID()){
						sl++;
						J.setRoomId(i+1);
						//J.setSlotId(sl);
					}
				}
			}
			*/
			
			if(foundSolution){
				BackTrackSearchTeachersSlotsRoomsObjectiveMatch btsatsrR = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
				btsatsrR.setNbVariablesInstantiated(jury.size());
				int[] orderR = new int[jury.size()];
				for(int i = 0; i < jury.size(); i++){
					orderR[i] = 7*i+6;
				}
				btsatsrR.setOrder(orderR);
				//btsatsr.setObjectiveType("movements");
				btsatsrR.search(jury, rooms, hustTeachers, nonHustTeachers, timeLimit);
				
				foundSolution = btsatsrR.foundSolution();
				
				if(foundSolution){
					BackTrackSearchTeachersSlotsRoomsObjectiveMatch btsatsrS = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
					btsatsrS.setNbVariablesInstantiated(jury.size());
					int[] orderS = new int[jury.size()];
					for(int i = 0; i < jury.size(); i++){
						orderS[i] = 7*i+5;
					}
					btsatsrS.setOrder(orderS);
					//btsatsr.setObjectiveType("movements");
					btsatsrS.search(jury, rooms, hustTeachers, nonHustTeachers, timeLimit);
					
					foundSolution = btsatsrS.foundSolution();
				}
			}
			
		}else{
			System.out.println("AssignPresidentsRooms::search --> After TRY, NOT found solution?????????????? maxDept = " + maxDept);
		}
	}
	
	private int[] eval(){
		//System.out.println("AssignPresidentsRooms::eval()");
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
	
	public boolean foundSolution(){
		return this.foundSolution;
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
		System.out.println("AssignPresidentsRooms::updateBest");
		if(stop) return;
		foundSolution = true;
		stop = true;
		
		
		int[] e = eval();
		//if(compare(opt_f,e) == 1){
			//System.out.print("UPDATE BEST, opt_f = ");
			//for(int i = 0; i < opt_f.length; i++) System.out.print(opt_f[i] + ",");
			//System.out.print("  new eval = ");
			//for(int i = 0; i < e.length; i++) System.out.print(e[i] + ",");
			//System.out.println();
			for(int i = 0; i < e.length; i++)
				opt_f[i] = e[i];
			//for(int i = 0; i < m; i++){
			for(int j = 0; j < nbVars; j++){
				int i = order[j];
				opt_x[i] = x[i];
				//System.out.println("UpdateBest, opt_x[" + i + "] = " + opt_x[i]);
			}
		//}
	}
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
	private void TRY(int k){
		int c = 2;// president
		int v = sel_presidents.get(k).getID();
		int depth = 10000;
		int[] or = new int[n];// order of jury to be assigned to teacher k
		int[] ma = new int[n];
		
		for(int i = 0; i < n; i++){
			or[i] = i;
			ma[i] = (Integer)matchScore[i].get(v);
		}
		for(int i = 0; i < n-1; i++)
			for(int j = i+1; j < n; j++)
				if(ma[i] > ma[j]){
					int tmp = or[i]; or[i] = or[j]; or[j] = tmp;
					tmp = ma[i]; ma[i] = ma[j]; ma[j] = tmp;
				}
		
		if(System.currentTimeMillis() - t0 > timeLimit) return;
		if(stop) return;
		
		for(int i = 0; i < n; i++){
			int r = or[i];
			int idx_president = r*7+c;
			int idx_room = r*7+6;
		//for(int i = 0; i < domain[var_idx].size(); i++){
			
			
			boolean ok = true;// check the consistency of value v to be assigned to x[var_idx]
			
			ok = x[idx_president] == 0;
			
			if(ok)	ok = checkRowConsistencyJuryMembers(r,v,c);
				//if(!ok && k >= depth) System.out.println("TRY(" + k + "), idx_president = " + idx_president + ", r = " + r + ", c = " + c + ", v = " + v + ", FALSE consistency of jury");
				if(ok) ok = jury.get(r).getSupervisorId() != v;
				
				if(ok){
					if(x[r*7+6] > 0){// room has been assigned
						int rid = x[r*7+6]; 
						//if(rid != rooms.get(k).getID()) ok = false;
					}
					if(x[r*7+5] > 0){// slot has been assigned
						int slid = x[r*7+5]; 
						if(hustOfSlot.get(slid).contains(v)) ok = false;
						
					}
				}
			if(!ok) continue;
			
			x[idx_president] = v;// ASSIGN
			if(k >  depth)System.out.println("AssignPresidentsRooms --> TRY, k = " + k + ", x[" + idx_president + "] = " + v);
			//x[idx_room] = rooms.get(k).getID();
				
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
				//sSystem.out.println("occTeacher(" + v + ") = " + occTeacher.get(v));
				if(occTeacher.get(v) == maxHustOfRoom.get(v)){
					//System.out.println("AssignPresidentsRooms, occTeacher of v == maxHustOfRoom(v)");
					if(k == rooms.size()-1){
						int count = 0;//count the number of instantiated variables
						for(int i1 = 0; i1 < n; i1++)
							if(x[i1*7+2] > 0) count++;
						if(count == n)
							updateBest();
					}else{
						TRY(k+1);
					}
				}else{
					TRY(k);
				}
				
			

			// RECOVER status
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
			
			x[idx_president] = 0;// recover
			//x[idx_room] = 0;
			
		//}
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
