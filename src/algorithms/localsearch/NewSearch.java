package algorithms.localsearch;

import java.util.List;
import java.util.*;
import utils.Utility;

import DataEntity.JuryInfo;
import DataEntity.Teacher;

import utils.*;

public class NewSearch {
	/**
	 * @param args
	 */
	// private Data D;
	// List<Students> students;
	int nbStudents;
	VarInt[] var_slots;
	boolean[][] conflict;
	int minSlot;
	int maxSlot;
	private List<JuryInfo> listJuryInfos;
	private List<Teacher> listProfessors;

	private int nbRooms;

	public NewSearch(List<JuryInfo> listJuryInfos,
			List<Teacher> listProfessors, int nbSlots, int nbRooms) {
		this.nbRooms = nbRooms;
		// this.nbSlots = nbSlots;
		this.listProfessors = listProfessors;
		this.listJuryInfos = listJuryInfos;
		nbStudents = listJuryInfos.size();
		var_slots = new VarInt[nbStudents];
		conflict = new boolean[nbStudents][nbStudents];
		int[] slots = new int[nbSlots];

		for (int i = 0; i < nbStudents; i++) {
			for (int j = 0; j < nbStudents; j++) {
				JuryInfo si = listJuryInfos.get(i);
				JuryInfo sj = listJuryInfos.get(j);
				if (i != j) {
					conflict[i][j] = conflictStudents(si, sj);
				} else {
					conflict[i][j] = false;
				}
			}
		}

		minSlot = 10000;
		maxSlot = -1;
		//for (int i = 0; i < nbSlots; i++) {
		for (int i = 1; i <= nbSlots; i++) {
			slots[i-1] = i;
		}
		//for (int i = 0; i < nbSlots; i++) {
		for (int i = 1; i <= nbSlots; i++) {
			if (minSlot > slots[i-1]) {
				minSlot = slots[i-1];
			}
			if (maxSlot < slots[i-1]) {
				maxSlot = slots[i-1];
			}
		}
		System.out.println("max slot :" + maxSlot);
		System.out.println("min slot :" + minSlot);
		// maxSlot = 10;
		for (int i = 0; i < nbStudents; i++) {
			var_slots[i] = new VarInt(i, minSlot, maxSlot);
			// var_slots[i].setValue(minSlot);
		}

	}

	public boolean conflictStudents(JuryInfo si, JuryInfo sj) {
		if (si.juryProfessor(sj.getPresidentId())) {
			return true;
		}
		if (si.juryProfessor(sj.getSecretaryId())) {
			return true;
		}
		if (si.juryProfessor(sj.getAdditionalMemberId())) {
			return true;
		}
		if (si.juryProfessor(sj.getExaminerId1())) {
			return true;
		}
		if (si.juryProfessor(sj.getExaminerId2())) {
			return true;
		}

		return false;
	}

	private void assignTimeSlot(ConstraintSystem CS, int maxTime) {
		for (int i = 0; i < nbStudents - 1; i++) {
			for (int j = i + 1; j < nbStudents; j++) {
				if (conflict[i][j]) {
					VarInt vari = var_slots[i];
					VarInt varj = var_slots[j];
					CS.post(new NotEqual(vari, varj));
				}
			}
		}
		AtMost am = new AtMost(var_slots, nbRooms);
		CS.post(am);
		CS.close();

		for (int i = 0; i < nbStudents; i++) {
			var_slots[i].setValue(minSlot);
		}

		System.out.println("violations = " + CS.violations());

		int[][] tabu = new int[nbStudents][maxSlot + 1];

		for (int i = 0; i < nbStudents; i++) {
			for (int sl = minSlot; sl <= maxSlot; sl++) {
				tabu[i][sl] = -1;
			}
		}

		int tbl = 20;

		int it = 1;
		int bestV = CS.violations();
		int oldV = CS.violations();
		double t0 = System.currentTimeMillis();
		
		maxTime = maxTime*1000;//convert seconds into miliseconds
		//while (it < maxIter && bestV > 0) {
		while(System.currentTimeMillis() - t0 < maxTime && bestV > 0){
			int minDelta = 1000000;
			int sel_i = -1;
			int sel_sl = -1;
			for (int i = 0; i < nbStudents; i++) {
				for (int sl = minSlot; sl <= maxSlot; sl++) {
					int d = CS.getAssignDelta(var_slots[i], sl);
					if (CS.violations() + d < bestV || tabu[i][sl] < it) {
						if (d < minDelta) {
							minDelta = d;
							sel_i = i;
							sel_sl = sl;
						}
					}
				}
			}

			if (sel_i >= 0 && sel_sl >= 0) {
				System.out.println("sel_i = " + sel_i + " sel_sl = " + sel_sl);

				CS.propagate(var_slots[sel_i], sel_sl);
				var_slots[sel_i].setValue(sel_sl);

				tabu[sel_i][sel_sl] = it + tbl;

				if (oldV + minDelta != CS.violations()) {
					System.out.println("Error, oldV = " + oldV + " delta = "
							+ minDelta + " violations = " + CS.violations());
					return;
				}
				if (!CS.verify()) {
					System.out.println("NOT verify");
					return;
				}
				oldV = CS.violations();
				if (CS.violations() < bestV) {
					bestV = CS.violations();
				}
				System.out.println("Step " + it + " : Assign var_slots["
						+ sel_i + "] to " + sel_sl + " violations = "
						+ CS.violations() + " best = " + bestV);
			}
			it++;
		}

		System.out.println("NewSearch::assignSlot, AtMost = " + am.violations()
				+ " CS = " + CS.violations());
	}

	private void assignContinuousSlot(ConstraintSystem csNotEqual, int maxTime) {
		ConstraintSystem csContinuous = new ConstraintSystem();
		Continuous continuous = new Continuous(listJuryInfos, listProfessors,
				var_slots);
		csContinuous.post(continuous);
		csContinuous.close();
		System.out.println("violations = " + csContinuous.violations());

		int[][] tabu = new int[nbStudents][maxSlot + 1];

		for (int i = 0; i < nbStudents; i++) {
			for (int sl = minSlot; sl <= maxSlot; sl++) {
				tabu[i][sl] = -1;
			}
		}

		int tbl = 20;

		int it = 1;
		int bestV = csContinuous.violations();
		int oldV = csContinuous.violations();

		maxTime = maxTime*1000;// convert from seconds into miliseconds
		double t0 = System.currentTimeMillis();
		//while (it < maxIter && bestV > 0) {
		while(System.currentTimeMillis() - t0 < maxTime && bestV > 0){
			int minDelta = 1000000;
			int sel_i = -1;
			int sel_sl = -1;

			//System.out.println("NewSearch::assignContiuousSlot, nbStudents = " + nbStudents + " minSlot = " + minSlot + " maxSlot = " + maxSlot);
			for (int i = 0; i < nbStudents; i++) {
				for (int sl = minSlot; sl <= maxSlot; sl++) {
					if (csNotEqual.getAssignDelta(var_slots[i], sl) == 0) {
						int d = csContinuous.getAssignDelta(var_slots[i], sl);
						//System.out.println("NewSearch::assignContiuousSlot, consider jury " + i + " and slot " + sl + " d = " + d + 
							//	" tabu[" + i + "][" + sl + "] = " + tabu[i][sl] + " it = " + it + " violations = " + 
								//csContinuous.violations() + " bestV " + bestV);
						if (csContinuous.violations() + d < bestV || tabu[i][sl] < it) {
							//System.out.println("NewSearch::assignContiuousSlot, accept move " + " minDelta = " + minDelta);
							if (d < minDelta) {
								minDelta = d;
								sel_i = i;
								sel_sl = sl;
								//System.out.println("NewSearch::assignContiuousSlot, update sel_i = " + sel_i + " sel_sl = " + sel_sl + " minDelta = " + minDelta);
							}
						}
					}

				}
			}
			if(sel_i == -1 || sel_sl == -1){// no move because of tabu list
				break;
			}
			int d = csNotEqual.getAssignDelta(var_slots[sel_i], sel_sl);
			System.out.println("AtMost::getAssignDelta(x[" + sel_i + "],"
					+ sel_sl + ") = " + d);
			System.out.println("sel_i = " + sel_i + " sel_sl = " + sel_sl);
			csContinuous.propagate(var_slots[sel_i], sel_sl);
			csNotEqual.propagate(var_slots[sel_i], sel_sl);
			var_slots[sel_i].setValue(sel_sl);

			tabu[sel_i][sel_sl] = it + tbl;

			if (oldV + minDelta != csContinuous.violations()) {
				System.out.println("Error, oldV = " + oldV + " delta = "
						+ minDelta + " violations = "
						+ csContinuous.violations());
				return;
			}

			if (!csNotEqual.verify()) {
				System.out.println("CS not verified");
				return;
			}
			if (!csContinuous.verify()) {
				System.out.println("NOT verify");
				return;
			}
			oldV = csContinuous.violations();
			if (csContinuous.violations() < bestV) {
				bestV = csContinuous.violations();
			}
			System.out.println("Step " + it + " : Assign var_slots[" + sel_i
					+ "] to " + sel_sl + " violations = "
					+ csContinuous.violations() + " best = " + bestV);
			if (it == 999) {
				continuous.printResult();
			}
			it++;
		}
		AtMost am = (AtMost) csNotEqual.getConstraint(csNotEqual.size() - 1);
		am.print();
	}

	private HashMap<Integer,Integer> normalizeArray(HashSet<Integer> S){		
		HashMap<Integer,Integer> m = new HashMap<Integer,Integer>();
		// S = {3,9,2,7} --> m[2] = 1, m[3] = 2, m[7] = 3, m[9] = 4
		int n = S.size();
		int[] a = new int[n];
		Iterator is = S.iterator();
		int idx = -1;
		while(is.hasNext()){
			int x = (Integer)is.next();
			idx++;
			a[idx] = x;
		}
		for(int i = 0; i < n-1; i++)
			for(int j = i+1; j < n; j++)
				if(a[i] > a[j]){
					int t = a[i]; a[i] = a[j]; a[j] = t;
				}
		for(int i = 0; i < n; i++){
			m.put(a[i], i+1);
		}
		return m;
	}
	private void postProcessing(List<JuryInfo> listJury){
		HashSet<Integer> slots_used = new HashSet<Integer>();
		HashSet<Integer> rooms_used = new HashSet<Integer>();
		
		for(int i = 0; i < listJury.size(); i++){
			JuryInfo jr = listJury.get(i);
			slots_used.add(jr.getSlotId());
			rooms_used.add(jr.getRoomId());
		}
		//int sz_Slots = slots_used.size();
		//int sz_Rooms = rooms_used.size();
		HashMap<Integer,Integer> n_slots = normalizeArray(slots_used);
		HashMap<Integer,Integer> n_rooms = normalizeArray(rooms_used);
		for(int i = 0; i < listJury.size(); i++){
			JuryInfo jr = listJury.get(i);
			int new_slot = n_slots.get(jr.getSlotId());
			int new_room = n_rooms.get(jr.getRoomId());
			jr.setSlotId(new_slot);
			jr.setRoomId(new_room);
		}
		
	}
	public void localsearch(int maxTime) {
		ConstraintSystem CS = new ConstraintSystem();
		assignTimeSlot(CS, maxTime/2);
		//System.out.println("assign continuous");
		assignContinuousSlot(CS, maxTime/2);
		for (int i = 0; i < listJuryInfos.size(); i++) {
			JuryInfo info = listJuryInfos.get(i);
			int slotId = var_slots[i].getValue();
			info.setSlotId(slotId);
		}

		AssignRooms ar = new AssignRooms();
		ar.assignRooms(listJuryInfos, nbRooms);
		
		postProcessing(listJuryInfos);
	}

	public String search(int maxIter) {
		ConstraintSystem CS = new ConstraintSystem();
		assignTimeSlot(CS, maxIter);
		System.out.println("assign continuous");
		assignContinuousSlot(CS, maxIter);
		int sz = listJuryInfos.size();
		for (int i = 0; i < sz; i++) {
			JuryInfo info = listJuryInfos.get(i);
			int slotId = var_slots[i].getValue();
			info.setSlotId(slotId);
		}

		AssignRooms ar = new AssignRooms();
		ar.assignRooms(listJuryInfos, nbRooms);
		
		String xml = Utility.checkConsistency(listJuryInfos);
		return xml;
	}
	
	public Vector<Integer> computeCandidateSlots(int juryID){
		Vector<Integer> cand = new Vector<Integer>();
		for(int sl = minSlot; sl <= maxSlot; sl++){
			
		}
		return cand;
	}
}
