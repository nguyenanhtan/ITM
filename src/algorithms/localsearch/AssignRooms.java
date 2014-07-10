package algorithms.localsearch;

import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;

import DataEntity.JuryInfo;

class RoomSlot {
	private int _r;
	private int _w;

	public RoomSlot(int r, int w) {
		_r = r;
		_w = w;
	}

	public int getRoom() {
		return _r;
	}

	public int getWeight() {
		return _w;
	}
	
	public String toString(){
		return "RoomSlot";
	}
}

public class AssignRooms {

	/**
	 * @param args
	 */

	private int computeCommon(JuryInfo j1, JuryInfo j2) {
		int c = 0;
		if (j2.juryProfessor(j1.getPresidentId()))
			c++;
		if (j2.juryProfessor(j1.getExaminerId1()))
			c++;
		if (j2.juryProfessor(j1.getExaminerId2()))
			c++;
		if (j2.juryProfessor(j1.getSecretaryId()))
			c++;
		if (j2.juryProfessor(j1.getAdditionalMemberId()))
			c++;

		return c;
	}

	private RoomSlot findRoom(HashSet<Integer> R, Map<Integer, Set<Integer>> S,
			JuryInfo s, JuryInfo[] listJuryInfos) {
		// 	input: 	set of rooms R
		//			S[ri] is the set of juries assigned to room ri, forall ri \in R
		//  		a jury s
		//			list of juries listJuryInfos
		//	output: a room sel_r containing a jury J having maximum professors in common with s 
		
		int sel_r = -1;
		int maxCommon = -1;
		Iterator it = R.iterator();
		while (it.hasNext()) {
			Integer r = (Integer) it.next();
			int maxCommon_r = 0;
			Set<Integer> rs = S.get(r);// set of session assigned to room r
			Iterator irs = rs.iterator();
			while (irs.hasNext()) {
				Integer j = (Integer) irs.next();
				int c = computeCommon(s, listJuryInfos[j]);// number of professors in common between jury s and jury j
				if (c > maxCommon_r)
					maxCommon_r = c;
			}
			// maxCommon_r is the maximal number of members in common between
			// jury s and room r
			if (maxCommon_r > maxCommon) {
				maxCommon = maxCommon_r;
				sel_r = r;
			}
		}
		return new RoomSlot(sel_r, maxCommon);
	}

	public void assignRooms(List<JuryInfo> listJuryInfos, int nbRooms) {
		int sz = listJuryInfos.size();
		JuryInfo[] ji = new JuryInfo[sz];
		for (int i = 0; i < sz; i++) {
			ji[i] = listJuryInfos.get(i);
		}
		// sort  list of juries in increasing order of slot assigned to it
		for (int i = 0; i < sz - 1; i++) {
			for (int j = i + 1; j < sz; j++) {
				if (ji[i].getSlotId() > ji[j].getSlotId()) {
					JuryInfo tmp = ji[i];
					ji[i] = ji[j];
					ji[j] = tmp;
				}
			}
		}

		int minSl = 100000;
		int maxSl = -100000;
		int maxOccSlot = -1;
		int idMaxOccSlot = -1;
		for (int i = 0; i < sz; i++) {
			int sl = ji[i].getSlotId();
			if (minSl > sl)
				minSl = sl;
			if (maxSl < sl)
				maxSl = sl;
		}
		int[] occSl = new int[maxSl + 1];
		for (int i = 0; i < sz; i++) {
			int sl = ji[i].getSlotId();
			occSl[sl]++;
			if (occSl[sl] > maxOccSlot) {
				maxOccSlot = occSl[sl];
				idMaxOccSlot = sl;
			}
		}

		int[] var_rooms = new int[sz];
		int[][] room_session = new int[nbRooms][maxSl];// room_session[r][i] =
														// the session located
														// at i_th of room r
		int szR[] = new int[nbRooms];// szR[r] is the number of sessions
										// assigned to room r
		Map<Integer, Set<Integer>> S = new HashMap<Integer, Set<Integer>>();

		//for (int i = 0; i < nbRooms; i++) {
		for (int i = 1; i <= nbRooms; i++) {
			szR[i-1] = 0;
			S.put(i, new HashSet<Integer>());
		}
		int session_id = 0;
		int sl_id = minSl;// each iteration, assign rooms to all juries having slot = sl_id (sl_id = minSl,minSl+1,...,maxSl)
		while (session_id < sz) {
			HashSet<Integer> R = new HashSet<Integer>();
			//for (int r = 0; r < nbRooms; r++) {
			for (int r = 1; r <= nbRooms; r++) {
				R.add(r);
			}
			// System.out.println("AssignRooms::assigmRoom, consider sl_id = " +
			// sl_id + ", session_id = " + session_id + " info : " +
			// ji[session_id].info());
			//for (int r = 0; r < nbRooms; r++) {
			for (int r = 1; r <= nbRooms; r++) {
				//System.out.println("Room " + r + "  has: ");
				HashSet<Integer> rs = (HashSet<Integer>) S.get(r);
				Iterator ir = rs.iterator();
				while (ir.hasNext()) {
					Integer j = (Integer) ir.next();
					//System.out.println(ji[j].info());
				}
			}
			HashSet<Integer> Sid = new HashSet<Integer>();
			// Sid is the set of juries (rooms not assigned) assigned to slot sl_id
			while (ji[session_id].getSlotId() == sl_id) {
				Sid.add(session_id);
				session_id++;
				if (session_id >= sz)
					break;
			}

			while (Sid.size() > 0) {
				// for each jury of Sid and the set of room R, find the jury-room pair (sel_jury,sel_r) such that
				// sel_jury has most professors in common with some jury in sel_r
				if(R.size() == 0){// if the set of rooms is empty but there exist other juries not yet assigned
									// then re-initialize R. 
									// This is the case when slot-assigned solution is violating:
									// number of juries assign to a same slot is greater than nbRooms
					
					//for(int r = 0; r < nbRooms; r++) R.add(r);
					for(int r = 1; r <= nbRooms; r++) R.add(r);
				}
				Iterator is = Sid.iterator();
				int sel_r = -1;
				int maxCommon = -100;
				int sel_jury = -1;
				while (is.hasNext()) {
					Integer s = (Integer) is.next();
					RoomSlot rs = findRoom(R, S, ji[s], ji);
					if (maxCommon < rs.getWeight()) {
						sel_r = rs.getRoom();
						maxCommon = rs.getWeight();
						sel_jury = s;
					}
					
					//System.out.println("R.size = " + R.size() + ", Sid.size = "
						//	+ Sid.size() + " try s = " + s + " maxCommon = "
							//+ maxCommon + " rs=(" + rs.getRoom() + ","
							//+ rs.getWeight());
				}
				// System.out.println(ji[sel_session].info() +
				// " --> FindRoom = " + sel_r);
				R.remove(sel_r);
				if (sel_jury > -1 && sel_r > -1) {
					var_rooms[sel_jury] = sel_r;
					HashSet<Integer> R_id = (HashSet<Integer>) S.get(sel_r);
					R_id.add(sel_jury);
					Sid.remove(sel_jury);
				}
			}

			sl_id++;
		}

		listJuryInfos.clear();
		for (int i = 0; i < sz; i++) {
			listJuryInfos.add(ji[i]);
		}

		for (int i = 0; i < sz; i++) {
			//System.out.println("room[" + i + "] = " + var_rooms[i]);
			ji[i].setRoomId(var_rooms[i]);
		}
		//for (int r = 0; r < nbRooms; r++) {
		for (int r = 1; r <= nbRooms; r++) {	
			System.out.println("Jury in room " + r + " are:");
			for (int s = 0; s < sz; s++) {
				if (var_rooms[s] == r) {
					//System.out.println(ji[s].getSlotId() + " : "
					//		+ ji[s].getPresidentName() + "\t"
					//		+ ji[s].getExaminerName1() + "\t"
						//	+ ji[s].getExaminerName2() + "\t"
							//+ ji[s].getSecretaryName() + "\t"
							//+ ji[s].getAdditionalMemberName());
				}
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
