package algorithms.backtracksearch;

import DataEntity.JuryInfo;

public class BackTrackSearchTeachersSlotsRoomsObjectiveMatchNotMove extends
		BackTrackSearchTeachersSlotsRoomsObjectiveMatch {

	/**
	 * @param args
	 */
	protected boolean checkFeasible(int r, int c, int v, int k, int dept) {
		// TODO Auto-generated method stub
		boolean ok = super.checkFeasible(r, c, v, k, dept);
		if(!ok) return false;
		
		JuryInfo J = jury.get(r);
		int var_idx = 7*r+c;
		
		if(c == 6){
			for(int rid = 1; rid <= rooms.size(); rid++) if(rid != v){
				int[] p = J.getJuryMemberIDs();
				for(int j = 0; j < p.length; j++){
					
					if(p[j] > 0){
						//System.out.println("PartitionJuriesToRoomsTeacherNotMove::checkFeasible(" + r + "," + c + 
									//"," + v + ", occTeacherRoom[" + rid + "].get(" + p[j] + ") = " + occTeacherRoom[rid].get(p[j]));
						if(occTeacherRoom[rid].get(p[j]) > 0) return false;
					}
				}
				
			}
			
			// check if the number of hust, nonHust assigned to room v exceed the upperbound
			
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
			if(!ok && k >= dept) System.out.println("BackTrackSearchTeachersSlotsRoomsObjectiveMatchNotMove::TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + ", v = " + v + 
					", FALSE hustOfRoom[" + v + "].sz = " + hustOfRoom[v].size() + ", nbHust = " + nbHust);
			if(nonHustOfRoom[v].size() + nbNonHust > maxNonHustOfRoom.get(v)) ok = false;
			if(!ok && k >= dept) System.out.println("BackTrackSearchTeachersSlotsRoomsObjectiveMatchNotMove::TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + ", v = " + v + 
					", FALSE nonHustOfRoom[" + v + "].sz = " + nonHustOfRoom[v].size() + ", nbNonHust = " + nbNonHust);
		}else if(c == 0 || c == 4){
			if(J.getRoomId() > 0){
				int rid = J.getRoomId();
				for(int rj = 1; rj <= rooms.size(); rj++) if(rj != rid){
					if(nonHustOfRoom[rj].contains(v)) ok = false;
					if(!ok && k >= dept) System.out.println("BackTrackSearchTeachersSlotsRoomsObjectiveMatchNotMove::TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + ", v = " + v + 
							", FALSE nonHustOfRoom[" + rj + "] contains nonHust " + v);
				}
				
				if(occSupervisorRoom[rid].get(v) > 0 && maxNonHustOfRoom.get(rid) <= 2) ok = false;
				if(!ok && k >= dept) System.out.println("BackTrackSearchTeachersSlotsRoomsObjectiveMatchNotMove::TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + ", v = " + v + 
						", FALSE occurrence of nonHust " + v + " in room " + rid + " = occSupervisorRoom[" + rid + "].get(" + v + ") = " + occSupervisorRoom[rid].get(v) + " > 0");
				
				if(nonHustOfRoom[J.getRoomId()].size() >= maxNonHustOfRoom.get(J.getRoomId()) && !nonHustOfRoom[J.getRoomId()].contains(v) ) ok = false;
				if(!ok && k >= dept) System.out.println("BackTrackSearchTeachersSlotsRoomsObjectiveMatchNotMove::TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + ", v = " + v + 
						", FALSE, nonHustOfRoom[" + J.getRoomId() + "].sz = " + nonHustOfRoom[J.getRoomId()].size() + " >= maxNonHustOfRoom.get(" + J.getRoomId() +
						") = " + maxNonHustOfRoom.get(J.getRoomId()) + " AND nonHustOfRoom[" + J.getRoomId() + "] does not contains " + v);
				
			}
		}else if(c == 1 || c == 2 || c == 3){
			if(J.getRoomId() > 0){
				int rid = J.getRoomId();
				for(int rj = 1; rj <= rooms.size(); rj++) if(rj != rid){
					if(hustOfRoom[rj].contains(v)) ok = false;
					if(!ok && k >= dept) System.out.println("BackTrackSearchTeachersSlotsRoomsObjectiveMatchNotMove::TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + ", v = " + v + 
							", FALSE hustOfRoom[" + rj + "] contains hust " + v);
				}
				
				if(occSupervisorRoom[rid].get(v) > 0 && maxHustOfRoom.get(rid) <= 3) ok = false;
				if(!ok && k >= dept) System.out.println("BackTrackSearchTeachersSlotsRoomsObjectiveMatchNotMove::TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + ", v = " + v + 
						", FALSE occurrence of hust " + v + " in room " + rid + " =  occSupervisorRoom[" + rid + "].get(" + v + ") = " + occSupervisorRoom[rid].get(v) + " > 0");
				
				if(hustOfRoom[J.getRoomId()].size() >= maxHustOfRoom.get(J.getRoomId()) && !hustOfRoom[J.getRoomId()].contains(v) ) ok = false;
				if(!ok && k >= dept) System.out.println("BackTrackSearchTeachersSlotsRoomsObjectiveMatchNotMove::TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + ", v = " + v + 
						", FALSE, hustOfRoom[" + J.getRoomId() + "].sz = " + hustOfRoom[J.getRoomId()].size() + " >= maxHustOfRoom.get(" + J.getRoomId() +
						") = " + maxHustOfRoom.get(J.getRoomId()) + " AND hustOfRoom[" + J.getRoomId() + "] does not contains " + v);
			}
		}
		
		
		return ok;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
