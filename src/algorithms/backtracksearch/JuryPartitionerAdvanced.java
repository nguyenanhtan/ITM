package algorithms.backtracksearch;

import java.util.*;

import utils.Configure;
import utils.Utility;

import DataEntity.*;

import DataEntity.JuryDataForScheduling;

public class JuryPartitionerAdvanced {

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
	int[] occ;// occ[v] is the number of items assigned to the bin v
	int[][] occTJ;// occ[t][j] is the number of occurrences of teacher t in room j
	HashSet<Integer>[] nonHustJury; //nonHustJury[j] is the set of non hust members assigned to room j
	HashSet<Integer> fixedTeachers;
	
	HashMap[] matchJuryProfessors;// matchJuryProfessors[i].get(j) is the match score of jury i and professor j
	int[][] matchJuryJury;
	
	int[] maxOccJ;
	Vector<JuryInfo> jury = null;
	Vector<Room> rooms;
	double t0;
	double timeLimit;
	
	/*
	Vector<JuryInfo> extract(Vector<JuryInfo> jury, Vector<Teacher> hustTeachers, Vector<Teacher> nonHustTeachers, HashMap<JuryInfo, Integer> mark, int sz){
		HashMap<Integer, Integer> occHT = new HashMap<Integer, Integer>();
		AT = new Vector<Teacher>();
		for(int i = 0; i < hustTeachers.size(); i++){
			Teacher t = hustTeachers.get(i);
			occHT.put(t.getID(), 0);
			AT.add(t);
		}
		for(int i = 0; i < nonHustTeachers.size(); i++){
			Teacher t = nonHustTeachers.get(i);
			occHT.put(t.getID(), 0);
			AT.add(t);
		}
		for(int i = 0; i < jury.size(); i++) if(mark.get(jury.get(i))==0){
			JuryInfo J = jury.get(i);
			int e1ID = J.getExaminerId1();
			int e2ID = J.getExaminerId2();
			int preID = J.getPresidentId();
			int secreID = J.getSecretaryId();
			int addID = J.getAdditionalMemberId();
			if(e1ID > 0) occHT.put(e1ID, occHT.get(e1ID)+1);
			if(e2ID > 0) occHT.put(e2ID, occHT.get(e2ID)+1);
			if(preID > 0) occHT.put(preID, occHT.get(preID)+1);
			if(secreID > 0) occHT.put(secreID, occHT.get(secreID)+1);
			if(addID > 0) occHT.put(addID, occHT.get(addID)+1);
		}
		int maxOcc = -1;
		int maxTea = -1;
		for(int i = 0; i < AT.size(); i++){
			Teacher t = AT.get(i);
			//System.out.println("T[" + i + "], id = " + t.getID() + ", occ = " + occHT.get(t.getID()));
			if(maxOcc < occHT.get(t.getID())){
				maxOcc = occHT.get(t.getID());
				maxTea = t.getID();
			}
		}

		Vector<JuryInfo> e_jury = new Vector<JuryInfo>();
		for(int i = 0; i < jury.size(); i++){
			JuryInfo J = jury.get(i);
			if(mark.get(J) == 0){
				if(J.juryProfessor(maxTea)){
					e_jury.add(J);
					mark.put(J, 1);
				}
			}
		}
		return e_jury;
	}
	*/
	
	/*
	public Vector<JuryDataForScheduling> partitionJury(Vector<JuryInfo> jury, Vector<Teacher> hustTeachers, Vector<Teacher> nonHustTeachers, int nbJuries, int timeLimit){
		System.out.println("JuryPartitioner::partitionJuryAdvanced");
		this.jury = jury;
		this.hustTeachers = hustTeachers;
		this.nonHustTeachers = nonHustTeachers;
		this.nbJuries = nbJuries;
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
			//occHT.put(t.getID(), 0);
			AT.add(t);
		}
		for(int i = 0; i < nonHustTeachers.size(); i++){
			Teacher t = nonHustTeachers.get(i);
			//occHT.put(t.getID(), 0);
			AT.add(t);
		}

		JuryInfo[] arr = new JuryInfo[n];
		for(int i = 0; i < n; i++){
			JuryInfo J = jury.get(i);
			arr[i] = J;
			if(J.getRoomId() == 0) J.setRoomId(100000);
		}
		
		//sort arr
		for(int i = 0; i < n-1; i++)
			for(int j = i+1; j < n; j++)
				if(arr[i].getRoomId() > arr[j].getRoomId()){
					JuryInfo tj = arr[i]; arr[i] = arr[j]; arr[j] = tj;
				}
		for(int i = 0; i < n; i++){
			if(arr[i].getRoomId() == 100000) arr[i].setRoomId(0);
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
		
		HashMap<Integer, Integer> occHT = new HashMap<Integer, Integer>();
		int maxTeacherID  = -1;
		Vector<Teacher> AT = new Vector<Teacher>();
		for(int i = 0; i < hustTeachers.size(); i++){
			Teacher t = hustTeachers.get(i);
			occHT.put(t.getID(), 0);
			AT.add(t);
			if(t.getID() > maxTeacherID) maxTeacherID = t.getID();
		}
		for(int i = 0; i < nonHustTeachers.size(); i++){
			Teacher t = nonHustTeachers.get(i);
			occHT.put(t.getID(), 0);
			AT.add(t);
			if(t.getID() > maxTeacherID) maxTeacherID = t.getID();
		}
		
		
		m = jury.size();
		x = new int[m];
		opt_x = new int[m];
		domain = new Vector[m];
		for(int i = 0; i < m; i++){
			JuryInfo J = jury.get(i);
			domain[i] = new Vector<Integer>();
			if(J.getRoomId() > 0){
				domain[i].add(J.getRoomId());
			}else{
				for(int j = 1; j <= nbJuries; j++ )
					domain[i].add(j);
			}
		}
		
		maxOccJ = new int[nbJuries+1];
		maxOccJ[1] = 5;
		maxOccJ[2] = 5;
		//maxOccJ[3] = 4;
		//maxOccJ[4] = 4;
		//maxOccJ[5] = 4;
		
		occTJ = new int[maxTeacherID+1][nbJuries+1];
		for(int i = 0; i < maxTeacherID+1; i++)
			for(int v = 0; v < nbJuries+1; v++)
				occTJ[i][v] = 0;
		nonHustJury = new HashSet[nbJuries+1];
		for(int v = 1; v <= nbJuries; v++)
			nonHustJury[v] = new HashSet<Integer>();
		occ = new int[nbJuries+1];
		for(int v = 1; v <= nbJuries; v++){
			occ[v] = 0;
		}
		t0 = System.currentTimeMillis();
		System.out.println("Starting Try, timelimit = " + timeLimit);
		bestEval = 100000;
		Try(0);
		
		arr = new JuryInfo[m];
		for(int i = 0; i < m; i++){
			JuryInfo J = jury.get(i);
			J.setRoomId(opt_x[i]);
			arr[i] = J;
		}
		
		//sort arr
		for(int i = 0; i < m-1; i++)
			for(int j = i+1; j < m; j++)
				if(arr[i].getRoomId() > arr[j].getRoomId()){
					JuryInfo tj = arr[i]; arr[i] = arr[j]; arr[j] = tj;
				}
		
		System.out.println("RESULT:");
		for(int i = 0; i < n; i++){
			JuryInfo J = arr[i];
			System.out.println("Room " + J.getRoomId() + "\t" + J.getSupervisorId() + "\t" + J.getExaminerId1() + "\t" + J.getExaminerId2() + "\t" + 
			J.getPresidentId() + 
					"\t" + J.getSecretaryId() + "\t" + J.getAdditionalMemberId());
		}
		jury.clear();
		for(int i = 0; i < m; i++)
			jury.add(arr[i]);
		
		AssignNonHustToJuryRooms AN = new AssignNonHustToJuryRooms();
		AN.assignNonHustMembersToJuryRooms(jury, nonHustTeachers, nbJuries, timeLimit);
		
		AssignHustToJuryRooms A = new AssignHustToJuryRooms();
		A.assignHustMembersToJuryRooms(jury, hustTeachers, nbJuries, timeLimit);
		
		return juryDataArr;
	}
	 */
	
	/*
	public HashMap[] getMatchJuryProfessor(){ return this.matchJuryProfessors;}
	
	private int getMin(int a, int b){
		return a < b ? a : b;
	}
	private int computeMatchScore(int[] sm, int[] pm){
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
	private int[] getStudentMatchScore(int studentID, Vector<StudentSubjectCategoryMatch> sscm, Vector<SubjectCategory> categories){
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
	private int[] getProfessorMatchScore(int professorID, Vector<ProfessorSubjectCategoryMatch> pscm, Vector<SubjectCategory> categories){
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
	private int computeMatchScore(int studentID, int professorID, Vector<StudentSubjectCategoryMatch> sscm, Vector<ProfessorSubjectCategoryMatch> pscm, Vector<SubjectCategory> categories){
		int[] sm = getStudentMatchScore(studentID, sscm, categories);
		int[] pm = getProfessorMatchScore(professorID, pscm, categories);
		
		
		return computeMatchScore(sm,pm);
	}
	*/
	
	public Vector<JuryDataForScheduling> partitionJury(Vector<JuryInfo> jury, Vector<Room> rooms, Vector<Teacher> hustTeachers, Vector<Teacher> nonHustTeachers, int timeLimit){
		System.out.println("JuryPartitioner::partitionJuryAdvanced");
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
			//occHT.put(t.getID(), 0);
			AT.add(t);
		}
		for(int i = 0; i < nonHustTeachers.size(); i++){
			Teacher t = nonHustTeachers.get(i);
			//occHT.put(t.getID(), 0);
			AT.add(t);
		}

		for(int i = 0; i < rooms.size(); i++){
			Room r = rooms.get(i);
			r.setNbJuries(sz_of_jury);
			r.setMaxNbHustMembers(hustTeachers.size()/nbJuries);
			r.setMaxNbNonHustMembers(nonHustTeachers.size()/nbJuries);
		}
		for(int i = 0; i < d; i++){
			Room r  = rooms.get(i);
			r.setNbJuries(r.getMaxNbJuries()+1);
			
		}
		for(int i = 0; i < hustTeachers.size()%nbJuries; i++){
			Room r = rooms.get(i);
			r.setMaxNbHustMembers(r.getMaxNbHustMembers()+1);
		}
		for(int i = 0; i < nonHustTeachers.size()%nbJuries; i++){
			Room r = rooms.get(i);
			r.setMaxNbNonHustMembers(r.getMaxNbNonHustMembers()+1);
		}
		
		for(int i = 0; i < rooms.size(); i++){
			Room r = rooms.get(i);
			System.out.println("Room[" + i + "] " + ", nbJuries = " + r.getMaxNbJuries() + ", maxHust = " + r.getMaxNbHustMembers() + ", nonHust = " + r.getMaxNbNonHustMembers());
		}
		
		JuryInfo[] arr = new JuryInfo[n];
		for(int i = 0; i < n; i++){
			JuryInfo J = jury.get(i);
			arr[i] = J;
			if(J.getRoomId() == 0) J.setRoomId(100000);
		}
		
		//sort arr
		for(int i = 0; i < n-1; i++)
			for(int j = i+1; j < n; j++)
				if(arr[i].getRoomId() > arr[j].getRoomId()){
					JuryInfo tj = arr[i]; arr[i] = arr[j]; arr[j] = tj;
				}
		for(int i = 0; i < n; i++){
			if(arr[i].getRoomId() == 100000) arr[i].setRoomId(0);
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
		
		/*
		Vector<StudentSubjectCategoryMatch> sscm = Utility.getStudentSubjectCategoryMatches();
		Vector<ProfessorSubjectCategoryMatch> spcm = Utility.getProfessorSubjectCategoryMatches();
		Vector<SubjectCategory> categories = Utility.getSubjectCategories();
		*/
		
		matchJuryProfessors = Utility.computeMatchJuryProfessor(jury, AT);
		
		/*
		matchJuryProfessors = new HashMap[jury.size()];
		for(int i = 0; i < jury.size(); i++){
			JuryInfo J = jury.get(i);
			matchJuryProfessors[i] = new HashMap<Integer, Integer>();
			for(int j = 0; j < AT.size(); j++){
				Teacher t = AT.get(j);
				int matchScore = computeMatchScore(J.getStudentID(), t.getID(), sscm,spcm,categories);
				matchJuryProfessors[i].put(t.getID(), matchScore);
				System.out.println("JuryPartitionerAdvanced::search, matchScore[" + i + "][" + t.getID() + "] = " + matchJuryProfessors[i].get(t.getID()));
			}
		}
		*/
		
		matchJuryJury = Utility.computeMatchJuryJury(jury);
		
		/*
		matchJuryJury = new int[n][n];
		for(int i = 0; i < n-1; i++){
			int[] smi = getStudentMatchScore(jury.get(i).getStudentID(),sscm,categories);
			for(int j = i+1; j < n; j++){
				int[] smj = getStudentMatchScore(jury.get(j).getStudentID(),sscm,categories);
				int score = computeMatchScore(smi,smj);
				matchJuryJury[i][j] = score;
				matchJuryJury[j][i] = score;
			}
		}
		
		for(int i = 0; i < n; i++){
			for(int j = 0; j < n; j++)
				System.out.print(matchJuryJury[i][j] + " ");
			System.out.println();
		}
		*/
		
		HashMap<Integer, Integer> occHT = new HashMap<Integer, Integer>();
		int maxTeacherID  = -1;
		Vector<Teacher> AT = new Vector<Teacher>();
		for(int i = 0; i < hustTeachers.size(); i++){
			Teacher t = hustTeachers.get(i);
			occHT.put(t.getID(), 0);
			AT.add(t);
			if(t.getID() > maxTeacherID) maxTeacherID = t.getID();
		}
		for(int i = 0; i < nonHustTeachers.size(); i++){
			Teacher t = nonHustTeachers.get(i);
			occHT.put(t.getID(), 0);
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
		for(int i = 0; i < m; i++){
			JuryInfo J = jury.get(i);
			domain[i] = new Vector<Integer>();
			if(J.getRoomId() > 0){
				domain[i].add(J.getRoomId());
				opt_x[i] = J.getRoomId();
			}else{
				for(int j = 1; j <= nbJuries; j++ )
					domain[i].add(j);
			}
		}
		
		maxOccJ = new int[nbJuries+1];
		for(int i = 1; i <= nbJuries; i++)
			maxOccJ[i] = rooms.get(i-1).getMaxNbJuries();
		//maxOccJ[1] = 5;
		//maxOccJ[2] = 5;
		//maxOccJ[3] = 4;
		//maxOccJ[4] = 4;
		//maxOccJ[5] = 4;
		
		occTJ = new int[maxTeacherID+1][nbJuries+1];
		for(int i = 0; i < maxTeacherID+1; i++)
			for(int v = 0; v < nbJuries+1; v++)
				occTJ[i][v] = 0;
		nonHustJury = new HashSet[nbJuries+1];
		for(int v = 1; v <= nbJuries; v++)
			nonHustJury[v] = new HashSet<Integer>();
		occ = new int[nbJuries+1];
		for(int v = 1; v <= nbJuries; v++){
			occ[v] = 0;
		}
		t0 = System.currentTimeMillis();
		System.out.println("JuryPartitionerAdvanced::search, Starting Try, timelimit = " + timeLimit);
		nbObj = 2;
		bestEval = new int[nbObj];
		for(int i = 0; i < nbObj; i++)
			bestEval[i] = 100000;
		
		
		Try(0);
		System.out.println("JuryPartitionerAdvanced::search, Try finished, timelimit = " + timeLimit);
		
		arr = new JuryInfo[m];
		for(int i = 0; i < m; i++){
			JuryInfo J = jury.get(i);
			J.setRoomId(opt_x[i]);
			arr[i] = J;
		}
		
		//sort arr
		for(int i = 0; i < m-1; i++)
			for(int j = i+1; j < m; j++)
				if(arr[i].getRoomId() > arr[j].getRoomId()){
					JuryInfo tj = arr[i]; arr[i] = arr[j]; arr[j] = tj;
				}
		
		System.out.println("RESULT:");
		for(int i = 0; i < n; i++){
			JuryInfo J = arr[i];
			System.out.println("Room " + J.getRoomId() + "\t" + J.getSupervisorId() + "\t" + J.getExaminerId1() + "\t" + J.getExaminerId2() + "\t" + 
			J.getPresidentId() + 
					"\t" + J.getSecretaryId() + "\t" + J.getAdditionalMemberId());
		}
		/*
		jury.clear();
		for(int i = 0; i < m; i++)
			jury.add(arr[i]);
		*/
		
		
		AssignTopExpert ate = new AssignTopExpert();
		ate.search(jury, rooms, matchJuryProfessors, hustTeachers, timeLimit);
		
		
		
		
		
		AssignNonHustToJuryRooms AN = new AssignNonHustToJuryRooms();
		AN.assignNonHustMembersToJuryRooms(jury, nonHustTeachers, rooms, timeLimit);
		
		AssignHustToJuryRooms A = new AssignHustToJuryRooms();
		A.assignHustMembersToJuryRooms(jury, hustTeachers, rooms, timeLimit);
		
		/*
		HashSet<Integer> remain_non_hust = new HashSet<Integer>();
		
		for(int v = 1; v <= nbJuries; v++){
			Vector<JuryInfo> V = new Vector<JuryInfo>();
			for(int i = 0; i < m; i++){
				if(arr[i].getRoomId() == v){
					V.add(arr[i]);
				}
			}
			Vector<Teacher> sel_hust = new Vector<Teacher>();
			Vector<Teacher> sel_non_hust = new Vector<Teacher>();
			JuryDataForScheduling jdfs = new JuryDataForScheduling(V,sel_hust,sel_non_hust);
			
		}
		*/
		return juryDataArr;
	}

	private int[] eval(){
		int[] e = new int[nbObj];
		int bestE = 0;
		for(int tj = 0; tj < AT.size(); tj++){
			Teacher t = AT.get(tj);
			int o = 0;
			for(int v = 1; v <= nbJuries; v++){
				if(occTJ[t.getID()][v] > 0)
					o++;
			}
			if(o > 1) bestE += o;
		}
		e[0] = bestE;
		
		int bestMatchSubject = 0;
		for(int v = 1; v <= nbJuries; v++){
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
		e[1] = bestMatchSubject;
		
		return e;
	}
	private void updateBest(){
		//System.out.println("JuryPartitioner::updateBest...");
		boolean ok = true;
		int[] rid  = new int[jury.size()];
		
		for(int i = 0; i < m; i++){
			JuryInfo J = jury.get(i);
			rid[i] = J.getRoomId();// store
			J.setRoomId(x[i]);
		}
		
		AssignNonHustToJuryRooms AN = new AssignNonHustToJuryRooms();
		ok = AN.canAssignNonHustMembersToJuryRooms(jury, nonHustTeachers, rooms, timeLimit);
		
		if(!ok) return;
		
		AssignHustToJuryRooms A = new AssignHustToJuryRooms();
		ok = A.canAssignHustMembersToJuryRooms(jury, hustTeachers, rooms, timeLimit);
		
		if(!ok) return;

		int[] e = eval();
		//if(bestEval > e){
		if(Utility.compare(bestEval,e) == 1){
			System.out.println("JuryPartitioner::updateBest, bestEval = " + bestEval[0] + "," + bestEval[1] + 
					", new eval = " + e[0] + "," + e[1]);
			bestEval = e;
			for(int j = 0; j < m; j++)
				opt_x[j] = x[j];
		}
		
		for(int i = 0; i < m; i++){
			JuryInfo J = jury.get(i);
			J.setRoomId(rid[i]);// restore
		}
		//System.out.println("JuryPartitionerAdvanced::updateBest...finished");
	}

	private boolean checkSeparate(int tid, int roomID){
		if(tid > 0){
			for(int vi = 1; vi <= nbJuries; vi++)
				if(vi != roomID && occTJ[tid][vi] > 0) return true;
		}
		return false;
	}
	private void Try(int k){
		if(System.currentTimeMillis() - t0 > timeLimit) return;
		boolean optEval = true;
		for(int i = 0; i < nbObj; i++)
			if(bestEval[i] > 0) optEval = false;
		if(optEval) return;
		
		JuryInfo J = jury.get(k);
		
		Vector<Integer> D = (Vector<Integer>)domain[k];
		//System.out.println("Try(" + k + "), D.sz = " + D.size());
		for(int iv = 0; iv < D.size(); iv++){
			int v = (Integer)D.get(iv);
			boolean ok = true;
			if(occ[v] >= maxOccJ[v]) ok = false;
			if(ok)if(J.getExaminerId1() > 0 && nonHustJury[v].size() >= rooms.get(v-1).getMaxNbNonHustMembers() && 
					!nonHustJury[v].contains(J.getExaminerId1())) ok = false;
			if(ok)if(J.getAdditionalMemberId() > 0 && nonHustJury[v].size() >= rooms.get(v-1).getMaxNbNonHustMembers() && 
					!nonHustJury[v].contains(J.getAdditionalMemberId())) ok = false;

			if(ok) ok = !checkSeparate(J.getExaminerId1(),v);
			if(ok) ok = !checkSeparate(J.getExaminerId2(),v);
			if(ok) ok = !checkSeparate(J.getPresidentId(),v);
			if(ok) ok = !checkSeparate(J.getSecretaryId(),v);
			if(ok) ok = !checkSeparate(J.getAdditionalMemberId(),v);
			
			
			if(!ok){
				//System.out.println("Try(" + k + "), D.sz = " + D.size() + " --> continue, m = " + m);
				continue;
			}
			
			x[k] = v;
			occ[v]++;
			
			if(J.getExaminerId1() > 0){ 
				occTJ[J.getExaminerId1()][v]++;
				if(occTJ[J.getExaminerId1()][v]==1)
					nonHustJury[v].add(J.getExaminerId1());
			}
			if(J.getSupervisorId() > 0) occTJ[J.getSupervisorId()][v]++;
			if(J.getExaminerId2() > 0) occTJ[J.getExaminerId2()][v]++;
			if(J.getPresidentId() > 0) occTJ[J.getPresidentId()][v]++;
			if(J.getSecretaryId() > 0) occTJ[J.getSecretaryId()][v]++;
			
			if(J.getAdditionalMemberId() > 0){ 
				occTJ[J.getAdditionalMemberId()][v]++;
				if(occTJ[J.getAdditionalMemberId()][v] == 1)
					nonHustJury[v].add(J.getAdditionalMemberId());
			}
			
			if(k == m-1){
				updateBest();
			}else{
				Try(k+1);
			}

			if(J.getExaminerId1() > 0){
				occTJ[J.getExaminerId1()][v]--;
				if(occTJ[J.getExaminerId1()][v] == 0)
					nonHustJury[v].remove(J.getExaminerId1());
			}
			if(J.getSupervisorId() > 0) occTJ[J.getSupervisorId()][v]--;
			if(J.getExaminerId2() > 0) occTJ[J.getExaminerId2()][v]--;
			if(J.getPresidentId() > 0) occTJ[J.getPresidentId()][v]--;
			if(J.getSecretaryId() > 0) occTJ[J.getSecretaryId()][v]--;
			
			if(J.getAdditionalMemberId() > 0){
				occTJ[J.getAdditionalMemberId()][v]--;
				if(occTJ[J.getAdditionalMemberId()][v] == 0)
					nonHustJury[v].remove(J.getAdditionalMemberId());
			}
			occ[v]--;
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
