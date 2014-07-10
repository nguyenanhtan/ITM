package algorithms.backtracksearch;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import algorithms.localsearch.AssignRooms;

import DataEntity.*;

import java.util.*;

public class BackTrackSearch {

	/**
	 * @param args
	 */
	
	Vector<JuryInfo> jury;
	int nbRooms;
	int[] var_slots;
	int	nbStudents;
	Vector<Slot> slots;
	Vector<Student> students;
	Vector<Integer> professors;
	int minSlot;
	int maxSlot;
	int[] countSlot;// countSlot[sl] = number of sessions assigned to slot sl
	int[][] slotFeasible; // slotFeasible[st][sl] = 0 if slot sl can be assigned to student st
	boolean [][] conflict;
	//int[] appear;//appear[i] = number of variables assigned to value i
	boolean found = false;
	int[] solution;
	
	//
	int nbClusters = 0;
	int[] idx;
	int[] d;// d[i] is the degree of i
	int[] order;
	
	// view parameters
	int view_length;
	int maxTime;
	double t0;
	int bestEval;
	
	public BackTrackSearch(int vl){
		view_length = vl;
	}
	void explore(int k){
		idx[k] = nbClusters;
		for(int i = 0; i < nbStudents; i++)
			if(idx[i] == -1 && conflict[i][k])
				explore(i);
	}
	
	void preprocess(){
		cluster();
		d = new int[nbStudents];
		for(int i = 0; i < nbStudents; i++)
			d[i] = 0;
		for(int i = 0; i < nbStudents; i++){
			for(int j = 0; j < nbStudents; j++)
				if(conflict[i][j])
					d[i]++;
		}
		
		order = new int[nbStudents];
		for(int i = 0; i < nbStudents; i++)
			order[i] = i;
		for(int i = 0; i < nbStudents-1; i++)
			for(int j = i+1; j < nbStudents; j++)
				if(d[i] < d[j]){
					int tmp = order[i];
					order[i] = order[j];
					order[j] = tmp;
					tmp = d[i]; d[i] = d[j]; d[j] = tmp;
				}
		
		for(int i = 0; i < nbStudents; i++){
			System.out.println(i + " order " + order[i] + " d = " + d[i]);
		}
	}
	void cluster(){
		idx = new int[nbStudents];
		for(int i = 0; i < nbStudents; i++) idx[i] = -1;
		for(int i = 0; i < nbStudents; i++){
			if(idx[i] == -1){
				nbClusters++;
				explore(i);
			}
		}
		for(int c = 1; c <= nbClusters; c++){
			System.out.print("Cluster " + c + ": ");
			for(int i = 0; i < nbStudents; i++)
				if(idx[i] == c)
					System.out.print(i + ",");
			System.out.println("");
		}
	}
	
	int computeConsecutive(HashSet<Integer> slots){
		int v = 0;
		int sz = slots.size();
		int[] s = new int[sz];
		Iterator it = slots.iterator();
		int idx = -1;
		while(it.hasNext()){
			int sl = (Integer)it.next();
			idx++;
			s[idx] = sl;
		}
		for(int i = 0; i < sz - 1; i++)
			for(int j = i+1; j < sz; j++)
				if(s[i] > s[j]){
					int tmp = s[i]; s[i] = s[j]; s[j] = tmp;
				}
		for(int i = 0; i < sz-1; i++){
			if(s[i+1]-s[i] > 1)
				v += s[i+1] - s[i] - 1;
		}
		return v;
	}
	int eval(){
		HashMap<Integer, HashSet<Integer>> slots_professors = new HashMap<Integer, HashSet<Integer>>();
		for(int i = 0; i < professors.size(); i++){
			int tid = professors.get(i);
			slots_professors.put(tid, new HashSet<Integer>());
		}
		
		for(int i = 0; i < jury.size(); i++){
			JuryInfo J = jury.get(i);
			int sl = var_slots[i];
			int e1 = J.getExaminerId1();
			int e2 = J.getExaminerId2();
			int p = J.getPresidentId();
			int s = J.getSecretaryId();
			int a = J.getAdditionalMemberId();
			slots_professors.get(e1).add(sl);
			slots_professors.get(e2).add(sl);
			slots_professors.get(p).add(sl);
			slots_professors.get(s).add(sl);
			slots_professors.get(a).add(sl);
		}
		
		int total = 0;
		for(int i = 0; i < professors.size(); i++){
			Integer t = professors.get(i);
			total += computeConsecutive(slots_professors.get(t));
		}
		return total;
	}
	void retrieveSolution(){
		//if(found) return;
		int e = eval();
		if(bestEval > e){
			found = true;
			System.out.println("BackTracjSearch::updateBest, bestEval = " + bestEval + " new eval = " + e);
			bestEval = e;
			for(int i = 0; i < nbStudents; i++)
				solution[i] = var_slots[i];
		}
	}
	void TRY(int j){
		
		int k = order[j];
		//if(found) return;
		if(bestEval == 0) return;
		if(System.currentTimeMillis() - t0 > maxTime) return;
		//System.out.println("TRY(" + j + "), order[" + j + "] = " + order[j] + " found = " + found + " minSlot = " + minSlot + " maxSlot = " + maxSlot +
				//" nbRooms " + nbRooms);
		for(int sl = minSlot; sl <= maxSlot; sl++){
			//System.out.println("countSlot[" + sl + "] = " + countSlot[sl] + " slotFeasible[" + k + "][" + sl + "] = " + slotFeasible[k][sl]);
			if(countSlot[sl] < nbRooms && slotFeasible[k][sl] == 0){
				var_slots[k] = sl;
				//appear[sl]++;
				if(j == view_length){
				for(int i = 0; i < j; i++)
					System.out.print(i + "[" + var_slots[order[i]] + "]");
					System.out.println(" var_slots[" + k + "] = " + sl);
				}
				countSlot[sl]++;
				for(int i = j+1; i < nbStudents; i++){
					if(conflict[k][order[i]])
						slotFeasible[order[i]][sl]++;
				}
				for(int sli = minSlot; sli < sl; sli++) if(countSlot[sli] == 0){
					for(int i = j+1; i < nbStudents; i++){
						slotFeasible[order[i]][sli]++;
					}
				}
				if(j == nbStudents-1){
					retrieveSolution();
					found = true;
					
				}else{
					TRY(j+1);
				}
				for(int sli = minSlot; sli < sl; sli++) if(countSlot[sli] == 0){
					for(int i = j+1; i < nbStudents; i++){
						slotFeasible[order[i]][sli]--;
					}
				}
				
				for(int i = j+1; i < nbStudents; i++){
					if(conflict[k][order[i]])
						slotFeasible[order[i]][sl]--;
				}
				countSlot[sl]--;
			}
		}
	}
	
	public void printResult(){
		for(int i = 0; i < nbStudents; i++){
			Student s = students.elementAt(i);
			int sl = solution[i];
			System.out.print(s.getName() + ", ");
			//for(int j = 0; j < s.getExaminers().size(); j++){
				//Professor p = s.getExaminers().elementAt(j);
				//System.out.print(p.getName() + ", ");
			//}
			//System.out.println(s.getPresident().getName() + "," + s.getSecretary().getName() + "," + s.getAdditionalMember().getName() + ", " +  
			//sl);			
		}
	}
	public void search(Vector<JuryInfo> listJury, Vector<Slot> slots, Vector<Room> rooms){
		//slots = D.getSlots();
		//students = D.getStudents();
		//professors = D.getProfessors();
		nbRooms = rooms.size();
		nbStudents = listJury.size();
		var_slots = new int[nbStudents];
		solution = new int[nbStudents];
		conflict = new boolean[nbStudents][nbStudents];
		for(int i = 0; i < nbStudents; i++){
			JuryInfo jury_i = listJury.get(i);
			for(int j = 0; j < nbStudents; j++){
				JuryInfo jury_j = listJury.get(j);
				//Student si = students.elementAt(i);
				//Student sj = students.elementAt(j);
				conflict[i][j] = jury_i.conflict(jury_j);
			}				
		}
		minSlot = 10000;
		maxSlot = -1;
		for(int i = 0; i < slots.size(); i++){
			Slot sl = slots.elementAt(i);
			if(minSlot > sl.getID()) minSlot = sl.getID();
			if(maxSlot < sl.getID()) maxSlot = sl.getID();
		}
		//maxSlot = 9;
		countSlot = new int[maxSlot+1];
		for(int sl = minSlot; sl <= maxSlot; sl++)
			countSlot[sl] = 0;
		
		for(int i = 0; i < nbStudents; i++){
			for(int j = 0; j < nbStudents; j++){
				if(conflict[i][j]){
					System.out.println(i + " " + students.elementAt(i).getName() + " and " + j + " " + students.elementAt(j).getName() + " conflict");
				}
			}
		}
		slotFeasible = new int[nbStudents][maxSlot+1];
		for(int i = 0; i < nbStudents; i++){
			for(int sl = minSlot; sl <= maxSlot; sl++){
				slotFeasible[i][sl] = 0;
			}
		}		
		
		HashSet<Integer> st = new HashSet<Integer>();
		for(int i = 0; i < jury.size(); i++){
			JuryInfo J = jury.get(i);
			Integer tid = J.getExaminerId1();
			st.add(tid);
			tid = J.getExaminerId2();
			st.add(tid);
			tid = J.getPresidentId();
			st.add(tid);
			tid = J.getSecretaryId();
			st.add(tid);
			tid = J.getAdditionalMemberId();
			st.add(tid);
		}
		
		Iterator it = st.iterator();
		while(it.hasNext()){
			int tid = (Integer)it.next();
			professors.add(tid);
		}
		//nbStudents = 34;
		//nbRooms = 10;
		found = false;
		preprocess();
		bestEval = 10000;
		TRY(0);
		
		if(found){
			//printResult();
			//System.out.println("minSlot = " + minSlot + " maxSlot = " + maxSlot);
			//printHTML();
			int sz = listJury.size();
			for (int i = 0; i < sz; i++) {
				JuryInfo info = listJury.get(i);
				int slotId = solution[i];//var_slots[i];
				info.setSlotId(slotId);
			}

			AssignRooms ar = new AssignRooms();
			ar.assignRooms(listJury, nbRooms);
			System.out.println("A solution found!!!!!!!!!!!!!");
		}else
			System.out.println("No solution found!!!!!!!!!!!!!");
	}

	public void search(Vector<JuryInfo> listJury, int nbSlots, int nbRooms, int maxTime){
		this.maxTime = maxTime*1000;// convert from seconds to milliseconds
		this.jury = listJury;
		
		//slots = D.getSlots();
		//students = D.getStudents();
		//professors = D.getProfessors();
		//nbRooms = rooms.size();
		this.nbRooms = nbRooms;
		nbStudents = listJury.size();
		var_slots = new int[nbStudents];
		solution = new int[nbStudents];
		conflict = new boolean[nbStudents][nbStudents];
		for(int i = 0; i < nbStudents; i++){
			JuryInfo jury_i = listJury.get(i);
			for(int j = 0; j < nbStudents; j++){
				JuryInfo jury_j = listJury.get(j);
				//Student si = students.elementAt(i);
				//Student sj = students.elementAt(j);
				conflict[i][j] = jury_i.conflict(jury_j);
			}				
		}
		minSlot = 1;//10000;
		maxSlot = nbSlots;//-1;
		/*
		for(int i = 0; i < slots.size(); i++){
			Slot sl = slots.elementAt(i);
			if(minSlot > sl.getID()) minSlot = sl.getID();
			if(maxSlot < sl.getID()) maxSlot = sl.getID();
		}
		*/
		//maxSlot = 9;
		countSlot = new int[maxSlot+1];
		for(int sl = minSlot; sl <= maxSlot; sl++)
			countSlot[sl] = 0;
		
		for(int i = 0; i < nbStudents; i++){
			for(int j = 0; j < nbStudents; j++){
				if(conflict[i][j]){
					//System.out.println(i + " " + students.elementAt(i).getName() + " and " + j + " " + students.elementAt(j).getName() + " conflict");
				}
			}
		}
		slotFeasible = new int[nbStudents][maxSlot+1];
		for(int i = 0; i < nbStudents; i++){
			for(int sl = minSlot; sl <= maxSlot; sl++){
				slotFeasible[i][sl] = 0;
			}
		}		
		HashSet<Integer> st = new HashSet<Integer>();
		for(int i = 0; i < jury.size(); i++){
			JuryInfo J = jury.get(i);
			Integer tid = J.getExaminerId1();
			st.add(tid);
			tid = J.getExaminerId2();
			st.add(tid);
			tid = J.getPresidentId();
			st.add(tid);
			tid = J.getSecretaryId();
			st.add(tid);
			tid = J.getAdditionalMemberId();
			st.add(tid);
		}
		
		professors = new Vector<Integer>();
		Iterator it = st.iterator();
		while(it.hasNext()){
			int tid = (Integer)it.next();
			professors.add(tid);
		}

		//nbStudents = 34;
		//nbRooms = 10;
		found = false;
		preprocess();
		
		t0 = System.currentTimeMillis();
		bestEval = 10000;
		System.out.println("BackTrackSearch::search, Start TRY......");
		TRY(0);
		
		if(found){
			//printResult();
			//System.out.println("minSlot = " + minSlot + " maxSlot = " + maxSlot);
			//printHTML();
			int sz = listJury.size();
			for (int i = 0; i < sz; i++) {
				JuryInfo info = listJury.get(i);
				int slotId = solution[i];//var_slots[i];
				info.setSlotId(slotId);
			}

			AssignRooms ar = new AssignRooms();
			ar.assignRooms(listJury, nbRooms);
			System.out.println("A solution found!!!!!!!!!!!!!");
		}else
			System.out.println("No solution found!!!!!!!!!!!!!");
	}

	/*
	void printHTML(){
		//write to file
		try{
			BufferedWriter fo = new BufferedWriter(new FileWriter("scheduling.html"));
			fo.write("<style type=" + '"' + "text/css" + '"' + ">\n");
			fo.write("table.timetabling_type {background-color:transparent;border-collapse:collapse;width:100%;}\n");
			fo.write("table.timetabling_type th, table.timetabling_type td {text-align:center;border:1px solid black;padding:5px;}\n");
			fo.write("table.timetabling_type th {background-color:AntiqueWhite;}\n");
			//fo.write("table.timetabling_type td:first-child {width:20%;text-align:center;}\n");
			fo.write("</style>\n");
						
			fo.write("<table border=1, class=" + '"' + "timetabling_type" + '"' + ">\n");
			fo.write("<tr>\n");
			fo.write("<td>STT</td>\n");
			fo.write("<td>Ho ten</td>\n");
			fo.write("<td>Ten de tai</td>\n");
			fo.write("<td>Phan bien 1</td>\n");
			fo.write("<td>Phan bien 2</td>\n");
			fo.write("<td>Chu tich</td>\n");
			fo.write("<td>Thu ky</td>\n");
			fo.write("<td>Uy vien</td>\n");
			fo.write("<td>Kip</td>\n");
			fo.write("</tr>\n");

			for(int i = 0; i < nbStudents; i++){
				Student st = students.elementAt(i);
				fo.write("<td>" + i + "</td>\n");
				fo.write("<td>" + st.getName() + "</td>\n");
				fo.write("<td>" + st.getTitle() + "</td>\n");
				Vector<Professor> examiner = st.getExaminers();
				for(int j = 0; j < examiner.size(); j++)
					fo.write("<td>" + examiner.elementAt(j).getName() + "</td>\n");
				fo.write("<td>" + st.getPresident().getName() + "</td>\n");
				fo.write("<td>" + st.getSecretary().getName() + "</td>\n");
				fo.write("<td>" + st.getAdditionalMember().getName() + "</td>\n");
				fo.write("<td>" + solution[i] + "</td>\n");
				fo.write("<tr>\n");
			}
			fo.close();
			
		}catch(IOException e){
			System.out.println("IO error while writing to file scheduling.html");
		}
		
	}
	*/
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
